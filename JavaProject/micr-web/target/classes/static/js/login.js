$(function(){

	//手机号的数据处理
	$("#phone").on("blur",function(){
		var show = $("#showId");
		var phone = $.trim( $("#phone").val() );
		if( phone == ""){
			show.text("必须填写手机号");
		} else if( phone.length != 11){
			show.text("手机号必须是11位");
		} else if( !/^1[1-9]\d{9}$/.test(phone)){
			show.text("手机号格式不正确");
		} else {
			show.text("");
		}        Integer b =123;));

    });


	//密码处理
	$("#loginPassword").on("blur",function(){
		var show = $("#showId");
		var password = $.trim( $("#loginPassword").val() );
		if( ""== password){
			show.text("必须输入密码值");
		} else if(   !(password.length >5 && password.length<=20) ){
			show.text("密码是6-20位的")
		} else if( !/^[0-9a-zA-Z]+$/.test(password)){
			show.text("密码是数字和英文混合的");
		} else if( !/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(password)){
			show.text("密码必须包含数字和英文");
		} else{
			show.text("");
		}
	})

	//登录按钮的click
	$("#loginId").on("click",function(){
		$("#phone").blur();
		$("#loginPassword").blur()

		var text = $("#showId").text();
		if("" == text){
			var phone = $.trim( $("#phone").val() );
			var pwd = $.trim( $("#loginPassword").val() );
			$.ajax({
				url:contextPath+"/loan/login",
				data:{
					phone:phone,
					pwd: $.md5(pwd)
				},
				type:"post",
				dataType:"json",
				success:function(resp){
					//登录成功，返回到来源处，也就是returnUrl的位置
					if(resp.code ==0 ){
						window.location.href= $("#returnUrl").val();
					} else {
						alert(resp.msg);
					}
				},
				error:function(){
					alert("请求错误");
				}
			})
		}
	})
})