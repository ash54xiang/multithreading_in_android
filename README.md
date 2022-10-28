# MultiThreading in Android

## Garbage collection in Android
- when an object is reachable, it will not be eligible for GC
- when an object is not reachable, it will be eligible for GC

- UML example

![Class Diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/ash54xiang/multithreading_in_android/master/UML/garbage_collection.puml)


- An object is reachable when the root object is referencing it.
- Objects are not reachable when no root object is referencing them even if they are contained in a circular reference, without root object referencing either of them, they are unreachable.

## Important Root objects in Android Application
1. Objects referenced from static fields
2. Instances of APplication class
3. **Live threads**

## What is Live Thread?
- when a thread is instantiated, it is not live yet
- when a thread.start() is called, it is live.
    - whatever objects this thread references by itself, those will be held in the memory and will not be eligible for GC
- when a thread.run() returns, the thread is **dead**.
- At that point, the references in this thread will be GC if they are not referenced anywhere else.

## Inner Thread
- When a thread is internally instantiated and started in the MainActivity, it will have implicit references to MainActivity, therefore as long as the inner thread is live, the MainActivity can't be eligible for GC.

*Therefore, it is very important to terminate a thread started in the application in a timely manner to prevent memory leaks!*

## Thread termination Strategies
1. Return from run() after successful execution
2. Return from run() in response to internal error
3. Return from run() in response to externally set flag
4. Return from run() in response to interruption

## UI Thread (Main thread) Characteristic in Android 
1. Started by Android Framework when application starts
2. Termintaed only when application's process terminates
3. Application's user interface is "drawn" on UI thread
4. Lifecycle callbacks (e.g. onStart(), onStop()) are executed on UI thread
5. User Interface event handlers (e.g. onClick()) are executed on UI thread
6. Changes of user interface must be performed on UI thread

Background thread:
    - Any thread in Android application other than UI thread

## UI Responsiveness Requirements
- Android applications should render 60 FPS
- New frame each 16ms
- Your code shouldn't execute on UI thread for longer than few ms
- Warning Signs:
    1. Freezes of UI, non-smooth animations, etc
    2. Notifications about skipped frames in logcat
    3. Crashes due to Application Not Responding (ANR) error
- Zero skipped frames is one of the fundamental quality standards of Android applications
- All time-consuming operations should be offloaded to background threads

## How to pass a view from a backgroud thread back to UI thread (Main)
- When a background thread wishes to change something on the UI, simply changing it would cause the app to crash.
- Tha means, all modification on UI has to be on *UI thread ONLY*.
- We need a special UI handler to allow us to modify the UI from a background thread.

```
private Button mBtnCountIterations; // this is UI component
private final Handler mUiHandler = new Handler(Looper.getMainLooper()); // this is the Handler we need to modify UI from background thread

// post the modification to the UI thread via the handler
mUiHandler.post(new Runnable() {
    @Override
    public void run() {
        Log.d("UiHandler", "Current thread: " + Thread.currentThread().getName());
        mBtnCountIterations.setText("Iterations: " + iterationsCountFinal);
    }
});
```

- UI thread is a special "looper" thread that Android framework starts for each app.

## UI thread rules of thumb
1. Don't perform any long-running tasks on UI thread (longer than few ms).
2. Offload long-running tasks to background threads.
3. Return execution from background to UI thread for user interface manipulations.
4. Aim at zero skipped frames and watch for warning signs

## Multithreading Challenges
1. Visibility
    - changes made by 1 thread to shared data are visible to other threads
    - in Java, a modifier **volatile** is required to be added to a shared data
        - Volatile basically tells the compiler that the value of the data must *never be cached* as its value may change outside of the scope of the program/threads
2. Atomicity
    - Only **One thread** or process can execute a block of code at a time
