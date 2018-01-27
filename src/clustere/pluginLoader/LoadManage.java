package clustere.pluginLoader;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.wuxuehong.interfaces.NewAlgorithm;

import clustere.pluginAction.PluginAlgorithmAction;
import clustere.pluginAction.PluginEvaluationAction;


public class LoadManage {

//	private Vector<NewAlgorithm> evaluateList = new Vector<NewAlgorithm>(); // 所有实现评估参数的实例
//	public HashMap<String, Vector> tempMap; // 一个算法名对应一个统计图
//	private HashMap<NewAlgorithm, MenuItem[]> algorithmItem = new HashMap<NewAlgorithm, MenuItem[]>();
//	public static Vector<String> algorithmNames = new Vector<String>();
	
	public static LoadManage loadmanage = new LoadManage();

	public static LoadManage getInstance() {
       return loadmanage;
	}
	
	public void addItem(MenuManager runMenu,MenuManager evaluationMenu,NewAlgorithm section){
		int style = section.getStyle();
		if(style == NewAlgorithm.Algorithm){
		    runMenu.add(new PluginAlgorithmAction(section));
		}else if(style == NewAlgorithm.Evaluation){
			evaluationMenu.add(new PluginEvaluationAction(section));
		}
	}
	
	

//	/**
//	 * 添加菜单项
//	 */
//	public void addItem(NewAlgorithm section) {
//		if (section == null)
//			return;
//		String name = section.getAlgorithmName(); // 算法名
//		String[] itemname = section.getEvaluateNames(); // 评估参数名
//		if (name != null && itemname == null) { // 如果名字不为空 则是算法类插件 否则则是评估类插件
//			MenuItem newMenu = new MenuItem(MainFrame.Algorithmlist,
//					SWT.CASCADE);
//			newMenu.setText(name);
////			newMenu.addSelectionListener(new AlgorithmListener(section));
//		}
//		if (itemname != null && name == null) {
//			MenuItem[] item = new MenuItem[itemname.length];
//			for (int i = 0; i < itemname.length; i++) {
//				item[i] = new MenuItem(MainFrame.evaluate_down, SWT.CASCADE);
//				item[i].setText(itemname[i]);
//				item[i].addSelectionListener(new EvaluateListener(item[i],
//						section));
//			}
//			algorithmItem.put(section, item);
//			evaluateList.add(section);
//		}
//	}

//	/**
//	 * 响应 算法菜单
//	 */
//	class AlgorithmListener extends SelectionAdapter {
//		private NewAlgorithm thm;
//
//		AlgorithmListener(NewAlgorithm thm) {
//			this.thm = thm;
//		}
//
//		public void widgetSelected(SelectionEvent e) {
//			// TODO Auto-generated method stub
//			if (GraphInfo.nodelist.size() == 0) { // 当在没有读取节点与边的信息文件前就调用了聚类算法
//				MessageBox box = new MessageBox(new Shell(), SWT.YES);
//				box.setText("ERROR");
//				box.setMessage("Please first load the NewWork");
//				box.open();
//				return;
//			}
//			String[] params = thm.getParams();
//			String[] paraValues = thm.getParaValues();
//			String[] result = null;
//			if (params != null) {
//				AlgorithmSetDialog asd = new AlgorithmSetDialog(MainFrame.shell,
//						SWT.NONE, params, paraValues);
//				result = asd.getResult();
//				if (result == null)
//					return;
//			}
//			String algorithmName = thm.getAlgorithmName();
//			if(result!=null){
//			for (int i = 0; i < result.length; i++) {
//				algorithmName += "-" + result[i];
//			}
//			}
//             if(algorithmNames.contains(algorithmName))
//             { 
//            	 MessageBox mb = new MessageBox(new Shell(),SWT.YES|SWT.ERROR);
//            	 mb.setText("Warn");
//            	 mb.setMessage("The algorithm has been calculated!");
//            	 mb.open();
//            	 return;
//             }
//            algorithmNames.add(algorithmName); // 保存算法名
//			NewThread nt = new NewThread(result, thm, algorithmName);
//			Thread t = new Thread(nt);
//			t.start();
//		}
//	}
//
//	/**
//	 * 进度条的实现
//	 * 
//	 * @author Administrator
//	 * 
//	 */
//	class NewThread implements Runnable {
//		String[] result;
//		ProgressBar pb;
//		Vector[] v;
//		NewAlgorithm thm;
//		ProgressBarDialog pbd;
//		String algorithmName;
//
//		NewThread(String[] result, NewAlgorithm thm,String algorithmName) {
//			this.result = result;
//			this.thm = thm;
//			this.algorithmName = algorithmName;
//		}
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			MainFrame.display.asyncExec(new Runnable() {
//				@Override
//				public void run() {
//					MainFrame.statueLabel.setText("Caculating clusters......");
//					pbd = new ProgressBarDialog(MainFrame.shell, SWT.NONE);
//					pbd.getLabel().setText("Caculating clusters......");
//				}
//			});
//			v = thm.getClusters(result);
//			final String anname = algorithmName;
//			MainFrame.display.asyncExec(new Runnable() {
//				@Override
//				public void run() {
//					Paramater.colorChoose.put(anname, new RGB(255, 0, 0)); // 给每个算法结果一个初始颜色
//					TableViewerEditor.inputData(); // 往表格中添加 算法颜色 面板
//				   if(MainFrame.algorithmGroup!=null){	Button b =new Button(MainFrame.algorithmGroup,SWT.CHECK);
//				                                        b.setText(anname);
//				                                        MainFrame.buttonList.add(b);
//					                                    MainFrame.algorithmGroup.layout();
//					                                    
//					                                     }
//					if (MainFrame.extendCombo != null) {
//						MainFrame.extendCombo.add(anname);
//						MainFrame.extendCombo.setText(anname);
//					}
//					MainFrame.statueLabel
//							.setText("With the clustering results,Buinding tree......");
//					pbd.getLabel().setText(
//							"With the clustering results,Buinding tree......");
//				}
//			});
//			MainFrame.display.asyncExec(new Runnable() {
//				public void run() {
//					buildTree(v, anname); // 建树
//				}
//			});
//			MainFrame.display.asyncExec(new Runnable() {
//				@Override
//				public void run() {
//					MainFrame.statueLabel
//							.setText("With the clustering results,Caculating the charts...... ");
//					pbd
//							.getLabel()
//							.setText(
//									"With the clustering results,Caculating the charts...... ");
//				}
//			});
//			for (int i = 0; i < evaluateList.size(); i++) {
//				Vector tempv = evaluateList.get(i).BuildCharts(v);
//				MenuItem[] item = algorithmItem.get(evaluateList.get(i));
//				for (int j = 0; j < item.length; j++) {
//					if (Paramater.chartsMap.get(item[j]) != null) {
//						Paramater.chartsMap.get(item[j]).put(algorithmName,
//								tempv);
//					} else {
//						tempMap = new HashMap<String, Vector>();
//						tempMap.put(algorithmName, tempv);
//						Paramater.chartsMap.put(item[j], tempMap);
//					}
//				}
//			}
//			MainFrame.display.asyncExec(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					MainFrame.statueLabel.setText("complete!");
//					pbd.getLabel().setText("complete!");
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					pbd.dispose();
//				}
//			});
//		}
//	}
//	
//
//	/**
//	 * calculate the charts info with the known Clusters
//	 * @param v
//	 * @param algorithmName
//	 */
////public void CalChart(Vector[] v,String algorithmName){
////	for (int i = 0; i < evaluateList.size(); i++) {
////		Vector tempv = evaluateList.get(i).BuildCharts(v);
////		MenuItem[] item = algorithmItem.get(evaluateList.get(i));
////		for (int j = 0; j < item.length; j++) {
////			if (Paramater.chartsMap.get(item[j]) != null) {
////				Paramater.chartsMap.get(item[j]).put(algorithmName,
////						tempv);
////			} else {
////				tempMap = new HashMap<String, Vector>();
////				tempMap.put(algorithmName, tempv);
////				Paramater.chartsMap.put(item[j], tempMap);
////			}
////		}
////	}
////}
//
//	/**
//	 * 显示经过聚类算法后 网络的基本信息
//	 */
//	public void showNetworkInfo(Vector[] v, String name) {
//		int max, min, total, size;
//		float avg;
//		size = v.length;
//		max = 0;
//		min = Integer.MAX_VALUE;
//		total = 0;
//		for (int i = 0; i < size; i++) {
//			if (v[i].size() > max)
//				max = v[i].size();
//			if (v[i].size() < min)
//				min = v[i].size();
//			total += v[i].size();
//		}
//		avg = (float) total / (float) size;
//		new TableItem(MainFrame.table, SWT.LEFT).setText(new String[] { name,
//				"Clusters", "" + size + "" }); // 建树的同时 显示算法信息 及结果
//		new TableItem(MainFrame.table, SWT.LEFT).setText(new String[] { "",
//				"Max Size", "" + max + "" });
//		new TableItem(MainFrame.table, SWT.LEFT).setText(new String[] { "",
//				"Min Size", "" + min + "" });
//		new TableItem(MainFrame.table, SWT.LEFT).setText(new String[] { "",
//				"Avg Size", "" + avg + "" });
//
//		MenuItem item = new MenuItem(MainFrame.commmenu, SWT.CASCADE);
//		item.setText(name);
//		final BarChart barchart = SizeDistribution.BuildCharts(v);
//		barchart.setRowkeys(new String[]{name});
//		item.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				new ShowChart(barchart);
//			}
//		});
//	}
//
//	/**
//	 * 建树 将聚类结果以树的结构显示
//	 */
//	public void buildTree(Vector[] v, String name) {
//		if (v == null)
//			return;
//		showNetworkInfo(v, name); // 显示聚类结果基本信息
//		CTabItem citem1 = new CTabItem(MainFrame.ctab_tree, SWT.NONE);
//		citem1.setText(name);
//		Tree tree = new Tree(MainFrame.ctab_tree, SWT.BORDER);
//		tree.addSelectionListener(treelistener);
//		TreeItem treeitem = new TreeItem(tree, SWT.NULL);
//		treeitem.setText("Results:(" + v.length + ")");
//		TreeItem tempitem;
//		Vector edgelist;
//		HashMap<String, Node> tempmap = new HashMap<String, Node>(); // 一个字符串对应一个节点实例
//		completeNetwork(v, treeitem, tempmap); // 整体网络图信息
//		int total = 0;
//		for (int i = 0; i < v.length; i++) {
//			total += v[i].size();
//			tempitem = new TreeItem(treeitem, SWT.NULL); // 每个簇生成一个树节点
//			tempitem.setText("Cluster:" + i + " (" + v[i].size() + ")"); // 簇信息
//			Paramater.clusterNodes.put(tempitem, v[i]); // 将该簇中的节点信息保存
//			// caculateNode(v[i]); //计算所有节点的位置
//			edgelist = new Vector();
//			for (int j = 0; j < v[i].size(); j++) { // 获取该簇中的边的信息 并保存
//				Node n1 = (Node) v[i].get(j);
//				tempmap.put(n1.getNodeID(), n1); // 获取所有经过聚类算法后的节点信息
//				for (int k = j + 1; k < v[i].size(); k++) {
//					Node n2 = (Node) v[i].get(k);
//					Edge edge = GraphInfo.edgemap.get(n1.getNodeID()
//							+ n2.getNodeID());
//					if (edge != null) {
//						n1.getNeighbours().add(n2);
//						n2.getNeighbours().add(n1);
//						Edge edge2 = new Edge(n1, n2);
//						edgelist.add(edge2);
//					}
//				}
//			}
//			Paramater.clusterEdges.put(tempitem, edgelist);
//		}
//		Paramater.totalNodes.put(name, tempmap);
//		citem1.setControl(tree);
//		MainFrame.ctab_tree.setSelection(citem1);
//		Paramater.MyAlgorithims.put(name, v);
//		MenuControllor.showGraphInfo(MainFrame.text1);
//		MainFrame.item3.setExpanded(true);//  set the ExpandItem of the clusters  expnded
//	}
//
//	/**
//	 * 
//	 * 响应树节点
//	 */
//	class MyTreeListener extends SelectionAdapter {
//		public void widgetSelected(SelectionEvent e) {
//			TreeItem treeitem = (TreeItem) e.item;
//			Paramater.uniqueClusterNode = Paramater.clusterNodes.get(treeitem); // 获取该树节点对应的
//			Paramater.uniqueClusterEdge = Paramater.clusterEdges.get(treeitem); // 获取该树节点对应的簇中的边信息
//	        mainframe.showSubGraph();
//	        MainFrame.ctab.setSelection(MainFrame.citem2);
//			// 簇中的节点信息
//            ClusterLayout.setNetLayout(ClusterLayout.Style, Paramater.uniqueClusterNode, MainFrame.canvas2);//布局网络的显示
//			MainFrame.separateNode = null; // 显示其他簇的时候 实现将选中节点置为空
//			showClusterInfo(Paramater.uniqueClusterNode);
//			MenuControllor.showGraphInfo(MainFrame.text1);
//		}
//	}
//
//	public static String evaluatePara = null; // 评估参数
//
//	/**
//	 * 响应评估参数菜单
//	 */
//	class EvaluateListener extends SelectionAdapter {
//		MenuItem item;
//		NewAlgorithm section;
//
//		EvaluateListener(MenuItem item, NewAlgorithm section) {
//			this.item = item;
//			this.section = section;
//		}
//
//		public void widgetSelected(SelectionEvent e) {
//			Paramater.executeInstance = section; // 根据菜单获取 画图表的实例
//			Paramater.showChart = Paramater.chartsMap.get(item); // 根据菜单获取图表的信息
//			evaluatePara = item.getText();
//			mainframe.showChart();
//			MainFrame.ctab.setSelection(MainFrame.citem3);
//			if (Paramater.showChart == null)
//				return;
//			String[] algorithm = new String[] { MainFrame.extendCombo
//					.getText() };// 拿到要显示的算法名
//			MainFrame.extendLabel.setText(evaluatePara); // 显示参数名
//			TableViewerEditor.setCololist(); // 设置初始颜色
//			MainFrame.CompositeRefresh();
//			Paramater.executeInstance.drawCharts(algorithm,
//					evaluatePara, Paramater.showChart, MainFrame.chartComp,
//					Paramater.colorChoose);
//		}
//	}
//
//	/**
//	 * 
//	 * 计算并显示每个cluster的信息
//	 */
//	public void showClusterInfo(Vector v) {
//		MainFrame.tableShow.removeAll();
//		MainFrame.tableShow2.removeAll();
//		int nodecount = v.size();
//		new TableItem(MainFrame.tableShow, SWT.LEFT).setText(new String[] {
//				"Nodes Num", "" + nodecount + "" });
//		int edgecount = Paramater.uniqueClusterEdge.size();
//		for (int i = 0; i < v.size(); i++) {
//			Node n1 = (Node) v.get(i);
//			Vector<String> function = Paramater.proteinFunction.get(n1
//					.getNodeID());
//			String result = "";
//			if (function != null) {
//				for (int k = 0; k < function.size(); k++)
//					result = result + function.get(k) + " ";
//			}
//			new TableItem(MainFrame.tableShow2, SWT.LEFT)
//					.setText(new String[] { n1.getNodeID(),
//							"" + n1.getNeighbours().size() + "", result });
//		}
//		MainFrame.tableShow2.setToolTipText(MainFrame.ctab_tree.getSelection()
//				.getText());
//		new TableItem(MainFrame.tableShow, SWT.LEFT).setText(new String[] {
//				"Edges Num", "" + edgecount + "" });
//		float degree = 2*(float)edgecount/(float)nodecount;
//		new TableItem(MainFrame.tableShow, SWT.LEFT).setText(new String[] {
//				"Average Degree", "" + degree + "" });
//
//	}
//
//	/**
//	 * 计算两个簇之间是否有交叠 整体网络处理方式 将 一个簇作为一个节点 如过这两个簇之间有交叠情况则这两个节点间有一条边
//	 */
//	public void completeNetwork(Vector[] v, TreeItem tempItem,
//			HashMap<String, Node> hashmap) {
//		if (v == null)
//			return;
//		//HashMap<String, Node> tempmap = new HashMap<String, Node>();
//		String str = null;
//		Node node = null;
//		Vector<Node> v1 = new Vector<Node>();
//		Vector<Edge> v2 = new Vector<Edge>();
//		for (int i = 0; i < v.length; i++) {
//			str = "Cluster" + i;
//			node = new Node(str);
//		//	tempmap.put(str, node);
//			hashmap.put(str, node);
//			v1.add(node);
//		}
//		// caculateNode(v1);
//		Paramater.clusterNodes.put(tempItem, v1);
//		Node node1, node2, node3, node4;
//		for (int i = 0; i < v.length; i++) {
//			Vector temp1 = v[i];
//			for (int j = i + 1; j < v.length; j++) {
//				Vector temp2 = v[j];
//				boolean t = false;
//				for (int k = 0; k < temp1.size(); k++) {
//					if (t)
//						break;
//					node1 = (Node) temp1.get(k);
//					for (int l = 0; l < temp2.size(); l++) {
//						node2 = (Node) temp2.get(l);
//						if (GraphInfo.edgemap.get(node1.getNodeID()
//								+ node2.getNodeID()) != null) {
//							node3 = hashmap.get("Cluster" + i);
//							node4 = hashmap.get("Cluster" + j);
//							node3.getNeighbours().add(node4);
//							node4.getNeighbours().add(node3);
//							v2.add(new Edge(node3, node4));
//							t = true;
//							break;
//						}
//					}
//				}
//			}
//		}
//		Paramater.clusterEdges.put(tempItem, v2);
//		// System.out.println(v1.size()+"cluster size"+v2.size());
//	}
//
//	
//	/**
//	 * 计算网络布局  发散型
//	 * @param v
//	 */
//	public static void calculateNode2(Vector v,Canvas canvas){
//	if(canvas==null)return;
//		Collections.sort(v,new Comparator(){
//			@Override
//			public int compare(Object n1, Object n2) {
//				// TODO Auto-generated method stub
//				Node node1 = (Node)n1;
//				Node node2 = (Node)n2;
//				return node1.getNeighbour_NUM()-node2.getNeighbour_NUM();
//			}
//		});
//		Collections.reverse(v);
////		for(int i=0;i<v.size();i++){
////			Node n = (Node)v.get(i);
////			System.out.print(n.getNeighbour_NUM()+"  ");
////		}
//		LinkedList<Node> list = new LinkedList<Node>();
//		Node node = (Node)v.get(0);
//		node.setMx(canvas.getBounds().width/2);
//		node.setMy(canvas.getBounds().height/2);
//		list.add(node);
//		Vector<Node> all = new Vector<Node>();
//		all.add(node);
//		while(list.size()!=0){
//			Node no = list.remove(0);     //取走队列中的第一个元素
//			Vector<Node> calno = no.getNeighbours();
//			calno.removeAll(all);
//			cal(calno,no);
//			for(int i=0;i<no.getNeighbour_NUM();i++){
//				Node tempn = no.getNeighbours().get(i);
//			    if(!all.contains(tempn))
//					list.add(tempn);
//			}
//			all.addAll(calno);
//		}
//	}
//	
//	public static void cal(Vector<Node> v,Node node){
//		int size = Paramater.NODE_SIZE;  //节点尺寸
//		int num = v.size();
//		int r = size*5;                   //两节点之间距离
//		double total = 2*Math.PI*r;          
//		double have = 2.5*size*num;
//		double arc = 0;
//		if(have<total){
//			arc = 2*Math.PI/(double)num;
//			double arcc = Math.random()*Math.PI*2;
//			for(int i=0;i<num;i++){
//				Node n = v.get(i);
//				n.setMx((int) (node.getMx()+r*Math.cos(arcc)));
//				n.setMy((int) (node.getMy()+r*Math.sin(arcc)));
//				arcc+=arc;
//			}
//			return;
//		}else{
//			int index = 0;
//			while(have>total){
//			int getrid = (int) (total / (2.5 * size));
//			arc = ((2 * Math.PI) / (getrid));
//			double arcc = Math.random()*Math.PI*2;
//			int i;
//			for (i = index; i < index + getrid ; i++) {
//				Node n = (Node) v.get(i);
//				n.setMx((int) (node.getMx() + r * Math.cos(arcc)));
//				n.setMy((int) (node.getMy() + r * Math.sin(arcc)));
//				arcc+=arc;
//			}
//			index+=getrid;           //对节点列表中节点的索引往后
//			num-=getrid;             //剩余的节点数
//			r = r+2*size;             //半径递增
//			total = (2 * Math.PI * r);  //新的周长
//			have = (2.5 * size * num); //需要周长
//			}
//			arc = ((2 * Math.PI) / (num));
//			double arcc = Math.random()*Math.PI*2;
//			int i;
//			for (i = index; i < v.size(); i++) {
//				Node n = (Node) v.get(i);
//				n.setMx((int) (node.getMx() + r * Math.cos(arcc)));
//				n.setMy((int) (node.getMy() + r * Math.sin(arcc)));
//				arcc+=arc;
//			}
//		}
//		
//	}
//	/**
//	 * 计算网络布局 圆形
//	 */
//	public static void caculateNode(Vector v) {
//		if (MainFrame.canvas2 == null)
//			return;
//		int canvas_w = MainFrame.canvas2.getBounds().width;
//		int canvas_h = MainFrame.canvas2.getBounds().height;
//		int temp = 100;
//		double arc = 0;
//		int index = 0;
//		int size = v.size();
//		double total = 2 * Math.PI * temp; // 初始设置位置 半径100的圆的周长
//		double have = (2.5 * Paramater.NODE_SIZE * size); // 近似 计算该complex中
//		// 过所有节点圆心周长
//		if (have < total) {
//			arc = ((2 * Math.PI) / (size));
//			double j;
//			int i;
//			for (i = 0, j = 0; i < size; i++, j += arc) {
//				Node node = (Node) v.get(i);
//				node.setMx((int) (canvas_w / 2 + temp * Math.cos(j)));
//				node.setMy((int) (canvas_h / 2 + temp * Math.sin(j)));
//			}
//			return;
//		} else {
//			temp = 50;
//			while (have > total) {
//				int getrid = (int) (total / (2.5 * Paramater.NODE_SIZE));
//				arc = ((2 * Math.PI) / (getrid));
//				if (index + getrid > v.size()) {
//					getrid = v.size() - index;
//					System.out.println("***********************************");
//				}
//				double j;
//				int i;
//				for (i = index, j = 0; i < index + getrid && j < 2 * Math.PI; i++, j += arc) {
//					Node node = (Node) v.get(i);
//					node.setMx((int) (canvas_w / 2 + temp * Math.cos(j)));
//					node.setMy((int) (canvas_h / 2 + temp * Math.sin(j)));
//				}
//				index = index + getrid;
//				size = size - getrid;
//				temp += (int) (1.5 * Paramater.NODE_SIZE);
//				total = (2 * Math.PI * temp);
//				have = (2.5 * Paramater.NODE_SIZE * size);
//			}
//			arc = ((2 * Math.PI) / (size));
//			double j;
//			int i;
//			for (i = index, j = 0; i < v.size(); i++, j += arc) {
//				Node node = (Node) v.get(i);
//				node.setMx((int) (canvas_w / 2 + temp * Math.cos(j)));
//				node.setMy((int) (canvas_h / 2 + temp * Math.sin(j)));
//			}
//		}
//	}

}
