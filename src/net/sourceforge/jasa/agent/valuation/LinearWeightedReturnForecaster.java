package net.sourceforge.jasa.agent.valuation;

import java.io.Serializable;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;
import net.sourceforge.jasa.market.Market;
import cern.jet.random.AbstractContinousDistribution;

public class LinearWeightedReturnForecaster extends AbstractReturnForecaster
		implements Serializable {

	protected AbstractReturnForecaster[] forecasters;
	
	protected double[] weights;
	
	protected AbstractContinousDistribution[] distributions;

	@Override
	public double getReturnForecast(Market auction) {
		double result = 0.0;
		for(int i=0; i<forecasters.length; i++) {
			double forecast = forecasters[i].getReturnForecast(auction);
			result += weights[i] * forecast;
		}
		return result;
	}
	
	@Override
	public void eventOccurred(SimEvent event) {
		super.eventOccurred(event);
		if (event instanceof SimulationStartingEvent) {
			onSimulationStarting();
		}
	}
	
	public void onSimulationStarting() {
		initialiseWeights();
	}
	
	public void initialiseWeights() {
		weights = new double[distributions.length];
		for(int i=0; i<distributions.length; i++) {
			weights[i] = distributions[i].nextDouble();
		}
	}

	@Override
	public void subscribeToEvents(EventScheduler scheduler) {
		super.subscribeToEvents(scheduler);
		scheduler.addListener(SimulationStartingEvent.class, this);
		for(int i=0; i<forecasters.length; i++) {
			forecasters[i].subscribeToEvents(scheduler);
		}
	}

	@Override
	public void setValuationPolicy(ValuationPolicy policy) {
		super.setValuationPolicy(policy);
		for(int i=0; i<forecasters.length; i++) {
			forecasters[i].setValuationPolicy(policy);
		}
	}

	public AbstractReturnForecaster[] getForecasters() {
		return forecasters;
	}

	public void setForecasters(AbstractReturnForecaster[] forecasters) {
		this.forecasters = forecasters;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public AbstractContinousDistribution[] getDistributions() {
		return distributions;
	}

	public void setDistributions(AbstractContinousDistribution[] distributions) {
		this.distributions = distributions;
	}
	
	
	
}
