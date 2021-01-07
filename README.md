# netlogo-jvmgr

A NetLogo extension allowing you to access information about the Java Virtual Machine (JVM) in NetLogo. This is useful for getting information on the computational resources your model might demand; especially when preparing it for running in headless mode on a cluster where such information may be needed.

The following reporters are provided, none of which take any arguments, and all of which are observer only. (There's an argument that `thread-cpu`, `thread-system` and `thread-user` should not be observer-only, as this would allow you to find out more about resources used agent-by-agent.)

+ `mgr:mem` The total amount of (heap and non-heap) memory used, in bytes.
+ `mgr:mem-p` The amount of heap and non-heap memory used as a proportion of the amount allocated.
+ `mgr:mem-pct-str` A string reporting `mgr:mem-p` as an integer percentage (ending with a "%" character).
+ `mgr:heap-used` Amount of heap memory used (in bytes). Heap memory is the memory allocated by the JVM to storage of Java objects. 
+ `mgr:non-heap-used` Amount of non-heap memory used. 'Non-heap' memory is memory allocated by the JVM for classes and metadata.
+ `mgr:heap-alloc` Amount of heap memory allocated ('committed' in Java jargon). See [Java documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/management/MemoryUsage.html) for more information.
+ `mgr:non-heap-alloc` Amount of non-heap memory allocated.
+ `mgr:heap-init` Initial amount of heap memory the JVM requested from the operating system. (May be undefined.)
+ `mgr:non-heap-init` Initial amount of non-heap memory the JVM requested from the operating system. (May be undefined.)
+ `mgr:heap-max` Maximum amount of heap memory that can be used for memory management. (May be undefined.)
+ `mgr:non-heap-max` Maximum amount of non-heap memory that can be used for memory management. (May be undefined.)
+ `mgr:mem-str` The heap and non-heap memory used in a human-readable string.
+ `mgr:heap-used-str` The heap memory used in a human-readable string.
+ `mgr:non-heap-used-str` The non-heap memory used in a human-readable string.
+ `mgr:alloc-str` The heap and non-heap memory allocated in a human-readable string.
+ `mgr:threads` The number of currently running threads.
+ `mgr:peak-threads` The highest number of concurrent threads.
+ `mgr:all-threads` The total number of threads that the JVM has ever started.
+ `mgr:thread-cpu` The total amount of CPU time the thread associated with the caller has been running for.
+ `mgr:thread-user` The amount of CPU time the thread associated with the caller has spent running user code.
+ `mgr:thread-system` The amount of CPU time the thread associated with the caller has spent running operating system code (e.g. for file I/O).
+ `mgr:cpu-time` The total amount of CPU time all threads have been running for.
+ `mgr:user-time` The total amount of CPU time all threads have spent running user code.
+ `mgr:system-time` The total amount of CPU time all threads have spent running operating system code.
+ `mgr:blocked-time` The total amount of time threads have spent being blocked from execution (i.e. waiting to enter `synchronized` code currently occupied by another thread)
+ `mgr:blocked-count` A count of the number of times threads have been blocked from execution.
+ `mgr:waited-time` The total amount of time threads have spent waiting for a signal from another thread.
+ `mgr:waited-count` A count of the number of times threads have waited for a signal from another thread.
+ `mgr:jvm-start-time` The (approximate) start time of the JVM in seconds since midnight UTC on 1 January 1970
+ `mgr:jvm-uptime` The (approximate) amount of time the JVM has been running for (in seconds)
+ `mgr:gc-count` A count of the number of garbage collections the JVM has done.
+ `mgr:gc-time` The amount of time in seconds spent on garbage collection.
+ `mgr:jvm-proc-count` The number of cores available to the JVM.
+ `mgr:system-load` The average system load over the last minute. See [the java documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/management/OperatingSystemMXBean.html#getSystemLoadAverage--) for more information.
+ `mgr:jvm-name` The name of the JVM. There's no guaranteed information contained here.
+ `mgr:cpu-time-str` A human-readable string option for `mgr:cpu-time`.
+ `mgr:user-time-str` A human-readable string option for `mgr:user-time`.
+ `mgr:system-time-str` A human-readable string option for `mgr:system-time`.
+ `mgr:waited-time-str` A human-readable string option for `mgr:waited-time`.
+ `mgr:blocked-time-str` A human-readable string option for `mgr:blocked-time`.
+ `mgr:jvm-uptime-str` A human-readable string option for `mgr:jvm-uptime`.

Note that the ability to run these commands is sensitive to the particular Java Virtual Machine you are running, and any permission settings it has. Numerical commands return `Double.NaN` if the JVM does not support the command. Since it traps division by zero, NetLogo doesn't provide commands like `is-nan?` and `is-finite?` (and `is-number?` returns `true` for a `Double.NaN`). You can nevertheless still test for `Double.NaN` returned by one of the above functions by saying `(word mgr:`_`whatever`_`) = "NaN"`, or use `carefully` around any mathematical expressions using a numeric answer. Reporters ending `str` return `"NA"`. Java's `[java.lang.management](https://docs.oracle.com/javase/8/docs/api/java/lang/management/package-frame.html)` package provides many more methods that could be called to provide useful information. In this first release, I picked methods that are less likely to be unsupported.

The netlogo folder contains an example.