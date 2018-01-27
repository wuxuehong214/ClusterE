// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Test.java

package com.wuxuehong.plugin;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;
import java.io.PrintStream;
import java.util.*;

// Referenced classes of package com.wuxuehong.plugin:
//			IdentifyModulesByAgglomerative, SubGraph, Graph, Node1, 
//			Edge

public class Test
{

	ArrayList deletedEdges;
	IdentifyModulesByAgglomerative idma;

	public Test()
	{
		deletedEdges = new ArrayList();
		try
		{
			Graph yeastProtein = readYeastDIPCoreLargeComponent();
			idma = new IdentifyModulesByAgglomerative();
			idma.identifyModules(yeastProtein, deletedEdges);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Vector[] getResult()
	{
		ArrayList al = idma.printModules2();
		Vector vector[] = new Vector[al.size()];
		for (int i = 0; i < al.size(); i++)
		{
			SubGraph temp = (SubGraph)al.get(i);
			vector[i] = new Vector();
			vector[i].addAll(temp.nodes);
		}

		return vector;
	}

//	public static void main(String args[])
//	{
//		Test test1 = new Test();
//		System.out.println((new StringBuilder(String.valueOf(test1.getResult().length))).append("&&&&&&&&&&&&&&&&&&&&&&&").toString());
//	}

	public Graph readYeastDIPCoreLargeComponent()
	{
		Graph yeastP = new Graph();
		try
		{
			Vector<Edge> edgelist = GraphInfo.edgelist;
			String result[] = new String[2];
			for (int i = 0; i < edgelist.size(); i++)
			{
				Edge edge = (Edge)edgelist.get(i);
				result[0] = edge.getNode1().getNodeID();
				result[1] = edge.getNode2().getNodeID();
				deletedEdges.add((new StringBuilder(String.valueOf(result[0].trim()))).append(result[1].trim()).toString());
				if (!result[0].equals(result[1]))
				{
					Node1 n1 = (Node1)yeastP.nodeMap.get(result[0]);
					Node1 n2 = (Node1)yeastP.nodeMap.get(result[1]);
					if (n1 == null && n2 == null)
					{
						n1 = new Node1(result[0]);
						yeastP.nodes.add(n1);
						yeastP.nodeMap.put(result[0].trim(), n1);
						n2 = new Node1(result[1]);
						yeastP.nodes.add(n2);
						yeastP.nodeMap.put(result[1].trim(), n2);
						n1.adj.add(n2.nodeID.trim());
						n2.adj.add(n1.nodeID.trim());
					} else
					if (n1 == null && n2 != null)
					{
						n1 = new Node1(result[0]);
						yeastP.nodes.add(n1);
						yeastP.nodeMap.put(result[0].trim(), n1);
						n1.adj.add(n2.nodeID.trim());
						n2.adj.add(n1.nodeID.trim());
					} else
					if (n1 != null && n2 == null)
					{
						n2 = new Node1(result[1]);
						yeastP.nodes.add(n2);
						yeastP.nodeMap.put(result[1].trim(), n2);
						n1.adj.add(n2.nodeID.trim());
						n2.adj.add(n1.nodeID.trim());
					} else
					if (n1 != null && n2 != null)
					{
						n1.adj.add(n2.nodeID.trim());
						n2.adj.add(n1.nodeID.trim());
					}
					com.wuxuehong.plugin.Edge e = new com.wuxuehong.plugin.Edge(result[0], result[1]);
					yeastP.edges.add(e);
					yeastP.edgeMap.put((new StringBuilder(String.valueOf(result[0].trim()))).append(result[1].trim()).toString(), e);
					yeastP.edgeMap.put((new StringBuilder(String.valueOf(result[1].trim()))).append(result[0].trim()).toString(), e);
				}
			}

//			System.out.println((new StringBuilder("Total nodes ")).append(yeastP.nodes.size()).append("\tTotal Edges ").append(yeastP.edges.size()).toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return yeastP;
	}
}
