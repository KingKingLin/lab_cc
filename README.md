# 实验教学管理系统

## day1 完成后端项目的配置

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

   3. 添加 maven 命令
   
      > mybatis-generator:generate -e
   
6. 集成热部署（ 代码即删即用 ）

   1. 添加热部署的依赖

      ```xml
      ```

   2. 修改项目相关配置

## day2 完成登录页面的开发

 1. 初始化前端项目 vue create web

 2. 集成 ElementUI

    > npm i element-ui -S
    
 3. 添加路由拦截器，必须先登录才可以访问后续页面

    ```js
    // 路由登录拦截
    // to 跳到哪一个路由
    // from 旧路由
    // next 是一个方法
    router.beforeEach((to, from, next) => {
      if (to.matched.some(function (item) { // 如果返回 true，代表需要做路由的校验
        return item.meta.loginRequire;
      })) {
        if (JSON.stringify(store.state.m_user.user) === '{}') {
          next('/login'); // 跳到登录页
        } else {
          next();
        }
      } else {
        next()
      }
    });
    ```

 4. 集成 axios，同时配置访问的后端路径

    > npm install axios -s

    1. 添加一个 .env.dev 和 .env.prod 配置文件

        ```
        NODE_ENV=production
        VUE_APP_SERVER=http://127.0.0.1:8888
        VUE_APP_WS_SERVER=ws://127.0.0.1:8888
        ```

    2. 修改启动文件 pakage.json

        ```json
        "scripts": {
            "serve-dev": "vue-cli-service serve --mode dev --port 8080",
            "serve-prod": "vue-cli-service serve --mode prod",
            "build-dev": "vue-cli-service build --mode dev",
            "build-prod": "vue-cli-service build --mode prod",
            "lint": "vue-cli-service lint"
          },
        ```

 5. 编写登录页面 login.vue

    ```vue
    <template>
        <div class="login-container">
            <div class="login-item">
                <!-- 中南林的logo-->
                <img src="../assets/csuft.jpg" alt="图片失联中..."/>
                <!-- 教工号 或 学号 -->
                <div class="info">
                    <div class="pad">
                        <i class="el-icon-user"></i>
                    </div>
                    <el-input
                            :placeholder="type === 0 ? '学号' : '教工号'"
                            v-model="id"
                            clearable>
                    </el-input>
                </div>
                <!-- 密码 -->
                <div class="info">
                    <div class="pad">
                        <i class="el-icon-lock"></i>
                    </div>
                    <el-input placeholder="密码, 默认: 000000" v-model="password" show-password></el-input>
                </div>
                <!-- 登录按钮 -->
                <div class="info">
                    <el-dropdown @command="changeStatus" class="pad" size="small">
                        <span class="el-dropdown-link">
                            身份: {{status[selected]}}
                            <i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-for="(item, i) in status" :key="i" :disabled="i === selected" :command="i">{{item}}</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <el-button size="medium" @click="login">登录</el-button>
                </div>
            </div>
        </div>
    </template>
    
    <script>
        import axios from 'axios'
        import { mapMutations, mapState } from 'vuex'
    
        export default {
            name: 'login',
            data() {
                return {
                    id: '20010609',
                    password: '000000',
                    status: ['学生', '教师'],
                    selected: 0
                };
            },
            computed: {
                ...mapState('m_user', ['type'])
            },
            methods: {
                ...mapMutations('m_user', ['setToken', 'setUser', 'setType']),
                async login() {
                    if (this.id.length !== 8) {
                        this.$message({
                            message: '学号不符合规则',
                            type: 'warning'
                        })
                        return
                    }
                    if (this.password.length < 6 || this.password.length > 18) {
                        this.$message({
                            message: '密码在需6~18位字符之间',
                            type: 'warning'
                        })
                        return
                    }
                    if (this.type === 0) { // 学生登录
                        const {data: res} = await axios.post('/student/login?id='+this.id+'&password='+this.password)
                        if (!res.success) { // 登陆失败
                            this.$message({
                                message: res.message,
                                type: 'warning'
                            })
                            return
                        }
                        await this.storeUser(res)
                    } else { // 教师登录
                        const {data: res} = await axios.post('/teacher/login?id='+this.id+'&password='+this.password)
                        if (!res.success) { // 登陆失败
                            this.$message({
                                message: res.message,
                                type: 'warning'
                            })
                            return
                        }
                        await this.storeUser(res)
                    }
                },
                async storeUser(res) {
                    console.log("登录成功: ", res)
                    // 否则登录成功
                    this.setToken(res.content.token)
                    this.setUser({
                        id: res.content.id,
                        name: res.content.name
                    })
                    this.setType(res.content.type)
                    // this.$router.push("/") // 路由跳转到首页 "/"
                    await this.$router.replace("/") // 路由跳转到首页 "/"
                },
                changeStatus(i) {
                    console.log(i)
                    this.selected = i
                    this.setType(i)
                }
            }
        }
    </script>
    
    <style>
        .login-container {
            position: absolute;
            width: 100%;
            height: 100%;
            background-color: #efefef;
        }
        .login-item {
            /*让 div 居中*/
            position: absolute;
            top: 0;
            right: 0;
            left: 0;
            bottom: 0;
            margin: auto;
            /*设置子元素布局*/
            width: 450px;
            height: 300px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: space-around;
            background-color: #ffffff;
            box-shadow: #909090 2px 2px 10px; /* 边框阴影 */
        }
        .login-item img {
            width: 400px;
        }
        .info {
            display: flex;
            align-items: center;
        }
        .pad {
            padding-right: 10px;
        }
    </style>
    ```
    

## day3 教师修改密码、退出登录

1. 解决刷新页面，登录信息消失的问题，<font color='crimson'>使用 H5 提供的 sessionStorage</font>

   ```js
   SessionStorage = {
       get: function (key) {
           const v = sessionStorage.getItem(key);
           if (v && typeof(v) !== "undefined" && v !== "undefined") {
               return JSON.parse(v);
           }
       },
       set: function (key, data) {
           sessionStorage.setItem(key, JSON.stringify(data));
       },
       remove: function (key) {
           sessionStorage.removeItem(key);
       },
       clearAll: function () {
           sessionStorage.clear();
       }
   };
   ```

