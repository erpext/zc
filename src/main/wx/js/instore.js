// 页面初始化时 就调用的方法
$(function() {
    //todo: 进入页面的时候，拉取数据，并给仓库 赋值
    $.ajax({
        type: 'GET',
        url: InterfaceDomain + '/user/getWxConfig?pageName=' + AppDomain + '/instore.html',
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

    //todo: 进入页面的时候，拉取数据，并给仓库 赋值
    let store1 = [{ckdd_no:1,ckdd_name:'测试仓库1'},{ckdd_no:3,ckdd_name:'测试仓库2'}]
    
    $.ajax({
        type: 'GET',
        url: InterfaceDomain + '/warehouse/listWareHouse',
        xhrFields:{withCredentials: true},
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            //alert(data.result);
            //alert(data.wxUserId);
            if (data.result == "OK"){
                console.log(data);
                store1 = data.ngData;
                //alert('仓库list from db：'+ JSON.stringify(store1).substr(1,200))
                //alert('store1  new=' + JSON.stringify(store1).substr(1,200))
                // 添加 移出库 名单
                for (const val of store1) {
                    $("#in_store").append("<option value='"+val.ckdd_no+"'>"+val.ckdd_name+"</option>");
                }

                // 先給 库位一个默认值
                let default_store = store1[0].ckdd_no
                //alert('默认仓库地点代号='+default_store);
                getStoreSit(default_store)
                
            }else{
                
            }
        },
        error: function () {
            alert("获取仓库地点代号和名称失败，请稍候再试!");
        }
    });

    //当移入库更换时调用的方法
    $('#in_store').change(function(){
        let inStore = $("#in_store").val()
        getStoreSit(inStore)
    })

})

// todo:扫描框 点击事件（需要去集成 调取 微信扫码功能）
function scanClick(id){
    //alert('扫描start');
    wx.scanQRCode({
        needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
        scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
        success: function (res) {
            var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
            if(result.includes(',')){
                result = result.split(',')[1];        
            }
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
    // 移入库 值
    let inStore = $("#in_store").val()
    // 库位 值
    let storeSite = $("#store_site").val()
    //单号
    let val1 = $("#num_1").val();
    let val2 = $("#num_2").val();
    let val3 = $("#num_3").val();
    let val4 = $("#num_4").val();

    //todo:
    //alert(inStore + storeSite + val1 + val2 + val3 + val4);

    let param = { ckdd_yc: inStore, ckqy: storeSite, cpjhlist: [] };
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
    param.currentPathname = window.location.pathname;
    //alert(JSON.stringify(param));

    $.ajax({
        type: 'POST',
        url: InterfaceDomain + '/movein',
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

// 查询库位(注: params 根据你的需求来写)
function getStoreSit(params){
    $("#store_site").empty();
    let param = { ckdd: params };
    $.ajax({
        type: 'POST',
        url: InterfaceDomain + '/warehouse/listWareHouseDetail',
        xhrFields:{withCredentials: true},
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        data: JSON.stringify(param),
        success: function (data) {
            //alert(data.result);
            //alert(data.wxUserId);
            if (data.result == "OK"){
                console.log(data);

                // store2  就是库位
                store2 = data.ngData;
                //alert(store2)
                // 添加 移出库 名单
                for (const val of store2) {
                    $("#store_site").append("<option value='"+val+"'>"+val+"</option>");
                }
            }else{
                
            }
        },
        error: function () {
            alert("获取库位失败，请稍候再试!");
        }
    });
}