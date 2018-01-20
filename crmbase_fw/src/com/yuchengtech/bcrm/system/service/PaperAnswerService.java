package com.yuchengtech.bcrm.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.OcrmFSeCustRiskInfoList;
import com.yuchengtech.bcrm.system.model.OcrmFSeCustRiskQa;
import com.yuchengtech.bcrm.system.model.OcrmFSeTitle;
import com.yuchengtech.bcrm.system.model.OcrmFSeTitleResult;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

@Service
public class PaperAnswerService extends CommonService {
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource dsOracle;

	public PaperAnswerService() {
		JPABaseDAO<OcrmFSeCustRiskInfoList, Long> baseDAO = new JPABaseDAO<OcrmFSeCustRiskInfoList, Long>(
				OcrmFSeCustRiskInfoList.class);
		super.setBaseDAO(baseDAO);
	}

	@SuppressWarnings("unchecked")
	public String addCustRiskEvaluation(OcrmFSeCustRiskInfoList model,
			String paperId,String user,String title_result)throws Exception {
		model.setEvaluateDate(new Date(System.currentTimeMillis()));
		model.setEvaluateName(user);
		model.setPaperId(Long.valueOf(paperId));
	
		model = (OcrmFSeCustRiskInfoList) baseDAO.save(model);

		String[] tr = title_result.split(",");
		for (String s : tr) {
			String title = s.split(":")[0];
			String result = s.split(":")[1];
			String score = s.split(":")[2];
			OcrmFSeCustRiskQa o2 = new OcrmFSeCustRiskQa();
			o2.setCustqId(model.getCustqId());
			o2.setQaTitle(title);
			o2.setCustSelectContent(result);
			o2.setScoring(BigDecimal.valueOf(Long.parseLong(score)));
			baseDAO.save(o2);
		}
		baseDAO.flush();
		return "success";
	}

	public OcrmFSeTitleResult getByResultId(Long resultId) {
		String JQL = "select t from OcrmFSeTitleResult t where t.resultId = ?1";
		Query q = em.createQuery(JQL);
		q.setParameter(1, resultId);
		return (OcrmFSeTitleResult) q.getResultList().get(0);
	}

	public OcrmFSeTitle getByTitleId(Long titleId) {
		String JQL = "select t from OcrmFSeTitle t where t.titleId = ?1";
		Query q = em.createQuery(JQL);
		q.setParameter(1, titleId);
		return (OcrmFSeTitle) q.getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> loadCustRiskQa(Long custqId) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
		String JQL = "select t from OcrmFSeCustRiskQa t where t.custqId = ?1";
		Query q = em.createQuery(JQL);
		q.setParameter(1, custqId);
		List<OcrmFSeCustRiskQa> rsList = q.getResultList();
		for (OcrmFSeCustRiskQa qa : rsList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("custqtId", qa.getCustqtId());
			map.put("custqId", qa.getCustqId());
			map.put("qaTitle", qa.getQaTitle());
			map.put("custSelectContent", qa.getCustSelectContent());
			map.put("scoring", qa.getScoring());
			map.put("titleRemark", qa.getTitleRemark());
			rowsList.add(map);
		}
		result.put("data", rowsList);
		result.put("count", rsList.size());
		return result;
	}

	public Object save(OcrmFSeCustRiskInfoList obj) {
		OcrmFSeCustRiskInfoList ocl = this.getOcrmFSeCustRiskInfoListById(obj
				.getCustqId());
		obj.setEvaluateDate(ocl.getEvaluateDate());
		return super.save(obj);
	}

	public OcrmFSeCustRiskInfoList getOcrmFSeCustRiskInfoListById(Long custqId) {
		String JQL = "select t from OcrmFSeCustRiskInfoList t where t.custqId = ?1";
		Query q = em.createQuery(JQL);
		q.setParameter(1, custqId);
		if (q.getResultList().size() > 0) {
			return (OcrmFSeCustRiskInfoList) q.getResultList().get(0);
		} else
			return null;
	}

}
