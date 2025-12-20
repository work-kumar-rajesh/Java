package Multithreading;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;


/*
=======================================
BASICS
=======================================

CPU :
The CPU (Central Processing Unit) is the brain of the computer where computation
and instruction execution take place.


CORE :
A core is an individual processing unit inside a CPU that can execute instructions.
Modern CPUs have multiple cores, allowing them to perform multiple tasks in parallel.
For example, in a quad-core processor, one core may handle a browser, another music playback,
another file downloads, and another background system tasks.


PROGRAM :
A program is a set of instructions written in a programming language that tells the computer
how to perform a specific task. Microsoft Word is a program that allows users to create
and edit documents.


PROCESS :
A process is an instance of a program that is currently being executed.
When a program runs, the operating system creates a process to manage its execution.
For example, when Microsoft Word is opened, it becomes a process.


THREAD :
A thread is the smallest unit of execution within a process.
A process can have multiple threads that share the same resources (memory, files, etc.)
but execute independently.
Example: A web browser may use multiple threads for rendering, user input,
and background tasks. (Note: Chrome often uses multiple processes as well.)


MULTITASKING :
Multitasking allows an operating system to run multiple processes concurrently.
On single-core CPUs, this is achieved using time-sharing, where the CPU rapidly
switches between tasks.
On multicore CPUs, true parallel execution is possible, with tasks distributed
across different cores. The OS scheduler manages and balances these tasks.
Example: Browsing the web while listening to music and editing a document.


MULTITHREADING :
Multithreading.Multithreading refers to the execution of multiple threads within a single process
concurrently.
Example: A browser may use separate threads for rendering pages, executing JavaScript,
and handling user input, making it more responsive and efficient.
Multithreading.Multithreading improves efficiency by dividing a task into smaller sub-tasks
(threads) that can run concurrently, making better use of CPU resources.


SINGLE CORE VS MULTI CORE :
In a single-core system, threads and processes are managed by the OS scheduler
using time slicing and context switching to create the illusion of simultaneous execution.
In a multicore system, threads and processes can execute in true parallel on
different cores, with the OS scheduler distributing work efficiently.


MULTITASKING VS MULTITHREADING EXAMPLE :
Multitasking refers to running multiple applications, while multithreading refers
to multiple threads within the same application.
Example: A manager handling multiple teams (multitasking), and within each team,
members working on different parts of the same project (multithreading).


MULTITHREADING IN JAVA :
Multithreading.Multithreading in Java is the execution of two or more threads to maximize CPU utilization.
Java’s multithreading support is part of the java.lang package. [ JAVA.LANG.THREAD class
and JAVA.LANG.RUNNABLE Interface ] .
In a single-core environment, Java threads are managed by the JVM and OS using
time slicing to provide the illusion of concurrency.
In a multicore environment, the JVM can schedule threads across multiple cores,
allowing true parallel execution.


WORKING IN JAVA
When a java program starts one thread begins running immediately , which is called the main
thread.This thread is responsible for executing the main method of the program .
*/


/*
=======================================
SIMPLE PROGRAM
=======================================
*/
public class Multithreading {
    public static void main(String[] args) {
        System.out.println("Hello");
    }
}


/*
=======================================
USING THREAD CLASS
=======================================
*/
class PrintWorld extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            System.out.println(Thread.currentThread().getName());
            System.out.println("World");
        }
    }
}

class PrintWorldMainClass {
    public static void main(String[] args) {
        PrintWorld obj = new PrintWorld();
        obj.start();

        for (int i = 0; i < 100000; i++) {
            System.out.println(Thread.currentThread().getName());
            System.out.println("Hello");
        }
    }
}


/*
=======================================
USING RUNNABLE INTERFACE
=======================================
*/
class PrintHello implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            System.out.println(Thread.currentThread().getName());
            System.out.println("Hello");
        }
    }
}

class PrintHelloMainClass {
    public static void main(String[] args) {
        // Classic Runnable usage
        PrintHello obj = new PrintHello();
        Thread t1 = new Thread(obj);
        t1.start();

        for (int i = 0; i < 100000; i++) {
            System.out.println(Thread.currentThread().getName());
            System.out.println("Hello");
        }

        // ==================== MODERN JAVA EXAMPLES ====================
        // Example 1: Using Anonymous Class
        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println("Hello");
                }
            }
        };
        Thread t2 = new Thread(task);
        t2.start();

        // Example 2: Using Lambda Expression
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("Hello");
            }
        });
        t3.start();
    }
}


