package clustere.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;

import clustere.views.AlgorithmInfoView;

import com.wuxuehong.bean.AlgorithmInfo;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ResultClusters extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Table table1;
	private Button button1;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
//	public static void main(String[] args) {
//		try {
//			Display display = Display.getDefault();
//			Shell shell = new Shell(display);
//			ResultClusters inst = new ResultClusters(shell, SWT.NULL);
//			inst.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public ResultClusters(Shell parent, int style) {
		super(parent, style);
		open();
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialogShell.setText("Cluster Results");
			dialogShell.setLayout(null);
			{
				table1 = new Table(dialogShell, SWT.BORDER|SWT.FULL_SELECTION);
				table1.setBounds(12, 14, 528, 167);
				table1.setLinesVisible(true);
				table1.setHeaderVisible(true);
				TableColumn tc1 = new TableColumn(table1,SWT.LEFT);
				tc1.setText("Algorithm");
				tc1.setWidth(120);
				TableColumn tc2 = new TableColumn(table1,SWT.LEFT);
				tc2.setText("Cluster Num");
				tc2.setWidth(100);
				TableColumn tc3 = new TableColumn(table1,SWT.LEFT);
				tc3.setText("Max size");
				tc3.setWidth(100);
				TableColumn tc4 = new TableColumn(table1,SWT.LEFT);
				tc4.setText("Min Size");
				tc4.setWidth(100);
				TableColumn tc5 = new TableColumn(table1,SWT.LEFT);
				tc5.setText("Avg degree");
				tc5.setWidth(100);
				
				for(int i=0;i<AlgorithmInfoView.infoList.size();i++){
					AlgorithmInfo  ai = AlgorithmInfoView.infoList.get(i);
					new TableItem(table1,SWT.LEFT).setText(new String[]{ai.getName(),""+ai.getTotalCluster()+"",""+ai.getMax()+"",""+ai.getMin()+"",""+ai.getDegree()+""});
				}
			}
			{
				button1 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				button1.setText("Close");
				button1.setBounds(245, 191, 60, 30);
				button1.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent e) {
						dialogShell.dispose();
					}
					
				});
			}
			dialogShell.layout();
				dialogShell.setSize(560, 250);		
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
