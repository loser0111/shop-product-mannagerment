-- ========================================================
-- 电商商品管理系统数据库初始化脚本
-- 数据库: shop_product
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_unicode_ci
-- ========================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS shop_product 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE shop_product;

-- ========================================================
-- 1. 商品表 (t_product)
-- ========================================================
DROP TABLE IF EXISTS t_product;
CREATE TABLE t_product (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    category_id BIGINT UNSIGNED NOT NULL COMMENT '分类ID',
    brand_id BIGINT UNSIGNED NOT NULL COMMENT '品牌ID',
    supplier_id BIGINT UNSIGNED DEFAULT NULL COMMENT '供应商ID',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-草稿，1-上架，2-下架，9-删除',
    main_image VARCHAR(500) DEFAULT NULL COMMENT '主图URL',
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '售价',
    market_price DECIMAL(10, 2) DEFAULT NULL COMMENT '市场价',
    description VARCHAR(2000) DEFAULT NULL COMMENT '商品描述',
    detail_html TEXT COMMENT '商品详情HTML',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    PRIMARY KEY (id),
    INDEX idx_category_id (category_id),
    INDEX idx_brand_id (brand_id),
    INDEX idx_supplier_id (supplier_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    UNIQUE KEY uk_category_name (category_id, name, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- ========================================================
-- 2. 商品图片表 (t_product_image)
-- ========================================================
DROP TABLE IF EXISTS t_product_image;
CREATE TABLE t_product_image (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '图片ID',
    product_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    url VARCHAR(500) NOT NULL COMMENT '图片URL',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    is_main TINYINT NOT NULL DEFAULT 0 COMMENT '是否主图：0-否，1-是',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_product_id (product_id),
    INDEX idx_is_main (is_main)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片表';

-- ========================================================
-- 3. SKU表 (t_sku)
-- ========================================================
DROP TABLE IF EXISTS t_sku;
CREATE TABLE t_sku (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
    product_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    sku_code VARCHAR(100) NOT NULL COMMENT 'SKU编码',
    barcode VARCHAR(100) DEFAULT NULL COMMENT '条形码',
    attributes_json JSON COMMENT '属性组合JSON',
    attribute_signature VARCHAR(500) DEFAULT NULL COMMENT '属性签名（用于去重）',
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '售价',
    cost_price DECIMAL(10, 2) DEFAULT NULL COMMENT '成本价',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sku_code (sku_code, deleted),
    INDEX idx_product_id (product_id),
    INDEX idx_barcode (barcode),
    INDEX idx_attribute_signature (attribute_signature),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SKU表';

-- ========================================================
-- 4. 库存表 (t_inventory)
-- ========================================================
DROP TABLE IF EXISTS t_inventory;
CREATE TABLE t_inventory (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '库存ID',
    sku_id BIGINT UNSIGNED NOT NULL COMMENT 'SKU ID',
    warehouse_id BIGINT UNSIGNED NOT NULL COMMENT '仓库ID',
    available_stock DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '可用库存',
    locked_stock DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '锁定库存',
    warning_threshold DECIMAL(10, 2) DEFAULT 0.00 COMMENT '预警阈值',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sku_warehouse (sku_id, warehouse_id),
    INDEX idx_sku_id (sku_id),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_warning (available_stock, warning_threshold)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存表';

-- ========================================================
-- 5. 库存锁定表 (t_stock_lock)
-- ========================================================
DROP TABLE IF EXISTS t_stock_lock;
CREATE TABLE t_stock_lock (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '锁定ID',
    lock_id VARCHAR(64) NOT NULL COMMENT '锁定编号',
    order_id VARCHAR(64) NOT NULL COMMENT '订单ID',
    sku_id BIGINT UNSIGNED NOT NULL COMMENT 'SKU ID',
    warehouse_id BIGINT UNSIGNED NOT NULL COMMENT '仓库ID',
    quantity DECIMAL(10, 2) NOT NULL COMMENT '锁定数量',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-已锁定，2-已释放，3-已确认，4-已过期',
    expire_time DATETIME NOT NULL COMMENT '过期时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_lock_id (lock_id),
    INDEX idx_order_id (order_id),
    INDEX idx_sku_id (sku_id),
    INDEX idx_status (status),
    INDEX idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存锁定表';

-- ========================================================
-- 6. 分类表 (t_category)
-- ========================================================
DROP TABLE IF EXISTS t_category;
CREATE TABLE t_category (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT UNSIGNED DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    level TINYINT NOT NULL DEFAULT 1 COMMENT '层级',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    icon VARCHAR(500) DEFAULT NULL COMMENT '图标URL',
    attributes_schema JSON COMMENT '属性模板JSON',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_level (level),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- ========================================================
-- 7. 品牌表 (t_brand)
-- ========================================================
DROP TABLE IF EXISTS t_brand;
CREATE TABLE t_brand (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
    name VARCHAR(100) NOT NULL COMMENT '品牌名称',
    logo VARCHAR(500) DEFAULT NULL COMMENT 'Logo URL',
    description VARCHAR(1000) DEFAULT NULL COMMENT '品牌描述',
    website VARCHAR(200) DEFAULT NULL COMMENT '官网',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='品牌表';

-- ========================================================
-- 8. 供应商表 (t_supplier)
-- ========================================================
DROP TABLE IF EXISTS t_supplier;
CREATE TABLE t_supplier (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
    name VARCHAR(200) NOT NULL COMMENT '供应商名称',
    contact_name VARCHAR(100) DEFAULT NULL COMMENT '联系人',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    address VARCHAR(500) DEFAULT NULL COMMENT '地址',
    tax_id VARCHAR(50) DEFAULT NULL COMMENT '税号',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商表';

-- ========================================================
-- 9. 仓库表 (t_warehouse)
-- ========================================================
DROP TABLE IF EXISTS t_warehouse;
CREATE TABLE t_warehouse (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
    name VARCHAR(100) NOT NULL COMMENT '仓库名称',
    code VARCHAR(50) NOT NULL COMMENT '仓库编码',
    region VARCHAR(100) DEFAULT NULL COMMENT '所属区域',
    address VARCHAR(500) DEFAULT NULL COMMENT '详细地址',
    contact_name VARCHAR(100) DEFAULT NULL COMMENT '联系人',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code),
    INDEX idx_region (region),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库表';

-- ========================================================
-- 10. 库存变更日志表 (t_inventory_log)
-- ========================================================
DROP TABLE IF EXISTS t_inventory_log;
CREATE TABLE t_inventory_log (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    sku_id BIGINT UNSIGNED NOT NULL COMMENT 'SKU ID',
    warehouse_id BIGINT UNSIGNED NOT NULL COMMENT '仓库ID',
    change_type TINYINT NOT NULL COMMENT '变更类型：1-增加，2-减少，3-锁定，4-释放，5-扣减',
    quantity DECIMAL(10, 2) NOT NULL COMMENT '变更数量',
    before_stock DECIMAL(10, 2) NOT NULL COMMENT '变更前库存',
    after_stock DECIMAL(10, 2) NOT NULL COMMENT '变更后库存',
    biz_type VARCHAR(50) DEFAULT NULL COMMENT '业务类型',
    biz_id VARCHAR(64) DEFAULT NULL COMMENT '业务单号',
    operator_id BIGINT UNSIGNED DEFAULT NULL COMMENT '操作人ID',
    operator_name VARCHAR(100) DEFAULT NULL COMMENT '操作人姓名',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_sku_id (sku_id),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_change_type (change_type),
    INDEX idx_biz_id (biz_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存变更日志表';

-- ========================================================
-- 初始化数据
-- ========================================================

-- 插入默认仓库
INSERT INTO t_warehouse (name, code, region, address, status) VALUES
('默认仓库', 'DEFAULT', '全国', '默认地址', 1),
('北京仓库', 'BJ001', '华北', '北京市朝阳区', 1),
('上海仓库', 'SH001', '华东', '上海市浦东新区', 1),
('广州仓库', 'GZ001', '华南', '广州市天河区', 1);

-- 插入示例分类
INSERT INTO t_category (name, parent_id, level, sort_order, status) VALUES
('电子产品', 0, 1, 1, 1),
('手机', 1, 2, 1, 1),
('电脑', 1, 2, 2, 1),
('服装', 0, 1, 2, 1),
('男装', 4, 2, 1, 1),
('女装', 4, 2, 2, 1);

-- 插入示例品牌
INSERT INTO t_brand (name, logo, description, status) VALUES
('Apple', 'https://example.com/apple.png', '苹果公司', 1),
('华为', 'https://example.com/huawei.png', '华为技术有限公司', 1),
('小米', 'https://example.com/xiaomi.png', '小米科技', 1),
('Nike', 'https://example.com/nike.png', '耐克', 1),
('Adidas', 'https://example.com/adidas.png', '阿迪达斯', 1);

-- 插入示例供应商
INSERT INTO t_supplier (name, contact_name, phone, email, status) VALUES
('Apple中国', '张三', '13800138000', 'apple@example.com', 1),
('华为供应链', '李四', '13900139000', 'huawei@example.com', 1),
('小米供应链', '王五', '13700137000', 'xiaomi@example.com', 1);

-- ========================================================
-- 完成
-- ========================================================
SELECT 'Database initialization completed successfully!' AS result;
