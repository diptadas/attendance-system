<?php

include 'server_config.php';


$response["success"] = 0;
$response["key"] = array();
$response["data"] = array();

try
{
	$con = new PDO($dsn, $user_server, $pass_server);
	$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	$sql = "SELECT * FROM course";

	$stmt = $con->query($sql);

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