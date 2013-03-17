Camel Script
============

**Note**: Not yet available from central maven repository

Introduction
------------
This project enables you to simplify this type of Groovy Script:
```groovy
@Grab('org.apache.camel:camel-jetty:2.4.0')
@Grab('org.slf4j:slf4j-simple:1.6.6')
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.builder.RouteBuilder
import javax.servlet.http.HttpServletRequest

def context = new DefaultCamelContext()
context.addRoutes(new RouteBuilder() {
    void configure() {
        from('jetty:http://localhost:8090/hello/world').process({ it.out.body = "Hello World!" } as Processor)

        from('jetty:http://localhost:8090/byeworld.txt').process({ it.out.body = "Buh bye" } as Processor)

        from('jetty:http://localhost:8091/').process ({
          def httpRequest = it.in.getBody(HttpServletRequest)
          String name = httpRequest.getParameter('name')

          it.out.body = "Yay $name!"
        } as Processor)
    }
})
context.start()
```

into this instead:
```groovy
@CamelScript // This whole script is a CamelContext now
package test

@Grab('com.github.yihtserns:camelscript:1.0.0')
import javax.servlet.http.HttpServletRequest

routes {
    // Don't have to coerce closure to Processor anymore
    from('jetty:http://localhost:8090/hello/world').process { it.out.body = "Hello World!" }

    from('jetty:http://localhost:8090/byeworld.txt').process { it.out.body = "Buh bye" }

    from('jetty:http://localhost:8091/').process {
      def httpRequest = it.in.getBody(HttpServletRequest)
      String name = httpRequest.getParameter('name')

      it.out.body = "Yay $name!"
    }
}
start()
```

[Referring to Objects from URI query string] [bean-querystring]
-------------------------
```groovy
// Adapted from http://camel.apache.org/file2.html#File2-Filterusingorg.apache.camel.component.file.GenericFileFilter
@CamelScript
package test

@Grab('com.github.yihtserns:camelscript:1.0.0')
import org.apache.camel.component.file.GenericFileFilter
import org.apache.camel.component.file.GenericFile

// Add object to binding
myFilter = { GenericFile file -> !file.fileName.startsWith('skip') } as GenericFileFilter

routes {
    from('file://inbox?filter=#myFilter') // Will look for 'myFilter' in binding
        .process { println it.in.getBody(String) }
}
start()
Thread.currentThread().join() // Wait forever
```

[Referring to Objects from URI scheme] [bean-urischeme]
------------------------------------------------------
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

// Add object to binding
activemq = new JmsComponent(connectionFactory: new ActiveMQConnectionFactory(brokerURL: 'tcp://localhost:1444'))

routes {
    from('activemq:MyQueue') // Will look for 'activemq' in binding
        .process { println it.in.body }
}
start()
```

References
----------
[bean-querystring]: http://camel.apache.org/configuring-camel.html#ConfiguringCamel-ReferringbeansfromEndpointURIs
[bean-urischeme]: http://camel.apache.org/configuring-camel.html#ConfiguringCamel-WorkingwithSpringXML
[Simplest way I know to create a simple HTTP server using Groovy](http://zefifier.wordpress.com/2012/07/06/quickest-way-i-know-to-create-a-simple-http-server-using-groovy/)
