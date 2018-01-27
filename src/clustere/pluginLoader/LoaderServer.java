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
	 * 加载插件
	 */
	public void loadPlugins() {
		pluginManager = ObjectFactory.newInstance().createManager(); // 获取插件管理器
		File pluginsDir = new File("Myplugins"); // 获取插件所在目录 Myplugins
		File[] plugins = pluginsDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar"); // 获取该目录下所有.jar文件
			}});
		try {
			Vector<PluginLocation> temp = new Vector<PluginLocation>();
			PluginLocation[] locations = new PluginLocation[plugins.length];
			for (int i = 0; i < plugins.length; i++) {
				locations[i] = StandardPluginLocation.create(plugins[i]);
				if (locations[i] == null) {
					continue;
				}
				temp.add(locations[i]); // 保留有效插件 过滤不包含Plugin.xml文件的插件
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
	 * 刷新插件中变量
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