2. 目前教师页面

   ```vue
   <template>
       <el-container>
           <el-header>
               <img src="../../assets/csuft.jpg" alt="图片失联中..." class="logo"/>
               <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" router @select="handleSelect">
                   <el-menu-item index="/">首页</el-menu-item>
                   <el-menu-item index="/teacher/manager/teach">教学管理</el-menu-item>
                   <el-menu-item index="/teacher/manager/class">班级管理</el-menu-item>
               </el-menu>
               <div class="el-breadcrumb">
                   <span>欢迎 {{user.name}} 老师</span>
                   <el-divider direction="vertical"></el-divider>
                   <span @click="resetPassword">修改密码</span>
                   <el-divider direction="vertical"></el-divider>
                   <span @click="logout">退出登录</span>
               </div>
           </el-header>
           <my-resetpassword @sendMessage="sendMessage" ref="child"></my-resetpassword>
       </el-container>
   </template>
   
   <script>
       import { mapState, mapMutations } from 'vuex'
       import myResetpassword from '../../components/my-resetpassword.vue'
       import axios from "axios";
   
       export default {
           name: 'my-head',
           components: {
               myResetpassword
           },
           computed: {
               ...mapState('m_user', ['user']),
               ...mapState('m_teacher', ['activeIndex'])
           },
           methods: {
               ...mapMutations('m_teacher', ['setActiveIndex']),
               ...mapMutations('m_user', ['setUser']),
               handleSelect(key, keyPath) {
                   this.setActiveIndex(key)
               },
               resetPassword() {
                   this.$refs.child.dialogVisible = true
               },
               async logout() {
                   console.log("退出登录")
                   SessionStorage.clearAll()
                   await this.$router.replace("/login")
                   // window.location.reload()
               },
               async sendMessage(teacher) {
                   // console.log(teacher)
                   const {data: res} = await axios.post('/teacher/reset-password?id='+teacher.id+'&name='+teacher.name+'&password='+teacher.password)
                   if (res.success) {
                       // 密码修改成功
                       this.setUser(teacher)
                       this.$message.success(res.message)
                   } else {
                       this.$message.error(res.message)
                   }
               }
           }
       }
   </script>
   
   <style>
       .el-header {
           display: flex;
           align-items: center;
           justify-content: space-between;
       }
   
       .logo {
           height: 100%;
       }
   </style>
   ```

   ```vue
   <template>
       <div>
           <my-head></my-head>
           <el-main>Main</el-main>
       </div>
   </template>
   
   <script>
       import myHead from '../../components/teacher/my-head.vue'
   
       export default {
           name: 'my-teacher',
           components: {
               myHead
           },
           data() {
               return {};
           },
           setup() {
   
           }
       }
   </script>
   
   <style>
       .el-main {
           background-color: #E9EEF3;
           color: #333;
           text-align: center;
           line-height: 160px;
       }
   </style>
   ```

3. 抽取 修改密码 对话框，以便于后面 学生页面 的开发

   ```vue
   <template>
       <el-dialog
               title="修改密码"
               :visible.sync="dialogVisible"
               width="30%"
               :before-close="handleClose">
           <div>
               <div class="resetPassword-item">
                   <div class="resetPassword-info">
                       <span>{{type === 0 ? '学号' : '教工号'}}: </span>
                   </div>
                   <el-input
                           suffix-icon="el-icon-user"
                           v-model="mine.id"
                           :disabled="true">
                   </el-input>
               </div>
               <div class="resetPassword-item">
                   <div class="resetPassword-info">
                       <span>姓名: </span>
                   </div>
                   <el-input
                           suffix-icon="el-icon-postcard"
                           v-model="mine.name"
                           :disabled="true">
                   </el-input>
               </div>
               <div class="resetPassword-item">
                   <div class="resetPassword-info">
                       <span>当前密码: </span>
                   </div>
                   <el-input
                           placeholder="当前密码"
                           suffix-icon="el-icon-key"
                           v-model="mine.pre_password"
                           show-password>
                   </el-input>
               </div>
               <div class="resetPassword-item">
                   <div class="resetPassword-info">
                       <span>新密码: </span>
                   </div>
                   <el-input
                           placeholder="新密码"
                           suffix-icon="el-icon-unlock"
                           v-model="mine.new_password"
                           show-password>
                   </el-input>
               </div>
               <div class="resetPassword-item">
                   <div class="resetPassword-info">
                       <span>确认密码: </span>
                   </div>
                   <el-input
                           placeholder="确认密码"
                           suffix-icon="el-icon-lock"
                           v-model="mine.confirm_password"
                           show-password>
                   </el-input>
               </div>
           </div>
           <span slot="footer" class="dialog-footer">
                   <el-button size="small" @click="dialogVisible = false">取消</el-button>
                   <el-button type="primary" size="small" @click="confirm">确定</el-button>
               </span>
       </el-dialog>
   </template>
   
   <script>
       import { mapState } from 'vuex'
   
       export default {
           name: 'my-resetpassword',
           computed: {
               ...mapState('m_user', ['user', 'type'])
           },
           data() {
               return {
                   mine: {
                       id: '',
                       name: '',
                       pre_password: '',
                       new_password: '',
                       confirm_password: ''
                   },
                   dialogVisible: false
               }
           },
           mounted() {
               this.mine.id = this.user.id
               this.mine.name = this.user.name
           },
           methods: {
               handleClose(done) {
                   this.$confirm('确认关闭？')
                       .then(_ => {
                           // console.log("确认关闭")
                           // 关闭后将数据重置
                           this.mine.pre_password = ''
                           this.mine.new_password = ''
                           this.mine.confirm_password = ''
                           //
                           done();
                       })
                       .catch(_ => {});
               },
               confirm() {
                   // console.log("确认修改密码, 参数校验中...")
                   if (this.mine.id !== this.user.id || this.mine.name !== this.user.name || this.mine.pre_password !== this.user.password) {
                       if (this.type === 0) {
                           this.$message.error('学号或密码错误')
                       } else {
                           this.$message.error('教工号或密码错误')
                       }
                       return
                   }
                   if (this.mine.new_password.length === 0) {
                       this.$message({
                           message: '【新密码】不能为空',
                           type: 'warning'
                       })
                       return
                   }
                   if (!this.mine.new_password.match('^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$')) {
                       this.$message({
                           message: '【密码】至少包含 数字和英文，长度6-18',
                           type: 'warning'
                       })
                       return
                   }
                   if (this.mine.confirm_password.length === 0) {
                       this.$message({
                           message: '【确认密码】不能为空',
                           type: 'warning'
                       })
                       return
                   }
                   if (this.mine.new_password !== this.mine.confirm_password) {
                       this.$message({
                           message: '前后密码不一致',
                           type: 'warning'
                       })
                       return
                   }
                   // 确认无误后，修改密码
                   this.$emit('sendMessage', {
                       id: this.mine.id,
                       name: this.mine.name,
                       password: this.mine.new_password
                   })
                   // 确认后，重置数据
                   this.mine.pre_password = ''
                   this.mine.new_password = ''
                   this.mine.confirm_password = ''
                   // 对话框消失
                   this.dialogVisible = false
               }
           }
       }
   </script>
   
   <style>
       .resetPassword-item {
           display: flex;
           justify-content: space-around;
           padding-bottom: 10px;
       }
   
       .resetPassword-info {
           width: 100px;
           display: flex;
           flex-direction: column;
           justify-content: center;
       }
   </style>
   ```

