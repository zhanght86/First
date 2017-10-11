package com.sinosoft.service;



import com.sinosoft.common.Page;
import com.sinosoft.dto.ContractDTO;
import com.sinosoft.entity.Contract;

public interface ContractService {
	public void saveConTract(ContractDTO cdt);
	public void updateConTract(int id,ContractDTO cdt);
	public void deleteConTract(String[] idArr);
	public Page<?> qryConTract(int page,int rows,ContractDTO cdt);
	public Contract findConTract(int id);
}
