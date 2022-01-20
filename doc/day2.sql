-- 教师表
drop table if exists `teacher`;

create table `teacher` (
   `id` char(8) primary key , -- 固定为 8 个字符，例如 "20192705"
   `name` varchar(10),
   `password` varchar(20) not null default '000000'-- 密码，不能为空，默认 "000000"
);

insert into teacher(id, name) VALUES ('20010609', '陈贵云');