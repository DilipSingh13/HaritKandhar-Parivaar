 <?php
	$hostname = "localhost";
	$username = "username here";
  $password = "password here";
  $dbname = "database name here";
	
     $con = mysqli_connect($hostname,$username,$password,$dbname);
 
            $email = $_POST['email'];
            $file_name = $_POST['file_name'];
         
            $Sql_Query = "UPDATE haritkandharapp_user SET profile='$file_name' WHERE email='$email'";
                
            if(mysqli_query($con,$Sql_Query)){
                $response["error"] = false;
                $response["message"] = "Profile Updated successfully !";
                die(json_encode($response));
            }
        
        else{
          $response["error"] = true;
          $response["message"] = "Error occured try again!";
          die(json_encode($response));
        }
        
    mysqli_close($con);
?>
