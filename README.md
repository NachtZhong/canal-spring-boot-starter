# canal-spring-boot-starter [![LICENSE](https://img.shields.io/badge/Fuck-996-red)](https://github.com/TequilaCN/canal-spring-boot-starter/blob/main/LICENSE)

### 配置项说明:

| 配置项            | 说明                 | 是否能为空 |
| ----------------- | -------------------- | ---------- |
| canal.instances.{destination}.server      | canal server端ip地址 | YES       |
| canal.instances.{destination}.port        | canal server端端口   | YES       |
| canal.instances.{destination}.userName | canal server端账号   | YES       |
| canal.instances.{destination}.password     | canal server端密码        | YES       |
| canal.instances.{destination}.batchSize     | 单批次拉取最大消息数        | YES       |
| canal.instances.{destination}.sleepAfterFailAcquire     | 拉取不到消息时休眠间隔(ms)        | YES       |
| canal.instances.{destination}.filter     | 订阅表过滤        | YES       |

### QuickStart
1. 引入maven依赖
```xml
       <!-- https://mvnrepository.com/artifact/com.alibaba.otter/canal.client -->
        <dependency>
            <groupId>com.alibaba.otter</groupId>
            <artifactId>canal.client</artifactId>
            <version>1.1.4</version>
        </dependency>
        <dependency>
            <groupId>com.nacht</groupId>
            <artifactId>canal-spring-boot-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

2. 配置canal实例地址
```yaml
canal:
  instances:
    example:
      server: 127.0.0.1
      port: 11111
      batchSize: 100
```

3. 主启动类添加@EnableCanal注解, 开启canal

4. 实现CanalDmlRawEventListener接口, 分别处理增删改各个场景的rowData
```java
@CanalEventListener
public class MyCanalDmlEventHandler implements CanalDmlRawEventListener {
    @Override
    public void onInsert(List<CanalEntry.Column> afterColumns) {
        System.out.println(afterColumns.toString());
    }

    @Override
    public void onUpdate(List<CanalEntry.Column> beforeColumns, List<CanalEntry.Column> afterColumns) {
        System.out.println(beforeColumns.toString());
        System.out.println(afterColumns.toString());
    }

    @Override
    public void onDelete(List<CanalEntry.Column> beforeColumns) {
        System.out.println(beforeColumns.toString());
    }
}
```