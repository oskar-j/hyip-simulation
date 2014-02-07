package CredibilityGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.ContextUtils;
import CredibilityGame.rating.strategy.RatingStrategy;

public class Investor extends Player {

	public static HashMap<String, Double> PAYOFFS = new HashMap<String, Double>();
	private static int CONSUMER_TYPE_H;
	private static int CONSUMER_TYPE_L;
	private InvestorType risk_level;
	private InvestorAccount invest_money; // do zmiany
	private double inv_invest;
	private double inv_rec;
	private int expertise;
	private RatingStrategy ratingStrategy;

	public Investor(InvestorType investorType) {
		this.expertise = RandomHelper.createUniform(CONSUMER_TYPE_L,
				CONSUMER_TYPE_H).nextInt();
		Parameters params = RunEnvironment.getInstance().getParameters();
		inv_invest = (Double) params.getValue("inv_invest");
		inv_rec = (Double) params.getValue("inv_rec");
		risk_level = investorType;
	}

	public void initializeWallet() {
		if (invest_money != null) {
			throw new UnsupportedOperationException(
					"Agent (investor) already charged his account!");
		} else {
			invest_money = new InvestorAccount(this);
			switch (risk_level) {
			case HIGH_AVERSION:
				invest_money.setBalance(50 * 5);
				break;
			case MEDIUM_AVERSION:
				invest_money.setBalance(50 * 50);
				break;
			case LOW_AVERSION:
				invest_money.setBalance(50 * 1000);
				break;
			}
		}
	}

	@ScheduledMethod(start = 1.0, interval = 1.0, priority = 100)
	public void step() {
		if (invest_money.hasMoney()) {
			int howManyInvests = RandomHelper.nextIntFromTo(0, 5);
			// (inwestr losuje, ile inwestycji chce dokonac w danym ticku)
			for (int i = 0; i < howManyInvests; i++) {
				// losuje 3 hyip-y, z ktorych wybiera ten z najlepszym
				// marketingiem
				List<Hyip> hyipsConsiderated = chooseProducers(3);
				Hyip chosen = hyipsConsiderated.get(0);
				// teraz wybierz ten z najlepszym marketingiem
				for(Hyip hyip : hyipsConsiderated){
					if(hyip.getAdvert() > chosen.getAdvert())
						chosen = hyip;
				}
				chooseOffert(chosen);
			}
		}

//		// invest_money = (int) (invest_money * (1 + hyipOwner.perc));
//		if (Math.random() < inv_invest + hyipOwner.getAdvert())
//			chooseOffert(hyipOwner);
//		if (Math.random() < inv_rec)
//			hyipOwner.makeWithdrawal(invest_money);
	}

	private void chooseOffert(Hyip hyip) {
		invest(hyip, hyip.getFirstOffert());
	}

	private void invest(Hyip hyip, HyipOffert hyipOffert) {
		int invest = 0;
		if (risk_level == InvestorType.HIGH_AVERSION)
			invest = (int) (Math.random() * 4) + 1;
		new Invest(hyip, invest, hyipOffert);
		if (risk_level == InvestorType.MEDIUM_AVERSION)
			invest = (int) (Math.random() * 45) + 5;
		new Invest(hyip, invest, hyipOffert);
		if (risk_level == InvestorType.LOW_AVERSION)
			invest = (int) (Math.random() * 950) + 50;
		new Invest(hyip, invest, hyipOffert);
		invest_money.spendMoney(invest);
		hyip.acceptDeposit(invest);
	}

	private Hyip chooseProducer() {
		Context<Player> context = ContextUtils.getContext(this);
		return (Hyip) context.getRandomObjects(Hyip.class, 1).iterator().next();
	}

	private List<Hyip> chooseProducers(int howMany) {
		Context<Player> context = ContextUtils.getContext(this);
		Iterable<Player> it = context.getRandomObjects(Hyip.class, 3);
		List<Hyip> result = new ArrayList<Hyip>();
		Iterator<Player> iterator = it.iterator();
		for (int i = 0; i < howMany; i++) {
			result.add((Hyip) iterator.next());
		}
		return result;
	}

	public static void reset() {
		for (Object p : CredibilityGame.PLAYERS.getObjects(Investor.class)) {
			((Investor) p).setGain(0);
			((Investor) p).getStrategy().clear();
		}
	}

	public int getExpertise() {
		return expertise;
	}

	public void setExpertise(int expertise) {
		this.expertise = expertise;
	}

	public RatingStrategy getRatingStrategy() {
		return ratingStrategy;
	}

	public void setRatingStrategy(RatingStrategy ratingStrategy) {
		this.ratingStrategy = ratingStrategy;
	}
}
