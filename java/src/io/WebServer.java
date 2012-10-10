package io;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
	ConnectionManager conManager;
	HttpServer server;
	boolean stopServer = false;

	private void actionConstant(String[] params){
		int d=0;
		int r=0;
		int g=0;
		int b=0;
		for(String param : params){
			String[] parts = param.split("=");
			if(parts.length==2){
				if(parts[0].equals("d"))
					d=Integer.parseInt(parts[1]);
				if(parts[0].equals("r"))
					r=Integer.parseInt(parts[1]);
				if(parts[0].equals("g"))
					g=Integer.parseInt(parts[1]);
				if(parts[0].equals("b"))
					b=Integer.parseInt(parts[1]);
			}
		}
		conManager.getOutputLED(d).sendRGB(r, g, b);
	}
	
	class Handler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String path = exchange.getRequestURI().toASCIIString();
			System.out.print("new req: " + path + " -> ");
			// InputStream is = exchange.getRequestBody();
			// is.read();
			String response;
			if (path.startsWith("/action")){
				String module="constant";
				if(path.split("\\/").length>2 && path.split("\\/")[2].split("\\?").length>0)
					module=path.split("\\/")[2].split("\\?")[0];
				System.out.println("module: "+module);
				String[] params=new String[]{};
				if(path.split("\\?").length>1)
					params=path.split("\\?")[1].split("&");
				if(module.equals("constant"))
					actionConstant(params);
				response = "ok";
			}
			else if (path.startsWith("/stop")) {
				response = "stopping server";
				stopServer = true;
			} else
				response = "main page";
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public void start(int port, ConnectionManager conManager) {
		this.conManager=conManager;
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/", new Handler());
			server.setExecutor(null);
			server.start();
			System.out.println("Server running on port " + port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		server.stop(500);
	}
}
