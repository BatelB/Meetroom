<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$room_id = $_POST['room_id'];
$manager_id = $_POST['manager_id'];
$begin_time =  $_POST['begin_time'];
$end_time =  $_POST['end_time'];
$invitations = $_POST['invitations'];

require_once('dbConnect.php');

$res = mysqli_query($con, "INSERT INTO schedule (room_id, manager_id, begin_time, end_time) VALUES('$room_id', '$manager_id', '$begin_time', '$end_time')");
$insert_id = mysqli_insert_id($con);

$json_array = json_decode($invitations, true);
for ($i = 0; $i < sizeof($json_array); $i++) {
	 $user_id = $json_array[$i]["id"];
	 mysqli_query($con, "INSERT INTO invitations(schedule_id, user_id) VALUES ('$insert_id', '$user_id')");
}

if($res){
	echo $insert_id;
}else{
	echo "fail :  " . mysqli_error($con);
}

 mysqli_close($con);

}else{
	echo "Fail, no POST";
}

?>