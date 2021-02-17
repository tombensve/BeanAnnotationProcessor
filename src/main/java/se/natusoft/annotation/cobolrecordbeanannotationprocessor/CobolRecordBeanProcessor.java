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
package se.natusoft.annotation.cobolrecordbeanannotationprocessor;

import se.natusoft.annotation.cobolrecordbeanannotationprocessor.annotations.CobolRecordBean;
import se.natusoft.annotation.processor.simplified.SimplifiedAnnotationProcessor;
import se.natusoft.annotation.processor.simplified.annotations.Process;
import se.natusoft.annotation.processor.simplified.annotations.*;
import se.natusoft.annotation.processor.simplified.codegen.GenerationSupport;
import se.natusoft.annotation.processor.simplified.codegen.JavaSourceOutputStream;
import se.natusoft.annotation.processor.simplified.model.SAPAnnotation;
import se.natusoft.annotation.processor.simplified.model.SAPType;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This generates the class that the annotated file extends.
 */
@SuppressWarnings("DuplicatedCode")
@AutoDiscovery
@ProcessedAnnotations({ CobolRecordBean.class })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CobolRecordBeanProcessor extends SimplifiedAnnotationProcessor {
    //
    // Private Members
    //

    private static final boolean verbose = true;

    private List<Element> toGenerate = null;


    //
    // Constructors
    //

    public CobolRecordBeanProcessor() {
        super( verbose );
    }

    //
    // Methods
    //

    /**
     * Called when a new round of processing is started. Called by
     * SimplifiedAnnotationProcessor, triggered by compiler.
     */
    @NewRound
    public void newRound() {
        this.toGenerate = new LinkedList<Element>();
    }

    /**
     * Called to process an annotation. Called for each annotation of specified type.
     * Called by SimplifiedAnnotationProcessor, triggered by compiler.
     *
     * @param annotatedElements The elements that was annotated.
     */
    @Process(CobolRecordBean.class)
    public void processBean( Set<? extends Element> annotatedElements ) {
        for ( Element element : annotatedElements ) {
            SAPType type = new SAPType( element );

            this.toGenerate.add( element );
        }
    }

    /**
     * Generates code.
     * Called by SimplifiedAnnotationProcessor, triggered by compiler.
     *
     * @param generationSupport Tool class to help with code generation.
     */
    @GenerateSource
    public void generate( GenerationSupport generationSupport ) {
        try {

            for ( Element annotatedElement : this.toGenerate ) {

                // This defaults to true, which will generate getters and setters for everything.
                // But when a use=true is specified for a record value then this switches to false,
                // and only use=true will result in a getter and setter. This way all fields are
                // always provided and to String() will always include the full record. When use=true
                // setters and getters are only generated for properties that have the use=true.
                //
                // This allows for only providing getters and setters for values you actually use
                // but the while record is read and provided on toString(), but only those with
                // use=true is readable and modifiable.
                boolean useAllFields = true;

                SAPType type = new SAPType( (TypeElement) annotatedElement );

                // Check for "useAllFields". If any use=true then the use flag will apply and
                // those without a true value will be excluded.
                {
                    SAPAnnotation cobolRecordBeanAnnotation = type.getAnnotationByClass( CobolRecordBean.class );

                    AnnotationValue annVal = cobolRecordBeanAnnotation.getAnnotationValueFor( "value" );
                    @SuppressWarnings("unchecked")
                    List<AnnotationMirror> props = (List<AnnotationMirror>) annVal.getValue();

                    // Get total record size
                    int recordSize = 0;
                    for ( AnnotationMirror propMirror : props ) {
                        SAPAnnotation propAnn = new SAPAnnotation( propMirror );

                        if ( propAnn.getValueFor( "use" ).toBoolean() ) useAllFields = false;
                    }
                }

                //==== Start write of class ====//

                String genQName = type.getPackage() + "." + type.getExtendsSimpleName();
                JavaSourceOutputStream jos = generationSupport.getToBeCompiledJavaSourceOutputStream( genQName, annotatedElement );
                jos.packageLine( type.getPackage() );
                jos.generatedAnnotation( getClass().getName(), "" );
                jos.begClass( "public", "abstract", type.getExtendsSimpleName() );
                jos.begMethod( "protected", "", "", type.getExtendsSimpleName() );
                jos.endMethod();
                jos.emptyLine();

                //==== CRBDate for support of date values ====/

                // Note that this block of code was actually written in another class and verified, and then
                // copied and pasted here. Thereby only println(...) statements rather than begClass(...),
                // begMethod(...), etc.
                //
                // Also note that I'm not entirely convinced that this CRBDate class is a good idea ...
                jos.println( "    public static class CRBDate {" );
                jos.println( "        private int size;" );
                jos.println( "        private String date = \"      \";" );
                jos.println( "        private java.text.SimpleDateFormat sdf;" );
                jos.println( "        " );
                jos.println( "        public CRBDate( String date ) {" );
                jos.println( "           this( date, \"yyMMdd\" );" );
                jos.println( "        }" );
                jos.println( "        " );
                jos.println( "        public CRBDate( String date, String dateFormat ) {" );
                jos.println( "            if ( dateFormat != null && dateFormat.length() != 0 ) {" );
                jos.println( "                this.size = dateFormat.length();" );
                jos.println( "                this.date = date.substring( 0, this.size );" );
                jos.println( "                this.sdf = new java.text.SimpleDateFormat( dateFormat );" );
                jos.println( "            }" );
                jos.println( "        }" );
                jos.println( "        " );
                jos.println( "        public CRBDate( java.util.Date date, String dateFormat ) {" );
                jos.println( "            if ( dateFormat == null ) throw new IllegalArgumentException( \"Must supply date format!\" );" );
                jos.println( "            " );
                jos.println( "            this.sdf = new java.text.SimpleDateFormat( dateFormat );" );
                jos.println( "            this.date = this.sdf.format( date );" );
                jos.println( "        }" );
                jos.println( "        " );
                //                   See comment below! Had to name match this also.
                jos.println( "        public java.util.Date getDate() {" );
                jos.println( "           java.util.Date date = null;" );
                jos.println( "           if ( this.sdf != null ) {" );
                jos.println( "               try {" );
                jos.println( "                    date = this.sdf.parse( this.date );" );
                jos.println( "                } catch ( java.text.ParseException pe ) {" );
                jos.println( "                    throw new IllegalArgumentException( \"Date format and actual date string does not match!\", pe );" );
                jos.println( "                }" );
                jos.println( "            }" );
                jos.println( "            return date;" );
                jos.println( "        }" );
                jos.println( "        " );
                jos.println( "        public CRBDate setDate( java.util.Date date ) {" );
                jos.println( "            if ( this.sdf == null ) {" );
                jos.println( "                throw new IllegalArgumentException( \"No date format has been provided!\" );" );
                jos.println( "            }" );
                jos.println( "            this.date = this.sdf.format( date );" );
                jos.println( "            return this;" );
                jos.println( "        }" );
                jos.println( "        " );
                jos.println( "        public CRBDate setDate( String date ) {" );
                jos.println( "            this.date = date;" );
                jos.println( "             if (this.date.length() > this.size) this.date = this.date.substring(0, size );" );
                jos.println( "            return this;" );
                jos.println( "        }" );
                jos.println( "        " );
                jos.println( "        public String toString() {" );
                jos.println( "            return this.date;" );
                jos.println( "        }" );
                jos.println( "    }" );
                jos.println( "    " );

                //==== Static support method to ensure field size. ====//

                jos.println( "    private static String ensureSize( String value, int size ) {" );
                jos.println( "        if ( value.length() > size ) {" );
                jos.println( "            value = value.substring( 0, size );" );
                jos.println( "        } else while ( value.length() < size ) {" );
                jos.println( "            value = value + \" \";" );
                jos.println( "        }" );
                jos.println( "        return value;" );
                jos.println( "    }" );
                jos.println( "    " );

                {
                    SAPAnnotation cobolRecordBeanAnnotation = type.getAnnotationByClass( CobolRecordBean.class );
                    boolean pure = (boolean) cobolRecordBeanAnnotation.getAnnotationValueFor( "pure" ).getValue();

                    AnnotationValue annVal = cobolRecordBeanAnnotation.getAnnotationValueFor( "value" );
                    @SuppressWarnings("unchecked")
                    List<AnnotationMirror> props = (List<AnnotationMirror>) annVal.getValue();

                    // Get total record size
                    int recordSize = 0;
                    for ( AnnotationMirror propMirror : props ) {
                        SAPAnnotation propAnn = new SAPAnnotation( propMirror );
                        int propSize = propAnn.getValueFor( "size" ).toInt();
                        recordSize += propSize;
                    }

                    //==== Constructor ====//

                    jos.beginConstructorMethod( type.getExtendsSimpleName() );
                    {
                        jos.methodArg( "String", "cobolRecord" );

                        // Validate that the passed string has expected size.
                        jos.println( "        if (cobolRecord.length() != " + recordSize +
                                ") throw new IllegalArgumentException(\"Bad record size (\" + cobolRecord.length() + \")! Expected: " +
                                recordSize + "!\" );" );

                        int position = 0;
                        for ( AnnotationMirror propMirror : props ) {
                            SAPAnnotation propAnn = new SAPAnnotation( propMirror );

                            boolean use = propAnn.getValueFor( "use" ).toBoolean();

                            String propName = propAnn.getValueFor( "name" ).toString();
                            int propSize = propAnn.getValueFor( "size" ).toInt();
                            String dateFormat = propAnn.getValueFor( "dateFormat" ).toString();


                            String fieldType = dateFormat.length() == 0 ? "String" : "CRBDate";
                            if ( fieldType.equals( "CRBDate" ) ) {
                                jos.println( "        this." + propName + " = new CRBDate(cobolRecord.substring(" +
                                        position + "," + ( position + propSize ) + "));" );
                            } else {
                                jos.println( "        this." + propName + " = cobolRecord.substring(" + position + ", " +
                                        ( position + propSize ) + ");" );
                            }
                            position += propSize;
                        }
                    }
                    jos.endMethod();
                    jos.println( "" );

                    //==== Properties ====//

                    for ( AnnotationMirror propMirror : props ) {
                        SAPAnnotation propAnn = new SAPAnnotation( propMirror );
                        String propName = propAnn.getValueFor( "name" ).toString();
                        int propSize = propAnn.getValueFor( "size" ).toInt();
                        String dateFormat = propAnn.getValueFor( "dateFormat" ).toString();
                        boolean use = propAnn.getValueFor( "use" ).toBoolean();

                        //==== Property field ====//

                        verbose( "Generating property: " + propName );

                        String fieldType = dateFormat.length() == 0 ? "String" : "CRBDate";

                        if ( fieldType.equals( "CRBDate" ) ) {
                            jos.field( "private", fieldType, propName, "new CRBDate(ensureSize(\"\", " + propSize + "))" );
                        } else {
                            jos.field( "private", fieldType, propName, "ensureSize(\"\"," + propSize + ")" );
                        }

                        //==== Getters and Setters ====//

                        if ( useAllFields || use ) {

                            jos.begMethod( "public", "", pure ? "void" : type.getQualifiedName(), setterName( propName ) );
                            {
                                // Dates require special handling
                                jos.methodArg( fieldType, "value" );
                                if ( fieldType.equals( "CRBDate" ) ) {
                                    jos.println( "        this." + propName + " = value;" );
                                    jos.println( "    " );
                                    jos.println( "        this." + propName + ".setDate(ensureSize(this." + propName + ".toString(), " + propSize + "));" );
                                } else {
                                    jos.println( "        this." + propName + " = ensureSize(value.toString(), " + propSize + ");" );
                                }

                                if ( !pure ) {
                                    jos.println( "        return (" + type.getQualifiedName() + ")this;" );
                                }
                            }
                            jos.endMethod();

                            jos.begMethod( "public", "", fieldType, getterName( propName ) );
                            {
                                jos.println( "        return this." + propName + ";" );
                            }
                            jos.endMethod();
                            jos.emptyLine();
                        }

                    }

                    //==== toString() ====//

                    jos.begMethod( "public", "", "String", "toString" );
                    {
                        jos.println( "        StringBuilder sb = new StringBuilder();" );
                        for ( AnnotationMirror propMirror : props ) {
                            SAPAnnotation propAnn = new SAPAnnotation( propMirror );
                            String propName = propAnn.getValueFor( "name" ).toString();

                            jos.println( "        sb.append(this." + propName + ");" );
                        }
                        jos.println( "        return sb.toString();" );
                    }
                    jos.endMethod();

                }
                jos.endClass();

                jos.close();
            }
        } catch ( IOException ioe ) {
            failCompile( "@CobolRecordBean processor failed to generate bean!", ioe );
        }
    }

    //==== Support methods ====//

    private String setterName( String propName ) {
        return "set" + propName.substring( 0, 1 ).toUpperCase() + propName.substring( 1 );
    }

    private String getterName( String propName ) {
        return "get" + propName.substring( 0, 1 ).toUpperCase() + propName.substring( 1 );
    }

    /**
     * Called when all processing is done.
     */
    @AllProcessed
    public void done() {
    }

}
