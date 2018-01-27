package com.wuxuehong.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class CommonInfo extends Plugin implements NewAlgorithm{

	
	private  int WindowShowAt;
	private HashMap<String,Vector> ChartsInfo = null;
	
	private final String sizeDistributon = "The size distribution is to describe the basic information of the cluster results"+
	" by showing the charts with the nodes size and the cluster number distributed on it.\n";
	private final String density ="Density is to describe how closely the nodes in the cluster interact with each other. Given a cluster consisting of n nodes and m edges, its density is 2m/n(n-1).\n";
	
	
	@Override
	public void variableInit() {
		// TODO Auto-generated method stub
		WindowShowAt = 0;
		ChartsInfo = new HashMap<String,Vector>();
	}
	
    public void calCulateChartsInfo(String[] algorithm,HashMap<String,Vector<Node>[]> resultList){
    	for(int i=0;i<algorithm.length;i++){
    		Vector<Node>[] v = resultList.get(algorithm[i]);
    		if(ChartsInfo.get(algorithm[i])==null)
    			ChartsInfo.put(algorithm[i], calCulateEachCluster(v));
    	}
    }
    
    public Vector calCulateEachCluster(Vector<Node>[] v){
    	List<Float> vv = new ArrayList<Float>();
		Vector<Cluster> clusters = new Vector<Cluster>();
		Cluster cluster = null;
		int edgecount = 0;
		float density = 0;
		int size = 0;
		int[] a = new int[10];
		for (int i = 0; i < v.length; i++) {
			Vector v1 = v[i];
			edgecount = 0;
			density = 0;
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
		cluster = new Cluster();
		cluster.setClusterID("Cluster"+i);
		cluster.setNodeCount(size);
		cluster.setEdgeCount(edgecount);
		cluster.setDensity(density);
		cluster.setAvgDegree(2*(float)edgecount/(float)size);
		clusters.add(cluster);
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
		vector.add(linechart);                   //密度曲线图            0
		vector.add(barchart);                   //密度柱状图               1
		
	/**********************************基本信息柱状图 ，曲线图计算*********************************************************/
		BarChart barchart1 = new BarChart();
		Vector<LinePoint> pointlist1 = new Vector<LinePoint>();
		LinePoint linepoint1 = null;
		HashMap<Integer,Clum> hm  = new HashMap<Integer,Clum>();
		Vector vector1 = null;
		Clum clum = null;
		int value = 0;
		for(int i=0;i<v.length;i++){
			vector1 = v[i];
			value = vector1.size();
			linepoint1 = new LinePoint(i+1,value);
			pointlist1.add(linepoint1);
			if(hm.get(value)!=null) {
				  int a1 = hm.get(value).value;
			      hm.get(value).value = a1+1;
			}
			else {
				clum = new Clum(value,1);
				hm.put(value, clum);
			}
		}
		    List<Clum> list = new ArrayList<Clum>();
	        Collection<Clum> c = hm.values();
	        Iterator it = c.iterator();
	        while(it.hasNext()){
	        	list.add((Clum)it.next());
	        }
		Collections.sort(list);
		Collections.reverse(list);
		
		double[][] data1 = new double[1][list.size()];
		String[] columns = new String[list.size()];
		for(int i=0;i<list.size();i++){
			Clum cc = list.get(i);
			data1[0][i] = cc.value;            //cc.value  簇的个数
			columns[i] = String.valueOf(cc.size);     //簇的大小
		}
		barchart1.setData(data1);
		barchart1.setColumkeys(columns);
		Collections.sort(pointlist1);
		LineChart linechart1 = new LineChart(pointlist1);
		vector.add(linechart1);                     //基本信息曲线图         2
		vector.add(barchart1);                     //基本信息柱状图            3
		
		vector.add(clusters);                        //the table info  for density          4
		vector.add(list);                            //table info for size distribution     5
		return vector;
    }

	
	private String density_figa = "Fig A shows the dentsity of each cluster";
	private String density_figb = "Fig B shows the number distribution of the clusters in different interval of the density";
	private String size_figa = "Fig A shows the number of the nodes each cluster contains";
	private String size_figb = "Fig B shows the number of clusters in different scope of the cluster";
	private Composite mainComposite;
	private Composite sizeComposite;
	private Composite densityComposite;
	private StackLayout stackLayout;
	
	private Composite sizeChartComposite;
	private Composite densityChartComposite;
	@Override
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist){
		calCulateChartsInfo(algorithm, resultList);
	     if(composite.getChildren().length==0){
	      createMainComposite(algorithm,composite);
	      createSizeComposite(composite);
	      createDensityComposite(composite);
	      WindowShowAt = 0;
	     }
	      if(WindowShowAt == 0){
	    	  stackLayout.topControl = mainComposite;
	    	  update(algorithm);     //
	    	  composite.layout();
	      }else if(WindowShowAt == 1){
	    	  stackLayout.topControl = densityComposite;
	    	  update(algorithm);
	    	  updateCharts(densityChartComposite, "Density",algorithm,colorlist);
	    	  densityChartComposite.layout();
	      }else if(WindowShowAt == 2){
	    	  update(algorithm);
	    	  stackLayout.topControl = sizeComposite;
	    	  updateCharts(sizeChartComposite, "Size",algorithm,colorlist);
	    	  sizeChartComposite.layout();
	      }
	}
	
	private Group group11;
	public void createMainComposite(String[] algorithm,final Composite composite){
		stackLayout = new StackLayout();
	    composite.setLayout(stackLayout);
	    mainComposite = new Composite(composite,SWT.BORDER);
	    
	    mainComposite.setLayout(new FillLayout(SWT.VERTICAL));
		Composite twogroup = new Composite(mainComposite,SWT.BORDER);
		twogroup.setLayout(new GridLayout(2,true));
		GridData tgd = new GridData(GridData.FILL_BOTH);
		Group group1 = new Group(twogroup,SWT.NONE);
		group1.setLayoutData(tgd);
		group1.setLayout(new GridLayout());
		group1.setText("Basic Information");
		final Button button1 = new Button(group1,SWT.RADIO);
		button1.setText("Density");
		button1.setSelection(true);
		final Button button2 = new Button(group1,SWT.RADIO);
		button2.setText("Size Distribution");
		Button  showChart = new Button(group1,SWT.NONE);
		showChart.setText("Show Chart");
		showChart.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
			        if(button1.getSelection()){
			        	stackLayout.topControl = densityComposite;
			        	WindowShowAt = 1;
			        	comboDensity.setText("Density");
			        	composite.layout();
//			        	claculateDistribution(algorithm, "Density", resultList, composite, colorlist, density);
			        }else if(button2.getSelection()){
//			        	claculateDistribution(algorithm, "Size", resultList, composite, colorlist, sizeDistributon);
			        	stackLayout.topControl = sizeComposite;
			        	comboSize.setText("Size Distribution");
			        	WindowShowAt = 2;
			        	composite.layout();
			        }
			}
		});
		group11 = new Group(twogroup,SWT.NONE);
		group11.setLayoutData(tgd);
		group11.setLayout(new GridLayout());
		group11.setText("Current Algorihtms");
		for(int i=0;i<algorithm.length;i++){
			Label l = new Label(group11,SWT.NONE);
			l.setText(algorithm[i]);
		}
		Group group2 = new Group(mainComposite,SWT.NONE);
		group2.setText("Algorithm Description");
		group2.setLayout(new FillLayout());
		final Text text = new Text(group2,SWT.WRAP|SWT.V_SCROLL|SWT.H_SCROLL);
		text.append(density);
		button1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				text.append(density);
			}
		});
		button2.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				 text.append(sizeDistributon);
			}
		});
	}
	
	public void update(String[] algorithm){
		int l1 = group11.getChildren().length;
		for(int i=0;i<l1;i++)
			{
			group11.getChildren()[0].dispose();
			}
		for(int i=0;i<algorithm.length;i++){
			Label l = new Label(group11,SWT.NONE);
			l.setText(algorithm[i]);
			
		}
		group11.layout();
	}
	
	private Combo comboSize;
	public void createSizeComposite(Composite composite){
		sizeComposite = new Composite(composite,SWT.BORDER);
		sizeComposite.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(sizeComposite, SWT.VERTICAL);
		Composite desComp = new Composite(sashForm,SWT.BORDER);
		desComp.setLayout(new GridLayout(5,true));
		GridData gdd = new GridData(GridData.FILL_BOTH);
		gdd.horizontalSpan = 4;
		Group group1 = new Group(desComp,SWT.NONE);
		group1.setText("Description for Size Distribution");
		group1.setLayoutData(gdd);
		group1.setLayout(new FillLayout());
		Text text = new Text(group1,SWT.WRAP|SWT.V_SCROLL);
		text.setText(sizeDistributon+size_figa+"\n"+size_figb);
		Group group2 = new Group(desComp,SWT.NONE);
		group2.setText("Navigation");
		group2.setLayout(new FillLayout());
		comboSize = new Combo(group2,SWT.READ_ONLY|SWT.DROP_DOWN);
		comboSize.add("Main Composite");
		comboSize.add("Density");
		comboSize.add("Size Distribution");
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		comboSize.addSelectionListener(new Controllor(comboSize,composite));
		
		sizeChartComposite = new Composite(sashForm,SWT.BORDER);
		sashForm.setWeights(new int[]{1,4});
	}
	
	private Combo comboDensity;
	public void createDensityComposite(Composite composite){
		densityComposite = new Composite(composite,SWT.BORDER);
		densityComposite.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(densityComposite, SWT.VERTICAL);
		Composite desComp = new Composite(sashForm,SWT.BORDER);
		desComp.setLayout(new GridLayout(5,true));
		GridData gdd = new GridData(GridData.FILL_BOTH);
		gdd.horizontalSpan = 4;
		Group group1 = new Group(desComp,SWT.NONE);
		group1.setText("Description for Density Distribution");
		group1.setLayoutData(gdd);
		group1.setLayout(new FillLayout());
		Text text = new Text(group1,SWT.WRAP|SWT.V_SCROLL);
		text.setText(density+density_figa+"\n"+density_figb);
		Group group2 = new Group(desComp,SWT.NONE);
		group2.setText("Navigation");
		group2.setLayout(new FillLayout());
		comboDensity = new Combo(group2,SWT.READ_ONLY|SWT.DROP_DOWN);
		comboDensity.add("Main Composite");
		comboDensity.add("Size Distribution");
		comboDensity.add("Density");
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		comboDensity.addSelectionListener(new Controllor(comboDensity,composite));
		
		densityChartComposite = new Composite(sashForm,SWT.BORDER);
		sashForm.setWeights(new int[]{1,4});
	}
	
