package proteinCompelex_20110416;
/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date��2011-4-3 ����09:51:14 
 * 
 */

public class EdgeVo {
	
	private String n1,n2;
	private double pcc;
	private double ecc;
	private double weight;
	private double funSimilarity;
	
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

	public double getPcc() {
		return pcc;
	}

	public void setPcc(double pcc) {
		this.pcc = pcc;
	}

	public double getFunSimilarity() {
		return funSimilarity;
	}

	public void setFunSimilarity(double funSimilarity) {
		this.funSimilarity = funSimilarity;
	}

	public double getEcc() {
		return ecc;
	}

	public void setEcc(double ecc) {
		this.ecc = ecc;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}
