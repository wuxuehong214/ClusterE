package com.wuxuehong.interfaces;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;

public interface NewAlgorithm {
	public static final int Algorithm = 1;
	public static final int Evaluation = 2;
	
	/**
	 * 返回算法名称 并以此创建菜单
	 * 
	 * @return
	 */
	public String getAlgorithmName();

	/**
	 * 返回聚类结果
	 * 
	 * @param para
	 *            算法参数
	 * @return
	 */
	public Vector<Node>[] getClusters(String[] para);

	/**
	 * 返回参数名称
	 * 
	 * @return
	 */
	public String[] getParams();

	/**
	 * 返回参数初始值
	 * 
	 * @return
	 */
	public String[] getParaValues();

	/**
	 * 返回对算法或者对评估方法的描述
	 * 
	 * @return
	 */
	public String getDescription();	
	
	/**
	 *   设置插件类型  返回1 则是 算法类插件  返回2则是评估方法类插件
	 * @return
	 */
	public int getStyle();
	
	/**
	 * 返回评估参数名称
	 * 
	 * @return
	 */
	public String getEvaluateNames();

	/**
	 * 
	 * @param algorithm
	 * @param resultList
	 * @param composite
	 * @param colorlist
	 */
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist);
	
	/**
	 * 插件中 变量初始化
	 */
    public void variableInit();

}
