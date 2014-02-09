Camel Script
============
Provides syntactic sugar for Apache Camel to make it easy to configure in Groovy script.

**Note**: Not yet available from central maven repository

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

def context = new DefaultCamelContext()
context.addRoutes(new RouteBuilder() {
    void configure() {
        from('jetty:http://localhost:8090/hello/world').transform(constant('Hello World!'))
    }
})
context.start()
```

into this instead:
```groovy
// MyScript.camel <-- File extension must be .camel
@Grab('com.github.yihtserns:camelscript:0.0.1')
@Grab('org.apache.camel:camel-jetty:2.4.0')
import groovy.*

routes {
    from('jetty:http://localhost:8090/hello/world').transform(constant('Hello World!'))
}
// Auto-start
```

Route building
--------------
### Process
```groovy
from('jetty:http://localhost:8090/hello/world').process {exchange -> exchange.out.body = 'Hello World!'}
```
### Transform
```groovy
from('jetty:http://localhost:8090/hello/world').transform {exchange -> return 'Hello World!'}
```
### Filter
```groovy
from('jetty:http://localhost:8090/hello/world').filter {exchange -> exchange.in.body == 'Hi'}.transform {exchange -> return 'Hello World'}
```
### Multi-line route building
```groovy
from('jetty:http://localhost:8090/hello/world') {
    filter {exchange -> exchange.in.body == 'Hi'}
    transform {exchange -> return 'Hello World'}
}
```
### [Registry](http://camel.apache.org/registry.html)
Registry in Camel Script is backed by the [Groovy script's binding](http://groovy.codehaus.org/api/groovy/lang/Binding.html), so anything placed in the latter can be referenced.

#### [Referring to Objects from URI query string](http://camel.apache.org/configuring-camel.html#ConfiguringCamel-ReferringbeansfromEndpointURIs)
```groovy
// Adapted from http://camel.apache.org/file2.html#File2-Filterusingorg.apache.camel.component.file.GenericFileFilter
@CamelScript
package test

@Grab('com.github.yihtserns:camelscript:1.0.0')
import org.apache.camel.component.file.GenericFileFilter
import org.apache.camel.component.file.GenericFile

myFilter = { GenericFile file -> !file.fileName.startsWith('skip') } as GenericFileFilter

routes {
    from('file://inbox?filter=#myFilter') { // Will look for 'myFilter' in binding
        process { println it.in.getBody(String) }
    }
}
waitForever()
```

#### [Referring to Objects from URI scheme](http://camel.apache.org/configuring-camel.html#ConfiguringCamel-WorkingwithSpringXML)
```groovy
// Adapted from http://camel.apache.org/jms.html#JMS-UsingJNDItofindtheConnectionFactory
@CamelScript
package test

@Grab('com.github.yihtserns:camelscript:1.0.0')
@Grab('org.apache.activemq:activemq-core:5.5.0')
@Grab('org.apache.camel:camel-jms:2.4.0')
@Grab('org.slf4j:slf4j-simple:1.6.0')
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.component.jms.JmsComponent

activemq = new JmsComponent(connectionFactory: new ActiveMQConnectionFactory(brokerURL: 'tcp://localhost:1444'))

routes {
    from('activemq:MyQueue') { // Will look for 'activemq' in binding
        process { println it.in.body }
    }
}
```

#### Closure
// TODO:

Gotcha
------
1. If getting this:
```
unexpected token: routes @ line 5, column 1.
   routes {
   ^

1 error
```
Make sure ```@Grab``` is followed by at least one ```import``` statement.