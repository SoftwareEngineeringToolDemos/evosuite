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
package org.evosuite.jee;

import com.examples.with.different.packagename.jee.db.SimpleDBInteraction;
import org.evosuite.Properties;
import org.evosuite.SystemTest;
import org.junit.Test;

/**
 * Created by Andrea Arcuri on 17/08/15.
 */
public class DBSystemTest extends SystemTest {

    @Test
    public void testSimpleDBInteraction(){
        Properties.JEE = true;
        do100percentLineTest(SimpleDBInteraction.class);
    }
}
