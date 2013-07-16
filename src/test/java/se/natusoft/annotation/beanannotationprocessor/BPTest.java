package se.natusoft.annotation.beanannotationprocessor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
/**
 */
@RunWith(JUnit4.class)
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
