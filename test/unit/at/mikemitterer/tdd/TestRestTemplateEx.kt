package at.mikemitterer.tdd

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClientException
import java.util.*

/**
 * Simplifies posting Form
 *
 * @since   07.05.20, 16:15
 */
@Throws(RestClientException::class)
fun <T> TestRestTemplate.postFormEntity(url: String, params: MultiValueMap<String, String>, responseType: Class<T>): ResponseEntity<T> {
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
    headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

    val formEntity = HttpEntity(params, headers)

    return this.postForEntity(url, formEntity, responseType)
}

fun <T> TestRestTemplate.getForAuthorizedEntity(url: String, jwt: String, responseType: Class<T>): ResponseEntity<T> {
    return this.exchange(url,
        HttpMethod.GET,
        HttpEntity<Any>(LinkedMultiValueMap<String, String>().apply {
            add("Authorization", "Bearer $jwt")
                }),
        responseType
    )

    // return this.postForEntity(url, formEntity, responseType)
}


