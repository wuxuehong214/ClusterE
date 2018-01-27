package com.wuxuehong.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import jxl.read.biff.Formula;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.java.plugin.Plugin;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import clustere.dialogs.ProgressBarDialog;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.NewAlgorithm;

public class Comparision extends Plugin implements NewAlgorithm {

	
	private String comparison = "To evaluate the effectiveness of a algorithm for detecting protein complexes,we compare the predicted "
			+ "clusters produced by the algorithm with known protein complexes，The overlapping score"
			+ "OS(Pc,Kc) between a predicted cluster Pc and a known complex Kc is calculated by the following formula: OS (Pc,Kc)=i*i/a*b where i is the size of the intersection set of the "
			+ "predicted cluster and the known complex,  a is the size of the predicted cluster and b is the size of the known complex.";
	private String snsp = "Sensitivity and specificity are two important aspects to estimate the performance of algorithms for detecting protein complexes.Sensitivity is the fraction of "
             +"the true-positive predictions out of all the true predictions,defined by the following formula:Sn = TP/(TP+FN) where TP(true positive)is the number of the predicted  clusters matched by the known complexes with "
             +"OS(Pc,Kc)≥os(the default os value is 0.2,here you can also set the os value),and FN(false negative)is the number of the known complexes that are not matched by the predicted clusters.Specificity is the fraction of the true-positive "
             +"predictions out of all the positive predictions,defined by the following formula:Sp=TP/(TP+FP) where FP(false positive)equals the total number of the predicted clusters minus TP.According to the assumption "
             +",a predicted cluster and a known complex are considered to be matched if OS(Pc,Kc)≥os(os is the value you set).Generally,we use 0.2 as the matched overlapping threshold but here you can set the value you like.";

		@Override
		public int getStyle() {
			// TODO Auto-generated method stub
			return NewAlgorithm.Evaluation;
		}
		
		private Vector<Vector<String>> proteins ;
		private String getFilename; // the file name of the known proteins
		private  SashForm sashForm;
		private  Composite currentComposite;
		private int formula ;   //the way to calculate comparision
		private double ovalapScore;  //overlapping Score Threshold
		private int WindowShowAt;  //0 indicates the main window  1 indicates the charts window
		private HashMap<String,EvaluateVo> snspEvaluateList;
		
		private HashMap<Integer,HashMap<String,Vector<LinePoint>>> secondChartslist;
		private HashMap<Integer,HashMap<String,int[]>> firstChartslist;
		
		private HashMap<String, Vector<Node>[]> resultList;
		
		private HashMap<String,Vector<Node>[]>  unqiue;
		@Override
		public void variableInit() {
			// TODO Auto-generated method stub
		    WindowShowAt = 0;
		    formula = 0;
		    ovalapScore = 0.2;
		    getFilename = "";
//		    evaluate = null;//一个算法对应一个结果    图表结果
		    snspEvaluateList = new HashMap<String,EvaluateVo>();
		    secondChartslist = new HashMap<Integer,HashMap<String,Vector<LinePoint>>>();
		    firstChartslist = new HashMap<Integer,HashMap<String,int[]>>();
		    proteins = new Vector<Vector<String>>(); // 保存从文件读取的已知蛋白信息
		}
	/**
	 * WindowShowAt show the opened window default value is 0 ,it indicates that
	 * the window opened is the Integrated window value 1 p-value charts
	 */
	 @Override
		public void drawCharts(String[] algorithm,
				HashMap<String, Vector<Node>[]> resultList, Composite composite,
				HashMap<String, RGB> colorlist) {
		        unqiue = resultList;
		        if(algorithm.length==0){
		        	WindowShowAt = 0;
		        }
		     	if (WindowShowAt == 0){
				createComposite(composite, algorithm,colorlist);
				return ;
		      	}
		     	this.resultList = resultList;
		     	new NewThread(algorithm,composite,colorlist);
		}
	 
