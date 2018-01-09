package net.extrategy.bernardo.interfaces

import org.json.JSONObject

/**
 * Created by saverio on 09/01/18.
 */

interface HTTPInterface {
    fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun get(path: String, completionHandler: (response: JSONObject?) -> Unit)
}