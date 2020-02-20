function change(){
//	document.form.select.option.selected = true;
	var value = document.getElementById("pulldown").value;
//	var values = value.options;
//	for(var i = 0, l = value.length; i < l; i++){
//			var option = value.options[i];
//				option.selected;
//	}
	$('#form').submit();
	console.log(value);
}