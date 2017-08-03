package in.msruas.project.analysis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ListMultimap;

public class checkParallelBlocks {
	ArrayList<String> visited=new ArrayList<>();
	ListMultimap<String, String> pbMap;
	ParallelBlocks pb;
	
	
	
	public checkParallelBlocks() {
		pb=ParallelBlocks.getInstance();
		pbMap=pb.getPBMap();
	}



	public boolean checkPB(String before,String after){
		
		
		if(visited.contains(before)){
			return false;
		}
		else{
			visited.add(before);
		}
		List<String> value= pbMap.get(before);
	     if(value.contains(after)){
	    	 return true;
	     }
	     
	     /*we are already added subsequent parallel blocks for each block
	      * hence the below check for individual blocks is not required guess so
	      */
//	     else {
//	    	 for (String val : value) {
//	       		 
//	    		 if(checkPB(val, after)){
//	    			 return true;
//	    		 }
//	       			 
//	       		 }
//	    	 
//	     }
	   

	return false;
	}
}
