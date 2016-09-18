package twitterbot;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitterbot.services.FollowingRepository;


public class Main {
	
	public static void main(String[] args) {
		Operations op = new Operations();
		Logger logger = LoggerFactory.getLogger(Main.class);
		
		 while(true){
			 
			 try{
				 if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY % 4) == 0 ){
					 op.tweetSth();
					 op.sleepMin(2);
				 }
				 
				 logger.info("------------------------------------------------------------------------------------");
				 
				 op.follow20();
				 op.sleepMin(16);
				 
				 logger.info("------------------------------------------------------------------------------------");
				 
				 op.removeUnfollows();
				 op.sleepMin(16);
				 
				 logger.info("------------------------------------------------------------------------------------");
			 }
			 catch(Exception e){
				 logger.info("THERE IS SOMETHING WRONG IN MAIN FUNCTION");
				 logger.error("main error", e);
				 op.sleepMin(16);
			 }
		 }
	}
	

}
