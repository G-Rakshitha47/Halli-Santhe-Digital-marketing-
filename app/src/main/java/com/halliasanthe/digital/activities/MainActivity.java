package com.halliasanthe.digital.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.halliasanthe.digital.R;
import com.halliasanthe.digital.adapters.ProductAdapter;
import com.halliasanthe.digital.models.Product;
import com.halliasanthe.digital.utils.FirebaseHelper;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ShimmerFrameLayout shimmerLayout;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout emptyState;
    private EditText searchBar;
    private ChipGroup categoryChips;
    private ExtendedFloatingActionButton fabAdd;
    private ListenerRegistration listenerRegistration;
    private String selectedCategory = "All";

    private final String[] CATEGORIES = {"All", "Handicrafts", "Textiles", "Food & Spices", "Pottery", "Jewellery", "Toys", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupCategoryChips();
        setupSearch();
        setupFab();
        setupSwipeRefresh();
        loadProducts();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        shimmerLayout = findViewById(R.id.shimmerLayout);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        emptyState = findViewById(R.id.emptyState);
        searchBar = findViewById(R.id.searchBar);
        categoryChips = findViewById(R.id.categoryChips);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy > 10) fabAdd.shrink();
                else if (dy < -10) fabAdd.extend();
            }
        });
    }

    private void setupCategoryChips() {
        for (String cat : CATEGORIES) {
            Chip chip = new Chip(this);
            chip.setText(cat);
            chip.setCheckable(true);
            chip.setChecked(cat.equals("All"));
            chip.setChipBackgroundColorResource(R.color.chip_bg_selector);
            chip.setTextColor(getResources().getColorStateList(R.color.chip_text_selector, getTheme()));
            chip.setOnClickListener(v -> {
                selectedCategory = cat;
                // Uncheck others
                for (int i = 0; i < categoryChips.getChildCount(); i++) {
                    View child = categoryChips.getChildAt(i);
                    if (child instanceof Chip) {
                        Chip c = (Chip) child;
                        c.setChecked(c.getText().toString().equals(cat));
                    }
                }
                loadProducts();
            });
            categoryChips.addView(chip);
        }
    }

    private void setupSearch() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
                updateEmptyState();
            }
        });
    }

    private void setupFab() {
        fabAdd.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.primary_saffron, R.color.accent_turmeric, R.color.primary_deep);
        swipeRefresh.setOnRefreshListener(this::loadProducts);
    }

    private void loadProducts() {
        showShimmer();
        if (listenerRegistration != null) listenerRegistration.remove();

        Query query;
        if (selectedCategory.equals("All")) {
            query = FirebaseHelper.getInstance().getAllProducts();
        } else {
            query = FirebaseHelper.getInstance().getProductsByCategory(selectedCategory);
        }

        listenerRegistration = query.addSnapshotListener((snapshots, error) -> {
            hideShimmer();
            swipeRefresh.setRefreshing(false);
            if (error != null) {
                Toast.makeText(this, R.string.error_load_products, Toast.LENGTH_SHORT).show();
                return;
            }
            List<Product> products = new ArrayList<>();
            if (snapshots != null) {
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    Product p = doc.toObject(Product.class);
                    if (p != null) {
                        p.setId(doc.getId());
                        products.add(p);
                    }
                }
            }
            adapter.setProducts(products);
            searchBar.setText("");
            updateEmptyState();
        });
    }

    private void showShimmer() {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        recyclerView.setVisibility(View.GONE);
        emptyState.setVisibility(View.GONE);
    }

    private void hideShimmer() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void updateEmptyState() {
        if (adapter.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) listenerRegistration.remove();
    }
}
