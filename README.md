# 实验教学管理系统

## day1

1. 初始化后端项目

2. 用 git 管理，推送到 gittee

3. 创建 lab_teach 数据库（ MySQL）

   - 由于 root 用户权限过大
   - 创建一个用户 lab_teach 来访问数据库
   - 创建 student 表，并添加测试案例

   ```sql
   drop table if exists `student`;
   
   create table `student` (
       `id` char(8) primary key ,
       `name` varchar(10)
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
   
   import com.cc.lab_teach.domain.Student;
   
   import java.util.List;
   
   public interface Test {
       List<Student> selectAllStudent();
   }
   ```
   
   
