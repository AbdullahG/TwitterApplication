package twitterbot.models;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table
public class Following {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String screenName;
	private Boolean state;
	
	@Temporal(TemporalType.DATE)
	private Date followDate;
	
	public Following() {
		// TODO Auto-generated constructor stub
	}
	
	public Following(String screenName){
		setScreenName(screenName);
		setFollowDate(new Date(Calendar.getInstance().getTimeInMillis()));
		setState(null);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public Boolean isState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public Date getFollowDate() {
		return followDate;
	}
	public void setFollowDate(Date followDate) {
		this.followDate = followDate;
	}
}
