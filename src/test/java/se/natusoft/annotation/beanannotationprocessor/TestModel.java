package se.natusoft.annotation.beanannotationprocessor;

import se.natusoft.annotation.beanannotationprocessor.annotations.Bean;
import se.natusoft.annotation.beanannotationprocessor.annotations.Property;

/**
 */
@Bean({
        @Property(name="name", required = true),
        @Property(name = "address", init = "\"Address\""),
        @Property(name = "age", type = int.class)
})
public class TestModel extends TestModelBean {}
