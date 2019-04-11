$(function () {
    initLogin();
})

function initLogin() {
    $('#treeLeft').tree({    //初始化左侧功能树（不同用户显示的树是不同的）
        method: 'GET',
        url: 'ashx/bg_menu.ashx'/*tpa=http://www.beidouguard.com:6010/Precise/js/ashx/bg_menu.ashx*/,
        lines: true,
        onClick: function (node) {    //点击左侧的tree节点  打开右侧tabs显示内容
            if (node.attributes) {
                addTab(node.text, node.attributes.url, node.iconCls);
            }
        }
    });

    $.ajax({
        url: "ashx/bg_user_login.ashx"/*tpa=http://www.beidouguard.com:6010/Precise/js/ashx/bg_user_login.ashx*/,
        type: "post",
        data: { action: "getuser" },
        dataType: "json",
        success: function (result) {
            if (result.success) {
                //console.log(result.msg);    //当前用户对象
                $("#div_welcome").html("当前登录用户：" + result.msg.UserName);
                if (!result.msg.IfChangePwd) {    //如果是首次登陆必须重置密码
                    $("<div/>").dialog({
                        id: "ui_user_userfirstlogin_dialog",   //给dialog一个id，操作完好销毁，否则一直在html里
                        href: "html/ui_user_firstlogin.html"/*tpa=http://www.beidouguard.com:6010/Precise/js/html/ui_user_firstlogin.html*/,
                        title: "首次登陆需重置密码",
                        height: 160,
                        width: 360,
                        modal: true,
                        closable: false,
                        buttons: [{
                            id: "ui_user_userfirstlogin_edit",   //给button一个id 方便控制其可用和不可用
                            text: '修 改',
                            handler: function () {
                                $("#ui_user_userfirstlogin_form").form("submit", {
                                    url: "ashx/bg_user.ashx"/*tpa=http://www.beidouguard.com:6010/Precise/js/ashx/bg_user.ashx*/,
                                    onSubmit: function (param) {
                                        $('#ui_user_userfirstlogin_edit').linkbutton('disable');  //点击就不可用，防止连击
                                        param.action = 'firstlogin';
                                        //return $(this).form('validate');   //这么验证如果出错无法恢复按钮状态
                                        if ($(this).form('validate'))
                                            return true;
                                        else {
                                            $('#ui_user_userfirstlogin_edit').linkbutton('enable');   //恢复按钮
                                            return false;
                                        }
                                    },
                                    success: function (data) {
                                        $('#ui_user_userfirstlogin_edit').linkbutton('enable');   //恢复按钮
                                        var dataBack = $.parseJSON(data);   //序列化成对象，否则是字符串
                                        if (dataBack.success) {
                                            $("#ui_user_userfirstlogin_dialog").dialog('destroy');  //销毁dialog对象
                                            $.show_warning("提示", dataBack.msg);
                                        }
                                        else {
                                            $('#ui_user_userfirstlogin_edit').linkbutton('enable');
                                            $.show_warning("提示", dataBack.msg);
                                        }
                                    }
                                });
                            }
                        }, {
                            text: '退 出',
                            handler: function () { loginOut(); }
                        }],
                        onLoad: function () {
                            $("#ui_user_firstlogin_pwd").focus();    //聚焦密码框
                            $("#ui_user_firstlogin_id").val(result.msg.Id);
                        }
                    });
                }
            }
            else {
                //直接访问index页面没有cookie不会发这个ajax请求的，而是被FormsAuthentication带到了登录页面了
                //这个else是有cookie，但是cookie里的用户再次验证的时候发现数据库里的当前用户已经修改密码/设置不可用等，然后干掉了cookie
                window.location.href = "login.html"/*tpa=http://www.beidouguard.com:6010/Precise/js/login.html*/;
            }
        }
    });
}

function addTab(subtitle, url, icon) {
    if (!$('#tabs').tabs('exists', subtitle)) {
        $('#tabs').tabs('add', {
            title: subtitle,
            href: url,
            iconCls: icon,
            closable: true,
            loadingMessage: '正在加载中......'
        });
    } else {
        $('#tabs').tabs('select', subtitle);
    }
}

function refreshTab() {
    var index = $('#tabs').tabs('getTabIndex', $('#tabs').tabs('getSelected'));
    if (index != -1) {
        $('#tabs').tabs('getTab', index).panel('refresh');
    }
}

function closeTab() {
    $('.tabs-inner span').each(function (i, n) {
        var t = $(n).text();
        if (t != '') {
            $('#tabs').tabs('close', t);
        }
    });
}

