package com.sinosoft.common;
import cn.org.rapid_framework.generator.GeneratorFacade;
/** 
* 
*----------Dragon be here!----------/ 
* 　　　┏┓　　　┏┓ 
* 　　┏┛┻━━━┛┻┓ 
* 　　┃　　　　　　　┃ 
* 　　┃　　　━　　　┃ 
* 　　┃　┳┛　┗┳　┃ 
* 　　┃　　　　　　　┃ 
* 　　┃　　　┻　　　┃ 
* 　　┃　　　　　　　┃ 
* 　　┗━┓　　　┏━┛ 
* 　　　　┃　　　┃神兽保佑 
* 　　　　┃　　　┃代码无BUG！ 
* 　　　　┃　　　┗━━━┓ 
* 　　　　┃　　　　　　　┣┓ 
* 　　　　┃　　　　　　　┏┛ 
* 　　　　┗┓┓┏━┳┓┏┛ 
* 　　　　　┃┫┫　┃┫┫ 
* 　　　　　┗┻┛　┗┻┛ 
* ━━━━━━神兽出没━━━━━━by:coder-pig 
*/  

public class Generator {
    public static void main(String[]args)throws Exception{
        GeneratorFacade g = new GeneratorFacade();
        //g.deleteOutRootDir();
        g.getGenerator().setTemplateRootDir("WebContent/template");
        //g.getGenerator().setOutRootDir("d:/temp/rapid/");
        g.deleteByTable("student"); //自行修改生成的表
        g.generateByTable("student");
        g.getGenerator().setTemplateRootDir("WebContent/webtemplate");
        g.getGenerator().setOutRootDir("WebContent/page");
        g.deleteByTable("student");
        g.generateByTable("student");
    }
}