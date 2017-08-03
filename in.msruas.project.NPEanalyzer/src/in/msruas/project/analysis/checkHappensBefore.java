package in.msruas.project.analysis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ListMultimap;

/*this class is created seperately from happensBefore becz
 * the visited variable was creating a problem as it is static and we need it 
 * as static for our purpose
 */
public class checkHappensBefore {

	ArrayList<String> visited=new ArrayList<>();
	ListMultimap<String, String> hbMap;
	HappensBefore hb;
	
	
	
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
	    	 return true;
	     }
	     else {
	    	 for (String val : value) {
	       		 
	    		 if(checkHB(val, after)){
	    			 return true;
	    		 }
	       			 
	       		 }
	    	 
	     }
	   

	return false;
}
}
