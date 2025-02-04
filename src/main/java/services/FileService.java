package services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import components.ResourceModel;
import utilities.GeneralUtilities;

public class FileService {
	
	private static final Logger logger = LogManager.getLogger(FileService.class);
	private String selectedResourcesBasePath;
	
	public FileService() {}
	
	/**
	 * Sorts the files into a map of lists, where the key is the directory, and the list is all the files under that directory. 
	 * We use a tree list, as it sorts the keys, and the base directory (which may have some loose files) will thus be the first key in the map. 
	 * @param resources
	 * @return
	 */
	public Map<String, List<ResourceModel>> organizeResources(List<ResourceModel> resources) {
		Map<String, List<ResourceModel>> result = new TreeMap<>();
		for(ResourceModel model : resources) {
			String path = model.getFile().getPath();
			String target = File.separator;
			int endIndex = path.lastIndexOf(target);
			if(endIndex < 0) {
				logger.error("ERROR with path: " + path);
				continue;
			}
			String parentPath = path.substring(0, endIndex);
			List<ResourceModel> list;
			if(result.containsKey(parentPath)) {
				list = result.get(parentPath);
			}else {
				list = new ArrayList<>();
				result.put(parentPath, list);
			}
				
			list.add(model);
		}
		
		String firstKey = result.keySet().iterator().next();
		selectedResourcesBasePath = firstKey;
		return result;
	}

	public void renameDirectories(Map<String, List<ResourceModel>> data) {
		//invert the keyset so that we rename the most deeply nested directories first. 
		Set<String> keys = data.keySet();
		String[] invertedKeys = invertKeys(keys);
		for(String key : invertedKeys) {
			List<ResourceModel> list = data.get(key);
			for(ResourceModel model : list) {
				if(model.getFile().isDirectory()) {
					if(model.getFile().getName().equals(model.getUserProvidedName()) == false) {								
						try {
							String dirName = key.replace(selectedResourcesBasePath, GeneralUtilities.getResourceSavePath(false));
							String currentName = dirName + File.separator + model.getFile().getName();
							String newName = dirName + File.separator + model.getUserProvidedName();
							Path source = Paths.get(currentName);
							Files.move(source, source.resolveSibling(newName));
						}catch(Exception e) {
							logger.error("error renaming directories", e);
						}
					}
				}
			}
		}
	}
	
	private String[] invertKeys(Set<String> keys) {
		int keysetSize = keys.size();
		String[] result = new String[keysetSize];				
		for(String key : keys) {
			result[(keysetSize - 1)] =  key;
			keysetSize--;
		}
		return result;
	}
	
	/**
	 * Moves the files in the list, to the resources directory. 
	 * If it moves a directory, it will move all files in the directory as well. 
	 * The moved files will be renamed if necessary with the names provided by the user. 
	 * @param key
	 * @param list
	 */
	public void moveFilesAtSelectedRoot(List<ResourceModel> list) {
		try {
			String savePath = GeneralUtilities.getResourceSavePath(false);
			Path filePath = Paths.get(savePath);
			Files.createDirectories(filePath);
			
			for(ResourceModel model : list) {
				if(model.getFile().isDirectory()) {
					//don't rename directories here. We will handle that later. 
					File renamedFile = new File(savePath + File.separator + model.getFile().getName());
					model.getFile().renameTo(renamedFile);
				}else {
					File renamedFile = new File(savePath + File.separator + model.getUserProvidedName());
					model.getFile().renameTo(renamedFile);
				}
				
			}
		}catch(Exception error) {
			logger.error("Error moving resource ", error);
		}
		
	}
	
	/**
	 * copies the files in the list, to the resources directory. 
	 * If it moves a directory, it will move all files in the directory as well. 
	 * The moved files will be renamed if necessary with the names provided by the user. 
	 * @param key
	 * @param list
	 */
	public void copyFilesAtSelectedRoot(List<ResourceModel> list) {
		try {
			String savePath = GeneralUtilities.getResourceSavePath(false);
			Path filePath = Paths.get(savePath);
			Files.createDirectories(filePath);
			
			for(ResourceModel model : list) {
				if(model.getFile().isDirectory()) {
					//don't rename directories here. We will handle that later. 
					File newPath = new File(savePath + File.separator + model.getFile().getName());
					FileUtils.copyDirectory(model.getFile(), newPath);
//					Files.copy(model.getFile().toPath(), newPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}else {
					File newPath = new File(savePath + File.separator + model.getUserProvidedName());
					Files.copy(model.getFile().toPath(), newPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				
			}
		}catch(Exception error) {
			logger.error("Error moving resource ", error);
		}
		
	}

	/**
	 * Enters the directory in the new location, and ensures all filenames, match the user provided names. 
	 * 
	 * @param key
	 * @param list
	 */
	public void renameFilesInNestedDirectories(String key, List<ResourceModel> list) {
		try {
			String dirName = key.replace(selectedResourcesBasePath, GeneralUtilities.getResourceSavePath(false));
			File directory = new File(dirName);
			File[] existingFiles = directory.listFiles();
			if(existingFiles != null) {
				for(File file : existingFiles) {
					if(file.isDirectory()) {
						//don't touch directories. That will be handled later. 
						continue;
					}
					for(ResourceModel model : list) {
						if(file.getName().equals(model.getFile().getName())) {
							String newName = file.getParent();
							newName = newName + File.separator + model.getUserProvidedName();
							file.renameTo(new File(newName));
						}
					}
				}
			}					
		}catch(Exception error) {
			logger.error("Error moving resource ", error);
		}
		
	}
}
