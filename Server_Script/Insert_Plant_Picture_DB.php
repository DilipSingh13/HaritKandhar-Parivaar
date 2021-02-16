 <?php
	$hostname = "localhost";
	$username = "username here";
    $password = "password here";
    $dbname = "database name here";
	
     $con = mysqli_connect($hostname,$username,$password,$dbname);
            
            $code = $_POST['code'];
            $name = $_POST['name'];
            $email = $_POST['email'];
            $mobile = $_POST['mobile'];
            $longitude = $_POST['longitude'];
            $latitudes = $_POST['latitudes'];
            $month = $_POST['month'];
            $tree_name = $_POST['tree_name'];
            $file_name = $_POST['file_name'];
            
$Sql_Query="INSERT INTO Approve_Plant_Picture (Unique_code,name,email,mobile,longitude,latitudes,month,tree_name,date,time,plant_picture,approve_status) VALUES ('$code','$name','$email','$mobile','$longitude','$latitudes','$month','$tree_name',NOW(),NOW(),'$file_name','unapproved')";
            
            $sql = "UPDATE Approved_Plantation_Plan SET Grant_Status = 'No' WHERE Unique_code='$code'";

            if(mysqli_query($con,$Sql_Query) && mysqli_query($con,$sql)){
                $response["error"] = false;
                $response["message"] = "Picture Uploaded successfully !";
                die(json_encode($response));
            }
        else{
          $response["error"] = true;
          $response["message"] = "Error occured try again!";
          die(json_encode($response));
        }
        
    mysqli_close($con);
?>
