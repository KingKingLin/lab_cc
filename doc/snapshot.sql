-- 插入并更新数据
insert into `snapshot`(`views`, `date`)
select 0, curdate()
from dual where not exists(
        select 1 from `snapshot` where `date` = curdate()
    )

update `snapshot`
set `views` = 21
where `date` = curdate()

-- 查询30天的快照信息
select
    `views`, `date`
from
    `snapshot`
where
    `date` between date_sub(curdate(), interval 30 day ) and date_sub(curdate(), interval 1 day)
order by `date` asc;
