package at.mikemitterer.catshostel.model

/**
 *
 *
 * @since   14.05.20, 12:18
 */
class UploadFileResponse(
        val fileName: String,
        val fileDownloadUri: String,
        val fileType: String,
        val size: Long)
