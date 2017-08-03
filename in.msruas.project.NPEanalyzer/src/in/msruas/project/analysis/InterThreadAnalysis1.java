package in.msruas.project.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

public class InterThreadAnalysis1 {
	String filePath1,filePath2;
	BasicBlock[] bb1,bb2;
	GraphGenerator g;
	WaitAndNotifyOps wno;
	Queue waitQ1, notifyQ1, waitQ2, notifyQ2;
	//BasicBlockOps bbo;
	Nodes nodelist;
	public InterThreadAnalysis1(GraphGenerator g) {
		this.g=g;
		wno=WaitAndNotifyOps.getInstance();
		//bbo=BasicBlockOps.getInstance();
		nodelist=Nodes.getInstance();
		initBasicBlocks();	//not required when method changed to generalized. delete later
		initQueues();	//not required when method changed to generalized. delete later
	}
	
	/*function to initialize the basic blocks*/
	void initBasicBlocks(){
		HashMap blockList=nodelist.getEntireNodesMap();
//		Iterator keyset = blockList.keySet().iterator();		
//		bb1=(BasicBlock[]) blockList.get(keyset.next());
//		bb2=(BasicBlock[]) blockList.get(keyset.next());
		
	}
	
	/*function to initialize the wait and notify queues*/
	void initQueues(){
		ArrayList<WaitAndNotify> wnList = wno.getWnList();
//		waitQ1=wnList.get(1).getWaitQueue();
//		notifyQ1=wnList.get(1).getNotifyQueue();
//		waitQ2=wnList.get(0).getWaitQueue();
//		notifyQ2=wnList.get(0).getNotifyQueue();		
	}
	
public void computerInterThreadEdges(BasicBlock[] basicBlocks, BasicBlock[] basicBlocks2,Queue waitQ1,Queue notifyQ1,Queue waitQ2,Queue notifyQ2){
	
	bb1=basicBlocks;
	bb2=basicBlocks2;
		for(Object item : notifyQ1){
			if(notifyQ1.isEmpty()||waitQ2.isEmpty()){
				continue;
			}
			int from = Integer.parseInt((String) notifyQ1.peek()) , to = Integer.parseInt((String) waitQ2.peek());
			String fromNode = findNode(from,bb1);
			String toNode = findNode(to,bb2);
			if (fromNode == null || toNode == null) {
				continue;
			}
			notifyQ1.remove();			//remove only if both fromNode and toNode not null, otherwise accidently notify may find linenumber in bb1 eventhough actual is in bb2 and pop off element
			waitQ2.remove();
			g.addInterEdge(fromNode+toNode, fromNode, toNode);
			/*find and join wait prefix */
			BasicBlock b=findNodeandReturnBlock(to, bb2);
			if(b==null){
				continue;
			}
			String waitPrefix=b.getPrevBlock();
			if(waitPrefix!=null){
				g.addInterEdge(waitPrefix+fromNode, waitPrefix, fromNode);
			}
		}
		
		
		for(Object item : notifyQ2){
			if(notifyQ2.isEmpty()||waitQ1.isEmpty()){
				continue;
			}
			int from = Integer.parseInt((String) notifyQ2.peek()) , to = Integer.parseInt((String) waitQ1.peek());
			String fromNode = findNode(from,bb2);
			String toNode = findNode(to,bb1);
			if (fromNode == null || toNode == null) {
				continue;
			}
			notifyQ2.remove();
			waitQ1.remove();
			g.addInterEdge(fromNode+toNode, fromNode, toNode);
			/*find and join wait prefix */
			BasicBlock b=findNodeandReturnBlock(to, bb1);
			if(b==null){
				continue;
			}
			String waitPrefix=b.getPrevBlock();
			if(waitPrefix!=null){
				g.addInterEdge(waitPrefix+fromNode, waitPrefix, fromNode);
			}
		}
		
		for(Object item : notifyQ1){
			if(notifyQ1.isEmpty()||waitQ2.isEmpty()){
				continue;
			}
			int from = Integer.parseInt((String) notifyQ1.peek()) , to = Integer.parseInt((String) waitQ2.peek());
			String fromNode = findNode(from,bb2);
			String toNode = findNode(to,bb1);
			if (fromNode == null || toNode == null) {
				continue;
			}
			notifyQ1.remove();			//remove only if both fromNode and toNode not null, otherwise accidently notify may find linenumber in bb1 eventhough actual is in bb2 and pop off element
			waitQ2.remove();
			g.addInterEdge(fromNode+toNode, fromNode, toNode);
			/*find and join wait prefix */
			BasicBlock b=findNodeandReturnBlock(to, bb1);
			if(b==null){
				continue;
			}
			String waitPrefix=b.getPrevBlock();
			if(waitPrefix!=null){
				g.addInterEdge(waitPrefix+fromNode, waitPrefix, fromNode);
			}
		}
		for(Object item : notifyQ2){
			if(notifyQ2.isEmpty()||waitQ1.isEmpty()){
				continue;
			}
			int from = Integer.parseInt((String) notifyQ2.peek()) , to = Integer.parseInt((String) waitQ1.peek());
			String fromNode = findNode(from,bb1);
			String toNode = findNode(to,bb2);
			if (fromNode == null || toNode == null) {
				continue;
			}
			notifyQ2.remove();
			waitQ1.remove();
			g.addInterEdge(fromNode+toNode, fromNode, toNode);
			/*find and join wait prefix */
			BasicBlock b=findNodeandReturnBlock(to, bb2);
			if(b==null){
				continue;
			}
			String waitPrefix=b.getPrevBlock();
			if(waitPrefix!=null){
				g.addInterEdge(waitPrefix+fromNode, waitPrefix, fromNode);
			}
		}
		
		
		
	}
	
	
	public void computerNewInterThreadAnalysis(){
		
	}
	
	String findNode(int instrNo,BasicBlock[] bb) {
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
	/*finds a basic block from instruction number */
	
	BasicBlock findNodeandReturnBlock(int instrNo,BasicBlock[] bb) {
		for (int i = 0; i < bb.length; i++) {
			ArrayList<Integer> ar = bb[i].getInstLines();
			for (int ele : ar) {
				if (ele == instrNo) {
					return bb[i];

				}

			}

		}
		return null;
	}
	
	
}
