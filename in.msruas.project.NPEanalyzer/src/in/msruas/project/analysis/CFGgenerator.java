package in.msruas.project.analysis;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CFGgenerator {

	public void main(String path, ArrayList<String> funcName) throws IOException {
		int count=1;
		String[] filePath=new String[funcName.size()];
		NPAnalyzer npa=NPAnalyzer.getInstance();
		CodeStore cs=CodeStore.getInstance();
		HappensBefore hb=HappensBefore.getInstance();
		GraphGenerator g = new GraphGenerator();
		InterThreadAnalysis1 it=new InterThreadAnalysis1(g);
		WaitAndNotifyOps wno=WaitAndNotifyOps.getInstance();
		ArrayList<WaitAndNotify> wnList = wno.getWnList();
		LineStore ls=LineStore.getInstance();
		
		CFGconstructor1[] cfgObj=new CFGconstructor1[funcName.size()];
		
		for (int i = 0; i < cfgObj.length; i++) {
			filePath[i]=path+"\\bin\\"+funcName.get(i)+".txt";
		      cfgObj[i] = new CFGconstructor1(filePath[i],count++);
		      cfgObj[i].computeCFGInfo();
		      cfgObj[i].sortLeaders();
		      cfgObj[i].basicBlockGenerator();
		      g.generateGraph(cfgObj[i].getBasicBlock(), cfgObj[i].getEdge(), cfgObj[i].getGotoList());
		      cfgObj[i].printblock();
		      if(i==cfgObj.length-1){
		    	  continue;
		      }
		      
		     
		      //g.renderGraph();
		      
		    }
		
		for (int i = 0; i < cfgObj.length; i++){
			if(i==cfgObj.length-1){
		    	  continue;
		      }
			 it.computerInterThreadEdges(cfgObj[i].getBasicBlock(),cfgObj[i+1].getBasicBlock(),wnList.get(i+1).getWaitQueue(),wnList.get(i+1).getNotifyQueue(),wnList.get(i).getWaitQueue(),wnList.get(i).getNotifyQueue());
		}
//		CFGconstructor1 cfg = new CFGconstructor1("F:\\individual_project\\bytecodes\\days_assign_At_Notify.txt",1);
//		CFGconstructor1 cfg1 = new CFGconstructor1("F:\\individual_project\\bytecodes\\days_print_At_Notify.txt",2);
//		CFGconstructor1 cfg = new CFGconstructor1("F:\\individual_project\\bytecodes\\days_assign_1.txt",1);
//		CFGconstructor1 cfg1 = new CFGconstructor1("F:\\individual_project\\bytecodes\\days_print_1.txt",2);
//		CFGconstructor1 cfg = new CFGconstructor1("F:\\individual_project\\bytecodes\\producer_parallel_null.txt",1);
//		CFGconstructor1 cfg1 = new CFGconstructor1("F:\\individual_project\\bytecodes\\consumer_parallel_null.txt",2);
//		CFGconstructor cfg = new CFGconstructor("F:\\individual_project\\bytecodes\\caseStudy1_thread1.txt",1);
//		CFGconstructor cfg1 = new CFGconstructor("F:\\individual_project\\bytecodes\\caseStudy1_thread2.txt",2);
//		CFGconstructor1 cfg = new CFGconstructor1("F:\\individual_project\\bytecodes\\SimpleWaitAndNotifyMain.txt",1);
//		CFGconstructor1 cfg1 = new CFGconstructor1("F:\\individual_project\\bytecodes\\SimpleWaitAndNotifyThreadOne.txt",2);
		
		
		
		
		
		
//		cfg.computeCFGInfo();
//		cfg.sortLeaders();
//		cfg.basicBlockGenerator();			
//		g.generateGraph(cfg.getBasicBlock(), cfg.getEdge(),cfg.getGotoList());
//		cfg1.computeCFGInfo();
//		cfg1.sortLeaders();
//		cfg1.basicBlockGenerator();		
//		cfg.printblock();
//		cfg1.printblock();			
//		g.generateGraph(cfg1.getBasicBlock(), cfg1.getEdge(),cfg1.getGotoList());
//		
		
		
		
		
		
		
	
		
		
		
		g.renderGraph();

		g.findHappensBefore();

		//g.findParallelBlocks(cfgObj);
		g.findPBWithHB(cfgObj,filePath);
		
	
		npa.analyzeNull();
		npa.analyzeDeref();
		npa.analyzeIntermetiateWrites();
		npa.printNull();
		
		hb.printHB();
		npa.findNPE();
		
		for(String func:funcName){
		ls.printCode(func);
		}

		System.out.println("analysis completed");
	}

}
