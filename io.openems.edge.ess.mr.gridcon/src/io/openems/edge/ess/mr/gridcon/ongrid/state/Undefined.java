package io.openems.edge.ess.mr.gridcon.ongrid.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.edge.ess.mr.gridcon.EssGridcon;
import io.openems.edge.ess.mr.gridcon.IState;
import io.openems.edge.ess.mr.gridcon.State;
import io.openems.edge.ess.mr.gridcon.battery.SoltaroBattery;

public class Undefined extends BaseState implements State {

	private final Logger log = LoggerFactory.getLogger(Undefined.class);

	public Undefined(EssGridcon gridconPCS, SoltaroBattery b1, SoltaroBattery b2, SoltaroBattery b3) {
		super(gridconPCS, b1, b2, b3);
	}

	@Override
	public IState getState() {
		return io.openems.edge.ess.mr.gridcon.ongrid.State.UNDEFINED;
	}

	@Override
	public IState getNextState() {
		// According to the state machine the next state can only be Stopped, ERROR, RUN
		if (isNextStateUndefined()) {
			return io.openems.edge.ess.mr.gridcon.ongrid.State.UNDEFINED;
		}
		
		if (isNextStateError()) {
			return io.openems.edge.ess.mr.gridcon.ongrid.State.ERROR;
		}
		
		if (isNextStateRunning()) {
			return io.openems.edge.ess.mr.gridcon.ongrid.State.RUN;
		}
		
		if (isNextStateStopped()) {
			return io.openems.edge.ess.mr.gridcon.ongrid.State.STOPPED;
		}
		
		return io.openems.edge.ess.mr.gridcon.ongrid.State.UNDEFINED;
	}

	

	@Override
	public void act() {
		log.info("Nothing to do!");
	}
}