	 class NewThread implements Runnable{
		 String[] algorithm;
		 Composite composite;
		 HashMap<String, RGB> colorlist;
		 ProgressBarDialog pbd;
		 Thread t;
		 NewThread(String[] algorithm,Composite composite,HashMap<String, RGB> colorlist){
			 this.algorithm = algorithm;
			 this.colorlist = colorlist;
			 this.composite = composite;
			 t =new Thread(this);
			 t.start();
		 }
		@Override
		public void run() {
			// TODO Auto-generated method stub
			composite.getDisplay().asyncExec(new Runnable(){
				public void run() {
					pbd = new ProgressBarDialog(composite.getShell(), SWT.NONE,t);
					pbd.getLabel().setText("Calculating the charts.......");
				}
			});
	     	calPredictKnow(algorithm, resultList);    //second step    cal second graph
	     	calSensitySpecificity(ovalapScore, algorithm,resultList);
	     	composite.getDisplay().asyncExec(new Runnable(){
				public void run() {
					 if(WindowShowAt==1){
							HashMap<String, int[]> datalist = firstChartslist.get(formula);
							HashMap<String,Vector<LinePoint>>  alg_line = secondChartslist.get(formula);
							createChart(algorithm, "Comparision with the known Complexes", composite, colorlist, comparison,datalist,alg_line);
						}
						else if(WindowShowAt==2){
							createSnSpComposite(algorithm, "Sensitivity and Specificity", snspEvaluateList, composite, colorlist, snsp);
						}
					 pbd.getLabel().setText("complete!");
						pbd.dispose();
				}
			});
		}
	 }

	private Text text;
	private Button button4, button;

