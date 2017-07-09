package in.msruas.project;

import java.util.ArrayList;

public class BasicBlockOps {
	private static BasicBlockOps instance = null;
	ArrayList<BasicBlock> basicBlock;
	
	private BasicBlockOps(){
		basicBlock=new ArrayList<>();
	}
	
	public static BasicBlockOps getInstance() {
	      if(instance == null) {
	         instance = new BasicBlockOps();
	      }
	      return instance;
	   }

	public ArrayList<BasicBlock> getBasicBlock() {
		return basicBlock;
	}

	public void setBasicBlock(BasicBlock basicBlock) {
		this.basicBlock.add(basicBlock);
	}
}
