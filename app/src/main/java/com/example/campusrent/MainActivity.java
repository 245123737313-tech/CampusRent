package com.example.campusrent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private RecyclerView recyclerViewItems;

    private FirebaseFirestore db;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);

        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        fabAddItem.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddItemActivity.class)));

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = db.collection("items").orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        adapter = new ItemAdapter(options);
        recyclerViewItems.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        String id = documentSnapshot.getId();
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra(ItemDetailsActivity.EXTRA_ITEM_ID, id);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.my_listings) {
            startActivity(new Intent(this, MyListingsActivity.class));
            return true;
        } else if (itemId == R.id.my_requests) {
            startActivity(new Intent(this, MyRequestsActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}