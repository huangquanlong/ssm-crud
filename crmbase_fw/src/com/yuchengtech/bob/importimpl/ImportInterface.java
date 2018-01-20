package com.yuchengtech.bob.importimpl;

import java.sql.Connection;

import com.yuchengtech.bob.vo.AuthUser;

public interface ImportInterface {
    
	public static String BACK_IMPORT_ERROR = "BACK_IMPORT_ERROR";
    /**
     * @describe The main method to do the imported data trans.
     * @return
     */
    public void excute(Connection conn,String PKhead,AuthUser aUser) throws Exception;
}
