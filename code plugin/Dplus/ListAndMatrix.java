package com.wuxuehong.plugin;
// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ListAndMatrix.java

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.interfaces.GraphInfo;

public class ListAndMatrix
{

	ListAndMatrix()
	{
	}

	public  Vector<Vector> makeListAndMatrix(double s2, double s3)
	{
		Vector vector = new Vector();
		int i = 0;
		boolean flag = false;
		String s5;
		for(int i1=0;i1<GraphInfo.edgelist.size();i1++){
			Edge edge = GraphInfo.edgelist.get(i1);
			s5 = edge.getNode1().getNodeID()+"\t"+edge.getNode2().getNodeID();
			vector.add(s5);
			i++;
		}
		String as[][] = new String[i][2];
		String as1[][] = new String[i][2];
		int j = 0;
		for (int k = 0; k <= i - 1; k++)
		{
			StringTokenizer stringtokenizer = new StringTokenizer((String)vector.get(k), "\t");
			as[k][0] = stringtokenizer.nextToken().trim();
			as[k][1] = stringtokenizer.nextToken().trim();
		}

		for (int l = 0; l <= i - 1; l++)
		{
			boolean flag2 = false;
			int j1 = 0;
			do
			{
				if (j1 > j)
					break;
				if (as[l][0].equals(as[l][1]) || as[l][0].equals(as1[j1][0]) && as[l][1].equals(as1[j1][1]) || as[l][0].equals(as1[j1][1]) && as[l][1].equals(as1[j1][0]))
				{
					flag2 = true;
					break;
				}
				j1++;
			} while (true);
			if (flag2)
				continue;
			for (int k1 = 0; k1 <= 1; k1++)
				as1[j][k1] = as[l][k1];

			j++;
		}

		i = j;
		int i1 = 5000;
		String as2[] = new String[i1 + 1];
		int l1 = 0;
		int i2 = 1;
		for (; l1 < 2; l1++)
		{
			for (int i3 = 0; i3 <= i - 1; i3++)
			{
				int j2 = 0;
				int j3 = 0;
				do
				{
					if (j3 > i2)
						break;
					j2 = j3;
					if (as1[i3][l1].equals(as2[j3]))
						break;
					j3++;
				} while (true);
				if (j2 == i2)
				{
					as2[i2 - 1] = as1[i3][l1];
					i2++;
				}
			}

		}

		i1 = i2 - 1;
		byte abyte0[][] = new byte[i1][i1];
		for (int k3 = 0; k3 <= i - 1; k3++)
		{
			int k2 = position(as2, as1[k3][0]);
			int l2 = position(as2, as1[k3][1]);
			abyte0[k2][l2] = 1;
			abyte0[l2][k2] = 1;
		}

	 return	ClusSearch3.makeClus(abyte0, as2, i1,s2, s3);
	}

	public static int position(String as[], String s)
	{
		int i = 0;
		int j = as.length;
		int k = 0;
		do
		{
			if (k > j - 1)
				break;
			i = k;
			if (as[k].equals(s))
				break;
			k++;
		} while (true);
		return i;
	}
}
