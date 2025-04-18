<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<title>Fix</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href=".//layui/css/layui.css" rel="stylesheet">
<script src=".//layui/layui.js"></script>
<script src=".//js/jquery-3.6.0.min.js"></script>
<style>
/* 隐藏横向滚动条 */
body {
	overflow-x: hidden;
}
</style>
</head>

<body>
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo layui-hide-xs layui-bg-black">FIX</div>
			<!-- 头部区域（可配合layui 已有的水平导航） -->
			<ul class="layui-nav layui-layout-left">
				<!-- 移动端显示 -->
				<li class="layui-nav-item layui-show-xs-inline-block layui-hide-sm"
					lay-header-event="menuLeft"><i
					class="layui-icon layui-icon-spread-left"></i></li>
				<li class="layui-nav-item layui-hide-xs"><a href="javascript:;">首页</a></li>
				<li class="layui-nav-item"><a href="javascript:;">维修指南</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="Article/PhoneArticle.html">手机</a>
						</dd>
						<dd>
							<a href="Article/LaptopArticle.html">笔记本电脑</a>
						</dd>
						<dd>
							<a href="Article/Console.html">游戏主机</a>
						</dd>
						<dd>
							<a href="Article/AllArticle.html">所有文章</a>
						</dd>
					</dl></li>
				<li class="layui-nav-item layui-hide-xs"><a href="javascript:;">nav
						1</a></li>
				<li class="layui-nav-item"><a href="javascript:;">创作中心</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="">创作</a>
						</dd>
						<dd>
							<a href="Article/MyArticle.html">我的文章</a>
						</dd>
					</dl></li>
			</ul>
			<ul class="layui-nav layui-layout-right">
				<li class="layui-nav-item layui-hide layui-show-sm-inline-block">
					<a href="javascript:;"> <img src="" class="layui-nav-img"
						id="top-username">
				</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="Personal/PersonalInfo.html">个人信息</a>
						</dd>
						<dd>
							<a href="Personal/EditPassword.html">修改密码</a>
						</dd>
						<dd>
							<a href="javascript:void(0);" id="logout-link">退出登录</a>
						</dd>
					</dl>
				</li>
				<li class="layui-nav-item" lay-header-event="menuRight" lay-unselect>
					<a href="javascript:;"> <i
						class="layui-icon layui-icon-more-vertical"></i>
				</a>
				</li>
			</ul>
		</div>
		<div class="layui-body">
			<!-- 内容主体区域 -->
			<div style="width: 100%; height: 90vh; padding: 10px;">
				<iframe src="zhanwei.html" id="main" height="100%" width="90%"
					scrolling="no" style="border: none;"></iframe>
			</div>
		</div>

		<script>
            layui.use(['element', 'layer', 'util'], function () {
                var element = layui.element;
                var layer = layui.layer;
                var util = layui.util;
                var $ = layui.jquery;

                // 简洁的 AJAX 请求获取头像地址
                $.getJSON('SelectPersonalInfoServlet', function (response) {
                    var avatar = response.data[0].avatar || "";
                    $("#top-username").attr("src", avatar);
                }).fail(function () {
                    layer.msg('获取头像信息失败', { icon: 2, time: 2000 });
                });

                // 头部事件
                util.event('lay-header-event', {
                    menuRight: function () { // 右侧菜单事件
                        layer.open({
                            type: 1,
                            title: '更多',
                            content: '<div style="padding: 15px;">处理右侧面板的操作</div>',
                            area: ['260px', '100%'],
                            offset: 'rt', // 右上角
                            anim: 'slideLeft', // 从右侧抽屉滑出
                            shadeClose: true,
                            scrollbar: false
                        });
                    }
                });

                // main 主 iframe 窗口
                $(document).ready(function () {
                    $("dd>a, li.layui-nav-item a").not("#logout-link").click(function (e) {
                        e.preventDefault();
                        $('#main').attr("src", $(this).attr("href"));
                    });

                    // 退出登录处理
                    $('#logout-link').click(function () {
                        layer.confirm('确定要退出登录吗？', {
                            icon: 3,
                            title: '提示'
                        }, function (index) {
                            $.ajax({
                                type: "POST",
                                url: "LoginOutServlet",
                                success: function (data) {
                                    if (data === "success") {
                                        layer.msg('退出登录成功', { icon: 1, time: 2000 });
                                        // 跳转到登录页面
                                        setTimeout(function () {
                                            window.location.href = "login.html";
                                        }, 2000);
                                        window.location.href = "LoginServlet";
                                    } else {
                                        layer.msg('退出登录失败', { icon: 2, time: 2000 });
                                    }
                                },
                                error: function () {
                                    layer.msg('请求出错', { icon: 2, time: 2000 });
                                }
                            });
                            layer.close(index);
                        });
                    });
                });
            });
        </script>
</body>

</html>