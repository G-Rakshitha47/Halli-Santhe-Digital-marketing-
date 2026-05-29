package com.halliasanthe.digital.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.halliasanthe.digital.R;
import com.halliasanthe.digital.activities.ProductDetailActivity;
import com.halliasanthe.digital.models.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> products = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        this.filteredProducts = new ArrayList<>(products);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredProducts.clear();
        if (query.isEmpty()) {
            filteredProducts.addAll(products);
        } else {
            String lower = query.toLowerCase().trim();
            for (Product p : products) {
                if (p.getName().toLowerCase().contains(lower) ||
                        p.getCategory().toLowerCase().contains(lower) ||
                        (p.getDescription() != null && p.getDescription().toLowerCase().contains(lower))) {
                    filteredProducts.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = filteredProducts.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() { return filteredProducts.size(); }

    public boolean isEmpty() { return filteredProducts.isEmpty(); }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productCategory;
        CardView cardView;
        View stockBadge;

        ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCategory = itemView.findViewById(R.id.productCategory);
            cardView = itemView.findViewById(R.id.cardView);
            stockBadge = itemView.findViewById(R.id.stockBadge);
        }

        void bind(Product product) {
            productName.setText(product.getName());
            productPrice.setText(itemView.getContext().getString(R.string.price_format, String.format(Locale.getDefault(), "%.0f", product.getPrice())));
            productCategory.setText(product.getCategory());
            stockBadge.setVisibility(product.isInStock() ? View.VISIBLE : View.GONE);

            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.placeholder_product)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(productImage);
            } else {
                productImage.setImageResource(R.drawable.placeholder_product);
            }

            cardView.setOnClickListener(v -> {
                Context context = itemView.getContext();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product_id", product.getId());
                intent.putExtra("product_name", product.getName());
                intent.putExtra("product_price", product.getPrice());
                intent.putExtra("product_image", product.getImageUrl());
                intent.putExtra("product_category", product.getCategory());
                intent.putExtra("product_description", product.getDescription());
                intent.putExtra("seller_name", product.getSellerName());
                intent.putExtra("seller_phone", product.getSellerPhone());
                intent.putExtra("in_stock", product.isInStock());
                context.startActivity(intent);
            });
        }
    }
}
