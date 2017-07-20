package in.msruas.project.analysis;

import java.util.ArrayList;

public class SyncBlocksOps {
	private static SyncBlocksOps instance = null;
	ArrayList<SyncBlock> syncBlock;
	
	private SyncBlocksOps(){
		syncBlock=new ArrayList<>();
	}
	
	public static SyncBlocksOps getInstance() {
	      if(instance == null) {
	         instance = new SyncBlocksOps();
	      }
	      return instance;
	   }

	public ArrayList<SyncBlock> getSyncBlock() {
		return syncBlock;
	}

	public void setSyncBlock(SyncBlock syncBlock) {
		this.syncBlock.add(syncBlock);
	}

	public static void setInstance(SyncBlocksOps instance) {
		SyncBlocksOps.instance = instance;
	}
	
	
	
}
