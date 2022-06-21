//错误提示
function showException(id,msg) {
    $("#"+id+"Exception").html("<i></i><p>"+msg+"</p>");
    $("#"+id+"Exception").show();
    $("#"+id).addClass("input-red");
}
//错误提示
function showError(id,msg) {
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
    $("#"+id).removeClass("input-red");
}