<?php
$conn = new mysqli("localhost", "root", "");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Create database if not exists
$sql = "CREATE DATABASE IF NOT EXISTS arnolds_db";
if ($conn->query($sql) === TRUE) {
    echo "Database created successfully<br>";
} else {
    echo "Error creating database: " . $conn->error . "<br>";
}

$conn->select_db("arnolds_db");

// Create table if not exists
// Added last_login column
$sql = "CREATE TABLE IF NOT EXISTS users (
    id INT(11) AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    dob VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
)";

if ($conn->query($sql) === TRUE) {
    echo "Table 'users' created successfully<br>";
} else {
    echo "Error creating table: " . $conn->error . "<br>";
}

// Check if last_login column exists, if not add it (for existing tables)
$result = $conn->query("SHOW COLUMNS FROM users LIKE 'last_login'");
if ($result->num_rows == 0) {
    $conn->query("ALTER TABLE users ADD last_login TIMESTAMP NULL");
    echo "Column 'last_login' added successfully<br>";
}

// Check if created_at column exists, if not add it
$result = $conn->query("SHOW COLUMNS FROM users LIKE 'created_at'");
if ($result->num_rows == 0) {
    $conn->query("ALTER TABLE users ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
    echo "Column 'created_at' added successfully<br>";
}

$conn->close();
?>
