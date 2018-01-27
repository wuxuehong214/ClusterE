package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;

import clustere.dialogs.ProteinFunctionDialog;

public class DataSourceAction extends Action {
	
	private IWorkbenchWindow window;
	
	public DataSourceAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("&Gene Annotation");
		setToolTipText("Gene Annotation");
		setImageDescriptor(clustere.Activator.getImageDescriptor("/icons/setting.ico"));
	}
	
	public void run(){
		new ProteinFunctionDialog(window.getShell(),SWT.NONE);
	}

}
