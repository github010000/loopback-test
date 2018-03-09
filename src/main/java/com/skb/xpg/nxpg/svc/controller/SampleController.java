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

	@RequestMapping(value = "/menu/gnb")
	public Map<String, Object> nxpg001(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-001");
	}

	@RequestMapping(value = "/menu/all")
	public Map<String, Object> nxpg002(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-002");
	}

	@RequestMapping(value = "/grid/promotion")
	public Map<String, Object> nxpg003(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-003");
	}

	@RequestMapping(value = "/menu/block")
	public Map<String, Object> nxpg004(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-004");
	}

	@RequestMapping(value = "/grid/grid")
	public Map<String, Object> nxpg005(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-005");
	}

	@RequestMapping(value = "/grid/event")
	public Map<String, Object> nxpg006(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-006");
	}

	@RequestMapping(value = "/menu/defaulthome")
	public Map<String, Object> nxpg007(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-007");
	}

	@RequestMapping(value = "/menu/month")
	public Map<String, Object> nxpg008(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-008");
	}

	@RequestMapping(value = "/inter/cwgrid")
	public Map<String, Object> nxpg009(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-009");
	}

	@RequestMapping(value = "/contents/synopsis")
	public Map<String, Object> nxpg010(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-010");
	}

	@RequestMapping(value = "/contents/purchase")
	public Map<String, Object> nxpg011(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-011");
	}

	@RequestMapping(value = "/contents/people")
	public Map<String, Object> nxpg012(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-012");
	}

	@RequestMapping(value = "/inter/cwrelation")
	public Map<String, Object> nxpg013(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-013");
	}

	@RequestMapping(value = "/added/epg")
	public Map<String, Object> nxpg014(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-014");
	}

	@RequestMapping(value = "/added/channelplus")
	public Map<String, Object> nxpg015(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-015");
	}

	@RequestMapping(value = "/contents/gwsynop")
	public Map<String, Object> nxpg016(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-016");
	}

	@RequestMapping(value = "/contents/commerce")
	public Map<String, Object> nxpg017(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-017");
	}

	@RequestMapping(value = "/contents/corner")
	public Map<String, Object> nxpg018(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-018");
	}

	@RequestMapping(value = "/contents/vodlist")
	public Map<String, Object> nxpg019(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-019");
	}

	@RequestMapping(value = "/menu/kzchar")
	public Map<String, Object> nxpg020(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-101");
	}

	@RequestMapping(value = "/menu/kzgnb")
	public Map<String, Object> nxpg021(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-102");
	}

	@RequestMapping(value = "/grid/kzcharinfo")
	public Map<String, Object> nxpg022(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-103");
	}

	@RequestMapping(value = "/menu/lftmenu")
	public Map<String, Object> nxpg023(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-401");
	}

	@RequestMapping(value = "/grid/lftgrid")
	public Map<String, Object> nxpg024(@PathVariable String ver, @RequestParam Map<String, String> param) {
		return getJson("IF-NXPG-402");
	}

	@RequestMapping(value = "/contents/lftsynop")
	public Map<String, Object> nxpg025(@PathVariable String ver, @RequestParam Map<String, String> param) {
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
