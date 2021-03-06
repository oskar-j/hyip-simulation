package Players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import HyipSimulator.HyipOffert;
import HyipSimulator.Invest;
import HyipSimulator.InvestorAccount;
import HyipSimulator.Player;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.ContextUtils;

/**
 * Represents a single investor willing to spend his money on one or more HYIPs
 * through all the Hyip Game
 * 
 * @author Oskar
 * @updated 13.07.2014
 */
public class Investor extends Player {

	public static HashMap<String, Double> PAYOFFS = new HashMap<String, Double>();
	private static int CONSUMER_TYPE_H;
	private static int CONSUMER_TYPE_L;
	private InvestorType risk_level;
	private InvestorAccount investorAccount;
	private int expertise;

	private List<Hyip> hyipsChosen = new ArrayList<Hyip>();
	private Set<Hyip> allHyips;
	private static final double MINIMAL_INVESTMENT_TO_CONSIDER = 1.0;

	public Investor(InvestorType investorType) {
		this.expertise = RandomHelper.createUniform(CONSUMER_TYPE_L,
				CONSUMER_TYPE_H).nextInt();
		hyipsChosen = new ArrayList<Hyip>();
		risk_level = investorType;
	}

	public void initializeWallet() {
		if (investorAccount != null) {
			// throw new UnsupportedOperationException(
			// "Agent (investor) already charged his account!");
			investorAccount.clear();
		} else
			investorAccount = new InvestorAccount(this);
		switch (risk_level) {
		case HIGH_AVERSION:
			investorAccount.setBalance(100);
			break;
		case MEDIUM_AVERSION:
			investorAccount.setBalance(1000);
			break;
		case LOW_AVERSION:
			investorAccount.setBalance(10000);
			break;
		default:
			assert false; // should never happen
			break;
		}
	}

	@ScheduledMethod(start = 1.0, interval = 1.0, priority = 100)
	public void step() {
		hyipsChosen.clear();
		allHyips = chooseAllProducers(false);
		for (Hyip hyip : allHyips) {
			if (investorAccount.hasMoney()) {
				double invChances = 0;
				switch (this.risk_level) {
				case HIGH_AVERSION:
					invChances = hyip.isGoodLooking() ? 0.9 : 0.1;
					break;
				case MEDIUM_AVERSION:
					invChances = hyip.isGoodLooking() ? 0.9 : 0.3;
					break;
				case LOW_AVERSION:
					invChances = hyip.isGoodLooking() ? 0.9 : 0.5;
					break;
				default:
					// throw new UnsupportedOperationException(
					// "should never happen");
					break;
				}
				if (RandomHelper.nextDoubleFromTo(0, 1) < invChances) {
					if (RandomHelper.nextDoubleFromTo(0, 1) < hyip.getAdvert()) {
						// test passed
						hyipsChosen.add(hyip);
					}
				}
			}
		}
		for (Hyip hyip : hyipsChosen) {
			chooseOffert(hyip);
		}
	}

	public void resetMe() {
		this.initializeWallet();
		this.hyipsChosen.clear();
		this.allHyips.clear();
	}

	private void chooseOffert(Hyip hyip) {
		invest(hyip, hyip.getFirstOffert());
	}

	private void invest(Hyip hyip, HyipOffert hyipOffert) {
		int amountToInvest = 0;
		Invest investment = null;
		if (risk_level == InvestorType.HIGH_AVERSION) {
			amountToInvest = (int) (Math.random() * 49) + 1;
			investment = new Invest(this, hyip, amountToInvest, hyipOffert);
		} else if (risk_level == InvestorType.MEDIUM_AVERSION) {
			amountToInvest = (int) (Math.random() * 450) + 50;
			investment = new Invest(this, hyip, amountToInvest, hyipOffert);
		} else if (risk_level == InvestorType.LOW_AVERSION) {
			amountToInvest = (int) (Math.random() * 4500) + 500;
			investment = new Invest(this, hyip, amountToInvest, hyipOffert);
		}
		if (amountToInvest <= investorAccount.getBalance()) {
			investorAccount.spendMoney(amountToInvest);
			hyip.registerInvestment(investment);
		} else if (amountToInvest >= MINIMAL_INVESTMENT_TO_CONSIDER) {
			investorAccount.spendMoney(investorAccount.getBalance());
			hyip.registerInvestment(investment);
		}
	}

	public void acceptReward(double moneyTransfer) {
		this.investorAccount.addFunds(moneyTransfer);
	}

	@SuppressWarnings("unchecked")
	private Set<Hyip> chooseAllProducers(boolean allowFrozen) {
		Context<Player> context = ContextUtils.getContext(this);
		Iterable<Player> it = context.getObjects(Hyip.class);
		Set<Hyip> result = new HashSet<Hyip>();
		Iterator<Player> iterator = it.iterator();
		while (iterator.hasNext()) {
			Hyip hyip = (Hyip) iterator.next();
			if (allowFrozen)
				result.add(hyip);
			else if (!hyip.getFrozen())
				result.add(hyip);
		}
		return result;
	}

	public int getExpertise() {
		return expertise;
	}

}
