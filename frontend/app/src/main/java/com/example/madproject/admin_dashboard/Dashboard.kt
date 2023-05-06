package com.example.test.admin_dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.test.R

class Dashboard : AppCompatActivity() {

    private lateinit var addNotice: Button
    private lateinit var viewAllNotices: Button
    private lateinit var viewAllFeeds: Button
    private lateinit var feeds:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        addNotice = findViewById(R.id.add_notice)
        viewAllNotices = findViewById(R.id.view_notice_btn)
        viewAllFeeds = findViewById(R.id.view_feed_btn)
        feeds = findViewById(R.id.add_feed)

        feeds.setOnClickListener {
            var intent = Intent(this,AddFeedback::class.java)
            startActivity(intent)
        }

        addNotice.setOnClickListener {
            var intent = Intent(this,AddNotice::class.java)
            startActivity(intent)
        }
        viewAllNotices.setOnClickListener {
            var intent = Intent(this,AllNotices::class.java)
            startActivity(intent)
        }
        viewAllFeeds.setOnClickListener {
            var intent = Intent(this,AllFeedbacks::class.java)
            startActivity(intent)
        }

    }
}