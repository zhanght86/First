/** 
 *  页面加载等待页面 
 * 
 * @author gxjiang 
 * @date 2010/7/24 
 * 
 */  
var height = window.screen.height-250;  
var width = window.screen.width;  
var leftW = 300;  
if(width>1200){  
   leftW = 500;  
}else if(width>1000){  
   leftW = 350;  
}else {  
   leftW = 100;  
}  
var _html = "<div id='loading' style='position:absolute;left:0;width:100%;height:"+height+"px;top:0;background:white;opacity:1.0;filter:alpha(opacity=100);'>";
/*_html+="<div style='position:absolute;  cursor1:wait;left:"+leftW+"px;top:200px;width:auto;height:16px;padding:12px 5px 10px 30px;"
_html+=" background:#fff url("
_html+="/cf/js/easyui/themes/default/images/loading.gif) no-repeat scroll 5px 10px;border:2px solid #ccc;color:#000;'>"
_html+="正在加载，请等待..."  
_html+="</div>"*/
_html+="</div><script>$.messager.progress({ title:'请稍后', msg:'页面加载中...'})</script>";  

  
window.onload = function(){  
   var _mask = document.getElementById('loading');  
   _mask.parentNode.removeChild(_mask);   
   $.messager.progress('close');
}    
document.write(_html);  