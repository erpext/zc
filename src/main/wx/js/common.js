$(function() {
    //页面初始化时 就会调用的方法
    console.log('页面初始化好啦');
    //alert('页面初始化好啦')
    //alert(InterfaceDomain)
    $.ajax({
        type: 'GET',
        url: InterfaceDomain + '/user/getCookieUserId',
        xhrFields:{withCredentials: true},
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            //alert(data.result);
            //alert(data.wxUserId);
            if (data.result == "OK"){
                console.log(data);
                alert('(已登录)欢迎你：'+ data.wxUserId)
            }else{
                //Cookie取不到用户信息，跳回首页
                window.location.href="/getWxUserIdinit.html";
            }
        },
        error: function () {
            alert("获取用户登录信息失败，请稍候再试!");
        }
    });
    
})