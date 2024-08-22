package sk.lg.scratchCard.api

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sk.lg.scratchCard.api.data.ValidationResult
import java.lang.reflect.Type

class Api {
    companion object {


        fun getValidationResult(context: Context, code: String, result: (Boolean) -> Unit){
            val url = "https://api.o2.sk/version?code=$code"

            val responseListener = Response.Listener<String> { response ->
                val data = getObjectFromJson<ValidationResult>(response, object : TypeToken<ValidationResult>() {}.type)
                result(data != null && data.android > 287028)
            }

            val errorListener = Response.ErrorListener {
                result(false)
            }

            try {
                val queue = Volley.newRequestQueue(context)
                val getRequest = StringRequest(url,responseListener, errorListener)
                queue.add(getRequest)
            }
            catch (ex: Exception){
                result(false)
            }
        }

        private fun <T> getObjectFromJson(response: String, type: Type): T?{
            return try {
                val encoded = String(response.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                Gson().fromJson(encoded, type) as T
            } catch(ex: Exception) {
                null
            }
        }
    }

}