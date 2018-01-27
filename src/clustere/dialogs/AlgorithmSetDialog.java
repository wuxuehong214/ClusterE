package clustere.dialogs;

import java.util.Vector;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class AlgorithmSetDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Label label1;
	private Text text1;
	private String[] str;
	private String[] value;
	private String[] result = null;
	private Vector<Text> vector = new Vector<Text>();

	public AlgorithmSetDialog(Shell parent, int style, String[] str,
			String[] value) {
		super(parent, style);
		this.str = str;
		this.value = value;
		open();
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM
					| SWT.APPLICATION_MODAL);
			dialogShell.setText("Paramater Set");
//			dialogShell.setImage(new Image(dialogShell.getDisplay(),
//					"icos/open.ico"));
			GridLayout grid = new GridLayout(2, true);
			dialogShell.setLayout(grid);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			for (int i = 0; i < str.length; i++) {
				new Label(dialogShell, SWT.NONE).setText(str[i]);
				Text t = new Text(dialogShell, SWT.BORDER);
				t.setText(value[i]);
				t.setLayoutData(gd);
				vector.add(t);
			}
			Button confirm = new Button(dialogShell, SWT.NONE);
			confirm.setText("Confirm");
			confirm.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					result = new String[value.length];
					for (int i = 0; i < result.length; i++) {
						result[i] = vector.get(i).getText();
					}
					dialogShell.dispose();
				}
			});
			Button cancel = new Button(dialogShell, SWT.NONE);
			cancel.setText("Cancel");
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					dialogShell.dispose();
				}
			});
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			Rectangle rec = parent.getBounds();
            dialogShell.setLocation(rec.x+200,rec.y+200);
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

	public String[] getResult() {
		return result;
	}

	// public static void main(String args[]){
	// String[] s = {"threod:","cluster size:"};
	// String[] v= {"1.0","3"};
	// AlgorithmSetDialog as = new AlgorithmSetDialog(new Shell(),SWT.NONE,s,v);
	// for(int i=0;i<as.result.length;i++){
	// System.out.println(as.result[i]);
	// }
	// System.out.println(as.getResult());
	// }

}
