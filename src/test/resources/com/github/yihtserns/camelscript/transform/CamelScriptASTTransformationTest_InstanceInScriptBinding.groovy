@CamelScript
package com.github.yihtserns.camelscript.transform

import org.apache.camel.Processor
import org.apache.camel.Exchange
import org.apache.camel.Endpoint
import org.apache.camel.impl.DefaultComponent
import org.apache.camel.impl.ProcessorEndpoint

// Class definitions
class MyProcessor implements Processor {
    MyBean valueProvider;

    void process(Exchange exchange) {
        exchange.out.body = valueProvider.value
    }
}
class MyBean {
    String getValue() {
        'Result'
    }
}

// Variable definitions
myComponent = new DefaultComponent() {
    Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        def processor = new MyProcessor()
        setProperties(processor, parameters)
        return new ProcessorEndpoint(uri, this, processor)
    }
}
myBean = new MyBean()

// Execute
routes {
    from('direct:input').to('myComponent:dummy?valueProvider=#myBean')
}

start()

createProducerTemplate().requestBody('direct:input', (Object) null)