package temp;

import java.math.BigDecimal;
import java.math.BigInteger;


public class Pvalue {
	
	/**
	 * 
	 * @param total_protein
	 *            整个蛋白质网络中总结点个数 N
	 * @param complex_protein
	 *            蛋白质复合物中蛋白质数量 C
	 * @param complex_funs
	 *            蛋白质复合物中含有某功能蛋白质数量 K
	 * @param net_funs
	 *            整个蛋白质网络中含有该功能蛋白质数量 F
	 * @return
	 */
	public static double CalPvalue(int total_protein, int complex_protein,
			int complex_funs, int net_funs) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal  a ,b;
		for (int i = 0; i < complex_funs; i++) {
			BigInteger aa = getC(net_funs, i).multiply(
					getC(total_protein - net_funs, complex_protein - i));
			BigInteger bb = getC(total_protein, complex_protein);
			a = new BigDecimal(aa.toString());
			b = new BigDecimal(bb.toString());
			 a= a.setScale(150);
			 b = b.setScale(150);
			a = a.divide(b,BigDecimal.ROUND_UP);
			result = result.add(a);
		}
		BigDecimal total = new BigDecimal(1);
		total = total.subtract(result);
		return total.doubleValue();
	}
	
	public static BigInteger getC(int a, int b) {
		String str = String.valueOf(1);
		BigInteger value = new BigInteger(str);
		BigInteger bi;
		int aa = a;
		for (int i = 0; i < b; i++) {
			str = String.valueOf(aa);
			bi = new BigInteger(str);
			value = value.multiply(bi);
			aa--;
		}
		return value.divide(fun1(b));
	}

	public static BigInteger fun1(int a) {
		BigInteger value = new BigInteger(String.valueOf(1));
		for (int i = a; i >= 1; i--) {
			String str = String.valueOf(i);
			BigInteger bi = new BigInteger(str);
			value = value.multiply(bi);
		}
		return value;
	}
	
//	public Pvalue(){
//		System.out.println("C1--------->:"+CalPvalue(6352, 9, 8, 18));
//		System.out.println("C2--------->:"+CalPvalue(6352, 9, 8, 25));
//		System.out.println("C3--------->:"+CalPvalue(6352, 9, 9, 1241));
//	}
	
//	public static void main(String args[])
//		{
//	Pvalue p = new Pvalue();
////		double a = Math.log10(10);
////		double b = Math.pow(10, a);
////		System.out.println(a);
////		System.out.println(b);
//	}
	
	//C1--------->:-1.34E-98
	//C2--------->:1.6122210118787E-87
	//C3--------->:2.013235528659016289708875414882860207746308921424599890672438956879752712498E-25

}
