/// <reference path="../Terminal/perfectInfo.html" />
/**
 * Éè±¸£¨¿¨£©×ÊÁÏÏêÇéÊý¾Ý½»»¥½Å±¾
 * º¬ÒÆ¶¯¡¢ÁªÍ¨¿¨£¬·Ö±ð¶Á²»Í¬µÄ½Ó¿ÚAPI
 * simcard_lt_new.aspx ÒýÓÃ
 */
var simTypes,
    cardType = shared.getUrlQuery("cardType") ? shared.getUrlQuery("cardType") : "",
    mobile = shared.getUrlQuery("mobile") ? shared.getUrlQuery("mobile") : "",
    imei = shared.getUrlQuery("imei") ? shared.getUrlQuery("imei") : "",
    apptype = shared.getUrlQuery("apptype"),
    wxchatId = shared.getUrlQuery("wechatId"),// $("#hid_WeChatId").val();
    accessname = shared.getUrlQuery("accessname"),
    mchId = shared.getUrlQuery("mchId"),
    from_app = shared.getUrlQuery("fromapp"),
    activeTime,
    currentTime,
    vexpireTime,
    isUsageReset,
    historyMonthUsageList,
    totalMonthUsage,
    firstActiveTime,
    historyOption,
    isExpire;
(function () {
    // 初始化微信配置
    //wxapi.readyWXCgi("../../", []);

    //语音套餐长度控制
    var leftFlow_lg = $("#voice_left_flow").text();
    if (leftFlow_lg.length > 11) {
        $("#voice_flow_con").attr("class", "col-xs-7");
        $("#voice_time_con").attr("class", "col-xs-5");
        $(".font-30").css("font-size", "25px");
        $(".voice-package-details .col-xs-5,.voice-package-details .col-xs-7,.voice-package-details .col-xs-6").css("padding", "0 5px");
    } else if (leftFlow_lg.length > 10) {
        $("#voice_flow_con").attr("class", "col-xs-7");
        $("#voice_time_con").attr("class", "col-xs-5");
        $(".voice-package-details .col-xs-5,.voice-package-details .col-xs-7,.voice-package-details .col-xs-6").css("padding", "0 5px");
    } else if (leftFlow_lg.length > 9) {
        $("#voice_flow_con").attr("class", "col-xs-7");
        $("#voice_time_con").attr("class", "col-xs-5");
        $(".voice-package-details .col-xs-5,.voice-package-details .col-xs-7,.voice-package-details .col-xs-6").css("padding", "0 10px");
    } else {
        $("#voice_flow_con").attr("class", "col-xs-6");
        $("#voice_time_con").attr("class", "col-xs-6");
        $(".voice-package-details .col-xs-5,.voice-package-details .col-xs-7,.voice-package-details .col-xs-6").css("padding", "0 10px");
    }
    //APP调用隐藏切换按钮
    if (from_app == "h5") {
        $(".switchNo").hide();
    }
    //高度控制
    w_h();
    // ³õÊ¼»¯
    var simId = $("#hid_simId").val(),
        searchURL = $("#hid_url").val(),
        userName;
    initData(simId, true);
    $(".switchNo").click(function () {
        window.location.href = searchURL;
    });
    // bootstrap 居中
    $("#history").on("click", function () {
        $.getJSON('../../api/GetSimHistoryBill', historyOption).
        done(function (res) {
            if (res.error !== 0) {
                alert("数据异常!");
                return
            }
            historyMonthUsageList = res.result;
            totalMonthUsage ? historyMonthUsageList[0].UsageMB = totalMonthUsage : '';
            dataInteractive.historyMonthUsage(historyMonthUsageList);
            $('#myModal').modal('show');
        }).
        fail(function () {
            alert("网络异常")
        })
    })
    $("[role='dialog']").on('show.bs.modal', function () {
        var $this = $(this);
        var $modal_dialog = $this.find('.modal-dialog');
        $this.css('display', 'block');
        $modal_dialog.css({ 'margin-top': Math.max(0, ($(window).height() - $modal_dialog.height()) / 2) });
    });
    $(".hasBind").click(function () {
        $("#bindShow").show();
        $("#self_active_success").hide();
        $('#cardBindModal').modal('show');
        //h_bindRecord();
    });
    //手淘实名点击弹出框
    $(".taoModal").click(function () {
        $('#TaoRealName').modal('show');
    });
    var dataInteractive = (function () {

        return {
            header: header,
            usageData: usageData,
            packageList: packageList,
            historyMonthUsage: historyMonthUsage,
            hasBindModalInfo: hasBindModalInfo,
            TaoRealNameModal:TaoRealNameModal,
            simFromType: 1,
            simState: simState
        }

        function header(data) {
            var width = $(window).width();
                //fontSize = width < 375 ? "16px" : "20px"
            dataInteractive.simFromType = data.simFromType;
            simTypes = data.simFromType;
            //是否显示续费记录
            if (data.isShowWXRenewals == 0) 
                $(".f-renewalRecord").hide();
            //是否允许续费
            if (data.limitType == 1)
                $(".renewalBtn").attr("href", "#").css({ background: "#ccc", borderColor: "#ccc" });
            //是否播放公告
            if (data.limitInfo) {
                $("#limitInfo").text(data.limitInfo).show();
                $(".lt-part1").css("padding-top","0px")
            }
            if (data.simFromType === 1) {
                // 联通
                $("header .container.lt-part1 .img-container img").prop("src", "../images/terminal/lt-logol.png");
                //$("header .container.lt-part1 .pull-left.text-center span").text("中国联通");
                $("#cardNo").text(addBlank(data.iccid));//.css("font-size", fontSize)
                if (data.imei == "") {
                    $("#IMEInum").html("SIM号:<i>" + data.simNo + "</i>");
                } else {
                    $("#IMEInum").html("设备IMEI:<i>" + data.imei.substring(0, 14) + "*</i>");
                }
                simState(data, 4);
                tabChange();
                //机卡绑定
                //机卡绑定执行类型：0正常(不执行)，1告警，2停机
                if (data.bindExecuteType !== 0) {
                    if (data.imei == "") {//有无IMEI号
                        //realStateByAli=3 代表阿里绑定
                        //bindingRule 机卡绑定规则：1绑定初始设备(一对一卡号绑定)，2绑定厂家设备(查库匹配)
                        if (data.realStateByAli == 3) {
                            cardBindLabel(data.commState, data.simState, data.imeiChangeLogList);
                        } else {
                            if (data.bindingRule == 1) {
                                if (data.ruleBindIMEI == null || data.ruleBindIMEI == "") {
                                    $(".noBind").css("display", "inline-block");
                                } else {
                                    cardBindLabel(data.commState, data.simState, data.imeiChangeLogList);
                                }
                            } else {
                                //$(".noBind").css("display", "inline-block");
                                if (data.ruleBindIMEI == null || data.ruleBindIMEI == "") {
                                    $(".noBind").css("display", "inline-block");
                                } else {
                                    cardBindLabel(data.commState, data.simState, data.imeiChangeLogList);
                                }
                            }
                        }
                    } else {
                        cardBindLabel(data.commState, data.simState, data.imeiChangeLogList);
                    }
                }
            } else if (data.simFromType === 2) {
                $("header .container.lt-part1 .img-container img").prop("src", "../images/terminal/dx-logol-new.png");
                $(".btn-top").css("padding", "1px 15px");
                $(".switchNo").css("padding", "1px 15px 1px 32px");
                $(".switchNo .iconfont").css("left", "15px");
                //$("header .container.lt-part1 .pull-left.text-center span").text("中国电信");
                $("#cardNo").text(addBlank(data.iccid));//.css("font-size", fontSize)
                $("#IMEInum").html("SIM号:<i>" + data.simNo + "</i>");
                simState(data, 4)
            } else {
                tabChange();
                // 移动
                $("header .container.lt-part1 .img-container img").prop("src", "../images/terminal/yd-logol-new.png");
                $(".btn-top").css("padding", "1px 15px");
                $(".switchNo").css("padding", "1px 15px 1px 32px");
                $(".switchNo .iconfont").css("left", "15px");
                //$("header .container.lt-part1 .pull-left.text-center span").text("中国移动");
                $("#cardNo").text(addBlank(data.iccidFull));
                $("#IMEInum").html("SIM号:<i>" + data.simNo + "</i>");
                $(".f-flowDetail").hide();
                $("#fqLink").attr("href", "fq_yd.html");
                simState(data, 2);
            }
            //计算lt-part1-simInfo距离上面的高度
            var h_simInfo = $(".lt-part1-simInfo").prop("offsetHeight");
            var w_window = $(window).width();
            if (w_window > 320) {
                $(".lt-part1-simInfo").css("padding-top", (54 - h_simInfo) / 2);
            } else {
                $(".lt-part1-simInfo").css("padding-top", (48 - h_simInfo) / 2);
            }

           $("#lastDays").text(data.surplusPeriod);
            //if (data.vexpireTime == "") {
            //    $("#ExpireDate").text("");
            //} else {
            //    $("#ExpireDate").text(data.vexpireTime + "到期");
            //}
            w_h();
        }
        //机卡绑定标签公用方法
        function cardBindLabel(commState, simState, imeiChangeLogList) {
            $(".hasBind").css("display", "inline-block");
            //通信状态禁用
            if (commState == 0) {
                //停用
                if (simState == 4) {
                    $(".hasBind").text("机卡分离停机").removeClass("c-green").addClass("c-red");
                    if (isExpire) {
                        $("#modea_selfActive").show();
                    }
                    // w_h();
                } else {//其他状态
                    $(".hasBind").text("机卡已分离").removeClass("c-green").addClass("c-blue");
                }
                $(".renewalBtn").removeClass("button-primary btn-uncertification").attr("href", "javascript:void(0)");
                $(".renewalBtn").click(function () {
                    $.showTip("机卡已分离，暂时不支持续费!");
                });
            } else {//通信状态正常  //ExecuteType  0正常   1告警  2停机
                if (imeiChangeLogList != null) {
                    var imei_len = imeiChangeLogList.length;
                    if (imei_len > 0 && imeiChangeLogList[0].executeType == 1) {
                        $(".hasBind").text("机卡分离告警").removeClass("c-green").addClass("c-yellow");
                        $(".hasBind-stop").text("已机卡分离告警，请尽快将流量卡插回原设备，否则停机！");
                        $(".lt-part1").css("padding-top", "0px")
                        $(".hasBind-stop").show();
                    }
                }
            }
        }
        function simState(data, num) {
            if (data.simState === num) {
                // Í£»ú×´Ì¬
                $(".cardState-stop").css("display","inline-block");
                $(".cardState-normal").hide();
                $(".cardState-unused").hide();
            } else {
                //已失效
                if (data.simStateSrc == "已失效") {
                    $(".cardState-unused").text(data.simStateSrc);
                    $(".cardState-unused").css("display", "inline-block");
                    $(".cardState-stop").hide();
                    $(".cardState-normal").hide();
                    $(".renewalBtn").removeClass("button-primary btn-uncertification").attr("href", "javascript:void(0)");
                    $(".renewalBtn").click(function () {
                        $.showTip("该卡已失效，不支持充值续费",2,2000);
                    });
                    //$("#ydTip").show();
                    //$("#ydTip").text("该卡已失效，不支持充值续费！");
                } else if (data.simStateSrc == "已注销") {
                    $(".cardState-unused").text(data.simStateSrc);
                    $(".cardState-unused").css("display", "inline-block").text("已注销");
                    $(".cardState-stop").hide();
                    $(".cardState-normal").hide();
                    $(".renewalBtn").removeClass("button-primary btn-uncertification").attr("href", "javascript:void(0)");
                    $(".renewalBtn").click(function () {
                        $.showTip("该卡已注销，不支持充值续费");
                    });
                    //$("#ydTip").show();
                    //$("#ydTip").text("该卡已注销，不支持充值续费！");
                } else {
                    $(".cardState-normal").text(data.simStateSrc);
                    $(".cardState-normal").css("display", "inline-block");
                    $(".cardState-stop").hide();
                    $(".cardState-unused").hide();
                }
            }
        }

        function usageData(data) {
            $(".allFlow").text(judgement(data.amountUsageData));
            $("#usedFlow").text(judgement(data.doneUsage));
            if (data.simFromType === 1) {
                if (data.isUsageReset === 1 && !data.isLimitlessUsage) {
                    // ÇåÁã¿¨
                    var usedText = data.simState !== 2 ? "已使用" + (changeCloseTime(firstActiveTime, currentTime) + 1) + "个月(含本月)" : "未使用";
                    $(".resetTag,.flowTips").show();
                    $(".flowState").html(
                        "<div id='usedTime'>" + usedText + "</div>" +
                        "<div>剩余" + judgement(data.usageToPeriod * dOrM(data.surplusPeriod), "", 0) + "(" + judgement(data.usageToPeriod,"", 0) + "/月*" + dOrM(data.surplusPeriod) + ")</div>"
                    )
                } else {
                    // ²»ÇåÁã¿¨
                    $(".resetTag").hide();
                    $(".date-info").show();
                    $(".flowState").html("");
                }
            } else {
                $(".date-info").show();
                $(".resetTag").hide();
                $(".leftFlowTxt").css('padding-right', '0px');
                $(".innerContainer .flowTips").hide();
                w_h();
            }
            //剩余流量显示拿掉改本月已用          
            if (data.simFromType === 0) {
                //if ((data.sourceType == 65) || (data.sourceType == 77) || (data.sourceType == 95) || (data.sourceType == 96)) {
                //    $("#surplusFlowText").text("本月已用");
                //    $("#amountUsageData").addClass("notShowData");
                //    $("#surplusFlow").html(judgement(data.doneUsage, true));
                   
                //} else {
                //    $("#surplusFlowText").text("剩余");
                //    $("#amountUsageData").removeClass("notShowData");
                //    $("#surplusFlow").html(judgement(data.surplusUsage, true));
                //}
                $("#surplusFlowText").text("剩余");
                $("#amountUsageData").removeClass("notShowData");
                $("#surplusFlow").html(judgement(data.surplusUsage, true));
               
            } else {
                $("#surplusFlowText").text("剩余");
                $("#surplusFlow").html(judgement(data.surplusUsage, true));
            }
        }

        function packageList(data) {
            if (data.simFromType === 1) {
                // 联通
                $("#currentPackage").text(data.packageName);
                $("#currentPackageInfo").text(data.packageInfo);
            } else if (data.simFromType === 0) {
                // 移动
                $("#currentPackage").text(data.packageName);
                $("#currentPackageInfo").text(data.packageInfo);
            } else {
                $.map(data.renewalsPackageList, function (dd) {
                    var $p = $("<p style='margin-bottom: 5px;'></p>");
                    $p.text(dd.PackageType);
                    $("#nowPackageList").append($p);
                });
            }
            var getAddPackage = 0;
            if (data.isUsageReset === 1) {
                // ÇåÁãÌ×²ÍÏÔÊ¾µ±Ç°Ì×²ÍºÍµ±ÔÂµþ¼Ó°üÌ×²Í
                if (data.simFromType === 2) {
                    $("#packageTitle").remove();
                    $(".tabs a").css("width", "100%");
                    $($(".swiper-wrapper").find(".swiper-slide")[1]).remove();
                } else {
                    $("#packageTitle").text("当月叠加套餐");
                }                
                getAddPackage = 1;
            } else {
                // ²»ÇåÁãÌ×²ÍÏÔÊ¾µ±Ç°Ì×²ÍºÍÒÑÐø·ÑÌ×²Í
                if (data.simFromType === 2) {
                    $("#packageTitle").remove();
                    $(".tabs a").css("width", "100%");
                   $($(".swiper-wrapper").find(".swiper-slide")[1]).remove();
                } else {
                    $("#packageTitle").text("已续费套餐");
                }
                getAddPackage = 0;
            }
            $("#packageList_wrap .packageList").empty();
            var $li = null;
            // $(".noclear-package").hide();
            $("#nopackage_record").show();
            var zlist1 = [];
            var zlist2 = [];
            if (data.simFromType === 1) {
                // 联通
                $.map(data.renewalsPackageList, function (dd) {
                    if (dd.isAddPackage === getAddPackage) {
                        if ($.inArray(dd.PackageType, zlist1) > -1) {
                            zlist2[$.inArray(dd.PackageType, zlist1)] += 1;
                        } else {
                            zlist1.push(dd.PackageType);
                            zlist2.push(1);
                        }
                    }
                });
                var i = 0;
                $.map(zlist1, function (d) {
                    var name = zlist1[i];
                    var num = zlist2[i];
                    // $(".noclear-package").show();
                    $("#nopackage_record").hide();
                    $li = $("<li>");
                    var $span = $("<span>");
                    var $span2 = $("<span>");
                    $span.addClass("pull-right").html("[<i class='packageNum'>" + num + "</i>笔]");
                    $li.append($span);
                    $span2.text(name);
                    $li.append($span2);
                    $("#packageList_wrap .packageList").append($li);
                    ++i;
                });
            } else if(data.simFromType === 0) {
                // 移动
                $.map(data.renewalsPackageList, function (dd) {
                    if (dd.isAddPackage === getAddPackage) {
                        // $(".noclear-package").show();
                        $("#nopackage_record").hide();
                        $li = $("<li>");
                        if (dd.isToActiveOrder == 1) {
                            dd.PackageType = "(激活)" + dd.PackageType;
                        }
                        $li.text(dd.PackageType).append(
                            $("<span>").addClass("pull-right").text("到期日期" + dd.ExpireTime)
                        );

                        $("#packageList_wrap .packageList").append($li);
                    }
                });
            } else if (data.simFromType === 2) {
            }

            $("#renewalAmount").text("￥" + commafy(data.renewalAmount));
        }

        // 历史用量列表
        function historyMonthUsage(data) {
            if(!data){
                $("#noAmountData").show();
                return;
            }
            $("#noAmountData").hide();
            $("#dvHistoryUsage").empty();
            var $div, $div0, $div1, $div2, $h5, $span, $percent, $max, $flow;
            var z = 0;
            var newData = data;
            
            // 排序
            if (newData) {
                for (var i = 0; i < newData.length - 1; i++) {
                    for (var j = 1; j < newData.length - 1 - i; j++) {
                        if (newData[j].BillTime < newData[j + 1].BillTime) {
                            var temp = newData[j];
                            newData[j] = newData[j + 1];
                            newData[j + 1] = temp;
                        }
                    }
                }
            }


            $.map(data, function (dd) {
                if (dd.UsageMB > z) {
                    z = dd.UsageMB;
                }
            });
            $.map(newData, function (dd) {
                $max = z / 0.7;
                $flow = dd.UsageMB / $max * 100;
                if ($flow < 1 && $flow > 0) {
                    $flow = 1;
                }
                $percent = $flow + "%";

                $h5 = $("<h5>");
                $span = $("<span>");
                $div = $("<div>");
                $div0 = $("<div>");
                $div1 = $("<div>");
                $div2 = $("<div>");

                $h5.text(dd.BillTime);
                $span.text(commafy(dd.UsageMB) + "MB").css("paddingLeft", "5px");
                $div2.addClass("progress-bar").attr("role", "progressbar").attr("aria-valuenow", "100").attr("aria-valuemin", "0").attr("aria-valuemax", "100").css("width", "100%");
                $div1.addClass("progress").css({ "float": 'left', "width": $percent }).append($div2);
                $div0.css({overflow:"auto",marginBottom:"5px"}).append($div1).append($span)
                $div.addClass("progressList").append($h5).append($div0);

                $("#dvHistoryUsage").append($div);
            });
        }
        //机卡已绑定弹出框
        function hasBindModalInfo(realStateByAli, aliBindingIMEI, imei, bindingRule, ruleBindIMEI, time, isRuleBindIMEI) {
            //realStateByAli=3 代表阿里认证
            //aliBindingIMEI: 阿里绑定IMEI号
            //bindingRule 机卡绑定规则：1绑定初始设备(一对一卡号绑定)，2绑定厂家设备(查库匹配)
            //ruleBindIMEI: 一对一绑定的IMEI
            //当前设备IMEI号
            //变更时间
            if (time == null || time == "" || time == undefined) {
                $(".modal-changeTime").hide();
            } else {
                if (time.length == 0) {
                    $(".modal-changeTime").hide();
                } else {
                    $("#modal_changeTime").text(time[0].createTime);
                }
            }
            //当前设备IMEI号
            if (imei == null || imei == "" || imei == undefined) {
                $(".modal-currentIMEI").hide();
            } else {
                $("#modal_currentIMEI").text(addBlank(addStar(imei, 14).substring(0, 15)));
            }
            if (realStateByAli == 3) {//阿里绑定
                //当前设备IMEI号前14位是否和绑定IMEI号前14位相同
                if (imei.substring(0, 14) == aliBindingIMEI.substring(0, 14)) {
                    $("#modal_bindIMEI").text(addBlank(addStar(aliBindingIMEI, 14).substring(0, 15)));
                    $("#modal_cardBindTips").hide();
                    $(".modal-currentIMEI").hide();
                    $(".modal-changeTime").hide();
                    $(".modal-sameimei-tips").show();
                    $("#IMEInum").html("绑定IMEI:<i>" + addStar(aliBindingIMEI, 14).substring(0, 15) + "</i>");
                } else {
                    if (aliBindingIMEI == "") {
                        $(".modal-bindIMEI").hide();
                    } else {
                        $("#modal_bindIMEI").text(addBlank(addStar(aliBindingIMEI, 14).substring(0, 15)));
                        $("#IMEInum").html("绑定IMEI:<i>" + addStar(aliBindingIMEI, 14).substring(0, 15) + "</i>");
                    }
                    modal_cardBindTips(aliBindingIMEI);
                }               
            } else {
                if (bindingRule == 1) {//机卡设置一对一
                    if (ruleBindIMEI == "" || ruleBindIMEI == null || ruleBindIMEI == undefined) {
                        $(".modal-bindIMEI").hide();
                        modal_cardBindTips(ruleBindIMEI);
                    } else {
                        //当前设备IMEI号前14位是否和绑定IMEI号前14位相同
                        if (imei.substring(0, 14) == ruleBindIMEI.substring(0, 14)) {
                            $("#modal_bindIMEI").text(addBlank(addStar(ruleBindIMEI,14).substring(0, 15)));
                            $("#modal_cardBindTips").hide();
                            $(".modal-currentIMEI").hide();
                            $(".modal-changeTime").hide();
                            $(".modal-sameimei-tips").show();
                        } else {
                            $("#modal_bindIMEI").text(addBlank(addStar(ruleBindIMEI, 14).substring(0, 15)));
                            modal_cardBindTips(ruleBindIMEI);
                        }
                        $("#IMEInum").html("绑定IMEI:<i>" + addStar(ruleBindIMEI, 14).substring(0, 15) + "</i>");
                    }
                } else {
                    if (ruleBindIMEI == "" || ruleBindIMEI == null || ruleBindIMEI == undefined) {
                        if (isRuleBindIMEI == 1) {// isRuleBindIMEI   1 合法   0 不合法
                            $("#modal_bindIMEI").text(addBlank(addStar(imei, 14).substring(0, 15)));
                        } else {
                            $(".modal-bindIMEI").hide();
                        }
                         modal_cardBindTips(imei);
                    } else {
                        //当前设备IMEI号前14位是否和绑定IMEI号前14位相同
                        if (imei.substring(0, 14) == ruleBindIMEI.substring(0, 14)) {
                            $("#modal_bindIMEI").text(addBlank(addStar(imei, 14).substring(0, 15)));
                            $("#modal_cardBindTips").hide();
                            $(".modal-currentIMEI").hide();
                            $(".modal-changeTime").hide();
                            $(".modal-sameimei-tips").show();
                        } else {
                            $("#modal_bindIMEI").text(addBlank(addStar(ruleBindIMEI, 14).substring(0, 15)));
                            modal_cardBindTips(ruleBindIMEI);
                        }
                        $("#IMEInum").html("绑定IMEI:<i>" + addStar(ruleBindIMEI, 14).substring(0, 15) + "</i>");
                    }
                }
            }
        }
        //机卡已绑定弹出框 提示语
        function modal_cardBindTips(bindIMEI) {
            var text = ""
            if (bindIMEI == null || bindIMEI == "" || bindIMEI == undefined) {
                text = "*请尽快将流量卡插回原始设备！"
               // $("#modal_cardBindTips").hide();
            } else {
                if ($(".hasBind").text()=="机卡已绑定") {
                    //text = "*请尽快将流量卡插回绑定设备！"
                } else if ($(".hasBind").text() == "机卡分离告警") {
                    text = "*目前因机卡分离导致告警，请尽快将流量卡插回设备！"
                } else if ($(".hasBind").text() == "机卡分离停机") {
                    if (isExpire === true) {
                        text = '*目前因机卡分离导致停机！请插回绑定设备,然后点击"激活"';
                    } else {
                        text = '*目前因机卡分离导致停机！请插回绑定设备,联系客服恢复使用'
                    }
                } else if ($(".hasBind").text() == "机卡已分离") {
                    text = "*目前已机卡分离，请尽快将流量卡插回绑定设备！";
                    $("#modal_cardBindTips").css("color", "#2283e5");
                }
            }
            $("#modal_cardBindTips").text(text);
        }
        //手淘弹出框
        function TaoRealNameModal(name,tel,id,time) {
            $("#m_tao_name").text(addStar(name, 1));
            $("#m_tao_tel").text(tel.substring(0, 3) + "****" + tel.slice(-4));
            $("#m_tao_id").text(id.substring(0, 4) + "******" + id.slice(-4)); 
            $("#m_tao_time").text(time);
        }

    }());
    function tip(str) {
        $(".successBind").text(str).show();
        setTimeout(function () {
            $(".successBind").hide();
        }, 1500)
    }
    function bindCard(userId, simId) {
        var data = {
            "userId": userId,
            "simId": simId
        }
        $.ajax({
            type: "post",
            url: "../../api/BindUserSimInfo",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json",
            success: function (res) {
                if (res.error === 0) {
                    localStorage.setItem("success", "true");
                    location.href = location.href;
                } else {
                    localStorage.setItem("success", "false");
                }
            },
            error: function () {
                console.log("网络异常");
            }
        });
    }
    if (localStorage.getItem("success") == "true") {
        tip("绑定成功")
        localStorage.removeItem("success")
    } else if (localStorage.getItem("success") == "false") {
        tip("你已绑定该卡")
        localStorage.removeItem("success")
    }
    var userName,
        idCard,
        trueName,
        realStateByAli,
        activeByRealName;

    //无限流量处理
    function isLimitless(data) {
        $('.cardTag').hide();
        $('#dOrMTip').hide();
        $('.state').remove();
        $('#tabs_package').hide();

        $('#isLimitlessInfo').show();
        $('#LimitlessInfo').show();
        
        $('.cardState-normal').hide();
        $('.cardState-stop').hide();

        $('.leftFlowTxt').css({
            'margin-top': '5%'
        })

        $("#surplusFlowText").text("本月已用");
        $("#surplusFlow").html(judgement(data.monthUsageData, true));
    }

    //通过状态值来改变 img  以及 是否为在线  text  ------------
    function stateChange(state) {    
        if (state == 2) {
            $('.state').css('display', 'block');
            $('.state img').attr('src', '../images/terminal/offLine.png');
            $('.state span').text('离线').css('color', '#aaa');
        } else if (state == 1) {
            $('.state').css('display', 'block');
            $('.state img').attr('src', '../images/terminal/onLine.png');
            $('.state span').text('在线').css('color', '#4dcb7d');
        } else if(state == 3){
            //$('.state').css('display', 'none');
            $('.state').css('display', 'block');
            $('.state img').attr('src', '../images/terminal/fq-icon.png');
            $('.state span').text('未知').css('color', '#4dcb7d');
        } else {
            $('.state').css('display', 'none');
        }
    }
    function uncertification() {
        $(".renewalBtn").removeClass("button-primary btn-uncertification").attr("href", "javascript:void(0)").addClass('uncertificationTip');
    }
    function initData(simId, refreshFlag) {
        if (!simId)
            return;
        shared.loadCfm.open()
        var userId = $("#hid_userId").val();
        // 1.»ñÈ¡¿¨×ÊÁÏ
        $.ajax({
            method: "GET",
            url: "../../api/GetTerminalCompreForH5/" + simId + "?userId=" + userId,
            dataType: "json",
            headers: {
                Authorization: "379C68321D4C3FD90E139A2EB982189E"
            },
            success: function (rs) {
                var result = rs.result,
                    iccid = result.iccid,
                    iccidFull = result.iccidFull;
                currentTime = result.currentTime.split(' ')[0];
                vexpireTime = result.vexpireTime;
                isUsageReset = result.isUsageReset;
                historyMonthUsageList = result.historyMonthUsageList;
                firstActiveTime = result.firstActiveTime;
                isExpire = +new Date(vexpireTime.split('-').join('/')) > +new Date(currentTime.split('-').join('/'));
                userName = result.memberInfo.userName.split("");
                userName.splice(3, 4, "****");
                idCard = result.memberInfo.idCard.split("");
                idCard.splice(5, 8, "*****");
                trueName = result.memberInfo.trueName;
                realStateByAli = result.realStateByAli;
                activeByRealName = result.activeByRealName;
                shared.loadCfm.close()
                if (rs === null || rs === "" || rs === undefined)
                    return;

                if (rs.error !== 0)
                    return;

                if (result === null || result === "" || result === undefined)
                    return;
                realStateByAli = result.realStateByAli
                if (result.simFromType == 1) {
                    //realStateByAli：手淘实名，3已实名，2审核中，-1实名失败，其他未实名
                    //realNameLevel：实名等级，1企业实名，2个人实名 0则未设置则归为个人实名等级
                    //realIndustry：实名企业简称 为空则未企业实名 不为空则企业实名
                    //isAuthIdentity：true则已个人实名，false则未个人实名
                    if (result.realStateByAli == 3) {
                        $(".certification").css("display", "inline-block");
                        $(".certification").text("手淘实名");
                        $("#TaoRealName .modal-title").text("手淘实名认证详情");
                        $(".m-realName-img img").attr("src", "../images/terminal/taoAuthen.png")
                    } else {
                        if (result.realNameLevel != 1) {
                            //等级个人实名或未设置
                            if (result.isAuthIdentity) {
                                $(".certification").css("display", "inline-block");
                                $(".certification").text("个人实名");
                            } else {
                                if (result.realIndustry != "") {
                                    //if (result.realMobile != "") {
                                    //    $(".companyCertification").css("display", "inline-block");//企业实名
                                    //} else {
                                    //    $(".infoNeedPerfected").css("display", "inline-block");//信息待完善
                                    //    $(".xczs").unbind('click').on('click', function () {
                                    //        $('#infoNeedBox').modal('hide');
                                    //    })
                                    //    $(".zdl").unbind('click').on('click', function () {
                                    //        location.href = "perfectInfo.html?simId=" + simId + '&iccid=' + iccid;
                                    //    })
                                    //    if (!$(".infoNeedPerfected").is(":hidden") && activeByRealName === 1)
                                    //        $(".infoNeedPerfected").trigger("click");
                                    //    else
                                    //        $(".infoNeedPerfected").addClass('authentication-animation');
                                    //}
                                    $(".uncertification").css("display", "inline-block");
                                    if (!result.noRealNameRenewals) { //未实名无法续费
                                        uncertification();
                                    }
                                } else {
                                    $(".uncertification").css("display", "inline-block");
                                    if (!result.noRealNameRenewals) { //未实名无法续费
                                        uncertification();
                                    }
                                }
                            }
                        } else {
                            //等级企业实名
                            if (result.isAuthIdentity) {
                                $(".certification").css("display", "inline-block");
                                $(".certification").text("个人实名");
                            } else {
                                if (result.realIndustry == "") {
                                    $(".uncertification").css("display", "inline-block");
                                    if (!result.noRealNameRenewals) { //未实名无法续费
                                        uncertification();
                                    }
                                } else {
                                    $(".companyCertification").css("display", "inline-block");//企业实名
                                }
                            }
                        }
                    }
                } else {
                    if (result.isAuthIdentity) {
                        $(".certification").css("display", "inline-block");
                    } else {
                        $(".uncertification").css("display", "inline-block");
                        if (!result.noRealNameRenewals) { //未实名无法续费
                            uncertification();
                        }
                    }
                }

                var data = {
                    iccid: iccidFull,
                    simId: result.simId,
                    amountUsageData: result.amountUsageData,
                    totalMonthUsage: result.totalMonthUsage,
                    isUsageReset: result.isUsageReset,
                    apiAccount: result.apiAccount,
                    contractType: result.contractType,
                    isLimitlessUsage: result.isLimitlessUsage
                }
                historyOption = {
                    simFromType: result.simFromType,
                    simId: result.simId,
                    monthUsage: result.monthUsageData,
                    simNo: result.simNo,
                    doneUsage: result.doneUsage,
                    apiCode: result.apiCode

                }
                dataInteractive.header(result);
                dataInteractive.usageData(result);
                dataInteractive.packageList(result);
                dataInteractive.hasBindModalInfo(result.realStateByAli, result.aliBindingIMEI, result.imei, result.bindingRule, result.ruleBindIMEI, result.imeiChangeLogList, result.isRuleBindIMEI);
                if (result.realNameInfo != null) {
                    dataInteractive.TaoRealNameModal(result.realNameInfo.trueName, result.realNameInfo.userName, result.realNameInfo.idCard, result.realNameInfo.authTime);
                }
                if (result.simFromType == 1 && refreshFlag) {
                    setTimeout(function () {
                        updateTerminalState(data);
                    }, 300);
                }
                //离线、在线状态
                stateChange(result.gprsState);

                userState(userId, simId, iccidFull);
                
                if (result.isLimitlessUsage) {
                    isLimitless(result);
                    $('#LimitlessInfo > p').eq(0).find('span').text(result.packageName);

                    var a = result.netWorkSpeed;

                    $('#LimitlessInfo > p').eq(1).find('span').eq(0).text(a == '1' ? '极速' : (a == '2' ? '中速' : '低速')).css('color', a == '1' ? '#01c05f' : (a == '2' ? '#ffb21d' : '#fc6334'));
                    $('#LimitlessInfo > p').eq(1).find('span').eq(1).text(a == '1' ? '150Mb/s' : (a == '2' ? '21Mb/s' : '50Kb/s'));
                    $('#LimitlessInfo > p').eq(1).find('img').eq(0).attr('src', a == '1' ? '../images/terminal/js.png' : (a == '2' ? '../images/terminal/zs.png' : '../images/terminal/ds.png'));
                    $('#limitlessMonth').text(changeCloseTime(result.firstActiveTime, result.currentTime) + 1);
                    $('#limitlessTime').text(result.vexpireTime);

                    if (!isExpire) { //到期卡
                        $('#LimitlessInfo > p').eq(1).html('流量卡' + '<span style="color:red;">已停机</span>' + '，请充值续费！');
                    }

                    var timer = null;
                    $('#ljts').on('click', function () {
                        
                        if ($('#limitlessBox:visible').length) {
                            $('#limitlessBox').fadeOut(300);
                            clearInterval(timer);
                            return;
                        }

                        function getAnimate(){
                            $('#ljts > img').animate({
                                top: '-30px'
                            }, 300).queue(function () {
                                $('#ljts > img').css('top', '30px');
                                $(this).dequeue();
                            }).animate({
                                top: '0px'
                            }, 300);
                        }

                        clearInterval(timer);
                        timer = setInterval(function () {
                            getAnimate();
                        }, 1000);

                        $.ajax({
                            url: "../../api/RenewalsPackageList/" + simId,
                            dataType: "json",
                            type: "GET",
                            headers: {
                                "Authorization": "4H5F24ZAHU8MTFI6U66T2E8UTDHC8MI5"
                            },
                            success: function (res) {
                                var result = res.result,
                                    packageType = '';
                                if (res.error == 0) {
                                    var html = '', num = 0;

                                    for (var i = 0 ; i < result.monthAddPackage.length; i++) {
                                        if (result.monthAddPackage[i].isSpeedUpPack) {
                                            num++;
                                            packageType = result.monthAddPackage[i].packageId;
                                            html += '<li data-id="' + result.monthAddPackage[i].packageId + '">';
                                            html += '<p>' + result.monthAddPackage[i].packageName + '</p>';
                                            html += '<p>￥ ' + result.monthAddPackage[i].price + '</p>';
                                            html += '</li>';
                                            
                                        }
                                    }

                                    if (num == 1) {
                                        window.location.href = "../WechatPay/Action/SimRecommendPackagePay.aspx?simId=" + simId + "&package=" + packageType +
                            "&appType=" + apptype + "&mchId=" + mchId + "&accessname=" + accessname;
                                        return;
                                    }

                                    if (num == 0) {
                                        clearInterval(timer);
                                        $.showTip("您暂时无法续费提速包！");
                                        return;
                                    }

                                    $('#limitlessBox > ul').html(html).on('click', 'li', function () {
                                        window.location.href = "../WechatPay/Action/SimRecommendPackagePay.aspx?simId=" + simId + "&package=" + $(this).data('id') +
                            "&appType=" + apptype + "&mchId=" + mchId + "&accessname=" + accessname;
                                    });
                                    if (num == 2) {
                                        $('#limitlessBox > ul').css('width', 34 * num + '%')
                                        $('#limitlessBox > ul > li').css('width', 96 / num + '%');
                                    } else if (num >= 3) {
                                        $('#limitlessBox > ul').css('width', 32 * num + '%')
                                        $('#limitlessBox > ul > li').css('width', 96 / num + '%');
                                    }
                                    
                                    $('#limitlessBox').fadeIn(300);
                                    
                                    goTo(num);
                                    var index = 0;
                                    function goTo(num) {
                                        if (num == 0) {
                                            return;
                                        }

                                        setTimeout(function () {
                                            $('#limitlessBox > ul > li').eq(index).slideDown(300);
                                            return goTo(num);
                                        }, 300 * (index + 1))

                                        num--;
                                        index++;
                                    }

                                    
                                    
                                }
                            },
                            error: function () {
                                console.log("网络异常");
                            }
                        })


                        var packageType = 1069;
                        
                    })
                }
            
            }
        });
    }

    $("#self_active").on("click", function () {
        var self = this;
        $(this).text('执行中...').css({'background-color':'#ddd','color':'#fff'}).attr("disabled", "disabled");
        $.ajax({
            type: "POST",
            url: "../../api/SelfHelpActivated",
            dataType: "JSON",
            data: JSON.stringify({ simId: $("#hid_simId").val() }),
            contentType: "application/json",
            headers: {
                "Authorization": "4H5F24ZAHU8MTFI6U66T2E8UTDHC8MI5"
            },
            success: function (res) {
                if (res.error !== 0) {
                    alert("激活失败");
                    $(this).text('激活').removeAttr("disabled");
                    return;
                }
                $(this).text('已激活');
                $("#bindShow").hide();
                $("#self_active_success").show();
            },
            error: function () {
                $(this).text('激活').css({ 'background-color': '#ddd', 'color': '#fff' }).removeAttr("disabled");
                alert('网络异常');
            }
        });
    })

    function addBlank(src) {
        var len = src.length,
            arr,
            n,
            srcObj = src.split("");
        switch (len) {
            case 11:
                arr = [3, 7];
                break;
            case 13:
                arr = [5, 9];
                break;
            case 19:
                arr = [4, 8, 12, 16];
                break;
            default:
                arr = [4, 8, 12, 16];
        };
        n = arr.length;
        for (var i = 0, length = n; i < length; i++) {
            srcObj.splice(arr[i] + i, 0, " ");
        }
        return srcObj.join("");
    }
    //后面多少位用*代替
    function addStar(src,j) {
        var len = src.length,
            arr,
            n,
            srcObj = src.split("");
        if (len > j) {
            for (var i = j;i < len; i++) {
                srcObj.splice(i, 1, "*");
            }
            return srcObj.join("");
        } else {
            return src;
        }
    }



    function userState(userId, simId,iccid) {
        var verUrl = from_app === 'h5' ? "../Terminal/AuthRealName.aspx" : "../member/Verified.aspx",
            verData = from_app === 'h5' ?
            "?num="+ iccid +"&num_type=iccid&imei="+imei : 
            "?userId=" + userId + "&simId=" + simId + "&iccid=" + iccid + "&fromapp=" + from_app + "&onlyVer=true"
        $.ajax({
            url: "../../api/CheckMemberRealBind?userId=" + userId + "&simId=" + simId,
            type: "GET",
            dataType: "json",
            success: function (res) {
                var result = res.result,
                    iccid = $.trim($("#hid_iccid").val());
                    
                if (res.error == 0) {
                    if (cardType) {
                        //实名验证
                        if (realStateByAli !== 3) {
                            $(".modal-body div.modal-body-text").html("当前流量卡未实名,请完成实名认证");
                            $(".btn-right").text("实名认证");
                            $(".btn-right").on("click", function () {
                                location.href = "simcard_realbind.aspx?imei=" + imei + "&mobile=" + mobile + "&simNo=" + iccid + "&apptype=" + apptype + "&wechatId=" + wxchatId + "&mchId=" + mchId + "&accessname=" + accessname + "&fromapp=" + from_app;
                            })
                            $(".btn-left").attr("data-dismiss", "modal").text("下次再说");
                            if (!$("#uncertification").is(":hidden") && activeByRealName === 1)
                                $("#uncertification").trigger("click");
                            else
                                $("#uncertification").addClass('authentication-animation');
                            
                        }
                    } else {
                        if (result.user) {
                            //实名验证
                            if (result.realState == 3) {
                                //是否与此卡绑定--
                                //1
                                if (result.binds == false) {
                                    $(".btn-right").attr("data-dismiss", "modal").text("确定");
                                    $(".btn-left").attr("data-dismiss", "modal").text("测试后实名");
                                    $(".modal-body div.modal-body-text").html("是否用会员账号(" + userName.join("") + ")" + trueName + "实名认证该流量卡?");
                                    $(".btn-right").on("click", function () {
                                        bindCard(userId, simId);
                                    })
                                    if (!$("#uncertification").is(":hidden") && activeByRealName === 1) 
                                        $("#uncertification").trigger("click");
                                    else
                                        $("#uncertification").addClass('authentication-animation')
                                }
                                //实名审核中
                            } else if (result.realState == 2) {
                                if (result.binds == false) {
                                    //3
                                    $(".btn-left").attr("data-dismiss", "modal").text("测试后实名");
                                    $(".btn-right").attr("data-dismiss", "modal").text("确定");
                                    $(".modal-body div.modal-body-text").html("是否用审核中的会员(" + userName.join("") + ")认证该流量卡,认证通过后自动完成该流量卡的实名认证");
                                    $(".btn-right").on("click", function () {
                                        bindCard(userId, simId);
                                    })
                                    if (!$("#uncertification").is(":hidden") && activeByRealName === 1)
                                        $("#uncertification").trigger("click");
                                    else
                                        $("#uncertification").addClass('authentication-animation')
                                } else {
                                    //2
                                    $(".modal-body div.modal-body-text").text("您已提交实名信息,认证将在3个工作日内完成审核,请留意公众号");
                                    $(".btn-right").attr("data-dismiss", "modal").text("知道了").parent().attr("class", "col-xs-12 text-center");
                                    $(".btn-left").hide();
                                }
                            }
                                //非实名验证
                            else {
                                //4
                                $(".modal-body div.modal-body-text").html("当前会员账号(" + userName.join("") + ")未实名,请完成实名认证");
                                $(".btn-right").text("实名认证");
                                $(".btn-right").on("click", function () {
                                    location.href = verUrl + verData;
                                })
                                $(".btn-left").attr("data-dismiss", "modal").text("测试后实名");
                                if (!$("#uncertification").is(":hidden") && activeByRealName === 1)
                                    $("#uncertification").trigger("click");
                                else
                                    $("#uncertification").addClass('authentication-animation');
                            }
                        } else {
                            //5
                            $(".modal-body div.modal-body-text").text("根据国家工信部相关规定,所有物联网卡需进行实名身份认证,未实名流量卡将依法停止通信服务");
                            $(".btn-right").text("实名注册").parent().attr("class", "col-xs-12 text-center");
                            $(".btn-left").text("测试后实名").attr("data-dismiss", "modal").parent().css("marginTop", "15px").attr("class", "col-xs-12 text-center");
                            $(".btn-right").on("click", function () {
                                location.href = "../member/register.aspx?userId=" + userId + "&simId=" + simId + "&iccid=" + iccid + "&onlyVer=true&fromapp=" + from_app;
                            })
                            if (result.realState !== 2 && result.realState !== 3 && !$("#uncertification").is(":hidden") && activeByRealName === 1)
                                $("#uncertification").trigger("click");
                            else
                                $("#uncertification").addClass('authentication-animation');
                        }
                    }
                }
            },
            error: function () {
                console.log("失败")
            }
        })
    }

    function updateTerminalState(data) {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "../../api/UpdateTerminalUsageByICCID",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (c) {
                if (c === null || c === undefined || c === "") {
                    console.log("同步出错");
                    return;
                }

                if (c.error === 0) {
                    var res = c.result;
                    console.log("同步成功");
                    totalMonthUsage = res.totalMonthUsage;
                    var usedText = c.result.simState !== 2 ? "已使用" + (changeCloseTime(firstActiveTime, currentTime)+1) + "个月(含本月)" : "未使用";
                    $("#surplusFlow").html(judgement(res.surplusUsage, true));
                    $(".allFlow").text(judgement(res.amountUsageData));
                    $("#usedFlow").text(judgement(res.doneUsage));
                    $("#usedTime").text(usedText);
                    res.surplusPeriod !== 0 && $("#lastDays").text(res.surplusPeriod);

                    if (dataInteractive.simFromType == 1) {
                        dataInteractive.simState(res, 4)
                    } else {
                        dataInteractive.simState(res, 2)
                    }
                    stateChange(res.gprsState);

                    if (data.isLimitlessUsage) {
                        isLimitless(res);
                    }


                } else {
                    console.log("同步失败 " + c.reason);
                }
            }
        });
    }
}());

