# Arnold's Login App 🚀

A modern Android application with a PHP/MySQL backend featuring user registration, secure login, and a live monitoring dashboard.

## ✨ Features
- **Modern UI/UX**: Designed with Material Components, CardViews, and a professional indigo theme.
- **Adaptive Themes**: Full support for **Light and Dark Mode**.
- **Secure Authentication**: SHA-256 password hashing and secure API communication.
- **Live Dashboard**: A web-based dashboard for lecturers to monitor user activity in real-time.
- **Persistence**: Hybrid storage using local Room Database and remote XAMPP MySQL.

## 🛠️ Tech Stack
- **Frontend**: Java, Android SDK, Material Design.
- **Networking**: Retrofit 2, GSON.
- **Backend**: PHP (XAMPP), MySQL.
- **Database**: Room (Local), MySQL (Remote).

## 🚀 Setup Instructions

### 1. Backend Setup (XAMPP)
1. Copy the contents of the `/backend` folder to your XAMPP `htdocs` directory (e.g., `C:/xampp/htdocs/myloginapp/`).
2. Open XAMPP Control Panel and start **Apache** and **MySQL**.
3. Run the database setup script by visiting: `http://localhost/myloginapp/setup_db.php`.
4. Open the live dashboard: `http://localhost/myloginapp/view_users.php`.

### 2. Android App Setup
1. Open the project in **Android Studio**.
2. Find your computer's local IP address (run `ipconfig` in CMD).
3. Update the `BASE_URL` in `app/src/main/java/com/example/myloginapp/api/RetrofitClient.java` with your IP.
4. Ensure your phone and PC are on the same Wi-Fi network.
5. (Optional) Disable Windows Firewall if connection is blocked.
6. Build and Run the app!

## 📸 Screenshots
*(Add your screenshots here later!)*

---
Developed for University Lecturer Presentation.
