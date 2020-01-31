/**
 * 1週間ごとのプルダウン.
 */
//$(function(){
//	var date = new Date(),addDate = 7, max = 10;
//	
//	$('#date_pulldown').html('<select name="date>');
//	for(var i = 0; i < max; i++){
//		date.setDate(date.getDate() + addDate);
//		
//		$('select[name=date]').append('<option value="' + date.getMonth() + 1 + '/' + date.getDate() + '</option>');
//		console.log(date.getMonth() + 1 + '/' + date.getDate());
//	}
//});

function change(){
	var value = document.getElementById("oneWeek").value;
	console.log(value);
}
