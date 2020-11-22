package net.cocooncreations.topstories.util

import net.cocooncreations.topstories.AppExecutors

import java.util.concurrent.Executor

class InstantAppExecutors : AppExecutors(instant, instant, instant) {
    companion object {
        private val instant = Executor { it.run() }
    }
}
