-- 学生表
drop table if exists `student`;

create table `student` (
   `id` char(8) primary key ,
   `name` varchar(10)
);

insert into student(id, name) VALUES ('20193306', '张三');
insert into student(id, name) values ('20203205', '李四');