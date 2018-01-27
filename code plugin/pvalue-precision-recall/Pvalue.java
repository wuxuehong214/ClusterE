package com.wuxuehong.plugin;

import java.awt.GradientPaint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import jxl.Workbook;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
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

import clustere.dialogs.ProgressBarDialog;

import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class Pvalue extends Plugin implements NewAlgorithm {

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
		return "About the p-value,precision,recall and f-measure distribution of the clusters";
	}
	

	public Vector BuildCharts(Vector[] v,HashMap<String,Set<String>> functionProtein,HashMap<String,Set<String>> proteinFunction) {
		Cluster cluster;
		Vector<Cluster> clusterList = new Vector<Cluster>();
		Vector tempv;
		Set<String> funlist = new HashSet<String>();
		int complex_protein = 0; // 复合物中蛋白质数量
		int total_protein = GraphInfo.nodelist.size(); // 整个蛋白质网络中节点总个数
		int complex_fun = 0; // 复合物中含有某功能蛋白质数量
		int network_fun = 0; // 整个网络中含有该功能的蛋白质数量
		double min_value = 100;
		String function = null;
		for (int i = 0; i < v.length; i++) {
			/*************** 变量初始化 ********************/
			complex_protein = 0;
			complex_fun = 0;
			network_fun = 0;
			min_value = 100;
			function = "";
			int c = 0, d = 0;
			funlist.clear();

			tempv = v[i];
			complex_protein = tempv.size(); // 复合物中蛋白质数量
			for (int j = 0; j < tempv.size(); j++) {
				Node node = (Node) tempv.get(j);
				if (proteinFunction.get(node.getNodeID()) != null) {
					funlist.addAll(proteinFunction.get(node
							.getNodeID())); // 将该簇中所有的功能收集
//	if(funlist.contains("550.2.195"))System.out.println(node.getNodeID()+"&&&&&&&&&&&&&&&&");
				}
//if(i==167)System.out.println(funlist.size()+"  "+j);YHR024C&&&&&&&&&&&&&&&&
//				YLR163C&&&&&&&&&&&&&&&&
			}
			Iterator it = funlist.iterator();
			while(it.hasNext()){
				String str = (String)it.next();
				complex_fun = 0;
				// 查看本复合物中含有该功能蛋白质数量
				for (int l = 0; l < tempv.size(); l++) {
					Node node = (Node) tempv.get(l);
					String protein = node.getNodeID();
					Set v1 = proteinFunction.get(protein);
					if (v1 != null && v1.contains(str))
						complex_fun++; // 复合物中含有某功能蛋白质数量
				}
				// 查看整个网络中含有该功能蛋白质数量
//System.out.println(str+"************"+functionProtein.get(str));
				network_fun = functionProtein.get(str).size(); // 整个网络中含有该功能的蛋白质数量
				double vr = CalPvalue(total_protein, complex_protein,
						complex_fun, network_fun);
//if(i==167)System.out.println("pvalue:"+"**"+vr);
				if (vr <= min_value) {
					c = complex_fun;
					d = network_fun;
					min_value = vr;
					function = str;
				}
			}

			cluster = new Cluster();
			cluster.setProbableFunctions(funlist.size());
			cluster.setClusterID("Cluster" + i);
			cluster.setFunProtein(c);
			cluster.setNetProtein(d);
			cluster.setFunctionCode(function);
	String ff = functionAnnotation.get(function);
	      if(ff!=null)cluster.setFunction(ff);
			cluster.setProteins(tempv);
			if (funlist.size() == 0) {
				cluster.setPvalue(0.0);
			} else
				cluster.setPvalue(min_value);
			clusterList.add(cluster);
		}
		return clusterList;
	}

	/*
	 * 
	 *             n!
     *   nCr =  ---------
     *          r! (n-r)!
     * 
     * which means:
     * 
     *   log(nCr) = log(n!) - (log(r!) + log((n-r)!))
     *
     *   * Since :
   * 
   *        n! = n * (n-1) * (n-2) ... * 1
   * 
   * Then :
   * 
   *   log(n!) = log(n * (n-1) * (n-2) ... * 1)
   * 
   *           = log(n) + log(n-1) + log(n-2) ... + log(1)
     * If the value of N as supplied here is greater than the
     * constructor parameter maxPopulationSize, the behavior of this
     * method is undefined.
     *
     */
	/**
	 * 
	 * @param total_protein
	 *            整个蛋白质网络中总结点个数 N
	 * @param complex_protein
	 *            蛋白质复合物中蛋白质数量 C
	 * @param complex_funs
	 *            蛋白质复合物中含有某功能蛋白质数量 K
	 * @param net_funs
	 *            整个蛋白质网络中含有该功能蛋白质数量 F
	 * @return
	 */
