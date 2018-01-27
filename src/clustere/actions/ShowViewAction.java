package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public class ShowViewAction extends Action{
	
	private final IWorkbenchWindow window;
    private final String view1ID = "ClusterE.view1";
    private final String view3ID = "ClusterE.view3";
    private final String view4ID = "ClusterE.view4";
	
	public ShowViewAction(final IWorkbenchWindow window){
		super("&Show View",Action.AS_DROP_DOWN_MENU);
		this.window = window;
		setToolTipText("Show View");
		setImageDescriptor(clustere.Activator.getImageDescriptor("/icons/view.ico"));
		setMenuCreator(new IMenuCreator() {
			public Menu getMenu(Menu parent) {
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Cluster Result view");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
					showView(view1ID);
					}
				});
				MenuItem item3 = new MenuItem(menu,SWT.NONE);
				item3.setText("&Current network");
				item3.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showView(view3ID);
					}
				});
				MenuItem item4 = new MenuItem(menu,SWT.NONE);
				item4.setText("&Using algorithms");
				item4.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showView(view4ID);
					}
				});
				return menu;
			}
			public Menu getMenu(Control parent) {
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Cluster result view");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showView(view1ID);
					}
				});
				MenuItem item3 = new MenuItem(menu,SWT.NONE);
				item3.setText("&Current network");
				item3.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showView(view3ID);
					}
				});
				MenuItem item4 = new MenuItem(menu,SWT.NONE);
				item4.setText("&Using algorithms");
				item4.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						showView(view4ID);
					}
				});
				return menu;
			}
			public void dispose() {
			}
		});
	}
	public void run(){
	}
	public void showView(String id){
		if(window!=null){
			try {
				window.getActivePage().showView(id);
			} catch (PartInitException e1) {
				MessageDialog.openError(window.getShell(), "Error", e1.getMessage());
			}
		}
	}
}
