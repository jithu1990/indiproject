package in.msruas.project.analysis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

public class ViewHandler extends ViewPart {
        private Label label;
        List list;
        public ViewHandler() {
                super();
                
        }
     public void setFocus() {
                label.setFocus();
        }
     public void createPartControl(Composite parent) {
    	 parent.setLayout(new GridLayout());
    	 list = new List(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
    	 list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    	 
    	 
        }
     public void addEntry(String s){
    	 
    	 list.add(s);
     }

}