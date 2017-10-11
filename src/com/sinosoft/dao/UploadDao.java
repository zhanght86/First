package com.sinosoft.dao;



import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sinosoft.common.Dao;
import com.sinosoft.common.Page;
import com.sinosoft.entity.CfUploadDocument;
import com.sinosoft.entity.UploadFile;


@Repository
public class UploadDao {
	@Resource(name="dao")
	private Dao dao;
	public void save(UploadFile c){
		dao.create(c);
	}
	public void save(CfUploadDocument c){
		dao.create(c);
	}
	public void update(UploadFile c){
		dao.update(c);
	}
	public void delete(int id){
		UploadFile u=(UploadFile) dao.get(UploadFile.class, id);
		dao.delete(u);
	}
	public Page<?> query(int page,int rows,int id){
		  Page<?> pagex=dao.queryPage("select uploadno,filename,filepath,summary,remark from uploadfile where uploadid="+id, page, rows, false);
		return pagex;
	}
	public UploadFile find(int id){
		return (UploadFile) dao.get(UploadFile.class, id);
	}
}
