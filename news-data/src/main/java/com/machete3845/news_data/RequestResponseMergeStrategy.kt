package com.machete3845.news_data

interface RequestResponseMergeStrategy<E> {

    fun merge(right: RequestResult<E>, left: RequestResult<E>): RequestResult<E>{

    }
}

private class DefaultRequestResponseMergeStrategy