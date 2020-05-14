package at.mikemitterer.catshostel.services

import at.mikemitterer.catshostel.model.UploadFileResponse
import at.mikemitterer.catshostel.services.exceptions.FileStorageException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


/**
 * Upload File
 *
 * @since   14.05.20, 11:34
 */
@Service
class UploadService {
    private val logger: Logger = LoggerFactory.getLogger(UploadService::class.java)

    @Value("\${app.upload.dir:\${user.home}}")
    private lateinit var uploadDir: String

    fun uploadFile(file: MultipartFile): UploadFileResponse {
        try {
            val cleanedFile = StringUtils.cleanPath(checkNotNull(file.originalFilename))
            val copyLocation = Paths
                    .get(uploadDir
                            + File.separator
                            + cleanedFile
                    )

            logger.debug("Uploading $cleanedFile to $uploadDir...")
            Files.copy(file.inputStream, copyLocation, StandardCopyOption.REPLACE_EXISTING)

            val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(uploadDir)
                    .path(cleanedFile)
                    .toUriString()

            return UploadFileResponse(cleanedFile, fileDownloadUri, checkNotNull(file.contentType), file.size)

        } catch (e: Exception) {
            e.printStackTrace()
            throw FileStorageException("Could not store file " + file.originalFilename
                    + ". Please try again!")
        }
    }
}