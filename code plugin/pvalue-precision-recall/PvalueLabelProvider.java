package com.wuxuehong.plugin;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class PvalueLabelProvider implements ITableLabelProvider {

	String para;
	
	public PvalueLabelProvider(String para){
		this.para = para;
	}
	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int key) {
		// TODO Auto-generated method stub
		Cluster c = (Cluster)element;
		if(para.equals("Pvalue")){
		switch(key){
		case 0:
			return c.getClusterID();
		case 1:
			return String.valueOf(c.getProteins().size());
		case 2:
			return String.valueOf(c.getNetProtein());
		case 3:
			return String.valueOf(c.getFunProtein());
		case 4:
			return String.valueOf(c.getPvalue());
		case 5:
			return c.getFunctionCode();
		case 6:
			return c.getFunction();
		case 7:
			return String.valueOf(c.getProbableFunctions());
		}
		}else
			if(para.equals("Precision")){
				switch(key){
				case 0:
					return c.getClusterID();
				case 1:
					return String.valueOf(c.getProteins().size());
				case 2:
					return String.valueOf(c.getNetProtein());
				case 3:
					return String.valueOf(c.getFunProtein());
				case 4:
					return String.valueOf(c.getPrecision());
				}
			}else 
				if(para.equals("Recall")){
					switch(key){
					case 0:
						return c.getClusterID();
					case 1:
						return String.valueOf(c.getProteins().size());
					case 2:
						return String.valueOf(c.getNetProtein());
					case 3:
						return String.valueOf(c.getFunProtein());
					case 4:
						return String.valueOf(c.getRecall());
					}
				}else 
					if(para.equals("f-measure")){
						switch(key){
						case 0:
							return c.getClusterID();
						case 1:
							return String.valueOf(c.getProteins().size());
						case 2:
							return String.valueOf(c.getNetProtein());
						case 3:
							return String.valueOf(c.getFunProtein());
						case 4:
							return String.valueOf(c.getMeasure());
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
