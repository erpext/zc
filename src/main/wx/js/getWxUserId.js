
$(function() {
    //页面初始化时 就会调用的方法
    console.log('getWxUserId.html 初始化好啦');
    //alert(window.location);
    var pageurl=location.search;
    var str = pageurl.substr(1)　//去掉?号
    var strs = str.split("&");
    var code = strs[0].split("=")[1];
    //alert('code=' + code);
    $.ajax({
        type: 'GET',
        url: InterfaceDomain + '/user/getWxUserIdByCode?code='+code,
        xhrFields:{withCredentials: true},
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            let wxUserId = data;
            console.log(wxUserId);
            //alert(wxUserId.toString())
            if (wxUserId.toString() ==  "NG_UnauthorizedUser"){
                alert("未绑定企业号，或者未授权此应用，请联系管理员解决!");
            }else{
                //alert('您的帐号是' + wxUserId);
                window.location.href= '/wx/index.html';
            }
        },
        error: function () {
            alert("获取wxUserId失败，请稍候再试!");
            window.location.href='/'
        }
    })
})