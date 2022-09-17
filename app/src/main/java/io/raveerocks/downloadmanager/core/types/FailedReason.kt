package io.raveerocks.downloadmanager.core.types

import io.raveerocks.downloadmanager.R

enum class FailedReason(val reason: Int) {
    FILE_ERROR(R.string.message_reason_file_error),
    NETWORK_ERROR(R.string.message_reason_network_error),
    MEMORY_ERROR(R.string.message_reason_memory_error),
    CANNOT_RESUME_ERROR(R.string.message_reason_cannot_resume),
    FILE_ALREADY_EXISTS_ERROR(R.string.message_reason_duplicate),
    HTTP_ERROR(R.string.message_reason_resource_request_failed),
    UNKNOWN_ERROR(R.string.message_reason_unknown),
}