@CamelScript
package com.github.yihtserns.camelscript.transform

import org.apache.camel.builder.*

routes {
    from('direct:input').transform(constant('Result'))
}

createProducerTemplate().requestBody('direct:input', (Object) null)