package com.wuxuehong.plugin;


import java.util.Vector;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.java.plugin.Plugin;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class FirstPlugin extends Plugin implements NewAlgorithm{
	
	
	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "k-menas";
	}

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void calculate(){
		
	}

	@Override
	public Vector[] getClusters() {
		// TODO Auto-generated method stub
		Vector tempv = GraphInfo.nodelist;
        Vector dataPoints = new Vector();
        if(tempv.size()==0) return null;
        for(int i=0;i<tempv.size();i++){
        	Node tempn = (Node)tempv.get(i);
        	dataPoints.add(new DataPoint(tempn.getMx(),tempn.getMy(),tempn.getNodeID(),tempn.getNeighbours()));
        }
        JCA jca = new JCA(5, 1000, dataPoints);
        jca.startAnalysis();

         return jca.getClusterOutput();
	}

	@Override
	public Vector BuildCharts(Vector[] v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawChar(Vector<Vector> tempv, Rectangle rec, Point point, GC gc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "kmeans Ëã·¨";
	}

	@Override
	public String[] getEvaluateNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
