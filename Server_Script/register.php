<?php
    require("config.php");
    if (!empty($_POST)) {
        $response = array(
            "error" => FALSE
        );
        
        $query = " SELECT * FROM haritkandharapp_user WHERE email = :email";
        //now lets update what :user should be
        $query_params = array(
            ':email' => $_POST['email']
        );
        try {
            $stmt = $db->prepare($query);
            $result = $stmt->execute($query_params);
        }
        catch (PDOException $ex) {
            $response["error"] = TRUE;
            $response["message"] = "Database Error1. Please Try Again!";
            die(json_encode($response));
        }
        $row = $stmt->fetch();
        
        if ($row) {
            $response["error"] = TRUE;
            $response["message"] = "sorry, this email is already in use";
            die(json_encode($response));
        } else {
            $qry = "INSERT INTO haritkandharapp_user (unique_id, name, address, village, tehsil, district, pincode, survey_number, house_number, mobile, whatsapp_number, email, encrypted_password, verified, created_at, otp, profile, admin_approved, block_status, agreement_status) VALUES (:uuid, :name, :address, :village , :tehsil, :district, :pincode, :survey_number, :house_number, :mobile, :whatsapp_number, :email, :encrypted_password, :verified, NOW(), :otp, :profile, :admin_approved, :block_status, :agreement_status)";
            $otp = rand(100000, 999999);
         $verified = 'unverified';
         $admin_approved = 'unapproved';
         $block_status = 'Not Available';
         $agreement_status='Not Agree';
            $qry_params = array(
                ':uuid' => uniqid('', true),
                ':name' => $_POST['name'],
                ':address' => $_POST['address'],
                ':village' => $_POST['village'],
                ':tehsil' => $_POST['tehsil'],
                ':district' => $_POST['district'],
                ':pincode' => $_POST['pincode'],
                ':survey_number' => $_POST['survey_number'],
                ':house_number' => $_POST['house_number'],
                ':mobile' => $_POST['mobile'],
                ':whatsapp_number' => $_POST['whatsapp_number'],
                ':email' => $_POST['email'],
                ':encrypted_password' => password_hash($_POST['password'], PASSWORD_DEFAULT),
                ':verified' => $verified,
                ':otp' => $otp,
                ':profile' => $_POST['profile'],
                ':admin_approved' => $admin_approved,
                ':block_status' => $block_status,
                ':agreement_status' => $agreement_status);
                
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
            $subject = "Harit Kandhar Email Verification";
            $message = "Hello $name,\n\nVerify that you own $email.\n\nYou may be asked to enter this confirmation code:\n\n$otp\n\nRegards,\nHarit Kandhar Parivar";
            $from = "support@anviinfotechs.com";
            $headers = "From:" . $from;
            
            mail($email,$subject,$message,$headers);
            
            $response["error"] = FALSE;
            $response["message"] = "Register successful!";
            echo json_encode($response);
        }
    } else {
        echo 'Error Occured';
    }