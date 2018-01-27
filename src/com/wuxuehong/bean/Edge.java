package com.wuxuehong.bean;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.wuxuehong.bean.Paramater;


public class Edge implements Serializable {

	private Node node1;
	private Node node2;
	private double weight = 1;

	public Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	public Node getNode1() {
		return node1;
	}

	public void setNode1(Node node1) {
		this.node1 = node1;
	}

	public Node getNode2() {
		return node2;
	}

	public void setNode2(Node node2) {
		this.node2 = node2;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void drawMe(GC gc, Display display) {
		if (Paramater.IS_SHOWEDGE){
//			gc.setLineWidth(Paramater.LINE_WIDTH);
			gc.drawLine((int) node1.getMx(), (int) node1.getMy(), (int) node2
					.getMx(), (int) node2.getMy());
		}
	}

}
