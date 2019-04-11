//恢复treeMain的高度auto，解决firefox的bug
(function () {
    if (/Firefox/i.test(navigator.userAgent)) {
        var treeMain = $('#treeMain');
        treeMain.css('height', 'auto');
    }
})();