//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});

	//手机号的数据处理
	$("#phone").on("blur",function(){
	   var phone = $.trim( $("#phone").val() );
	   if( phone == ""){
	   	   showError("phone","必须填写手机号");
	   } else if( phone.length != 11){
	   	   showError("phone","手机号必须是11位");
	   } else if( !/^1[1-9]\d{9}$/.test(phone)){
	   	   showError("phone","手机号格式不正确");
	   } else {
	   	   showSuccess("phone");
	   	   //判断手机号是否注册过。 如果注册过， 提示用户换手机号
		   $.ajax({
			   url: contextPath + "/loan/checkPhone",
			   data:"phone="+$("#phone").val(),
			   dataType:"json",
			   success:function(resp){
			   	  if( resp.code != 0 ){
			   	  	//alert("提示："+resp.msg);
					showError("phone",resp.msg);
				  }
			   },
			   error:function(){
			       alert("请稍后重试");
			   }
		   })
	   }
	})


	//密码处理
	$("#loginPassword").on("blur",function(){
		var password = $.trim( $("#loginPassword").val() );
		if( ""== password){
			showError("loginPassword","必须输入密码值");
		} else if(   !(password.length >5 && password.length<=20) ){
			showError("loginPassword","密码是6-20位的")
		} else if( !/^[0-9a-zA-Z]+$/.test(password)){
			showError("loginPassword","密码是数字和英文混合的");
		} else if( !/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(password)){
			showError("loginPassword","密码必须包含数字和英文");
		} else{
			showSuccess("loginPassword");
		}
	})

	//获取验证码的按钮点击事件
	$("#messageCodeBtn").on("click",function(){

		let btn = $("#messageCodeBtn");

		//有on样式，说明已经点击按钮了，不要在点击了
		if(!btn.hasClass("on")){

			//检查手机号的数据
			$("#phone").blur();
			let text = $.trim($("#phoneErr").text());
			if( text == ""){ //没有错误，手机号是正确的值

				//添加on样式
				btn.addClass("on");
				$.leftTime(10,function(d){
					let second = parseInt(d.s);
					if( second == 0 ){
						btn.text( "获取验证码");
						btn.removeClass("on");
					} else {
						btn.text( second+"秒重新获取");
					}
				});

				//发送ajax
				$.ajax({
					url:contextPath+"/send/sms",
					data:"phone="+$.trim( $("#phone").val() ),
					type:"post",
					dataType:"json",
					success:function(resp){
						if(resp.code !=0 ){
							alert(resp.msg);
						}
					},
					error:function(){
						alert("请稍后重试");
					}
				})
			}
		}

	})


	//验证码文本框
	$("#messageCode").on("blur",function(){
		var code = $.trim( $("#messageCode").val() );
		if( code == ""){
			showError("messageCode","必须输入验证码");
		} else if ( code.length != 4 ) {
			showError("messageCode","验证码是4位数字")
		} else {
			showSuccess("messageCode");
		}
	})


	//注册按钮
	$("#btnRegist").on("click",function(){
		//验证数据 手机号，密码域， 短信验证码
		$("#phone").blur();
		$("#loginPassword").blur();
		$("#messageCode").blur();

		let text = $("[id $= 'Err']").text();
		if("" == text){ //数据没有问题，提交请求，注册用户

			let code = $.trim( $("#messageCode").val() );
			let pwd = $.trim( $("#loginPassword").val() );
			let phone = $.trim( $("#phone").val() );

			$.ajax({
				url:contextPath + "/loan/register",
				type:"post",
				data:{
					phone:phone,
					pwd: $.md5(pwd),
					code:code
				},
				dataType:"json",
				success:function(resp){
					if( resp.code == 0){
						window.location.href= contextPath+"/loan/page/realName";
					} else {
						alert( resp.msg);
					}
				},
				error:function(){
					alert("请求失败")
				}
			})
		}
	})
});
