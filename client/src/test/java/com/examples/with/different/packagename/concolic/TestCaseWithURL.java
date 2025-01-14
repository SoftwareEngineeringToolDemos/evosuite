/**
 * Copyright (C) 2010-2015 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser Public License as published by the
 * Free Software Foundation, either version 3.0 of the License, or (at your
 * option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License along
 * with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.examples.with.different.packagename.concolic;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class TestCaseWithURL {

	public boolean test(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		Scanner in = new Scanner(conn.getInputStream());
		String line = in.nextLine();
		in.close();
		if (line.contains("<html>")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isZero(int value) {
		if (value == 0) {
			return true;
		} else {
			return false;
		}
	}
}
