package clustere.editors;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.part.EditorPart;

import clustere.Activator;
import clustere.actions.ExpandNetworkAction;
import clustere.actions.NarrowNetworkAction;
import clustere.actions.ParamaterSetAction;
import clustere.actions.SaveAsPictureAction;
import clustere.actions.SetLayoutAction;
import clustere.layout.ClusterLayout;
import clustere.tableViewer.NodeContentProvider;
import clustere.tableViewer.NodeLabelProvider;
import clustere.tableViewer.NodeSorter;
import clustere.views.ViewPart2;

import com.wuxuehong.bean.*;
import com.wuxuehong.interfaces.GraphInfo;

public class ClusterEditor extends EditorPart {
	
	private static Vector<Node> nodelist = new Vector<Node>();
	private static Vector<Edge> edgelist = new Vector<Edge>();
	public static Canvas canvas;
    private static Table table;
    private static TableViewer tv;
    private boolean a = true;
    private Text text;
    
    
    private SaveAsPictureAction sapAction = null;
    private ExpandNetworkAction enAction = null;
    private NarrowNetworkAction nnAction = null;
    private SetLayoutAction setLayoutAction = null;
	public void doSave(IProgressMonitor monitor) {
	}
	public void doSaveAs() {
	}
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
             setSite(site);
             setInput(input);
	}
	public boolean isDirty() {
		return false;
	}
	public boolean isSaveAsAllowed() {
		return false;
	}
	public void createPartControl(Composite parent) {
         SashForm sashForm = new SashForm(parent,SWT.HORIZONTAL);
         SashForm sashForm2 = new SashForm(sashForm,SWT.VERTICAL);
         canvas = new Canvas(sashForm2,SWT.DOUBLE_BUFFERED|SWT.BORDER);
         Composite composite = new Composite(sashForm2,SWT.BORDER);
         composite.setLayout(new GridLayout());
         Label label = new Label(composite,SWT.BORDER);
         label.setText("Description for the selected node");
         label.setFont(new Font(parent.getDisplay(), "Arial", 10, SWT.BOLD
 				| SWT.ITALIC));
         label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
         text = new Text(composite,SWT.WRAP|SWT.MULTI|SWT.V_SCROLL|SWT.BORDER);
         text.setLayoutData(new GridData(GridData.FILL_BOTH));
         
         sashForm2.setWeights(new int[]{4,1});
         canvas.setBackground(new Color(parent.getDisplay(),new RGB(250,250,250)));
         CanvasListener cl = new CanvasListener(this,1);
         canvas.addListener(SWT.MouseDown, cl);
         canvas.addListener(SWT.MouseMove, cl);
         canvas.addListener(SWT.MouseUp, cl);
         canvas.addListener(SWT.MouseEnter, cl);
         canvas.addListener(SWT.MouseWheel, cl);
         Composite composite1 = new Composite(sashForm,SWT.BORDER);
         createTables(composite1);
         sashForm.setWeights(new int[]{4,1});
         sapAction  = new SaveAsPictureAction(canvas);
         enAction = new ExpandNetworkAction(this, 1);
         nnAction = new NarrowNetworkAction(this, 1); 
         setLayoutAction = new SetLayoutAction(this,1);
         addCanvasListener(parent.getDisplay());
         fillePopMenu();
         addEditorToolBar();  //添加编辑器工具栏
         
	}
	public void createTables(Composite composite){
		composite.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(composite,SWT.VERTICAL);
		table = new Table(sashForm,SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn tc1 = new TableColumn(table,SWT.LEFT);
		tc1.setText("Property");
		tc1.setWidth(80);
		TableColumn tc2 = new TableColumn(table,SWT.LEFT);
		tc2.setText("Value");
		tc2.setWidth(100);
		tv = new TableViewer(sashForm,SWT.FULL_SELECTION);
		Table tvTable = tv.getTable();
		tvTable.setHeaderVisible(true);
		tvTable.setLinesVisible(true);
		tvTable.addSelectionListener(new Controllor());
		TableColumn tc3 = new TableColumn(tvTable,SWT.LEFT);
		tc3.setText("NodeID");
		tc3.setWidth(80);
		tc3.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((NodeSorter)tv.getSorter()).doSort(a? -1:1);
				tv.refresh();
			}
		});
		tc3 = new TableColumn(tvTable,SWT.LEFT);
		tc3.setText("Neighbours");
		tc3.setWidth(60);
		tc3.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((NodeSorter)tv.getSorter()).doSort(a? -2:2);
				tv.refresh();
			}
		});
		sashForm.setWeights(new int[]{1,3});
		tv.setContentProvider(new NodeContentProvider());
		tv.setLabelProvider(new NodeLabelProvider());
		tv.setSorter(new NodeSorter());
	}
	public void addCanvasListener(final Display display){
		canvas.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				if(nodelist.size()!=0){
					gc.setFont(new Font(canvas.getDisplay(), "Arial", nodelist.get(0).getFontSize(), SWT.NONE));
				}
				gc.setBackground(Paramater.BACK_COLOR);
				gc.setForeground(Paramater.FORE_COLOR);
				gc.setLineWidth((int) (Paramater.LINE_WIDTH/nodelist.get(0).getExpand_paramater()));
				for(int i=0;i<edgelist.size();i++)
					edgelist.get(i).drawMe(gc, display);
				for(int i=0;i<nodelist.size();i++)
					nodelist.get(i).drawMe(gc, display);
				gc.setBackground(Paramater.SEPARATE_COLOR);
				    if(Paramater.SEPARATE_NODE!=null)  Paramater.SEPARATE_NODE.drawMe(gc, display);
			}
		});
		canvas.addListener(SWT.MouseEnter,new Listener(){
			public void handleEvent(Event event) {
				StatusLineContributionItem statusItem = new StatusLineContributionItem("");
				statusItem.setText("click expand");
			}
		});
	}
	public void setRedraw(Vector<Node> nodelist,Vector<Edge> edgelist){
		ClusterLayout.setLayout(ClusterLayout.LAYOUT_STYLE, nodelist, canvas);
		if(nodelist!=null)this.nodelist = nodelist;
		if(edgelist!=null)this.edgelist = edgelist;
		canvas.redraw();
	}
	public void setTableRefresh(int nodeNum,int edgeNum){
		table.removeAll();
		float avgDegree = 2*(float)edgeNum/(float)nodeNum;
		new TableItem(table,SWT.LEFT).setText(new String[]{"Nodes",""+nodeNum+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"Edges",""+edgeNum+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"AvgDegree",""+avgDegree+""});
	}
	public void setTableViewerRefresh(List<Node> list){
		tv.setInput(list);
		tv.refresh();
	}
	
	class Controllor extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			int index = tv.getTable().getSelectionIndex();
			Node node = (Node)tv.getElementAt(index);
			text.setText("");
			text.append("NodeID:\t"+node.getNodeID()+"\n");
			text.append("Neighbours:\t"+getString(node.getNeighbours())+"\n");
			text.append("Annotation:\t"+Paramater.currentProteinFunction.get(node.getNodeID().toUpperCase())+"\n");
			chooseNode(node);
		}
	}
	public String getString(Vector<Node> nei){
		String result = "";
		for(int i=0;i<nei.size();i++)
			result += nei.get(i).getNodeID()+" ";
		return result;
	}
	/**
	 * 右键菜单
	 */
	public void fillePopMenu(){
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(canvas);
		canvas.setMenu(menu);
		menuManager.add(sapAction);
		menuManager.add(enAction);
		menuManager.add(nnAction);
		menuManager.add(setLayoutAction);
	}
	public void addEditorToolBar(){
		IActionBars bars = getEditorSite().getActionBars();
		IToolBarManager iToolBar = bars.getToolBarManager();
		iToolBar.add(sapAction);
		iToolBar.add(enAction);
		iToolBar.add(nnAction);
		iToolBar.add(setLayoutAction);
	}
	public void chooseNode(Node node){
		int width = canvas.getBounds().width;
		int height = canvas.getBounds().height;
		int dx = width/2-node.getMx();
		int dy = height/2-node.getMy();
		for(int i=0;i<nodelist.size();i++){
			Node n = nodelist.get(i);
			n.setMx(n.getMx()+dx);
			n.setMy(n.getMy()+dy);
		}
		Paramater.SEPARATE_NODE = node;
		canvas.redraw();
	}
	public static void setRedraw(){
		if(canvas!=null&&!canvas.isDisposed())
		  canvas.redraw();
	}
	/**
	 * 变量刷新
	 */
	public static void refresh(){
		nodelist.clear();
		edgelist.clear();
	}
	public Canvas getCanvas(){
		return canvas;
	}
	public Vector<Node> getNodes(){
		return nodelist;
	}
	public void setFocus() {
	}
}
