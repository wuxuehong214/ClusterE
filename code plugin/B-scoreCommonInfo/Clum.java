package com.wuxuehong.plugin;

class Clum implements Comparable{
	int size;          //���нڵ�ĸ���
	int value;        // �������size��С�� �صĸ���
	Clum(int size,int value){
		this.size = size;
		this.value = value;
	}
	public int compareTo(Object o) {
		Clum a = (Clum)o;
		return  value-a.value;
	}
}