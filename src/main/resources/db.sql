CREATE TABLE `t_user` (
`user_id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(64) DEFAULT NULL COMMENT '用户姓名',
`username` varchar(32) DEFAULT NULL COMMENT '登陆账号',
`password` varchar(512) DEFAULT NULL COMMENT '登陆密码',
`remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
`password_update_time` datetime DEFAULT NULL COMMENT '密码修改时间',
`contact_qq` varchar(255) DEFAULT NULL COMMENT '联系QQ',
`enabled` tinyint(1) DEFAULT '1' COMMENT '是否可用',
`is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除',
`created_by` int(11) DEFAULT NULL COMMENT '创建人',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` int(11) DEFAULT NULL COMMENT '修改人',
`update_time` datetime DEFAULT NULL COMMENT '修改时间',
PRIMARY KEY (`user_id`) USING BTREE,
UNIQUE KEY `t_user_username` (`username`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户表';