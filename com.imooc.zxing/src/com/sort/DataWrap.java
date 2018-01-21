package com.sort;

public class DataWrap implements Comparable<DataWrap> {
	int data;
	String flag;
	public  DataWrap(int data,String flag) {
		this.data=data;
		this.flag=flag;
	}
	
	@Override
	public String toString() {
		return  data +  flag ;
	}

	@Override
	public int compareTo(DataWrap d) {
		return this.data > d.data ? 1:(this.data==d.data ? 0 : -1 );
	}

}
