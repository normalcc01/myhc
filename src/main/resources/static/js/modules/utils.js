layui.define(['util'], function (exp) {
    var $ = layui.jquery
        ,util = layui.util
        ,extras = {}
        ,scriptsArray //定义一个全局script的标记数组，用来标记是否某个script已经下载到本地
        ,cssArray;

    //对Date的扩展，将 Date 转化为指定格式的String   //月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   //年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   //例子：   //(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   //(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };

    //  重新定义 加载动态js方法
    $.cachedScript = function (url, options) {
        scriptsArray = scriptsArray ? scriptsArray : [];
        //循环script标记数组
        for (var s in scriptsArray) {
            //如果某个数组已经下载到了本地
            if (scriptsArray[s]==url) {
                return { //则返回一个对象字面量，其中的done之所以叫做done是为了与下面$.ajax中的done相对应
                    done: function (method) {
                        if (typeof method == 'function'){ //如果传入参数为一个方法
                            method();
                        }
                    }
                };
            }
        }
        //这里是jquery官方提供类似getScript实现的方法，也就是说getScript其实也就是对ajax方法的一个拓展
        options = $.extend(options || {}, {
            dataType: "script",
            url: url,
            cache:true //缓存
        });
        scriptsArray.push(url); //将url地址放入script标记数组中
        return $.ajax(options);
    };

    //动态加载css
    function loadCss(urls) {
        urls = $.isArray(urls) ? urls : [urls];
        cssArray = cssArray ? cssArray : [];
        for (let index in urls){
            let url = urls[index];
            if($.inArray(url, cssArray) === -1){
                layui.link(url);
                cssArray.push(url);
            }
        }
    }

    //动态加载js
    function loadScript(urls, success, index) {
        index = index ? index : 0;
        let url = $.isArray(urls) ? urls[index] : urls
            ,max = $.isArray(urls) ? urls.length : 1;

        if(index >= max){
            //调用完成方法
            if(success && typeof success === 'function'){
                return success();
            }
            return;
        }
        $.cachedScript(url).done(function () {
            loadScript(urls, success, index + 1);
        });
    }

    var utils = $.extend(util, {
        // 遍历 （对象， 回调， 完成回调）
        each: function (obj, fn, done) {
            var key, that = this, success = function(){};
            if (typeof fn !== 'function') return that;
            if(typeof done === 'function'){
                success = done;
            }
            obj = obj || [];
            if (obj.constructor === Object) {
                for (key in obj) {
                    if (fn.call(obj[key], key, obj[key])) break;
                }
                success(that);
            } else {
                for (key = 0; key < obj.length; key++) {
                    if (fn.call(obj[key], key, obj[key])) break;
                }
                success(that);
            }
            return that;
        }
        //动态加载js
        ,loadScript: function (url, success) {
            loadScript(url, success);
        }
        //取Cookie的值
        ,getCookie: function (cookie_name) {
            var allcookies = document.cookie;
            var cookie_pos = allcookies.indexOf(cookie_name);   //索引的长度
            // 如果找到了索引，就代表cookie存在，反之，就说明不存在。
            if (cookie_pos !== -1){
                // 把cookie_pos放在值的开始，只要给值加1即可。
                cookie_pos += cookie_name.length + 1;      //这里容易出问题
                var cookie_end = allcookies.indexOf(";", cookie_pos);
                if (cookie_end === -1){
                    cookie_end = allcookies.length;
                }
                var value = unescape(allcookies.substring(cookie_pos, cookie_end));
            }
            return value;
        }
        //写入到Cookie
        ,setCookie: function (name, value) {
            var argv = utils.setCookie.arguments;
            //本例中length = 3
            var argc = utils.setCookie.arguments.length;
            var expires = (argc > 2) ? argv[2] : null;
            var path = (argc > 3) ? argv[3] : null;
            var domain = (argc > 4) ? argv[4] : null;
            var secure = (argc > 5) ? argv[5] : false;
            document.cookie = name + "=" + escape(value) + ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) + ((path == null) ? "" : ("; path=" + path)) + ((domain == null) ? "" : ("; domain=" + domain)) + ((secure == true) ? "; secure" : "");
        }
        //时间组件
        ,clock: function(item, msg){
            const weeks = ["&nbsp;星期日","&nbsp;星期一","&nbsp;星期二","&nbsp;星期三","&nbsp;星期四","&nbsp;星期五","&nbsp;星期六"];

            let date = new Date();
            let time = date.Format("yyyy-MM-dd hh:mm:ss");
            let week = weeks[date.getDay()];

            let hour = date.getHours(); //当前系统时间的小时值
            let timeFrame = "" +((hour >= 12) ? (hour >= 18) ? "&nbsp;晚上" : "&nbsp;下午" : "&nbsp;上午" ); //当前时间属于上午、晚上还是下午

            let time_clock = time + week;
            if(msg){
                time_clock += "</br>" + timeFrame + "好，" + msg;
            }
            $(item).html(time_clock);

            setTimeout(function () {
                utils.clock(item, msg);
            },1000);
        }
        ,nowYear: function () {
            return new Date().Format("yyyy");
        }
        ,nowDate: function () {
            return new Date().Format("yyyy-MM-dd");
        }
        ,nowTime: function () {
            return new Date().Format("yyyy-MM-dd hh:mm");
        }
        ,formatDate: function (time){
            let date = new Date();
            date.setTime(time);
            return date.Format("yyyy-MM-dd");
        }
        ,formatTime: function (time){
            let date = new Date();
            date.setTime(time);
            return date.Format("yyyy-MM-dd hh:mm");
        }
        ,timespan: function (){
            return new Date().Format("hhmmsss");
        }
        //检查elem中指定的class，不存在则添加
        ,check_clz: function (elem, clz) {
            if(typeof elem === 'string'){
                elem = $(elem);
            }
            if(!elem.hasClass(clz)){
                elem.addClass(clz);
            }
        }
        /**
         * 获取表单值
         * @param itemForm
         */
        , formValue : function (itemForm) {
            var nameIndex = {} //数组 name 索引
                ,field = {}
                ,fieldElem = itemForm.find('input,select,textarea'); //获取所有表单域

            layui.each(fieldElem, function(_, item){
                item.name = (item.name || '').replace(/^\s*|\s*&/, '');

                if(!item.name) return;

                //用于支持数组 name
                if(/^.*\[\]$/.test(item.name)){
                    var key = item.name.match(/^(.*)\[\]$/g)[0];
                    nameIndex[key] = nameIndex[key] | 0;
                    item.name = item.name.replace(/^(.*)\[\]$/, '$1['+ (nameIndex[key]++) +']');
                }

                if(/^checkbox|radio$/.test(item.type) && !item.checked) return;
                field[item.name] = item.value;
            });

            return field;
        }
        //form自定义验证规则
        , otherReq: function (value, item, text) {
            var verifyName = $(item).attr('name')
                , verifyType = $(item).attr('type')
                , formElem = $(item).parents('.layui-form')//获取当前所在的form元素，如果存在的话
                , verifyElem = formElem.find('input[name=' + verifyName + ']')//获取需要校验的元素
                , isTrue = verifyElem.is(':checked')//是否命中校验
                , focusElem = verifyElem.next().find('i.layui-icon');//焦点元素
            if (!isTrue || !value) {
                //定位焦点
                focusElem.css(verifyType == 'radio' ? {"color": "#FF5722"} : {"border-color": "#FF5722"});
                //对非输入框设置焦点
                focusElem.first().attr("tabIndex", "1").css("outline", "0").blur(function () {
                    focusElem.css(verifyType == 'radio' ? {"color": ""} : {"border-color": ""});
                }).focus();
                return text || '必填项不能为空';
            }
        }
        //获取请求参数
        ,requestParam: function(strParame) {
        var args = {};
        var query = location.search.substring(1);

        var pairs = query.split("&"); // Break at ampersand
        for(var i = 0; i < pairs.length; i++) {
            var pos = pairs[i].indexOf('=');
            if (pos == -1) continue;
            var argname = pairs[i].substring(0,pos);
            var value = pairs[i].substring(pos+1);
            value = decodeURIComponent(value);
            args[argname] = value;
        }
        return args[strParame] == 'undefined' ? '' : args[strParame];
    }
    });

    utils.each(extras, function (method, fn) {
        utils[method] = fn;
    }, function () {
        exp('utils', utils);
    });
});
