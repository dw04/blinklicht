<?php
require_once('serverconnection.php');
$conn = new ServerConnection('127.0.0.1',1811);
?>
<html>
<head>

		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
        <title>blinklicht</title>
        <script type="text/javascript" charset="utf-8">
        </script>
</head>
 <body style="background-color: #555658; color:#D5D6D7">

	<div style="text-align: center;">
		<?php
			if(isset($_GET['p'])){ 
				
				//Set Module
				if($_GET['p'] == 'fade'){
					$conn->setModule(Command_Module::FADE);

				}
				if($_GET['p'] == 'random'){
					$conn->setModule(Command_Module::RANDOM);

				}
				if($_GET['p'] == 'constant'){
					$conn->setModule(Command_Module::CONSTANT);

				}


				//Set Action
				if($_GET['b'] == 1){
					$conn->setAction(Command_ACTION::START);
				}
				else{
					$conn->setAction(Command_ACTION::STOP);
				}

				$conn->send();
				
			 
			}else if(isset($_GET['e'])){ //execute event
				if($_GET['e']== 'home'){  
					
					
				}else if($_GET['e']== 'leave'){
				
				}
			}
		?>


		</div>
</body>
</html>