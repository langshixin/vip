jQuery(document).ready(function () {
    $(window).on("scroll",function(){
        var Wscroll = $(window).scrollTop();
        Wscroll >= 500 ? $('.fix_link').fadeIn() : $('.fix_link').fadeOut();
        if(Wscroll>50){
            $(".header").addClass("mini");
        }else{
            $(".header").removeClass("mini");
        }
    });
    $(function(){
        $(".top").click(function(event) {
            $('html,body').animate({scrollTop:0},500);
        });
    })
})
//导航
$(function(){
    $(".navico").click(function(){
        navpix();
    })
})
function navpix(){
    if($(".navico").hasClass("on")){
        $(".navico").removeClass("on");
        $(".navico").siblings("ul").hide();
        $(".hsbg").hide();
    }else{
        $(".navico").addClass("on");
        $(".navico").siblings("ul").show();
        $(".hsbg").show();
    }
}
//tab切换
$(function(){
    function tabs(tabTit,on,tabCon){
        $(tabTit).find("li:first").addClass(on).show();
        $(tabCon).find(".tabbox").hide();
        $(tabCon).find(".tabbox:first").show();

        $(tabTit).find("li").click(function(){
            $(this).addClass(on).siblings("li").removeClass(on);
            var index = $(tabTit).find("li").index(this);
            $(tabCon).find(".tabbox").eq(index).show().siblings().hide();
        });
    };
    tabs(".tab-hd","active",".tab-bd");
});

$(document).on("touchstart", ".anlianli", function (e) {});