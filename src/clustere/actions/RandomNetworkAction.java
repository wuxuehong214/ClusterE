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
 *����ϵͳ��¼��ĵ�����������Ϣ ��ȡ�������������           Ҫ��  �ߵĸ�������
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
