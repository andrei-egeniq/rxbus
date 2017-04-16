# RxBus
A RxJava (2) Android specific Event Bus

Based on https://github.com/JakeWharton/RxRelay 

What does `BusRelay` do that is special? 

`BusRelay` keeps the event until the Android framework can consume it. If an API call returns data right after the user rotates the device, the data is kept and delived next time `subscribe` is called on the same `BusRelay` object.

**`BusRelay`**

```java
// observer will receive all events.
BusRelay<String> relay = BusRelay.create();
relay.subscribe(observer);
relay.accept("one");
relay.accept("two");
relay.accept("three");
```
```java
// observer1 will receive the "one", "two" and "three" events, but not "zero"
// observer2 will receive the "two" and "three" events, as "one" was emited to observer1
BusRelay<String> relay = new BusRelay.create();
relay.accept("zero");
relay.accept("one");
relay.subscribe(observer1);
relay.subscribe(observer2);
relay.accept("two");
relay.accept("three");
```
Why not just use `BehaviorRelay`?

If the event is kept, as `BehaviorRelay` does, on rotation the event is handled again, with posible undesired side effects.

If the event is kept, as `BehaviorRelay` does, there is a temptation to store the data in memory or use `BehaviorRelay` as a way to pass data between Activities. 

`BusRelay` was written to replicate the https://github.com/greenrobot/EventBus sticky event functionality, in a more strict way to prevent bad patterns. 

**`Setup instructions`**

https://jitpack.io/#andrei-egeniq/rxbus/

No proguard rules are needed for the libray.
