package clustere.treeViewer;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class TreeViewerContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		TreeElement element = (TreeElement)parentElement;
		List<TreeElement> list = element.getChildren();
		if(list==null||list.isEmpty())
			return new Object[0];
		else 
			return list.toArray();
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element1) {
		TreeElement element = (TreeElement)element1;
		List<TreeElement> list = element.getChildren();
		if(list==null||list.isEmpty())
			return false;
		else
		   return true;
	}

	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){
			List<TreeElement> list = (List)inputElement;
			return list.toArray();
		}else 
			return new Object[0];
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

}
