package Generics;

import java.util.*;

/*
========================================================================================
                                GENERICS IN JAVA
========================================================================================

WHAT ARE GENERICS?

- Generics allow you to write code that works with **different types** without losing **type safety**.
- Before generics:
    - We used Object for everything.
    - Needed explicit casting.
    - Runtime errors occurred if casting failed.
- With generics:
    - Type checking happens at compile time.
    - No casting needed.
    - Safer and cleaner code.

GOAL:
Catch type errors **as early as possible** (compile time) rather than runtime.
*/

/*
========================================================================================
                                PROBLEM WITHOUT GENERICS
========================================================================================
*/
class WithoutGenerics {
    public static void main(String[] args) {
        Object data = "Hello";

        // Runtime error because we assumed Object is Integer type
        // Integer num = (Integer) data; // Uncommenting this will throw ClassCastException
        System.out.println("Without generics, type errors appear at runtime.");
    }
}

/*
========================================================================================
                                GENERIC CLASS
========================================================================================

<T> is a **type parameter**. It represents one specific type chosen by the user.

- Using <T> ensures **type safety** inside the class.
- You can read and write values safely of type T.

Example:
*/
class Box<T> {
    private T value;

    public void set(T value) { this.value = value; }
    public T get() { return value; }
}

class GenericClassDemo {
    public static void main(String[] args) {
        Box<String> sBox = new Box<>();
        sBox.set("Java"); // Only String allowed
        String s = sBox.get();
        System.out.println("String Box contains: " + s);

        Box<Integer> iBox = new Box<>();
        iBox.set(10); // Only Integer allowed
        int i = iBox.get();
        System.out.println("Integer Box contains: " + i);
    }
}

/*
========================================================================================
                                GENERIC METHODS
========================================================================================

- Generic methods define their **own type parameter**, independent of class-level generics.
- Useful for methods that work with multiple types without duplicating code.

Example:
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
        print(123);

        String s = identity("Java");
        Integer i = identity(10);
        System.out.println("Identity returns: " + s + ", " + i);
    }
}

/*
========================================================================================
                                BOUNDED TYPE PARAMETERS
========================================================================================

- Sometimes we want to **restrict types** to a subset.
- Example: numbers only.
- Syntax: <T extends Number>

Example:
*/
class MathUtil {
    static <T extends Number> double sum(T a, T b) {
        return a.doubleValue() + b.doubleValue();
    }

    public static void main(String[] args) {
        System.out.println("Sum of Integers: " + sum(10, 20));
        System.out.println("Sum of Doubles: " + sum(5.5, 2.2));
    }
}

/*
========================================================================================
                                GENERIC INTERFACES
========================================================================================

- Interfaces can be generic.
- Implementing class can:
    1. Fix the type (StringRepository)
    2. Remain generic (GenericRepository)

Example:
*/
interface Repository<T> {
    void save(T t);
    T get();
}

class StringRepository implements Repository<String> {
    private String data;
    public void save(String t) { data = t; }
    public String get() { return data; }
}

class GenericRepository<T> implements Repository<T> {
    private T data;
    public void save(T t) { data = t; }
    public T get() { return data; }
}

class GenericInterfaceDemo {
    public static void main(String[] args) {
        StringRepository sr = new StringRepository();
        sr.save("Hello Repo");
        System.out.println("StringRepository: " + sr.get());

        GenericRepository<Integer> gr = new GenericRepository<>();
        gr.save(100);
        System.out.println("GenericRepository: " + gr.get());
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

    Pair(K k, V v) { key = k; value = v; }

    void display() { System.out.println("Key: " + key + ", Value: " + value); }
}

class MultipleTypeDemo {
    public static void main(String[] args) {
        Pair<String, Integer> p = new Pair<>("Age", 25);
        p.display();
    }
}

/*
========================================================================================
                                INVARIANCE IN GENERICS
========================================================================================

Even if Dog extends Animal:
Box<Dog> is NOT a Box<Animal>

- Prevents adding wrong types into a Box<Animal>.

Example:
*/
class Animal { void sound() { System.out.println("Animal sound"); } }
class Dog extends Animal { void sound() { System.out.println("Dog barks"); } }

class InvarianceDemo {
    public static void main(String[] args) {
        Box<Dog> dogBox = new Box<>();
        dogBox.set(new Dog());

        Dog d = dogBox.get();
        d.sound();

        // Box<Animal> animalBox = dogBox; // Compile-time error due to invariance
    }
}

/*
========================================================================================
                        <T> vs ? (WILDCARDS)
========================================================================================

<T>:
- Defined by **you** in class or method.
- Type is known.
- Supports read & write safely.

? (wildcard):
- Type is **unknown**.
- Used in method parameters.
- Can read only as Object.
- Writing is restricted.

Comparison Example:
*/
class TvsWildcardDemo {
    static <T> void genericMethod(Box<T> box, T value) { box.set(value); }
    static void wildcardMethod(Box<?> box) {
        Object o = box.get(); // Safe read as Object
        System.out.println("Wildcard box contains: " + o);
        // box.set(new Object()); // Cannot write
    }

