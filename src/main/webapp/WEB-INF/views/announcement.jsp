<%--
  Created by IntelliJ IDEA.
  User: 余昕宇
  Date: 2018-06-24
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>查看公告</title>

    <link href="/assets/css/bootstrap.min.css" rel="stylesheet">
    <script src="/assets/js/jquery.min.js"></script>
    <script src="/bootstrap3.3.5/js/bootstrap.js" ></script>
    <script src="http://cdn.bootcss.com/mustache.js/2.2.1/mustache.js"></script>
    <jsp:include page="/common/backend_common.jsp" />

</head>
<body class="no-skin" youdao="bind" style="background: white">
<div class="page-header">
    <h1>
        通知公告
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            查看公告
        </small>
    </h1>
</div>
<div class="container-fluid" >
    <div class="row-fluid">
        <div class="span12">
            <blockquote>
                <p>
                    NOTICE——通知.
                </p> <small><cite>公告</cite></small>
            </blockquote>
        </div>
    </div>
</div>

<div class="panel-group" id="logList" style="margin-left: 50px; margin-right: 50px" ></div>

<script id="logListTemplate" type="x-tmpl-mustache">
{{#logList}}
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#logList"
                   href="#collapse{{id}}">
                    <span class="caret" style="float: right"></span>
                    {{title}}
                </a>
            </h4>
        </div>
        <div id="collapse{{id}}" class="panel-collapse collapse">
            <div class="panel-body">
                详情：{{content}}<br>
                发布人：{{author}}<br>
                发布时间：{{#showDate}}{{/showDate}}

            </div>
        </div>
    </div>
{{/logList}}
</script>

<script type="text/javascript">
    $(function () {
        var logListTemplate = $('#logListTemplate').html();
        Mustache.parse(logListTemplate);
        var logMap = {};

        loadLogList();

        function loadLogList() {
            var url = "/sys/announcement/page.json";
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
                        }
                    });
                    $('#logList').html(rendered);
                    $.each(result.data.data, function (i, log) {
                        logMap[log.id] = log;
                    });
                } else {
                    $('#logList').html('');
                }

                //var pageSize = $("#pageSize").val();
                //var pageNo = $("#logPage .pageNo").val() || 1;
                //renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "logPage", renderLogListAndPage);
            } else {
                showMessage("获取公告列表", result.msg, false);
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
