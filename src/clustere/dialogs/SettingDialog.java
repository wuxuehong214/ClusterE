package clustere.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

import clustere.Activator;
import clustere.editors.ClusterEditor;
import clustere.editors.NetworkView;

import com.wuxuehong.bean.Paramater;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class SettingDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Group group1;
	private Label label1;
	private Button button8;
	private Button button9;
	private Button button7;
	private Button button6;
	private Button button5;
	private Label label8;
	private Button button4;
	private Label label5;
	private Button button3;
	private Label label4;
	private Text text1;
	private Label label3;
	private Button button2;
	private Label label2;
	private Button button1;

	private Color back_color;
	private Color fore_color;
	private Color separate_color;
	private Canvas canvas3;
	private Canvas canvas2;
	private Canvas canvas1;
	private Button button14;
	private Button button13;
	private Button button12;
	private Label label10;
	private Button button11;
	private Label label9;
	private Group group2;
	private Text text2;
	private Label label7;
	private Button button10;
	private Label label6;

	/**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog
	 * inside a new Shell.
	 */

	public SettingDialog(Shell parent, int style) {
		super(parent, style);
		open();
	}

	public void open() {
		FormData canvas1LData = new FormData();
		canvas1LData.left =  new FormAttachment(0, 1000, 137);
		canvas1LData.top =  new FormAttachment(0, 1000, -3);
		canvas1LData.width = 21;
		canvas1LData.height = 19;
		FormData group2LData = new FormData();
		group2LData.left =  new FormAttachment(0, 1000, 17);
		group2LData.top =  new FormAttachment(0, 1000, 284);
		group2LData.width = 434;
		group2LData.height = 73;
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM
					| SWT.APPLICATION_MODAL);
			dialogShell.setLayout(new FormLayout());
			dialogShell.setText("Paramater Setting");
			{
				group2 = new Group(dialogShell, SWT.NONE);
				group2.setLayout(null);
				group2.setText("Gene Annotation");
				group2.setLayoutData(group2LData);
				{
					label9 = new Label(group2, SWT.NONE);
					label9.setText("MIPS:");
					label9.setBounds(8, 21, 41, 21);
				}
				{
					button11 = new Button(group2, SWT.RADIO | SWT.LEFT);
					button11.setText("MIPS");
					button11.setBounds(70, 21, 60, 21);
				}
				{
					label10 = new Label(group2, SWT.NONE);
					label10.setText("GO :");
					label10.setBounds(8, 59, 29, 21);
				}
				{
					button12 = new Button(group2, SWT.RADIO | SWT.LEFT);
					button12.setText("Function");
					button12.setBounds(70, 59, 78, 21);
				}
				{
					button13 = new Button(group2, SWT.RADIO | SWT.LEFT);
					button13.setText("Process");
					button13.setBounds(177, 59, 65, 21);
				}
				{
					button14 = new Button(group2, SWT.RADIO | SWT.LEFT);
					button14.setText("Component");
					button14.setBounds(270, 59, 94, 21);
				}
			}
			{
				button9 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				button9.setText("Defalut");
				button9.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						defaultSetting(); // 默认设置
					}
				});
			}
			{
				button8 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				button8.setText("Cancel");
				button8.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						dialogShell.dispose();
					}
				});
			}
			{
				button7 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				button7.setText("Confirm");
				button7.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						doSetting(); // 应用设置
						dialogShell.dispose(); // 关闭对话框
					}
				});
			}
			{
				button6 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				button6.setText("Apply");
				button6.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						doSetting();
					}
				});
			}
			{
				group1 = new Group(dialogShell, SWT.NONE);
				FormLayout group1Layout = new FormLayout();
				group1.setLayout(group1Layout);
				group1.setText("Common Setting");
				{
					canvas1 = new Canvas(group1, SWT.NONE);
					canvas1.setLayoutData(canvas1LData);
				}
				{
					button4 = new Button(group1, SWT.CHECK | SWT.LEFT);
					button4.setSelection(true);
				}
				{
					label5 = new Label(group1, SWT.NONE);
					label5.setText("Show Label:");
				}
				{
					button3 = new Button(group1, SWT.CHECK | SWT.LEFT);
					button3.setSelection(true);
				}
				{
					label4 = new Label(group1, SWT.NONE);
					label4.setText("Show Nodes:");
				}
				{
					text1 = new Text(group1, SWT.BORDER );
					text1.setText("10");
				}
				{
					label3 = new Label(group1, SWT.NONE);
					label3.setText("Node Size:");
				}
				{
					label1 = new Label(group1, SWT.NONE);
					label1.setText("BackGround:");
					FormData label1LData = new FormData();
					label1LData.width = 82;
					label1LData.height = 22;
					label1LData.left =  new FormAttachment(21, 1000, 0);
					label1LData.right =  new FormAttachment(208, 1000, 0);
					label1LData.top =  new FormAttachment(27, 1000, 0);
					label1LData.bottom =  new FormAttachment(109, 1000, 0);
					label1.setLayoutData(label1LData);
				}
				{
					button1 = new Button(group1, SWT.PUSH | SWT.CENTER);
					button1.setText("Choose");
					FormData button1LData = new FormData();
					button1LData.width = 185;
					button1LData.height = 24;
					button1LData.left =  new FormAttachment(454, 1000, 0);
					button1LData.right =  new FormAttachment(875, 1000, 0);
					button1LData.top =  new FormAttachment(-15, 1000, 0);
					button1LData.bottom =  new FormAttachment(88, 1000, 0);
					button1.setLayoutData(button1LData);
					button1.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							ColorDialog colordialog = new ColorDialog(
									new Shell());
							colordialog.setText("choose BackgroundColor");
							RGB rgb = colordialog.open();
							if (rgb != null){
								back_color = new Color(dialogShell.getDisplay(),rgb);
								canvas1.setBackground(back_color);
							}
						   
						}
					});
				}
				{
					label2 = new Label(group1, SWT.NONE);
					label2.setText("ForeGround:");
					FormData label2LData = new FormData();
					label2LData.width = 71;
					label2LData.height = 21;
					label2LData.left =  new FormAttachment(21, 1000, 0);
					label2LData.right =  new FormAttachment(183, 1000, 0);
					label2LData.top =  new FormAttachment(131, 1000, 0);
					label2LData.bottom =  new FormAttachment(209, 1000, 0);
					label2.setLayoutData(label2LData);
				}
				{
					button2 = new Button(group1, SWT.PUSH | SWT.CENTER);
					button2.setText("Choose");
					FormData button2LData = new FormData();
					button2LData.width = 185;
					button2LData.height = 24;
					button2LData.left =  new FormAttachment(454, 1000, 0);
					button2LData.right =  new FormAttachment(875, 1000, 0);
					button2LData.top =  new FormAttachment(107, 1000, 0);
					button2LData.bottom =  new FormAttachment(205, 1000, 0);
					button2.setLayoutData(button2LData);
					button2.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							ColorDialog colordialog = new ColorDialog(
									new Shell());
							colordialog.setText("choose ForegroundColor");
							RGB rgb = colordialog.open();
							if (rgb != null){
								fore_color = new Color(dialogShell.getDisplay(),rgb);
								canvas2.setBackground(fore_color);
							}
						}
					});
				}
				{
					label8 = new Label(group1, SWT.NONE);
					FormData label8LData = new FormData();
					label8LData.left =  new FormAttachment(0, 1000, 9);
					label8LData.top =  new FormAttachment(0, 1000, 205);
					label8LData.width = 61;
					label8LData.height = 17;
					label8.setLayoutData(label8LData);
					label8.setText("Show Edges:");
				}
				{
					button5 = new Button(group1, SWT.CHECK | SWT.LEFT);
					FormData button5LData = new FormData();
					button5LData.left =  new FormAttachment(0, 1000, 272);
					button5LData.top =  new FormAttachment(0, 1000, 205);
					button5LData.width = 26;
					button5LData.height = 23;
					button5.setLayoutData(button5LData);
					button5.setSelection(true);
				}
				{
					label6 = new Label(group1, SWT.NONE);
					FormData label6LData = new FormData();
					label6LData.left =  new FormAttachment(0, 1000, 9);
					label6LData.top =  new FormAttachment(0, 1000, 56);
					label6LData.width = 103;
					label6LData.height = 21;
					label6.setLayoutData(label6LData);
					label6.setText("Separate Color:");
				}
				{
					button10 = new Button(group1, SWT.PUSH | SWT.CENTER);
					FormData button10LData = new FormData();
					button10LData.left =  new FormAttachment(0, 1000, 199);
					button10LData.top =  new FormAttachment(0, 1000, 53);
					button10LData.width = 185;
					button10LData.height = 24;
					button10.setLayoutData(button10LData);
					button10.setText("Choose");
					button10.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							ColorDialog colordialog = new ColorDialog(
									new Shell());
							colordialog.setText("choose SeparateColor");
							RGB rgb = colordialog.open();
							if (rgb != null){
								separate_color = new Color(dialogShell.getDisplay(),rgb);
								canvas3.setBackground(separate_color);
							}
						}
					});
				}
				{
					label7 = new Label(group1, SWT.NONE);
					FormData label7LData = new FormData();
					label7LData.left =  new FormAttachment(0, 1000, 9);
					label7LData.top =  new FormAttachment(0, 1000, 123);
					label7LData.width = 71;
					label7LData.height = 16;
					label7.setLayoutData(label7LData);
					label7.setText("Line Width:");
				}
				{
					text2 = new Text(group1, SWT.BORDER);
					FormData text2LData = new FormData();
					text2LData.left =  new FormAttachment(0, 1000, 248);
					text2LData.top =  new FormAttachment(0, 1000, 123);
					text2LData.width = 67;
					text2LData.height = 16;
					text2.setLayoutData(text2LData);
					text2.setText("1");
				}
				{
					FormData canvas2LData = new FormData();
					canvas2LData.left =  new FormAttachment(0, 1000, 137);
					canvas2LData.top =  new FormAttachment(0, 1000, 24);
					canvas2LData.width = 21;
					canvas2LData.height = 19;
					canvas2 = new Canvas(group1, SWT.NONE);
					canvas2.setLayoutData(canvas2LData);
				}
				{
					FormData canvas3LData = new FormData();
					canvas3LData.left =  new FormAttachment(0, 1000, 137);
					canvas3LData.top =  new FormAttachment(0, 1000, 53);
					canvas3LData.width = 21;
					canvas3LData.height = 19;
					canvas3 = new Canvas(group1, SWT.NONE);
					canvas3.setLayoutData(canvas3LData);
				}
			}
			FormData button7LData = new FormData();
			button7LData.left =  new FormAttachment(0, 1000, 261);
			button7LData.top =  new FormAttachment(0, 1000, 427);
			button7LData.width = 66;
			button7LData.height = 23;
			button7.setLayoutData(button7LData);
			FormData button6LData = new FormData();
			button6LData.left =  new FormAttachment(0, 1000, 261);
			button6LData.top =  new FormAttachment(0, 1000, 388);
			button6LData.width = 66;
			button6LData.height = 23;
			button6.setLayoutData(button6LData);
			FormData button4LData = new FormData();
			button4LData.left =  new FormAttachment(0, 1000, 272);
			button4LData.top =  new FormAttachment(0, 1000, 180);
			button4LData.width = 18;
			button4LData.height = 16;
			button4.setLayoutData(button4LData);
			FormData label5LData = new FormData();
			label5LData.left =  new FormAttachment(0, 1000, 5);
			label5LData.top =  new FormAttachment(0, 1000, 180);
			label5LData.width = 84;
			label5LData.height = 13;
			label5.setLayoutData(label5LData);
			FormData button3LData = new FormData();
			button3LData.left =  new FormAttachment(0, 1000, 272);
			button3LData.top =  new FormAttachment(0, 1000, 151);
			button3LData.width = 26;
			button3LData.height = 23;
			button3.setLayoutData(button3LData);
			FormData label4LData = new FormData();
			label4LData.left =  new FormAttachment(0, 1000, 5);
			label4LData.top =  new FormAttachment(0, 1000, 151);
			label4LData.width = 75;
			label4LData.height = 17;
			label4.setLayoutData(label4LData);
			FormData text1LData = new FormData();
			text1LData.left =  new FormAttachment(0, 1000, 248);
			text1LData.top =  new FormAttachment(0, 1000, 95);
			text1LData.width = 67;
			text1LData.height = 16;
			text1.setLayoutData(text1LData);
			FormData label3LData = new FormData();
			label3LData.left =  new FormAttachment(0, 1000, 9);
			label3LData.top =  new FormAttachment(0, 1000, 95);
			label3LData.width = 61;
			label3LData.height = 16;
			label3.setLayoutData(label3LData);
			FormData group1LData = new FormData();
			group1LData.left =  new FormAttachment(0, 1000, 12);
			group1LData.top =  new FormAttachment(0, 1000, 16);
			group1LData.width = 442;
			group1LData.height = 232;
			group1.setLayoutData(group1LData);

			FormData button9LData = new FormData();
			button9LData.left =  new FormAttachment(0, 1000, 104);
			button9LData.top =  new FormAttachment(0, 1000, 427);
			button9LData.width = 67;
			button9LData.height = 23;
			button9.setLayoutData(button9LData);
			FormData button8LData = new FormData();
			button8LData.left =  new FormAttachment(0, 1000, 104);
			button8LData.top =  new FormAttachment(0, 1000, 388);
			button8LData.width = 67;
			button8LData.height = 23;
			button8.setLayoutData(button8LData);

			initSetting();// 初始化设置

			Display display = dialogShell.getDisplay();
