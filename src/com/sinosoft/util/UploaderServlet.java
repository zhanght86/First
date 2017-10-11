package com.sinosoft.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.sinosoft.dto.PluploadDTO;
import com.sinosoft.zcfz.dto.interfase.ImpDataInfoDTO;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.UploadFile;

@Component
public class UploaderServlet{

	String repositoryPath;
	String uploadPath;
	String fileName=null;
	String date;
	public String getUploadPath(){
		return uploadPath;
	}
	public void upload(PluploadDTO pld, HttpServletResponse response)
			throws ServletException, IOException {
		//repositoryPath = FileUtils.getTempDirectoryPath();
		//uploadPath = config.getServletContext().getRealPath(config.getInitParameter("uploadPath"));
		uploadPath=getUploadPath();
		File up = new File(uploadPath);
		if(!up.exists()){
			up.mkdir();
		}
		response.setCharacterEncoding("UTF-8");
		Integer schunk = pld.getChunk();//分割块数
		Integer schunks = pld.getChunks();//总分割数
		String name = null;//文件名
		String oname=null;
		BufferedOutputStream outputStream=null; 
		if (ServletFileUpload.isMultipartContent(pld.getRequest())) {
			try {
				DefaultMultipartHttpServletRequest dmhsr=(DefaultMultipartHttpServletRequest)pld.getRequest();
				MultiValueMap<String, MultipartFile> map =dmhsr.getMultiFileMap();
				String newFileName = null;
				 Iterator<String> iter = map.keySet().iterator();  
		            while(iter.hasNext()) { 
		                String str = (String) iter.next();  
		                List<MultipartFile> fileList =  map.get(str);
		                for(MultipartFile multipartFile : fileList) {  
		                		name=multipartFile.getOriginalFilename();
		                		oname=multipartFile.getName();
		                		//newFileName = UUID.randomUUID().toString().replace("-","").concat("_").concat(multipartFile.getName()).concat(".").concat(FilenameUtils.getExtension(name));
		                		newFileName =System.currentTimeMillis()+"_".concat(FilenameUtils.getBaseName(name)).concat(".").concat(FilenameUtils.getExtension(name));
		                		if (oname != null) {
									String nFname = newFileName;
									if (schunk != null&&schunks!=0) {
										nFname = schunk + "_" + name;
									}else if(schunks==0){
										nFname=newFileName;
										fileName=newFileName;
									}
									File savedFile = new File(uploadPath, nFname);
									multipartFile.transferTo(savedFile);
								}else {
		    						//判断是否带分割信息
		    							schunk = pld.getChunk();
		    							schunks = pld.getChunks();
		    					}
		                	}
		                }
		            /*以下为servlet方式生成的，需要在配置文件中去掉mulpartviewrelover这个视图解析器
		             * DiskFileItemFactory factory = new DiskFileItemFactory();
					factory.setSizeThreshold(1024);
					factory.setRepository(new File(repositoryPath));//设置临时目录
					ServletFileUpload upload = new ServletFileUpload(factory);
					upload.setHeaderEncoding("UTF-8");
					upload.setSizeMax(5 * 1024 * 1024);//设置附近大小
				List<FileItem> items = upload.parseRequest(dmhsr.getRequest());
				//生成新文件名
				String newFileName = null;
				for (FileItem item : items) {
					if (!item.isFormField()) {// 如果是文件类型
						name = item.getName();// 获得文件名
						newFileName = UUID.randomUUID().toString().replace("-","").concat(".").concat(FilenameUtils.getExtension(name));
						if (name != null) {
							String nFname = newFileName;
							if (schunk != null) {
								nFname = schunk + "_" + name;
							}
							File savedFile = new File(uploadPath, nFname);
							item.write(savedFile);
						}
					} else {
						//判断是否带分割信息
						if (item.getFieldName().equals("chunk")) {
							schunk = Integer.parseInt(item.getString());
						}
						if (item.getFieldName().equals("chunks")) {
							schunks = Integer.parseInt(item.getString());
						}
					}
				}*/
				if (schunk != null && schunk + 1 == schunks) {
					System.out.println("6");
					fileName=newFileName;
					outputStream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath, newFileName)));
					//遍历文件合并
					for (int i = 0; i < schunks; i++) {
						System.out.println("7");
						File tempFile=new File(uploadPath,i+"_"+name);
						byte[] bytes=FileUtils.readFileToByteArray(tempFile);  
						outputStream.write(bytes);
						outputStream.flush();
						tempFile.delete();
					}
					outputStream.flush();
				}
				response.getWriter().write("{\"status\":true,\"newName\":\""+newFileName+"\"}");
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write("{\"status\":false}");
			}finally{  
	            try {  
	            	if(outputStream!=null)
	            		outputStream.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }   
		}
	}
	public void upload(UploadInfoDTO pld, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("--UploaderServlet");
		//repositoryPath = FileUtils.getTempDirectoryPath();
		//uploadPath = config.getServletContext().getRealPath(config.getInitParameter("uploadPath"));
		uploadPath=pld.getFilePath();
		File up = new File(uploadPath);
		if(!up.exists()){
			up.mkdir();
		}
		System.out.println("repositoryPath="+repositoryPath);
		System.out.println("uploadPath="+uploadPath);
//		response.setCharacterEncoding("UTF-8");
		Integer schunk = pld.getChunk();//分割块数
		Integer schunks = pld.getChunks();//总分割数
		String name = null;//文件名
		String oname=null;
		BufferedOutputStream outputStream=null; 
		if (ServletFileUpload.isMultipartContent(pld.getRequest())) {
			try {
				DefaultMultipartHttpServletRequest dmhsr=(DefaultMultipartHttpServletRequest)pld.getRequest();
				MultiValueMap<String, MultipartFile> map =dmhsr.getMultiFileMap();
				String newFileName = null;
				 Iterator<String> iter = map.keySet().iterator();  
		            while(iter.hasNext()) {  
		                String str = (String) iter.next();  
		                List<MultipartFile> fileList =  map.get(str);
		                for(MultipartFile multipartFile : fileList) { 
		                		name=multipartFile.getOriginalFilename();
		                		oname=multipartFile.getName();
		                		SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		                		date=df.format(new Date());
						// if(pld.getDepartment()!=null){
						// newFileName =
						// pld.getDepartment()+"_"+date.substring(0,10)+"_"+pld.getReporttype()+"_"+pld.getReportname()+"_"+pld.getDepartment()+"_".concat(FilenameUtils.getBaseName(name)).concat(".").concat(FilenameUtils.getExtension(name));
						// }else{
						// newFileName =
						// date.substring(0,10)+"_"+pld.getReporttype()+"_"+pld.getReportname()+"_".concat(FilenameUtils.getBaseName(name)).concat(".").concat(FilenameUtils.getExtension(name));
						// }
						// newFileName =
						// pld.getDepartment()+"_"+date.substring(0,10)+"_"+pld.getReporttype()+"_"+pld.getReportname()+"_"+pld.getDepartment()+"_".concat(FilenameUtils.getBaseName(name)).concat(".").concat(FilenameUtils.getExtension(name));
						newFileName = pld.getNewfileName();
		                		if (oname != null) {
									String nFname = newFileName;
									if (schunk != null&&schunks!=0) {
										nFname = schunk + "_" + name;
									}else if(schunks==0){
										nFname=newFileName;
										fileName=newFileName;
									}
									File savedFile = new File(uploadPath, nFname);
									multipartFile.transferTo(savedFile);
								}else {
		    						//判断是否带分割信息
		    							schunk = pld.getChunk();
		    							schunks = pld.getChunks();
		    					}
		                	}
		                }
		            /*以下为servlet方式生成的，需要在配置文件中去掉mulpartviewrelover这个视图解析器
		             * DiskFileItemFactory factory = new DiskFileItemFactory();
					factory.setSizeThreshold(1024);
					factory.setRepository(new File(repositoryPath));//设置临时目录
					ServletFileUpload upload = new ServletFileUpload(factory);
					upload.setHeaderEncoding("UTF-8");
					upload.setSizeMax(5 * 1024 * 1024);//设置附近大小
				List<FileItem> items = upload.parseRequest(dmhsr.getRequest());
				//生成新文件名
				String newFileName = null;
				for (FileItem item : items) {
					if (!item.isFormField()) {// 如果是文件类型
						name = item.getName();// 获得文件名
						newFileName = UUID.randomUUID().toString().replace("-","").concat(".").concat(FilenameUtils.getExtension(name));
						if (name != null) {
							String nFname = newFileName;
							if (schunk != null) {
								nFname = schunk + "_" + name;
							}
							File savedFile = new File(uploadPath, nFname);
							item.write(savedFile);
						}
					} else {
						//判断是否带分割信息
						if (item.getFieldName().equals("chunk")) {
							schunk = Integer.parseInt(item.getString());
						}
						if (item.getFieldName().equals("chunks")) {
							schunks = Integer.parseInt(item.getString());
						}
					}
				}*/
				if (schunk != null && schunk + 1 == schunks) {
					fileName=newFileName;
					outputStream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath, newFileName)));
					//遍历文件合并
					for (int i = 0; i < schunks; i++) {
						File tempFile=new File(uploadPath,i+"_"+name);
						byte[] bytes=FileUtils.readFileToByteArray(tempFile);  
						outputStream.write(bytes);
						outputStream.flush();
						tempFile.delete();
					}
					outputStream.flush();
				}
