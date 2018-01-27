package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;

import clustere.dialogs.PluginManage;

public class PluginManageAction extends Action {
	
	private IWorkbenchWindow window;
	
	public PluginManageAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("&Plugin Manage");
		setToolTipText("plugin manage");
		setImageDescriptor(clustere.Activator.getImageDescriptor("/icons/manage.ico"));
	}
	
	public void run(){
		new PluginManage(window.getShell(), SWT.NONE);
	}
	

}