//	public double CalPvalue2(int total_protein,int complex_protein,int complex_funs,int net_funs){
//		BigDecimal result = new BigDecimal(String.valueOf(0));
//		for(int i=0;i<complex_funs;i++){
//	             BigDecimal a = Cnr(net_funs,i);
//	             BigDecimal b = Cnr(total_protein-net_funs,complex_protein-i);
//	             BigDecimal c = Cnr(total_protein,complex_protein);
//	             a = a.add(b);
//	             a = a.subtract(c);
//	             BigDecimal d = new BigDecimal(Math.pow(10, a.doubleValue()));
//	             result = result.add(d);
//		}
//		return 1-result.doubleValue();
//	}
//	
//	public BigDecimal Cnr(int n,int r){
//		BigDecimal result = getlog(n);
//		result = result.subtract(getlog(r));
//		result = result.subtract(getlog(n-r));
//		return result;
//	}
//	
//	public BigDecimal getlog(int n){
//		BigDecimal result = new BigDecimal(0);
//		for(int i=1;i<=n;i++){
//			BigDecimal b = new BigDecimal(Math.log10(i));
//			result = result.add(b);
//		}
//		return result;
//	}
//	
	/**
	 * 该求pvalue方法采用大数定理
	 */
	public double CalPvalue(int total_protein, int complex_protein,
			int complex_funs, int net_funs) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal  a ,b;
		for (int i = 0; i < complex_funs; i++) {
			BigInteger aa = getC(net_funs, i).multiply(
					getC(total_protein - net_funs, complex_protein - i));
			BigInteger bb = getC(total_protein, complex_protein);
			a = new BigDecimal(aa.toString());
			b = new BigDecimal(bb.toString());
			 a= a.setScale(150);
			 b = b.setScale(150);
			a = a.divide(b,BigDecimal.ROUND_UP);
			result = result.add(a);
		}
		BigDecimal total = new BigDecimal(1);
		total = total.subtract(result);
		return total.doubleValue();
	}

	public BigInteger getC(int a, int b) {
		String str = String.valueOf(1);
		BigInteger value = new BigInteger(str);
		BigInteger bi;
		int aa = a;
		for (int i = 0; i < b; i++) {
			str = String.valueOf(aa);
			bi = new BigInteger(str);
			value = value.multiply(bi);
			aa--;
		}
		// str = String.valueOf(fun(b));
		return value.divide(fun1(b));
	}

	public BigInteger fun1(int a) {
		BigInteger value = new BigInteger(String.valueOf(1));
		for (int i = a; i >= 1; i--) {
			String str = String.valueOf(i);
			BigInteger bi = new BigInteger(str);
			value = value.multiply(bi);
		}
		return value;
	}

	// public int fun(int a){
	// if(a==0)return 1;
	// else return a*fun(a-1);
	// }

	// public BigInteger getC(int a, int b){
	// String str = String.valueOf(1);
	// BigInteger value = new BigInteger(str);
	// BigInteger bi;
	// int aa= a;
	// for(int i=0;i<b;i++){
	// str = String.valueOf(aa);
	// bi = new BigInteger(str);
	// value = value.multiply(bi);
	// aa--;
	// }
	// return value.divide(fun(a));
	// }

	// public BigInteger fun(int a){
	// if(a==0)return new BigInteger("1");
	// else return new BigInteger(String.valueOf(a)).multiply(fun(a-1));
	// }

	@Override
	public String getEvaluateNames() {
		return "C-Score";
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

	
	/**
	 * WindowShowAt show the opened window   default value is 0 ,it indicates that the window opened is the Integrated window 
	 * value 1   p-value charts 
	 * value 2  precision charts
	 * value 3  recall charts
	 * value 4  f-measure charts
	 */
	private  int WindowShowAt;     
	private HashMap<String,Vector> chartsResult ;
	private HashMap<String,Set<String>> proteinFunction;
	private HashMap<String,String> functionAnnotation;
	private HashMap<String,HashMap<String,Set<String>>> annotationFunctionProtein;
	private HashMap<String,HashMap<String,Vector>> annotationProteinCharts;//  一个基因注释对应      一个算法对应一个图表结果集
	private String annotation;
	private HashMap<String,Set<String>> otherProteinFunction;
	private HashMap<String,String> otherFunctionAnnotation;
	@Override
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist) {
		// TODO Auto-generated method stub
		 if(algorithm.length==0){
			 WindowShowAt = 0;
		 }
		 if(WindowShowAt==0) {
			 createComposite(composite,algorithm,colorlist);
			 return;
		 }
		new NewThread(composite,algorithm,resultList,colorlist);
	}
	
	@Override
	public void variableInit() {
		// TODO Auto-generated method stub
		WindowShowAt = 0;
		chartsResult = null;
		otherProteinFunction = null;
		otherFunctionAnnotation = null;
		proteinFunction = Paramater.proteinFunction;          //  一个蛋白质对应一个功能标号
		functionAnnotation = Paramater.functionAnnotation;     //一个功能标号 对应一个注释信息
		annotationFunctionProtein = new HashMap<String,HashMap<String,Set<String>>>();
		annotationProteinCharts = new HashMap<String,HashMap<String,Vector>>();
		annotation = "MIPS";
	}
	
	class NewThread implements Runnable{
		Composite comp;
		HashMap<String, RGB> colorlist;
		HashMap<String, Vector<Node>[]> resultList;
		String[] algorithm;
		ProgressBarDialog pbd;
		Thread t;
		NewThread(Composite comp,String[] algorithm,HashMap<String, Vector<Node>[]> resultList,HashMap<String, RGB> colorlist){
			this.comp = comp;
			this.colorlist = colorlist;
			this.resultList = resultList;
			this.algorithm = algorithm;
			t = new Thread(this);
			t.start();
		}
		public void run() {
			comp.getDisplay().asyncExec(new Runnable(){
				public void run() {
					pbd = new ProgressBarDialog(comp.getShell(), SWT.NONE,t);
					pbd.getLabel().setText("Calculating the charts.......");
				}
			});
			 calculate(algorithm, resultList);//  annotation  ------algorithm--charts
			 chartsResult = annotationProteinCharts.get(annotation);
			 comp.getDisplay().asyncExec(new Runnable(){
				public void run() {
					if(WindowShowAt==1) calCulatePvalue(algorithm, chartsResult, comp, colorlist, pvalue+getFigA("lg(p-value)")+getFigB("-lg(p-value)"));
					else if(WindowShowAt==2)calCulatePreCisionandRecall(algorithm, "Precision", chartsResult, comp, colorlist, precision+getFigA("precision")+getFigB("precision"));
					else if(WindowShowAt==3)calCulatePreCisionandRecall(algorithm, "Recall", chartsResult, comp, colorlist, recall+getFigA("recall")+getFigB("recall"));
					else if(WindowShowAt==4)calCulatePreCisionandRecall(algorithm, "f-measure", chartsResult, comp, colorlist, recall+getFigA("f-measure")+getFigB("f-measure"));
					pbd.getLabel().setText("complete!");
					pbd.dispose();
				}
			 });
		}
	}
	/**
	 * 计算  一个算法对应 一个图形结果集
	 * @param algorithm
	 * @param resultList
	 */
	public void calculate(String[] algorithm,HashMap<String,Vector<Node>[]> resultList){
		HashMap<String,Set<String>> functionProtein = annotationFunctionProtein.get(annotation);
		for(int i=0;i<algorithm.length;i++){
			Vector[] v=resultList.get(algorithm[i]); 
			if(annotationProteinCharts.get(annotation)==null){
				HashMap<String,Vector> temp = new HashMap<String,Vector>();
				temp.put(algorithm[i], BuildCharts(v,functionProtein,proteinFunction));
				annotationProteinCharts.put(annotation, temp);
			}else {
				if(annotationProteinCharts.get(annotation).get(algorithm[i])==null)
				annotationProteinCharts.get(annotation).put(algorithm[i], BuildCharts(v, functionProtein, proteinFunction));
			}
		}
	}
	/**
	 * 计算  每个function 所对应的所有protein
	 */
	public void calCulateFunctionProtein(){
	    if(annotationFunctionProtein.get(annotation)!=null) return;
	    HashMap<String,Set<String>> functionProtein = new HashMap<String,Set<String>>();
		Set<String> funlist = null;
		String fun = null;
		for(int i=0;i<GraphInfo.nodelist.size();i++){
			Node n = GraphInfo.nodelist.get(i);
			String str = n.getNodeID();
			if(proteinFunction.get(str)!=null){
				funlist = proteinFunction.get(str); //功能注释集
				Iterator it = funlist.iterator();  //遍历所有功能
				while(it.hasNext()){
					fun = (String)it.next();
//		if(fun.equals("550.2.195"))System.out.print(str+"**********************************");
					if(functionProtein.get(fun)!=null)
						functionProtein.get(fun).add(str);
					else {
						Set<String> proteins = new HashSet<String>();
						proteins.add(str);
						functionProtein.put(fun, proteins);
					}
				}
			}
		}
		annotationFunctionProtein.put(annotation, functionProtein);
	}
	
	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return NewAlgorithm.Evaluation;
	}

