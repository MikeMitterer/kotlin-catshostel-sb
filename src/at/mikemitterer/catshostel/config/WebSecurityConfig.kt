package at.mikemitterer.catshostel.config

import at.mikemitterer.catshostel.filter.JWTAuthFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


/**
 *
 *
 * @since   08.05.20, 10:30
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(value = ["use.keycloak"], havingValue = "false")
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    // @Autowired
    // private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint? = null

    companion object {
        private val WHITE_LIST = arrayListOf<String>(
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/v2/api-docs",
                "/webjars/**"
        )
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        // We don't need CSRF for this example
        // .csrf().disable()
        httpSecurity
                // PATCH, POST, PUT, and DELETE are csrf protected!
                .csrf().disable()
                
                // dont authenticate this particular request
                .authorizeRequests()

                // deny protected
                .antMatchers("/protected/**").authenticated()

                // allow all others
                .antMatchers("/**").permitAll().anyRequest()

                // .antMatchers(AUTH_WHITELIST).permitAll().anyRequest()

                .authenticated()

                .and().cors()

                // make sure we use stateless session; session won't be used to
                // store user's state.
                .and().exceptionHandling()

                // .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // httpSecurity
        //         // dont authenticate this particular request
        //         .authorizeRequests().anyRequest().permitAll()

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterAfter(JWTAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

}