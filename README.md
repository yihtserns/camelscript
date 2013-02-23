Camel Script
============

**Note**: Not yet available from central maven repository

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


**Reference**: [Simplest way I know to create a simple HTTP server using Groovy](http://zefifier.wordpress.com/2012/07/06/quickest-way-i-know-to-create-a-simple-http-server-using-groovy/)
