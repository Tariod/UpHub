package com.tariod.uphub.utilities.coroutine

import com.tariod.uphub.utilities.guard
import kotlinx.coroutines.Job
import java.util.concurrent.atomic.AtomicBoolean

class JobComposite {

    private val jobs: MutableList<Job> = mutableListOf()
    private val isCanceled: AtomicBoolean = AtomicBoolean(false)

    fun cancel() {
        if (isCanceled.getAndSet(true)) return
        synchronized(this) {
            cancelAll()
            trim()
        }
    }

    @Synchronized
    fun add(job: Job): Boolean {
        trim()
        return if (!isCanceled()) {
            jobs.add(job)
            true
        } else {
            job.cancel()
            false
        }
    }

    private fun cancelAll() {
        for (job in jobs)
            if (job.isActive) guard { job.cancel() }
    }

    private fun isCanceled(): Boolean = isCanceled.get()

    @Synchronized
    private fun trim() {
        jobs.removeAll {
            it.isCancelled || it.isCompleted
        }
    }
}