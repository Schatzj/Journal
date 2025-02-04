package components;

import java.io.File;

public class ResourceModel {
	private String userProvidedName; 
	private File file;
	private String parentDirectoryPath;
	
	public ResourceModel() {}
	
	public ResourceModel(String userProvidedName, File file) {
		super();
		this.userProvidedName = userProvidedName;
		this.file = file;
	}
	
	public String getUserProvidedName() {
		return userProvidedName;
	}
	public void setUserProvidedName(String userProvidedName) {
		this.userProvidedName = userProvidedName;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	public String getParentDirectoryPath() {
		return parentDirectoryPath;
	}

	public void setParentDirectoryPath(String parentDirectoryPath) {
		this.parentDirectoryPath = parentDirectoryPath;
	}
	
	
}
