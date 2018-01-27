package com.wuxuehong.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.NewAlgorithm;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date��2011-4-3 ����01:19:27 
 * 
 */

public class WuTest extends Plugin implements NewAlgorithm{

	protected void doStart() throws Exception {
		
	}
	protected void doStop() throws Exception {
		
	}

	public void drawCharts(String[] arg0, HashMap<String, Vector<Node>[]> arg1,
			Composite arg2, HashMap<String, RGB> arg3) {
	}

	public String getAlgorithmName() {
		return "WuComplex";
	}

	public Vector<Node>[] getClusters(String[] arg0) {
		Main main = null;
		try {
			main = new Main();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return main.outputCluster();
	}

	public String getDescription() {
		return "des";
	}

	public String getEvaluateNames() {
		return null;
	}

	public String[] getParaValues() {
		return null;
	}

	public String[] getParams() {
		return null;
	}

	public int getStyle() {
		return NewAlgorithm.Algorithm;
	}

	public void variableInit() {
		
	}
	

}
