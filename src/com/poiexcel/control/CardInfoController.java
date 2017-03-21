package com.poiexcel.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.poiexcel.service.CardInfoService;
import com.poiexcel.vo.InfoVo;


@Controller
@RequestMapping("/card")
public class CardInfoController {
	  
	    @Autowired
	    CardInfoService service;
	    
	    @RequestMapping("/querySingle")
	    public ModelAndView getCardInfo(String iccid){
	    	ModelAndView mv = new ModelAndView("index");
	    	InfoVo  vo = service.queryInfoByISMI(iccid);
	    	if(vo!=null){
	    		mv.addObject("info", vo);
	    	}else{
	    		InfoVo   wrongInfo = new InfoVo();
	    		wrongInfo.setUserStatus("卡号信息异常");
	    		mv.addObject("info", wrongInfo);
	    	}
	    	return mv ;
	    	
	    }
	    
	    @RequestMapping("/search")
	    public ModelAndView searchHistory(String imsi){
	    	ModelAndView mv = new ModelAndView("history");
	    	InfoVo  vo = service.queryInfoByISMI(imsi);
	    	if(vo!=null){
	    		mv.addObject("info", vo);
	    	}else{
	    		InfoVo   wrongInfo = new InfoVo();
	    		wrongInfo.setUserStatus("卡号信息异常");
	    		mv.addObject("info", wrongInfo);
	    	}
	    	return mv ;
	    	
	    }
	    
	    @RequestMapping("/xinfu_wechat_pay")
	    public String getPay(){
	    	return "xfpay";
	    }
	    
}