/*
=======================================
THREAD LIFECYCLE
=======================================

1. New - A thread is in this state when it is created but not yet started.
2. Runnable - After the start method is called , the thread becomes runnable,it is  ready to
              tun and is waiting for cpu time.
3. Running - The thread is in this state when executing .
4. Blocked/Waiting - A thread is in this state when it is waiting for a resource or for another
                     thread to perform an action .
5. Terminated - A thread is in this state when it has finished executing.

*/

class ThreadStates extends Thread {
    @Override
    public void run() {
        System.out.println("RUNNING STATE");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadStates t1 = new ThreadStates(); // NEW STATE
        System.out.println(t1.getState());
        t1.start();                           // RUNNABLE STATE
        System.out.println(t1.getState());
        Thread.sleep(100);
        System.out.println(t1.getState());    // TIMED_WAITING STATE
        t1.join(); // Blocking method -- main thread wait for t1 thread to complete.
        System.out.println(t1.getState());    // TERMINATED STATE
    }
}



/*
=======================================
THREAD METHODS
=======================================
*/

class ThreadMethods extends Thread {
    @Override
    public void run() {
        try {
            Thread.sleep(100);             // Sleep method to pause thread execution
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for( int i = 0 ; i < 100 ; i++) {
            System.out.println("Running");

            /*
                Yield
                This method is used to give a hint to jvm that you can run other threads in meantime
                just a hint final choice is with jvm
            */
            Thread.yield();
        }
        System.out.println("Thread is running.");
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadMethods t1 = new ThreadMethods() ;
        t1.start();                              // Start method start thread execution
        t1.join();                               //  blocking method to wait for t1 to finish
        t1.setPriority(Thread.MAX_PRIORITY);     // just an indication to jvm  does not guarantee first execution if multiple thread are there
        t1.interrupt();

         /*
         Daemon
         All threads are by default non daemon threads that means jvm waits for their execution even if
         main thread is completed .
         But for daemon threads the process is completed as soon as main and all other non daemon
         threads are finished execution , jvm does not wait for daemon threads even if their execution is not completed.
         */
        t1.setDaemon(true);
    }
}



/*
=======================================
SYNCHRONIZATION
=======================================


Synchronization is a mechanism in Java used in a multithreaded environment to control access to shared resources.
It ensures that only ONE thread can execute a critical section of code at a time for a given object or class.
Synchronization helps to:
- Prevent race conditions
- Maintain data consistency
- Avoid incorrect or unpredictable results

The 'synchronized' keyword:
- Acquires a lock (monitor) on an object or class
- Allows only one thread to hold that lock at a time
- Forces other threads to wait until the lock is released
- Guarantees visibility of changes to shared data



When 'synchronized' is applied directly to a method:
- The ENTIRE method becomes synchronized
- The lock is automatically taken on:
    * the current object (for instance methods)
    * the class object (for static methods)
- Only one thread can execute the method at a time
- Simple to use but may reduce performance


A synchronized block synchronizes only a SPECIFIC
portion of code instead of the whole method.

In a synchronized block:
- A specific object is used as the lock
- Only the code inside the block is locked
- Non-critical code can run concurrently
- Provides better performance and flexibility
*/

class UtilizeCounter extends Thread {
    private final Counter counter;

    public UtilizeCounter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run(){
        for( int i = 0 ; i < 1000 ; i++ ) {
            counter.increment();
        }
    }
}

class Counter {
    private int count = 0 ;

    public void increment() {

        /*
         ==================== NORMAL (NOT SYNCHRONIZED) ====================

        If we write:
            count++;

        → Both threads can execute this line at the same time
        → They may read the same value of count
        → Increment happens only once
        → Final output is often LESS than 2000
        */

        // count++;   // ❌ NOT thread-safe

        /*

        ===============================SYNCHRONIZED BLOCK===============================
        synchronized(this) means:
        → Lock the current Counter object
        → Only ONE thread can enter this block at a time
        → Other thread waits until lock is released
        → count++ becomes thread-safe
        → Final output will ALWAYS be 2000
        */
        synchronized (this) {   // ✅ thread-safe
            count++;
        }
    }

