package at.mikemitterer.tdd

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriBuilder

/**
 * Aufgrund der Serverdaten wird eine URI erstellt.
 * Is insofern praktisch da der Server eine dynamische Portnummer hat.
 */
object MakeURI {
    fun forServer(template: TestRestTemplate): UriBuilder {
        return  DefaultUriBuilderFactory().uriString(template.rootUri)
    }
}
