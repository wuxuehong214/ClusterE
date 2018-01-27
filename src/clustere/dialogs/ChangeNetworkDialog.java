package clustere.dialogs;

import java.util.Random;
import java.util.Vector;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

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
public class ChangeNetworkDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Group group1;
	private Button button2;
	private Text text1;
	private Label label1;
	private Button button6;
	private Button button5;
	private Label label2;
	private Text text2;
	private Button button4;
	private Button button3;
	private Button button1;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/

	public ChangeNetworkDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

			dialogShell.setLayout(new FormLayout());
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(285, 289);
			dialogShell.setText("Change Network");
			{
				group1 = new Group(dialogShell, SWT.NONE);
				group1.setLayout(null);
				FormData group1LData = new FormData();
				group1LData.left =  new FormAttachment(0, 1000, 12);
				group1LData.top =  new FormAttachment(0, 1000, 21);
				group1LData.width = 247;
				group1LData.height = 207;
				group1.setLayoutData(group1LData);
				group1.setText("Change network");
				{
					button1 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button1.setText("Random Network");
					button1.setBounds(8, 17, 143, 30);
					if(Paramater.NETWORK_STYLE == 2) button1.setSelection(true);
				}
				{
					button2 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button2.setText("Original Network");
					button2.setBounds(8, 59, 158, 26);
					if(Paramater.NETWORK_STYLE == 1) button2.setSelection(true);
				}
				{
					button3 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button3.setText("increase by");
					button3.setBounds(6, 102, 87, 16);
					if(Paramater.NETWORK_STYLE == 3) button3.setSelection(true);
				}
				{
					text1 = new Text(group1, SWT.BORDER);
					text1.setText("0");
					text1.setBounds(99, 102, 31, 17);
				}
				{
					label1 = new Label(group1, SWT.NONE);
					label1.setText("% in edges");
					label1.setBounds(130, 102, 60, 17);
				}
				{
					button4 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button4.setText("decrease by");
					button4.setBounds(5, 142, 87, 21);
					if(Paramater.NETWORK_STYLE == 4) button4.setSelection(true);
				}
				{
					text2 = new Text(group1, SWT.BORDER);
					text2.setText("0");
					text2.setBounds(98, 146, 31, 17);
				}
				{
					label2 = new Label(group1, SWT.NONE);
					label2.setText("% in edges");
					label2.setBounds(129, 146, 60, 17);
				}
				{
					button5 = new Button(group1, SWT.PUSH | SWT.CENTER);
					button5.setText("confirm");
					if(GraphInfo.nodelist.size()==0)
						button5.setEnabled(false);
					else 
						button5.setEnabled(true);
					button5.setBounds(55, 186, 60, 30);
					button5.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							if(button1.getSelection()){
								getRandomNetwork();
							}else if(button2.getSelection()){
								getOriginalNetwork();
							}else if(button3.getSelection()){
								getIncreasedNetwork();
							}else if(button4.getSelection()){
								getDecresedNetwork();
							}
						}
					});
				}
				{
					button6 = new Button(group1, SWT.PUSH | SWT.CENTER);
					button6.setText("close");
					button6.setSize(60, 30);
					button6.setBounds(127, 186, 60, 30);
					button6.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							dialogShell.dispose();
						}
					});
				}
			}
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
	 * 获取随机网络
	 */
	public void getRandomNetwork(){
		if(Paramater.NETWORK_STYLE==2){
			MessageDialog.openInformation(null, "warning", "current network is a random network!");
			return ;
		}
		if(GraphInfo.tempEdges.size()==0){
			GraphInfo.tempEdges.addAll(GraphInfo.edgelist);
		}
		int size = GraphInfo.tempEdges.size(); //获取当前蛋白质网络边的个数
		int count = GraphInfo.nodelist.size();  //获取当前蛋白质网络中节点规模
		Vector<Edge> edges = new Vector<Edge>();
		Random random = new Random();
		for(int i =0;i<size;i++){
			Node n1 = GraphInfo.nodelist.get(random.nextInt(count));
			Node n2 = GraphInfo.nodelist.get(random.nextInt(count));
			Edge edge = new Edge(n1,n2);
			edges.add(edge);                                                     
		}
		GraphInfo.edgelist.clear();
		GraphInfo.edgelist.addAll(edges);
		MessageDialog.openInformation(null, "success", "successfully get a random protein network!");
		Paramater.NETWORK_STYLE = 2;
	}
	/**
	 * 获取原始蛋白质网络
	 */
	public void getOriginalNetwork(){
		if(Paramater.NETWORK_STYLE==1){
			MessageDialog.openInformation(null, "warning", "current network is the orginal network!");
			return;
		}
			GraphInfo.edgelist.clear();
			GraphInfo.edgelist.addAll(GraphInfo.tempEdges);
			MessageDialog.openInformation(null, "success", "successfully get a original network!");
			Paramater.NETWORK_STYLE = 1;
	}
	
	/**
	 * 鲁棒性分析  随机增加网络边
	 */
	public void getIncreasedNetwork(){
		float a = 0;
		try{
			a = Float.parseFloat(text1.getText());
		}catch(Exception e){
			MessageDialog.openInformation(null, "error", "Number format exception!");
			return;
		}
		Paramater.NETWORK_EXPAND_PERCENT = a;
		if(GraphInfo.tempEdges.size()==0){
			GraphInfo.tempEdges.addAll(GraphInfo.edgelist);
		}
		int  size = GraphInfo.tempEdges.size();
		size = (int)((a/(float)100)*size);
		int count = GraphInfo.nodelist .size();
		Vector<Edge> edges = new Vector<Edge>();
		Random random = new Random();
		edges.addAll(GraphInfo.tempEdges);
		for(int i=0;i<size;){
			Node n1 = GraphInfo.nodelist.get(random.nextInt(count));
			Node n2 = GraphInfo.nodelist.get(random.nextInt(count));
			if(GraphInfo.edgemap.get(n1.getNodeID()+n2.getNodeID())!=null){
				continue;
			}else{
				Edge edge = new Edge(n1,n2);
				edges.add(edge);
				i++;
			}
		}
		GraphInfo.edgelist.clear();
		GraphInfo.edgelist.addAll(edges);
		MessageDialog.openInformation(null, "success", "successfully get a increased network!");
		Paramater.NETWORK_STYLE = 3;
		System.out.println("increased******************"+GraphInfo.edgelist.size());
	}
	
	/**
	 * 鲁棒性分析  随机减少网络边
	 */
	public void getDecresedNetwork(){
		float a = 0;
		try{
			a = Float.parseFloat(text2.getText());
		}catch(Exception e){
			MessageDialog.openInformation(null, "error", "Number format exception!");
			return;
		}
		Paramater.NETWORK_EXPAND_PERCENT = a;
		if(GraphInfo.tempEdges.size()==0){
			GraphInfo.tempEdges.addAll(GraphInfo.edgelist);
		}
		int  size = GraphInfo.tempEdges.size();
		size = (int)((a/(float)100)*size);
		Vector<Edge> edges = new Vector<Edge>();
		Random random = new Random();
		edges.addAll(GraphInfo.tempEdges);
		for(int i=0;i<size;i++){
			int index = random.nextInt(edges.size());
			edges.remove(index);
		}
		GraphInfo.edgelist.clear();
		GraphInfo.edgelist.addAll(edges);
		MessageDialog.openInformation(null, "success", "successfully get a decreased network!");
		Paramater.NETWORK_STYLE = 4;
		System.out.println("decreased******************"+GraphInfo.edgelist.size());
	}
	
}
