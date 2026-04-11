# 电商商品管理系统

> 基于DDD领域驱动设计的电商商品管理系统，采用Spring Boot 3.x + MyBatis-Plus + Dubbo技术栈

## 项目简介

本项目是一个完整的电商商品管理系统，实现了商品全生命周期管理，包括商品创建、SKU管理、库存管理、分类管理、品牌管理、供应商管理等核心功能。

## 技术架构

### 核心技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.0 | 基础框架 |
| MyBatis-Plus | 3.5.5 | ORM框架 |
| Dubbo | 3.2.10 | RPC框架 |
| Redisson | 3.24.3 | Redis客户端 |
| RocketMQ | 2.2.3 | 消息队列 |
| MySQL | 8.0+ | 数据库 |
| Seata | 1.8.0 | 分布式事务 |

### 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                        前端应用层                            │
└─────────────────────────────┬───────────────────────────────┘
                              │
                       ┌──────▼──────┐
                       │  API网关    │
                       └──────┬──────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
 ┌──────▼──────┐      ┌───────▼───────┐     ┌──────▼──────┐
 │  商品服务    │      │   库存服务     │     │   订单服务   │
 │  (Product)  │      │  (Inventory)  │     │   (Order)   │
 └─────────────┘      └───────────────┘     └─────────────┘
```

## 项目结构

```
shop-product-management/
├── shop-product-domain/          # 领域层
│   ├── entity/                   # 实体
│   ├── valueobject/              # 值对象
│   ├── event/                    # 领域事件
│   ├── repository/               # 仓储接口
│   └── service/                  # 领域服务
├── shop-product-application/     # 应用层
│   ├── service/                  # 应用服务
│   ├── dto/                      # 数据传输对象
│   ├── command/                  # 命令对象
│   └── query/                    # 查询对象
├── shop-product-infrastructure/  # 基础设施层
│   ├── persistence/              # 持久化
│   │   ├── entity/               # 数据库实体
│   │   ├── mapper/               # MyBatis Mapper
│   │   └── repository/           # 仓储实现
│   ├── config/                   # 配置类
│   └── event/                    # 事件发布
├── shop-product-interface/       # 接口层
│   ├── http/controller/          # HTTP控制器
│   ├── http/request/             # 请求对象
│   ├── http/response/            # 响应对象
│   └── rpc/                      # RPC服务
└── shop-product-start/           # 启动模块
    └── ProductApplication.java   # 启动类
```

## 核心功能

### 1. 商品管理
- ✅ 商品创建、编辑、删除
- ✅ 商品上下架控制
- ✅ 商品图片管理
- ✅ 商品分类、品牌关联

### 2. SKU管理
- ✅ 多维度SKU生成
- ✅ SKU属性组合管理
- ✅ SKU编码唯一性校验
- ✅ SKU启用/禁用

### 3. 库存管理
- ✅ 多仓库存管理
- ✅ 库存锁定/释放（订单预占）
- ✅ 库存预警机制
- ✅ 乐观锁并发控制

### 4. 分类管理
- ✅ 无限级分类树
- ✅ 分类属性模板
- ✅ 分类排序

### 5. 品牌管理
- ✅ 品牌CRUD
- ✅ 品牌Logo管理

### 6. 供应商管理
- ✅ 供应商信息管理
- ✅ 供应商商品关联

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7.0+
- RocketMQ 5.0+

### 数据库初始化

```bash
# 执行数据库初始化脚本
mysql -u root -p < doc/init.sql
```

### 编译打包

```bash
# 编译整个项目
mvn clean package -DskipTests

# 运行启动模块
cd shop-product-start
cd shop-product-start
mvn spring-boot:run
```

### 访问接口文档

启动后访问 Swagger UI：
```
http://localhost:8080/swagger-ui.html
```

## 核心领域模型

### 聚合根

1. **Product（商品）**
   - 商品基础信息管理
   - 生命周期控制（草稿/上架/下架）
   - 关联SKU、分类、品牌

2. **Sku（SKU）**
   - 规格属性组合
   - 唯一编码管理
   - 价格管理

3. **Inventory（库存）**
   - 多仓库存管理
   - 库存锁定机制
   - 预警阈值设置

### 领域事件

- `ProductCreatedEvent` - 商品创建事件
- `ProductStatusChangedEvent` - 商品状态变更事件
- `SkuCreatedEvent` - SKU创建事件
- `InventoryChangedEvent` - 库存变更事件
- `StockLockedEvent` - 库存锁定事件
- `LowStockWarningEvent` - 低库存预警事件

## API接口

### HTTP接口

#### 商品管理

```http
POST   /api/v1/products              # 创建商品
GET    /api/v1/products/{id}         # 查询商品详情
GET    /api/v1/products              # 查询商品列表
PUT    /api/v1/products/{id}         # 更新商品
DELETE /api/v1/products/{id}         # 删除商品
POST   /api/v1/products/{id}/on-shelf   # 商品上架
POST   /api/v1/products/{id}/off-shelf  # 商品下架
```

#### SKU管理

```http
POST /api/v1/products/{id}/skus      # 创建SKU
GET  /api/v1/products/{id}/skus      # 查询SKU列表
PUT  /api/v1/skus/{id}               # 更新SKU
```

#### 库存管理

```http
GET    /api/v1/skus/{id}/inventory                    # 查询库存
POST   /api/v1/skus/{id}/inventory/increase           # 增加库存
POST   /api/v1/skus/{id}/inventory/decrease           # 减少库存
PUT    /api/v1/skus/{id}/inventory/warning-threshold  # 设置预警阈值
```

### RPC接口（Dubbo）

```java
public interface ProductRpcService {
    ProductDTO getProductById(Long productId);
    SkuDTO getSkuById(Long skuId);
    BigDecimal getSkuStock(Long skuId);
    String lockStock(String orderId, Long skuId, BigDecimal quantity);
    Boolean releaseStock(String lockId);
    Boolean isProductOnShelf(Long productId);
}
```

## 文档目录

- [领域设计文档](doc/领域设计文档.md) - DDD领域模型设计
- [技术选型对比](doc/技术选型对比.md) - 技术栈选型说明
- [系统交互图](doc/系统交互图.md) - 时序图和架构图
- [代码审查报告](doc/代码审查报告.md) - 代码质量评估
- [数据库设计](doc/init.sql) - 数据库初始化脚本

## 待办事项

### 高优先级
- [ ] 完善PO和Domain之间的转换逻辑
- [ ] 实现全局异常处理器
- [ ] 添加单元测试
- [ ] 实现Redis缓存
- [ ] 集成RocketMQ消息队列

### 中优先级
- [ ] 实现SKU属性组合生成算法
- [ ] 实现多仓库存分配策略
- [ ] 添加数据库索引
- [ ] 实现批量导入导出功能
- [ ] 集成Sentinel限流降级

### 低优先级
- [ ] 完善Swagger文档
- [ ] 添加操作日志记录
- [ ] 实现数据权限控制
- [ ] 添加接口响应时间监控

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/xxx`)
3. 提交更改 (`git commit -am 'Add some feature'`)
4. 推送到分支 (`git push origin feature/xxx`)
5. 创建 Pull Request

## 许可证

[Apache License 2.0](LICENSE)

## 联系方式

- 邮箱: developer@example.com
- 项目主页: https://github.com/example/shop-product-management

---

**注意**: 本项目为示例项目，部分功能尚未完全实现，标记为 `TODO` 的代码需要进一步完善。
