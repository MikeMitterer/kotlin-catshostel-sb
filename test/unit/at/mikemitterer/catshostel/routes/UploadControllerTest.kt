package at.mikemitterer.catshostel.routes

import at.mikemitterer.catshostel.model.UploadFileResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.nio.file.Files


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UploadControllerTest {
    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate
    //
    // @Autowired
    // private lateinit var gson: Gson

    @Test
    fun testFileUploadFileSystemResource() {
        val filenamePrefix = "upload-file"
        val fileContent = "this is file content".toByteArray()

        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            contentType = MediaType.MULTIPART_FORM_DATA
        }

        val parts = HttpHeaders()
        parts.contentType = MediaType.TEXT_PLAIN

        val partsEntity = HttpEntity(createTempFileResource(filenamePrefix, fileContent), parts)
        
        val requestMap: MultiValueMap<String, Any> = LinkedMultiValueMap<String, Any>().apply {
            add("file", partsEntity )
        }

        val response = restTemplate.exchange(
                "/upload",
                HttpMethod.POST,
                HttpEntity(requestMap, headers),
                UploadFileResponse::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response?.body?.fileName).startsWith(filenamePrefix)
    }

    @Test
    fun testFileUploadFileByteArrayResource() {
        val filename = "upload-file_test.txt"
        val fileContent = "this is file content".toByteArray()

        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            contentType = MediaType.MULTIPART_FORM_DATA
        }

        val parts = HttpHeaders()
        parts.contentType = MediaType.TEXT_PLAIN

        val byteArrayResource: Resource = object : ByteArrayResource(fileContent) {
            override fun getFilename(): String {
                return filename
            }
        }
        val partsEntity = HttpEntity(byteArrayResource, parts)

        val requestMap: MultiValueMap<String, Any> = LinkedMultiValueMap<String, Any>().apply {
            add("file", partsEntity )
        }

        val response = restTemplate.exchange(
                "/upload",
                HttpMethod.POST,
                HttpEntity(requestMap, headers),
                UploadFileResponse::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response?.body?.fileName).startsWith("upload-file")
    }

    fun createTempFileResource(filenamePrefix: String, content: ByteArray): Resource {
        val tempFile = Files.createTempFile(filenamePrefix, ".txt")
        Files.write(tempFile, content)
        return FileSystemResource(tempFile.toFile())
    }

}