package com.jung.sys.config;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jung.sys.util.LogUtil;

@Component
public class CertificationInterceptor implements HandlerInterceptor {
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> enumName = request.getParameterNames();
		while (enumName.hasMoreElements()) {
			String name = enumName.nextElement();
			map.put(name, request.getParameter(name));
		}
		LogUtil.tlog(request.getParameter("IF"), "RECV.REQ", request.getHeader("UUID"), request.getParameter("stb_id"), "STB", map);
    	
    	return true;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    	
    }
 
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
//    	if (LogUtil.logSwitch) {
//        	Enumeration<String> enumName = request.getParameterNames();
//    		Map<String, String> map = new HashMap<String, String>();
//    		while (enumName.hasMoreElements()) {
//    			String name = enumName.nextElement();
//    			map.put(name, request.getParameter(name));
//    		}
//    		map.put("status", response.getStatus() + "");
//    		
//    		LogUtil.tlog(request.getParameter("IF"), "SEND.RES", request.getHeader("UUID"), request.getParameter("stb_id"), "STB", map);
//    	}
    }
}