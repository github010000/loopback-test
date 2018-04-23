package com.skb.xpg.nxpg.svc.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResultCommon {
	
	public static final List<String> commonFields;
	static {
		commonFields = new ArrayList<String>();
	}
	
	public static final Map<String, String> reason;
	static {
		reason = new HashMap<String, String>();
		reason.put("7073", "CI 리스트에 데이터 없음");
		reason.put("7157", "인물정보 데이터 없음");
		reason.put("7162", "크롤링 데이터 없음");
		reason.put("7163", "컨텐츠 데이터 없음");
		reason.put("7164", "그리드 데이터 없음");
		reason.put("7165", "에피소드 데이터 없음");
		reason.put("7200", "실시간 채널 정보 없음");
		reason.put("7201", "실시간 채널 장르 정보 없음");
		reason.put("9001", "입력 파라미터 오류");
		reason.put("9999", "기타 에러");
		reason.put("0000", "성공");
	}
}