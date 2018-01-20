package com.yuchengtech.bcrm.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bcrm.system.model.OcrmFSeTitle;
import com.yuchengtech.bcrm.system.model.OcrmFSeTitleResult;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 * @describe 试题管理模块service
 * @author wangwan
 * @since 2012.11.13
 *
 */
@Service
@Transactional(value="postgreTransactionManager")
public class QuestionManageService extends CommonService{
	
	public QuestionManageService(){
		JPABaseDAO<OcrmFSeTitle,Long> baseDao = new JPABaseDAO<OcrmFSeTitle,Long>(OcrmFSeTitle.class);
		
		super.setBaseDAO(baseDao);
	}
	private EntityManager em;
		
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
//	public void save(String titleName, String titleType, String available,
//			String updator,JSONArray jarray,JSONArray jarray2) {
//		// TODO Auto-generated method stub
//		OcrmFSeTitle ws = new OcrmFSeTitle();//保存
//		ws.setAvailable(available);
//		ws.setTitleName(titleName);
//		ws.setTitleType(titleType);
//		ws.setUpdateDate(new Date(System.currentTimeMillis()));
//		ws.setUpdator(updator);
//		this.em.merge(ws);
//		for(int i=0;i<jarray2.size();i++){
//			JSONObject wb = (JSONObject)jarray.get(i);
//			OcrmFSeTitle wq = new OcrmFSeTitle();
//			wq.setAvailable((String)wq.get("available"));
//			wq.setTitleName((String)wq.get("titleName"));
//			wq.setUpdateDate(new Date(System.currentTimeMillis()));
//			wq.setTitleType((String)wq.get("titleType"));
//			ws.setUpdator((String)wq.get("updator"));
//			this.em.merge(wq);
//		}
//		for(int i=0;i<jarray.size();i++){
//			JSONObject wa = (JSONObject)jarray.get(i);
//			OcrmFSeTitleResult wp = new OcrmFSeTitleResult();
//			wp.setResult((String)wa.get("result"));
//			wp.setResultScoring(new BigDecimal((String)wa.get("resultScoring")));
//			wp.setResultSort(new BigDecimal((String)wa.get("resultSort")));
//			wp.setTitleId(ws);
//		}
//		
////		if (map.get("resultInfo") != null && !map.get("resultInfo").equals("")) {
////			List<String> list = (List) map.get("resultInfo");
////			for (String s1 : list) {
////				String[] s2 = s1.split(":");
////				OcrmFSeTitleResult result = new OcrmFSeTitleResult();
////				result.setResult(s2[0]);
////				result.setResultScoring(BigDecimal.valueOf(Long
////						.parseLong(s2[1])));
////				result.setResultSort(BigDecimal.valueOf(Long.parseLong(s2[2])));
////				result.setTitleId(title);
////				baseDAO.save(result);
////			}
////		}
//		baseDAO.flush();
//		
//	}
	@SuppressWarnings("unchecked")
	public void createQuession(Map<String, Object> map) {
		OcrmFSeTitle title = new OcrmFSeTitle();

		title.setTitleIdL(new ArrayList());

		title.setUpdateDate(new Date(System.currentTimeMillis()));

		for (String key : map.keySet()) {
			if (null != map.get(key) && !map.get(key).equals("")) {
				if (key.equals("AVAILABLE")) {
					title.setAvailable(map.get(key).toString());
				} else if (key.equals("CREATE_TITLE_NAME")) {
					title.setTitleName(map.get(key).toString());
				} else if (key.equals("CREATE_TITLE_SORT")) {
					title.setTitleSort(BigDecimal.valueOf(Long.parseLong(map
							.get(key).toString())));
				} else if (key.equals("CREATE_TITLE_TYPE")) {
					title.setTitleType(map.get(key).toString());
				} else if (key.equals("UPDATOR")) {
					title.setUpdator(map.get(key).toString());
				}
			}
		}

		title = (OcrmFSeTitle) baseDAO.save(title);

		if (map.get("resultInfo") != null && !map.get("resultInfo").equals("")) {
			List<String> list = (List) map.get("resultInfo");
			for (String s1 : list) {
				String[] s2 = s1.split(":");
				OcrmFSeTitleResult result = new OcrmFSeTitleResult();
				result.setResult(s2[0]);
				result.setResultScoring(BigDecimal.valueOf(Long
						.parseLong(s2[1])));
				result.setResultSort(BigDecimal.valueOf(Long.parseLong(s2[2])));
				result.setTitleId(title);
				baseDAO.save(result);
			}

		}
		baseDAO.flush();
	}
}
