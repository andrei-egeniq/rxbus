# RxBus
An RxJava (2) Android specific Event Bus

Based on https://github.com/JakeWharton/RxRelay 

What does BusRelay do that is special? 

BusRelay keeps the event until the Android framework can consume it. If an API call returns data right after the user rotates the device, the object is kept and delived to the recreated Activity.

If the event is kept, as BehaviorRelay does, on rotation the event is handled again, with posible undesired side effects.

If the event is kept, as BehaviorRelay does, there is a temptation to store the data in memory or use BehaviorRelay as a way to pass data between Activities. 

**`BusRelay`**

```java
// observer will receive all events.
BusRelay<Object> relay = BusRelay.create();
relay.subscribe(observer);
relay.accept("one");
relay.accept("two");
relay.accept("three");
```
```java
// observer1 will receive the "one", "two" and "three" events, but not "zero"
// observer2 will receive the "two" and "three" events, as "one" was emited to observer1
BusRelay<Object> relay = new BusRelay.create();
relay.accept("zero");
relay.accept("one");
relay.subscribe(observer1);
relay.subscribe(observer2);
relay.accept("two");
relay.accept("three");
```
