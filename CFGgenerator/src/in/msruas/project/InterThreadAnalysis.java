package in.msruas.project;
import java.util.ArrayList;
import java.util.Queue;

public class InterThreadAnalysis {
	
	String filePath1,filePath2;
	BasicBlock[] bb1,bb2;
	GraphGenerator g;
	//Queue waitQueue,notifyQueue;
	

	public InterThreadAnalysis(BasicBlock[] bb1, BasicBlock[] bb2,
			GraphGenerator g) {
		super();
		
		this.bb1 = bb1;
		this.bb2 = bb2;
		this.g=g;
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
		}
		
		
		for(Object item : notifyQ2){
			int from = Integer.parseInt((String) notifyQ2.remove()) , to = Integer.parseInt((String) waitQ1.remove());
			String fromNode = findNode(from,bb2);
			String toNode = findNode(to,bb1);
			if (fromNode == null || toNode == null) {
				continue;
			}
			g.addInterEdge(fromNode+toNode, fromNode, toNode);
		}
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
	
}
