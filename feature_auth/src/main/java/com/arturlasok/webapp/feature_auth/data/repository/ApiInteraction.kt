package com.arturlasok.webapp.feature_auth.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiInteraction(
    private val ktorClient: HttpClient
) {
    fun getServerTime() : Flow<String> = flow {
        emit(ktorClient.get("http://server873539.nazwa.pl/servertime-plu").bodyAsText())
    }
}