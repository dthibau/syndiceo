package com.syndiceo.dto;

import com.syndiceo.model.Account;

public class ConseilDTO {

	private boolean selected;
	private Account account;
	
	public ConseilDTO(Account account) {
		this.account = account;
	}
	public ConseilDTO(Account account, boolean selected) {
		this.account = account;
		this.selected = selected;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConseilDTO other = (ConseilDTO) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		return true;
	}
	
	
}
