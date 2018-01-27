package ipcipg_n;
/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date£º2011-4-3 ÉÏÎç09:51:14 
 * 
 */

public class EdgeVo {
	
	private String n1,n2;
	private float weight;
	private float ecc;
	private float pcc;
	
	public EdgeVo(String n1, String n2) {
		super();
		this.n1 = n1;
		this.n2 = n2;
	}

	public String getN1() {
		return n1;
	}

	public void setN1(String n1) {
		this.n1 = n1;
	}

	public String getN2() {
		return n2;
	}

	public void setN2(String n2) {
		this.n2 = n2;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getEcc() {
		return ecc;
	}

	public void setEcc(float ecc) {
		this.ecc = ecc;
	}

	public float getPcc() {
		return pcc;
	}

	public void setPcc(float pcc) {
		this.pcc = pcc;
	}
	
	

}
