CamelScript
============
Provides syntactic sugar for Apache Camel to make it easy to set up in Groovy script.

Requirement
-----------
Groovy 2.2.2

Overview
--------
This project enables you to simplify this type of Groovy script:
```groovy
// MyScript.groovy
@Grab('org.apache.camel:camel-jetty:2.4.0')
@Grab('org.slf4j:slf4j-simple:1.6.6')
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.builder.RouteBuilder

def camelContext = new DefaultCamelContext()
camelContext.addRoutes(new RouteBuilder() {
    void configure() {
        from('jetty:http://localhost:8090/hello/world').transform(constant('Hello World!'))
    }
})
camelContext.start()
```
into this instead:
```groovy
// MyScript.camel <-- Change file extension to .camel to turn this script into a CamelScript
@Grab('com.github.yihtserns:camelscript:0.0.1')
@Grab('org.apache.camel:camel-jetty:2.4.0')
import groovy.*

routes {
    from('jetty:http://localhost:8090/hello/world').transform(constant('Hello World!'))
}
// Auto-start
```

API
---
Property | Description
-------- | -----------
`camelContext` | The underlying `CamelContext` instance.
`log` | Out-of-the-box SLF4J logger instance. **Note**: `print(Object)`, `printf(String, Object)`, `printf(String, Object[])`, `println()`, and `println(Object)` method calls are delegated to `log.info`.

Method | Description
------ | -----------
`routes(Closure)` | Syntactic sugar for `addRoutes(RoutesBuilder)`.
`waitForever()` | Some routes do not block (e.g. `from("file:...")`) so use this to prevent the script from ending/exiting/terminating.

Route building
--------------
### Process
```groovy
from('jetty:http://localhost:8090/hello/world').process {exchange -> exchange.out.body = 'Hello World!'}
```

### Transform
```groovy
from('jetty:http://localhost:8090/hello/world').transform {exchange, type -> return 'Hello World!'}
```

### Filter
```groovy
from('jetty:http://localhost:8090/hello/world').filter {exchange -> exchange.in.body == 'Hi'}.transform {exchange, type -> return 'Hello World'}
```

### Multi-line route building
```groovy
from('jetty:http://localhost:8090/hello/world') {
    filter {exchange -> exchange.in.body == 'Hi'}
    transform {exchange, type -> return 'Hello World'}
}
```

### Dynamic route building
Iterate over a range:
```groovy
from('jetty:http://localhost:8090/hello/world') {
    (1..3).each {
        transform { exchange, type -> return exchange.in.body + 'Hi' }
    }
}
// Calling http://localhost:8089/hello/world returns 'HiHiHi'
```

Iterate over a list:
```groovy
from('jetty:http://localhost:8090/hello/world') {
    ['a', 'b', 'c'].each { item ->
        transform { exchange, type -> return exchange.in.body + item }
    }
}
// Calling http://localhost:8089/hello/world returns 'abc'
```

Conditional:
```groovy
boolean rudeMode = Boolean.getBoolean('rudeMode')

routes {
    from('jetty:http://localhost:8090/hello/world') {
        transform(constant('Hello World'))
        if (rudeMode) {
            transform { exchange, type -> exchange.in.body.toUpperCase() }
        }
    }
}
// When -DrudeMode=true, calling http://localhost:8090/hello/world returns 'HELLO WORLD'
```

### On Exception
```
onException(Exception).process { println 'Exception Swallowed' }.handled(true)

from('direct:input').process { throw new Exception('Simulated') }
```


### [Registry](http://camel.apache.org/registry.html)
Registry in Camel Script is backed by the [Groovy script's binding](http://groovy.codehaus.org/api/groovy/lang/Binding.html), so anything placed in the latter can be referenced.

#### [Referring to Objects from URI scheme](http://camel.apache.org/configuring-camel.html#ConfiguringCamel-WorkingwithSpringXML)
```groovy
// Adapted from http://camel.apache.org/jms.html#JMS-UsingJNDItofindtheConnectionFactory
@Grab('com.github.yihtserns:camelscript:0.0.1')
@Grab('org.apache.activemq:activemq-core:5.5.0')
@Grab('org.apache.camel:camel-jms:2.4.0')
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.component.jms.JmsComponent

activemq = new JmsComponent(connectionFactory: new ActiveMQConnectionFactory(brokerURL: 'tcp://localhost:1444'))

routes {
    from('activemq:FROM') { // Will look for 'activemq' in binding
        process { println it.in.body }
    }
}
```

#### [Referring to Objects from URI query string](http://camel.apache.org/configuring-camel.html#ConfiguringCamel-ReferringbeansfromEndpointURIs)
```groovy
@Grab('com.github.yihtserns:camelscript:0.0.1')
@Grab('org.apache.activemq:activemq-core:5.5.0')
@Grab('org.apache.camel:camel-jms:2.4.0')
import org.apache.activemq.ActiveMQConnectionFactory

amcf = new ActiveMQConnectionFactory(brokerURL: 'tcp://localhost:1444')

routes {
    // Both 'from' and 'to' will be using the same ActiveMQConnectionFactory instance
    from('jms:FROM?connectionFactory=#amcf').to('jms:TO?connectionFactory=#amcf')
}
```

### Type Conversion
```groovy
@Grab('com.github.yihtserns:camelscript:0.0.1')
import groovy.*

routes {
    from('file:start?delete=true') {
        // process { println it.in.getBody(String) }
        process { println it.in as String }
    }
}
waitForever()
```

Logging
-------
CamelScript ships with a default [`logback.xml`](http://logback.qos.ch).  `log.level` flag can be used to change the [log level](http://logback.qos.ch/manual/architecture.html#basic_selection) e.g. `groovy -Dlog.level=DEBUG MyScript.camel`

If you want to further change the logging, [use your own custom `logback.xml`](http://logback.qos.ch/manual/configuration.html#configFileProperty).

Change Camel version
--------------------
```groovy
@Grab('org.apache.camel:camel-core:2.15.0')  // Must be declared before camelscript
@Grab('com.github.yihtserns:camelscript:0.0.1')
import groovy.*

routes {
    ...
}
```

Gotcha
------
1. If getting this:
```
unexpected token: routes @ line 5, column 1.
   routes {
   ^

1 error
```
Make sure `@Grab` is followed by at least one `import` statement.
