package clustere.tableViewer;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * ��TableViewer �鿴���趨 �����ṩ�� ����ģ�Ͷ����ģ�ͼ��� ����鿴��������Ҫ���ڲ��Ա��ṹ֮�����ӳ��
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
