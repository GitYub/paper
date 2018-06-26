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
    <title>发布公告</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/assets/css/bootstrap-responsive.css" rel="stylesheet">

    <link href="/assets/css/bootstrap.min.css" rel="stylesheet">
    <script src="/assets/js/jquery.min.js"></script>
    <script src="/bootstrap3.3.5/js/bootstrap.js" ></script>
    <jsp:include page="/common/backend_common.jsp" />
    <script type="text/javascript">
        $(document).ready(function() {

        });
        function checkForm(){
            var t = document.getElementById("title").value;
            var s = document.getElementById("content").value;
            if (t == "" || s == "" || t == null || s == null) {
                document.getElementById("error").innerHTML="信息填写不完整！";
                return false;
            } else {
                var subt = document.getElementById("submit");
                var rest = document.getElementById("reset");
                subt.disabled = 'disabled';
                rest.disabled = 'disabled';
                document.getElementById("error").innerHTML="正在发布请稍等！";
                return true;
            }
        }

    </script>

</head>
<body>
<div class="page-header">
    <h1>
        通知公告
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            发布公告
        </small>
    </h1>
</div>
<div class="container-fluid">
    <form action="" method="post"  onsubmit="return checkForm()">
        <div class="data_form" >
            <table align="center">
                <tr>
                    <td style="width: 10%"><font color="red">*</font>标题：</td>
                    <td><input type="text" id="title"  name="title" class="form-control" /></td>
                </tr>
                <tr style="height: 30px">
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td style="width: 10%">&nbsp;内容：</td>
                    <td><textarea id="content" name="content" class="form-control" style="width: 70%" rows="7"></textarea></td>
                </tr>

            </table>

            <div align="center" style="margin-top: 30px">
                <input id="submit" type="submit" class="btn btn-primary research" value="发布"/>
                &nbsp;<button id="reset" class="btn btn-primary" type="reset" >重置</button>
            </div>
            <div style="margin-top: 30px" align="center">
                <font id="error" color="red">${error}</font>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {

        $(".research").click(function (e) {
            e.preventDefault();
            loadLogList();
        });

        function loadLogList() {
            var title = $("#title").val();
            var content = $("#content").val();
            var url = "/sys/announcement/save.json";

            $.ajax({
                url: url,
                data: {
                    title: title,
                    content: content
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        showMessage("发布公告", "操作成功", true);
                    } else {
                        showMessage("发布公告", result.msg, false);
                    }
                }
            });
        }

    });
</script>

</body>
</html>
