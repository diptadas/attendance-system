<?php

include 'server_config.php';

$response["success"] = 0;
$response["key"] = array();
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
	$sql = "SELECT * FROM user_info JOIN department ON user_info.dept_id = department.dept_id";
    
    if(isset($_GET['userID'])) 
    {
        $userID = $_GET['userID'];
        $sql = "SELECT * FROM user_info JOIN department ON user_info.dept_id = department.dept_id WHERE user_id = ?";
        $stmt = $con->prepare($sql);
	    $stmt->execute([$userID]);
    }
    else if(isset($_GET['userType']))
    {
        $userType = $_GET['userType'];
        $sql = "SELECT * FROM user_info JOIN department ON user_info.dept_id = department.dept_id WHERE user_type = ? ORDER BY student_id";
        $stmt = $con->prepare($sql);
	    $stmt->execute([$userType]);
    }
    else if(isset($_GET['userName']))
    {
        $userName = $_GET['userName'];
        $password = $_GET['password'];
        $sql = "SELECT * FROM user_info JOIN department ON user_info.dept_id = department.dept_id WHERE user_name = ? AND password = ?";
        $stmt = $con->prepare($sql);
	    $stmt->execute([$userName, $password]);
    }
    else
    {
        $stmt = $con->query($sql);
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