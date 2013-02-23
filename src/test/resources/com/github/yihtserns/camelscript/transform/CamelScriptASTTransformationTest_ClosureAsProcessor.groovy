@CamelScript
package com.github.yihtserns.camelscript.transform

import org.apache.camel.builder.*

routes {
    from('direct:input').process { it.out.body = 'Result' }
}

start()

createProducerTemplate().requestBody('direct:input', (Object) null)