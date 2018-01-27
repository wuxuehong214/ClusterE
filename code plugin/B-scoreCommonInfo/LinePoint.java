package com.wuxuehong.plugin;

public class LinePoint implements Comparable{

	public double x;

	public double y;

	public LinePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		LinePoint l = (LinePoint)o;
		return new Double(y).compareTo(new Double(l.y));
	}

}
