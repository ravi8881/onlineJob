package com.main.interconnection.util;

/*
 * @author Name:Rajiv Kumar
 * @Created Date:17/10/2014
 * @update Date:17/10/2014
 * @purpose:UserAttemptTask For quartz scheduler
 *  
 */
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.main.interconnection.clientBo.User;
import com.main.interconnection.dao.UserDao;

public class AttemptResetTask {
	
	private static final Logger logger = LoggerFactory.getLogger(AttemptResetTask.class);
	@Autowired
	UserDao userDao;
	
	
	public void attemptUserReset() {
		logger.info("attemptUserReset()"+AttemptResetTask.class);
		//ModelAndView mav = new ModelAndView();
		List<User> berUser = userDao.findAllUserAttemptMoreZero(); // Find all user which more than zero Attempt
		if (berUser.size() == 0) {
		// no record found in database
			logger.info(" no record found in database"+AttemptResetTask.class);

		} else {
			for (User user : berUser) {
				user.setAttemptNumber(MagicNumbers.ATTEMPT_ZERO_SETTING);
				userDao.updateUserAttemptLogin(user);
			}
		}
	}
}