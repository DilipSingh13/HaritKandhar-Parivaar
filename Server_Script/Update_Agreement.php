<?php
include 'config.php';

// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 
 die("Connection failed: " . $conn->connect_error);
}

$status = $_POST['status'];
$email = $_POST['email'];
$mobile = $_POST['mobile'];


$sql = "UPDATE haritkandharapp_user SET agreement_status = '$status' WHERE email = '$email' and mobile='$mobile'";

if ($conn->query($sql)) {
        $response["error"] = false;
        $response["message"] = "Agreement status updated";
        die(json_encode($response));
} else {
        $response["error"] = true;
        $response["message"] = "Database error";
        die(json_encode($response));
}
 echo $json;
$conn->close();
?>