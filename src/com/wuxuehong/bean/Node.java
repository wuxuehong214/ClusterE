package com.wuxuehong.bean;

import java.io.Serializable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.wuxuehong.bean.Paramater;


public class Node implements Serializable {

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		Node node = (Node)arg0;
		return this.getNodeID().equals(node.getNodeID());
	}

	private String NodeID = null;
	private int mx = 0;
	private int my = 0;
	private Vector<Node> neighbours = new Vector<Node>();
	private float expand_paramater = 1;
	private int scope = 1;         //节点规模       一般蛋白质节点规模为一   而当一个簇作为一个节点的时候规模就>=1
	private float v_scope = 1;
	

	public Node(String NodeID) {
		this.NodeID = NodeID;
	}

	// public Node(int mx,int my,String NodeID){
	// this.mx = mx;
	// this.my = my;
	// this.NodeID = NodeID;
	// }

	public String getNodeID() {
		return NodeID;
	}

	public void setNodeID(String nodeID) {
		NodeID = nodeID;
	}

	public int getMx() {
		return mx;
	}

	public void setMx(int mx) {
		this.mx = mx;
	}

	public int getMy() {
		return my;
	}

	public void setMy(int my) {
		this.my = my;
	}

	public Vector<Node> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(Vector<Node> neighbours) {
		this.neighbours = neighbours;
	}

	public int getNeighbour_NUM() {
		return neighbours.size();
	}

	public void drawMe(GC gc, Display display3) {
		int WIDTH = (int) (v_scope*Paramater.NODE_SIZE / expand_paramater);
//		int ds = Paramater.LINE_WIDTH/2;？
		int  ds = 1;
		if (Paramater.IS_SHOWNAME){
			gc.drawString(NodeID, mx - WIDTH / 2, my - WIDTH / 4 - getFontSize()*2, true);
		}
		if (Paramater.IS_SHOWNODE) {
			gc.fillOval(mx - WIDTH / 2, my - WIDTH / 2, WIDTH, WIDTH);
			gc.drawOval(mx - WIDTH / 2-ds, my - WIDTH / 2-ds, WIDTH, WIDTH);
		}
	}

	public Rectangle getRectangle() {
		int WIDTH = (int) (v_scope*Paramater.NODE_SIZE / expand_paramater);
		return new Rectangle(mx - WIDTH / 2, my - WIDTH / 2, WIDTH, WIDTH);
	}

	public float getExpand_paramater() {
		return expand_paramater;
	}

	public void setExpand_paramater(float expand_paramater) {
		this.expand_paramater = expand_paramater;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
		float value = 1+scope/5;
		if(value<=5)
		this.v_scope = 1+scope/5;
		else
			this.v_scope = 5;
	}

	public int getFontSize(){
		return (int) (Paramater.NODE_SIZE *0.3/ expand_paramater);
	}
	
}
