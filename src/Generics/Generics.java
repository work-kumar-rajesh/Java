package Generics;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
========================================================================================
                                GENERICS IN JAVA
========================================================================================

WHAT ARE GENERICS?
Generics allow writing reusable and type-safe code by parameterizing types.

Before generics:
- Code used Object
- Explicit casting was required
- Errors occurred at runtime

With generics:
- Type checking happens at compile time
- No casting required
- Safer and cleaner code

GOAL:
Catch type errors as early as possible.
*/

/*
========================================================================================
                                PROBLEM WITHOUT GENERICS
========================================================================================
*/
class WithoutGenerics {
    public static void main(String[] args) {

        Object data = "Hello";

        // Integer num = (Integer) data; // Runtime ClassCastException
        // Uncommenting the above line will throw ClassCastException
        System.out.println("Data stored as Object: " + data);
    }
}

/*
========================================================================================
                                GENERIC CLASS
========================================================================================

<T> is a type parameter.
It represents one specific type chosen by the user.

Same T is used consistently inside the class.
This allows safe read and write operations.
*/
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class GenericClassDemo {
    public static void main(String[] args) {

        Box<String> sBox = new Box<>();
        sBox.set("Java");
        String s = sBox.get();
        System.out.println("String from Box: " + s);

        Box<Integer> iBox = new Box<>();
        iBox.set(10);
        int i = iBox.get();
        System.out.println("Integer from Box: " + i);
    }
}

/*
========================================================================================
                                GENERIC METHODS
========================================================================================

Generic methods define their own type parameters.
They are independent of class-level generics.
*/
class GenericMethods {

    static <T> void print(T value) {
        System.out.println(value);
    }

    static <T> T identity(T value) {
        return value;
    }

    public static void main(String[] args) {
        print("Hello");
        print(100);

        String s = identity("Java");
        Integer i = identity(20);

        System.out.println("Identity String: " + s);
        System.out.println("Identity Integer: " + i);
    }
}

/*
========================================================================================
                                BOUNDED TYPE PARAMETERS
========================================================================================

Bounds restrict which types are allowed.

<T extends Number> means T must be Number or a subclass of Number.
*/
class MathUtil {

    static <T extends Number> double sum(T a, T b) {
        return a.doubleValue() + b.doubleValue();
    }

    public static void main(String[] args) {
        System.out.println("Sum of Integers: " + sum(5, 10));
        System.out.println("Sum of Doubles: " + sum(5.5, 2.5));
    }
}

/*
========================================================================================
                                GENERIC INTERFACES
========================================================================================

Interfaces can be generic.
Implementing classes can either fix the type or remain generic.
*/
interface Repository<T> {
    void save(T t);
    T get();
}

class StringRepository implements Repository<String> {
    private String data;

    public void save(String t) {
        data = t;
    }

    public String get() {
        return data;
    }
}

class GenericRepository<T> implements Repository<T> {
    private T data;

    public void save(T t) {
        data = t;
    }

    public T get() {
        return data;
    }
}

class RepositoryDemo {
    public static void main(String[] args) {
        StringRepository repo = new StringRepository();
        repo.save("Hello");
        System.out.println("StringRepository: " + repo.get());

        GenericRepository<Integer> intRepo = new GenericRepository<>();
        intRepo.save(100);
        System.out.println("GenericRepository<Integer>: " + intRepo.get());
    }
}

/*
========================================================================================
                                MULTIPLE TYPE PARAMETERS
========================================================================================
*/
class Pair<K, V> {
    K key;
    V value;

    Pair(K k, V v) {
        key = k;
        value = v;
    }
}

class PairDemo {
    public static void main(String[] args) {
        Pair<String, Integer> p = new Pair<>("Age", 25);
        System.out.println("Pair: " + p.key + " = " + p.value);
    }
}

/*
========================================================================================
                                INVARIANCE IN GENERICS
========================================================================================

CORE PROBLEM:
In Java, generics are INVARIANT.

This means:
Even if Dog extends Animal,
Box<Dog> is NOT a Box<Animal>
*/

/*
--------------------------------
        SIMPLE INHERITANCE
--------------------------------
*/
class Animal {
    void sound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    void sound() {
        System.out.println("Dog barks");
    }
}

/*
--------------------------------
        WHAT YOU EXPECT (WRONG)
--------------------------------
*/
class InvarianceWrongExpectation {
    public static void main(String[] args) {

        Box<Dog> dogBox = new Box<>();
        dogBox.set(new Dog());

        // Box<Animal> animalBox = dogBox;
        // COMPILATION ERROR:
        // Incompatible types: Box<Dog> cannot be converted to Box<Animal>
    }
}

