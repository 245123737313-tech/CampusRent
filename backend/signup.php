<?php
require 'db_connect.php';

$email = $_POST['email'];
$password = $_POST['password'];

if (!$email || !$password) {
    echo json_encode(["status" => "error", "message" => "Missing fields"]);
    exit;
}

$hashed_password = password_hash($password, PASSWORD_DEFAULT);

$stmt = $conn->prepare("INSERT INTO users (email, password) VALUES (?, ?)");
$stmt->bind_param("ss", $email, $hashed_password);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "User registered successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Registration failed: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>
