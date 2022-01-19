-- 学生表
drop table if exists `student`;

create table `student` (
   `id` char(8) primary key , -- 固定为 8 个字符，例如 "20192705"
   `name` varchar(10),
   `password` varchar(20) not null default '000000'-- 密码，不能为空，默认 "000000"
);

insert into student(id, name) VALUES ('20193306', '张三');
insert into student(id, name) values ('20203205', '李四');