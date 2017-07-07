package in.msruas.project;

public class SyncBlocksOps {
	private static SyncBlocksOps instance = null;
	SyncBlock syncBlock;
	
	private SyncBlocksOps(){
		syncBlock=new SyncBlock();
	}
	
	public static SyncBlocksOps getInstance() {
	      if(instance == null) {
	         instance = new SyncBlocksOps();
	      }
	      return instance;
	   }

	public SyncBlock getSyncBlock() {
		return syncBlock;
	}

	public void setSyncBlock(SyncBlock syncBlock) {
		this.syncBlock = syncBlock;
	}

	public static void setInstance(SyncBlocksOps instance) {
		SyncBlocksOps.instance = instance;
	}
	
}
