package com.wuxuehong.bean;


import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class Paramater {

//	public static Color BACK_COLOR = SWTResourceManager.getColor(200, 0, 0); // 背景色
//	public static Color FORE_COLOR = SWTResourceManager.getColor(0, 0, 0); // 前景色

	public static Color BACK_COLOR = new Color(Display.getDefault(),new RGB(250,0,0)); // 背景色
	public static Color FORE_COLOR = new Color(Display.getDefault(),new RGB(0,0,0));// 前景色
	public static Color SEPARATE_COLOR = new Color(Display.getDefault(),new RGB(000,0,250)); //特殊颜色
	
	public static Node SEPARATE_NODE = null;
	public static int NODE_SIZE = 20;
	public static int LINE_WIDTH = 2;
	public static boolean IS_SHOWNODE = true; // 是否显示节点
	public static boolean IS_SHOWEDGE = true; // 是否显示边
	public static boolean IS_SHOWNAME = true; // 时候显示节点标签
	
	public static final int NETWORK_ORIGINAL = 1;
	public static int NETWORK_STYLE = NETWORK_ORIGINAL;   //当前蛋白质网络类型 1-原始网络类型   2-随机网络类型   3-边数随机增加网络    4-边数随机减少网络
	public static final int NWTWORK_RANDOM = 2;
	public static final int NWTWORK_I_RANDOM = 3;
	public static final int NWTWORK_D_RANDOM = 3;
	
	public static float NETWORK_EXPAND_PERCENT = 0;  //网络缩放百分比
//	public static Vector<String> pluginPath = new Vector<String>(); // 插件位置 插件名称
//	public static Vector<String> PluginInfo = new Vector<String>(); // 插件描述
	
	public static final int CLUSTER = 1;          //显示一个簇中 所有蛋白
	public static final int OVERLAP = 2;          //显示簇与簇之间的交叠
	public static int CLUSTER_SHOW_MODE = CLUSTER;  //设置CLUSTER 显示方式 
	
	public static HashMap<String,Vector<Node>[]> algorithmsResults = new HashMap<String,Vector<Node>[]>();
	public static HashMap<String,RGB> algorithmsColorList = new HashMap<String,RGB>();
	
	public static HashMap<String, Set<String>> proteinFunction = new HashMap<String, Set<String>>();
	public static HashMap<String, String> functionAnnotation = new HashMap<String, String>();
	
	public static HashMap<String,String> goGeneAnnotation = new HashMap<String,String>();
	public static HashMap<String,Set<String>>  goFuncitonAnnotation = new HashMap<String,Set<String>>();
	public static HashMap<String,Set<String>>  goProcessAnnotation = new HashMap<String,Set<String>>();
	public static HashMap<String,Set<String>>  goComponentAnnotation = new HashMap<String,Set<String>>();
	public static HashMap<String,Set<String>> currentProteinFunction = proteinFunction;
	
	public static void refresh(){
		NETWORK_STYLE = 1;
		algorithmsResults.clear();
		algorithmsColorList.clear();
	}
}
