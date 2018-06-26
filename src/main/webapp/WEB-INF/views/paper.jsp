<%--
  Created by IntelliJ IDEA.
  User: 余昕宇
  Date: 2018-06-23
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>论文浏览</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="/assets/css/bootstrap-datetimepicker.css" rel="stylesheet">

    <script src="/assets/js/jquery.min.js"></script>
    <script src="/bootstrap3.3.5/js/bootstrap.js" ></script>
    <script src="/assets/js/date-time/bootstrap-datetimepicker.fr.js"></script>
    <script src="http://cdn.bootcss.com/mustache.js/2.2.1/mustache.js"></script>

    <link href="/assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/dataTables.bootstra.css">
    <script type="text/javascript" src="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/jquery.js"></script>
    <script type="text/javascript" src="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/jquery.dataTables.js"></script>
    <script type="text/javascript" src="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/bootstrap.min.js"></script>
    <script type="text/javascript" src="http://sandbox.runjs.cn/uploads/rs/238/n8vhm36h/dataTables.bootstrap.js"></script>

    <jsp:include page="/common/backend_common.jsp" />
    <jsp:include page="/common/page.jsp"/>
</head>
<body>
<div class="page-header">
    <h1>
        论文系统
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            查看论文
        </small>
    </h1>
</div>
<div class="data_list" >
    <div style="margin-top: 40px;" class="col-md-10 col-md-push-1">
        <label>
            展示<select id="pageSize" name="dynamic-table_length" aria-controls="dynamic-table" class="form-control input-sm">
                <option value="10">10</option>
                <option value="25">25</option>
                <option value="50">50</option>
                <option value="100">100</option>
            </select> 条记录 </label>
            <table  class="table table-striped table-bordered table-hover datatable" >
                <thead>
                <tr>
                    <th>标题</th>
                    <th>作者</th>
                    <th>上传时间</th>
                    <th>所属版块</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="logList">

                </tbody>
                <tr>
                    <td colspan="5" >
                        <div class="row" id="logPage">
                        </div>
                    </td></tr>
            </table>
        </div>
        <div align="center"><font color="red">${error}</font></div>

    </div>

<script id="logListTemplate" type="x-tmpl-mustache">
{{#logList}}
    <tr>
        <td>{{title}}</td>
        <td>{{author}}</td>
        <td>{{#showDate}}{{/showDate}}</td>
        <td>{{#showModule}}{{/showModule}}</td>
        <td>
            <a href="{{url}}" ><button class="btn btn-mini btn-success" type="button" onclick="">浏览</button></a>
        </td>
    </tr>
{{/logList}}
</script>
<script type="text/javascript">
    $(function () {
        var logListTemplate = $('#logListTemplate').html();
        Mustache.parse(logListTemplate);
        var logMap = {};

        loadLogList();

        function loadLogList() {
            var url = "/sys/paper/page.json";
            $.ajax({
                url: url,
                type: 'POST',
                success: function (result) {
                    renderLogListAndPage(result, url);
                }
            });
        }

        function renderLogListAndPage(result, url) {
            if (result.ret) {
                if (result.data.total > 0) {
                    var rendered = Mustache.render(logListTemplate, {
                        "logList": result.data.data,
                        "showDate" :function () {
                            return function (text, render) {
                                return new Date(this.uploadTime).Format("yyyy-MM-dd hh:mm:ss");
                            }
                        },
                        "showModule": function() {
                            return function (text, render) {
                                var id = this.moduleId;
                                if(id==14)
                                return new String("农林牧渔、卫生、科学研究");
                                else if (id==16)
                                    return new String("建筑、能源、冶炼、交通运输");
                                else if (id==17)
                                    return new String("制造、信息技术、贸易");
                                else if(id==18)
                                    return new String("党政、社团、国防、法律、金融");
                                else if(id==19)
                                    return new String("教育、公共文化、社会服务");
                                else
                                    return new String("我也不知道这是什么JB文章");
                            }
                        }
                    });
                    $('#logList').html(rendered);
                    $.each(result.data.data, function (i, log) {
                        logMap[log.id] = log;
                    });
                } else {
                    $('#logList').html('');
                }

                var pageSize = $("#pageSize").val();
                var pageNo = $("#logPage .pageNo").val() || 1;
                renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "logPage", renderLogListAndPage);
            } else {
                showMessage("获取论文列表", result.msg, false);
            }
        }


        Date.prototype.Format = function (fmt) { //author: meizz
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
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        };
        var formatJson = function(json, options) {
            if(json == '') return '';
            var reg = null,
                formatted = '',
                pad = 0,
                PADDING = '    '; // one can also use '\t' or a different number of spaces

            // optional settings
            options = options || {};
            // remove newline where '{' or '[' follows ':'
            options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
            // use a space after a colon
            options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

            // begin formatting...
            if (typeof json !== 'string') {
                // make sure we start with the JSON as a string
                json = JSON.stringify(json);
            } else {
                // is already a string, so parse and re-stringify in order to remove extra whitespace
                json = JSON.parse(json);
                json = JSON.stringify(json);
            }

            // add newline before and after curly braces
            reg = /([\{\}])/g;
            json = json.replace(reg, '\r\n$1\r\n');

            // add newline before and after square brackets
            reg = /([\[\]])/g;
            json = json.replace(reg, '\r\n$1\r\n');

            // add newline after comma
            reg = /(\,)/g;
            json = json.replace(reg, '$1\r\n');

            // remove multiple newlines
            reg = /(\r\n\r\n)/g;
            json = json.replace(reg, '\r\n');

            // remove newlines before commas
            reg = /\r\n\,/g;
            json = json.replace(reg, ',');

            // optional formatting...
            if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
                reg = /\:\r\n\{/g;
                json = json.replace(reg, ':{');
                reg = /\:\r\n\[/g;
                json = json.replace(reg, ':[');
            }
            if (options.spaceAfterColon) {
                reg = /\:/g;
                json = json.replace(reg, ': ');
            }

            $.each(json.split('\r\n'), function(index, node) {
                var i = 0,
                    indent = 0,
                    padding = '';

                if (node.match(/\{$/) || node.match(/\[$/)) {
                    indent = 1;
                } else if (node.match(/\}/) || node.match(/\]/)) {
                    if (pad !== 0) {
                        pad -= 1;
                    }
                } else {
                    indent = 0;
                }

                for (i = 0; i < pad; i++) {
                    padding += PADDING;
                }

                formatted += padding + node + '\r\n';
                pad += indent;
            });
            return formatted;
        };

    });
</script>
</body>
</html>
