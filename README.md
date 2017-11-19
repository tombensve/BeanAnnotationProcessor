# BeanAnnotationProcessor

Copyright © 2013 Natusoft AB

__Version:__ 1.3

__Author:__ Tommy Svensson (tommy@natusoft.se)

----

_Generates java beans using an @Bean annotation._

[Docs](https://github.com/tombensve/BeanAnnotationProcessor/blob/master/docs/BeanAnnotationProcessor.md)

[Licenses](https://github.com/tombensve/SimplifiedAnnotationProcessor/blob/master/licenses.md)

----

## History

### Version 1.3

* Now expects source level 1.8.

* Updated dependency versions.

### Version 1.2

* Fixed bug that generated non compilable code when annotating an inner class. Part of that fix was in _SimplifiedAnnotationProcessor_ so version 1.1 of that is now used.

* Now always uses fully qualified return type in generated classes when pure is false to support annotated inner classes.


