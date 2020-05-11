package at.mikemitterer.catshostel.config

import at.mikemitterer.catshostel.filter.JWTAuthFilter
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
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    // @Autowired
    // private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint? = null

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        // We don't need CSRF for this example
        // .csrf().disable()
        // httpSecurity
        //         // dont authenticate this particular request
        //         .authorizeRequests()
        //
        //         // deny protected
        //         .antMatchers("/protected/**").authenticated()
        //
        //         // allow all others
        //         .antMatchers("/**").permitAll().anyRequest()
        //
        //         .authenticated()
        //
        //         .and().cors()
        //
        //         // make sure we use stateless session; session won't be used to
        //         // store user's state.
        //         .and().exceptionHandling()

        httpSecurity
                // dont authenticate this particular request
                .authorizeRequests().anyRequest().permitAll()


                //
                // .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterAfter(JWTAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}