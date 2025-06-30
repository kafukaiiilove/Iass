/*
 Navicat Premium Data Transfer

 Source Server         : manager
 Source Server Type    : MySQL
 Source Server Version : 80040
 Source Host           : localhost:3306
 Source Schema         : backend

 Target Server Type    : MySQL
 Target Server Version : 80040
 File Encoding         : 65001

 Date: 30/06/2025 10:42:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for a10
-- ----------------------------
DROP TABLE IF EXISTS `a10`;
CREATE TABLE `a10`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `parameter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of a10
-- ----------------------------
INSERT INTO `a10` VALUES (1, '啊', '<p>啊</p>', '<p>啊</p>', '<p><img src=\"http://localhost:9090/api/manager/files/1750406029438-小程序码.png\" alt=\"\" data-href=\"\" style=\"\"/></p>', '2025-06-20 15:53:23.27238', 'admin');

-- ----------------------------
-- Table structure for a12
-- ----------------------------
DROP TABLE IF EXISTS `a12`;
CREATE TABLE `a12`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `parameter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of a12
-- ----------------------------

-- ----------------------------
-- Table structure for a13
-- ----------------------------
DROP TABLE IF EXISTS `a13`;
CREATE TABLE `a13`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `parameter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 125 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of a13
-- ----------------------------
INSERT INTO `a13` VALUES (1, '禁用认证，开放与 Android 设备建立 ADB 连接', '<p>提交SHA：9c84c278b40d58fba8fec4f060cf1b45ed79fba0</p><p>auth.cpp android-13-ingdian-rk3588/packages/modules/adb/daemon</p><p><br></p>', 'false', '<p>禁用认证，任何设备都可以与 ADB 守护进程建立连接，而无需任何授权或确认------Frank-2024年7月8日提交</p>', '2025-06-04 11:09:24.042544', 'admin');
INSERT INTO `a13` VALUES (2, '从系统属性中读取名为 persist.audio.disable.output 的值；  如果这个属性不存在，则使用默认值 \"true\"；  结果会存放在变量 audio_disable 中。', '<p>提交SHA：4c1f85173a4a1d88ed1528bee074f3adef1114f0</p><p>hardware/rockchip/audio/tinyalsa_hal/audio_hw.c</p><p>property_get(\"persist.audio.disable.output\", audio_disable, \"true\");</p><p>调换audio和disable的位置</p>', '<p>true</p>', '<p><img src=\"http://localhost:9090/api/manager/files/1749006310637-image.png\" alt=\"\" data-href=\"\" style=\"\"/></p>', '2025-06-04 11:11:38.565853', 'admin');
INSERT INTO `a13` VALUES (3, 'Wi-Fi 模式下 ADB 连接唯一标识', '<p>persist.adb.wifi.guid</p>', '<p>adb-7EExvSc0QWgswXRh-7lNd2V</p>', '<p>Android Debug Bridge（ADB）通过 Wi-Fi 连接设备时的全局唯一会话 ID。通常自动生成，用于识别当前设备在无线调试中的身份。</p>', '2025-06-04 11:22:39.602861', 'admin');
INSERT INTO `a13` VALUES (4, '是否启用 Bluetooth HCI snoop 日志', '<p>persist.bluetooth.btsnoopenable</p>', '<p>false</p>', '<p>该属性控制是否启用蓝牙 HCI（主机控制器接口）日志功能，用于问题调试。设置为 true 时，会记录蓝牙通信细节以供抓包分析</p>', '2025-06-04 11:23:07.308618', 'admin');
INSERT INTO `a13` VALUES (5, 'Bluetooth snoop 日志保存路径', '<p>persist.bluetooth.btsnooppath</p>', '<p>/sdcard/btsnoop_hci.cfa😁</p>', '<p>指定蓝牙 snoop 日志文件的保存位置。该日志可通过 Wireshark 工具打开，用于深入分析蓝牙通信过程。</p>', '2025-06-20 16:27:14.982408', 'admin');
INSERT INTO `a13` VALUES (6, 'Bluetooth snoop 日志最大大小', '<p>persist.bluetooth.btsnoopsize</p>', '<p>0xffff</p>', '<p>配置蓝牙 snoop 文件允许的最大大小。超出后会覆盖旧内容。0xffff 为十六进制，表示 65535 字节（约 64KB）</p>', '2025-06-04 11:23:56.380374', 'admin');
INSERT INTO `a13` VALUES (7, 'Realtek Wi-Fi 与蓝牙协同开关', '<p>persist.bluetooth.rtkcoex</p>', '<p>true</p>', '<p>启用 Realtek 芯片的 Wi-Fi/Bluetooth 协同机制（COEX）。在 Wi-Fi 与 BT 同时工作时避免干扰，增强连接稳定性</p>', '2025-06-04 11:24:18.695629', 'admin');
INSERT INTO `a13` VALUES (8, '蓝牙模块是否关闭电源', '<p>persist.bt.power.down</p>', '<p>true</p>', '<p>决定系统是否允许在闲置时关闭蓝牙模块电源以节省能耗，设为 true 表示可关闭</p>', '2025-06-04 11:25:01.143033', 'admin');
INSERT INTO `a13` VALUES (9, '是否禁用 USB 通知弹窗', '<p>persist.debug.disable_usb_notification🤓</p>', '<p>true</p>', '<p>设为 true 可禁用插入 USB 后的弹窗通知，常用于某些自定义设备或 kiosk 模式</p>', '2025-06-20 16:27:23.943752', 'admin');
INSERT INTO `a13` VALUES (10, '设备尝试引导启动次数计数器', '<p>persist.device_config.attempted_boot_count</p>', '<p>0</p>', '<p>用于记录系统尝试启动的次数，供系统判断是否需进入恢复模式等保护机制</p>', '2025-06-04 11:25:55.587384', 'admin');
INSERT INTO `a13` VALUES (11, '是否启用设备配置标志位（1）', '<p>persist.device_config.configuration.flag</p>', '<p>true</p>', '<p>用于设备定制或控制某些配置参数加载的标志位，视系统用途而定</p>', '2025-06-04 11:26:22.151725', 'admin');
INSERT INTO `a13` VALUES (12, '是否启用设备配置标志位（2）', '<p>persist.device_config.configuration.flag_two</p>', '<p>false</p>', '<p>作为第二通道配置项，用于控制不同配置项启用/禁用状态</p>', '2025-06-04 11:26:56.029706', 'admin');
INSERT INTO `a13` VALUES (13, '是否为公测系统配置', '<p>persist.device_config.configuration.public_launch</p>', '<p>false</p>', '<p>标记系统是否处于公开发布版本，常用于调试和 beta 测试阶段区分</p>', '2025-06-04 14:19:14.940172', 'admin');
INSERT INTO `a13` VALUES (14, 'DNS 超时次数门限配置', '<p>persist.device_config.connectivity.data_stall_consecutive_dns_timeout_threshold</p>', '<p>5</p>', '<p>配置网络连接中断前允许的连续 DNS 超时次数，超过则判定为“数据阻塞”</p>', '2025-06-04 14:19:40.320066', 'admin');
INSERT INTO `a13` VALUES (15, '是否启用 DHCP 初始重启', '<p>persist.device_config.connectivity.dhcp_init_reboot_enabled</p>', '<p>false</p>', '<p>决定 DHCP 是否启用“初始阶段重启机制”，用于加快网络连接恢复</p>', '2025-06-04 14:20:04.001271', 'admin');
INSERT INTO `a13` VALUES (16, 'DHCP 初始重启支持的版本号', '<p>persist.device_config.connectivity.dhcp_init_reboot_version</p>', '<p>0</p>', '<p>用于标识支持初始重启功能的 DHCP 客户端版本，通常配合上项一起使用。</p>', '2025-06-04 14:20:25.889304', 'admin');
INSERT INTO `a13` VALUES (17, 'IP 冲突检测机制版本号', '<p>persist.device_config.connectivity.dhcp_ip_conflict_detect_version</p>', '<p>0</p>', '<p>标识 DHCP 客户端中用于检测 IP 地址冲突的功能版本号，为兼容性配置。</p>', '2025-06-04 14:20:46.114404', 'admin');
INSERT INTO `a13` VALUES (18, '是否启用 DHCP 快速提交模式', '<p>persist.device_config.connectivity.dhcp_rapid_commit_enabled</p>', '<p>false</p>', '<p>快速提交模式允许 DHCP 客户端与服务器一步完成租约协商，启用后连接速度更快但可能影响某些网络的兼容性</p>', '2025-06-04 14:27:05.323266', 'admin');
INSERT INTO `a13` VALUES (19, '支持快速提交的 DHCP 客户端版本号', '<p>persist.device_config.connectivity.dhcp_rapid_commit_version</p>', '<p>1</p>', '<p>指示当前系统的 DHCP 客户端是否支持 rapid commit 功能的版本，用于兼容性控制</p>', '2025-06-04 14:27:55.909867', 'admin');
INSERT INTO `a13` VALUES (20, 'DHCP 配置重启延迟时间（毫秒）', '<p>persist.device_config.connectivity.dhcp_restart_configuration_delay</p>', '<p>1000</p>', '<p>当网络变动触发 DHCP 重启时，延迟指定毫秒后再发起请求，避免瞬间触发多次。</p>', '2025-06-04 14:28:16.45079', 'admin');
INSERT INTO `a13` VALUES (21, 'DHCP 服务拒绝请求的功能版本号', '<p>persist.device_config.connectivity.dhcp_server_decline_version</p>', '<p>0</p>', '<p>控制是否支持客户端向 DHCP 服务器拒绝 IP 地址的功能（如检测到 IP 冲突）</p>', '2025-06-04 14:28:39.255883', 'admin');
INSERT INTO `a13` VALUES (22, '忽略不可达邻居的 IP reachability 机制版本', '<p>persist.device_config.connectivity.ip_reachability_ignore_never_reachable_neighbor_version</p>', '<p>0</p>', '<p>当 ARP/ND 发现某个 IP 永远不可达时，控制是否忽略它而不是报告网络异常。</p>', '2025-06-04 14:28:57.923159', 'admin');
INSERT INTO `a13` VALUES (23, 'IP Client 端支持多播 Neighbor Solicitation 的版本号', '<p>persist.device_config.connectivity.ipclient_multicast_ns_version</p>', '<p>0</p>', '<p>指示系统是否支持通过多播方式发送 IPv6 邻居发现请求，以提升 IPv6 网络效率</p>', '2025-06-04 14:29:17.742961', 'admin');
INSERT INTO `a13` VALUES (24, '低内存杀进程守护进程的关键抖动阈值', '<p>persist.device_config.lmkd_native.thrashing_limit_critical</p>', '<p>300</p>', '<p>当系统频繁换页到达该值时触发 critical level 的内存回收策略（防止系统卡顿）</p>', '2025-06-04 14:29:39.775503', 'admin');
INSERT INTO `a13` VALUES (25, 'DNS 事件采样率控制', '<p>persist.device_config.netd_native.dns_event_subsample_map</p>', '<p>default:1 0:100 7:10</p>', '<p>为不同网络类型（如默认、蜂窝、WiFi）配置 DNS 事件采样比例，用于减少日志开销</p>', '2025-06-04 14:30:00.618516', 'admin');
INSERT INTO `a13` VALUES (26, '是否启用 DNS over HTTPS (DoH)', '<p>persist.device_config.netd_native.doh</p>', '<p>1</p>', '<p>启用后 DNS 查询会走 HTTPS 协议，增强隐私安全性；1 表示启用，0 为关闭</p>', '2025-06-04 14:30:18.144897', 'admin');
INSERT INTO `a13` VALUES (27, 'DoT 查询超时阈值（毫秒）', '<p>persist.device_config.netd_native.dot_query_timeout_ms</p>', '<p>-1</p>', '<p>DNS over TLS 查询的超时时间（负数表示使用默认值）。</p>', '2025-06-04 14:30:40.023581', 'admin');
INSERT INTO `a13` VALUES (28, 'DoT 验证延迟因子', '<p>persist.device_config.netd_native.dot_validation_latency_factor</p>', '<p>-1</p>', '<p>控制验证 DNS over TLS 时延迟的因子值，调节网络验证策略，-1 表示默认配置</p>', '2025-06-04 14:31:01.166363', 'admin');
INSERT INTO `a13` VALUES (29, 'DoT 验证延迟偏移量（毫秒）', '<p>persist.device_config.netd_native.dot_validation_latency_offset_ms</p>', '<p>-1</p>', '<p>用于调整验证 DoT 时的延迟偏移时间，-1 表示使用默认值。</p>', '2025-06-04 14:31:22.361711', 'admin');
INSERT INTO `a13` VALUES (30, '是否允许 DNS 并行查询', '<p>persist.device_config.netd_native.parallel_lookup</p>', '<p>0</p>', '<p>控制是否并行发起多个 DNS 查询以加快响应速度，0 表示禁用。</p>', '2025-06-04 14:31:43.093984', 'admin');
INSERT INTO `a13` VALUES (31, '是否对 DNS nameserver 排序优化', '<p>persist.device_config.netd_native.sort_nameservers</p>', '<p>0</p>', '<p>若启用，对返回的 DNS 服务器排序以优化连接延迟。</p>', '2025-06-04 14:32:00.546943', 'admin');
INSERT INTO `a13` VALUES (32, 'NNAPI 当前支持的 Feature Level', '<p>persist.device_config.nnapi_native.current_feature_level</p>', '<p>7</p>', '<p>代表 Android Neural Networks API 当前启用的功能级别，7 对应 Android 13+</p>', '2025-06-04 14:32:20.566807', 'admin');
INSERT INTO `a13` VALUES (33, '是否启用 NNAPI 使用数据上报（遥测）', '<p>persist.device_config.nnapi_native.telemetry_enable</p>', '<p>false</p>', '<p>用于开发和调试场景，开启后系统会上传 NNAPI 使用数据，false 为默认关闭</p>', '2025-06-04 15:05:04.287376', 'admin');
INSERT INTO `a13` VALUES (34, '本地指标收集模块启用模式（客户端）', '<p>persist.device_config.runtime_native.metrics.reporting-mods</p>', '<p>2</p>', '<p>决定本地 metrics 模块的工作模式，例如定期上报、事件驱动等，2 为某种固定策略</p>', '2025-06-04 14:33:06.636098', 'admin');
INSERT INTO `a13` VALUES (35, '本地指标收集模块启用模式（服务端）', '<p>persist.device_config.runtime_native.metrics.reporting-mods-server</p>', '<p>2</p>', '<p>与上一项类似，作用于服务端的指标收集逻辑，用于上报数据筛选策略。</p>', '2025-06-04 14:33:24.974707', 'admin');
INSERT INTO `a13` VALUES (36, '单次指标最多采集多少种模块（客户端）', '<p>persist.device_config.runtime_native.metrics.reporting-num-mods</p>', '<p>100</p>', '<p>限制一次指标采集涉及模块数量上限，防止过多采样导致性能问题。</p>', '2025-06-04 14:33:43.550239', 'admin');
INSERT INTO `a13` VALUES (37, '单次指标最多采集模块数量（服务端）', '<p>persist.device_config.runtime_native.metrics.reporting-num-mods-server</p>', '<p>100</p>', '<p>与上一项功能相同，但应用于服务器端采样策略。</p>', '2025-06-04 14:34:01.23632', 'admin');
INSERT INTO `a13` VALUES (38, '本地指标上报时间间隔策略（客户端）', '<p>persist.device_config.runtime_native.metrics.reporting-spec</p>', '<p>1,5,30,60,600</p>', '<p>指标数据采样周期，单位为秒，表示每隔多少秒触发上报事件（如：1秒，5秒...）</p>', '2025-06-04 14:34:25.679328', 'admin');
INSERT INTO `a13` VALUES (39, '本地指标上报时间间隔策略（服务端）', '<p>persist.device_config.runtime_native.metrics.reporting-spec-server</p>', '<p>1,10,60,3600,*</p>', '<p>用于服务端控制指标采样周期，* 表示根据当前负载动态调整</p>', '2025-06-04 14:34:52.080932', 'admin');
INSERT INTO `a13` VALUES (40, '启用原生指标采集写入 statsd，用于性能统计与分析', '<p>persist.device_config.runtime_native.metrics.write-to-statsd</p>', '<p>true</p>', '<p>启用后系统运行时收集的关键指标（如启动时间、内存使用）会被写入 statsd，供性能分析使用。</p>', '2025-06-04 14:52:02.548496', 'admin');
INSERT INTO `a13` VALUES (41, '使用应用启动镜像缓存以优化启动速度', '<p>persist.device_config.runtime_native.use_app_image_startup_cache</p>', '<p>true</p>', '<p>启用后，系统会缓存应用启动所需的资源镜像以加快下次冷启动速度</p>', '2025-06-04 14:52:23.469197', 'admin');
INSERT INTO `a13` VALUES (42, '控制是否禁用锁分析（用于性能调试）', '<p>persist.device_config.runtime_native_boot.disable_lock_profiling</p>', '<p>false</p>', '<p>为 false 时，允许系统启用锁分析功能，以追踪线程锁竞争问题</p>', '2025-06-04 14:52:45.140534', 'admin');
INSERT INTO `a13` VALUES (43, '启用第二代基于 userfaultfd 的垃圾回收机制', '<p>persist.device_config.runtime_native_boot.enable_uffd_gc_2</p>', '<p>false</p>', '<p>此选项用于实验性启用 UFFD GC v2。设为 false 表示未启用该机制。</p>', '2025-06-04 14:53:03.751895', 'admin');
INSERT INTO `a13` VALUES (44, '强制禁用 userfaultfd 垃圾回收机制', '<p>persist.device_config.runtime_native_boot.force_disable_uffd_gc</p>', '<p>false</p>', '<p>设为 true 可强制关闭所有使用 userfaultfd 的 GC 操作。</p>', '2025-06-04 14:53:21.933062', 'admin');
INSERT INTO `a13` VALUES (45, '设定不使用 iorap 的包名', '<p>persist.device_config.runtime_native_boot.iorap_blacklisted_packages</p>', '<p>[]</p>', '<p>指定不进行预读取优化的应用包名数组，空数组表示未屏蔽任何包</p>', '2025-06-04 14:53:46.995151', 'admin');
INSERT INTO `a13` VALUES (46, '启用 iorap 的 Perfetto 数据采集支持', '<p>persist.device_config.runtime_native_boot.iorap_perfetto_enable</p>', '<p>false</p>', '<p>启用后会使用 Perfetto 进行 I/O 访问追踪，提高分析精度，当前为禁用状态</p>', '2025-06-04 14:54:06.272974', 'admin');
INSERT INTO `a13` VALUES (47, '启用 iorap 的预读取功能', '<p>persist.device_config.runtime_native_boot.iorap_readahead_enable</p>', '<p>false</p>', '<p>开启后系统会在应用启动时预读所需文件，提升启动性能，此处为关闭状态</p>', '2025-06-04 14:54:27.592611', 'admin');
INSERT INTO `a13` VALUES (48, '设置 iorap 守护进程启动参数', '<p>persist.device_config.runtime_native_boot.iorapd_options</p>', '<p>[]</p>', '<p>可设置自定义选项传递给 iorap 守护进程。空数组表示使用默认配置</p>', '2025-06-04 14:54:50.160375', 'admin');
INSERT INTO `a13` VALUES (49, '启用代际垃圾回收机制', '<p>persist.device_config.runtime_native_boot.use_generational_gc</p>', '<p>true</p>', '<p>启用后将使用代际 GC 策略，更有效地回收短生命周期对象，从而优化整体内存性能。</p>', '2025-06-04 15:05:14.543302', 'admin');
INSERT INTO `a13` VALUES (50, '设置后台存储维护任务是否仅在充电时进行', '<p>persist.device_config.storage_native_boot.charging_required</p>', '<p>true</p>', '<p>当为 true 时，后台如缓存清理等耗电任务只在设备处于充电状态时执行</p>', '2025-06-04 14:55:31.936561', 'admin');
INSERT INTO `a13` VALUES (51, '设定脏页回收率，用于控制文件系统的写入效率', '<p>persist.device_config.storage_native_boot.dirty_reclaim_rate</p>', '<p>0.5</p>', '<p>表示内存页脏数据被写回磁盘的速率。值越大，清理频率越高，可能影响写入延迟</p>', '2025-06-04 14:55:53.135971', 'admin');
INSERT INTO `a13` VALUES (52, '启用 FUSE 文件系统支持（用于虚拟存储等场景）', '<p>persist.device_config.storage_native_boot.fuse_enabled</p>', '<p>1</p>', '<p>1 表示启用 Android 上的 FUSE 文件系统，提供更强的文件访问权限隔离。</p>', '2025-06-04 14:56:12.177942', 'admin');
INSERT INTO `a13` VALUES (53, '设置存储设备的寿命阈值（以百分比表示）', '<p>persist.device_config.storage_native_boot.lifetime_threshold</p>', '<p>70</p>', '<p>当存储设备使用寿命达到该百分比时，系统可能会记录告警或限制写入操作</p>', '2025-06-04 14:56:32.384309', 'admin');
INSERT INTO `a13` VALUES (54, '定义低电量的判定电量百分比', '<p>persist.device_config.storage_native_boot.low_battery_level</p>', '<p>20.0</p>', '<p>当电池电量低于此百分比时，系统将限制后台存储任务执行，以节省电力。</p>', '2025-06-04 14:56:50.513578', 'admin');
INSERT INTO `a13` VALUES (55, '设置最小垃圾回收睡眠时间（单位：毫秒）', '<p>persist.device_config.storage_native_boot.min_gc_sleeptime</p>', '<p>10000</p>', '<p>表示系统进行垃圾回收操作之间的最小休眠时间，防止频繁 GC 耗能</p>', '2025-06-04 14:58:09.428692', 'admin');
INSERT INTO `a13` VALUES (56, '设置触发段回收的最小段数阈值', '<p>persist.device_config.storage_native_boot.min_segments_threshold</p>', '<p>512</p>', '<p>系统只有在超过指定数量的段时才执行段回收，有助于延长设备寿命</p>', '2025-06-04 14:58:26.561386', 'admin');
INSERT INTO `a13` VALUES (57, '控制段回收操作的权重', '<p>persist.device_config.storage_native_boot.segment_reclaim_weight</p>', '<p>1.0</p>', '<p>影响系统如何权衡是否回收 segment。数值越高，系统越积极清理未使用数据。</p>', '2025-06-04 14:58:46.107002', 'admin');
INSERT INTO `a13` VALUES (58, '启用智能空闲期间维护功能', '<p>persist.device_config.storage_native_boot.smart_idle_maint_enabled</p><li></li>', '<p>false</p>', '<p>true 时系统在设备空闲时自动执行维护任务，如清理缓存或整理文件</p>', '2025-06-04 15:04:45.754632', 'admin');
INSERT INTO `a13` VALUES (59, '设置智能空闲维护周期（单位：分钟）', '<p>persist.device_config.storage_native_boot.smart_idle_maint_period</p>', '<p>60</p>', '<p>空闲维护触发的时间间隔。默认每 60 分钟触发一次（若启用该功能）</p>', '2025-06-04 14:59:23.564805', 'admin');
INSERT INTO `a13` VALUES (60, '是否允许系统接管 GetContent 请求处理', '<p>persist.device_config.storage_native_boot.take_over_get_content</p>', '<p>false</p>', '<p>启用后系统会替代默认应用处理“获取内容”的请求，比如图片选择器等</p>', '2025-06-04 14:59:41.543207', 'admin');
INSERT INTO `a13` VALUES (61, '目标“脏页比例”阈值（百分比）', '<p>persist.device_config.storage_native_boot.target_dirty_ratio</p>', '<p>80</p>', '<p>当内存中脏页占比达到此阈值时，系统会强制进行写回操作，以保证稳定性</p>', '2025-06-04 15:00:01.356653', 'admin');
INSERT INTO `a13` VALUES (62, '旧转码兼容配置项（保留）', '<p>persist.device_config.storage_native_boot.transcode_compat_stale</p>', '<p>[]</p>', '<p>暂时无实际用途，可能预留给视频转码兼容策略配置</p>', '2025-06-04 15:00:20.997281', 'admin');
INSERT INTO `a13` VALUES (63, '启用 Skia 图形系统的追踪功能', '<p>persist.device_config.surface_flinger_native_boot.SkiaTracingFeature__use_skia_tracing</p>', '<p>false</p>', '<p>用于性能调试与分析，启用后会记录 GPU 渲染路径的详细执行轨迹。</p>', '2025-06-04 15:00:55.077925', 'admin');
INSERT INTO `a13` VALUES (64, '设定显示屏 X 向的 DPI（像素密度）', '<p>persist.device_config.surface_flinger_native_boot.display.dpi_x</p>', '<p>403411</p>', '<p>单位为 1000 倍的 DPI 值（403.411 DPI），用于精细控制图形缩放</p>', '2025-06-04 15:01:14.671652', 'admin');
INSERT INTO `a13` VALUES (65, '设定显示屏 Y 向的 DPI（像素密度）', '<p>persist.device_config.surface_flinger_native_boot.display.dpi_y</p>', '<p>402389</p>', '<p>单位为 1000 倍的 DPI 值（402.389 DPI），主要用于图形渲染精度调整。</p>', '2025-06-04 15:01:34.009464', 'admin');
INSERT INTO `a13` VALUES (66, '启用或禁用任务快照（Task Snapshots）功能', '<p>persist.enable_task_snapshots</p>', '<p>false</p>', '<p>设为 false 时禁用最近任务界面的小窗口快照（节省资源但影响视觉预览）</p>', '2025-06-04 15:01:53.125596', 'admin');
INSERT INTO `a13` VALUES (67, '配置网络守护进程（netd）的稳定秘钥（IPv6 地址形式）', '<p>persist.netd.stable_secret</p>', '<p>6469:9de3:2cb:4de9:97d7:c184:96da:de5</p>', '<p>用于确定性地生成私有地址（Privacy Extensions），提高 IPv6 地址稳定性</p>', '2025-06-04 15:02:12.624356', 'admin');
INSERT INTO `a13` VALUES (68, '启用 VoNR（5G语音）功能', '<p>persist.radio.is_vonr_enabled_0</p>', '<p>false</p>', '<p>false 表示当前设备的主卡（SIM0）未启用 VoNR。VoNR 为 5G 独立语音通话功能</p>', '2025-06-04 15:02:30.941596', 'admin');
INSERT INTO `a13` VALUES (69, '设定设备支持的多 SIM 卡模式', '<p>persist.radio.multisim.config</p>', '<p>dsds</p>', '<p>表示设备处于“双卡双待”（Dual SIM Dual Standby）模式。可选值如 dsda, ss</p>', '2025-06-04 15:04:33.427609', 'admin');
INSERT INTO `a13` VALUES (70, '记录系统的最近一次启动原因', '<p>persist.sys.boot.reason</p>', '<p>[]</p>', '<p>该字段记录上次系统启动的原因（如 reboot、powerkey、rtc 等），为空表示无记录或重置。</p>', '2025-06-04 15:06:59.35537', 'admin');
INSERT INTO `a13` VALUES (71, '记录系统启动历史原因', '<p>persist.sys.boot.reason.history</p>', '<p>reboot,factory_reset,1749002095</p>', '<p>以逗号分隔的启动记录，例如人为重启（reboot）、恢复出厂设置（factory_reset）等。</p>', '2025-06-04 15:07:17.404363', 'admin');
INSERT INTO `a13` VALUES (72, '设置默认国家码', '<p>persist.sys.country</p>', '<p>[]</p>', '<p>用于定位、语言等区域相关服务。为空时，系统可能根据 SIM 或 GPS 自动判断</p>', '2025-06-04 15:07:36.596481', 'admin');
INSERT INTO `a13` VALUES (73, '设置系统语言（语言代码）', '<p>persist.sys.language</p>', '<p>[]</p>', '<p>表示语言设置，例如 en 为英文，zh 为中文。为空表示未明确设置。</p>', '2025-06-04 15:07:56.909364', 'admin');
INSERT INTO `a13` VALUES (74, '设置完整区域语言与国家代码', '<p>persist.sys.locale</p>', '<p>en-US</p>', '<p>组合形式的区域语言设置，格式为 language-country，用于全局本地化</p>', '2025-06-04 15:08:17.338909', 'admin');
INSERT INTO `a13` VALUES (75, '区域语言附加变量配置（备用字段）', '<p>persist.sys.localevar</p>', '<p>[]</p>', '<p>用于系统额外控制区域行为，通常为空或内部使用</p>', '2025-06-04 15:08:38.73517', 'admin');
INSERT INTO `a13` VALUES (76, '系统时区设置', '<p>persist.sys.timezone</p>', '<p>GMT</p>', '<p>系统时区设定，例如 GMT, Asia/Shanghai, America/New_York</p>', '2025-06-04 15:11:22.684904', 'admin');
INSERT INTO `a13` VALUES (77, '指定 Android 虚拟机使用的库', '<p>persist.sys.dalvik.vm.lib.2</p>', '<p>libart.so</p>', '<p>指向当前使用的 Android 运行时（ART）库名称，ART 替代 Dalvik 是 Android 默认运行时。</p>', '2025-06-04 15:11:55.124426', 'admin');
INSERT INTO `a13` VALUES (78, '标志设备是否已完成首次初始化', '<p>persist.sys.device_provisioned</p>', '<p>1</p>', '<p>1 表示设备已经初始化完成（如 Google 账号登录等）；0 表示尚未完成设置</p>', '2025-06-04 15:12:15.068174', 'admin');
INSERT INTO `a13` VALUES (79, '状态栏或 UI 顶部保留区域设置', '<p>persist.sys.displayinset.top</p>', '<p>0</p>', '<p>设置顶部 inset 区域的高度，单位为像素，影响屏幕内容的显示区域。</p>', '2025-06-04 15:12:37.718502', 'admin');
INSERT INTO `a13` VALUES (80, '是否启用 FUSE 文件系统支持', '<p>persist.sys.fuse</p>', '<p>true</p>', '<p>表示系统是否使用 FUSE（用户空间文件系统）以支持扩展存储访问</p>', '2025-06-04 15:12:59.894305', 'admin');
INSERT INTO `a13` VALUES (81, '是否允许 FUSE 直通模式（绕过虚拟化）', '<p>persist.sys.fuse.passthrough.enable</p>', '<p>true</p>', '<p>开启后 FUSE 文件操作可绕过部分虚拟文件系统层，提高访问性能</p>', '2025-06-04 15:13:17.925044', 'admin');
INSERT INTO `a13` VALUES (82, '设置 GPS LPP（定位协议）级别', '<p>persist.sys.gps.lpp</p>', '<p>2</p>', '<p>0 表示关闭，1 为 LPP-user-plane 模式，2 为 control-plane 模式，用于高精度定位。</p>', '2025-06-04 15:13:35.246065', 'admin');
INSERT INTO `a13` VALUES (83, '启用 LMK（Low Memory Killer）杀进程报告', '<p>persist.sys.lmk.reportkills</p>', '<p>true</p>', '<p>系统是否报告 LMK 杀死进程的情况，便于调试内存不足的问题</p>', '2025-06-04 15:13:55.928358', 'admin');
INSERT INTO `a13` VALUES (84, '调节系统全局色彩饱和度', '<p>persist.sys.sf.color_saturation</p>', '<p>1.0</p>', '<p>取值范围一般为 0.0~2.0，1.0 表示标准色彩，&lt;1 为去饱和，&gt;1 为增强</p>', '2025-06-04 15:14:15.944509', 'admin');
INSERT INTO `a13` VALUES (85, '启用或禁用 StrictMode 可视化指示器', '<p>persist.sys.strictmode.visual</p>', '<p>false</p>', '<p>StrictMode 用于检测主线程阻塞问题，开启视觉提示会显示红框警告</p>', '2025-06-04 15:14:33.84756', 'admin');
INSERT INTO `a13` VALUES (86, '设定当前系统主题', '<p>persist.sys.theme</p>', '<p>1</p>', '<p>数字代表不同主题风格，可能因设备定制 ROM 而异，如 0 表示浅色、1 表示深色模式。</p>', '2025-06-04 15:14:52.673046', 'admin');
INSERT INTO `a13` VALUES (87, '设置系统默认 USB 功能', '<p>persist.sys.usb.config</p>', '<p>none</p>', '<p>控制设备通过 USB 连接时启用的功能，如 mtp, adb, ptp, none 等，none 表示不启用任何模式</p>', '2025-06-04 15:21:49.920353', 'admin');
INSERT INTO `a13` VALUES (88, '是否启用 Android 性能追踪系统 traced', '<p>persist.traced.enable</p>', '<p>1</p>', '<p>是否启用 perfetto 的跟踪守护进程，1 表示开启，用于性能分析、系统事件追踪</p>', '2025-06-04 15:23:17.990436', 'admin');
INSERT INTO `a13` VALUES (89, '设置主帧缓冲区（主屏）分辨率与刷新率', '<p>persist.vendor.framebuffer.main</p>', '<p>1080x2408@60</p>', '<p>设置设备主屏幕的分辨率和刷新率格式为 宽x高@刷新率Hz，如 1080x2408 分辨率，60Hz。</p>', '2025-06-04 15:23:43.195588', 'admin');
INSERT INTO `a13` VALUES (90, 'HID（人机接口设备）状态控制（厂商自定义）', '<p>persist.vendor.sys.hid</p>', '<p>[]</p>', '<p>供应商用于控制或调试 HID（如鼠标、手柄、键盘等），通常为空，依厂商 ROM 实现。</p>', '2025-06-04 15:24:00.141554', 'admin');
INSERT INTO `a13` VALUES (91, 'WiFi 睡眠延迟时间（毫秒）', '<p>persist.wifi.sleep.delay.ms</p>', '<p>0</p>', '<p>当屏幕关闭时 WiFi 延迟断开的时间。0 表示立即断开；设置非零值可延迟进入 WiFi 睡眠。</p>', '2025-06-04 15:24:16.881731', 'admin');
INSERT INTO `a13` VALUES (92, '控制厂商云服务白名单开关', '<p>persists.sys.cloud.white_list_enable</p>', '<p>0</p>', '<p>厂商定制白名单控制，如是否允许某些应用始终运行在后台；0 表示关闭，1 表示启用。</p>', '2025-06-04 15:24:37.501745', 'admin');
INSERT INTO `a13` VALUES (93, '指定 FRP（Factory Reset Protection）数据分区位置', '<p>ro.frp.pst</p>', '<p>/dev/block/persistent</p>', '<p>Android 的 FRP 机制用于防止未授权恢复出厂设置，此路径为其数据存储位置</p>', '2025-06-04 15:25:12.725972', 'admin');
INSERT INTO `a13` VALUES (94, '标记持久属性系统是否就绪', '<p>ro.persistent_properties.ready</p>', '<p>true</p>', '<p>表示系统是否完成只读 persist.* 属性的初始化。为 true 表示可以安全读取这些值</p>', '2025-06-04 15:25:32.312874', 'admin');
INSERT INTO `a13` VALUES (95, 'Wi-Fi IP地址', '<p>persist.sys.cloud.wifi.ip &nbsp; &nbsp;</p>', '<p>192.168.93.86</p>', '<p>虚拟设备的本地IP地址</p>', '2025-06-16 17:23:04.200902', 'admin');
INSERT INTO `a13` VALUES (96, 'Wi-Fi 网关地址', '<p>persist.sys.cloud.wifi.gateway</p>', '<p>192.168.93.1</p>', '<p>局域网网关地址</p>', '2025-06-16 17:23:34.03095', 'admin');
INSERT INTO `a13` VALUES (97, 'Wi-Fi MAC地址', '<p>persist.sys.cloud.wifi.mac</p>', '<p>D2:0D:BB:38:85:C4</p>', '<p>无线网卡的物理MAC地址</p>', '2025-06-16 17:24:04.648742', 'admin');
INSERT INTO `a13` VALUES (98, 'DNS服务器', '<p>persist.sys.cloud.wifi.dns1</p>', '<p>192.168.93.1</p>', '<p>指定DNS服务器的地址</p>', '2025-06-16 17:24:25.633622', 'admin');
INSERT INTO `a13` VALUES (99, '蓝牙MAC地址', '<p>persist.sys.cloud.bluetoothaddr</p>', '<p>8C:E1:08:90:88:0F</p>', '<p>蓝牙适配器的物理地址</p>', '2025-06-16 17:24:44.628379', 'admin');
INSERT INTO `a13` VALUES (100, '蓝牙设备名称', '<p>ro.bt.devicename</p>', '<p>bacuu</p>', '<p>蓝牙设备对外显示名称</p>', '2025-06-16 17:25:06.146917', 'admin');
INSERT INTO `a13` VALUES (101, '模拟ADB控制开关', '<p>persist.sys.cloud.madb_enable</p>', '<p>0</p>', '<p>是否开启模拟ADB调试，0=关闭，1=开启</p>', '2025-06-16 17:25:25.125058', 'admin');
INSERT INTO `a13` VALUES (102, '国家代码', '<p>persist.sys.country</p>', '<p>AD</p>', '<p>国家代号（例如AD=安道尔）</p>', '2025-06-16 17:25:43.130493', 'admin');
INSERT INTO `a13` VALUES (103, '地区语言', '<p>persist.sys.locale</p>', '<p>en-US</p>', '<p>区域设定（语言-国家）</p>', '2025-06-16 17:26:05.978186', 'admin');
INSERT INTO `a13` VALUES (104, '语言', '<p>persist.sys.language</p>', '<p>en</p>', '<p>简单语言设置（通常已被locale覆盖）</p>', '2025-06-16 17:26:26.31515', 'admin');
INSERT INTO `a13` VALUES (105, '时区', '<p>persist.sys.timezone</p>', '<p>America/New_York</p>', '<p>系统默认时区（纽约时间）</p>', '2025-06-16 17:26:50.124328', 'admin');
INSERT INTO `a13` VALUES (106, '集群标识码', '<p>ro.boot.cluster_code</p>', '<p>ZEG4201259</p>', '<p>厂商自定义的设备集群识别码</p>', '2025-06-16 17:27:41.199121', 'admin');
INSERT INTO `a13` VALUES (107, 'Redroid DNS地址', '<p>ro.boot.redroid_net_dns1</p>', '<p>192.168.201.15</p>', '<p>Redroid网络DNS地址</p>', '2025-06-16 17:28:03.672879', 'admin');
INSERT INTO `a13` VALUES (108, 'Redroid DNS数量标志', '<p>ro.boot.redroid_net_ndns</p>', '<p>1</p>', '<p>启用自定义DNS标志（注释）</p>', '2025-06-16 17:28:20.851591', 'admin');
INSERT INTO `a13` VALUES (109, '电池容量', '<p>persist.sys.cloud.battery.capacity</p>', '<p>8000</p>', '<p>电池总容量（单位：毫安时）</p>', '2025-06-16 17:28:44.30793', 'admin');
INSERT INTO `a13` VALUES (110, '当前电量百分比', '<p>persist.sys.cloud.battery.level</p>', '<p>64</p>', '<p>当前电池剩余电量百分比</p>', '2025-06-16 17:29:04.410952', 'admin');
INSERT INTO `a13` VALUES (111, 'GPS纬度', '<p>persist.sys.cloud.gps.lat</p>', '<p>42.5082</p>', '<p>模拟的GPS纬度坐标</p>', '2025-06-16 17:29:25.928403', 'admin');
INSERT INTO `a13` VALUES (112, 'GPS经度', '<p>persist.sys.cloud.gps.lon</p>', '<p>1.5448</p>', '<p>模拟的GPS经度坐标</p>', '2025-06-16 17:29:46.634766', 'admin');
INSERT INTO `a13` VALUES (113, 'SIM卡ICCID', '<p>persist.sys.cloud.iccidnum</p>', '<p>89376427788487715131</p>', '<p>国际集成电路卡标识码</p>', '2025-06-16 17:30:07.673653', 'admin');
INSERT INTO `a13` VALUES (114, 'IMEI编号', '<p>persist.sys.cloud.imeinum</p>', '<p>305732724840426</p>', '<p>移动设备国际识别码（IMEI）</p>', '2025-06-16 17:30:32.056722', 'admin');
INSERT INTO `a13` VALUES (115, 'MCC/MNC代码', '<p>persist.sys.cloud.mobileinfo</p>', '<p>213,03</p>', '<p>MCC: 国家码（213=安道尔），MNC: 运营商代码</p>', '2025-06-16 17:30:49.16106', 'admin');
INSERT INTO `a13` VALUES (116, '基站信息', '<p>persist.sys.cloud.cellinfo</p>', '<p>9,213,03,15,12C0052,3E8,10</p>', '<p>模拟小区/基站信息（不固定格式）</p>', '2025-06-16 17:31:10.842453', 'admin');
INSERT INTO `a13` VALUES (117, '手机号码', '<p>persist.sys.cloud.phonenum</p>', '<p>37622238078</p>', '<p>模拟手机号码（376=安道尔国家码）</p>', '2025-06-16 17:31:27.673559', 'admin');
INSERT INTO `a13` VALUES (118, 'IMSI码', '<p>persist.sys.cloud.imsinum</p>', '<p>213038515134157</p>', '<p>国际移动用户识别码</p>', '2025-06-16 17:31:46.90059', 'admin');
INSERT INTO `a13` VALUES (119, '开放应用获取root权限', '<p>setprop persist.sys.cloud.ssaid_enable</p>', '<p>😅0是开放应用获取权限</p><p>😁1是关闭应用获取权限</p>', '<p>在高版本安卓系统中，应用获取到的安卓id是调用String androidId = Settings.Secure.getString(</p><p> &nbsp; &nbsp;getContentResolver(), Settings.Secure.ANDROID_ID);</p><p>Log.d(\"MyAndroidID\", androidId);动态生成的，应用默认无法获取全局安卓id</p>', '2025-06-18 13:36:48.871322', 'admin');
INSERT INTO `a13` VALUES (120, '禁用谷歌服务框架更新', '<p>getprop persist.sys.cloud.gsfsu </p>', '<p>0 关闭自动更新</p><p>1 开启更新</p>', '<p>禁用谷歌服务框架自动更新</p>', '2025-06-20 15:52:16.444954', 'admin');
INSERT INTO `a13` VALUES (121, '谷歌服务禁用自动更新', '<p><br></p><p>getprop persist.sys.cloud.gmssu</p>', '<p>0 禁用自动更新</p><p>1 开放自动更新</p>', '<p>禁用谷歌服务更新</p>', '2025-06-20 15:50:39.213634', 'admin');
INSERT INTO `a13` VALUES (122, '谷歌商店禁用更新', '<p>getprop persist.sys.cloud.gpsu</p>', '<p>0 禁用自动更新</p><p>1 开放自动更新</p>', '<p>禁用谷歌商店自动更新</p>', '2025-06-20 15:51:40.498056', 'admin');
INSERT INTO `a13` VALUES (123, '查看系统是否支持 32位还是64 位', '<p>getprop ro.product.cpu.abilist</p>', '<p>arm64-v8a,armeabi-v7a</p>', '<p>该属性可以获取当前系统or机型模板是支持32位还是64位或是通用</p>', '2025-06-25 18:00:26.570423', 'liyi');
INSERT INTO `a13` VALUES (124, '面具启用', '<p>ro.boot.vmos_magisk</p>', '', '<p>存在就启用，不存在就不启用</p>', '2025-06-27 15:01:31.244342', 'liyi');

-- ----------------------------
-- Table structure for a14
-- ----------------------------
DROP TABLE IF EXISTS `a14`;
CREATE TABLE `a14`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `parameter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of a14
-- ----------------------------
INSERT INTO `a14` VALUES (1, 'a', 'a阿', '<p><img src=\"http://localhost:9090/api/manager/files/1750128390350-旺仔.png\" alt=\"\" data-href=\"\" style=\"\"/></p>', '<p><br></p>', '2025-05-14 17:49:09.24165', 'admin');

-- ----------------------------
-- Table structure for a15
-- ----------------------------
DROP TABLE IF EXISTS `a15`;
CREATE TABLE `a15`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `parameter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of a15
-- ----------------------------
INSERT INTO `a15` VALUES (1, 'a', 'a&nbsp;', '阿', NULL, '2025-05-14 18:07:23.641009', 'admin');

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色标识',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'admin', '管理员', 'http://localhost:9090/api/manager/files/1748526996113-旺仔.png', 'ADMIN', '17371457172', 'coolkafuka@163.com');
INSERT INTO `admin` VALUES (6, 'liyi', '123456', 'liyi', NULL, 'ADMIN', '17371457171', 'coolkafuka@163.com');
INSERT INTO `admin` VALUES (7, 'zhangwenyi', '123456', '张文奕', NULL, 'CeShi', NULL, NULL);
INSERT INTO `admin` VALUES (8, 'tanghuanhuan', '123', '汤欢欢', NULL, 'CeShi', NULL, NULL);
INSERT INTO `admin` VALUES (9, 'lisong', '123', '李松', NULL, 'CeShi', NULL, NULL);
INSERT INTO `admin` VALUES (10, 'chaixiang', 'like123', '柴祥', NULL, 'KaiFa', NULL, NULL);
INSERT INTO `admin` VALUES (11, 'caoyuan', '123456', '曹媛', NULL, 'CeShi', NULL, NULL);
INSERT INTO `admin` VALUES (12, 'chensijia', 'iasszglht', '陈斯佳', NULL, 'CeShi', NULL, NULL);
INSERT INTO `admin` VALUES (13, 'yt', '123456', '杨霆', NULL, 'KaiFa', NULL, NULL);
INSERT INTO `admin` VALUES (14, 'zengzhiyu', '123456', '曾知宇', NULL, 'KaiFa', NULL, NULL);

-- ----------------------------
-- Table structure for category_config
-- ----------------------------
DROP TABLE IF EXISTS `category_config`;
CREATE TABLE `category_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `android_version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Android版本 (例如: a10, a13)',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `category_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类值',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否为默认分类 (0:否, 1:是)',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序顺序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_version_value`(`android_version` ASC, `category_value` ASC) USING BTREE COMMENT '版本和分类值的唯一索引',
  INDEX `idx_android_version`(`android_version` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category_config
-- ----------------------------
INSERT INTO `category_config` VALUES (1, 'a10', 'VMOS', 'vmos', 1, 1, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (2, 'a10', '公共包', 'public', 1, 2, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (3, 'a10', '其他', 'other', 1, 99, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (4, 'a13', 'VMOS', 'vmos', 1, 1, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (5, 'a13', '公共包', 'public', 1, 2, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (6, 'a13', '上海通用', 'shanghai_common', 1, 3, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (7, 'a13', 'QT', 'qt', 1, 4, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (8, 'a13', '巴德', 'bade', 1, 5, '2025-06-27 17:05:00', '2025-06-27 17:54:34');
INSERT INTO `category_config` VALUES (9, 'a13', '毕少', 'bishao', 1, 6, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (10, 'a13', '其他', 'other', 1, 99, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (11, 'a14', 'VMOS', 'vmos', 1, 1, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (12, 'a14', '公共包', 'public', 1, 2, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (13, 'a14', 'QT', 'qt', 1, 3, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (14, 'a14', '新硕', 'xinshuo', 1, 4, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (15, 'a14', 'GeeLark', 'geelark', 1, 5, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (16, 'a14', 'ML', 'ml', 1, 6, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (17, 'a14', '毕少', 'bishao', 1, 7, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (18, 'a14', '其他', 'other', 1, 99, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (19, 'a15', 'VMOS', 'vmos', 1, 1, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (20, 'a15', '公共包', 'public', 1, 2, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (21, 'a15', 'QT', 'qt', 1, 3, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (22, 'a15', 'ML', 'ml', 1, 4, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (23, 'a15', 'GeeLark', 'geelark', 1, 5, '2025-06-27 17:05:00', '2025-06-27 17:05:00');
INSERT INTO `category_config` VALUES (24, 'a15', '其他', 'other', 1, 99, '2025-06-27 17:05:00', '2025-06-27 17:05:00');

-- ----------------------------
-- Table structure for customer_package_record
-- ----------------------------
DROP TABLE IF EXISTS `customer_package_record`;
CREATE TABLE `customer_package_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `record_date` datetime NULL DEFAULT NULL COMMENT '时间年月日时分',
  `package_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '镜像包地址',
  `fix_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '修复内容描述',
  `image_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '镜像ID',
  `developer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发工程师',
  `commit_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Commit id',
  `tester` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '测试工程师',
  `test_result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '待测试' COMMENT '测试结果',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
  `android_version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Android版本 (例如: a10, a13)',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类 (例如: vmos, qt)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_android_version`(`android_version` ASC) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_record_date`(`record_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户镜像包记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer_package_record
-- ----------------------------
INSERT INTO `customer_package_record` VALUES (14, '2025-06-24 17:14:00', 'http://192.168.22.43/files/lingdian/A10/vcloud_android10_user_20250620_030734_master_535241e.tar', '修复升级img-25051923275，img-25060299348镜像失败，clash无法代理', 'img-25062025769', '大林', '535241e', '李松', '已完成', '验证的是清空数据升级的，导致运维那边报不清除数据升级异常，定位问题是SQLiteException、AccountManagerService 错误，需要删除/data/system_ce/0/accounts_ce.db.journal；/data/system_ce/0/accounts_ce.db文件重启，需要修改selinux修复', 'a10', 'vmos', '2025-06-24 17:16:27', '2025-06-25 18:16:16');
INSERT INTO `customer_package_record` VALUES (15, '2025-06-24 17:16:00', 'http://192.168.22.43/files/lingdian/A14/vcloud_android14_user_20250620_180558_vmos_ab34578.tar', '修复级镜像失败，每次更新时清除package_cache', 'img-25062063174', '大林', 'ab34578', '张文奕', '已完成', NULL, 'a14', 'vmos', '2025-06-24 17:18:10', '2025-06-24 17:18:10');
INSERT INTO `customer_package_record` VALUES (16, '2025-06-24 17:19:00', 'http://192.168.22.43/files/lingdian/A13/vcloud_android13_user_20250621_012153_vmos-magisk_f8d96f6.tar', '云机适配magisk', 'img-25061987725', '曾知宇', 'f8d96f6', '李露霜', '已完成', '内核版本需要用1.0.54', 'a13', 'vmos', '2025-06-24 17:20:08', '2025-06-24 17:25:51');
INSERT INTO `customer_package_record` VALUES (17, '2025-06-24 17:31:00', 'http://192.168.22.43/files/lingdian/A13/vcloud_android13_user_vmos_yt_3fcd9501.tar', '1、修复ipv6 address，蓝牙，wifimac地址一样\n2、添加运营商标识\n3、内置chrome和代理两个应用', 'img-25062434137', '杨霆', '3fcd9501', '汤欢欢', '已完成', '客户经理已交付', 'a13', 'vmos', '2025-06-24 17:33:05', '2025-06-24 20:02:39');
INSERT INTO `customer_package_record` VALUES (18, '2025-06-24 17:50:00', 'http://192.168.22.43/files/lingdian/A14/vcloud_android14_user_20250624_master_de8edbdf.tar', 'Androir 14从应用调vu获取/data/data下的内容', 'img-25062498884', '岳彪', 'de8edbdf', '张文奕', '未通过', '设置Pin码闪退为java.lang.IllegalStateException: Failed to decrypt blob，\nat com.android.server.locksettings.SyntheticPasswordCrypto.decryptBlob解密 SyntheticPassword blob 时失败。\n', 'a14', 'xinshuo', '2025-06-24 17:59:49', '2025-06-24 17:59:49');
INSERT INTO `customer_package_record` VALUES (19, '2025-06-24 18:01:00', 'http://192.168.22.43/files/lingdian/A13/vcloud_android13_user_yt_186191e7.tar', '抖音没声音、通话没声音，更新Rtc1.0适配P2P', NULL, '杨霆', '186191e7', '汤欢欢', '测试中', '上海通用私有云验证Rtc1.0修复内容', 'a13', 'shanghai_common', '2025-06-24 18:02:40', '2025-06-24 19:58:40');
INSERT INTO `customer_package_record` VALUES (20, '2025-06-24 18:04:00', 'http://192.168.22.43/files/lingdian/A15/vcloud_android15_user_20250624_ML_cx3.tar', '修复点：\n1.phoneNumber和SIM卡状态手机号码一致；\n2.ipv6地址一致；\n3.运营商显示；\n4.信号图标显示；\n5.应用获取root获取/data/data目录下的内容\n5.rtc版本5.3.5{\n1.修复设置无效代理之后断网；\n}', 'img-25062467299', '柴祥', 'ba0b3acc   ', '李露霜', '已完成', '这个tar包未增加应用调vu拿/data/data功能，在http://192.168.22.43/files/lingdian/A15/vcloud_android15_user_20250624_ML_e379c268.tar中有增加，Commit ID是e379c2684246c84d82f81c6317af84f400806c7a；\n历史问题：\n相机偶发性白屏\n云机屏幕白边\n导航栏', 'a15', 'ml', '2025-06-24 18:06:45', '2025-06-24 21:27:41');
INSERT INTO `customer_package_record` VALUES (21, '2025-06-24 19:30:00', 'http://192.168.22.43/files/lingdian/A13/vcloud_android13_user_bs_yt.tar', 'Vmos去除GMS，完成通用测试用例', 'img-25062322735', '杨霆', 'deca218d', '陈斯佳', '已完成', NULL, 'a13', 'bishao', '2025-06-24 19:30:58', '2025-06-24 20:08:17');
INSERT INTO `customer_package_record` VALUES (22, '2025-06-24 19:32:00', 'http://192.168.22.43/files/lingdian/A14/vcloud_android14_user_20250623_231341_MoreLogin_44f1c3c.tar', '1.GP闪退修复\n2.应用调vu获取/data/data下的内容', NULL, '柴祥', '44f1c3c', '汤欢欢', '未通过', NULL, 'a14', 'ml', '2025-06-24 19:34:16', '2025-06-24 21:26:07');
INSERT INTO `customer_package_record` VALUES (25, '2025-06-25 11:48:00', 'http://192.168.22.43/files/lingdian/A14/vcloud_android14_user_20250625_153041_MoreLogin_129fb3bd.tar ', '1.phoneNumber和SIM卡状态手机号码一致；\n2.ipv6地址一致；\n3.运营商显示；\n4.信号图标显示；\n5.旧镜像不清空数据升级新镜像设置Pin码崩溃\n6.rtc版本5.3.6{\n1.修复设置无效代理之后断网；\n2. ip和域名规则的绕过添加到yaml配置文件\n3. 新增设置分辨率dpi功能\n4. 恢复分辨率dpi功能\n}', 'img-25062562451', '柴祥', '129fb3bd', '张文奕', '已完成', '<p>😀</p>', 'a14', 'ml', '2025-06-25 11:50:13', '2025-06-27 09:45:20');
INSERT INTO `customer_package_record` VALUES (26, '2025-06-25 14:37:00', 'http://192.168.22.43/files/lingdian/A14/vcloud_android14_user_20250625_master_bf716c7d.tar', '基于Commit ID：4a955955，【A14新硕】公共包—大林（sysytem ui崩溃修复) +（Rtc5.3.4回滚clash)修复\n新增修复点：\n1.Androir 14从应用调vu获取/data/data下的内容；\n2.从旧镜像不清空数据升级新镜像设置Pin码崩溃闪退；\nrtc版本5.3.6{\n1. ip和域名规则的绕过添加到yaml配置文件\n2. 新增设置分辨率dpi功能\n3. 恢复分辨率dpi功能\n}', NULL, '岳彪', 'bf716c7d', '汤欢欢', '已完成', NULL, 'a14', 'xinshuo', '2025-06-25 14:37:15', '2025-06-26 18:08:57');
INSERT INTO `customer_package_record` VALUES (27, '2025-06-25 14:37:00', 'http://192.168.22.43/files/lingdian/A13/vcloud_android13_user_vmos_yt_e3dab242.tar', '基于镜像id：img-25062434137,Commit:3fcd9501,Vmos分支新增修复点\n1.应用分身；\n2.64位chrome，修复google Pixel7pro机型打开chrome不闪退；\nrtc版本：3.3.7{\n1.修复GMS自动更新导致流量消耗异常\n}\nLauncher版本：2.3.12\nAppStore版本：1.0.11\nTools版本：1.0.44.1', NULL, '何光明、杨霆', 'e3dab242', '陈斯佳', '测试中', NULL, 'a13', 'vmos', '2025-06-25 14:37:55', '2025-06-25 18:13:20');
INSERT INTO `customer_package_record` VALUES (28, '2025-06-25 18:10:00', 'http://192.168.22.43/files/lingdian/A10/vcloud_android10_user_20250625_200447_master_4292145.tar', '验证不清空数据升级img-25051923275 ，img-25060299348两个镜像能否成功;', 'img-25062593217', '岳彪', '4292145', '张文奕', '未通过', '<p>实例任务-改机属性执行失败</p>', 'a10', 'vmos', '2025-06-25 18:11:04', '2025-06-26 16:13:17');
INSERT INTO `customer_package_record` VALUES (33, '2025-06-26 11:22:00', 'http://192.168.22.43/files/lingdian/A10/vcloud_android10_user_20250626_111445_master_b019aab.tar', '1.修复升级img-25051923275，img-25060299348不清空数据失败\n2.rtc版本：3.3.8{\n1.\n}\n3.调用改机属性报错一致在执行中', 'img-25062624294', '岳彪', 'b019aab', '张文奕', '已完成', '<p>风险点：1、屏幕无白边 2、vmos10没有调用修改改机属性功能</p>', 'a10', 'vmos', '2025-06-26 11:23:05', '2025-06-26 16:11:56');
INSERT INTO `customer_package_record` VALUES (34, '2025-06-26 16:23:00', 'http://192.168.22.43/files/lingdian/A13/vcloud_android13_user_public_yt_3d12297d.tar', 'rtc3.3.8', 'img-25062636467', '杨霆', '3d12297d', '汤欢欢', '未通过', '<p>pif没过，Pif 版本过低</p>', 'a13', 'public', '2025-06-26 16:23:24', '2025-06-27 12:06:31');
INSERT INTO `customer_package_record` VALUES (36, '2025-06-27 12:10:00', 'http://192.168.22.43/files/lingdian/A13/vcloud_android13_user_public_yt_66fd6f78.tar', '验证修复点：\n1.系统播放没有声音，第三方APP中播放是有声音的+公共测试用例\n2.修复pif三绿', 'img-25062788102', '杨霆', '66fd6f78', '汤欢欢', '已完成', '<p>1.下拉框未显示手机运营商，网络状态；</p><p>2.ipv6地址创建实例时每个容器都一样，未随机；</p>', 'a13', 'public', '2025-06-27 12:10:29', '2025-06-27 15:41:52');
INSERT INTO `customer_package_record` VALUES (38, '2025-06-27 16:37:00', 'http://192.168.22.43/files/lingdian/A14/vcloud_android14_user_20250627_161503_GeeLark_d313f77a.tar    14    GL   新RTC和PIF@李溢 ', '1.pif三绿', NULL, '柴祥', 'd313f77a', '汤欢欢', '已完成', '<p>客户反应：pif三绿掉了;</p><p>客户6/29日反应无人直播推流有问题</p>', 'a14', 'geelark', '2025-06-27 16:37:54', '2025-06-30 09:45:44');

-- ----------------------------
-- Table structure for lucky_draw_record
-- ----------------------------
DROP TABLE IF EXISTS `lucky_draw_record`;
CREATE TABLE `lucky_draw_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '问题描述',
  `priority` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'normal' COMMENT '优先级：urgent紧急，important重要，normal一般',
  `participants` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参与人员列表，逗号分隔',
  `winner` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '中奖者姓名',
  `winner_index` int NOT NULL COMMENT '中奖者在参与列表中的索引',
  `participant_count` int NOT NULL COMMENT '参与人数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_winner`(`winner` ASC) USING BTREE,
  INDEX `idx_priority`(`priority` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '幸运大转盘抽奖记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lucky_draw_record
-- ----------------------------
INSERT INTO `lucky_draw_record` VALUES (1, '解决登录页面的样式问题', 'normal', '张三,李四,王五,赵六', '李四', 1, 4, '2025-06-30 09:54:41', '2025-06-30 09:54:41');
INSERT INTO `lucky_draw_record` VALUES (2, '修复数据库连接超时bug', 'urgent', '张三,李四,王五', '王五', 2, 3, '2025-06-30 09:54:41', '2025-06-30 09:54:41');
INSERT INTO `lucky_draw_record` VALUES (3, '优化查询性能', 'important', '张三,李四,王五,赵六,钱七', '张三', 0, 5, '2025-06-30 09:54:41', '2025-06-30 09:54:41');
INSERT INTO `lucky_draw_record` VALUES (4, '添加新的用户权限功能', 'normal', '李四,王五,赵六', '赵六', 2, 3, '2025-06-30 09:54:41', '2025-06-30 09:54:41');
INSERT INTO `lucky_draw_record` VALUES (5, '处理前端组件兼容性问题', 'important', '张三,李四,王五,赵六', '王五', 2, 4, '2025-06-30 09:54:41', '2025-06-30 09:54:41');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `parameter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (6, '2025/6/9-安卓14-qt-柴祥', '<p>1. 新增音视频打开关闭广播</p><p>2. 新增任务注入音频，默认循环播放</p><p>3.更新Rtc版本5.2.6</p><p>4.镜像id：img-2506091619</p>', '<p><br></p>', '<p><br></p>', '2025-06-10 14:02:43.183299', 'admin');
INSERT INTO `notice` VALUES (7, '2025/6/9-安卓13出境易—柴祥', '<p><br></p><p>1. apmt可用</p><p>2. pif3绿</p><p>3.rtc 3.3.0@易豪杰</p>', '', '<p><br></p>', '2025-06-10 17:24:44.004096', 'admin');
INSERT INTO `notice` VALUES (8, '2025/6/9—安卓13vmos—杨霆', '<p>1.pif20250604版本<br>2.rtc3.3.0<br>3.AppStore1.0.11,launcher2.3.11,Tools1.0.33<br>4. &nbsp;第三方（抖音，淘宝）app注册页面输入不了, &nbsp; &nbsp;<br>5. &nbsp;相机注入有黑边 &nbsp; 设置比例9:16后再验证<br>6. &nbsp;cashAPP风控问题</p>', '', '<p><br></p>', '2025-06-10 17:26:04.199878', 'admin');
INSERT INTO `notice` VALUES (9, '2025/6/9—安卓14vmos—杨霆', '<p> 1.pif3绿2.cash闪退。3.camera白屏,rtc5.2.6</p>', '', '<p><br></p>', '2025-06-10 17:26:44.741243', 'admin');
INSERT INTO `notice` VALUES (10, '2025/6/16巴德—杨霆', '<p>1.新的jsclient,</p><p>2.应用列表，</p><p>3.google play store添加银行卡闪退，</p><p>4.shoop风控，</p><p>5.亚马逊音乐订阅</p><p>6.rtc2.0，增加vcjsclient_v2.0.0,gp-patch两个apk@李佳豪(李佳豪)</p>', '', '<p><br></p>', '2025-06-16 20:34:41.939538', 'admin');
INSERT INTO `notice` VALUES (11, '2025/6/16安卓14ML—柴祥', '<p><br></p><p>1.修改Phone number和SIM 状态中的手机号码不一致（现在一致）</p><p>2.修改ipv6ip和mac地址重复（开放属性接口）</p><p>😃</p><p>sim手机号码属性：persist.sys.cloud.phonenum=37622238078</p><p>ipv6地址：persist.sys.cloud.wifi.ipv6=fe80::cd7f:1cb2:3d5c:65ed</p><p>ip：persist.sys.cloud.wifi.ip=192.168.93.86</p><p>如果后端未对这三个属性传值，默认值为上@李佳豪(李佳豪)</p>', '', '<p><br></p>', '2025-06-16 20:35:25.55001', 'admin');
INSERT INTO `notice` VALUES (12, '2025/6/16姚总内置5个apk', '<p>1.修复Google play store 闪退问题</p>', '<p><img src=\"http://localhost:9090/api/manager/files/1750078359887-image.png\" alt=\"\" data-href=\"\" style=\"\"/></p>', '<p><br></p>', '2025-06-16 20:52:54.709651', 'admin');
INSERT INTO `notice` VALUES (13, '2025/6/20-大林-回退systemui崩溃，回退rtc相关Clash', '<p>1.回退取消在状态栏显示运营商</p><p>2.回退Rtc相关的Clash</p>', '<p> <a href=\"http://192.168.22.43/files/lingdian/A14/vcloud_android14_user_20250620_master_sun_4a95595.tar\" target=\"_blank\">http://192.168.22.43/files/lingdian/A14/vcloud_android14_user_20250620_master_sun_4a95595.tar</a> </p>', '<p><br></p>', '2025-06-20 16:01:40.180152', 'admin');

-- ----------------------------
-- Table structure for notice_markdown
-- ----------------------------
DROP TABLE IF EXISTS `notice_markdown`;
CREATE TABLE `notice_markdown`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `parameter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `filePath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice_markdown
-- ----------------------------
INSERT INTO `notice_markdown` VALUES (21, '[功能名称内容]', '[方法说明内容]', '[参数说明内容]', '[描述内容]', '2025-06-18T10:33:36.977551200', 'admin', '/markdown/admin/[功能名称内容]_1750214016993.md');
INSERT INTO `notice_markdown` VALUES (22, '[这是功能名称内容]', '[这是属性键名内容]', '[这是实例参数内容]', '[这是参数描述内容]', '2025-06-18T17:29:02.237284100', 'admin', '/markdown/admin/[这是功能名称内容]_1750238942258.md');

SET FOREIGN_KEY_CHECKS = 1;
