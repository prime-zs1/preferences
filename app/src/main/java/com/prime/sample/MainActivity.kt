package com.prime.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.prime.preferences.LongMapper
import com.prime.preferences.Preferences
import com.prime.preferences.longPreferenceKey

val key = longPreferenceKey("Zak", object : LongMapper<Test> {
    override fun from(value: Test): Long {
        return when (value) {
            Test.fghf -> 0
            Test.ghgh -> 1
            Test.ghh -> 2
        }
    }

    override fun to(value: Long): Test {
        return when (value) {
            0L -> Test.fghf
            1L -> Test.ghgh
            2L -> Test.ghh
            else -> {
                error("")
            }
        }
    }
})

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = Preferences.get(this)
        Log.i(TAG, "onCreate: ")
    }
}

enum class Test {

    fghf,

    ghgh,

    ghh
}
