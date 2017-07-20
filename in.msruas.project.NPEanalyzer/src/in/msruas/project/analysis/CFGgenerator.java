package in.msruas.project.analysis;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CFGgenerator {

	public void main(String path, ArrayList<String> funcName) throws IOException {
		int count=1;
		
//		CFGconstructor1 cfg = new CFGconstructor1("F:\\individual_project\\bytecodes\\days_assign_At_Notify.txt",1);
//		CFGconstructor1 cfg1 = new CFGconstructor1("F:\\individual_project\\bytecodes\\days_print_At_Notify.txt",2);
		CFGconstructor1 cfg = new CFGconstructor1("F:\\individual_project\\bytecodes\\days_assign_1.txt",1);
		CFGconstructor1 cfg1 = new CFGconstructor1("F:\\individual_project\\bytecodes\\days_print_1.txt",2);
//		CFGconstructor1 cfg = new CFGconstructor1("F:\\individual_project\\bytecodes\\producer_parallel_null.txt",1);
//		CFGconstructor1 cfg1 = new CFGconstructor1("F:\\individual_project\\bytecodes\\consumer_parallel_null.txt",2);
//		CFGconstructor cfg = new CFGconstructor("F:\\individual_project\\bytecodes\\caseStudy1_thread1.txt",1);
//		CFGconstructor cfg1 = new CFGconstructor("F:\\individual_project\\bytecodes\\caseStudy1_thread2.txt",2);
//		CFGconstructor1 cfg = new CFGconstructor1("F:\\individual_project\\bytecodes\\SimpleWaitAndNotifyMain.txt",1);
//		CFGconstructor1 cfg1 = new CFGconstructor1("F:\\individual_project\\bytecodes\\SimpleWaitAndNotifyThreadOne.txt",2);
		
		
		
		NPAnalyzer npa=NPAnalyzer.getInstance();
		CodeStore cs=CodeStore.getInstance();
		HappensBefore hb=HappensBefore.getInstance();
		
		
		cfg.computeCFGInfo();
		cfg.sortLeaders();
		cfg.basicBlockGenerator();
		
		GraphGenerator g = new GraphGenerator();
		g.generateGraph(cfg.getBasicBlock(), cfg.getEdge(),cfg.getGotoList());
		cfg1.computeCFGInfo();
		cfg1.sortLeaders();
		cfg1.basicBlockGenerator();
		
		cfg.printblock();
		cfg1.printblock();
		
		
		g.generateGraph(cfg1.getBasicBlock(), cfg1.getEdge(),cfg1.getGotoList());
		
		
		
		
		
		
		//g.genInterThreadComm(cfg1.getWaitStack(), cfg1.getNotifyStack());
//		InterThreadAnalysis it=new InterThreadAnalysis(cfg.getBasicBlock(), cfg1.getBasicBlock(), g);
//		it.computerInterThreadEdges(cfg.getWaitStack(), cfg.getNotifyStack(), cfg1.getWaitStack(), cfg1.getNotifyStack());
//		
		InterThreadAnalysis1 it=new InterThreadAnalysis1(g);
		it.computerInterThreadEdges();
		
		g.renderGraph();
//		cfg.printblock();
//		cfg1.printblock();
		g.findHappensBefore();
//		g.findParallelBlocks("F:\\individual_project\\bytecodes\\days_assign_At_Notify.txt", "F:\\individual_project\\bytecodes\\days_print_At_Notify.txt");
//		g.findParallelBlocks("F:\\individual_project\\bytecodes\\producer_parallel_null.txt", "F:\\individual_project\\bytecodes\\consumer_parallel_null.txt");
		g.findParallelBlocks("F:\\individual_project\\bytecodes\\days_assign_1.txt", "F:\\individual_project\\bytecodes\\days_print_1.txt");
		
		// GraphGenerator g=new GraphGenerator();
		// g.generateGraph();
		//
		npa.analyzeNull();
		npa.analyzeDeref();
		npa.printNull();
		//cs.printCode();
		hb.printHB();
		npa.findNPE();

	}

}