    /*
    ===============================ADD DIRECTLY TO METHOD SIGNATURE===============================
    public synchronized void increment(){
        count++
    }
    */

    public int getCount() {
        return count;
    }
}

class Synchronization {
    public static void main(String[] args) throws InterruptedException {
        Counter myCounter = new Counter() ;

        UtilizeCounter t1 = new UtilizeCounter(myCounter);
        UtilizeCounter t2 = new UtilizeCounter(myCounter);
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        /*
        Here the output in ideal scenario should be 2000 as two threads running .
        But it is not as the counter is shared resource and the code is not thread safe , many times
        as both threads running simultaneously  they read the same value and increment only once .
        */
        System.out.println(myCounter.getCount());


        /*
        But if we add "synchronized" keyword to increment method
        output will always be 2000 ,
        Run same code by adding or removing synchronized
         */
    }
}



/*
=======================================
LOCKS (java.util.concurrent.locks)
=======================================

Locks in Java provide a more flexible and powerful mechanism for thread synchronization
than the 'synchronized' keyword.

There are 2 types of locks:
1. Intrinsic lock (built-in, used by synchronized)
2. Explicit lock (programmer-controlled, advanced)

Locks allow:
- Explicit locking/unlocking
- Try-lock with timeout
- Interruptible waiting
- Fair/unfair lock acquisition
- Multiple condition variables
- Read-write separation
- Deadlock avoidance
*/

// ==================== EXPLICIT LOCKING ====================
// EXPLANATION:
// Unlike synchronized, where locking is automatic, ReentrantLock allows the programmer
// to acquire and release locks explicitly. This gives more control and flexibility.
// Use case: when you want finer control over locking scope or need advanced features.
class ExplicitLockDemo {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock(); // acquire lock manually
        try {
            count++;
        } finally {
            lock.unlock(); // always release lock in finally
        }
    }

    public int getCount() {
        return count;
    }
}

class ExplicitLockTest {
    public static void main(String[] args) throws InterruptedException {
        ExplicitLockDemo counter = new ExplicitLockDemo();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("ExplicitLockDemo count: " + counter.getCount()); // always 2000
    }
}

// ==================== TRY LOCK ====================
// EXPLANATION:
// tryLock() allows a thread to attempt acquiring a lock without blocking indefinitely.
// If the lock is unavailable, the thread can continue executing other tasks.
// Use case: avoid waiting forever and implement timeout logic.
// we can also pass timeout in try lock then it will wait for that time to acquire lock
class TryLockDemo {
    private final Lock lock = new ReentrantLock();

    public void doWork() {
        if (lock.tryLock()) { // try to acquire lock, returns false if unavailable
            try {
                System.out.println(Thread.currentThread().getName() + " acquired lock");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " could not acquire lock");
        }
    }
}

class TryLockTest {
    public static void main(String[] args) {
        TryLockDemo obj = new TryLockDemo();

        Runnable task = () -> {
            for (int i = 0; i < 3; i++) obj.doWork();
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");

        t1.start();
        t2.start();
    }
}

// ==================== INTERRUPTIBLE LOCK ====================
// EXPLANATION:
// lockInterruptibly() allows a thread to be interrupted while waiting for a lock.
// This is useful to handle deadlocks or stop long waiting threads gracefully.
/*
important note -- so the main purpose here is that lock should not wait infinitely if
the thread is in waiting state, and we call t2.interrupt() it will stop waiting ,
but if normal lock was used and this was called nothing will happen thread will
continue to wait until lock gets free.
One more notable thing interrupt() can be called normally anytime on thread and it
will interrupt thread if it's doing some waiting action like sleep, wait or join
otherwise thread will continue working normally .
*/


class InterruptibleLockDemo {
    // Declare as ReentrantLock to access isHeldByCurrentThread()
    private final ReentrantLock lock = new ReentrantLock();

