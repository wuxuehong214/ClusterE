package clustere.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.EditorPart;

import com.wuxuehong.interfaces.GraphInfo;

import clustere.editors.ClusterEditor;
import clustere.editors.NetworkView;
import clustere.layout.ClusterLayout;

public class SetLayoutAction extends Action {
	
	private int style ;
	private EditorPart editor;
	
	public SetLayoutAction(EditorPart editor ,int style){
		super("&Set Layout",Action.AS_DROP_DOWN_MENU);
		this.style = style;
		this.editor = editor;
		setToolTipText("Set Layout");
		setImageDescriptor(clustere.Activator.getImageDescriptor("/icons/layout.ico"));
		setMenuCreator(new IMenuCreator() {
			@Override
			public Menu getMenu(Menu parent) {
				// TODO Auto-generated method stub
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Rectangle");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						setLayout(ClusterLayout.RECTANGLE);
					}
				});
				MenuItem item2 = new MenuItem(menu,SWT.NONE);
				item2.setText("&Oval");
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						setLayout(ClusterLayout.OVAL);
					}
				});
				MenuItem item3 = new MenuItem(menu,SWT.NONE);
				item3.setText("&Star-type");
				item3.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						setLayout(ClusterLayout.STAR);
					}
				});
				return menu;
			}
			
			@Override
			public Menu getMenu(Control parent) {
				// TODO Auto-generated method stub
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Rectangle");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						setLayout(ClusterLayout.RECTANGLE);
					}
				});
				MenuItem item2 = new MenuItem(menu,SWT.NONE);
				item2.setText("&Oval");
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						setLayout(ClusterLayout.OVAL);
					}
				});
				MenuItem item3 = new MenuItem(menu,SWT.NONE);
				item3.setText("&Star-type");
				item3.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						setLayout(ClusterLayout.STAR);
					}
				});
				return menu;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
			}
		});
	}
	
	
	/**
	 * 完整重新布局
	 * 如果style ==1 表示当前是ClusterEditor显示窗口
	 * 否则  表示当前是完整网络显示窗口
	 * @param layout
	 */
	public void setLayout(int layout){
		ClusterLayout.LAYOUT_STYLE = layout;
		if(style==1){
	System.out.println("是指的此时");
			ClusterEditor clusterEditor = (ClusterEditor)editor;
			ClusterLayout.setLayout(ClusterLayout.LAYOUT_STYLE, clusterEditor.getNodes(), clusterEditor.getCanvas());
		}else {
			NetworkView networkView = (NetworkView)editor;
			ClusterLayout.setLayout(ClusterLayout.LAYOUT_STYLE, GraphInfo.nodelist, networkView.getCanvas());
		}
	}
	
	public void run(){
	}

}
