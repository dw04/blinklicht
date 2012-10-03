<?php
function templateparser($templatefile,$values) {
	foreach($values as $key => $value) { 
		$searchpattern = "/<%(".strtolower($key).")%>/si";
		$templatefile = preg_replace($searchpattern, $value, $templatefile); 
	}
	//remove not used tokens from template
	$templatefile = preg_replace("/((<%)(.+?)(%>))/si", '', $templatefile);
	return $templatefile;
}

require_once('serverconnection.php');
$conn = new ServerConnection('192.168.2.88',1811);
$template = implode(file('template/main.html'));
$values = array(
	'content' => '',
	'nav0active' => '',
	'nav1active' => '',
	'nav2active' => '',
	'nav3active' => '',
	'nav4active' => '',
	'nav5active' => '',
	'nav6active' => ''
	);
if(isset($_GET['page']))
	$page=$_GET['page'];
else
	$page='home';
if($page=='settings'){
	$values['nav0active']=' class="ui-btn-active"';
}
if($page=='home'){
	$values['nav1active']=' class="ui-btn-active"';
}
if($page=='constant'){
	$values['content']=implode(file('template/constant.html'));
	$values['nav2active']=' class="ui-btn-active"';
	$conn->setModule(Command_Module::CONSTANT);
	$conn->setAction(Command_ACTION::START);
	$conn->send();
}
if($page=='fade'){
	$values['nav3active']=' class="ui-btn-active"';
	$conn->setModule(Command_Module::FADE);
	$conn->setAction(Command_ACTION::START);
	$conn->send();
}
if($page=='random'){
	$values['nav4active']=' class="ui-btn-active"';
	$conn->setModule(Command_Module::RANDOM);
	$conn->setAction(Command_ACTION::START);
	$conn->send();
}
if($page=='mouse'){
	$values['nav5active']=' class="ui-btn-active"';
}
if($page=='off'){
	$values['nav6active']=' class="ui-btn-active"';
}
print(templateparser($template,$values));
?>