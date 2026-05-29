package com.halliasanthe.digital.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.halliasanthe.digital.R;
import com.halliasanthe.digital.models.Product;
import com.halliasanthe.digital.utils.FirebaseHelper;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private ImageView productImagePreview;
    private TextInputEditText etProductName, etPrice, etDescription, etSellerName, etSellerPhone;
    private AutoCompleteTextView categoryDropdown;
    private MaterialButton btnPickImage, btnSubmit;
    private LinearProgressIndicator progressBar;
    private Uri selectedImageUri = null;

    private final String[] CATEGORIES = {"Handicrafts", "Textiles", "Food & Spices", "Pottery", "Jewellery", "Toys", "Other"};

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "Permission needed to select image", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this).load(selectedImageUri).centerCrop().into(productImagePreview);
                    productImagePreview.setVisibility(View.VISIBLE);
                    btnPickImage.setText(R.string.change_photo);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initViews();
        setupCategoryDropdown();
        setupImagePicker();
        setupSubmit();

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        productImagePreview = findViewById(R.id.productImagePreview);
        etProductName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        etSellerName = findViewById(R.id.etSellerName);
        etSellerPhone = findViewById(R.id.etSellerPhone);
        categoryDropdown = findViewById(R.id.categoryDropdown);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupCategoryDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, CATEGORIES);
        categoryDropdown.setAdapter(adapter);
        categoryDropdown.setText(CATEGORIES[0], false);
    }

    private void setupImagePicker() {
        btnPickImage.setOnClickListener(v -> {
            String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    ? Manifest.permission.READ_MEDIA_IMAGES
                    : Manifest.permission.READ_EXTERNAL_STORAGE;

            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                requestPermissionLauncher.launch(permission);
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }


    private void setupSubmit() {
        btnSubmit.setOnClickListener(v -> {
            String name = etProductName.getText() != null ? etProductName.getText().toString().trim() : "";
            String priceStr = etPrice.getText() != null ? etPrice.getText().toString().trim() : "";
            String description = etDescription.getText() != null ? etDescription.getText().toString().trim() : "";
            String sellerName = etSellerName.getText() != null ? etSellerName.getText().toString().trim() : "";
            String sellerPhone = etSellerPhone.getText() != null ? etSellerPhone.getText().toString().trim() : "";
            String category = categoryDropdown.getText().toString().trim();

            if (name.isEmpty()) { etProductName.setError(getString(R.string.error_name_required)); return; }
            if (priceStr.isEmpty()) { etPrice.setError(getString(R.string.error_price_required)); return; }
            if (sellerName.isEmpty()) { etSellerName.setError(getString(R.string.error_seller_name_required)); return; }
            if (sellerPhone.isEmpty() || sellerPhone.length() < 10) { etSellerPhone.setError(getString(R.string.error_phone_required)); return; }

            double price;
            try { price = Double.parseDouble(priceStr); } catch (NumberFormatException e) {
                etPrice.setError(getString(R.string.error_invalid_price)); return;
            }

            setLoading(true);

            String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                    ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "anonymous";

            if (selectedImageUri != null) {
                uploadImageAndSave(name, price, description, sellerName, sellerPhone, category, uid);
            } else {
                saveProduct(name, price, "", description, sellerName, sellerPhone, category, uid);
            }
        });
    }

    private void uploadImageAndSave(String name, double price, String desc, String sellerName,
                                     String sellerPhone, String category, String uid) {
        String fileName = "product_" + UUID.randomUUID().toString() + ".jpg";
        StorageReference ref = FirebaseHelper.getInstance().getImageStorageRef(fileName);

        ref.putFile(selectedImageUri)
                .addOnProgressListener(snapshot -> {
                    int progress = (int) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress(progress);
                })
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(uri ->
                                saveProduct(name, price, uri.toString(), desc, sellerName, sellerPhone, category, uid)
                        ))
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void saveProduct(String name, double price, String imageUrl, String desc,
                              String sellerName, String sellerPhone, String category, String uid) {
        Product product = new Product(name, price, imageUrl, category, desc, sellerName, sellerPhone, uid);
        FirebaseHelper.getInstance().addProduct(product)
                .addOnSuccessListener(unused -> {
                    setLoading(false);
                    Toast.makeText(this, R.string.product_success, Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSubmit.setEnabled(!loading);
        btnSubmit.setText(loading ? R.string.listing_product : R.string.list_product_btn);
    }
}
