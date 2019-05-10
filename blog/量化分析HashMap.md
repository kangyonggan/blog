---
title: 量化分析HashMap
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---


## HashMap的数据结构
HashMap是由`数组`和`链表`构成的，如下图：

![HashMap](/upload/article/hashmap.png)

<!-- more -->

用代码来描述如下：

```
public class HashMap<K,V> {
    // 数组。数组元素为链表的首节点
    Node<K,V>[] table;
    
    // 链表的节点
    class Node<K,V> {
        int hash;
        K key;
        V value;
        // 下一个节点
        Node<K,V> next;
    }
}
```

## 变量说明
```
public class HashMap<K,V> {
    /**
     * 默认容量，16。
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * 最大容量
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 默认负载因子，0.75f。
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    /**
     * 数组
     */
    private Node<K,V>[] table;
    
    /**
     * 节点的总数
     */
    private int size;

    /**
     * 阈值。当size大于阈值时会进行扩容
     */
    private int threshold;
    
    /**
     * 负载因子，是用于计算阈值的一个因子
     */
    private float loadFactor;

}
```

## new HashMap()
当我们new一个HashMap时，量化分析法如下。

### new HashMap()
- loadFactor = DEFAULT_LOAD_FACTOR； // 0.75f

### 第1次put
- size++; // 1
- table = new Node[DEFAULT_INITIAL_CAPACITY]; // table.length: 16
- threshold = (int) DEFAULT_INITIAL_CAPACITY * loadFactor; // 12

### 第2次put
- size++; // 2
- table.length不变; // 16
- threshold不变; // 12

### 第13次put
- size++; // 13
- table.length扩大一倍;// 32
- threshold扩大一倍;// 24

## new HashMap(5)
当我们new一个带一个参数的HashMap时，量化分析法如下。

### new HashMap(5)
- loadFactor = DEFAULT_LOAD_FACTOR； // 0.75f
- threshold = 不小于5的最小的2的n次方; // 8

### 第1次put
- size++; // 1
- table = new Node[threshold]; // table.length: 8
- threshold = (int) table.length * loadFactor;// 6

### 第2次put
- size++; // 2
- table.length不变; // 8
- threshold不变; // 6

### 第7次put
- size++; // 7
- table.length扩大一倍; // 16
- threshold扩大一倍; // 12
 
## new HashMap(0/1)
当我们new一个带一个参数的HashMap时，参数值为负数时会报错，参数值为0或1时，量化分析法如下。

### new HashMap(0/1)
- loadFactor = DEFAULT_LOAD_FACTOR； // 0.75f
- threshold = 不小于0/1的最小的2的n次方; // 1

### 第1次put（会扩容）
- size++; // 1
- table = new Node[threshold]; // table.length: 1
- threshold = (int) table.length * loadFactor;// 0

此时由于size > threshold，因此会扩容，扩容后：

- table.length扩大一倍; // 2
- threshold = (int) table.length * loadFactor;// 1

### 第2次put（会扩容）
- size++; // 2

此时由于size > threshold，因此会扩容，扩容后：

- table.length扩大一倍; // 4
- threshold = (int) table.length * loadFactor;// 2

### 第3次put（会扩容）
- size++; // 3

此时由于size > threshold，因此会扩容，扩容后：

- table.length扩大一倍; // 8
- threshold = (int) table.length * loadFactor;// 4

### 第4次put
- size++; // 4
- table.length不变; // 8
- threshold不变;// 4

## 小结
1. 不要new HashMap(0)或者new HasMap(1)，即使你只需要存1个值。
2. 尽量在new HashMap时指定容量大小，因为默认为16，可能有点浪费。
3. 如果你需要存放1~3个，那就new HashMap(2~4)。
4. new Hash(2)和new HashMap(3)、new HashMap(4)是一样的。
5. 如果想要存放4~6个，那就new HashMap(5~8)。
6. 如果想要存放7~12个，那就new HashMap(9~16)或者new HashMap()。
7. 并不是new HashMap(初始大小)，它的容量就是‘初始大小’。

> new HashMap还有两个参数的构造，第二个参数是负载因子，一般不需要指定，默认0.75即可。










