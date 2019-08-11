
// 页面初始化时 就调用的方法
$(function() {

    //todo: 进入页面的时候，拉取数据，并给仓库 赋值
    let store1 = [{ckdd_no:1,ckdd_name:'测试仓库1'},{ckdd_no:3,ckdd_name:'测试仓库2'}]
    //alert('store1  old=' + JSON.stringify(store1))
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
            }else{
                
            }
        },
        error: function () {
            alert("获取仓库地点代号和名称失败，请稍候再试!");
        }
    });

})

// todo:扫描框 点击事件（需要去集成 调取 微信扫码功能）
function scanClick(id){

    //todo: 修改 num_+id 显示文字
    $("#num_"+id).html(id)
    //给 num_+id 赋值 
    $("#num_"+id).val('单号');
}

//删除按钮点击事件
function deleteClick(id){
    $("#num_"+id).val('')
}

function confirmClick(){
    // 移入库 值
    let outStore = $("#in_store").val()
    // 库位 值
    let storeSite = $("#store_site").val()
    //单号
    let val1 = $("#num_1").val();
    let val2 = $("#num_2").val();
    let val3 = $("#num_3").val();
    let val4 = $("#num_4").val();

    //todo:
    alert(outStore + storeSite + val1 + val2 + val3 + val4);
}