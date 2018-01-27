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
	 * �����㷨���� ���Դ˴����˵�
	 * 
	 * @return
	 */
	public String getAlgorithmName();

	/**
	 * ���ؾ�����
	 * 
	 * @param para
	 *            �㷨����
	 * @return
	 */
	public Vector<Node>[] getClusters(String[] para);

	/**
	 * ���ز�������
	 * 
	 * @return
	 */
	public String[] getParams();

	/**
	 * ���ز�����ʼֵ
	 * 
	 * @return
	 */
	public String[] getParaValues();

	/**
	 * ���ض��㷨���߶���������������
	 * 
	 * @return
	 */
	public String getDescription();	
	
	/**
	 *   ���ò������  ����1 ���� �㷨����  ����2����������������
	 * @return
	 */
	public int getStyle();
	
	/**
	 * ����������������
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
	 * ����� ������ʼ��
	 */
    public void variableInit();

}
