package com.skb.xpg.nxpg.svc.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dmshin on 06/02/2017.
 */
@RestController
@RequestMapping(value = "/xpg/{ver}", produces = "application/json; charset=utf-8")
public class SampleController {

	@Autowired
	private ResourceLoader resourceLoader;

	@RequestMapping(value = "/xpg/v5/menu/gnb")
	public Map<String, Object> nxpg1(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-001");
	}

	@RequestMapping(value = "/xpg/v5/menu/all")
	public Map<String, Object> nxpg2(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-002");
	}

	@RequestMapping(value = "/xpg/v5/menu/block")
	public Map<String, Object> nxpg3(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-003");
	}

	@RequestMapping(value = "/xpg/v5/menu/monthinfo")
	public Map<String, Object> nxpg4(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-004");
	}

	@RequestMapping(value = "/xpg/v5/menu/month")
	public Map<String, Object> nxpg5(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-005");
	}

	@RequestMapping(value = "/xpg/v5/grid/grid")
	public Map<String, Object> nxpg6(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-006");
	}

	@RequestMapping(value = "/xpg/v5/grid/event")
	public Map<String, Object> nxpg7(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-007");
	}

	@RequestMapping(value = "/xpg/v5/contents/rating")
	public Map<String, Object> nxpg8(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-008");
	}

	@RequestMapping(value = "/xpg/v5/inter/cwgrid")
	public Map<String, Object> nxpg9(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-009");
	}

	@RequestMapping(value = "/xpg/v5/contents/synopsis")
	public Map<String, Object> nxpg10(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-010");
	}

	@RequestMapping(value = "/xpg/v5/contents/people")
	public Map<String, Object> nxpg12(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-011");
	}

	@RequestMapping(value = "/xpg/v5/inter/cwrelation")
	public Map<String, Object> nxpg13(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-012");
	}

	@RequestMapping(value = "/xpg/v5/added/epg")
	public Map<String, Object> nxpg14(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-013");
	}

	@RequestMapping(value = "/xpg/v5/contents/gwsynop")
	public Map<String, Object> nxpg15(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-014");
	}

	@RequestMapping(value = "/xpg/v5/contents/commerce")
	public Map<String, Object> nxpg16(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-015");
	}

	@RequestMapping(value = "/xpg/v5/contents/corner")
	public Map<String, Object> nxpg17(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-016");
	}

	@RequestMapping(value = "/xpg/v5/contents/vodlist")
	public Map<String, Object> nxpg18(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-017");
	}

	@RequestMapping(value = "/xpg/v5/menu/guide")
	public Map<String, Object> nxpg19(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-018");
	}

	@RequestMapping(value = "/xpg/v5/menu/liveevent")
	public Map<String, Object> nxpg20(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-019");
	}

	@RequestMapping(value = "/xpg/v5/menu/kzchar")
	public Map<String, Object> nxpg21(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-101");
	}

	@RequestMapping(value = "/xpg/v5/menu/kzgnb")
	public Map<String, Object> nxpg22(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-102");
	}

	@RequestMapping(value = "/xpg/v5/grid/kzcharinfo")
	public Map<String, Object> nxpg23(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-103");
	}

	@RequestMapping(value = "/xpg/v5/menu/lftmenu")
	public Map<String, Object> nxpg24(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-401");
	}

	@RequestMapping(value = "/xpg/v5/grid/lftgrid")
	public Map<String, Object> nxpg25(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-402");
	}

	@RequestMapping(value = "/xpg/v5/contents/lftsynop")
	public Map<String, Object> nxpg26(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-403");
	}

	private Map<String, Object> getJson(String fileName) {
		Resource resource = resourceLoader.getResource("classpath:static/" + fileName + ".json");
		File jsonFile = null;
		try {
			jsonFile = resource.getFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StringToJsonList(readFile(jsonFile));
	}
	
	private Map<String, Object> StringToJsonList(String json) {
//		JacksonJsonParser parser = new JacksonJsonParser();
//		Map<String, Object> map = parser.parseMap(json);
//		
		GsonJsonParser parser = new GsonJsonParser();
		Map<String, Object> map = parser.parseMap(json);
		return map;
		
//		return map;

	}
	
	private String readFile(File file) {
		// ==========================//
		// 텍스트 파일 읽기
		// ==========================//
		BufferedReader br = null;
		String result = "";
		try {
			if (file != null && file.isFile()) {
				br = new BufferedReader(new FileReader(file));
				String line;
				while ((line = br.readLine()) != null) {
					result += line;
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					
				}
			}
		}
		return result;

	}
}
