//$(function(){
//	var current = new Date(); /* 現在の日付取得 */
//	var year = current.getFullYear(); /* 年取得 */
//	var month = current.getMonth() + 1; /* 月取得 */
//	var day = current.getDate(); /* 日取得 */
//	
//	$('#ymd_pulldown').html('<select name="ymd">');
//	for(var i = 0; i < 30; i++){
//		var date = new Date(year,month - 1,day + i);
//		var y = date.getFullYear();
//		var m = date.getMonth() + 1;
//		var d = date.getDate();
//		
//	  $('select[name=ymd]').append('<option value="' + y + '/' + m + '/' + d + '">' + y + '/' + m + '/' + d + '</option>');
//	  }
//	
//	/** プルダウンが変更された時に値をformでsubmitする */
//	$('#ymd_pulldown').change(function(){
//		var val = $(this).val();
//		$('#form').submit();
//	})
//});

function change(){
	var value = document.getElementById("pulldown").value;
	$('#form').submit();
	console.log(value);
}