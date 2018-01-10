package net.extrategy.bernardo.services

import net.extrategy.bernardo.interfaces.HTTPInterface
import org.json.JSONObject

/**
 * Created by saverio on 09/01/18.
 */

class HTTPService constructor(serviceInjection: HTTPInterface): HTTPInterface {
    private val service: HTTPInterface = serviceInjection

    override fun post(path: String, params: HashMap<String, String>, completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path, params, completionHandler)
    }

    override fun get(path: String, completionHandler: (response: JSONObject?) -> Unit) {
        service.get(path, completionHandler)
    }
}