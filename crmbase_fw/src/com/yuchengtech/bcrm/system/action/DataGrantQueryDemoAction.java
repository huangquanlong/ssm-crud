package com.yuchengtech.bcrm.system.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.core.QueryHelper;

/**
 * @author GUOCHI
 * 
 */
@ParentPackage("json-default")
@Action(value = "/datagrantdemoquery", results = { @Result(name = "success", type = "json") })
public class DataGrantQueryDemoAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
    @SuppressWarnings("unchecked")
    private List depAndLonTreeList = new ArrayList();
	// private HttpServletRequest request;

	private Map<String, Object> JSON;

	public Map<String, Object> getJSON() {
		return JSON;
	}

	public void setJSON(Map<String, Object> jSON) {
		JSON = jSON;
	}

	public String index() {
		// ActionContext ctx = ActionContext.getContext();
		// request =
		// (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		try {
			//ORACLE
//			StringBuilder sb = new StringBuilder(
//					" SELECT DISTINCT (case when MAP.ID is null then x.ID else MAP.ID end) ID,"
//					+ "(case when (MAP.class_desc is NULL or MAP.class_desc = '' ) then x.NAME else MAP.class_desc end) NAME,"
//					+ "x.PARENT_ID,x.ORDER_  FROM CNT_MENU x  "
//					+ "left join FW_FUNCTION FW  ON x.MOD_FUNC_ID = FW.ID "
//					+ "left join AUTH_SYS_FILTER_MAP MAP ON FW.ID=MAP.FUNCTION_ID "
//					+ "START WITH x.ID in(SELECT DISTINCT C.ID "
//					+ "FROM CNT_MENU C  INNER JOIN FW_FUNCTION FW  ON C.MOD_FUNC_ID = FW.ID  "
//					+ "INNER JOIN AUTH_SYS_FILTER_MAP MAP ON FW.ID=MAP.FUNCTION_ID  "
//					+ "INNER JOIN AUTH_SYS_FILTER FIL ON MAP.CLASS_NAME=FIL.CLASS_NAME)  "
//					+ "CONNECT BY PRIOR x.PARENT_ID=x.ID ORDER BY x.ORDER_");
			//DB2
			StringBuilder sb = new StringBuilder(
					"WITH REPORT (ID,NAME,PARENT_ID,MOD_FUNC_ID,ORDER_,CLASS_NAME) AS" 
					+ "(SELECT (CASE WHEN MAP.ID IS NULL THEN X.ID ELSE MAP.ID END) ID,"
					+ "(CASE WHEN (MAP.CLASS_DESC IS NULL OR MAP.CLASS_DESC = '' ) THEN X.NAME ELSE MAP.CLASS_DESC END) NAME,"
					+ "X.PARENT_ID,X.MOD_FUNC_ID,X.ORDER_,MAP.CLASS_NAME  FROM CNT_MENU X  "
					+ "INNER JOIN FW_FUNCTION FW  ON X.MOD_FUNC_ID = FW.ID " 
					+ "LEFT JOIN AUTH_SYS_FILTER_MAP MAP ON FW.ID=MAP.FUNCTION_ID "
					+ "WHERE X.ID IN (SELECT DISTINCT C.ID FROM CNT_MENU C  " 
					+"INNER JOIN FW_FUNCTION FW  ON C.MOD_FUNC_ID = FW.ID " 
					+"INNER JOIN AUTH_SYS_FILTER_MAP MAP ON FW.ID=MAP.FUNCTION_ID "
					+"INNER JOIN AUTH_SYS_FILTER FIL ON MAP.CLASS_NAME=FIL.CLASS_NAME) "
					+ "UNION ALL SELECT A.ID,A.NAME,A.PARENT_ID,A.MOD_FUNC_ID,A.ORDER_,'' FROM CNT_MENU A, REPORT B WHERE A.ID= B.PARENT_ID )"
					+ "SELECT DISTINCT * FROM REPORT ORDER BY ID");
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
			getDepAndLonTreeList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}

  @SuppressWarnings("unchecked")
