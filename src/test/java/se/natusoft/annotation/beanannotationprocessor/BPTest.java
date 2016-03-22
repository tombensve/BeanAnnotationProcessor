/* 
 * 
 * PROJECT
 *     Name
 *         bean-annotation-processor
 *     
 *     Code Version
 *         1.2
 *     
 *     Description
 *         This provides some variants of annotation processors producing JavaBeans.
 *         
 * COPYRIGHTS
 *     Copyright (C) 2013 by Natusoft AB All rights reserved.
 *     
 * LICENSE
 *     Apache 2.0 (Open Source)
 *     
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *     
 *       http://www.apache.org/licenses/LICENSE-2.0
 *     
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *     
 * AUTHORS
 *     tommy ()
 *         Changes:
 *         2016-03-22: Created!
 *         
 */
package se.natusoft.annotation.beanannotationprocessor;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 */
public class BPTest {

    @Test
    public void testAnn() {
        TestModel testModel = new TestModel().setName("Tommy Svensson").setAge(45);
        testModel.validate();

        assertEquals(testModel.getAddress(), "Address");
        assertEquals(testModel.getName(), "Tommy Svensson");
        assertEquals(testModel.getAge(), 45);
    }
}
