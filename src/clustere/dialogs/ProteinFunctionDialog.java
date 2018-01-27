package clustere.dialogs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import jxl.Workbook;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

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
public class ProteinFunctionDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Table table1;
	private Button button2;
	private Button button4;
	private Button button6;
	private Button button8;
	private Label label3;
	private Button button10;
	private Label label2;
	private Label label1;
	private Button button9;
	private Group group3;
	private Button button7;
	private Button button5;
	private Text text1;
	private Group group2;
	private Button button3;
	private Button button1;
	private Group group1;
	private TableColumn tc,tc2;

	public ProteinFunctionDialog(Shell parent, int style) {
		super(parent, style);
		open();
	}
	
	public String getString(Set<String> v){
		if(v==null)return "";
		StringBuffer str = new StringBuffer("");
		Iterator it = v.iterator();
		while(it.hasNext()){
			String s = (String)it.next();
			str.append(s+"  ");
		}
		return str.toString();
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.MODELESS);
//			dialogShell.setImage(new Image(dialogShell.getDisplay(), "icos/open.ico"));
			dialogShell.setLayout(null);
			dialogShell.setText("Gene Annotation");
			{
				table1 = new Table(dialogShell, SWT.FULL_SELECTION|SWT.BORDER);
				table1.setBounds(12, 181, 431, 261);
				tc = new TableColumn(table1,SWT.LEFT);
				table1.setSortColumn(tc);
				tc2 = new TableColumn(table1,SWT.LEFT);
				tc.setWidth(100);
				tc2.setWidth(356);
				tc.setText("Protein");
				tc2.setText("Functions");
				table1.setHeaderVisible(true);
				table1.setLinesVisible(true);
			}
			{
				group1 = new Group(dialogShell, SWT.NONE);
				group1.setLayout(null);
				group1.setText("Gene Annotation");
				group1.setBounds(12, 12, 431, 63);
				{
					button1 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button1.setText("MIPS");
					button1.setBounds(49, 18, 48, 16);
					button1.setSelection(true);
				}
				{
					button2 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button2.setText("Function");
					button2.setBounds(49, 40, 68, 16);
				}
				{
					button3 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button3.setText("Process");
					button3.setBounds(123, 40, 61, 16);
				}
				{
					button4 = new Button(group1, SWT.RADIO | SWT.LEFT);
					button4.setText("Component");
					button4.setBounds(190, 40, 90, 16);
				}
				{
					button7 = new Button(group1, SWT.PUSH | SWT.CENTER);
					button7.setText("Check");
					button7.setBounds(344, 33, 60, 30);
					button7.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							if(button1.getSelection()){
							  showDataSource(Paramater.proteinFunction);
							}else if(button2.getSelection()){
								showDataSource(Paramater.goFuncitonAnnotation);
							}else if(button3.getSelection()){
								showDataSource(Paramater.goProcessAnnotation);
							}else if(button4.getSelection()){
								showDataSource(Paramater.goComponentAnnotation);
							}
						}
					});
				}
				{
					label1 = new Label(group1, SWT.NONE);
					label1.setText("MIPS:");
					label1.setBounds(7, 18, 42, 22);
				}
				{
					label2 = new Label(group1, SWT.NONE);
					label2.setText("GO  :");
					label2.setBounds(7, 40, 42, 22);
				}
			}
			{
				group2 = new Group(dialogShell, SWT.NONE);
				group2.setLayout(null);
				group2.setText("Search");
				group2.setBounds(12, 81, 232, 88);
				{
					text1 = new Text(group2, SWT.BORDER);
					text1.setText("");
					text1.setBounds(8, 23, 155, 20);
				}
				{
					button5 = new Button(group2, SWT.PUSH | SWT.CENTER);
					button5.setText("Search");
					button5.setBounds(169, 23, 49, 24);
					button5.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							String str = text1.getText().trim();
							for(int i=0;i<table1.getItemCount();i++)
							{
							if(button6.getSelection()){
								if(table1.getItems()[i].getText().equals(str)){
								  table1.setSelection(i);
								  table1.setFocus();
								}
							}
							else {
								if(table1.getItems()[i].getText().equalsIgnoreCase(str)){
									table1.setSelection(i);
									table1.setFocus();
								}
							}
							}
						}
					});
				}
				{
					button6 = new Button(group2, SWT.CHECK | SWT.LEFT);
					button6.setText("Case Sensitive");
					button6.setBounds(55, 52, 100, 24);
				}
			}
			{
				group3 = new Group(dialogShell, SWT.NONE);
				group3.setLayout(null);
				group3.setText("Load New");
				group3.setBounds(258, 87, 185, 82);
				{
					button8 = new Button(group3, SWT.PUSH | SWT.CENTER);
					button8.setText("Current Proteins");
					button8.setBounds(38, 22, 107, 24);
					button8.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							if(button1.getSelection()){
								  showCurrentProteins(Paramater.proteinFunction);
								}else if(button2.getSelection()){
									  showCurrentProteins(Paramater.goFuncitonAnnotation);
								}else if(button3.getSelection()){
									  showCurrentProteins(Paramater.goProcessAnnotation);
								}else if(button4.getSelection()){
									 showCurrentProteins(Paramater.goComponentAnnotation);
								}
						}
					});
				}
				{
					button9 = new Button(group3, SWT.PUSH | SWT.CENTER);
					button9.setText("Save");
					button9.setBounds(113, 52, 60, 24);
					button9.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							SaveXls();
						}
					});
				}
				{
					button10 = new Button(group3, SWT.PUSH | SWT.CENTER);
					button10.setText("Load Proteins");
					button10.setBounds(13, 52, 88, 24);
					button10.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e) {
							if(button1.getSelection()){
								  loadProteins(Paramater.proteinFunction);
								}else if(button2.getSelection()){
									loadProteins(Paramater.goFuncitonAnnotation);
								}else if(button3.getSelection()){
									loadProteins(Paramater.goProcessAnnotation);
								}else if(button4.getSelection()){
									loadProteins(Paramater.goComponentAnnotation);
								}
						}
					});
				}
			}
			{
				label3 = new Label(dialogShell, SWT.NONE);
				label3.setText("Status Message:");
				label3.setBounds(12, 450, 428, 26);
			}
			dialogShell.layout();
			dialogShell.setSize(460,510);			
			dialogShell.setLocation(getParent().toDisplay(200, 50));
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
	 * 将表格中数据保存到excel文件中
	 */
	public void SaveXls(){
		FileDialog fd = new FileDialog(dialogShell,SWT.SAVE);
		fd.setFilterExtensions(new String[]{"*.xls"});
		fd.setFilterNames(new String[]{"Excel.xls"});
		String filename = fd.open();
		if(filename==null||filename.equals(""))return;
		TableItem[] ti = table1.getItems();
		try{
		  WritableWorkbook book=
			  Workbook.createWorkbook(new File(filename));
			  WritableSheet sheet=book.createSheet("第一页",0);
//			  jxl.write.Label label=new jxl.write.Label(0,0,"test");
//			  sheet.addCell(label);
//			  for(int i=0;i<10;i++){
//				   sheet.addCell(new jxl.write.Label(0,i+1,""+i+""));
//			  }
			  WritableFont font3=new WritableFont(WritableFont.createFont("楷体 _GB2312"),17,WritableFont.NO_BOLD );
			  WritableCellFormat format1=new WritableCellFormat(font3); 
//			  sheet.setRowView(0, 20);
			  sheet.setColumnView(0, 20);
			  sheet.setColumnView(1, 100);
			  jxl.write.Label label1 = new jxl.write.Label(0,0,"Pritein");
			  jxl.write.Label label2 = new jxl.write.Label(1,0,"Functions");
			  label1.setCellFormat(format1);
			  label2.setCellFormat(format1);
			  sheet.addCell(label1);
			  sheet.addCell(label2);
			  
			  for(int i=0;i<ti.length;i++){
				  sheet.addCell(new jxl.write.Label(0,i+1,ti[i].getText(0)));
				  sheet.addCell(new jxl.write.Label(1,i+1,ti[i].getText(1)));
			  }
//			  jxl.write.Number number = new jxl.write.Number(1,0,789.123);
//			  sheet.addCell(number);
			  book.write();
			  book.close();
			  }catch(Exception e){
			         MessageDialog.openError(dialogShell, "Error", e.toString());
			         return;
			  }
			 MessageDialog.openInformation(dialogShell, "Success", "File Saved Successfully");
	}
	/**
	 * 显示蛋白质数据   蛋白质----功能代码 
	 * @param result
	 */
	public void showDataSource(HashMap<String,Set<String>> result){
		Set keyset = result.keySet();
		Iterator it = keyset.iterator();
		table1.removeAll();
		while(it.hasNext()){
			String str = (String)it.next();
			Set<String>  v = result.get(str);
			new TableItem(table1,SWT.LEFT).setText(new String[]{str,getString(v)});
		}
		label3.setText("Total Item: "+table1.getItemCount());
	}

	/**
	 * 显示当前蛋白质网络中 蛋白质注释信息
	 * @param result
	 */
	public void showCurrentProteins(HashMap<String,Set<String>> result){
		table1.removeAll();
		for(int i=0;i<GraphInfo.nodelist.size();i++){
			String str = GraphInfo.nodelist.get(i).getNodeID();
			Set<String> v = null;
			v = result.get(str);
			new TableItem(table1,SWT.LEFT).setText(new String[]{str,getString(v)});
		}
		label3.setText("Total Item: "+table1.getItemCount());
	}
	

	/**
	 * 加载蛋白质  并显示信息
	 */
	public void loadProteins(HashMap<String,Set<String>> result){
		FileDialog fd = new FileDialog(dialogShell,SWT.OPEN);
		fd.setText("Load Proteins");
		fd.setFilterExtensions(new String[]{"*.txt","*"});
		fd.setFilterNames(new String[]{"文本格式(*.txt)","文本()"});
		String filename = fd.open();
		if(filename==null||filename.equals(""))return;
		Set<String> proteins = new HashSet<String>();
		try{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		Scanner s ;
		String str = br.readLine();
		while(str!=null){
			s = new Scanner(str);
			while(s.hasNext()){
				proteins.add(s.next().toUpperCase());
			}
			str = br.readLine();
		}
		br.close();
		}catch(Exception e1){
			MessageDialog.openError(dialogShell, "Error", "文件读取错误！");
			return;
		}
	   Iterator it = proteins.iterator();
	   table1.removeAll();
	   while(it.hasNext()){
		   String s = (String)it.next();
		   Set<String> v = null;
			   v = result.get(s);
		   new TableItem(table1,SWT.LEFT).setText(new String[]{s,getString(v)});
		  }
	   label3.setText("Total Item: "+table1.getItemCount());
	   MessageDialog.openInformation(dialogShell, "Success", "File read Successfully!");
	}
	
}
