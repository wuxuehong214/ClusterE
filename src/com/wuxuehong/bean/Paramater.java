package com.wuxuehong.bean;


import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class Paramater {

//	public static Color BACK_COLOR = SWTResourceManager.getColor(200, 0, 0); // ����ɫ
//	public static Color FORE_COLOR = SWTResourceManager.getColor(0, 0, 0); // ǰ��ɫ

	public static Color BACK_COLOR = new Color(Display.getDefault(),new RGB(250,0,0)); // ����ɫ
	public static Color FORE_COLOR = new Color(Display.getDefault(),new RGB(0,0,0));// ǰ��ɫ
	public static Color SEPARATE_COLOR = new Color(Display.getDefault(),new RGB(000,0,250)); //������ɫ
	
	public static Node SEPARATE_NODE = null;
	public static int NODE_SIZE = 20;
	public static int LINE_WIDTH = 2;
	public static boolean IS_SHOWNODE = true; // �Ƿ���ʾ�ڵ�
	public static boolean IS_SHOWEDGE = true; // �Ƿ���ʾ��
	public static boolean IS_SHOWNAME = true; // ʱ����ʾ�ڵ��ǩ
	
	public static final int NETWORK_ORIGINAL = 1;
	public static int NETWORK_STYLE = NETWORK_ORIGINAL;   //��ǰ�������������� 1-ԭʼ��������   2-�����������   3-���������������    4-���������������
	public static final int NWTWORK_RANDOM = 2;
	public static final int NWTWORK_I_RANDOM = 3;
	public static final int NWTWORK_D_RANDOM = 3;
	
	public static float NETWORK_EXPAND_PERCENT = 0;  //�������Űٷֱ�
//	public static Vector<String> pluginPath = new Vector<String>(); // ���λ�� �������
//	public static Vector<String> PluginInfo = new Vector<String>(); // �������
	
	public static final int CLUSTER = 1;          //��ʾһ������ ���е���
	public static final int OVERLAP = 2;          //��ʾ�����֮��Ľ���
	public static int CLUSTER_SHOW_MODE = CLUSTER;  //����CLUSTER ��ʾ��ʽ 
	
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
