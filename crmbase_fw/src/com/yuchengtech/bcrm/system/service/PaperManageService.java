package com.yuchengtech.bcrm.system.service;

import java.math.BigDecimal;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.OcrmFSmPaper;
import com.yuchengtech.bcrm.system.model.OcrmFSmPapersQuestionRel;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 *问卷管理
 * @author wangwan
 * @since 2012-12-07 
 */
@Service
public class PaperManageService extends CommonService {
   
	public PaperManageService(){
		JPABaseDAO<OcrmFSmPaper, Long>  baseDAO=new JPABaseDAO<OcrmFSmPaper, Long>(OcrmFSmPaper.class);  
		super.setBaseDAO(baseDAO);
	}
	/**
	 * 新增试题
	 * @param jarray//新增试题的信息数组
	 */
	public void saveQ( JSONArray jarray){
		if (jarray.size() > 0){
			for (int i = 0; i < jarray.size(); ++i){
				JSONObject wa = (JSONObject)jarray.get(i);
				OcrmFSmPapersQuestionRel ws = new OcrmFSmPapersQuestionRel();
				ws.setPaperId(new BigDecimal((String)wa.get("paper_id")));
				ws.setQuestionId(new BigDecimal((String)wa.get("question_id")));
				ws.setQuestionOrder(new BigDecimal((String)wa.get("sort_id")));
				this.em.persist(ws);
			}
		}	
	}
	/**
	 * 删除试题
	 * @param jarray2//删除试题的信息数组
	 */
	public void removeQ(JSONArray jarray2){
		if (jarray2.size() > 0){
			for (int i = 0; i < jarray2.size(); ++i) {
				JSONObject wb = (JSONObject)jarray2.get(i);
				String t = (String)wb.get("id");
				OcrmFSmPapersQuestionRel ws2 = (OcrmFSmPapersQuestionRel)this.em.find(OcrmFSmPapersQuestionRel.class,Long.valueOf(t));
				if (ws2 != null){
					this.em.remove(ws2);
				}
			}
		}
	}
}
