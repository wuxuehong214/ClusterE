package com.wuxuehong.plugin;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SizeContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object e) {
		// TODO Auto-generated method stub
		return ((List)e).toArray();
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
