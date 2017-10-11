$(function(){
	clockon();
    InitLeftMenu();
	tabClose();
	tabCloseEven();
})

//初始化左侧
function InitLeftMenu() {

	$(".easyui-accordion").empty();
	var menulist = "";

	$.each(_menus.menus, function(i, n) {
		/*menulist += '<div title="'+n.menuname+'"  data-options="iconCls:"'+n.icon+'" style="overflow:auto;">';*/
		menulist="";
		//menulist ='<ul>'
		$.each(n.menus, function(m, y) {//二级菜单
			if(y.url=='9999'){//判断有没有三级菜单，如果有的话，第二级会是9999，没有的话，二级是路径
				menulist += '<div class="two-tree"><div><span  class="icon '+y.icon+'" >'+'&nbsp;&nbsp;&nbsp;&nbsp;'+ y.menuname+'</span></div>' ;
				menulist += '<div class="three-tree" style=＂display:none;＂>';
				$.each(y.menus, function(j, o) {//三级菜单
				       menulist += '<li><div style=＂display:none;＂><a target="mainFrame" ghref="' + o.url + '" ><span class="icon '+o.icon+'" ></span>' + o.menuname + '</a></div></li>';
				  })
			   menulist += '</div></div>';
			}else{//没有三级菜单，这是第二级
				  menulist += '<li><div><a target="mainFrame" ghref="' + y.url + '" ><span class="icon '+y.icon+'" ></span>' + y.menuname + '</a></div></li> ';
            }
			
		})   
		
		menulist ='<ul>'+menulist+'</ul></div>';
		
		$(".easyui-accordion").accordion('add',{
			title:n.menuname,
			iconCls:n.icon,
			content:menulist,
			selected:false
		});
		
	})

	/*$(".easyui-accordion").append(menulist);
    console.log(menulist);*/
  

	$('.easyui-accordion li a').click(function(){
		var tabTitle = $(this).text();
		var url = $(this).attr("ghref");
		addTab(tabTitle,url);
		$('.easyui-accordion li div').removeClass("selected");
		$(this).parent().addClass("selected");
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});
	//新加，点击二级菜单，会出现三级菜单，第二次点击二级菜单，三级菜单会回去
	$(document).ready(function() {
		var menuList = $('.three-tree');
		$('.two-tree').each(function(i) {//获取列表的大标题并遍历
			$(this).click(function(){
				if($(menuList[i]).css('display') == 'none'){
					$(menuList[i]).slideDown(300);
				}
				else{
					$(menuList[i]).slideUp(300);
				}
			});
		});
	});
	

}

function addTab(subtitle,url){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			//content:createFrame(url),
			content:createFrame(url),
			closable:true,
			width:$('#mainPanle').width()-10,
			height:$('#mainPanle').height()-26
		});
	}else{
		$('#tabs').tabs('select',subtitle);
	}
	tabClose();
}

/**
 * 用户右侧显示内容的iframe
 * @param url
 * @returns {String}
 */
function createFrame(url)
{
	var s = '<iframe name="mainFrame" fit="true" scrolling="auto" frameborder="0"  overflow-y:"hidden" src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose()
{
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children("span").text();
		if(subtitle!='首页')
			$('#tabs').tabs('close',subtitle);
	})

	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY,
		});

		var subtitle =$(this).children("span").text();
		$('#mm').data("currtab",subtitle);

		return false;
	});
}
//绑定右键菜单事件
function tabCloseEven()
{
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		if(currtab_title!='首页')
			$('#tabs').tabs('close',currtab_title);
	})
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			if(t!='首页')
				$('#tabs').tabs('close',t);
		});	
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		var currtab_title = $('#mm').data("currtab");
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			if(t!=currtab_title&&t!='首页')
				$('#tabs').tabs('close',t);
		});	
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length==0){
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			if(t!='首页')
				$('#tabs').tabs('close',t);
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	})
}

//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}

function clockon() {
	var now = new Date();
	var year = now.getFullYear(); //getFullYear getYear
	var month = now.getMonth();
	var date = now.getDate();
	var day = now.getDay();
	var hour = now.getHours();
	var minu = now.getMinutes();
	var sec = now.getSeconds();
	var week;
	month = month + 1;
	if (month < 10) month = "0" + month;
	if (date < 10) date = "0" + date;
	if (hour < 10) hour = "0" + hour;
	if (minu < 10) minu = "0" + minu;
	if (sec < 10) sec = "0" + sec;
	var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
	week = arr_week[day];
	var time = "";
	time = year + "年" + month + "月" + date + "日" + " " + hour + ":" + minu + ":" + sec + " " + week;

	$("#bgclock").html(time);

	var timer = setTimeout("clockon()", 200);
}

