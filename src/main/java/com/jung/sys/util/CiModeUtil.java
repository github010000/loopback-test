package com.jung.sys.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class CiModeUtil {
	public static String cimodeCheck;
	
	@Value("${user.cimodeCheck}")
    private void setCimodeCheck(String cimodeCheck){
		CiModeUtil.cimodeCheck = cimodeCheck;
    }

	public static List<Map<String, Object>> getPrdFilter(List<Map<String, Object>> data) {
		Map<String, Object> temp = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<>();
		
		if(data != null || !data.isEmpty()) {
			for(int i=0; i < data.size(); i++ ) {
				temp = data.get(i);
				if (temp != null && temp.containsKey("prd_typ_cd")) {
					
					String prd_typ_cd = temp.get("prd_typ_cd") + "";
					
					if(cimodeCheck.contains(prd_typ_cd)) {
						continue;
					} else {
						result.add(temp);
					}
				}
			}
		} 
		
		return result;
	}
	
	public static Map<String, Object> getFilter(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		data.put("sale_prc_vat", null);
		data.put("sale_prc", null);
		data.put("prd_prc_vat", null);
		data.put("prd_prc", null);
		data.put("prd_prc_id", "");
		data.put("asis_prd_typ_cd", "");
		data.put("prd_typ_cd", "");
		
		return result;
	}
}
