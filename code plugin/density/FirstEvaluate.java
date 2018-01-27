package com.wuxuehong.plugin;

import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import com.wuxuehong.bean.Clum;
import com.wuxuehong.bean.Node;
import com.wuxuehong.chart.BarChart;
import com.wuxuehong.chart.LineChart;
import com.wuxuehong.chart.LinePoint;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class FirstEvaluate extends Plugin implements NewAlgorithm {

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "一个评估方法 ，评估参数density";
	}

	/**
	 * 返回向量中 第一个装LineChart 第二个装BarChart
	 */
	@Override
	public Vector BuildCharts(Vector[] v) {
		List<Float> vv = new ArrayList<Float>();
		int edgecount = 0;
		float density = 0;
		int size = 0;
		int[] a = new int[10];
		for (int i = 0; i < v.length; i++) {
			Vector v1 = v[i];
			edgecount = 0;
			for (int j = 0; j < v1.size(); j++) {
				Node n1 = (Node) v1.get(j);
				for (int k = j + 1; k < v1.size(); k++) {
					Node n2 = (Node) v1.get(k);
					if (GraphInfo.edgemap.get(n1.getNodeID() + n2.getNodeID()) != null) {
						edgecount++;
					}
				}
			}
			size = v1.size();
			if (size > 1) {
				density = 2 * (float) edgecount
						/ ((float) size * (float) (size - 1));
				vv.add(density);
			}
			if (density <= 0.1) {
				a[0]++;
			} else if (density <= 0.2) {
				a[1]++;
			} else if (density <= 0.3) {
				a[2]++;
			} else if (density <= 0.4) {
				a[3]++;
			} else if (density <= 0.5) {
				a[4]++;
			} else if (density <= 0.6) {
				a[5]++;
			} else if (density <= 0.7) {
				a[6]++;
			} else if (density <= 0.8) {
				a[7]++;
			} else if (density <= 0.9) {
				a[8]++;
			} else if (density <= 1.0) {
				a[9]++;
			}
		}
		Collections.sort(vv);
		Collections.reverse(vv);
		LinePoint linepoint = null;
		Vector<LinePoint> pointlist = new Vector<LinePoint>();
		for (int i = 0; i < vv.size(); i++) {
			float value = vv.get(i);
			linepoint = new LinePoint(i + 1, value);
			pointlist.add(linepoint);
		}
		LineChart linechart = new LineChart(pointlist);

		double[][] data = new double[1][a.length];
		for (int i = 0; i < a.length; i++)
			data[0][i] = a[i];
		String[] columnkeys = { "0-0.1", "0.1-0.2", "0.2-0.3", "0.3-0.4",
				"0.4-0.5", "0.5-0.6", "0.6-0.7", "0.7-0.8", "0.8-9", "0.9-1" };
		BarChart barchart = new BarChart();
		barchart.setColumkeys(columnkeys);
		barchart.setData(data);

		Vector vector = new Vector();
		vector.add(linechart);
		vector.add(barchart);
		return vector;
	}

	@Override
	public String[] getEvaluateNames() {
		return new String[]{"density"};
	}

	@Override
	public Vector[] getClusters(String[] para) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParaValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	class ChartInfo {
		List v1; // 所有簇的密度值
		Vector v2; // 密度分布

		ChartInfo(List v1, Vector v2) {
			this.v1 = v1;
			this.v2 = v2;
		}
	}

	private static ChartComposite frame1;
	private static ChartComposite frame2;
	private static JFreeChart chart1;
	private static JFreeChart chart2;

	@Override
	public void drawCharts(String[] algorithm,String evaluatePara,
			HashMap<String, Vector> resultList, Composite composite,
			HashMap<String, RGB> colorlist) {
		// TODO Auto-generated method stub
		if (frame1 != null)
			frame1.dispose();
		if (frame2 != null)
			frame2.dispose();
		Vector vector = resultList.get(algorithm[0]);
		Color color = new Color(null, colorlist.get(algorithm[0]));
		composite.setLayout(new GridLayout(2, true));
		GridData gd = new GridData(GridData.FILL_BOTH);
		LineChart linechart = null;
		BarChart barchart = null;
		// 创建曲线图
		XYSeries series = null;
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		for (int i = 0; i < algorithm.length; i++) {
			series = new XYSeries(algorithm[i]);
			linechart = (LineChart) resultList.get(algorithm[i]).get(0);
			for (int j = 0; j < linechart.getPointList().size(); j++) {
				LinePoint linepoint = linechart.getPointList().get(j);
				series.add(linepoint.x, linepoint.y);
			}
			dataset1.addSeries(series);
		}
		chart1 = ChartFactory.createXYLineChart(null, "Cluster",
				"Density distribution", dataset1, PlotOrientation.VERTICAL,
				true, true, true);
		XYPlot cate1 = (XYPlot) chart1.getPlot();
		XYLineAndShapeRenderer lineRender =  (XYLineAndShapeRenderer) cate1.getRenderer();
		 for(int i=0;i<algorithm.length;i++){
			 RGB rgb = colorlist.get(algorithm[i]);
			 lineRender.setSeriesPaint(i, new java.awt.Color(rgb.red,rgb.green,rgb.blue));
			 }
		frame1 = new ChartComposite(composite, SWT.NONE, chart1);
		frame1.setLayoutData(gd);

		// 创建条形图
		double[][] data = new double[algorithm.length][];
		String[] rowkeys = algorithm;

		for (int i = 0; i < algorithm.length; i++) {
			barchart = (BarChart) resultList.get(algorithm[i]).get(1);
			data[i] = barchart.getData()[0];
		}
		String[] columnkeys = barchart.getColumkeys();
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
				rowkeys, columnkeys, data);
		chart2 = ChartFactory.createBarChart3D(null, "Density distribution",
				"Cluster Number", dataset, PlotOrientation.VERTICAL, true,
				false, false);
		CategoryPlot cate2 = chart2.getCategoryPlot();
		 BarRenderer brender = (BarRenderer) cate2.getRenderer();
		 for(int i=0;i<algorithm.length;i++){
		 RGB rgb = colorlist.get(algorithm[i]);
		 brender.setSeriesPaint(i, new java.awt.Color(rgb.red,rgb.green,rgb.blue));
		 }
		CategoryAxis categoryaxis = cate2.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		frame2 = new ChartComposite(composite, SWT.NONE, chart2);
		frame2.setLayoutData(gd);
		composite.layout();

	}

}
