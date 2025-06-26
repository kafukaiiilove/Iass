-- 创建客户镜像包记录表
CREATE TABLE IF NOT EXISTS `customer_package_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `record_date` datetime DEFAULT NULL COMMENT '时间年月日时分',
  `package_url` varchar(500) DEFAULT NULL COMMENT '镜像包地址',
  `fix_content` text COMMENT '修复内容描述',
  `image_id` varchar(100) DEFAULT NULL COMMENT '镜像ID',
  `developer` varchar(100) DEFAULT NULL COMMENT '开发工程师',
  `commit_id` varchar(100) DEFAULT NULL COMMENT 'Commit id',
  `tester` varchar(100) DEFAULT NULL COMMENT '测试工程师',
  `test_result` varchar(50) DEFAULT '待测试' COMMENT '测试结果',
  `remarks` text COMMENT '备注',
  `android_version` varchar(20) DEFAULT NULL COMMENT 'Android版本 (例如: a10, a13)',
  `category` varchar(50) DEFAULT NULL COMMENT '分类 (例如: vmos, qt)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_android_version` (`android_version`),
  KEY `idx_category` (`category`),
  KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户镜像包记录表';

-- 插入一些测试数据
INSERT INTO `customer_package_record` (`record_date`, `package_url`, `fix_content`, `image_id`, `developer`, `commit_id`, `tester`, `test_result`, `remarks`, `android_version`, `category`) VALUES
('2024-01-15 10:30:00', 'http://example.com/package1.zip', '修复了系统崩溃问题', 'img_001', '张三', 'abc123', '李四', '已完成', '重要修复', 'a10', 'vmos'),
('2024-01-16 14:20:00', 'http://example.com/package2.zip', '优化了性能表现', 'img_002', '王五', 'def456', '赵六', '已完成', '性能优化', 'a13', 'qt'),
('2024-01-17 09:15:00', 'http://example.com/package3.zip', '修复了UI显示问题', 'img_003', '孙七', 'ghi789', '周八', '测试中', 'UI修复', 'a14', 'bade'); 