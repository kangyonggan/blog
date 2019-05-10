---
title: 学习Activiti 6.0.0 （三）
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---


## 流程引擎的接口和服务

引擎API是与Activiti交互的最常见的方式。中心起始点是ProcessEngine，从ProcessEngine中，可以获得各种服务。ProcessEngine和服务service是线程安全的。因此，可以为整个服务器保留一个引用。

![](/upload/article/api.services.png)

<!-- more -->

下面是从ProcessEngine中获取各个service的代码:

```
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

RuntimeService runtimeService = processEngine.getRuntimeService();
RepositoryService repositoryService = processEngine.getRepositoryService();
TaskService taskService = processEngine.getTaskService();
ManagementService managementService = processEngine.getManagementService();
IdentityService identityService = processEngine.getIdentityService();
HistoryService historyService = processEngine.getHistoryService();
FormService formService = processEngine.getFormService();
DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
```

每个服务的作用暂不解释。我的一贯作风是先上手玩一下，再去思考它的作用。

使用工作流的步骤大致分为：

- 画一个流程图，并打包成zip文件。一般是使用eclipse插件或者idea插件。
- 部署流程图（zip文件）。包括：部署、查询、挂起、恢复和删除。
- 启动一个工作流实例。包括：启动、查询。
- 执行任务。包括：执行、查询。

## 画流程图
工欲善其事必先利其器，先在idea中安装画流程图的插件，如下图：

![](/upload/article/idea-acti.png)

安装后重启idea即可，如果电脑不能联网，也可以离线安装，插件下载地址：[http://plugins.jetbrains.com/plugin/download?rel=true&updateId=17789](http://plugins.jetbrains.com/plugin/download?rel=true&updateId=17789)

下面我画一个最经典的请假流程图，我先描述一下这个流程：
1. 员工填写请假信息。
2. 部门经理审批。
3. 如果部门经理通过，流程结束。
4. 如果部门经理不通过，回到步骤1。

下面是使用idea画的流程图:

![](/upload/article/acti-01.png)

![](/upload/article/acti-02.png)

![](/upload/article/acti-03.png)

![](/upload/article/acti-04.png)

![](/upload/article/acti-05.png)

从创建好的`leave.bpmn`复制出一个`leave.xml`, 其实就是改个后缀名。然后从`leave.xml`生成一个`leave.png`， 操作如下：

![](/upload/article/acti-06.png)
![](/upload/article/acti-07.png)

最终生成的流程图如下：

![](/upload/article/acti-08.png)

如果图片中文乱码，请修改idea的配置文件`idea64.exe.vmoptions`(win x64)，在最后添加`-Dfile.encoding=UTF-8`

## 部署流程图
把`leave.bpmn`和`leave.png`打包成`leave.zip`。然后使用下面的代码进行部署。

```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.util.zip.ZipInputStream;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo04 extends AbstractServiceTest {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 部署流程定义
     */
    @Test
    public void deploy() throws Exception {
        String zipPath = "D:\\code\\acti-01\\src\\main\\resources\\leave.zip";
        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .addZipInputStream(new ZipInputStream(new FileInputStream(zipPath))).deploy();

        System.out.println(deployment.getId());
    }

}
```

上面的方法是使用zip的方式部署的，当然也是可以使用bpmn+png的方式部署的。

部署成功后，从表数据来看，会在`act_re_deployment`表和`act_re_procdef`表中分别插入一条数据。

我个人的理解：部署一个流程定义，就像是定义一个class类，是一个抽象的概念。  
员工去申请请假的时候，就是从部署的这个抽象的流程定义中创建一个流程实例，就好比从一个抽象的class类中创建一个Object对象。

所以，一定要理解好流程定义和流程实例的概念，关于流程定义的增删改查，在ide中点一下服务的方法就能看的到。这些服务请查考图一。

如果`leave.xml`中没有乱码，但是数据库中有乱码，请检查jdbc-url连接是否带有utf8参数。

## 启动流程实例
代码中有注释，简单解释了一点点东东，实际操作时，还需要自己多点点服务的方法，多多实验。

```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo05 extends AbstractServiceTest {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 启动流程实例
     */
    @Test
    public void start() throws Exception {
        // 流程定义ID， 指明了流程定义ID，引擎才能知道你使用的是哪个模板
        String processDefinitionKey = "LeaveProcess";
        // 业务主键，比如请假申请, 就可以使用请假的流水号
        String businessKey = "20170411000001";
        // 实例参数
        Map<String, Object> variables = new HashMap<>(1);
        // 谁请假?
        variables.put("user", "zhangsan");

        ProcessInstance processInstance = processEngine.getRuntimeService().
                startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        System.out.println(processInstance.getId());
    }

}
```

启动流程实例后，从表数据来看，会在`act_ru_execution`表插入两条数据、在`act_ru_task`表中插入一条数据，task表中的这条数据表示接下来需要Assignee（zhangsan）来处理这个任务，即填写请假表。

## 执行任务
张三可以查询出指派给自己的任务，然后去执行。即从`act_ru_task`表中查出`Assignee`等于zhangsan的任务，也可以根据业务主键来查询，这些查询我们不用写，引擎已经封装成服务了，方法如下：

```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo06 extends AbstractServiceTest {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * zhangsan查询我的待办任务，并执行任务
     */
    @Test
    public void complete() throws Exception {
        // 查询任务
        String businessKey = "20170411000001";
        TaskQuery query = processEngine.getTaskService().createTaskQuery();
        query.processInstanceBusinessKey(businessKey);
        Task task = query.singleResult();

        // 执行任务
        Map<String, Object> variables = new HashMap<>(1);
        variables.put("reason", "请假去阿里面试");
        processEngine.getTaskService().complete(task.getId(), variables);
    }

}
```

员工填写请假申请后，从表数据来看，会把张三之前那个任务删除，即`act_ru_task`表中的一条记录。
他的Assignee=zhangsan, 那么这条数据也不会真的删除了，而是转义到了历史任务表`act_hi_taskinst`中。
同时，task表中会新增一个Assignee=manager的新任务，等待角色为manager的用户去处理。

## 审批
```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo07 extends AbstractServiceTest {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * manager查询我的待办任务，并执行任务
     */
    @Test
    public void complete() throws Exception {
        // 查询任务
        String assignee = "manager";
        TaskQuery query = processEngine.getTaskService().createTaskQuery();
        // 其实这里使用候选组（Candidate Groups）来查询比较好
        query.taskAssignee(assignee);
        List<Task> tasks = query.list();

        // 执行任务
        Map<String, Object> variables = new HashMap<>(2);
        variables.put("status", "complete");
        variables.put("replyMsg", "准了");
        processEngine.getTaskService().complete(tasks.get(0).getId(), variables);
    }

}
```

执行此任务后task表中assignee=manager的那条记录就被转义到历史任务表中了，并且也不再生成新的task了，因为流程已经结束。  

至此，一个完整的工作流就走完了。后面估计还要查询历史任务，查询流程进度等，下次再学习。








