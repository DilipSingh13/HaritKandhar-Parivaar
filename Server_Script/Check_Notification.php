<?php
include 'config.php';

// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 
 die("Connection failed: " . $conn->connect_error);
}

$query = "SELECT * FROM Approved_Plantation where Name=:name and Email=:email and Mobile=:mobile and Grant_Status=:status";
    
    $query_params = array(
        ':name' => $_POST['name'],
        ':email' => $_POST['email'],
        ':mobile' => $_POST['mobile'],
        ':status' => $_POST['status']
    );
    
    try {
        $stmt = $db->prepare($query);
        $result = $stmt->execute($query_params);
}

    catch (PDOException $ex) {
        $response["error"] = true;
        $response["message"] = "Database Error. Please Try Again!";
        die(json_encode($response));
    }
    $row = $stmt->fetch();
    if($row){
         $response["error"] = false;
         $response["message"] = "Found";
        die(json_encode($response));
    }
    else{
        $response["error"] = true;
        $response["message"] = "Not Found";
        die(json_encode($response));
    }
$conn->close();
?>