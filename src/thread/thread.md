* `String`나 원시값 같은 불변 타입은 스레드 안전하다.
  

* `java.util.concurrent.atomic`을 사용하면 가변 클래스일 경우에도 스레드 안전을 보장할 수 있다.
  * `int` &rarr; `AtomicInteger`, `int[]` &rarr; `AtomicIntegerArray`
  * 참조 변수 &rarr; `AtomicRefence`
    

* `java.util.Collections`를 사용하여 컬렉션 동기화
  * `Set` &rarr; `Collections.synchronizedSet(something)`
  * `List` &rarr; `Collections.synchronizedList(something)`
  * `Map` &rarr; `Collections.synchronizedMap(something)`
    
    
* `java.util.concurrent.CopyOnWriteArrayList<E>` (스레드 안전한 `ArrayList`)
* `java.util.concurrent.ConcurrentHashMap<K,V>` (스레드 안전 && 병렬 처리)