package in.msruas.project.analysis;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Table;
import com.google.common.collect.Tables;

public class LineStore {
	private static LineStore instance = null;
	Table<String,Integer, Integer> lineTable = Tables.newCustomTable(new LinkedHashMap<>(), LinkedHashMap::new);
	
	
	
	protected LineStore() {
		
	}
	
	public static LineStore getInstance() {
	      if(instance == null) {
	         instance = new LineStore();
	      }
	      return instance;
	   }
	
	public void addLineEntry(String fname,int sourceLineNo,int ByteLineNo){
		lineTable.put(fname,ByteLineNo, sourceLineNo);
	}
	
	public int FindLineNo(int byteNo,String Method){
		System.out.println("finding source line");
		 Map<Integer,Integer> code =  new TreeMap<Integer,Integer>();
				 code=lineTable.row(Method);
			int lineno=(int) findNearest(code, byteNo);	 
		
	         System.out.println("line No: " + lineno );
	         return lineno;
	      
	}
	
	public void printCode(String Method){
		System.out.println("printing line table");
		 Map<Integer,Integer> code =  lineTable.row(Method);
		for(Map.Entry<Integer, Integer> entry : code.entrySet()){
	         System.out.println("code key: " + entry.getKey() + ", value : " + entry.getValue());
	      }
	}
	
	private static Object findNearest(Map<Integer, Integer> map, int value) {
	    Map.Entry<Integer, Integer> previousEntry = null;
	    for (Map.Entry<Integer, Integer> e : map.entrySet()) {
	        if (e.getKey().compareTo(value) >= 0) {
	            if (previousEntry == null) {
	                return e.getValue();
	            } else {
	                if (e.getKey() - value >= value - previousEntry.getKey()) {
	                    return previousEntry.getValue();
	                } else {
	                    return e.getValue();
	                }
	            }
	        }
	        previousEntry = e;
	    }
	    return previousEntry.getValue();
	}
}
