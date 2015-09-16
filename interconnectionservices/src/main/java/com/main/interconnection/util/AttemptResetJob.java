package com.main.interconnection.util;

/*
 * @author Name:Rajiv Kumar
 * @Created Date:17/10/2014
 * @update Date:17/10/2014
 * @purpose:AttemptResetJob For quartz scheduler
 *  
 */
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class AttemptResetJob extends QuartzJobBean {
	private AttemptResetTask attemptResetTask;
	private static final Logger logger = LoggerFactory
			.getLogger(AttemptResetJob.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.info("executeInternal(JobExecutionContext arg0)"
				+ AttemptResetJob.class);
		attemptResetTask.attemptUserReset();

	}


	public AttemptResetTask getAttemptResetTask() {
		return attemptResetTask;
	}

	public void setAttemptResetTask(AttemptResetTask attemptResetTask) {
		this.attemptResetTask = attemptResetTask;
	}

	

}