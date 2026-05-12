<?php
// 1. Absolute first line - NO spaces or lines above this
ob_start();
error_reporting(E_ALL);
ini_set('display_errors', 0); // Don't let errors break JSON
header('Content-Type: application/json; charset=utf-8');

$response = ["status" => "error", "message" => "Server initialization error"];

try {
    $conn = new mysqli("localhost", "root", "", "arnolds_db");
    if ($conn->connect_error) {
        throw new Exception("DB Connection failed. Is MySQL running in XAMPP?");
    }

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        $action = $conn->real_escape_string($_POST['action'] ?? '');

        if ($action === 'signup') {
            $name = $conn->real_escape_string($_POST['name'] ?? '');
            $email = $conn->real_escape_string($_POST['email'] ?? '');
            $dob = $conn->real_escape_string($_POST['dob'] ?? '');
            $pass = $conn->real_escape_string($_POST['password'] ?? '');

            $sql = "INSERT INTO users (name, email, dob, password) VALUES ('$name', '$email', '$dob', '$pass')";
            if ($conn->query($sql)) {
                $response = ["status" => "success"];
            } else {
                throw new Exception("Signup Error: " . $conn->error);
            }
        }
        elseif ($action === 'login') {
            $email = $conn->real_escape_string($_POST['email'] ?? '');
            $pass = $conn->real_escape_string($_POST['password'] ?? '');

            $sql = "SELECT * FROM users WHERE email='$email' AND password='$pass'";
            $result = $conn->query($sql);

            if ($result && $result->num_rows > 0) {
                $user = $result->fetch_assoc();

                // Try to update last login, but don't crash if column missing
                @$conn->query("UPDATE users SET last_login = NOW() WHERE id = " . $user['id']);

                $response = [
                    "status" => "success",
                    "name"   => $user['name'],
                    "email"  => $user['email']
                ];
            } else {
                $response = ["status" => "error", "message" => "Invalid email or password"];
            }
        }
    }
    $conn->close();
} catch (Exception $e) {
    $response = ["status" => "error", "message" => $e->getMessage()];
}

// Clear the buffer of any accidental whitespace/warnings
ob_clean();
echo json_encode($response);
exit;
// NO CLOSING TAG HERE - this prevents accidental whitespace errors
