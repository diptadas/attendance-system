<?php

include 'server_config.php';

$assignmentID = $_GET['assignmentID'];
$date = $_GET['date'];

$response["success"] = 0;
$response["key"] = array();
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
	$sql = "SELECT * FROM attendance A JOIN user_info B ON A.student_id = B.student_id WHERE assignment_id = ? AND date_time = ? ORDER BY A.student_id";
    
    $stmt = $con->prepare($sql);
	$stmt->execute([$assignmentID, $date]);
    
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