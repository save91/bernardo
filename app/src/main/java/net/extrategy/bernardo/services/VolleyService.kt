package net.extrategy.bernardo.services

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import net.extrategy.bernardo.BackendVolley
import net.extrategy.bernardo.interfaces.HTTPInterface
import org.json.JSONObject

/**
 * Created by saverio on 09/01/18.
 */
class VolleyService : HTTPInterface{
    val TAG = VolleyService::class.java.simpleName

    override fun post(path: String, params: HashMap<String, String>, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = object : StringRequest(Method.POST, path,
                Response.Listener<String> { data ->
                    Log.d(TAG, "/post request OK! Response: $data")
                    val response = JSONObject()
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/x-www-form-urlencoded")
                return headers
            }
        }

        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun get(path: String, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = object : JsonObjectRequest(Method.GET, path, null,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/get request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/get request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }.setShouldCache(false)

        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }
}