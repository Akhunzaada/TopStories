package net.cocooncreations.topstories

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [net.cocooncreations.topstories.util.TopStoriesTestRunner].
 */
class TestApp : Application()
