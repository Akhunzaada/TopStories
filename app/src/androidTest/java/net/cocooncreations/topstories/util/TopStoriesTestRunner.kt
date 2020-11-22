package net.cocooncreations.topstories.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

import net.cocooncreations.topstories.TestApp

/**
 * Custom runner to disable dependency injection.
 */
class TopStoriesTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}
