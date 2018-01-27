package com.wuxuehong.plugin;


import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class Kmeans extends Plugin implements NewAlgorithm{
	
	
	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "K-menas";
	}

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "kmeans �㷨,���ڸ����ڵ�֮�������㷨���㷨������������,1-�����صĸ���,2-��������е�������";
	}

	@Override
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector<Node>[] getClusters(String[] para) {
		// TODO Auto-generated method stub
		int para1,para2;
		try{
		 para1 = Integer.parseInt(para[0]);//�趨  ��Ҫ���ɶ��ٸ�������������ٸ��أ������ٸ�����ģ��
		 para2 = Integer.parseInt(para[1]);//�趨  K-means�㷨��  ��������
	}catch(Exception e){
		return null;
	}
		Vector tempv = GraphInfo.nodelist;
        Vector dataPoints = new Vector();
        if(tempv.size()==0) return null;
        if(tempv.size()<para1) para1 = tempv.size();
        for(int i=0;i<tempv.size();i++){
        	Node tempn = (Node)tempv.get(i);
        	dataPoints.add(new DataPoint(tempn.getMx(),tempn.getMy(),tempn.getNodeID(),tempn.getNeighbours()));
        }
        JCA jca = new JCA(para1, para2, dataPoints);
        jca.startAnalysis();
        return jca.getClusterOutput();
	}

	@Override
	public String[] getParaValues() {
		// TODO Auto-generated method stub
		return new String[]{"20","500"};
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return new String[]{"Cluster Num:","Loop times:"};
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return NewAlgorithm.Algorithm;
	}

	@Override
	public void variableInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEvaluateNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
