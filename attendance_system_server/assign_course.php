<?php
include 'server_config.php';

$courseID = $_GET['courseID'];
$teacherUserID = $_GET['teacherUserID'];
$session = $_GET['session'];
$section = $_GET['section'];
$isRunning = $_GET['isRunning'];


$response["success"] = 0;
$response["key"] = array("status");
$response["data"] = array();


try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	$sql = "SELECT * FROM course_assignment WHERE course_id = ? AND session = ? AND section = ?";
    $stmt = $con->prepare($sql);
    $stmt->execute([$courseID, $session, $section]);

    if ($row = $stmt->fetch()) 
	{
		$sql = "UPDATE course_assignment SET teacher_user_id = ?, is_running = ? WHERE course_id = ? AND session = ? AND section = ?";
    	$stmt = $con->prepare($sql);
    	$stmt->execute([$teacherUserID, $isRunning, $courseID, $session, $section]);

		$response["success"] = 1;
		$single_info = array();
		$single_info["status"] = "Assignment Updated.";
		array_push($response["data"], $single_info);
		goto END;
	}

	$sql = "INSERT INTO course_assignment VALUES(0, ?, ?, ?, ?, ?)";
    $stmt = $con->prepare($sql);
    $stmt->execute([$courseID, $teacherUserID, $session, $section, $isRunning]);

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