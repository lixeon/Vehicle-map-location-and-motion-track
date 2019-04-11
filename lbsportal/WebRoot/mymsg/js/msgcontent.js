$(document).ready(function() {   
    var idx = $(location).attr('href').split('?')[1].split('&')[0].split('=')[1];
    var type = $(location).attr('href').split('?')[1].split('&')[1].split('=')[1];
    var responseData;
    if("sendBox" == type){
        responseData =  window.localStorage.getItem('sendBox');             
    }else{
         responseData =  window.localStorage.getItem('receiveBox');
    }
    var objJson =  jQuery.parseJSON(responseData); 
    var msgList = objJson;
    var i = 0;
    $.each(msgList, function() {
        if(i == idx){ 
             var sendType = this.sendtype == 1 ?'APP短信':'手机短信';
             $('#userid').html(this.userid);
             $('#receiveId').html(this.receiveid);
             $('#addtime').html(this.addtime);
             $('#sendtype').html(sendType);
             $('#content').html(this.content);
        }
        i++;
    });   
    
});
