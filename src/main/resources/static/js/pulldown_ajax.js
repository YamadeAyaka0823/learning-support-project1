//$(function(){
//	var value = document.getElementById("#pulldown").value;
//	$('#puldown').on('change', function(){
//		$.ajax({
//			type: "POST",
//			url: "/student/date_load" + value,
//			datatype: "html"
//		}).done(function(data){
//				$('#result').html(data);
//		});
//	})
//	return
//});

//$('#pulldown').on('change', function(){
//	var value = document.getElementById("#pulldown").value;
//	console.log(value);
//	$.ajax({
//		type: "POST",
//		url: "/student/date_load" + value,
//		dataType: "html",
//		success: function(data,status,xhr){
//			$('#result').html(data);
//		}
//	});
//});

function change(){
	var date = document.getElementById("pulldown").value;
	var trainingId = document.getElementById("training").value;
	var studentId = document.getElementById("student").value;
	$.ajax({
		type: "POST",
		url: "/student/date_load/" + date + "/" + trainingId + "/" + studentId,
//		dataType: "html",
//		success: function(data){
//			$('#result').html(data);
//		}
	});
}