# Reproducer for a Gradle bug in evaluating build service parameters

Execute `./gradlew myTask1 --warning-mode=all`

It prints that `myTask1` uses `Project` at execution time.

<hr/>

Execute `./gradlew myTask2 --warning-mode=all`

`myTask2` also uses `Project` similarly at execution time, but no warning is printed.

<hr/>

Execute `./gradlew myTask1 --configuration-cache`

It fails to store a configuration cache entry reporting that 
> invocation of 'Task.project' at execution time is unsupported.

However, it works fine on Gradle 8.0-rc-3

<hr/>

Execute `./gradlew myTask1 --configuration-cache --configuration-cache-problems=warn` twice

It prints a warning, but both runs run fine.