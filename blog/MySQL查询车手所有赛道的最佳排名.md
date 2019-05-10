---
title: MySQL查询车手所有赛道的最佳排名
date: 2018-09-14 12:40:22
categories: MySQL
tags:
- MySQL
---

## 数据结构
不相关字段不在此列出

### 赛局表
- 赛局ID - BIGINT
- 赛道 - INTEGER

### 成绩表
- 赛局ID - BIGINT
- 车手 - VARCHAR
- 成绩(毫秒) - INTEGER

<!-- more -->

## 测试数据
### 赛局表
| 赛局ID | 赛道
| ---- | ----
|   1  | 10
|   2  | 11
|   3  | 10

### 成绩表
| 赛局ID | 车手 | 成绩
| ---- | ---- | ----
|   1  | 小明 | 121005
|   1  | 小红 | 120832
|   1  | 小白 | 120511
|   2  | 小明 | 150932
|   2  | 小白 | 151023
|   3  | 小明 | 120732
|   3  | 小红 | 120833

从表中数据可以看到，小明同学10赛道的最佳排名是第2，在11赛道的最佳排名是第1，所以小明在所有赛道的最佳排名应该是第1。

## 分解需求
### 1. 查询10赛道各车手的最佳成绩
```
SELECT
  b.赛道, a.车手, min(a.成绩) AS 成绩
FROM 成绩表 a, 赛局表 b
WHERE a.赛局ID = b.赛局ID
  AND b.赛道 = 10
GROUP BY a.车手
ORDER BY 成绩 ASC
```

为什么group by 车手？  
因为一个车手在一个赛道中可能跑了N多个成绩，我们只取最好的那个成绩。

### 2. 查询10赛道各车手的排名
```
SELECT
  @排名 := @排名 + 1 AS 排名,
  c.赛道,
  c.车手,
  c.成绩
FROM
  (
    SELECT
      b.赛道, a.车手, min(a.成绩) AS 成绩
    FROM 成绩表 a, 赛局表 b
    WHERE a.赛局ID = b.赛局ID
          AND b.赛道 = 10
    GROUP BY a.车手
    ORDER BY 成绩 ASC
  ) c, (SELECT @排名 := 1) d
```

在MySQL中，@变量名 := 值，是一个赋值语句。

### 3. 查询所有赛道各车手的最佳成绩
```
SELECT
  b.赛道, a.车手, min(a.成绩) AS 成绩
FROM 成绩表 a, 赛局表 b
WHERE a.赛局ID = b.赛局ID
      AND b.赛道 = 10
GROUP BY a.车手, b.赛道
ORDER BY b.赛道 ASC, 成绩 ASC
```

### 4. 查询所有赛道各车手的排名
```
SELECT
  if(@上一个赛道 = c.赛道, @排名 := @排名 + 1, @排名 := 1) AS 排名,
  (@上一个赛道 := c.赛道) AS 赛道,
  c.车手,
  c.成绩
FROM
  (
    SELECT
      b.赛道, a.车手, min(a.成绩) AS 成绩
    FROM 成绩表 a, 赛局表 b
    WHERE a.赛局ID = b.赛局ID
          AND b.赛道 = 10
    GROUP BY a.车手, b.赛道
    ORDER BY b.赛道 ASC, 成绩 ASC
  ) c, (SELECT @排名 := 1, @上一个赛道 := 0) d
```

变量@上一个赛道的作用是：当变换赛道时排名要从1重新计算。

### 5. 只查询小明所有赛道的排名
```
SELECT
  if(@上一个赛道 = c.赛道, @排名 := @排名 + 1, @排名 := 1) AS 排名,
  (@上一个赛道 := c.赛道) AS 赛道,
  c.车手,
  c.成绩
FROM
  (
    SELECT
      b.赛道, a.车手, min(a.成绩) AS 成绩
    FROM 成绩表 a, 赛局表 b
    WHERE a.赛局ID = b.赛局ID
          AND b.赛道 = 10
    GROUP BY a.车手, b.赛道
    ORDER BY b.赛道 ASC, 成绩 ASC
  ) c, (SELECT @排名 := 1, @上一个赛道 := 0) d
WHERE c.车手 = '小明'
```

### 6. 查询小明所有赛道的最佳排名（目标达到）
```
SELECT
  if(@上一个赛道 = c.赛道, @排名 := @排名 + 1, @排名 := 1) AS 排名,
  (@上一个赛道 := c.赛道) AS 赛道,
  c.车手,
  c.成绩
FROM
  (
    SELECT
      b.赛道, a.车手, min(a.成绩) AS 成绩
    FROM 成绩表 a, 赛局表 b
    WHERE a.赛局ID = b.赛局ID
          AND b.赛道 = 10
    GROUP BY a.车手, b.赛道
    ORDER BY b.赛道 ASC, 成绩 ASC
  ) c, (SELECT @排名 := 1, @上一个赛道 := 0) d
WHERE c.车手 = '小明'
ORDER BY 排名 ASC
LIMIT 1
```

## 索引
赛局表的赛道字段需要加个索引，成绩表的成绩字段需要加个索引，车手字段不需要加索引。



