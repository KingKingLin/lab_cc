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

-- 班级 与 学生之间的关联表
drop table if exists `c_s`;

create table `c_s` (
    `c_id` int,
    `s_id` char(8),
    primary key (`c_id`, `s_id`),
    foreign key (`c_id`) references `classes`(`id`),
    foreign key (`s_id`) references `student`(`id`)
);

-- 实验表：一个班级的所有学生的实验题目都是一样的
drop table if exists `experiment`;

create table `experiment` (
    `e_id` bigint primary key auto_increment,
    `c_id` int,
    `title` varchar(50), -- 实验的名字，如: ['实验一', '实验二'] 等
    `deadline` timestamp, -- 截止时间，timestamp 能够存储 日期+时分秒（date 只有日期，time只有时间）
    foreign key (`c_id`) references `classes`(`id`)
);

-- 题目表，一个实验可能有多个题目
drop table if exists `homework`;

create table `homework` (
    `h_id` bigint primary key auto_increment,
    `e_id` bigint, -- 对应的是哪一个实验
    `content` mediumtext, -- 富文本，存放题目的内容
    `standard` mediumtext, -- 标准答案
    foreign key (`e_id`) references `experiment`(`e_id`)
);

-- 答案表
drop table if exists `answer`;

create table `answer` (
    `a_id` bigint primary key auto_increment, -- 答案 id
    `s_id` char(8), -- 学生 id, 一个学生对应一个答案
    `h_id` bigint, -- 外键，homework 表，表示该答案是对应哪一个题目的
    `correct` mediumtext, -- 教师的评阅
    `result` mediumtext, -- 学生的答案，只要这个值不为空，就意味着学生提交了答案
    `redo` boolean default false, -- 是否允许重做
    foreign key (`s_id`) references `student`(`id`),
    foreign key (`h_id`) references `homework`(`h_id`)
);