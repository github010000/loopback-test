package com.skb.xpg.nxpg.svc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.skb.xpg.nxpg.svc.common.NXPGCommon;
import com.skb.xpg.nxpg.svc.config.Properties;
import com.skb.xpg.nxpg.svc.redis.RedisClient;
import com.skb.xpg.nxpg.svc.util.CastUtil;
import com.skb.xpg.nxpg.svc.util.DateUtil;

@Service
public class MakeCWService {
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private GridService gridService;
	
	@Autowired
	private Properties properties;
	
	@Async("fixedThreadPool")
	public ListenableFuture<List<Map<String, Object>>> makeCwGridMap(String[] idNblockId, Map<String, Object> gridData, Map<String, String> param) {
//		int redisCnt = 0;
		List<Map<String, Object>> resultGridList = new ArrayList<Map<String, Object>>();
		String epsd_rslu_id = idNblockId[0];
		String trackId = idNblockId[1];
		
		Map<String, Object> cidInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_CIDINFO, epsd_rslu_id, param));
//		redisCnt++;
		if(cidInfo != null) {
			String sris_id = CastUtil.getObjectToString(cidInfo.get("sris_id"));
			String epsd_id = CastUtil.getObjectToString(cidInfo.get("epsd_id"));
			Map<String, Object> gridInfo = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.GRID_CONTENTS_ITEM, epsd_id, param));
//			redisCnt++;
			if(gridInfo != null && !gridInfo.isEmpty()) {
			
				gridService.checkBadge(gridInfo);

				gridData.putAll(gridInfo);
				gridData.put("track_id", trackId);
				gridData.put("epsd_rslu_id", epsd_rslu_id);
				resultGridList.add(gridData);
			} else {
				
				Map<String, Object> sris = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_CONTENTS, sris_id, param));
//				redisCnt++;
				Map<String, Object> purchares = null;
				Map<String, Object> epsd = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.SYNOPSIS_SRISINFO, epsd_id, param));
//				redisCnt++;
				if(sris != null && epsd != null) {
					Map<String, Object> cwGridMap = CastUtil.getObjectToMap(properties.getCw().get("grid"));
					if (cwGridMap != null) {
						gridData.putAll(cwGridMap);
					}
				
					gridData.put("poster_filename_h", sris.get("sris_poster_filename_h"));
					gridData.put("sris_id", sris.get("sris_id"));
					gridData.put("poster_filename_v", sris.get("sris_poster_filename_v"));
					gridData.put("epsd_id", epsd.get("epsd_id"));
					gridData.put("wat_lvl_cd", epsd.get("wat_lvl_cd"));
					gridData.put("adlt_lvl_cd", epsd.get("adlt_lvl_cd"));
					gridData.put("title", sris.get("title"));				
					gridData.put("trackId", trackId);	
					gridData.put("synon_typ_cd", (sris.get("sris_typ_cd").equals("01") ? "02" : "01"));
					gridData.put("meta_typ_cd", sris.get("meta_typ_cd"));
					gridData.put("rslu_typ_cd", epsd.get("rslu_typ_cd"));
					gridData.put("kids_yn", sris.get("kids_yn"));
					gridData.put("cacbro_yn", epsd.get("cacbro_yn"));
					gridData.put("epsd_rslu_id", epsd_rslu_id);
					resultGridList.add(gridData);
					
					purchares = CastUtil.StringToJsonMap(redisClient.hget(NXPGCommon.CONTENTS_PURCHARES, sris_id, param));
//					redisCnt++;
					
					List<Map<String, Object>> products = CastUtil.getObjectToMapList(sris.get("products"));
					DateUtil.getCompare(products, "prd_prc_fr_dt", "purc_wat_to_dt", false);
					
					if (products != null && products.size() > 0) {
						for (Map<String, Object> p : products) {
							gridData.put("sale_prc", p.get("sale_prc"));
							break;
						}
					} else {

						if (purchares != null && purchares.size() > 0) {
							List<Map<String, Object>> purchares_products = CastUtil.getObjectToMapList(purchares.get("products"));
							DateUtil.getCompare(purchares_products, "prd_prc_fr_dt", "purc_wat_to_dt", false);
							
							for (Map<String, Object> pp : purchares_products) {
								gridData.put("sale_prc", pp.get("sale_prc"));
								break;
							}
						}
					}
					
				} else {
//					LogUtil.info("", "", "", "", "CW", "CONTENTS NULL : " + epsd_id);
					return AsyncResult.forValue(resultGridList);
				}


				gridService.checkBadge(gridData);
			}
		}
		DateUtil.getCompare(resultGridList, "svc_fr_dt", "svc_to_dt", true);
		
		return AsyncResult.forValue(resultGridList);
	}
	
}
