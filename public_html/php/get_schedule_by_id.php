<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$schedule_id = $_POST['id'];

require_once('dbConnect.php');
 
$result = array();

$res = mysqli_query($con, "SELECT * FROM schedule WHERE id = '$schedule_id'");
 
$row = mysqli_fetch_array($res);
	
	$result["begin"] = $row["begin_time"];
	$result["end"] = $row["end_time"];

    $invitations = array();
	$invitations_res = mysqli_query($con, "SELECT * FROM invitations WHERE schedule_id = '$schedule_id'");

    while($invitations_row = mysqli_fetch_array($invitations_res)){

		
    	$user_id = $invitations_row["user_id"];
    	$username_res = mysqli_query($con, "SELECT * FROM users WHERE id = '$user_id'");
 		$username_row = mysqli_fetch_array($username_res);

	 	array_push($invitations,
			array(
				'user_id'=>$user_id,
				'fullname'=>$username_row["fullname"]
			));

	}

   $result["invitations"] = $invitations;
 
 echo json_encode($result);
 
 mysqli_close($con);

}else{

}

?>