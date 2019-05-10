---
title: 使用jdbc向MySQL中插入时间丢失毫秒
date: 2018-08-06 13:45:08
categories: 数据库
tags:
- MySQL
---

```
create table la_trans_monitor
(
	begin_time timestamp(3) default CURRENT_TIMESTAMP(3) not null
)
```

其他和本题无关字段已省略。

<!-- more -->

生成的Model：

```
package com.kangyonggan.app.dfjz.model.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Table(name = "la_trans_monitor")
@Data
public class LaTransMonitor implements Serializable {

    @Column(name = "begin_time")
    private Date beginTime;

    private static final long serialVersionUID = 1L;
}
```

## 问题分析
从debug日志中可以看出参数是有毫秒的，但是数据库中就是没有落入毫秒

```
[DEBUG] 2017-06-16 10:59:11.039 [com.kangyonggan.app.dfjz.mapper.LaTransMonitorMapper.insertSelective.main:145] - ==>  Preparing: INSERT INTO la_trans_monitor ( sys_date,sys_time,method_type,method_route,method_name,begin_time,end_time,used_time ) VALUES ( ?,?,?,?,?,?,?,? )
[DEBUG] 2017-06-16 10:59:11.065 [com.kangyonggan.app.dfjz.mapper.LaTransMonitorMapper.insertSelective.main:145] - ==> Parameters: 20170615(String), 112233(String), XX(String), XX(String), XXX(String), 2017-06-16 10:59:10.342(Timestamp), 2017-06-16 10:59:10.342(Timestamp), 12345(Long)
[DEBUG] 2017-06-16 10:59:11.188 [com.kangyonggan.app.dfjz.mapper.LaTransMonitorMapper.insertSelective.main:145] - <==    Updates: 1
```

解决这个问题的过程中有想到：
1. 会不会是MySQL版本问题？
2. 会不会是Mybatis版本问题？
3. 会不会是建表脚本问题？
4. 会不会是配置问题？

通过控制变量法，经过一一验证，都没发现问题。

我曾经手写过简版Mybatis，所以清楚以下几点：
1. 日志不等于它实际执行的SQL，所以看到日志中有毫秒是没多大意义的。
2. PreparedStatement有两个主要步骤，一个是准备带有占位符的SQL，另一个就是给占位符填数据。

所以我猜测PreparedStatement在填数据的时候，对java.util.date的处理过程中丢掉了毫秒，接下来就是debug跟踪源代码，最后发现PreparedStatement填数据的类是在mysql-connector-java这个jar包下的。
源代码如下：

```
private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
    if(x == null) {
        this.setNull(parameterIndex, 93);
    } else {
        this.checkClosed();
        if(!this.useLegacyDatetimeCode) {
            this.newSetTimestampInternal(parameterIndex, x, targetCalendar);
        } else {
            String timestampString = null;
            Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift()?this.connection.getUtcCalendar():this.getCalendarInstanceForSessionOrNew();
            synchronized(sessionCalendar) {
                x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
            }

            if(this.connection.getUseSSPSCompatibleTimezoneShift()) {
                this.doSSPSCompatibleTimezoneShift(parameterIndex, x, sessionCalendar);
            } else {
                synchronized(this) {
                    if(this.tsdf == null) {
                        this.tsdf = new SimpleDateFormat("\'\'yyyy-MM-dd HH:mm:ss", Locale.US);
                    }

                    timestampString = this.tsdf.format(x);
                    StringBuffer buf = new StringBuffer();
                    buf.append(timestampString);
                    buf.append('.');
                    buf.append(this.formatNanos(x.getNanos()));
                    buf.append('\'');
                    this.setInternal(parameterIndex, buf.toString());
                }
            }
        }

        this.parameterTypes[parameterIndex - 1 + this.getParameterIndexOffset()] = 93;
    }

}
```

从上面代码中可以看出，它在处理时间的时候，使用SimpleDateFormat进行格式化的，格式化中没保留毫秒，紧接着后面又去拼接纳秒formatNanos，但是继续跟踪此方法后发现它返回的是0，因此最后PreparedStatement填的值形如"yyyy-MM-dd HH:MM:ss.0"。

问题已经定位到，所以就想着能不能升级jar包版本解决问题呢？

去[中央仓库](https://mvnrepository.com/)搜一把, 发现我现在的版本5.1.9实在是太古老了，用的人也不多，于是换了新版的用的人较多的5.1.34, 果然成功解决问题。新版jar包核心代码如下：

```
private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
    synchronized(this.checkClosed().getConnectionMutex()) {
        if(x == null) {
            this.setNull(parameterIndex, 93);
        } else {
            this.checkClosed();
            if(!this.useLegacyDatetimeCode) {
                this.newSetTimestampInternal(parameterIndex, x, targetCalendar);
            } else {
                Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift()?this.connection.getUtcCalendar():this.getCalendarInstanceForSessionOrNew();
                synchronized(sessionCalendar) {
                    x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
                }

                if(this.connection.getUseSSPSCompatibleTimezoneShift()) {
                    this.doSSPSCompatibleTimezoneShift(parameterIndex, x, sessionCalendar);
                } else {
                    synchronized(this) {
                        if(this.tsdf == null) {
                            this.tsdf = new SimpleDateFormat("\'\'yyyy-MM-dd HH:mm:ss", Locale.US);
                        }

                        StringBuffer buf = new StringBuffer();
                        buf.append(this.tsdf.format(x));
                        if(this.serverSupportsFracSecs) {
                            int nanos = x.getNanos();
                            if(nanos != 0) {
                                buf.append('.');
                                buf.append(TimeUtil.formatNanos(nanos, this.serverSupportsFracSecs, true));
                            }
                        }

                        buf.append('\'');
                        this.setInternal(parameterIndex, buf.toString());
                    }
                }
            }

            this.parameterTypes[parameterIndex - 1 + this.getParameterIndexOffset()] = 93;
        }

    }
}
```









