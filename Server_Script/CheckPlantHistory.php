 <?php
 require("config.php");
    $hostname = "localhost";
    $username = "username here";
    $password = "password here";
    $dbname = "database name here";
    
     $con = mysqli_connect($hostname,$username,$password,$dbname);
    
    $approve_status='approved';
         $query = "SELECT * FROM Plantation_Approval WHERE email = :email AND approve_status=:approve_status";
         $query_params = array(
        ':email' => $_POST['email'],
        ':approve_status' => $approve_status);   
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
    $count = "1";
    $count2 = "2";
    $status = "unapproved";
    $checkdetails=strcmp($count,$row['tree_count']);
    $checkdetails2=strcmp($count2,$row['tree_count']);
    $checkdetails3=strcmp($status,$row['approve_status']);
        if ($row) {
         if($checkdetails==0){
    	$response["error"] = false;
        $response["message"] = "approved!";
        die(json_encode($response));
        }
        else if($checkdetails2==0){
    	$response["error"] = true;
          $response["message"] = "Your already have 2 plant approved!";
          die(json_encode($response));
        }
        else if($checkdetail3==0){
            $response["error"] = true;
          $response["message"] = "Your pervious request is under process wait for previous request approval!";
          die(json_encode($response));
        }
        else{
          $response["error"] = true;
          $response["message"] = "Your already have 2 plant approved!";
          die(json_encode($response));
        }
        }
   else {
            $response["error"] = false;
            $response["message"] = "Your pervious request is under process wait for previous request approval!";
            die(json_encode($response));
   }
        
    mysqli_close($con);
?>
