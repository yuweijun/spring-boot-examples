package com.example.spring.jdbc.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * 缓存基类，CRUD的实现需要由子类实现，缓存相关的操作都封装在这个基类里。
 * <p>
 * 每个类的全名空间里有4个缓存数据：
 * 1. 对象缓存
 * 2. 列表缓存
 * 3. 列表缓存键的集合缓存
 * <p>
 * 缓存操作：
 * 1. 每个对象的create/update/delete操作会清除对象本身缓存，对象的缓存键和移除列表缓存和移除列表键缓存
 * 2. 单个对象查询后会缓存对象，同时缓存此对象的缓存键
 * 3. 集合查询结果会缓存整个集合，同时缓存此集合的缓存键，并且集合缓存键的版本自增1
 *
 * @author yuweijun 2016-09-12
 */
public abstract class CacheDaoSupport<PK extends Number, E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDaoSupport.class);

    private final String CLASS_NAME = this.getClass().getCanonicalName();

    private final String LIST_NAME_SPACE = CLASS_NAME + "#LIST_NAME_SPACE";

    private final String CLASS_NAME_COUNTER = CLASS_NAME + "#COUNTER";

    private static final int CACHE_TIME_DEFAULT = 4 * 60 * 60; // 4 hours

    private static int incrementCounter = 1; // 列表版本计数器

    /**
     * 子类中一般使用依赖注入{@link Jedis}对象。
     *
     * @return 返回Jedis对象
     */
    protected abstract Jedis getJedis();

    /**
     * @return 返回实际要代理的mybatis Mapper类
     */
    protected abstract Class<?> getMapperClass();

    /**
     * @return 返回子类的依赖注入的 {@link SqlSessionTemplate}
     */
    protected abstract SqlSessionTemplate getSqlSessionTemplate();

    /**
     * @param entity 持久化的entity对象
     * @return 返回entity的主键
     */
    protected abstract PK getPrimaryKey(E entity);

    /**
     * @return 单个对象在缓存中的有效时间
     */
    protected int getObjectExpiredSeconds() {
        return CACHE_TIME_DEFAULT;
    }

    /**
     * @return 列表在缓存中的有效时间
     */
    protected int getListExpiredSeconds() {
        return CACHE_TIME_DEFAULT;
    }

    protected final void clearCacheById(PK id) {
        String key = getKeyById(id);
        LOGGER.info("clear cache for key : {}", key);
        getJedis().del(key);

        // 清除所有列表缓存
        clearAllCacheOfNamespace();
    }

    protected final void clearAllCacheOfNamespace() {
        getJedis().incr(CLASS_NAME_COUNTER);

        // 如果对象版本有发生100次变化，则进行列表缓存清理，避免过多对象占用memcache内存
        if (incrementCounter % 100 == 0) {
            incrementCounter = 0; // 重置计数器
            // 列表的缓存键清除
            Set<String> keys = getJedis().smembers(LIST_NAME_SPACE);
            if (keys != null) {
                LOGGER.info("clear cache for list : {}", CLASS_NAME);
                for (String key : keys) {
                    LOGGER.debug("clear key of list : {}", key);

                    // 列表键缓存时删除了前缀的类名，在这里补上前缀
                    key = CLASS_NAME + key;
                    getJedis().del(key);
                }
            }

            getJedis().del(LIST_NAME_SPACE);
        }

        incrementCounter++;
    }

    protected E selectOne(String statement, Object parameter) {
        String key = getKeyByStatementAndParameter(statement, parameter);
        E entity = (E) getJedis().get(key);
        if (entity == null) {
            LOGGER.debug("select one entity from database using key : {}", key);
            statement = getMapperFullMethodName(statement);
            entity = getSqlSessionTemplate().selectOne(statement, parameter);
            if (entity != null) {
                getJedis().set(key, new Gson().toJson(entity));
                addCacheKeyToNamespace(key.replace(CLASS_NAME, ""));
            }
        } else {
            LOGGER.debug("get cache using key : {}", key);
        }
        return entity;
    }

    protected List<E> selectList(String statement, Object parameter) {
        String key = getKeyByStatementAndParameter(statement, parameter);
        List<E> entities = new Gson().fromJson(getJedis().get(key), new TypeToken<List<E>>(){}.getType());
        if (entities == null) {
            LOGGER.debug("find list from database using key: {}", key);
            statement = getMapperFullMethodName(statement);
            entities = getSqlSessionTemplate().selectList(statement, parameter);

            // 不管entities长度是否为0，都进行缓存
            getJedis().set(key, new Gson().toJson(entities));
            // 在列表键缓存空间中不需要保存类名，减少缓存大小，在删除缓存时加上类名
            addCacheKeyToNamespace(key.replace(CLASS_NAME, ""));
        } else {
            LOGGER.debug("get cache using key : {}", key);
        }
        return entities;
    }

    protected int insert(String statement, E entity) {
        if (statement == null) {
            throw new IllegalArgumentException("statement is null for " + CLASS_NAME);
        }
        if (entity == null) {
            throw new IllegalArgumentException("entity is null for " + CLASS_NAME);
        }

        PK id = getPrimaryKey(entity);
        if (id != null) {
            throw new IllegalArgumentException("primary key id not null for " + CLASS_NAME);
        }

        LOGGER.debug("insert entity into database : {}", entity);

        // 必须先保存到数据，再清缓存
        int affectedRows = getSqlSessionTemplate().insert(statement, entity);

        if (affectedRows > 0) {
            clearCacheById(id);
        }

        return affectedRows;
    }

    protected int update(String statement, E entity) {
        if (statement == null) {
            throw new IllegalArgumentException("statement is null for " + CLASS_NAME);
        }
        if (entity == null) {
            throw new IllegalArgumentException("entity is null for " + CLASS_NAME);
        }

        PK id = getPrimaryKey(entity);
        if (id == null) {
            throw new IllegalArgumentException("primary key id is null for " + CLASS_NAME);
        }

        LOGGER.debug("update entity into database : {}", entity);

        // 必须先保存到数据，再清缓存
        int affectedRows = getSqlSessionTemplate().update(statement, entity);

        if (affectedRows > 0) {
            clearCacheById(id);
        }

        return affectedRows;
    }

    protected int delete(String statement, PK id) {
        if (statement == null) {
            throw new IllegalArgumentException("statement is null for " + CLASS_NAME);
        }
        if (id == null) {
            throw new IllegalArgumentException("primary key id is null for " + CLASS_NAME);
        }

        LOGGER.info("delete entity from database : {}", id);

        int affectedRows = getSqlSessionTemplate().delete(statement, id);

        if (affectedRows > 0) {
            clearCacheById(id);
        }

        return affectedRows;
    }

    private String getKeyById(PK id) {
        return CLASS_NAME + "#ID#" + id;
    }

    private String getKeyByStatementAndParameter(String statement, Object parameter) {
        String version = getJedis().get(CLASS_NAME_COUNTER);
        return CLASS_NAME + "#VERSION#" + version + "#" + statement + "#" + parameter;
    }

    private String getMapperFullMethodName(String method) {
        if (!method.contains(".")) {
            method = getMapperClass().getCanonicalName() + "." + method;
        }

        LOGGER.debug("run mapper statement : {}", method);
        return method;
    }

    private void addCacheKeyToNamespace(String key) {
        boolean ismember = getJedis().sismember(LIST_NAME_SPACE, key);

        // 只有当缓存键集合中不存在时，才会进行保存操作
        if (!ismember) {
            getJedis().sadd(LIST_NAME_SPACE, key);
        }
    }

}