public List getDepAndLonTreeList() {
      
      List scheduleList = (List)this.getJSON().get("data");
      
      if(scheduleList!=null){
          
          int level=0;//树节点的层次
          Map leftMap = new HashMap();//节点left开始位置
          Map rightMap = new HashMap();//节点right结束位置
          leftMap.put("left", 1);
          rightMap.put("right", 0);
          Map rootMap=null;
          for(int i=0;i<scheduleList.size();i++){
              Map tempMap = (Map)scheduleList.get(i);
              if((tempMap.get("PARENT_ID")+"").equals("0")){
                  rootMap = tempMap;
                  rootMap.put("_parent", "null");
                  rootMap.put("_is_leaf",false);
                  rootMap.put("_level", "1");
                  rootMap.put("_id", tempMap.get("ID")+"");
                  buildChild(rootMap,scheduleList,level);//构造树,并且计算层级
                  ArrayList treeList = new ArrayList();
                  buildClientTree(rootMap,leftMap,rightMap,treeList);//计算每个节点的left和right位置.
                  for(int j=0;j<treeList.size();j++)
                      depAndLonTreeList.add(treeList.get(j));
              }
          }
      }
      return depAndLonTreeList;
  }
  //构造treegrid的left和right位置
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Map  buildClientTree(Map rootMap,Map leftMap,Map rightMap,List treeList){
      
      List childList = (List)rootMap.get("children");
      
      if(childList==null || childList.size()==0 ){//如果是叶子节点
          int left = (Integer)rightMap.get("right")+1;
          rightMap.put("right", left+1);//下一个子节点的开始位置
          leftMap.put("left", left);//下一个叶子节点额开始位置
          rootMap.put("_lft", left);//叶子节点的right和left相同.
          rootMap.put("_rgt", left+1);
          rootMap.put("_is_leaf", true);
          rootMap.remove("children");
          treeList.add(rootMap);
        
          return rightMap;
          
      }else//如果是子节点
      {           
          int left = (Integer)rightMap.get("right")+1;//子节点left的开始位置是,前一个right后开始
          leftMap.put("left", left);//设置子节点下叶子节点的left开始位置
          rightMap.put("right", left);//子节点的right开始位置
          rootMap.put("_lft", left);
          
          rootMap.put("_is_leaf",false);
          
          for(int i=0;i<childList.size();i++){
              
              Map nodeMap = (Map)childList.get(i);
              rightMap = buildClientTree(nodeMap,leftMap,rightMap,treeList);
          }
          int right = (Integer)rightMap.get("right")+1;//得到当前节点的右边位置
          rightMap.put("right", right);//
          leftMap.put("left", right); //下一节点left的开始位置，在当前节点后。     
          rootMap.put("_rgt", right);
          
          rootMap.remove("children");
          treeList.add(rootMap);
          return rightMap;
      }
  }
  
  //递归,构造树状结构,并且计算每个节点的层次level
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void buildChild(Map parentMap,List scheduleList,int level){
      level++;//每递归执行一次,层次加1
      ArrayList childList = new ArrayList();      
      for(int i=0;i<scheduleList.size();i++){
          
          Map tempMap = (Map)scheduleList.get(i);
          
          if((tempMap.get("PARENT_ID")+"").equals((parentMap.get("ID")+"")) ){
              
              tempMap.put("_parent", tempMap.get("PARENT_ID")+"");
              
              childList.add(tempMap);             
              
              buildChild(tempMap,scheduleList,level);
          }
      }
      parentMap.put("_level", level);//添加层次
      parentMap.put("_id", parentMap.get("ID")+"");
      
      parentMap.put("children", childList);   

  }
  @SuppressWarnings("unchecked")
public void setDepAndLonTreeList(List depAndLonTreeList) {
      this.depAndLonTreeList = depAndLonTreeList;
  } 	
}
