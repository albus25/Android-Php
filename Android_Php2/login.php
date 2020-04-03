<?php

$response = array();
$con = mysqli_connect("localhost","root","toor","androiddb") or die(mysqli_error());

$studentName = $_REQUEST['studentName'];
$password = $_REQUEST['password'];

$sql = "select * from tblstudent where studentName = '$studentName' and password = '$password'";

$res = mysqli_query($con,$sql);

if(mysqli_num_rows($res)>0)
{
	$response['success'] = 1;
	echo json_encode($response);
}
else
{
	$response['success'] = 0;
	echo json_encode($response);
}
?>