package clustere.dialogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.java.plugin.registry.PluginDescriptor;

import clustere.pluginLoader.LoaderServer;
import clustere.tableViewer.PluginInfoContentProvider;
import clustere.tableViewer.PluginInfoLabelProvider;

import com.wuxuehong.bean.Paramater;
import com.wuxuehong.bean.PluginVo;

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
public class PluginManage extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private TableViewer tv;
	private Table table1;
	private TableColumn tableColumn1;
	private Text text1;
	private Group group1;
	private Button button2;
	private Button button1;
	private TableColumn tableColumn2;

	/**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog
	 * inside a new Shell.
	 */
	// public static void main(String[] args) {
	// new PluginManage(new Shell(),SWT.NONE);
	// }
	public PluginManage(Shell parent, int style) {
		super(parent, style);
		open();
	}

	public void open() {
		FormData group1LData = new FormData();
		group1LData.left =  new FormAttachment(0, 1000, 12);
		group1LData.top =  new FormAttachment(0, 1000, 255);
		group1LData.width = 466;
		group1LData.height = 155;
		FormData button2LData = new FormData();

		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM
					| SWT.MODELESS);
			Display display = dialogShell.getDisplay();
			dialogShell.setLayout(new FormLayout());
			dialogShell.setText("Plugin-in Management");
//			dialogShell.setImage(new Image(display, "icos/open.ico"));
			{
				group1 = new Group(dialogShell, SWT.NONE);
				FillLayout group1Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
				group1.setLayout(group1Layout);
				group1.setText("Detail");
				group1.setLayoutData(group1LData);
				{
					text1 = new Text(group1, SWT.MULTI | SWT.WRAP|SWT.V_SCROLL);
					text1.setEditable(false);
				}
			}
			{
				button2 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				button2.setText("Close");
				button2.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						dialogShell.dispose();
					}
				});
			}
			{
				button1 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				button1.setText("Import New");
				button1.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
//						MenuControllor.pluginImport();
					}
				});
			}
			{
				tv = new TableViewer(dialogShell, SWT.FULL_SELECTION|SWT.BORDER);
				FormData tvLData = new FormData();
				tvLData.left =  new FormAttachment(0, 1000, 6);
				tvLData.top =  new FormAttachment(0, 1000, 10);
				tvLData.width = 458;
				tvLData.height = 186;
				tv.getControl().setLayoutData(tvLData);
				table1 = tv.getTable();
				table1.setLinesVisible(true);
				table1.setHeaderVisible(true);
				{
					tableColumn1 = new TableColumn(table1, SWT.NONE);
					tableColumn1.setText("Plugin-in");
					tableColumn1.setWidth(200);
				}
				{
					tableColumn2 = new TableColumn(table1, SWT.NONE);
					tableColumn2.setText("Style");
					tableColumn2.setWidth(315);
				}
				tv.setContentProvider(new PluginInfoContentProvider());
				tv.setLabelProvider(new PluginInfoLabelProvider());
				tv.setInput(LoaderServer.PluginInfo);
				table1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						int index = table1.getSelectionIndex();
						PluginVo p = (PluginVo)tv.getElementAt(index);
						showPluginInfo(text1, p);
					}
				});
			}
			button2LData.left =  new FormAttachment(0, 1000, 383);
			button2LData.top =  new FormAttachment(0, 1000, 220);
			button2LData.width = 57;
			button2LData.height = 23;
			button2.setLayoutData(button2LData);
			FormData button1LData = new FormData();
			button1LData.left =  new FormAttachment(0, 1000, 256);
			button1LData.top =  new FormAttachment(0, 1000, 220);
			button1LData.width = 84;
			button1LData.height = 23;
			button1.setLayoutData(button1LData);
			Menu menu = new Menu(dialogShell, SWT.POP_UP);
			//table1.setMenu(menu);
			MenuItem item1 = new MenuItem(menu, SWT.PUSH);
			item1.setText("Detail");
			item1.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event event) {
					// TODO Auto-generated method stub
					int index = table1.getSelectionIndex();
					PluginVo p = (PluginVo)tv.getElementAt(index);
					showPluginInfo(text1, p);
				}
			});
			dialogShell.layout();
			dialogShell.setSize(500, 480);
			dialogShell.setLocation(getParent().toDisplay(300, 50));
			dialogShell.open();

			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void showPluginInfo(Text text,PluginVo pv){
		text.setText("");
		PluginDescriptor p = pv.getP();
		text.append("Version:  "+p.getVersion()+"\n");
		text.append("Plugin ID:  "+p.getId()+"\n");
		text.append("Unique ID:  "+p.getUniqueId()+"\n");
		text.append("Plugin Location:  "+p.getLocation().toString()+"\n");
		text.append("Plugin MainClass:  "+p.getPluginClassName()+"\n");
		text.append("Plugin Description:  "+pv.getSection().getDescription());
	}
	/*
	 * 删除插件
	 */
	public void deletePlugin() {
		if (table1.getSelection().length == 0)
			return;
		String filename = table1.getSelection()[0].getText();
		File f = new File("plugins/" + filename);
		System.out.println(f.getAbsolutePath());
		MessageBox box = new MessageBox(new Shell(), SWT.YES);
		box.setText("插件删除信息");
		if (f.delete()) {
			box.setMessage("插件" + filename + "删除成功");
			table1.remove(table1.getSelectionIndices());
//			Paramater.pluginPath.remove(filename);
		} else
			box.setMessage("插件" + filename + "删除失败，该插件正在被程序占用");
		box.open();
	}

}
