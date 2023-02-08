group = "org.example"
version = "1.0-SNAPSHOT"

abstract class MyBuildService : BuildService<MyBuildService.Parameters> {
    interface Parameters : BuildServiceParameters {
        val someProp: Property<String>
    }
}

inline fun <reified T : Any?> ObjectFactory.property(initialValue: T) = property<T>().value(initialValue)

inline fun <reified T : Any?> ObjectFactory.providerWithLazyConvention(
    noinline lazyConventionValue: () -> T
) = property(lazyConventionValue).map { it.invoke() }

val myService1 = gradle.sharedServices.registerIfAbsent("my-service-1", MyBuildService::class.java) {
    parameters.someProp.set(objects.providerWithLazyConvention {
        println("The property is being calculated")
        gradle.taskGraph.allTasks.joinToString { it.project.name }
    })
}

val myService2 = gradle.sharedServices.registerIfAbsent("my-service-2", MyBuildService::class.java) {
    parameters.someProp.set(objects.providerWithLazyConvention {
        println("The property is being calculated")
        project.version.toString()
    })
}

val myTask1 by tasks.registering {
    val localService = myService1
    usesService(localService)
    doFirst {
        println(localService.get().parameters.someProp.get())
    }
}

val myTask2 by tasks.registering {
    val localService = myService2
    usesService(localService)
    doFirst {
        println(localService.get().parameters.someProp.get())
    }
}