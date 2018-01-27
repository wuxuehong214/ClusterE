// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Edge.java

package com.wuxuehong.plugin;

import java.io.Serializable;

public class Edge
	implements Serializable
{

	String edgeID;
	String node1_ID;
	String node2_ID;
	boolean flag;
	double betweenness;
	double tempBetweeen;
	boolean reached;
	int depth;

	public Edge()
	{
		flag = false;
		betweenness = 0.0D;
		tempBetweeen = 0.0D;
		reached = false;
		depth = 0;
	}

	public Edge(String eID, String n1_ID, String n2_ID)
	{
		flag = false;
		betweenness = 0.0D;
		tempBetweeen = 0.0D;
		reached = false;
		depth = 0;
		edgeID = eID;
		node1_ID = n1_ID;
		node2_ID = n2_ID;
	}

	public Edge(String n1_ID, String n2_ID)
	{
		flag = false;
		betweenness = 0.0D;
		tempBetweeen = 0.0D;
		reached = false;
		depth = 0;
		node1_ID = n1_ID;
		node2_ID = n2_ID;
	}

	public void clean()
	{
		betweenness = 0.0D;
		tempBetweeen = 0.0D;
		flag = false;
		depth = 0;
		reached = false;
	}

	public void reset()
	{
		flag = false;
		tempBetweeen = 0.0D;
		depth = 0;
		reached = false;
	}
}
