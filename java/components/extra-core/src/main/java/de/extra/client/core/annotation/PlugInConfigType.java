package de.extra.client.core.annotation;

public enum PlugInConfigType {
	
	DataPlugIns("plugins.dataplugin"), DataSource("plugins.datasource"),
	Certificates("plugins.certificates"), DataTransforms("plugins.datatransform"), Contacts("plugins.contacts"),
	OutputPlugIns("plugins.outputplugin");
	
	String configPrefix;
	
	PlugInConfigType(String configPrefix){
		this.configPrefix = configPrefix;
	}
	
	String getConfigPrefix(){
		return configPrefix;
	}
}
