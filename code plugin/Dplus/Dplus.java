package com.wuxuehong.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.NewAlgorithm;

public class Dplus extends Plugin implements NewAlgorithm{

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
		return "Dpclus";
	}

	@Override
	public Vector[] getClusters(String[] para) {
		// TODO Auto-generated method stub
		ListAndMatrix  lam = new ListAndMatrix();
		double para1 = Double.parseDouble(para[0]);
		double para2 = Double.parseDouble(para[1]);
		if(para1<0||para1>1) return null;
		if(para2<0||para2>1) return null;
		Vector<Vector>  v = lam.makeListAndMatrix(para1, para2);
		Vector vector[] = new Vector[v.size()];
		for(int i=0;i<v.size();i++)
			vector[i] = v.get(i);
		return vector;
			
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "A cluster algorithm named Dpclus with two paramaters cp value(0-1) and density(0-1).";
	}

	@Override
	public String[] getParaValues() {
		// TODO Auto-generated method stub
		return new String[]{"0.2","0.2"};
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return new String[]{"cp value(0-1)","density(0-1)"};
	}


	@Override
	public String getEvaluateNames() {
		// TODO Auto-generated method stub
		return null;
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
	public void variableInit() {
		// TODO Auto-generated method stub
		
	}

}