4. 后端集成 Validation 做参数校验

   ```xml
   <!-- 集成 Validation 做参数校验 -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-validation</artifactId>
   </dependency>
   ```

   1. 在要校验的参数前加上 @Valid 注解

   2. 在要校验的属性上加上 @Pattern 等校验规则

   3. 编写校验失败的处理函数

      ```java
      package com.cc.lab_teach.controller;
      
      import com.cc.lab_teach.exception.BusinessException;
      import com.cc.lab_teach.resp.CommonResp;
      import org.slf4j.Logger;
      import org.slf4j.LoggerFactory;
      import org.springframework.validation.BindException;
      import org.springframework.web.bind.annotation.ControllerAdvice;
      import org.springframework.web.bind.annotation.ExceptionHandler;
      import org.springframework.web.bind.annotation.ResponseBody;
      
      @ControllerAdvice
      public class ExceptionController {
          private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);
      
          /**
           * 拦截业务异常
           */
          @ExceptionHandler(value = BusinessException.class)
          @ResponseBody
          public CommonResp businessExceptionHandler(BusinessException e) {
              CommonResp commonResp = new CommonResp();
              LOG.warn("业务异常：{}", e.getCode().getDesc());
              commonResp.setSuccess(false);
              commonResp.setMessage(e.getCode().getDesc());
              return commonResp;
          }
      
          /**
           * validation 校验，出现异常都会被它捕获
           * 校验异常统一处理
           */
          @ExceptionHandler(value = BindException.class)
          @ResponseBody
          public CommonResp<Object> validExceptionHandler(BindException e) {
              CommonResp<Object> commonResp = new CommonResp<>();
              LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
              commonResp.setSuccess(false);
              commonResp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()); // 拿到校验失败的信息
              return commonResp;
          }
      }
      ```


## day4 班级管理页面的开发

