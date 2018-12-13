package com.skb.xpg.nxpg.svc.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.StrUtil;

@Service
public class InsideService {

	@Autowired
	private RedisClient redisClient;
	
	//IF-NXPG-019
	@SuppressWarnings("unchecked")
	public void getInsideInfo(Map<String, Object> rtn, Map<String, String> param) {
		String epsd_id = "", prs_id = "", epsd_rslu_id = "";
		int tmtag_tmsc = 0;
		List<Map<String, Object>> info = null;
		Map<String, Object> data = null;
		Map<String, Object> epsd_id_info = null;
		
		epsd_id = param.get("epsd_id");
		epsd_rslu_id = param.get("epsd_rslu_id");
		if("".equals(epsd_id) || epsd_id.isEmpty()) {	
			//에피소드 아이디가 빈값이면 해상도아이디로 넘어온 것이므로 에피소드 아이디를 찾아낸다.
			if(!"".equals(epsd_rslu_id) && !epsd_rslu_id.isEmpty()){
				epsd_id_info = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_CIDINFO, epsd_rslu_id, param));
				epsd_id = (String) epsd_id_info.get("epsd_id");
			}
		}
		
		prs_id = param.get("prs_id");
		tmtag_tmsc = CastUtil.getStrToInt(param.get("tmtag_tmsc"));
		
		data = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.INSIDE_INFO, epsd_id, param));
		
		// 조회값 없음
//		if (data == null) {
//			info = CastUtil.StringToJsonListMap(redisClient.hget(NXPGCommon.INSIDE_INFO, epsd_id, param));
//			rtn.put("result", "9998");
//			return;
//		}
		
		// 조회값 없음
		if (data == null) {
			rtn.put("result", "9998");
			return;
		}
		///////////////////////////
		// 인물 필터링
		// prs_id가 없으면 그냥 넘어간다.
//		if (data != null && !StrUtil.isEmpty(prs_id)) {
//			for (Iterator<Map<String,Object>> iterator = info.iterator(); iterator.hasNext();) {
//				Map<String, Object> map = CastUtil.getObjectToMap(iterator.next());
//				List<Map<String, Object>> peopleScenes = CastUtil.getObjectToMapList(map.get("people_scenes"));
//				
//				// people_scenes가 없으면 넘어간다.
//				if (peopleScenes == null) continue;
//				
//				for (Iterator<Map<String,Object>> psIterator = peopleScenes.iterator(); psIterator.hasNext();) {
//					Map<String, Object> peopleScenesMap = CastUtil.getObjectToMap(psIterator.next());
//					String tempPrsId = CastUtil.getObjectToString(peopleScenesMap.get("prs_id"));
//					
//					// prs_id 비교 후 다르면 삭제한다.
//					if (!prs_id.equals(tempPrsId)) {
//						psIterator.remove();
//					}
//				}
//			}
//		}
		///////////////////////////
		
		///////////////////////////
		// 시간 필터링
//		if (tmtag_tmsc > 0) {
//			for (Iterator<Map<String,Object>> iterator = data.iterator(); iterator.hasNext();) {
//				Map<String, Object> map = CastUtil.getObjectToMap(iterator.next());
//				List<Map<String, Object>> peopleScenes = CastUtil.getObjectToMapList(map.get("people_scenes"));
//				
//				// people_scenes가 없으면 넘어간다.
//				if (peopleScenes == null) continue;
//				
//				for (Iterator<Map<String,Object>> psIterator = peopleScenes.iterator(); psIterator.hasNext();) {
//					Map<String, Object> peopleScenesMap = CastUtil.getObjectToMap(psIterator.next());
//					List<Map<String, Object>> personScenes = CastUtil.getObjectToMapList(peopleScenesMap.get("person_scenes"));
//					
//					// person_scenes가 없으면 넘어간다.
//					if (personScenes == null) continue;
//
//					for (Iterator<Map<String,Object>> personIterator = personScenes.iterator(); personIterator.hasNext();) {
//						Map<String, Object> personScenesMap = CastUtil.getObjectToMap(personIterator.next());
//						int frtmsc = 0, totmsc = 0;
//						
//						if (personScenesMap.get("tmtag_fr_tmsc") != null)
//							frtmsc = CastUtil.getStrToInt(personScenesMap.get("tmtag_fr_tmsc").toString());
//						if (personScenesMap.get("tmtag_to_tmsc") != null)
//							totmsc = CastUtil.getStrToInt(personScenesMap.get("tmtag_to_tmsc").toString());
//						
//						// frtmsc보다 작거나 totmsc보다 크면 삭제 
//						if (frtmsc > tmtag_tmsc || totmsc < tmtag_tmsc) {
//							personIterator.remove();
//							continue;
//						}
//					}
//				}
//			}
//		}
		///////////////////////////
		
		rtn.put("result", "0000");
		rtn.put("inside_info", data);
	}
}
