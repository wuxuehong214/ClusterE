package com.wuxuehong.plugin;

public class BarChart {

	private double[][] data;
	private String[] rowkeys;
	private String[] columkeys;

	public BarChart(double[][] data, String[] rowkeys, String[] columkeys) {
		this.data = data;
		this.rowkeys = rowkeys;
		this.columkeys = columkeys;
	}

	public BarChart() {

	}

	public double[][] getData() {
		return data;
	}

	public void setData(double[][] data) {
		this.data = data;
	}

	public String[] getRowkeys() {
		return rowkeys;
	}

	public void setRowkeys(String[] rowkeys) {
		this.rowkeys = rowkeys;
	}

	public String[] getColumkeys() {
		return columkeys;
	}

	public void setColumkeys(String[] columkeys) {
		this.columkeys = columkeys;
	}

}
