<?php
include 'server_config.php';

$idPairs = $_GET['idPairs'];

$response["success"] = 0;
$response["key"] = array("status");
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	$sql = "INSERT INTO course_registration VALUES " . $idPairs;
    $stmt = $con->query($sql);

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