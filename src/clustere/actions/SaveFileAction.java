package clustere.actions;

import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;

import clustere.dialogs.SaveOptionDialog;

import com.wuxuehong.bean.Paramater;


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
public class SaveFileAction extends Action {

	IWorkbenchWindow  window;
	
	public SaveFileAction(IWorkbenchWindow  window){
		this.window = window;
		this.setText("&Save");
		this.setImageDescriptor(clustere.Activator.getImageDescriptor("icons/filesave.ico"));
	}
	
	public void run(){
		Set algorithms = Paramater.algorithmsResults.keySet();
		if(algorithms.size()==0){
			MessageDialog.openWarning(window.getShell(), "Warning", "There is no result to save!");
		}else{
			new SaveOptionDialog(window.getShell(),SWT.NONE);
		}
	}
}
