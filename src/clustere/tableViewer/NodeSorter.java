package clustere.tableViewer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.wuxuehong.bean.Node;

public class NodeSorter extends ViewerSorter {

	private int column;
	public void doSort(int column){
		this.column = column;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		Node n1 = (Node)e1;
		Node n2 = (Node)e2;
		switch(column){
		case 1:{
			String str1 = n1.getNodeID();
			String str2 = n2.getNodeID();
			return str1.compareTo(str2);
		}
		case -1:{
			String str1 = n1.getNodeID();
			String str2 = n2.getNodeID();
			return str2.compareTo(str1);
		}
		case 2:{
			Integer str1 = n1.getNeighbour_NUM();
			Integer str2 = n2.getNeighbour_NUM();
			return str1.compareTo(str2);
		}
		case -2:{
			Integer str1 = n1.getNeighbour_NUM();
			Integer str2 = n2.getNeighbour_NUM();
			return str2.compareTo(str1);
		}
		}
		return super.compare(viewer, e1, e2);
	}
	
}
