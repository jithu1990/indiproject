
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class GraphGenerator {

	BasicBlock[] bb;
	int[][] edges;
	DirectedSparseGraph<String, String> g;

	void generateGraph(BasicBlock[] b, int[][] edges) {
		bb = b;
		this.edges = edges;

		g = new DirectedSparseGraph<String, String>();

		// Add some vertices. From above we defined these to be type Integer.
		for (int i = 0; i < bb.length; i++) {
			g.addVertex(bb[i].getBlockName());

		}

		// Add some edges. From above we defined these to be of type String
		// Note that the default is for undirected edges.
		for (int i = 0; i < bb.length; i++) {
			if (i == bb.length - 1) {
				break;
			}
			g.addEdge("edge" + i, bb[i].getBlockName(), bb[i + 1].getBlockName());
		}

		for (int i = 0; i < edges.length; i++) {

			int from = edges[i][0], to = edges[i][1];
			System.out.println("edgeLIst: from : " + edges[i][0] + "to : " + edges[i][1]);
			if (findNode(from) == null || findNode(to) == null || edges[i][0] == 0 || edges[i][1] == 0) {
				continue;
			}
			g.addEdge("goto" + i + i, findNode(from), findNode(to));

		}

		// Let's see what we have. Note the nice output from the
		// SparseMultigraph<V,E> toString() method
		System.out.println("The graph g = " + g.toString());
		// Note that we can use the same nodes and edges in two different
		// graphs.

		Layout<String, String> layout = new FRLayout<String, String>(g);
		layout.setSize(new Dimension(500, 500)); // sets the initial size of the
													// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<String, String> vv = new BasicVisualizationServer<String, String>(layout);
		vv.setPreferredSize(new Dimension(500, 500)); // Sets the viewing area
														// size

		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
		System.out.println("edgelist is:" + g.getEndpoints("goto11"));
		findHappensBefore();

	}

	void findHappensBefore() {
		Collection<String> edgelist = g.getEdges();
		
		Iterator<String> iterator = edgelist.iterator();
		Set<Pair> pairs = new HashSet<Pair>();
		;
		while (iterator.hasNext()) {
			edu.uci.ics.jung.graph.util.Pair<String> edge = g.getEndpoints(iterator.next());

			pairs.add(new Pair(edge.getFirst(), edge.getSecond()));
		}
		Iterator<Pair> iterator1 = pairs.iterator();
		while (iterator1.hasNext()) {
			Pair item = iterator1.next();
			System.out.println(item.before + ">" + item.after);
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

	public class Pair {
		String before;
		String after;

		Pair(String first, String second) {
			before = first;
			after = second;
		}
	}
}
