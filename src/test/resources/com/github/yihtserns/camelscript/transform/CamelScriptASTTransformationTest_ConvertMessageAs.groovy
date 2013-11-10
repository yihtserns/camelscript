@CamelScript
package com.github.yihtserns.camelscript.transform

import org.apache.camel.Exchange
import org.apache.camel.TypeConverter

typeConverterRegistry.addTypeConverter(Date, String, new TypeConverter() {

        def mandatoryConvertTo(Class type, Object value) {
            Date.parse('yyyy-MM-dd', value)
        }

        def convertTo(Class type, Object value) {
            mandatoryConvertTo(type, value)
        }
        def convertTo(Class type, Exchange exchange, Object value) {
            mandatoryConvertTo(type, value)
        }

        def mandatoryConvertTo(Class type, Exchange exchange, Object value) {
            mandatoryConvertTo(type, value)
        }
    })

routes {
    from 'direct:input' process { it.out.body = it.in as Date }
}

createProducerTemplate().requestBody('direct:input', (Object) '2013-04-26')