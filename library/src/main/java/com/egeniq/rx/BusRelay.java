package com.egeniq.rx;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.reactivex.Observer;

/**
 * Created by andrei on 15/04/2017.
 */

/**
 * Relay that stores the most recent item until it can emit it.
 * @param <T>
 */
public class BusRelay<T> extends Relay<T> {

    private AtomicReference<T> mValue;
    private PublishRelay<T> mRelay;

    private Lock mReadLock;
    private Lock mWriteLock;

    public static <T> BusRelay<T> create() {
        return new BusRelay<T>();
    }

    public static <T> BusRelay<T> createDefault(T defaultValue) {
        return new BusRelay<T>(defaultValue);
    }

    private BusRelay() {
        mRelay = PublishRelay.create();
        mValue = new AtomicReference<T>();

        ReadWriteLock lock = new ReentrantReadWriteLock();

        mReadLock = lock.readLock();
        mWriteLock = lock.writeLock();
    }

    private BusRelay(T t) {
        super();
        setValue(t);
    }

    private T getValue() {
        mReadLock.lock();
        T t = mValue.get();
        mReadLock.unlock();

        return t;
    }

    private void setValue(T t) {
        mWriteLock.lock();
        try {
            mValue.lazySet(t);
        } finally {
            mWriteLock.unlock();
        }
    }

    @Override
    public void accept(T t) {
        mRelay.accept(t);

        if(hasObservers()) {
            setValue(null);
        } else {
            setValue(t);
        }
    }

    @Override
    public boolean hasObservers() {
        return mRelay.hasObservers();
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        T t = getValue();

        if(t != null) {
            observer.onNext(t);
            setValue(null);
        }

        mRelay.subscribe(observer);
    }
}
