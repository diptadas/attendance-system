<?php

include 'server_config.php';

$assignmentID = $_GET['assignmentID'];

$response["success"] = 0;
$response["key"] = array();
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
	$sql = "SELECT DISTINCT(date_time) FROM attendance WHERE assignment_id = ? ORDER BY date_time DESC";
    
    $stmt = $con->prepare($sql);
	$stmt->execute([$assignmentID]);
    
	while ($row = $stmt->fetch(PDO::FETCH_ASSOC))
    {
        $response["key"] = array_keys($row);
		array_push($response["data"], $row);
	}

	$response["success"] = 1;
}

catch(PDOException $e)
{
    echo $e;
	$response["success"] = 0;
}

echo json_encode($response);

?>