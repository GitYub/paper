<%--
  Created by IntelliJ IDEA.
  User: 余昕宇
  Date: 2018-06-18
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>模块管理</title>
    <jsp:include page="../../common/backend_common.jsp" />
    <jsp:include page="../../common/page.jsp" />
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        用户管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护模块与用户关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            模块列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 module-add"></i>
            </a>
        </div>
        <div id="moduleList">
        </div>
    </div>
    <div class="col-sm-9">
        <div class="col-xs-12">
            <div class="table-header">
                用户列表&nbsp;&nbsp;
                <a class="green" href="#">
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 user-add"></i>
                </a>
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="dataTables_length" id="dynamic-table_length"><label>
                                展示
                                <select id="pageSize" name="dynamic-table_length" aria-controls="dynamic-table" class="form-control input-sm">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select> 条记录 </label>
                            </div>
                        </div>
                    </div>
                    <table id="dynamic-table" class="table table-striped table-bordered table-hover dataTable no-footer" role="grid"
                           aria-describedby="dynamic-table_info" style="font-size:14px">
                        <thead>
                        <tr role="row">
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                姓名
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                所属模块
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                邮箱
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                电话
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                状态
                            </th>
                            <th class="sorting_disabled" rowspan="1" colspan="1" aria-label=""></th>
                        </tr>
                        </thead>
                        <tbody id="userList"></tbody>
                    </table>
                    <div class="row" id="userPage">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-module-form" style="display: none;">
    <form id="moduleForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">上级模块</label></td>
                <td>
                    <select id="parentId" name="parentId" data-placeholder="选择模块" style="width: 200px;"></select>
                    <input type="hidden" name="id" id="moduleId"/>
                </td>
            </tr>
            <tr>
                <td><label for="moduleName">名称</label></td>
                <td><input type="text" name="name" id="moduleName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="moduleSeq">顺序</label></td>
                <td><input type="text" name="seq" id="moduleSeq" value="1" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="moduleRemark">备注</label></td>
                <td><textarea name="remark" id="moduleRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<div id="dialog-user-form" style="display: none;">
    <form id="userForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">所在模块</label></td>
                <td>
                    <select id="moduleSelectId" name="moduleId" data-placeholder="选择模块" style="width: 200px;"></select>
                </td>
            </tr>
            <tr>
                <td><label for="userName">名称</label></td>
                <input type="hidden" name="id" id="userId"/>
                <td><input type="text" name="username" id="userName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userEmail">邮箱</label></td>
                <td><input type="text" name="email" id="userEmail" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userTelephone">电话</label></td>
                <td><input type="text" name="telephone" id="userTelephone" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userStatus">状态</label></td>
                <td>
                    <select id="userStatus" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                        <option value="2">删除</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="userRemark">备注</label></td>
                <td><textarea name="remark" id="userRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>

