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
$code = $_POST['code'];
$month = $_POST['month'];

$query = "SELECT * FROM Approve_Plant_Picture where Unique_code=:code";
         $query_params = array(
        ':code' => $_POST['code']);   
    try {
        $stmt = $db->prepare($query);
        $result = $stmt->execute($query_params);
        }
        
    catch (PDOException $ex) {
        $response["error"] = true;
        $response["message"] = "Database Error1. contact admin!";
        die(json_encode($response));
    }
    
    $row = $stmt->fetch();
    $email = $_POST['email'];
    $checkdetails=strcmp($mobile,$row['email']);
    if($row){
    $response["error"] = false;
    $response["longitude"] = $row['longitude'];
    $response["latitudes"] = $row['latitudes'];
    $response["month"] = $row['month'];
    die(json_encode($response));
}
else{
     $response["error"] = false;
    $response["longitude"] = '0';
    $response["latitudes"] = '0';
    $response["month"] = '0';
    die(json_encode($response));
}
$conn->close();
?>