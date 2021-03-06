package in.msruas.project;
import java.util.ArrayList;
import java.util.Queue;

public class InterThreadAnalysis {
	
	String filePath1,filePath2;
	BasicBlock[] bb1,bb2;
	GraphGenerator g;
	//SyncBlocksOps sbo;
	//Queue waitQueue,notifyQueue;
	

	public InterThreadAnalysis(BasicBlock[] bb1, BasicBlock[] bb2,
			GraphGenerator g) {
				
		this.bb1 = bb1;
		this.bb2 = bb2;
		this.g=g;
		//sbo=SyncBlocksOps.getInstance();
	}


	public void computerInterThreadEdges(Queue waitQ1,Queue notifyQ1,Queue waitQ2,Queue notifyQ2){
		
		for(Object item : notifyQ1){
			int from = Integer.parseInt((String) notifyQ1.remove()) , to = Integer.parseInt((String) waitQ2.remove());
			String fromNode = findNode(from,bb1);
			String toNode = findNode(to,bb2);
			if (fromNode == null || toNode == null) {
				continue;
			}
			g.addInterEdge(fromNode+toNode, fromNode, toNode);
			/*find and join wait prefix */
			BasicBlock b=findNodeandReturnBlock(to, bb2);
			String waitPrefix=b.getPrevBlock();
			if(waitPrefix!=null){
				g.addInterEdge(waitPrefix+fromNode, waitPrefix, fromNode);
			}
		}
		
		
		for(Object item : notifyQ2){
			int from = Integer.parseInt((String) notifyQ2.remove()) , to = Integer.parseInt((String) waitQ1.remove());
			String fromNode = findNode(from,bb2);
			String toNode = findNode(to,bb1);
			if (fromNode == null || toNode == null) {
				continue;
			}
			g.addInterEdge(fromNode+toNode, fromNode, toNode);
			/*find and join wait prefix */
			BasicBlock b=findNodeandReturnBlock(to, bb1);
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
