package clustere.actions;

import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.ui.part.EditorPart;

import clustere.editors.ClusterEditor;
import clustere.editors.NetworkView;
import clustere.layout.ClusterLayout;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;

public class ExpandNetworkAction extends Action {

	private EditorPart edit;
	private int style;
	private Vector<Node> v;
	private Canvas canvas;
	
	public ExpandNetworkAction(EditorPart edit,int style){
		this.setText("&Zoom Out");
		this.edit = edit;
		this.style = style;
		setToolTipText("narrow down the picture");
		this.setImageDescriptor(clustere.Activator.getImageDescriptor("icons/expand.ico"));
	}
	
	public void run(){
		if(style==1){
			ClusterEditor ce = (ClusterEditor)edit;
			v = ce.getNodes();
			canvas = ce.getCanvas();
		}else if(style==2){
			NetworkView nv = (NetworkView)edit;
			v = GraphInfo.nodelist;
			canvas = nv.getCanvas();
		}else
			return;
		ClusterLayout.expandNetwork(v, canvas);
	}
	
}
