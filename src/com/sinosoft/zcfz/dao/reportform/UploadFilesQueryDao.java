package com.sinosoft.zcfz.dao.reportform;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sinosoft.common.Dao;
import com.sinosoft.dao.BaseAbstractDao;

//虽然为空，但不能省，目的为了获取配置文件里的sessionFactory，也就是为了获取dao
@Repository
public class UploadFilesQueryDao extends BaseAbstractDao {
	@Resource(name = "dao")
	private Dao dao;
}
