package com.wuxuehong.plugin;
// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClusSearch3.java

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.wuxuehong.bean.Node;

class ClusSearch3
{

	public static int clusterLength = 0;
	public static int d = 0;
	public static int neighborLength = 0;
	public static double clusterDensity1 = 0.69999999999999996D;
	public static double clusterDensity = 0.69999999999999996D;
	public static double clusprop = 0.5D;
	public static int neighWeight[] = new int[5000];
	public static int neighDegree[] = new int[5000];
	public static int neigh_weight2[] = new int[5000];

	ClusSearch3()
	{
	}

	public static Vector<Vector> makeClus(byte abyte0[][], String as[], int i,double s1, double s2)
	{
		
		clusprop = s1;
		clusterDensity = s2;
		clusterDensity1 = clusterDensity;
		Vector<Vector>  result = new Vector<Vector>();
		d = i;
		byte abyte1[][] = new byte[d][d];
		for (int j = 0; j <= d - 1; j++)
		{
			for (int k = 0; k <= d - 1; k++)
				abyte1[j][k] = abyte0[j][k];

		}

		int ai[][] = new int[2000][200];
		boolean flag = false;
		String as1[] = new String[2000];
		int ai1[] = new int[2000];
		int ai2[] = new int[d];
		int i1 = 0;
		boolean flag1 = false;
		boolean flag2 = false;
		double d1 = 0.0D;
		int j1 = 0;
		int ai3[] = new int[d];
		int k1 = 0;
		boolean flag3 = false;
		Hashtable hashtable = new Hashtable();
label0:
		do
		{
			int l1;
			int ai5[];
			do
			{
				if (flag3)
					break label0;
				if (k1 >= 2000)
					flag3 = true;
				l1 = 0;
				boolean flag8 = false;
				double d3 = 0.0D;
				int l5 = 0;
				int l6 = 0;
				ai5 = new int[500];
				int ai10[] = new int[d];
				int l8 = 0;
				int ai11[] = new int[d];
				int i9 = 0;
				int j9 = 0;
				int k9 = 0;
				for (int l9 = 0; l9 <= d - 1; l9++)
				{
					for (int i12 = 0; i12 <= d - 1; i12++)
						if (abyte0[l9][i12] == 1)
							k9++;

					ai11[l9] = k9;
					k9 = 0;
				}

				for (int i10 = 0; i10 <= d - 1; i10++)
					if (i9 < ai11[i10])
					{
						i9 = ai11[i10];
						j9 = i10;
					}

				if (i9 == 0)
					break label0;
				for (int j10 = 0; j10 <= d - 1; j10++)
				{
					for (int j12 = 0; j12 <= d - 1; j12++)
						if (abyte0[j10][j12] == 1)
							l8 += comnNeigh(abyte0, j10, j12);

					ai10[j10] = l8;
					l8 = 0;
				}

				for (int k10 = 0; k10 <= d - 1; k10++)
					if (l6 < ai10[k10])
					{
						l6 = ai10[k10];
						l5 = k10;
					}

				if (l6 != 0)
				{
					ai5[0] = l5;
					clusterLength = 1;
				} else
				{
					ai5[0] = j9;
					clusterLength = 1;
				}
				do
				{
					if (flag8)
						break;
					boolean flag7 = false;
					double d7 = clusprop;
					int ai7[] = neighbor(abyte0, ai5);
					int i8 = neighDegree[0];
					if (i8 == 1 && clusterLength > 1)
					{
						int ai9[] = sort(abyte0, ai5, ai7);
						ai7 = ai9;
						int k8 = neigh_weight2[0];
						if (k8 > 0)
							d7 /= 2D;
					}
					for (int l10 = 0; l10 <= neighborLength - 1; l10++)
					{
						ai5[clusterLength] = ai7[l10];
						double d4 = density(abyte0, ai5);
						if (d4 < clusterDensity1)
							continue;
					//	d4 = density1(abyte0, ai5);
						int j8 = degree(abyte0, ai5, ai7[l10]);
						if ((double)j8 < d4 * (double)clusterLength * d7)
							continue;
						flag7 = true;
						clusterLength++;
						break;
					}

					if (!flag7)
						flag8 = true;
				} while (true);
				for (int i11 = 0; i11 <= clusterLength - 1; i11++)
				{
					for (int k12 = 0; k12 <= d - 1; k12++)
					{
						abyte0[ai5[i11]][k12] = 0;
						abyte0[k12][ai5[i11]] = 0;
					}

				}

				if (clusterLength >= 2)
					continue;
				flag3 = true;
				continue label0;
			} while (clusterLength < 2);
			k1++;
			ai1[k1 - 1] = clusterLength;
			as1[k1 - 1] = "clus" + k1;
			if (i1 < clusterLength)
				i1 = clusterLength;
			Vector result1  = new Vector();
			for (int j11 = 0; j11 <= clusterLength - 1; j11++)
			{
				for (int l12 = 0; l12 <= clusterLength - 1; l12++)
					if (abyte1[ai5[j11]][ai5[l12]] == 1)
						l1++;

				ai[k1 - 1][j11] = ai5[j11];
				//printwriter.println(as[ai5[j11]] + " " + l1);
				result1.add(new Node(as[ai5[j11]]));
				hashtable.put(as[ai5[j11]], "cluster" + k1);
				ai2[ai5[j11]] = 1;
				ai3[j1] = ai5[j11];
				j1++;
				l1 = 0;
			}
			
			result.add(result1);
		} while (true);
		return result;
	}

