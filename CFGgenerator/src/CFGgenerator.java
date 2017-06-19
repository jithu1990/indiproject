import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CFGgenerator {

	public static void main(String[] args) throws IOException {
		CFGconstructor cfg = new CFGconstructor("F:\\individual_project\\bytecodes\\consumer.txt",1);
		CFGconstructor cfg1 = new CFGconstructor("F:\\individual_project\\bytecodes\\producer.txt",2);
		
		
		cfg.computeCFGInfo();
		cfg.sortLeaders();
		cfg.basicBlockGenerator();
		
		GraphGenerator g = new GraphGenerator();
		g.generateGraph(cfg.getBasicBlock(), cfg.getEdge(),cfg.getGotoList());
		cfg1.computeCFGInfo();
		cfg1.sortLeaders();
		cfg1.basicBlockGenerator();
		
		
		
		
		g.generateGraph(cfg1.getBasicBlock(), cfg1.getEdge(),cfg1.getGotoList());
		
		
		
		
		
		
		//g.genInterThreadComm(cfg1.getWaitStack(), cfg1.getNotifyStack());
		InterThreadAnalysis it=new InterThreadAnalysis("F:\\individual_project\\bytecodes\\producer.txt", "F:\\individual_project\\bytecodes\\producer.txt", cfg.getBasicBlock(), cfg1.getBasicBlock(), g);
		it.computerInterThreadEdges(cfg.getWaitStack(), cfg.getNotifyStack(), cfg1.getWaitStack(), cfg1.getNotifyStack());
		g.renderGraph();
//		cfg.printblock();
//		cfg1.printblock();
		g.findHappensBefore();
		// GraphGenerator g=new GraphGenerator();
		// g.generateGraph();
		//

	}

}
