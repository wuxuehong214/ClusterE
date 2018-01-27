package clustere.pluginLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.runtime.adaptor.LocationManager;
import org.eclipse.osgi.service.datalocation.Location;
import org.java.plugin.ObjectFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.PluginLocation;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.standard.StandardPluginLocation;

import com.wuxuehong.bean.Paramater;
import com.wuxuehong.bean.PluginVo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class LoaderServer {

	private PluginManager pluginManager;
	public static Vector<NewAlgorithm> pluginList = new Vector<NewAlgorithm>();
	public static ArrayList<PluginVo> PluginInfo = new ArrayList<PluginVo>();

	public LoaderServer() {
		loadPlugins();
	}

	/**
	 * ���ز��
	 */
	public void loadPlugins() {
		pluginManager = ObjectFactory.newInstance().createManager(); // ��ȡ���������
		File pluginsDir = new File("Myplugins"); // ��ȡ�������Ŀ¼ Myplugins
		File[] plugins = pluginsDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar"); // ��ȡ��Ŀ¼������.jar�ļ�
			}});
		try {
			Vector<PluginLocation> temp = new Vector<PluginLocation>();
			PluginLocation[] locations = new PluginLocation[plugins.length];
			for (int i = 0; i < plugins.length; i++) {
				locations[i] = StandardPluginLocation.create(plugins[i]);
				if (locations[i] == null) {
					continue;
				}
				temp.add(locations[i]); // ������Ч��� ���˲�����Plugin.xml�ļ��Ĳ��
			}
			locations = new PluginLocation[temp.size()];
			for (int i = 0; i < temp.size(); i++) {
				locations[i] = temp.get(i);
			}
			pluginManager.publishPlugins(locations);
			Iterator it = pluginManager.getRegistry().getPluginDescriptors()
					.iterator();
			while (it.hasNext()) {
				PluginDescriptor p = (PluginDescriptor) it.next();
				NewAlgorithm section = (NewAlgorithm) pluginManager.getPlugin(p
						.getId());
				section.variableInit();
				pluginList.add(section);
				PluginInfo.add(new PluginVo(p, section));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ˢ�²���б���
	 */
	public static void pluginRefresh() {
		for (int i = 0; i < pluginList.size(); i++) {
			pluginList.get(i).variableInit();
		}
	}

	public static void main(String args[]) {
		new LoaderServer();
	}
}
