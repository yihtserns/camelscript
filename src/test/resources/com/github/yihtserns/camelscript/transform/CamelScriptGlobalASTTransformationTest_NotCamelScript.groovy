routes {
    from('direct:input').process { it.out.body = 'Result' }
}