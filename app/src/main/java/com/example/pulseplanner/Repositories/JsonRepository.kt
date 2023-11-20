package com.example.pulseplanner.Repositories

import android.content.Context
import org.json.JSONObject


class JsonRepository private constructor() {
    private var context: Context? = null

    companion object {
        private var instance: JsonRepository? = null

        fun getInstance(): JsonRepository {
            if (instance == null) {
                instance = JsonRepository()
            }
            return instance!!
        }

        fun setContext(context: Context) {
            this.getInstance().context = context
        }
    }
}