package in.msruas.project.analysis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class ParallelBlocks {
	private static ParallelBlocks instance = null;
	ListMultimap<String, String> pbMap = ArrayListMultimap.create();
	ArrayList<String> visited=new ArrayList<>();
	
	
	
private ParallelBlocks() {
		
	}

public static ParallelBlocks getInstance() {
    if(instance == null) {
       instance = new ParallelBlocks();
    }
    return instance;
 }

public void addPB(String key,String value){
	pbMap.put(key, value);
}

public ListMultimap<String, String> getPBMap(){
	return pbMap;
}

public void printPB(){
	for (String key : pbMap.keySet()) {
	     List<String> value= pbMap.get(key);
	     System.out.println(key + "||" + value);
	   }
}

/* this method has been shifted to 
 * checkparallelblocks.java
 */

//public boolean checkPB(String before,String after){
//	
//	
//	if(visited.contains(before)){
//		return false;
//	}
//	else{
//		visited.add(before);
//	}
//	List<String> value= pbMap.get(before);
//     if(value.contains(after)){
//    	 return true;
//     }
//     
//     /*we are already added subsequent parallel blocks for each block
//      * hence the below check for individual blocks is not required guess so
//      */
////     else {
////    	 for (String val : value) {
////       		 
////    		 if(checkPB(val, after)){
////    			 return true;
////    		 }
////       			 
////       		 }
////    	 
////     }
//   
//
//return false;
//}
}
