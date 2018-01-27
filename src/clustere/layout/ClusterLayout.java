package clustere.layout;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Vector;

import org.eclipse.swt.widgets.Canvas;

import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;

public class ClusterLayout {

	public static final int RECTANGLE = 1;
	public static final int OVAL = 2;
	public static final int STAR = 3;
	
	public static int LAYOUT_STYLE = OVAL; 
	
	
	public static void setLayout(int style,Vector<Node> v,Canvas canvas){
		switch(style){
		case RECTANGLE:{
			Calculate1(v, canvas);
			break;
		}
		case OVAL:{
			Calculate2(v, canvas);
			break;
		}
		case STAR:{
			Calculate3(v, canvas);
			break;
		}
		}
		canvas.redraw();
	}
	
	/**
	 * �Ŵ�����
	 */
	public static void expandNetwork(Vector<Node> v,Canvas canvas) {
		int size = v.size();
		for (int i = 0; i < size; i++) {
			Node node = (Node) v.get(i);
			if (node.getExpand_paramater() > 1)
				node.setExpand_paramater(node.getExpand_paramater() - 1);
			else if (node.getExpand_paramater() > 0.1)
				node.setExpand_paramater(node.getExpand_paramater()
						- (float) 0.1);
		}
		setLayout(LAYOUT_STYLE, v, canvas);
		canvas.redraw();
	}

	/**
	 * ��С����
	 */
	public static void reduceNetwork(Vector<Node> v,Canvas canvas) {
		int size = v.size();
		for (int i = 0; i < size; i++) {
			Node node = (Node) v.get(i);
			if ((Paramater.NODE_SIZE / node.getExpand_paramater()) < 4)
				break;
			if (node.getExpand_paramater() < 1)
				node.setExpand_paramater(node.getExpand_paramater()
						+ (float) 0.1);
			else
				node.setExpand_paramater(node.getExpand_paramater() + 1);
		}
		setLayout(LAYOUT_STYLE, v, canvas);
	    canvas.redraw();
	}

	/**
	 * ������ ���֣�
	 * 
	 * @param v
	 * @param canvas
	 */
	public static void Calculate1(Vector<Node> v, Canvas canvas) {
		float paramater = 1;
		if (v.size() > 0) {
			paramater = v.get(0).getExpand_paramater();
		}
		int node_size = (int) (Paramater.NODE_SIZE / paramater);

		int width = (int) (node_size + node_size / (2 * paramater));
		int canvas_w = canvas.getBounds().width;
		int canvas_h = canvas.getBounds().height;
		int size = v.size();
		double avg = Math.sqrt(size
				/ (canvas_w * canvas_h / Math.pow(canvas_w + canvas_h, 2)));
		int w_num = (int) (canvas_w * avg / (canvas_w + canvas_h));
		int h_num = (int) (canvas_h * avg / (canvas_w + canvas_h));
//	if(w_num*width*3<canvas_w) width=width*3;
		int x = (canvas_w - w_num * width) / 2;
		int y = (canvas_h - h_num * width) / 2;
		if (x < 10 || y < 10) {
			x = 10;
			y = 10;
		}
		int tempx = x;
		int w_count = 0;
		for (int i = 0; i < size; i++) {
			Node node = v.get(i);
			node.setMx(x);
			node.setMy(y);
			w_count++;
			x += width;
			if (w_count % w_num == 0) {
				y += width;
				x = tempx;
			}
		}
	}

	/**
	 * Բ�β���
	 * 
	 * @param v
	 * @param canvas
	 */
	public static void Calculate2(Vector<Node> v, Canvas canvas) {
		float paramater = 1;
		if (v.size() > 0) {
			paramater = v.get(0).getExpand_paramater();
		}
		int node_size = (int) (Paramater.NODE_SIZE / paramater);
		int canvas_w = canvas.getBounds().width;
		int canvas_h = canvas.getBounds().height;
		int temp = 5 * node_size;
		double arc = 0;
		int index = 0;
		int size = v.size();
		double total = 2 * Math.PI * temp; // ��ʼ����λ�� �뾶100��Բ���ܳ�
		double have = (2.5 * node_size * size); // ���� �����complex��
		// �����нڵ�Բ���ܳ�
		if (have < total) {
			arc = ((2 * Math.PI) / (size));
			double j;
			int i;
			for (i = 0, j = 0; i < size; i++, j += arc) {
				Node node = (Node) v.get(i);
				node.setMx((int) (canvas_w / 2 + temp * Math.cos(j)));
				node.setMy((int) (canvas_h / 2 + temp * Math.sin(j)));
			}
			return;
		} else {
			temp = 3 * node_size;
			while (have > total) {
				int getrid = (int) (total / (2.5 * node_size));
				arc = ((2 * Math.PI) / (getrid));
				if (index + getrid > v.size()) {
					getrid = v.size() - index;
					System.out.println("***********************************");
				}
				double j;
				int i;
				for (i = index, j = 0; i < index + getrid && j < 2 * Math.PI; i++, j += arc) {
					Node node = (Node) v.get(i);
					node.setMx((int) (canvas_w / 2 + temp * Math.cos(j)));
					node.setMy((int) (canvas_h / 2 + temp * Math.sin(j)));
				}
				index = index + getrid;
				size = size - getrid;
				temp += (int) (1.5 * node_size);
				total = (2 * Math.PI * temp);
				have = (2.5 * node_size * size);
			}
			arc = ((2 * Math.PI) / (size));
			double j;
			int i;
			for (i = index, j = 0; i < v.size(); i++, j += arc) {
				Node node = (Node) v.get(i);
				node.setMx((int) (canvas_w / 2 + temp * Math.cos(j)));
				node.setMy((int) (canvas_h / 2 + temp * Math.sin(j)));
			}
		}
	}
	
