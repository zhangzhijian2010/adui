package com.kuyun.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;

/**
 * 功能：通用Hibernate DAO
 * 
 * @author zhijian.zhang
 * 
 * @param <T>
 *            实体对象类名
 * @param <ID>
 *            实体ID的类名
 */
public class GenericHibernateDAO<T, ID extends Serializable> {
    private static final Logger logger = Logger.getLogger(GenericHibernateDAO.class);
    @Resource
    private SessionFactory sessionFactory;

    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public GenericHibernateDAO() {
        this.persistentClass = GenericsUtils.getSuperClassGenricType(getClass());
        if (this.persistentClass == null) {
            if (logger.isTraceEnabled()) {
                logger
                    .warn("Could not determine the generics type - you will need to set manually");
            }
        }
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public String getPersistentClassName() {
        return persistentClass.getName();
    }

    /**
     * 
     * 功能：根据ID获取特定记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param id
     *            主键ID的value值
     * @return
     */
    @SuppressWarnings("unchecked")
    public T get(ID id) {
        return (T) sessionFactory.getCurrentSession().get(persistentClass, id);
    }

    /**
     * 
     * 功能：检索所有记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @return
     */
    public List<T> findAll() {
        return findByCriteria();
    }

    /**
     * 
     * 功能：保存记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param entity
     * @return
     */
    public T save(T entity) {
        sessionFactory.getCurrentSession().save(entity);
        return entity;
    }

    /**
     * 
     * 功能：保存或修改记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param entity
     * @return
     */
    public T saveOrUpdate(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    /**
     * 
     * 功能：保存或修改所有集合类的记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param entities
     */
    public void saveOrUpdateAll(Collection<T> entities) {
        for (T t : entities) {
            sessionFactory.getCurrentSession().saveOrUpdate(t);
        }
    }

    /**
     * 
     * 功能：修改指定记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param entity
     * @return
     */
    public T update(T entity) {
        sessionFactory.getCurrentSession().update(entity);
        return entity;
    }

    /**
     * 
     * 功能：删除指定记录 必须是持久对象（即ID不能为空）
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param entity
     */
    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    /**
     * 
     * 功能：删除集合内所有的记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param entities
     */
    @SuppressWarnings("rawtypes")
    public void delete(List entities) {
        for (Object object : entities) {
            sessionFactory.getCurrentSession().delete(object);
        }
    }

    /**
     * 
     * 功能：删除ID值为id的记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param id
     */
    public void delete(ID id) {
        T entity = get(id);
        if (null != entity) {
            sessionFactory.getCurrentSession().delete(entity);
        }
    }

    /**
     * 
     * 功能：根据指定条件删除记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            HQL 例如：delete table where id=:id and name=:name
     * @param conditionMap
     *            key指的是HQL中冒号后的字符 例如上面的:id中的id value为限制条件
     */
    @SuppressWarnings("rawtypes")
    public void delete(final String queryString, final Map conditionMap) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        if (null != conditionMap)
            query.setProperties(conditionMap);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(persistentClass);
        criteria.add(Example.create(exampleInstance));
        return criteria.list();
    }

    /**
     * 
     * 功能：根据实体中的条件得到一个新的实体
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param exampleInstance
     * @return
     */
    public T get(T exampleInstance) {
        List<T> list = findByExample(exampleInstance);
        return (null != list && list.size() > 0) ? list.get(0) : null;
    }

    /**
     * 
     * 功能：根据实体中的指定的条件获取相应的记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param exampleInstance
     *            实体对象
     * @param excludeProperty
     *            该字符串数组主要指排除exampleInstance的实体属性值 例如： exampleInstance
     *            有三个属性id、name、age 如果excludeProperty为{name,age}
     *            则忽视这两个条件，只限制id这个条件
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    public List<T> findByExample(final T exampleInstance, final String[] excludeProperty) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(persistentClass);
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        return criteria.list();
    }

    /**
     * 
     * 功能：
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param begin
     *            开始行
     * @param length
     *            取的行数
     * @return
     */
    public List<?> find(int begin, int length) {
        return findByCriteria(begin, length);
    }

    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    public void clear() {
        sessionFactory.getCurrentSession().clear();
    }

    /**
     * 
     * 功能：创建与会话无关的检索标准
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     *
     * @return
     */
    public DetachedCriteria createDetachedCriteria() {
        return DetachedCriteria.forClass(this.persistentClass);
    }

    /**
     * 
     * 功能：根据criteria查询结果集
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     *
     * @param criteria
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(DetachedCriteria criteria) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria2 = criteria.getExecutableCriteria(session);
        return criteria2.list();
    }

    public List<?> findBycriteria(DetachedCriteria criteria, T exampleInstance) {
        Example example = Example.create(exampleInstance);
        criteria.add(example);
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria2 = criteria.getExecutableCriteria(session);
        return criteria2.list();
    }

    /**
     * 
     * 功能： 检索满足标准的数据，返回指定范围的记录 （分页）
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     *
     * @param criteria 
     * @param firstResult 起始记录
     * @param maxResults 最大记录数
     * @return
     */
    public List<?> findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
        Session session = sessionFactory.getCurrentSession();
        return criteria.getExecutableCriteria(session).setFirstResult(firstResult)
            .setMaxResults(maxResults).list();
    }

