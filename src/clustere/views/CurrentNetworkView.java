package clustere.views;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import com.wuxuehong.interfaces.GraphInfo;

public class CurrentNetworkView extends ViewPart {

	private static Table table ;
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		table = new Table(parent,SWT.FULL_SELECTION);
		TableColumn tc1 = new TableColumn(table,SWT.LEFT);
		tc1.setText("Property");
		tc1.setWidth(100);
		TableColumn tc2 = new TableColumn(table,SWT.LEFT);
		tc2.setText("Value");
		tc2.setWidth(100);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		initTable();
	}
	
	
	public void initTable(){
		int nodeNum = GraphInfo.nodelist.size();
		int edgeNum = GraphInfo.edgelist.size();
		float avgDegree = 2*(float)edgeNum/(float)nodeNum;
		new TableItem(table,SWT.LEFT).setText(new String[]{"Nodes Num",""+nodeNum+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"Edges Num",""+edgeNum+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"AvgDegree",""+avgDegree+""});
	}
	
	public static void update(){
		if(table==null||table.isDisposed())return;
		table.removeAll();
		int nodeNum = GraphInfo.nodelist.size();
		int edgeNum = GraphInfo.edgelist.size();
		float avgDegree = 2*(float)edgeNum/(float)nodeNum;
		new TableItem(table,SWT.LEFT).setText(new String[]{"Nodes Num",""+nodeNum+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"Edges Num",""+edgeNum+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"AvgDegree",""+avgDegree+""});
	}
	
	public static void refresh(){
		if(table==null||table.isDisposed())return;
		table.removeAll();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
