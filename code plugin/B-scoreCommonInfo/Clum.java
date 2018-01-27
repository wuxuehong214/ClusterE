package com.wuxuehong.plugin;

class Clum implements Comparable{
	int size;          //簇中节点的个数
	int value;        // 含有如此size大小的 簇的个数
	Clum(int size,int value){
		this.size = size;
		this.value = value;
	}
	public int compareTo(Object o) {
		Clum a = (Clum)o;
		return  value-a.value;
	}
}