//			dialogShell.setImage(new Image(display, "icos/open.ico"));
//			dialogShell.setImage(clustere.Activator.getImageDescriptor("icons/open.ico").createImage());
			dialogShell.layout();
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.setSize(480,550);
			dialogShell.open();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 执行参数设置 (应用)
	 */
	public void doSetting() {
		boolean isNum1 = text1.getText().matches("[0-9]+");
		boolean isNum2 = text2.getText().matches("[0-9]+");
		if (!isNum1 || !isNum2 ) {
			MessageBox box = new MessageBox(dialogShell, SWT.YES);
			box.setMessage("Number Format error!");
			box.open();
			return;
		}
		if (back_color != null)
			Paramater.BACK_COLOR = back_color;
		if (fore_color != null)
			Paramater.FORE_COLOR = fore_color;
		if(separate_color!=null)
			Paramater.SEPARATE_COLOR = separate_color;
		Paramater.IS_SHOWNAME = button4.getSelection();
		Paramater.IS_SHOWNODE = button3.getSelection();
		Paramater.IS_SHOWEDGE = button5.getSelection();
		Paramater.NODE_SIZE = Integer.parseInt(text1.getText());
        Paramater.LINE_WIDTH = Integer.parseInt(text2.getText());
        if(button11.getSelection())
        	Paramater.currentProteinFunction=Paramater.proteinFunction;
        else if(button12.getSelection())
        	Paramater.currentProteinFunction=Paramater.goFuncitonAnnotation;
        else if(button13.getSelection())
        	Paramater.currentProteinFunction=Paramater.goProcessAnnotation;
        else if(button14.getSelection())
        	Paramater.currentProteinFunction=Paramater.goComponentAnnotation;

			ClusterEditor.setRedraw();
			NetworkView.setRedraw();
	}

	/*
	 * 初始化设置对话框
	 */
	public void initSetting() {
		canvas1.setBackground(Paramater.BACK_COLOR);
		canvas2.setBackground(Paramater.FORE_COLOR);
		canvas3.setBackground(Paramater.SEPARATE_COLOR);
		if (Paramater.IS_SHOWNAME)
			button4.setSelection(true);
		else
			button4.setSelection(false);
		if (Paramater.IS_SHOWNODE)
			button3.setSelection(true);
		else
			button3.setSelection(false);
		if (Paramater.IS_SHOWEDGE)
			button5.setSelection(true);
		else
			button5.setSelection(false);
		text1.setText(String.valueOf(Paramater.NODE_SIZE));
		text2.setText(String.valueOf(Paramater.LINE_WIDTH));
		if(Paramater.currentProteinFunction==Paramater.proteinFunction)
			button11.setSelection(true);
		else if(Paramater.currentProteinFunction == Paramater.goFuncitonAnnotation)
			button12.setSelection(true);
		else if(Paramater.currentProteinFunction==Paramater.goProcessAnnotation)
			button13.setSelection(true);
		else if(Paramater.currentProteinFunction == Paramater.goComponentAnnotation)
			button14.setSelection(true);
	}

	/*
	 * 默认设置
	 */
	public void defaultSetting() {
		Paramater.IS_SHOWNAME = true; // 显示节点名称
		button4.setSelection(true);
		Paramater.IS_SHOWNODE = true; // 显示节点
		button3.setSelection(true);
		Paramater.IS_SHOWEDGE = true; // 显示边
		button5.setSelection(true);
		text1.setText("20"); // 节点大小10
		text2.setText("2");
		Paramater.BACK_COLOR = new Color(Display.getDefault(),new RGB(250,0,0));
		canvas1.setBackground(Paramater.BACK_COLOR);
		Paramater.FORE_COLOR = new Color(Display.getDefault(),new RGB(0,0,0));
		canvas2.setBackground(Paramater.FORE_COLOR);
		Paramater.SEPARATE_COLOR = new Color(Display.getDefault(),new RGB(0,0,250));
		canvas3.setBackground(Paramater.SEPARATE_COLOR);
		Paramater.currentProteinFunction =Paramater.proteinFunction;
		button11.setSelection(true);
	}
}
