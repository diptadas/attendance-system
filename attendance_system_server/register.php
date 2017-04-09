<?php
include 'server_config.php';

$userType = $_GET['userType'];
$userName = $_GET['userName'];
$studentID = $_GET['studentID'];
$fullName = $_GET['fullName'];
$dept = $_GET['dept'];
$section = $_GET['section'];
$designation = $_GET['designation'];
$email = $_GET['email'];
$password = $_GET['password'];

$userID = 0; //auto increment
$isAdmin = 0;

$response["success"] = 0;
$response["key"] = array("status");
$response["data"] = array();

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) //Invalid email format
{
	$response["success"] = 1;
	$single_info = array();
	$single_info["status"] = "Invalid email format";
	array_push($response["data"], $single_info);
	goto END;
}

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
	$sql = "SELECT * FROM user_info WHERE user_name = ?";
	$stmt = $con->prepare($sql);
	$stmt->execute([$userName]);
	while ($row = $stmt->fetch()) 
	{
		$response["success"] = 1;
		$single_info = array();
		$single_info["status"] = "User name not available.";
		array_push($response["data"], $single_info);
		goto END;
	}

	if ($userType == "1")
	{
        $sql = "SELECT * FROM user_info WHERE student_id = ?";
        $stmt = $con->prepare($sql);
        $stmt->execute([$studentID]);
        while ($row = $stmt->fetch()) 
        {
            $response["success"] = 1;
            $single_info = array();
            $single_info["status"] = "Student ID already registered.";
            array_push($response["data"], $single_info);
            goto END;
        }
	}

	$sql = "INSERT INTO user_info VALUES(?, ?, ?, ?, ?, ?, ?, (select dept_id from department where dept_name = ?), ?, ?, ?)";
    $stmt = $con->prepare($sql);
    $stmt->execute([$userID, $userName, $fullName, $email, $password, $userType, $isAdmin, $dept, $studentID, $section, $designation]);

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