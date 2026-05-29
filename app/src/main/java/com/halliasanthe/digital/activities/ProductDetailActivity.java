package com.halliasanthe.digital.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.halliasanthe.digital.R;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get data from intent
        String productId = getIntent().getStringExtra("product_id");
        String name = getIntent().getStringExtra("product_name");
        double price = getIntent().getDoubleExtra("product_price", 0);
        String imageUrl = getIntent().getStringExtra("product_image");
        String category = getIntent().getStringExtra("product_category");
        String description = getIntent().getStringExtra("product_description");
        String sellerName = getIntent().getStringExtra("seller_name");
        String sellerPhone = getIntent().getStringExtra("seller_phone");
        boolean inStock = getIntent().getBooleanExtra("in_stock", true);

        // Bind views
        ImageView productImage = findViewById(R.id.detailProductImage);
        TextView productName = findViewById(R.id.detailProductName);
        TextView productPrice = findViewById(R.id.detailProductPrice);
        TextView productDescription = findViewById(R.id.detailDescription);
        TextView sellerNameTv = findViewById(R.id.detailSellerName);
        Chip categoryChip = findViewById(R.id.detailCategoryChip);
        Chip stockChip = findViewById(R.id.detailStockChip);
        MaterialButton btnContact = findViewById(R.id.btnContactSeller);
        MaterialButton btnWhatsapp = findViewById(R.id.btnWhatsapp);
        ImageView btnBack = findViewById(R.id.btnBack);

        productName.setText(name);
        productPrice.setText(getString(R.string.price_format, String.format(Locale.getDefault(), "%.0f", price)));
        categoryChip.setText(category);
        sellerNameTv.setText("by " + (sellerName != null ? sellerName : "Artisan"));

        if (description != null && !description.isEmpty()) {
            productDescription.setText(description);
        } else {
            productDescription.setText("Handcrafted with love by a local artisan. Authentic and unique piece.");
        }

        if (inStock) {
            stockChip.setText("✓ In Stock");
            stockChip.setChipBackgroundColorResource(R.color.green_light);
        } else {
            stockChip.setText("Out of Stock");
            stockChip.setChipBackgroundColorResource(R.color.red_light);
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).centerCrop().into(productImage);
        } else {
            productImage.setImageResource(R.drawable.placeholder_product);
        }

        btnBack.setOnClickListener(v -> finish());

        btnContact.setOnClickListener(v -> {
            if (sellerPhone != null && !sellerPhone.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sellerPhone));
                startActivity(callIntent);
            } else {
                Toast.makeText(this, R.string.error_no_contact, Toast.LENGTH_SHORT).show();
            }
        });

        btnWhatsapp.setOnClickListener(v -> {
            if (sellerPhone != null && !sellerPhone.isEmpty()) {
                String message = "Hi! I'm interested in your product: " + name + " (" + getString(R.string.price_format, String.format(Locale.getDefault(), "%.0f", price)) + ") listed on Halli-Santhe Digital. Is it available?";
                String url = "https://wa.me/91" + sellerPhone.replaceAll("[^0-9]", "") + "?text=" + Uri.encode(message);
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                try {
                    startActivity(whatsappIntent);
                } catch (Exception e) {
                    Toast.makeText(this, R.string.error_no_whatsapp, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.error_no_contact, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
