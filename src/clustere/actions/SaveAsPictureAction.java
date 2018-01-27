package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

public class SaveAsPictureAction extends Action {

	private Canvas canvas;
	
	public SaveAsPictureAction(Canvas canvas){
		setText("&Save as picture");
		setToolTipText("save picture");
		this.canvas = canvas;
//		ImageDescriptor imgdes = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_EXPORT_WIZ);
		setImageDescriptor(clustere.Activator.getImageDescriptor("icons/pictureSave.ico"));
	}
	
	public void run(){
		GC gc = new GC(canvas);
		Image image = new Image(canvas.getDisplay(),canvas.getBounds());
		gc.copyArea(image, 0, 0);
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		FileDialog saveFileDialog = new FileDialog(canvas.getShell(), SWT.SAVE);
		saveFileDialog.setText("Svae Picture");
		saveFileDialog.setFilterExtensions(new String[] { "*.jpg" });
		saveFileDialog.setFileName("");
		saveFileDialog.setFilterPath("D:/");
		String filename = saveFileDialog.open();
		if (filename == null || filename.equals(""))
			return;
		loader.save(filename, SWT.IMAGE_JPEG);
		MessageBox box = new MessageBox(canvas.getShell(), SWT.YES);
		box.setText("Tip");
		box.setMessage("Location of the picture:" + filename);
		box.open();
		gc.dispose();
		image.dispose();
	}
}
