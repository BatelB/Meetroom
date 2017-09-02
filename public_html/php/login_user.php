<?php

$result = array();
$result["status"] = "false";
$result["message"] = "Login server error";
$result["id"] = "";
$result["type"] = "";
$result["fullname"] = "";

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$email = $_POST['email'];
$password = $_POST['password'];

$password = md5($password);

require_once('dbConnect.php');
 
 $sql = "SELECT * FROM users WHERE email='$email' AND password='$password'";
 
 $res = mysqli_query($con,$sql);
 
 $row = mysqli_fetch_array($res);
 
 if(isset($row)){

        $result["status"] = "true";
	$result["id"] = $row["id"];
	$result["type"] = $row["type"];
        $result["fullname"] = $row["fullname"];
	$result["message"] = "Welcome " . $row["fullname"] . " !";

 }else{ 
	$result["message"] = "Wrong username or password";
 }
 
 mysqli_close($con);

}else{

}

echo json_encode($result);

?>