    public void doWork() {
        try {
            lock.lockInterruptibly(); // can be interrupted while waiting for lock
            System.out.println(Thread.currentThread().getName() + " acquired lock interruptibly");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted");
        } finally {
            // Only unlock if current thread holds the lock
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }
}

class InterruptibleLockTest {
    public static void main(String[] args) throws InterruptedException {
        InterruptibleLockDemo obj = new InterruptibleLockDemo();

        Thread t1 = new Thread(() -> obj.doWork(), "T1");
        Thread t2 = new Thread(() -> obj.doWork(), "T2");

        t1.start();
        t2.start();

        Thread.sleep(500);
        t2.interrupt();
    }
}

// ==================== FAIRNESS ====================
// EXPLANATION:
// ReentrantLock can be fair or unfair.
// Fair lock grants access to threads in the order they requested it (FIFO).
// Unfair lock (default) may allow some threads to "jump the queue" for performance.
class FairLockDemo {
    private final Lock lock = new ReentrantLock(true); // fair lock

    public void access() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " acquired fair lock");
        } finally {
            lock.unlock();
        }
    }
}

class FairLockTest {
    public static void main(String[] args) {
        FairLockDemo obj = new FairLockDemo();

        Runnable task = obj::access;

        for (int i = 0; i < 5; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }
}

// ==================== MULTIPLE CONDITIONS ====================
// EXPLANATION:
// ReentrantLock allows creating multiple Condition objects for fine-grained signaling.
// Each Condition acts like a separate wait/notify queue.
// Use case: producer-consumer with multiple producers or consumers.
class SharedBuffer {
    private int data = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition producerCondition = lock.newCondition();
    private final Condition consumerCondition = lock.newCondition();
    private boolean available = false;

    public void produce(int value) throws InterruptedException {
        lock.lock();
        try {
            while (available) producerCondition.await(); // wait until consumed
            data = value;
            System.out.println("Produced: " + data);
            available = true;
            consumerCondition.signal(); // notify consumer
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (!available) consumerCondition.await(); // wait until produced
            System.out.println("Consumed: " + data);
            available = false;
            producerCondition.signal(); // notify producer
        } finally {
            lock.unlock();
        }
    }
}

class ProducerConsumerTest {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try { buffer.produce(i); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try { buffer.consume(); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        });

        producer.start();
        consumer.start();
    }
}

// ==================== READ-WRITE LOCK ====================
// EXPLANATION:
// ReadWriteLock allows multiple readers simultaneously, but only one writer exclusively.
// Improves performance in read-heavy applications.
class ReadWriteDemo {
    private int data = 10;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void write(int value) {
        rwLock.writeLock().lock();
        try {
            data = value;
            System.out.println(Thread.currentThread().getName() + " wrote " + data);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void read() {
        rwLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " read " + data);
        } finally {
            rwLock.readLock().unlock();
        }
    }
}

class ReadWriteTest {
    public static void main(String[] args) {
        ReadWriteDemo obj = new ReadWriteDemo();

        Runnable reader = obj::read;
        Runnable writer = () -> obj.write((int) (Math.random() * 100));

        Thread w = new Thread(writer, "Writer");
        Thread r1 = new Thread(reader, "Reader-1");
        Thread r2 = new Thread(reader, "Reader-2");

        w.start();
        r1.start();
        r2.start();
    }
}

// ==================== CLASSICAL WAIT / NOTIFY / NOTIFYALL ====================
// EXPLANATION:
// wait(), notify(), notifyAll() are used with synchronized blocks.
// wait() makes a thread release the lock and wait.
// notify() wakes up one waiting thread.
// notifyAll() wakes up all waiting threads.
// Use case: traditional producer-consumer coordination.
class WaitNotifyDemo {
    private static final Object lock = new Object();
    private static boolean ready = false;

