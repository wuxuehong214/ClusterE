// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CalculateDeleteEdgeOrder.java

package com.wuxuehong.plugin;

import java.io.*;
import java.util.*;

// Referenced classes of package com.wuxuehong.plugin:
//			Graph, Edge, Node1

public class CalculateDeleteEdgeOrder
{

	public CalculateDeleteEdgeOrder(Graph net)
	{
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter("Yeast DIP Core Delete Edge based on Betweenness 3.txt", true), true);
			ArrayList deletedEdges = new ArrayList();
			BufferedReader in = new BufferedReader(new FileReader("Yeast DIP Core Delete Edge based on Betweenness 3.txt"));
			String st;
			while ((st = in.readLine()) != null) 
			{
				String result[] = st.split("\\t");
				deletedEdges.add((new StringBuilder(String.valueOf(result[0].trim()))).append(result[1].trim()).toString());
			}
			net.removeDeletedEdges(deletedEdges);
			Node1 n1;
			Node1 n2;
			for (; net.edges.size() > 0; n2.adj.remove(n1.nodeID))
			{
				Edge e = net.getGreatestBetweennessEdge();
				net.edges.remove(e);
				pw.println((new StringBuilder(String.valueOf(e.node1_ID))).append("\t").append(e.node2_ID).append("\t").append(e.betweenness).toString());
				pw.flush();
				net.edgeMap.remove((new StringBuilder(String.valueOf(e.node1_ID))).append(e.node2_ID).toString());
				net.edgeMap.remove((new StringBuilder(String.valueOf(e.node2_ID))).append(e.node1_ID).toString());
				n1 = (Node1)net.nodeMap.get(e.node1_ID);
				n2 = (Node1)net.nodeMap.get(e.node2_ID);
				n1.adj.remove(n2.nodeID);
			}

			pw.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
