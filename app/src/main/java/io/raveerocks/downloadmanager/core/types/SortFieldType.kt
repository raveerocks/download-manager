package io.raveerocks.downloadmanager.core.types

import io.raveerocks.downloadmanager.data.db.dao.DoneDownloadDAO

enum class SortFieldType(val inverse: SortFieldType?, val column: Int, val sortOrder: Int) {

    NONE(NONE, DoneDownloadDAO.COLUMN_ID, DoneDownloadDAO.SORT_DESC),
    TITLE_DESC(null, DoneDownloadDAO.COLUMN_TITLE, DoneDownloadDAO.SORT_DESC),
    DOMAIN_DESC(null, DoneDownloadDAO.COLUMN_DOMAIN, DoneDownloadDAO.SORT_DESC),
    SIZE_DESC(null, DoneDownloadDAO.COLUMN_SIZE, DoneDownloadDAO.SORT_DESC),
    COMPLETED_AT_DESC(null, DoneDownloadDAO.COLUMN_COMPLETED_AT, DoneDownloadDAO.SORT_DESC),

    TITLE_ASC(TITLE_DESC, DoneDownloadDAO.COLUMN_TITLE, DoneDownloadDAO.SORT_ASC),
    DOMAIN_ASC(DOMAIN_DESC, DoneDownloadDAO.COLUMN_DOMAIN, DoneDownloadDAO.SORT_ASC),
    SIZE_ASC(SIZE_DESC, DoneDownloadDAO.COLUMN_SIZE, DoneDownloadDAO.SORT_ASC),
    COMPLETED_AT_ASC(
        COMPLETED_AT_DESC,
        DoneDownloadDAO.COLUMN_COMPLETED_AT,
        DoneDownloadDAO.SORT_ASC
    );

    fun isASC(): Boolean {
        return inverse != null
    }
}
