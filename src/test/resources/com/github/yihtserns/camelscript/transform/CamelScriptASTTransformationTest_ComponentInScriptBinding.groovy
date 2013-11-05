@CamelScript
package com.github.yihtserns.camelscript.transform

import org.apache.camel.component.direct.DirectComponent

inDirect = new DirectComponent()

routes {
    from('inDirect:input').transform(constant('Result'))
}

createProducerTemplate().requestBody('inDirect:input', (Object) null)