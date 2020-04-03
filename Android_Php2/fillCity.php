<?php

$con = mysqli_connect("localhost","root","toor","androiddb") or die(mysqli_error());

$sql = "select * from tblcity";

$res = mysqli_query($con,$sql);

$result['city'] = array();

while($row = mysqli_fetch_array($res))
{
	$city = array();
	$city['cityID'] = $row[0];
	$city['cityName'] = $row[1];

	array_push($result['city'],$city);
}
echo json_encode($result);
?>