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
        //�˵����ɼ�
        configurer.setShowMenuBar(true);
        //�������ɼ�
        configurer.setShowCoolBar(true);
        //״̬���ɼ�
        configurer.setShowStatusLine(true);
        //�������ȿɼ�
        configurer.setShowProgressIndicator(true);
        //������ͼ�ɼ�
        configurer.setShowFastViewBars(true);
        //͸��ͼ�ɼ�
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
    	//��ʼ��ϵͳ����
    	trayItem = initTaskItem(window);
    	if(trayItem!=null){
    		//�����Զ��巽��  �÷���ʵ�ֽ�������С��������
    		createMinimize();
    		//�����Զ��巽��  ʵ���Ҽ��˵�
    		hookPopupMenu(window);
    	}
    }
    
    //��ʼ��ϵͳ����
    private TrayItem initTaskItem(IWorkbenchWindow window){
    	//������̶���
    	final Tray tray = window.getShell().getDisplay().getSystemTray();
    	//�����������������Ŀ
    	TrayItem trayItem = new  TrayItem(tray,SWT.NONE);
    	//����ϵͳ����ͼ��
    	trayImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/alt_window_16.gif").createImage();
    	//����ͼ��
    	trayItem.setImage(trayImage);
    	//����������ʾ���ı�
    	trayItem.setToolTipText("ClusterE");
    	return trayItem;
    }
    //������С�������̷���
    private void createMinimize(){
    	//ʵ�ֵ���������С��  �����򴰿���С��������
    	window.getShell().addShellListener(new ShellAdapter(){
			public void shellIconified(ShellEvent e) {
				window.getShell().setVisible(false);
			}
    	});
    	//ʵ��˫������ͼ��  �򿪴��ڹ���
    	trayItem.addListener(SWT.DefaultSelection, new Listener(){
			public void handleEvent(Event event) {
				//���shell����
				Shell shell = window.getShell();
				//�жϵ�ǰ�����Ƿ�ɼ�
				if(!shell.isVisible()){
					//������ɼ�  �򽫴�������Ϊ�ɼ�
					shell.setVisible(true);
					window.getShell().setMinimized(false);
				}
			}
    	});
    }
    //���������Ҽ��˵�
    private void hookPopupMenu(final IWorkbenchWindow window){
    	//ʵ���Ҽ�����
    	trayItem.addListener(SWT.MenuDetect, new Listener(){
    		public void handleEvent(Event event){
    			//����˵�������
    			MenuManager trayMenu = new MenuManager();
    			//����˵�
    			Menu menu = trayMenu.createContextMenu(window.getShell());
    			//Ϊ���̴����˵�
    			fillTrayItemAction(trayMenu, window);
    			menu.setVisible(true);
    		}
    		
    	});
    }
    //�Զ��巽�� �������̲˵�
    private void fillTrayItemAction(IMenuManager trayItem,final IWorkbenchWindow window){
    	exitAction = ActionFactory.QUIT.create(window);
    	aboutAction = ActionFactory.ABOUT.create(window);
    	trayItem.add(aboutAction);
    	trayItem.add(exitAction);
    }
    //������Դ
    public void dispose(){
    	if(trayImage!=null){
    		trayImage.dispose();
    		trayItem.dispose();
    	}
    }
    
    /**
     * ��ȡMIPS���ݿ���  ������ע����Ϣ�ļ�
     * ��һ���ļ�  protein�� function code ��ϵ�ļ�
     * �ڶ����ļ� function code��  function name ��ϵ�ļ�
     */
    public void readFileMIPS() {
		/************************* ��ȡ�����ʹ�����Ϣ�ļ�   MIPS*****************************/
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
				+ "������MIPS Protein---Function Code");
		/************************* ��ȡ�����ʹ�����Ϣ�ļ�   MIPS*****************************/
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
				+ "������MIPS Function Code---Function Name");
	}
	
    /**
     * ��ȡ go���ݿ���   ������
     * biological_process        �������
     * cellular_component		ϸ�����
     * molecular_function       ģ�鹦��
     *                  go���ݿ���protein��go:id��ϵ�ļ�
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
					+ "������successfully read file(protein-functionCode):"+filename);
	}
	
	/**
	 * ��ȡgo���ݿ��� go:id�� go:name��ϵ�ļ�
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
//				str = br.readLine();    //��ȡgo:id
//				int index = str.lastIndexOf(':');
//				str = str.substring(index+1);    //ֻ��ȡid���
//				key = str;
//				str = br.readLine();         //��ȡgo:id��Ӧ������
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
				+ "������successfully read file(functionCode--functionName):"+filename);
	}
}
