package com.sinosoft.dao;



import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;










import com.sinosoft.common.Constant;
import com.sinosoft.common.Dao;
import com.sinosoft.common.Page;

public abstract class BaseAbstractDao {
	@Resource(name="dao")
	protected Dao dao;
	
	protected String databaseType=Constant.DBTYPE;
	
    /**
     * 保存实体类
     */
    public void save(Object o) {
        if(o!=null&&!"".equals(o)){
        	dao.create(o);
        }
    }
    /**
     * 修改实体类
     */
    public void update(Object o) {
    	if(o!=null&&!"".equals(o)){
    		dao.update(o);
    	}
    }

    /**
     * 删除实体类
     */
    public void remove(Object o) {
    	if(o!=null&&!"".equals(o)){
        	dao.delete(o);
        }
    }
    /**
     * 删除多个实体类
     */
    public void removes(Serializable[] o,Class<?> clazz) {
    	if(o!=null&&!"".equals(o)){
    		for(Serializable id:o){
    			remove(get(clazz,id));
    		}
    	}
    }
    
    @SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz, Serializable id) {
        return (T) dao.get(clazz, id);
    }
    
    public List<?> getByPropName(Class<?> clazz, String propName, Serializable value) {
    	StringBuilder sb=new StringBuilder();
    	sb.append("from ");
    	sb.append(clazz.getSimpleName());
    	sb.append(" where ");
    	sb.append(propName);
    	sb.append(" = ");
    	sb.append(" '");
    	sb.append(value);
    	sb.append("' ");
    	return dao.query(sb.toString());
    }



    /**
     * 查找所有的实体类的数据    
     *  @param
     *  @param clazz 
     * @return */
    public List<?> findAll(Class<?> clazz) {
        return dao.queryAll(clazz);
    }
    /**
     * 分页查找
     *  @param sql
     *  @param page
     *  @param rows
     * @return Page*/
	public Page<?> queryByPage(String sql,int page,int rows){
		/*boolean isOracle=false;
		 try {
			DataSource dataSource=dao.getCurrDataSource();
			String databaseType=DatabaseUtils.getDatabaseType(dataSource.getConnection());
			if("Oracle".equals(databaseType)){
				isOracle=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		boolean isOracle=false;
		if("Oracle".equals(databaseType)){
			isOracle=true;
		}
		Page<?> pagex=dao.queryPage(sql, page, rows, isOracle);
		return pagex;
	}
	/**
	 * 分页查找
	 *  @param sql
	 *  @param page
	 *  @param rows
	 * @return Page*/
	public Page<?> queryByPage(String sql,int page,int rows, Class<?> returnType){
		boolean isOracle=false;
		if("Oracle".equals(databaseType)){
			isOracle=true;
		}
		Page<?> pagex=dao.queryPage(sql, page, rows, isOracle,returnType);
		return pagex;
	}
	/**
	 * sql
	 *  @param sql
	 * @return List*/
	public List<?> queryBysql(String sql){
		List<?> pagex=dao.queryWithJDBC(sql);
		return pagex;
	}
	/**
	 * hsql
	 *  @param hsql
	 * @return List*/
	public List<?> queryByhsql(String hsql){
		List<?> pagex=dao.query(hsql);
		return pagex;
	}
	/**
	 * sql
	 *  @param sql
	 * @return List*/
	public List<?> queryBysql(String sql,Class<?> returnType,Object... paramValue){
		List<?> pagex=dao.getList(sql,returnType,paramValue);
		return pagex;
	}
	public Object queryByID(Class<?> entityClass, Serializable id){
		return dao.get(entityClass, id);
	}
	public int getCount(String sql, Object... paramValue){
		return dao.getCount(sql, paramValue);
		
	}
	
	public void excute(String sql){
		dao.excute(sql);
	}
	public void commonUpdate(String sql, Object... paramValue){
		dao.commonUpdate(sql, paramValue);
	}
	public void flush() {
		dao.flush();
	}
}