//	public void drawCharts(String[] algorithm, String para,
//			HashMap<String, Vector> resultList, Composite composite,
//			HashMap<String, RGB> colorlist) {
////	      this.algorithm = algorithm;
////	      this.para = para;
////	      this.resultList = resultList;
////	      this.colorlist = colorlist;
////	      this.composite = composite;
//		 if(WindowShowAt==0) createComposite(composite);
//		 if(WindowShowAt==1) calCulatePvalue(algorithm, para, resultList, composite, colorlist, pvalue+getFigA("lg(p-value)")+getFigB("-lg(p-value)"));
//		 if(WindowShowAt==2)calCulatePreCisionandRecall(algorithm, "Precision", resultList, composite, colorlist, precision+getFigA("precision")+getFigB("precision"));
//		 if(WindowShowAt==3)calCulatePreCisionandRecall(algorithm, "Recall", resultList, composite, colorlist, recall+getFigA("recall")+getFigB("recall"));
//		 if(WindowShowAt==4)calCulatePreCisionandRecall(algorithm, "f-measure", resultList, composite, colorlist, recall+getFigA("f-measure")+getFigB("f-measure"));
//		
//		
////		
////		if(para.equals("P-value"))calCulatePvalue(algorithm, para, resultList, composite, colorlist);
////		else 
////			calCulatePreCisionandRecall(algorithm, para, resultList, composite, colorlist);
//
//	}

	private  SashForm sashForm;
	
	private  Composite currentComposite;
	
	private String pvalue = "In order to detect the functional characteristics of the predicted clusters,we compare the predicted clusters with "+
                            "known functional classification.The P-value based on hypergeometric distribution is often used to estimate whether a given set of proteins is accumulated by chance."+
                            "It has been used as a criteria to assign each predicted cluster a main function.Here,we also calculate Pvalue for each predicted cluster and assign a function category to it when the minimum P-value occurrs.\n";
	private String precision = "The Precision for a cluster is the number of true positives divided by the total number of elements "+
	"labeled as belonging to the positive cluster.\nprecision = tp/(tp+fp) where tp is the number of overlap and fp+tp is the namuber of the nodes in the cluster\n";
	private String recall = "Recall is defined as the number of true positives divided by the "+
	"total number of elements that actually belong to the positive.\nrecall=tp/(tp+fn) where tp is the number of overlap and tp+fn is the number of the background\n";
	private String fmeasure = "A measure that combines Precision and Recall is the harmonic mean of precision and recall, the traditional F-measure or balanced F-score.\nf-measure=2*precision*recall/(precision+recall)\n";
	
