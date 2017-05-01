package civ.util;

import java.util.Properties;

public class CProperties {

	private Properties _properties;

	private static CProperties _instance;
	private CProperties() {
		// TODO: read properties from file
	}
	public static CProperties getInstance() {
		if (_instance == null) {
			_instance = new CProperties();
		}
		return _instance;
	}

	public int getPropertyAsInt(String key) {
		return Integer.parseInt(_properties.getProperty(key));
	}

//	public Class<?> getPropertyAsClass(String key) {
//		try {
//			return Class.forName(_properties.getProperty(key));
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public String getProperty(String key) {
		return _properties.getProperty(key);
	}
}
