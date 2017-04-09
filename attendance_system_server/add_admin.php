<?php
include 'server_config.php';

$adminIDs = $_GET['adminIDs'];
$nonAdminIDs = $_GET['nonAdminIDs'];

$response["success"] = 0;
$response["key"] = array("status");
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	if($adminIDs != "")
	{
		$sql = "UPDATE user_info SET is_admin = 1 WHERE user_id IN (?) AND user_type = 2";
    
    	$stmt = $con->prepare($sql);
		$stmt->execute([$adminIDs]);
	}

	if($nonAdminIDs != "")
	{
		$sql = "UPDATE user_info SET is_admin = 0 WHERE user_id IN (?) AND user_type = 2";
    
    	$stmt = $con->prepare($sql);
		$stmt->execute([$nonAdminIDs]);
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