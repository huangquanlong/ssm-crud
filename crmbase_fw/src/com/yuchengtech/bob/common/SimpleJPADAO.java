package com.yuchengtech.bob.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;


/**
 * <pre>
 * Title:封装JPA的原生API操作基类
 * Description: 对JPA的原生API进行封装，建议不要直接使用此类，使用此类的子类JPABaseDAO
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
public class SimpleJPADAO<T, PK extends Serializable> {

	
	protected static Logger log = LoggerFactory.getLogger(SimpleJPADAO.class);
	
	@SuppressWarnings("unchecked")
	protected Class<T> entityClass = (Class<T>)ReflectionUtils.getTypeArguments(SimpleJPADAO.class,this.getClass()).get(0);
	
	@PersistenceContext
	protected EntityManager entityManager ;
	//private EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("BOB_JPA_ORACLE");
	//protected EntityManager entityManager = emf.createEntityManager();
	
	
	//构造函数
	public SimpleJPADAO(){

	}
	
	public SimpleJPADAO(EntityManager entityManager,Class<T> entityClass){
		
		this.entityManager = entityManager;
		this.entityClass = entityClass;
		
	}
	
	public SimpleJPADAO(Class<T> entityClass){

		this.entityClass = entityClass;
		
	}
	
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	//@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * 保存新增的对象
	 * @param entity 
	 */
	public void persist(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		log.debug("persist entity: {}", entity);
		
		 entityManager.persist(entity);
		
	}
	
	/**
	 * 修改对象
	 * @param entity
	 */
	public T merge(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		log.debug("merge entity: {}", entity);
		return entityManager.merge(entity);
		
	}

	/**
	 * 保存或者修改对象
	 * @param entity
	 */
	public T save(final T entity) {

		Assert.notNull(entity, "entity不能为空");
		log.info("save entity: {}", entity);
		//AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (entityManager.contains(entity))
			return entity;
		
		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
		
		Serializable id = metadataUtil.getId(entity);
		if (!validId(id)) {
			this.persist(entity);
			//auth.setPid(metadataUtil.getId(entity).toString());
			return entity;
		}
		
		@SuppressWarnings("unchecked")
		T prev = entityManager.find((Class<T>) entity.getClass(), id);
		if (prev == null) {
			this.persist(entity);
			//auth.setPid(metadataUtil.getId(entity).toString());
			return entity;
		} else {
			//auth.setPid(id.toString());
			BeanUtils.copyProperties(entity, prev);
			return this.merge(prev);
		}
		
		
	}
	


	/**
	 * 删除对象.
	 * 
	 * @param entity .
	 */
	public void remove(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		entityManager.remove(entity);
		log.debug("delete entity: {}", entity);
	}

	
	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	public T get(final PK id) {
		Assert.notNull(id, "id不能为空");
		return (T) entityManager.find(this.entityClass, id);
	}
	
	
	/**
	 * 根据查询JQL语句与参数列表创建Query对象，JQL中参数按顺序绑定，从0开始.
	 * 
	 * @param values 一个或者多个参数
	 */
	public Query createQueryWithIndexParam(final String queryJQL, final Object... values) {
		Assert.hasText(queryJQL, "queryJQL不能为空");
		Query query = this.entityManager.createQuery(queryJQL);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	/**
	 * 根据查询JQL语句与命名参数列表创建Query对象，JQL中参数按名称绑定
	 * 
	 * @param values 参数Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Query createQueryWithNameParam(final String queryJQL, final Map<String, ?> values) {
		Assert.hasText(queryJQL, "queryJQL不能为空");
		Query query = this.entityManager.createQuery(queryJQL);
		
		if (values != null) {
			
			Iterator it = values.entrySet().iterator();
			
			while(it.hasNext()){
				
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
				query.setParameter(entry.getKey(), entry.getValue());
			}
			
		}
		
		
		
		return query;
	}

	/**
	 * 根据查询SQL语句创建Query对象
	 * 
	 * @param values 一个或者多个参数
	 */
	public Query createNativeQueryWithSQL(final String sql) {
		
		Assert.hasText(sql, "sql不能为空");
		Query query = this.entityManager.createNativeQuery(sql);
        
		return query;
	}
	
	/**
	 * 根据查询SQL语句与参数列表创建Query对象，SQL中参数按顺序绑定，从0开始.
	 * 
	 * @param values 一个或者多个参数
	 */
	public Query createNativeQueryWithIndexParam(final String sql, final Object... values) {
		
		Assert.hasText(sql, "sql不能为空");
		Query query = this.entityManager.createNativeQuery(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	
	
	/**
	 * 根据查询SQL语句与参数列表创建Query对象，JQL中参数按名称绑定
	 * 
	 * @param values 一个或者多个参数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Query createNativeQueryWithNameParam(final String sql, final Map<String, ?> values) {
		
		Assert.hasText(sql, "sql不能为空");
		Query query = this.entityManager.createNativeQuery(sql);
		if (values != null) {
			
			Iterator it = values.entrySet().iterator();
			
			while(it.hasNext()){
				
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
				query.setParameter(entry.getKey(), entry.getValue());
			}
			
		}
		return query;
	}



	/**
	 * Flush当前Session.
	 */
	public void flush() {
		this.entityManager.flush();
	}

	
	/**
	 * 验证ID是否有效
	 * @param id
	 * @return
	 */
	private boolean validId(Serializable id) {
		if (id == null)
			return false;
		if (id instanceof Number && ((Number) id).equals(0))
			return false;
		if (id instanceof String && "".equals(id))
			return false;
		return true;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
}
