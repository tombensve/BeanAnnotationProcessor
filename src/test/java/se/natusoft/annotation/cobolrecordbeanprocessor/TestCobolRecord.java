package se.natusoft.annotation.cobolrecordbeanprocessor;

import se.natusoft.annotation.cobolrecordbeanannotationprocessor.annotations.CobolRecordBean;
import se.natusoft.annotation.cobolrecordbeanannotationprocessor.annotations.RecordProperty;

@CobolRecordBean(
        value = {
                @RecordProperty( name="test", size = 10),
                @RecordProperty( name="date", size = 6, dateFormat = "yyMMdd"),
                @RecordProperty( name = "number", size = 12)
        }
)
public class TestCobolRecord extends TestCobolRecordBean {

        public TestCobolRecord() {}

        public TestCobolRecord(String data) {
                super(data);
        }
}
