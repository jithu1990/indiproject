package in.msruas.project.analysis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class HappensBefore {
	private static HappensBefore instance = null;
	ListMultimap<String, String> hbMap = ArrayListMultimap.create();
	ArrayList<String> visited=new ArrayList<>();;
	
	
	
protected HappensBefore() {
		
	}

public static HappensBefore getInstance() {
    if(instance == null) {
       instance = new HappensBefore();
    }
    return instance;
 }

public void addHB(String key,String value){
	hbMap.put(key, value);
}

public ListMultimap<String, String> getHBMap(){
	return hbMap;
}

/*this function is removed and added in 
 * checkHappensBefore.java
 */

//public boolean checkHB(String before,String after){
//	 
//		
//		if(visited.contains(before)){
//			return false;
//		}
//		else{
//			visited.add(before);
//		}
//		List<String> value= hbMap.get(before);
//	     if(value.contains(after)){
//	    	 return true;
//	     }
//	     else {
//	    	 for (String val : value) {
//	       		 
//	    		 if(checkHB(val, after)){
//	    			 return true;
//	    		 }
//	       			 
//	       		 }
//	    	 
//	     }
//	   
//
//	return false;
//}

public boolean checkKeyEquals(String before,String after){
	List<String> value= hbMap.get(before);
	
	if(value.contains(after)){
  		 return true;
  	 }
    
   	 for (String val : value) {
   		 
   		 checkKeyEquals(val, after);
   			 
   		 }
		
    return false;
}

public void printHB(){
	for (String key : hbMap.keySet()) {
	     List<String> value= hbMap.get(key);
	     System.out.println(key + ">" + value);
	   }
}
}
