package in.msruas.project;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

public class NPAnalyzer {
	private static NPAnalyzer instance = null;
	
	Table<String,Integer, String> nullTable = HashBasedTable.create();
	Table<String,Integer, String> drefTable = HashBasedTable.create();
	
	 CodeStore cs=CodeStore.getInstance();
	 HappensBefore hb=HappensBefore.getInstance();
	 Nodes nodes=Nodes.getInstance();

	private NPAnalyzer() {
		
	}
	
	public static NPAnalyzer getInstance() {
	      if(instance == null) {
	         instance = new NPAnalyzer();
	      }
	      return instance;
	   }
	
	public void storeNullAssign(String fileName,int codeLine,String fieldId){
		nullTable.put(fileName, codeLine,fieldId);
	}
	
	public void storeDref(String fileName,int codeLine,String fieldId){
		drefTable.put(fileName, codeLine,fieldId);
	}
	
	public void printNull(){
		for (Cell<String, Integer, String> cell: nullTable.cellSet()){
		    System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
		}
		
		System.out.println("printing dereferences");
		for (Cell<String, Integer, String> cell: drefTable.cellSet()){
		    System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
		}
			
			
			
			
	}

	
	public void analyzeNull(){
		boolean nullkey=false;
		Table<String,Integer, String> codeTable=cs.getCodeTable();
		for (Cell<String, Integer, String> cell: codeTable.cellSet()){
		   // System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
			
			if (nullkey) {
				String str = cell.getValue();

				Pattern p = Pattern.compile("#([0-9]{2})");
				Matcher m = p.matcher(str);
				if (m.find()) {
					String fstr=m.group(1);
					this.storeNullAssign(cell.getRowKey(),cell.getColumnKey(),(m.group(1)));
					System.out.println("assignment of null value: "+cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue() );
					
				}
				
				nullkey=false;
			}
			
			if((cell.getValue()).contains("aconst_null")){
				nullkey=true;
			}
			
		}
		
	}
	
	public void analyzeDeref(){
		Table<String,Integer, String> codeTable=cs.getCodeTable();
		for (Cell<String, Integer, String> cell: codeTable.cellSet()){
		   // System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
			
			
				String str = cell.getValue();
				if(str.contains("getfield")||str.contains("getstatic")){
					for (Cell<String, Integer, String> cell1 : nullTable.cellSet()){
					    if(str.contains(cell1.getValue())){
					    	this.storeDref(cell.getRowKey(), cell.getColumnKey(), cell1.getValue());
					    	System.out.println("dereference of value: "+cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue() );
					    }
					}							
				
			}
			
			
			}
			
		
	}
	
	public void findNPE(){
		
		Integer source,sink;
		String sourceBlock,sinkBlock;
		for (Cell<String, Integer, String> sourceCell: nullTable.cellSet()){
			
		     source=sourceCell.getColumnKey();
		    for (Cell<String, Integer, String> sinkCell: drefTable.cellSet()){
		    	
			     sink=sinkCell.getColumnKey();
			    sourceBlock=nodes.findNode(source,sourceCell.getRowKey());
			    sinkBlock=nodes.findNode(sink,sinkCell.getRowKey());
			    if(hb.checkHB(sourceBlock, sinkBlock)){
			    	System.out.println("possible null pointer flow:");
			    	System.out.println("source: "+sourceCell.getRowKey()+" "+sourceCell.getColumnKey()+" "+sourceCell.getValue());
			    	System.out.println("sink: "+sinkCell.getRowKey()+" "+sinkCell.getColumnKey()+" "+sinkCell.getValue());
			    	System.out.println("--------------------");
			    }
			}
		}
		
	}
	
	
}
