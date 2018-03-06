
//android调用方法，清楚本地存储
 function clearLocal(){
      localStorage.clear();
}
(function(){
    $(function(){
         var dt;
         //请求获取详细数据
         var url = location.search;
         var theRequest = new Object();
         if(url.indexOf("?") != -1){
           var urlStr = url.substr(1);
           var strs = urlStr.split("&");
           for(var i = 0; i < strs.length; i ++) { 
             theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);   
           }
         }


        //判断本地数据与android端数据是否同一用户
        function isFaceId() {
          var localDt = localStorage.getItem("key") && JSON.parse(localStorage.getItem("key"));
          var loginFaceId = androidEnv.getUserFaceId();
          if ( loginFaceId == null || loginFaceId == undefined ) {
            return false;
          }

          if ( localDt && loginFaceId == localDt.faceId ) {
            return true;
          }
          else {
            return false;
          }
        }


         var localDt = localStorage.getItem("key") && JSON.parse(localStorage.getItem("key"));

        var flag = isFaceId();
        if(flag){
           var html = template('test',JSON.parse(localStorage.getItem("key")).m);
           //找到并替换
           // localStorage.setItem("key",JSON.stringify(m));
           document.getElementById('content').innerHTML = html;
           //获取总的费用
           getTotalCost();

           //将替换所有信息隐藏，不允许修改、
           $(".repalce_hotel").css({"display":"none"});
        }else{
          //调用ajax发送请求

          reqAjax();
        }
         
        //调用切换Day事件
        dayToggle();
       
        //调用点击替换事件
        replaceInfo();
        
        //添加行程夹，传参到app端
        $("#content").on("click","#addTravel",function(){
          var faceId = androidEnv.getUserFaceId();
          if( faceId && faceId !="" && !isFaceId()){
            //新用户数据本地存储
             var data = {};
             //获取上一页面传入的faceID，保存本地
             data.faceId = faceId;
             data.m = dt;
             localStorage.setItem("key",JSON.stringify(data));
          }
           
           //调用android方法，传输数据
           window.AndroidWebView.showInfoFromJs("hello");
        })
        
        //点击Day进行每天内容展示，切换
        function dayToggle(){
          var imgSrc1 = "images/imgs/icon1.png";
          var imgSrc2 = "images/imgs/icon2.png";
          $("#content").on("click",".day_day",function(){
              var hasClass = $(this).next().hasClass("hident_ul");
              var ulArr = $(this).next().siblings("ul");
              // 当前圆点选中
              $(this).find("img")[0].setAttribute("src",imgSrc2);
              // debugger;
              if($(this).find("img")[0].getAttribute("src")==imgSrc1){
                  // $(this).find(".circle")[0].style.width = "0.8rem";
              }
              // 其他圆点复位
              var imgArr = $(this).siblings("h5").find("img");
              for(var i = 0; i < imgArr.length; i++){
                imgArr[i].setAttribute("src",imgSrc1);
                imgArr[i].parentNode.style.width = "0.7rem";
              }
              for(var i = 0; i < ulArr.length; i++){
                if(!$(ulArr[i]).hasClass("hident_ul")){
                  $(ulArr[i]).addClass("hident_ul");
                }
              }

              if($(this).next().hasClass("hident_ul")){
                 $(this).next().removeClass("hident_ul");
                 $(this).find("img")[0].setAttribute("src",imgSrc2);
               }else{
                 $(this).next().addClass("hident_ul");
                 $(this).find("img")[0].setAttribute("src",imgSrc1);
               }
          })
        }
        //获取当前点击数据，并更新数据
        function getUpdate(tar,curentDom, type){
          debugger;
          console.log(tar);
          var str = $(curentDom).attr("data");
          var obj = JSON.parse(str);
          var currentDay = $(curentDom).attr("days");

          dt && $.each(dt.model.days, function(i, v){
             if(i == currentDay){
               var dataStr = JSON.stringify(v[type].recommend);
                v[type].recommend = obj;
                console.log($(tar)[0].getAttribute("data"))
                $(tar)[0].setAttribute("data",dataStr);
                // console.log(obj);
             }
          })
          // console.log(dt);
        }

        //点击替换按钮，展开替换内容动画
        function deployContent(){
          var display = false;
          $("#content").on("click",".reHotel",function(){
              display = !display;
              //添加显示/隐藏小三角
              // if($(this).next(".triangle_top").hasClass("toggle_hid")){
              //   $(this).next(".triangle_top").removeClass("toggle_hid");
              // }else{
              //   $(this).next(".triangle_top").addClass("toggle_hid");
              // }
              var silb = $(this).siblings(".all_hotel");
              //遍历替换数量的个数
              var _length = silb.find(".info_item").length;
              var _h = silb.find(".info_item").height()*_length+"px";
              //遍历替换显示区域高度
              display?silb.css({"height":_h}):silb.css({"height":"0px"});
              //添加显示/隐藏小三角
              display?$(this).next(".triangle_top").removeClass("toggle_hid"):$(this).next(".triangle_top").addClass("toggle_hid");
              // 点击元素距离可视区上方的距离
              var dis = $(this).offset().top - $(document).scrollTop();
              if(dis > 400){
                setTimeout(function(){
                  window.scrollBy(0, 200);
                },300)
              }
             return false;
          })
        }
        
        //点击替换事件方法
        function replaceInfo(){
           //  展开替换的内容动画
           deployContent();
           // 点击替换酒店
           $("#content").on("click",".hotelItem",function(e){
               //调用替换数据 
              getUpdate(e.target,".hotelItem", "hotel");
              // 设置点击高亮效果,其他复原
               var sibs = $(this).parents(".info_item").siblings(".info_item");
               $.each(sibs,function(i, v){
                 $(v).children(".hotelItem").css({"background":"#E1E1E1"});
               })
               $(this).parents(".info_item").children(".hotelItem").css({"background":"#fff"});
              var hotelItem = $(this).html();
              var hotelInfo = $(this).parents(".repalce_hotel").prev();
              // var hotelInfo = $(".hotelInfo").html();
              $(this).html(hotelInfo.html());
              hotelInfo.html(hotelItem);
              getTotalCost();
           })
           //点击替换返航机票
           $("#content").on("click",".endTicketItem2",function(e){
               //调用替换数据
               // getUpdate(e.target, "ticket");
               getUpdate(e.target,".endTicketItem2", "ticket");
               // 设置点击高亮效果,其他复原
               $(this).css({"background":"#fff"});
               var sibs = $(this).siblings(".endTicketItem2");
               $.each(sibs,function(i, v){
                 $(v).css({"background":"#E1E1E1"});
               })
               var endTicketItem2 = $(this).html();
               var endTicketItem1 = $(".endTicketItem1").html();
               $(".endTicketItem1").html(endTicketItem2);
               $(this).html(endTicketItem1);
               getTotalCost();
           })
           //点击始发航班选项替换数据
           $("#content").on("click",".startTicketItem2",function(e){
               //调用替换数据
               getUpdate(e.target,".startTicketItem2", "ticket");
               // 设置点击高亮效果,其他复原
               $(this).css({"background":"#fff"});
               var sibs = $(this).siblings(".startTicketItem2");
               $.each(sibs,function(i, v){
                 $(v).css({"background":"#E1E1E1"});
               })
               var startTicketItem2 = $(this).html();
               var startTicketItem1 = $(".startTicketItem1").html();
               $(".startTicketItem1").html(startTicketItem2);
               $(this).html(startTicketItem1);
               getTotalCost();
           })
        }
       
        //ajax请求方法
        function reqAjax(){
            var isCloudConnected = androidEnv.isCloudConnected();
            var url = "";
            if ( isCloudConnected ) {
                url = "http://" + androidEnv.getCloudDomain() + "/passport/getWisdomTripList";
            }
            else {
                url = "http://" + androidEnv.getLocalDomain() + "/passport/getWisdomTripList";
            }
            $.ajax({
               url: url,
               type:"post",
               data:{
                 "faceId":androidEnv.getUserFaceId(),
                 "day":theRequest.dayTime,
                 "priceType":theRequest.price,
                 "crowdType":theRequest.person
               },
                // dataType:"json",
                success:function(m){
                  console.log(m);
                  if(m.success){
                    //  调用模板引擎提供的方法
                     /* 参数1：模板的id
                        参数2：对象（注意是  对象）*/
                     dt = m;
                     var html = template('test',m);
                     document.getElementById('content').innerHTML = html;
                     //获取总的费用
                    getTotalCost(m);
                   }else{
                    alert("数据丢了...");
                  }
                },
                error:function(e){
                  alert("加载失败...");
                }
            })
        }
       
        //获取总的费用
       function getTotalCost(m){
          var totalCost=0; 
          //始发机票价格
          var price1 = parseInt($(".startTicket span.travel_price i").html());
          //返航机票价格
          var price2 = parseInt($(".endTicket span.travel_price i").html());
          //每天旅馆价格
          var price3 = $(".hotelInfo span.travel_price i");
          //所有景点价格
          var prices = $(".addPrice");
          $.each(price3,function(i,v){
            totalCost += parseInt($(v).html());
          })
          $.each(prices,function(i,v){
             totalCost += parseInt($(v).html());
          })
          totalCost += price1;
          totalCost += price2;
          // $.each(totalDays,function(i,v){
          //   debugger;
          //     if(v.hotel){
          //       totalCost += parseInt(v.hotel.recommend.price);
          //     }
          //     if(v.ticket){
          //       totalCost += parseInt(v.ticket.recommend.price);
          //     }
          //     if(v.play){
          //       $.each(v.play,function(i,v){
          //           totalCost += parseInt(v.price);
          //       })
          //     }
          // })
          //总费用渲染到页面
          $("#totalFee").text(totalCost);
          console.log(totalCost);
       }
      
     })
})()
