package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel.model.UploadFileResponse
import at.mikemitterer.catshostel.services.UploadService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


/**
 *
 *
 * @since   14.05.20, 11:20
 */
@RestController
class UploadController(private val uploadService: UploadService) {
    // private val logger: Logger = LoggerFactory.getLogger(UploadController::class.java)

    @PostMapping("/upload")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile): UploadFileResponse {
        return uploadService.uploadFile(file)
    }
}

