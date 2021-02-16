<!DOCTYPE html>
<html lang="en">
		<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1"/>
		<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
		
	</head>
<body>
	<div class="col-md-1"></div>
	<div class="col-md-6 well">
		<h3 class="text-primary"><center>Download Reports</center></h3>
		<hr style="border-top:1px dotted #ccc;"/>
		<br />
		<br />
		<div style="text-align:center">
		<a class="btn btn-info" href="plan_export_excel.php">Download All Users</a>
		<br></br>
		<br></br>
		<a class="btn btn-info" href="export_active_users.php">Download Active Users</a>
		<br></br>
		<br></br>
		<a class="btn btn-info" href="export_inactive_users.php">Download Inactive Users</a>
		<br></br>
		<br></br>
		<a class="btn btn-info" href="Plantation_Report.php">Download Plantation Report</a>
	</div>
	</div>

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