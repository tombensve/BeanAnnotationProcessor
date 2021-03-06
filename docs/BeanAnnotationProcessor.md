# BeanAnnotationProcessor

This is a very simple java bean annotation processor. 

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface Property {
    
        /** The name of the property. */
        String name();
    
        /** The type of the property, default String. */
        Class type() default String.class;
    
        /** Generics types for the type. */
        Class[] generics() default {};

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
    
        /** If set to true then a standard compliant java bean will be generated. */
        boolean pure() default false;
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

When this is compiles AddressBean will be generated with private fields and setters and getters. And **no**, there is no class name + Bean name standard! It will generate whatever class name the annotated class extends. You can call it what you want!

If any property has specified _required=true_ then a `public void validate()` method will also be generated. Calling that method will throw an IllegalStateException if any of the required fields are null. The exception message will list the offending fields.

**Note** that the generated setters are not exactly "standard". They return _this_ cast as the annotated type! This means you can do the following:

    Address address = new Address().setName("Tommy Svensson").setStreetName("Gronbrinksgatan").setStreetNo(9)
        .setZip(11759).setCity("Stockholm");

**Note** that this behavior is the default! You can however set the _pure_ property on `@Bean` to force a standard compliant java bean that also plays nice with for example Groovy. 

To generate a Collection property like a List do something like this:

    @Property(name="myList", type=List.class, generics={String.class})

If you want the list initialized from the beginning so that you just can do _...getMyList().add(...)_ declare the property like this:

    @Property(name="myList", type=List.class, generics={String.class}, init="new LinkedList<String>()")

## Dependencies

This annotation processor depends on [SimplifiedAnnotationProcessor](https://github.com/tombensve/SimplifiedAnnotationProcessor).

## Requirements

This requires Java 8 or higher.

## Usage

All you need to use this is to have the annotation processor (and the SimplifiedAnnotationProcessor) on the compile class path.

## Maven Usage

    <dependencies>
        ...
        <dependency>
            <groupId>se.natusoft.annotation</groupId>
            <artifactId>bean-annotation-processor</artifactId>
            <version>1.3</version>
            <scope>provided</scope>
         </dependency>
         ...
    </dependencies>

The reason for the `<scope>provided</scope>` is that this is only needed during compile, not runtime.

# CobolRecordBeanProcessor (Since 1.3)

There is another variant of "Java Bean" processor: CobolRecordBeanProcessor. It takes the following annotations:

        @CobolRecordBean(
            value = {
                @RecordProperty( name="test", size = 10),
                @RecordProperty( name="date", size = 6, dateFormat = "YYMMDD"),
                @RecordProperty( name = "number", size = 12, use=true),
            }
        )
        public class TestCobolRecord extends TestCobolRecordBean { }

The date field is actually a string also, but it is wrapped by an 
inner generated CRBDate instance. This allows
for getting and setting date as `java.util.Date` instances. `toString()` will return the date in String format.
It also holds `java.text.SimpleDateFormat` specification for converting between `Date` and `String`. 

The `use` property is by default false, and when all properties
have this as false then everything will be generated, but when
one or more `use` is set to true then only those set to true
will have a generated setter and getter. All will always have
fields and will alsway be included in toString() and constructor
will fill all fields. It is only getters and setters that get 
affected. The reason for this is when you actually don't use
the full record, only parts of it. In that case you specify
the records you are interested in with `use=true` and you
will only get setters and getters for those.

The order of the `@RecordProperty(...)` annotations are important. Only size is specified in this annotation.
The start and end position of each field in the Cobol record is calculated using size and position. You do not
have to specify from and to, only size. This also means that if fields are added to the record, then you just
add annotations for these new fields at the correct place, and it will work. If the from and to positions 
had been specified then all fields after inserted field would have to be updated! Not so with this solution.

Numeric values have no special handling like dates! You have to use Integer/Long/Float/Double.valueOf(str);

# Available at

**Note:** This is available in Bintrays JCenter, not in maven central!
