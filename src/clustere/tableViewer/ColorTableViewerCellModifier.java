package clustere.tableViewer;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.TableItem;

import clustere.editors.EvaluationView;

import com.wuxuehong.bean.AlgorithmColor;

/**
 * 表格单元格修改器
 * 
 * @author Administrator
 * 
 */
public class ColorTableViewerCellModifier implements ICellModifier {

	private Viewer viewe;

	public ColorTableViewerCellModifier(Viewer viewe) {
		this.viewe = viewe;
	}

	/**
	 * 返回true表示可以修改
	 */
	@Override
	public boolean canModify(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 参数element是记录表格的对象 property是表格列属性
	 */
	@Override
	public Object getValue(Object element, String property) {
		// TODO Auto-generated method stub
		AlgorithmColor ac = (AlgorithmColor) element;
		if (EvaluationView.NAME.equals(property))
			return ac.getStr();
		if (EvaluationView.COLOR1.equals(property))
			return ac.getColor();
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		// TODO Auto-generated method stub
		TableItem item = (TableItem) element;
		AlgorithmColor ac = (AlgorithmColor) item.getData();
		if (EvaluationView.NAME.equals(property))
			ac.setStr(ac.getStr());
		if (EvaluationView.COLOR1.equals(property))
			ac.setColor((RGB) value);
	}

}
