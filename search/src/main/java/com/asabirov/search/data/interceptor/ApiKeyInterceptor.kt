package com.asabirov.search.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val url = original.url.newBuilder().addQueryParameter("key", "AIzaSyDElqkAo17lD7C9VvD-3oBRYDgBi0lytEE").build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}