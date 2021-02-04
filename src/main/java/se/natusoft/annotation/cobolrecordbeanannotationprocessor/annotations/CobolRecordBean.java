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
 *         2013-07-15: Created!
 *
 */
package se.natusoft.annotation.cobolrecordbeanannotationprocessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This indicates that the annotated type is a JavaBean.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface CobolRecordBean {
    RecordProperty[] value();

    /** If set to true then a standard compliant java bean will be generated. */
    boolean pure() default false;
}
