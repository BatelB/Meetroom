<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$room_id = $_POST['room_id'];
$number = $_POST['number'];
$floor = $_POST['floor'];
$chairs = $_POST['chairs'];
$equipment =  $_POST['equipment'];

require_once('dbConnect.php');
 
$res = mysqli_query($con, "UPDATE rooms SET number = '$number', floor = '$floor', chairs = '$chairs', equipment = '$equipment' WHERE id = '$room_id'");
 
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