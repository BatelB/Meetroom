<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$id = $_POST['id'];

require_once('dbConnect.php');
 
$res = mysqli_query($con, "DELETE FROM schedule WHERE id = '$id'");
mysqli_query($con, "DELETE FROM invitations WHERE schedule_id = '$id'");
 
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