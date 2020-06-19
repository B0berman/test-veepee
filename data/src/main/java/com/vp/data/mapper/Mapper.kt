package com.vp.data.mapper

interface Mapper<I, O> {

    fun map(input: I): O
}
