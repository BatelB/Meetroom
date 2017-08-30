<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
require_once('dbConnect.php');
 
$result = array();

$res = mysqli_query($con, "SELECT * FROM rooms");
 
while($row = mysqli_fetch_array($res)){
	array_push($result,
			array(
				'id'=>$row["id"],
                                'number'=>$row["number"],
				'floor'=>$row["floor"],
				'chairs'=>$row["chairs"],
				'equipment'=>$row["equipment"],
			));
}
 
 echo json_encode($result);
 
 mysqli_close($con);

}else{

}

?>