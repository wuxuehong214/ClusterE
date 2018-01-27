package clustere;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.wuxuehong.bean.Paramater;

import clustere.pluginLoader.LoaderServer;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	private TrayItem trayItem;
	
	private Image trayImage;
	
	private IWorkbenchWindow window;
	
	private IWorkbenchAction aboutAction;
	
	private IWorkbenchAction exitAction;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1000, 700));
        //菜单栏可见
        configurer.setShowMenuBar(true);
        //工具栏可见
        configurer.setShowCoolBar(true);
        //状态栏可见
        configurer.setShowStatusLine(true);
        //工作进度可见
        configurer.setShowProgressIndicator(true);
        //快速视图可见
        configurer.setShowFastViewBars(true);
        //透视图可见
        configurer.setShowPerspectiveBar(true);
        IPreferenceStore preStore = PrefUtil.getAPIPreferenceStore();
        preStore.setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR, IWorkbenchPreferenceConstants.TOP_RIGHT);
        preStore.setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
        configurer.setTitle("ClusterE");
        new LoaderServer();
        readFileMIPS();
        OpenFileGo("ComplexFunction/Go.Function", Paramater.goFuncitonAnnotation);
        OpenFileGo("ComplexFunction/Go.Process", Paramater.goProcessAnnotation);
        OpenFileGo("ComplexFunction/Go.Component", Paramater.goComponentAnnotation);
        OpenFileGoAnnotation("ComplexFunction/GO.annotation");
    }
    
    public void postWindowOpen(){
    	super.postWindowOpen();
    	window = getWindowConfigurer().getWindow();
    	//初始化系统托盘
    	trayItem = initTaskItem(window);
    	if(trayItem!=null){
    		//调用自定义方法  该方法实现将界面最小化到托盘
    		createMinimize();
    		//调用自定义方法  实现右键菜单
    		hookPopupMenu(window);
    	}
    }
    
    //初始化系统托盘
    private TrayItem initTaskItem(IWorkbenchWindow window){
    	//获得托盘对象
    	final Tray tray = window.getShell().getDisplay().getSystemTray();
    	//向托盘中添加托盘项目
    	TrayItem trayItem = new  TrayItem(tray,SWT.NONE);
    	//定义系统托盘图表
    	trayImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/alt_window_16.gif").createImage();
    	//设置图标
    	trayItem.setImage(trayImage);
    	//设置托盘提示性文本
    	trayItem.setToolTipText("ClusterE");
    	return trayItem;
    }
    //设置最小化到托盘方法
    private void createMinimize(){
    	//实现单击界面最小化  将程序窗口最小化到托盘
    	window.getShell().addShellListener(new ShellAdapter(){
			public void shellIconified(ShellEvent e) {
				window.getShell().setVisible(false);
			}
    	});
    	//实现双机托盘图标  打开窗口功能
    	trayItem.addListener(SWT.DefaultSelection, new Listener(){
			public void handleEvent(Event event) {
				//获得shell对象
				Shell shell = window.getShell();
				//判断当前窗口是否可见
				if(!shell.isVisible()){
					//如果不可见  则将窗口设置为可见
					shell.setVisible(true);
					window.getShell().setMinimized(false);
				}
			}
    	});
    }
    //设置托盘右键菜单
    private void hookPopupMenu(final IWorkbenchWindow window){
    	//实现右键监听
    	trayItem.addListener(SWT.MenuDetect, new Listener(){
    		public void handleEvent(Event event){
    			//定义菜单管理器
    			MenuManager trayMenu = new MenuManager();
    			//定义菜单
    			Menu menu = trayMenu.createContextMenu(window.getShell());
    			//为托盘创建菜单
    			fillTrayItemAction(trayMenu, window);
    			menu.setVisible(true);
    		}
    		
    	});
    }
    //自定义方法 创建托盘菜单
    private void fillTrayItemAction(IMenuManager trayItem,final IWorkbenchWindow window){
    	exitAction = ActionFactory.QUIT.create(window);
    	aboutAction = ActionFactory.ABOUT.create(window);
    	trayItem.add(aboutAction);
    	trayItem.add(exitAction);
    }
    //销毁资源
    public void dispose(){
    	if(trayImage!=null){
    		trayImage.dispose();
    		trayItem.dispose();
    	}
    }
    
    /**
     * 读取MIPS数据库中  蛋白质注释信息文件
     * 第一个文件  protein到 function code 关系文件
     * 第二个文件 function code到  function name 关系文件
     */
    public void readFileMIPS() {
		/************************* 读取蛋白质功能信息文件   MIPS*****************************/
		File f = new File("ComplexFunction/MIPS.protein");
		FileReader fr = null;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String str = null;
		Scanner s = null;
		Set<String> v = null;
		try {
			str = br.readLine();
			while (str != null) {
				str = str.toUpperCase();
				s = new Scanner(str);
				if (!s.hasNext()) {
					str = br.readLine();
					continue;
				}
				String str1 = s.next();
				String str2 = s.next();
				if (Paramater.proteinFunction.get(str1) != null) {
					Paramater.proteinFunction.get(str1).add(str2);
				} else {
					v = new HashSet<String>();
					v.add(str2);
					Paramater.proteinFunction.put(str1, v);
				}
				str = br.readLine();
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Paramater.proteinFunction.size()
				+ "　　　MIPS Protein---Function Code");
		/************************* 读取蛋白质功能信息文件   MIPS*****************************/
		File f2 = new File("ComplexFunction/MIPS.annotation");
		FileReader fr2 = null;
		try {
			fr2 = new FileReader(f2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br2 = new BufferedReader(fr2);
		try {
			str = br2.readLine();
			while (str != null) {
				s = new Scanner(str);
				if (!s.hasNext()) {
					str = br2.readLine();
					continue;
				}
				String str1 = s.next();
				String str2 = s.next();
				int index = str.indexOf(str2);
				str2 = str.substring(index, str.length());
				Paramater.functionAnnotation.put(str1, str2);
				str = br2.readLine();
			}
			br2.close();
			fr2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Paramater.functionAnnotation.size()
				+ "　　　MIPS Function Code---Function Name");
	}
	
    /**
     * 读取 go数据库中   三大本体
     * biological_process        生物过程
     * cellular_component		细胞组件
     * molecular_function       模块功能
     *                  go数据库中protein到go:id关系文件
     * @param filename
     * @param result
     */
	public void OpenFileGo(String filename,HashMap<String,Set<String>> result){
		try{
			BufferedReader br3 = new BufferedReader(new FileReader(new File(filename)));
			String str = br3.readLine();
			Scanner scanner = null;
			Scanner s = null;
			while(str!=null){
				str = str.toUpperCase();
				s = new Scanner(str);
				String ss = s.next();
				String id = s.next();
				scanner = new Scanner(ss);
				scanner.useDelimiter("\\|");
				while(scanner.hasNext()){
					String key = scanner.next();
					if(result.get(key)!=null)
						result.get(key).add(id);
					else {
						Set<String>  vv = new HashSet<String>();
						vv.add(id);
						result.put(key, vv);
					}
				}
			    str = br3.readLine();
			}
			}catch (Exception e){
				e.printStackTrace();
				MessageDialog.openError(new Shell(), "Error", filename+" read fail");
			}
			System.out.println(result.size()
					+ "　　　successfully read file(protein-functionCode):"+filename);
	}
	
	/**
	 * 读取go数据库中 go:id到 go:name关系文件
	 */
	public void OpenFileGoAnnotation(String filename){
		try{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("goAnnotation")));
		String str = null;
		String key = null;
		Scanner scanner = null;
		str = br.readLine();
		while(str!=null){
//			if(str.equals("[Term]")){
//				str = br.readLine();    //读取go:id
//				int index = str.lastIndexOf(':');
//				str = str.substring(index+1);    //只获取id编号
//				key = str;
//				str = br.readLine();         //读取go:id对应的名字
//				index = str.indexOf(':');
//				str = str.substring(index+1);
//				Paramater.goGeneAnnotation.put(key, str);
//				bw.write(key);
//				bw.write(str);
//				bw.newLine();
//			}else {
//				str = br.readLine();
//			}
			scanner = new Scanner(str);
			key = scanner.next();
			str = scanner.next();
			Paramater.goGeneAnnotation.put(key, str);
			str = br.readLine();
		}
//		bw.close();
		br.close();
		}catch(Exception e){
			e.printStackTrace();
			MessageDialog.openError(new Shell(), "Error", filename+" read fail");
		}
		System.out.println(Paramater.goGeneAnnotation.size()
				+ "　　　successfully read file(functionCode--functionName):"+filename);
	}
}
