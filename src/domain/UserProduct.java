package domain;


import java.io.Serializable;

public class UserProduct implements Serializable {
	private String password;
	private boolean isAdmin;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String statusToString() {
		if (this.isAdmin) {
			return ("Admin.");
		} else {
			return ("User");
		}
	}
}