function dOrM(num) {
    if (isUsageReset === 1) {
        if (num === 0) {
            return 0;
        }
        if (vexpireTime === '') {
            if (num % 365 === 0) {
                return num / 365 * 12;
            } else {
                return num / 30;
            }
        } else {
            return changeCloseTime(currentTime, vexpireTime);
        }
    }
    return num;
}

function judgement(num, sub, tofixed) {

    var sub = sub || false,
        tofixed = tofixed === 0 ? 0 : tofixed ? tofixed : 2,
        subFStr = sub !== false ? "<sub>" : "",
        subBstr = sub !== false ? "</sub>" : "",
        flowNum,
        flowUnit;
    if (num < 1000) {
        flowNum = num;
        flowUnit = "MB"
    } else {
        flowNum = num / 1024;
        flowUnit = "GB"
        if (flowNum > 1000) {
            flowNum = flowNum / 1024;
            flowUnit = "TB"
        }
    }
    return commafy(flowNum.toFixed(tofixed)) + subFStr + flowUnit + subBstr;
}

//nTime:首次激活 eTime:当前日期
function changeCloseTime(nTime,eTime) {
    var nTime = nTime.split(' ');
        nTime = nTime[0].split('-');
    var eTime = eTime.split('-'),
        addM = eTime[0] - nTime[0],
        theM = parseInt(eTime[1]) + 12 * addM;
    nTime[2] > 26 ? theM-- : "";
    eTime[2] > 26 ? theM++ : "";
    return theM - nTime[1];
}

