<?php

$con = mysqli_connect("localhost","root","toor","androiddb") or die(mysqli_error());

$sql = "select s.*,c.cityName from tblstudent s,tblcity c where s.cityID = c.cityID";

$res = mysqli_query($con,$sql);

$result['stud'] = array();

while($row = mysqli_fetch_array($res))
{
	$stud = array();
	$stud['studentID'] = $row[0];
	$stud['studentName'] = $row[1];
	$stud['gender'] = $row[3];
	$stud['birthDate'] = $row[4];
	$stud['phoneNumber'] = $row[5];
	$stud['cityID'] = $row[6];
	$stud['cityName'] = $row[7];

	array_push($result['stud'],$stud);
}
echo json_encode($result);
?>