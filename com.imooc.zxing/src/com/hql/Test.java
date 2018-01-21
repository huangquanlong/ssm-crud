package com.hql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

class TestExtend extends Object {
	@Override
	public String toString() {
		System.out.println("toString");
		return super.toString();
	}

}

public class Test {
	@SuppressWarnings({ "rawtypes", "unchecked", "unchecked" })
	public static void main(String[] args) {
/*		List list=new ArrayList();
		ArrayList list1=new ArrayList(20);
		System.out.println(list1);
		Map<String, Integer> map=new HashMap<String ,Integer>();
		ArrayList list2=new ArrayList();
		for(int i=0;i<23;i++){
			map.put("key"+i, i);
			list.add(i);
			list2.add(i);
		}*/
//		generateWord.randowNum();
/*		ArrayList<FltCntrfl> oldLists=new ArrayList<FltCntrfl>();
		oldLists.add(new FltCntrfl("张三", "CEO"));
		oldLists.add(new FltCntrfl("李四", "Manager"));
		oldLists.add(new FltCntrfl("王五", "employee"));
		@SuppressWarnings("unchecked")
		ArrayList<FltCntrfl> newLists=(ArrayList<FltCntrfl>) oldLists.clone();
		for (int i = 0; i < oldLists.size(); i++) {
			System.out.println(oldLists.get(i).getName());
		}
		for(FltCntrfl oldList:oldLists){
			System.out.println("旧对象集合"+oldList.getName());
		}
		System.out.println("******************************");
		for(FltCntrfl newList:newLists){
			System.out.println("新对象集合"+newList.getName());
		}
		System.out.println(oldLists.size());
		System.out.println("******************************");
		oldLists.remove(0);
		System.out.println(oldLists.size());
		for(FltCntrfl oldList:oldLists){
			System.out.println("修改后旧对象集合"+oldList.getName());
		}
		System.out.println("******************************");
		for(FltCntrfl newList:newLists){
			System.out.println("新对象集合"+newList.getName());		
		}
		*/
		
/*		int [] array={10,-5,15,200,76,-100,1000,600};
		int temp=0;*/
/*		for(int i=0;i<array.length-1;i++){
			for(int j=i+1;j<array.length;j++){
				if(array[j]<array[i]){
					temp=array[i];
					array[i]=array[j];
					array[j]=temp;
				}
			}
			System.out.println(Arrays.toString(array));
		}*/
/*		for (int i = 0; i < array.length-1; i++) {
			for (int j = 0; j < array.length-1-i; j++) {
				if(array[j]<array[j+1]){
					temp=array[j+1];
					array[j+1]=array[j];
					array[j]=temp;
				}
			}
			System.out.println(Arrays.toString(array));
		}*/
//		BigDecimal bg= BigDecimal.ZERO;
//		BigDecimal bg1= BigDecimal.ZERO;
//		Arrays.sort(array);
//		System.out.println(Arrays.toString(array));
	}
}

class FltCntrfl implements Cloneable{
	private String name;
	private String desigtion;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesigtion() {
		return desigtion;
	}
	public void setDesigtion(String desigtion) {
		this.desigtion = desigtion;
	}
	public FltCntrfl(String name, String desigtion) {
		super();
		this.name = name;
		this.desigtion = desigtion;
	}
}

class generateWord{
	static String randowNum(){
		String [] beforeShuffle=new String[]{"2", "3", "4", "5", "6", "7",    
                "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",    
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",    
                "W", "X", "Y", "Z" 
		};
		List list=Arrays.asList(beforeShuffle);
		Collections.shuffle(list);
		StringBuffer sbBuffer=new StringBuffer();
		for(int i=0;i<list.size();i++){
			sbBuffer.append(list.get(i));
		}
		String afterShuffle=sbBuffer.toString();
		System.out.println(afterShuffle.substring(4,8));
		return null;
	}
}
