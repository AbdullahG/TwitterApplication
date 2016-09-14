package twitterbot;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.PagableResponseList;
import twitter4j.Relationship;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitterbot.models.Following;
import twitterbot.services.FollowingRepository;

public class Operations {
	private String consumerKey = "*************************";
	private String consumerSecret = "*********************************************";
	private String accessToken = "*****************-*******************************";
	private String accessTokenSecret = "***************************************";

	private ConfigurationBuilder cb = null;
	private TwitterFactory tf = null;
	private Twitter twitter = null;
	// private Controller controller = null;
	private ArrayList<String> targetList = null;
	private int count;
	private FollowingRepository followingRepository = new FollowingRepository();
	private Logger logger = LoggerFactory.getLogger(Operations.class);

	public Operations() {
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);
		tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		setFollowingRepository(followingRepository);

		targetList = new ArrayList<String>();
		targetList.add("shiftdeletenet");
		targetList.add("donanimhaber");
		targetList.add("webrazzi");
		count = 0;
		// controller = new Controller();
	}

	public void follow20() {
		PagableResponseList<User> followers = null;
		try {
			followers = twitter.getFollowersList(targetList.get(count), -1);
			count++;
			count = count % targetList.size();

			for (int i = 0; i < followers.size(); i++) {
				if (follow(followers.get(i).getScreenName())) {
					logger.info((i + 1) + ". " + followers.get(i).getScreenName() + " is followed");
				} else {
					logger.info((i + 1) + ". " + followers.get(i).getScreenName() + " is ALREADY followed BEFORE");
				}
			}
		} catch (TwitterException e) {
			logger.info("API RATE LIMIT EXCEEDED");
			logger.error("operations, follow20 error", e);
			sleepMin(16);
			follow20();
		}
	}

	public boolean follow(String screenName) {
		if (followingRepository.findByscreenName(screenName) == null) {
			try {
				twitter.createFriendship(screenName);
				followingRepository.create(new Following(screenName));
				return true;
			} catch (TwitterException e) {
				sleepMin(16);
				logger.error("operations, follow error", e);
				follow(screenName);
			}
		}

		return false;
	}

	public void removeUnfollows() {
		List<Following> toBeRemoved = followingRepository.getToBeRemoved();
		Relationship rl = null;
		for (int i = 0; i < toBeRemoved.size(); i++) {
			rl = getRelationShip(toBeRemoved.get(i).getScreenName());
			
			if (rl.isSourceFollowedByTarget()) {
				followingRepository.confirmFriend(toBeRemoved.get(i));
			} else {
				destroyFriendship(toBeRemoved.get(i).getScreenName());
				followingRepository.removeFriend(toBeRemoved.get(i));
			}
		}
	}

	public Relationship getRelationShip(String screenName) {
		Relationship rl = null;

		try {
			rl = twitter.showFriendship(twitter.getScreenName(), screenName);
		} catch (IllegalStateException | TwitterException e) {
			sleepMin(16);
			logger.error("operations, getrelationship error", e);
			return getRelationShip(screenName);
		}

		return rl;
	}

	public void destroyFriendship(String screenName) {
		try {
			twitter.destroyFriendship(screenName);
		} catch (TwitterException e) {
			sleepMin(16);
			logger.error("operations, destroyFriendship follow error", e);
			destroyFriendship(screenName);
		}
	}

	public void tweetSth() {
		List<Status> list = getStatus(targetList.get(count));
		try {
			twitter.updateStatus(list.get(0).getText());
			logger.info("TWEETED! :" + list.get(0).getText());
		} catch (TwitterException e) {
			logger.info("IN TWEETSTH(), API LIMIT EXCEEDED OR TWITTER NETWORK IS UNAVAILABLE");
			logger.error("operations, tweetSth error", e);
			sleepMin(16);
			tweetSth(list);
		}
	}

	public void tweetSth(List<Status> list) {
		try {
			twitter.updateStatus(list.get(0).getText());
			logger.info("TWEETED! :" + list.get(0).getText());
		} catch (TwitterException e) {
			logger.info("IN TWEETSTH(), API LIMIT EXCEEDED OR TWITTER NETWORK IS UNAVAILABLE");
			logger.error("operations, tweetSth error", e);
			sleepMin(16);
			tweetSth(list);
		}
	}

	public List<Status> getStatus(String screenName) {
		List<Status> list = null;
		try {
			list = twitter.getUserTimeline(targetList.get(count));
		} catch (TwitterException e) {
			logger.info("IN GETSTATUS(), API LIMIT EXCEEDED OR TWITTER NETWORK IS UNAVAILABLE");
			logger.error("operations, getStatus error", e);
			sleepMin(16);
			return getStatus(screenName);
		}
		return list;
	}

	public void sleepMin(int minute) {
		try {
			logger.info(" - SLEEPING " + minute + " MINUTES -");
			Thread.sleep(1000 * 60 * minute);
		} catch (InterruptedException e) {
			logger.error("operations, sleep error", e);
			sleepMin(minute);
		}
	}

	public FollowingRepository getFollowingRepository() {
		return followingRepository;
	}

	public void setFollowingRepository(FollowingRepository followingRepository) {
		this.followingRepository = followingRepository;
	}

}
