<?php

    $timestamp = time();
    $filename = 'Active_Users_Report' . $timestamp . '.xls';
        
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
			    	<th>User Name</th>
					<th>Mobile Number</th>
					<th>WhatsApp Number</th>
					<th>Email ID</th>
					<th>Addres</th>
					<th>Village</th>
					<th>Tehsil</th>
                	<th>District</th>
                    <th>Survey Number</th>
                    <th>House Number</th>       
                    <th>Pincode</th>
				</tr>
			<tbody>
	";
	
	$query = $conn->query("SELECT `name`,`mobile`,`whatsapp_number`,`email`,`address`,`village`,`tehsil`,`district`,`survey_number`,`house_number`,`pincode` FROM `haritkandharapp_user` WHERE block_status='unblocked';") or die(mysqli_errno());
	while($fetch = $query->fetch_array()){
		
	$output .= "
				<tr>
				
			        <td>".$fetch['name']."</td>
					<td>".$fetch['mobile']."</td>
					<td>".$fetch['whatsapp_number']."</td>
					<td>".$fetch['email']."</td>
					<td>".$fetch['address']."</td>
					<td>".$fetch['village']."</td>
					<td>".$fetch['tehsil']."</td>
					<td>".$fetch['district']."</td>
					<td>".$fetch['survey_number']."</td>
					<td>".$fetch['house_number']."</td>
					<td>".$fetch['pincode']."</td>
				</tr>
	";
	}
	
	$output .="
			</tbody>
			
		</table>
	";
	
	echo $output;
	
	
?>