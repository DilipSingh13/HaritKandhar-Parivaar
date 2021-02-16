<?php

    $timestamp = time();
    $filename = 'Plantation_Report_' . $timestamp . '.xls';
        
    header("Content-Type: application/vnd.ms-excel");
    header("Content-Disposition: attachment; filename=\"$filename\"");

	header("Pragma: no-cache"); 
	header("Expires: 0");

	require_once 'conn.php';
	
	$output = "";
	
	$output .="
		<table>
			<thead>
				<tr>
				    <th>Unique Tree Code</th>
			    	<th>User Name</th>
					<th>Email ID</th>
					<th>Mobile Number</th>
					<th>Longitude</th>
					<th>Latitude</th>
					<th>Tree Name</th>
					<th>Month Slot</th>
					<th>Tree Picture Name</th> 
					<th>Date</th>
                	<th>Time</th>
				</tr>
			<tbody>
	";
	
	$query = $conn->query("SELECT * FROM `Approve_Plant_Picture` WHERE approve_status='approved';") or die(mysqli_errno());
	while($fetch = $query->fetch_array()){
		
	$output .= "
				<tr>
				
			        <td>".$fetch['Unique_code']."</td>
					<td>".$fetch['name']."</td>
					<td>".$fetch['email']."</td>
					<td>".$fetch['mobile']."</td>
					<td>".$fetch['longitude']."</td>
					<td>".$fetch['latitudes']."</td>
					<td>".$fetch['tree_name']."</td>
					<td>".$fetch['month']."</td>
					<td>".$fetch['plant_picture']."</td>
					<td>".$fetch['date']."</td>
					<td>".$fetch['time']."</td>
				</tr>
	";
	}
	
	$output .="
			</tbody>
			
		</table>
	";
	
	echo $output;
	
	
?>