package com.sinosoft.common;

import java.io.Serializable;
import java.util.List;


public interface Dao {
	public abstract Serializable create(Object obj);

	public abstract void update(Object obj);

	public abstract void delete(Object obj);

    public abstract Object load(Class<?> entityClass, Serializable id);

    public abstract Object get(String className, Serializable id);

    public abstract Object get(Class<?> entityClass, Serializable id);
    
	public abstract List<?> query(String hql);
	
	public abstract List<?> queryAll(Class<?> clazz);

	public abstract List<?> queryWithJDBC(String sql);

    public abstract List<?> queryWithJDBC(String sql,Class<?> elementType);

	public abstract void excute(String sql);

	public abstract void refresh(Object object);
	
	public abstract void evict(Object entity);
	
	public abstract void flush();
	
	public abstract int update(String namedSql, Object javaBean);
	
	public abstract int commonUpdate(String sql, Object... paramValue);
	
	public abstract Object getJavaBean(String sql, Class<?> returnType,Object... paramValue); 
	
	public abstract List<?> getList(String sql, Class<?> returnType,Object... paramValue);
	
	public abstract List<?> getList(String sql, Class<?> returnType);
	
	public abstract List<?> getList(String sql, Object... paramValue);
	
	public abstract int getCount(String countSQL, List<?> paramValue);
	
	public abstract int getCount(String countSQL, Object... paramValue);
	
	public abstract Page<?> queryPage(String sql,int page,int rows,boolean hasoffset);
	
	public abstract Page<?> queryPage(String sql,int page,int rows,boolean hasoffset, Class<?> returnType);
}
