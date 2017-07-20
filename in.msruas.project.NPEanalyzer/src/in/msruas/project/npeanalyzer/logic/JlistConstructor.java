package in.msruas.project.npeanalyzer.logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import in.msruas.project.analysis.CFGgenerator;

public class JlistConstructor extends JFrame implements ActionListener {
	 private JList<String> countryList;
	 private JButton checkButton;
	 JScrollPane pane;

	 ArrayList<String> listItems,funcName=new ArrayList<>();
	 String path;
	 JList list;
	 
	  
	   
	        public JlistConstructor(ArrayList<String> listItems) {
		super();
		this.listItems = listItems;
	}

//	        public void Frame()
//	        {
//	        	DefaultListModel<String> listModel = new DefaultListModel<>();
//	            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//	            for(String item: listItems){
//	            	 listModel.addElement(item);
//	            }
//
//	            
//	            list = new JList(listModel);
//	            checkButton = new JButton("Check");
//	            checkButton.addActionListener(this);
//	            pane= new JScrollPane(list); 
//	            // add list to frame
//	            JPanel panel = new JPanel();
//	            panel.add(pane);
//	            panel.add(checkButton);
//	            
//	            add(panel);
//	            setVisible(true);
//	        }

	 
	    public  JlistConstructor runList(ArrayList<String> arg, ExecutionEvent event) throws ExecutionException {
//	        SwingUtilities.invokeLater(new Runnable() {
//	            @Override
//	            public void run() {
//	                new JlistConstructor(arg);
//	            }
//	        });return new JlistConstructor(arg);
	    	
	    	
	    	DefaultListModel<String> listModel = new DefaultListModel<>();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            for(String item: listItems){
            	 listModel.addElement(item);
            }

            
            list = new JList(listModel);
            checkButton = new JButton("Check");
            checkButton.addActionListener(this);
            pane= new JScrollPane(list); 
            // add list to frame
            JPanel panel = new JPanel();
            panel.add(pane);
            panel.add(checkButton);
            
            add(panel);
            setVisible(true);
            return null;
//	    	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//	    	Composite composite = new Composite(window.getShell(), SWT.EMBEDDED);
//	        java.awt.Frame frame = SWT_AWT.new_Frame(composite);
//	        frame.add(pane);
//	        composite.setSize(350, 350); 
				
	    }
	        
	     

		@Override
		public void actionPerformed(ActionEvent e) {
			ExtractByteCode extractByte=new ExtractByteCode();
			 if (e.getActionCommand().equals("Check"))
	            {
	                int[] index = list.getSelectedIndices();
	                //System.out.println("Index Selected: " + index);
	                List s =  list.getSelectedValuesList();
	                for(Object item:s){
	                	System.out.println("Value Selected: " + item.toString());
	                }
	               
	               boolean status=extractByte.runjavap(s);
	               
	               if(!status){
	            	   System.out.println("there is some problem. bytecode extraction failed");
	               }
	               else{
	            	   System.out.println("bytecode extracted.");
	               }
	               dispose();
	               
	               try {
					extractByte.getMethodByteCodes();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	               
	               /*start the analysis of bytecode*/
	               for(Object strobj:s){
	       			String str=(String)strobj;
	       			String[] str_array = str.split("\\$");
	       			path=str_array[0];
	       			funcName.add(str_array[1]);
	       			path = path.replace('/', '\\');
	       		}
	            
	               CFGgenerator cfgen=new CFGgenerator();
	               try {
					cfgen.main(path,funcName);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	               
	                
	            }
	        }
			
		 
}
