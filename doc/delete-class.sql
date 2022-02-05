delete from `classes` where `id` = 2;

delete from `t_c` where `c_id` = 2;

delete from `c_s` where `c_id` = 2;

delete from `experiment` where `c_id` = 2;

delete from `homework`
where `e_id` = (select `e_id` from `experiment` where `c_id` = 2);

delete from `answer`
where `h_id` = (select `h_id` from `homework` where `e_id` = (
    select `e_id` from `experiment` where `c_id` = 2
));