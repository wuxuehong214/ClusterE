package clustere.tableViewer;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 由TableViewer 查看器设定 内容提供器 用于模型对象或模型几何 它与查看器本身需要的内部猎豹结构之间进行映射
 * 
 * @author Administrator
 * 
 */
public class ColorContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object input) {
		// TODO Auto-generated method stub
		return ((List) input).toArray();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

}