1. 完成后端 添加班级、查询所有班级 功能的开发

   1. 创建对应的 classes 表，以及 teacher 表 和 classes 表 关联的 t_c 表

      ```sql
      -- 班级表
      drop table if exists `classes`;
      
      create table `classes` (
         `id` int auto_increment primary key,
         `name` varchar(50) not null
      );
      
      -- insert into `classes`(name) VALUES ('2019级计科三班');
      -- insert into `classes`(name) VALUES ('2021级软工四班');
      
      -- 教师和班级的关联表, 存在外键索引 teacher 和 classes 表, 需要先删除
      drop table if exists `t_c`;
      
      create table `t_c` (
         `t_id` char(8),
         `c_id` int,
         primary key (`t_id`, `c_id`),
         constraint `with_teacher` foreign key (`t_id`) references `teacher`(`id`),
         constraint `with-classes` foreign key (`c_id`) references `classes`(`id`)
      );
      
      -- insert into `t_c`(t_id, c_id) VALUES ('20010609', 1);
      
      -- 课程表 lessons 暂不设计相关具体操作, 因为在本项目中, 只有 c++ 实验课
      ```

   2. <font color='crimson'>这里不创建 lesstion 表，因为在本项目中，只有 c++ 实验课</font>

   3. 使用 mybatis-generator 生成对应的代码

   4. 自定义一个 mapper，用来实现——<font color='crimson'>通过教工号，查询对应所有的 classes</font>

      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
      <mapper namespace="com.cc.lab_teach.mapper.MyMapper">
          <select id="getAllClasses" resultType="com.cc.lab_teach.domain.Classes">
              select id, name from classes
              where id in ( select c_id from t_c where t_id = ${id})
              order by name asc
          </select>
      </mapper>
      ```

   5. 要注意，在创建 classes 表时候，避免重复创建，需要进行判断

      ```java
      package com.cc.lab_teach.service;
      
      import com.cc.lab_teach.domain.Classes;
      import com.cc.lab_teach.domain.ClassesExample;
      import com.cc.lab_teach.exception.BusinessException;
      import com.cc.lab_teach.exception.BusinessExceptionCode;
      import com.cc.lab_teach.mapper.ClassesMapper;
      import com.cc.lab_teach.mapper.MyMapper;
      import com.cc.lab_teach.resp.ClassesResp;
      import com.cc.lab_teach.util.CopyUtil;
      import org.slf4j.Logger;
      import org.slf4j.LoggerFactory;
      import org.springframework.stereotype.Service;
      import org.springframework.util.ObjectUtils;
      
      import javax.annotation.Resource;
      import java.util.List;
      
      /**
       * 管理 classes 和 t_c 表的 service 层
       */
      @Service
      public class ClassesService {
          private static final Logger LOG = LoggerFactory.getLogger(ClassesService.class);
      
          @Resource
          private ClassesMapper classesMapper; // 操作 classes 表
      
          @Resource
          private TCService tcService; // 关联 tcService 对象 ( 职责隔离原则 )
      
          @Resource
          private TeacherService teacherService; // 关联 teacherService 对象 ( 职责隔离原则 )
      
          @Resource
          private MyMapper myMapper; // 自定义 Mapper 实例
      
          public Boolean addClasses(String id, String name) {
              // 1. 先根据 id 查询, 是否该教师存在, 如果不存在则抛出异常
              if (!teacherService.hasObject(id)) {
                  LOG.warn("教工号为: {} 的教师不存在", id);
                  throw new BusinessException(BusinessExceptionCode.DANGEROUS_OPERATION);
              }
              // 2. 再判断该班级是否已经被创建过, 不可以重复创建, 如果已经创建则, 取出其 c_id
              if (this.hasCreatedClasses(name)) {
                  LOG.info("班级名为: {} 已经被创建过", name);
              } else {
                  // 3. 以上条件都满足后, 在 classes 表中插入一条 值为 name 的数据
                  LOG.info("开始创建班级 {}", name);
                  Classes classes = new Classes();
                  classes.setName(name);
                  classesMapper.insert(classes);
              }
              // 4. 更新 t_C 教师-班级关系表
              tcService.addTC(id, this.getID(name));
              // 5. 返回 创建成功的信息
              return true;
          }
      
          private int getID(String name) {
              ClassesExample classesExample = new ClassesExample();
              ClassesExample.Criteria criteria = classesExample.createCriteria();
              criteria.andNameEqualTo(name); // name = name
      
              List<Classes> classes = classesMapper.selectByExample(classesExample);
              return classes.get(0).getId();
          }
      
          // 查询该班级是否被创建过
          private boolean hasCreatedClasses(String name) {
              LOG.info("开始查询班级名为: {} 是否被创建过...", name);
      
              // 查询条件
              ClassesExample classesExample = new ClassesExample();
              ClassesExample.Criteria criteria = classesExample.createCriteria();
              criteria.andNameEqualTo(name); // and name == name
      
              List<Classes> classes = classesMapper.selectByExample(classesExample);
              LOG.info("班级创建的结果: {}", !ObjectUtils.isEmpty(classes));
              return !ObjectUtils.isEmpty(classes);
          }
      
          public List<ClassesResp> getAllClasses(String id) {
              LOG.info("教工号为: {} 请求查询跟其有关的所有班级信息", id);
              List<Classes> list = myMapper.getAllClasses(id);
              List<ClassesResp> resps = CopyUtil.copyList(list, ClassesResp.class);
              return resps;
          }
      }
      ```

6. 前端开发 <font color='crimson'>添加班级</font> 的按钮，以及 <font color='crimson'>显示所有班级信息</font> 的滚动框

   ```vue
   <template>
       <div>
           <my-head></my-head>
           <div class="el-container my-class-container">
               <div class="my-class-aside">
                   <button class="my-class-aside-button" @click="addNewClass">添加班级</button>
                   <div v-if="classes.length !== 0" class="my-class-aside-container">
                       <div :class="['my-class-aside-item', i === activeIndex ? 'active' : '']" v-for="(item, i) in classes" :key="i" @click="getStudents(i, item.id)">
                           {{item.name}}
                       </div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                       <div class="my-class-aside-item">xxxx</div>
                   </div>
                   <el-empty :image-size="100" description="暂无任何班级信息" v-else></el-empty>
               </div>
               <div class="my-class-main">
                   Main
               </div>
           </div>
       </div>
   </template>
   
   <script>
       import myHead from '../../../components/teacher/my-head.vue'
       import axios from "axios";
       import { mapState } from 'vuex';
   
       export default {
           name: 'my-class',
           components: {
               myHead
           },
           computed: {
               ...mapState('m_user', ['user'])
           },
           data() {
               return {
                   classes: [],
                   activeIndex: -1
               };
           },
           mounted() {
               // 初始时便加载班级信息
               this.getClasses()
           },
           methods: {
               // 请求 classes[] 数据
               async getClasses() {
                   console.log("正在请求班级信息")
                   const {data: res} = await axios.get('/teacher/getAllClasses', {
                       params: {
                           id: this.user.id
                       }
                   })
                   if (res.success) {
                       this.classes = res.content
                   } else {
                       this.classes = []
                       console.error("请求班级信息失败")
                   }
               },
               addNewClass() {
                   console.log("请求新建班级")
                   this.$prompt('班级名', '新建班级', {
                       confirmButtonText: '确定',
                       cancelButtonText: '取消'
                   }).then(async ({value}) => {
                       const loading = this.$loading({
                           lock: true,
                           text: '请稍等片刻, 正在全力为您创建班级信息中...',
                           spinner: 'el-icon-loading',
                           background: 'rgba(0, 0, 0, 0.7)'
                       });
                       const {data: res} = await axios.get('/teacher/addClasses', {
                           params: {
                               id: this.user.id,
                               name: value
                           }
                       })
                       if (res.success) { // 创建成功
                           this.$message({
                               type: 'success',
                               message: res.message
                           })
                           loading.text = "正在为您全力加载数据..."
                           // 创建成功后, 重新请求列表 classes 列表数据
                           await this.getClasses()
                       }
                       else {
                           this.$message({
                               type: 'error',
                               message: res.message
                           })
                       }
                       loading.close(); // 不管创建与否都要关闭 loading 效果
                   }).catch(() => {
                       console.log("用户取消新建班级")
                   })
               },
               getStudents(i, id) {
                   console.log("需要有点击记录的组件的索引: ", i)
                   this.activeIndex = i
                   console.log("查询班级 "+ id +" 的学生列表: ")
               }
           }
       }
   </script>
   
   <style>
       .my-class-container {
           margin: 20px 20px;
       }
   
       .my-class-aside {
           width: 222px;
           height: 540px;
           display: flex;
           flex-direction: column;
           align-items: center;
           /*border: 2px solid #efefef;*/
           margin-right: 20px;
       }
   
       .my-class-aside-button {
           width: 100%;
           height: 50px;
           color: rgb(96, 98, 102);
           background-color: white;
           border-radius: 18px;
           border: 1px solid #efefef;
       }
   
       .my-class-aside-button:hover {
           color: rgb(64, 158, 255);
           background-color: rgb(236, 245, 255);
       }
   
       .my-class-aside-container {
           border: 1px solid #efefef;
           margin-top: 10px;
           overflow-y: scroll;
           height: 100%;
       }
   
       /* 修改滚动条容器的宽度 */
       .my-class-aside-container::-webkit-scrollbar {
           width: 1px;
           background-color: #F5F5F5;
       }
   
       /* 修改滚动条的样式 */
       .my-class-aside-container::-webkit-scrollbar-thumb {
           border-radius: 20px;
           background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0.44, rgb(122,153,217)), color-stop(0.72, rgb(73,125,189)), color-stop(0.86, rgb(28,58,148)));
       }
   
       .my-class-aside-item {
           width: 100%;
           height: 40px;
           font-size: 13px;
           line-height: 40px;
           text-align: center;
           color: rgb(96, 98, 102);
           background-color: #ffffff;
           /*background-color: lightblue;*/
           border-bottom: 1px solid #efefef;
           white-space: nowrap; /* 不换行显示 */
           text-overflow: ellipsis; /* 超过一行则显示... */
       }
   
       .my-class-aside-item:hover {
           color: rgb(64, 158, 255);
           background-color: rgb(236, 245, 255);
       }
   
       .my-class-main {
           width: 100%;
       }
   
       .active {
           color: rgb(64, 158, 255);
           background-color: rgb(236, 245, 255);
       }
   </style>
   ```

## day5 教学管理页面的开发

1. 修改 day4 班级管理页面开发时，一次性读取所有班级信息，导致页面冗长

   1. 后端集成 PageHelper 插件，做分页管理

      ```xml
      <!-- pagehelper 插件 -->
      <dependency>
          <groupId>com.github.pagehelper</groupId>
          <artifactId>pagehelper-spring-boot-starter</artifactId>
          <version>1.2.13</version>
      </dependency>
      ```

      <font color='crimson'>注意：PageHelper 如果报错（ 循环依赖 ）的异常，可能是因为 Spring 和 PageHelper 版本不兼容的关系，如这里 pagehelper( 1.2.13 ) 就与 Spring ( 2.6.4 ) 不兼容</font>

   2. 在后端查询时，使用 PageHelper 做分页管理

      - pagehelper 会自动找寻第一个 select 语句，在其后面追加 limit ? ,? 语句；同时，会执行一次 select count(0) ... 进行条目统计

      - 下面是代码示例：

        ```java
        public PageResp<StudentPageResp> getStudentByPage(PageReq page, int c_id) {
            PageHelper.startPage(page.getPage(), page.getSize());
            LOG.info("分页查询班级数据：{}", page);
        
            List<Student> lists = myMapper.selectByCid(c_id);
            LOG.info("分页查询到的数据: {}", lists);
        
            PageInfo<Student> pageInfo = new PageInfo<>(lists);
            LOG.info("总行数: {}", pageInfo.getTotal()); // 总行数，一般返回给前端
            LOG.info("总页数: {}", pageInfo.getPages()); // 总页数
        
            List<StudentPageResp> students = CopyUtil.copyList(lists, StudentPageResp.class);
            PageResp<StudentPageResp> results = new PageResp<>();
            results.setTotal(pageInfo.getTotal());
            results.setSize(pageInfo.getPages());
            results.setList(students);
            return results;
        }
        ```

   3. 班级管理页面做分页管理，使用 element-ui 的分页管理工具

      ```vue
      <template>
          <el-pagination
                         background
                         layout="prev, pager, next"
                         :total="page.total"
                         :page-size="page.size"
                         :pager-count="page.count"
                         @current-change="currentChange">
          </el-pagination>
      </template>
      <script>
      	export default {
              name: 'xxx',
              methods: {
                  async currentChange(e) {
                      // console.log("页码: " + e)
                      // console.log("currentChange")
                      this.page.pageNum = e
                      await this.getStudents(this.activeIndex, this.classes[this.activeIndex].id)
                  }
              }
          }
      </script>
      ```

   4. 教学管理页面，添加一个下拉列表，完成可以选择上课班级

      - <font color='crimson'>这里有一个问题就是：是不是该将所有的班级一次性都查询出来？</font>

        下拉列表，选择上课的班级，即需要一开始就去请求班级信息
        用下拉列表的问题： 如果班级信息很多，会导致选项条超出页面范围？
        如何解决？
        ==用表格？分页请求班级数据？==
        <font color='crimson'>**或者下拉列表中添加一条记录：下一页，上一页，或者搜索功能**</font>

      - 因此这里的代码如下所示：

        ```vue
        <template>
            <div>
                <my-head></my-head>
                <div class="my-teach-container">
                    <!-- 下拉列表，选择上课的班级，即需要一开始就去请求班级信息 -->
                    <!--
                        用下拉列表的问题： 如果班级信息很多，会导致选项条超出页面范围？
                        如何解决？
                        用表格？分页请求班级数据？
                        或者下拉列表中添加一条记录：下一页，上一页，或者搜索功能
                    -->
                    <el-dropdown @command="handleCommand">
                        <span class="el-dropdown-link">
                        {{activeIndex === -1 ? '请选择上课的班级' : classes[activeIndex].name}}<i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="pre" :disabled="page.pageNum === 1">上一页</el-dropdown-item>
                            <el-dropdown-item v-for="(item, i) in classes" :key="i" :command="item.id+','+i" :disabled="i === activeIndex">{{item.name}}</el-dropdown-item>
                            <el-dropdown-item command="next" :disabled="page.pageNum === page.pages">下一页</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </div>
        </template>
        
        <script>
            import myHead from '../../../components/teacher/my-head.vue'
            import { mapState } from 'vuex'
            import axios from "axios";
        
            export default {
                name: 'my-teach',
                components: {
                    myHead
                },
                computed: {
                    ...mapState('m_user', ['user'])
                },
                data() {
                    return {
                        classes: [],
                        activeIndex: -1,
                        page: {
                            pageNum: 1,
                            size: 10,
                            pages: Number, // 总页数, 从后端获取
                            total: Number, // 总条目数，从后端获取
                        }
                    }
                },
                mounted() {
                    // 初始时便加载班级信息
                    this.getPartClasses()
                },
                methods: {
                    // 请求 classes[] 数据
                    async getPartClasses() {
                        console.log("正在请求班级信息")
                        const {data: res} = await axios.get('/teacher/part-classes', {
                            params: {
                                id: this.user.id,
                                page: this.page.pageNum,
                                size: this.page.size
                            }
                        })
                        if (res.success) {
                            this.classes = res.content.list
                            this.page.total = res.content.total
                            this.page.pages = res.content.size
                        } else {
                            this.classes = []
                            console.error("请求班级信息失败")
                        }
                    },
                    handleCommand(e) {
                        console.log(e)
                        if (e === 'pre') { // 点击上一页
                            this.page.pageNum -= 1
                            this.getPartClasses()
                            return
                        }
                        if (e === 'next') { // 下一页
                            this.page.pageNum += 1
                            this.getPartClasses()
                            return
                        }
                        // 请求改班级的实验信息，如 实验一，实验二，实验三
                        const split = String(e).split(',');
                        const id = split[0]
                        this.activeIndex = Number(split[1])
                    }
                }
            }
        </script>
        
        <style>
            .my-teach-container {
                margin-top: 20px;
            }
        </style>
        ```

      - 后端添加对应接口，根据 <font color='crimson'>教工号</font> 以及 <font color='crimson'>分页条件</font> 查询部分班级信息，最后选择对应的班级信息，则可以开始上课（ 发布实验题目<<font color='crimson'>以 word 的形式 或者 粘贴题目，也就是富文本形式</font>>，和 批阅题目等功能 ）

# 不想写了，摆烂了

​	在本次项目中，新的收获：

1. 后端

   - <font color='crimson'>学会了处理 doc、docx、图片等文件的上传</font>

     - image：将文件存储到本地，最后以访问路径的形式存入数据库。前端通过网络获取本地数据。
     - video：同理
     - doc、docx：则是将其装换为对应的 html 字符串，存入数据库中，最后直接返回给前端
     - txt：处理起来最容易，通过流读取文件中的内容，存入数据库即可

     ```java
     package com.cc.lab_teach.controller;
     
     import com.cc.lab_teach.exception.BusinessException;
     import com.cc.lab_teach.exception.BusinessExceptionCode;
     import com.cc.lab_teach.resp.CommonResp;
     import com.cc.lab_teach.resp.HomeworkResp;
     import com.cc.lab_teach.service.HomeworkService;
     import com.cc.lab_teach.util.OfficeConvertUtil;
     import org.slf4j.Logger;
     import org.slf4j.LoggerFactory;
     import org.springframework.util.ResourceUtils;
     import org.springframework.web.bind.annotation.GetMapping;
     import org.springframework.web.bind.annotation.PostMapping;
     import org.springframework.web.bind.annotation.RequestMapping;
     import org.springframework.web.bind.annotation.RestController;
     import org.springframework.web.multipart.MultipartFile;
     
     import javax.annotation.Resource;
     import java.io.*;
     import java.util.HashMap;
     import java.util.List;
     import java.util.UUID;
     
     /**
      * 处理发布题目的类
      */
     @RestController
     @RequestMapping("/teacher")
     public class HomeworkController {
         private final static Logger LOG = LoggerFactory.getLogger(HomeworkController.class);
     
         private final static HashMap<String, String> hashMap = new HashMap<>();
     
         @Resource
         private HomeworkService homeworkService;
     
         static {
             try {
                 // 获得 classpath 的绝对路径
                 hashMap.put("image", ResourceUtils.getURL("classpath:").getPath() + "static/image");
                 hashMap.put("video", ResourceUtils.getURL("classpath:").getPath() + "static/video");
             } catch (FileNotFoundException e) {
                 LOG.error(String.valueOf(e));
             }
         }
     
         /**
          * homework 可能是图片，可能是文本(word，由前端解析过)，还可能是视频
          * 1. 如果是 图片，则保存到本地，记录其存储路径，最后将路径存入数据库
          * 2. 如果是 文本，则直接存入数据库
          * 3. 如果是 视频，则和图片一样的处理
          * @return
          */
         @PostMapping("/release")
         public CommonResp<Boolean> release(long h_id, MultipartFile file) throws Exception {
             if (file.isEmpty()) throw new BusinessException(BusinessExceptionCode.FILE_IS_EMPTY);
     
             LOG.info("开始上传答案");
             LOG.info("h_id: " + h_id);
             LOG.info("file: " + file.getOriginalFilename());
             LOG.info("contentType" + file.getContentType());
             if (file.getOriginalFilename().endsWith(".doc")) { // 处理 doc 文件
                 LOG.info("开始处理 .doc 文件");
                 String htmlContent = OfficeConvertUtil.docToHtml(file);
                 LOG.info("处理 .doc 文件 完成");
                 homeworkService.uploadHomework(h_id, "html", htmlContent);
             } else if (file.getOriginalFilename().endsWith(".docx")) { // 处理 docx 文件
                 LOG.info("开始处理 .doc 文件");
                 String htmlContent = OfficeConvertUtil.docxToHtml(file);
                 homeworkService.uploadHomework(h_id, "html", htmlContent);
                 LOG.info("处理 .doc 文件 完成");
             } else if (file.getOriginalFilename().endsWith(".txt")) { // 处理 txt 文件
                 LOG.info("开始处理 .txt 文件");
                 String txtContent = handleTxt(file);
                 homeworkService.uploadHomework(h_id, "txt", txtContent);
                 LOG.info("处理 .txt 文件 完成");
             } else {
                 LOG.info("开始处理 媒体 文件");
                 String[] handle = handleImageAndVideo(file);
                 homeworkService.uploadHomework(h_id, handle[0], handle[0] + "/" + handle[1]);
                 LOG.info("处理 媒体 文件完成");
             }
     
             CommonResp<Boolean> resp = new CommonResp<>();
             resp.setMessage("上传成功");
             resp.setContent(true);
             return resp;
         }
     
         @GetMapping("/get-homeworks")
         public CommonResp<List<HomeworkResp>> getHomework(long e_id) {
             LOG.info("开始查询 {} 的所有题目", e_id);
             List<HomeworkResp> result = homeworkService.getHomework(e_id);
             CommonResp<List<HomeworkResp>> resp = new CommonResp<>();
             resp.setContent(result);
             resp.setMessage("查询成功");
             return resp;
         }
     
         /**
          * homework 可能是图片，可能是文本(word，由前端解析过)，还可能是视频
          * 1. 如果是 图片，则保存到本地，记录其存储路径，最后将路径存入数据库
          * 2. 如果是 文本，则直接存入数据库
          * 3. 如果是 视频，则和图片一样的处理
          * @return
          */
         @PostMapping("/upload")
         public CommonResp<Boolean> upload(long e_id, MultipartFile file) throws Exception {
             if (file.isEmpty()) throw new BusinessException(BusinessExceptionCode.FILE_IS_EMPTY);
     
             LOG.info("开始发布题目");
             LOG.info("e_id: " + e_id);
             LOG.info("file: " + file.getOriginalFilename());
             LOG.info("contentType" + file.getContentType());
             if (file.getOriginalFilename().endsWith(".doc")) { // 处理 doc 文件
                 LOG.info("开始处理 .doc 文件");
                 String htmlContent = OfficeConvertUtil.docToHtml(file);
                 LOG.info("处理 .doc 文件 完成");
                 homeworkService.insertHomework(e_id, "html", htmlContent);
             } else if (file.getOriginalFilename().endsWith(".docx")) { // 处理 docx 文件
                 LOG.info("开始处理 .doc 文件");
                 String htmlContent = OfficeConvertUtil.docxToHtml(file);
                 homeworkService.insertHomework(e_id, "html", htmlContent);
                 LOG.info("处理 .doc 文件 完成");
             } else if (file.getOriginalFilename().endsWith(".txt")) { // 处理 txt 文件
                 LOG.info("开始处理 .txt 文件");
                 String txtContent = handleTxt(file);
                 homeworkService.insertHomework(e_id, "txt", txtContent);
                 LOG.info("处理 .txt 文件 完成");
             } else {
                 LOG.info("开始处理 媒体 文件");
                 String[] handle = handleImageAndVideo(file);
                 homeworkService.insertHomework(e_id, handle[0], handle[0] + "/" + handle[1]);
                 LOG.info("处理 媒体 文件完成");
             }
             CommonResp<Boolean> resp = new CommonResp<>();
             resp.setMessage("上传成功");
             resp.setContent(true);
             return resp;
         }
     
         private String handleTxt(MultipartFile file) throws IOException {
             Reader reader = new InputStreamReader(file.getInputStream());
             StringBuilder stringBuilder = new StringBuilder();
             char[] buffer = new char[1024];
             int i = 0;
             while ((i = reader.read(buffer)) != -1) {
                 stringBuilder.append(new String(buffer, 0, i));
             }
             return stringBuilder.toString();
         }
     
         private String[] handleImageAndVideo(MultipartFile file) throws IOException {
             if (file.isEmpty()) throw new BusinessException(BusinessExceptionCode.FILE_IS_EMPTY);
             // 获取文件名
             String realPath = null;
             String type = null;
             String contentType = file.getContentType();
             if (contentType.contains("image")) { // 如果是图片
                 realPath = hashMap.get("image");
                 type = "image";
             } else if (contentType.contains("video")) { // 如果是视频
                 realPath = hashMap.get("video");
                 type = "video";
             }
             File newFile = new File(realPath);
             // 如果文件夹不存在、则新建
             if (!newFile.exists()) newFile.mkdirs();
             // 上传，不使用用户上传的名字，因为容易重复
             String name = UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
             file.transferTo(new File(newFile, name));
     
             return new String[]{type, name};
         }
     }
     ```

     ```java
     package com.cc.lab_teach.util;
     
     import org.apache.poi.hwpf.HWPFDocument;
     import org.apache.poi.hwpf.converter.WordToHtmlConverter;
     import org.apache.poi.xwpf.converter.core.BasicURIResolver;
     import org.apache.poi.xwpf.converter.core.FileImageExtractor;
     import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
     import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
     import org.apache.poi.xwpf.usermodel.XWPFDocument;
     import org.slf4j.Logger;
     import org.slf4j.LoggerFactory;
     import org.springframework.util.ResourceUtils;
     import org.springframework.web.multipart.MultipartFile;
     
     import javax.xml.parsers.DocumentBuilderFactory;
     import javax.xml.transform.OutputKeys;
     import javax.xml.transform.Transformer;
     import javax.xml.transform.TransformerFactory;
     import javax.xml.transform.dom.DOMSource;
     import javax.xml.transform.stream.StreamResult;
     import java.io.File;
     import java.io.FileOutputStream;
     import java.io.StringWriter;
     import java.io.StringWriter;
     import java.util.UUID;
     
     public class OfficeConvertUtil {
         private static final Logger LOG = LoggerFactory.getLogger(OfficeConvertUtil.class);
     
         // doc转换为html
         public static String docToHtml(MultipartFile file) throws Exception {
             LOG.info("正在处理：将 .doc 文件装换成 .html 格式");
             // 图片的存储路径
             String image_path = ResourceUtils.getURL("classpath:").getPath() + "static/image/doc/";
     
             HWPFDocument wordDocument = new HWPFDocument(file.getInputStream());
             org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
             WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
             //保存图片，并返回图片的相对路径
             wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
                 String suffix = name.substring(name.lastIndexOf("."));
                 String filename = UUID.randomUUID().toString().replaceAll("-", "");
                 try (FileOutputStream out = new FileOutputStream(image_path + filename + suffix)) {
                     out.write(content);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 LOG.info("处理图片文件，储存路径为: image/doc/{}", filename + suffix);
                 return "image/doc/" + filename + suffix;
             });
             // 处理 word 文件
             wordToHtmlConverter.processDocument(wordDocument);
             org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
             DOMSource domSource = new DOMSource(htmlDocument);
             // 以 字符串流 的形式，暂存与 内存 中
             StreamResult streamResult = new StreamResult(new StringWriter());
             // 转换成 html 格式
             TransformerFactory tf = TransformerFactory.newInstance();
             Transformer serializer = tf.newTransformer();
             serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
             serializer.setOutputProperty(OutputKeys.INDENT, "yes");
             serializer.setOutputProperty(OutputKeys.METHOD, "html");
             serializer.transform(domSource, streamResult);
     
             return streamResult.getWriter().toString();
         }
     
         // docx转换为html
         public static String docxToHtml(MultipartFile file) throws Exception {
             LOG.info("正在处理：将 .docx 文件装换成 .html 格式");
             // 图片的存储路径
             String uuid = UUID.randomUUID().toString().replaceAll("-", "");
             String image_path = ResourceUtils.getURL("classpath:").getPath() + "static/image/docx/" + uuid;
     
             StringWriter stringWriter = null;
             String htmlContent = null;
             try {
                 XWPFDocument document = new XWPFDocument(file.getInputStream());
                 XHTMLOptions options = XHTMLOptions.create();
                 // 存放图片的文件夹
                 options.setExtractor(new FileImageExtractor(new File(image_path)));
                 // html中图片的路径
                 options.URIResolver(new BasicURIResolver("image/docx/" + uuid));
                 stringWriter = new StringWriter();
                 XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
                 xhtmlConverter.convert(document, stringWriter, options);
     
                 htmlContent = stringWriter.toString();
             } finally {
                 if (stringWriter != null) {
                     stringWriter.close();
                 }
             }
             return htmlContent;
         }
     }
     ```

   - <font color='crimson'>通过 WebSocket 得到，目前在线人数</font>

     ```java
     package com.cc.lab_teach.config;
     
     import org.springframework.context.annotation.Bean;
     import org.springframework.context.annotation.Configuration;
     import org.springframework.web.socket.server.standard.ServerEndpointExporter;
     
     @Configuration
     public class WebSocketConfig {
         /**
          * 声明我这个SpringBoot应用要暴露、使用websocket了
          */
         @Bean
         public ServerEndpointExporter serverEndpointExporter() {
             return new ServerEndpointExporter();
         }
     }
     ```

     ```java
     package com.cc.lab_teach.websocket;
     
     import org.slf4j.Logger;
     import org.slf4j.LoggerFactory;
     import org.springframework.stereotype.Component;
     
     import javax.annotation.Resource;
     import javax.websocket.*;
     import javax.websocket.server.PathParam;
     import javax.websocket.server.ServerEndpoint;
     import java.io.IOException;
     import java.util.HashMap;
     
     @Component
     @ServerEndpoint("/ws/{token}")
     public class WebSocketServer {
         private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);
     
         /**
          * 每个客户端一个token
          */
         private String token = "";
     
         private static HashMap<String, Session> map = new HashMap<>(); // 放置所有的连接
     
         /**
          * 连接成功
          *
          * 推送信息需要session，所有连接后需要保存session
          */
         @OnOpen
         public void onOpen(Session session, @PathParam("token") String token) {
             map.put(token, session);
             this.token = token;
             LOG.info("有新连接：token：{}，session id：{}，当前连接数：{}", token, session.getId(), map.size());
         }
     
         /**
          * 连接关闭
          */
         @OnClose
         public void onClose(Session session) {
             map.remove(this.token);
             LOG.info("连接关闭，token：{}，session id：{}！当前连接数：{}", this.token, session.getId(), map.size());
         }
     
         /**
          * 收到消息，这个是指我们服务端收到消息
          */
         @OnMessage
         public void onMessage(String message, Session session) {
             LOG.info("收到消息：{}，内容：{}", token, message);
         }
     
         /**
          * 连接错误
          */
         @OnError
         public void onError(Session session, Throwable error) {
             LOG.error("发生错误", error);
         }
     
         /**
          * 群发消息，给客户端通知其实就是调用这个方法，遍历每一个session
          */
         public void sendInfo(String message) {
             for (String token : map.keySet()) {
                 Session session = map.get(token);
                 try {
                     session.getBasicRemote().sendText(message); // getBasicRemote()得到远程的IP地址，然后发送一个文本
                 } catch (IOException e) {
                     LOG.error("推送消息失败：{}，内容：{}", token, message);
                 }
                 LOG.info("推送消息：{}，内容：{}", token, message);
             }
         }
     
         /**
          * 得到在线人数
          */
         public int getOnlineNumbers() {
             return map.size();
         }
     }
     ```

   - 使用定时任务，每天凌晨4点更新昨天的访问人数

     ```java
     package com.cc.lab_teach.job;
     
     import com.cc.lab_teach.mapper.MyMapper;
     import org.slf4j.Logger;
     import org.slf4j.LoggerFactory;
     import org.springframework.data.redis.core.RedisTemplate;
     import org.springframework.scheduling.annotation.Scheduled;
     import org.springframework.stereotype.Component;
     import org.springframework.transaction.annotation.Transactional;
     
     import javax.annotation.Resource;
     
     @Component
     public class SnapshotJob {
         private static final Logger LOG = LoggerFactory.getLogger(SnapshotJob.class);
     
         @Resource
         private RedisTemplate redisTemplate;
     
         @Resource
         private MyMapper myMapper;
     
         /**
          * corn 每天的 04:00:00 更新快照表
          */
         @Scheduled(cron = "0 0 4 * * ?")
         @Transactional
         public void cron() {
             LOG.info("04:00:00 更新快照表开始（昨天的访问量）");
             long start = System.currentTimeMillis();
     
             Long views = (Long) redisTemplate.opsForValue().get("views");
             if (views == null) views = 0l;
             // 更新数据
             myMapper.insertSnapshot(views);
             // 重置 views
             redisTemplate.opsForValue().set("views", 0l);
             LOG.info("更新快照表数据结束，耗时：{} 毫秒", System.currentTimeMillis() - start);
         }
     
        /* @Scheduled(cron = "0 0 0 * * ?")
         public void test() {
             LOG.info("00:00:00 重置访问量");
         }*/
     }
     ```

   - 为了防止，黑客入侵，添加 token 检测机制，集成 redis

     - 使用拦截器，对请求进行拦截，并检测 token

     ```java
     package com.cc.lab_teach.interceptor;
     
     import org.slf4j.Logger;
     import org.slf4j.LoggerFactory;
     import org.springframework.data.redis.core.RedisTemplate;
     import org.springframework.http.HttpStatus;
     import org.springframework.stereotype.Component;
     import org.springframework.web.servlet.HandlerInterceptor;
     import org.springframework.web.servlet.ModelAndView;
     
     import javax.annotation.Resource;
     import javax.servlet.http.HttpServletRequest;
     import javax.servlet.http.HttpServletResponse;
     
     /**
      * 拦截器：Spring框架持有的，常用于登录校验，权限校验，请求日志打印
      */
     @Component
     public class LogInterceptor implements HandlerInterceptor {
     
         private static final Logger LOG = LoggerFactory.getLogger(LogInterceptor.class);
     
         @Resource
         private RedisTemplate redisTemplate;
     
         @Override
         public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
             // 打印请求信息
             LOG.info("----------------LogInterceptor 开始----------------");
     
             long startTime = System.currentTimeMillis();
             request.setAttribute("requestStartTime", startTime);
     
             // OPTIONS请求不做校验,
             // 前后端分离的架构, 前端会发一个OPTIONS请求先做预检, 对预检请求不做校验
             if(request.getMethod().toUpperCase().equals("OPTIONS")){
                 return true;
             }
     
             String path = request.getRequestURL().toString();
             LOG.info("接口登录拦截：，path：{}", path);
     
             //获取header的token参数
             String token = request.getHeader("token");
             LOG.info("登录校验开始，token：{}", token);
             if (token == null || token.isEmpty()) {
                 LOG.info( "token为空，请求被拦截" );
                 response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
                 return false;
             }
             // redis 对 token 这个参数的要求是有要求的
             Object object = redisTemplate.opsForValue().get(token);
             if (object == null) {
                 LOG.warn( "token无效，请求被拦截" );
                 response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
                 return false;
             } else {
                 LOG.info("已登录：{}", object);
                 return true;
             }
         }
     
         @Override
         public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
             long startTime = (Long) request.getAttribute("requestStartTime");
             LOG.info("----------LogInterceptor 结束 耗时: {} ms----------", System.currentTimeMillis() - startTime);
         }
     }
     ```

   - 前后端交互时，日期数据的传输，可能出现问题

     - 使用  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 解决，但是<font color='crimson'>需要注意的是：前端必须以这种格式传递过来，才能正确接受。</font>

     ```xml
     <!-- 处理前后端日期格式问题 -->
     <dependency>
         <groupId>com.fasterxml.jackson.core</groupId>
         <artifactId>jackson-core</artifactId>
     </dependency>
     
     <dependency>
         <groupId>com.fasterxml.jackson.core</groupId>
         <artifactId>jackson-annotations</artifactId>
     </dependency>
     
     <dependency>
         <groupId>com.fasterxml.jackson.core</groupId>
         <artifactId>jackson-databind</artifactId>
     </dependency>
     ```

     ```java
     @GetMapping("/add-experiment/{c_id}")
     public CommonResp<Boolean> addExperiment(String title,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
                                              Date deadline,
                                              @PathVariable String c_id)
     ```

   - <font color='crimson'>学会使用 SpringMVC 提供的 MultipartFile处理二进制文件</font>

     ```
     file.transferTo(new File(newFile, name)); // 转存文件
     ```

   - <font color='crimson'>使用 poi 和 jsoup 处理 word 文件，转存为 html</font>

     ```xml
     <!-- 处理 word -->
     <dependency>
         <groupId>org.apache.poi</groupId>
         <artifactId>poi</artifactId>
         <version>3.14</version>
     </dependency>
     
     <dependency>
         <groupId>org.apache.poi</groupId>
         <artifactId>poi-scratchpad</artifactId>
         <version>3.14</version>
     </dependency>
     
     <dependency>
         <groupId>org.apache.poi</groupId>
         <artifactId>poi-ooxml</artifactId>
         <version>3.14</version>
     </dependency>
     
     <dependency>
         <groupId>fr.opensagres.xdocreport</groupId>
         <artifactId>xdocreport</artifactId>
         <version>1.0.6</version>
     </dependency>
     
     <dependency>
         <groupId>org.apache.poi</groupId>
         <artifactId>poi-ooxml-schemas</artifactId>
         <version>3.14</version>
     </dependency>
     
     <dependency>
         <groupId>org.apache.poi</groupId>
         <artifactId>ooxml-schemas</artifactId>
         <version>1.3</version>
     </dependency>
     
     <dependency>
         <groupId>org.jsoup</groupId>
         <artifactId>jsoup</artifactId>
         <version>1.11.3</version>
     </dependency>
     ```

2. 前端

   - 总之学会了很多，如：自己设计滚动条，各种 UI 组件的使用等等

   - 学会使用自定义协议，打开外联的PC应用（一定要运行进注册表文件）

     ```reg
     Windows Registry Editor Version 5.00
     
     [HKEY_CLASSES_ROOT\cfree]
     "URL Protocol"=""
     @=""
     
     [HKEY_CLASSES_ROOT\CFree.c\shell\open\command]
     @="D:\\lab_cc\\C-Free 5\\CppIDE.exe \"%1\""
     ```

     

