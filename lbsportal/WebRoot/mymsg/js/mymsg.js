$(document).ready(function() {   
     $( "#btn_sendbox" ).on( "click", function( event ) {
         $('#rtnInfo').html("正在加载");
         $('#rtnInfo').show();
         $("#btn_sendbox").addClass('on');
         $('#btn_receivebox').removeClass('on');
         $('#receivebox').hide();
         $('#sendbox').show();
       $.ajax({
            type: "POST",
            url: serverURL + '../admin_app/ws_msgbox.ashx',            
            crossDomain: true,
            //dataType: 'json',
            dataType: "text",
            data: { 'getmsgtype': 'send',  },
			//jsonp: "callbackparam",//服务端用于接收callback调用的function名的参数            
            error: function(xhr, ajaxOptions, thrownError){
              //alert(xhr.status);                
            },
            success: function(responseData, status, jqxhr) {             
              $('#rtnInfo').hide();
              if( "0" != responseData ){//成功
                  window.localStorage.setItem("sendBox", responseData);
                  var objJson =  jQuery.parseJSON(responseData);
                  var msgList = objJson;
                  var targetList = $('#sendmsg_list');   
                  targetList.html("");
                  var i = 0;
                  $.each(msgList, function() {     
                      var sendType = this.sendtype == 1 ?'APP短信':'手机短信';
                      var receiveIdlen = getBLen(this.receiveid);
                      var receiveId;
                      if(receiveIdlen>22){
                          receiveId = this.receiveid.substr(0,20)+"..";
                      }else{
                          receiveId = this.receiveid;
                      }
                      var contentlen = getBLen(this.content);
                      var content;
                      if(contentlen>9){
                          content = this.content.substr(0,8)+"..";
                      }else{
                          content = this.content;
                      }
                        targetList.append(
'                        <li style="display:block;"> '+
'                            <a href="msgcontent.html-idx=%27+i+%27&type=sendBox"/*tpa=http://www.beidouguard.com:6010/mymsg/js/msgcontent.html?idx=%27+i+%27&type=sendBox*/> '+
'                                <div class="bd-message"> '+  
'                                    <p class="bd-font"> '+
'                                        <span>接收者:</span> '+
'                                        <span style="margin-left:7%">'+receiveId+'</span>'+
'                                    </p>'+                                
'                                     <p class="bd-font">'+
'                                        <span>时间:</span>'+
'                                        <span style="margin-left:12%">'+this.addtime+'</span>'+
'                                    </p>'+
'                                    <p class="bd-font">'+
'                                        <span>短信类别:</span>\<span style="margin-left:2%">'+sendType+'</span>'+
'                                    </p>'+                                   
'                                    <p class="bd-font">'+
'                                        <span style="float:left">发送内容:</span>'+
'                                        <span>'+content+'</span>'+
'                                    </p>'+
'                                </div>'+
'                                <div class="bd-right">></div>'+
'                           </a>'+
'                        </li> '
                       );
                      i++;
                   }); 
                     
              }else{//失败                  
                  $('#rtnInfo').html("没有短信");
                  $('#rtnInfo').show();
              }            
            }
        });	
    });	  
    $( "#btn_receivebox" ).on( "click", function( event ) {
         $('#rtnInfo').html("正在加载");
         $('#rtnInfo').show();
         $("#btn_receivebox").addClass('on');
         $("#btn_sendbox").removeClass('on');
         $('#sendbox').hide();
         $('#receivebox').show();
         $.ajax({
            type: "POST",
            url: serverURL + '../admin_app/ws_msgbox.ashx',            
            crossDomain: true,
            //dataType: 'json',
            dataType: "text",
            data: { 'getmsgtype': 'receive'},
			//jsonp: "callbackparam",//服务端用于接收callback调用的function名的参数            
            error: function(xhr, ajaxOptions, thrownError){
              //alert(xhr.status);                
            },
            success: function(responseData, status, jqxhr) {
               $('#rtnInfo').hide();
              if( "0" != responseData ){//成功
                  window.localStorage.setItem("receiveBox", responseData);
                  var objJson =  jQuery.parseJSON(responseData);
                  var msgList = objJson;
                  var targetList = $('#receivemsg_list');   
                  targetList.html("");
                  var i = 0;
                  $.each(msgList, function() {     
                       var sendType = this.sendtype == 1 ?'APP短信':'手机短信';
                      var contentlen = getBLen(this.content);
                      var content;
                      if(contentlen>9){
                          content = this.content.substr(0,8)+"..";
                      }else{
                          content = this.content;
                      }
                        targetList.append(
'                        <li style="display:block;"> '+
'                            <a href="msgcontent.html-idx=%27+i+%27&type=receiveBox"/*tpa=http://www.beidouguard.com:6010/mymsg/js/msgcontent.html?idx=%27+i+%27&type=receiveBox*/> '+
'                                <div class="bd-message"> '+
'                                    <p class="bd-font"> '+
'                                        <span>发送者:</span> '+
'                                        <span style="margin-left:7%">'+this.userid+'</span>'+
'                                    </p>'+
'                                     <p class="bd-font">'+
'                                        <span>发送时间:</span>'+
'                                        <span style="margin-left:13%">'+this.addtime+'</span>'+
'                                    </p>'+
'                                    <p class="bd-font">'+
'                                        <span>短信类别:</span>\<span style="margin-left:2%">'+sendType+'</span>'+
'                                    <p class="bd-font">'+
'                                        <span style="float:left">发送内容:</span>'+
'                                        <span>'+content+'</span>'+
'                                    </p>'+
'                                </div>'+
'                                <div class="bd-right">></div>'+
'                           </a>'+
'                        </li> '
                       );
                      i++;
                   });                     
              }else{//失败
                  $('#rtnInfo').show();
                  $('#rtnInfo').html("没有短信");
              }            
            }
        });	
    });	 
     $("#btn_receivebox").trigger("click"); 
    
    
});
//获得字符串长度
    getBLen = function(str) {
      if (str == null) return 0;
      if (typeof str != "string"){
        str += "";
      }
      return str.replace(/[^\x00-\xff]/g,"01").length;
    }