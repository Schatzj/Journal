package components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrganizedResourcesModel {
	Map<String, List<ResourceModel>> result = new TreeMap<>();
	Map<String, String> keyUserName = new HashMap<>();
	
	public OrganizedResourcesModel() {}

	public Map<String, List<ResourceModel>> getResult() {
		return result;
	}

	public void setResult(Map<String, List<ResourceModel>> result) {
		this.result = result;
	}

	public Map<String, String> getKeyUserName() {
		return keyUserName;
	}

	public void setKeyUserName(Map<String, String> keyUserName) {
		this.keyUserName = keyUserName;
	}
	
	public void addKeyUserName(String key, String userGivenName) {
		if(keyUserName.containsKey(key)) {
			String currentname = keyUserName.get(key);
			currentname = userGivenName;
		}else {
			keyUserName.put(key, userGivenName);
		}
	}
	
}
