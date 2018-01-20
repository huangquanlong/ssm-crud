package com.yuchengtech.bob.common;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * <pre>
 * Title:封装基于JPA的数据库操作基类
 * Description: 封装开发中常用的数据操作，包括CURD、复杂查询、分页，开发数据库交互功能时，尽量直接使用此类或者继承此类进行扩展。
 *              此类底层功能基于googlecode的genericdao，进行了简化和扩展。
 *              具体的使用方式，可以参考JPABaseDaoTest.java中的测试用例。
 *              
 *  Examples:
 *             直接使用：               
 *              JPABaseDAO<User,Long> dao = new JPABaseDAO<User,Long>;
 *             
 *              继承扩展：
 *              
 *              public class UserDAO extends JPABaseDAO<User,Long>{
 *              
 *                 //扩展方法
 *              }
 *              
 *              UserDAO dao = new UserDAO();
 * 
 * 
 * </pre>
 * 
 * @author 
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
@Repository("baseDAO")
public class JPABaseDAO<T extends Object, PK extends Serializable> extends SimpleJPADAO<T, PK> {

	protected static Logger log = LoggerFactory.getLogger(JPABaseDAO.class);
	private JPASearchProcessor searchProcessor = null;

	// 构造函数
	public JPABaseDAO() {

		this.searchProcessor = new JPASearchProcessor(
				new JPAAnnotationMetadataUtil());
	}

	public JPABaseDAO(EntityManager entityManager, Class<T> entityClass) {

		this();
		this.entityManager = entityManager;
		this.entityClass = entityClass;

	}

	public JPABaseDAO(Class<T> entityClass) {

		this();
		this.entityClass = entityClass;

	}

