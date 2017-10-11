package com.sinosoft.entity;




/**
 */

public class UploadFileId  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String applyType;
     private String batchNo;
     private Integer uploadNo;


    // Constructors

    /** default constructor */
    public UploadFileId() {
    }

    
    /** full constructor */
    public UploadFileId(String applyType, String batchNo, Integer uploadNo) {
        this.applyType = applyType;
        this.batchNo = batchNo;
        this.uploadNo = uploadNo;
    }
    

   
    // Property accessors

    public String getApplyType() {
        return this.applyType;
    }
    
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getBatchNo() {
        return this.batchNo;
    }
    
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getUploadNo() {
        return this.uploadNo;
    }
    
    public void setUploadNo(Integer uploadNo) {
        this.uploadNo = uploadNo;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof UploadFileId) ) return false;
		 UploadFileId castOther = ( UploadFileId ) other; 
         
		 return ( (this.getApplyType()==castOther.getApplyType()) || ( this.getApplyType()!=null && castOther.getApplyType()!=null && this.getApplyType().equals(castOther.getApplyType()) ) )
 && ( (this.getBatchNo()==castOther.getBatchNo()) || ( this.getBatchNo()!=null && castOther.getBatchNo()!=null && this.getBatchNo().equals(castOther.getBatchNo()) ) )
 && ( (this.getUploadNo()==castOther.getUploadNo()) || ( this.getUploadNo()!=null && castOther.getUploadNo()!=null && this.getUploadNo().equals(castOther.getUploadNo()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getApplyType() == null ? 0 : this.getApplyType().hashCode() );
         result = 37 * result + ( getBatchNo() == null ? 0 : this.getBatchNo().hashCode() );
         result = 37 * result + ( getUploadNo() == null ? 0 : this.getUploadNo().hashCode() );
         return result;
   }   





}
