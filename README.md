# TwitterApplication

 Tweets 6 times per a day<br>
     -Gets the random tweet from its target accounts.<br><br>
	 
 Follows new 20 accounts per 1 hour -approximately-<br>
     -Registers their screen_name's to db with timestamp<br>
 
 Controls past follows<br>
     -Unfollows them if they dont follow-back in 3 days.<br>
     -Makes 'false' their states in db<br>
     -Does not follow them in the future<br>
 
 Supports to specify target list.<br>
     -By the way, you can gain followers from your potential audience..<br><br>
	 
Usage:<br>

 -Create a schema named as "twitter" in your mysql server.<br>
     -It will create appropriate tables by using JPA.<br>
 -Change targetList according to your target audience in Operations class.<br><br>
 
  Run the program and get new followers..<br>
  
   