package org.hibernate.engine.jdbc.internal;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.ResultSetReturn;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.resource.jdbc.ResourceRegistry;
import org.hibernate.resource.jdbc.spi.JdbcObserver;
import org.hibernate.resource.jdbc.spi.JdbcSessionContext;
import org.hibernate.resource.jdbc.spi.JdbcSessionOwner;
import org.hibernate.service.ServiceRegistry;

import com.alibaba.druid.proxy.jdbc.WrapperProxy;

public class ResultSetReturnImpl
  implements ResultSetReturn
{
  private final JdbcCoordinator jdbcCoordinator;
  private final Dialect dialect;
  private final SqlStatementLogger sqlStatementLogger;
  private final SqlExceptionHelper sqlExceptionHelper;
  private boolean isJdbc4 = true;

  public ResultSetReturnImpl(JdbcCoordinator jdbcCoordinator)
  {
    this.jdbcCoordinator = jdbcCoordinator;

    JdbcServices jdbcServices = (JdbcServices)jdbcCoordinator.getJdbcSessionOwner()
      .getJdbcSessionContext()
      .getServiceRegistry()
      .getService(JdbcServices.class);

    this.dialect = jdbcServices.getDialect();

    this.sqlStatementLogger = jdbcServices.getSqlStatementLogger();
    this.sqlExceptionHelper = jdbcServices.getSqlExceptionHelper();
  }

  public ResultSet extract(PreparedStatement statement)
  {
    if (isTypeOf(statement, CallableStatement.class))
    {
      //CallableStatement callableStatement = (CallableStatement)statement;
    //modifed by xiaolong
    /*	System.out.println(statement.toString());
    	try {
			System.out.println(statement.isWrapperFor(CallableStatement.class));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	WrapperProxy temp = (WrapperProxy) statement;
    	com.alibaba.druid.pool.DruidPooledPreparedStatement temp2 = null;
    	java.sql.Wrapper wr =null;
      CallableStatement callableStatement = (CallableStatement) temp.getRawObject();*/
      
      //return extract(callableStatement);
    }
    try {
      ResultSet rs;
      try {
        jdbcExecuteStatementStart();
        rs = statement.executeQuery();

        jdbcExecuteStatementEnd(); } finally { jdbcExecuteStatementEnd(); }


      return rs;
    }
    catch (SQLException e) {
      throw this.sqlExceptionHelper.convert(e, "could not extract ResultSet");
    }
  }

  private void jdbcExecuteStatementEnd() {
    this.jdbcCoordinator.getJdbcSessionOwner().getJdbcSessionContext().getObserver().jdbcExecuteStatementEnd();
  }

  private void jdbcExecuteStatementStart() {
    this.jdbcCoordinator.getJdbcSessionOwner().getJdbcSessionContext().getObserver().jdbcExecuteStatementStart();
  }

  private boolean isTypeOf(Statement statement, Class<? extends Statement> type) {
    if (this.isJdbc4) {
      try
      {
        return statement.isWrapperFor(type);
      }
      catch (SQLException localSQLException)
      {
      }
      catch (Throwable e)
      {
        this.isJdbc4 = false;
      }
    }
    return type.isInstance(statement);
  }

  public ResultSet extract(CallableStatement callableStatement)
  {
    try
    {
      ResultSet rs;
      try {
        jdbcExecuteStatementStart();
        rs = this.dialect.getResultSet(callableStatement);

        jdbcExecuteStatementEnd(); } finally { jdbcExecuteStatementEnd(); }


      return rs;
    }
    catch (SQLException e) {
      throw this.sqlExceptionHelper.convert(e, "could not extract ResultSet");
    }
  }

  public ResultSet extract(Statement statement, String sql)
  {
    this.sqlStatementLogger.logStatement(sql);
    try {
      ResultSet rs;
      try {
        jdbcExecuteStatementStart();
        rs = statement.executeQuery(sql);
      }
      finally {
        jdbcExecuteStatementEnd();
      }
      postExtract(rs, statement);
      return rs;
    }
    catch (SQLException e) {
      throw this.sqlExceptionHelper.convert(e, "could not extract ResultSet");
    }
  }

  public ResultSet execute(PreparedStatement statement)
  {
    try
    {
      ResultSet rs;
      try {
        jdbcExecuteStatementStart();
        while ((!statement.execute()) && 
          (!statement.getMoreResults()) && (statement.getUpdateCount() != -1));
        rs = statement.getResultSet();

        jdbcExecuteStatementEnd(); } finally { jdbcExecuteStatementEnd(); }


      return rs;
    }
    catch (SQLException e) {
      throw this.sqlExceptionHelper.convert(e, "could not execute statement");
    }
  }

  public ResultSet execute(Statement statement, String sql)
  {
    this.sqlStatementLogger.logStatement(sql);
    try {
      ResultSet rs;
      try {
        jdbcExecuteStatementStart();
        while ((!statement.execute(sql)) && 
          (!statement.getMoreResults()) && (statement.getUpdateCount() != -1));
        rs = statement.getResultSet();
      }
      finally {
        jdbcExecuteStatementEnd();
      }
      postExtract(rs, statement);
      return rs;
    }
    catch (SQLException e) {
      throw this.sqlExceptionHelper.convert(e, "could not execute statement");
    }
  }

  public int executeUpdate(PreparedStatement statement)
  {
    try {
      jdbcExecuteStatementStart();
      return statement.executeUpdate();
    }
    catch (SQLException e) {
      throw this.sqlExceptionHelper.convert(e, "could not execute statement");
    }
    finally {
      jdbcExecuteStatementEnd();
    }
  }

  public int executeUpdate(Statement statement, String sql)
  {
    this.sqlStatementLogger.logStatement(sql);
    try {
      jdbcExecuteStatementStart();
      return statement.executeUpdate(sql);
    }
    catch (SQLException e) {
      throw this.sqlExceptionHelper.convert(e, "could not execute statement");
    }
    finally {
      jdbcExecuteStatementEnd();
    }
  }

  private void postExtract(ResultSet rs, Statement st) {
    if (rs != null)
      this.jdbcCoordinator.getResourceRegistry().register(rs, st);
  }
}