//	private String[] algorithm;
//	private String para ;
//	private HashMap<String, Vector> resultList;
//	private HashMap<String, RGB> colorlist;
//	private Composite composite;
	
	private String getFigA(String para){
		return "Fig A shows the "+para+" of each cluster.\n";
	}
	private String getFigB(String para){
		return "Fig B shows the number of the clusters in different interval of the "+para+".";
	}
	
	public void createComposite(final Composite composite,final String[] algorithm,final HashMap<String, RGB> colorlist){
		if(currentComposite!=null)
			currentComposite.dispose();
		if (sashForm != null)
			sashForm.dispose();
		WindowShowAt = 0;
		composite.setLayout(new FillLayout());
		currentComposite = new Composite(composite,SWT.NONE);
		currentComposite.setLayout(new FillLayout(SWT.VERTICAL));
		Group group2 = new Group(currentComposite,SWT.NONE);
		group2.setText("Evaluation Description");
		group2.setLayout(new FillLayout());
		final Text text = new Text(group2,SWT.WRAP|SWT.V_SCROLL|SWT.H_SCROLL);
		text.append(pvalue);
		Group group3 = new Group(currentComposite,SWT.NONE);
		group3.setText("Gene Annotation");
		group3.setLayout(new GridLayout(4,true));
		GridData gd = new GridData();
		gd.horizontalSpan= 3;
		Label label5 = new Label(group3,SWT.NONE);
		label5.setText("GO:");
		final Button button5 = new Button(group3,SWT.RADIO);
		button5.setText("Process");
		if(annotation.equals("Process"))button5.setSelection(true);
		final Button button6 = new Button(group3,SWT.RADIO);
		button6.setText("Function");
		if(annotation.equals("Function"))button6.setSelection(true);
		final Button button7 = new Button(group3,SWT.RADIO);
		button7.setText("Componment");
		if(annotation.equals("Componment"))button7.setSelection(true);
		Label label8 = new Label(group3,SWT.NONE);
		label8.setText("MIPS:");
		final Button button8 = new Button(group3,SWT.RADIO);
		button8.setText("MIPS");
		if(annotation.equals("MIPS"))button8.setSelection(true);
		button8.setLayoutData(gd);
		Label label9 = new Label(group3,SWT.NONE);
		label9.setText("Others:");
		final Button button9 = new Button(group3,SWT.RADIO);
		button9.setText("Others");
		button9.setLayoutData(gd);
		if(annotation.equals("Others"))button9.setSelection(true);
		Group group4 = new Group(currentComposite,SWT.NONE);
		group4.setText("Import new gene annotation");
		group4.setLayout(new GridLayout(2,true));
		GridData gd2 = new GridData();
		gd2.horizontalSpan= 2;
		Label label1 = new Label(group4,SWT.NONE);
		label1.setText("Please input the file with the format:"+"protein description"+"for example:YGL200w cyst per line");
		label1.setLayoutData(gd2);
		final Text text2 = new Text(group4,SWT.BORDER);
		text2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text2.setEditable(false);
		final Button button10 = new Button(group4,SWT.NONE);
		button10.setText("import");
		button10.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				openOthersAnnotation(composite,text2);
			}
		});
		button10.setEnabled(false);
		Label label2 = new Label(group4,SWT.NONE);
		label2.setText("If you need to load a new file to match the file imported ,please import ...");
		label2.setLayoutData(gd2);
		final Text text3 = new Text(group4,SWT.BORDER);
		text3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text3.setEditable(false);
		final Button button11 = new Button(group4,SWT.NONE);
		button11.setText("import");
		button11.setEnabled(false);
		button11.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				openOtherFunctionAnnotation(composite,text3);
			}
		});
		button5.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				button10.setEnabled(false);
				button11.setEnabled(false);
			}
		});
		button6.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				button10.setEnabled(false);
				button11.setEnabled(false);
			}
		});
		button7.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				button10.setEnabled(false);
				button11.setEnabled(false);
			}
		});
		button8.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				button10.setEnabled(false);
				button11.setEnabled(false);
			}
		});
		button9.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				button10.setEnabled(true);
				button11.setEnabled(true);
			}
 		});
		Composite twogroup = new Composite(currentComposite,SWT.NONE);
		twogroup.setLayout(new GridLayout(2,true));
		GridData tgd = new GridData(GridData.FILL_BOTH);
		Group group1 = new Group(twogroup,SWT.NONE);
		group1.setLayoutData(tgd);
		group1.setLayout(new GridLayout(2,true));
		group1.setText("Choose Evaluation");
		final Button button1 = new Button(group1,SWT.RADIO);
		button1.setText("p-value");
		button1.setSelection(true);
		final Button button2 = new Button(group1,SWT.RADIO);
		button2.setText("Precision");
		final Button button3 = new Button(group1,SWT.RADIO);
		button3.setText("Recall");
		final Button button4 = new Button(group1,SWT.RADIO);
		button4.setText("f-measure");
		GridData gd4 = new GridData();
		gd4.horizontalSpan = 2;
		Button  showChart = new Button(group1,SWT.NONE);
		showChart.setText("Show Chart");
		showChart.setLayoutData(gd4);
		showChart.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				   if(button8.getSelection())
					   {
					   proteinFunction = Paramater.proteinFunction;   //MIPS
					   functionAnnotation = Paramater.functionAnnotation; //MIPS annotation
					   annotation = button8.getText();
					   }
			        else  if(button5.getSelection())
			        	{
			        	proteinFunction = Paramater.goProcessAnnotation;   //GO : Process
			        	functionAnnotation = Paramater.goGeneAnnotation;
			        	annotation = button5.getText();
			        	}
				    else if(button6.getSelection())
				    	{
				    	proteinFunction = Paramater.goFuncitonAnnotation;  //GO: Function
				    	functionAnnotation = Paramater.goGeneAnnotation;
				    	annotation = button6.getText();
				    	}
				    else if(button7.getSelection())
				    	{
				    	proteinFunction = Paramater.goComponentAnnotation;  //GO: Component
				    	functionAnnotation = Paramater.goGeneAnnotation;
				    	annotation = button7.getText();
				    	}else if(button9.getSelection()){
				    		proteinFunction = otherProteinFunction;
				    		functionAnnotation = otherFunctionAnnotation;
				    		if(proteinFunction==null){
				    			MessageDialog.openError(composite.getShell(), "Eror", "Please import the gene annotation");
				    			return;
				    		}
				    		annotation = button9.getText();
				    	}
				    calCulateFunctionProtein();
				    chartsResult = annotationProteinCharts.get(annotation);
			        if(button1.getSelection()){
			        	calCulatePvalue(algorithm, chartsResult, composite, colorlist, pvalue+getFigA("lg(p-value)")+getFigB("-lg(p-value)"));
			        	WindowShowAt = 1;
			        }else if(button2.getSelection()){
			        	calCulatePreCisionandRecall(algorithm, "Precision", chartsResult, composite, colorlist, precision+getFigA("precision")+getFigB("precision"));
			        	WindowShowAt = 2;
			        }else if(button3.getSelection()){
			        	calCulatePreCisionandRecall(algorithm, "Recall", chartsResult, composite, colorlist, recall+getFigA("recall")+getFigB("recall"));
			        	WindowShowAt = 3;
			        }else if(button4.getSelection()){
			        	calCulatePreCisionandRecall(algorithm, "f-measure", chartsResult, composite, colorlist, recall+getFigA("f-measure")+getFigB("f-measure"));
			        	WindowShowAt = 4;
			        }
			}
		});
		Group group11 = new Group(twogroup,SWT.NONE);
		group11.setLayoutData(tgd);
		group11.setLayout(new GridLayout());
		group11.setText("Current Algorihtms");
		for(int i=0;i<algorithm.length;i++){
			Label l = new Label(group11,SWT.NONE);
			l.setText(algorithm[i]);
		}
		button1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				text.append(pvalue);
			}
		});
		button2.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				 text.append(precision);
			}
		});
		button3.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				text.append(recall);
			}
		});
		button4.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				text.append(fmeasure);
			}
		});
		composite.layout();
	}
	
	private static Combo combo;
	private boolean a = true;
	
