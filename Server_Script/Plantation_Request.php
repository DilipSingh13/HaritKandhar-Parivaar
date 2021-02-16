<?php
    require("config.php");
    if (!empty($_POST)) {
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
    $blockstatus = "blocked";
    $status = "approved";
    $tree1='Not Available';
    $tree2='Not Available';
    $count='Not Available';
    $status2='rejected';
    $code = rand(100000, 999999);
    $checkblock=strcmp($blockstatus,$row['block_status']);
    $checkdetails=strcmp($status,$row['admin_approved']);
    $checkdetails2=strcmp($status2,$row['block_status']);
    if($checkblock==0){
        $response["error"] = true;
            $response["message"] = "Your account is blocked by admin. Contact admin for more information";
            die(json_encode($response));
    }
    else{
        if($checkdetails==0){
        if($checkdetails2==0){
           $response["message"] = "Your account is rejected by admin. Contact admin for more information";
            die(json_encode($response));
        }
        else{
              $qry = "INSERT INTO Plantation_Approval (name, email, mobile, address, tree_count, tree_name1, tree_name2, approve_status, date, time,unique_code,approved_date,approved_time)
           VALUES (:name, :email, :mobile,  :address, :count, :tree1,:tree2, :approve_status, NOW(), NOW(), :code,NOW(),NOW())";
            $qry_params = array(
                ':name' => $_POST['name'],
                ':email' => $_POST['email'],
                ':mobile' => $_POST['mobile'],
                ':address' => $_POST['address'],
                ':count' => $count,
                ':tree1' => $tree1,
                ':tree2' => $tree2,
                ':code' => $code,
                ':approve_status' => $_POST['approve_status']);
            
            try {
                $st = $db->prepare($qry);
                $result = $st->execute($qry_params);
            }
            catch (PDOException $ex) {
                $response["error"] = TRUE;
                $response["message"] = "Database Error. Please Try Again!";
                die(json_encode($response));
            }
            $name = $_POST['name'];
            $email = $_POST['email'];
            $count = $_POST['count'];
            $subject = "Harit Kandhar Plantation Request";
            $message = "Hello $name,\n\nYour tree plantation request is under process. It will take 3-4 working days to process your request.\n\nRegards,\nHarit Kandhar Parivar";
            $from = "support@anviinfotechs.com";
            $headers = "From:" . $from;
            
            mail($email,$subject,$message,$headers);
            
            $response["error"] = FALSE;
            $response["message"] = "Sumbit successful!";
            echo json_encode($response);
        }
        }
        else{
            $response["error"] = true;
          $response["message"] = "Your account approval is under process please wait for approval before submitting request";
          die(json_encode($response));
        }
    }
    }
?>