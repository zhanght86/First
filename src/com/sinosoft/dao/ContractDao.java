package com.sinosoft.dao;



import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinosoft.common.Dao;
import com.sinosoft.common.Page;
import com.sinosoft.entity.Contract;


@Repository
public class ContractDao {
	@Resource(name="mySessionFactory")
	private SessionFactory sf;
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jt;
	@Resource(name="dao")
	private Dao dao;
	//private HibernateTemplate ht;
	public Session getSession(){
		return sf.openSession();
	}
	public void save(Contract c){
		dao.create(c);
	}
	public void update(Contract c){
		sf.getCurrentSession().update(c);
	}
	public void delete(int id){
		Contract c=getSession().get(Contract.class, id);
		sf.getCurrentSession().delete(c);
	}
	public Page<?> query(int page,int rows,Contract contract){

		  StringBuilder querySql= new StringBuilder("select id,contractId,contractName,applyNo,remark from Contract _contract where 1=1");
		  if(contract.getContractId()!=null && !contract.getContractId().equals("")){
			  querySql.append(" and _contract.contractId like '%"+contract.getContractId()+"%'");
		  }
		  if(contract.getContractName()!=null && !contract.getContractName().equals("")){
			  querySql.append(" and _contract.contractName like '%"+contract.getContractName()+"%'");
		  }
		  if(contract.getApplyNo() !=null && !contract.getApplyNo().equals("")){
			  querySql.append(" and _contract.applyNo like '%"+contract.getApplyNo()+"%'");
		  }
		  if(contract.getRemark() !=null && !contract.getRemark().equals("")){
			  querySql.append(" and _contract.remark like '%"+contract.getRemark()+"%'");
		  }
		  Page<?> pagex=dao.queryPage(querySql.toString(), page, rows, false);

		return pagex;
	}
	public Contract find(int id){
		return getSession().get(Contract.class, id);
	}
}
