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

      
