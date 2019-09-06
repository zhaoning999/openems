package io.openems.edge.controller.ess.limitdischargecellvoltage.state;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.controller.ess.limitdischargecellvoltage.Config;
import io.openems.edge.controller.ess.limitdischargecellvoltage.IState;
import io.openems.edge.controller.ess.limitdischargecellvoltage.State;
import io.openems.edge.ess.api.SymmetricEss;

public class Undefined implements IState {

	private final Logger log = LoggerFactory.getLogger(Undefined.class);
	private ComponentManager componentManager;
	private Config config;

	public Undefined(ComponentManager componentManager, Config config) {
		this.componentManager = componentManager;
		this.config = config;
	}

	@Override
	public State getState() {
		return State.UNDEFINED;
	}

	@Override
	public IState getNextStateObject() {
		// According to the state machine the next states can be normal, critical,
		// warning or undefined
		SymmetricEss ess;
		try {
			ess = this.componentManager.getComponent(this.config.ess_id());
		} catch (OpenemsNamedException e) {
			log.error(e.getMessage());
			return this;
		}
		Optional<Integer> minCellVoltageOpt = ess.getMinCellVoltage().value().asOptional();
		if (!minCellVoltageOpt.isPresent()) {
			return this;
		}

		int minCellVoltage = minCellVoltageOpt.get();

		if (minCellVoltage < this.config.criticalCellVoltage()) {
			return new Critical(this.componentManager, this.config);			
		}

		if (minCellVoltage < this.config.warningCellVoltage()) {
			return new Warning(this.componentManager, this.config);
		}

		return new Normal(this.componentManager, this.config);
	}

	@Override
	public void act() {
		log.info("Undefined.act() --> nothing to do");
	}
}
