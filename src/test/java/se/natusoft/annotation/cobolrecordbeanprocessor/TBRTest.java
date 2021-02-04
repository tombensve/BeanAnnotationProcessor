package se.natusoft.annotation.cobolrecordbeanprocessor;

import org.junit.Test;

public class TBRTest {

    @Test
    public void testCobolRecord() {
        TestCobolRecord tcr = new TestCobolRecord();
        tcr.getDate().setDate( "20210204" ); // 6 chars  -> '04' will be cut!
        tcr.setTest( "Tommy" );              // 10 chars -> Only 5, so 5 spaces will be added.
        tcr.setNumber( "12345678901234" );   // 12 chars -> 14 chars, so last 2 will be cut.

        System.out.println(">" + tcr.toString() + "<");

        assert tcr.toString().equals( "Tommy     202102123456789012" );

        TestCobolRecord tcr2 = new TestCobolRecord("Tommy     202102123456789012");

        System.out.println("date   ==>" + tcr2.getDate().toString() + "<==");
        System.out.println("test   ==>" + tcr2.getTest() + "<==");
        System.out.println("number ==>" + tcr2.getNumber() + "<==");

        assert tcr2.getDate().toString().equals( "202102" );
        assert tcr2.getTest().equals( "Tommy     " );
        assert tcr2.getNumber().equals( "123456789012" );
    }
}
