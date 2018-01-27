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
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import com.wuxuehong.bean.Paramater;

import clustere.editors.ClusterEditorInput;
import clustere.editors.EvaluationViewInput;

public class ClusterShowWayAction extends Action {

	public ClusterShowWayAction(){
		super("&Show Cluster",Action.AS_DROP_DOWN_MENU);
		ImageDescriptor imgdes = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_RESTORE_TRIMPART);
		setImageDescriptor(imgdes);
		setMenuCreator(new IMenuCreator(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Menu getMenu(Control parent) {
				Menu menu = new Menu(parent);
				final MenuItem item1 = new MenuItem(menu,SWT.CHECK);
				final MenuItem item2 = new MenuItem(menu,SWT.CHECK);
				item1.setText("&Show Cluster");
				item2.setText("&Show Overlap");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
					Paramater.CLUSTER_SHOW_MODE = Paramater.CLUSTER;
					item1.setSelection(true);
					item2.setSelection(false);
					}
				});
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						Paramater.CLUSTER_SHOW_MODE = Paramater.OVERLAP;
						item1.setSelection(false);
						item2.setSelection(true);
					}
				});
				return menu;
			}

			@Override
			public Menu getMenu(Menu parent) {
				// TODO Auto-generated method stub
				Menu menu = new Menu(parent);
				final MenuItem item1 = new MenuItem(menu,SWT.CHECK);
				final MenuItem item2 = new MenuItem(menu,SWT.CHECK);
				item1.setText("&Show Cluster");
				item2.setText("&Show Overlap");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
					Paramater.CLUSTER_SHOW_MODE = Paramater.CLUSTER;
					item1.setSelection(true);
					item2.setSelection(false);
					}
				});
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						Paramater.CLUSTER_SHOW_MODE = Paramater.OVERLAP;
						item1.setSelection(false);
						item2.setSelection(true);
					}
				});
				return menu;
			}
			
		});
	}
}
