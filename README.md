[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](/LICENSE)
# Mionik

`Mionik` is an tool that allows your to create an instance of kotlin class on the fly. It creates an object and fills its properties with random values, but imposing restrictions as specified by the user.

## Prerequisites

Before you begin, ensure you have met the following requirements:
* You have installed the `java Runtime Environment(JRE)`

## Build with 
* [kotlin.reflect](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/ "kotlin.reflect")
* [Gradle](https://gradle.org/ "Gradle")
`
## Usage

#### DSL

```kotlin
    val config = configuration {
        properties {
            isNullableGenerated = false  // property marked as nullable will not be generated 
            itemsInCollection = 2        // will be generated 2 items in each collection
        }

        restriction {
            // Set value by property data type
            // For property with type UUID with be generated random uuid
            bound(UUID::class) { UUID.randomUUID() }
             
            // Set value by property name
            // For property 'name' set value "John"
            bound(Person::name) { "John" }
        }
    }

    val person = morph(Person::class, config)
```
#### Processing result
```kotlin

    // '.get' may throw exception if something gone wrong
    val person: Result<Person, ReflectionError> = morph(Person::class, config).get
    

    // handle fail 
    val person: Result<Person, ReflectionError> = morph(Person::class, config)
        .doOnError { /* do something */ }
```