/*
--------------------------------
        WHY JAVA DISALLOWS THIS
--------------------------------
*/
class WhyInvarianceExists {
    static void breakTypeSafety(Box<Animal> animalBox) {
        animalBox.set(new Animal()); // perfectly valid for Box<Animal>
    }

    public static void main(String[] args) {

        Box<Dog> dogBox = new Box<>();
        dogBox.set(new Dog());

        // breakTypeSafety(dogBox);
        // If this were allowed, we could insert Animal into Box<Dog>
        // This breaks type safety
    }
}

/*
========================================================================================
                        <T> vs ? (WILDCARDS)
========================================================================================

CORE IDEA:
<T> means the code defines and owns the type.
? means the type is unknown and chosen by the caller.

<T>
- Used in class and method definitions
- Same type everywhere
- Supports read and write

?
- Used in method parameters
- Type is unknown
- Cannot safely read and write
*/

/*
========================================================================================
                        USING ? WITHOUT extends OR super
========================================================================================
*/
class PlainWildcardDemo {

    static void inspect(Box<?> box) {

        Object o = box.get();
        System.out.println(o);

        box.set(null); // only null is allowed
    }

    public static void main(String[] args) {
        Box<String> sBox = new Box<>();
        sBox.set("Hello");
        inspect(sBox);
    }
}

/*
========================================================================================
                        ? extends T (READING)
========================================================================================
*/
class ExtendsWildcardDemo {

    static void readAnimal(Box<? extends Animal> box) {
        Animal a = box.get(); // Safe to read as Animal
        a.sound();

        // box.set(new Dog()); // COMPILATION ERROR: Cannot write
    }

    public static void main(String[] args) {
        Box<Dog> dogBox = new Box<>();
        dogBox.set(new Dog());
        readAnimal(dogBox);
    }
}

/*
========================================================================================
                        ? super T (WRITING)
========================================================================================
*/
class SuperWildcardDemo {

    static void addDog(Box<? super Dog> box) {
        box.set(new Dog()); // Safe to write

        Object o = box.get(); // Only Object is safe to read
        System.out.println(o);
    }

    public static void main(String[] args) {
        Box<Animal> animalBox = new Box<>();
        addDog(animalBox);
    }
}

/*
========================================================================================
                        COMPARISON: ?, ? extends, ? super
========================================================================================

Box<?>:
- Type completely unknown
- Read as Object
- No writing except null

Box<? extends T>:
- Known minimum type T
- Safe reading as T
- No writing

Box<? super T>:
- Accepts T
- Writing allowed
- Reading only as Object
*/

/*
========================================================================================
                        PECS RULE
========================================================================================

Producer Extends:
Use ? extends when reading data.

Consumer Super:
Use ? super when writing data.
*/

/*
========================================================================================
                                RAW TYPES
========================================================================================
*/
class RawTypeDemo {
    public static void main(String[] args) {

        Box box = new Box();
        box.set("Hello");
        box.set(10);

        String s = (String) box.get(); // Needs casting
        System.out.println(s);
    }
}

/*
========================================================================================
                                TYPE ERASURE
========================================================================================
*/
class TypeErasureDemo<T> {
    T value;

    TypeErasureDemo(T v) {
        value = v;
    }

    public static void main(String[] args) {
        TypeErasureDemo<String> demo = new TypeErasureDemo<>("Hello");
        System.out.println(demo.value);
    }
}

/*
========================================================================================
                                GENERICS AND ARRAYS
========================================================================================
*/
class GenericArrayDemo {
    public static void main(String[] args) {

        Box<?>[] arr = new Box<?>[10];
        arr[0] = new Box<Integer>();
        arr[1] = new Box<String>();

        arr[0].set(null);
        arr[1].set(null);

        Object o = arr[0].get();
        System.out.println(o);
    }
}

/*
========================================================================================
                                GENERICS AND PRIMITIVES
========================================================================================

Generics work only with reference types.

Use wrapper classes instead of primitives.
*/

/*
========================================================================================
                                FINAL SUMMARY
========================================================================================

1. Generics provide compile-time type safety
2. <T> defines and owns a type
3. ? represents an unknown type
4. Generics are invariant
5. ? extends is for reading
6. ? super is for writing
7. Plain ? is for inspection only
8. Avoid raw types
9. Generics use type erasure
10. Avoid arrays with generics
*/
