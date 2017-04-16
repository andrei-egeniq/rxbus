/**
 * Copyright 2017 Egeniq BV
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

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
        this();
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
