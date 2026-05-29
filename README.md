# 🛒 Halli-Santhe Digital — Android App

A **Hyper-Local Marketplace** Android app built for the MindMatrix VTU Internship Program.
Connects local artisans with urban buyers. "Vocal for Local" 🇮🇳

---

## 📱 Features

- **Buyer Browse** — Grid view of all products with category filters
- **Artisan Upload** — Add product name, price, photo, description
- **Search** — Real-time search by product name or category
- **Product Detail** — Full detail view with Call & WhatsApp CTA
- **Firebase Firestore** — Real-time product sync
- **Firebase Storage** — Image upload with progress bar
- **Shimmer Loading** — Beautiful skeleton loading UI
- **Empty State** — Friendly message when no products exist
- **Swipe to Refresh** — Pull down to refresh listings

---

## 🔥 Firebase Setup (REQUIRED — do this first!)

### Step 1: Create Firebase Project
1. Go to [https://console.firebase.google.com](https://console.firebase.google.com)
2. Click **"Add Project"** → Name it `HalliSanthe` → Continue
3. Disable Google Analytics (optional) → **Create Project**

### Step 2: Add Android App to Firebase
1. In your Firebase project, click the **Android icon** (</> icon)
2. Enter package name: **`com.halliasanthe.digital`**
3. Enter App nickname: `Halli-Santhe`
4. Click **Register App**
5. **Download `google-services.json`**
6. **Replace** the placeholder `app/google-services.json` with this downloaded file

### Step 3: Enable Firestore Database
1. In Firebase Console → **Firestore Database** → **Create Database**
2. Choose **Start in test mode** (for development)
3. Select a region (e.g., `asia-south1` for India) → **Enable**

### Step 4: Enable Firebase Storage
1. In Firebase Console → **Storage** → **Get Started**
2. Choose **Start in test mode** → **Done**

### Step 5: Enable Anonymous Auth
1. In Firebase Console → **Authentication** → **Get Started**
2. **Sign-in Method** tab → **Anonymous** → Enable → **Save**

---

## 🚀 Running in Android Studio

1. Open Android Studio
2. **File → Open** → Select the `HalliSanthe` folder
3. Wait for Gradle sync to finish
4. Replace `app/google-services.json` with your real Firebase file
5. Connect Android device or start emulator (API 24+)
6. Click **Run ▶**

---

## 📁 Project Structure

```
app/src/main/
├── java/com/halliasanthe/digital/
│   ├── activities/
│   │   ├── SplashActivity.java       ← Launch screen with animation
│   │   ├── MainActivity.java         ← Browse products (grid + search + filter)
│   │   ├── ProductDetailActivity.java ← Product detail + Call/WhatsApp CTA
│   │   └── AddProductActivity.java   ← Artisan upload form
│   ├── adapters/
│   │   └── ProductAdapter.java       ← RecyclerView GridLayoutManager adapter
│   ├── models/
│   │   └── Product.java              ← Firestore data model
│   └── utils/
│       └── FirebaseHelper.java       ← Firebase singleton helper
└── res/
    ├── layout/                       ← All XML layouts
    ├── drawable/                     ← Icons, shapes, gradients
    ├── values/                       ← Colors, strings, themes, dimens
    ├── anim/                         ← Splash animations
    └── xml/                          ← FileProvider paths
```

---

## 🎨 UI Design Highlights

- **Saffron & Turmeric** color palette — authentic Indian market feel
- **Serif fonts** for headings — traditional & elegant
- **Card-based grid** with rounded corners & soft shadows
- **Shimmer loading** effect while fetching from Firebase
- **Chip-based category filter** horizontally scrollable
- **Extended FAB** that shrinks on scroll
- **Hero image** with gradient overlay on detail screen

---

## 📦 Key Libraries Used

| Library | Purpose |
|---|---|
| Firebase Firestore | Real-time product database |
| Firebase Storage | Image upload & hosting |
| Firebase Auth | Anonymous user sessions |
| Glide | Fast image loading & caching |
| Shimmer (Facebook) | Loading skeleton animation |
| Material Components | Chips, FAB, TextInputLayout |
| SwipeRefreshLayout | Pull-to-refresh |

---

## ✅ Success Criteria Met

- [x] Clicking a product opens a detailed view with "Call to Action" (Call + WhatsApp)
- [x] Search functionality works for product names and categories
- [x] App handles "Empty States" gracefully with friendly illustration
- [x] UI is colorful and vibrant, reflecting a traditional Indian market
- [x] RecyclerView with GridLayoutManager (2 columns)
- [x] Firebase Firestore for data storage
- [x] Firebase Storage for image upload
- [x] Image compression-ready via Glide

---

## 🐛 Troubleshooting

**Gradle sync fails?**
→ Check internet connection. Make sure `google-services.json` is in the `app/` folder.

**"google-services.json not found"?**
→ Download from Firebase Console and place at `HalliSanthe/app/google-services.json`

**Images not uploading?**
→ Make sure Firebase Storage rules are in test mode and Storage is enabled.

**App crashes on launch?**
→ Enable Anonymous Authentication in Firebase Console → Authentication.

---

*Built with ❤️ for MindMatrix VTU Internship Program — Project #85*
