package clustere.treeViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.ui.IEditorInput;


import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;

public class TreeElement {

	private String name;
	private List<TreeElement> list = new ArrayList<TreeElement>();
	private Vector<Node> nodelist;
	private Vector<Edge> edgelist;
	private Node node;
	
	public TreeElement(){
	}
	
	public TreeElement(String name){
		this.name = name;
	}
	public void setChildren(List<TreeElement> children){
		this.list = children;
	}
	public List<TreeElement> getChildren(){
		return  list;
	}
	public void addChild(TreeElement element){
		list.add(element);
	}
	public boolean hasChildren(){
		if(list.size()>0)
			return true;
		else return false;
	}
	public void setNodes(Vector<Node> v){
		this.nodelist = v;
	}
	public Vector<Node> getNodes(){
		return nodelist;
	}
	public void setEdges(Vector<Edge> v){
		this.edgelist = v;
	}
	public Vector<Edge> getEdges(){
		return edgelist;
	}
	public void setNode(Node node){
		this.node = node;
	}
	public Node getNode(){
		return node;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
}
