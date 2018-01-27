// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Graph.java

package com.wuxuehong.plugin;

import java.io.*;
import java.util.*;

// Referenced classes of package com.wuxuehong.plugin:
//			Node1, Edge, SubGraph

public class Graph
	implements Serializable
{

	public HashMap nodeMap;
	public HashMap edgeMap;
	public ArrayList nodes;
	public ArrayList edges;
	public ArrayList subGraphs;
	ArrayList removedEdges;
	boolean dividFlag;

	public Graph()
	{
		nodeMap = new HashMap();
		edgeMap = new HashMap();
		nodes = new ArrayList();
		edges = new ArrayList();
		subGraphs = new ArrayList();
		removedEdges = new ArrayList();
		dividFlag = true;
	}

	public Graph(ArrayList e, ArrayList n)
	{
		nodeMap = new HashMap();
		edgeMap = new HashMap();
		nodes = new ArrayList();
		edges = new ArrayList();
		subGraphs = new ArrayList();
		removedEdges = new ArrayList();
		dividFlag = true;
		edges = e;
		nodes = n;
	}

	public boolean findEdge(Object e)
	{
		return edges.contains(e);
	}

	public boolean findVextex(Object v)
	{
		return nodes.contains(v);
	}

	public void cleanAllNode1s()
	{
		for (Iterator itr = nodes.iterator(); itr.hasNext(); ((Node1)itr.next()).clean());
	}

	public void resetNode1s()
	{
		for (Iterator itr = nodes.iterator(); itr.hasNext(); ((Node1)itr.next()).reset());
	}

	public void cleanAllEdges()
	{
		for (Iterator itr = edges.iterator(); itr.hasNext(); ((Edge)itr.next()).clean());
	}

	public void resetEdges()
	{
		for (Iterator itr = edges.iterator(); itr.hasNext(); ((Edge)itr.next()).reset());
	}

	public boolean isConnected()
	{
		return true;
	}

	public ArrayList getSubGraphs()
	{
		return subGraphs;
	}

	public void addEdge(Edge aE)
	{
		if (aE != null)
		{
			edgeMap.put((new StringBuilder(String.valueOf(aE.node1_ID))).append(aE.node2_ID).toString(), aE);
			edgeMap.put((new StringBuilder(String.valueOf(aE.node2_ID))).append(aE.node1_ID).toString(), aE);
			Node1 n1 = (Node1)nodeMap.get(aE.node1_ID);
			Node1 n2 = (Node1)nodeMap.get(aE.node2_ID);
			n1.adj.add(n2.nodeID);
			n2.adj.add(n1.nodeID);
			n1.extAdj.remove(n2.nodeID);
			n2.extAdj.remove(n1.nodeID);
			edges.add(aE);
		} else
		{
			System.out.println((new StringBuilder("Edge ")).append(aE).append(" is not exist").toString());
		}
	}

	public void removeEdge(Edge e)
	{
		if (e != null)
		{
			edges.remove(e);
			edgeMap.remove((new StringBuilder(String.valueOf(e.node1_ID))).append(e.node2_ID).toString());
			edgeMap.remove((new StringBuilder(String.valueOf(e.node2_ID))).append(e.node1_ID).toString());
			Node1 n1 = (Node1)nodeMap.get(e.node1_ID);
			Node1 n2 = (Node1)nodeMap.get(e.node2_ID);
			n1.adj.remove(n2.nodeID);
			n2.adj.remove(n1.nodeID);
			n1.extAdj.add(n2.nodeID);
			n2.extAdj.add(n1.nodeID);
		} else
		{
			System.out.println((new StringBuilder("Edge ")).append(e).append(" is not exist").toString());
		}
	}

	public void removeDeletedEdges(ArrayList deletedEdges)
	{
		int size = deletedEdges.size();
		for (int i = 0; i < size; i++)
		{
			String nodes = (String)deletedEdges.get(i);
			Edge e = (Edge)edgeMap.get(nodes);
			if (e != null)
			{
				edges.remove(e);
				edgeMap.remove((new StringBuilder(String.valueOf(e.node1_ID))).append(e.node2_ID).toString());
				edgeMap.remove((new StringBuilder(String.valueOf(e.node2_ID))).append(e.node1_ID).toString());
				Node1 n1 = (Node1)nodeMap.get(e.node1_ID);
				Node1 n2 = (Node1)nodeMap.get(e.node2_ID);
				n1.adj.remove(n2.nodeID);
				n2.adj.remove(n1.nodeID);
			} else
			{
				System.out.println((new StringBuilder("Edge ")).append(e).append(" is not exist").toString());
			}
		}

	}

	public void removeDeletedEdges(int size, ArrayList deletedEdges)
	{
		for (int i = 0; i < size; i++)
		{
			String nodes = (String)deletedEdges.get(i);
			Edge e = (Edge)edgeMap.get(nodes);
			if (e != null)
			{
				edges.remove(e);
				edgeMap.remove((new StringBuilder(String.valueOf(e.node1_ID))).append(e.node2_ID).toString());
				edgeMap.remove((new StringBuilder(String.valueOf(e.node2_ID))).append(e.node1_ID).toString());
				Node1 n1 = (Node1)nodeMap.get(e.node1_ID);
				Node1 n2 = (Node1)nodeMap.get(e.node2_ID);
				n1.adj.remove(n2.nodeID);
				n2.adj.remove(n1.nodeID);
			} else
			{
				System.out.println((new StringBuilder("Edge ")).append(e).append(" is not exist").toString());
			}
		}

	}

	public int[][] getMatrix(int numOfDeletedEdges, ArrayList deletedEdges)
	{
		removeDeletedEdges(numOfDeletedEdges, deletedEdges);
		return getMatrix();
	}

	public int[][] getMatrix()
	{
		int size = nodes.size();
		int net[][] = new int[size][size];
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
				if (i != j)
					net[i][j] = 0;
				else
					net[i][j] = 1;

		}

		System.out.println((new StringBuilder("Number of Edges is ")).append(edges.size()).toString());
		for (Iterator iter = edges.iterator(); iter.hasNext();)
		{
			Edge edge = (Edge)iter.next();
			int index1 = nodes.indexOf((Node1)nodeMap.get(edge.node1_ID));
			int index2 = nodes.indexOf((Node1)nodeMap.get(edge.node2_ID));
			net[index1][index2] = 1;
			net[index2][index1] = 1;
		}

		return net;
	}

	public Edge getGreatestBetweennessEdge()
	{
		Edge e = null;
		System.out.println((new StringBuilder("Size of Edges ")).append(edges.size()).append("\tSize of Node1s ").append(nodes.size()).toString());
		calculateEdgeShortestPathBetweenness();
		Collections.shuffle(edges);
		Collections.sort(edges, new Comparator() {

			final Graph this$0;

			public int compare(Object o1, Object o2)
			{
				if (((Edge)o2).betweenness < ((Edge)o1).betweenness)
					return -1;
				return ((Edge)o2).betweenness <= ((Edge)o1).betweenness ? 0 : 1;
			}

			
			{
				this$0 = Graph.this;
			}
		});
		return (Edge)edges.get(0);
	}

	public void calculateEdgeShortestPathBetweenness()
	{
		try
		{
			cleanAllEdges();
			cleanAllNode1s();
			ArrayList geneID = new ArrayList((Collection)nodeMap.keySet());
			int size = geneID.size();
			for (int i = 0; i < size; i++)
			{
				String geneid = (String)geneID.get(i);
				Node1 n = (Node1)nodeMap.get(geneid);
				shortestPath(n);
				updateEdgeBetweenness(n);
				for (Iterator iter = edges.iterator(); iter.hasNext();)
				{
					Edge e = (Edge)iter.next();
					e.betweenness += e.tempBetweeen;
				}

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void printAllShortestPath()
	{
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter("Yeast core PIN Large Component All Shortest path.txt"));
			cleanAllEdges();
			cleanAllNode1s();
			ArrayList geneID = new ArrayList((Collection)nodeMap.keySet());
			int size = geneID.size();
			for (int i = 0; i < size; i++)
			{
				String geneid = (String)geneID.get(i);
				Node1 n = (Node1)nodeMap.get(geneid);
				shortestPath(n);
				for (int j = 0; j < nodes.size(); j++)
				{
					Node1 node = (Node1)nodes.get(j);
					pw.print((new StringBuilder(String.valueOf(node.dist))).append("\t").toString());
				}

				pw.println();
				pw.flush();
				updateEdgeBetweenness(n);
				for (Iterator iter = edges.iterator(); iter.hasNext();)
				{
					Edge e = (Edge)iter.next();
					e.betweenness += e.tempBetweeen;
				}

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void shortestPath(Node1 start)
	{
		resetNode1s();
		resetEdges();
		try
		{
			LinkedList q = new LinkedList();
			q.addLast(start);
			start.dist = 0;
			start.weight = 1.0D;
			while (!q.isEmpty()) 
			{
				Node1 v = (Node1)q.removeFirst();
				for (Iterator itr = v.adj.iterator(); itr.hasNext();)
				{
					String p1 = (String)itr.next();
					Node1 w = (Node1)nodeMap.get(p1);
					if (w.dist == 0x7fffffff || w.dist > v.dist + 1)
					{
						w.dist = v.dist + 1;
						w.weight = v.weight;
						w.path = v;
						if (!w.parents.contains(v))
							w.parents.add(v);
						v.hasChild = true;
						q.addLast(w);
					} else
					if (w.dist == v.dist + 1)
					{
						w.weight += v.weight;
						if (!w.parents.contains(v))
							w.parents.add(v);
						v.hasChild = true;
					}
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void shortestPath(int pos)
	{
		resetNode1s();
		resetEdges();
		try
		{
			Node1 start = (Node1)nodes.get(pos);
			LinkedList q = new LinkedList();
			q.addLast(start);
			start.dist = 0;
			start.weight = 1.0D;
			while (!q.isEmpty()) 
			{
				Node1 v = (Node1)q.removeFirst();
				for (Iterator itr = v.adj.iterator(); itr.hasNext();)
				{
					String p1 = (String)itr.next();
					Node1 w = (Node1)nodeMap.get(p1);
					if (w.dist == 0x7fffffff || w.dist > v.dist + 1)
					{
						w.dist = v.dist + 1;
						w.weight = v.weight;
						w.path = v;
						if (!w.parents.contains(v))
							w.parents.add(v);
						v.hasChild = true;
						q.addLast(w);
					} else
					if (w.dist == v.dist + 1)
					{
						w.weight += v.weight;
						if (!w.parents.contains(v))
							w.parents.add(v);
						v.hasChild = true;
					}
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateEdgeBetweenness(Node1 n)
	{
		if (!n.hasChild && n.dist == 0)
			return;
		Collections.sort(nodes, new Comparator() {

			final Graph this$0;

			public int compare(Object o1, Object o2)
			{
				if (((Node1)o2).dist < ((Node1)o1).dist)
					return -1;
				return ((Node1)o2).dist <= ((Node1)o1).dist ? 0 : 1;
			}

			
			{
				this$0 = Graph.this;
			}
		});
		for (Iterator iter = nodes.iterator(); iter.hasNext();)
		{
			Node1 n1 = (Node1)iter.next();
			if (n1.parents.size() > 0)
			{
				for (Iterator iter3 = n1.parents.iterator(); iter3.hasNext();)
				{
					Node1 n3 = (Node1)iter3.next();
					Edge e = (Edge)edgeMap.get((new StringBuilder(String.valueOf(n1.nodeID))).append(n3.nodeID).toString());
					e.tempBetweeen = (n3.weight / n1.weight) * (1.0D + n1.tempBetweeen);
					n3.tempBetweeen += e.tempBetweeen;
				}

			}
		}

	}

	public double getEdgeBetweenness(Node1 n)
	{
		double betw = 0.0D;
		if (!n.hasChild && n.dist != 0x7fffffff)
			return betw;
		for (Iterator iter = n.adj.iterator(); iter.hasNext();)
		{
			String p1 = (String)iter.next();
			Node1 n1 = (Node1)nodeMap.get(p1);
			if (n1.dist > n.dist)
			{
				Edge e = (Edge)edgeMap.get((new StringBuilder(String.valueOf(n.nodeID))).append(n1.nodeID).toString());
				e.tempBetweeen = (n.weight / n1.weight) * (1.0D + getEdgeBetweenness(n1));
				betw += e.tempBetweeen;
			}
		}

		return betw;
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
				ArrayList subNode1s = new ArrayList();
				if (!subNode1s.contains(start))
					subNode1s.add(start);
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
							if (!subNode1s.contains(w))
								subNode1s.add(w);
							w.pathFlag = true;
							q.addLast(w);
						}
					}

				}
				System.out.println((new StringBuilder("sub nodes ")).append(subNode1s.size()).append("\tsub edges ").append(inEdges.size()).toString());
				if (subNode1s.size() == nodes.size())
					break;
				ArrayList extEdges = new ArrayList();
				ArrayList extNode1s = new ArrayList();
				ArrayList otherEdges = new ArrayList();
				ArrayList rmvEdges = new ArrayList();
				for (Iterator iter = removedEdges.iterator(); iter.hasNext();)
				{
					Edge aE = (Edge)iter.next();
					Node1 n1 = (Node1)nodeMap.get(aE.node1_ID);
					Node1 n2 = (Node1)nodeMap.get(aE.node2_ID);
					if (!subNode1s.contains(n1) || !subNode1s.contains(n2))
						if (subNode1s.contains(n1) && !subNode1s.contains(n2))
						{
							extEdges.add(aE);
							if (!extNode1s.contains(n2))
								extNode1s.add(n2);
						} else
						if (!subNode1s.contains(n1) && subNode1s.contains(n2))
						{
							extEdges.add(aE);
							if (!extNode1s.contains(n1))
								extNode1s.add(n1);
						} else
						{
							otherEdges.add(aE);
						}
				}

				System.out.println((new StringBuilder("Sub nodes\t")).append(subNode1s.size()).append("\tIn Edges\t").append(inEdges.size()).append("\tExt nodes\t").append(extNode1s.size()).append("\tExt Edge\t").append(extEdges.size()).append("\tOther Edges\t").append(otherEdges.size()).toString());
				SubGraph subN = new SubGraph(subNode1s, inEdges, extNode1s, extEdges, otherEdges, rmvEdges);
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

	public void printEdgeBetweenness()
	{
		Edge e = null;
		System.out.println((new StringBuilder("Size of Edges ")).append(edges.size()).append("\tSize of Node1s ").append(nodes.size()).toString());
		calculateEdgeShortestPathBetweenness();
		Collections.shuffle(edges);
		Collections.sort(edges, new Comparator() {

			final Graph this$0;

			public int compare(Object o1, Object o2)
			{
				if (((Edge)o2).betweenness < ((Edge)o1).betweenness)
					return -1;
				return ((Edge)o2).betweenness <= ((Edge)o1).betweenness ? 0 : 1;
			}

			
			{
				this$0 = Graph.this;
			}
		});
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter("Betweenness.txt"));
			for (int i = 0; i < edges.size(); i++)
			{
				Edge ae = (Edge)edges.get(i);
				pw.println((new StringBuilder(String.valueOf(ae.node1_ID))).append("\t").append(ae.node2_ID).append("\t").append(ae.betweenness).toString());
			}

			pw.flush();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