/**
 * 显示pvalue 图表
 * @param algorithm
 * @param para
 * @param resultList
 * @param composite
 * @param colorlist
 */
	public void calCulatePvalue(String[] algorithm,
			final HashMap<String, Vector> resultList, Composite composite,
			HashMap<String, RGB> colorlist,String showInfo) {
		if(currentComposite!=null)
			currentComposite.dispose();
		if (sashForm != null)
			sashForm.dispose();
		composite.setLayout(new FillLayout());
		sashForm = new SashForm(composite, SWT.VERTICAL);
		SashForm comp1 = new SashForm(sashForm, SWT.VERTICAL);
		Composite desComp = new Composite(comp1,SWT.NONE);
		desComp.setLayout(new GridLayout(5,true));
		GridData gdd = new GridData(GridData.FILL_BOTH);
		gdd.horizontalSpan = 4;
		gdd.verticalSpan = 2;
		Group group1 = new Group(desComp,SWT.NONE);
		group1.setText("Description for p-value");
		group1.setLayoutData(gdd);
		group1.setLayout(new FillLayout());
		Text text = new Text(group1,SWT.WRAP|SWT.V_SCROLL);
		text.setText(showInfo);
		Group group2 = new Group(desComp,SWT.NONE);
		group2.setText("Navigation");
		group2.setLayout(new FillLayout());
		combo = new Combo(group2,SWT.READ_ONLY|SWT.DROP_DOWN);
		combo.add("Main Composite");
		combo.add("Precision");
		combo.add("Recall");
		combo.add("f-measure");
		combo.add("P-value");
		combo.setText("P-value");
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		combo.addSelectionListener(new Controllor(composite,algorithm,colorlist));
		Group group3 = new Group(desComp,SWT.NONE);
		group3.setLayoutData(new GridData(GridData.FILL_BOTH));
		group3.setText("Gene Annotation");
		group3.setLayout(new FillLayout());
		Label anno = new Label(group3,SWT.NONE);
		anno.setText(annotation);
		
		Composite comp = new Composite(comp1,SWT.BORDER);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp1.setWeights(new int[]{2,4});
		// comp.setBackground(new Color(null,new RGB(0,0,0)));
		Composite tableComp = new Composite(sashForm,SWT.BORDER);
		sashForm.setWeights(new int[] { 3, 1 });
		tableComp.setLayout(new GridLayout(6,true));
		final TableViewer tv = new TableViewer(tableComp, SWT.FULL_SELECTION);
		GridData gdtable = new GridData(GridData.FILL_BOTH);
		gdtable.horizontalSpan = 5;
		final Table table = tv.getTable();
		table.setLayoutData(gdtable);
		TableColumn tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Cluster");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?1:-1);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Nodes Num");
		tc.setWidth(80);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?2:-2);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Background");
		tc.setWidth(80);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?3:-3);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Overlap");
		tc.setWidth(60);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?4:-4);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("p-value");
		tc.setWidth(130);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?5:-5);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("FunctionCode");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?6:-6);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Function");
		tc.setWidth(200);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?7:-7);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Contain functions");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?8:-8);
				tv.refresh();
			}
		});
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		Group tableControl = new Group(tableComp,SWT.NONE);
		tableControl.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableControl.setText("Table Control");
		tableControl.setLayout(new GridLayout());
		final Combo tableCombo = new Combo(tableControl,SWT.READ_ONLY);
		Button saveTable = new Button(tableControl,SWT.NONE);
		saveTable.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				saveExcelTable(table,"Gene Annotation:"+annotation+"   "+"Algorithm："+tableCombo.getText()+"   "+"Evaluation:p-value");
			}
		});
		saveTable.setText("Save Table");
		if(algorithm.length==0||resultList==null){
			composite.layout();
			return;
		}
		for(int i=0;i<algorithm.length;i++){
			if(resultList.get(algorithm[i])==null){
				composite.layout();
				return;
			}
		}
		tv.setContentProvider(new PvalueContentProvider());
		tv.setLabelProvider(new PvalueLabelProvider("Pvalue"));
		tv.setSorter(new PvalueSort("Pvalue"));
		if(algorithm.length==1){
			ArrayList<Cluster> al = new ArrayList<Cluster>();
			Vector clusterlist = resultList.get(algorithm[0]);
			for(int i=0;i<clusterlist.size();i++){
				al.add((Cluster)clusterlist.get(i));
			}
			tableCombo.add(algorithm[0]);
			tableCombo.setText(algorithm[0]);
			tv.setInput(al);
		}else{
			for(int i=0;i<algorithm.length;i++)
				tableCombo.add(algorithm[i]);
			tableCombo.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					String alg = tableCombo.getText();
					Vector clusterlist = resultList.get(alg);
					ArrayList<Cluster> al = new ArrayList<Cluster>();
					al.addAll(clusterlist);
					tv.setInput(al);
					tv.refresh();
				}
			});
		}
		
		/**************** 创建曲线图 **************/
		XYSeries series = null;
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		Vector clusterlist = null;
		Cluster cluster = null;
		Cluster.comparePara ="P-value";
		for (int i = 0; i < algorithm.length; i++) {
			series = new XYSeries(algorithm[i]);
			clusterlist = (Vector) resultList.get(algorithm[i]);
			Collections.sort(clusterlist);
			for (int j = 0; j < clusterlist.size(); j++) {
				cluster = (Cluster) clusterlist.get(j);
				if (cluster.getPvalue() != (double) 0.0)
					series.add(j + 1, Math.log10(cluster.getPvalue()));
			}
			dataset1.addSeries(series);
		}
		JFreeChart chart = ChartFactory.createXYLineChart(null, "Fig A",
				"lg(p-value)", dataset1, PlotOrientation.VERTICAL, true, true,
				true);
		XYPlot cate1 = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer lineRender = (XYLineAndShapeRenderer) cate1
				.getRenderer();
		for (int i = 0; i < algorithm.length; i++) {
			RGB rgb = colorlist.get(algorithm[i]);
			lineRender.setSeriesPaint(i, new java.awt.Color(rgb.red, rgb.green,
					rgb.blue));
		}
		comp.setLayout(new GridLayout(2,true));
		GridData framegd = new GridData(GridData.FILL_BOTH);
		ChartComposite frame = new ChartComposite(comp, SWT.NONE, chart);
		frame.setLayoutData(framegd);
		/************************* 创建 柱状图************************/
		 double[][] data = new double[algorithm.length][10];
		  String[] rowkeys = algorithm;
		  Cluster cluster1 = null;
		  double value = 0;
		   for (int i = 0; i < algorithm.length; i++) {
			   Vector clusterlist1 = resultList.get(algorithm[i]);
              for(int j=0;j<clusterlist1.size();j++){
           	   cluster1 = (Cluster)clusterlist1.get(j);
           	   value = cluster1.getPvalue();
           	   value = -Math.log10(value);
           	   if(value<=1)
           		   data[i][0]++;
           	   else if(value<=2)
           		   data[i][1]++;
           	   else if(value<=3)
           		   data[i][2]++;
           	   else if(value<=4)
           		   data[i][3]++;
           	   else if(value<=5)
           		   data[i][4]++;
           	   else if(value<=6)
           		   data[i][5]++;
           	   else if(value<=7)
           		   data[i][6]++;
           	   else if(value<=8)
           		   data[i][7]++;
           	   else if(value<=9)
           		   data[i][8]++;
           	   else 
           		   data[i][9]++;
              }
			   
		   }
		String[] columnkeys = { "0-1", "1-2", "2-3", "3-4",
				"4-5", "5-6", "6-7", "7-8", "8-9", "9--" };
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
				rowkeys, columnkeys, data);
		JFreeChart chart2 = ChartFactory.createBarChart3D(null, "Fig B",
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
		ChartComposite frame2 = new ChartComposite(comp, SWT.NONE, chart2);
		frame2.setLayoutData(framegd);
		composite.layout();
	}
	/**
	 * 计算precision recall
	 * @param algorithm
	 * @param para
	 * @param resultList
	 * @param composite
	 * @param colorlist
	 */
	public void calCulatePreCisionandRecall(String[] algorithm, final String para,
			final HashMap<String, Vector> resultList, Composite composite,
			HashMap<String, RGB> colorlist,String showInfo) {
		if(currentComposite!=null)
			currentComposite.dispose();
		if (sashForm != null)
			sashForm.dispose();
		composite.setLayout(new FillLayout());
		sashForm = new SashForm(composite, SWT.VERTICAL);
		
		SashForm comp1 = new SashForm(sashForm, SWT.VERTICAL);
		Composite desComp = new Composite(comp1,SWT.NONE);
		desComp.setLayout(new GridLayout(5,true));
		GridData gdd = new GridData(GridData.FILL_BOTH);
		gdd.horizontalSpan = 4;
		gdd.verticalSpan = 2;
		Group group1 = new Group(desComp,SWT.NONE);
		group1.setText("Description for "+para);
		group1.setLayoutData(gdd);
		group1.setLayout(new FillLayout());
		Text text = new Text(group1,SWT.WRAP|SWT.V_SCROLL);
		text.setText(showInfo);
		Group group2 = new Group(desComp,SWT.NONE);
		group2.setText("Navigation");
		group2.setLayout(new FillLayout());
		combo = new Combo(group2,SWT.READ_ONLY|SWT.DROP_DOWN);
		combo.add("Main Composite");
		combo.add("P-value");
		combo.add("Precision");
		combo.add("f-measure");
		combo.add("Recall");
		if(para.equals("Recall")){
			combo.setText("Recall");
		}
		if(para.equals("Precision")){
			combo.setText("Precision");
		}
		if(para.equals("f-measure")){
			combo.setText("f-measure");
		}
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		combo.addSelectionListener(new Controllor(composite,algorithm,colorlist));
		Group group3 = new Group(desComp,SWT.NONE);
		group3.setLayoutData(new GridData(GridData.FILL_BOTH));
		group3.setText("Gene Annotation");
		group3.setLayout(new FillLayout());
		Label anno = new Label(group3,SWT.NONE);
		anno.setText(annotation);
		
		Composite com = new Composite(comp1,SWT.BORDER);
		com.setLayout(new GridLayout(2,true));
		GridData gd = new GridData(GridData.FILL_BOTH);
		comp1.setWeights(new int[]{2,4});
		Composite tableComp = new Composite(sashForm,SWT.BORDER);
		tableComp.setLayout(new GridLayout(6,true));
		sashForm.setWeights(new int[]{3,1});
		final TableViewer tv = new TableViewer(tableComp,SWT.FULL_SELECTION);
		GridData gdtable = new GridData(GridData.FILL_BOTH);
		gdtable.horizontalSpan = 5;
		final Table table = tv.getTable();
		table.setLayoutData(gdtable);
		TableColumn tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Cluster");
		tc.setWidth(130);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?1:-1);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Nodes Num");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?2:-2);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Background");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?3:-3);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText("Overlap");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?4:-4);
				tv.refresh();
			}
		});
		tc = new TableColumn(table, SWT.CENTER);
		tc.setText(para);
		tc.setWidth(150);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a = !a;
				((PvalueSort)tv.getSorter()).doSort(a?5:-5);
				tv.refresh();
			}
		});
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		Group tableControl = new Group(tableComp,SWT.NONE);
		tableControl.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableControl.setLayout(new GridLayout());
		final Combo tableCombo = new Combo(tableControl,SWT.READ_ONLY);
		Button button = new Button(tableControl,SWT.NONE);
		button.setText("Save Table");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				saveExcelTable(table,"Gene Annotation:"+annotation+"   "+"Algorithm："+tableCombo.getText()+"   "+"Evaluation:"+para);
			}
		});
		if(algorithm.length==0||resultList==null){
			composite.layout();
			return;
		}
		for(int i=0;i<algorithm.length;i++){
			if(resultList.get(algorithm[i])==null){
				composite.layout();
				return;
			}
		}
		tv.setContentProvider(new PvalueContentProvider());
		tv.setLabelProvider(new PvalueLabelProvider(para));
		tv.setSorter(new PvalueSort(para));
		if(algorithm.length==1){
			Vector list = resultList.get(algorithm[0]);
			ArrayList<Cluster> al = new ArrayList<Cluster>();
			for(int i=0;i<list.size();i++)
				al.add((Cluster)list.get(i));
			tableCombo.add(algorithm[0]);
			tableCombo.setText(algorithm[0]);
			tv.setInput(al);
		}else{
			for(int i=0;i<algorithm.length;i++){
				tableCombo.add(algorithm[i]);
			}
			tableCombo.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					String alg = tableCombo.getText();
					Vector list = resultList.get(alg);
					ArrayList<Cluster> al = new ArrayList<Cluster>();
					al.addAll(list);
					tv.setInput(al);
					tv.refresh();
				}
			});
		}
