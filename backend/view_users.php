<?php
// 1. Establish connection
$conn = new mysqli("localhost", "root", "", "arnolds_db");

if ($conn->connect_error) {
    die("<h2 style='color:red;'>Connection failed: " . $conn->connect_error . "</h2><p>Make sure MySQL is running in XAMPP.</p>");
}

// 2. Query all users
$sql = "SELECT * FROM users ORDER BY created_at DESC";
$result = $conn->query($sql);

echo "<html>
<head>
    <title>Lecturer Dashboard - User Activity</title>
    <style>
        body { font-family: sans-serif; margin: 40px; background-color: #f4f4f9; }
        table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #4CAF50; color: white; }
        tr:hover { background-color: #f1f1f1; }
        .status-online { color: green; font-weight: bold; }
        .header { display: flex; justify-content: space-between; align-items: center; }
        .error-box { padding: 20px; background: #ffebee; border-left: 5px solid #f44336; margin-bottom: 20px; }
    </style>
</head>
<body>";

echo "<div class='header'>";
echo "<h1>User Management System (Localhost)</h1>";
echo "</div>";

if (!$result) {
    echo "<div class='error-box'>";
    echo "<h3>Database Error!</h3>";
    echo "<p>Error details: " . $conn->error . "</p>";
    echo "<p><strong>Fix:</strong> Please run <a href='setup_db.php'>setup_db.php</a> to fix the table structure.</p>";
    echo "</div>";
} else {
    echo "<p>Total Users: " . $result->num_rows . "</p>";
    if ($result->num_rows > 0) {
        echo "<table>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Email Address</th>
                    <th>Date of Birth</th>
                    <th>Signed Up On</th>
                    <th>Last Login Activity</th>
                </tr>";

        while($row = $result->fetch_assoc()) {
            $lastLogin = (isset($row["last_login"]) && $row["last_login"]) ? $row["last_login"] : "Never logged in";
            $name = isset($row["name"]) ? $row["name"] : "N/A";
            $email = isset($row["email"]) ? $row["email"] : "N/A";
            $dob = isset($row["dob"]) ? $row["dob"] : "N/A";
            $created = isset($row["created_at"]) ? $row["created_at"] : "N/A";

            echo "<tr>
                    <td>" . $row["id"] . "</td>
                    <td><strong>" . htmlspecialchars($name) . "</strong></td>
                    <td>" . htmlspecialchars($email) . "</td>
                    <td>" . htmlspecialchars($dob) . "</td>
                    <td>" . $created . "</td>
                    <td class='status-online'>" . $lastLogin . "</td>
                  </tr>";
        }
        echo "</table>";
    } else {
        echo "<div style='padding: 20px; background: white; border-radius: 5px;'>
                <h3>No users found in the database.</h3>
                <p>Please use the Android App to Sign Up.</p>
              </div>";
    }
}

echo "<br><hr>";
echo "<p><small>Database: arnolds_db | Table: users | Host: localhost</small></p>";
echo "</body></html>";

$conn->close();
?>