    public static void main(String[] args) {
        Thread consumer = new Thread(() -> {
            synchronized (lock) {
                while (!ready) {
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                System.out.println("Consumer received signal!");
            }
        });

        Thread producer = new Thread(() -> {
            synchronized (lock) {
                ready = true;
                lock.notify(); // use notifyAll() if multiple consumers
                System.out.println("Producer sent signal!");
            }
        });

        consumer.start();
        producer.start();
    }
}



/*
=======================================
JAVA THREAD POOL
=======================================

Core Explanation and Analogy
Scenario Setup:
Imagine you are selling lemonade. Initially, you have only one customer, so you serve him personally.
When the number of customers increases, you call your friends to help serve them.
However, calling many friends (e.g., 10 friends) to handle 10 customers doesn’t always work well because:
Some friends may be busy or unavailable.
You might end up with fewer helpers than needed, causing delays or incomplete work.
To solve this, you decide to call only a fixed set of three reliable friends who always come to help.
Even if customer numbers grow, your fixed group of friends will handle tasks one by one, ensuring at least
some customers get served promptly.
This analogy represents how thread pools work by maintaining a fixed number of pre-initialized threads ready
to perform tasks instead of creating a new thread every time.

Technical Explanation of Thread Pools
Thread Pool Definition:
A thread pool is a collection of pre-created threads that are ready to perform tasks.
Instead of creating and destroying a thread for each task, threads are reused, improving efficiency.

Key Benefits of Thread Pools:

Benefits
Resource Management	: Creating and destroying threads for every task is expensive and resource-intensive.
Reduced Overhead : 	Thread creation and destruction add overhead; thread pools minimize this by reusing threads.
Improved Response Time	: Pre-initialized threads are immediately available, reducing waiting time and speeding up execution.
Control Over Thread Count: Limits the maximum number of threads, preventing resource exhaustion from creating too many threads.
Problem Without Thread Pools:
Creating a new thread for each task leads to heavy overhead, inefficient memory use,
and unpredictable behavior due to thread availability.

How Thread Pools Solve These Problems:
By maintaining a fixed pool of threads, the system avoids repeatedly creating and destroying threads,
thus optimizing resource usage and ensuring better control over concurrent processing.
*/



/*
=======================================
Executor Framework
=======================================

Core Concepts and Motivation
Executors Framework abstracts and simplifies thread creation, scheduling, and management in Java,
avoiding manual thread handling which is error-prone and inefficient.
Prior to Executors, developers manually created and destroyed threads, leading to:
Manual thread management overhead
Resource management issues
Poor scalability (system might crash under heavy load)
Performance overhead due to frequent thread creation and destruction
Complex error handling
Executors handle these complexities internally, allowing developers to focus on business logic rather than threading details.
The framework supports thread reuse via thread pools, improving performance and resource utilization.

==================== KEY INTERFACES AND CLASSES ====================
Executor :
Basic interface with method execute(Runnable command) to run tasks asynchronously.

ExecutorService	:
Extends Executor, adds lifecycle management (shutdown, awaitTermination) and task submission with submit(). Supports Future results.

ScheduledExecutorService :
Extends ExecutorService, supports delayed and periodic task execution (schedule(), scheduleAtFixedRate()).

Executors (Utility Class)	:
Provides factory methods to create different types of thread pools (fixed, single, cached).

==================== MANUAL THREAD CREATION vs EXECUTOR  FRAMEWORK====================
Using Manual thread creation :
Manual thread creation requires   thread management like Creating an array to hold threads ,Starting threads
Waiting for all threads to complete using .join() . Also, manual thread management lacks thread reuse.

Using ExecutorService :
Executors provide thread pools for reusing threads, e.g., Executors.newFixedThreadPool(n).
Task submission via submit() accepts: Runnable (no return value) , Callable (returns a value)
submit() returns a Future object to track task completion and retrieve results.
Key methods:
shutdown() prevents new tasks but completes submitted tasks.
shutdownNow() attempts to stop all executing tasks immediately.
awaitTermination(timeout, unit) waits for all tasks to finish or timeout.
isShutdown(), isTerminated() check executor status.


==================== RUNNABLE vs CALLABLE ====================
Feature	                            Runnable	                                Callable
Return Value	        None (void run())	                    Returns a value (call())
Exception Handling	    Cannot throw checked exceptions	        Can throw exceptions
Usage	                When no return value needed	            When a result is expected
                        simpler but limited;                    supports return values and exceptions.
***  submit() overloads support both types.

==================== FUTURE INTERFACE ====================
Represents the result of an asynchronous computation.
Methods :
get(): blocks until computation completes, returns result.
get(timeout, unit): waits for specified time, throws TimeoutException if not done.
cancel(mayInterruptIfRunning): attempts to cancel task.
isDone(): checks if task is completed.
isCancelled(): checks if task was cancelled.


====================  ScheduledExecutorService ====================
Used for delayed and periodic task execution.
Key methods:
schedule(Runnable, delay, TimeUnit): executes task once after delay.
scheduleAtFixedRate(Runnable, initialDelay, period, TimeUnit): executes task repeatedly at fixed intervals.
scheduleWithFixedDelay(Runnable, initialDelay, delay, TimeUnit): executes task repeatedly with fixed delay between completions.
Shutdown behavior requires explicit handling to avoid premature termination of periodic tasks.

For periodic tasks, proper shutdown management is critical to avoid tasks running indefinitely.
Types of Thread Pools via Executors
Thread Pool Type	Description	Use Case
FixedThreadPool	Fixed number of threads reused for tasks	When task load is steady and predictable
SingleThreadExecutor	Single thread executor for sequential execution	Serial task execution
CachedThreadPool	Dynamically sized pool, creates new threads as needed, reuses idle threads,
removes threads after 60 seconds inactivity	When load is variable and tasks are short-lived
Cached thread pools can grow unbounded, which may lead to resource exhaustion under heavy load.
Choosing thread pool type depends on application needs and load characteristics.

*/

//Guarantees order
//Useful for logging, file writes, sequential tasks
class SingleThreadExecutorDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> System.out.println("Task 1"));
        executor.submit(() -> System.out.println("Task 2"));
        executor.submit(() -> System.out.println("Task 3"));

        executor.shutdown();
    }
}


