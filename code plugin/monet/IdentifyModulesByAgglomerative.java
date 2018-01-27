// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IdentifyModulesByAgglomerative.java

package com.wuxuehong.plugin;

import java.io.PrintStream;
import java.util.*;

// Referenced classes of package com.wuxuehong.plugin:
//			SubGraphCollection, Graph, Edge, Node1, 
//			SubGraph

public class IdentifyModulesByAgglomerative
{

	private String moduleObject;
	private String moduleFile;
	private String modeleRelationFile;
	private String biolayoutFile;
	private Graph graph;
	public SubGraphCollection sgCollection;

	public IdentifyModulesByAgglomerative()
	{
		moduleObject = "Yeast DIP Core sub Graph Collection Object.dat";
		moduleFile = "Yeast DIP Core MoNet modules.txt";
		modeleRelationFile = "Yeast DIP Core modules relation.txt";
		biolayoutFile = "Yeast DIP Core Modules Biolayout.txt";
		sgCollection = new SubGraphCollection();
	}

	public void identifyModules(Graph graph, ArrayList deletedEdges)
	{
		this.graph = graph;
		int totalLink = 0;
		try
		{
			for (int i = deletedEdges.size() - 1; i >= 0; i--)
			{
				String nodes = (String)deletedEdges.get(i);
				Edge e = (Edge)graph.edgeMap.get(nodes);
				Node1 n1_orig = (Node1)graph.nodeMap.get(e.node1_ID);
				Node1 n2_orig = (Node1)graph.nodeMap.get(e.node2_ID);
				SubGraph sg1 = sgCollection.findSubGraph(n1_orig.nodeID);
				SubGraph sg2 = sgCollection.findSubGraph(n2_orig.nodeID);
				if (sg1 == null)
				{
					sg1 = new SubGraph();
					Node1 n1 = new Node1(e.node1_ID);
					n1.extAdj.addAll(n1_orig.adj);
					sg1.nodes.add(n1);
					sg1.createNodeMap();
					String link[] = (String[])n1.adj.toArray(new String[0]);
					for (int i3 = 0; i3 < link.length; i3++)
					{
						Edge extE = (Edge)graph.edgeMap.get((new StringBuilder(String.valueOf(n1.nodeID))).append(link[i3]).toString());
						sg1.externalEdges.add(extE);
					}

					sgCollection.add(sg1);
				}
				if (sg2 == null)
				{
					sg2 = new SubGraph();
					Node1 n2 = new Node1(e.node2_ID);
					n2.extAdj.addAll(n2_orig.adj);
					sg2.nodes.add(n2);
					sg2.createNodeMap();
					String link[] = (String[])n2.adj.toArray(new String[0]);
					for (int i3 = 0; i3 < link.length; i3++)
					{
						Edge extE = (Edge)graph.edgeMap.get((new StringBuilder(String.valueOf(n2.nodeID))).append(link[i3]).toString());
						sg2.externalEdges.add(extE);
					}

					sgCollection.add(sg2);
				}
				if (sg1 == sg2)
				{
					Node1 n1 = (Node1)sg1.nodeMap.get(e.node1_ID);
					Node1 n2 = (Node1)sg2.nodeMap.get(e.node2_ID);
					n1.extAdj.remove(n2_orig.nodeID);
					n1.adj.add(n2_orig.nodeID);
					n2.extAdj.remove(n1_orig.nodeID);
					n2.adj.add(n1_orig.nodeID);
					if (!sg1.edges.contains(e))
						sg1.edges.add(e);
					else
					if (sg1.externalEdges.contains(e))
						sg1.externalEdges.remove(e);
					if (isModule2(sg1))
						sg1.isModule = true;
					sg1.edgeMap.put((new StringBuilder(String.valueOf(e.node1_ID))).append(e.node2_ID).toString(), e);
					sg1.edgeMap.put((new StringBuilder(String.valueOf(e.node2_ID))).append(e.node1_ID).toString(), e);
					totalLink++;
				} else
				if (!sg1.isModule && !sg2.isModule)
				{
					if (sg1.merg && sg2.merg)
					{
						SubGraph sg = mergeSubGraph2(sg1, sg2, e);
						if (isModule2(sg))
							sg.isModule = true;
						sgCollection.remove(sg1);
						sgCollection.remove(sg2);
						sgCollection.add(sg);
						totalLink++;
					} else
					{
						sg1.merg = false;
						sg2.merg = false;
					}
				} else
				if (sg1.isModule && !sg2.isModule)
				{
					if (sg1.merg && sg2.merg)
					{
						SubGraph sg = mergeSubGraph2(sg1, sg2, e);
						if (isModule2(sg))
						{
							sg.isModule = true;
							sgCollection.remove(sg1);
							sgCollection.remove(sg2);
							sgCollection.add(sg);
							totalLink++;
						}
					} else
					{
						sg1.merg = false;
						sg2.merg = false;
					}
				} else
				if (!sg1.isModule && sg2.isModule)
				{
					if (sg1.merg && sg2.merg)
					{
						SubGraph sg = mergeSubGraph2(sg1, sg2, e);
						if (isModule2(sg))
						{
							sg.isModule = true;
							sgCollection.remove(sg1);
							sgCollection.remove(sg2);
							sgCollection.add(sg);
							totalLink++;
						}
					} else
					{
						sg1.merg = false;
						sg2.merg = false;
					}
				} else
				if (sg1.isModule && sg2.isModule)
				{
					sg1.merg = false;
					sg2.merg = false;
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public SubGraph mergeSubGraph2(SubGraph sg1, SubGraph sg2, Edge e)
	{
		SubGraph sg = new SubGraph();
		try
		{
			for (int i1 = 0; i1 < sg1.nodes.size(); i1++)
			{
				Node1 n_orig = (Node1)sg1.nodes.get(i1);
				Node1 n = new Node1(n_orig.nodeID);
				n.adj.addAll(n_orig.adj);
				n.extAdj.addAll(n_orig.extAdj);
				sg.nodes.add(n);
			}

			for (int i2 = 0; i2 < sg2.nodes.size(); i2++)
			{
				Node1 n_orig = (Node1)sg2.nodes.get(i2);
				Node1 n = new Node1(n_orig.nodeID);
				n.adj.addAll(n_orig.adj);
				n.extAdj.addAll(n_orig.extAdj);
				sg.nodes.add(n);
			}

			sg.createNodeMap();
			for (int i = 0; i < sg.nodes.size(); i++)
			{
				Node1 n3 = (Node1)sg.nodes.get(i);
				for (int j = 0; j < sg.nodes.size(); j++)
				{
					Node1 n4 = (Node1)sg.nodes.get(j);
					if (n3 != n4)
					{
						Edge aE = (Edge)graph.edgeMap.get((new StringBuilder(String.valueOf(n3.nodeID.trim()))).append(n4.nodeID.trim()).toString());
						if (aE != null && !sg.edges.contains(aE))
						{
							sg.edges.add(aE);
							n3.extAdj.remove(n4.nodeID);
							n3.adj.add(n4.nodeID);
							n4.extAdj.remove(n3.nodeID);
							n4.adj.add(n3.nodeID);
						}
					}
				}

			}

			sg.edges.trimToSize();
			sg.createEdgeMap();
		}
		catch (Exception ee)
		{
			ee.printStackTrace();
		}
		return sg;
	}

	public boolean isModule2(SubGraph net)
	{
		int in = 0;
		int out = 0;
		for (int i = 0; i < net.nodes.size(); i++)
		{
			Node1 n = (Node1)net.nodes.get(i);
			in += n.adj.size();
			out += n.extAdj.size();
		}

		return in > 2 * out;
	}

	public float getModularity(SubGraph net)
	{
		int in = 0;
		int out = 0;
		for (int i = 0; i < net.nodes.size(); i++)
		{
			Node1 n = (Node1)net.nodes.get(i);
			in += n.adj.size();
			out += n.extAdj.size();
		}

		return (float)in / 2.0F / (float)out;
	}

	public ArrayList printModules2()
	{
		System.out.println((new StringBuilder("Number of moudles ")).append(sgCollection.subGraphs.size()).toString());
		Collections.sort(sgCollection.subGraphs, new Comparator() {

			final IdentifyModulesByAgglomerative this$0;

			public int compare(Object o1, Object o2)
			{
				if (((SubGraph)o2).nodes.size() < ((SubGraph)o1).nodes.size())
					return -1;
				return ((SubGraph)o2).nodes.size() <= ((SubGraph)o1).nodes.size() ? 0 : 1;
			}

			
			{
				this$0 = IdentifyModulesByAgglomerative.this;
			}
		});
		return sgCollection.subGraphs;
	}
}
