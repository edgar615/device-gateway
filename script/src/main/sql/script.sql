drop table if exists product_script;

/*==============================================================*/
/* Table: product_script                                         */
/*==============================================================*/
create table product_script
(
   product_script_id     varchar(32) not null comment 'product_script_Id',
   product_type         char(7) comment '产品类型',
   message_type         char(16) comment '消息类型',
   command              varchar(64) comment '消息命令',
   script_content       text comment '脚本内容',
   state                int comment '状态',
   add_on               int comment '添加时间',
   last_modify_on       int comment '上次修改时间',
   created_on           datetime default CURRENT_TIMESTAMP comment '创建时间',
   updated_on           datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (product_script_id)
);

alter table product_script comment '消息类型: 1-down 2-up
状态：1-test 2-pre 3-release';
