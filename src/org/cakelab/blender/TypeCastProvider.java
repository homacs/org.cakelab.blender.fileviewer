package org.cakelab.blender;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.cakelab.blender.io.FileVersionInfo;
import org.cakelab.blender.io.FileHeader.Version;

public class TypeCastProvider {
	
	public static enum Category {
		ListBase
	}
	
	
	private String packageName;
	private ClassLoader classLoader;

	
	private Map<String, Map<String, Class<?>>> mappings = new HashMap<>();
	
	
	public TypeCastProvider(String packageName) {
		this.packageName = packageName;
		this.classLoader = TypeCastProvider.class.getClassLoader();
	}
	
	
	
	public void load(File folder) throws IOException {
		File[] files = folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return (f.isFile() && f.getName().endsWith(".properties"));
			}
			
		});
		
		for (File file : files) {
			String category = file.getName().replaceAll("\\.properties", "");
			Map<String, Class<?>> map = new HashMap<>();
			load(map, file);
			mappings.put(category, map);
		}
	}
	
	private void load(Map<String, Class<?>> map, File file) throws IOException {
		Properties content = new Properties();
		FileInputStream in = new FileInputStream(file);
		content.load(in);
		
		for (Entry<Object, Object> entry : content.entrySet()) {
			String name = entry.getKey().toString();
			String typeName = entry.getValue().toString();
			try {
				Class<?> type = classLoader.loadClass(packageName + "." + typeName);
				map.put(name, type);
			} catch (ClassNotFoundException e) {
				System.err.println("Error: " + file.toString() + ": " + name + " = " + typeName);
				e.printStackTrace();
			}
		}
	}
	

	public static File getMatchingFolder(File parent, FileVersionInfo versionInfo) throws FileNotFoundException {
		File[] folders = parent.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		File subfolder = null;
		if (folders != null && folders.length > 0) {
			int maxVersion = versionInfo.getVersion().getCode();
			int minVersion = versionInfo.getMinversion().getCode();
			int subfolderVer = -1;
			for (File folder : folders) {
				try {
					int version = new Version(folder.getName()).getCode();
					if (version <= maxVersion && version >= minVersion && version > subfolderVer) {
						subfolder = folder;
						subfolderVer = version;
					}
				} catch (NumberFormatException e) {
					// not a version directory --> ignore
				}
			}
		}

		if (subfolder == null) 
			throw new FileNotFoundException("No folder for version " + versionInfo.getVersion() + " found in path '" + parent);

		return subfolder;
	}



	public Class<?> getTypeCast(Category category, Class<?> parenttype, String attrib) {
		Map<String, Class<?>> map = mappings.get(category.name());
		if (map != null) {
			return map.get(parenttype.getSimpleName() + "." + attrib);
		}
		return null;
	}


}
