<?php
include 'server_config.php';

$assignmentID = $_GET['assignmentID'];
$date = $_GET['date'];
$presentIDs = $_GET['presentIDs'];
$absentIDs = $_GET['absentIDs'];

$response["success"] = 0;
$response["key"] = array("status");
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	if($presentIDs != "")
	{
		$sql = "UPDATE attendance SET is_present = 1 WHERE assignment_id = ?  AND date_time = ? AND student_id IN (?)";
    
    	$stmt = $con->prepare($sql);
		$stmt->execute([$assignmentID, $date, $presentIDs]);
	}

	if($absentIDs != "")
	{
		$sql = "UPDATE attendance SET is_present = 0 WHERE assignment_id = ?  AND date_time = ? AND student_id IN (?)";
    
    	$stmt = $con->prepare($sql);
		$stmt->execute([$assignmentID, $date, $absentIDs]);
	}

	$response["success"] = 1;
	$single_info = array();
	$single_info["status"] = "ok";
	array_push($response["data"], $single_info);
}
catch(PDOException $e)
{
	$response["success"] = 0;
}

END:
	echo json_encode($response);
?>