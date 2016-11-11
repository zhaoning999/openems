/*******************************************************************************
 * OpenEMS - Open Source Energy Management System
 * Copyright (c) 2016 FENECON GmbH and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *   FENECON GmbH - initial API and implementation and initial documentation
 *******************************************************************************/
package io.openems.impl.controller.debuglog;

import java.util.Optional;
import java.util.Set;

import io.openems.api.channel.ConfigChannel;
import io.openems.api.controller.Controller;
import io.openems.api.exception.InvalidValueException;

public class DebugLogController extends Controller {

	public final ConfigChannel<Set<Ess>> esss = new ConfigChannel<Set<Ess>>("esss", this, Ess.class).optional();
	public final ConfigChannel<Set<SymmetricMeter>> symmetricMeters = new ConfigChannel<Set<SymmetricMeter>>(
			"symmetricMeters", this, SymmetricMeter.class).optional();
	public final ConfigChannel<Set<AsymmetricMeter>> asymmetricMeters = new ConfigChannel<Set<AsymmetricMeter>>(
			"asymmetricMeters", this, AsymmetricMeter.class).optional();
	public final ConfigChannel<RealTimeClock> rtc = new ConfigChannel<RealTimeClock>("rtc", this, RealTimeClock.class)
			.optional();

	@Override public void run() {
		try {
			System.out.println(esss.valueOptional());
			System.out.println(symmetricMeters.valueOptional());
			System.out.println(asymmetricMeters.valueOptional());
			System.out.println(rtc.valueOptional());
			StringBuilder b = new StringBuilder();
			if (symmetricMeters.valueOptional().isPresent()) {
				for (SymmetricMeter meter : symmetricMeters.value()) {
					b.append(meter.id() + ": " + meter.activePower.format() + " ");
				}
			}
			if (asymmetricMeters.valueOptional().isPresent()) {
				for (AsymmetricMeter meter : asymmetricMeters.value()) {
					b.append(meter.id() + " L1: " + meter.activePowerL1.format() + ", " + meter.reactivePowerL1.format()
							+ ", L2: " + meter.activePowerL2.format() + ", " + meter.reactivePowerL2.format() + ", L3: "
							+ meter.activePowerL3.format() + ", " + meter.reactivePowerL3.format() + "");
				}
			}
			if (rtc.valueOptional().isPresent()) {
				RealTimeClock r = rtc.value();
				b.append(rtc.id() + " " + r.year.format() + "-" + r.month.format() + "-" + r.day.format() + " "
						+ r.hour.format() + ":" + r.minute.format() + ":" + r.second.format());
			}
			if (esss.valueOptional().isPresent()) {
				for (Ess ess : esss.value()) {
					b.append(ess.id() + " [" + ess.soc.format() + "] " //
					// + "Act[" + ess.activePower.format() + "] " //
							+ "Charge[" + ess.allowedCharge.format() + "] " //
							+ "Discharge[" + ess.allowedDischarge.format() + "] " //
							+ "State[" + ess.systemState.format() + "]");
					Optional<String> warning = ess.warning.labelOptional();
					if (warning.isPresent()) {
						b.append(" Warning[" + warning.get() + "]");
					}
				}
			}
			log.info(b.toString());
		} catch (InvalidValueException e) {
			e.printStackTrace();
		}
	}

}
