package com.example.mobil.controller.fxml;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class ShopListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopListActivity.class.getName();
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
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
        mItems = mFirestore.collection("Items");
        Log.d(LOG_TAG,"oncreate");
        //getItemsFromFirestore();
        //test();
        //initializeData();
       queryData();


    }




    FirebaseFirestore db =  FirebaseFirestore.getInstance();


    public void test(){
        Map<String, Object> user = new HashMap<>();
        user.put("asd",1);
        user.put("asasdad","1");
        Object asd = db.collection("Items").limit(10).get();
        db.collection("Items")
                .add(user);

    }

    private void queryData(){
        mItemList.clear();

        //mItems.whereEqualTo();...
        mItems.orderBy("name").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                //ShoppingItem item = ShoppingItem.fromQueryDocumentSnapshot(document);
                ShoppingItem item = document.toObject(ShoppingItem.class);
                mItemList.add(item);
            }
            if(mItemList.size() == 0){
                initializeData();
                queryData();
            }
            mAdapter.notifyDataSetChanged();
            Log.d(LOG_TAG,"notify");
        });


    }

    private void initializeData(){
        Log.d(LOG_TAG,"inistarted");
        String[] itemsList = getResources().getStringArray(R.array.shopping_item_names);
        String[] itemsInfo = getResources().getStringArray(R.array.shopping_item_desc);
        String[] itemsPrice = getResources().getStringArray(R.array.shopping_item_price);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.shopping_item_images);
        TypedArray itemsRate = getResources().obtainTypedArray(R.array.shopping_item_rates);

        //mItemList.clear();

        for (int i = 0; i < itemsList.length; i++) {
            Log.d(LOG_TAG,"iniitems");
            mFirestore.collection("Items").add(new ShoppingItem(itemsList[i], itemsInfo[i], itemsPrice[i], itemsRate.getFloat(i,0), itemsImageResource.getResourceId(i,0)))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(LOG_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(LOG_TAG, "Error adding document", e);
                        }
                    });;
        }
        itemsImageResource.recycle();
        Log.d(LOG_TAG,"ini");
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG,s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out_button:
                Log.d(LOG_TAG,"Log out clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.setting_button:
                Log.d(LOG_TAG,"Settings clicked!");
                return true;
            case R.id.cart:
                Log.d(LOG_TAG,"Cart clicked!");
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.view_selector:
                Log.d(LOG_TAG,"View selector clicked!");
                if(viewRow){
                    changeSpanCount(item,R.drawable.ic_view_grid, 1);
                }else{
                    changeSpanCount(item,R.drawable.ic_view_row, 2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount){
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(){
        cartItems = (cartItems + 1);
        if (0 < cartItems){
            contentTextView.setText(String.valueOf(cartItems));
        } else {
            contentTextView.setText("");
        }
    }


    public void upload(View view) {
        initializeData();
    }
}