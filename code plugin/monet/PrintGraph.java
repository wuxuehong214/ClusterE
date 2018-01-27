// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PrintGraph.java

package com.wuxuehong.plugin;

import java.io.*;
import java.util.*;

// Referenced classes of package com.wuxuehong.plugin:
//			Graph, Node1, SubGraph, Edge

public class PrintGraph
{

	private String subGraphFile;
	private String biolayoutInputFile;
	private String moduleFile;

	public PrintGraph()
	{
		subGraphFile = "Modules of Yeast DIP Core Network with 1230 links removed.txt";
		biolayoutInputFile = "Yeast DIP Core Biolayout.txt";
		moduleFile = "Yeast DIP Core modules.txt";
	}

	public void printGraph(Graph net)
	{
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter("C:\\Documents and Settings\\Administrator\\workspace\\Monet\\src\\ppi.txt"));
			Node1 node1;
			for (Iterator iter = net.nodes.iterator(); iter.hasNext();)
				node1 = (Node1)iter.next();

			pw.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void printSubGraph(Graph aNet)
	{
		int communityNum = 0;
		int total = 0;
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter(subGraphFile));
			for (Iterator iter5 = aNet.subGraphs.iterator(); iter5.hasNext();)
			{
				SubGraph net = (SubGraph)iter5.next();
				if (net.nodes.size() > 100)
				{
					Edge e;
					for (Iterator iter = net.edges.iterator(); iter.hasNext(); pw.println((new StringBuilder(String.valueOf(e.edgeID))).append("\t").append(e.node1_ID).append("\t").append(e.node2_ID).toString()))
						e = (Edge)iter.next();

				}
				pw.flush();
				communityNum++;
			}

			pw.close();
			System.out.println((new StringBuilder("total nodes ")).append(total).toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void pringModules(Graph net)
	{
		int communityNum = 0;
		int total = 0;
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter(moduleFile, true), true);
			pw.println("module\tmodule size\t");
			ArrayList subs = getModule(net);
			System.out.println((new StringBuilder("Number of moudles ")).append(subs.size()).toString());
			for (Iterator iter2 = subs.iterator(); iter2.hasNext();)
			{
				SubGraph sub = (SubGraph)iter2.next();
				pw.println((new StringBuilder(String.valueOf(communityNum))).append("\t").append(sub.nodes.size()).toString());
				total += sub.nodes.size();
				for (Iterator iter = sub.nodes.iterator(); iter.hasNext(); pw.println())
				{
					Node1 n = (Node1)iter.next();
					String link[] = (String[])n.adj.toArray(new String[0]);
					for (int i3 = 0; i3 < link.length; i3++)
						pw.print((new StringBuilder(String.valueOf(link[i3]))).append(" ").toString());

					pw.print("\t");
					for (Iterator iter3 = sub.externalEdges.iterator(); iter3.hasNext();)
					{
						Edge e = (Edge)iter3.next();
						if (e.node1_ID.equals(n.nodeID))
							pw.print((new StringBuilder(String.valueOf(e.node2_ID))).append(" ").toString());
						else
						if (e.node2_ID.equals(n.nodeID))
							pw.print((new StringBuilder(String.valueOf(e.node1_ID))).append(" ").toString());
					}

				}

				pw.flush();
				communityNum++;
			}

			pw.close();
			System.out.println((new StringBuilder("total nodes ")).append(total).toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList getModule(Graph net)
	{
		ArrayList subs = new ArrayList();
		if (net.subGraphs.size() > 0)
		{
			SubGraph subN;
			for (Iterator iter = net.subGraphs.iterator(); iter.hasNext(); subs.addAll(getModule(((Graph) (subN)))))
				subN = (SubGraph)iter.next();

		} else
		if (net.nodes.size() > 0)
			subs.add((SubGraph)net);
		return subs;
	}
}
