package in.msruas.project.analysis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Table;

/*this class is created seperately from happensBefore becz
 * the visited variable was creating a problem as it is static and we need it 
 * as static for our purpose
 */
public class checkHappensBefore {

	ArrayList<String> visited=new ArrayList<>();
	
	ListMultimap<String, String> hbMap;
	HappensBefore hb;
	CodeStore cs=CodeStore.getInstance();
	BasicBlockOps bbo=BasicBlockOps.getInstance();
	
	
	
	
public checkHappensBefore() {
		hb=HappensBefore.getInstance();
		hbMap=hb.getHBMap();
	}



public boolean checkHB(String before,String after){
	 
		
		if(visited.contains(before)){
			return false;
		}
		else{
			visited.add(before);
		}
		List<String> value= hbMap.get(before);
	     if(value.contains(after)){
	    	 System.out.println(before+" happens before "+after);
	    	 return true;
	     }
	     else {
	    	 for (String val : value) {
	       		 
	    		 if(checkHB(val, after)){
	    			 System.out.println(before+" happens before "+after);
	    			 return true;
	    		 }
	       			 
	       		 }
	    	 
	     }
	   

	return false;
}

/*the below function checks 
 * if there is a dereference of a null pointer using 
 * happens before - this is not used
 */

public int checkNullFlowWithHB(String nullBlock, int ref,int nullLineNo){
	Table<String,Integer, String> codeTable=cs.getCodeTable();
	if(visited.contains(nullBlock)){
		return 0;
	}
	else{
		visited.add(nullBlock);
	}
	
	
	BasicBlock bb = bbo.findBasicBlock(nullBlock);
	String fname = bb.filename;
	ArrayList<Integer> instLines=bb.getInstLines();
	for(int line:instLines){
		if(line>nullLineNo){
		String code = cs.getCodeAtLine(fname, line);
		if(code.contains("getfield")||code.contains("getstatic")){
			if(code.contains("#"+ref)){
				return line;
			}
		}
		}
	}
	
	List<String> value= hbMap.get(nullBlock);
	
	for (String val : value) {
		int result=checkDref(val, ref);
		if(result==0){
			return  checkNullFlowWithHB(val, ref,nullLineNo);
		}
		else{
			return result;
		}
	}
	return 0;
	
	
}
 /* this is not used*/
public int checkDref(String val,int ref){

		 
	BasicBlock bb = bbo.findBasicBlock(val);
	String fname = bb.filename;
	ArrayList<Integer> instLines=bb.getInstLines();
	for(int line:instLines){
		String code = cs.getCodeAtLine(fname, line);
		if(code.contains("getfield")||code.contains("getstatic")){
			if(code.contains("#"+ref)){
				return line;
			}
		}
	}
	return 0;
			 
		 
}
}
