package com.wuxuehong.plugin;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class DensityLabelProvider implements ITableLabelProvider{

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object arg0, int key) {
		// TODO Auto-generated method stub
		Cluster c = (Cluster)arg0;
		switch(key){
		case 0:
			return c.getClusterID();
		case 1:
			return String.valueOf(c.getNodeCount());
		case 2:
			return String.valueOf(c.getEdgeCount());
		case 3:
			return String.valueOf(c.getAvgDegree());
		case 4:
			return String.valueOf(c.getDensity());
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
