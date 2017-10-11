package com.sinosoft.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
 



import javax.servlet.http.HttpServletRequest;
 



import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
 



import freemarker.template.Template;
import freemarker.template.TemplateException;
 
/**
 * @author zh 静态化方法
 **/
public class FreeMarkerUtil {
    /**
     * 
     * 生成HTML静态页面的公公方法
     * @param fmc 
     * @param templateName 模板的名称
     * @param request
     * @param map 生成模板需要的数据
     * @param filePath 相对于web容器的路径
     * @param fileName 要生成的文件的名称，带扩展名
     * 
     */
    public static void createHtml(FreeMarkerConfig fmc, String templateName,
            HttpServletRequest request, Map<?, ?> map, String filePath,
            String fileName) throws Exception{
        Writer out = null;
        OutputStream outputStream = null;
        try {
            Template template = fmc.getConfiguration()
                    .getTemplate(templateName);
            template.setEncoding("UTF-8");
            String htmlPath = filePath
                    + "/" + fileName;
            File htmlFile = new File(htmlPath);
            if (!htmlFile.getParentFile().exists()) {
                htmlFile.getParentFile().mkdirs();
            }
            if (!htmlFile.exists()) {
                htmlFile.createNewFile();
            }
            outputStream = new FileOutputStream(htmlPath);
            out = new OutputStreamWriter(outputStream,"UTF-8");
            template.process(map, out);
            System.out.println("生成成功");
            out.flush();
            outputStream.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (TemplateException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
            	if(out!=null)
                out.close();
//                out = null;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
            if(outputStream!=null){
            	try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
            }
        }
    }
     
     
    /**
     * @param request
     * @param filePath  文件存放的路径
     * @param fileName 文件的名称，需要扩展名
     * @return
     */
    public static Map<String,Object> htmlFileHasExist(HttpServletRequest request,String filePath,
            String fileName) {
        Map<String,Object> map = new HashMap<String,Object>();
        String htmlPath = filePath
                + "doc/" + fileName;
        File htmlFile = new File(htmlPath);
        if(htmlFile.exists()){
            map.put("exist", true);
        }else{
            map.put("exist",false);
        }
        return map ;
    }
}