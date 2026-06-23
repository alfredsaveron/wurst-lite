/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.cracker;

import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.cracker.storage.TimeMachine;

@FunctionalInterface
public interface DataAddedEvent
{
	
	DataAddedEvent POKE_PILLARS =
		s -> s.getTimeMachine().poke(TimeMachine.Phase.PILLARS);
	DataAddedEvent POKE_STRUCTURES =
		s -> s.getTimeMachine().poke(TimeMachine.Phase.STRUCTURES);
	DataAddedEvent POKE_LIFTING =
		s -> s.getTimeMachine().poke(TimeMachine.Phase.LIFTING);
	DataAddedEvent POKE_BIOMES =
		s -> s.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
	
	void onDataAdded(DataStorage dataStorage);
	
}
