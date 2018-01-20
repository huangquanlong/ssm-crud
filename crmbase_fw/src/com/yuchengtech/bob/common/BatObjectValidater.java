package com.yuchengtech.bob.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BatObjectValidater {
    
    private Object voObj;

    private String className = "";
        
    Boolean isBat = false;
        
    private String[] paraListArray;
    
    public BatObjectValidater(Object voObj){      
        
        this.className = voObj.getClass().toString().substring(6);
        try {            
            this.isBat = (Boolean)voObj.getClass().getMethod("getIsBat").invoke(voObj);
            if(this.isBat){
                String batStrings = (String)voObj.getClass().getMethod("getBatString").invoke(voObj);
                paraListArray = batStrings.split(";");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }       
        this.voObj = voObj;
    }
    
    public List<Object> Validate() {
        if (this.isBat) {
            List<Object> list = new ArrayList<Object>();
            try {
                if(paraListArray.length>0){
                    int dataLength = 0;
                    for(int fieldCount=0;fieldCount<paraListArray.length;fieldCount++){
                        if((paraListArray[fieldCount].split(":").length)>dataLength){
                           dataLength = paraListArray[fieldCount].split(":").length;
                        }
                    }
                    if(dataLength==0){
                        return null;
                    }
                    for(int i=1;i<dataLength;i++){
                        Object listMember = Class.forName(this.className).newInstance();
                        for(int j=0;j<paraListArray.length;j++){
                            String fieldName = paraListArray[j].split(":")[0];
                            if(fieldName!=null&&!"".equals(fieldName)){
                                Field parField = voObj.getClass().getDeclaredField(fieldName);
                                String fieldType = parField.getType().getName();
                                String fieldData = paraListArray[j].split(":")[i];
                                if(null==fieldData||"".equals(fieldData)){
                                    continue;
                                }
                                Method parSetMethod = voObj.getClass().getMethod("set"+UpcaseFirstLetter.CheckAndSetFirst(fieldName), Class.forName(fieldType));
                                if("java.lang.String".equals(fieldType)){
                                    parSetMethod.invoke(listMember, String.valueOf(fieldData));
                                } else if("java.lang.Integer".equals(fieldType)){
                                    parSetMethod.invoke(listMember, Integer.valueOf(fieldData));
                                } else if("java.lang.Boolean".equals(fieldType)){
                                    parSetMethod.invoke(listMember, Boolean.valueOf(fieldData));
                                } else if("java.lang.Long".equals(fieldType)){
                                    parSetMethod.invoke(listMember, Long.valueOf(fieldData));
                                } else if("java.lang.Double".equals(fieldType)){
                                    parSetMethod.invoke(listMember, Double.valueOf(fieldData));
                                } else if("java.lang.Short".equals(fieldType)){
                                    parSetMethod.invoke(listMember, Short.valueOf(fieldData));
                                }
                            }
                        }
                        list.add(listMember);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }  
            return list;
        } else
            return null;
    }
    
    public Object getVoObj() {
        return voObj;
    }

    public void setVoObj(Object voObj) {
        this.voObj = voObj;
    }
    
    public String getClassName() {
        return className;
    }

}