	/**
	 * create a control composite t
	 * 
	 * @param composite
	 */
	public void createComposite(final Composite composite, final String[] algorithm,
			final HashMap<String, RGB> colorlist) {
		if (currentComposite != null)
			currentComposite.dispose();
		if (sashForm != null)
			sashForm.dispose();
		WindowShowAt = 0;
		composite.setLayout(new FillLayout());
		currentComposite = new Composite(composite, SWT.NONE);
		currentComposite.setLayout(new FillLayout(SWT.VERTICAL));

		Group group1 = new Group(currentComposite, SWT.NONE);
		group1.setText("Load the known complexes");
		group1.setLayout(new GridLayout(2, true));
		Label label = new Label(group1, SWT.NONE);
		label.setText("The format of the input file should be like this:");
		final Link link = new Link(group1, SWT.NONE);
		link.setText("<a>example</a>");
		link.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Shell she = composite.getShell();
				new TipDialog(she, SWT.NONE).open();
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text = new Text(group1, SWT.BORDER);
		text.setText(getFilename);
		text.setLayoutData(gd);
		statueLabel = new Label(group1,SWT.NONE);
		statueLabel.setLayoutData(gd);
		statueLabel.setText("Total Known Complexes:"+proteins.size());
		button = new Button(group1, SWT.NONE);
		button.setText("Load File");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				openKnownFile(); 
			}
		});

		Group group2 = new Group(currentComposite, SWT.NONE);
		group2.setText("Use formula");
		group2.setLayout(new GridLayout());
		final Button button1 = new Button(group2, SWT.RADIO);
		button1.setText("i*i/a*b");
		if(formula==0)button1.setSelection(true);
		final Button button2 = new Button(group2, SWT.RADIO);
		button2.setText("i/min(a,b)");
		if(formula==1)button2.setSelection(true);
		Composite tempc = new Composite(currentComposite, SWT.NONE);
		tempc.setLayout(new GridLayout(2, true));
		Group group3 = new Group(tempc, SWT.NONE);
		group3.setLayout(new GridLayout());
		group3.setText("Begin Match");
		group3.setLayoutData(new GridData(GridData.FILL_BOTH));
		final Button button3 = new Button(group3, SWT.RADIO);
		button3.setText("Comparision with known complexes");
		button3.setSelection(true);
		final Button button6 = new Button(group3,SWT.RADIO);
		button6.setText("Sensitivity,Specificity");
		button4 = new Button(group3, SWT.NONE);
		button4.setText("Start");
		if(proteins.size()==0)button4.setEnabled(false);
		button4.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if(button1.getSelection()) {
					formula = 0;
				}
				else if(button2.getSelection()){
					formula = 1;
				}
				if(button6.getSelection()){
					WindowShowAt = 2;
					createSnSpComposite(algorithm, "Sensitivity adn Specificity", null, composite, colorlist, snsp);
				}else if(button3.getSelection()){
					WindowShowAt = 1;
				createChart(algorithm,"Comparision with the known Complexes",composite,colorlist,comparison,null,null);
				}
			}
		});
		Group group5 = new Group(tempc, SWT.NONE);
		group5.setText("Current Algorithms");
		group5.setLayoutData(new GridData(GridData.FILL_BOTH));
		group5.setLayout(new GridLayout());
		for (int i = 0; i < algorithm.length; i++) {
			new Label(group5, SWT.NONE).setText(algorithm[i]);
		}
		Group group4 = new Group(currentComposite, SWT.NONE);
		group4.setText("Description for the Comparision");
		group4.setLayout(new GridLayout());
		final Text text2 = new Text(group4, SWT.WRAP | SWT.V_SCROLL);
		text2.setText(comparison);
		text2.setLayoutData(new GridData(GridData.FILL_BOTH));
		button3.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				text2.setText(comparison);
			}
		});
		button6.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				text2.setText(snsp);
			}
		});
		composite.layout();
	}


	
	/**
	 * 计算 sensitivity   specificity
	 */
	public void calSensitySpecificity(double os,String[] algorithm,HashMap<String,Vector<Node>[]> resultList){
		for(int i=0;i<algorithm.length;i++){
			Vector<Node>[] v = resultList.get(algorithm[i]);
			int count = 0;
			int count1 = 0;
//			boolean T = false;
//			for(int j=0;j<proteins.size();j++){             //已知复合物中被标识的数量    count
//				                                            //算法识别复合物中被标识的数量 count1
//				Vector<String> complex = proteins.get(j);
//				T = false;
//				for(int k=0;k<v.length;k++){
//					Vector<Node> predit = v[k];
//					float com = os(complex,predit);
//					if(com>=os){
//						T = true;
//						count1++;
//					}
//				}
//				if(T)count++;                     
//			}
			for(int j=0;j<proteins.size();j++){
				Vector<String> complex = proteins.get(j);
				for(int k=0;k<v.length;k++){
					Vector<Node> predit = v[k];
					if(os(complex,predit)>=os)
						{
						count++;
						break;
						}
				}
			}
			for(int k=0;k<v.length;k++){
				Vector<Node> predit = v[k];
			     for(int j=0;j<proteins.size();j++){
				   Vector<String> complex = proteins.get(j);
					if(os(complex,predit)>=os)
						{
						count1++;
						break;
						}
				}
			}
			EvaluateVo ev = new EvaluateVo();
			ev.setTP(count1);            //算法识别的复合物中被标识的数量
			ev.setFP(v.length-count1);     //算法识别的复合物中未被标识的数量
			ev.setFN(proteins.size()-count);   //已知蛋白质复合物中没有被识别的数量
			ev.setSp((float)count1/(float)v.length);   //specificity
			ev.setSn((float)ev.getTP()/(float)(ev.getTP()+ev.getFN()));  //sensitivity
			ev.setFm(2*ev.getSn()*ev.getSp()/(ev.getSn()+ev.getSp()));
			snspEvaluateList.put(algorithm[i], ev);
		}
	}
	
	/**
	 * sensitivity  specificity   显示面板
	 */
	public void createSnSpComposite(final String[] algorithm, String evaluateName,
			HashMap<String,EvaluateVo> evaluate, final Composite composite,
			final HashMap<String, RGB> colorlist, String showInfo){
		if(currentComposite!=null)
			currentComposite.dispose();
		if (sashForm != null)
			sashForm.dispose();
		
		composite.setLayout(new FillLayout());
		sashForm = new SashForm(composite,SWT.VERTICAL);
		Group group1 = new Group(sashForm,SWT.NONE);
		group1.setText("Description for "+evaluateName );
		group1.setLayout(new FillLayout());
		Text text = new Text(group1,SWT.WRAP|SWT.V_SCROLL);
		text.setText(showInfo);
		Composite com = new Composite(sashForm,SWT.NONE);
		sashForm.setWeights(new int[]{2,3});
		com.setLayout(new GridLayout(2,true));
		Group group3 = new Group(com,SWT.NONE);
		group3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group3.setText("Navigation");
        group3.setLayout(new GridLayout(2,false));
        Button button1 = new Button(group3,SWT.NONE);
        button1.setText("Main Composite");
        button1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				createComposite(composite, algorithm,colorlist);
			}
        });
        Button button2 = new Button(group3,SWT.NONE);
        button2.setText("Comparision");
        button2.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent e) {
        		WindowShowAt = 1;
				HashMap<String,int[]> datalist = firstChartslist.get(formula);
				HashMap<String,Vector<LinePoint>> alg_line = secondChartslist.get(formula);
				createChart(algorithm,"Comparision with the known Complexes",composite,colorlist,comparison,datalist,alg_line);
        	}
        });
        Group group4 = new Group(com,SWT.NONE);
        group4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group4.setText("OverLapping Score Threshold");
        group4.setLayout(new GridLayout(2,true));
        Text textos = new Text(group4,SWT.BORDER);
        textos.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        textos.setText(""+ovalapScore+"");
        Button setbutton = new Button(group4,SWT.NONE);
        setbutton.setText("Set");
        setbutton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        setbutton.addSelectionListener(new Controllor(textos, algorithm,composite,colorlist));
        
		Group group2 = new Group(com,SWT.NONE);
		group2.setText("Specificity and Sensitivity");
		group2.setLayout(new FillLayout());
		Table table = new Table(group2,SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
        TableColumn tc = new TableColumn(table,SWT.LEFT);
        tc.setText("Algorithms");
        tc.setWidth(100);
        tc = new TableColumn(table,SWT.LEFT);
        tc.setText("True Positive");
        tc.setWidth(100);
        tc = new TableColumn(table,SWT.LEFT);
        tc.setText("False Positive");
        tc.setWidth(100);
        tc = new TableColumn(table,SWT.LEFT);
        tc.setText("False Negative");
        tc.setWidth(100);
        tc = new TableColumn(table,SWT.LEFT);
        tc.setText("Sensitivity");
        tc.setWidth(80);
        tc = new TableColumn(table,SWT.LEFT);
        tc.setText("Specificity");
        tc.setWidth(80);
        tc = new TableColumn(table,SWT.LEFT);
        tc.setText("F-measure");
        tc.setWidth(80);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan= 2;
        group2.setLayoutData(gd);
        if(evaluate==null){
        	composite.layout();
        	return;
        }
        for(int i=0;i<algorithm.length;i++){
        	EvaluateVo ev = evaluate.get(algorithm[i]);
        	new TableItem(table,SWT.LEFT).setText(new String[]{
        			algorithm[i],""+ev.getTP()+"",""+ev.getFP()+"",""+ev.getFN()+"",""+ev.getSn()+"",
        			""+ev.getSp()+"",""+ev.getFm()+"",
        	});
        }
        composite.layout();
	}
	/**
	 * calculate the os(complex,predict) between the predicted complex predict
	 * and the known complex complex ranges from 0.1-1.0
	 */
	public void calculComparision(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList) {
		HashMap<String,int[]> temp;
		if(firstChartslist.get(formula)!=null)temp = firstChartslist.get(formula);
		else  {
			temp = new HashMap<String,int[]>();
			firstChartslist.put(formula,temp);
		}
		for (int i = 0; i < algorithm.length; i++) {
			if(temp.get(algorithm[i])!=null)continue;
			Vector[] v = resultList.get(algorithm[i]);
			int[] data = new int[11];
			data[0] = proteins.size();
			for (int j = 0; j < proteins.size(); j++) {
				float max = -1;
				Vector<String> complex = proteins.get(j);
				for (int k = 0; k < v.length; k++) {
					Vector predict = v[k];
					float com = os(complex, predict);
					if (com > max)
						max = com;
				}
				if (max >= 1) {
					data[1]++;
					data[2]++;
					data[3]++;
					data[4]++;
					data[5]++;
					data[6]++;
					data[7]++;
					data[8]++;
					data[9]++;
					data[10]++;
				} else if (max >= 0.9) {
					data[1]++;
					data[2]++;
					data[3]++;
					data[4]++;
					data[5]++;
					data[6]++;
					data[7]++;
					data[8]++;
					data[9]++;
				} else if (max >= 0.8) {
					data[1]++;
					data[2]++;
					data[3]++;
					data[4]++;
					data[5]++;
					data[6]++;
					data[7]++;
					data[8]++;
				} else if (max >= 0.7) {
					data[1]++;
					data[2]++;
					data[3]++;
					data[4]++;
					data[5]++;
					data[6]++;
					data[7]++;
				} else if (max >= 0.6) {
					data[1]++;
					data[2]++;
					data[3]++;
					data[4]++;
					data[5]++;
					data[6]++;
				} else if (max >= 0.5) {
					data[1]++;
					data[2]++;
					data[3]++;
					data[4]++;
					data[5]++;
				} else if (max >= 0.4) {
					data[1]++;
					data[2]++;
					data[3]++;
					data[4]++;
				} else if (max >= 0.3) {
					data[1]++;
					data[2]++;
					data[3]++;
				} else if (max >= 0.2) {
					data[1]++;
					data[2]++;
				} else if (max >= 0.1) {
					data[1]++;
				}
			}
			temp.put(algorithm[i], data);
		}
	}

	/**
	 * calculate the os between the predicted complex predict and the known
	 * complex complex
	 * 
	 * @param complex
	 * @param predict
	 * @return
	 */
	public float os(Vector<String> complex, Vector predict) {
		int i=0, a, b;
		a = complex.size();
		b = predict.size();
		Set<String> container = new HashSet<String>(complex);
		int c = container.size();
		for(int j=0;j<b;j++){
			Node node = (Node)predict.get(j);
			container.add(node.getNodeID());
		}
		int d = container.size();
		i = b-(d-c);
		float result = 0;
		if (formula == 0)
			result = (float) i * (float) i / ((float) a * (float) b);
		if (formula == 1)
			result = a > b ? (float) i / (float) b : (float) i / (float) a;
		return result;
	}
	
	
   /**
	 * calculate the distribution of the kunow complex and predicted complex with the different os value
	 * @param os
	 */
	public void calPredictKnow(String[] algorithm,HashMap<String, Vector<Node>[]> resultList){
		HashMap<String,Vector<LinePoint>> temp;
		HashMap<String,int[]> temp1;
		if(secondChartslist.get(formula)==null){
			temp = new HashMap<String,Vector<LinePoint>>();
			temp1 = new HashMap<String,int[]>();
			firstChartslist.put(formula, temp1);
			secondChartslist.put(formula, temp);
		}else {
			temp = secondChartslist.get(formula);
			temp1 = firstChartslist.get(formula);
		}
		for(int k=0;k<algorithm.length;k++){
			if(temp.get(algorithm[k])!=null)continue;
			Vector<Node>[] predict = resultList.get(algorithm[k]);
			Vector<LinePoint> lineList = new Vector<LinePoint>();
			int[] data = new int[11];
			data[0] = proteins.size();
			lineList.add(new LinePoint(predict.length,proteins.size())); //OS = 0.0
			for(int i=1;i<11;i++){
				float oss = i/(float)10;
				int predict_size = 0;
				int known_size = 0;
				for(int a=0;a<proteins.size();a++){
					Vector<String> complex = proteins.get(a);
					for(int b=0;b<predict.length;b++){
						if(os(complex,predict[b])>=oss){
							known_size++;
							break;
						}
					}
				}
				for(int a=0;a<predict.length;a++){
					for(int b=0;b<proteins.size();b++){
						Vector<String> complex = proteins.get(b);
						if(os(complex,predict[a])>=oss){
							predict_size++;
							break;
						}
					}
				}
				data[i] = known_size;
				lineList.add(new LinePoint(predict_size,known_size));
			}
		temp1.put(algorithm[k], data);
		temp.put(algorithm[k], lineList);
		}
	}
	

	private Button buttonos;    //calculate with the value of ovalapScore
	private Text textos;        //input the value ovalapScore
	private Table table;        //show the result list
	private Combo tablecombo;   // choose to show the content of the table
	private Label statueLabel ; //show the basic info of the known complexes
	/**
	 * create the charts
	 * 
	 * @param algorithm
	 * @param evaluateName
	 * @param resultList
	 * @param composite
	 * @param colorlist
	 */
	public void createChart(final String[] algorithm, String evaluateName,
			 final Composite composite,
			final HashMap<String, RGB> colorlist, String showInfo,HashMap<String,int[]>datalist,HashMap<String,Vector<LinePoint>> alg_line) {
		if (currentComposite != null)
			currentComposite.dispose();
		if (sashForm != null)
			sashForm.dispose();
		composite.setLayout(new FillLayout());
		sashForm = new SashForm(composite, SWT.VERTICAL);
		SashForm comp1 = new SashForm(sashForm, SWT.VERTICAL);
		Composite desComp = new Composite(comp1, SWT.NONE);
		desComp.setLayout(new GridLayout(5, true));
		GridData gdd = new GridData(GridData.FILL_BOTH);
		gdd.horizontalSpan = 4;
		Group group1 = new Group(desComp, SWT.NONE);
		group1.setText("Description for" + evaluateName);
		group1.setLayoutData(gdd);
		group1.setLayout(new FillLayout());
		Text text = new Text(group1, SWT.WRAP | SWT.V_SCROLL);
		text.setText(showInfo);
		Group group2 = new Group(desComp, SWT.NONE);
		group2.setText("Navigation");
		group2.setLayout(new GridLayout());
		final Combo combo = new Combo(group2, SWT.READ_ONLY | SWT.DROP_DOWN);
		combo.add("Main Composite");
		combo.add("Comparision");
		combo.add("SnSp");
		combo.setText("Comparision");
		combo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if("Main Composite".equals(combo.getText())){
					WindowShowAt = 0;
					createComposite(composite,algorithm,colorlist);
				}
				else if("SnSp".equals(combo.getText())){
					WindowShowAt = 2;
					createSnSpComposite(algorithm, "Sensitivity adn Specificity", snspEvaluateList, composite, colorlist, snsp);
				}
			}
		});
		GridData gdata = new GridData(GridData.FILL_BOTH);
		combo.setLayoutData(gdata);
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite comp = new Composite(comp1, SWT.BORDER);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp1.setWeights(new int[] { 2, 5 });
		Composite tablecom = new Composite(sashForm, SWT.NONE);
		sashForm.setWeights(new int[] { 3, 1 });
		createTable(tablecom, algorithm,colorlist,datalist,alg_line);
		if(algorithm.length==0||datalist==null||alg_line==null){
			composite.layout();
			return;
		}
		/************************ draw linechart ***********************************/
		comp.setLayout(new GridLayout(2,true));
		ChartComposite frame1 = createLinechart(algorithm, evaluateName, comp, colorlist,datalist);
		frame1.setLayoutData(new GridData(GridData.FILL_BOTH));
		/************************ draw linechart2 *************************************/
		ChartComposite frame2 = createLinechart2(algorithm, evaluateName, comp, colorlist,alg_line);
		frame2.setLayoutData(new GridData(GridData.FILL_BOTH));
		/**************************** show table ************************************/
		composite.layout();
	}

	/**
	 * create  second lineChart
	 * @param algorithm
	 * @param para
	 * @param composite
	 * @param colorlist
	 * @return
	 */
	public ChartComposite createLinechart2(String[] algorithm, String para,Composite composite,
			HashMap<String, RGB> colorlist,HashMap<String,Vector<LinePoint>> alg_line) {
		XYSeries series = null;
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		for (int i = 0; i < algorithm.length; i++) {
			Vector<LinePoint> linelist = alg_line.get(algorithm[i]);
			series = new XYSeries(algorithm[i]);
			for (int j = 0; j < linelist.size(); j++) {
				LinePoint lp = linelist.get(j);
				series.add(lp.getPredit_size(), lp.getKnown_size());
			}
			dataset1.addSeries(series);
		}
		JFreeChart chart1 = ChartFactory.createXYLineChart(null, "Predicted Complexes",
				"Known Complexes", dataset1, PlotOrientation.VERTICAL,
				true, true, true);
		XYPlot cate1 = (XYPlot) chart1.getPlot();
		XYTextAnnotation xa = null;
		float value = 0;
		for (int i = 0; i < algorithm.length; i++) {
			Vector<LinePoint> linelist = alg_line.get(algorithm[i]);
			for (int j = 0; j < linelist.size(); j++) {
				LinePoint lp = linelist.get(j);
				value = (float)(j)/(float)10;
				xa = new XYTextAnnotation("os="+value+"", lp.getPredit_size(), lp.getKnown_size());
				cate1.addAnnotation(xa);
			}
		}
		XYLineAndShapeRenderer lineRender = (XYLineAndShapeRenderer) cate1
				.getRenderer();
		lineRender.setShapesVisible(true);
		for (int i = 0; i < algorithm.length; i++) {
			RGB rgb = colorlist.get(algorithm[i]);
			lineRender.setSeriesPaint(i, new java.awt.Color(rgb.red, rgb.green,
					rgb.blue));
		}
		ChartComposite frame1 = new ChartComposite(composite, SWT.NONE, chart1);
		return frame1;
	}
	
	/**
	 * first linechart
	 * @param algorithm
	 * @param para
	 * @param composite
	 * @param colorlist
	 * @return
	 */
	public ChartComposite createLinechart(String[] algorithm, String para,Composite composite,
			HashMap<String, RGB> colorlist,HashMap<String,int[]> datalist) {
		XYSeries series = null;
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		System.out.println("draw chart1!!!!!!!!!!!!!!!!!!!");
		for (int i = 0; i < algorithm.length; i++) {
			series = new XYSeries(algorithm[i]);
			int[] data = datalist.get(algorithm[i]);
			float f = 0;
			for (int j = 0; j < 11; j++) {
				series.add(f, data[j]);
				f += 0.1;
			}
			dataset1.addSeries(series);
		}
		JFreeChart chart1 = ChartFactory.createXYLineChart(null, "Overlapping Score Threshold",
				"Number of Matched Known Complexes", dataset1, PlotOrientation.VERTICAL,
				true, true, true);
		XYPlot cate1 = (XYPlot) chart1.getPlot();
		XYLineAndShapeRenderer lineRender = (XYLineAndShapeRenderer) cate1
				.getRenderer();
		for (int i = 0; i < algorithm.length; i++) {
			RGB rgb = colorlist.get(algorithm[i]);
			lineRender.setSeriesPaint(i, new java.awt.Color(rgb.red, rgb.green,
					rgb.blue));
		}
		ChartComposite frame1 = new ChartComposite(composite, SWT.NONE, chart1);
		return frame1;
	}
	
	/**
	 * create  table composite
	 * @param composite
	 * @param algorithm
	 */
	public void createTable(Composite composite,String[] algorithm,HashMap<String,RGB> colorlist,final HashMap<String,int[]>datalist,final HashMap<String,Vector<LinePoint>> alg_line){
		composite.setLayout(new GridLayout(5,true));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 4;
		table = new Table(composite,SWT.FULL_SELECTION);
		table.setLayoutData(gd);
		Group group = new Group(composite,SWT.NONE);
		group.setText("Table Control");
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setLayout(new GridLayout());
		tablecombo = new Combo(group,SWT.READ_ONLY|SWT.DROP_DOWN);
		tablecombo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String str = tablecombo.getText().trim();
		    tableUpdate(str, datalist, alg_line);
// else 
//				 tableUpdate(str, datalist, varityOSresult);
			}
		});
