package com.example.mobil.controller.fxml;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mobil.R;
import com.example.mobil.controller.AsyncLoader;
import com.example.mobil.controller.ShoppingItemAdapter;
import com.example.mobil.model.ShoppingItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
public class CartActivity extends AppCompatActivity {
    private static final String LOG_TAG = CartActivity.class.getName();
    private FirebaseUser user;
    private RecyclerView mRecyclerView;
    private ArrayList<ShoppingItem> mItemList;
    private ShoppingItemAdapter mAdapter;

    private SharedPreferences preferences;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private FrameLayout redCircle;
    private TextView contentTextView;

    private int downloadCount = 10;
    private int gridNumber = 1;
    private int cartItems = 0;
    private boolean viewRow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(LOG_TAG,"Authenticated user!");
        }else{
            Log.d(LOG_TAG,"Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new ShoppingItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        //getSupportLoaderManager().restartLoader(0,null,this);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Carts");
        //getItemsFromFirestore();
        //test();
        //initializeData();

    }

    private void queryData(){
        mItemList.clear();

        //mItems.whereEqualTo();...
        mItems.whereEqualTo("user",user.getEmail()).orderBy("name").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                //ShoppingItem item = ShoppingItem.fromQueryDocumentSnapshot(document);
                ShoppingItem item = document.toObject(ShoppingItem.class);
                mItemList.add(item);
            }
//            if(mItemList.size() == 0){
//                initializeData();
//                queryData();
//            }
            mAdapter.notifyDataSetChanged();
            Log.d(LOG_TAG,"notify");
        });


    }



}