	public static int degree(byte abyte0[][], int ai[], int i)
	{
		int j = 0;
		for (int k = 0; k <= clusterLength - 1; k++)
			if (abyte0[i][ai[k]] == 1)
				j++;

		return j;
	}

	public static int[] neighbor(byte abyte0[][], int ai[])
	{
		int i = 0;
		int j = 0;
		int k = -1;
		int ai1[] = new int[d];
		int ai2[] = new int[d];
		int l = 0;
		int i1 = 0;
		for (int j1 = 0; j1 <= clusterLength - 1; j1++)
		{
			for (int i2 = 0; i2 <= d - 1; i2++)
			{
				boolean flag2 = false;
				if (abyte0[ai[j1]][i2] != 1)
					continue;
				int l2 = 0;
				do
				{
					if (l2 > clusterLength - 1)
						break;
					if (ai[l2] == i2)
					{
						flag2 = true;
						break;
					}
					l2++;
				} while (true);
				if (flag2)
					continue;
				l2 = 0;
				do
				{
					if (l2 > i)
						break;
					j = l2;
					if (ai1[l2] == i2)
						break;
					l2++;
				} while (true);
				if (j == i)
				{
					ai1[i] = i2;
					i++;
					j = 0;
				}
			}

		}

		for (int k1 = 0; k1 <= i - 1; k1++)
		{
			for (int j2 = 0; j2 <= clusterLength - 1; j2++)
				if (abyte0[ai1[k1]][ai[j2]] == 1)
					l += comnNeigh(abyte0, ai1[k1], ai[j2]);

			neighWeight[k1] = l;
			l = 0;
		}

		for (int l1 = 0; l1 <= i - 1; l1++)
		{
			for (int k2 = 0; k2 <= clusterLength - 1; k2++)
				if (abyte0[ai1[l1]][ai[k2]] == 1)
					i1++;

			neighDegree[l1] = i1;
			i1 = 0;
		}

		int ai3[][] = new int[i][3];
		int ai4[][] = new int[i][3];
		for (int i3 = 0; i3 <= i - 1; i3++)
		{
			for (int k3 = 0; k3 <= i - 1; k3++)
				if (k < neighDegree[k3])
				{
					k = neighDegree[k3];
					j = k3;
				}

			ai3[i3][0] = ai1[j];
			ai3[i3][1] = neighDegree[j];
			ai3[i3][2] = neighWeight[j];
			neighDegree[j] = -1;
			k = -1;
			j = 0;
		}

		int j3 = 0;
		int l3 = 0;
		do
		{
			if (j3 >= i)
				break;
			boolean flag4 = false;
			int j4 = j3;
			do
			{
				if (j4 > i - 1)
					break;
				if (ai3[j4][1] < ai3[j3][1])
				{
					flag4 = true;
					l3 = j4 - 1;
					break;
				}
				j4++;
			} while (true);
			if (!flag4)
				l3 = i - 1;
			for (int k4 = j3; k4 <= l3; k4++)
			{
				for (int l4 = j3; l4 <= l3; l4++)
					if (k < ai3[l4][2])
					{
						k = ai3[l4][2];
						j = l4;
					}

				ai4[k4][0] = ai3[j][0];
				ai4[k4][1] = ai3[j][1];
				ai4[k4][2] = ai3[j][2];
				ai3[j][2] = -1;
				k = -1;
				j = 0;
			}

			j3 = l3 + 1;
			if (j3 == i - 1)
			{
				ai4[i - 1][0] = ai3[i - 1][0];
				ai4[i - 1][1] = ai3[i - 1][1];
				ai4[i - 1][2] = ai3[i - 1][2];
			}
		} while (true);
		for (int i4 = 0; i4 <= i - 1; i4++)
		{
			ai2[i4] = ai4[i4][0];
			neighDegree[i4] = ai4[i4][1];
			neighWeight[i4] = ai4[i4][2];
		}

		neighborLength = i;
		return ai2;
	}

