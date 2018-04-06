package com.skb.xpg.appd.svc.util;

import com.skb.xpg.appd.svc.redis.RedisClient;
import com.skb.xpg.appd.svc.xpg.XpgCommon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResponseUtil {
	private final static Logger logger = LoggerFactory.getLogger(ResponseUtil.class.getName());

	@Autowired
	private RedisClient redisClient;
	
	@Value("${server.image.url}")
	private String serverImageUrl;
	
  	//	관련 인물 정보(스포츠그룹) 컨텐츠 가져오기
	public Map<String, Object> getXpgSportGroupRefVO(String key, String id_package) throws Exception{
		List<Map<String, Object>> series = new ArrayList<Map<String,Object>>();
		Map<String, Object> vo = null;
		try{
			Map<String, Object> personRefMap = (Map<String, Object>) redisClient.hget(XpgCommon.PEOPLESNEW, key);
					
			if(personRefMap !=null){				
		        //출연한 VOD 리스트
				List<String> contents = StringUtils.getStringToList((String) personRefMap.get("cast_content"), ",");
		        
		        if(contents !=null) {
			        for(String con : contents) {

			        	Map<String, Object> content = (Map<String, Object>) redisClient.hget(XpgCommon.VODSNEW, con);
			            
			            if(content != null) {
				            String content_id = (String)content.get("skb_cid");
				            String poster_filename = (String)content.get("nm_poster_file");
				            String title = (String)content.get("svc_title");
				            String cur_menu = (String)content.get("cms_menu");
				            String link_type = XpgCommon.LINK_TYPE_12;
				            String call_type = XpgCommon.CALL_TYPE_6;
				            String call_url = "";
				            
				            String content_type = (String)content.get("content_type");
				            
				            String no_service_pack = (String)content.get("no_service_pack");
				            String grade_internal = (String)content.get("grade_internal");
				            String yn_adult = StringUtils.defaultIfEmpty((String)content.get("yn_adult"), "");	// 신규메타(vodsNew)는 yn_adult로 내림		by oh2jj - 2017.05.22
				            			            
				            
				            Map<String, Object> content_vo = new HashMap<String, Object>();
				            
				            
				            if(content_type !=null && content_type.equals(XpgCommon.__shopping)){
								content_vo.put("synopsis_type", "2");
							}else{
								content_vo.put("synopsis_type", "1");
							}
				            
							 if(content_type !=null && content_type.equals(XpgCommon.__eros)){
								 yn_adult = "Y";
							 }
				         	
							// 신규메타(vodsNew)는 yn_adult로 내림		by oh2jj - 2017.05.22
							if("Y".equals(yn_adult)) {
								 yn_adult = "Y";
							}
				            content_vo.put("content_id", content_id);
				            content_vo.put("poster_filename", poster_filename);
				            content_vo.put("title", title);
				            content_vo.put("cur_menu", cur_menu);
				            content_vo.put("call_type", call_type);
				            content_vo.put("call_url", call_url);
				            content_vo.put("link_type", link_type);
				            content_vo.put("yn_adult", yn_adult);
				            content_vo.put("domestic_level", grade_internal);
				            /*if(xpgCommon.isServicePackNo(no_service_pack + "#" + id_package)) {*/
								if(content_id != null) {
									series.add(content_vo);
								}
							/*}*/
			            }
			        }  
		        }
		        
	        	//30개가 넘어가면 30개까지만 처리
	        	if(series.size() > XpgCommon.MAX_LIST_CNT) {
		        	for(int i = series.size()-1 ; i >= XpgCommon.MAX_LIST_CNT ; i--) {
		        		series.remove(i);
		        	}
		        }		        		        
		        
		        String gender = (String)personRefMap.get("gender");
		        
		        
		        String poster_filename = (String)personRefMap.get("photo");
		        String person_id = (String)personRefMap.get("person_id");
		        String name_kor = (String)personRefMap.get("name_kor");	
		        String name_en = (String)personRefMap.get("name_en");	
		        
		        String place_birth = (String)personRefMap.get("place_birth");
		        String birth = (String)personRefMap.get("birth");
		        String entries = (String)personRefMap.get("entries");
		        String sub_entries = (String)personRefMap.get("sub_entries");
		        
		        String coach = (String)personRefMap.get("coach");
		        String record = (String)personRefMap.get("record");
		        String ranking = (String)personRefMap.get("ranking");
		        String tactics = (String)personRefMap.get("tactics");
		        String mascot = (String)personRefMap.get("mascot");
		        String hometown = (String)personRefMap.get("hometown");
		        String stadium = (String)personRefMap.get("stadium");
		        String award = (String)personRefMap.get("award");
		        String career_sum = (String)personRefMap.get("career_sum");
		        
//		        if(poster_filename == null || poster_filename.equals("") || poster_filename.equals("null")){
		        if(poster_filename == null || poster_filename.isEmpty() || poster_filename.equals(XpgCommon.__null)){
		        	 poster_filename = "";
		        } else{
		        	 poster_filename = serverImageUrl + poster_filename;
		        }
		        		        
		        List<Map<String, Object>> contentInfoList = new ArrayList<Map<String,Object>>();
		        
				String icon_image = "";
				
				if(person_id !=null && person_id.startsWith("comz")){
					icon_image = "/DATA/epg/menu_image/default/vod_nate_logo.png";
				}else{
					icon_image = "";
				}
				
				if(birth !=null && birth.length()==8){
					birth = birth.substring(0,4) + "년" + birth.substring(4,6) + "월" + birth.substring(6,8) + "일";
				}else if(birth !=null && birth.length()==4){
					birth = birth.substring(0,4) + "년";
				}
				
				//영문명
//				if(name_en !=null && !name_en.equals("")){
				if(name_en !=null && !name_en.isEmpty()){
					contentInfoList.add(getRefVo("영문명", name_en));
				}
				
				//국적
//				if(place_birth !=null && !place_birth.equals("")){
				if(place_birth !=null && !place_birth.isEmpty()){
					contentInfoList.add(getRefVo("국적", place_birth));
				}
				
				//종목
//				if(entries !=null && !entries.equals("")){
				if(entries !=null && !entries.isEmpty()){
					contentInfoList.add(getRefVo("종목", entries));
				}
				
				//소속
//				if(sub_entries !=null && !sub_entries.equals("")){
				if(sub_entries !=null && !sub_entries.isEmpty()){
					contentInfoList.add(getRefVo("소속", sub_entries));
				}
				
				
				if(gender !=null && (gender.equals(XpgCommon.__1) || gender.equals(XpgCommon.__2))){					
					//생일
//					if(birth !=null && !birth.equals("")){
					if(birth !=null && !birth.isEmpty()){
						contentInfoList.add(getRefVo("출생", birth));
					}
				}else if(gender !=null && (gender.equals(XpgCommon.__10) || gender.equals(XpgCommon.__20)  || gender.equals(XpgCommon.__30))){
					//생일
//					if(birth !=null && !birth.equals("")){
					if(birth !=null && !birth.isEmpty()){
						contentInfoList.add(getRefVo("결성일", birth));
					}
				}else if(gender !=null && (gender.equals(XpgCommon.__100))){
					//창단일
//					if(birth !=null && !birth.equals("")){
					if(birth !=null && !birth.isEmpty()){
						contentInfoList.add(getRefVo("창단일", birth));
					}
				}
				//감독
//				if(coach !=null && !coach.equals("")){
				if(coach !=null && !coach.isEmpty()){
					contentInfoList.add(getRefVo("감독", coach));
				}
				
		        //전적
//				if(record !=null && !record.equals("")){
				if(record !=null && !record.isEmpty()){
					contentInfoList.add(getRefVo("전적", record));
				}
				
				
				//랭킹
//				if(ranking !=null && !ranking.equals("")){
				if(ranking !=null && !ranking.isEmpty()){
					contentInfoList.add(getRefVo("랭킹", ranking));
				}
				
				//전술
//				if(tactics !=null && !tactics.equals("")){
				if(tactics !=null && !tactics.isEmpty()){
					contentInfoList.add(getRefVo("전술", tactics));
				}
				
				//마스코드
//				if(mascot !=null && !mascot.equals("")){
				if(mascot !=null && !mascot.isEmpty()){
					contentInfoList.add(getRefVo("마스코트", mascot));
				}
				
				//연고지
//				if(hometown !=null && !hometown.equals("")){
				if(hometown !=null && !hometown.isEmpty()){
					contentInfoList.add(getRefVo("연고지", hometown));
				}
				
				//홈구장
//				if(stadium !=null && !stadium.equals("")){
				if(stadium !=null && !stadium.isEmpty()){
					contentInfoList.add(getRefVo("홈구장", stadium));
				}
					
				//성적
//				if(award !=null && !award.equals("")){
				if(award !=null && !award.isEmpty()){
					contentInfoList.add(getRefVo("성적", award));
				}

				//경력
//				if(career_sum !=null && !career_sum.equals("")){
				if(career_sum !=null && !career_sum.isEmpty()){
					contentInfoList.add(getRefVo("경력", career_sum));
				}
				
				vo = new HashMap<String, Object>();
				vo.put("staff_id", key);
				vo.put("title", name_kor);
				vo.put("poster_filename", icon_image);
				vo.put("series", series);
				vo.put("content_info_list", contentInfoList);
			}					
		}catch(Exception e){
			logger.error("Error", e);
//			e.printStackTrace();
			vo = null;
		}
		return vo;
	}	
	
	//관련 인물 정보 컨텐츠 가져오기
	public Map<String, Object> getXpgPersonRefVO(String key,String id_package) throws Exception{
		List<Map<String, Object>> series = new ArrayList<Map<String,Object>>();
		Map<String, Object> vo = null;
		
		try{
			Map<String, Object> personRefMap = (Map<String, Object>) redisClient.hget(XpgCommon.META_PRSN_INFO, key);
					
			if(personRefMap !=null){				
		        //출연한 VOD 리스트
		        // List<String> contents = (List<String>) personRefMap.get("cast_content");
				List<String> contents = StringUtils.getStringToList((String) personRefMap.get("cast_content"), ",");
				// List<String> contents = StringUtils.getStringToList((String) personRefMap.get("{27D47C69-E295-11E6-A386-0B031516F381}"), ",");
				
		        if(contents !=null){
			        for(String con : contents) {

			        	Map<String, Object> content = (Map<String, Object>) redisClient.hget(XpgCommon.META_INFO, con);
			            
			            if(content != null) {
				            String content_id = (String)content.get("skb_cid");
				            String poster_filename = (String)content.get("nm_poster_file");
				            String title = (String)content.get("svc_title");
//				            String cur_menu = (String)content.get("cms_menu");
				            String link_type = XpgCommon.LINK_TYPE_12;
				            String call_type = XpgCommon.CALL_TYPE_6;
				            String call_url = "";
				            
				            String content_type = (String)content.get("content_type");
				            
				            String no_service_pack = (String)content.get("no_service_pack");
				            String grade_internal = (String)content.get("grade_internal");
				            String yn_adult = StringUtils.defaultIfEmpty((String)content.get("yn_adult"), "");	// 신규메타(vodsNew)는 yn_adult로 내림		by oh2jj - 2017.05.22
				            			            
				            
				            Map<String, Object> content_vo = new HashMap<String, Object>();
				            
				            
				            if(content_type !=null && content_type.equals(XpgCommon.__shopping)){
								content_vo.put("synopsis_type", "2");
							}else{
								content_vo.put("synopsis_type", "1");
							}
				            
							 if(content_type !=null && content_type.equals(XpgCommon.__eros)){
								 yn_adult = "Y";
							 }
							 
							// 신규메타(vodsNew)는 yn_adult로 내림		by oh2jj - 2017.05.22
							if("Y".equals(yn_adult)) {
								yn_adult = "Y";
							}
				         		            
				            content_vo.put("content_id", content_id);
				            content_vo.put("poster_filename", poster_filename);
				            content_vo.put("title", title);
				            content_vo.put("sub_title", title);
				            content_vo.put("cur_menu", "");
				            content_vo.put("call_type", call_type);
				            content_vo.put("call_url", call_url);
				            content_vo.put("link_type", link_type);
				            content_vo.put("yn_adult", yn_adult);
				            content_vo.put("domestic_level", grade_internal);
				            
				            /*if(xpgCommon.isServicePackNo(no_service_pack + "#" + id_package)) {*/
								if(content_id != null) {
									series.add(content_vo);
								}
						/*	}*/
			            }
			        }  
		        }
		        
	        	//30개가 넘어가면 30개까지만 처리
	        	if(series.size() > XpgCommon.MAX_LIST_CNT) {
		        	for(int i = series.size()-1 ; i >= XpgCommon.MAX_LIST_CNT ; i--) {
		        		series.remove(i);
		        	}
		        }
		        		        
		        
		        String gender = (String)personRefMap.get("gender");
		        String height = (String)personRefMap.get("hght_val");
		        String weight = (String)personRefMap.get("wgt_val");
		        String blood_type = (String)personRefMap.get("blood_cd");
		        String agent = (String)personRefMap.get("mngco_nm");
		        String hobbies = (String)personRefMap.get("hobby_cts");
		        String specialty = (String)personRefMap.get("spn_cts");
		        String scholarship = (String)personRefMap.get("scholarship");
		        String career = (String)personRefMap.get("career");
		        String book = (String)personRefMap.get("wrbk_det_cts");
		        String poster_filename = (String)personRefMap.get("photo");
		        String person_id = (String)personRefMap.get("person_id");
		        String name_kor = (String)personRefMap.get("name_kor");	
		        String name_en = (String)personRefMap.get("name_en");	
		        String birth = (String)personRefMap.get("birth");
		        String debut = (String)personRefMap.get("dbu_det_cts");
		        
		        String award = (String)personRefMap.get("award"); 
		        
		        String gender_name = "";
		        
		        List<String> occupation_list = StringUtils.getStringToList((String) personRefMap.get("occupation"), ",");
		        
//		        if(poster_filename == null || poster_filename.equals("") || poster_filename.equals("null") ){
		        if(poster_filename == null || poster_filename.isEmpty() || poster_filename.equals(XpgCommon.__null) ){
		        	 poster_filename = "";
		        } else{
		        	poster_filename = serverImageUrl + poster_filename;
		        }
		        	        		        		        		       		        
		        		        
		        List<Map<String, Object>> contentInfoList = new ArrayList<Map<String,Object>>();
				
				String icon_image = "";
				
				if(person_id !=null && person_id.startsWith("comz")){
					icon_image = "/DATA/epg/menu_image/default/vod_nate_logo.png";
				}else{
					icon_image = "";
				}
				
				if(birth !=null && birth.length()==8){
					birth = birth.substring(0,4) + "년" + birth.substring(4,6) + "월" + birth.substring(6,8) + "일";
				}else if(birth !=null && birth.length()==4){
					birth = birth.substring(0,4) + "년";
				}
				
		        //성별
//				if(gender !=null && !gender.equals("")){
				if(gender !=null && !gender.isEmpty()){
					if(gender.equals(XpgCommon.__1)){
						gender_name = "남자";
					}else if(gender.equals(XpgCommon.__2)){
						gender_name = "여자";
					}else if(gender.equals(XpgCommon.__10)){
						gender_name = "남성그룹";
					}else if(gender.equals(XpgCommon.__20)){
						gender_name = "여성그룹";
					}else if(gender.equals(XpgCommon.__30)){
						gender_name = "혼성그룹";
					}					
					
					contentInfoList.add(getRefVo("성별", gender_name));
				}
				
				//영문명
//				if(name_en !=null && !name_en.equals("")){
				if(name_en !=null && !name_en.isEmpty()){
					contentInfoList.add(getRefVo("영문명", name_en));
				}
				
				if(gender !=null && (gender.equals(XpgCommon.__1) || gender.equals(XpgCommon.__2))){					
					//생일
//					if(birth !=null && !birth.equals("")){
					if(birth !=null && !birth.isEmpty()){
						contentInfoList.add(getRefVo("출생", birth));
					}
				}else if(gender !=null && (gender.equals(XpgCommon.__10) || gender.equals(XpgCommon.__20)  || gender.equals(XpgCommon.__30))){
					//생일
//					if(birth !=null && !birth.equals("")){
					if(birth !=null && !birth.isEmpty()){
						contentInfoList.add(getRefVo("결성일", birth));
					}
				}
				
				//직업
				if(occupation_list !=null){
					String occupation = "";
					
					int cnt = occupation_list.size();
					for(int i=0;i<cnt;i++){
						String tmp_occupation = (String)occupation_list.get(i);
						
						if(i != cnt-1){
							occupation =  tmp_occupation + "," + occupation ;
						}else{
							occupation =  occupation + tmp_occupation ;
						}
					}
					
					if(cnt > 0){
						contentInfoList.add(getRefVo("직업", occupation));
					}
				}
				
				//신장 체중
//				if(height !=null && !height.equals("")){
				if(height !=null && !height.isEmpty()){
					String height_weight = height;
					
//					if(weight !=null && !weight.equals("")){
					if(weight !=null && !weight.isEmpty()){
						height_weight = height_weight + "/" + weight;
					}
					
					contentInfoList.add(getRefVo("신체", height_weight));
				}
				
				//혈액형
//				if(blood_type !=null && !blood_type.equals("")){
				if(blood_type !=null && !blood_type.isEmpty()){
					blood_type = blood_type.toUpperCase();
					contentInfoList.add(getRefVo("혈액형", blood_type +"형"));
				}
				
				//학력
//				if(scholarship !=null && !scholarship.equals("")){
				if(scholarship !=null && !scholarship.isEmpty()){
					contentInfoList.add(getRefVo("학력", scholarship));
				}
				
				//저서
//				if(book !=null && !book.equals("")){
				if(book !=null && !book.isEmpty()){
					contentInfoList.add(getRefVo("저서", book));
				}
				
		        //데뷰
//				if(debut !=null && !debut.equals("")){
				if(debut !=null && !debut.isEmpty()){
					contentInfoList.add(getRefVo("데뷰", debut));
				}
				
				//소속사
//				if(agent !=null && !agent.equals("")){
				if(agent !=null && !agent.isEmpty()){
					contentInfoList.add(getRefVo("소속", agent));
				}
				
				//취미
//				if(hobbies !=null && !hobbies.equals("")){
				if(hobbies !=null && !hobbies.isEmpty()){
					contentInfoList.add(getRefVo("취미", hobbies));
				}
				
				//특기
//				if(specialty !=null && !specialty.equals("")){
				if(specialty !=null && !specialty.isEmpty()){
					contentInfoList.add(getRefVo("특기", specialty));
				}
					
				//수상
//				if(award !=null && !award.equals("")){
				if(award !=null && !award.isEmpty()){
					contentInfoList.add(getRefVo("수상", award));
				}
				
				//경력
//				if(career !=null && !career.equals("")){
				if(career !=null && !career.isEmpty()){
					career = career.replaceAll("\n","");
					contentInfoList.add(getRefVo("경력", career));
				}
				
				vo = new HashMap<String, Object>();
		        vo.put("staff_id", key);
		        vo.put("title", name_kor);
		        vo.put("poster_filename", poster_filename);
		        vo.put("icon_image", icon_image);
		        vo.put("series", series);
		        vo.put("content_info_list", contentInfoList);
		        
			}					
		}catch(Exception e){
			logger.error("Error", e);
//			e.printStackTrace();
			vo = null;
		}
		
		return vo;
	}
  	
  	Map<String, Object> getRefVo( String title, String value){
  		Map<String, Object> refVo = new HashMap<String, Object>();
  		refVo.put("title", title);
  		refVo.put("value", value);
		
  		return refVo;
  	}
}
