<?php
$username = "username here";
$password = "password here";
$host = "localhost"; 
$database = "database name here"; 

$conn=mysqli_connect($host,$username,$password,$database);
if (mysqli_connect_errno())
 {
 echo "Failed to connect to MySQL: " . mysqli_connect_error();
 }
else
{
    
/*echo "Connection sucess.";*/
}

?>