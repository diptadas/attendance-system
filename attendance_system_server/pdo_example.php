<?php

   include 'server_config.php';

   $response["success"] = 0;
   $response["data"] = array();
   
try{
    $con = new PDO($dsn, $user_server, $pass_server);
    $con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    $sql = "SELECT user_name FROM user";
    $stmt = $con->query($sql);
    
    while($row = $stmt->fetch())
    {
        $single_info = array();
        $single_info["userName"] = $row["user_name"];   
        array_push($response["data"],$single_info);
    }
    
    $userType = 1;
    $sql = "SELECT user_name FROM user WHERE user_type = ?";
    $stmt = $con->prepare($sql);
    $stmt->execute([$userType]);
    
    while($row = $stmt->fetch())
    {
        $single_info = array();
        $single_info["userName"] = $row["user_name"];   
        array_push($response["data"],$single_info);
    }
    
    $response["success"] = 1;
    
}catch(PDOException $e)
{
    $response["success"] = 0;
}
 
echo json_encode($response);
   
?>