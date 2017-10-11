package com.sinosoft.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

public class UnZipRarUtil {
	/**
	 * 方式一 zip压缩文件解压
	 * 
	 * @param zipFilePath
	 * @param targetPath
	 * @throws IOException
	 */
	public static List<String> unzip(String zipFilePath, String targetPath)
			throws IOException {
		List<String> fileLists = new ArrayList<String>();
		OutputStream os = null;
		InputStream is = null;
		ZipFile zipFile = null;
		OutputStream outputStream = null;
		
		try {
			zipFile = new ZipFile(zipFilePath);
			String directoryPath = "";
			if (null == targetPath || "".equals(targetPath)) {
				directoryPath = zipFilePath.substring(0,
						zipFilePath.lastIndexOf("."));
			} else {
				directoryPath = targetPath;
			}
			Enumeration entryEnum = zipFile.getEntries();
			if (null != entryEnum) {
				ZipEntry zipEntry = null;
				
				while (entryEnum.hasMoreElements()) {
					zipEntry = (ZipEntry) entryEnum.nextElement();
					if (zipEntry.isDirectory()) {
						directoryPath = directoryPath + File.separator
								+ zipEntry.getName();
						//System.out.println(directoryPath);
						continue;
					}
					if (zipEntry.getSize() > 0) {
						// 文件
						File targetFile = buildFile(directoryPath
								+ File.separator + zipEntry.getName(), false);
						fileLists.add(targetFile.getPath());
						outputStream = new FileOutputStream(targetFile);
						os = new BufferedOutputStream(outputStream);
						is = zipFile.getInputStream(zipEntry);
						byte[] buffer = new byte[4096];
						int readLen = 0;
						while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
							os.write(buffer, 0, readLen);
						}

						os.flush();
						outputStream.flush();
						//os.close();
					} else {
						// 空目录
						buildFile(
								directoryPath + File.separator
										+ zipEntry.getName(), true);
					}
				}
			}
			
//			os.close();
//			is.close();
//			zipFile.close();
			return fileLists;
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != zipFile) {
				zipFile.close();
				zipFile = null;
			}
			if(outputStream!=null){
				outputStream.close();
			}
			if (null != is) {
				is.close();
			}
			if (null != os) {
				os.close();
			}
		}
	}

	public static File buildFile(String fileName, boolean isDirectory) {
		File target = new File(fileName);
		if (isDirectory) {
			target.mkdirs();
		} else {
			if (!target.getParentFile().exists()) {
				target.getParentFile().mkdirs();
				target = new File(target.getAbsolutePath());
			}
		}
		return target;
	}

	/**
	 * 方式二 解压zip格式压缩包 对应的是 Apache的ant.jar
	 */
	/*private static void unzip1(String sourceZip, String destDir)
			throws Exception {
		try {
			Project p = new Project();
			Expand e = new Expand();
			e.setProject(p);
			e.setSrc(new File(sourceZip));
			e.setOverwrite(false);
			e.setDest(new File(destDir));
			
			 * ant下的zip工具默认压缩编码为UTF-8编码， 而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
			 * 所以解压缩时要制定编码格式
			 
			e.setEncoding("gbk");
			e.execute();
		} catch (Exception e) {
			throw e;
		}
	}
*/
	public static List<String> unrar(String sourceRar, String destDir)throws Exception {
		List<String> fileLists = new ArrayList<String>();
		Archive a = null;
		FileOutputStream fos = null;
		try {
			a = new Archive(new File(sourceRar));
			FileHeader fh = a.nextFileHeader();
			while (fh != null) {
				String fhpath = "";
				if(fh.isUnicode()){
					fhpath = fh.getFileNameW().trim();
				}else{
					fhpath = fh.getFileNameString().trim();
				}
				
				fhpath = fhpath.replaceAll("\\\\", "/");
				File file = new File(destDir + fhpath);
				/*if(file.isFile()){
					fileLists.add(file.getPath());
				}*/
				if(fh.isDirectory()){
					file.mkdirs();
				}else{
					File parent = file.getParentFile();
					if(parent != null && !parent.exists()){
						parent.mkdirs();
					}
					fos = new FileOutputStream(file);
					a.extractFile(fh, fos);
					fos.close();
					fileLists.add(file.getPath());
				}
				fh = a.nextFileHeader();
			}
			a.close();
			return fileLists;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fos != null){
				try {
					fos.close();
					fos = null;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			if(a != null){
				try {
					a.close();
					a = null;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		return fileLists;
	}

	/**
	 * 解压缩
	 */
	public static List<String> deCompress(String sourceFile, String destDir)
			throws Exception {
		// 保证文件夹路径最后是"/"或者"\"
		List<String> filelist = new ArrayList<String>();
		char lastChar = destDir.charAt(destDir.length() - 1);
		if (lastChar != '/' && lastChar != '\\') {
			destDir += File.separator;
		}
		// 根据类型，进行相应的解压缩
		String type = sourceFile.substring(sourceFile.lastIndexOf(".") + 1);
		if (type.equals("zip")) {
			filelist = unzip(sourceFile, destDir);
			
		} else if (type.equals("rar")) {
			filelist = unrar(sourceFile, destDir);
		} 
		return filelist;
	}

	public static void main(String[] args) {
		
		/*try {
			deCompress("D:\\zipTest.zip","D:\\");
			deCompress("D:\\rarTest.rar","D:\\");
			//deCompress("D:\\spring4参考文档以及API（英文）.zip","D:\\");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
