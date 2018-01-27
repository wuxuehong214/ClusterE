package clustere.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
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
public class ProgressBarDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private ProgressBar progressBar1;
	private Label label1;
	private Button button1;
	Thread t;

	public ProgressBarDialog(Shell parent, int style,Thread t) {
		super(parent, style);
		open();
		this.t = t;
	}

	public void open() {
		FormData label1LData = new FormData();
		FormData progressBar1LData = new FormData();
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.TITLE | SWT.APPLICATION_MODAL);
//			dialogShell.setImage(new Image(dialogShell.getDisplay(),
//					"icos/open.ico"));
			dialogShell.setText("Please wait...");
			dialogShell.addShellListener(new ShellListener() {

				@Override
				public void shellActivated(ShellEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void shellClosed(ShellEvent arg0) {
					// TODO Auto-generated method stub
					dialogShell.dispose();
				}

				@Override
				public void shellDeactivated(ShellEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void shellDeiconified(ShellEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void shellIconified(ShellEvent arg0) {
					// TODO Auto-generated method stub

				}

			});
			dialogShell.setLayout(new FormLayout());
			{
				label1 = new Label(dialogShell, SWT.NONE);
			}
			{
				button1 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				button1.setText("Cancel");
				button1.addSelectionListener(new SelectionAdapter(){
					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						System.out.println("interrupt");
						t.suspend();
						t.stop();
						dialogShell.dispose();
					}
				});
			}
			{
				progressBar1 = new ProgressBar(dialogShell, SWT.INDETERMINATE);
			}
			progressBar1LData.left = new FormAttachment(0, 1000, 7);
			progressBar1LData.top = new FormAttachment(0, 1000, 51);
			progressBar1LData.width = 473;
			progressBar1LData.height = 26;
			progressBar1.setLayoutData(progressBar1LData);
			label1LData.left = new FormAttachment(0, 1000, 0);
			label1LData.top = new FormAttachment(0, 1000, 12);
			label1LData.width = 492;
			label1LData.height = 33;
			label1.setLayoutData(label1LData);
//			Font font = new Font(dialogShell.getDisplay(), "Arial", 13,
//					SWT.ITALIC);
//			label1.setFont(font);
			FormData button1LData = new FormData();
			button1LData.left = new FormAttachment(0, 1000, 212);
			button1LData.top = new FormAttachment(0, 1000, 90);
			button1LData.width = 56;
			button1LData.height = 23;
			button1.setLayoutData(button1LData);
			dialogShell.setSize(500, 150);
			dialogShell.layout();
			Rectangle rec = parent.getBounds();
			dialogShell.setLocation(rec.x+rec.width/2-250,rec.y+rec.height/2-80);
			dialogShell.open();
			// Display display = dialogShell.getDisplay();
			// while (!dialogShell.isDisposed()) {
			// if (!display.readAndDispatch())
			// display.sleep();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dispose() {
		dialogShell.dispose();
	}

	public Label getLabel() {
		return label1;
	}

}
