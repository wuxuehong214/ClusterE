package hf_measure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.wuxuehong.bean.Node;

public class MainComposite extends Composite {
	
	private String description = "hF-measure(Tf) is a topology-free measurement and hF-measure(Tb) " +
			"is a topology-based measurement. hF-measure(Tf) and hF-measure(Tb) can discriminate between" +
			"different types of errors.";

	private String[] algorithm;
	private HashMap<String, Vector<Node>[]> resultList;
	private HashMap<String, RGB> colorlist;
	
	private Button button5,button6,button7;
	private String bp = "GO:0008150";
	private String mf = "GO:0003674";
	private String cc = "GO:0005575";
	
	private Button button4,buttons; //graph / table
	
	private Button button1,button2,button3; //tf tb f
	
	private HashMap<String,List<ClusterVo>> results = new HashMap<String,List<ClusterVo>>();
	
	public void setData(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList,
			HashMap<String, RGB> colorlist){
		this.algorithm = algorithm;
		this.resultList = resultList;
		this.colorlist = colorlist;
		try {
			calculate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void calculate() throws IOException{
		
		boolean hftf = false;
		boolean hftb = false;
		boolean hff = false;
		if(button2.getSelection()){
			hftb = true;
		}
		if(button1.getSelection()){
			hftf = true;
		}
		if(button3.getSelection())
			hff = true;
		if(!hftf && !hftb && !hff){
			MessageDialog.openWarning(new Shell(), "warning", "please choose one evaluation method!");
			return ;
		}
		
		
		String annotation, pcf;
		
		ProteinNetwork pn = new ProteinNetwork();
		pn.readProteins();
		
		DAGanalysis dag = new DAGanalysis();
		String top = bp;
		if(button5.getSelection()){
			top = bp;
			annotation = "GO_Process_annotation.txt";
			pcf = "GO_Process";
		}
		else if(button6.getSelection()){
			top = mf;
			annotation = "GO_Function_annotation.txt";
			pcf = "GO_Function";
		}
		else {
			top = cc;
			annotation = "GO_Component_annotation.txt";
			pcf = "GO_Component";
		}
		pn.readGeneOntology(annotation);
		dag.buildDAGwithMF(pcf);
		dag.rankDAG(top);
		

		
		
		for(int i=0;i<algorithm.length;i++){
			Vector<Node>[] clusters = resultList.get(algorithm[i]);
			pn.readClusters(clusters);
			if(hftb){
				pn.calculateTopological(dag);
			}
			if(hftf){
				hftf = true;
			}
			
			results.put(algorithm[i], pn.clusters);
		}
		if(button4.getSelection()){
			
		}else if(buttons.getSelection()){
			HfmeasureTable hft = new HfmeasureTable(hftf, hftb, hff, results);
			hft.open();
		}
		
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MainComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, true));
		
		//evaluation description
		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setText("Evaluation Description");
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
//		group.setLayout(new FillLayout());
		Text text = new Text(group,SWT.WRAP|SWT.V_SCROLL|SWT.H_SCROLL);
		text.append(description);
		
		//gene annotation
		Group group3 = new Group(this, SWT.NONE);
		group3.setText("Go annotation");
		group3.setLayoutData(new GridData(GridData.FILL_BOTH));
		group3.setLayout(new GridLayout(3,true));
		button5 = new Button(group3,SWT.RADIO);
		button5.setText("Process");
		button5.setSelection(true);
		button6 = new Button(group3,SWT.RADIO);
		button6.setText("Function");
		button7 = new Button(group3,SWT.RADIO);
		button7.setText("Componment");
		

		Composite com = new Composite(this, SWT.NONE);
		com.setLayoutData(new GridData(GridData.FILL_BOTH));
		com.setLayout(new GridLayout(2,true));
		
		
		Group group1 = new Group(com,SWT.NONE);
		group1.setLayoutData(new GridData(GridData.FILL_BOTH));
		group1.setLayout(new GridLayout(1,true));
		group1.setText("Choose Evaluation");
		Button button1 = new Button(group1,SWT.CHECK);
		button1.setText("hF-measure(Tf)");
		button1.setSelection(true);
		Button button2 = new Button(group1,SWT.CHECK);
		button2.setText("hF-measure(Tb)");
		Button button3 = new Button(group1,SWT.CHECK);
		button3.setText("F-measure");
		
		Group group2 = new Group(com,SWT.NONE);
		group2.setText("Show type");
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		group2.setLayout(new GridLayout(1, true));
		button4 = new Button(group2,SWT.RADIO);
		button4.setText("graph");
		button4.setSelection(true);
		buttons = new Button(group2,SWT.RADIO);
		buttons.setText("table");
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