//Reuses a fixed number of threads
class FixedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 6; i++) {
            int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
    }
}


//Best for short-lived tasks
//Threads created as needed, reused if idle
class CachedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " handled by " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
    }
}


// Callable → returns value
// Future.get() blocks until result is ready
class CallableDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> task = () -> {
            Thread.sleep(500);
            return 42;
        };

        Future<Integer> future = executor.submit(task);

        System.out.println("Result: " + future.get());

        executor.shutdown();
    }
}


//SCHEDULED EXECUTOR (Delayed Task)
class ScheduledDemo {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            System.out.println("Task executed after 3 seconds");
        }, 3, TimeUnit.SECONDS);

        scheduler.shutdown();
    }
}


//PERIODIC TASK (Fixed Rate)
//Runs forever unless shutdown
class ScheduledFixedRateDemo {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Running every 2 seconds");
        }, 1, 2, TimeUnit.SECONDS);
    }
}


//FUTURE OBJECT EXAMPLE
class FutureCompleteDemo {

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);


        // TASK 1 : FINISHES NORMALLY
        Callable<Integer> fastTask = () -> {
            Thread.sleep(1000);
            System.out.println("Fast task completed");
            return 10;
        };


        // TASK 2 : LONG RUNNING TASK
        Callable<Integer> slowTask = () -> {
            try {
                while (true) {
                    System.out.println("Slow task running...");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println("Slow task interrupted!");
                return -1;
            }
        };

        Future<Integer> future1 = executor.submit(fastTask);
        Future<Integer> future2 = executor.submit(slowTask);


        // CHECK STATUS (isDone)
        System.out.println("Future1 done? " + future1.isDone());
        System.out.println("Future2 done? " + future2.isDone());


        // GET WITH TIMEOUT
        try {
            System.out.println("Future1 result: " + future1.get(2, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            System.out.println("Future1 timed out");
        }


        // CANCEL SLOW TASK
        Thread.sleep(2000);
        System.out.println("Cancelling slow task...");
        future2.cancel(true);


        // CHECK CANCELLATION STATUS
        System.out.println("Future2 cancelled? " + future2.isCancelled());
        System.out.println("Future2 done? " + future2.isDone());


        // SHUTDOWN EXECUTOR
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Executor shut down");
    }
}



/*
=======================================
COUNTDOWNLATCH
=======================================

PROBLEM IT SOLVES:
In multithreading, sometimes we want a thread (usually main) to wait until
a set of other threads finish their work.
Without this, we would have to:
- Use multiple join() calls → cumbersome for many threads
- Use Future.get() → blocks, but not flexible for multiple waiters

CountDownLatch simplifies this:
- You give it a count
- Other threads call countDown() when they finish
- Waiting threads call await() and automatically continue when count reaches 0


KEY METHODS

- new CountDownLatch(n)        → create latch with count n
- countDown()                  → decrement count
- await()                      → block until count reaches 0
- await(timeout, TimeUnit)     → block with timeout
- getCount()                   → remaining count

Rules:
- Count cannot be reset
- Once count reaches 0, all waiting threads are released forever

*/


class CountDownLatchBasicDemo {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(3); // create latch with count = 3

        Runnable worker = () -> {
            try {
                Thread.sleep(1000); // simulate work
                System.out.println(Thread.currentThread().getName() + " finished work");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown(); // signal completion
            }
        };

        new Thread(worker, "Worker-1").start();
        new Thread(worker, "Worker-2").start();
        new Thread(worker, "Worker-3").start();

        System.out.println("Main thread waiting...");
        latch.await(); // wait until count reaches 0
        System.out.println("All workers finished. Main continues.");
    }
}



