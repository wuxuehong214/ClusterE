package clustere.actions;

import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.part.EditorPart;

import clustere.editors.ClusterEditor;
import clustere.editors.NetworkView;
import clustere.layout.ClusterLayout;

import com.wuxuehong.bean.Node;

public class NarrowNetworkAction extends Action {

	private EditorPart edit;
	private int style;
	private Vector<Node> v;
	private Canvas canvas;
	
	public NarrowNetworkAction(EditorPart edit,int style){
		this.setText("&Zoom In");
		this.edit = edit;
		this.style = style;
		setToolTipText("narrow down the picture");
		this.setImageDescriptor(clustere.Activator.getImageDescriptor("icons/reduce.ico"));
	}
	
	public void run(){
		if(style==1){
			ClusterEditor ce = (ClusterEditor)edit;
			v = ce.getNodes();
			canvas = ce.getCanvas();
		}else if(style==2){
			NetworkView nk = (NetworkView)edit;
			v = nk.getNodes();
			canvas = nk.getCanvas();
		}else
			return;
		ClusterLayout.reduceNetwork(v, canvas);
	}
}
