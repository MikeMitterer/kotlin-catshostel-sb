package at.mikemitterer.catshostel.routes._root

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Antwortet auf den "Connection-Check"
 *
 * +-------------+-----------------------------------------+-------------------------+
 * | HTTP Method |                  URI                    |         Action          |
 * +-------------+-----------------------------------------+-------------------------+
 * | GET         | http://[hostname]/api/v1/jobs           | Retrieve list of jobs   |
 * | GET         | http://[hostname]/api/v1/jobs/[task_id] | Retrieve a job          |
 * | POST        | http://[hostname]/api/v1/jobs           | Create a new job        |
 * | PUT         | http://[hostname]/api/v1/jobs/[task_id] | Update an existing job  |
 * | DELETE      | http://[hostname]/api/v1/jobs/[task_id] | Delete a job            |
 * +-------------+-----------------------------------------+-------------------------+
 * http://www.sensefulsolutions.com/2010/10/format-text-as-table.html
 *
 * @since   28.04.20, 08:21
 */
@RestController
@RequestMapping("/api")
class HomeRouteController {
    val logger: Logger = LoggerFactory.getLogger(HomeRouteController::class.java)

    @RequestMapping(path = ["/v1"], method = [RequestMethod.HEAD, RequestMethod.GET], headers = ["Accept=application/json"])
    fun isApiAvailable(): Boolean {
        return true;
    }
}
