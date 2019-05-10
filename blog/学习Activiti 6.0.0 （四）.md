---
title: 学习Activiti 6.0.0 （四）
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

## 挂起 & 激活
当一个流程定义被挂起后，如果再去尝试启动一个流程，那么就会抛一个异常`ActivitiException`, 测试代码如下：

<!-- more -->

```
package com.kangyonggan.acti;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo08 extends AbstractServiceTest {

    @Autowired
    private ProcessEngine processEngine;

    private static final String DEF_KEY = "LeaveProcess";

    /**
     * 挂起
     */
    @Test
    public void suspend() throws Exception {
        processEngine.getRepositoryService().suspendProcessDefinitionByKey(DEF_KEY);
    }

    /**
     * 激活
     */
    @Test
    public void active() throws Exception {
        processEngine.getRepositoryService().activateProcessDefinitionByKey(DEF_KEY);
    }

    /**
     * 启动一个工作流
     */
    @Test
    public void start() {
        try {
            // 业务主键，比如请假申请, 就可以使用请假的流水号
            String businessKey = "20170411000002";
            // 实例参数
            Map<String, Object> variables = new HashMap<>(1);
            // 谁请假?
            variables.put("user", "lisi");
            processEngine.getRuntimeService().startProcessInstanceByKey(DEF_KEY, businessKey, variables);
        } catch (ActivitiException e) {
            e.printStackTrace();
        }
    }
}
```

运行挂起方法后, 再运行启动工作流的方法抛的异常如下：

```
org.activiti.engine.ActivitiException: Cannot start process instance. Process definition 员工请假申请 (id = LeaveProcess:1:4) is suspended
```

> * 重复挂起也会抛异常：org.activiti.engine.ActivitiException: Cannot set suspension state 'suspended' for ProcessDefinitionEntity[LeaveProcess:1:4]': already in state 'suspended'.
> * 重复激活也会抛异常：org.activiti.engine.ActivitiException: Cannot set suspension state 'active' for ProcessDefinitionEntity[LeaveProcess:1:4]': already in state 'active'.

## 查询接口
需求：部门经理想查询张三的请假申请。

```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo09 extends AbstractServiceTest {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 需求：部门经理想查询张三的请假申请。
     */
    @Test
    public void query() throws Exception {
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("manager")
                .processVariableValueEquals("user", "zhangsan")
                .list();

        for (Task task : tasks) {
            System.out.println(task);
        }

    }

}
```

尽管activiti提供的api已经很强大很完善了，但是任然满足不了脑洞大开的需求。比如：部门经理想查询张三和李四的请假申请。

在activiti现有的api中我是没找到这种api，如下图：

![](/upload/article/ARTICLE20180412bcadfe1368eafc10393bfdb6438b779f98f883e2.png)

也许有其他曲线救国的方法，在此不多研究，我想说的是，如果api中没有提供方法，我们可以使用本地查询，代码如下：

```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo10 extends AbstractServiceTest {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 需求：部门经理想查询张三和李四的请假申请。
     */
    @Test
    public void query() throws Exception {
        String sql = "SELECT a.* FROM ACT_RU_TASK a "
                + "LEFT JOIN ACT_RU_VARIABLE b "
                + "ON a.PROC_INST_ID_ = b.PROC_INST_ID_ WHERE "
                + "b.NAME_ = 'user' AND b.TEXT_ IN ('zhangsan', 'lisi')";

        List<Task> tasks = processEngine.getTaskService()
                .createNativeTaskQuery()
                .sql(sql).list();

        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
```








