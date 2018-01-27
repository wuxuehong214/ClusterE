package clustere.dialogs;

import java.io.File;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;

import clustere.Activator;

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
public class HelpDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private SashForm sashForm1;
	private Tree tree1;
	private Browser browser1;

	public HelpDialog(Shell parent, int style) {
		super(parent, style);
		open();
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.CLOSE|SWT.MAX|SWT.MIN);
			dialogShell.setImage(new Image(parent.getDisplay(),Activator.getImageDescriptor("icons/help.ico").getImageData()));
			dialogShell.setText("ClusterE Help");
			FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			dialogShell.setLayout(dialogShellLayout);
			dialogShell.layout();
			dialogShell.setSize(800,600);		
			{
				sashForm1 = new SashForm(dialogShell, SWT.NONE);
				sashForm1.setSize(60, 30);
				{
					File f = new File("doc");
					String dir = f.getAbsolutePath();
				System.out.println(dir);
					tree1 = new Tree(sashForm1, SWT.NONE);
					tree1.setBounds(32, 199, 792, 566);
					TreeItem item1 = new TreeItem(tree1,SWT.NONE);
					item1.setText("ClusterE Introduction");
					item1.setData("file://"+dir+"/help/ClusterE1.html");
					item1 = new TreeItem(tree1,SWT.NONE);
					item1.setText("ClusterE Basic Operation");
					item1.setData("file://"+dir+"/help/ClusterE2.html");
					item1 = new TreeItem(tree1,SWT.NONE);
					item1.setText("ClusterE Plug-in");
					item1.setData("file://"+dir+"/help/ClusterE3.html");
					item1 = new TreeItem(tree1,SWT.NONE);
					item1.setText("ClusterE Source API");
					item1.setData("file://"+dir+"/index.html");
					tree1.addSelectionListener(new SelectionAdapter(){

						@Override
						public void widgetSelected(SelectionEvent e) {
							// TODO Auto-generated method stub
							TreeItem item = (TreeItem)e.item;
							browser1.setUrl((String)item.getData());
						}
						
					});
				}
				{
					browser1 = new Browser(sashForm1, SWT.NONE);
					browser1.setText("browser1");
					browser1.setBounds(168, 0, 624, 566);
				}
				sashForm1.setWeights(new int[]{1,3});
			}
			dialogShell.setLocation(getParent().toDisplay(80, 0));
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
	
	public static void main(String ars[]){
		new HelpDialog(new Shell(),SWT.NONE);
	}
	
}
