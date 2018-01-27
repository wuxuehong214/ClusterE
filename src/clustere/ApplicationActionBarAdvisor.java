package clustere;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import clustere.actions.DataSourceAction;
import clustere.actions.HelpAction;
import clustere.actions.ImportPluginAction;
import clustere.actions.OpenFileAction;
import clustere.actions.ParamaterSetAction;
import clustere.actions.PluginManageAction;
import clustere.actions.RandomNetworkAction;
import clustere.actions.SaveFileAction;
import clustere.actions.SetLayoutAction;
import clustere.actions.ShowClusterResultAction;
import clustere.actions.ShowEditorAction;
import clustere.actions.ShowViewAction;
import clustere.pluginLoader.LoadManage;
import clustere.pluginLoader.LoaderServer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

//自定义
	private OpenFileAction openFileAction;
	private SaveFileAction saveFileAction;
	private ShowViewAction showViewAction;
	private ShowEditorAction showEditorAction;
//	private SetLayoutAction setLayoutAction;
	private PluginManageAction pluginManageAction;
	private ImportPluginAction importPluginAction;
	private ShowClusterResultAction showClusterResultAction;
	private ParamaterSetAction  paramaterSetAction;
	private DataSourceAction dataSourceAciton;
	private HelpAction helpAction;
	private RandomNetworkAction randomNetworkAction;
//内置
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction prespectiveAction;
	private IContributionItem showVieListAction;
	
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	//自定义
    	openFileAction = new OpenFileAction();
    	saveFileAction = new SaveFileAction(window);
    	showViewAction = new ShowViewAction(window);
    	showEditorAction = new ShowEditorAction(window);
    	pluginManageAction = new PluginManageAction(window);
    	importPluginAction = new ImportPluginAction(window);
    	showClusterResultAction = new ShowClusterResultAction(window);
    	paramaterSetAction = new ParamaterSetAction(window);
    	dataSourceAciton = new DataSourceAction(window);
    	helpAction = new HelpAction(window);
    	randomNetworkAction = new RandomNetworkAction(window);
    	//内置
    	exitAction = ActionFactory.QUIT.create(window);
    	aboutAction = ActionFactory.ABOUT.create(window);
    	prespectiveAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
    	showVieListAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    /*************************File****************************/
    	MenuManager fileMenu = new MenuManager("&File",IWorkbenchActionConstants.M_FILE);
    	fileMenu.add(openFileAction); //打开文件
        fileMenu.add(saveFileAction); //保存文件
    	fileMenu.add(exitAction);
     /*************************View****************************/
    	MenuManager viewMenu = new MenuManager("&View","view");
    	viewMenu.add(showViewAction);
    	viewMenu.add(showEditorAction);
//    	viewMenu.add(setLayoutAction);
    	viewMenu.add(prespectiveAction);
    	viewMenu.add(showVieListAction);
    /*************************Plugin****************************/
    	MenuManager pluginMenu = new MenuManager("&Plugin","plugin");
    	pluginMenu.add(pluginManageAction);
    	pluginMenu.add(importPluginAction);
    /*************************Run****************************/
    	MenuManager runMenu = new MenuManager("&Algorithm","algorithm");
    	runMenu.add(importPluginAction);
    	runMenu.add(new Separator());
    /*************************Evaluation****************************/ 
    	MenuManager evaluationMenu = new MenuManager("&Evaluation","evaluation");
    	evaluationMenu.add(showClusterResultAction);
    	evaluationMenu.add(new Separator());
     /*************************Evaluation****************************/
    	MenuManager toolMenu = new MenuManager("&Tool","tool");
    	toolMenu.add(paramaterSetAction);
    	toolMenu.add(dataSourceAciton);
    	toolMenu.add(randomNetworkAction);
    /*************************Help****************************/
    	MenuManager helpMenu = new MenuManager("&Help","help");
    	helpMenu.add(aboutAction);
    	helpMenu.add(helpAction);
    	fillPluginMenu(runMenu, evaluationMenu);
    	
    	menuBar.add(fileMenu);
    	menuBar.add(viewMenu);
    	menuBar.add(pluginMenu);
    	menuBar.add(runMenu);
    	menuBar.add(evaluationMenu);
    	menuBar.add(toolMenu);
    	menuBar.add(helpMenu);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
    	 IToolBarManager toolbar = new ToolBarManager(coolBar.getStyle());
    	 toolbar.add(openFileAction);        //打开文件
    	 toolbar.add(saveFileAction);			//保存文件
    	 IToolBarManager toolbar4 = new ToolBarManager(coolBar.getStyle());
    	 toolbar4.add(pluginManageAction);         //插件管理
    	 toolbar4.add(importPluginAction);       //导入插件
    	 IToolBarManager toolbar2 = new ToolBarManager(coolBar.getStyle());
    	 toolbar2.add(paramaterSetAction);		//参数设置
    	 toolbar2.add(dataSourceAciton);		//基因注释信息
    	 IToolBarManager toolbar3 = new ToolBarManager(coolBar.getStyle());
    	 toolbar3.add(showViewAction);			//   显示视图
    	 toolbar3.add(showEditorAction);		//显示编辑器
    	 IToolBarManager toolbar5 = new ToolBarManager(coolBar.getStyle());
    	 toolbar5.add(helpAction);
    	 IToolBarManager toolbar6 = new ToolBarManager(coolBar.getStyle());
    	 toolbar6.add(randomNetworkAction);
    	 coolBar.add(toolbar);
    	 coolBar.add(toolbar4);
    	 coolBar.add(toolbar2);
    	 coolBar.add(toolbar3);
    	 coolBar.add(toolbar5);
    	 coolBar.add(toolbar6);
    }
    
    protected void fillStatusLine(IStatusLineManager statusLine){
    	super.fillStatusLine(statusLine);
    	StatusLineContributionItem statusItem = new StatusLineContributionItem("");
    	statusLine.getProgressMonitor();
    	statusItem.setText("Status Message");
    	statusLine.add(statusItem);
    }
    
    protected void fillPluginMenu(MenuManager runMenu,MenuManager evaluationMenu){
    	for(int i=0;i<LoaderServer.pluginList.size();i++){
    		LoadManage.getInstance().addItem(runMenu, evaluationMenu, LoaderServer.pluginList.get(i));
    	}
    }
}
