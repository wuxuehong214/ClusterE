package clustere.tableViewer;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.wuxuehong.bean.AlgorithmColor;

/**
 * 标签提供器 决定了数据记录在表格的每一列如何显示
 * 
 * @author Administrator
 * 
 */
public class ColorLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object input, int index) {
		// TODO Auto-generated method stub
		AlgorithmColor ac = (AlgorithmColor) input;
		switch (index) {
		case 0:
			return ac.getStr();
		case 1:
			return ac.getColor().toString();
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
