package com.example.campusrent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class RequestAdapter extends FirestoreRecyclerAdapter<Request, RequestAdapter.RequestViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RequestAdapter(@NonNull FirestoreRecyclerOptions<Request> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull Request model) {
        holder.textViewStatusRequest.setText(model.getStatus());

        db.collection("items").document(model.getItemId()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        holder.textViewItemNameRequest.setText(documentSnapshot.getString("name"));
                    }
                });

        db.collection("users").document(model.getOwnerId()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        holder.textViewOwnerNameRequest.setText("Owner: " + documentSnapshot.getString("name"));
                    }
                });
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_layout, parent, false);
        return new RequestViewHolder(view);
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemNameRequest;
        TextView textViewOwnerNameRequest;
        TextView textViewStatusRequest;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemNameRequest = itemView.findViewById(R.id.textViewItemNameRequest);
            textViewOwnerNameRequest = itemView.findViewById(R.id.textViewOwnerNameRequest);
            textViewStatusRequest = itemView.findViewById(R.id.textViewStatusRequest);
        }
    }
}