package in.msruas.project.analysis;
import java.util.ArrayList;

public class BasicBlock {
	String blockName, prevBlock, nextBlock, filename;
	int leader, lastInst;
	ArrayList<Integer> instLines;

	BasicBlock() {
		instLines = new ArrayList<Integer>();
	}

	public void addInstLines(int i) {
		instLines.add(i);
	}

	public ArrayList<Integer> getInstLines() {
		return instLines;
	}

	public void setInstLines(ArrayList<Integer> instLines) {
		this.instLines = instLines;
	}

	public int getLastInst() {
		return lastInst;
	}

	public void setLastInst(int lastInst) {
		this.lastInst = lastInst;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getPrevBlock() {
		return prevBlock;
	}

	public void setPrevBlock(String prevBlock) {
		this.prevBlock = prevBlock;
	}

	public String getNextBlock() {
		return nextBlock;
	}

	public void setNextBlock(String nextBlock) {
		this.nextBlock = nextBlock;
	}

	public int getLeader() {
		return leader;
	}

	public void setLeader(int leader) {
		this.leader = leader;
	}

}
