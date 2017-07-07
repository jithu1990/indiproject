package in.msruas.project;
import java.util.Map;

import com.google.common.collect.*;
public class CodeStore { 
	private static CodeStore instance = null;
	Table<String,Integer, String> codeTable = HashBasedTable.create();
	
	
	
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
		 Map<Integer,String> code =  codeTable.row("F:\\individual_project\\bytecodes\\consumer.txt");
		for(Map.Entry<Integer, String> entry : code.entrySet()){
	         System.out.println("code key: " + entry.getKey() + ", value : " + entry.getValue());
	      }
	}
}
