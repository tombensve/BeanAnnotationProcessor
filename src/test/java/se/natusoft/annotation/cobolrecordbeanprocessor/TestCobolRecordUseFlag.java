package se.natusoft.annotation.cobolrecordbeanprocessor;

import se.natusoft.annotation.cobolrecordbeanannotationprocessor.annotations.CobolRecordBean;
import se.natusoft.annotation.cobolrecordbeanannotationprocessor.annotations.RecordProperty;

@CobolRecordBean(
        value = {
                @RecordProperty( name="test", size = 10, use = true),
                @RecordProperty( name="date", size = 6, dateFormat = "yyMMdd", use = true),
                @RecordProperty( name = "ingore", size = 16  ),
                @RecordProperty( name = "number", size = 12, use = true)
        }
)
public class TestCobolRecordUseFlag extends TestCobolRecordUseFlagBean {

        public TestCobolRecordUseFlag() {}

        public TestCobolRecordUseFlag( String data) {
                super(data);
        }
}
