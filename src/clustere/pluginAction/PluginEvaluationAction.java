package clustere.pluginAction;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import clustere.Activator;
import clustere.dialogs.AlgorithmSetDialog;
import clustere.editors.ClusterEditorInput;
import clustere.editors.EvaluationView;
import clustere.editors.EvaluationViewInput;
import clustere.views.ViewPart1;

import com.wuxuehong.bean.Paramater;
import com.wuxuehong.interfaces.NewAlgorithm;


public class PluginEvaluationAction extends Action{
	
	private NewAlgorithm section;
    private String editorID = "ClusterE.editor2";
	
	public PluginEvaluationAction(NewAlgorithm section){
		this.setText("&"+section.getEvaluateNames());
		this.section = section;
	}
	
	public void run(){
		IWorkbenchPage workbenchpage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorInput editorInput = EvaluationViewInput.getInstance();
		IEditorPart editor = workbenchpage.findEditor(editorInput);
		  if(editor!=null){
			  workbenchpage.bringToTop(editor);
          }else {
       	   try {
				editor = workbenchpage.openEditor(editorInput, editorID);
			} catch (PartInitException e1) {
				e1.printStackTrace();
			}
          }
		EvaluationView.section = section;
		EvaluationView.updateLabel(section.getEvaluateNames());
		section.drawCharts(new String[0], Paramater.algorithmsResults, EvaluationView.getComposite(), Paramater.algorithmsColorList);
	}
	
}
