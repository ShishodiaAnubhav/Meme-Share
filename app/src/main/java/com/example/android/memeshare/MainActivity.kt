package com.example.android.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currentMemeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()

        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "type/plain"
            intent.putExtra(Intent.EXTRA_TEXT, currentMemeUrl)
            val chooser = Intent.createChooser(intent, "Share this Meme Using... ")
            startActivity(chooser)
        }

        nextButton.setOnClickListener {
            loadMeme()
        }


    }

    private fun loadMeme() {
        nextButton.isEnabled = false
        shareButton.isEnabled = false
        progress_circular.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                currentMemeUrl = response.getString("url")

                Glide.with(this).load(currentMemeUrl).listener(object : RequestListener<Drawable>{
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_circular.visibility = View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled = true
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_circular.visibility = View.GONE
                        return false
                    }
                }).into(memeImageView)
            }, {
                progress_circular.visibility = View.GONE
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}