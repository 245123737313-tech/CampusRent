package com.example.campusrent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ItemDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "com.example.campusrent.EXTRA_ITEM_ID";

    private ImageView imageViewItemDetails;
    private TextView textViewItemNameDetails;
    private TextView textViewItemPriceDetails;
    private TextView textViewItemDescriptionDetails;
    private TextView textViewOwnerName;
    private Button buttonRequestRent;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String itemId;
    private String ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        imageViewItemDetails = findViewById(R.id.imageViewItemDetails);
        textViewItemNameDetails = findViewById(R.id.textViewItemNameDetails);
        textViewItemPriceDetails = findViewById(R.id.textViewItemPriceDetails);
        textViewItemDescriptionDetails = findViewById(R.id.textViewItemDescriptionDetails);
        textViewOwnerName = findViewById(R.id.textViewOwnerName);
        buttonRequestRent = findViewById(R.id.buttonRequestRent);

        itemId = getIntent().getStringExtra(EXTRA_ITEM_ID);

        loadItemDetails();

        buttonRequestRent.setOnClickListener(v -> createRentalRequest());
    }

    private void loadItemDetails() {
        DocumentReference itemRef = db.collection("items").document(itemId);
        itemRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Item item = documentSnapshot.toObject(Item.class);
                if (item != null) {
                    ownerId = item.getUserId();
                    textViewItemNameDetails.setText(item.getName());
                    textViewItemPriceDetails.setText(String.format("$%.2f/day", item.getPrice()));
                    textViewItemDescriptionDetails.setText(item.getDescription());
                    Picasso.get().load(item.getImageUrl()).into(imageViewItemDetails);

                    loadOwnerDetails();
                }
            }
        });
    }

    private void loadOwnerDetails() {
        if (ownerId != null) {
            DocumentReference userRef = db.collection("users").document(ownerId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String ownerName = documentSnapshot.getString("name");
                    textViewOwnerName.setText("Owner: " + ownerName);
                }
            });
        }
    }

    private void createRentalRequest() {
        String requesterId = mAuth.getCurrentUser().getUid();

        Map<String, Object> request = new HashMap<>();
        request.put("itemId", itemId);
        request.put("ownerId", ownerId);
        request.put("requesterId", requesterId);
        request.put("status", "Pending");

        db.collection("requests").add(request)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ItemDetailsActivity.this, "Request sent.", Toast.LENGTH_SHORT).show();
                    buttonRequestRent.setEnabled(false);
                    buttonRequestRent.setText("Request Sent");
                })
                .addOnFailureListener(e -> Toast.makeText(ItemDetailsActivity.this, "Failed to send request.", Toast.LENGTH_SHORT).show());
    }
}