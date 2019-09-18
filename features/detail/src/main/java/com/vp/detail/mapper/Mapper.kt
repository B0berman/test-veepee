package com.vp.detail.mapper

/**
 * Created by Uxio Lorenzo on 2019-09-10.
 */
interface Mapper<I, O> {

    fun map(from: I): O
}