# BeanAnnotationProcessor

Copyright © 2013 Natusoft AB

__Version:__ 2.0.2

__Author:__ Tommy Svensson (tommy@natusoft.se)

----

_Generates java beans using an @Bean annotation._

[Docs](https://github.com/tombensve/BeanAnnotationProcessor/blob/master/docs/BeanAnnotationProcessor.md)

[Licenses](https://github.com/tombensve/SimplifiedAnnotationProcessor/blob/master/licenses.md)

----

## History

### 2.0.2

Added `use=...` property on @RecordProperty used by
@CobolRecordBean. If this is set to true for any property
then setters and getters are only generated for those fields
that have `use=true`.

### 2.0.1

Added validation of passed string size to generated 
CobolRecordBean. Throws an IllegalArgumentException if
size is bad. This is a clear indication of that the
record specification have changed, and the @RecordPropery
annotations need to be updated.

### 2.0

One more processor: CobolRecordBeanProcessor. This uses
@CobolRecordBean with multiple @RecordProperty. To read it
takes a Cobol record String, passed to the String constructor.

After instantiated, all fields can be accessed with setters and
getters just like a JavaBean. A `toString()` will produce a
Cobol record String again. You only specify name and record size
in `©RecordProperty`, positions are calculated by order, which 
makes it easy to add or remove properties.

I did something similar with this (BeanAnnotationProcessor) at a
customer a few years ago, to handle Cobol data from Swedish
department of transportation, but only checked it in locally 
since this was something I would never need again ... which was
wrong! Now somewhere else, I need to read similar data from
department of transportation again, so I fixed it again, and 
this time I check it in with the project at GitHub. It is
completely based on BeanProcessor.

Never say never!

### Version 1.3

* Now expects source level 1.8.

* Updated dependency versions.

### Version 1.2

* Fixed bug that generated non compilable code when annotating an inner class. Part of that fix was in _SimplifiedAnnotationProcessor_ so version 1.1 of that is now used.

* Now always uses fully qualified return type in generated classes when pure is false to support annotated inner classes.