//				response.getWriter().write("文件上传成功");
			} catch (Exception e) {
				e.printStackTrace();
//				response.getWriter().write("文件上传失败");
			}finally{  
	            try {  
	            	if(outputStream!=null)
	            		outputStream.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }   
		}
		//释放掉上传文件
		up = null;
	}
	public void download(HttpServletRequest request,HttpServletResponse response,UploadFile u) throws Exception{
		response.setContentType("application/octet-stream");
		request.setCharacterEncoding("UTF-8");
		BufferedOutputStream bos = null;  
		try{
	        String name=u.getFileName();
	        String path=u.getFilePath();
			long fileLength = new File(path).length();
	        response.setHeader("Content-disposition", "attachment; filename="+URLEncoder.encode(name,"UTF-8"));  
	        response.setHeader("Content-Length", String.valueOf(fileLength));  
	        bos = new BufferedOutputStream(response.getOutputStream());  
			download(path, name, bos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bos!=null){
				bos.close();
			}
		}
		
	}
	public static void download(String path,String name, BufferedOutputStream out) {
        try {
			FileInputStream in = new FileInputStream(new File(path));
            write(in, out);
        } catch (FileNotFoundException e) {
            try {
				FileInputStream in = new FileInputStream(new File(path));
                write(in, out);
            } catch (IOException e1) {              
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }       
    }
    /**
     * 写入数据
     * @param in
     * @param out
     * @throws IOException
     */
    public static void write(InputStream in, OutputStream out) throws IOException{
        try{
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        } finally {
            try {
                in.close();
            }
            catch (IOException ex) {
            }
            try {
                out.close();
            }
            catch (IOException ex) {
            }
        }
    }   
	
	
	public void upload(ImpDataInfoDTO info)throws ServletException, IOException {
		//repositoryPath = FileUtils.getTempDirectoryPath();
		uploadPath=info.getFilePath();
		File up = new File(uploadPath);
		if(!up.exists()){
			up.mkdir();
		}
		System.out.println("repositoryPath="+repositoryPath);
		System.out.println("uploadPath="+uploadPath);
		Integer schunk = info.getChunk();//分割块数
		Integer schunks = info.getChunks();//总分割数
		String name = null;//文件名
		String oname=null;
		BufferedOutputStream outputStream=null; 
		if (ServletFileUpload.isMultipartContent(info.getRequest())) {
			try {
				DefaultMultipartHttpServletRequest dmhsr=(DefaultMultipartHttpServletRequest)info.getRequest();
				MultiValueMap<String, MultipartFile> map =dmhsr.getMultiFileMap();
				String newFileName = null;
				 Iterator<String> iter = map.keySet().iterator();  
		            while(iter.hasNext()) {  
		                String str = (String) iter.next();  
		                List<MultipartFile> fileList =  map.get(str);
		                for(MultipartFile multipartFile : fileList) { 
		                		name=multipartFile.getOriginalFilename();
		                		oname=multipartFile.getName();
		                		SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		                		date=df.format(new Date());
		                		newFileName = "日期_"+date.substring(0,10)+"_"+info.getYear()+"年第"+info.getQuarter()+"季度"+"_".concat(FilenameUtils.getBaseName(name)).concat(".").concat(FilenameUtils.getExtension(name));
		                		if (oname != null) {
									String nFname = newFileName;
									if (schunk != null&&schunks!=0) {
										nFname = schunk + "_" + name;
									}else if(schunks==0){
										nFname=newFileName;
										fileName=newFileName;
									}
									File savedFile = new File(uploadPath, nFname);
									multipartFile.transferTo(savedFile);
								}else {
		    						//判断是否带分割信息
		    						schunk = info.getChunk();
		    						schunks = info.getChunks();
		    					}
		                	}
		                }
				if (schunk != null && schunk + 1 == schunks) {
					fileName=newFileName;
					outputStream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath, newFileName)));
					//遍历文件合并
					for (int i = 0; i < schunks; i++) {
						File tempFile=new File(uploadPath,i+"_"+name);
						byte[] bytes=FileUtils.readFileToByteArray(tempFile);  
						outputStream.write(bytes);
						outputStream.flush();
						tempFile.delete();
					}
					outputStream.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{  
	            try {  
	            	if(outputStream!=null)
	            		outputStream.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }   
		}
	}

	/**
	 * 打包文件
	 * @param request
	 * @param response
	 * @param files
	 * @param zipFilePath  打包文件路径，
	 */
	public void zipFile(HttpServletRequest request,
			HttpServletResponse response, File[] files, String zipFilePath) {

		ZipOutputStream out = null;
		FileInputStream fis = null;
		try {    
			out = new ZipOutputStream(new FileOutputStream(zipFilePath));    
            byte[] buffer = new byte[1024];
       
            for (int i = 0; i < files.length; i++) {    
            	fis = new FileInputStream(files[i]);    
                out.putNextEntry(new ZipEntry(files[i].getName()));    
                //设置压缩文件内的字符编码，不然会变成乱码    
                out.setEncoding("GBK");    
                int len;    
                // 读入需要下载的文件的内容，打包到zip文件    
                while ((len = fis.read(buffer)) > 0) {    
                    out.write(buffer, 0, len);    
                }    
                out.closeEntry();    
                fis.close();    
            }    
//            out.close();    
  

        } catch (Exception e) {    
        	e.printStackTrace();
        }   finally {
        	if(fis!=null){
        		try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if(out!=null){
        		try {
        			out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
	}
	public void delete(HttpServletRequest request,HttpServletResponse response,UploadFile u) throws Exception{
		String name=u.getFileName();
        String path=u.getFilePath();
		File tempFile=new File(path,name);
		tempFile.delete();
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
