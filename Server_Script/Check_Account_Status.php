 <?php
 require("config.php");
    $hostname = "localhost";
    $username = "username here";
    $password = "password here";
    $dbname = "database name here";
    
     $con = mysqli_connect($hostname,$username,$password,$dbname);
    
         $query = "SELECT * FROM haritkandharapp_user WHERE email = :email";
         $query_params = array(
        ':email' => $_POST['email']);   
    try {
        $stmt = $db->prepare($query);
        $result = $stmt->execute($query_params);
        }
        
    catch (PDOException $ex) {
        $response["error"] = true;
        $response["message"] = "Database Error. contact admin!";
        die(json_encode($response));
    }
    
    $row = $stmt->fetch();
    $statusagree = "Agree";
    $checkdetailsAgree=strcmp($statusagree,$row['agreement_status']);
    $status = "unblocked";
    $checkdetails=strcmp($status,$row['block_status']);
    
    if($checkdetailsAgree==0){
        if($checkdetails==0){
            $response["error"] = false;
            $response["message"] = "grant!";
            die(json_encode($response));
            
        }
    else{
    $response["error"] = true;
    $response["message"] = "Your account is blocked.. contact admin!";
    die(json_encode($response));
    }
    }
else {
    $response["error"] = true;
    $response["message"] = "Kindly accept the agreements";
    die(json_encode($response));
  }
        
mysqli_close($con);
?>
