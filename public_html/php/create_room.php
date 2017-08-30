<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$number = $_POST['number'];
$floor = $_POST['floor'];
$chairs = $_POST['chairs'];
$equipment =  $_POST['equipment'];

require_once('dbConnect.php');
 
$res = mysqli_query($con, "INSERT INTO rooms (number, floor, chairs, equipment, entry_date) VALUES('$number', '$floor', '$chairs', '$equipment', now())");
 
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