//查看当前用户信息
function searchMyInfo() {
    $("<div/>").dialog({
        id: "ui_myinfo_dialog",
        href: "html/ui_myinfo.html"/*tpa=http://www.beidouguard.com:6010/Precise/js/html/ui_myinfo.html*/,
        title: "我的信息",
        height: 500,
        width: 580,
        modal: true,
        onLoad: function () {
            $.ajax({
                url: "ashx/bg_user.ashx?action=getUserInfo&timespan=" + new Date().getTime(),
                type: "post",
                dataType: "json",
                success: function (result) {
                    $("#ui_myinfo_userid").html(result[0].UserId);
                    $("#ui_myinfo_username").html(result[0].UserName);
                    $("#ui_myinfo_adddate").html(result[0].AddDate);
                    $("#ui_myinfo_roles").html(result[0].RoleName.length > 12 ? "<span title=" + result[0].RoleName + ">" + result[0].RoleName.substring(0, 12) + "...</span>" : result[0].RoleName);
                    $("#ui_myinfo_departments").html(result[0].DepartmentName.length > 12 ? "<span title=" + result[0].DepartmentName + ">" + result[0].DepartmentName.substring(0, 12) + "...</span>" : result[0].DepartmentName);
                    //长度超过12个字符就隐藏
                }
            });

            $('#ui_myinfo_authority').tree({
                url: "ashx/bg_menu.ashx?action=getMyAuthority&timespan=" + new Date().getTime(),
                onlyLeafCheck: true,
                checkbox: true
            });
        },
        onClose: function () {
            $("#ui_myinfo_dialog").dialog('destroy');  //销毁dialog对象
        }
    });
}

//修改密码
function changePwd() {
    $("<div/>").dialog({
        id: "ui_user_userchangepwd_dialog",
        href: "html/ui_user_changepwd.html"/*tpa=http://www.beidouguard.com:6010/Precise/js/html/ui_user_changepwd.html*/,
        title: "修改密码",
        height: 240,
        width: 380,
        modal: true,
        closable: false,
        buttons: [{
            id: "ui_user_userchangepwd_edit",
            text: '修 改',
            handler: function () {
                $("#ui_user_userchangepwd_form").form("submit", {
                    url: "ashx/bg_user.ashx"/*tpa=http://www.beidouguard.com:6010/Precise/js/ashx/bg_user.ashx*/,
                    onSubmit: function (param) {
                        $('#ui_user_userchangepwd_edit').linkbutton('disable');  //点击就不可用，防止连击
                        param.action = 'changepwd';
                        if ($(this).form('validate'))
                            return true;
                        else {
                            $('#ui_user_userchangepwd_edit').linkbutton('enable');   //恢复按钮
                            return false;
                        }
                    },
                    success: function (data) {
                        $('#ui_user_userchangepwd_edit').linkbutton('enable');   //恢复按钮
                        var dataBack = $.parseJSON(data);    //序列化成对象
                        if (dataBack.success) {
                            //$("#ui_user_userchangepwd_dialog").dialog('destroy');  //销毁dialog对象（已跳转，不需要销毁了）
                            //$.show_warning("提示", dataBack.msg);
                            alert(dataBack.msg);
                            window.location.href = "login.html"/*tpa=http://www.beidouguard.com:6010/Precise/js/login.html*/;
                        }
                        else {
                            $('#ui_user_userchangepwd_edit').linkbutton('enable');
                            $.show_warning("提示", dataBack.msg);
                        }
                    }
                });
            }
        }, {
            text: '取 消',
            handler: function () { $("#ui_user_userchangepwd_dialog").dialog('destroy'); }
        }],
        onLoad: function () {
            $("#ui_user_userchangepwd_originalpwd").focus();
        }
    });
}

//退出系统
function loginOut() {
    //$.messager.confirm('提示！', '确定退出系统？', function (r) {
    //if(r){
    if (confirm("确定退出当前陆登账户？")) {
        var para = { "action": "logout" };
        $.ajax({
            url: "ashx/bg_user_login.ashx"/*tpa=http://www.beidouguard.com:6010/Precise/js/ashx/bg_user_login.ashx*/,
            type: "post",
            data: para,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    window.location.href = "login.html"/*tpa=http://www.beidouguard.com:6010/Precise/js/login.html*/;
                }
                else {
                    $.show_warning("提示", result.msg);
                }
            }
        })
    }
    //}
    //})
}