//		for(int i=0;i<algorithm.length;i++){
//			Vector list = resultList.get(algorithm[i]);
//			for(int j=0;j<list.size();j++){
//				cluster = (Cluster)list.get(j);
//				if(para.equals("Precision"))	new TableItem(table, SWT.LEFT).setText(new String[]{cluster.getClusterID(),
//						"" + cluster.getProteins().size() + "",
//						"" + cluster.getNetProtein() + "",
//						"" + cluster.getFunProtein() + "",
//						"" + cluster.getPrecision()+ ""});
//				if(para.equals("Recall"))new TableItem(table, SWT.LEFT).setText(new String[]{cluster.getClusterID(),
//						"" + cluster.getProteins().size() + "",
//						"" + cluster.getNetProtein() + "",
//						"" + cluster.getFunProtein() + "",
//						"" + cluster.getRecall()+ ""});
//				if(para.equals("f-measure"))new TableItem(table, SWT.LEFT).setText(new String[]{cluster.getClusterID(),
//						"" + cluster.getProteins().size() + "",
//						"" + cluster.getNetProtein() + "",
//						"" + cluster.getFunProtein() + "",
//						"" + cluster.getMeasure()+ ""});
//			}
//		}
		/**********************画曲线图*************************/
		ChartComposite form1 = createLineChart(algorithm, para, resultList, com, colorlist);
		form1.setLayoutData(gd);
		/**********************画柱状图************************/
		ChartComposite form2 = createBarChart(algorithm, para, resultList, com, colorlist);
		form2.setLayoutData(gd);
		
		composite.layout();
	     }
	
	/**
	 * 曲线图
	 * @param algorithm
	 * @param para
	 * @param resultList
	 * @param composite
	 * @param colorlist
	 * @return
	 */
	public  ChartComposite createLineChart(String[] algorithm, String para,
			HashMap<String, Vector> resultList, Composite composite,
			HashMap<String, RGB> colorlist){
		 XYSeries series = null;
		 Cluster cluster = null;
		 Vector clusterlist = null;
		 Cluster.comparePara = para;
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		for (int i = 0; i < algorithm.length; i++) {
			series = new XYSeries(algorithm[i]);
			 clusterlist = resultList.get(algorithm[i]);
			 Collections.sort(clusterlist);
			for (int j = 0; j < clusterlist.size(); j++) {
				cluster = (Cluster)clusterlist.get(j);
				if(para.equals("Precision"))series.add(j+1, cluster.getPrecision());
				if(para.equals("Recall"))series.add(j+1, cluster.getRecall());
				if(para.equals("f-measure"))series.add(j+1,cluster.getMeasure());
			}
			dataset1.addSeries(series);
		}
		JFreeChart chart1 = ChartFactory.createXYLineChart(null, "Fig A",
				para+"distribution", dataset1, PlotOrientation.VERTICAL,
				true, true, true);
		XYPlot cate1 = (XYPlot) chart1.getPlot();
		XYLineAndShapeRenderer lineRender =  (XYLineAndShapeRenderer) cate1.getRenderer();
		 for(int i=0;i<algorithm.length;i++){
			 RGB rgb = colorlist.get(algorithm[i]);
			 lineRender.setSeriesPaint(i, new java.awt.Color(rgb.red,rgb.green,rgb.blue));
			 }
		 ChartComposite frame1 = new ChartComposite(composite, SWT.NONE, chart1);
		 return frame1;
	}
	
