package com.machete3845.news_data

import com.machete3845.news_data.RequestResult.InProgress
import com.machete3845.news_data.RequestResult.Success

interface MergeStrategy<E> {

    fun merge(right: E, left: E): E
}

internal class DefaultRequestResponseMergeStrategy<T: Any>: MergeStrategy<RequestResult<T>>
{

    override fun merge(cache: RequestResult<T>, server: RequestResult<T>): RequestResult<T> {
    return when{
        cache is InProgress && server is InProgress -> merge(cache, server)
        cache is Success && server is InProgress -> merge(cache, server)
        cache is InProgress && server is Success -> merge(cache, server)
        cache is Success && server is RequestResult.Error -> merge(cache, server)
        cache is InProgress && server is RequestResult.Error -> merge(cache, server)
        cache is Success && server is Success -> merge(cache, server)
        else -> error("Unimplemented branch: server: ${server}, cache: ${cache}")
    }
    }

    private fun merge(
        cache: InProgress<T>,
        server: InProgress<T>
    ): RequestResult<T>{
        return if (server.data != null){
            InProgress(server.data)
        } else{
            InProgress(cache.data)
        }
    }

    private fun merge(
        cache: InProgress<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(data = server.data ?: cache.data, error = server.error)
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T>{
        return when{
            server.data != null -> RequestResult.InProgress(server.data)
            else -> RequestResult.InProgress(cache.data)
        }
    }

    private fun merge(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T>{
        return RequestResult.InProgress(server.data)
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T>{
        return RequestResult.Error(data = cache.data, error = server.error)
    }

    private fun merge(
        cache: Success<T>,
        server: Success<T>
    ): RequestResult<T> {
        return Success(data = server.data)
    }

}