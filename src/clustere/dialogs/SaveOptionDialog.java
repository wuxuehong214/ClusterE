package clustere.dialogs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;

import clustere.treeViewer.TreeElement;
import clustere.views.ViewPart1;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;
import com.wuxuehong.interfaces.GraphInfo;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class SaveOptionDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Group group1;
	private List list1;
	private Button button2;
	private Button button1;
	private Button button3;
	private Button button4;

	public SaveOptionDialog(Shell parent, int style) {
		super(parent, style);
		open();
	}

	public void open() {
		FormData group1LData = new FormData();
		group1LData.left =  new FormAttachment(0, 1000, 12);
		group1LData.top =  new FormAttachment(0, 1000, 12);
		group1LData.width = 159;
		group1LData.height = 270;
		
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialogShell.setText("Save");
			dialogShell.setLayout(new FormLayout());
			{
				group1 = new Group(dialogShell, SWT.NONE);
				group1.setLayout(null);
				group1.setText("Choose to save");
				group1.setLayoutData(group1LData);
				{
					list1 = new List(group1, SWT.NONE);
					list1.setBounds(16, 23, 137, 157);
					Set values = Paramater.algorithmsResults.keySet();
					Iterator<String> it = values.iterator();
					while(it.hasNext()){
						list1.add(it.next());
						list1.setSelection(0);
					}
				}
				{
					button1 = new Button(group1, SWT.PUSH | SWT.CENTER);
					button1.setText("Save");
					button1.setBounds(12, 243, 60, 30);
					button1.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
					         saveCluster();
						}
					});
				}
				{
					button2 = new Button(group1, SWT.PUSH | SWT.CENTER);
					button2.setText("Close");
					button2.setBounds(93, 243, 60, 30);
					button2.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							dialogShell.dispose();
						}
					});
				}
				{
					button3 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button3.setText("easy save");
					button3.setBounds(16, 181, 109, 30);
				}
				{
					button4 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button4.setText("complete save");
					button4.setBounds(16, 211, 109, 25);
				}
			}
			dialogShell.setSize(200, 360);
			dialogShell.layout();
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 保存聚类结果
	 */
	public void saveCluster(){
		String str = list1.getSelection()[0];
		TreeElement treeElement = null;
		for(int i=0;i<ViewPart1.list.size();i++){
			TreeElement te = ViewPart1.list.get(i);
		    String testr = te.getName();
		    int index = testr.indexOf('(');
		    testr = testr.substring(0, index);
			if(te.hasChildren()&&testr.equals(str)){
				treeElement = te;
				break;
			}
		}
		if(treeElement==null){
			MessageDialog.openError(dialogShell, "Error", "No result found");
			return;
		}
		FileDialog fd = new FileDialog(dialogShell,SWT.SAVE);
		if(button3.getSelection()){
		fd.setFilterExtensions(new String[]{"*.ey","*.txt"});
		fd.setFilterNames(new String[]{"简易保存*.ey","文本文件*.txt"});
		}else if(button4.getSelection()){
			fd.setFilterExtensions(new String[]{"*.cx","*.txt"});
			fd.setFilterNames(new String[]{"完整保存*.cx","文本文件*.txt"});
		}
		String filename = fd.open();
		if(filename==null||filename.equals(""))return;
		try{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
		bw.write(str + ":Total Clusters:" +treeElement.getChildren().size());
		bw.newLine();
		bw.write("Node ID\t\tNode Neighbours");
		bw.newLine();
		for(int i=0;i<treeElement.getChildren().size();i++){
			TreeElement te = treeElement.getChildren().get(i);
			Node n = te.getNode();
			bw.write("Cluster:"+i+"\t"+n.getScope());
			bw.newLine();
			for(int j=0;j<te.getNodes().size();j++){
				Node node = te.getNodes().get(j);
				bw.write(node.getNodeID());
//				bw.write("\t"+getString(node.getNeighbours()));
				bw.newLine();
			}
		}
		if(button4.getSelection()){
		bw.write("***********************************Original Network*****************************");
		bw.newLine();
		for(int i=0;i<GraphInfo.edgelist.size();i++){
			Edge edge = GraphInfo.edgelist.get(i);
			bw.write(edge.getNode1().getNodeID()+" ");
			bw.write(edge.getNode2().getNodeID()+" ");
			bw.write(String.valueOf((int)edge.getWeight()));
			bw.newLine();
		}
		}
//		Vector<Node> clusterNode = treeElement.getNodes();
//		bw.write("Overlap of clusters****************");
//		bw.newLine();
//		for(int i=0;i<clusterNode.size();i++){
//			Node node = clusterNode.get(i);
//			bw.write(node.getNodeID()+"\t"+node.getScope());
//			for(int j=0;j<node.getNeighbour_NUM();j++){
//				Node node2 = node.getNeighbours().get(j);
//				bw.write("\t"+node2.getNodeID());
//			}
//			bw.newLine();
//		}
		bw.flush();
		bw.close();
		}catch(Exception e){
			MessageDialog.openError(dialogShell, "Error", "File read exception:"+e.toString());
		}
		MessageDialog.openInformation(dialogShell, "Success", "File saved successfully");
	}
	
	public String getString(Vector<Node> v){
		String result = "";
		for(int i=0;i<v.size();i++)
			result = result+"\t"+v.get(i).getNodeID();
		return result;
	}
	
	public static void main(String[] args){
		new SaveOptionDialog(new Shell(),SWT.NONE);
	}
	
}