//套餐tab切换
function tabChange() {
    var tabsSwiper = new Swiper('.swiper-container', {
        speed: 500,
        onSlideChangeStart: function () {
            $(".tabs .active").removeClass('active');
            $(".tabs a").eq(tabsSwiper.activeIndex).addClass('active');
        }
    });

    $(".tabs a").on('touchstart mousedown', function (e) {
        e.preventDefault()
        $(".tabs .active").removeClass('active');
        $(this).addClass('active');
        tabsSwiper.swipeTo($(this).index());
    });

    $(".tabs a").click(function (e) {
        e.preventDefault();
    });
}
//统计点击次数
function statisticsClickCount() {
    return true;
}
//跳转续费列表
function toRenewalList(self) {
    if (statisticsClickCount()) {
        if ($(self).hasClass('uncertificationTip')) {
            $("#tipBox").modal("show");
        } else {
            var url = $('#hid_payUrl').val();
            window.location.href = url;
        }
    }
}
//跳转至历史续费记录和本月用量详情
function toRenewalRecord() {
    if(statisticsClickCount()){
        var iccid = $.trim($("#hid_iccid").val()),
               d = new Date(),
               year = d.getFullYear();
        window.location.href = "../Terminal/renewalRecord.aspx?iccid=" + iccid + "&year=" + year;
    }
}
function toMonthAmount() {
    if (statisticsClickCount()) {
        var iccid = $.trim($("#hid_iccid").val());
        window.location.href = "../Terminal/MonthAmount.aspx?iccid=" + iccid + "&simTypes=" + simTypes;
    }
}
//点击状态标签 跳转至智能诊断页面
function toIntelligentDiagnosis() {
    if (statisticsClickCount()) {
        var iccid = $.trim($("#hid_iccid").val());
        window.location.href = "../IntelligentDiagnosis/IntelligentDiagnosis.aspx?iccid=" + iccid + "&fromapp=" + from_app;
    }
}
//带URL参数跳转到help页面
function toHelp() {
    if (statisticsClickCount()) {
        //var url = location.href;
        var iccid = $.trim($("#hid_iccid").val());
        window.location.href = "../Terminal/fq.html?iccid=" + iccid;
    }
}
//高度
function w_h() {
    var h = $(window).height();
    var h_header = $("header").prop("offsetHeight");
    var h_footer = $("footer").prop("offsetHeight");
    var h_part2 = $(".lt-part2").prop("offsetHeight");
    var h_tabs = $(".tabs").prop("offsetHeight");
    var h_main = h - h_header - h_footer - h_part2 - 7;
    $("#tabs_package").outerHeight(h_main);
    $("#nowPackageList").outerHeight(h_main - h_tabs);
    $("#packageList_wrap").outerHeight(h_main - h_tabs);
}
//窗体大小自动调整
window.onresize = function () {
    w_h();
}
//机卡绑定弹框记录高度控制
//function h_bindRecord() {
//    var h = $(window).height();
//    var h_modal = $("#cardBindModal .modal-content").prop("offsetHeight");
//    var h_modal_header = $("#cardBindModal .modal-header").prop("offsetHeight");
//    var h_modal_NowbindDevice = $("#cardBindModal .NowbindDevice").prop("offsetHeight");
//    if (h_modal > h) {
//        $(".bindRecord-container").outerHeight(h - h_modal_header - h_modal_NowbindDevice-40);
//    }
//}
//下拉刷新
var slide = function (option) {
    var defaults = {
        container: '',
        next: function () { }
    }
    var start,
        end,
        length,
        isLock = false,//是否锁定整个操作
        isCanDo = false,//是否移动滑块
        isTouchPad = (/hp-tablet/gi).test(navigator.appVersion),
        hasTouch = 'ontouchstart' in window && !isTouchPad;
    var obj = document.querySelector(option.container);
    var loading = obj.firstElementChild;
    var offset = loading.clientHeight;
    var objparent = obj.parentElement;
    /*操作方法*/
    var fn =
    {
        //移动容器
        translate: function (diff) {
            obj.style.webkitTransform = 'translate3d(0,' + diff + 'px,0)';
            obj.style.transform = 'translate3d(0,' + diff + 'px,0)';
        },
        //设置效果时间
        setTransition: function (time) {
            obj.style.webkitTransition = 'all ' + time + 's';
            obj.style.transition = 'all ' + time + 's';
        },
        //返回到初始位置
        back: function () {
            fn.translate(0 - offset);
            //标识操作完成
            isLock = false;
        },
        addEvent: function (element, event_name, event_fn) {
            if (element.addEventListener) {
                element.addEventListener(event_name, event_fn, false);
            } else if (element.attachEvent) {
                element.attachEvent('on' + event_name, event_fn);
            } else {
                element['on' + event_name] = event_fn;
            }
        }
    };

    fn.translate(0 - offset);
    fn.addEvent(obj, 'touchstart', start);
    fn.addEvent(obj, 'touchmove', move);
    fn.addEvent(obj, 'touchend', end);
    fn.addEvent(obj, 'mousedown', start)
    fn.addEvent(obj, 'mousemove', move)
    fn.addEvent(obj, 'mouseup', end)

    //滑动开始
    function start(e) {
        var even = typeof event == "undefined" ? e : event;
        end = hasTouch ? even.touches[0].pageY : even.pageY;
        if (even.srcElement == "swiper-container" || $(".swiper-container").find(even.srcElement).length > 0) {
            if ($("#packageList_wrap").scrollTop() <= 0) {
                //标识操作进行中
                isLock = true;
                isCanDo = true;
                //保存当前鼠标Y坐标
                start = hasTouch ? even.touches[0].pageY : even.pageY;
                //消除滑块动画时间
                fn.setTransition(0);
                loading.innerHTML = '下拉刷新数据';
            }
        }else if (objparent.scrollTop <= 0 && !isLock) {
            //标识操作进行中
            isLock = true;
            isCanDo = true;
            //保存当前鼠标Y坐标
            start = hasTouch ? even.touches[0].pageY : even.pageY;
            //消除滑块动画时间
            fn.setTransition(0);
            loading.innerHTML = '下拉刷新数据';
        }
        return false;
    }

    //滑动中
    function move(e) {
        if (objparent.scrollTop <= 0 && isCanDo) {
            var even = typeof event == "undefined" ? e : event;
            //保存当前鼠标Y坐标
            end = hasTouch ? even.touches[0].pageY : even.pageY;
            if (start < end) {
                even.preventDefault();
                //消除滑块动画时间
                fn.setTransition(0);
                //移动滑块
                if ((end - start - offset) / 2 <= 150) {
                    length = (end - start - offset) / 2;
                    fn.translate(length);
                }
                else {
                    length += 0.3;
                    fn.translate(length);
                }
            }
        }
    }
    //滑动结束
    function end(e) {
        if (isCanDo) {
            isCanDo = false;
            //判断滑动距离是否大于等于指定值
            if (end - start >= offset) {
                //设置滑块回弹时间
                fn.setTransition(1);
                //保留提示部分
                fn.translate(0);
                //执行回调函数
                loading.innerHTML = '正在刷新数据';
                if (typeof option.next == "function") {
                    option.next.call(fn, e);
                }
            } else {
                //返回初始状态
                fn.back();
            }
        }
    }
}

slide({
    container: "#refreshDiv", next: function (e) {
        //松手之后执行逻辑,ajax请求数据，数据返回后隐藏加载中提示
        var that = this;
        setTimeout(function () {
            //that.back.call();
            window.location.href = window.location.href;
            //$.showTip("刷新成功！");
        }, 800);
    }
});

