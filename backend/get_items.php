<?php
require 'db_connect.php';

$sql = "SELECT * FROM items ORDER BY id DESC";
$result = $conn->query($sql);

$items = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $items[] = $row;
    }
}

echo json_encode($items);

$conn->close();
?>