//		Button button = new Button(group,SWT.NONE);
//		button.setText("Save Table");
		TableColumn tc = new TableColumn(table,SWT.LEFT);
		tc.setText("Overlap Score");
		tc.setWidth(100);
		tc = new TableColumn(table,SWT.LEFT);
		tc.setText("Mathced Known Complexes");
		tc.setWidth(160);
		tc = new TableColumn(table,SWT.LEFT);
		tc.setText("Predicted Complexes");
		tc.setWidth(120);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		if(datalist==null||alg_line==null){
			composite.layout();
			return;
		}
		if(algorithm.length==1){
			tablecombo.add(algorithm[0]);
			tablecombo.setText(algorithm[0]);
			int[] data = datalist.get(algorithm[0]);
			Vector<LinePoint> linelist = alg_line.get(algorithm[0]);
			float value = 0;
			for(int i=0;i<linelist.size();i++){
				value = (float)i/(float)10;
				LinePoint lp = linelist.get(i);
				new TableItem(table,SWT.LEFT).setText(new String[]{""+value+"",""+lp.getKnown_size()+"",""+lp.getPredit_size()+""});
			}
			return;
		}
		for(int i=0;i<algorithm.length;i++){
			tablecombo.add(algorithm[i]);
//			int[]  data = datalist.get(algorithm[i]);
//			Vector<LinePoint> linelist = alg_line.get(algorithm[i]);
//			for(int j=0;j<data.length;j++){
//				new TableItem(table,SWT.LEFT).setText(new String[]{"0."+j,""+data[j]+""});
//			}
//			for(int j=0;j<linelist.size();j++){
//				LinePoint lp = linelist.get(j);
//				new TableItem(table,SWT.LEFT).setText(new String[]{""+ovalapScore+"","",""+lp.getKnown_size()+"",""+lp.getPredit_size()+""});
//			}
//			
		}
	}
	
	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector[] getClusters(String[] para) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Comparision with the known complexes and show the Sensitivity ,Specificity";
	}

	@Override
	public String getEvaluateNames() {
		// TODO Auto-generated method stub
		return "Comparison with the known complexes" ;
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

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * read the overlappint score and calculate the charts
	 */
//	public void getResultWithOS(String[] algorithm,HashMap<String,Vector<Node>[]> resultList,
//			HashMap<String,RGB> colorlist,HashMap<String,int[]> datalist){
//		  String str = textos.getText();
//		  double os = 0;
//		 try{
//		 os = Double.parseDouble(str);
//		 if(os>1||os<0)throw new NumberFormatException();
//		 }catch(NumberFormatException ee){
//			 MessageBox box = new MessageBox(new Shell(),SWT.ERROR);
//			 box.setText("error");
//			 box.setMessage("NumberFormat Exception:it should be 0-1");
//			 box.open();
////			 ovalapScore = os;
//			 return;
//		 }
//		 ovalapScore = os;
////		 HashMap<String,Vector<LinePoint>> temp = new HashMap<String,Vector<LinePoint>>();
//		  calPredictKnow(ovalapScore, algorithm, resultList);
//		   if(algorithm.length==1){
//		   int[]  data = datalist.get(algorithm[0]);
//		   Vector<LinePoint> linelist = secondChartslist.get(formula).get(algorithm[0]);
//		    table.removeAll();
//			for(int j=0;j<data.length;j++){
//				new TableItem(table,SWT.LEFT).setText(new String[]{"0."+j,""+data[j]+""});
//			}
//			for(int j=0;j<linelist.size();j++){
//				LinePoint lp = linelist.get(j);
//				new TableItem(table,SWT.LEFT).setText(new String[]{""+os+"","",""+lp.getKnown_size()+"",""+lp.getPredit_size()+""});
//			}
//			table.layout();
//		   }
//	}
	
	  class Controllor extends SelectionAdapter{
		  Text text;
		  String[] algorithm;
		  Composite composite;
		  HashMap<String,RGB> colorlist;
		  Controllor(Text  text,String[] algorithm,Composite composite,HashMap<String,RGB> colorlist){
			  this.text = text;
			  this.algorithm = algorithm;
			  this.colorlist = colorlist;
			  this.composite = composite;
		  }
		public void widgetSelected(SelectionEvent e) {
			  double os = 0;
				 try{
				 os = Double.parseDouble(text.getText());
				 if(os>1||os<0)throw new NumberFormatException();
				 }catch(NumberFormatException ee){
					 MessageBox box = new MessageBox(new Shell(),SWT.ERROR);
					 box.setText("error");
					 box.setMessage("NumberFormat Exception:it should be 0-1");
					 box.open();
					 return;
				 }
				 ovalapScore = os;
				 calSensitySpecificity(ovalapScore, algorithm,resultList);
				 createSnSpComposite(algorithm, "Sensitivity and Specificity", snspEvaluateList, composite, colorlist, snsp);
		}
	  }
	
	
	/**
	 * read the file with known complexes 
	 */
	public void openKnownFile(){
		FileDialog fd = new FileDialog(new Shell(), SWT.OPEN);
		fd.setText("open known complexes");
		fd.setFilterExtensions(new String[] { "*.*", "*.txt" });
		fd.setFilterNames(new String[] { "所有文件*.*", "文本*.txt" });
		getFilename = fd.open();
		if(getFilename==null||getFilename=="")return;
		text.setText(getFilename);
		String str = null;
		Vector<String> vector = null;
		BufferedReader br = null;
		Scanner scanner = null;
		proteins.clear();
		try {
			br = new BufferedReader(new FileReader(
					new File(getFilename)));
			str = br.readLine();
			while (str != null) {
				str = str.trim();
				scanner = new Scanner(str);
				str = scanner.next();
				if (str.startsWith("Complex")
						|| str.startsWith("complex")||str.startsWith("Cluster")) {
					vector = new Vector<String>();
					proteins.add(vector);
				} else{
					if(vector==null)throw new IOException();
				    str = str.toUpperCase();
					vector.add(str);
				}
				str = br.readLine();
			}
			button4.setEnabled(true);
			statueLabel.setText("Total Known Complexes:"+proteins.size());
			br.close();
			MessageBox box = new MessageBox(new Shell(), SWT.YES);
			box.setText("Tip");
			box.setMessage("File read successfully!");
			box.open();
		} catch (IOException ee) {
			MessageBox box = new MessageBox(new Shell(), SWT.ERROR);
			box.setText("Error");
			box.setMessage(ee.toString());
			box.open();
			proteins.removeAll(proteins);
			button4.setEnabled(false);
			return;
		} finally {
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * update the data of the table
	 * @param name
	 */
	public void tableUpdate(String name,HashMap<String,int[]> datalist,HashMap<String,Vector<LinePoint>> alg_line){
		int[]  data = datalist.get(name);
		Vector<LinePoint> linelist = alg_line.get(name);
		table.removeAll();
		float value = 0;
		for(int i=0;i<linelist.size();i++){
			value = (float)i/(float)10;
			LinePoint lp = linelist.get(i);
			new TableItem(table,SWT.LEFT).setText(new String[]{""+value+"",""+lp.getKnown_size()+"",""+lp.getPredit_size()+""});
		}
		table.layout();
	}

}
