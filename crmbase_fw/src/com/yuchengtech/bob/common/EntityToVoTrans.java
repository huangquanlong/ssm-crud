package com.yuchengtech.bob.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 
 * @describe A simple tool, it's used for translating JPA entity object to a VO
 *           object. Need to thinking in patterns maybe.
 * 
 */
public class EntityToVoTrans {

    /**
     * @describe Translate function.
     * @param majorEntity : The JPA Entity Object.
     * @return The VO object you want.
     * @throws Exception
     */
    public Object trans(Object majorEntity) {

        String majorClassName = majorEntity.getClass().toString();
        try {
            if (!majorClassName.startsWith("class com.yuchengtech.bob.model")) {
                throw new Exception("Entity Wrong, it's not a Entity!");
            }

            String simpleClassName = majorClassName.substring(32);
            String majorVoClassName = "com.yuchengtech.bob.vo." + simpleClassName + "Vo";
            Object majorVo = Class.forName(majorVoClassName).newInstance();
            
            Field[] modelFields = majorEntity.getClass().getDeclaredFields();
            for (Field f : modelFields) {
                String type = f.getGenericType().toString();
                if (type.equals("class java.lang.String")) {
                    /** do with String properties. */                   
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    String fieldValue = (String) getMethod.invoke(majorEntity);
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()),java.lang.String.class);
                    setMethod.invoke(majorVo, fieldValue);
                    
                } else if (type.equals("class java.lang.Integer")) {
                    /** do with Integer properties. */
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    Integer fieldValue = (Integer) getMethod.invoke(majorEntity);
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()), java.lang.Integer.class);
                    setMethod.invoke(majorVo, fieldValue);
                    
                } else if (type.equals("class java.lang.Boolean")) {
                    /** do with Boolean properties. */
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    Boolean fieldValue = (Boolean) getMethod.invoke(majorEntity);
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()), java.lang.Boolean.class);
                    setMethod.invoke(majorVo, fieldValue);
                    
                } else if (type.equals("class java.lang.Long")) {
                    /** do with Long properties. */
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    Long fieldValue = (Long) getMethod.invoke(majorEntity);
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()), java.lang.Long.class);
                    setMethod.invoke(majorVo, fieldValue);
                    
                } else if (type.equals("class java.util.Date")) {
                    /** do with Date properties. */
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    Date fieldValue = (Date) getMethod.invoke(majorEntity);
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()), java.util.Date.class);
                    setMethod.invoke(majorVo, fieldValue);
                    
                } else if (type.equals("class java.lang.Double")) {
                    /** do with Double properties. */
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    Double fieldValue = (Double) getMethod.invoke(majorEntity);
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()), java.lang.Double.class);
                    setMethod.invoke(majorVo, fieldValue);
                    
                } else if (type.equals("class java.lang.Short")) {
                    /** do with Short properties. */
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    Short fieldValue = (Short) getMethod.invoke(majorEntity);
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()), java.lang.Short.class);
                    setMethod.invoke(majorVo, fieldValue);
                    
                } else if (type.equals("class java.math.BigDecimal")){
                    /** do with BigDecimal properties. */
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    BigDecimal fieldValue = (BigDecimal) getMethod.invoke(majorEntity);
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()), java.math.BigDecimal.class);
                    setMethod.invoke(majorVo, fieldValue);
                    
                }else if (type.startsWith("java.util.List") || type.startsWith("java.util.ArrayList")) {
                    /**
                     * do with the List properties, which contains the ONE TO MANY Entity object.
                     */
                    Method getMethod = majorEntity.getClass().getMethod("get" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()));
                    @SuppressWarnings("unchecked")
                    List<Object> subList = (List<Object>) getMethod.invoke(majorEntity);
                    List<Object> subVoList = new ArrayList<Object>();
                    if (subList.size() > 0) {
                        for (Object o : subList) {
                            String subClassName = o.getClass().toString();
                            if (null != subClassName && subClassName.startsWith("class com.yuchengtech.bob.model")) {
                                Object subVo = new EntityToVoTrans().trans(o);
                                subVoList.add(subVo);
                            }
                        }
                    }
                    Method setMethod = majorVo.getClass().getMethod("set" + UpcaseFirstLetter.CheckAndSetFirst(f.getName()), java.util.List.class);
                    setMethod.invoke(majorVo, subVoList);
                    
                } else if (type.startsWith("class com.yuchengtech.bob.model")) {
                    /**
                     * TODO Code here is used to do with the ONE-TO-ONE
                     * relationship between two entities. Now i'v do nothing,
                     * for when there is a ONE TO ONE relationship between two
                     * entities, it's always so hard to know that which one is
                     * the major table, and which one is the sun table.
                     */
                }
            }
            
            return majorVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
