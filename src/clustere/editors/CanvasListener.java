package clustere.editors;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.EditorPart;

import clustere.editors.ClusterEditor;
import clustere.editors.NetworkView;
import clustere.layout.ClusterLayout;
import clustere.Activator;

import com.wuxuehong.bean.*;

/**
 * 实现  对网络图 的拖动
 * @author Administrator
 *
 */
public class CanvasListener implements Listener{

	private Vector<Node> nodelist;
	private boolean isMouseDown = false;
	private boolean isAllmove = true;
	private ClusterEditor clusterEditor;
	private NetworkView networkView;
	private Point point = null;
	private Point old_point = null;
	private int style;
	
	public CanvasListener(EditorPart clusterEditor,int style){
		this.style = style;
		if(style == 1)this.clusterEditor = (ClusterEditor)clusterEditor;
		else 
			this.networkView = (NetworkView)clusterEditor;
	}
	public void handleEvent(Event event) {
		if(style==1)nodelist = clusterEditor.getNodes();
		else nodelist = networkView.getNodes();
		switch(event.type){
		case SWT.MouseDown:{
			isMouseDown = true;
			point = new Point(event.x,event.y);
			old_point = new Point(event.x,event.y);
			for(int i=0;i<nodelist.size();i++){
				if(nodelist.get(i).getRectangle().contains(point)){
					Paramater.SEPARATE_NODE = nodelist.get(i);
					isAllmove = false;
					if(style==1)ClusterEditor.setRedraw();
					else if(style==2) NetworkView.setRedraw();
					break;
				}
			}
			break;
		}
		case SWT.MouseMove:{
			if(isMouseDown){
				if(isAllmove){
					point.x = event.x;
					point.y = event.y;
					int dx = point.x-old_point.x;
					int dy = point.y-old_point.y;
					old_point.x = point.x;
					old_point.y = point.y;
					for(int i=0;i<nodelist.size();i++){
						Node node = nodelist.get(i);
						node.setMx(node.getMx()+dx);
						node.setMy(node.getMy()+dy);
					}
				}else{
					Paramater.SEPARATE_NODE.setMx(event.x);
					Paramater.SEPARATE_NODE.setMy(event.y);
				}
				if(style==1)ClusterEditor.setRedraw();
				else NetworkView.setRedraw();
			}
			break;
		}
		case SWT.MouseUp:{
			isMouseDown = false;
			isAllmove = true;
			Paramater.SEPARATE_NODE = null;
			break;
		}
		case SWT.MouseEnter:{
			System.out.println("鼠标进入....");
			isMouseDown = false;
			isAllmove = true;
			Paramater.SEPARATE_NODE = null;
			if(style == 1) ClusterEditor.canvas.forceFocus();
			else
				NetworkView.canvas.forceFocus();
			break;
		}
		case SWT.MouseWheel:{
			System.out.println("...D.............."+event.count);
			if(event.count == 3)
				ClusterLayout.expandNetwork(nodelist, style==1?ClusterEditor.canvas:NetworkView.canvas);
			else if(event.count == -3)
				ClusterLayout.reduceNetwork(nodelist, style==1?ClusterEditor.canvas:NetworkView.canvas);
		}
		}
	}
}
