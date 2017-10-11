package  com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cfmap")
public class Cfmap {  
      
    /**  
     */ 
    @Id
    private Long mapid;  
    /**  
     */ 
    @Column
    private String oriname;  
    /**  
     */ 
    @Column
    private String sysname;  
 
    public void setMapid(Long mapid) {  
        this.mapid = mapid;  
    }  
      
    public Long getMapid() {  
        return this.mapid;  
    }  
    public void setOriname(String oriname) {  
        this.oriname = oriname;  
    }  
      
    public String getOriname() {  
        return this.oriname;  
    }  
    public void setSysname(String sysname) {  
        this.sysname = sysname;  
    }  
      
    public String getSysname() {  
        return this.sysname;  
    }  
 
}