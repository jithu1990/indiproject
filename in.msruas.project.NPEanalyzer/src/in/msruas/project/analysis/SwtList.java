package in.msruas.project.analysis;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

/**
 * @author Steven Holzner
 * 
 */

public class SwtList extends JFrame {
	 private JList<String> countryList;
	 DefaultListModel<String> listModel;
	 JTable jt=new JTable();
	 JScrollPane sp;
	 DefaultTableModel dtm;
	 
	    public SwtList() {
	        //create the model and add elements
	        listModel = new DefaultListModel<>();
	        dtm = new DefaultTableModel(0, 0);
	        String header[] = new String[] { "filename","source", "sink"};
	        dtm.setColumnIdentifiers(header);
	        jt.setModel(dtm);
	        jt.setBounds(30,40,200,300);
	        sp=new JScrollPane(jt);
	        //create the list
	        
	        add(sp);
	         
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setTitle("JList Example");       
	        this.setSize(200,200);
	        this.setLocationRelativeTo(null);
	        this.setVisible(true);
	    }
	    
	    void addElement(String sourceFile,int src,String sinkFile, int snk){
	    	dtm.addRow(new Object[] { sourceFile,src, sinkFile,snk });
	    }
	     
	   
  
}

