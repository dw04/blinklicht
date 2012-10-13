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
	private ModuleManager manager;
	private HttpServer server;
	private String template;
	private boolean stopServer = false;
	
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
				manager.action(params.get("id"), module, params);
				response = "";
			}
			else if (path.startsWith("/stop")) {
				response = "stopping server";
				stopServer = true;
			} else {
				response = replaceTokens(template);
			}
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		private String replaceTokens(String template) {
			String outputswitch = "";
			String slider0 = "";
			String sliderX = "";
			String scriptActivateOutput = "";
			String scriptColorEvent = "";
			int slider0r = 0,slider0g = 0,slider0b = 0,slider0d = 0;
			for(OutputLED out : manager.getConnectionManager().getOutputLEDList()){
				int i = out.getID();
				outputswitch+="<input type=\"checkbox\" name=\"checkbox-"+i+"\" id=\"checkbox-"+i+"\" class=\"custom\" checked=\"checked\" /><label for=\"checkbox-"+i+"\">"+i+" ("+manager.getModule(i).getName()+")</label>\n";
				sliderX+="<div data-content-theme=\"c\" data-role=\"collapsible\"><h3><div class=\"outputdrop\" data-role=\"fieldcontain\">\n";
				sliderX+="<label class=\"outputdroptext\" for=\"flip-"+i+"\">Output "+i+"</label><select name=\"slider"+i+"\" id=\"flip-"+i+"\" class=\"sliderConstant\" data-role=\"slider\">\n";
				sliderX+="<option value=\"off\">Off</option><option value=\"on\"";
				if(!manager.getModule(i).getName().equals("none"))
					sliderX+=" selected=\"selected\"";
				sliderX+=">On</option></select></div></h3><p>\n";
				scriptActivateOutput+="if(\\$('#checkbox-"+i+"').attr(\"checked\")==\"checked\") \\$('#flip-"+i+"').val(\\$(this).val()).slider(\"refresh\");\n";
				if(out.getColor()==LEDcolor.RGB){
					slider0r=out.getR();
					slider0g=out.getG();
					slider0b=out.getB();
					sliderX+="<div id=\"constant"+i+"rb\" class=\"control-r\"><input type=\"range\" name=\"slider\" id=\"constant"+i+"r\" class=\"slider-r\" value=\""+out.getR()+"\" min=\"0\" max=\"255\" data-highlight=\"true\" /></div>\n";
					sliderX+="<div id=\"constant"+i+"gb\" class=\"control-g\"><input type=\"range\" name=\"slider\" id=\"constant"+i+"g\" class=\"slider-g\" value=\""+out.getG()+"\" min=\"0\" max=\"255\" data-highlight=\"true\" /></div>\n";
					sliderX+="<div id=\"constant"+i+"bb\" class=\"control-b\"><input type=\"range\" name=\"slider\" id=\"constant"+i+"b\" class=\"slider-b\" value=\""+out.getB()+"\" min=\"0\" max=\"255\" data-highlight=\"true\" /></div>\n";
					scriptColorEvent+="\\$( \"#constant"+i+"rb\" ).on( 'slidestop', function( event ) { updateColorSlider("+i+"); });";
					scriptColorEvent+="\\$( \"#constant"+i+"gb\" ).on( 'slidestop', function( event ) { updateColorSlider("+i+"); });";
					scriptColorEvent+="\\$( \"#constant"+i+"bb\" ).on( 'slidestop', function( event ) { updateColorSlider("+i+"); });";
				}else if(out.getColor()==LEDcolor.WHITE){
					slider0d=out.getR();
					sliderX+="<div id=\"constant"+i+"db\" class=\"control-d\"><input type=\"range\" name=\"slider\" id=\"constant"+i+"d\" class=\"slider-d\" value=\""+out.getR()+"\" min=\"0\" max=\"255\" data-highlight=\"false\" /></div>\n";
					scriptColorEvent+="\\$( \"#constant"+i+"db\" ).on( 'slidestop', function( event ) { updateColorSlider("+i+"); });";
				}
				sliderX+="</p></div>\n";
			}
			if(manager.hasRGBmodule()){
				slider0+="<tr><td colspan=\"2\"><div id=\"constant0rb\" class=\"control-r\"><input type=\"range\" name=\"slider\" id=\"constant0r\" class=\"slider-r\" value=\""+slider0r+"\" min=\"0\" max=\"255\" data-highlight=\"true\" /></div></td></tr>\n";
				slider0+="<tr><td colspan=\"2\"><div id=\"constant0gb\" class=\"control-g\"><input type=\"range\" name=\"slider\" id=\"constant0g\" class=\"slider-g\" value=\""+slider0g+"\" min=\"0\" max=\"255\" data-highlight=\"true\" /></div></td></tr>\n";
				slider0+="<tr><td colspan=\"2\"><div id=\"constant0bb\" class=\"control-b\"><input type=\"range\" name=\"slider\" id=\"constant0b\" class=\"slider-b\" value=\""+slider0b+"\" min=\"0\" max=\"255\" data-highlight=\"true\" /></div></td></tr>\n";
				scriptColorEvent+="\\$( \"#constant0rb\" ).on( 'slidestop', function( event ) { updateColorSlider(0); });";
				scriptColorEvent+="\\$( \"#constant0gb\" ).on( 'slidestop', function( event ) { updateColorSlider(0); });";
				scriptColorEvent+="\\$( \"#constant0bb\" ).on( 'slidestop', function( event ) { updateColorSlider(0); });";
			}
			if(manager.hasWhiteModule()){
				slider0+="<tr><td colspan=\"2\"><div id=\"constant0db\" class=\"control-d\"><input type=\"range\" name=\"slider\" id=\"constant0d\" class=\"slider-d\" value=\""+slider0d+"\" min=\"0\" max=\"255\" data-highlight=\"false\" /></div></td></tr>\n";
				scriptColorEvent+="\\$( \"#constant0db\" ).on( 'slidestop', function( event ) { updateColorSlider(0); });";
			}
			template=template.replaceAll("<%outputswitch%>", outputswitch);
			template=template.replaceAll("<%slider0%>", slider0);
			template=template.replaceAll("<%sliderX%>", sliderX);
			template=template.replaceAll("<%scriptActivateOutput%>", scriptActivateOutput);
			template=template.replaceAll("<%scriptColorEvent%>", scriptColorEvent);
			return template;
		}
	}

	public void start(int port, ConnectionManager connection) {
		manager=new ModuleManager(connection);
		try {
			StringBuilder text = new StringBuilder();
		    String nl = System.getProperty("line.separator");
		    Scanner scanner = new Scanner(new FileInputStream("resources/index2.html"), "UTF-8");
		    try {
		    	while (scanner.hasNextLine())
		    		text.append(scanner.nextLine() + nl);
		    }
		    finally{
		    	scanner.close();
		    }
			template = text.toString();
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
