# Mapping Test Utils

This library provides useful functionality for creating 
test cases that check the correct implementation of 
a previously defined mapping specification inside an Excel sheet.

## Getting Started

### Add Maven Dependency
First include the library as a dependency inside your project's ``pom.xml``.
```xml
<dependency>
  <groupId>tbd</groupId>
  <artifactId>tbd</artifactId>
  <version>tbd</version>
</dependency>
```

### Specify Mapping

The mapping is specified via Excel sheets. Make sure you have the following columns:
* *Source Path (XPath):* Destination of the source value
* *Target Path (JsonPath):* Target destination of the value
* *Assert Mapping:* Whether the mapping should be checked during test execution
* *Default:* A fixed value that should be set at the target destination; the source path then can be empty

Each of the column names can be chosen freely and then passed as parameters during test creation.

#### Example

#### JsonPath to XPath
tbd

#### Fixed Default Value to XPath 
tbd

### Testing

In order to validate the implementation of the mapping previously defined, this library provides 
a couple of useful test util classes:

tbd

