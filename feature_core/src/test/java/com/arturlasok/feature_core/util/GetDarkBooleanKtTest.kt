package com.arturlasok.feature_core.util

import org.junit.Assert.*

import org.junit.Test

class GetDarkBooleanKtTest {

    @Test
    fun getDarkBoolean() {
        assertEquals(true, com.arturlasok.feature_core.util.getDarkBoolean(true,0))
    }
}