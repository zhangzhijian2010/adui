/**
 * tenfen.com Inc.
 * Copyright (c) 2012-2015 All Rights Reserved.
 */
package com.kuyun.common.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * MongoTemplate 抽象业务类
 * <pre>
 * 
 * </pre>
 *
 * @author zhang.zhijian@kuyun.com
 * @version $Id: AbstractBaseMongoTemplete.java, v 0.1 2013-7-18 下午12:53:39 zhangzhijian Exp $
 */
public abstract class AbstractBaseMongoTemplete<T> {

    @Resource(name = "mongoTemplate")
    protected MongoTemplate mongoTemplate;

    @Resource(name = "safe_mongoTemplate")
    protected MongoTemplate safeMongoTemplate;

    /**
     * 通过条件查询实体(集合)
     * 
     * @param query
     */
    public List<T> find(Query query) {
        return mongoTemplate.find(query, this.getEntityClass());
    }

    public List<T> find(Query query, String collectionName) {
        return mongoTemplate.find(query, this.getEntityClass(), collectionName);
    }

    /**
     * 通过一定的条件查询一个实体
     * 
     * @param query
     * @return
     */
    public T findOne(Query query) {
        return mongoTemplate.findOne(query, this.getEntityClass());
    }

    /**
     * 通过条件查询更新数据
     * 
     * @param query
     * @param update
     * @return
     */
    public void update(Query query, Update update) {
        safeMongoTemplate.upsert(query, update, this.getEntityClass());
    }

    /**
     * 保存一个对象到mongodb
     * 
     * @param bean
     * @return
     */
    public T save(T bean) {
        safeMongoTemplate.save(bean);
        return bean;
    }

    public T add(T bean) {
        safeMongoTemplate.insert(bean);
        return bean;
    }
    
    public T add(T bean,String collectionName) {
        safeMongoTemplate.insert(bean, collectionName);
        return bean;
    }

    /**
     * 通过ID获取记录
     * 
     * @param id
     * @return
     */
    public T get(String id) {
        return safeMongoTemplate.findById(id, this.getEntityClass());
    }

    /**
     * 获取需要操作的实体类class
     * 
     * @return
     */
    protected abstract Class<T> getEntityClass();
}
