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
/**
 * 
 */
package org.evosuite.junit;

import java.io.File;

import org.evosuite.Properties;
import org.evosuite.utils.Utils;

/**
 * <p>
 * CoverageReportGenerator class
 * </p>
 * 
 * @author José Campos
 */
public class CoverageReportGenerator {

	public static void writeCoverage(boolean[][] coverage, Properties.Criterion criterion) {

		StringBuilder suite = new StringBuilder();
		for (int i = 0; i < coverage.length; i++) {
			StringBuilder test = new StringBuilder();

			for (int j = 0; j < coverage[i].length - 1; j++) {
				if (coverage[i][j])
					test.append("1 ");
				else
					test.append("0 ");
			}

			if (!test.toString().contains("1")) // if a test case does not contains a "1", means it does not coverage anything
				continue ;

			if (coverage[i][coverage[i].length - 1])
				test.append("+\n");
			else
				test.append("-\n");

			suite.append(test);
		}

		Utils.writeFile(suite.toString(), new File(getReportDir().getAbsolutePath() +
				File.separator + "data" + File.separator +
				Properties.TARGET_CLASS + "." + criterion.toString() + ".matrix"));
	}

	/**
     * Return the folder of where reports should be generated.
     * If the folder does not exist, try to create it
     * 
     * @return
     * @throws RuntimeException if folder does not exist, and we cannot create it
     */
    private static File getReportDir() throws RuntimeException{
        File dir = new File(Properties.REPORT_DIR);

        if (!dir.exists()) {
            if (!dir.mkdirs())
                throw new RuntimeException("Cannot create report dir: " + Properties.REPORT_DIR);
        }

        return dir;
    }
}
