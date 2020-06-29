package com.vp.favorite.usecase

data class CompositeExecutionException(val errors: List<Throwable>) : Exception()