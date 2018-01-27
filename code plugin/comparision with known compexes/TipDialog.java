package com.wuxuehong.plugin;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;


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
public class TipDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Text text1;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/


	public TipDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

			FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			dialogShell.setLayout(dialogShellLayout);
			{
				text1 = new Text(dialogShell, SWT.MULTI | SWT.WRAP|SWT.V_SCROLL);
				text1.setText("Complex 0  7\uff1a\nYBR288c\nYCR066w\nYHL020c\nYLR309c\nYML001w\nYMR010w\nYPL195w\nComplex 1  5\nYBR241c\nYBR249c\nYDR105c\nYDR479c\nYLR151c\nComplex 2  6\nYOR175c\nYDL217c\nYDR529c\nYFL012w\nYGR110w\nYGR215w");
			}
			dialogShell.layout();
			dialogShell.pack();			
			Rectangle rec = getParent().getBounds();
    		dialogShell.setLocation(rec.x+rec.width/2,rec.y+160);
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
