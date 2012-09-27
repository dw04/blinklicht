<?php
/**
*@author Daniel Wilbers
*/

require_once('protocolbuf/message/pb_message.php');
require_once('protocolbuf/clientcommands/pb_proto_commands.php');

class ServerConnection
{
	
	public $address;
	public $port;

	function __construct($address, $port){
		$this->address = $address;
		$this->port = $port;
	}

	public function sendCommand($module, $action)
	{


		$com = new Command();
		$com->set_module($module);
		$com->set_action($action);

		$serialized_string = $com->SerializeToString();

		$address = '192.168.2.88';

		$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
		echo "Creating Socket ... <br>";
		if ($socket === false) {
		    echo "socket_create() fehlgeschlagen: Grund: " . socket_strerror(socket_last_error()) . "\n <br>";
		} else {
		    echo "OK.\n <br>";
		}

		echo "Versuche, zu '$address' auf Port '$service_port' zu verbinden ... <br>";
		$result = socket_connect($socket, $this->address, $this->port);
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

	}
}

?>
