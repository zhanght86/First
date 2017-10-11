/*
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sinosoft.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.orm.hibernate5.HibernateTemplate;

/**
 * @author Administrator
 *
 */
public class BaseDAO<T>  extends NamedParameterJdbcDaoSupport implements Dao {

	private HibernateTemplate hibernateTemplate;

	public Serializable create(Object obj) {
		return hibernateTemplate.save(obj);
	}

	public void update(Object obj) {
		hibernateTemplate.saveOrUpdate(obj);
	}
	

	public void delete(Object obj) {
		hibernateTemplate.delete(obj);
	}

	public Object load(Class<?> entityClass, Serializable id) {
		Object obj = null;
		try {
			obj = hibernateTemplate.load(entityClass, id);
		} catch (Exception e) {
			logger.error(e);
		}
		return obj;
	}

	public Object get(String className, Serializable id) {
		Object obj = null;
		try {
			obj = hibernateTemplate.get(className, id);
		} catch (Exception e) {
			logger.error(e);
		}
		return obj;
	}

	public Object get(Class<?> entityClass, Serializable id) {
		Object obj = null;
		try {
			obj = hibernateTemplate.get(entityClass, id);
		} catch (Exception e) {
			logger.error(e);
		}
		return obj;
	}

	public void evict(Object entity) {
		hibernateTemplate.evict(entity);
	}

	public List<?> query(String hsql) {
		return hibernateTemplate.find(hsql);
	}
	
	public List<?> queryAll(Class<?> clazz) {
		String queryString = "select o from " + clazz.getName() + " as o";
		return hibernateTemplate.find(queryString);
	}


	public void refresh(Object obj) {
		hibernateTemplate.refresh(obj);
	}

	public void flush() {
		hibernateTemplate.flush();
	}
	
	//jdbc封装的jdbctemplate
	
	protected final static String PAGE_SQL_PREFIX = "select * from(select m.*,rownum num from (";
    protected final static String PAGE_SQL_END = ") m where rownum<=?) where num>?";
    
 
    /**
     * 适用于更新数据库,insert,update, delete
     *
     * @param namedSql
     *            :命名参数的ＳＱＬ语句，而且参数的命名必须和ＪａｖａＢｅａｎ中的属性名对应
     * @param javaBean
     *            ：ｊａｖａｂｅａｎ对象
     * @return
     */
    public List<?> queryWithJDBC(String sql) {
		return this.getJdbcTemplate().queryForList(sql);
	}

	public List<?> queryWithJDBC(String sql, Class<?> elementType) {
		return this.getJdbcTemplate().queryForList(sql, elementType);
	}

	/**
   */
	public void excute(String sql) {
		this.getJdbcTemplate().execute(sql);
	}
    /**
     * jdbc执行update
     * @param namedSql
     * @param javaBean
     * @return
     */
	public int update(String namedSql, Object javaBean) {
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(
                javaBean);
        return this.getNamedParameterJdbcTemplate().update(namedSql,paramSource);
    }
	/**
     * jdbc执行update
     * @param namedSql
     * @param paramValue
     * @return
     */
	public int commonUpdate(String sql, Object... paramValue) {
        return this.getJdbcTemplate().update(sql, paramValue);
    }
 
	/**
	 * 带参数查找object
	 * @param sql
	 * @param returnType
	 * @param paramValue
	 * @return
	 */
	public Object getJavaBean(String sql, Class<?> returnType,
            Object... paramValue) {
        @SuppressWarnings("unchecked")
		RowMapper<T> rowMapper = new BeanPropertyRowMapper<T>((Class<T>) returnType);
        try{
        return this.getJdbcTemplate()
                .queryForObject(sql, rowMapper, paramValue);
        }catch(Exception ex){
            return null;
        }
    }
	 /**
	  * 返回指定格式的数据
	  * @param sql
	  * @param returnType
	  * @param 参数
	  * @return
	  */
	public List<?> getList(String sql, Class<?> returnType,
            Object... paramValue) {
        @SuppressWarnings("unchecked")
		RowMapper<T> rowMapper = new BeanPropertyRowMapper<T>((Class<T>) returnType);
        return this.getJdbcTemplate().query(sql, rowMapper, paramValue);
    }
	/**
	 * 返回指定格式的数据
	 * @param sql
	 * @param returnType
	 * @param 参数
	 * @return
	 */
	public List<?> getList(String sql,Object... paramValue) {
		return this.getJdbcTemplate().queryForList(sql, paramValue);
	}
     
