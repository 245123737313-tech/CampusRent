<?php
require 'db_connect.php';

$name = $_POST['name'];
$price = $_POST['price'];
$description = $_POST['description'];
$user_id = $_POST['user_id'];

$image_url = "";

if (isset($_FILES['image'])) {
    $target_dir = "uploads/";
    if (!file_exists($target_dir)) {
        mkdir($target_dir, 0777, true);
    }
    
    $image_name = basename($_FILES["image"]["name"]);
    $target_file = $target_dir . time() . "_" . $image_name;
    
    if (move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
        // Use 10.0.2.2 for Android Emulator
        $image_url = "http://10.0.2.2/campusrent/" . $target_file; 
    } else {
        // Return detailed error if file move fails (usually permissions)
        echo json_encode([
            "status" => "error", 
            "message" => "Server Error: Failed to save image. Check 'uploads' folder permissions."
        ]);
        exit;
    }
}

$stmt = $conn->prepare("INSERT INTO items (name, price, description, image_url, user_id) VALUES (?, ?, ?, ?, ?)");
$stmt->bind_param("sdssi", $name, $price, $description, $image_url, $user_id);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Item added successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Database Error: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>
