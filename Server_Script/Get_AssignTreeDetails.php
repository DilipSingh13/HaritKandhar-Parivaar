 <?php
 require("config.php");
    $hostname = "localhost";
    $username = "username here";
    $password = "password here";
    $dbname = "database name here";
    
     $con = mysqli_connect($hostname,$username,$password,$dbname);
    
        
     $query = "SELECT * FROM Assign_Tree_Details WHERE email = :email";
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
    $count1 = "1";
    $count2 = "2";
    $checkCount1=strcmp($count1,$row['tree_count']);
    $checkCount2=strcmp($count2,$row['tree_count']);
    if ($row) {
    if($checkCount1==0){
    $response["error"] = false;
    $response["count"] = $row['tree_count'];
    $response["tree_name1"] = $row['tree_name1'];
    $response["tree_name2"] = $row['tree_name2'];
    die(json_encode($response));
    }
    else if($checkCount2==0){
    
    $response["error"] = false;
    $response["count"] = $row['tree_count'];
    $response["tree_name1"] = $row['tree_name1'];
    $response["tree_name2"] = $row['tree_name2'];
    die(json_encode($response));
    }
    else{
    $response["error"] = true;
    $response["message"] = "No assigned plant found !";
    die(json_encode($response));
    }
}
else {
            $response["error"] = false;
            $response["message"] = "No assigned plant found !";
            die(json_encode($response));
   }
    mysqli_close($con);
?>
