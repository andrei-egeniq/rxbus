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

Download
--------

Add jitpack repository in your root `build.gradle`:
```groovy
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
```

Add the `rxbus` dependency:

```groovy
compile 'com.github.andrei-egeniq:rxbus:v1.2'
```

No proguard rules are needed for the libray.

License
-------

    Copyright 2017 Egeniq BV

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
