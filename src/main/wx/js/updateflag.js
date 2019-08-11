
$(function() {
    //页面初始化时 就会调用的方法
    console.log('updateflage.html 初始化好啦');
    
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
    // 是否可销售  (false: 为不可销售   true: 为可销售)
    let saleAble = $("[name='sale_able']").filter(":checked").val();
    
    //单号
    let val1 = $("#num_1").val();
    let val2 = $("#num_2").val();
    let val3 = $("#num_3").val();
    let val4 = $("#num_4").val();

    //todo:
    alert(saleAble + val1 + val2 + val3 + val4);
}