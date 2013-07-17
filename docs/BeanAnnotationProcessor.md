# BeanAnnotationProcessor

This is a very simple java bean annotation processor. 

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface Property {
    
        /** The name of the property. */
        String name();
    
        /** The type of the property, default String. */
        Class type() default String.class;
    
        /** The default value of the property, default none. */
        String init() default "";
    
        /** A description of the property. */
        String description() default "";
    
        /** If true this property is required. */
        boolean required() default false;
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface Bean {
        Property[] value();
    }

Just annotate a class with @Bean({@Property(...), ...}):

    @Bean({
        @Property(name="name"),
        @Property(name="streetName"),
        @Property(name="streetNo", type=int.class),
        @Property(name="zip", type=int.class),
        @Property(name="city"),
        @Property(name="country", type=Country.class, init="new Country(\"SE\")")
    })
    public class Address extends AddressBean {}
 
When this is compiles AddressBean will be generated with private fields and setters and getters. And **no**, there is no class name + Bean name standard! It will generated whatever class name the annotated class extends. You can call it what you want!

If any property has specified _required=true_ then a `public void validate()` method will also be generated. Calling that method will throw an IllegalStateException if any of the required fields are null. The exception message will list the offending fields.

**Note** that the generated setters are not exactly "standard". They return _this_ cast as the annotated type! This means you can do the following:

    Address address = new Address().setName("Tommy Svensson").setStreetName("Gronbrinksgatan").setStreetNo(9)
        .setZip(11759).setCity("Stockholm");

## Dependencies

This annotation processor depends on [SimplifiedAnnotationProcessor](https://github.com/tombensve/SimplifiedAnnotationProcessor).

## Requirements

This requires Java 6 or higher.

## Usage

All you need to use this is to have the annotation processor (and the SimplifiedAnnotationProcessor) on the compile class path.

## Maven Usage

    <dependencies>
        ...
        <dependency>
            <groupId>se.natusoft.annotation</groupId>
            <artifactId>bean-annotation-processor</artifactId>
            <version>1.0</version>
            <scope>provided</scope>
         </dependency>
         ...
    </dependencies>

    <repositories>
        <repository>
            <id>maven-natusoft-se</id>
            <name>Natusofts maven repository</name>
            <url>http://maven.natusoft.se/</url>
        </repository>
    </repositories>

The reason for the `<scope>provided</scope>` is that this is only needed during compile, not runtime.

