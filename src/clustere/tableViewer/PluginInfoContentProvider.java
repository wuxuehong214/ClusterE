package clustere.tableViewer;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PluginInfoContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object element) {
		// TODO Auto-generated method stub
		return ((List)element).toArray();
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
