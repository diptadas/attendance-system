<?php

include 'server_config.php';

$userType = $_GET['userType'];

$response["success"] = 0;
$response["key"] = array();
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	if($userType == "1") //student
	{
		$studentID = $_GET['studentID'];

		$sql = "SELECT *, (SELECT COUNT(DISTINCT date_time) FROM attendance A1 WHERE A1.assignment_id = B.assignment_id) as total_count,
		(SELECT COUNT(*) FROM attendance A2 WHERE A2.assignment_id = B.assignment_id AND A2.student_id = ? AND is_present = 1) as attend_count
		FROM course_assignment B JOIN course_registration C ON B.assignment_id = C.assignment_id JOIN course D ON B.course_id = D.course_id WHERE student_id = ?";

		$stmt = $con->prepare($sql);
		$stmt->execute([$studentID, $studentID]);
	}
	else //teacher
	{
		$userID = $_GET['userID'];

		$sql = "SELECT *, (SELECT COUNT(DISTINCT date_time) FROM attendance A WHERE A.assignment_id = B.assignment_id) as total_count FROM 
		course_assignment B JOIN course C ON C.course_id = B.course_id WHERE teacher_user_id = ?";

		$stmt = $con->prepare($sql);
		$stmt->execute([$userID]);
	}
    
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