package twitterbot.services;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.persistence.jpa.*;
import twitterbot.models.Following;


public class FollowingRepository {
	EntityManagerFactory emfactory;
	EntityManager entitymanager;
	private Logger logger = LoggerFactory.getLogger(FollowingRepository.class);

	public FollowingRepository() {
		emfactory = Persistence.createEntityManagerFactory("TWITTER_JPA");
		
		entitymanager = emfactory.createEntityManager();
	}

	public void create(Following following) {
		entitymanager.getTransaction().begin();
		entitymanager.persist(following);
		entitymanager.getTransaction().commit();
	}

	public void update(Following following) {
		entitymanager.getTransaction().begin();
		Following f = entitymanager.find(Following.class, following.getId());
		f.setScreenName(following.getScreenName());
		f.setState(following.isState());
		entitymanager.getTransaction().commit();
	}

	public Following findById(Integer id) {
		Following Following = entitymanager.find(Following.class, id);
		return Following;
	}
	
	public Following findByscreenName(String screenName){
		Query query = entitymanager.createQuery("SELECT f FROM Following f WHERE f.screenName = :screenName");
		query.setParameter("screenName", screenName);
		@SuppressWarnings("unchecked")
		List<Following> followingList = query.getResultList();
		
		if(followingList != null && followingList.size()>0)
			return followingList.get(0);
		else
			return null;
	}

	public void delete(Integer id) {
		entitymanager.getTransaction( ).begin( );
		Following Following = entitymanager.find(Following.class, id);
		entitymanager.remove(Following);
		entitymanager.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<Following> getAll(){
		List<Following> followingList;
		Query query = entitymanager.createQuery("SELECT f FROM Following f");
		followingList = query.getResultList();
		return followingList;
	}
	
	public List<Following> getToBeRemoved(){
		List<Following> list = new ArrayList<>();
		Query query = entitymanager.createQuery("SELECT f FROM Following f WHERE f.followDate < :unfollowDate and f.state = :initialState");
		query.setParameter("initialState", null);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -3);
		Date date = new Date(cal.getTimeInMillis());
		query.setParameter("unfollowDate", date);
		
		list = query.getResultList();
		
		return list;
	}
	
	public void removeFriend(Following following){
		entitymanager.getTransaction().begin();
		following.setState(false);
		entitymanager.getTransaction().commit();
		logger.info(following.getScreenName()+"' is removed from friendships");
	}
	
	public void confirmFriend(Following following){
		entitymanager.getTransaction().begin();
		following.setState(true);
		entitymanager.getTransaction().commit();
		logger.info(following.getScreenName()+"'s friendship is confirmed");
	}
	
}
