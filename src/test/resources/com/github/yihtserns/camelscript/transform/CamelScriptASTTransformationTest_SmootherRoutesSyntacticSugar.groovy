@CamelScript
package com.github.yihtserns.camelscript.transform

import org.apache.camel.builder.*

routes {
    from 'direct:input' transform constant('Res') process { it.out.body = it.in.body + 'u' } process { it.out.body = it.in.body + 'lt'}
}

createProducerTemplate().requestBody('direct:input', (Object) null)