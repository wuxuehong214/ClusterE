package clustere.pluginAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;


import com.wuxuehong.bean.AlgorithmColor;
import com.wuxuehong.bean.AlgorithmInfo;
import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;

import clustere.Activator;
import clustere.dialogs.AlgorithmSetDialog;
import clustere.dialogs.ProgressBarDialog;
import clustere.editors.ClusterEditor;
import clustere.editors.ClusterEditorInput;
import clustere.editors.EvaluationView;
import clustere.treeViewer.TreeElement;
import clustere.views.AlgorithmInfoView;
import clustere.views.ViewPart1;

import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.NewAlgorithm;


public class PluginAlgorithmAction extends Action{
	
	 private NewAlgorithm section;
	 private ProgressBarDialog pbd;

	 
	public PluginAlgorithmAction(NewAlgorithm section){
		this.setText("&"+section.getAlgorithmName());
		this.section = section;
	}
	
	public void run(){
		calCulate();
	}

	public void calCulate(){
		String[] params = section.getParams();
		String[] paraValues = section.getParaValues();
		String algorithName = section.getAlgorithmName();
		String[] result = null;
		if (params != null) {
			AlgorithmSetDialog asd = new AlgorithmSetDialog(new Shell(),
					SWT.NONE, params, paraValues);
			result = asd.getResult();
			if (result == null)
				return;
		}
		if(result!=null){
			for (int i = 0; i < result.length; i++) {
				algorithName += "-" + result[i];
			}
			}
		if(Paramater.NETWORK_STYLE==Paramater.NWTWORK_RANDOM)//如果当前进行聚类的网络是随机网络
			algorithName += "-r";
		else if(Paramater.NETWORK_STYLE==Paramater.NWTWORK_I_RANDOM)  //当前网络 随机增加边
			algorithName += "-i"+Paramater.NETWORK_EXPAND_PERCENT+"%";
		else if(Paramater.NETWORK_STYLE==Paramater.NWTWORK_D_RANDOM)   //当前网络随机减少边
			algorithName += "-d"+Paramater.NETWORK_EXPAND_PERCENT+"%";
			
		if(Paramater.algorithmsResults.keySet().contains(algorithName)){
			boolean a = MessageDialog.openConfirm(new Shell(), "Confirm", "This algorithm with the paramaters has been calculated!do you want to calculate it again?");
			if(!a)
			return;
		}
		new NewThread(result,algorithName);
	}
	
	public void cluster(String[] result,String algorithmName){
		
	}
	class NewThread implements Runnable{
		String[] result;
		String algorithmName;
		Thread t ;
		NewThread(String[] result,String algorithmName){
			this.result = result;
			this.algorithmName = algorithmName;
			t = new Thread(this);
			t.start();
		}
		public void run() {
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					pbd = new ProgressBarDialog(new Shell(), SWT.NONE,t);
					pbd.getLabel().setText("Caculating clusters......");
				}
			});
			final Vector<Node>[] v = section.getClusters(result);
			if(GraphInfo.edgelist.size()==0||v==null||v.length==0){
				Display.getDefault().asyncExec(new Runnable(){
					public void run() {
						pbd.dispose();
						MessageDialog.openError(new Shell(), "Error", "Paramater is not available...or no file input!");
					}
				});
				return ;
			}
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					pbd.getLabel().setText("Building tree......");
				}
			});
			buildTree(v,algorithmName);
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					pbd.dispose();
					ViewPart1.updateInput();
					getAlgorithmResultInfo(v, algorithmName);   //显示算法计算后的信息
					EvaluationView.algorithms.add(algorithmName);
					EvaluationView.ac.add(new AlgorithmColor(algorithmName,new RGB(250,0,0)));
					EvaluationView.updateCombo(algorithmName);
					EvaluationView.updateGroup(algorithmName);
					EvaluationView.updateTable();
				}
			});
		}
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
		AlgorithmInfoView.update(ai);
	}
	
	
	public void buildTree(Vector<Node>[]v ,String name){
		Paramater.algorithmsResults.put(name, v);
		HashMap<String,Node> tempMap = new HashMap<String,Node>();
		TreeElement parentElement = new TreeElement(name+"("+v.length+")");
		for(int i=0;i<v.length;i++){
			Vector<Node> temp = v[i];
			Vector<Node> addNodes = new Vector<Node>();
			TreeElement te = new TreeElement("Cluster:"+i+"("+v[i].size()+")");
			Vector<Edge> tempedge = new Vector<Edge>();
			
			for(int j=0;j<temp.size();j++)addNodes.add(new Node(temp.get(j).getNodeID()));
			
			for(int j=0;j<addNodes.size();j++){
				 Node n1 = addNodes.get(j);
				for(int k=j+1;k<addNodes.size();k++){
					Node n2 = addNodes.get(k);
					if(GraphInfo.edgemap.get(n1.getNodeID()+n2.getNodeID())!=null&&Paramater.NETWORK_STYLE==1){
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
			te.setNodes(addNodes);
			te.setEdges(tempedge);
			parentElement.addChild(te);
		}
		calOvalap(v, parentElement,tempMap);      //计算簇之间的交叠情况
		ViewPart1.list.add(parentElement);
	}
	
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
			if(v2.contains(n1)){
				System.out.println("*************");
				return true;
			}
		}
		return false;
	}
	
	
}
