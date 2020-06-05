# spring-caching-examples

# Overview

Caching at its most fundamental level, provides a means of being able to retrieve data faster than would otherwise 
be possible if a complete trip was to have occurred to retrieve that data from some persistent storage such as an RDBMS.

This set of samples provides two implementations of caching leveraging:

- Caffeine (In-Process Cache)
- Redis (Distributed Cache)

## Local Cache

An **In-Process Cache** is an object-based cache implementation which runs within the same VM as the application.

## Distributed Cache

A **Distributed Cache** is external to the application and in a High Availabilty (HA) topology, may likely be deployed
on multiple nodes forming a large logical cache.

# Spring Caching Abstraction

The Spring Framework provides support for transparently adding caching support to a Spring application. The [Spring Cache
Abstraction](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-caching) 
allows for consistent usage of various Caching solutions with minimal changes made to the codebase.


For access to the DB2 H2 console, please navigate to:

```
http://localhost:[port]/h2-console
```

where:

**[port]** is the port used to host the specific application.

|   Application                 | Port      | Description                           |
|-------------------------------|-----------|---------------------------------------|
| spring-caching-with-caffeine  | 10090     | Cache implementation using Caffeine   |
| spring-caching-with-redis     | 10091     | Cache implementation using Redis      |

### Guides

The following guides illustrate how to use some features concretely:

* [Caching Data with Spring](https://spring.io/guides/gs/caching/)
