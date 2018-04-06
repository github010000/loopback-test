package com.skb.xpg.appd.svc.xpg;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ResultCommon {
	
	public static final Map<String, String> resultCodeListMap;
    static{
    	resultCodeListMap = new HashMap<String, String>();
    	resultCodeListMap.put("7073", "CI 리스트에 데이터 없음");
    	resultCodeListMap.put("7157", "인물정보 데이터 없음");
		resultCodeListMap.put("7162", "크롤링 데이터 없음");
    	resultCodeListMap.put("7163", "컨텐츠 데이터 없음");
    	resultCodeListMap.put("7164", "그리드 데이터 없음");
    	resultCodeListMap.put("7165", "에피소드 데이터 없음");
    	resultCodeListMap.put("7200", "실시간 채널 정보 없음");
    	resultCodeListMap.put("7201", "실시간 채널 장르 정보 없음");
    	resultCodeListMap.put("9001", "입력 파라미터 오류");
    	resultCodeListMap.put("9999", "기타 에러");
    	resultCodeListMap.put("0000", "성공");
    }
}
