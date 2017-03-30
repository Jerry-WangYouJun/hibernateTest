package com.poiexcel.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.poiexcel.service.CardInfoService;
import com.poiexcel.util.DateUtils;
import com.poiexcel.vo.History;
import com.poiexcel.vo.InfoVo;


@Controller
@RequestMapping("/card")
public class CardInfoController {
	  
	    @Autowired
	    CardInfoService service;
	    
	    @RequestMapping("/querySingle")
	    public ModelAndView getCardInfo(String iccid){
	    	ModelAndView mv = new ModelAndView("index");
	    	InfoVo  vo = service.queryInfoByICCID(iccid);
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
	    public ModelAndView searchHistory(String iccid){
	    	ModelAndView mv = new ModelAndView("history");
	    	InfoVo  vo = service.queryInfoByICCID(iccid);
	    	if(vo!=null){
	    		List<History>  historyList = new ArrayList<History>() ;
	    		History h = new History();
	    		h.setMoney(36);
	    		h.setPname(("10M/月（包年）"));
	    		h.setUpdateDate(DateUtils.formatDate("yyyy-MM-dd"));
	    		h.setPremark("10M/月（包年）");
	    		historyList.add(h);
	    		vo.setHistory(historyList);
	    		mv.addObject("info", vo);
	    	}else{
	    		InfoVo   wrongInfo = new InfoVo();
	    		wrongInfo.setUserStatus("卡号信息异常");
	    		mv.addObject("info", wrongInfo);
	    	}
	    	return mv ;
	    	
	    }
	    
	    @RequestMapping("/xinfu_wechat_pay")
	    public ModelAndView getPay(@RequestParam("iccid") String iccid , HttpServletRequest request){
	    	if(iccid==null){
	    		iccid = request.getParameter("iccid");
	    	}
	    	ModelAndView mv = new ModelAndView("xfpay");
	    	mv.addObject("iccid", iccid);
	    	return mv;
	    }
	    
}
