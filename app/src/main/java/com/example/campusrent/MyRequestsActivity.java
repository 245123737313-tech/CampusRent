package com.example.campusrent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMyRequests;

    private FirebaseFirestore db;
    private RequestAdapter adapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerViewMyRequests = findViewById(R.id.recyclerViewMyRequests);
        recyclerViewMyRequests.setLayoutManager(new LinearLayoutManager(this));

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        String userId = mAuth.getCurrentUser().getUid();
        Query query = db.collection("requests").whereEqualTo("requesterId", userId);

        FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        adapter = new RequestAdapter(options);
        recyclerViewMyRequests.setAdapter(adapter);
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
}