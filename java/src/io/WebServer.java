package io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import main.ModuleManager;
import modules.ModuleLED;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import device.LEDcolor;
import device.OutputLED;
import device.OutputOutlet;
import device.OutputRadio;

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
			System.out.println("Request: "+path);
			
			//check if a file is requested
			File f = new File("resources/www-root/"+ path.substring(1, path.length()));
			if(!path.contains("..") && !path.contains("//") && f.exists() && f.isFile()){
				InputStream file = new FileInputStream(f);
				
				OutputStream os = exchange.getResponseBody();
				
				exchange.sendResponseHeaders(200, f.length());
			
				try {
		            byte[] buffer = new byte[1000];
		            while (file.available()>0) 
		                os.write(buffer, 0, file.read(buffer));
		        } catch (IOException e) { System.err.println(e); }
				
				os.close();
				
				return;
			}
			
			if(path.startsWith("/radio-action/")){ 
				String[] split = path.split("\\=");
				String todo = split[1];
				String device = split[0].split("\\/")[2];		
				System.out.println("device:" + device + " todo:" + todo);
				manager.radioAction(device,todo);
				response = "";
			}	
			else if (path.startsWith("/action")){
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
			}
			else if (path.startsWith("/apple-touch-icon.png")) {
				
				String url ="resources/apple-touch-icon.png";
				
				
				//FileInputStream image   = new FileInputStream(url);
				BufferedImage image = ImageIO.read(new File(url));   //better just send file here
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, "png", baos);
				
				byte[] imageInBytes = baos.toByteArray();
				
				exchange.sendResponseHeaders(200, imageInBytes.length);
				OutputStream os = exchange.getResponseBody();
				os.write(imageInBytes);
				os.close();
				
		        return;
			}
			else {
				response = replaceTokens2(template);
			}
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		private String guessContentType(String path)
	    {
	        if (path.endsWith(".html") || path.endsWith(".htm")) 
	            return "text/html";
	        else if (path.endsWith(".txt") || path.endsWith(".java")) 
	            return "text/plain";
	        else if (path.endsWith(".gif")) 
	            return "image/gif";
	        else if (path.endsWith(".class"))
	            return "application/octet-stream";
	        else if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
	            return "image/jpeg";
	        else if (path.endsWith(".js"))
	            return "text/javascript";
	        else    
	            return "text/plain";
	    }
		
		private StringBuffer wrapLEDModule(StringBuffer buffer, String name, String content, LEDcolor[] c){
		
			//this is the module representation on the home screen
			String module =  "<li class=\"arrow\">" + "<a href=\"#"+name+"\" style>"+ name +"</a></li>";
			
			String output = ""; //= "<li><input type=\"checkbox\" name=\"food\" value=\"pie\" checked=\"checked\" title=\"Pie\"></li>";
			for(LEDcolor col : c){
				for(OutputLED o : manager.getConnectionManager().outputLEDList){
					if(o.getColor().equals(col)){
						output+= "<li><input type=\"checkbox\" name=\""+o.getID()+"\" value=\""+o.getID()+"\" checked=\"checked\" title=\""+o.getID()+"\"></li>";
					}
					
				}
			}
				
			
			//this is the div for the specific page
			String moduleClass = "<div id=\""+name+"\">" +
									"<div class=\"toolbar\"> " +
										"<a class=\"back\" href=\"#\" style=\"\">Home</a>" +
									"</div>" +
									"<div class=\"scroll\">" +
										"<h2>"+name+"</h2>" +
										"<ul class=\"edit rounded\">"+content+"</ul>"+
										"<ul class=\"edit rounded\">"+output+"</ul>"+
									"</div>" +
								"</div>";
			
			//template=template.replaceAll("<%modules%>", module);
			buffer.insert(buffer.indexOf("<-modules->"),module);
			buffer.insert(buffer.indexOf("<-moduleclasses->"),moduleClass);
			
		return buffer;
		}
		
		private String addColorSlider(String moduleName, Color color){
			String rgb = Integer.toHexString(color.getRGB());
			rgb = rgb.substring(2, rgb.length());
	
			String identifier = moduleName +rgb;
			String result=  "<div id=\"slider-container\">" +
					"<div style=\"background-color: #"+rgb+"\" id=\"chosen"+identifier+"\" class=\"chosen\">255</div>" +
					"<div id=\"slider"+identifier+"\" class=slider>0 <input id=\"slide"+identifier+"\" type=\"range\"min=\"0\" max=\"255\" step=\"1\" value=\"255\"onchange=\"updateSlider(this.value,'chosen"+identifier+"')\" />255" +
					"</div></div>";
			
			return result;
		}
		
		private String addIntervalSlider(String moduleName, int min, int max){
			String identifier = moduleName;
			String result=  "<div id=\"slider-container\">" +
					"<div style=\"background-color: #000000\" id=\"chosen"+identifier+"\" class=\"chosen\">"+min+"</div>" +
					"<div id=\"slider"+identifier+"\" class=slider>"+min+" <input id=\"slide"+identifier+"\" type=\"range\"min=\""+min+"\" max=\""+max+"\" step=\"1\" value=\"0\"onchange=\"updateSlider(this.value,'chosen"+identifier+"')\" />"+max+"   in ms"+
					"</div></div>";
			return result;
		}
		
		
		private StringBuffer addConstantColor(StringBuffer buffer){
			String name = "Constant";
			String content = "";
			
			content+=addColorSlider(name, Color.RED);
			content+=addColorSlider(name, Color.GREEN);
			content+=addColorSlider(name, Color.BLUE);
			
			LEDcolor[] supported = {LEDcolor.RGB};
			wrapLEDModule(buffer, name, content, supported);
			return buffer;
		}
		
		private StringBuffer addFade(StringBuffer buffer){
			String name = "Fade";
			String content = "";
			
			content+=addIntervalSlider(name, 500, 10000);
			
			LEDcolor[] supported = {LEDcolor.RGB,LEDcolor.WHITE};
			wrapLEDModule(buffer, "Fade", content, supported);
			return buffer;
		}
		
		private StringBuffer addRandom(StringBuffer buffer){
			String name = "Random";
			String content = "";
			
			content+=addIntervalSlider(name, 500, 20000);
			
			LEDcolor[] supported = {LEDcolor.RGB};
			wrapLEDModule(buffer, "Random", content,supported);
			return buffer;
		}
		
		private StringBuffer addRadioDevices(StringBuffer buffer){
			
			String switchTemplate = "";
			
			for(OutputRadio o: manager.getConnectionManager().getOutputRadioList() ){
				
				switchTemplate = "<li>"+o.getIdentifier()+"<span class=\"device toggle\" id=\""+o.getIdentifier()+"\"><input type=\"checkbox\"></span></li>";
				int i = buffer.indexOf("<-radiodevices->");
				buffer.insert(i,switchTemplate);
				
			}
			
			return buffer;
			
		}
		
		
		
		private String replaceTokens2(String template){
			StringBuffer buffer = new StringBuffer(template);
			
//		
			buffer=addConstantColor(buffer);
			buffer=addFade(buffer);
			buffer=addRandom(buffer);	
			buffer=addRadioDevices(buffer);
			
		
			String newTemplate = buffer.toString();
			
//			LinkedList<String> temp = new LinkedList<String>();
//			temp.add("<-radiodevices->");
//			temp.add("<-modules->");
//			temp.add("<-moduleclasses->");
//			
//			for(String s: temp){
//				buffer.delete(buffer.indexOf(s), s.length());
//			}
		
			
			
			newTemplate=newTemplate.replaceAll("<-modules->", "");
			newTemplate=newTemplate.replaceAll("<-moduleclasses->", "");
			newTemplate=newTemplate.replaceAll("<-radiodevices->", "");
			
			
			return newTemplate;
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
		    Scanner scanner = new Scanner(new FileInputStream("resources/www-root/index.html"), "UTF-8");
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
