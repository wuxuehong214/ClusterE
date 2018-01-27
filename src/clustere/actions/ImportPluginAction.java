package clustere.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

public class ImportPluginAction extends Action{

   private IWorkbenchWindow window;
	
	public ImportPluginAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("&Import Plugin");
		setToolTipText("import plugin");
		ImageDescriptor imgs = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_IMPORT_WIZ);
		this.setImageDescriptor(imgs);
	}
	
	public void run(){
		pluginImport();
	}
	
	/*
	 * µ¼Èë²å¼þ
	 */
	public static void pluginImport() {
		FileDialog openFile = new FileDialog(new Shell(), SWT.OPEN);
		openFile.setText("Import plug-in");
		openFile.setFilterExtensions(new String[] { "*.jar" });
		openFile.setFilterNames(new String[] { "java(*.jar)" });
		openFile.setFilterPath("c:/");
		String path = openFile.open();
		if (path == null)
			return;
		String filename = openFile.getFileName();
		try {
			InputStream is = new FileInputStream(new File(path));
			OutputStream os = new FileOutputStream(new File("Myplugins/"
					+ filename));
			int readBytes = 0;
			byte[] buffer = new byte[1024];
			while ((readBytes = is.read(buffer, 0, 1024)) != -1)
				os.write(buffer, 0, readBytes);
			os.close();
			is.close();
		} catch (Exception e) {
			MessageDialog.openError(new Shell(), "Erro", e.toString());
			return;
		}
		MessageBox box = new MessageBox(new Shell(), SWT.YES | SWT.NO);
		box.setText("Import plug-in");
		box.setMessage("Successfully import the plugin,but you need to restart the application,OK?");
		int result = box.open();
		if (result == SWT.YES)
			System.exit(0);
		else
			return;
	}
}
