package com.yuchengtech.bcrm.system.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AuthResAttrData;
import com.yuchengtech.bcrm.system.model.AuthResControlAttrData;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.core.MenuManager;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.upload.poi.ExportCellStyle;
import com.yuchengtech.bob.upload.poi.POIUtils;
import com.yuchengtech.crm.constance.MenuTreeNode;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.sec.common.SystemUserConstance;

/**
 * 资源权限配置 保存设置按钮 service
 * @author wz
 * @since 2012-10-17
 */
@Service
public class RoleMenuOptionService extends CommonService {

	@Autowired
    @Qualifier("dsOracle")
    private DataSource dsOracle;
	/**
	 * 创建baseDAO
	 */
	public RoleMenuOptionService() {
		JPABaseDAO<AuthResAttrData, Long> baseDAO = new JPABaseDAO<AuthResAttrData, Long>(
				AuthResAttrData.class);
		super.setBaseDAO(baseDAO);
	}
	
	protected EntityManager em ;//声明EntityManager
	
	public EntityManager getEntityManager(){
		return this.em;
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	/***菜单节点list*/
	private List<MenuTreeNode> menuNodeList;
	/***是否有权限符号*/
	private static String isAuthChar = "√";
	/**
	 * 保存资源权限设置项
	 * @param menuCodeStr:选择的菜单项
	 * @param optionCodeStr:选择的控制点项
	 * @param roleCodeGloble:选择的角儿项
	 * @param menuCodeGloble:选择的控制点对应的菜单项
	 * @return success
	 */
	@SuppressWarnings("unchecked")
	public String optionMenuSave(String[] menuAddCodeStr,String[] menuDelCodeStr, String[] optionCodeStr,String roleCodeGloble,String menuCodeGloble, String[] addGrantsStr, String[] delGrantsStr){
		
		Map<String,Object> values=new HashMap<String,Object>();
		String optionCode = "";
		String leftStr = "[";
		String rightStr = "]";
		int aa=0;
		
		/**判断所选角色是否配置菜单，如果有先清空，后添加新菜单项*/
		//表auth_res_attr_data
		JPABaseDAO<AuthResAttrData, Long> baseDAO1 = new JPABaseDAO<AuthResAttrData, Long>(AuthResAttrData.class);
		baseDAO1.setEntityManager(em);
		String attrDataQueryJQL = "select a.id from AuthResAttrData a where a.attrCode='"+roleCodeGloble+"'";
		List attrDataList = baseDAO1.findWithNameParm(attrDataQueryJQL, values);		
		if(attrDataList != null && menuDelCodeStr != null){
			for(int a=0; a<menuDelCodeStr.length;a++){
				String attrDataDelJQL = "delete from AuthResAttrData a where a.attrCode='"+roleCodeGloble+"' and a.resCode = '"+menuDelCodeStr[a]+"'";
				baseDAO1.batchExecuteWithNameParam(attrDataDelJQL, values);
			}
		}

		if(menuAddCodeStr != null){
			for(int i=0; i<menuAddCodeStr.length; i++){//向auth_res_attr_data表保存所选角色对应菜单项
				String searchSql = "select n from AuthResAttrData n where n.attrId =?1";
				Query query = em.createQuery(searchSql);
				query.setParameter(1, Long.valueOf(roleCodeGloble));
				List<AuthResAttrData> result = (List<AuthResAttrData>)query.getResultList();
				
				for(AuthResAttrData aapl : result){//与历史循环比对
					if((menuAddCodeStr[i].equals( aapl.getResCode()))&&(roleCodeGloble.endsWith(aapl.getAttrCode()))){
						aa++;
					}
				}
				if(aa==0 && !menuAddCodeStr[i].startsWith("ctr_")){
					AuthResAttrData arad = new AuthResAttrData();
					arad.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
					arad.setAttrCode(roleCodeGloble);
					arad.setAttrId(Long.valueOf(SystemConstance.LOGIC_SYSTEM_ATTR_ID));
					arad.setOperateKey("[\"VIEW\",\"AUTH_PERMISSION\"]");
					arad.setResCode(menuAddCodeStr[i]);
					arad.setResId(Long.valueOf(SystemConstance.LOGIC_SYSTEM_RES_ID));
					arad.setType(0);
					arad.setVersion(0);
					baseDAO1.save(arad);
				}
			
			}
		}
		
//		/**判断所选角色和菜单是否配置按钮功能，如果有先清空，后添加新按钮功能项*/
//		//表auth_res_control_attr_data
//		JPABaseDAO<AuthResControlAttrData, Long> baseDAO2 = new JPABaseDAO<AuthResControlAttrData, Long>(AuthResControlAttrData.class);
//		baseDAO2.setEntityManager(em);
//		if(optionCodeStr != null){
//			for(int j=0; j<optionCodeStr.length; j++){//向auth_res_control_attr_data表保存所选角色和菜单中的功能按钮项
//				optionCode = optionCode + "\""+ optionCodeStr[j] + "\"";
//				if(j != optionCodeStr.length-1){
//					optionCode = optionCode + ",";
//				}
//			}
//		}
//		optionCode = leftStr + optionCode + rightStr;//拼接operate_key字段字符形式 : ["",""]
//		String ctlAttrDataQueryJQL = "select a.id from AuthResControlAttrData a where a.attrCode='"+roleCodeGloble+"'" +
//		" and a.resCode='"+menuCodeGloble+"'";
//		List ctlAttrDataList = baseDAO2.findWithNameParm(ctlAttrDataQueryJQL, values);
//		if(ctlAttrDataList != null){
//			String attrDataDelJQL = "delete from AuthResControlAttrData a where a.attrCode='"+roleCodeGloble+"'" +
//			" and a.resCode='"+menuCodeGloble+"'";
//			baseDAO2.batchExecuteWithNameParam(attrDataDelJQL, values);
//		}
//		/**保存菜单中对应按钮配置项*/
//		if(optionCodeStr != null && !"".equals(optionCodeStr)){
//			AuthResControlAttrData arcad = new AuthResControlAttrData();
//			arcad.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
//			arcad.setAttrCode(roleCodeGloble);
//			arcad.setAttrId(Long.valueOf(SystemConstance.LOGIC_SYSTEM_ATTR_ID));
//			arcad.setOperateKey(optionCode);
//			arcad.setResCode(menuCodeGloble);
//			arcad.setResId(Long.valueOf(SystemConstance.LOGIC_SYSTEM_RES_ID));
//			arcad.setType(0);
//			arcad.setVersion(0);
//			baseDAO2.save(arcad);
//		}
		saveOpSet(roleCodeGloble, addGrantsStr, delGrantsStr);
		return "success";
	}
	/**
	 * 保存资源权限设置项
	 * @param roleCodeGloble: 选择的角色项
	 * @param addGrantsStr: 新增的控制点信息
	 * @param delGrantsStr: 删除的控制点信息
	 * @return 
	 */
	private void saveOpSet(String roleCodeGloble, String[] addGrantsStr, String[] delGrantsStr) {
		/**判断所选角色和菜单是否配置按钮功能，如果有先清空，后添加新按钮功能项*/
		//表auth_res_control_attr_data
		JPABaseDAO<AuthResControlAttrData, Long> saveOpSetDAO = new JPABaseDAO<AuthResControlAttrData, Long>(AuthResControlAttrData.class);
		saveOpSetDAO.setEntityManager(em);
		Map<String,Object> values=new HashMap<String,Object>();
		
		Map<String,Object> addOpMap = new HashMap<String,Object>();
		Map<String,Object> delOpMap = new HashMap<String,Object>();
		//查询控制点信息
		String ctlAttrDataQueryJQL = "select a from AuthResControlAttrData a where a.attrCode='"+roleCodeGloble+"'";
		List<AuthResControlAttrData> attrDataList = saveOpSetDAO.findWithNameParm(ctlAttrDataQueryJQL, values);
		//删除控制点信息
		if(attrDataList != null){
			String attrDataDelJQL = "delete from AuthResControlAttrData a where a.attrCode='"+roleCodeGloble+"'";
			saveOpSetDAO.batchExecuteWithNameParam(attrDataDelJQL, values);
		}
		List<Long> removeIds = new ArrayList<Long>();
		//移除控制点信息处理
		for (int i = 0; i < attrDataList.size(); i++) {
			AuthResControlAttrData removeARCAD = (AuthResControlAttrData)attrDataList.get(i);
			if (delGrantsStr != null) {
				for (int j = 0; j < delGrantsStr.length; j++) {
					String menuCode = delGrantsStr[j].split(" ")[0];
					String opCode = delGrantsStr[j].split(" ")[1];
						if (menuCode.equals(removeARCAD.getResCode())) {
							String replaceString = "";
							if (removeARCAD.getOperateKey().contains(",\""+opCode+"\"")) {
								replaceString = ",\""+opCode+"\"";
							} else if (removeARCAD.getOperateKey().contains("\""+opCode+"\",")) {
								replaceString = "\""+opCode+"\",";
							} else {
								replaceString = "\""+opCode+"\"";
							}
							removeARCAD.setOperateKey(removeARCAD.getOperateKey().replace(replaceString, ""));
							if (!removeARCAD.getOperateKey().contains("\"")) {
								//attrDataList.remove(removeARCAD);
								removeIds.add(removeARCAD.getId());
							}
					}
				}
			}
		}
		//移除控制点信息
		for (Long id : removeIds) {
			for (int i = 0; i < attrDataList.size(); i++) {
				if (id == attrDataList.get(i).getId()) {
					attrDataList.remove(attrDataList.get(i));
					break;
				}
			}
		}
		
		//新增控制点信息处理
		if (addGrantsStr != null) {
			for (int j = 0; j < addGrantsStr.length; j++) {
				String menuCode = addGrantsStr[j].split(" ")[0];
				String opCode   = addGrantsStr[j].split(" ")[1];
				List list = new ArrayList();
				//数据库中已保存过的数据处理
				for (int i = 0; i < attrDataList.size(); i++) {
					AuthResControlAttrData addARCAD = (AuthResControlAttrData)attrDataList.get(i);
					if (menuCode.equals(addARCAD.getResCode())) {
						AuthResControlAttrData addtemp = addARCAD;	
						if (!addtemp.getOperateKey().contains("\""+opCode+"\"")) {
							addtemp.setOperateKey(((String)addARCAD.getOperateKey()).replace("[", "[\""+opCode+"\","));				
						}
						list.add(addtemp);
					} 
				}
				//数据库中未保存过的数据处理
				if (list.size() == 0) {
					AuthResControlAttrData addtemp = new AuthResControlAttrData();	
					addtemp.setResCode(menuCode);
					addtemp.setOperateKey("[\""+opCode+"\"]");				
					attrDataList.add(addtemp);
				}
			}		 
		}
		
		/**保存菜单中对应按钮配置项*/
		for (int i = 0; i < attrDataList.size(); i++){
			AuthResControlAttrData arcad = new AuthResControlAttrData();
			arcad.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			arcad.setAttrCode(roleCodeGloble);
			arcad.setAttrId(Long.valueOf(SystemConstance.LOGIC_SYSTEM_ATTR_ID));
			arcad.setResId(Long.valueOf(SystemConstance.LOGIC_SYSTEM_RES_ID));
			arcad.setResCode(attrDataList.get(i).getResCode());
			arcad.setOperateKey(attrDataList.get(i).getOperateKey());
			arcad.setType(0);
			arcad.setVersion(0);
			saveOpSetDAO.save(arcad);
		}
	}
	/**
	 * 导出资源权限配置信息
	 */
	public void exportAuthInfo(String fileName, String rolesStr) {
		StringBuilder sb = new StringBuilder("");
		sb.append("select m.id, m.app_id,  m.res_id, m.attr_id, m.res_code,m.attr_code, m.operate_key " +
				"from auth_res_attr_data m where 1=1 and m.app_id = " + SystemConstance.LOGIC_SYSTEM_APP_ID );
		sb.append(" and m.attr_code in ( " + rolesStr +")");
		sb.append(" union all ");
		sb.append("select t.id, t.app_id,t.res_id, t.attr_id, t.res_code, t.attr_code,t.operate_key " +
				" from AUTH_RES_CONTROL_ATTR_DATA t " +
				" where t.attr_code in ( " + rolesStr +") and t.app_id = " + SystemConstance.LOGIC_SYSTEM_APP_ID);
		
		QueryHelper qh;
		List authList = new ArrayList();
		try {
			qh       = new QueryHelper(sb.toString(), dsOracle.getConnection());
			authList = (List) qh.getJSON().get("data");
			writeAuthData(fileName, authList, rolesStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean writeAuthData(String fileName, List<Map> authList, String rolesStr) throws FileNotFoundException {
		String exportType = "xls";
		int currentRow    = 1;
		int currentColumn = 0;
		Long MaxMenuLevel =  MenuManager.getInstance().menuMaxLevel;
		Workbook wb = new HSSFWorkbook();
		fmtRowDataOdd = wb.createCellStyle();
		fmtRowDataEve = wb.createCellStyle();
		Font font = wb.createFont();
		Sheet   cus = wb.createSheet("授权矩阵");;
		List<Map> columnList = new ArrayList<Map>();
		menuNodeList = new ArrayList<MenuTreeNode>();
		pushMenuArr(MenuManager.getInstance().getMenuTree());
		List<MenuTreeNode> rowList = this.menuNodeList;
		for (int i = 1; i <= MaxMenuLevel; i++) {
			Map map = new HashMap();
			map = new HashMap();
			map.put("id", "menu"+i);
			map.put("name", i+"级菜单");
			columnList.add(map);
		}
		
		List roleList = getRoleList(rolesStr);
		if (roleList != null) {
			for (int i = 0; i < roleList.size(); i++) {
				Map roleMap = (Map)roleList.get(i);
				Map map = new HashMap();
				map = new HashMap();
				map.put("id", (String)roleMap.get("ID"));
				map.put("name", (String)roleMap.get("ROLE_NAME"));
				columnList.add(map);
			}
		}
		File file = createFile(fileName, exportType);
		FileOutputStream fileOut = new FileOutputStream(file);
		try {
			//标题行
			Row titleRow = cus.createRow(0);
			for (Map colMap : columnList) {//循环列
				Cell cell = titleRow.createCell(currentColumn);
				cell.setCellStyle(getFmtColumnNames(wb));
				POIUtils.setCellValue(cell, (String) colMap.get("name"));
	            currentColumn++;
			}
			//矩阵中内容
			for (MenuTreeNode rowMap : rowList) {
				//循环结果集
				Row row = cus.createRow(currentRow);
				currentColumn = 0;
				for (Map colMap : columnList) {//循环列
					Cell cell = row.createCell(currentColumn);
					cell.setCellStyle(getCurrRowStyle(currentRow, font));
					//菜单部分
					if (currentColumn < MaxMenuLevel 
							&& currentColumn == rowMap.getNodeLevel()-1) {
						POIUtils.setCellValue(cell, rowMap.getName());
					} 
					//权限部分
					else if (currentColumn >= MaxMenuLevel) {
						POIUtils.setCellValue(cell, isAuthed(authList,rowMap,colMap)==true?isAuthChar:"");
					} else {
						POIUtils.setCellValue(cell, "");
					}
		            currentColumn++;
				}
				currentRow++;
			}
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e){
			e.printStackTrace();
			return false;
		} 		
		return true;
	}
	/***
	 * 递归处理菜单List
	 * @param node
	 */
	private void pushMenuArr(MenuTreeNode node) {
		if (node.getIsLeaf()) {
			return;
		} else {
			for (MenuTreeNode child : node.getChildren()) {
				menuNodeList.add(child);
				pushMenuArr(child);
			}
		}
	}
	/***
	 * 授权判断
	 * @param authList 授权list
	 * @param menuNode 菜单节点
	 * @param roleMap 角色信息
	 * @return
	 */
	private boolean isAuthed (List<Map> authList,MenuTreeNode menuNode, Map roleMap) {
		boolean isAuthed = false;
		if ("0".equals(menuNode.getNodeType())) {
			for (Map authMap : authList) {
				if (menuNode.getId().equals(authMap.get("RES_CODE"))
						&& authMap.get("ATTR_CODE").equals(roleMap.get("id"))) {
					isAuthed = true;
					break;
				}
			}
		} 
		else if ("1".equals(menuNode.getNodeType())) {
			for (Map authMap : authList) {
				if (null != menuNode.getAttribute("opCode")) {
					if (menuNode.getParentId().equals(authMap.get("RES_CODE"))
							&& authMap.get("ATTR_CODE").equals(roleMap.get("id"))
							&& ((String)authMap.get("OPERATE_KEY")).indexOf("\""+menuNode.getAttribute("opCode")+"\"") != -1) {
						isAuthed = true;
						break;
					}
				}
			}
		}
		return isAuthed;
	}
	/***
	 * 查询角色列表
	 * @param roleStr 角色Ids
	 * @return
	 */
	private List getRoleList(String roleStr) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ADMIN_AUTH_ROLE A " +
				" WHERE A.ROLE_TYPE = '"+SystemUserConstance.NORMAL_MANAGER_ROLE+"'" +
				" AND A.ID IN (" + roleStr +" ) ORDER BY A.ID");
		QueryHelper qh;
		List roles;
		try {
			qh = new QueryHelper(sql.toString(), dsOracle.getConnection());
			roles = (List) qh.getJSON().get("data");
			if (roles != null && roles.size() > 0) {
				return roles;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 创建文件
	 * @return
	 * @throws IOException
	 */
	private File createFile(String fileName, String exportType){
		StringBuilder builder = new StringBuilder();
        builder.append(FileTypeConstance.getExportPath());
        if (!builder.toString().endsWith(File.separator)) {
            builder.append(File.separator);
        }
        File file = new File(builder.toString());
        if (! file.exists()) {
            file.mkdirs();
        }
        builder.append(fileName + "." + exportType.toLowerCase());
        fileName = builder.toString();
        file = new File(fileName);
        try {
        	if (file.exists()) {
        		file.delete();
        	} 
        	file.createNewFile();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
        return file;
	}
	
	/**数据页表头*/
	private CellStyle fmtColumnNames = null;
	/**首页信息*/
	private CellStyle fmtIndexInfo = null;
	/**偶数行样式*/
	private CellStyle fmtRowDataOdd = null;
	/**奇数行样式*/
	private CellStyle fmtRowDataEve = null;
	/**
	 * 根据行号返回单元格样式
	 * @param currentRow
	 * @return
	 */
	private CellStyle getCurrRowStyle(int currentRow, Font font){
		return (currentRow&1)==1? getFmtRowDataOdd(fmtRowDataOdd, font) : getFmtRowDataEve(fmtRowDataEve, font);
	}
	public CellStyle getFmtRowDataOdd(CellStyle currRowStyle,Font font) {
		return getExcellStyle().getFmtRowDataOdd(currRowStyle, font);
	}
	
	public CellStyle getFmtRowDataEve(CellStyle currRowStyle,Font font) {
		 return getExcellStyle().getFmtRowDataEve( currRowStyle, font);
	}
	public ExportCellStyle getExcellStyle() {
		if (excellStyle == null) {
			excellStyle = new ExportCellStyle();
		}
		return excellStyle;
	}
	/**样式*/
	private ExportCellStyle excellStyle = null;
	/***表头样式*/
	public CellStyle getFmtColumnNames(Workbook wb) {
		fmtColumnNames = wb.createCellStyle();
		try {
			/**14号字体，楷体，加粗*/
			Font font = wb.createFont();
			font.setFontHeightInPoints((short)13);
		    font.setFontName("楷体");
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			fmtColumnNames.setFont(font);
			fmtColumnNames.setWrapText(true);
			fmtColumnNames.setAlignment(CellStyle.ALIGN_CENTER);
			fmtColumnNames.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			fmtColumnNames.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			fmtColumnNames.setFillPattern(CellStyle.SOLID_FOREGROUND);
		} catch (Exception e) {}
		return fmtColumnNames;
	}
}