private ChartComposite barchartComposite;

	public void updateCharts(Composite showComposite,String para,String[] algorithm,HashMap<String, RGB> colorlist){
		for(int i=0;i<showComposite.getChildren().length;i++)
			showComposite.getChildren()[i].dispose();
		showComposite.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(showComposite,SWT.VERTICAL);
		Composite chartsComposite = new Composite(sashForm,SWT.BORDER);
		chartsComposite.setLayout(new GridLayout(2,true));
		
		ChartComposite linechart = createLineChart(chartsComposite,para,algorithm,colorlist);
		linechart.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		barchartComposite = createBarChart(chartsComposite,para,algorithm[0],algorithm,colorlist);
		barchartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite tableComposite = new Composite(sashForm,SWT.BORDER);
		createTable(tableComposite, para,chartsComposite,algorithm,colorlist);
		
		sashForm.setWeights(new int[]{3,1});
	}
	/**
	 * calculate the charts of size distributon 
	 */
	public void claculateDistribution(String[] algorithm, String para,
			HashMap<String, Vector> resultList, Composite composite,
			HashMap<String, RGB> colorlist,String showInfo){
		composite.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
		WindowShowAt = 1;
		SashForm comp1 = new SashForm(sashForm, SWT.VERTICAL);
		Composite desComp = new Composite(comp1,SWT.NONE);
		desComp.setLayout(new GridLayout(5,true));
		GridData gdd = new GridData(GridData.FILL_BOTH);
		gdd.horizontalSpan = 4;
		Group group1 = new Group(desComp,SWT.NONE);
		group1.setText("Description for "+para+" distribution");
		group1.setLayoutData(gdd);
		group1.setLayout(new FillLayout());
		Text text = new Text(group1,SWT.WRAP|SWT.V_SCROLL);
		text.setText(showInfo);
		Group group2 = new Group(desComp,SWT.NONE);
		group2.setText("Choose");
		group2.setLayout(new FillLayout());
		Combo combo = new Combo(group2,SWT.READ_ONLY|SWT.DROP_DOWN);
		combo.add("Back");
		if(para.equals("Size")){
			combo.add("Density");
			WindowShowAt = 2;
		}
		if(para.equals("Density")){
			WindowShowAt = 1;
			combo.add("Size");
		}
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		combo.addSelectionListener(new Controllor(combo,composite));
		
		Composite comp = new Composite(comp1,SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp1.setWeights(new int[]{1,4});
		comp.setLayout(new GridLayout(2,true));
		GridData grid = new GridData(GridData.FILL_BOTH);
		
		Table table = new Table(sashForm, SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		sashForm.setWeights(new int[] { 3, 1 });
		/*********************曲线图*****************************/
		//ChartComposite frame1  = createLineChart(algorithm, para, resultList, comp, colorlist);
		//frame1.setLayoutData(grid);
		/*********************柱状图*****************************/
//		ChartComposite frame2 = createBarChart(algorithm, para, resultList, comp, colorlist);
//		frame2.setLayoutData(grid);
		/************************表格***************************/
		//create(table, para);
		composite.layout();
	}
	
	/**
	 * create table
	 */
	boolean a=true;
	public void createTable(final Composite composite,String para,final Composite showComposite,final String[] algorithm,final HashMap<String, RGB> colorlist){
		composite.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(composite,SWT.HORIZONTAL);
		final TableViewer tv = new TableViewer(sashForm,SWT.FULL_SELECTION);
		final Table table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		Group group = new Group(sashForm,SWT.NONE);
		sashForm.setWeights(new int[]{4,1});
		group.setLayout(new GridLayout());
		group.setText("Show Table");
		final Combo combo = new Combo(group,SWT.DROP_DOWN|SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_BOTH));
		Button button = new Button(group,SWT.NONE);
		button.setText("Save table");
		for(int i=0;i<algorithm.length;i++){
		     combo.add(algorithm[i]);
		     combo.setText(algorithm[0]);
		}
		if(para.equals("Density")){
			button.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					SaveXls(composite, table, combo.getText(), "Density distribution");
				}
			});
