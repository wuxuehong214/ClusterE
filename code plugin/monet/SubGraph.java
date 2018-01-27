// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SubGraph.java

package com.wuxuehong.plugin;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.wuxuehong.plugin:
//			Graph, Node1, Edge

public class SubGraph extends Graph
	implements Serializable
{

	ArrayList externalEdges;
	ArrayList externalNodes;
	ArrayList otherEdges;
	public boolean isModule;
	public boolean merg;

	public SubGraph()
	{
		externalEdges = new ArrayList();
		externalNodes = new ArrayList();
		otherEdges = new ArrayList();
		isModule = false;
		merg = true;
	}

	public SubGraph(ArrayList ns, ArrayList es, ArrayList extN, ArrayList extE, ArrayList othE, ArrayList rmvE)
	{
		externalEdges = new ArrayList();
		externalNodes = new ArrayList();
		otherEdges = new ArrayList();
		isModule = false;
		merg = true;
		nodes = ns;
		edges = es;
		externalEdges = extE;
		externalNodes = extN;
		otherEdges = othE;
		removedEdges = rmvE;
		createNodeMap();
		createEdgeMap();
	}

	public void createNodeMap()
	{
		nodeMap = new HashMap();
		for (int i = 0; i < nodes.size(); i++)
		{
			Node1 n = (Node1)nodes.get(i);
			nodeMap.put(n.nodeID, n);
		}

	}

	public void createEdgeMap()
	{
		edgeMap = new HashMap();
		for (int i = 0; i < edges.size(); i++)
		{
			Edge e = (Edge)edges.get(i);
			edgeMap.put((new StringBuilder(String.valueOf(e.node1_ID))).append(e.node2_ID).toString(), e);
			edgeMap.put((new StringBuilder(String.valueOf(e.node2_ID))).append(e.node1_ID).toString(), e);
		}

	}

	public ArrayList createSubGraph()
	{
		ArrayList subNs = new ArrayList();
		System.out.println((new StringBuilder("Net node size\t")).append(nodes.size()).append("\tEdge size\t").append(edges.size()).append("\tremoved Edge size\t").append(removedEdges.size()).toString());
		resetNode1s();
		resetEdges();
		try
		{
			for (int i = 0; i < nodes.size(); i++)
			{
				Node1 start = (Node1)nodes.get(i);
				if (start.pathFlag)
					continue;
				ArrayList inEdges = new ArrayList();
				ArrayList subNodes = new ArrayList();
				if (!subNodes.contains(start))
					subNodes.add(start);
				LinkedList q = new LinkedList();
				q.addLast(start);
				start.pathFlag = true;
				while (!q.isEmpty()) 
				{
					Node1 v = (Node1)q.removeFirst();
					for (Iterator itr = v.adj.iterator(); itr.hasNext();)
					{
						String p1 = (String)itr.next();
						Node1 w = (Node1)nodeMap.get(p1);
						Edge e = (Edge)edgeMap.get((new StringBuilder(String.valueOf(v.nodeID))).append(w.nodeID).toString());
						if (e != null)
						{
							if (!inEdges.contains(e))
								inEdges.add(e);
						} else
						{
							System.out.println("No such edge");
						}
						if (!w.pathFlag)
						{
							if (!subNodes.contains(w))
								subNodes.add(w);
							w.pathFlag = true;
							q.addLast(w);
						}
					}

				}
				System.out.println((new StringBuilder("sub nodes ")).append(subNodes.size()).append("\tsub edges ").append(inEdges.size()).toString());
				if (subNodes.size() == nodes.size())
					break;
				ArrayList extEdges = new ArrayList();
				ArrayList extNodes = new ArrayList();
				ArrayList otherEdges = new ArrayList();
				ArrayList rmvEdges = new ArrayList();
				for (Iterator iter = removedEdges.iterator(); iter.hasNext();)
				{
					Edge aE = (Edge)iter.next();
					Node1 n1 = (Node1)nodeMap.get(aE.node1_ID);
					Node1 n2 = (Node1)nodeMap.get(aE.node2_ID);
					if (subNodes.contains(n1) && subNodes.contains(n2))
						rmvEdges.add(aE);
					else
					if (subNodes.contains(n1) && !subNodes.contains(n2))
					{
						extEdges.add(aE);
						if (!extNodes.contains(n2))
							extNodes.add(n2);
					} else
					if (!subNodes.contains(n1) && subNodes.contains(n2))
					{
						extEdges.add(aE);
						if (!extNodes.contains(n1))
							extNodes.add(n1);
					} else
					{
						otherEdges.add(aE);
					}
				}

				for (Iterator iter2 = externalEdges.iterator(); iter2.hasNext();)
				{
					Edge aE = (Edge)iter2.next();
					Node1 n1 = (Node1)nodeMap.get(aE.node1_ID);
					Node1 n2 = (Node1)nodeMap.get(aE.node2_ID);
					if (subNodes.contains(n1) && subNodes.contains(n2))
						rmvEdges.add(aE);
					else
					if (subNodes.contains(n1) && !subNodes.contains(n2))
					{
						extEdges.add(aE);
						if (!extNodes.contains(n2))
							extNodes.add(n2);
					} else
					if (!subNodes.contains(n1) && subNodes.contains(n2))
					{
						extEdges.add(aE);
						if (!extNodes.contains(n1))
							extNodes.add(n1);
					} else
					{
						otherEdges.add(aE);
					}
				}

				System.out.println((new StringBuilder("Sub nodes\t")).append(subNodes.size()).append("\tIn Edges\t").append(inEdges.size()).append("\tExt nodes\t").append(extNodes.size()).append("\tExt Edge\t").append(extEdges.size()).append("\tOther Edges\t").append(otherEdges.size()).toString());
				SubGraph subN = new SubGraph(subNodes, inEdges, extNodes, extEdges, otherEdges, rmvEdges);
				System.out.println((new StringBuilder("Remove edges size ")).append(removedEdges.size()).append("\t sub size\t").append(subN.nodes.size()).append("\tsub edge\t").append(subN.edges.size()).toString());
				subNs.add(subN);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (subNs.size() > 0)
			System.out.println((new StringBuilder("subNs ")).append(subNs.size()).toString());
		return subNs;
	}
}
