package in.msruas.project.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

public class NPAnalyzer1 {
	private static NPAnalyzer1 instance = null;
	
	Table<String,Integer, String> nullTable = HashBasedTable.create();
	Table<String,Integer, String> drefTable = HashBasedTable.create();
	Table<String,Integer,String> intermediateWriteTable=HashBasedTable.create();
	ListMultimap<String, Integer> intermediateWriteMap = ArrayListMultimap.create();
	
	
	 CodeStore cs=CodeStore.getInstance();
	 //HappensBefore hb=HappensBefore.getInstance();
	 
	 Nodes nodes=Nodes.getInstance();
	 //ParallelBlocks pb=ParallelBlocks.getInstance();
	 LineStore ls=LineStore.getInstance();
	 

	private NPAnalyzer1() {
		
	}
	
	public static NPAnalyzer1 getInstance() {
	      if(instance == null) {
	         instance = new NPAnalyzer1();
	      }
	      return instance;
	   }
	
	public void storeNullAssign(String fileName,int codeLine,String fieldId){
		nullTable.put(fileName, codeLine,fieldId);
	}
	
	public void storeIntermediateWrite(String fileName,int codeLine,String fieldId){
		intermediateWriteTable.put(fileName, codeLine,fieldId);
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
	
	/*this function finds out all the putfield calls and stores it
	 * this helps to check if any nonnull writes are happening inbetween
	 * null assignment and null dereference.
	 */
	
	public void analyzeIntermetiateWrites(){
		boolean nullkey=false;
		Table<String,Integer, String> codeTable=cs.getCodeTable();
		for (Cell<String, Integer, String> cell: codeTable.cellSet()){
		   // System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
			String str = cell.getValue();
			if (str.contains("putfield")) {
				if(!nullkey){

				Pattern p = Pattern.compile("#([0-9]{2})");
				Matcher m = p.matcher(str);
				if (m.find()) {
					String fstr=m.group(1);
					this.storeIntermediateWrite(cell.getRowKey(),cell.getColumnKey(),(m.group(1)));
					intermediateWriteMap.put((m.group(1)), cell.getColumnKey());
					System.out.println("assignment of intermediate write: "+cell.getRowKey()+" "+cell.getColumnKey()+" "+fstr +" codeline: "+cell.getValue() );
					
				}
				
				
			}
			}
			nullkey=false;
			if((cell.getValue()).contains("aconst_null")){
				nullkey=true;
			}
			
			
			
		}
		
	}
	
	public boolean pruneIntermediateWrites(String varRef,String sourceBlock,String sinkBlock){
		checkHappensBefore hb=new checkHappensBefore();
		for (Cell<String, Integer, String> cell: intermediateWriteTable.cellSet()){
			  if(varRef.equals(cell.getValue().toString())){
				  String InterBlock=nodes.findNode(cell.getColumnKey(),cell.getRowKey());
				  if(InterBlock==sourceBlock||InterBlock==sinkBlock){
					  return true;
				  }
				  if(hb.checkHB(InterBlock, sinkBlock)&&hb.checkHB(sourceBlock, InterBlock)){
					  return true;
				  }
			  }
				
					
					
				
				
				}
		return false;
	}
	
	public void analyzeDeref(){
		Table<String,Integer, String> codeTable=cs.getCodeTable();
		for (Cell<String, Integer, String> cell: codeTable.cellSet()){
		   // System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
			
			
				String str = cell.getValue();
				
					for (Cell<String, Integer, String> cell1 : nullTable.cellSet()){
					    if(str.contains("#"+cell1.getValue())){		//# is appended because or else it will match with line number too eg: 11 getfield #22, it might match with 11 instead of 22
					    	this.storeDref(cell.getRowKey(), cell.getColumnKey(), cell1.getValue());
					    	System.out.println("dereference of value: "+cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue() );
					    }
					}							
				
			
			
			
			}
			
		
	}
	
	
	
	public void findNPE(){
		
		
		
		SwtList vh=new SwtList();
		Integer source,sink;
		String sourceBlock,sinkBlock;
		//vh.clearList();
		for (Cell<String, Integer, String> sourceCell: nullTable.cellSet()){
			
		     source=sourceCell.getColumnKey();
		    for (Cell<String, Integer, String> sinkCell: drefTable.cellSet()){
		    	sourceBlock=nodes.findNode(source,sourceCell.getRowKey());
		    	int snk=analyzeNullFlow(sourceBlock,sourceCell.getValue());
		    	
		    }
		}
		
		System.out.println("starting parallel analysis: ");
		for (Cell<String, Integer, String> sourceCell: nullTable.cellSet()){
			
		     source=sourceCell.getColumnKey();
		    for (Cell<String, Integer, String> sinkCell: drefTable.cellSet()){
		    	if(sourceCell.getValue()==sinkCell.getValue()){
		    		 sink=sinkCell.getColumnKey();
					    sourceBlock=nodes.findNode(source,sourceCell.getRowKey());
					    sinkBlock=nodes.findNode(sink,sinkCell.getRowKey());
					    checkParallelBlocks pb=new checkParallelBlocks();
					    if(pb.checkPB(sourceBlock, sinkBlock)){
					    	if(pruneIntermediateWrites(sourceCell.getValue(), sourceBlock, sinkBlock)){
					    		System.out.println("pruned intermediate write: "+ sourceCell.getValue()+sourceBlock+sinkBlock );
					    	}
					    	else{
					    	System.out.println("possible null pointer flow:");
					    	System.out.println("source: "+sourceCell.getRowKey()+" "+sourceCell.getColumnKey()+" "+sourceCell.getValue());
					    	System.out.println("sink: "+sinkCell.getRowKey()+" "+sinkCell.getColumnKey()+" "+sinkCell.getValue());
					    	System.out.println("--------------------");
					    	int src=ls.FindLineNo(sourceCell.getColumnKey(), sourceCell.getRowKey());
					    	int snk=ls.FindLineNo(sinkCell.getColumnKey(), sinkCell.getRowKey());
					    	
					    	vh.addElement(sinkCell.getRowKey(),src,snk);
					    	}
		    	}
			    
			    }
			}
		}
		
	}

	private int analyzeNullFlow(String sourceBlock, String value) {
		
		return 0;
	}
	
	
}