	/**
	 * ��ɢ�� ����
	 * 
	 * @param v
	 * @param canvas
	 */
	public static void Calculate3(Vector<Node> v, Canvas canvas) {
		Collections.sort(v, new Comparator() { // ���ڵ��ڽڵ�����Ӵ�У����
					@Override
					public int compare(Object n1, Object n2) {
						// TODO Auto-generated method stub
						Node node1 = (Node) n1;
						Node node2 = (Node) n2;
						return node1.getNeighbour_NUM()
								- node2.getNeighbour_NUM();
					}
				});
System.out.println(v.size()+"**************************");
		Collections.reverse(v);
		// for(int i=0;i<v.size();i++){
		// Node n = (Node)v.get(i);
		// System.out.print(n.getNeighbour_NUM()+"  ");
		// }
		LinkedList<Node> list = new LinkedList<Node>();
		Node node = null;
//		if(list.size()==0)return;
		node = (Node) v.get(0);
		node.setMx(canvas.getBounds().width / 2);
		node.setMy(canvas.getBounds().height / 2);
		list.add(node);
		Vector<Node> all = new Vector<Node>();
		Vector<Node> total = new Vector<Node>();
		total.addAll(v);
		all.add(node);
		Vector<Node> calno;
		while (list.size() != 0) {
			Node no = list.remove(0); // ȡ�߶����еĵ�һ��Ԫ��
		    calno = new Vector<Node>();
		    calno.addAll(no.getNeighbours());
			calno.removeAll(all);
			cal(calno, no);
			for (int i = 0; i < no.getNeighbour_NUM(); i++) {
				Node tempn = no.getNeighbours().get(i);
				if (!all.contains(tempn))
					list.add(tempn);
			}
			all.addAll(calno);
		}
		Node choose = all.get(all.size() - 1);
		total.removeAll(all);
		cal(total, choose); // ����û���ڽڵ�Ľڵ㼯 ��λ��
		
		for(int i=0;i<v.size();i++){
			Node n = (Node)v.get(i);
			System.out.println(n.getNeighbour_NUM()+"&&&&&&&&&&&&&");
		}
	}

	/**
	 * ������ɢ�Ͳ��� ����ÿ���ڵ� �ڽڵ�λ��
	 * 
	 * @param v
	 * @param node
	 */
	public static void cal(Vector<Node> v, Node node) {
System.out.println("****************************");
		float paramater = 1;
		if (v.size() > 0) {
			paramater = v.get(0).getExpand_paramater();
		}
		int size = (int) (Paramater.NODE_SIZE / paramater); // �ڵ�ߴ�
		int num = v.size();
		int r = size * 5; // ���ڵ�֮�����
		double total = 2 * Math.PI * r;
		double have = 2.5 * size * num;
		double arc = Math.PI/(2*5);
		if (have < total) {
			//arc = 2 * Math.PI / (double) num;
			double arcc = Math.random();
			for (int i = 0; i < num; i++) {
				Node n = v.get(i);
				n.setMx((int) (node.getMx() + r * Math.cos(arcc)));
				n.setMy((int) (node.getMy() + r * Math.sin(arcc)));
				arcc += arc;
			}
			return;
		} else {
			int index = 0;
			while (have > total) {
				int getrid = (int) (total / (2.5 * size));
				arc = ((2 * Math.PI) / (getrid));
				double arcc = Math.random();
				int i;
				for (i = index; i < index + getrid; i++) {
					Node n = (Node) v.get(i);
					n.setMx((int) (node.getMx() + r * Math.cos(arcc)));
					n.setMy((int) (node.getMy() + r * Math.sin(arcc)));
					arcc += arc;
				}
				index += getrid; // �Խڵ��б��нڵ����������
				num -= getrid; // ʣ��Ľڵ���
				r = r + 2 * size; // �뾶����
				total = (2 * Math.PI * r); // �µ��ܳ�
				have = (2.5 * size * num); // ��Ҫ�ܳ�
			}
			arc = ((2 * Math.PI) / (num));
			double arcc = Math.random();
			int i;
			for (i = index; i < v.size(); i++) {
				Node n = (Node) v.get(i);
				n.setMx((int) (node.getMx() + r * Math.cos(arcc)));
				n.setMy((int) (node.getMy() + r * Math.sin(arcc)));
				arcc += arc;
			}
		}

	}
	
}
