package at.mikemitterer.catshostel.model

import java.security.Principal

/**
 *
 *
 * @since   08.05.20, 16:22
 */
data class User(
    val firstName: String,
    val familyName: String,
    val email: String
    ) : Principal {

    override fun getName(): String = "$firstName $familyName"
}


