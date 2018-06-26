<%--
  Created by IntelliJ IDEA.
  User: yinli
  Date: 2018-06-25
  Time: 15:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传论文</title>
    <link href="/assets/css/bootstrap.min.css" rel="stylesheet">
    <script src="/assets/js/jquery.js"></script>
    <script src="/bootstrap3.3.5/js/bootstrap.js" ></script>
    <%--<script src="/assets/js/bootstrap-select.js"></script>--%>
    <jsp:include page="/common/backend_common.jsp" />
    <script type="text/javascript">
        // $(document).ready(function() {
        //     $("#file").fileinput();
        // });
        // $('#basic2').selectpicker({
        //     liveSearch: true,
        //     maxOptions: 1
        // });
        function checkForm(){
            var t = document.getElementById("title").value;
            var k = document.getElementById("key").value;
            var s = document.getElementById("summary").value;
            var f = document.getElementById("file").value;
            if (f == "" || f == null ||t == "" || k == "" || s == "" || t == null || k == null || s == null) {
                document.getElementById("error").innerHTML="信息填写不完整！";
                return false;
            } else {
                var subt = document.getElementById("submit");
                var rest = document.getElementById("reset");
                subt.disabled = 'disabled';
                rest.disabled = 'disabled';
                document.getElementById("error").innerHTML="正在上传请稍等！";
                return true;
            }
        }

    </script>
    <style type="text/css">

        .file {
            position: relative;
            display: inline-block;
            background: #D0EEFF;
            border: 1px solid #99D3F5;
            border-radius: 4px;
            padding: 4px 12px;
            overflow: hidden;
            color: #1E88C7;
            text-decoration: none;
            text-indent: 0;
            line-height: 20px;
        }
        .file input {
            position: absolute;
            font-size: 100px;
            right: 0;
            top: 0;
            opacity: 0;
        }
        .file:hover {
            background: #AADFFD;
            border-color: #78C3F3;
            color: #004974;
            text-decoration: none;
        }
    </style>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>
<div class="page-header">
    <h1>
        论文系统
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            论文上传
        </small>
    </h1>
</div>
<%--<div>
    <form id="paperForm" action="" method="post" enctype="multipart/form-data" onsubmit="return checkForm()">
        <div class="data_form" >
            <table align="center">
                <tr>
                    <td><font color="red" >*</font>标题：</td>
                    <td><input type="text" id="title"  name="title" value="${title}" class="form-control" style="width: 750px;margin-top:5px;height:30px;" /></td>
                </tr>
                <tr style="height: 30px">
                    <td colspan="2"></td>
                </tr>

                <tr>
                    <td>&nbsp;类型：</td>
                    <td>
                        <select id="type" class="show-tick form-control" size="" name="type" id="type">
                            <option value="14">农林牧渔、卫生、科学研究</option>
                            <option value="15">建筑、能源、冶炼、交通运输</option>
                            <option value="17"> 制造、信息技术、贸易</option>
                            <option value="18">党政、社团、国防、法律、金融</option>
                            <option value="19">教育、公共文化、社会服务</option>
                        </select>
                    </td>
                </tr>
                <tr style="height: 20px">
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><font color="red">*</font>附件：</td>
                    <td>
                        <sapn class="file">
                            <input style="width: 300px" class="file" type="file" id="file" name="file" data-allowed-file-extensions='["pdf","docx","doc","txt","jpg","gif","zip","rar","7z","png","jpeg","rtf","html"]'>
                            点击此处选择文件
                        </sapn>
                    </td>

                </tr>
            </table>
            <div align="center" style="margin-top: 30px">
                <button id="submit" type="submit" class="btn btn-primary research" >上传</button>
                &nbsp;<button id="reset" class="btn btn-primary" type="reset" >重置</button>
            </div>
            <div style="margin-top: 30px" align="center">
                <font id="error" color="red">${error}</font>
            </div>
        </div>
    </form>
</div>--%>
<div class="container">
    <form action="#" class="form-horizontal" enctype="multipart/form-data">
        <div class="form-group">
            <span class="col-sm-2 text-right">标题：</span>
            <div class="col-sm-10">
                <input type="text" id="title"  name="title" value="${title}" class="form-control" placeholder="请输入标题..." />
            </div>
        </div>
        <div class="form-group">
            <span class="col-sm-2 text-right"> 类型：</span>
            <div class="col-sm-10">
                <select class="show-tick form-control" size="" name="type" id="type" >
                    <option value="14">农林牧渔、卫生、科学研究</option>
                    <option value="15">建筑、能源、冶炼、交通运输</option>
                    <option value="17"> 制造、信息技术、贸易</option>
                    <option value="18">党政、社团、国防、法律、金融</option>
                    <option value="19">教育、公共文化、社会服务</option>
                </select>
                <%--<input class="form-control" type="email" placeholder="请输入你的邮箱"/>--%>
            </div>
        </div>
        <div class="form-group">
            <span class="col-sm-2 text-right">附件：</span>
            <div class="col-sm-10">
                <sapn class="file">
                    <input style="width: 300px" class="file" type="file" id="file" name="file" data-allowed-file-extensions='["pdf","docx","doc","txt","jpg","gif","zip","rar","7z","png","jpeg","rtf","html"]'>
                    点击此处选择文件
                </sapn>
                <%--<input type="text" id="title"  name="title" value="${title }" class="form-control" placeholder="请输入标题..." />--%>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-4" >
                <input id="submit" type="submit" class="btn btn-primary research" value="上传"/>
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
            var type = $("#type").val();
            var url = "/sys/paper/save.json";

            let formData = new FormData();
            formData.append("file", document.getElementById("file").files[0]);
            formData.append("type", type);
            formData.append("title", title);
            $.ajax({
                url: url,
                data: formData,
                    // title: title,
                    // type: type
                processData: false,
                contentType: false,
                type: 'POST',
                success: function (result) {
                   if (result.ret) {
                       showMessage("文件上传", "操作成功", false);
                   } else {
                       showMessage("文件上传", result.msg, false);
                   }
                }
            });
        }

    });
</script>
</body>
</html>
