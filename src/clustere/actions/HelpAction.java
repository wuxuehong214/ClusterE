package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import clustere.dialogs.HelpDialog;

public class HelpAction extends Action {

private IWorkbenchWindow window;
	
	public HelpAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("&Help content");
		setToolTipText("help content");
//		ImageDescriptor imgs = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_HELP_CONTENTS);
		this.setImageDescriptor(clustere.Activator.getImageDescriptor("icons/help.ico"));
	}
	
	public void run(){
		new HelpDialog(window.getShell(), SWT.NONE);
	}
	
}
