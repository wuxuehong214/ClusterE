// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Node1.java

package com.wuxuehong.plugin;

import com.wuxuehong.bean.Node;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

public class Node1 extends Node
	implements Serializable
{

	public String nodeID;
	public TreeSet adj;
	TreeSet extAdj;
	int dist;
	Node1 path;
	ArrayList parents;
	double weight;
	boolean hasChild;
	boolean pathFlag;
	double betweenness;
	double tempBetweeen;

	public Node1(String id)
	{
		super(id);
		adj = new TreeSet();
		extAdj = new TreeSet();
		parents = new ArrayList();
		hasChild = false;
		pathFlag = false;
		betweenness = 0.0D;
		tempBetweeen = 0.0D;
		nodeID = id;
	}

	public void clean()
	{
		betweenness = 0.0D;
		tempBetweeen = 0.0D;
		dist = 0x7fffffff;
		path = null;
		hasChild = false;
		pathFlag = false;
		weight = 0.0D;
		extAdj = new TreeSet();
		parents = new ArrayList();
	}

	public void reset()
	{
		dist = 0x7fffffff;
		path = null;
		hasChild = false;
		pathFlag = false;
		tempBetweeen = 0.0D;
		weight = 0.0D;
		parents = new ArrayList();
	}
}
