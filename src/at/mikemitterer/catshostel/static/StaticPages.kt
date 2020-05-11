package at.mikemitterer.catshostel.static

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

/**
 * Aufruf eines statischen Templates
 *
 * Weitere Infos:
 *      http://zetcode.com/springboot/freemarker/
 *      
 * @since   06.05.20, 15:10
 */
@Controller
class StaticPages {
    @GetMapping("/")
    fun getIndexPage(): ModelAndView {
        return ModelAndView("index", "data", IndexData(listOf(1,2,3,4,5)))
    }
}

data class IndexData(val items: List<Int>)