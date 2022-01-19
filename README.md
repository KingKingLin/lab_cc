# 实验教学管理系统

## day1

1. 初始化后端项目

2. 用 git 管理，推送到 gittee

3. 创建 lab_teach 数据库（ MySQL）

   - 由于 root 用户权限过大
   - 创建一个用户 lab_teach 来访问数据库
   - 创建 student 表，并添加测试案例

   ```sql
   -- 学生表
   drop table if exists `student`;
   
   create table `student` (
      `id` char(8) primary key , -- 固定为 8 个字符，例如 "20192705"
      `name` varchar(10),
      `password` varchar(20) not null default '000000'-- 密码，不能为空，默认 "000000"
   );
   
   insert into student(id, name) VALUES ('20193306', '张三');
   insert into student(id, name) values ('20203205', '李四');
   ```

4. 使用 MyBatis 持久层框架

   1. 导入依赖

   ```xml
   <!-- 集成mybatis-->
   <dependency>
       <groupId>org.mybatis.spring.boot</groupId>
       <artifactId>mybatis-spring-boot-starter</artifactId>
       <version>2.2.0</version>
   </dependency>
   <!-- 集成mysql连接-->
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>8.0.27</version>
   </dependency>
   ```

   2. 在 application.properties 配置相关数据源

   ```
   # 配置数据源
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.datasource.url=jdbc:mysql://localhost:3306/lab_teach
   spring.datasource.username=lab_teach
   spring.datasource.password=lab_teach
   ```

   3. 新建 mapper 接口映射目录，在 启动类上添加 @MapperScan 注解
   
      > @MapperScan("com.cc.lab_teach")
   
   4. 新建 mapper.xml 配置目录，并在 application.properties 配置相关路径

   ```
   # 配置 MyBatis mapper.xml 的映射路径
   mybatis.mapper-locations=classpath:/mapper/**/*.xml
   ```
   
   5. 编写相关测试代码，进行测试

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.cc.lab_teach.mapper.Test">
       <select id="selectAllStudent" resultType="com.cc.lab_teach.domain.Student">
           select * from student
       </select>
   </mapper>
   ```

   ```java
   package com.cc.lab_teach.mapper;
   
   import java.util.List;
   
   public interface Test {
       List<Student> selectAllStudent();
   }
   ```
   
5. 使用 MyBatis Generator 代码生成器（ 自动生成对应的 mapper 和 接口类 ）

   1. 添加 mybatis-generator 依赖

      ```xml
      <!--mybatis generator插件-->
      <plugin>
          <groupId>org.mybatis.generator</groupId>
          <artifactId>mybatis-generator-maven-plugin</artifactId>
          <version>1.4.0</version>
          <configuration>
              <configurationFile>src/main/resources/generator/generator-config.xml</configurationFile>
              <overwrite>true</overwrite>
              <verbose>true</verbose>
          </configuration>
          <!--它要在数据库创建表，所以它需要一个数据库连接-->
          <dependencies>
              <dependency>
                  <groupId>mysql</groupId>
                  <artifactId>mysql-connector-java</artifactId>
                  <version>8.0.27</version>
              </dependency>
          </dependencies>
      </plugin>
      ```

   2. 编写 generator-config.xml 配置文件

      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <!DOCTYPE generatorConfiguration
              PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
              "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
      <generatorConfiguration>
          <context id="Mysql" targetRuntime="MyBatis3"  defaultModelType="flat">
      
              <!-- 自动检查关键字，为关键字增加反引号 -->
              <property name="autoDelimitKeywords" value="true"/>
              <property name="beginningDelimiter" value="`"/>
              <property name="endingDelimiter" value="`"/>
      
              <!-- 覆盖生成XML文件-->
              <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin" />
              <!-- 生成的实体类添加toString()方法 -->
              <plugin type="org.mybatis.generator.plugins.ToStringPlugin" />
      
              <!-- 不生成注释 -->
              <commentGenerator>
                  <!--            <property name="suppressDate" value="true" />-->
                  <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
                  <property name="suppressAllComments" value="true" />
              </commentGenerator>
      
              <!-- 数据库链接配置 -->
              <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                              connectionURL="jdbc:mysql://localhost:3306/lab_teach"
                              userId="lab_teach" password="lab_teach">
              </jdbcConnection>
      
              <!-- domain类的位置 -->
              <javaModelGenerator targetPackage="com.cc.lab_teach.domain"
                                  targetProject="src\main\java"/>
      
              <!-- mapper xml 的位置 -->
              <sqlMapGenerator targetPackage="mapper"
                               targetProject="src\main\resources"/>
      
              <!-- mapper 类的位置 -->
              <javaClientGenerator type="XMLMAPPER"
                                   targetPackage="com.cc.lab_teach.mapper"
                                   targetProject="src\main\java"/>
      
              <!-- 需要生成对应pojo、mapper等实体类对应的表 -->
              <table tableName="student"/>
          </context>
      </generatorConfiguration>
      ```

      
