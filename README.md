# RxBus
An RxJava (2) Android specific Event Bus

Based on https://github.com/JakeWharton/RxRelay 

What does BusRelay do that is special? 

It keeps the previous event until it can emit it. Once it has emitted the event, it resets state.

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
