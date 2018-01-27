package com.wuxuehong.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.NewAlgorithm;

public class Coach  extends Plugin implements NewAlgorithm{

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
		return "Coach";
	}

	@Override
	public Vector<Node>[] getClusters(String[] para) {
		// TODO Auto-generated method stub
		float  density = 0;
		float similarity = 0;
		try{
			density = Float.parseFloat(para[0]);
			similarity = Float.parseFloat(para[1]);
		if(density<0||density>1||similarity<0||similarity>1)return null;
		}catch(Exception e){
			return null;
		}
		Main main = new Main(density,similarity);
		List<SubGraph> cores = main.getCore();
		Vector[] v = new Vector[cores.size()];
		for(int i=0;i<cores.size();i++){
			v[i] = new Vector();
			for(int j=0;j<cores.get(i).getNodes().size();j++){
				v[i].add(new com.wuxuehong.bean.Node(cores.get(i).getNodes().get(j).getName()));
			}
		}
		return v;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "A core-attachment based method to detect protein complexes in PPI networks";
	}

	@Override
	public String getEvaluateNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParaValues() {
		// TODO Auto-generated method stub
		return new String[]{"0.7","0.225"};
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return new String[]{"Density(0-1)","Similarity(0-1)"};
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
