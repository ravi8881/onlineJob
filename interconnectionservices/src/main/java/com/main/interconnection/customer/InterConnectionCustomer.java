package com.main.interconnection.customer;

import java.text.DateFormat;
import java.util.Date;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public class InterConnectionCustomer {

private static final Logger logger = LoggerFactory.getLogger(InterConnectionCustomer.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString());
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		logger.info("---------------------------------->");
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
}