	public static int comnNeigh(byte abyte0[][], int i, int j)
	{
		int k = 0;
		for (int l = 0; l <= d - 1; l++)
			k += abyte0[i][l] * abyte0[l][j];

		return k;
	}

	public static double density(byte abyte0[][], int ai[])
	{
		int i = 0;
		int j = 0;
		for (int k = 0; k <= clusterLength; k++)
		{
			for (int l = 0; l <= clusterLength; l++)
				if (abyte0[ai[k]][ai[l]] == 1)
					j++;

		}

		i = ((clusterLength + 1) * clusterLength) / 2;
		double d1 = (double)j / (2D * (double)i);
		return d1;
	}

//	public static double density1(byte abyte0[][], int ai[])
//	{
//		boolean flag = false;
//		int j = 0;
//		double d1;
//		if (clusterLength == 1)
//		{
//			d1 = 1.0D;
//		} else
//		{
//			for (int k = 0; k <= clusterLength - 1; k++)
//			{
//				for (int l = 0; l <= clusterLength - 1; l++)
//					if (abyte0[ai[k]][ai[l]] == 1)
//						j++;
//
//			}
//
//			int i = (clusterLength * (clusterLength - 1)) / 2;
//			d1 = (double)j / (2D * (double)i);
//		}
//		return d1;
//	}

	public static int degree2(byte abyte0[][], int ai[], int i)
	{
		int j = 0;
		for (int k = 0; k <= clusterLength - 1; k++)
			if (abyte0[i][ai[k]] == 1)
				j++;

		return j;
	}

	public static int[] sort(byte abyte0[][], int ai[], int ai1[])
	{
		int ai2[] = new int[d];
		int ai3[] = new int[d];
		int i = -1;
		int j = 0;
		int k = 0;
		int l = 0;
		for (int i1 = 0; i1 <= neighborLength - 1; i1++)
		{
			for (int k1 = 0; k1 <= clusterLength - 1; k1++)
				l += comnNeigh(abyte0, ai1[i1], ai[k1]);

			int l1 = 0;
			do
			{
				if (l1 > clusterLength - 1)
					break;
				if (abyte0[ai1[i1]][ai[l1]] == 1)
				{
					k = degree2(abyte0, ai, ai[l1]);
					break;
				}
				l1++;
			} while (true);
			l -= k;
			ai3[i1] = l;
			l = 0;
			k = 0;
		}

		for (int j1 = 0; j1 <= neighborLength - 1; j1++)
		{
			for (int i2 = 0; i2 <= neighborLength - 1; i2++)
				if (i < ai3[i2])
				{
					i = ai3[i2];
					j = i2;
				}

			ai2[j1] = ai1[j];
			neigh_weight2[j1] = i;
			ai3[j] = -1;
			i = -1;
		}

		return ai2;
	}

	public static int[] neighboronly(byte abyte0[][], int ai[])
	{
		boolean flag = false;
		boolean flag1 = false;
		int i = 0;
		int j = 0;
		boolean flag3 = false;
		int ai1[] = new int[d];
		for (int k = 0; k <= clusterLength - 1; k++)
		{
			for (int l = 0; l <= d - 1; l++)
			{
				boolean flag2 = false;
				if (abyte0[ai[k]][l] != 1)
					continue;
				int i1 = 0;
				do
				{
					if (i1 > clusterLength - 1)
						break;
					if (ai[i1] == l)
					{
						flag2 = true;
						break;
					}
					i1++;
				} while (true);
				if (flag2)
					continue;
				i1 = 0;
				do
				{
					if (i1 > i)
						break;
					j = i1;
					if (ai1[i1] == l)
						break;
					i1++;
				} while (true);
				if (j == i)
				{
					ai1[i] = l;
					i++;
					j = 0;
				}
			}

		}

		neighborLength = i;
		return ai1;
	}

}
