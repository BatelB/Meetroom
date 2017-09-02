<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$email = $_POST['email'];
$password = $_POST['password'];
$type =  $_POST['type'];
$fullname = $_POST['fullname'];

require_once('dbConnect.php');
 
$password = md5($password);

$res = mysqli_query($con, "INSERT INTO users (email, password, fullname, entry_date, type) VALUES('$email', '$password', '$fullname', now(), '$type')");
 
if($res){
	echo "success";
}else{
	echo "fail";
}

 mysqli_close($con);

}else{
	echo "fail";
}

?>