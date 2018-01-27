// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MonetAlgorithm.java

package com.wuxuehong.plugin;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.NewAlgorithm;

import java.util.HashMap;
import java.util.Vector;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;

// Referenced classes of package com.wuxuehong.plugin:
//			Test

public class MonetAlgorithm extends Plugin
	implements NewAlgorithm
{

	public MonetAlgorithm()
	{
	}

	public Vector BuildCharts(Vector v[])
	{
		return null;
	}

	public String getAlgorithmName()
	{
		return "Monet";
	}

	public Vector[] getClusters(String para[])
	{
		Test test1 = new Test();
		return test1.getResult();
	}

	public String getDescription()
	{
		return "A cluster algorithm named Monet.";
	}

	public String getEvaluateNames()
	{
		return null;
	}

	public String[] getParaValues()
	{
		return null;
	}

	public String[] getParams()
	{
		return null;
	}

	protected void doStart()
		throws Exception
	{
	}

	protected void doStop()
		throws Exception
	{
	}


	@Override
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return NewAlgorithm.Algorithm;
	}

	@Override
	public void variableInit() {
		// TODO Auto-generated method stub
		
	}
}
