package com.wuxuehong.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class SecondPlugin extends Plugin implements NewAlgorithm{

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "FAG-EC";
	}
	
	int locate(ArrayList alNodes,String node){
		for(int i=0;i<alNodes.size();i++){
			NetNode curNode = (NetNode)alNodes.get(i);
			if(curNode.getIdentifier().equals(node))
				return i;
		}
		return -1;
	}

	@Override
	public Vector[] getClusters(String[] para) {
		Network network = new Network();
		Complex[] complexes;
		FAGECAlgorithm alg=new FAGECAlgorithm();
		double Threshold;
		int Complexsize;
		try{
	    Threshold = Double.parseDouble(para[0]);
	    Complexsize = Integer.parseInt(para[1]);
		}catch(Exception e){
			return null;
		}
		if(Threshold<0||Threshold>1) return null;
		if(Complexsize<1)return null;
		alg.getParameterSet().setFThreshold(Threshold);
		alg.getParameterSet().setComplexSizeThreshold(Complexsize);
		int arcNum = 0,nodeNum = 0;
		String s;
			ArrayList alNodes=new ArrayList();
			ArrayList alArcs=new ArrayList();		
			Vector edgelist = GraphInfo.edgelist;
		if(edgelist.size()==0)return null;
			Edge tempedge = null;
			for(int i=0;i<edgelist.size();i++){
				tempedge = (Edge)edgelist.get(i);
				String firstNode,secondNode;
				firstNode = tempedge.getNode1().getNodeID();
				secondNode = tempedge.getNode2().getNodeID();
				int index1,index2;
				index1 = locate(alNodes,firstNode);
				index2 = locate(alNodes,secondNode);
				NetNode nodeFrom,nodeTo;
				if(index1<0){	//a new node
					index1 = alNodes.size();
					nodeFrom = new NetNode(firstNode);
					nodeFrom.setRootGraphIndex(index1);
					nodeFrom.setDegree(1);
					alNodes.add(nodeFrom);
				}
				else{
					nodeFrom = (NetNode)alNodes.get(index1);
					int degree = nodeFrom.getDegree()+1;
					nodeFrom.setDegree(degree);
				}
				if(index2<0){	//a new node
					index2 = alNodes.size();
					nodeTo= new NetNode(secondNode);
					nodeTo.setRootGraphIndex(index2);
					nodeTo.setDegree(1);
					alNodes.add(nodeTo);
				}
				else{
					nodeTo = (NetNode)alNodes.get(index2);
					int degree = nodeTo.getDegree()+1;
					nodeTo.setDegree(degree);
				}
				nodeFrom.getAlNeighbors().add(new Integer(index2));
				nodeTo.getAlNeighbors().add(new Integer(index1));
				NetArc arc=new NetArc(index1,index2);
				arc.setRootGraphIndex(alArcs.size());
				alArcs.add(arc);
			}
			network.setAlNodes(alNodes);
			network.setAlArcs(alArcs);
		if(network==null) return null;
		    complexes=alg.clustering(network);
		    Vector[] v = new Vector[complexes.length];
		    for(int i=0;i<complexes.length;i++){
		        v[i] = new Vector();
		       ArrayList list = complexes[i].getALNodes();
		       int index = 0;
			   for(int j=0;j<list.size();j++){
		    	   index = ((Integer)list.get(j)).intValue();
		    	   v[i].add(network.getAlNodes().get(index));
			   }
		    }
		   return v;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "a cluster algorithm named FAG-EC which has two paramater:Threshold and ComplexSize.";
	}

	@Override
	public String[] getParaValues() {
		// TODO Auto-generated method stub
		return new String[]{"0.1","3"};
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return new String[]{"Threshold(0-1)","ComplexSize(>=2)"};
	}


	@Override
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return NewAlgorithm.Algorithm;
	}

	@Override
	public String getEvaluateNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void variableInit() {
		// TODO Auto-generated method stub
		
	}

}