    public static void main(String[] args) {
        Box<String> b = new Box<>();
        genericMethod(b, "Hello");
        wildcardMethod(b);
    }
}

/*
========================================================================================
                        ? extends T (READING)
========================================================================================

- Means the box contains **T or its subclasses**.
- Safe to **read** as T.
- Cannot write (exact type unknown).

Example:
*/
class ExtendsWildcardDemo {
    static void readAnimal(Box<? extends Animal> box) {
        Animal a = box.get(); // Safe
        a.sound();
        // box.set(new Dog()); // Cannot write
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

- Means the box accepts **T or superclasses**.
- Safe to **write**.
- Reading only as Object.

Example:
*/
class SuperWildcardDemo {
    static void addDog(Box<? super Dog> box) {
        box.set(new Dog()); // Safe
        Object o = box.get(); // Read only as Object
        System.out.println("Added Dog: " + o);
    }

    public static void main(String[] args) {
        Box<Animal> animalBox = new Box<>();
        addDog(animalBox);
    }
}

/*
========================================================================================
                                PECS RULE
========================================================================================

- **Producer Extends**: Use `? extends T` when reading from a box.
- **Consumer Super**: Use `? super T` when writing to a box.
*/

/*
========================================================================================
                                RAW TYPES
========================================================================================

- Raw types disable generics.
- Exist for backward compatibility.
- Unsafe: requires casting.

Example:
*/
class RawTypeDemo {
    public static void main(String[] args) {
        Box rawBox = new Box();
        rawBox.set("Hello");
        rawBox.set(10); // Allowed, but unsafe

        String s = (String) rawBox.get(); // Requires cast
        System.out.println("Raw Box contains: " + s);
    }
}

/*
========================================================================================
                                TYPE ERASURE
========================================================================================

- Generics exist only at compile time.
- At runtime: Box<String> and Box<Integer> both become Box.
- Cannot use `instanceof T` or `new T()`.

Example:
*/
class TypeErasureDemo<T> {
    T value;

    TypeErasureDemo(T v) { value = v; }

    public static void main(String[] args) {
        TypeErasureDemo<String> s = new TypeErasureDemo<>("Hello");
        TypeErasureDemo<Integer> i = new TypeErasureDemo<>(100);
        System.out.println("String: " + s.value + ", Integer: " + i.value);
    }
}

/*
========================================================================================
                                GENERICS AND ARRAYS
========================================================================================

- Arrays know their element type at runtime.
- Generics lose type information at runtime.
- Creating generic arrays is **not allowed**.
- Safe workaround: use Object[] or wildcard arrays.

Example:
*/
class GenericArrayDemo {
    public static void main(String[] args) {
        Box<?>[] arr = new Box<?>[2];
        arr[0] = new Box<Integer>();
        arr[1] = new Box<String>();

        arr[0].set(null);
        arr[1].set(null);

        Object o1 = arr[0].get();
        Object o2 = arr[1].get();
        System.out.println("Generic array elements: " + o1 + ", " + o2);
    }
}

/*
========================================================================================
                                GENERICS AND PRIMITIVES
========================================================================================

- Generics work only with reference types.
- Use wrapper classes instead of primitives.

Example:
*/
class GenericsPrimitivesDemo {
    public static void main(String[] args) {
        Box<Integer> intBox = new Box<>();
        intBox.set(10);
        System.out.println("Generic Integer Box: " + intBox.get());
    }
}

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
11. PECS: Producer Extends, Consumer Super
12. Always prefer wrappers for primitives
*/