<script id="moduleListTemplate" type="x-tmpl-mustache">
<ol class="dd-list">
    {{#moduleList}}
        <li class="dd-item dd2-item module-name" id="module_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <div class="dd2-content" style="cursor:pointer;">
            {{name}}
            <span style="float:right;">
                <a class="green module-edit" href="#" data-id="{{id}}" >
                    <i class="ace-icon fa fa-pencil bigger-100"></i>
                </a>
                &nbsp;
                <a class="red module-delete" href="#" data-id="{{id}}" data-name="{{name}}">
                    <i class="ace-icon fa fa-trash-o bigger-100"></i>
                </a>
            </span>
            </div>
        </li>
    {{/moduleList}}
</ol>
</script>
<script id="userListTemplate" type="x-tmpl-mustache">
{{#userList}}
<tr role="row" class="user-name odd" data-id="{{id}}"><!--even -->
    <td><a href="#" class="user-edit" data-id="{{id}}">{{username}}</a></td>
    <td>{{showModuleName}}</td>
    <td>{{email}}</td>
    <td>{{telephone}}</td>
    <td>{{#bold}}{{showStatus}}{{/bold}}</td> <!-- 此处套用函数对status做特殊处理 -->
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green user-edit" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-pencil bigger-100"></i>
            </a>
            <a class="red user-acl" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-flag bigger-100"></i>
            </a>
        </div>
    </td>
</tr>
{{/userList}}
</script>

<script type="application/javascript">
    $(function() {

        var moduleList; // 存储树形模块列表
        var moduleMap = {}; // 存储map格式的模块信息
        var userMap = {}; // 存储map格式的用户信息
        var optionStr = "";
        var lastClickModuleId = -1;

        var moduleListTemplate = $('#moduleListTemplate').html();
        Mustache.parse(moduleListTemplate);
        var userListTemplate = $('#userListTemplate').html();
        Mustache.parse(userListTemplate);

        loadModuleTree();

        function loadModuleTree() {
            $.ajax({
                url: "/sys/module/tree.json",
                success : function (result) {
                    if (result.ret) {
                        moduleList = result.data;
                        var rendered = Mustache.render(moduleListTemplate, {moduleList: result.data});
                        $("#moduleList").html(rendered);
                        recursiveRenderModule(result.data);
                        bindModuleClick();
                    } else {
                        showMessage("加载模块列表", result.msg, false);
                    }
                }
            })
        }

        // 递归渲染模块树
        function recursiveRenderModule(moduleList) {
            if(moduleList && moduleList.length > 0) {
                $(moduleList).each(function (i, module) {
                    moduleMap[module.id] = module;
                    if (module.moduleList.length > 0) {
                        var rendered = Mustache.render(moduleListTemplate, {moduleList: module.moduleList});
                        $("#module_" + module.id).append(rendered);
                        recursiveRenderModule(module.moduleList);
                    }
                })
            }
        }

        // 绑定模块点击事件
        function bindModuleClick() {

            $(".module-name").click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                var moduleId = $(this).attr("data-id");
                handleDepSelected(moduleId);
            });

            $(".module-delete").click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var moduleId = $(this).attr("data-id");
                var moduleName = $(this).attr("data-name");
                if (confirm("确定要删除模块[" + moduleName + "]吗?")) {
                    $.ajax({
                        url: "/sys/module/delete.json",
                        data: {
                            id: moduleId
                        },
                        success: function (result) {
                            if (result.ret) {
                                showMessage("删除模块[" + moduleName + "]", "操作成功", true);
                                loadModuleTree();
                            } else {
                                showMessage("删除模块[" + moduleName + "]", result.msg, false);
                            }
                        }
                    });
                }
            });

            $(".module-edit").click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                var moduleId = $(this).attr("data-id");
                $("#dialog-module-form").dialog({
                    model: true,
                    title: "编辑模块",
                    open: function(event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        optionStr = "<option value=\"0\">-</option>";
                        recursiveRenderModuleSelect(moduleList, 1);
                        $("#moduleForm")[0].reset();
                        $("#parentId").html(optionStr);
                        $("#moduleId").val(moduleId);
                        var targetModule = moduleMap[moduleId];
                        if (targetModule) {
                            $("#parentId").val(targetModule.parentId);
                            $("#moduleName").val(targetModule.name);
                            $("#moduleSeq").val(targetModule.seq);
                            $("#moduleRemark").val(targetModule.remark);
                        }
                    },
                    buttons : {
                        "更新": function(e) {
                            e.preventDefault();
                            updateModule(false, function (data) {
                                $("#dialog-module-form").dialog("close");
                            }, function (data) {
                                showMessage("更新模块", data.msg, false);
                            })
                        },
                        "取消": function () {
                            $("#dialog-module-form").dialog("close");
                        }
                    }
                });
            })
        }

        function handleDepSelected(moduleId) {
            if (lastClickModuleId != -1) {
                var lastModule = $("#module_" + lastClickModuleId + " .dd2-content:first");
                lastModule.removeClass("btn-yellow");
                lastModule.removeClass("no-hover");
            }
            var currentModule = $("#module_" + moduleId + " .dd2-content:first");
            currentModule.addClass("btn-yellow");
            currentModule.addClass("no-hover");
            lastClickModuleId = moduleId;
            loadUserList(moduleId);
        }

        function loadUserList(moduleId) {
            var pageSize = $("#pageSize").val();
            var url = "/sys/user/page.json?moduleId=" + moduleId;
            var pageNo = $("#userPage .pageNo").val() || 1;
            $.ajax({
                url : url,
                data: {
                    pageSize: pageSize,
                    pageNo: pageNo
                },
                success: function (result) {
                    renderUserListAndPage(result, url);
                }
            })
        }

        function renderUserListAndPage(result, url) {
            if (result.ret) {
                if (result.data.total > 0){
                    var rendered = Mustache.render(userListTemplate, {
                        userList: result.data.data,
                        "showModuleName": function() {
                            return moduleMap[this.moduleId].name;
                        },
                        "showStatus": function() {
                            return this.status == 1 ? '有效' : (this.status == 0 ? '无效' : '删除');
                        },
                        "bold": function() {
                            return function(text, render) {
                                var status = render(text);
                                if (status == '有效') {
                                    return "<span class='label label-sm label-success'>有效</span>";
                                } else if(status == '无效') {
                                    return "<span class='label label-sm label-warning'>无效</span>";
                                } else {
                                    return "<span class='label'>删除</span>";
                                }
                            }
                        }
                    });
                    $("#userList").html(rendered);
                    bindUserClick();
                    $.each(result.data.data, function(i, user) {
                        userMap[user.id] = user;
                    })
                } else {
                    $("#userList").html('');
                }
                var pageSize = $("#pageSize").val();
                var pageNo = $("#userPage .pageNo").val() || 1;
                renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "userPage", renderUserListAndPage);
            } else {
                showMessage("获取模块下用户列表", result.msg, false);
            }
        }

        $(".user-add").click(function() {
            $("#dialog-user-form").dialog({
                model: true,
                title: "新增用户",
                open: function(event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    optionStr = "";
                    recursiveRenderModuleSelect(moduleList, 1);
                    $("#userForm")[0].reset();
                    $("#moduleSelectId").html(optionStr);
                },
                buttons : {
                    "添加": function(e) {
                        e.preventDefault();
                        updateUser(true, function (data) {
                            $("#dialog-user-form").dialog("close");
                            loadUserList(lastClickModuleId);
                        }, function (data) {
                            showMessage("新增用户", data.msg, false);
                        })
                    },
                    "取消": function () {
                        $("#dialog-user-form").dialog("close");
                    }
                }
            });
        });
        function bindUserClick() {
            $(".user-acl").click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var userId = $(this).attr("data-id");
                $.ajax({
                    url: "/sys/user/acls.json",
                    data: {
                        userId: userId
                    },
                    success: function(result) {
                        if (result.ret) {
                            console.log(result)
                        } else {
                            showMessage("获取用户权限数据", result.msg, false);
                        }
                    }
                })
            });
            $(".user-edit").click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                var userId = $(this).attr("data-id");
                $("#dialog-user-form").dialog({
                    model: true,
                    title: "编辑用户",
                    open: function(event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        optionStr = "";
                        recursiveRenderModuleSelect(moduleList, 1);
                        $("#userForm")[0].reset();
                        $("#moduleSelectId").html(optionStr);

                        var targetUser = userMap[userId];
                        if (targetUser) {
                            $("#moduleSelectId").val(targetUser.moduleId);
                            $("#userName").val(targetUser.username);
                            $("#userEmail").val(targetUser.email);
                            $("#userTelephone").val(targetUser.telephone);
                            $("#userStatus").val(targetUser.status);
                            $("#userRemark").val(targetUser.remark);
                            $("#userId").val(targetUser.id);
                        }
                    },
                    buttons : {
                        "更新": function(e) {
                            e.preventDefault();
                            updateUser(false, function (data) {
                                $("#dialog-user-form").dialog("close");
                                loadUserList(lastClickModuleId);
                            }, function (data) {
                                showMessage("更新用户", data.msg, false);
                            })
                        },
                        "取消": function () {
                            $("#dialog-user-form").dialog("close");
                        }
                    }
                });
            });
        }

        $(".module-add").click(function() {
            $("#dialog-module-form").dialog({
                model: true,
                title: "新增模块",
                open: function(event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    optionStr = "<option value=\"0\">-</option>";
                    recursiveRenderModuleSelect(moduleList, 1);
                    $("#moduleForm")[0].reset();
                    $("#parentId").html(optionStr);
                },
                buttons : {
                    "添加": function(e) {
                        e.preventDefault();
                        updateModule(true, function (data) {
                            $("#dialog-module-form").dialog("close");
                        }, function (data) {
                            showMessage("新增模块", data.msg, false);
                        })
                    },
                    "取消": function () {
                        $("#dialog-module-form").dialog("close");
                    }
                }
            });
        });

        function recursiveRenderModuleSelect(moduleList, level) {
            level = level | 0;
            if (moduleList && moduleList.length > 0) {
                $(moduleList).each(function (i, module) {
                    moduleMap[module.id] = module;
                    var blank = "";
                    if (level > 1) {
                        for(var j = 3; j <= level; j++) {
                            blank += "..";
                        }
                        blank += "∟";
                    }
                    optionStr += Mustache.render("<option value='{{id}}'>{{name}}</option>", {id: module.id, name: blank + module.name});
                    if (module.moduleList && module.moduleList.length > 0) {
                        recursiveRenderModuleSelect(module.moduleList, level + 1);
                    }
                });
            }
        }

        function updateUser(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? "/sys/user/save.json" : "/sys/user/update.json",
                data: $("#userForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.ret) {
                        loadModuleTree();
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            })
        }

        function updateModule(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? "/sys/module/save.json" : "/sys/module/update.json",
                data: $("#moduleForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.ret) {
                        loadModuleTree();
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            })
        }
    })
</script>
</body>
</html>
