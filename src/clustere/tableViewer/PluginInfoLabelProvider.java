package clustere.tableViewer;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.wuxuehong.bean.PluginVo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class PluginInfoLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int key) {
		// TODO Auto-generated method stub
		PluginVo p = (PluginVo)element;
		switch(key){
		case 0:
			return p.getPluginName();
		case 1:
			{
		  if(p.getSection().getStyle()==NewAlgorithm.Algorithm)
			  return "Algorithm ";
		  if(p.getSection().getStyle() == NewAlgorithm.Evaluation)
				return "Evaluation";
			}
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

}
