// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SubGraphCollection.java

package com.wuxuehong.plugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

// Referenced classes of package com.wuxuehong.plugin:
//			SubGraph, Node1

public class SubGraphCollection
	implements Serializable
{

	public ArrayList subGraphs;

	public SubGraphCollection()
	{
		subGraphs = new ArrayList();
	}

	public SubGraph findSubGraph(Node1 n)
	{
		SubGraph sg[] = (SubGraph[])subGraphs.toArray(new SubGraph[0]);
		for (int i = 0; i < sg.length; i++)
			if (sg[i].nodes.contains(n))
				return sg[i];

		return null;
	}

	public int findSubGraphIndex(Node1 n)
	{
		SubGraph sg[] = (SubGraph[])subGraphs.toArray(new SubGraph[0]);
		for (int i = 0; i < sg.length; i++)
			if (sg[i].nodes.contains(n))
				return i;

		return -1;
	}

	public SubGraph findSubGraph(String nodeID)
	{
		SubGraph sg[] = (SubGraph[])subGraphs.toArray(new SubGraph[0]);
		for (int i = 0; i < sg.length; i++)
		{
			Node1 n = (Node1)sg[i].nodeMap.get(nodeID);
			if (n != null)
				return sg[i];
		}

		return null;
	}

	public void add(SubGraph sg)
	{
		subGraphs.add(sg);
	}

	public void remove(SubGraph sg)
	{
		subGraphs.remove(sg);
	}
}
