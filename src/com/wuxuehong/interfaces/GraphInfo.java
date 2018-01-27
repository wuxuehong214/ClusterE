package com.wuxuehong.interfaces;

import java.util.HashMap;
import java.util.Vector;

import com.wuxuehong.bean.*;

/**
 *  ���ฺ�𱣴浰����������Ϣ
 *   
 * @author Administrator
 *
 */
public class GraphInfo {
	
	//���нڵ���Ϣ  �ڵ������ڵ����ӳ��
	public static HashMap<String, Node> nodemap = new HashMap<String, Node>();
	//���б���Ϣ  �����Ƶ��߶���ӳ��
	public static HashMap<String, Edge> edgemap = new HashMap<String, Edge>();
	//���нڵ���Ϣ
	public static Vector<Node> nodelist = new Vector<Node>();
	//���б���Ϣ
	public static Vector<Edge> edgelist = new Vector<Edge>();
	//���ڵ���ȡ�������ʱ  ����ԭʼ�������Ϣ
	public static Vector<Edge> tempEdges = new Vector<Edge>();  

	public static void refresh(){
		nodemap.clear();
		edgemap.clear();
		nodelist.clear();
		edgelist.clear();
		tempEdges.clear();
	}
	
}
