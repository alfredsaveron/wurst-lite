/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package kaptainwutax.seedcrackerX.util;

import java.util.function.BiPredicate;

public class Predicates
{
	
	public static BiPredicate<Integer, Integer> EQUAL_TO = Integer::equals;
	public static BiPredicate<Integer, Integer> NOT_EQUAL_TO =
		(a, b) -> !a.equals(b);
	public static BiPredicate<Integer, Integer> LESS_THAN = (a, b) -> a < b;
	public static BiPredicate<Integer, Integer> MORE_THAN = (a, b) -> a > b;
	public static BiPredicate<Integer, Integer> LESS_OR_EQUAL_TO =
		(a, b) -> a <= b;
	public static BiPredicate<Integer, Integer> MORE_OR_EQUAL_TO =
		(a, b) -> a >= b;
	
}
