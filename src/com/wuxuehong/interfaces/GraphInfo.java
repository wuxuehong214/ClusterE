package com.wuxuehong.interfaces;

import java.util.HashMap;
import java.util.Vector;

import com.wuxuehong.bean.*;

/**
 *  该类负责保存蛋白质网络信息
 *   
 * @author Administrator
 *
 */
public class GraphInfo {
	
	//所有节点信息  节点名到节点对象映射
	public static HashMap<String, Node> nodemap = new HashMap<String, Node>();
	//所有边信息  边名称到边对象映射
	public static HashMap<String, Edge> edgemap = new HashMap<String, Edge>();
	//所有节点信息
	public static Vector<Node> nodelist = new Vector<Node>();
	//所有边信息
	public static Vector<Edge> edgelist = new Vector<Edge>();
	//用于当获取随机网络时  保存原始网络边信息
	public static Vector<Edge> tempEdges = new Vector<Edge>();  

	public static void refresh(){
		nodemap.clear();
		edgemap.clear();
		nodelist.clear();
		edgelist.clear();
		tempEdges.clear();
	}
	
}
