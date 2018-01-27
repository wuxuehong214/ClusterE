package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import clustere.Activator;
import clustere.editors.ClusterEditorInput;
import clustere.editors.EvaluationViewInput;
import clustere.editors.NetworkViewInput;

public class ShowEditorAction extends Action {
	
	private  IWorkbenchWindow window;
    private final String view1ID = "ClusterE.editor1";
    private final String view2ID = "ClusterE.editor2";
    private final String view3ID = "ClusterE.editor3";
	
	public ShowEditorAction(IWorkbenchWindow window){
		super("&Show Editor",Action.AS_DROP_DOWN_MENU);
		this.window = window;
		ImageDescriptor imgdes = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_NEW_PAGE);
		setImageDescriptor(imgdes);
		setMenuCreator(new IMenuCreator(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Menu getMenu(Control parent) {
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Cluster Editor");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
					showEditor(view1ID,ClusterEditorInput.getInstance());
					}
				});
				MenuItem item2 = new MenuItem(menu,SWT.NONE);
				item2.setText("&Evaluation Editor");
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showEditor(view2ID,EvaluationViewInput.getInstance());
					}
				});
				MenuItem item3 = new MenuItem(menu,SWT.NONE);
				item3.setText("&Network Editor");
				item3.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showEditor(view3ID,NetworkViewInput.getInstance());
					}
				});
				return menu;
			}

			@Override
			public Menu getMenu(Menu parent) {
				// TODO Auto-generated method stub
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Cluster Editor");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
					showEditor(view1ID,ClusterEditorInput.getInstance());
					}
				});
				MenuItem item2 = new MenuItem(menu,SWT.NONE);
				item2.setText("&Evaluation Editor");
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showEditor(view2ID,EvaluationViewInput.getInstance());
					}
				});
				MenuItem item3 = new MenuItem(menu,SWT.NONE);
				item3.setText("&Network Editor");
				item3.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showEditor(view3ID,NetworkViewInput.getInstance());
					}
				});
				return menu;
			}
			
		});
	}
	
	public void showEditor(String id,IEditorInput input){
		  IWorkbenchPage workbenchPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		  IEditorPart editor = workbenchPage.findEditor(input);
          if(editor!=null){
       	   workbenchPage.bringToTop(editor);
          }else {
       	   try {
				editor = workbenchPage.openEditor(input, id);
			} catch (PartInitException e1) {
				e1.printStackTrace();
			}
          }
	}

}
