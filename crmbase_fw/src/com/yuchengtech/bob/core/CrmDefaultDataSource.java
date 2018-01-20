package com.yuchengtech.bob.core;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class CrmDefaultDataSource implements DataSource{
	
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.dataSource.getConnection();
	}

	@Override
	public Connection getConnection(String arg0, String arg1)
			throws SQLException {
		return this.dataSource.getConnection(arg0, arg1);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.dataSource.getLogWriter();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return this.dataSource.getLoginTimeout();
	}

	@Override
	public void setLogWriter(PrintWriter arg0) throws SQLException {
		this.dataSource.setLogWriter(arg0);
		
	}

	@Override
	public void setLoginTimeout(int arg0) throws SQLException {
		this.dataSource.setLoginTimeout(arg0);
		
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return this.isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return this.unwrap(arg0);
	}
}
