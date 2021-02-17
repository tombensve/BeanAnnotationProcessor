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
 * This specifies a cobol record field. The order of the annotation is important
 * since positions will be calculated from this.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface RecordProperty {

    /** The name of the property. */
    String name();

    /** The size of the record field. */
    int size();

    /** The date format if its a date field. (SimpleDateFormat). */
    String dateFormat() default "";

    /** The group the property belongs to. Documentative, optional. */
    String group() default "";

    /** A description of the property. Documentative, Optional. */
    String description() default "";

    /**
     * If this is set to true for any of the records then this will have
     * effect and only those with use=true will be generated as bean properties.
     *
     * Do note that all records are still needed even those with use=false
     * since they are needed to calculate position of each field.
     *
     * When all are false this feature is disabled and all fields will be
     * included.
     */
    boolean use() default false;

}