	/**
	 * 按id删除对象.
	 */
	public void removeById(final PK id) {
		Assert.notNull(id, "id不能为空");
		remove(get(id));
		log.debug("delete entity {},id is {}",
				this.entityClass.getSimpleName(), id);
	}
/*
	/**
	 * 根据id获取对象列表
	 * 
	 * @param ids
	 * @return
	 *
	@SuppressWarnings("unchecked")
	public List<T> get(final Collection<PK> ids) {

		Search search = new Search(this.entityClass);
		search.setResultMode(Search.RESULT_LIST);

		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
		T entity;
		try {
			entity = this.entityClass.newInstance();
		} catch (Exception e) {

			log.error("根据主键集合查找对象 {}发生异常！ ", this.entityClass.getClass());
			throw new RuntimeException("根据主键集合查找对象发生异常", e);
		}
		String propertyName = metadataUtil.getIdPropertyName(entity);

		SearchUtil.addFilterIn(search, propertyName, ids);

		return searchProcessor.search(entityManager, search);
	}
	*/
	/**
	 * 根据id获取对象列表,并排序
	 * 
	 * @param ids
	 * @param orderByProperty
	 * @param isDesc true 降序，false升序
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> get(final Collection<PK> ids,String orderByProperty,boolean isDesc) {

		Search search = new Search(this.entityClass);
		search.setResultMode(Search.RESULT_LIST);

		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
		T entity;
		try {
			entity = this.entityClass.newInstance();
		} catch (Exception e) {

			log.error("根据主键集合查找对象 {}发生异常！ ", this.entityClass.getClass());
			throw new RuntimeException("根据主键集合查找对象发生异常", e);
		}
		String propertyName = metadataUtil.getIdPropertyName(entity);

		SearchUtil.addFilterIn(search, propertyName, ids);
		SearchUtil.addSort(search, orderByProperty, isDesc);

		return searchProcessor.search(entityManager, search);
	}

	/**
	 * 获取全部对象
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() {

		Search search = new Search(this.entityClass);
		search.setResultMode(Search.RESULT_LIST);

		return searchProcessor.search(entityManager, search);
	}

	/**
	 * 
	 * 获取全部对象, 支持按属性行序
	 * 
	 * @param orderByProperty
	 *            排序的属性名称
	 * @param isDesc
	 *            true：降序，false:升序
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll(String orderByProperty, boolean isDesc) {

		Search search = new Search(this.entityClass);
		search.setResultMode(Search.RESULT_LIST);

		SearchUtil.addSort(search, orderByProperty, isDesc);

		return searchProcessor.search(entityManager, search);
	}

	/**
	 * 按照属性查找对象，匹配方式为"="
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByProperty(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");

		Search search = new Search(this.entityClass);
		SearchUtil.addFilterEqual(search, propertyName, value);
		
		return searchProcessor.search(entityManager, search);
	}
	
	/**
	 * 按照属性查找对象，匹配方式为"="
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param orderByProperty
	 *            排序属性名称        
	 * @param isDesc
	 *            true:降序，false：升序           
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByPropertyAndOrder(final String propertyName, final Object value,final String orderByProperty,boolean isDesc) {
		Assert.hasText(propertyName, "propertyName不能为空");

		Search search = new Search(this.entityClass);
		SearchUtil.addFilterEqual(search, propertyName, value);
		SearchUtil.addSort(search, orderByProperty, isDesc);
		
		return searchProcessor.search(entityManager, search);
	}
	
	/**
	 * 按照属性查找对象，匹配方式为"in"
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param orderByProperty
	 *            排序属性名称        
	 * @param isDesc
	 *            true:降序，false：升序           
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByPropertyAndOrderWithParams(final String propertyName, final Collection<?> values,final String orderByProperty,boolean isDesc) {
		Assert.hasText(propertyName, "propertyName不能为空");

		Search search = new Search(this.entityClass);
		SearchUtil.addFilterIn(search, propertyName, values);
		SearchUtil.addSort(search, orderByProperty, isDesc);
		
		return searchProcessor.search(entityManager, search);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	@SuppressWarnings("unchecked")
	public T findUniqueByProperty(final String propertyName, final Object value) {

		Search search = new Search(this.entityClass);
		search.setResultMode(Search.RESULT_SINGLE);
		SearchUtil.addFilterEqual(search, propertyName, value);

		return (T) searchProcessor.searchUnique(entityManager, search);
	}

	/**
	 * 按JQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findWithIndexParam(final String jql,
			final Object... values) {
		return createQueryWithIndexParam(jql, values).getResultList();
	}

	/**
	 * 按JQL查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findWithNameParm(final String jql,
			final Map<String, ?> values) {
		return createQueryWithNameParam(jql, values).getResultList();
	}
	
	/**
	 * 按原生SQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public  List<Object[]> findByNativeSQLWithIndexParam(final String sql,
			final Object... values) {
		return createNativeQueryWithIndexParam(sql, values).getResultList();
	}
	
	
	/**
	 * 按原生SQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public  List<Object[]> findByNativeSQLWithNameParam(final String sql,
			final Map<String, ?> values) {
		return createNativeQueryWithNameParam(sql, values).getResultList();
	}

	/**
	 * 按JQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUniqueWithIndexParam(final String jql,
			final Object... values) {
		return (X) createQueryWithIndexParam(jql, values).getSingleResult();
	}

	/**
	 * 按JQL查询唯一对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUniqueWithNameParam(final String jql,
			final Map<String, ?> values) {
		return (X) createQueryWithNameParam(jql, values).getSingleResult();
	}

	/**
	 * 执行JQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecuteWithIndexParam(final String jql,
			final Object... values) {
		return createQueryWithIndexParam(jql, values).executeUpdate();
	}

	/**
	 * 执行JQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecuteWithNameParam(final String jql,
			final Map<String, ?> values) {
		return createQueryWithNameParam(jql, values).executeUpdate();
	}

	/**
	 * 按JQL分页查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> SearchResult<X> findPageIndexParam(final int firstResult,
			final int maxResult, final String jql, final Object... values) {

		SearchResult searResult = new SearchResult();

		List objectList = createQueryWithIndexParam(jql, values)
				.setFirstResult(firstResult).setMaxResults(maxResult)
				.getResultList();

		// 查询记录总数
		String countJql = SQLUtils.buildCountSQL(jql);
		Long totalCount = (Long) createQueryWithIndexParam(countJql, values)
				.getSingleResult();

		searResult.setResult(objectList);
		searResult.setTotalCount(totalCount.intValue());

		return searResult;

	}

	/**
	 * 按JQL分页查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> SearchResult<X> findPageWithNameParam(final int firstResult,
			final int maxResult, final String jql, final Map<String, ?> values) {

		SearchResult searResult = new SearchResult();

		List objectList = createQueryWithNameParam(jql, values)
				.setFirstResult(firstResult).setMaxResults(maxResult)
				.getResultList();

		// 查询记录总数
		String countJql = SQLUtils.buildCountSQL(jql);
		Long totalCount = (Long) createQueryWithNameParam(countJql, values)
				.getSingleResult();

		searResult.setResult(objectList);
		searResult.setTotalCount(totalCount.intValue());

		return searResult;
	}
	
	/**
	 * 按SQL分页查询对象列表. *暂不使用*
	 * 
	 * @param values
	 *            
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> SearchResult<X> findPageWithSQL(final int firstResult,
			final int maxResult, final String sql) {

		SearchResult searResult = new SearchResult();

		List objectList = createNativeQueryWithSQL(sql).setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
        
        for(int j=0;j<objectList.size();j++){
        	
        	Object[] row=(Object[])objectList.get(j);
        	int columNum=row.length;//获得列数
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i <columNum; i++) {
                String columnName = new Integer(i).toString();   //获取列名
                Object value = null;  //获得列值
                if(row[i]!=null)
                	value = row[i].toString();  //获得列值
                else
                	value = "";
                //system.out.printlnln("culum:"+columnName+"   value:"+value);
                map.put(columnName, value);     
                    //processLookup(map, columnName, rs.getObject(columnName));
                
            }
            rowsList.add(map);
        }
		
		// 查询记录总数
		String countSql = SQLUtils.buildCountSQL(sql);
		BigDecimal totalCount = (BigDecimal) createNativeQueryWithSQL(countSql)
				.getSingleResult();

		
		searResult.setResult(rowsList);
		searResult.setTotalCount(totalCount.intValue());

		return searResult;
	}
	
	/**
	 * 根据查询条件，返回对象列表
	 * @param search  查询对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> search(Search search){
		
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");
		if (this.entityClass != null && !search.getSearchClass().equals(this.entityClass))
			throw new IllegalArgumentException("Search class does not match expected type: " + this.entityClass.getName());

		return this.searchProcessor.search(this.entityManager, this.entityClass, search);
		
	}
	
	
    /**
     * 根据查询条件，返回符合条件的单个对象，用户单表查询
     * @param search 查询对象
     * @return
     */
	@SuppressWarnings("unchecked")
	public <X> X searchUnique(Search search){
		
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");

		return (X)this.searchProcessor.searchUnique(this.entityManager, search);
	}

    /**
     * 返回记录总数，用于单表查询
     * @param search  查询对象
     * @return
     */
	public int count(Search search){
		
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");

		return this.searchProcessor.count(this.entityManager, search);
	}

}

