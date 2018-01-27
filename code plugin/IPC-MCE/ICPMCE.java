package com.wuxuehong.plugin;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.NewAlgorithm;

public class ICPMCE  extends Plugin implements NewAlgorithm{

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "IPC-MCE";
	}

	@Override
	public Vector<Node>[] getClusters(String[] para) {
		// TODO Auto-generated method stub
		double  door=0;
		try{
			door = Double.parseDouble(para[0]);
		if(door<0||door>1)return null;
		}catch(Exception e){
			return null;
		}
		Test t = new Test(door);
		Vector<Node>[] v= t.getResult();
		return v;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "IPC-MCE是基于极大团扩展的蛋白质复合物算法";
	}

	@Override
	public String getEvaluateNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParaValues() {
		// TODO Auto-generated method stub
		return new String[]{"0.2"};
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return new String[]{"Interaction(0-1)"};
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