/**
 * 柱状图
 * @param algorithm
 * @param para
 * @param resultList
 * @param composite
 * @param colorlist
 * @return
 */
	public ChartComposite createBarChart(String[] algorithm, String para,
			HashMap<String, Vector> resultList, Composite composite,
			HashMap<String, RGB> colorlist){
		  double[][] data = new double[algorithm.length][10];
		  String[] rowkeys = algorithm;
		  Cluster cluster = null;
		  double value = 0;
		   for (int i = 0; i < algorithm.length; i++) {
			   Vector clusterlist = resultList.get(algorithm[i]);
               for(int j=0;j<clusterlist.size();j++){
            	   cluster = (Cluster)clusterlist.get(j);
            	   if(para.equals("Precision"))value = cluster.getPrecision();
            	   if(para.equals("Recall"))value = cluster.getRecall();
            	   if(para.equals("f-measure"))value = cluster.getMeasure();
            	   if(value<=0.1)
            		   data[i][0]++;
            	   else if(value<=0.2)
            		   data[i][1]++;
            	   else if(value<=0.3)
            		   data[i][2]++;
            	   else if(value<=0.4)
            		   data[i][3]++;
            	   else if(value<=0.5)
            		   data[i][4]++;
            	   else if(value<=0.6)
            		   data[i][5]++;
            	   else if(value<=0.7)
            		   data[i][6]++;
            	   else if(value<=0.8)
            		   data[i][7]++;
            	   else if(value<=0.9)
            		   data[i][8]++;
            	   else 
            		   data[i][9]++;
               }
			   
		   }
		String[] columnkeys = { "0-0.1", "0.1-0.2", "0.2-0.3", "0.3-0.4",
				"0.4-0.5", "0.5-0.6", "0.6-0.7", "0.7-0.8", "0.8-9", "0.9-1" };
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
				rowkeys, columnkeys, data);
		JFreeChart chart2 = ChartFactory.createBarChart3D(null, "Fig B",
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
		ChartComposite frame2 = new ChartComposite(composite, SWT.NONE, chart2);
		   return frame2;
	}
	
	class Controllor implements SelectionListener{
		Composite composite;
		String[] algorithm;
		HashMap<String, RGB> colorlist;
		Controllor(Composite composite,String[] algorithm,HashMap<String,RGB> colorlist){
			this.composite = composite;
			this.algorithm = algorithm;
			this.colorlist = colorlist;
		}
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
		}
		public void widgetSelected(SelectionEvent e) {
			String info = combo.getText();
			chartsResult = annotationProteinCharts.get(annotation);
			if("Main Composite".equals(info)){
				createComposite(composite,algorithm,colorlist);
				WindowShowAt = 0;
			}else if("P-value".equals(info)){
				calCulatePvalue(algorithm, chartsResult, composite, colorlist, pvalue+getFigA("lg(p-value)")+getFigB("-lg(p-value)"));
				WindowShowAt = 1;
			}else if("Precision".equals(info)){
				calCulatePreCisionandRecall(algorithm, "Precision", chartsResult, composite, colorlist, precision+getFigA("precision")+getFigB("precision"));
				WindowShowAt = 2;
			}else if("Recall".equals(info)){
				calCulatePreCisionandRecall(algorithm, "Recall", chartsResult, composite, colorlist, recall+getFigA("recall")+getFigB("recall"));
				WindowShowAt = 3;
			}else if("f-measure".equals(info)){
				calCulatePreCisionandRecall(algorithm, "f-measure", chartsResult, composite, colorlist, recall+getFigA("f-measure")+getFigB("f-measure"));
				WindowShowAt = 4;
			}
		}
	}
	
	/**
	 * 保存table信息到excel文件
	 * @param table
	 * @param para
	 */
	public void saveExcelTable(Table table,String para){
		FileDialog fd = new FileDialog(table.getShell(),SWT.SAVE);
		fd.setFilterExtensions(new String[]{"*.xls"});
		fd.setFilterNames(new String[]{"Excel.xls"});
		String filename = fd.open();
		if(filename==null||filename.equals(""))return;
		TableItem[] ti = table.getItems();
		try{
		  WritableWorkbook book=
			  Workbook.createWorkbook(new File(filename));
			  WritableSheet sheet=book.createSheet("第一页",0);
             
			  WritableFont font3=new WritableFont(WritableFont.createFont("楷体 _GB2312"),17,WritableFont.NO_BOLD );
			  WritableCellFormat format1=new WritableCellFormat(font3); 
			  sheet.mergeCells(0, 0, 15, 0);
			  jxl.write.Label label1 = new jxl.write.Label(0,0,para);
			  label1.setCellFormat(format1);
			  sheet.addCell(label1);
			  TableColumn[] tc = table.getColumns();
			  for(int i=0;i<tc.length;i++){
				  sheet.addCell(new jxl.write.Label(i,1,tc[i].getText()));
			  }
			  for(int i=0;i<ti.length;i++){
				  for(int j=0;j<tc.length;j++){
					  sheet.addCell(new jxl.write.Label(j,i+2,ti[i].getText(j)));
				  }
			  }
//			  jxl.write.Number number = new jxl.write.Number(1,0,789.123);
//			  sheet.addCell(number);
			  book.write();
			  book.close();
			  }catch(Exception e){
			         MessageDialog.openError(table.getShell(), "Error", e.toString());
			         return;
			  }
			 MessageDialog.openInformation(table.getShell(), "Success", "File Saved Successfully");
	}
	
	/**
	 * 读取其他  基因注释信息
	 */
	public void openOthersAnnotation(Composite composite,Text text){
		FileDialog fd = new FileDialog(composite.getShell(),SWT.OPEN);
		fd.setFilterExtensions(new String[]{"*.txt","*.*"});
		fd.setFilterNames(new String[]{"(文本文件).txt","所有文件(*.*)"});
		String filename = fd.open();
		if(filename==null||filename.equals(""))return;
		text.setText(filename);
		Scanner scanner = null;
		String s1,s2;
		HashMap<String,Set<String>> temp = new HashMap<String,Set<String>>();
		try{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
	    String str = br.readLine();
	    while(str!=null){
	    	str = str.toUpperCase();
	    	scanner = new Scanner(str);
	    	s1 = scanner.next();
	    	s2 = scanner.next();
	    	if(temp.get(s1)!=null)
	    		temp.get(s1).add(s2);
	    	else {
	    		Set<String> fun = new HashSet<String>();
	    		fun.add(s2);
	    		temp.put(s1, fun);
	    	}
	    	str = br.readLine();
	    }
	    br.close();
		}catch (Exception e){
			MessageDialog.openError(composite.getShell(), "Error", e.toString());
			temp.clear();
			return;
		}
		otherProteinFunction = temp;
		MessageDialog.openInformation(composite.getShell(), "Success", "File read Successfully!");
	}
	/**
	 * 如果之前导入的功能注释信息中 功能是以编号形式导入  则 需要在此导入对各个功能编号的注释
	 * @param composite
	 */
	public void openOtherFunctionAnnotation(Composite composite,Text text){
		FileDialog fd = new FileDialog(composite.getShell(),SWT.OPEN);
		fd.setFilterExtensions(new String[]{"*.txt","*.*"});
		fd.setFilterNames(new String[]{"(文本文件).txt","所有文件(*.*)"});
		String filename = fd.open();
		if(filename==null||filename.equals(""))return;
		text.setText(filename);
		Scanner scanner = null;
		String s1,s2;
		HashMap<String,String> temp = new HashMap<String,String>();
		try{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = br.readLine();
		while(str!=null){
			scanner = new Scanner(str);
			if(!scanner.hasNext()){
				str = br.readLine();
				continue;
			}
			s1 = scanner.next().toUpperCase();
			s2 = scanner.next();
			int index = str.indexOf(s2);
			s2 = str.substring(index, str.length());
			temp.put(s1, s2);
			str = br.readLine();
		}
		br.close();
		}catch (Exception e){
			e.printStackTrace();
			MessageDialog.openError(composite.getShell(), "Error", e.toString());
			temp.clear();
			return;
		}
		otherFunctionAnnotation = temp;
		MessageDialog.openInformation(composite.getShell(), "Success", "File read Successfully!");
	}

}
