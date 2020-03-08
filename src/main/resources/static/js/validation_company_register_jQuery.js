$(function(){
	$("#form").validate({
		rules:{
			name:{
				required: true
			},
			kana:{
				required: true
			},
			remarks:{
				required: true
			}
		},
		messages:{
			name:{
				required: "企業名を入力してください"
			},
			kana:{
				required: "企業名(かな)を入力してください"
			},
			remarks:{
				required: "備考を入力してください"
			}
		},
		errorPlacement: function(error, element){
			error.insertBefore(element);
	        }
	});
});