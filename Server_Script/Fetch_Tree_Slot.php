<?php
include 'config.php';

// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 die("Connection failed: " . $conn->connect_error);
}

$name = $_POST['name'];
$email = $_POST['email'];
$mobile = $_POST['mobile'];

$sql = "SELECT * FROM Approved_Plantation_Plan where Name='$name' and Email='$email' and Mobile='$mobile'";

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