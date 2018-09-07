## 1. 执行分库创建

1. 导入`1_databases_create.sql`文件

## 2. 修改mycat配置文件

修改安装包下面`conf`目录下面这3个配置文件。

### 2.1 server.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mycat:server SYSTEM "server.dtd">
<mycat:server xmlns:mycat="http://io.mycat/">
    <system>
		<property name="defaultSqlParser">druidparser</property>
    </system>
    <user name="root">
        <property name="password">test</property>
        <property name="schemas">dbtest</property>
    </user>

    <user name="user">
        <property name="password">user</property>
        <property name="schemas">dbtest</property>
        <property name="readOnly">true</property>
    </user>
</mycat:server>
```

### 2.2 schema.xml

```xml
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">

    <schema name="dbtest" checkSQLschema="false" sqlMaxLimit="100">
        <table name="tb1" primaryKey="id" type="global" dataNode="dn1,dn2,dn3,dn4,dn5" />
        <table name="tb2" dataNode="dn1,dn2,dn3" rule="mod-long" />
        <table name="tb3" dataNode="dn1,dn2,dn3" rule="auto-sharding-long" />
        <table name="tb4" dataNode="dn1,dn2,dn3,dn4,dn5" rule="mod-long-age" />
    </schema>
    
    <dataNode name="dn1" dataHost="localhost1" database="dbtest1" />
    <dataNode name="dn2" dataHost="localhost1" database="dbtest2" />
    <dataNode name="dn3" dataHost="localhost1" database="dbtest3" />
    <dataNode name="dn4" dataHost="localhost1" database="dbtest4" />
    <dataNode name="dn5" dataHost="localhost1" database="dbtest5" />
    
    <dataHost name="localhost1" maxCon="1000" minCon="10" balance="0"
        writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
        <heartbeat>select user()</heartbeat>
        <writeHost host="host1" url="localhost:3306" user="dbuser" password="dbpass"/>
    </dataHost>
</mycat:schema>
```

### 2.3 rule.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mycat:rule SYSTEM "rule.dtd">
<mycat:rule xmlns:mycat="http://io.mycat/">
	<tableRule name="mod-long">
		<rule>
			<columns>id</columns>
			<algorithm>mod-long</algorithm>
		</rule>
	</tableRule>
	<tableRule name="mod-long-age">
		<rule>
			<columns>age</columns>
			<algorithm>mod-long-age</algorithm>
		</rule>
	</tableRule>
	<function name="mod-long" class="io.mycat.route.function.PartitionByMod">
		<property name="count">3</property>
	</function>
	<function name="mod-long-age" class="io.mycat.route.function.PartitionByMod">
		<property name="count">5</property>
	</function>
</mycat:rule>
```

## 3. 启动mycat

$ bin/mycat start

## 4. mysql client connect to mycat

连接用户名为`root`，密码为`test`，参考上面的`server.xml`配置。

$ mysql --protocol=TCP -u root -h localhost -P 8066 -ptest

## 5. 创建全局表tb1和分表文件

1. 导入`2_table_tb1.sql`文件
2. 导入`3_table_tb2.sql`文件
3. 导入`4_table_tb3.sql`文件
4. 导入`5_table_tb4.sql`文件
