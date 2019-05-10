---
title: Java Compile Expression Language
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

## Quick Start

### Dependency

```
<dependency>
    <groupId>com.kangyonggan</groupId>
    <artifactId>jcel</artifactId>
    <version>1.0</version>
</dependency>
```

### Code
```
// treeMaker and names is JCTree's environment
JCExpressionParser parser = new JCExpressionParser(treeMaker, names);
JCTree.JCExpression expression = parser.parse(
    "Hello ${user.info.name}, welcome use my ${project[0](1).name}, thanks!");
System.out.println(expression);
```

Output:

```
"Hello " + user.getInfo().getName() + ", welcome use my " + project[0].get(1).getName();
```
