package io;
import gnu.io.SerialPortEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import protobuf.Commands;
import protobuf.Commands.Command;

import main.TaskManager;


import com.google.protobuf.*;

/**
 * This class handles a single Client connection
 * @author danielwilbers
 *
 */
class Connection implements Runnable{

	Socket socket;
	
	public Connection(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		try {
	
			while(socket.isConnected()){
				if(socket.getInputStream().available() > 0){
					CodedInputStream inStream = CodedInputStream.newInstance(socket.getInputStream());
					Command in = Command.parseFrom(inStream); 
				//	TaskManager.executeCommand(in);
					System.err.println("Currently socket input does nothing");
					//System.out.println("Protobuf input: " + in.getAction() + " " + in.getModule());
					socket.close();
					break;
				}
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	
		
	}
	
}

/**
 * 
 * @author danielwilbers
 *
 */
public class ProtobufInput implements Runnable{

	private int maximumConnections = 3;
	private int currentConnections = 0;
	private ServerSocket server;
	
	
	public  ProtobufInput(){
		try {
			server = new ServerSocket(1811);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		//Command c = Command.newBuilder().setModule(Command.Module.FADE).setAction(Command.Action.START).build();
		//System.out.println(c.toByteArray());
		
		while(true){
			if(currentConnections <= maximumConnections){
				try {
					Socket socket = server.accept();
					
					Connection singleCon = new Connection(socket);
					Thread t = new Thread(singleCon);
					t.start();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		
	}

	
	
}
