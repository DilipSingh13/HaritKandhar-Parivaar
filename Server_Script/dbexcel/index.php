<!DOCTYPE html>
<html lang="en">
		<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1"/>
		<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
	</head>
<body>
		<h3 class="text-primary"><center>View Users Records</center></h3>
		<a class="btn btn-info" href="plan_export_excel.php">Save as Excel</a>
		
		<br/>
		<br/>
		
		<table class="table table-bordered" id="myTable">
			<thead class="alert-info">
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
			</thead>
			<tbody>
				<?php
				require 'conn.php';
		/*		 $query = mysqli_query($conn, "SELECT * FROM `purchase`") or die(mysqli_error());
					while($fetch = mysqli_fetch_array($query))*/
	 $query = mysqli_query($conn, "SELECT * FROM haritkandharapp_user") or die(mysqli_error());
					while($fetch = mysqli_fetch_array($query)){
				?>
				<tr>
					<td><?php echo $fetch['name']?></td>
					<td><?php echo $fetch['mobile']?></td>
					<td><?php echo $fetch['whatsapp_number']?></td>
					<td><?php echo $fetch['email']?></td>
					<td><?php echo $fetch['address']?></td>
					<td><?php echo $fetch['village']?></td>
					<td><?php echo $fetch['tehsil']?></td>
					<td><?php echo $fetch['district']?></td>
					<td><?php echo $fetch['survey_number']?></td>
					<td><?php echo $fetch['house_number']?></td>
					<td><?php echo $fetch['pincode']?></td>

				</tr>
				<?php
					}
				?>
			</tbody>
			<tfoot>
				
			</tfoot>
		</table>

<script>
function myFunction() {
  var input, filter, table, tr, td, i, txtValue;
  input = document.getElementById("myInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName("tr");
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[0];
    if (td) {
      txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }       
  }
}
</script>

<script src="js/jquery-3.2.1.min.js"></script>	
<script src="js/bootstrap.js"></script>	
</body>	
</html>