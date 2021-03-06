package HyipSimulator;

import Players.Hyip;
import Players.Investor;


public class Invest {
	
	private static volatile long COUNT_INVESTS = 0;
	
	private Long InvestId;
	
	private double money; // amount of money "invested"
	private int tickCount; // 1 tick = 1 day in our simulation universe
	
	private Hyip hyip;
	private HyipOffert hyipOffert;
	private Investor investor;
	
	public Invest(Investor investor, Hyip hyip, double money, HyipOffert hyipOffert) {
		++COUNT_INVESTS;
		this.hyip = hyip;
		this.money = money;
		this.hyipOffert = hyipOffert;
		this.investor = investor;
		this.tickCount = 0;
		InvestId = COUNT_INVESTS;
	}

	public Investor getInvestor() {
		return investor;
	}

	public HyipOffert getHyipOffert() {
		return hyipOffert;
	}

	public Hyip getHyip() {
		return hyip;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getTickCount() {
		return tickCount;
	}

	public void setTickCount(int tickCount) {
		this.tickCount = tickCount;
	}
	
	public void incrementTickCount(){
		this.tickCount++;
	}
	
	public void calculateInterest(){
		this.money += this.money * hyipOffert.getPercent();
	}
	
	public double forecastInterest(){
		return this.money + (this.money * hyipOffert.getPercent());
	}
	
	@Override
	public int hashCode() {
		return InvestId.hashCode();
		// Returns a hash code for this Long.
		// The result is the exclusive OR of the two halves of the
		// primitive long value held by this Long object. That is, 
		// the hashcode is the value of the expression: 
		// (int)(this.longValue()^(this.longValue()>>>32))
	}

	@Override
	public boolean equals(Object obj) {
		if ( ((Invest)obj).InvestId.equals(this.InvestId) ){
			return true;
		} else
			return false;
	}

}
