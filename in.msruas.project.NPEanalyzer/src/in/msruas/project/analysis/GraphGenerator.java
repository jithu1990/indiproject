package in.msruas.project.analysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.*;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


public class GraphGenerator {

	static BasicBlock[] bb;
	int[][] edges;
	ArrayList<Integer> gotoList;
	DirectedSparseGraph<String, String> g;
	DirectedSparseGraph<String, String> g1;
	int gotoEdgeLabel = 0;
	HappensBefore hb;
	SyncBlocksOps sbo;
	BasicBlockOps bbo;
	ParallelBlocks pb;
	Nodes nodes;

	public GraphGenerator() {
		g = new DirectedSparseGraph<String, String>();
		hb=HappensBefore.getInstance();
		sbo=SyncBlocksOps.getInstance();
		bbo=BasicBlockOps.getInstance();
		pb=ParallelBlocks.getInstance();
		nodes=Nodes.getInstance();
	}

	public void generateGraph(BasicBlock[] b, int[][] edges, ArrayList<Integer> gotoList) {
		bb = b;
		this.edges = edges;
		this.gotoList = gotoList;

		// Add some vertices. From above we defined these to be type Integer.
		for (int i = 0; i < bb.length; i++) {
			if(bb[i].getBlockName()!="stop"){
			g.addVertex(bb[i].getBlockName());
			}

		}

		// Add some edges. From above we defined these to be of type String
		// Note that the default is for undirected edges.
		for (int i = 0; i < bb.length; i++) {
			if (i == bb.length - 1) {
				break;
			}
			if (checkIfGotoPresent(i)) {
				g.addEdge("edge" + bb[i].getBlockName() + bb[i + 1].getBlockName(), bb[i].getBlockName(),
						bb[i + 1].getBlockName());
			}

		}

		for (int i = 0; i < edges.length; i++) {
			gotoEdgeLabel++;
			int from = edges[i][0], to = edges[i][1];

//			if (findNode(from) == null || findNode(to) == null || edges[i][0] == 0 || edges[i][1] == 0) {
//				continue;
//			}
			if (findNode(from) == null || findNode(to) == null) {
				continue;
			}
			if (edges[i][0] == 0 && edges[i][1] == 0) {
				continue;
			}
			g.addEdge("goto" + findNode(from)+findNode(to), findNode(from), findNode(to));
		}

		// Let's see what we have. Note the nice output from the
		// SparseMultigraph<V,E> toString() method
		//System.out.println("The graph g = " + g.toString());
		// Note that we can use the same nodes and edges in two different
		// graphs.

		// findHappensBefore();

	}

