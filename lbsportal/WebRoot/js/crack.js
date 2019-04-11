//破解miniui alert弹出框
var WinAlerts = window.alert;
window.alert = function (e) {
    if (e != null && typeof e == 'string' 
        && e.indexOf("www.miniui.com"/*tpa=http://www.beidouguard.com:6010/js/www.miniui.com*/) > -1) {
    } else {
        WinAlerts(e);
    }
};