layui.define(['layer', 'utils', 'form'], function (exp) {
    var axiosUtil = {}
        , utils = parent.utils
        , form = parent.form
        , layer = parent.layer
        , EXPORT_ALL = "a2bfe105b4bcb4a1"
        , EXPORT_SELECT = "b8bc105b2309ba43";

    /**
     * axios请求返回错误数据处理
     *
     * @param error
     */
    axiosUtil.error = function (error) {
        if (error.response) {
            // The request was made and the server responded with a status code
            // that falls out of the range of 2xx
            let data = error.response.data;
            layer.alert("服务器响应错误！"+data.message, {title: data.error+"["+data.status+"]", icon:2});
        } else if (error.request) {
            // The request was made but no response was received
            // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
            // http.ClientRequest in node.js
            layer.msg("网络错误！请求失败!请检查网络之后重试!");
            console.log(error.request);
        } else {
            // Something happened in setting up the request that triggered an Error
            layer.msg("未知错误,请联系管理员!");
            console.log('Error', error.message);
        }
        console.log(error.config);
    };

    /**
     * axios请求返回数据处理
     * @param response 响应
     * @param success 成功函数
     * @param fail 失败函数
     */
    axiosUtil.axiosRes = function (response, success, fail) {
        var res = response.data;
        if (res.code === "0") {
            if (success) {
                if (typeof success === "function") {
                    success(res.data, res.msg);
                } else {
                    layer.msg(success);
                }
            } else {
                layer.msg(res.msg || '操作成功');
            }
        } else {
            if (fail) {
                if (typeof fail === "function") {
                    fail(res.msg);
                } else {
                    layer.msg(fail);
                }
            } else {
                layer.msg(res.msg || '操作失败');
            }
        }
    };

    /**
     * 刷新
     * @param table table刷新
     */
    axiosUtil.reload = function(table, config){
        if(table){
            table.reload(config);
        }else {
            location.reload();
        }
    };

    /**
     * 本地私有 axios请求方法
     *
     * @param type 请求类型 GET/POST
     * @param url 请求url
     * @param params GET请求参数
     * @param data POST请求参数
     * @param success 成功回调函数
     * @param fail 失败回调函数
     * @param done 请求完成回调函数
     * @private
     */
    function _axios(type, url, params, data, success, fail, done){
        if(!url || typeof url !== "string"){
            console.error("参数url缺失");
            return;
        }

        axios({
            method: type,
            url: url,
            params: params ? params : '',
            data: data ? Qs.stringify(data) : ''
        }).then(function (response) {
            axiosUtil.axiosRes(response, success, fail);
            if(done && typeof done === "function"){
                done();
            }
        }).catch(function (error) {
            axiosUtil.error(error);
        });
    }

    /**
     * axios POST请求
     *
     * @param url
     * @param data
     * @param success
     */
    axiosUtil.axios_post = function(url, data, success){
        if(!data){
            console.error("参数data缺失");
            return;
        }
        if(typeof data === "function" && !success){
            success = data;
            data = undefined;
        }

        _axios("post", url, undefined, data, success);
    };

    /**
     * axios GET请求
     *
     * @param url
     * @param param
     * @param success
     */
    axiosUtil.axios_get = function(url, param, success){
        if(typeof param === "function" && !success){
            success = param;
            param = undefined;
        }
        _axios("get", url, param, undefined, success);
    };

    axiosUtil.axios_select = function(config){
        config = $.extend({
            itemArr: [{item: undefined, url: undefined}],
            renderItem: form,
            showInit: true,
            success: function () {}
        }, config);

        var itemArr = config.itemArr;

        for (let k in itemArr){
            let _html = '', item = itemArr[k].item, url = itemArr[k].url;
            if(!item || !url){
                console.error("item/url 参数缺失!");
                continue;
            }

            if(config.showInit){
                _html += '<option value="">请选择</option>';
            }

            axios.get(url).then(function (response) {
                let data = response.data.data;
                for(let i in data){
                    _html += '<option value="'+data[i].val+'">'+data[i].text+'</option>';
                }
                $(item).html(_html);
                config.renderItem.render('select');
            }).catch(function (error) {
                layer.msg("有资源加载失败！");
                console.error(url + "加载失败");
            });
        }
    };

    axiosUtil.toExport = function(url, data, lastQueryConditions){
        //普通导出
        if(data.length === 0){
            if(lastQueryConditions){
                layer.confirm("导出【所有查询】数据？", {title: "请确认"}, function (index) {
                    axiosUtil.export(url, EXPORT_ALL, lastQueryConditions);
                    layer.close(index);
                })
            }else {
                layer.confirm("导出【所有】数据？", {title: "请确认"}, function (index) {
                    axiosUtil.export(url, EXPORT_ALL);
                    layer.close(index);
                })
            }
        }else {
            layer.confirm("导出【选中】数据?", {title: '请确认'}, function (index) {
                let ids = [];
                for (var i in data){
                    ids.push(data[i].id)
                }
                axiosUtil.export(url, EXPORT_SELECT, {ids: ids});
                layer.close(index);
            });
        }
    };

    /**
     * 导出请求
     * @param url
     * @param type
     * @param param
     */
    axiosUtil.export = function(url, type, param){
        if(!url || !type ){
            console.error("url/type is null");
            return;
        }

        axios({
            method: 'POST',
            headers: {"export-type": type},
            url: url,
            data: param,
            responseType: 'blob',
            transformRequest: function(data) {
                return Qs.stringify(data, {arrayFormat: 'repeat'})
            }
        }).then(function (response) {
            console.debug(response)

            let fileName = window.decodeURI(response.headers['content-disposition'].split('=')[1]);

            // new Blob([data])用来创建URL的file对象或者blob对象
            let url = window.URL.createObjectURL(new Blob([response.data]));
            // 生成一个a标签
            let link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.download = fileName;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }).catch(function (error) {
            axiosUtil.error(error);
        })
    };

    exp('axiosUtil', axiosUtil);

});