//MULTIPLE WAITERS EXAMPLE
//More than one thread can wait on the same latch.
//All waiting threads are released when count reaches 0
class MultipleWaitersDemo {

    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(2);

        Runnable waiter = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting");
                latch.await(); // blocks until count = 0
                System.out.println(Thread.currentThread().getName() + " released");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(waiter, "Waiter-1").start();
        new Thread(waiter, "Waiter-2").start();

        new Thread(() -> {
            try { Thread.sleep(1000); } catch (Exception e) {}
            latch.countDown();
            System.out.println("Task-1 done");
        }).start();

        new Thread(() -> {
            try { Thread.sleep(2000); } catch (Exception e) {}
            latch.countDown();
            System.out.println("Task-2 done");
        }).start();
    }
}


//AWAIT WITH TIMEOUT
//Avoid infinite waiting if some threads are delayed
class AwaitTimeoutDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Thread t = new Thread(() -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) {}
            latch.countDown();
        });
        t.start();

        boolean completed = latch.await(2, java.util.concurrent.TimeUnit.SECONDS);
        if(completed) System.out.println("Task completed within timeout");
        else System.out.println("Timeout occurred, proceeding anyway");
    }
}



/*
=======================================
CYCLICBARRIER
=======================================

PROBLEM IT SOLVES:
Sometimes in multithreading, we want a group of threads to wait for each other
at a common point before proceeding. Unlike CountDownLatch, which is one-time use,
CyclicBarrier can **reset and be reused** for multiple cycles.

Use case:
- Multiple threads perform partial work
- All threads wait for each other at a barrier
- Once all threads reach barrier → continue together

KEY METHODS

- new CyclicBarrier(int parties)              → number of threads to wait for
- new CyclicBarrier(int parties, Runnable barrierAction) → action to execute once all threads arrive
- await()                                     → thread waits at the barrier
- await(timeout, TimeUnit)                    → wait with timeout
- getParties()                                → total threads required
- getNumberWaiting()                          → threads currently waiting

Rules:
- CyclicBarrier can be **reused** after tripping
- If a thread is interrupted or times out, the barrier is broken → BrokenBarrierException

*/

class CyclicBarrierBasicDemo {
    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All threads reached barrier. Continue!"));

        Runnable worker = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " performing work");
                Thread.sleep((long)(Math.random() * 2000));
                System.out.println(Thread.currentThread().getName() + " waiting at barrier");
                barrier.await(); // wait for others
                System.out.println(Thread.currentThread().getName() + " passed barrier");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        new Thread(worker, "Thread-1").start();
        new Thread(worker, "Thread-2").start();
        new Thread(worker, "Thread-3").start();
    }
}



//REUSABILITY EXAMPLE
//CyclicBarrier can be reused after tripping. Threads can use it again in next cycle.
class CyclicBarrierReusableDemo {
    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(2, () -> System.out.println("Barrier tripped!"));

        Runnable worker = () -> {
            try {
                for(int i = 1; i <= 2; i++) {
                    System.out.println(Thread.currentThread().getName() + " working cycle " + i);
                    Thread.sleep((long)(Math.random() * 1000));
                    barrier.await(); // wait for other thread
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        new Thread(worker, "Thread-A").start();
        new Thread(worker, "Thread-B").start();
    }
}


//AWAIT WITH TIMEOUT
//Avoid waiting forever; if some thread is delayed, proceed or handle exception
class CyclicBarrierTimeoutDemo {
    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(2);

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting at barrier");
                barrier.await(2, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + " passed barrier");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(Thread.currentThread().getName() + " timed out or interrupted");
            }
        }, "Thread-1").start();

        new Thread(() -> {
            try { Thread.sleep(5000); } catch (Exception e) {}
            try { barrier.await(); } catch (Exception e) {}
        }, "Thread-2").start();
    }
}

