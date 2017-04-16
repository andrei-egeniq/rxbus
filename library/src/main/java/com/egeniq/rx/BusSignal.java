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

/**
 * Helper class to emit success or error.
 */

public class BusSignal<T> {

    private T mData;
    private Throwable mThrowable;

    public static <T> BusSignal<T> create(T t) {
        return new BusSignal<>(t);
    }

    public static <T> BusSignal<T> error(Throwable throwable) {
        return new BusSignal<>(throwable);
    }

    private BusSignal(T t) {
        mData = t;
    }

    private BusSignal(Throwable throwable) {
        mThrowable = throwable;
    }

    public T getData() {
        return mData;
    }

    public Throwable getError() {
        return mThrowable;
    }

    public boolean isError() {
        return mData == null;
    }
}
