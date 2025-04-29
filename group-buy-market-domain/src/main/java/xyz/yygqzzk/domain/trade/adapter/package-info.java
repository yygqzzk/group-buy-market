/**
 * 外部接口适配器层；当需要调用外部接口时，则创建出这一层，并定义接口，之后由基础设施层的 adapter 层具体实现
 * 操作数据库 -> repository, 调用外部端口 -> port
 */
package xyz.yygqzzk.domain.trade.adapter;