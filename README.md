# hyperion

A framework for creating data services in Java.

## Features
* Built in versioning support
* Sparse retrieves/updates
* Built in paging support
* Built in validation support
* Configurable ad-hoc queries
* Configurable ad-hoc sorting
* Optional history support
* Hooks for authorization based visibility of records and fields
* Built using a SEDA pipeline, supports either synchronous and asynchronous execution

Hyperion was designed to allow developers to quickly implement standardized data services without worrying about
the underlying service plumbing. It is based on experience implementing standard data service in several different organizations.
I've taken the lessons learned from implementing several different frameworks through the years and boiled it down to a framework that
supports quickly creating data services. I classify this as a "Simple Service Framework" to avoid the religious war behind the REST and Restful
service monikers. This framework supports most general use cases for data services but may not be a good fit for complex use cases, so caveat emptor.

# Why Another Data Service Framework?
I looked at several different Java based data service frameworks, such as [Spring Data Rest](http://projects.spring.io/spring-data-rest/)
and [odata4j](https://code.google.com/p/odata4j/) and found that they had similar attributes:
* Exposed the persistence model directly, with little or no ability to modify the external representation
* Limited support for complex and context specific (insert/update/delete) validations
* Limited support for truly ad-hoc queries and sorts
* No real support for user based visibility

This had several problems for the types of use cases I needed to support:
* Allowing data objects to change over time while maintaining backward compatibility using a single endpoint
* Arbitrarily complex ad-hoc queries
* Multi-level sorts
* User based visibility for records and fields
* Validations/referential integrity checks
* Sparse GETs

Hyperion was designed to separate the external API representation of a data object from the underlying persistence model used to store the data. This
has several advantages:
* The data model can change over time. Since the API model is independent from the persistence model different versions of the API model can be supported. The persistent
API models can also evolve independently.
* The persistence mechanism can be changed without impacting the callers.
* The caller can specify the fields to return. The framework will populate only the API fields being requested.
* The caller can specify only changed fields om a write operation. The framework will only populate the persistent fields that were sent in the API model.


# Underlying Architecture
Each data object defined in a Hyperion service is called an entity. Each entity is a separate endpoint in the service, each of which support
standard CRUD operations using POST, GET, PUT, and DELETE HTTP operations respectively. Each entity has a single persistent representation
and one or more API representations, specified as versions of that entity API object. Each version of an entity API object has
a corresponding translator to covert between the API representation and the persistent representation as well as a validator to
enforce any necessary validation rules for a specific version of an entity.

Translation uses a convention over configuration design principle. If a field on the API object has the same name and type
as the field on the persistent object then translation happens automatically. Otherwise some level of configuration needs to
be done to enable translation. Configuring fields that can be sorted and queried is similar, the assumption is that all fields
are supported unless excluded.

# Using Hyperion
TBD