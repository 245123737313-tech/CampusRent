package com.example.campusrent;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewItem;
    private Button buttonChooseImage;
    private EditText editTextItemName;
    private EditText editTextItemDescription;
    private EditText editTextItemPrice;
    private Button buttonAddItem;

    private Uri imageUri;

    private StorageReference storageRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        storageRef = FirebaseStorage.getInstance().getReference("item_images");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        imageViewItem = findViewById(R.id.imageViewItem);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemDescription = findViewById(R.id.editTextItemDescription);
        editTextItemPrice = findViewById(R.id.editTextItemPrice);
        buttonAddItem = findViewById(R.id.buttonAddItem);

        buttonChooseImage.setOnClickListener(v -> openFileChooser());
        buttonAddItem.setOnClickListener(v -> uploadFile());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewItem.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String name = editTextItemName.getText().toString().trim();
                        String description = editTextItemDescription.getText().toString().trim();
                        double price = Double.parseDouble(editTextItemPrice.getText().toString().trim());
                        String imageUrl = uri.toString();
                        String userId = mAuth.getCurrentUser().getUid();

                        Item item = new Item(name, description, price, imageUrl, userId);

                        db.collection("items").add(item)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(AddItemActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(AddItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                    }))
                    .addOnFailureListener(e -> Toast.makeText(AddItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}