//		tv.setSorter(new DensityDistributionSort());   //设置排序器
		TableColumn tc = new TableColumn(table,SWT.LEFT);
		tc.setText("Cluster");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a=!a;
				((DensityDistributionSort)tv.getSorter()).doSort(a?-1:1);
				tv.refresh();
			}
		});
		tc = new TableColumn(table,SWT.LEFT);
		tc.setText("Node Num");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a=!a;
				((DensityDistributionSort)tv.getSorter()).doSort(a?-2:2);
				tv.refresh();
			}
		});
		tc = new TableColumn(table,SWT.LEFT);
		tc.setText("Edge Num");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a=!a;
				((DensityDistributionSort)tv.getSorter()).doSort(a?-3:3);
				tv.refresh();
			}
		});
		tc = new TableColumn(table,SWT.LEFT);
		tc.setText("Avg Degree");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a=!a;
				((DensityDistributionSort)tv.getSorter()).doSort(a?-4:4);
				tv.refresh();
			}
		});
		tc = new TableColumn(table,SWT.LEFT);
		tc.setText("Density");
		tc.setWidth(100);
		tc.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				a=!a;
				((DensityDistributionSort)tv.getSorter()).doSort(a?-5:5);
				tv.refresh();
			}
		});
		tv.setContentProvider(new DensityContentProvider());
		tv.setLabelProvider(new DensityLabelProvider());
		tv.setSorter(new DensityDistributionSort());//设置排序器
		if(algorithm.length==1){
			Vector vv = ChartsInfo.get(algorithm[0]);
			Vector<Cluster> v = (Vector<Cluster>) vv.get(4);
	        ArrayList<Cluster> inputlist = new ArrayList<Cluster>();
			inputlist.addAll(v);
			tv.setInput(inputlist);
		}
		combo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				table.removeAll();
				String algorithm = combo.getText();
				Vector vv = ChartsInfo.get(algorithm);
				Vector<Cluster> v = (Vector<Cluster>) vv.get(4);
				ArrayList<Cluster> inputlist = new ArrayList<Cluster>();
				inputlist.addAll(v);
				tv.setInput(inputlist);
			}
		});
		}
		if(para.equals("Size")){
			button.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					SaveXls(composite, table, combo.getText(), "Size distribution");
				}
			});
			TableColumn tc = new TableColumn(table,SWT.LEFT);
			tc.setText("Size Num");
			tc.setWidth(100);
			tc.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					a=!a;
					((SizeDistributionSort)tv.getSorter()).doSort(a?-1:1);
					tv.refresh();
				}
			});
			tc = new TableColumn(table,SWT.LEFT);
			tc.setText("Cluster Num");
			tc.setWidth(100);
			tc.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					a=!a;
					((SizeDistributionSort)tv.getSorter()).doSort(a?-2:2);
					tv.refresh();
				}
			});
			tv.setContentProvider(new SizeContentProvider());
			tv.setLabelProvider(new SizeLabelProvider());
			tv.setSorter(new SizeDistributionSort());
			if(algorithm.length==1){
				Vector vv = ChartsInfo.get(algorithm[0]);
				List v = (List) vv.get(5);
				tv.setInput(v);
			}
			combo.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					table.removeAll();
					String alg = combo.getText();
					Vector vv = ChartsInfo.get(alg);
					List v = (List) vv.get(5);
					tv.setInput(v);
					if(barchartComposite!=null)barchartComposite.dispose();
					barchartComposite = createBarChart(showComposite, "Size", alg,algorithm,colorlist);
					barchartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
					showComposite.layout();
				}
			});
		}
		
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
	public  ChartComposite createLineChart(Composite composite,String para,String[] algorithm,HashMap<String, RGB> colorlist){
		 XYSeries series = null;
		 Vector clusterlist = null;
		 LineChart linechart = null;
		 LinePoint linepoint = null;
		 String info2 = null;
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		for (int i = 0; i < algorithm.length; i++) {
			series = new XYSeries(algorithm[i]);
			 clusterlist = ChartsInfo.get(algorithm[i]);
			 if(para.equals("Size"))linechart = (LineChart)clusterlist.get(2);
			 if(para.equals("Density")) linechart = (LineChart)clusterlist.get(0);
			for (int j = 0; j < linechart.getPointList().size(); j++) {
				linepoint = (LinePoint)linechart.getPointList().get(j);
				if(para.equals("Size")){
					series.add(j+1,linepoint.y);
					info2 = "Density";
				}
				if(para.equals("Density")){
					series.add(linepoint.x,linepoint.y);
					info2 = "Proteins Number";
				}
			}
			dataset1.addSeries(series);
		}
		JFreeChart chart1 = ChartFactory.createXYLineChart(null, "Fig A",
				"Nodes Number", dataset1, PlotOrientation.VERTICAL,
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
		public ChartComposite createBarChart(Composite composite,String para,String name,String[] algorithm,HashMap<String, RGB> colorlist){
			  double[][] data = new double[algorithm.length][];
			  String[] rowkeys = algorithm;
			  BarChart barchart = null;
			  double value = 0;
			  if(para.equals("Density")){
			   for (int i = 0; i < algorithm.length; i++) {
				   Vector clusterlist = ChartsInfo.get(algorithm[i]);
//				   if(para.equals("Size")) barchart = (BarChart)clusterlist.get(3);
				   barchart = (BarChart)clusterlist.get(1);
				   data[i] = barchart.getData()[0];
			   }
			  }else if(para.equals("Size")){
				  Vector clusterlist = ChartsInfo.get(name);
				  barchart = (BarChart)clusterlist.get(3);
				  data = new double[1][0];
				  data[0] = barchart.getData()[0];
				  rowkeys = new String[]{name};
			  }
			 String[] columnkeys = barchart.getColumkeys();
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
			Combo combo ;
			Composite composite;
			Controllor(Combo combo,Composite composite){
				this.combo = combo;
				this.composite = composite;
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				String str = combo.getText();
				combo.setText("");
				if(str.equals("Size Distribution")){
//					claculateDistribution(algorithm, str, resultList, composite, colorlist, sizeDistributon);
					WindowShowAt = 2;
					stackLayout.topControl = sizeComposite;
					comboSize.setText("Size Distribution");
					composite.layout();
				}else
				if(str.equals("Density")){
//					claculateDistribution(algorithm, str, resultList, composite, colorlist, density);
					stackLayout.topControl = densityComposite;
					WindowShowAt = 1;
					comboDensity.setText("Density");
					composite.layout();
				}else
				if(str.equals("Main Composite")){
//					createComposite(composite);
					WindowShowAt=0;
					stackLayout.topControl = mainComposite;
					composite.layout();
				}
			}
		}
		
		/**
		 * 将表格中数据保存到excel
		 * @param composite
		 * @param table1
		 * @param title
		 * @param evaluation
		 */
		public void SaveXls(Composite composite,Table table1,String title,String evaluation){
			FileDialog fd = new FileDialog(composite.getShell(),SWT.SAVE);
			fd.setFilterExtensions(new String[]{"*.xls"});
			fd.setFilterNames(new String[]{"Excel.xls"});
			String filename = fd.open();
			if(filename==null||filename.equals(""))return;
			TableItem[] ti = table1.getItems();
			try{
			  WritableWorkbook book=
				  Workbook.createWorkbook(new File(filename));
				  WritableSheet sheet=book.createSheet("第一页",0);
//				  jxl.write.Label label=new jxl.write.Label(0,0,"test");
//				  sheet.addCell(label);
//				  for(int i=0;i<10;i++){
//					   sheet.addCell(new jxl.write.Label(0,i+1,""+i+""));
//				  }
				  WritableFont font3=new WritableFont(WritableFont.createFont("楷体 _GB2312"),17,WritableFont.NO_BOLD );
				  WritableCellFormat format1=new WritableCellFormat(font3); 
//				  sheet.setRowView(0, 20);
				  sheet.mergeCells(0,0,10,0);
				  jxl.write.Label label1 = new jxl.write.Label(0,0,evaluation+"---"+title);
				  label1.setCellFormat(format1);
				  sheet.addCell(label1);
				  int count = table1.getColumnCount();
				  TableColumn[] tc = table1.getColumns();
				  for(int i=0;i<count;i++){
					  sheet.addCell(new jxl.write.Label(i,1,tc[i].getText()));
				  }
				  for(int i=0;i<ti.length;i++){
					  for(int j=0;j<count;j++)
					  sheet.addCell(new jxl.write.Label(j,i+2,ti[i].getText(j)));
				  }
//				  jxl.write.Number number = new jxl.write.Number(1,0,789.123);
//				  sheet.addCell(number);
				  book.write();
				  book.close();
				  }catch(Exception e){
				         MessageDialog.openError(composite.getShell(), "Error", e.toString());
				         return;
				  }
				 MessageDialog.openInformation(composite.getShell(), "Success", "File Saved Successfully");
		}

	public String getAlgorithmName() {
		return null;
	}
	public Vector[] getClusters(String[] para) {
		return null;
	}
	public String getDescription() {
		return "Density and Cluster size distribution.";
	}
	public String getEvaluateNames() {
		return "Basic Information";
	}
	public String[] getParaValues() {
		return null;
	}
	public String[] getParams() {
		return null;
	}
	protected void doStart() throws Exception {
	}
	protected void doStop() throws Exception {
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return NewAlgorithm.Evaluation;
	}

}
