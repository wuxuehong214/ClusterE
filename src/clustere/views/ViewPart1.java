package clustere.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;

import clustere.actions.ClusterShowWayAction;
import clustere.editors.ClusterEditor;
import clustere.editors.ClusterEditorInput;
import clustere.pluginAction.PluginAlgorithmAction;
import clustere.treeViewer.TreeElement;
import clustere.treeViewer.TreeViewerContentProvider;
import clustere.treeViewer.TreeViewerLabelProvider;

public class ViewPart1 extends ViewPart {

	private static TreeViewer tv;
	private ClusterShowWayAction csw;
	 public static List<TreeElement> list = new ArrayList<TreeElement>();
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub 
		tv = new TreeViewer(parent,SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL);
		tv.setContentProvider(new TreeViewerContentProvider());
		tv.setLabelProvider(new TreeViewerLabelProvider());
		tv.setInput(list);
		hookDoubleClickAction();
		csw = new ClusterShowWayAction();
		fillListContextMenu();
		fillViewToolBarAction();
		fillViewToolBarContectMenu();
	}
	
	public static void updateInput(){
		if(tv==null||tv.getTree().isDisposed())return;
		tv.setInput(list);
		tv.refresh();
	}
	
	public static void refresh(){
		list.clear();
		if(tv==null||tv.getTree().isDisposed())return;
		tv.setInput(list);
		tv.refresh();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	public void fillViewToolBarAction(){
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager toolBar = bars.getToolBarManager();
		toolBar.add(csw);
	}
	
	public void fillViewToolBarContectMenu(){
		IActionBars bars = getViewSite().getActionBars();
		IMenuManager toolBarMenu = bars.getMenuManager();
		toolBarMenu.add(csw);
	}
	
	public void fillListContextMenu(){
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(tv.getTree());
		tv.getTree().setMenu(menu);
		menuManager.add(csw);
	}
	
	private void hookDoubleClickAction(){
		tv.getTree().addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				  Paramater.SEPARATE_NODE = null;
				  String editorID = "ClusterE.editor1";
				  IEditorInput editorInput = ClusterEditorInput.getInstance();
		          IWorkbenchPage workbenchPage = getViewSite().getPage();
		          IEditorPart editor = workbenchPage.findEditor(editorInput);
		           if(editor!=null){
		        	   workbenchPage.bringToTop(editor);
		           }else {
		        	   try {
						editor = workbenchPage.openEditor(editorInput, editorID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
		           }
		           ISelection seleciton = tv.getSelection();
		           TreeElement treeElement = (TreeElement)((IStructuredSelection)seleciton).getFirstElement();
		           ClusterEditor clusterEditor = (ClusterEditor)editor;
		           List<Node> list = new ArrayList<Node>();
		           if(Paramater.CLUSTER_SHOW_MODE==Paramater.CLUSTER){
		        	   for(int i=0;i<treeElement.getNodes().size();i++)
		        		   treeElement.getNodes().get(i).setExpand_paramater(1);   //还原尺寸
		           clusterEditor.setRedraw(treeElement.getNodes(), treeElement.getEdges());//显示图形
		           clusterEditor.setTableRefresh(treeElement.getNodes().size(), treeElement.getEdges().size());
		           list.addAll(treeElement.getNodes());
		           clusterEditor.setTableViewerRefresh(list);
		           }else if(Paramater.CLUSTER_SHOW_MODE == Paramater.OVERLAP){
		        	   Vector<Node>  tempNodes = new Vector<Node>();
		        	   Vector<Edge>  tempEdges = new Vector<Edge>();
		        	   Node node = treeElement.getNode();
		        	   node.setExpand_paramater(1);
		        	   Paramater.SEPARATE_NODE = node;
		        	   tempNodes.add(node);
		        	   for(int i=0;i<node.getNeighbour_NUM();i++){
		        		   Node n = node.getNeighbours().get(i);
		        		   n.setExpand_paramater(1);//还原尺寸
		        		   tempNodes.add(n);
		        		   tempEdges.add(new Edge(node,n));
		        	   }
 		        	   clusterEditor.setRedraw(tempNodes, tempEdges);
 		        	   clusterEditor.setTableRefresh(tempNodes.size(), tempEdges.size());
 		        	   list.addAll(tempNodes);
 		        	   clusterEditor.setTableViewerRefresh(list);
		           }
			}
		});
	}

}
