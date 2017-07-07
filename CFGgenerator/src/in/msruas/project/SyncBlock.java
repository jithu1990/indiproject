package in.msruas.project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SyncBlock {
	public String fileName;
	public Queue enterLine,exitLine;
	public ArrayList waitList,notifyList;
	
	
	
	public SyncBlock() {
		waitList=new ArrayList<>();
		notifyList=new ArrayList<>();
		enterLine= new LinkedList<>();
		exitLine=new LinkedList<>();
	}

	SyncBlock (String fileName2){
		waitList=new ArrayList<>();
		notifyList=new ArrayList<>();
		enterLine= new LinkedList<>();
		exitLine=new LinkedList<>();
		this.fileName=fileName2;
	}
	
	public ArrayList getWaitList() {
		return waitList;
	}

	public void setWaitList(int line) {
		waitList.add(line);
	}

	public ArrayList getNotifyList() {
		return notifyList;
	}

	public void setNotifyList(int line) {
		notifyList.add(line);
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Queue getEnterLine() {
		return enterLine;
	}

	public void setEnterLine(int line) {
		enterLine.add(line);
	}

	public Queue getExitLine() {
		return exitLine;
	}

	public void setExitLine(int line) {
		exitLine.add(line);
	}
	
	
	
	
	
	
	
}
