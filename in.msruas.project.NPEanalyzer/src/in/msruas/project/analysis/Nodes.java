package in.msruas.project.analysis;

import java.util.ArrayList;
import java.util.HashMap;

public class Nodes {
	private static Nodes instance = null;
	HashMap nodesMap;

	private Nodes() {
		nodesMap=new HashMap<>();
	}
	
	public static Nodes getInstance() {
	    if(instance == null) {
	       instance = new Nodes();
	    }
	    return instance;
	 }
	
	public void setNodesMap(String fileName,BasicBlock[] bb){
		nodesMap.put(fileName, bb);
	}
	
	public BasicBlock[] getNodesMap(String fileName){
		return (BasicBlock[]) nodesMap.get(fileName);
	}
	
	public HashMap getEntireNodesMap(){
		return nodesMap;
	}
	
	String findNode(int instrNo,String fileName) {
		BasicBlock[] bb=(BasicBlock[]) nodesMap.get(fileName);
		for (int i = 0; i < bb.length; i++) {
			ArrayList<Integer> ar = bb[i].getInstLines();
			for (int ele : ar) {
				if (ele == instrNo) {
					return bb[i].getBlockName();

				}

			}

		}
		return null;
	}
}
