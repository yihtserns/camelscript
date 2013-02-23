@CamelScript
package com.github.yihtserns.camelscript.transform

import org.apache.camel.builder.*

addRoutes(new RouteBuilder() {
    void configure() {
        from('direct:input').transform(constant('Result'))
    }
})

start()

createProducerTemplate().requestBody('direct:input', (Object) null)