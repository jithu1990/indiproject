package in.msruas.project.analysis;

import java.util.Queue;

public class WaitAndNotify {
	Queue waitQueue,notifyQueue ;
	String filename;
	public Queue getWaitQueue() {
		return waitQueue;
	}
	public void setWaitQueue(Queue waitQueue) {
		this.waitQueue = waitQueue;
	}
	public Queue getNotifyQueue() {
		return notifyQueue;
	}
	public void setNotifyQueue(Queue notifyQueue) {
		this.notifyQueue = notifyQueue;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public WaitAndNotify(String filename) {
		super();
		this.filename = filename;
	}
	
}
