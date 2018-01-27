package clustere.views;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import com.wuxuehong.bean.AlgorithmInfo;

public class AlgorithmInfoView extends ViewPart {

	private static Table table;
	
	public static Vector<AlgorithmInfo> infoList = new Vector<AlgorithmInfo>();
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		table = new Table(parent,SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn tc1 = new TableColumn(table,SWT.LEFT);
		tc1.setText("Algorithm");
		tc1.setWidth(80);
		TableColumn tc2 = new TableColumn(table,SWT.LEFT);
		tc2.setText("Property");
		tc2.setWidth(80);
		TableColumn tc3 = new TableColumn(table,SWT.LEFT);
		tc3.setText("Value");
		tc3.setWidth(60);
		initTable();
	}

	public static void refresh(){
		infoList.clear();
		if(table==null||table.isDisposed())return;
		table.removeAll();
	}
	
	public void initTable(){
		for(int i=0;i<infoList.size();i++){
			AlgorithmInfo  ai = infoList.get(i);
			new TableItem(table,SWT.LEFT).setText(new String[]{ai.getName(),"Clusters",""+ai.getTotalCluster()+""});
			new TableItem(table,SWT.LEFT).setText(new String[]{"","Max size",""+ai.getMax()+""});
			new TableItem(table,SWT.LEFT).setText(new String[]{"","Min size",""+ai.getMin()+""});
			new TableItem(table,SWT.LEFT).setText(new String[]{"","AvgDegree",""+ai.getDegree()+""});
		}
	}
	
	public static void update(AlgorithmInfo ai){
		infoList.add(ai);
		if(table==null||table.isDisposed())return;
		new TableItem(table,SWT.LEFT).setText(new String[]{ai.getName(),"Clusters",""+ai.getTotalCluster()+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"","Max size",""+ai.getMax()+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"","Min size",""+ai.getMin()+""});
		new TableItem(table,SWT.LEFT).setText(new String[]{"","AvgDegree",""+ai.getDegree()+""});
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

