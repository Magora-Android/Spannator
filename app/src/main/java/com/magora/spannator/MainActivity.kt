package com.magora.spannator

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spannator = Spannee(resources)
        spannee_1.text = spannator(R.string.text_spannee_bg_and_color)
        spannee_2.text = spannator(R.string.text_spannee_size_and_tf)

        spannee_3.movementMethod = LinkMovementMethod.getInstance()
        spannee_3.highlightColor = Color.TRANSPARENT
        spannee_3.text = spannator(R.string.text_spannee_url)

        spannee_4.movementMethod = LinkMovementMethod.getInstance()
        spannee_4.highlightColor = Color.TRANSPARENT
        spannee_4.text = spannator(R.string.text_spannee_clickable) { url ->
            Toast.makeText(this, "Clicked at $url", Toast.LENGTH_SHORT).show()
        }
    }
}
