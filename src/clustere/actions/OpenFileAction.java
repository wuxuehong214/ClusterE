package clustere.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.internal.module.GroupingChecker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.WorkbenchPage;

import com.wuxuehong.bean.AlgorithmColor;
import com.wuxuehong.bean.AlgorithmInfo;
import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;
import com.wuxuehong.interfaces.GraphInfo;

import com.wuxuehong.bean.*;

import clustere.Activator;
import clustere.dialogs.ProgressBarDialog;
import clustere.editors.ClusterEditor;
import clustere.editors.ClusterEditorInput;
import clustere.editors.EvaluationView;
import clustere.editors.NetworkView;
import clustere.editors.NetworkViewInput;
import clustere.pluginAction.PluginAlgorithmAction;
import clustere.pluginLoader.LoaderServer;
import clustere.treeViewer.TreeElement;
import clustere.views.AlgorithmInfoView;
import clustere.views.CurrentNetworkView;
import clustere.views.ViewPart1;
import clustere.views.ViewPart2;

public class OpenFileAction extends Action {
	
	public OpenFileAction(){
		super("&Open",Action.AS_DROP_DOWN_MENU);
		setToolTipText("Open File");
		setImageDescriptor(clustere.Activator.getImageDescriptor("/icons/file_open.ico"));
		setMenuCreator(new IMenuCreator() {
			@Override
			public Menu getMenu(Menu parent) {
				// TODO Auto-generated method stub
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Text File");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						openTextFile();
					}
				});
				MenuItem item2 = new MenuItem(menu,SWT.NONE);
				item2.setText("&Clusters");
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
//						openExistClusters();
						openSavedFile();
					}
				});
				return menu;
			}
			
			@Override
			public Menu getMenu(Control parent) {
				// TODO Auto-generated method stub
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Text File");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						openTextFile();
					}
				});
				MenuItem item2 = new MenuItem(menu,SWT.NONE);
				item2.setText("&Clusters");
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
//						openExistClusters();
						openSavedFile();
					}
				});
				return menu;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void run(){
		openTextFile();
	}
	
	/**
	 * �����µ��ļ���ʱ�����б���ˢ��
	 */
	public void paramRefresh(){
		GraphInfo.refresh();                //����װ������Ϣ������ ��ʼ��
    	ClusterEditor.refresh();           //��ʾ�صı༭�� �����ʼ��
    	Paramater.refresh();				//��ʼ����ϵͳ����
    	EvaluationView.refresh();			//���������༭�� �����ʼ��
    	ViewPart1.refresh();          //��ͼһ������������ͼ ��ʼ��
    	ViewPart2.refresh();            //��ͼ�����������磩��ͼ��ʼ��
    	CurrentNetworkView.refresh();  //��ǰ������Ϣ��ͼ  ��ʼ��
    	AlgorithmInfoView.refresh();      //��ǰ�㷨��Ϣ��ͼ ��ʼ��
    	LoaderServer.pluginRefresh();       //����б�����ʼ��
    	IWorkbenchPage workbenchpage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	workbenchpage.closeAllEditors(true);
	}
	/**
	 * ��ȡ�����������ļ�  .txt
	 */
	public void openTextFile(){
		if(GraphInfo.nodelist.size()!=0||ViewPart1.list.size()!=0||Paramater.algorithmsResults.size()!=0){
		   boolean confirm = MessageDialog.openConfirm(new Shell(), "Warning", "A file has been inputed,Do you really want to open a new ?");
		    if(!confirm)
		          return;
		}
		FileDialog openDialog = new FileDialog(new Shell(), SWT.OPEN);
		openDialog.setText("Choose the file");
		openDialog.setFilterExtensions(new String[] { "*.*","*.txt" });
		openDialog.setFilterNames(new String[] {"�����ļ�*.*", "�ı���ʽ(*.txt)" });
		openDialog.setFilterPath("c:/");
		String name = openDialog.open();
		if (name == null || name.equals("")){
			return;
		}else{
			if(GraphInfo.nodelist.size()!=0||ViewPart1.list.size()!=0||Paramater.algorithmsResults.size()!=0)
			paramRefresh();
		}
		BufferedReader br = null;
		String str;
		Scanner s;
		String tempstr1;
		String tempstr2;
		Node tempnode1;
		Node tempnode2;
		Edge tempedge;
		try {
			br = new BufferedReader(new FileReader(new File(name)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			str = br.readLine();
			while (str != null) {
				str = str.toUpperCase();
				s = new Scanner(str);
//				while (s.hasNext()) {
					tempstr1 = s.next(); // ��һ���ڵ�
					tempnode1 = new Node(tempstr1);
					if (GraphInfo.nodemap.get(tempstr1) == null) {
						GraphInfo.nodemap.put(tempstr1, tempnode1);
						GraphInfo.nodelist.add(tempnode1);
					} else
						tempnode1 = GraphInfo.nodemap.get(tempstr1);
					tempstr2 = s.next(); // �ڶ����ڵ�
					tempnode2 = new Node(tempstr2);
					if (GraphInfo.nodemap.get(tempstr2) == null) {
						GraphInfo.nodemap.put(tempstr2, tempnode2);
						GraphInfo.nodelist.add(tempnode2);
					} else
						tempnode2 = GraphInfo.nodemap.get(tempstr2);
					tempedge = new Edge(tempnode1, tempnode2);
					if(s.hasNext()){
						double degree = s.nextDouble();
						tempedge.setWeight(degree);
					}
					if ((GraphInfo.edgemap.get(tempstr2 + tempstr1) == null)
							&& (GraphInfo.edgemap.get(tempstr1 + tempstr2) == null)) {
						GraphInfo.edgemap.put(tempstr2 + tempstr1, tempedge);
						GraphInfo.edgemap.put(tempstr1 + tempstr2, tempedge);
						GraphInfo.edgelist.add(tempedge);
						tempnode1.getNeighbours().add(tempnode2);
						tempnode2.getNeighbours().add(tempnode1);
					}
//					break;
//				}
				str = br.readLine();
			}
			br.close();
	System.out.println(GraphInfo.edgelist.size()+"$$$$$$$$$$$$$$$$$$$");
		} catch (Exception e) {
		 MessageDialog.openError(null, "ERROE", "File read exception!");
		 GraphInfo.refresh();
		 return;
		}
		CurrentNetworkView.update();
		NetworkView.update();
		MessageDialog.openInformation(new Shell(), "Information", "Total Nodes:"+GraphInfo.nodelist.size()+" \n Total Edges:"+GraphInfo.edgelist.size());
	}

	public void openNetworkView(){
		String editorID = "ClusterE.editor3";
		 IEditorInput editorInput = NetworkViewInput.getInstance();
		 IWorkbenchPage workbenchPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 NetworkView editor = null;
		try {
			editor = (NetworkView)workbenchPage.openEditor(editorInput, editorID);
//			editor.setRedraw(GraphInfo.nodelist,GraphInfo.edgelist);
//			System.out.println("going1111111111");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �򿪱�����ļ�
	 */
	public void openExistClusters(){
		if(GraphInfo.nodelist.size()!=0||ViewPart1.list.size()!=0||Paramater.algorithmsResults.size()!=0){
			   boolean confirm = MessageDialog.openConfirm(new Shell(), "Warning", "A file has been inputed,Do you really want to open a new ?");
			    if(!confirm)
			          return;
			}
		FileDialog fd = new FileDialog(new Shell(),SWT.OPEN);
		fd.setFilterExtensions(new String[] { "*.*","*.txt" });
		fd.setFilterNames(new String[] {"�����ļ�*.*", "�ı���ʽ(*.txt)" });
		String filename = fd.open();
		if(filename==null||filename.equals("")){
			return;
		}else {
			paramRefresh();
		}
		String algorithmName = null;
		Vector[] v = null;
		Scanner scanner = null;
		TreeElement rootElement = new TreeElement();
		TreeElement childElement = null;
		Vector<Edge> edgelist = null;
		HashMap<String,Edge> edgeMap = null;
		Vector<Node> rootNodes = new Vector<Node>();
		Vector<Edge> rootEdges = new Vector<Edge>();
		HashMap<String,Edge> rootMap = new HashMap<String,Edge>();
		HashMap<String,Node> rootNodesMap = new HashMap<String,Node>();
		HashMap<String,Node> tempNodesMap = new HashMap<String,Node>();
		try{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = br.readLine();
		int index1 = str.indexOf(':');
		int index2 = str.lastIndexOf(':');
		algorithmName = str.substring(0,index1);
		rootElement.setEdges(rootEdges);
		rootElement.setNodes(rootNodes);
		int size = Integer.parseInt(str.substring(index2+1, str.length()));
		rootElement.setName(algorithmName+"("+size+")");
		v = new Vector[size];
		int index = -1;
		str = br.readLine();
		str = br.readLine();
		while(str!=null){
			if(str.startsWith("*******"))break;
			if(str.startsWith("Cluster:")){
				index++;
				v[index] = new Vector();
				edgelist = new Vector<Edge>();
				edgeMap = new HashMap<String,Edge>();
				tempNodesMap.clear();
				childElement = new TreeElement();
				rootElement.addChild(childElement);
				childElement.setNodes(v[index]);
				childElement.setEdges(edgelist);
				Node temp ;
				if(rootNodesMap.get("Cluster"+index)!=null){
					temp = rootNodesMap.get("Cluster"+index);
				}else{
				   temp = new Node("Cluster"+index);
				   rootNodes.add(temp);
				    rootNodesMap.put("Cluster"+index, temp);
				}
				childElement.setNode(temp);
				scanner = new Scanner(str);
				String s1 = scanner.next();
				int scope = scanner.nextInt();
				temp.setScope(scope);
				childElement.setName(s1+"("+scope+")");
				while(scanner.hasNext()){
					String nodeID = scanner.next();
					Node n1;
					if(rootNodesMap.get(nodeID)!=null){
						n1 = rootNodesMap.get(nodeID);
					}else{
						n1 = new Node(nodeID);
						rootNodes.add(n1);
						rootNodesMap.put(nodeID, n1);
					}
					if(rootMap.get(temp.getNodeID()+n1.getNodeID())==null){
						Edge edge = new Edge(temp,n1);
						temp.getNeighbours().add(n1);
						n1.getNeighbours().add(temp);
						rootEdges.add(edge);
						rootMap.put(temp.getNodeID()+n1.getNodeID(), edge);
						rootMap.put(n1.getNodeID()+temp.getNodeID(), edge);
					}
				}
			}else{
				scanner = new Scanner(str);
				String str1 = scanner.next();
				Node nn1;
				if(tempNodesMap.get(str1)!=null)
					nn1 = tempNodesMap.get(str1);
				else {
					nn1 = new Node(str1);
					tempNodesMap.put(str1, nn1);
					v[index].add(nn1);
				}
				while(scanner.hasNext()){
					String id = scanner.next();
					Node nn2;
					if(tempNodesMap.get(id)!=null){
						nn2 = tempNodesMap.get(id);
					}else{
						nn2 = new Node(id);
						tempNodesMap.put(id, nn2);
						v[index].add(nn2);
					}
					if(edgeMap.get(nn1.getNodeID()+nn2.getNodeID())==null){
						Edge edge = new Edge(nn1,nn2);
						nn1.getNeighbours().add(nn2);
						nn2.getNeighbours().add(nn1);
						edgelist.add(edge);
						edgeMap.put(nn1.getNodeID()+nn2.getNodeID(), edge);
						edgeMap.put(nn2.getNodeID()+nn1.getNodeID(), edge);
					}
				}
			}
			str = br.readLine();
		}
		str = br.readLine();
		Scanner s;
		String tempstr1;
		String tempstr2;
		Node tempnode1;
		Node tempnode2;
		Edge tempedge;
		while (str != null) {               //��ȡԭʼ�����ļ�
			str = str.toUpperCase();
			s = new Scanner(str);
			while (s.hasNext()) {
				tempstr1 = s.next(); // ��һ���ڵ�
				tempnode1 = new Node(tempstr1);
				if (GraphInfo.nodemap.get(tempstr1) == null) {
					GraphInfo.nodemap.put(tempstr1, tempnode1);
					GraphInfo.nodelist.add(tempnode1);
				} else
					tempnode1 = GraphInfo.nodemap.get(tempstr1);
				tempstr2 = s.next(); // �ڶ����ڵ�
				tempnode2 = new Node(tempstr2);
				if (GraphInfo.nodemap.get(tempstr2) == null) {
					GraphInfo.nodemap.put(tempstr2, tempnode2);
					GraphInfo.nodelist.add(tempnode2);
				} else
					tempnode2 = GraphInfo.nodemap.get(tempstr2);
				tempedge = new Edge(tempnode1, tempnode2);
				if ((GraphInfo.edgemap.get(tempstr2 + tempstr1) == null)
						&& (GraphInfo.edgemap.get(tempstr1 + tempstr2) == null)) {
					GraphInfo.edgemap.put(tempstr2 + tempstr1, tempedge);
					GraphInfo.edgemap.put(tempstr1 + tempstr2, tempedge);
					GraphInfo.edgelist.add(tempedge);
					tempnode1.getNeighbours().add(tempnode2);
					tempnode2.getNeighbours().add(tempnode1);
				}
				break;
			}
			str = br.readLine();
		}
		br.close();
		}catch (Exception e) {
			e.printStackTrace();
		 MessageDialog.openError(null, "ERROE", "File read exception!");
		 GraphInfo.refresh();
		 return ;
		}
		MessageDialog.openInformation(new Shell(), "success", "File read successfully");
		initVariable(algorithmName, v, rootElement);
	}
	
	/**
	 * ��ȡ�걣���cluster�ļ���  ��ʼ����Ӧ�ı���
	 * @param algorithmName
	 * @param v
	 * @param rootElement
	 */
	public void initVariable(String algorithmName,Vector[] v,TreeElement rootElement){
		Paramater.algorithmsResults.put(algorithmName, v);
		ViewPart1.list.add(rootElement);
		ViewPart1.updateInput();
		getAlgorithmResultInfo(v, algorithmName);   //��ʾ�㷨��������Ϣ
		EvaluationView.algorithms.add(algorithmName);
		EvaluationView.ac.add(new AlgorithmColor(algorithmName,new RGB(250,0,0)));
		EvaluationView.updateCombo(algorithmName);
		EvaluationView.updateGroup(algorithmName);
		EvaluationView.updateTable();
		CurrentNetworkView.update();    //��ǰ���� ��Ϣ ����
	}
	
	public void getAlgorithmResultInfo(Vector[] v,String name){
		int max = 0;
		int min = Integer.MAX_VALUE;
		int total = 0;
		for(int i=0;i<v.length;i++){
			int size = v[i].size();
			if(size>max)max = size;
			if(size<min)min = size;
			total+=size;
		}
		float degree = (float)total/(float)v.length;
		AlgorithmInfo ai = new AlgorithmInfo();
		ai.setName(name);
		ai.setDegree(degree);
		ai.setMax(max);
		ai.setMin(min);
		ai.setTotalCluster(v.length);
		AlgorithmInfoView.update(ai);        //�㷨��Ϣ��ͼ����
	}
	
	
	private Vector<Node>[] v;
	private String algorithmName;
	
	/**
	 * ��ȡ����ľ�����
	 */
	public void openSavedFile(){
		if(GraphInfo.nodelist.size()!=0||ViewPart1.list.size()!=0||Paramater.algorithmsResults.size()!=0){
			   boolean confirm = MessageDialog.openConfirm(new Shell(), "Warning", "A file has been inputed,Do you really want to open a new ?");
			    if(!confirm)
			          return;
			}
		FileDialog fd = new FileDialog(new Shell(),SWT.OPEN);
		fd.setFilterExtensions(new String[] { "*.*","*.txt" });
		fd.setFilterNames(new String[] {"�����ļ�*.*", "�ı���ʽ(*.txt)" });
		String filename = fd.open();
		if(filename==null||filename.equals("")){
			System.out.println("ûѡ�κ��ļ�");
			return;
		}else {
			paramRefresh();
			System.out.println("��ʼ���ļ�");
		}
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine();
			int index1 = str.indexOf(':');
			int index2 = str.lastIndexOf(':');
			algorithmName = str.substring(0,index1);
			int size = Integer.parseInt(str.substring(index2+1, str.length()));
			 v = new Vector[size];
			str = br.readLine();
			str = br.readLine();
			HashMap<String ,Node> nodemap = new HashMap<String,Node>();
			int index = -1;
			while(str!=null){
				if(str.startsWith("******"))break;
				if(str.startsWith("Cluster:")){
					index ++;
					v[index] = new Vector<Node>();
				}else{
					str = str.trim();
					Node n = nodemap.get(str);
					if(n==null){
						n = new Node(str);
						nodemap.put(str, n);
					}
					v[index].add(n);
				}
				str = br.readLine();
			}
			Scanner s  = null;
			String tempstr1,tempstr2;
			Node tempnode1,tempnode2;
			Edge tempedge;
			if(str!=null&&str.startsWith("******")){
				str = br.readLine();
				while (str != null) {               //��ȡԭʼ�����ļ�
					str = str.toUpperCase();
					s = new Scanner(str);
					while (s.hasNext()) {
						tempstr1 = s.next(); // ��һ���ڵ�
						tempnode1 = new Node(tempstr1);
						if (GraphInfo.nodemap.get(tempstr1) == null) {
							GraphInfo.nodemap.put(tempstr1, tempnode1);
							GraphInfo.nodelist.add(tempnode1);
						} else
							tempnode1 = GraphInfo.nodemap.get(tempstr1);
						tempstr2 = s.next(); // �ڶ����ڵ�
						tempnode2 = new Node(tempstr2);
						if (GraphInfo.nodemap.get(tempstr2) == null) {
							GraphInfo.nodemap.put(tempstr2, tempnode2);
							GraphInfo.nodelist.add(tempnode2);
						} else
							tempnode2 = GraphInfo.nodemap.get(tempstr2);
						tempedge = new Edge(tempnode1, tempnode2);
						if ((GraphInfo.edgemap.get(tempstr2 + tempstr1) == null)
								&& (GraphInfo.edgemap.get(tempstr1 + tempstr2) == null)) {
							GraphInfo.edgemap.put(tempstr2 + tempstr1, tempedge);
							GraphInfo.edgemap.put(tempstr1 + tempstr2, tempedge);
							GraphInfo.edgelist.add(tempedge);
							tempnode1.getNeighbours().add(tempnode2);
							tempnode2.getNeighbours().add(tempnode1);
						}
						break;
					}
					str = br.readLine();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			 MessageDialog.openError(null, "ERROE", "File read exception!");
			 GraphInfo.refresh();
			 return;
		}
		new NewThread();
	}
	
	/**
	 * ��ȡ��������ʼ��
	 * @param algorithmName
	 * @param v
	 * @param rootElement
	 */
	public void initVariable(String algorithmName,Vector[] v){
		ViewPart1.updateInput();
		getAlgorithmResultInfo(v, algorithmName);   //��ʾ�㷨��������Ϣ
		EvaluationView.algorithms.add(algorithmName);
		EvaluationView.ac.add(new AlgorithmColor(algorithmName,new RGB(250,0,0)));
		EvaluationView.updateCombo(algorithmName);
		EvaluationView.updateGroup(algorithmName);
		EvaluationView.updateTable();
		CurrentNetworkView.update();    //��ǰ���� ��Ϣ ����
	}
	
	class NewThread implements Runnable{
		
		ProgressBarDialog pbd = null;
		Thread t ;
		NewThread(){
			t = new Thread(this);
			t.start();
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					pbd = new ProgressBarDialog(new Shell(), SWT.NONE,t);
					pbd.getLabel().setText("Calculating clusters......");
				}
			});
				buildTree(v, algorithmName);
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					
					pbd.getLabel().setText("init......");
					initVariable(algorithmName, v);
					pbd.dispose();
				}
			});
		}
		
	}
	
	
	/**
	 * ����
	 * @param v
	 * @param name
	 */
	public void buildTree(Vector<Node>[]v ,String name){
		Paramater.algorithmsResults.put(name, v);
		HashMap<String,Node> tempMap = new HashMap<String,Node>();
		TreeElement parentElement = new TreeElement(name+"("+v.length+")");
		for(int i=0;i<v.length;i++){
			Vector<Node> temp = v[i];
			TreeElement te = new TreeElement("Cluster:"+i+"("+v[i].size()+")");
			Vector<Edge> tempedge = new Vector<Edge>();
			for(int j=0;j<temp.size();j++){
				 Node n1 = temp.get(j);
				for(int k=j+1;k<temp.size();k++){
					Node n2 = temp.get(k);
					if(GraphInfo.edgemap.get(n1.getNodeID()+n2.getNodeID())!=null){
						n1.getNeighbours().add(n2);
						n2.getNeighbours().add(n1);
						tempedge.add(new Edge(n1,n2));
					}
				}
			}
			String str = "Cluster"+i;
			Node node = new Node(str);
			node.setScope(v[i].size());
			tempMap.put(str, node);
			te.setNode(node);
			te.setNodes(temp);
			te.setEdges(tempedge);
			parentElement.addChild(te);
		}
		calOvalap(v, parentElement,tempMap);      //�����֮��Ľ������
		ViewPart1.list.add(parentElement);
	}
	/**
	 * ��������֮��Ľ������
	 * @param v
	 * @param parentElement
	 * @param tempMap
	 */
	public void calOvalap(Vector[] v,TreeElement parentElement,HashMap<String,Node> tempMap){
		Vector<Node> tempNode = new Vector<Node>();
		Vector<Edge> tempEdge = new Vector<Edge>();
		tempNode.addAll(tempMap.values());
		for(int i=0;i<v.length;i++){
			Vector<Node> v1 = v[i];
			for(int j=i+1;j<v.length;j++){
				Vector<Node> v2 = v[j];
				if(CheckOvalap(v1,v2)){
					Node node1 = tempMap.get("Cluster"+i);
					Node node2 = tempMap.get("Cluster"+j);
					node1.getNeighbours().add(node2);
					node2.getNeighbours().add(node1);
					Edge edge = new Edge(node1,node2);
					tempEdge.add(edge);
				}
			}
		}
		parentElement.setNodes(tempNode);
		parentElement.setEdges(tempEdge);
	}
	
	public boolean CheckOvalap(Vector<Node> v1,Vector<Node> v2){
		for(int i=0;i<v1.size();i++){
			Node n1 = v1.get(i);
			if(v2.contains(n1))return true;
		}
		return false;
	}
}
