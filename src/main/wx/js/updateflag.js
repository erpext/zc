
$(function() {
    //页面初始化时 就会调用的方法
    console.log('updateflage.html 初始化好啦');
    $.ajax({
        type: 'GET',
        url: InterfaceDomain + '/user/getWxConfig?pageName=' + AppDomain + '/updateflag.html',
        xhrFields:{withCredentials: true},
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            console.log(JSON.stringify(data));

            let res = data.ngData[0];
            //alert(res.timestamp);
            //alert(res.noncestr);
            //alert(res.signature);
            wx.config({
                // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                debug: false,
                // 必填，公众号的唯一标识
                appId: res.appId,
                // 必填，生成签名的时间戳
                timestamp: res.timestamp,
                // 必填，生成签名的随机串
                nonceStr: res.noncestr,
                // 必填，签名
                signature: res.signature,
                // 必填，需要使用的JS接口列表
                jsApiList: ['checkJsApi', 'scanQRCode']
            });
            //alert('3.wx.config done')
        },
        error: function () {
            alert("获取扫码配置参数失败，请稍候再试!");
        }
    })

})

// todo:扫描框 点击事件（需要去集成 调取 微信扫码功能）
function scanClick(id){
    wx.scanQRCode({
        needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
        scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
        success: function (res) {
            var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
            //alert('扫描结果:'+result);
            //给 num_+id 赋值 
            $("#num_"+id).val(result);
        }
    });
}

//删除按钮点击事件
function deleteClick(id){
    $("#num_"+id).val('')
}

function confirmClick(){
    // 是否可销售  (0: 为不可销售   1: 为可销售)
    //let saleAble = $("[name='sale_able']").filter(":checked").val();
    let saleAble = "1";
    if ($("[name='sale_able']").filter(":checked").val() == "false") {
        saleAble ="0"
    }
    //单号
    let val1 = $("#num_1").val();
    let val2 = $("#num_2").val();
    let val3 = $("#num_3").val();
    let val4 = $("#num_4").val();

    //todo:
    //alert(saleAble + val1 + val2 + val3 + val4);

    let param = { saleflag: saleAble, cpjhlist: [] };
    if (val1) {
        param.cpjhlist.push({ cpjh: val1 });
    }
    if (val2) {
        param.cpjhlist.push({ cpjh: val2 });
    }
    if (val3) {
        param.cpjhlist.push({ cpjh: val3 });
    }
    if (val4) {
        param.cpjhlist.push({ cpjh: val4 });
    }
    //alert(CurrentLoginUser);
    param.currentLoginUser = CurrentLoginUser;
    //alert(JSON.stringify(param));
    $.ajax({
        type: 'POST',
        url: InterfaceDomain + '/warehouse/updateSaleFlag',
        xhrFields:{withCredentials: true},
        crossDomain: true,
        data: JSON.stringify(param),
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            console.log(JSON.stringify(data))
            if (data.result == "OK"){
                //let res = data.ngData[0];
                console.log(data);
                alert('操作成功！')
                //返回主页
                window.location.href= '/wx/index.html' 
            }else{
                alert('操作不成功:' + data.ngData[0].msg)
            }
            
        },
        error: function () {
            alert("提交失败，请稍候再试!");
        }
    })
}