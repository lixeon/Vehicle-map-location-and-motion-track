$(document).ready(function() {
     $( "#btn_login" ).on( "click", function( event ) {       
       var nickname = $('#nickname').val(); 
       var password = $('#password').val();       
       $.ajax({
            type: "POST",
            url: serverURL + '/web/admin_app/ws_login.ashx',            
            crossDomain: true,
            //dataType: 'json',
            dataType: "text",
            data: { 'u': nickname, 'p': password },
			//jsonp: "callbackparam",//服务端用于接收callback调用的function名的参数            
            error: function(xhr, ajaxOptions, thrownError){
              //alert(xhr.status);                
            },
            success: function(responseData, status, jqxhr) {
              if( "-1" != responseData ){//登录成功
                  //var objJson =  jQuery.parseJSON(responseData);
                  //window.localStorage.setItem("userID", objJson);
                  $('#rtnInfo').html("登录已成功,正在跳转...");
                  $(location).attr('href', 'index.html'/*tpa=http://www.beidouguard.com:6010/mymsg/js/index.html*/);     
              }else{//登录失败
                  $('#rtnInfo').html("服务器异常，在登陆时，请检查网络是否连接");
              }            
            }
        });	
    });	    
    
    
    
});