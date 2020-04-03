<?php

$response = array();
$con = mysqli_connect("localhost","root","toor","androiddb") or die(mysqli_error());

$studentID = $_REQUEST['studentID'];
$sql = "delete from tblstudent where studentID = '$studentID'";
$res = mysqli_query($con,$sql);

if($res)
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