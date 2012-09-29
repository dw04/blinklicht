package io;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import clientcommands.Commands;
import clientcommands.Commands.Command;

import com.google.protobuf.*;

public class ClientInput implements Runnable{

	@Override
	public void run() {
		
		Command c = Command.newBuilder().setModule(Command.Module.FADE).setAction(Command.Action.START).build();
		
		System.out.println(c.toByteArray());
//		try {
//			FileOutputStream f = new FileOutputStream("test.log");
//			c.writeTo(f);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String message;
		
		try {
			ServerSocket server = new ServerSocket(1811);
			while(true){
				Socket socket = server.accept();
				
				
					BufferedReader  fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					CodedOutputStream coded = CodedOutputStream.newInstance(socket.getOutputStream());
					//while(socket.isConnected()){
						
						while( fromClient.ready()){
							System.out.println(socket.getInputStream().available());
							Command in = Command.parseFrom(socket.getInputStream());
							System.out.println(in.getAction() + " " + in.getModule());
							//message = fromClient.readLine();
							//System.out.println("Received: " + message);
							//out.writeChars("Test1");
							//out.writeChars(c.toByteArray().toString());
						//	socket.
							//c.writeTo(coded);
							//coded.flush();
							//out.writeBytes(c.toString());
							//out.writeChars("Test2");
						}		
					//}
				
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	
}
