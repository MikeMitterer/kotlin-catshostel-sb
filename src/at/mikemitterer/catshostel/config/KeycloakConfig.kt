package at.mikemitterer.catshostel.config

import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

/**
 * Configure KeyCloak for Spring
 *
 * More:
 *      https://www.keycloak.org/docs/latest/securing_apps/#spring-security-configuration
 *
 * Könnte auch noch mit
 *
 *      @EnableGlobalMethodSecurity(jsr250Enabled = true)
 *
 * annotiert werden
 *
 * @since   26.05.20, 17:22
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(value = ["use.keycloak"], havingValue = "true")
class KeycloakConfig : KeycloakWebSecurityConfigurerAdapter() {

    /**
     * Make sure roles are not prefixed with ROLE_
     */
    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val keycloakAuthenticationProvider: KeycloakAuthenticationProvider = keycloakAuthenticationProvider()

        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        super.configure(httpSecurity)
        
        httpSecurity
                // PATCH, POST, PUT, and DELETE are csrf protected!
                .csrf().disable()

                // dont authenticate this particular request
                .authorizeRequests()

                .antMatchers("/protected*").hasRole("user")
                .antMatchers("/protected/message4admin*").hasRole("admin")

                .anyRequest().permitAll()
    }

    /**
     * Provide a session authentication strategy bean which should be of type
     * RegisterSessionAuthenticationStrategy for public or confidential applications
     * and NullAuthenticatedSessionStrategy for bearer-only applications.
     */
    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        // bearer-only!
        return NullAuthenticatedSessionStrategy()

        // Session-based strategy
        // return RegisterSessionAuthenticationStrategy(
        //         SessionRegistryImpl()
        // )
    }

    /**
     * keycloakConfigResolver: this defines that we want to use the Spring Boot
     * properties file support instead of the default keycloak.json
     */
    @Bean
    fun keycloakConfigResolver(): KeycloakConfigResolver {
        return KeycloakSpringBootConfigResolver()
    }
}