---
title: 学习Activiti 6.0.0 （五）
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---


## 需求

- 需求1：查询张三的请假申请
- 需求2：查询张三已完成的请假申请
- 需求3：查询张三未完成的请假申请
- 需求4：查询张三指定流水的请假申请
- 需求5：查询部门经理的请假待办任务
- 需求6：查询部门经理的请假未办任务
- 需求7：查询部门经理的流水号为20170411000001的请假待办任务
- 需求8：查询某次请假申请的流转和审批历史

<!-- more -->

## 查询申请历史
包含已完成的和未完成的，流程走到endEvent的叫做已完成的。

```
String processDefinitionKey = "LeaveProcess";
List<HistoricProcessInstance> list = historyService
        .createHistoricProcessInstanceQuery()
        .processDefinitionKey(processDefinitionKey)
        .variableValueEquals("user", "zhangsan").list();
```

> * 如果需要分页查询，把list()方法换成listPage(int firstResult, int maxResults)即可。
> * 当然也是支持排序的: orderByXxx()

只包含已完成的：

```
String processDefinitionKey = "LeaveProcess";
List<HistoricProcessInstance> list = historyService
        .createHistoricProcessInstanceQuery()
        .processDefinitionKey(processDefinitionKey).finished()
        .variableValueEquals("user", "zhangsan").list();
```

只包含未完成的：

```
String processDefinitionKey = "LeaveProcess";
List<HistoricProcessInstance> list = historyService
        .createHistoricProcessInstanceQuery()
        .processDefinitionKey(processDefinitionKey).unfinished()
        .variableValueEquals("user", "zhangsan").list();
```

查询指定流水（业务主键）的：

```
String processDefinitionKey = "LeaveProcess";
String businessKey = "20170411000001";
List<HistoricProcessInstance> list = historyService
        .createHistoricProcessInstanceQuery()
        .processDefinitionKey(processDefinitionKey)
        .processInstanceBusinessKey(businessKey)
        .variableValueEquals("user", "zhangsan").list();
```

## 查询任务
查询部门经理待办的请假申请：

```
String processDefinitionKey = "LeaveProcess";
List<HistoricTaskInstance> tasks = historyService
        .createHistoricTaskInstanceQuery()
        .processDefinitionKey(processDefinitionKey)
        .unfinished().taskAssignee("manager")
        .list();
```

查询部门经理已办的请假申请：

```
String processDefinitionKey = "LeaveProcess";
List<HistoricTaskInstance> tasks = historyService
        .createHistoricTaskInstanceQuery()
        .processDefinitionKey(processDefinitionKey)
        .finished().taskAssignee("manager")
        .list();
```

查询部门经理的流水号为20170411000001的请假待办任务

```
String processDefinitionKey = "LeaveProcess";
List<HistoricTaskInstance> tasks = historyService
        .createHistoricTaskInstanceQuery()
        .processDefinitionKey(processDefinitionKey)
        .processInstanceBusinessKey("20170411000001")
        .taskAssignee("manager").list();
```

## 查询流转历史
查询某次请假申请的流转和审批历史

```
String processDefinitionKey = "LeaveProcess";
List<HistoricTaskInstance> tasks = historyService
        .createHistoricTaskInstanceQuery()
        .processDefinitionKey(processDefinitionKey)
        .processInstanceBusinessKey("20170411000001")
        .list();
```

但是，结果集中没有审批意见和审批状态等信息。是不是有对应的api呢？还是说需要自己写sql？  

可不可以根据taskId查询到经理审批时传的replyMsg等流程参数呢？

好像也不行，ACT_HI_VARINST表中没有记录taskId，我也不知道为什么，有待研究...

发现可能是流程变量用的有问题，还有就是task的owner、assignee、groups以及Candidate等用的也很low，需要多多研究。











