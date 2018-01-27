package hf_measure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;

public class HfmeasureTable {
	private Table table;
	private HashMap<String,List<ClusterVo>> results;
	private boolean hftf = false;
	private boolean hftb = false;
	private boolean hff = false;

	public HfmeasureTable(boolean hftf, boolean hftb , boolean hff, HashMap<String,List<ClusterVo>> results){
		this.hff = hff;
		this.hftb = hftb;
		this.hftf = hftf;
		this.results = results;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(626, 438);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, true));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Combo combo = new Combo(composite, SWT.NONE);
		if(results != null){
			Iterator<String> it = results.keySet().iterator();
			String name = it.next();
			combo.add(name);
			combo.setData(name, results.get(name));
		}
		combo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				String text = combo.getText();
				List<ClusterVo> clusters = (List<ClusterVo>) combo.getData(text);
				table.removeAll();
				for(int i=0;i<clusters.size();i++){
					ClusterVo cluster = clusters.get(i);
					new TableItem(table,SWT.LEFT).setText(new String[]{cluster.getClusterid(),cluster.getHfmeasure()+"",cluster.getHfmeasure2()+"",cluster.getFmeasure()+""});
				}
				table.layout();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		Button btnExport = new Button(composite, SWT.NONE);
		btnExport.setText("Export");
		
		Group group = new Group(shell, SWT.NONE);
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		shell.open();
		shell.layout();
	}
}
