package com.wuxuehong.plugin;

public class EvaluateVo {
	
	private int TP;  //��ʾ�����ʸ�����������֪�����ʷ�����ƥ��̶ȴ���0.2������
	
	private int  FP;  //����ʶ��ĵ����ʸ�����������ȥTP
	
	private int FN;  //��ʾ��֪�����ʸ�������û�б���ʾ�ĵ�����
	
	private float Sp; // tp/(tp+fp)
	
	private float Sn;  //tp/(tp+fn)

	private float Fm;  //f-measure  2*sp*sn/(sp+sn)
	
	public float getFm() {
		return Fm;
	}

	public void setFm(float fm) {
		Fm = fm;
	}

	public int getTP() {
		return TP;
	}

	public void setTP(int tp) {
		TP = tp;
	}

	public int getFP() {
		return FP;
	}

	public void setFP(int fp) {
		FP = fp;
	}

	public int getFN() {
		return FN;
	}

	public void setFN(int fn) {
		FN = fn;
	}

	public float getSp() {
		return Sp;
	}

	public void setSp(float sp) {
		Sp = sp;
	}

	public float getSn() {
		return Sn;
	}

	public void setSn(float sn) {
		Sn = sn;
	}
	
	

}
