package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import clustere.dialogs.ResultClusters;
public class ShowClusterResultAction extends Action {
	
	private IWorkbenchWindow window;
	
	public ShowClusterResultAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("&Clustering Results");
		setToolTipText("Clustering Results");
		ImageDescriptor imgs = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_HELP_CONTENTS);
		this.setImageDescriptor(imgs);
	}
	
	public void run(){
		new ResultClusters(window.getShell(),SWT.NONE);
	}

}
