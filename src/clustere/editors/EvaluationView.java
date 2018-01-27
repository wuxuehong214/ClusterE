package clustere.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.wuxuehong.bean.AlgorithmColor;
import com.wuxuehong.bean.Paramater;
import com.wuxuehong.interfaces.NewAlgorithm;

import clustere.tableViewer.ColorContentProvider;
import clustere.tableViewer.ColorLabelProvider;
import clustere.tableViewer.ColorTableViewerCellModifier;

public class EvaluationView extends EditorPart {

	public static final String NAME = "algorithm";
	public static final String COLOR1 = "color";
	private static Combo combo;
	private static TableViewer tv;
	private static Group group2 ;
	private static Composite chartComposite;
	private static Composite composite;
	private static Label label;
	
	public static NewAlgorithm section = null;
	public static List<AlgorithmColor> ac = new ArrayList<AlgorithmColor>();
	public static Vector<String> algorithms = new Vector<String>();
	public static Vector<Button> buttonlist = new Vector<Button>();
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		setSite(site);
        setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		SashForm sashForm = new SashForm(parent,SWT.HORIZONTAL);
		composite = new Composite(sashForm,SWT.BORDER);
		Composite controlComposite = new Composite(sashForm,SWT.BORDER);
		sashForm.setWeights(new int[]{4,1});
		composite.setLayout(new GridLayout());
		controlComposite.setLayout(new GridLayout());
		
		label = new Label(composite,SWT.BORDER|SWT.CENTER);
		label.setFont(new Font(parent.getDisplay(), "Arial", 10, SWT.BOLD
				| SWT.ITALIC));
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		chartComposite = new Composite(composite,SWT.BORDER|SWT.DOUBLE_BUFFERED);
		chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group group1 = new Group(controlComposite,SWT.NONE);
		group1.setText("Choose Algorithm");
		group1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group1.setLayout(new GridLayout());
		combo = new Combo(group1,SWT.READ_ONLY);
		Button button1 = new Button(group1,SWT.NONE);
		button1.setText("Confirm");
		button1.addSelectionListener(new Controllor1());
		
		group2 = new Group(controlComposite,SWT.NONE);
		group2.setText("Compare");
		group2.setLayout(new GridLayout());
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		Button button2 = new Button(group2,SWT.NONE);
		button2.setText("Begin Compare");
		button2.addSelectionListener(new Controllor2());
		
		Group group3 = new Group(controlComposite,SWT.NONE);
		group3.setText("Choose color");
		group3.setLayoutData(new GridData(GridData.FILL_BOTH));
		group3.setLayout(new FillLayout());
		tv = new TableViewer(group3,SWT.FULL_SELECTION);
		Table table = tv.getTable();
		TableColumn co1 = new TableColumn(table, SWT.LEFT);
		co1.setText("Algorithm");
		co1.setWidth(80);
		TableColumn co2 = new TableColumn(table, SWT.LEFT);
		co2.setText("Color");
		co2.setWidth(70);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tv.setContentProvider(new ColorContentProvider()); // 设置内容提供器
		// 读入数据
		tv.setLabelProvider(new ColorLabelProvider()); // 设置标签提供器
		CellEditor[] cell = new CellEditor[2];
		cell[0] = null;
		cell[1] = new ColorCellEditor(table);
		tv.setColumnProperties(new String[] { NAME, COLOR1 }); // 设置列属性
		tv.setCellModifier(new ColorTableViewerCellModifier(tv)); // 设置表格单元修改器
		tv.setCellEditors(cell);
		
		initCombo();
		initGroup();
		updateTable();
	}
	
	public void initCombo(){
		combo.removeAll();
		for(int i=0;i<algorithms.size();i++){
			combo.add(algorithms.get(i));
			combo.setText(algorithms.get(0));
		}
	}
	public void initGroup(){
		for(int i=0;i<algorithms.size();i++){
		  Button b = new Button(group2,SWT.CHECK);
		  b.setText(algorithms.get(i));
		  buttonlist.add(b);
	}
	}
	
	public static void updateCombo(String name){
		if(combo==null||combo.isDisposed())return;
		combo.add(name);
		combo.setText(name);
	}
	
	public static void updateGroup(String name){
		if(group2==null||group2.isDisposed())return;
        Button b = new Button(group2,SWT.CHECK);
		b.setText(name);
		buttonlist.add(b);
		group2.layout();
	}
	
	public static void updateTable(){
		if(tv==null||tv.getTable().isDisposed())return;
		tv.getTable().removeAll();
		tv.setInput(ac);
		tv.refresh();
	}

	public void getColor(){
		for (int i = 0; i < ac.size(); i++) {
			String str = ac.get(i).getStr();
			RGB rgb = ac.get(i).getColor();
			Paramater.algorithmsColorList.put(str, rgb);
		}
	}
	
	public static Composite getComposite(){
		chartComposite.dispose();
		chartComposite = new Composite(composite,SWT.BORDER|SWT.DOUBLE_BUFFERED);
		chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.layout();
		return chartComposite;
	}
	
	public static void refresh(){
		ac.clear();
		algorithms.clear();
		buttonlist.clear();
		section = null;
	}
	
	public static void updateLabel(String name){
		label.setText(name);
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	class Controllor1 extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			String algorithm = combo.getText();
			String[] algorithms = new String[]{algorithm};
			getColor();
			if(Paramater.algorithmsResults.size()==0){
				MessageDialog.openWarning(combo.getShell(), "waning", "No cluster results to evaluate!");
				return ;
			}
			section.drawCharts(algorithms, Paramater.algorithmsResults, chartComposite, Paramater.algorithmsColorList);
		}
	}
	
	class Controllor2 extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			Vector<String> algorithms = new Vector<String>();
			for(int i=0;i<buttonlist.size();i++){
				if(buttonlist.get(i).getSelection()){
					algorithms.add(buttonlist.get(i).getText());
				}
			}
			String[] algs = new String[algorithms.size()];
			for(int i=0;i<algorithms.size();i++)
				algs[i] = algorithms.get(i);
			getColor();
			section.drawCharts(algs, Paramater.algorithmsResults, chartComposite, Paramater.algorithmsColorList);
		}
	}

}
