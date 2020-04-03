<?php

$response = array();
$con = mysqli_connect("localhost","root","toor","androiddb") or die(mysqli_error());

$studentID = $_REQUEST['studentID'];
$studentName = $_REQUEST['studentName'];
$password = $_REQUEST['password'];
$gender = $_REQUEST['gender'];
$birthDate = $_REQUEST['birthDate'];
$phoneNumber = $_REQUEST['phoneNumber'];
$cityID = $_REQUEST['cityID'];

$sql = "update tblstudent set studentName = '$studentName',password = '$password',gender = '$gender',birthDate = '$birthDate',phoneNumber = '$phoneNumber',cityID = '$cityID' where studentID = '$studentID'";

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