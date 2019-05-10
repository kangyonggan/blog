---
title: 如何把N个苹果按重量相对公平的分配给M个人
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---


## 问题描述
有N个苹果，想要平均分给M个人，而且每个人得到的苹果重量要尽量差不多。

例如：10个苹果的重量为：`{4, 9, 2, 8, 7, 5, 6, 1, 3, 7}`。

## 方案一：S形分法
这是大家最容易想到的又是相对简单的，而且还有很高的公平性。XX银行的分案就是采用此法，所以可行性很高。

思路：  
1. 先把数组降序（降序比升序更公平）排序，排序结果为：`{9, 8, 7, 7, 6, 5, 4, 3, 2, 1}`。
2. 然后把所有苹果从大到小依次分给：P1、P2、P3、P3、P2、P1、P1、P2、P3、P3。也就是S形分法。

<!-- more -->

分完之后各自手里的苹果： 
- P1: `{9, 5, 4}` = 18
- P2: `{8, 6, 3}` = 17
- P3: `{7, 7, 2, 1}` = 17

由此可见此算法基本算是公平的，但就怕极端数据比如`{6, 2, 2, 2, 2, 2, 2}`。  
如果使用S形分法：  
- P1: `{6, 2, 2}` = 10
- P2: `{2, 2}` = 4
- P3: `{2, 2}` = 4

显而易见最好的分法应该是：  
- P1: `{6}` = 6
- P2: `{2, 2, 2}` = 6
- P3: `{2, 2, 2}` = 6


## 方案二：穷举法
在说此方案之前，我要说一下“公平性”。  
观察方案一的结果`18、17、17`，我们感觉比较公平，因为每个数都接近平均数17.333。  
在数学中，有一个说法叫做离散程度，就是各个数字距离平均数的远近程度。而标准差的大小就可以体现离散程度。  
```
s = sqrt(((X1 - X0)^2 + (X2 - X0)^2 + ... (Xn - X0)^2) / n)
```

其中s为标准差，sqrt是求平方根，X0为平均数，Xi为各个元素，n为元素的个数。

有了衡量公平与否的方案之后，我们可以穷举出所有的分配方案，然后计算各自的标准差，取最小的一组即可。

但是，此算法的时间复杂度高的令人发指：`A(n, m)` 即：`n! / (n-m)! = n * (n-1) * (n-2) * ... * (n-m)`

由于时间复杂度太高，所以此法并不实用，除非在此基础上进行优化，比如：
1. 当标准差小于某个指标时，退出循环，但是此法还是不可控，搞不好就穷举了。
2. 可以在方案一的基础上进行`有限穷举`。

## 方案三：折中的方案
上面我说了可以在方案一的基础上进行有限穷举，这听着矛盾的有限穷举到底是什么骚操作呢？

在限穷举顾名思义就是有限个穷举，避免真正的穷举，所以不可能得到所有的标准差，  
也就没法获得一个最小的标准差（因为最小的那种情况可能就是你没有穷举到的）。  
但我们可以在有限的穷举中获得相对最小的标准差。  

再看一组数据：`{9, 8, 7, 5, 5, 5, 3, 2}`  
用S形分法的结果：  
- P1: `{9, 5, 3}` = 17
- P2: `{8, 5, 2}` = 15
- P3: `{7, 5}` = 12

调和方案：  
1. 用最大的减去最小的，17 - 12 = 5
2. 从最大的数组中找到恰好比5小的数3（也可能没有）。
3. 如果没找到比5小的，终止，在这有限的情况下找出最小标准差即可。
4. 如果找到5小的，即3，把3给最小的那组，然后计算一下此时的标准差，再回到第一步。

> 这只是我个人意淫的方案，如果时间充足，我相信还可以研究出其他更优方案，尔等若有时间，可以考虑考虑，回头告诉我。

## 附1：计算标准差
```
/**
 * 计算标准差
 *
 * @param arr
 * @return
 */
private static double calcStandardDeviation(int[] arr) {
	// 计算平均值
	double average = calcAverage(arr);
	double sum = 0;
	for (int i = 0; i < arr.length; i++) {
		sum += Math.pow((arr[i] - average), 2);
	}

	return Math.sqrt(sum / arr.length);
}
```

## 附2：A(n, m)的排列组合

```
private static double recursion(List<Double> apples, List<List<Double>> baskets, int m, double minStandardDeviation) {
    if (apples.isEmpty()) {
        // 每个篮子中苹果总重量
        List<Double> basketTotalApples = calcSumApples(baskets);
        // 标准差
        Double standardDeviation = calcStandardDeviation(basketTotalApples);
        if (standardDeviation < minStandardDeviation) {
            minStandardDeviation = standardDeviation;
            System.out.println("发现一个更小的标准差:" + minStandardDeviation);
        }

        return minStandardDeviation;
    }

    List<List<Double>> copyBaskets = new ArrayList<>(baskets);
    List<Double> copyApples = new ArrayList<>(apples);
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < apples.size(); j++) {
            Double a = apples.get(j);
            copyBaskets.get(i).add(a);
            copyApples.remove(j);
            copyApples.add(j, a);
            copyBaskets.get(i).remove(copyBaskets.get(i).size() - 1);
        }
    }

    return minStandardDeviation;
}
```

