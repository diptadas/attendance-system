<?php
include 'server_config.php';

$courseID = $_GET['courseID'];
$courseTitle = $_GET['courseTitle'];

$response["success"] = 0;
$response["key"] = array("status");
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	$sql = "INSERT INTO course VALUES (?, ?)";
    
    $stmt = $con->prepare($sql);
	$stmt->execute([$courseID, $courseTitle]);

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