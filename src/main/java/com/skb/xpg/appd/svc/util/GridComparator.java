package com.skb.xpg.appd.svc.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skb.xpg.appd.svc.xpg.XpgCommon;

public class GridComparator implements Serializable, Comparator<Map<String, Object>> {

	private static final Logger logger = LoggerFactory.getLogger(GridComparator.class.getName());
	
	public final static int DATA_TYPE_STRING = 0;
	public final static int DATA_TYPE_INTEGER = 1;
	
	private String target;	// 정렬대상
	private int dataType;		// 정렬대상의 데이터타입 
	private boolean flag;		// Order 방식 
	
	

	public GridComparator(String target, int dataType, boolean flag) {
		this.target = target;
		this.dataType = dataType;
		this.flag = flag;
	}

	@Override
	public int compare(Map<String, Object> grid0, Map<String, Object> grid1) {
		String v1;
		String v2;
		
		try {
			if(target.equals(XpgCommon.__newest)) { // 최신순
				v1 = (String)grid0.get("dd_disp_approve") + grid0.get("desc_disp_approve"); // 배포승인일+배포승인시간
				v2 = (String)grid1.get("dd_disp_approve") + grid1.get("desc_disp_approve"); // 배포승인일+배포승인시간
			} else {
				v1 = (String) grid0.get(target);
				v2 = (String) grid1.get(target);
			}
		}
		catch(Exception e) {
			if( logger != null){logger.error("Error", e);}
			if( logger != null) {
				logger.error("Error", e);
			}
			
			v1 = "";
			v2 = "";
		}
		
		if(dataType == DATA_TYPE_STRING) {
			return (flag ? v1.compareTo(v2) : v2.compareTo(v1));
		}
		else {
			try {
				int vInt1 = Integer.parseInt(v1.replaceAll(",", ""));
				int vInt2 = Integer.parseInt(v2.replaceAll(",", ""));
				if(vInt1 < vInt2) {
					return (flag ? -1 : 1);
				}
				else if(vInt1 > vInt2) {
					return (flag ? 1 : -1);
				}
				else {
					return 0;
				}
			} catch (NumberFormatException ne) {
				logger.error("Error", ne);
				throw new NumberFormatException();
			}
		}
	}

}