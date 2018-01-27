package clustere.actions;

import java.util.Random;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;

import clustere.dialogs.ChangeNetworkDialog;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;
import com.wuxuehong.interfaces.GraphInfo;
/**
 *根据系统中录入的蛋白质网络信息 获取随机蛋白质网络           要求：  边的个数不变
 * @author Administrator
 *
 */
public class RandomNetworkAction extends Action {
	
	 private IWorkbenchWindow window;
	
	public RandomNetworkAction(IWorkbenchWindow window){
		this.setText("&Change Network");
		setToolTipText("change the protein network");
		this.window = window;
		setImageDescriptor(clustere.Activator.getImageDescriptor("/icons/random.ico"));
	}
	
	public void run(){
		new ChangeNetworkDialog(window.getShell(), SWT.NONE).open();
	}

}
