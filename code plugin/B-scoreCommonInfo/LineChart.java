package com.wuxuehong.plugin;

import java.util.Vector;

import org.eclipse.swt.graphics.Point;

public class LineChart {

	private Vector<LinePoint> pointList;

	public LineChart(Vector<LinePoint> pointlist) {
		this.pointList = pointlist;
	}

	public LineChart() {

	}

	public Vector<LinePoint> getPointList() {
		return pointList;
	}

	public void setPointList(Vector<LinePoint> pointList) {
		this.pointList = pointList;
	}

}
