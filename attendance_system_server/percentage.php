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
    
	$sql = "SELECT *, (SELECT COUNT(DISTINCT date_time) FROM attendance A WHERE A.assignment_id = ?) as total_count FROM 
	(SELECT B.student_id, COUNT(B.student_id) as attend_count FROM attendance B WHERE B.assignment_id = ? AND is_present = 1 GROUP BY B.student_id) as C 
	JOIN user_info D ON C.student_id = D.student_id ORDER BY C.student_id";
    
    $stmt = $con->prepare($sql);
	$stmt->execute([$assignmentID, $assignmentID]);
    
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