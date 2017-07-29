package in.msruas.project.npeanalyzer.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.msruas.project.analysis.LineStore;

public class ExtractByteCode {
	String command,path,funcName;
	List s;
	LineStore ls=LineStore.getInstance();
	
	public boolean runjavap(List s){
		this.s=s;
		for(Object strobj:s){
			String str=(String)strobj;
			String[] str_array = str.split("\\$");
			path=str_array[0];
			funcName=str_array[1];
			path = path.replace('/', '\\');
		}
		command = "cd /d \"" + path.toString() + "\\bin\"";
		String command1 = "javap -c *.class > output.txt";
		System.out.println("command is : " + command + "---" + command1);
		try {
			File f=new File(path+"\\bin");
			Process process = Runtime.getRuntime().exec(command1,null,f);
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()+"\\output.txt"));
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				bw.write(line +"\n");
				
			}
			bw.flush();
			int exitValue = process.waitFor();
//			process = Runtime.getRuntime().exec(command1);
//			in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			while ((line = in.readLine()) != null) {
//				System.out.println(line);
//			}
//			exitValue = process.waitFor();
			if (exitValue != 0) {
				//System.out.println("*** Something Went Wrong :( !!! Abnormal process termination ***");

				return true;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
	
	public void runLineNumberTableCmd() throws IOException, InterruptedException{
		String command1 = "javap -l *.class > line.txt";
		File f=new File(path+"\\bin");
		Process process = Runtime.getRuntime().exec(command1,null,f);
		BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = null;
		BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()+"\\line.txt"));
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			bw.write(line +"\n");
		}
		bw.flush();
		int exitValue = process.waitFor();
		if (exitValue != 0) {
			System.out.println("Line number table extraction failed");
			
		}
		
		
	}
	
	public void extractLineNumberTable() throws IOException{
		boolean writeToFile=false;
		for(Object strobj:s){
			String str=(String)strobj;
			String[] str_array = str.split("\\$");
			path=str_array[0];
			funcName=str_array[1];
			path = path.replace('/', '\\');
			File f=new File(path+"\\bin");
			FileReader reader = new FileReader(f.getAbsolutePath()+"\\line.txt");
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line;
			
			FileWriter writer = new FileWriter(f.getAbsolutePath()+"\\"+funcName+"lineNo.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			 
	        while ((line = bufferedReader.readLine()) != null) {
	        	line=line.replace(": ", ":").trim();
	            if(line.contains(funcName+"()")){
	            	writeToFile=true;
	            	continue;
	            }
	            if(line.contains("LocalVariableTable")){
	            	writeToFile=false;
	            }
	            if(line.contains("line")){
	            	if(writeToFile){
	            		            	      
	            	      line= line.replace("line", "");
	            	      line=line.trim();            	      	            	      
	            	    
	            		bufferedWriter.write(line + "\n");
	            		String[] line_array = line.split(":");
	            		ls.addLineEntry(f.getAbsolutePath()+"\\"+funcName+".txt",Integer.parseInt(line_array[0]),Integer.parseInt(line_array[1]));
		            }
	            }
	            
	        }
	        reader.close();
	        bufferedWriter.flush();
	        bufferedWriter.close();
		}
	}
	
	public void getMethodByteCodes() throws IOException{
		boolean writeToFile=false;
		for(Object strobj:s){
			String str=(String)strobj;
			String[] str_array = str.split("\\$");
			path=str_array[0];
			funcName=str_array[1];
			path = path.replace('/', '\\');
			File f=new File(path+"\\bin");
			FileReader reader = new FileReader(f.getAbsolutePath()+"\\output.txt");
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line;
			
			FileWriter writer = new FileWriter(f.getAbsolutePath()+"\\"+funcName+".txt");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			 
	        while ((line = bufferedReader.readLine()) != null) {
	        	line=line.replace(":", " : ").trim();
	            if(line.contains(funcName+"()")){
	            	writeToFile=true;
	            	continue;
	            }
	            if(line.contains("Exception table")){
	            	writeToFile=false;
	            }
	            if(line.contains("Code :")){
	            	continue;
	            }
	            if(writeToFile){
	            	bufferedWriter.write(line + "\n");
	            }
	        }
	        reader.close();
	        bufferedWriter.flush();
	        bufferedWriter.close();
		}
		
		
	}
}
