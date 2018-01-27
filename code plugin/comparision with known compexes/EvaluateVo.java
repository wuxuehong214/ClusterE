package com.wuxuehong.plugin;

public class EvaluateVo {
	
	private int TP;  //表示蛋白质复合物中与已知蛋白质符合物匹配程度大于0.2的数量
	
	private int  FP;  //等于识别的蛋白质复合物总数减去TP
	
	private int FN;  //表示已知蛋白质复合物中没有被表示的的数量
	
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
