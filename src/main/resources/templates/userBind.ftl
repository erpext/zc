<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<title>登陆界面</title>
<style>
    .center{width: 500px;height: 0px';text-align: center;line-height: 50px;}
    </style>
    <!--要加入js包，网上很多，自己下载放到工作目录底下就ok  然后在HTML中引用-->
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">
    </script>
</head>
<!--JavaScript前台密码判断部分-->
<script>
    function myfunction()
    {
        <!--获取用户名和密码  -->
        var userNo=document.getElementById("name").value;
        var password=document.getElementById("password").value;
        if(userNo==""||password=="")
        {
            alert("用户名或密码不能为空")
        }
        else{
            $.ajax({
                type : 'post',
                url : '/user/bindWx/',
                contentType: "application/json; charset=utf-8",
                dataType : 'json',
                data : JSON.stringify(
                        {
                            "user_no" : userNo,
                            "user_password" : password
                        }
                ),
                success : function(data) {
                    alert(data.result);
                    alert(data.ngData[0].code);
                },
                error : function(){
                    alert("提交失败，请稍候再试!");
                }
            })
        }
    }
</script>
<!--登陆页面部分非常简单的页面-->
<body>
<center>
    <form class="center">

        <fieldset>
            <legend>绑定微信</legend>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用户名 <input id="name" type="text"><br/>

            密码 <input id="password" type="text"><br/>
            <button type="button" onclick="myfunction()">提交</button>
        </fieldset>
    </form>

</center>
</body>
</html>
<!--前台JavaScript方式匹配用户名和密码（前台写死）-->
<!--想学习就看菜鸟教程，非常适合初学者网页链接-->