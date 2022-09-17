package io.raveerocks.downloadmanager.core.types

import io.raveerocks.downloadmanager.R
import java.util.concurrent.TimeUnit

enum class TimeGranularity(val descriptor: Int) {
    SECOND(R.string.description_second) {
        override fun toMillis(): Long {
            return TimeUnit.MINUTES.toMillis(1)
        }
    },
    MINUTE(R.string.description_minute) {
        override fun toMillis(): Long {
            return TimeUnit.MINUTES.toMillis(1)
        }
    },
    HOUR(R.string.description_hour) {
        override fun toMillis(): Long {
            return TimeUnit.HOURS.toMillis(1)
        }
    },
    DAY(R.string.description_day) {
        override fun toMillis(): Long {
            return TimeUnit.DAYS.toMillis(1)
        }
    },
    WEEK(R.string.description_week) {
        override fun toMillis(): Long {
            return TimeUnit.DAYS.toMillis(7)
        }
    },
    MONTH(R.string.description_month) {
        override fun toMillis(): Long {
            return TimeUnit.DAYS.toMillis(30)
        }
    };

    abstract fun toMillis(): Long
}