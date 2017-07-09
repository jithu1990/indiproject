package in.msruas.project;

import java.util.ArrayList;

public class WaitAndNotifyOps {
	private static WaitAndNotifyOps instance = null;
	ArrayList<WaitAndNotify> wnList;
	
	private WaitAndNotifyOps(){
		wnList=new ArrayList<>();
	}
	
	public ArrayList<WaitAndNotify> getWnList() {
		return wnList;
	}

	public void setWnList(WaitAndNotify wnList) {
		this.wnList.add(wnList);
	}

	public static WaitAndNotifyOps getInstance() {
	      if(instance == null) {
	         instance = new WaitAndNotifyOps();
	      }
	      return instance;
	   }
}
