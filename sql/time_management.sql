drop table if exists task_list;
create table task_list
(
    task_id             bigint auto_increment comment '任务ID'
        primary key,
    task_name           varchar(100)            not null comment '任务名称',
    task_status         varchar(30) default '0' null comment '任务状态 (0todo 1doing 2done)',
    task_start_time     datetime(3)                null comment '任务开始日期时间',
    task_completed_time datetime(3)                null comment '任务完成日期时间',
    task_repeat_id      bigint                  null comment '重复ID',
    task_priority       varchar(30) default '0' null comment '任务优先级 (0无 1低 2中 3高)',
    create_by           varchar(64) default ''  null comment '创建者',
    create_time         datetime                null comment '创建时间',
    update_by           varchar(64) default ''  null comment '更新者',
    update_time         datetime                null comment '更新时间',
    remark              varchar(500)            null comment '备注',
    user_id             bigint                  not null comment '用户ID'
)
    comment '任务表';

drop table if exists task_repeat;
create table task_repeat
(
    task_repeat_id  bigint auto_increment comment '重复ID'
        primary key,
    template_id     bigint                 null comment '重复模版ID',
    repeat_value    varchar(30)            null comment '重复值',
    end_repeat      varchar(30)            null comment '结束重复',
    end_repeat_date datetime(3)               null comment '结束重复日期',
    num             bigint           null comment '自定义重复数值',
    frequency_value varchar(30)            null comment '自定义重复频率',
    selected_item   varchar(100)           null comment '自定义重复周期',
    create_by       varchar(64) default '' null comment '创建者',
    create_time     datetime               null comment '创建时间',
    update_by       varchar(64) default '' null comment '更新者',
    update_time     datetime               null comment '更新时间',
    remark          varchar(500)           null comment '备注'
)
    comment '任务重复表';

drop table if exists tag;
create table tag
(
    tag_id      bigint auto_increment comment '标签ID'
        primary key,
    tag_name    varchar(100)           null comment '标签名称',
    create_by   varchar(64) default '' null comment '创建者',
    create_time datetime               null comment '创建时间',
    update_by   varchar(64) default '' null comment '更新者',
    update_time datetime               null comment '更新时间',
    remark      varchar(500)           null comment '备注'
)
    comment '标签表';


drop table if exists task_tags;
create table task_tags
(
    task_id     bigint                 null comment '用户ID',
    tag_id      bigint                 null comment '标签ID',
    create_by   varchar(64) default '' null comment '创建者',
    create_time datetime               null comment '创建时间',
    update_by   varchar(64) default '' null comment '更新者',
    update_time datetime               null comment '更新时间',
    remark      varchar(500)           null comment '备注'
)
    comment '任务标签关联表';

drop table if exists task_template;
create table task_template
(
    template_id             bigint auto_increment comment '重复模版ID'
        primary key,
    task_name           varchar(100)            not null comment '任务名称',
    task_status         varchar(30) default '0' null comment '任务状态 (0todo 1doing 2done)',
    task_start_time     datetime(3)                null comment '任务开始日期时间',
    task_completed_time datetime(3)                null comment '任务完成日期时间',
    task_priority       varchar(30) default '0' null comment '任务优先级 (0无 1低 2中 3高)',
    create_by           varchar(64) default ''  null comment '创建者',
    create_time         datetime                null comment '创建时间',
    update_by           varchar(64) default ''  null comment '更新者',
    update_time         datetime                null comment '更新时间',
    remark              varchar(500)            null comment '备注',
    user_id             bigint                  not null comment '用户ID'
)
    comment '重复任务模版表';

drop table if exists template_tags;
create table template_tags
(
    template_id     bigint             null comment '重复模版ID',
    tag_id      bigint                 null comment '标签ID',
    create_by   varchar(64) default '' null comment '创建者',
    create_time datetime               null comment '创建时间',
    update_by   varchar(64) default '' null comment '更新者',
    update_time datetime               null comment '更新时间',
    remark      varchar(500)           null comment '备注'
)
    comment '重复任务模版标签关联表';