    /**
     * 
     * 功能：检索指定范围内的记录
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     *
     * @param firstResult 起始记录
     * @param maxResults 最大记录数
     * @return
     */
    public List<?> findByCriteria(int firstResult, int maxResults) {
        return this.findByCriteria(this.createDetachedCriteria(), firstResult, maxResults);
    }

    /**
     * 
     * 功能：获取所有的实体记录
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(persistentClass);
        return criteria.list();
    }

    /**
     * 
     * 功能：使用指定的检索标准获取满足标准的记录数
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     *
     * @param criteria
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Integer getRowCount(DetachedCriteria criteria) {
        criteria.setProjection(Projections.rowCount());
        List list = this.findByCriteria(criteria, 0, 1);
        return (Integer) list.get(0);
    }

    /**
     * 
     * 功能：使用指定的检索标准获取满足标准的记录数
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     *
     * @param criteria
     * @return
     */
    public Integer getCountByCriteria(final DetachedCriteria detachedCriteria) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }

    /**
     * 
     * 功能：使用指定的检索标准获取满足标准的记录数
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     *
     * @param criteria
     * @return
     */
    public Integer getCountByExample(final T exampleInstance) {
        DetachedCriteria detachedCriteria = createDetachedCriteria();
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        Example example = Example.create(exampleInstance);
        criteria.add(example);
        return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }

    /**
     * 功能：使用带命名的参数的HSQL语句检索数据
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            select user from User user where user.id=:id2 and
     *            user.name=:name
     * @param paramNames
     * @param values
     * @return
     */
    public List<?> findByNamedParam(String queryString, String[] paramNames, Object[] values) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        for (int i = 0; i < paramNames.length; i++) {
            query.setParameter(paramNames[i], values[i]);
        }
        return query.list();
    }

    /**
     * 
     * 功能：使用HSQL语句检索数据，返回 Iterator
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     * @return
     */
    public Iterator<?> iterate(String queryString) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        return query.iterate();
    }

    /**
     * 
     * 功能：使用带参数HSQL语句检索数据，返回 Iterator
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     * @param values
     * @return
     */
    public Iterator<?> iterate(String queryString, Object[] values) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query.iterate();
    }

    /**
     * 
     * 功能：使用HQL语句及条件MAP获取特定一条记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            HQL语句 例如 SELECT user from User as user where user.name=:name
     * @param conditionMap
     *            key指的是HQL中冒号后的字符 例如上面的:id中的id value为限制条件
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> get(final String queryString, final Map<String, Object> conditionMap) {
        List<Map<String, Object>> list = findByMap(-1, -1, queryString, conditionMap);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 
     * 功能：使用HQL语句及带参数的条件检索特定一条记录
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            例如 SELECT user from User as user where user.name=?
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String queryString, Object... values) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) find(queryString, values);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 
     * 功能：使用HQL语句检索数据
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     * @return
     */
    public List<?> find(String queryString) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        return query.list();
    }

    /**
     * 
     * 功能： 使用带参数的HQL语句检索数据
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            例如:from User where name=? and age=?
     * @param values
     *            例如 new Object[]{"aaa","bbb"}
     * @return
     */
    public List<?> find(String queryString, Object... values) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query.list();
    }

    /**
     * 
     * 功能：通过HQL语句检索数据（分页）
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param begin
     *            开始行
     * @param length
     *            取的行数
     * @param queryString
     *            HQL语句
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List find(final int begin, final int length, final String queryString) {
        return find(begin, length, queryString, null, null);
    }

    /**
     * 
     * 功能： 通过HQL语句得到表中记录（全部，不分页）
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            HQL语句 例如 SELECT user from User as user where user.name=:name
     * @param conditionMap
     *            key指的是HQL中冒号后的字符 例如上面的:id中的id value为限制条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findByMap(final String queryString, final Map<String, Object> conditionMap) {
        return find(-1, -1, queryString, null, conditionMap);
    }

    /**
     * 
     * 功能：通过HQL语句得到表中记录(分页)
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param begin
     *            开始行
     * @param length
     *            取的行数
     * @param queryString
     *            HQL语句 例如 SELECT user from User as user where user.name=:name
     * @param conditionMap
     *            key指的是HQL中冒号后的字符 例如上面的:id中的id value为限制条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findByMap(final int begin, final int length, final String queryString,
                          final Map<String, Object> conditionMap) {
        return find(begin, length, queryString, null, conditionMap);
    }

    /**
     * 通过HQL语句得到表中记录，begin为开始行，length为取的行数
     * 
     * @param begin
     *            开始行
     * @param length
     *            取的行数
     * @param queryString
     *            HQL语句 例如 SELECT user from User as user where user.name=:name
     * @param conditionObject
     *            一个对象 例如 User 则其属性name 为HQL语句：后的名称，其值为所需的限制条件值
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findByBean(final int begin, final int length, final String queryString,
                           final Object conditionObject) {
        return find(begin, length, queryString, conditionObject, null);
    }

    /**
     * 
     * 功能：
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            HQL语句 "from bean.User u where u.name=:name"
     * @param t
     *            实体BEAN对象 User user = new User() user.setName("dhcc")
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findByBean(String queryString, T t) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        query.setProperties(t);
        return query.list();
    }

    /**
     * 通过HQL语句得到表中记录
     * 
     * @param begin
     *            开始行
     * @param length
     *            取的行数
     * @param queryString
     *            HQL语句 "from bean.User u where u.name=:name"
     * @param conditionObject
     *            个对象 例如 User 则其属性name 为HQL语句：后的名称，其值为所需的限制条件值
     * @param conditionMap
     *            key指的是HQL中冒号后的字符 例如上面的:id中的id value为限制条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List find(final int begin, final int length, final String queryString,
                     final Object conditionObject, final Map<String, Object> conditionMap) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        if (begin > -1) {
            query.setFirstResult(begin);
        }
        if (length > -1) {
            query.setMaxResults(length);
        }
        if (null != conditionObject) {
            query.setProperties(conditionObject);
        }
        if (null != conditionMap) {
            query.setProperties(conditionMap);
        }
        return query.list();
    }

    /**
     * 
     * 功能：使用HQL语句查询记录总数
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            HQL语句 "from bean.User u where u.name=:name"
     * @param conditionMap
     *            key指的是HQL中冒号后的字符 例如上面的:id中的id value为限制条件
     * @return
     */
    public int count(final String queryString, final Map<String, Object> conditionMap) {
        return count(queryString, null, conditionMap);
    }

    /**
     * 
     * 功能：使用HQL语句查询记录总数
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            HQL语句 "from bean.User u where u.name=:name"
     * @param conditionObject
     *            一个对象 例如 User 则其属性name 为HQL语句：后的名称，其值为所需的限制条件值
     * @return
     */
    public int count(final String queryString, final Object conditionObject) {
        return count(queryString, conditionObject, null);
    }

    /**
     * 
     * 功能：使用HQL语句查询记录总数
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            HQL语句
     * @return
     */
    public int count(final String queryString) {
        return count(queryString, null, null);
    }

    @SuppressWarnings("rawtypes")
    public int count(final String queryString, final Object conditionObject,
                     final Map<String, Object> conditionMap) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        if (null != conditionObject) {
            query.setProperties(conditionObject);
        }
        if (null != conditionMap) {
            query.setProperties(conditionMap);
        }
        List list = query.list();
        return ((Number) list.get(0)).intValue();
    }

    /**
     * 
     * 功能：更新实体 可用于添加、修改、删除操作
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param hql
     *            更新的HQL语句
     * @param params
     *            参数,可有项目或多项目,代替Hql中的"?"号
     * @return
     */
    public int update(final String hql, final Object[] params) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        return query.executeUpdate();
    }

    /**
     * 
     * 功能：更新实体 可用于添加、修改、删除操作
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param hql
     *            更新的HQL语句
     * @param conditionMap
     *            参数,key指的是HQL中冒号后的字符 例如上面的:id中的id value为限制条件
     * @return
     */
    public int update(final String queryString, final Map<String, Object> conditionMap) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryString);
        query.setProperties(conditionMap);
        return query.executeUpdate();
    }

    /**
     * 
     * 功能：更新实体操作 upadte Table set aaa=:aaa ,bbb=:bbb where ccc=:ccc and ddd=:ddd
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param key
     *            set后中的列名
     * @param value
     *            set后中的列值
     * @param whereKey
     *            where后中的列名
     * @param whereValue
     *            where后中的列值
     * @return
     */
    public int update(String[] key, Object[] value, String[] whereKey, Object[] whereValue) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("update ").append(getPersistentClassName()).append(" set ");
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < key.length; i++) {
            buffer.append(key[i]).append("=:").append(key[i]).append(",");
            map.put(key[i], value[i]);
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(" ").append("where ");
        for (int i = 0; i < whereKey.length; i++) {
            buffer.append(whereKey[i]).append("=:").append(whereKey[i]).append(" and ");
            map.put(whereKey[i], whereValue[i]);
        }
        buffer.delete(buffer.length() - 5, buffer.length());
        return update(buffer.toString(), map);
    }

    /**
     * 
     * 功能：使用HQL语句及相应的条件检索数据
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param begin
     *            开始行
     * @param length
     *            取的行数
     * @param queryString
     *            Sql语句 
     * @param conditionObject
     *            个对象 例如 User 则其属性name 为SQL语句：后的名称，其值为所需的限制条件值
     * @param conditionMap
     *            key指的是Sql中冒号后的字符 例如上面的:id中的id value为限制条件
     * @return 返回List
     */
    @SuppressWarnings("rawtypes")
    public List findBySQL(final int begin, final int length, final String queryString,
                          final Object conditionObject, final Map<String, Object> conditionMap) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(queryString);
        if (begin > -1) {
            query.setFirstResult(begin);
        }
        if (length > -1) {
            query.setMaxResults(length);
        }
        if (null != conditionObject) {
            query.setProperties(conditionObject);
        }
        if (null != conditionMap) {
            query.setProperties(conditionMap);
        }
        return query.list();
    }

    /**
     * 
     * 功能：使用HQL语句及相应的条件获取记录数
     * 
     * @author: zhijian.zhang
     * @Date: 2011-6-15
     * 
     * @param queryString
     *            Sql语句 "select count(*) from table"
     * @param conditionObject
     *            个对象 例如 User 则其属性name 为HQL语句：后的名称，其值为所需的限制条件值
     * @param conditionMap
     *            key指的是HQL中冒号后的字符 例如上面的:id中的id value为限制条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    public int countBySQL(final String queryString, final Object conditionObject,
                          final Map<String, Object> conditionMap) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(queryString);
        if (null != conditionObject) {
            query.setProperties(conditionObject);
        }
        if (null != conditionMap) {
            query.setProperties(conditionMap);
        }
        List list = query.list();
        return ((Number) list.get(0)).intValue();
    }
}
