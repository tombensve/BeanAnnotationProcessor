/* 
 * 
 * PROJECT
 *     Name
 *         bean-annotation-processor
 *     
 *     Code Version
 *         1.0
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
package se.natusoft.annotation.beanannotationprocessor;

import se.natusoft.annotation.beanannotationprocessor.annotations.Bean;
import se.natusoft.annotation.processor.simplified.SimplifiedAnnotationProcessor;
import se.natusoft.annotation.processor.simplified.annotations.*;
import se.natusoft.annotation.processor.simplified.annotations.Process;
import se.natusoft.annotation.processor.simplified.codegen.GenerationSupport;
import se.natusoft.annotation.processor.simplified.codegen.JavaSourceOutputStream;
import se.natusoft.annotation.processor.simplified.model.SAPAnnotation;
import se.natusoft.annotation.processor.simplified.model.SAPType;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This generates the class that the annotated file extends.
 */
@AutoDiscovery
@ProcessedAnnotations({Bean.class})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class BeanProcessor extends SimplifiedAnnotationProcessor {
    //
    // Private Members
    //

    private static final boolean verbose = true;

    private List<Element> toGenerate = null;


    //
    // Constructors
    //

    public BeanProcessor() {
        super(verbose);
    }

    //
    // Methods
    //

    @NewRound
    public void newRound() {
        this.toGenerate = new LinkedList<>();
    }

    @Process(Bean.class)
    public void processBean(Set<? extends Element> annotatedElements) {
        for (Element element : annotatedElements) {
            this.toGenerate.add(element);
        }
    }

    @GenerateSource
    public void generate(GenerationSupport generationSupport) {
        try {
            for (Element annotatedElement : this.toGenerate) {
                SAPType type = new SAPType((TypeElement)annotatedElement);

                String genQName = type.getPackage() + "." + type.getExtends();
                JavaSourceOutputStream jos = generationSupport.getToBeCompiledJavaSourceOutputStream(genQName);
                jos.packageLine(type.getPackage());
                jos.generatedAnnotation(getClass().getName(), "");
                jos.begClass("public", "abstract", type.getExtends());
                jos.begMethod("protected", "", "", type.getExtends());
                jos.endMethod();
                jos.emptyLine();
                {
                    SAPAnnotation beanAnnotation = type.getAnnotationByClass(Bean.class);

                    AnnotationValue annVal = beanAnnotation.getAnnotationValueFor("value");
                    List<AnnotationMirror> props = (List<AnnotationMirror>)annVal.getValue();

                    List<String> nonNullProps = new LinkedList<>();

                    for (AnnotationMirror propMirror : props) {
                        SAPAnnotation propAnn = new SAPAnnotation(propMirror);
                        String propName = propAnn.getValueFor("name").toString();
                        String propType = propAnn.getValueFor("type").toString();
                        String propDef = propAnn.getValueFor("init").toString();
                        boolean required = propAnn.getValueFor("required").toBoolean();

                        if (required) {
                            nonNullProps.add(propName);
                        }

                        verbose("Generating property: " + propName);
                        String defValue = propDef.trim().length() > 0 ? propDef : null;
                        jos.field("private", propType, propName, defValue);

                        jos.begMethod("public", "", type.getSimpleName(), setterName(propName));
                        {
                            jos.methodArg(propType, "value");
                            jos.println("        this." + propName + " = value;");
                            jos.println("        return (" + type.getSimpleName() + ")this;");
                        }
                        jos.endMethod();

                        jos.begMethod("public", "", propType, getterName(propName));
                        {
                            jos.println("        return this." + propName + ";");
                        }
                        jos.endMethod();
                        jos.emptyLine();
                    }

                    if (!nonNullProps.isEmpty()) {
                        jos.begMethod("public", "", "void", "validate");
                        jos.println("        StringBuilder sb = new StringBuilder();");
                        jos.println("        String space = \"\";");
                        for (String propName : nonNullProps) {
                            jos.println("        if (this." + propName + " == null) {");
                            jos.println("            sb.append(space);");
                            jos.println("            space = \" \";");
                            jos.println("            sb.append(\"'" + propName + "' cannot be null!\");");
                            jos.println("        }");
                        }
                        jos.println("        if (sb.length() > 0) throw new IllegalStateException(sb.toString());");
                        jos.endMethod();
                    }
                }
                jos.endClass();
                jos.close();
            }
        }
        catch (IOException ioe) {
            failCompile("@Bean processor failed to generate bean!", ioe);
        }
    }

    private String setterName(String propName) {
        return "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
    }

    private String getterName(String propName) {
        return "get" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
    }

    @AllProcessed
    public void done() {
    }

}
