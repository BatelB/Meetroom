<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$schedule_id = $_POST["schedule_id"]; 
$begin_time =  $_POST['begin_time'];
$end_time =  $_POST['end_time'];
$invitations = $_POST['invitations'];

require_once('dbConnect.php');

$res = mysqli_query($con, "UPDATE schedule SET begin_time = '$begin_time', end_time = '$end_time' WHERE id = '$schedule_id'");

$delete_res = mysqli_query($con, "DELETE FROM invitations WHERE schedule_id = '$schedule_id'");

$json_array = json_decode($invitations, true);
for ($i = 0; $i < sizeof($json_array); $i++) {
	 $user_id = $json_array[$i]["id"];
	 mysqli_query($con, "INSERT INTO invitations(schedule_id, user_id) VALUES ('$schedule_id', '$user_id')");
}

if($res && $delete_res){
	echo "success";
}else{
	echo "fail";
}

mysqli_close($con);

}else{
	echo "Fail, no POST";
}

?>