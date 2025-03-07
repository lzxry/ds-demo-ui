package com.example.ds.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接单者用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ds_worker_user")
public class WorkerUser extends BaseEntity {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 项目唯一码
     */
    private String projectCode;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 身份证号
     */
    private String idCard;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;
    
    /**
     * 接单等级（1-5）
     */
    private Integer level;
    
    /**
     * 完成订单数
     */
    private Integer completedOrders;
    
    /**
     * 好评率
     */
    private Double goodRate;
    
    /**
     * API Key
     */
    private String apiKey;
    
    /**
     * API Secret
     */
    private String apiSecret;
    
    /**
     * 备注
     */
    private String remark;
} 