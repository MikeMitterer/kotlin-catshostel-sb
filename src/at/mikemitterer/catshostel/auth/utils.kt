package at.mikemitterer.catshostel.auth

/**
 * Löscht den BEGIN/END-Teil eines PEM-Zertifikates
 */
fun stripBeginEnd(pem: String): String {
    var stripped = pem.replace("-----BEGIN (.*)-----".toRegex(), "");

    stripped = stripped.replace("-----END (.*)----".toRegex(), "");
    stripped = stripped.replace("\r\n", "");
    stripped = stripped.replace("\n", "");

    return stripped.trim();
}

/**
 * Extension-Function für String um den PEM-Header/Footer zu löschen
 * @since   04.05.20, 17:58
 */
fun String.stripPEMMarker(): String {
    return stripBeginEnd(this)
}

/**
 * Reads resource from Path
 *
 * How cool ist this!
 */
fun String.asResource(): String {
    return this.javaClass.getResource(this).readText()
}
