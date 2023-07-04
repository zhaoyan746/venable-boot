package com.venble.boot.system.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Table(name = "sys_menu")
@Entity
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Transient
    private String parentName;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 重定向
     */
    private String redirect;

    /**
     * 是否固定为首页
     */
    private Boolean affix;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 当前路由是否不显示在菜单中
     */
    @Column(name = "hide_menu")
    private Boolean hideMenu;

    /**
     * 当前路由激活的菜单
     */
    @Column(name = "current_active_menu")
    private String currentActiveMenu;

    /**
     * 是否使用
     */
    @Column(name = "is_use")
    private Integer isUse;

}

