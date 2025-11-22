# CampusRent - Campus Rental Android Application

CampusRent is an Android application designed to allow users to list items for rent and view items listed by others within a campus environment. The app is built using **Kotlin** and **Jetpack Compose**, employing a **Client-Server architecture** with a **PHP/MySQL backend**.

## Project Architecture

### 1. Backend Layer (Server)
The backend manages data (Users and Items) and handles image uploads. It is designed to run on a local server (e.g., XAMPP).

*   **Database (`database.sql`)**:
    *   **`users` table**: Stores user credentials (`id`, `email`, hashed `password`).
    *   **`items` table**: Stores item listings (`id`, `name`, `price`, `description`, `image_url`, `user_id`).
*   **API Endpoints (PHP)**:
    *   `db_connect.php`: Establish database connection.
    *   `signup.php` & `login.php`: Handle user registration and authentication.
    *   `add_item.php`: Receives multipart POST requests to save item details and upload images to the `uploads/` directory. Returns an image URL accessible by the Android Emulator (`http://10.0.2.2/campusrent/...`).
    *   `get_items.php`: Returns a JSON list of all available items.

### 2. Networking Layer (Android)
*   **Retrofit**: Type-safe HTTP client used for API communication.
*   **`RetrofitClient.kt`**: Configured with base URL `http://10.0.2.2/campusrent/` for Emulator access to localhost.
*   **`ApiService.kt`**: Interface definition for API calls.
*   **Gson**: Handles JSON serialization and deserialization.

### 3. Data Layer (Repository Pattern)
*   **`Item.kt`**: Data model representing a rental item.
*   **`AuthRepository.kt`**: Manages user session and authentication calls.
*   **`ItemRepository.kt`**: Handles data operations like fetching the item list and uploading new items with images via Multipart requests.

### 4. ViewModel Layer (Business Logic)
*   **`AuthViewModel.kt`**: Manages UI state for Login/Signup screens.
*   **`HomeViewModel.kt`**: Fetches and holds the list of rental items (`loadItems()`).
*   **`AddItemViewModel.kt`**: Handles image selection logic, converting Gallery URIs to temporary Files (`uriToFile`), and interacting with the repository for uploads.

### 5. UI Layer (Jetpack Compose)
*   **`MainActivity.kt`**: Entry point.
*   **`CampusRentNavigation.kt`**: Manages navigation graph.
*   **Screens**:
    *   **Login/Signup**: User authentication forms.
    *   **Home**: Displays items in a `LazyColumn` with `AsyncImage` (Coil) for image loading. Auto-refreshes on entry.
    *   **Add Item**: Form for adding listings with image picker integration.
    *   **Item Detail**: Detailed view of a selected item.

## Setup & Installation

### Prerequisites
*   Android Studio (Hedgehog or newer recommended)
*   XAMPP (or WAMP/MAMP) for local PHP/MySQL server

### Backend Setup
1.  Install and start **XAMPP**. Start **Apache** and **MySQL**.
2.  Navigate to `htdocs` (e.g., `C:\xampp\htdocs`).
3.  Create a folder named `campusrent`.
4.  Copy all files from the project's `backend/` directory into `C:\xampp\htdocs\campusrent\`.
5.  Create an `uploads` folder inside `campusrent` (`C:\xampp\htdocs\campusrent\uploads`).
    *   **Important**: Grant full **Write Permissions** to the `uploads` folder (Right-click -> Properties -> Security -> Edit -> Add "Everyone" -> Full Control).
6.  Open `http://localhost/phpmyadmin`, create a database named `campusrent`, and import/run the SQL commands from `database.sql`.

### Android Setup
1.  Open the project in Android Studio.
2.  Sync Gradle.
3.  Ensure the Emulator is running.
4.  Run the app.
    *   **Note**: The app relies on `http://10.0.2.2` to reach the XAMPP server from the Android Emulator. If testing on a physical device, update `RetrofitClient.kt` with your PC's LAN IP.

## Features
*   User Registration & Login
*   View list of rental items with images
*   Add new rental items with image upload
*   Item details view
*   Pull-to-refresh logic (auto-refresh on home screen entry)
