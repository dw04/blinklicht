<?php
//error_reporting(E_ALL);

require_once('protocolbuf/message/pb_message.php');
require_once('protocolbuf/clientcommands/pb_proto_commands.php');

$com = new Command();
$com->set_module(Command_Module::FADE);
$com->set_action(Command_Action::STOP);

$serialized_string = $com->SerializeToString();


/* Den Port für den WWW-Dienst ermitteln. */
$service_port = 1811;

/* Die  IP-Adresse des Zielrechners ermitteln. */
$address = '192.168.2.88';

$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
echo "Creating Socket ... <br>";
if ($socket === false) {
    echo "socket_create() fehlgeschlagen: Grund: " . socket_strerror(socket_last_error()) . "\n <br>";
} else {
    echo "OK.\n <br>";
}

echo "Versuche, zu '$address' auf Port '$service_port' zu verbinden ... <br>";
$result = socket_connect($socket, $address, $service_port);
if ($result === false) {
    echo "socket_connect() fehlgeschlagen.\nGrund: ($result) " . socket_strerror(socket_last_error($socket)) . "\n <br>";
} else {
    echo "OK.\n <br>";
}

echo "Sending serialized string... <br>";
socket_write($socket, $serialized_string, strlen($serialized_string));
echo "OK.\n <br>";

echo "Socket schließen ... <br>";
socket_close($socket);
echo "OK.\n\n <br>";


?>