	/**
	  * 返回指定格式的数据
	  * @param sql
	  * @param returnType
	  * @return
	  */
	public List<?> getList(String sql, Class<?> returnType) {
        @SuppressWarnings("unchecked")
		RowMapper<T> rowMapper = new BeanPropertyRowMapper<T>((Class<T>) returnType);
        return this.getJdbcTemplate().query(sql, rowMapper);
    }
 
    /**
     * 计算总记录数
     *
     * @param countSQL
     *            计算总记录数的count语句
     * @param paramValue
     *            语句中对应的参数值
     * @return 总记录数
     */
	public int getCount(String countSQL, List<?> paramValue) {
        return Integer.parseInt( this.getJdbcTemplate().queryForList(countSQL,
                paramValue.toArray()).get(0).get("COUNT(*)").toString());
    }
 
	/**
	 * 计算总记录数
	 * @param countSQL
	 * @param paramValue
	 * @return
	 */
	public int getCount(String countSQL, Object... paramValue) {
        return  Integer.parseInt(this.getJdbcTemplate().queryForList(countSQL,
        		paramValue).get(0).get("COUNT(*)").toString());
    }
 
    /*protected Page getPageModel(Page model,
            StringBuilder querySQL, StringBuilder countSQL,
            StringBuilder whereSQL, List paramList, Class<T> returnType) {
        querySQL.append(whereSQL);
        countSQL.append(whereSQL);
        // 计算总记录数
        int allCount = this.getCount(countSQL.toString(), paramList);
        // 获取分页记录集
        // 1。构造完整的分页语句
        querySQL.insert(0, PAGE_SQL_PREFIX);
        querySQL.append(PAGE_SQL_END);
 
        // 2.把分页语句中的参数值加入到paramList中
        paramList.add(model.getNumPerPage()* model.getPageNum());
        paramList.add(( model.getPageNum() - 1) *model.getNumPerPage());
        List result = this.getList(querySQL.toString(), returnType,
                paramList.toArray());
        Page models = new Page();
        models.setTotalCount(allCount);
        models.setNumPerPage(model.getNumPerPage());
        models.setPageNum(model.getPageNum());
        models.setResult(result);
        models.setOrderDirection(model.getOrderDirection());
        models.setOrderField(model.getOrderField());
        return models;
    }*/
    
	/**
	 * 分页查找数据
	 * @param sql
	 * @param page
	 * @param rows
	 * @param hasoffset
	 * @param returnType
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<?> queryPage(String sql,int page,int rows,boolean hasoffset, Class<?> returnType) {
		int start=(page-1)*rows;
		 StringBuffer pagesql=new StringBuffer(sql.length()+20);
		 StringBuffer totalsql=new StringBuffer(sql.length()+20);
		 totalsql.append("select count(*)  from (");
		 if(hasoffset){
		    pagesql.append("select * from (select row_.*,rownum rownum_ from(");
		 }
		 else{
		    pagesql.append("select * from( ");
		 }
		 pagesql.append(sql);
		 totalsql.append(sql);
		 if(hasoffset){
		    pagesql.append(") row_ where rownum<="+(start+rows)+") t where rownum_>"+start+"");
		 }
		else{
		 pagesql.append(") t  limit "+start+","+rows+"");
		}
		 totalsql.append(") t");
		 System.out.println(totalsql.toString());
		 System.out.println(pagesql.toString());
		 int srs=this.getCount(totalsql.toString());
		 long total=srs;
		 List<?> list=this.getList(pagesql.toString(), returnType);
		 return new Page(total,list);
			} 


	/**
	 * 分页查找数据
	 * @param sql
	 * @param page
	 * @param rows
	 * @param hasoffset
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<?> queryPage(String sql, int page, int rows, boolean hasoffset) {
		int start=(page-1)*rows;
		 StringBuffer pagesql=new StringBuffer(sql.length()+20);
		 StringBuffer totalsql=new StringBuffer(sql.length()+20);
		 totalsql.append("select count(*)  from (");
		 if(hasoffset){
		    pagesql.append("select * from (select row_.*,rownum rownum_ from(");
		 }
		 else{
		    pagesql.append("select * from( ");
		 }
		 pagesql.append(sql);
		 totalsql.append(sql);
		 if(hasoffset){
		    pagesql.append(") row_ where rownum<="+(start+rows)+") t where rownum_>"+start+"");
		 }
		else{
		 pagesql.append(") t  limit "+start+","+rows+"");
		}
		 totalsql.append(") t");
		 System.out.println(totalsql.toString());
		 System.out.println(pagesql.toString());
		 int srs=this.getCount(totalsql.toString());
		 long total=srs;
		 List<?> list=this.queryWithJDBC(pagesql.toString());
		 //List<?> list=this.getJdbcTemplate().queryForList(pagesql.toString());
		 return new Page(total,list);
	}
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
