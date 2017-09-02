<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
$room_id = $_POST['room_id'];

require_once('dbConnect.php');
 
$result = array();

$res = mysqli_query($con, "SELECT * FROM schedule WHERE room_id = '$room_id' ORDER BY begin_time ASC");
 
while($row = mysqli_fetch_array($res)){
	array_push($result,
			array(
				'id'=>$row["id"],
                                'manager_id'=>$row["manager_id"],
				'begin'=>$row["begin_time"],
				'end'=>$row["end_time"]
			));
}
 
 echo json_encode($result);
 
 mysqli_close($con);

}else{

}

?>