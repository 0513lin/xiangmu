
//同意实名认证协议
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


	//姓名的数据处理
	$("#realName").on("blur",function(){
		var name = $.trim( $("#realName").val() );
		if( name == ""){
			showError("realName","必须输入姓名");
		} else if( name.length < 2 ){
			showError("realName","姓名至少2个字符");
		} else if( !/^[\u4e00-\u9fa5]{0,}$/.test(name)){
			showError("realName","姓名必须是中文");
		} else {
			showSuccess("realName");
		}
	})

	//身份证号码
	$("#idCard").on("blur",function () {
		var card = $.trim( $("#idCard").val() );
		if( card == ""){
			showError("idCard","必须输入身份证号码");
		} else if( !/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(card)){
			showError("idCard","身份证号码不符合要求");
		} else {
			showSuccess("idCard");
		}
	})

	//按钮的事件
	$("#btnRegist").on("click",function(){
		//验证数据 姓名和身份证号
		$("#idCard").blur();
		$("#realName").blur();

        var text = $("div[id $='Err']").text();
        if("" == text){
        	//发起ajax请求
			var name = $.trim( $("#realName").val() );
			var card = $.trim( $("#idCard").val() );
			$.ajax({
				url : contextPath + "/loan/realName",
				data:{
					name:name,
					card:card
				},
				dataType:"json",
				type:"post",
				success:function(resp){
					//认证成功， 跳转到用户中心页面
					if( resp.code == 0){
						window.location.href=contextPath+"/loan/page/myCenter";
					} else {
						alert(resp.msg);
					}
				},
				error:function(){
					alert("请求错误")
				}
			})
		}
	})
});
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