<?php
    require("config.php");
    if (!empty($_POST)) {

   $query = "SELECT * FROM Approved_Plantation WHERE email = :email and Year_Slot =:year";
         $query_params = array(
        ':email' => $_POST['email'],
        ':year' => $_POST['year']);   
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
    $year= $_POST['year'];
    $month= $_POST['month'];
    $status= $_POST['status'];
    $checkYear=strcmp($year,$row['Year_Slot']);
    $checkMonth=strcmp($month,$row['Month_Slot']);
    $checkGrant=strcmp($status,$row['Grant_Status']);
    if($checkYear==0){
        if($checkMonth==0){
            if($checkGrant==0){
            $response["error"] = false;
            $response["message"] = "Success";
            die(json_encode($response));
            }
        else{
        $response["error"] = true;
            $response["message"] = "You have already uploaded pic";
            die(json_encode($response));
        }
        }
        else{
            $response["error"] = true;
            $response["message"] = "Slot is currently not activated";
            die(json_encode($response));
        }
    }
    else{
            $response["error"] = true;
            $response["message"] = "Invalid year";
            die(json_encode($response));
    }
    }
    
    mysqli_close($con);
?>