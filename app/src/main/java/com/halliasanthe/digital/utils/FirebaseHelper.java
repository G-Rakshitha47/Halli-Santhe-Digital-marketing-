package com.halliasanthe.digital.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.halliasanthe.digital.models.Product;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private final FirebaseAuth auth;

    public static final String PRODUCTS_COLLECTION = "products";

    private FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) instance = new FirebaseHelper();
        return instance;
    }

    public CollectionReference getProductsRef() {
        return db.collection(PRODUCTS_COLLECTION);
    }

    public Query getAllProducts() {
        return db.collection(PRODUCTS_COLLECTION).orderBy("createdAt", Query.Direction.DESCENDING);
    }

    public Query getProductsByCategory(String category) {
        return db.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("category", category)
                .orderBy("createdAt", Query.Direction.DESCENDING);
    }

    public StorageReference getImageStorageRef(String fileName) {
        return storage.getReference().child("product_images/" + fileName);
    }

    public Task<Void> addProduct(Product product) {
        return db.collection(PRODUCTS_COLLECTION).document().set(product);
    }

    public Task<Void> deleteProduct(String productId) {
        return db.collection(PRODUCTS_COLLECTION).document(productId).delete();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public Task<Void> signInAnonymously() {
        return auth.signInAnonymously().continueWith(task -> null);
    }
}
