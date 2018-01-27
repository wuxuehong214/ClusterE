package clustere.editors;

import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import clustere.actions.ExpandNetworkAction;
import clustere.actions.NarrowNetworkAction;
import clustere.actions.SaveAsPictureAction;
import clustere.actions.SetLayoutAction;
import clustere.layout.ClusterLayout;

import com.wuxuehong.bean.*;
import com.wuxuehong.interfaces.GraphInfo;
public class NetworkView extends EditorPart {

	static Canvas canvas;
	
	private SaveAsPictureAction sapAction = null;
    private ExpandNetworkAction enAction = null;
    private NarrowNetworkAction nnAction = null;
    private SetLayoutAction setLayoutAction = null;
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		setSite(site);
        setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		final Display display = parent.getDisplay();
		canvas = new Canvas(parent,SWT.DOUBLE_BUFFERED|SWT.BORDER);
		canvas.setBackground(new Color(parent.getDisplay(),new RGB(250,250,250)));
		 CanvasListener cl = new CanvasListener(this,2);
         canvas.addListener(SWT.MouseDown, cl);
         canvas.addListener(SWT.MouseMove, cl);
         canvas.addListener(SWT.MouseUp, cl);
         canvas.addListener(SWT.MouseEnter, cl);
         canvas.addListener(SWT.MouseWheel, cl);
         
         sapAction  = new SaveAsPictureAction(canvas);
         enAction = new ExpandNetworkAction(this, 2);
         nnAction = new NarrowNetworkAction(this, 2); 
         setLayoutAction = new SetLayoutAction(this,2);
         for(int i=0;i<GraphInfo.nodelist.size();i++){
        	 Node n = GraphInfo.nodelist.get(i);
        	 n.setExpand_paramater(5);
         }
		 ClusterLayout.setLayout(ClusterLayout. OVAL, GraphInfo.nodelist, canvas);
		canvas.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				if(GraphInfo.nodelist.size()>0)
				gc.setFont(new Font(canvas.getDisplay(), "Arial", GraphInfo.nodelist.get(0).getFontSize(), SWT.NONE));
				gc.setBackground(Paramater.BACK_COLOR);
				gc.setForeground(Paramater.FORE_COLOR);
				gc.setLineWidth((int) (Paramater.LINE_WIDTH/GraphInfo.nodelist.get(0).getExpand_paramater()));
				for(int i=0;i<GraphInfo.edgelist.size();i++)
					GraphInfo.edgelist.get(i).drawMe(gc, display);
				for(int j=0;j<GraphInfo.nodelist.size();j++)
					GraphInfo.nodelist.get(j).drawMe(gc, display);
				gc.setBackground(Paramater.SEPARATE_COLOR);
				if(Paramater.SEPARATE_NODE!=null)
					Paramater.SEPARATE_NODE.drawMe(gc, display);
			}
		});
		fillePopMenu();
		addEditorToolBar();
	}
	
	public void addCanvasListener(final Display display){
		
	}
	/**
	 * Ìí¼Ó±à¼­Æ÷¹¤¾ßÀ¸
	 */
	public void addEditorToolBar(){
		IActionBars bars = getEditorSite().getActionBars();
		IToolBarManager iToolBar = bars.getToolBarManager();
		iToolBar.add(sapAction);
		iToolBar.add(enAction);
		iToolBar.add(nnAction);
		iToolBar.add(setLayoutAction);
	}
	
	public static void update(){
		if(canvas==null||canvas.isDisposed())return;
		for(int i=0;i<GraphInfo.nodelist.size();i++){
	       	 Node n = GraphInfo.nodelist.get(i);
	       	 n.setExpand_paramater(5);
	        }
			 ClusterLayout.Calculate2(GraphInfo.nodelist, canvas);
			 canvas.redraw();
	}
	
//	public void setRedraw(Vector<Node> nodelist,Vector<Edge> edgelist){
//		ClusterLayout.Calculate2(nodelist, canvas);
//		canvas.redraw();
//	}
	public void fillePopMenu(){
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(canvas);
		canvas.setMenu(menu);
		menuManager.add(sapAction);
		menuManager.add(enAction);
		menuManager.add(nnAction);
		menuManager.add(setLayoutAction);
	}

	public static void setRedraw(){
		if(canvas!=null&&!canvas.isDisposed())canvas.redraw();
	}
	
	public Canvas getCanvas(){
		return canvas;
	}

	public Vector<Node> getNodes(){
		return GraphInfo.nodelist;
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
