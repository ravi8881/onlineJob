package com.main.interconnection.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.ErrorCode;



@Controller
@RequestMapping(value="/error/*")
public class InterConnectionClientError {
	
	@RequestMapping( value="/400" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView Error400(							
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();	
		try{
			mav.addObject(ErrorCode.getCustomeError(400));
			return mav;	
		}catch (Exception e) {
			e.printStackTrace();
		}
	return mav;
	}
	
	@RequestMapping( value="/404" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView Error404(							
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();	
		try{
			mav.addObject(ErrorCode.getCustomeError(404));
			return mav;	
		}catch (Exception e) {
			e.printStackTrace();
		}
	return mav;
	}
	
	@RequestMapping( value="/genral" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView genral(							
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();	
		try{
			mav.addObject(ErrorCode.getCustomeError(400));
			return mav;	
		}catch (Exception e) {
			e.printStackTrace();
		}
	return mav;
	}

}