	public void renderGraph() {
		Layout<String, String> layout = new FRLayout<String, String>(g);
		System.out.println("vertices are:" + g.getVertices().toString());
		layout.setSize(new Dimension(600,600)); // sets the initial size of the
													// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<String, String> vv = new BasicVisualizationServer<String, String>(layout);
		VisualizationImageServer<String, String> viz = new VisualizationImageServer<String,String>(vv.getGraphLayout(),
		        vv.getGraphLayout().getSize());
		
		vv.setPreferredSize(new Dimension(400, 400)); // Sets the viewing area
														// size

		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		
		//color of vertex
		Transformer<String,Paint> vertexPaint = new Transformer<String,Paint>() {
	        public Paint transform(String i) {
	        	//System.out.println("i is "+i);
	        	if(isSyncBlock(i))
	        		return Color.GREEN;
	        	else
	        	  	return Color.RED;
	        }
	        
	    };

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
		//System.out.println("edgelist is:" + g.getEdges());
		
		viz.setBackground(Color.WHITE);
		viz.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<String>());
		viz.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<String, String>());
		viz.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
		viz.getRenderer().getVertexLabelRenderer()
		    .setPosition(Renderer.VertexLabel.Position.CNTR);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		
		BufferedImage image = (BufferedImage) viz.getImage(
			    new Point2D.Double(vv.getGraphLayout().getSize().getWidth() / 2,
			    vv.getGraphLayout().getSize().getHeight() / 2),
			    new Dimension(vv.getGraphLayout().getSize()));
		
		File outputfile = new File("graph.png");

		try {
		    ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
		    System.out.println("exception creating image");
		    e.printStackTrace();
		}
	}

	void genInterThreadComm(Stack waitStack, Stack notifyStack ){
		
		Iterator stackIter=waitStack.iterator();
		while (stackIter.hasNext()){
		int from = Integer.parseInt((String) notifyStack.pop()) , to = Integer.parseInt((String) waitStack.pop());

//		if (findNode(from) == null || findNode(to) == null || edges[i][0] == 0 || edges[i][1] == 0) {
//			continue;
//		}
		
		String fromNode = findNode(from);
		String toNode = findNode(to);
		if (findNode(from) == null || findNode(to) == null) {
			continue;
		}
		g.addEdge("inter" + findNode(from)+findNode(to), fromNode, toNode);
		}
		//System.out.println("edgelist is:" + g.getEdges());
	}
	
	
	void addInterEdge(String name,String fromNode,String toNode){
		g.addEdge("inter" + name, fromNode, toNode);
	}
	
	public void findHappensBefore() {
		Collection<String> edgelist = g.getEdges();
		
		

		Iterator<String> iterator = edgelist.iterator();
		Set<Pair> pairs = new HashSet<Pair>();

		while (iterator.hasNext()) {
			edu.uci.ics.jung.graph.util.Pair<String> edge = g.getEndpoints(iterator.next());

			pairs.add(new Pair(edge.getFirst(), edge.getSecond()));
		}
		Iterator<Pair> iterator1 = pairs.iterator();
		while (iterator1.hasNext()) {
			Pair item = iterator1.next();
			//System.out.println(item.before + ">" + item.after);
			hb.addHB(item.before, item.after);
		}
	}

	String findNode(int instrNo) {
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
	
	
	/*function to find basic block when a block name is give */
	public BasicBlock findBasicBlock(String name){
		ArrayList<BasicBlock> bbarray = bbo.getBasicBlock();
		for (BasicBlock bb : bbarray) {
			if (bb.getBlockName()==name){
				return bb;
			}

			}
		
		return null;
		
	}

	/* function to check if goto present in given block */
	Boolean checkIfGotoPresent(int i) {
		ArrayList<Integer> ar = bb[i].getInstLines();
		return Collections.disjoint(gotoList, ar);
	}

	public class Pair {
		String before;
		String after;

		Pair(String first, String second) {
			before = first;
			after = second;
		}
	}
	
	
	public boolean isSyncBlock(String i){
		ArrayList<SyncBlock> ar=sbo.getSyncBlock();
		BasicBlock b;
		Queue enterQ;
		Stack exitStack;
		String fname;
		b=findBasicBlock(i);
		if(b==null){
			return false;
		}
		for (SyncBlock sb : ar) {
			
			if (sb.getFileName().equals(b.filename)){
				enterQ= new LinkedList<>(sb.getEnterLine());		// should do this or refernce of queue is passes and the values gets dequed 
				exitStack=sb.getExitLine();		
				exitStack=(Stack) exitStack.clone();	//should clone or reference of stack is passed.
				while(!enterQ.isEmpty()){
					if(((Integer) enterQ.remove())<=b.getLeader()&&b.getLeader()<=((Integer) exitStack.pop())){
						
						return true;
					}
				}
			}
			
			
		}
		return false;
		
	}
	
	void findParallelBlocks(String fileName1,String fileName2){
		BasicBlock[] bb1 = nodes.getNodesMap(fileName1);
		BasicBlock[] bb2=nodes.getNodesMap(fileName2);
		
		for (BasicBlock b1 : bb1) {
			for(BasicBlock b2:bb2 ){
				if(isSyncBlock(b1.getBlockName())&&isSyncBlock(b2.getBlockName())){
					continue;
				}
				pb.addPB(b1.getBlockName(), b2.getBlockName());
			}

			}
		for (BasicBlock b2 : bb2) {
			for(BasicBlock b1:bb1 ){
				if(isSyncBlock(b1.getBlockName())&&isSyncBlock(b2.getBlockName())){
					continue;
				}
				pb.addPB(b2.getBlockName(), b1.getBlockName());
			}

			}
	pb.printHB();
	}
	
	
	/*this method does not consider happens before to find parallel
	 * because it is much easier to check if both are synchronized than to check
	 * if they both are paralle. Maybe in real program we will have to check 
	 * if they are happens before because for now we dont consider if they 
	 * use same locks.
	 */
	void findPBWithHB(CFGconstructor1[] cfgObj,String[] filePath){
		
		
		for (int i = 0; i < cfgObj.length-1; i++){
			BasicBlock[] bb1 = nodes.getNodesMap(filePath[i]);
			for(int j=i;j<cfgObj.length;j++){
				BasicBlock[] bb2=nodes.getNodesMap(filePath[i+1]);
				for (BasicBlock b1 : bb1) {
					for(BasicBlock b2:bb2 ){
						if(isSyncBlock(b1.getBlockName())&&isSyncBlock(b2.getBlockName())){
							continue;
						}
						pb.addPB(b1.getBlockName(), b2.getBlockName());
					}

					}
				for (BasicBlock b2 : bb2) {
					for(BasicBlock b1:bb1 ){
						if(isSyncBlock(b1.getBlockName())&&isSyncBlock(b2.getBlockName())){
							continue;
						}
						pb.addPB(b2.getBlockName(), b1.getBlockName());
					}

					}
			
			}
			
			
		}
		pb.printHB();
		
		
	}
}
