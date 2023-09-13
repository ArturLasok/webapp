package com.arturlasok.webapp.feature_auth.util

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.TimeUnit

fun TimeMilisecondsTo(dateTimeType: DateTimeType,milliseconds: Long) :String {

    return when(dateTimeType) {

        DateTimeType.DATE_MEDIUM ->
        {
            LocalDateTime.ofEpochSecond(
                TimeUnit.MILLISECONDS.toSeconds(milliseconds),0,
                OffsetDateTime.now().offset).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        }
        DateTimeType.TIME_H_AND_MIN ->
        {
            LocalDateTime.ofEpochSecond(
                TimeUnit.MILLISECONDS.toSeconds(milliseconds),0,
                OffsetDateTime.now().offset
            ).format(DateTimeFormatter.ISO_TIME).substringBeforeLast(":")
        }

    }


}

enum class DateTimeType {
    DATE_MEDIUM,TIME_H_AND_MIN
}