<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$user_id = $_POST['user_id'];

require_once('dbConnect.php');
 
$result = array();

$res = mysqli_query($con, "SELECT * FROM invitations WHERE user_id = '$user_id'");
 
while($row = mysqli_fetch_array($res)){
	
	$schedule_id = $row["schedule_id"];
	$sub_res = mysqli_query($con, "SELECT * FROM schedule WHERE id = '$schedule_id'");
	$sub_res_row = mysqli_fetch_array($sub_res);
	
	$room_id = $sub_res_row["room_id"];	
	$room_res = mysqli_query($con, "SELECT * FROM rooms WHERE id = '$room_id'");
 	$room_res_row = mysqli_fetch_array($room_res);
 	
 	array_push($result,
		array(
			'room_id'=>$room_id,
			'room_number'=>$room_res_row["number"],
			'room_floor'=>$room_res_row["floor"],
			'schedule_id'=>$schedule_id,
			'begin_time'=>$sub_res_row["begin_time"],
			'end_time'=>$sub_res_row["end_time"]
		));

}
 
 echo json_encode($result);
 
 mysqli_close($con);

}else{

}

?>