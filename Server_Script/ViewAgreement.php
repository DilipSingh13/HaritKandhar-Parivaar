<?php

$host = "localhost"; 
$username = "username here";
$password = "password here";
$dbname = "database name here";
// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 
 die("Connection failed: " . $conn->connect_error);
}

$email= $_POST['email'];
$mobile= $_POST['mobile'];

$sql = "SELECT agreement_status FROM haritkandharapp_user WHERE email='$email' and mobile='$mobile'";

$result = $conn->query($sql);

if ($result->num_rows >0) {
 
 
 while($row[] = $result->fetch_assoc()) {
 
 $tem = $row;
 
 $json = json_encode($tem);
 
 
 }
 
} else {
 echo "No Results Found";
}
 echo $json;
$conn->close();
?>