<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
 
require_once('dbConnect.php');
 
$result = array();

$res = mysqli_query($con, "SELECT * FROM users");
 
while($row = mysqli_fetch_array($res)){
	array_push($result,
			array(
				'id'=>$row["id"],
				'entry_date'=>$row["entry_date"],
				'fullname'=>$row["fullname"],
				'email'=>$row["email"],
				'type'=>$row["type"]
			));
}
 
 echo json_encode($result);
 
 mysqli_close($con);

}else{

}

?>