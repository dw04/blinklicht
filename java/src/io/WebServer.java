package io;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import main.ModuleManager;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import device.LEDcolor;
import device.OutputLED;

public class WebServer {
	ModuleManager manager;
	HttpServer server;
	boolean stopServer = false;
	
	Map<String, Integer> prepareParams(String path){
		String[] params=new String[]{};
		if(path.split("\\?").length>1)
			params=path.split("\\?")[1].split("&");
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("id", 0);
		result.put("r", 0);
		result.put("g", 0);
		result.put("b", 0);
		result.put("d", 0);
		for(String param : params){
			String[] parts = param.split("=");
			if(parts.length==2)
				result.put(parts[0], Integer.parseInt(parts[1]));
		}
		return result;
	}

	private void actionConstant(Map<String, Integer> params){		
		if(params.get("id")==0){
			for(OutputLED output : manager.getConnectionManager().getOutputLEDList()){
				if(output.getColor()==LEDcolor.RGB){
					output.sendRGB(params.get("r"), params.get("g"), params.get("b"));
				}else if(output.getColor()==LEDcolor.WHITE){
					output.sendRGB(params.get("d"), 0, 0);
				}
			}
		}else{
			OutputLED output = manager.getConnectionManager().getOutputLED(params.get("id"));
			if(output.getColor()==LEDcolor.RGB){
				output.sendRGB(params.get("r"), params.get("g"), params.get("b"));
			}else if(output.getColor()==LEDcolor.WHITE){
				output.sendRGB(params.get("d"), 0, 0);
			}
		}
	}
	
	private void actionFade(Map<String, Integer> params){
		
	}
	
	class Handler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String path = exchange.getRequestURI().toASCIIString();
			//System.out.print("new req: " + path + " -> ");
			String response;
			if (path.startsWith("/action")){
				String module="constant";
				if(path.split("\\/").length>2 && path.split("\\/")[2].split("\\?").length>0)
					module=path.split("\\/")[2].split("\\?")[0];
				//System.out.println("module: "+module);
				Map<String, Integer> params = prepareParams(path);
				if(module.equals("constant")) actionConstant(params);
				if(module.equals("fade")) actionFade(params);
				response = "ok";
			}
			else if (path.startsWith("/stop")) {
				response = "stopping server";
				stopServer = true;
			} else{
				StringBuilder text = new StringBuilder();
			    String nl = System.getProperty("line.separator");
			    Scanner scanner = new Scanner(new FileInputStream("resources/index.html"), "UTF-8");
			    try {
			    	while (scanner.hasNextLine())
			    		text.append(scanner.nextLine() + nl);
			    }
			    finally{
			    	scanner.close();
			    }
				response = text.toString()
						.replaceAll("<%currentR%>", ((Integer)manager.getConnectionManager().getOutputLED(1).getR()).toString())
						.replaceAll("<%currentG%>", ((Integer)manager.getConnectionManager().getOutputLED(1).getG()).toString())
						.replaceAll("<%currentB%>", ((Integer)manager.getConnectionManager().getOutputLED(1).getB()).toString());
			}
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public void start(int port, ConnectionManager connection) {
		manager=new ModuleManager(connection);
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
