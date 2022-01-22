-- 教师表
drop table if exists `teacher`;

create table `teacher` (
   `id` char(8) primary key , -- 固定为 8 个字符，例如 "20192705"
   `name` varchar(20),
   `password` varchar(20) not null default '000000'-- 密码，不能为空，默认 "000000"
);

insert into teacher(id, name) VALUES ('20010609', '陈贵云');

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

-- 学生表
drop table if exists `student`;

create table `student` (
   `id` char(8) primary key , -- 固定为 8 个字符，例如 "20192705"
   `name` varchar(20),
   `password` varchar(20) not null default '000000'-- 密码，不能为空，默认 "000000"
);

-- 测试案例
-- insert into `student`(id, name) values ('20193306', '张三');