package com.main.interconnection;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.main.interconnection.autoadmin.services.MongoInsert;
import com.main.interconnection.autoadmin.services.Person;


@Controller
public class HomeController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString());
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
	//	https://partner-api.groupon.com/deals.json?tsToken=3ba69902092a28fbde0e411e8b90e43a42336b3e&division_id=amarillo&offset=0&limit=1
		
	//	https://partner-api.groupon.com/deals.json?tsToken={token}&division_id={id}&offset={offset}&limit={limit}
		logger.info("---------------------------------->");
		
		model.addAttribute("serverTime", formattedDate );
				
		
		
		
		
		return "home";
	}
	
	
	@RequestMapping(method={RequestMethod.POST,RequestMethod.GET}, value="/getState/{id}")
	public @ResponseBody HashMap<Integer, String> getTaxstate(@PathVariable("id") String id , HttpServletRequest request) {
	
		HashMap<Integer, String> stateMap=new HashMap<Integer, String>();
		stateMap.put(4, "you");
		Person person=new Person();
		if (!mongoTemplate.collectionExists(Person.class)) {
			mongoTemplate.createCollection(Person.class);
		}	
		person.setId(id);
		person.setName("ravi");
		mongoTemplate.insert(person, "person");

		MongoInsert mongoInsert=new MongoInsert();
		
		if (!mongoTemplate.collectionExists(MongoInsert.class)) {
			mongoTemplate.createCollection(MongoInsert.class);
		}
	 	mongoInsert.setId(id);
	 	mongoInsert.setName(id);
	 	mongoInsert.setNumber(id);
	 	mongoInsert.setAge(id);
	 	
	 	mongoTemplate.insert(mongoInsert);
		return stateMap;
 }
	
}
