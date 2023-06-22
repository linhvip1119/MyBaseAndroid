package com.example.mybaseandroid.extension

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T> : MutableLiveData<T> {
    constructor(value: T) : super(value)
    constructor() : super()

    private val mPending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.d(TAG, " Multiple observers registered but only one will be notified of changes .")
        }
        // Observe the internal MutableLiveData
        super.observe(owner) {
            if (mPending.compareAndSet(true, false)) {
                if (it != null) {
                    observer.onChanged(it)
                }
            }
        }
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {
        private const val TAG = " SingleLiveEvent "
    }
}