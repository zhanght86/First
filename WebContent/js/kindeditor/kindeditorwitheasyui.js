(function ($, K) {
    if (!K)
        throw "KindEditor未定义!";
    function create(target) {
        var opts = $.data(target, 'kindeditor').options;
        var editor = K.create(target, opts);
        $.data(target, 'kindeditor').options.editor = editor;
        prettyPrint();
    }
    	
    $.fn.kindeditor = function (options, param) {
    	if (typeof options == 'string') {
            var method = $.fn.kindeditor.methods[options];
            if (method) {
                return method(this, param);
            }
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, 'kindeditor');
            if (state) {
                $.extend(state.options, options);
            } else {
                state = $.data(this, 'kindeditor', {
                        options : $.extend({}, $.fn.kindeditor.defaults, $.fn.kindeditor.parseOptions(this), options)
                    });
            }
            create(this);
        });
    }
    $.fn.kindeditor.parseOptions = function (target) {
        return $.extend({}, $.parser.parseOptions(target, []));
    };
    $.fn.kindeditor.methods = {
        editor : function (jq) {
            return $.data(jq[0], 'kindeditor').options.editor;
        }
    };
    $.fn.kindeditor.defaults = {
    	cssPath : '${contextPath}/js/kindeditor/plugins/code/prettify.css',
    	wellFormatMode:true,
    	designMode:true,
    	themeType : 'default',
    	themesPath:'${contextPath}/js/kindeditor/themes/',
        resizeType : 1,
        allowPreviewEmoticons : false,
        allowImageUpload : false,
        items : [
				'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
				'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
				'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
				'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
				'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
				'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
				'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
				'anchor', 'link', 'unlink', '|', 'about'],
        afterChange:function(){
            this.sync();//这个是必须的,如果你要覆盖afterChange事件的话,请记得最好把这句加上.
        },
        afterCreate:function(){
        	this.sync();
        }
    	//prettyPrint()
    };
    $.parser.plugins.push("kindeditor");
})(jQuery, KindEditor);
