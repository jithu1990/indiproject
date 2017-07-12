package in.msruas.project;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.collect.*;
public class CodeStore { 
	private static CodeStore instance = null;
	Table<String,Integer, String> codeTable = Tables.newCustomTable(new LinkedHashMap<>(), LinkedHashMap::new);
	
	
	
	protected CodeStore() {
		
	}

	public Table<String, Integer, String> getCodeTable() {
		return codeTable;
	}

	public void setCodeTable(Table<String, Integer, String> codeTable) {
		this.codeTable = codeTable;
	}

	public static CodeStore getInstance() {
	      if(instance == null) {
	         instance = new CodeStore();
	      }
	      return instance;
	   }

	void addCodeEntry(String fname,int key,String code){
		codeTable.put(fname,key, code);
	}
	
	public void printCode(){
		System.out.println("printing code");
		 Map<Integer,String> code =  codeTable.row("F:\\individual_project\\bytecodes\\producer_parallel_null.txt");
		for(Map.Entry<Integer, String> entry : code.entrySet()){
	         System.out.println("code key: " + entry.getKey() + ", value : " + entry.getValue());
	      }
	}
}
