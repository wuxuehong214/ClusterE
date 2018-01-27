package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;

import clustere.dialogs.SettingDialog;

public class ParamaterSetAction extends Action {
	
private IWorkbenchWindow window;
	
	public ParamaterSetAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("&Paramater Set");
		setToolTipText("Paramater set");
		setImageDescriptor(clustere.Activator.getImageDescriptor("/icons/config.ico"));
	}
	
	public void run(){
		new SettingDialog(window.getShell(), SWT.NONE);
	}

}
