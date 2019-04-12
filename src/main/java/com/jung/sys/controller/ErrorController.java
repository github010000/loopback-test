package com.jung.sys.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/", produces = "application/json; charset=utf-8")
public class ErrorController {

	
	@RequestMapping(value = "/sleep")
	public Map<String, Object> error(@RequestParam Map<String, String> param) {
		
        String millis = param.get("millis");
        System.out.println("##### sleep entered...millis=" + millis);

        Map<String, Object> myResult = new HashMap<String, Object>();
        myResult.put("svc_name", "SLEEP");
        myResult.put("result", "0000");
        myResult.put("millis", millis);

        try {
            Thread.sleep(Long.parseLong(millis));
        }
        catch(Exception e) {
        	System.err.println(e.toString());
            myResult.put("result", "-1");
            myResult.put("reason", "Exception");
        }

        System.out.println("##### the end..." + myResult.toString());
        return myResult;
	}
}
