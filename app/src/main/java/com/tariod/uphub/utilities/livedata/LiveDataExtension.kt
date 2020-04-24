package com.tariod.uphub.utilities.livedata

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T> : MediatorLiveData<T>() {
    // Used when observeForever is called since ConcurrentHashMap doesn't support null as key
    private val fakeOwner = object : LifecycleOwner {
        override fun getLifecycle(): Lifecycle {
            return object : Lifecycle() {
                override fun addObserver(observer: LifecycleObserver) {}

                override fun removeObserver(observer: LifecycleObserver) {}

                override fun getCurrentState(): State {
                    return State.STARTED
                }
            }
        }

        override fun hashCode(): Int {
            return 0
        }
    }
    private val observers = ConcurrentHashMap<LifecycleOwner?, MutableSet<ObserverWrapper<in T>>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        observeInternal(owner, observer)
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        observe(fakeOwner, observer)
    }

    private fun observeInternal(owner: LifecycleOwner, observer: Observer<in T>) {
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            // ignore
            return
        }

        val wrapper = ObserverWrapper(observer)
        val set = observers[owner]
        set?.apply {
            this.find { it.observer == observer }?.let {
                doObserve(owner, it)
                return
            }
            add(wrapper)
        } ?: run {
            val newSet =
                Collections.newSetFromMap(ConcurrentHashMap<ObserverWrapper<in T>, Boolean>())
            newSet.add(wrapper)
            observers[owner] = newSet
        }
        doObserve(owner, wrapper)
    }

    private fun doObserve(owner: LifecycleOwner, observer: Observer<in T>) {
        when (owner) {
            fakeOwner -> super.observeForever(observer)
            else -> super.observe(owner, observer)
        }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        observers.remove(owner)
        super.removeObservers(owner)
    }

    override fun removeObserver(observer: Observer<in T>) {
        observers.forEach { entry ->
            entry.value.find { it == observer || it.observer == observer }?.let {
                entry.value.remove(it)
                super.removeObserver(it)
                if (entry.value.isEmpty()) {
                    observers.remove(entry.key)
                }
                return@forEach
            }
        }
    }

    override fun postValue(value: T) {
        observers.forEach { it.value.forEach { wrapper -> wrapper.newValue() } }
        super.postValue(value)
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.value.forEach { wrapper -> wrapper.newValue() } }
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    private class ObserverWrapper<T>(
        val observer: Observer<T>
    ) : Observer<T> {

        private val pending = AtomicBoolean(false)

        override fun onChanged(t: T?) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending.set(true)
        }

    }
}

fun <T1, T2> LiveData<T1>.zip(source2: LiveData<T2>): LiveData<Pair<T1, T2>> =
    MediatorLiveData<Pair<T1, T2>>().apply {
        addSource(this@zip) { data -> value = data to (source2.value ?: return@addSource) }
        addSource(source2) { data -> value = (this@zip.value ?: return@addSource) to data }
    }

fun <T1 : Any, T2 : Any, T3 : Any> LiveData<T1>.zip(
    source2: LiveData<T2>,
    source3: LiveData<T3>
): LiveData<Triple<T1, T2, T3>> =
    MediatorLiveData<Triple<T1, T2, T3>>().apply {
        addSource(this@zip.zip(source2)) { (data1, data2) ->
            value = Triple(
                data1,
                data2,
                (source3.value ?: return@addSource)
            )
        }
        addSource(source3) { data ->
            value = Triple(
                (this@zip.value ?: return@addSource),
                (source2.value ?: return@addSource),
                data
            )
        }
    }

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any> LiveData<T1>.zip(
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>
): LiveData<Quad<T1, T2, T3, T4>> =
    MediatorLiveData<Quad<T1, T2, T3, T4>>().apply {
        addSource(this@zip.zip(source2)) { (data1, data2) ->
            value = Quad(
                data1,
                data2,
                (source3.value ?: return@addSource),
                (source4.value ?: return@addSource)
            )
        }
        addSource(source3.zip(source4)) { data ->
            val (data3, data4) = data
            value = Quad(
                (this@zip.value ?: return@addSource),
                (source2.value ?: return@addSource),
                data3,
                data4
            )
        }
    }

fun <T1, T2> LiveData<T1>.switchMap(function: (T1) -> LiveData<T2>): LiveData<T2> =
    Transformations.switchMap(this, function)

fun <T1, T2> LiveData<T1>.map(function: (T1) -> T2): LiveData<T2> =
    Transformations.map(this, function)

fun <T> LiveData<T>.asMutable(): MutableLiveData<T> = MediatorLiveData<T>().also { mediator ->
    mediator.addSource(this) {
        mediator.set(it)
    }
}

fun <T> LiveData<T>.startWith(first: T): LiveData<T> = MediatorLiveData<T>().apply {
    set(first)
    addSource(this@startWith) { set(it) }
}

fun <T1 : Any> LiveData<T1?>.notNull(): LiveData<T1> = MediatorLiveData<T1>().apply {
    addSource(this@notNull) { data ->
        value = data ?: return@addSource
    }
}

fun <T> MutableLiveData<T>.set(value: T?) {
    if (Looper.myLooper() == Looper.getMainLooper()) setValue(value)
    else postValue(value)
}

fun <T> MutableLiveData<T>.default(value: T): MutableLiveData<T> = this.also { set(value) }

fun MutableLiveData<Unit>.click() = set(Unit)

fun MutableLiveData<String>.click(message: String) = set(message)

fun <T> AppCompatActivity.observe(liveData: LiveData<T>, onChange: (T) -> Unit) {
    liveData.observe(this, Observer {
        onChange(it)
    })
}

fun <T> Fragment.observe(liveData: LiveData<T>, onChange: (T) -> Unit) {
    liveData.observe(this, Observer {
        onChange(it ?: return@Observer)
    })
}

fun <T> T.wrap() = MutableLiveData<T>(this)