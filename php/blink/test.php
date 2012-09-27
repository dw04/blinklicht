<?php
require_once('serverconnection.php');

$conn = new ServerConnection('192.168.2.88',1811);
$conn->sendCommand(Command_Module::FADE,Command_ACTION::START);

?>
