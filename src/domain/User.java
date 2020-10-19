package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;

import configuration.Password;

/**
 * Stores credentials and additional information of registered users, including the active bets it has in place. 
 * Users may be regular or administrators (represented by isAdmin boolean).
 *
 */

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class User implements Serializable{


	private UserProduct userProduct = new UserProduct();
	@Id
	@XmlID
	private String username;
	private float cash;

	@OneToOne(cascade = CascadeType.ALL,mappedBy = "owner", orphanRemoval = true)
	private CreditCard defaultcard;
	private Date registrationdate;
	private Date lastlogin;

	@OneToOne(cascade = CascadeType.ALL,mappedBy = "user", orphanRemoval = true)
	private Profile profile;

	@OneToMany(fetch=FetchType.EAGER)
	private List<Bet> bets;

	@ElementCollection
	@CollectionTable(name="CreditCard")
	@MapKeyColumn(name="cardNumber")
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Map<String,CreditCard> creditcards;

	public User() {
		this.bets = new ArrayList<Bet>();
		this.creditcards = new HashMap<String, CreditCard>();
	}

	public User(String username, String password, boolean isAdmin, Profile p) {
		super();
		this.username = username;
		userProduct.setPassword(Password.hashPassword(password));
		this.profile = p;
		userProduct.setIsAdmin(isAdmin);
		this.registrationdate = new Date();
		this.cash = 9999; 
		this.bets = new ArrayList<Bet>();
		this.creditcards = new HashMap<String, CreditCard>();
		setProfile(p);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return userProduct.getPassword();
	}

	public void setPassword(String password) {
		userProduct.setPassword(password);
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile p) {
		p.setUser(this);
		this.profile = p;
	}

	public CreditCard getDefaultCreditCard() {
		return this.defaultcard;
	}

	public void setDefaultCreditCard(CreditCard cc) {
		this.defaultcard = cc;
	}

	public Date getRegistrationdate() {
		return registrationdate;
	}

	public void setRegistrationdate(Date registrationdate) {
		this.registrationdate = registrationdate;
	}


	public Date getLastlogin() {
		return lastlogin;
	}


	public boolean isAdmin() {
		return userProduct.getIsAdmin();
	}

	public void setAdmin(boolean isAdmin) {
		userProduct.setIsAdmin(isAdmin);
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

	public float getCash() {
		return cash;
	}

	public void setCash(float cash) {
		this.cash = cash;
	}

	public float addCash(float cash) {
		this.cash = getCash() + cash;
		return cash;
	}
	
	public Map<String,CreditCard> getCreditCards() {
		return creditcards;
	}

	public void setCreditCards(Map<String,CreditCard> creditcards) {
		this.creditcards = creditcards;
	}

	/**
	 * Registers the bet performed by a user
	 * @param q			the question the bet has been placed on.
	 * @param amount	the amount of money.
	 */
	public void addBet(Bet c) {
		bets.add(c);
	}

	public void setBets(List<Bet> bets) {
		this.bets = bets;
	}

	public List<Bet> getBets() {
		return bets;
	}

	public void removeBet(Bet c) {
		int j=0;
		for (int i=0;i<bets.size();i++) {
			if (bets.get(i).equals(c)) {
				j=i;
				break;
			}
		}
		bets.remove(j);
	}

	public void addCreditCard(CreditCard cc) {
		if(creditcards == null) {
			creditcards = new HashMap<String,CreditCard>();
		}
		creditcards.put(cc.getCardNumber(),cc);
		cc.setOwner(this);
	}

	public String statusToString() {
		return userProduct.statusToString();
	}


	/**
	 * Retrieves the bets the currently logged has placed between the indicated dates
	 * 
	 * @param		fromdate lower bound date
	 * @param		fromdate upper bound date
	 * @return		List<Bet> user's bets
	 */
	public List<Bet> retrieveBetsByDate(Date fromdate, Date todate){
		List<Bet> betsbydate = new ArrayList<Bet>(); 
		for(Bet b : bets) {
			if(b.getPlacementdate().compareTo(fromdate)>=0 && b.getPlacementdate().compareTo(todate)<=0) {
				betsbydate.add(b);
			}
		}
		return betsbydate;
	}


}
