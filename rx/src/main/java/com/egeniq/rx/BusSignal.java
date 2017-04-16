package com.egeniq.rx;

/**
 * Created by andrei on 15/04/2017.
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
