package HyipSimulator;

import Players.Hyip;

public class HyipAccount {

	private Hyip owner;
	private volatile double cash;
	private volatile double income;

	public HyipAccount(Hyip owner, double cash) {
		this.owner = owner;
		this.cash = cash;
		this.income = 0;
	}

	public void addMoney(double value) {
		cash += value;
		addIncome(value);
	}

	public void withdrawMoney(double value) {
		cash -= value;
		minusIncome(value);
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public double getIncome() {
		return this.income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public void addIncome(double amount) {
		this.income += amount;
	}
	
	public void minusIncome(double amount) {
		this.income -= amount;
	}

	public void diffIncome(double amount) {
		this.income = amount - this.income;
	}

	public void diffIncome(double amount, boolean abs) {
		this.income = abs ? Math.abs(amount - this.income) : amount
				- this.income;
	}

	public Hyip getOwner() {
		return owner;
	}

	public void clear() {
		this.income = 0;
		this.cash = 0;
	}

	public void setOwner(Hyip owner) {
		System.exit(-10);
		// throw new UnsupportedOperationException(
		// "I doubt account can change owner");
		// this.owner = owner;
	}

}
