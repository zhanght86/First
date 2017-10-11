package org.activiti.engine.impl.cfg;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.engine.delegate.event.ActivitiEventDispatcher;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventDispatcherImpl;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.impl.DynamicBpmnServiceImpl;
import org.activiti.engine.impl.FormServiceImpl;
import org.activiti.engine.impl.HistoryServiceImpl;
import org.activiti.engine.impl.IdentityServiceImpl;
import org.activiti.engine.impl.ManagementServiceImpl;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.asyncexecutor.AsyncExecutor;
import org.activiti.engine.impl.asyncexecutor.DefaultAsyncJobExecutor;
import org.activiti.engine.impl.bpmn.data.ItemInstance;
import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
import org.activiti.engine.impl.bpmn.parser.BpmnParseHandlers;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.bpmn.parser.factory.AbstractBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.ActivityBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultListenerFactory;
import org.activiti.engine.impl.bpmn.parser.factory.ListenerFactory;
import org.activiti.engine.impl.bpmn.parser.handler.BoundaryEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.BusinessRuleParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CallActivityParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CancelEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CompensateEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EndEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ErrorEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EventBasedGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EventSubProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ExclusiveGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.InclusiveGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.IntermediateCatchEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.IntermediateThrowEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ManualTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.MessageEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ParallelGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ReceiveTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ScriptTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SendTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SequenceFlowParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SignalEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.StartEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SubProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TimerEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TransactionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.bpmn.webservice.MessageInstance;
import org.activiti.engine.impl.calendar.BusinessCalendarManager;
import org.activiti.engine.impl.calendar.CycleBusinessCalendar;
import org.activiti.engine.impl.calendar.DueDateBusinessCalendar;
import org.activiti.engine.impl.calendar.DurationBusinessCalendar;
import org.activiti.engine.impl.calendar.MapBusinessCalendarManager;
import org.activiti.engine.impl.cfg.standalone.StandaloneMybatisTransactionContextFactory;
import org.activiti.engine.impl.db.DbIdGenerator;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.db.IbatisVariableTypeHandler;
import org.activiti.engine.impl.delegate.DefaultDelegateInterceptor;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.event.CompensationEventHandler;
import org.activiti.engine.impl.event.EventHandler;
import org.activiti.engine.impl.event.MessageEventHandler;
import org.activiti.engine.impl.event.SignalEventHandler;
import org.activiti.engine.impl.event.logger.EventLogger;
import org.activiti.engine.impl.form.BooleanFormType;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.DoubleFormType;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.form.FormTypes;
import org.activiti.engine.impl.form.JuelFormEngine;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.history.parse.FlowNodeHistoryParseHandler;
import org.activiti.engine.impl.history.parse.ProcessHistoryParseHandler;
import org.activiti.engine.impl.history.parse.StartEventHistoryParseHandler;
import org.activiti.engine.impl.history.parse.UserTaskHistoryParseHandler;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.activiti.engine.impl.interceptor.CommandContextFactory;
import org.activiti.engine.impl.interceptor.CommandContextInterceptor;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.interceptor.CommandInterceptor;
import org.activiti.engine.impl.interceptor.CommandInvoker;
import org.activiti.engine.impl.interceptor.DelegateInterceptor;
import org.activiti.engine.impl.interceptor.LogInterceptor;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.jobexecutor.AsyncContinuationJobHandler;
import org.activiti.engine.impl.jobexecutor.CallerRunsRejectedJobsHandler;
import org.activiti.engine.impl.jobexecutor.DefaultFailedJobCommandFactory;
import org.activiti.engine.impl.jobexecutor.DefaultJobExecutor;
import org.activiti.engine.impl.jobexecutor.FailedJobCommandFactory;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.jobexecutor.JobHandler;
import org.activiti.engine.impl.jobexecutor.ProcessEventJobHandler;
import org.activiti.engine.impl.jobexecutor.RejectedJobsHandler;
import org.activiti.engine.impl.jobexecutor.TimerActivateProcessDefinitionHandler;
import org.activiti.engine.impl.jobexecutor.TimerCatchIntermediateEventJobHandler;
import org.activiti.engine.impl.jobexecutor.TimerExecuteNestedActivityJobHandler;
import org.activiti.engine.impl.jobexecutor.TimerStartEventJobHandler;
import org.activiti.engine.impl.jobexecutor.TimerSuspendProcessDefinitionHandler;
import org.activiti.engine.impl.persistence.DefaultHistoryManagerSessionFactory;
import org.activiti.engine.impl.persistence.GenericManagerFactory;
import org.activiti.engine.impl.persistence.GroupEntityManagerFactory;
import org.activiti.engine.impl.persistence.MembershipEntityManagerFactory;
import org.activiti.engine.impl.persistence.UserEntityManagerFactory;
import org.activiti.engine.impl.persistence.deploy.DefaultDeploymentCache;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionInfoCache;
import org.activiti.engine.impl.persistence.entity.AttachmentEntityManager;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityManager;
import org.activiti.engine.impl.persistence.entity.CommentEntityManager;
import org.activiti.engine.impl.persistence.entity.DeploymentEntityManager;
import org.activiti.engine.impl.persistence.entity.EventLogEntryEntityManager;
import org.activiti.engine.impl.persistence.entity.EventSubscriptionEntityManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricDetailEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntityManager;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntityManager;
import org.activiti.engine.impl.persistence.entity.JobEntityManager;
import org.activiti.engine.impl.persistence.entity.ModelEntityManager;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityManager;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionInfoEntityManager;
import org.activiti.engine.impl.persistence.entity.PropertyEntityManager;
import org.activiti.engine.impl.persistence.entity.ResourceEntityManager;
import org.activiti.engine.impl.persistence.entity.TableDataManager;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntityManager;
import org.activiti.engine.impl.scripting.BeansResolverFactory;
import org.activiti.engine.impl.scripting.ResolverFactory;
import org.activiti.engine.impl.scripting.ScriptBindingsFactory;
import org.activiti.engine.impl.scripting.ScriptingEngines;
import org.activiti.engine.impl.scripting.VariableScopeResolverFactory;
import org.activiti.engine.impl.util.DefaultClockImpl;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.impl.variable.BooleanType;
import org.activiti.engine.impl.variable.ByteArrayType;
import org.activiti.engine.impl.variable.CustomObjectType;
import org.activiti.engine.impl.variable.DateType;
import org.activiti.engine.impl.variable.DefaultVariableTypes;
import org.activiti.engine.impl.variable.DoubleType;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.activiti.engine.impl.variable.EntityManagerSessionFactory;
import org.activiti.engine.impl.variable.IntegerType;
import org.activiti.engine.impl.variable.JPAEntityListVariableType;
import org.activiti.engine.impl.variable.JPAEntityVariableType;
import org.activiti.engine.impl.variable.JsonType;
import org.activiti.engine.impl.variable.LongJsonType;
import org.activiti.engine.impl.variable.LongStringType;
import org.activiti.engine.impl.variable.LongType;
import org.activiti.engine.impl.variable.NullType;
import org.activiti.engine.impl.variable.SerializableType;
import org.activiti.engine.impl.variable.ShortType;
import org.activiti.engine.impl.variable.StringType;
import org.activiti.engine.impl.variable.UUIDType;
import org.activiti.engine.impl.variable.VariableType;
import org.activiti.engine.impl.variable.VariableTypes;
import org.activiti.engine.parse.BpmnParseHandler;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProcessEngineConfigurationImpl extends ProcessEngineConfiguration
{
  private static Logger log = LoggerFactory.getLogger(ProcessEngineConfigurationImpl.class);
  public static final int DEFAULT_GENERIC_MAX_LENGTH_STRING = 4000;
  public static final int DEFAULT_ORACLE_MAX_LENGTH_STRING = 2000;
  public static final String DB_SCHEMA_UPDATE_CREATE = "create";
  public static final String DB_SCHEMA_UPDATE_DROP_CREATE = "drop-create";
  public static final String DEFAULT_WS_SYNC_FACTORY = "org.activiti.engine.impl.webservice.CxfWebServiceClientFactory";
  public static final String DEFAULT_MYBATIS_MAPPING_FILE = "org/activiti/db/mapping/mappings.xml";
  protected RepositoryService repositoryService = new RepositoryServiceImpl();
  protected RuntimeService runtimeService = new RuntimeServiceImpl();
  protected HistoryService historyService = new HistoryServiceImpl(this);
  protected IdentityService identityService = new IdentityServiceImpl();
  protected TaskService taskService = new TaskServiceImpl(this);
  protected FormService formService = new FormServiceImpl();
  protected ManagementService managementService = new ManagementServiceImpl();
  protected DynamicBpmnService dynamicBpmnService = new DynamicBpmnServiceImpl(this);
  protected CommandConfig defaultCommandConfig;
  protected CommandConfig schemaCommandConfig;
  protected CommandInterceptor commandInvoker;
  protected List<CommandInterceptor> customPreCommandInterceptors;
  protected List<CommandInterceptor> customPostCommandInterceptors;
  protected List<CommandInterceptor> commandInterceptors;
  protected CommandExecutor commandExecutor;
  protected List<SessionFactory> customSessionFactories;
  protected DbSqlSessionFactory dbSqlSessionFactory;
  protected Map<Class<?>, SessionFactory> sessionFactories;
  protected boolean enableConfiguratorServiceLoader = true;
  protected List<ProcessEngineConfigurator> configurators;
  protected List<ProcessEngineConfigurator> allConfigurators;
  protected BpmnDeployer bpmnDeployer;
  protected BpmnParser bpmnParser;
  protected List<Deployer> customPreDeployers;
  protected List<Deployer> customPostDeployers;
  protected List<Deployer> deployers;
  protected DeploymentManager deploymentManager;
  protected int processDefinitionCacheLimit = -1;
  protected DeploymentCache<ProcessDefinitionEntity> processDefinitionCache;
  protected int bpmnModelCacheLimit = -1;
  protected DeploymentCache<BpmnModel> bpmnModelCache;
  protected int processDefinitionInfoCacheLimit = -1;
  protected ProcessDefinitionInfoCache processDefinitionInfoCache;
  protected int knowledgeBaseCacheLimit = -1;
  protected DeploymentCache<Object> knowledgeBaseCache;
  protected List<JobHandler> customJobHandlers;
  protected Map<String, JobHandler> jobHandlers;
  protected SqlSessionFactory sqlSessionFactory;
  protected TransactionFactory transactionFactory;
  protected Set<Class<?>> customMybatisMappers;
  protected Set<String> customMybatisXMLMappers;
  protected IdGenerator idGenerator;
  protected DataSource idGeneratorDataSource;
  protected String idGeneratorDataSourceJndiName;
  protected List<BpmnParseHandler> preBpmnParseHandlers;
  protected List<BpmnParseHandler> postBpmnParseHandlers;
  protected List<BpmnParseHandler> customDefaultBpmnParseHandlers;
  protected ActivityBehaviorFactory activityBehaviorFactory;
  protected ListenerFactory listenerFactory;
  protected BpmnParseFactory bpmnParseFactory;
  protected ProcessValidator processValidator;
  protected List<FormEngine> customFormEngines;
  protected Map<String, FormEngine> formEngines;
  protected List<AbstractFormType> customFormTypes;
  protected FormTypes formTypes;
  protected List<VariableType> customPreVariableTypes;
  protected List<VariableType> customPostVariableTypes;
  protected VariableTypes variableTypes;
  protected ExpressionManager expressionManager;
  protected List<String> customScriptingEngineClasses;
  protected ScriptingEngines scriptingEngines;
  protected List<ResolverFactory> resolverFactories;
  protected BusinessCalendarManager businessCalendarManager;
  protected String wsSyncFactoryClassName = "org.activiti.engine.impl.webservice.CxfWebServiceClientFactory";
  protected CommandContextFactory commandContextFactory;
  protected TransactionContextFactory transactionContextFactory;
  protected Map<Object, Object> beans;
  protected DelegateInterceptor delegateInterceptor;
  protected RejectedJobsHandler customRejectedJobsHandler;
  protected Map<String, EventHandler> eventHandlers;
  protected List<EventHandler> customEventHandlers;
  protected FailedJobCommandFactory failedJobCommandFactory;
  protected boolean enableSafeBpmnXml = false;

  protected int batchSizeProcessInstances = 25;
  protected int batchSizeTasks = 25;

  protected boolean isBulkInsertEnabled = true;

  protected int maxNrOfStatementsInBulkInsert = 100;

  protected boolean enableEventDispatcher = true;
  protected ActivitiEventDispatcher eventDispatcher;
  protected List<ActivitiEventListener> eventListeners;
  protected Map<String, List<ActivitiEventListener>> typedEventListeners;
  protected boolean enableDatabaseEventLogging = false;

  protected int maxLengthStringVariableType = -1;

  protected ObjectMapper objectMapper = new ObjectMapper();

  protected static Properties databaseTypeMappings = getDefaultDatabaseTypeMappings();
  public static final String DATABASE_TYPE_H2 = "h2";
  public static final String DATABASE_TYPE_HSQL = "hsql";
  public static final String DATABASE_TYPE_MYSQL = "mysql";
  public static final String DATABASE_TYPE_ORACLE = "oracle";
  public static final String DATABASE_TYPE_POSTGRES = "postgres";
  public static final String DATABASE_TYPE_MSSQL = "mssql";
  public static final String DATABASE_TYPE_DB2 = "db2";

  public ProcessEngine buildProcessEngine()
  {
    init();
    return new ProcessEngineImpl(this);
  }

  protected void init()
  {
    initConfigurators();
    configuratorsBeforeInit();
    initProcessDiagramGenerator();
    initHistoryLevel();
    initExpressionManager();
    initDataSource();
    initVariableTypes();
    initBeans();
    initFormEngines();
    initFormTypes();
    initScriptingEngines();
    initClock();
    initBusinessCalendarManager();
    initCommandContextFactory();
    initTransactionContextFactory();
    initCommandExecutors();
    initServices();
    initIdGenerator();
    initDeployers();
    initJobHandlers();
    initJobExecutor();
    initAsyncExecutor();
    initTransactionFactory();
    initSqlSessionFactory();
    initSessionFactories();
    initJpa();
    initDelegateInterceptor();
    initEventHandlers();
    initFailedJobCommandFactory();
    initEventDispatcher();
    initProcessValidator();
    initDatabaseEventLogging();
    configuratorsAfterInit();
  }

  protected void initFailedJobCommandFactory()
  {
    if (this.failedJobCommandFactory == null)
      this.failedJobCommandFactory = new DefaultFailedJobCommandFactory();
  }

  protected void initCommandExecutors()
  {
    initDefaultCommandConfig();
    initSchemaCommandConfig();
    initCommandInvoker();
    initCommandInterceptors();
    initCommandExecutor();
  }

  protected void initDefaultCommandConfig() {
    if (this.defaultCommandConfig == null)
      this.defaultCommandConfig = new CommandConfig();
  }

  private void initSchemaCommandConfig()
  {
    if (this.schemaCommandConfig == null)
      this.schemaCommandConfig = new CommandConfig().transactionNotSupported();
  }

  protected void initCommandInvoker()
  {
    if (this.commandInvoker == null)
      this.commandInvoker = new CommandInvoker();
  }

  protected void initCommandInterceptors()
  {
    if (this.commandInterceptors == null) {
      this.commandInterceptors = new ArrayList();
      if (this.customPreCommandInterceptors != null) {
        this.commandInterceptors.addAll(this.customPreCommandInterceptors);
      }
      this.commandInterceptors.addAll(getDefaultCommandInterceptors());
      if (this.customPostCommandInterceptors != null) {
        this.commandInterceptors.addAll(this.customPostCommandInterceptors);
      }
      this.commandInterceptors.add(this.commandInvoker);
    }
  }

  protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptors() {
    List interceptors = new ArrayList();
    interceptors.add(new LogInterceptor());

    CommandInterceptor transactionInterceptor = createTransactionInterceptor();
    if (transactionInterceptor != null) {
      interceptors.add(transactionInterceptor);
    }

    interceptors.add(new CommandContextInterceptor(this.commandContextFactory, this));
    return interceptors;
  }

  protected void initCommandExecutor() {
    if (this.commandExecutor == null) {
      CommandInterceptor first = initInterceptorChain(this.commandInterceptors);
      this.commandExecutor = new CommandExecutorImpl(getDefaultCommandConfig(), first);
    }
  }

  protected CommandInterceptor initInterceptorChain(List<CommandInterceptor> chain) {
    if ((chain == null) || (chain.isEmpty())) {
      throw new ActivitiException(new StringBuilder().append("invalid command interceptor chain configuration: ").append(chain).toString());
    }
    for (int i = 0; i < chain.size() - 1; i++) {
      ((CommandInterceptor)chain.get(i)).setNext((CommandInterceptor)chain.get(i + 1));
    }
    return (CommandInterceptor)chain.get(0);
  }

  protected abstract CommandInterceptor createTransactionInterceptor();

  protected void initServices()
  {
    initService(this.repositoryService);
    initService(this.runtimeService);
    initService(this.historyService);
    initService(this.identityService);
    initService(this.taskService);
    initService(this.formService);
    initService(this.managementService);
    initService(this.dynamicBpmnService);
  }

  protected void initService(Object service) {
    if ((service instanceof ServiceImpl))
      ((ServiceImpl)service).setCommandExecutor(this.commandExecutor);
  }

  protected void initDataSource()
  {
    if (this.dataSource == null) {
      if (this.dataSourceJndiName != null) {
        try {
          this.dataSource = ((DataSource)new InitialContext().lookup(this.dataSourceJndiName));
        } catch (Exception e) {
          throw new ActivitiException(new StringBuilder().append("couldn't lookup datasource from ").append(this.dataSourceJndiName).append(": ").append(e.getMessage()).toString(), e);
        }
      }
      else if (this.jdbcUrl != null) {
        if ((this.jdbcDriver == null) || (this.jdbcUrl == null) || (this.jdbcUsername == null)) {
          throw new ActivitiException("DataSource or JDBC properties have to be specified in a process engine configuration");
        }

        log.debug("initializing datasource to db: {}", this.jdbcUrl);

        PooledDataSource pooledDataSource = new PooledDataSource(ReflectUtil.getClassLoader(), this.jdbcDriver, this.jdbcUrl, this.jdbcUsername, this.jdbcPassword);

        if (this.jdbcMaxActiveConnections > 0) {
          pooledDataSource.setPoolMaximumActiveConnections(this.jdbcMaxActiveConnections);
        }
        if (this.jdbcMaxIdleConnections > 0) {
          pooledDataSource.setPoolMaximumIdleConnections(this.jdbcMaxIdleConnections);
        }
        if (this.jdbcMaxCheckoutTime > 0) {
          pooledDataSource.setPoolMaximumCheckoutTime(this.jdbcMaxCheckoutTime);
        }
        if (this.jdbcMaxWaitTime > 0) {
          pooledDataSource.setPoolTimeToWait(this.jdbcMaxWaitTime);
        }
        if (this.jdbcPingEnabled == true) {
          pooledDataSource.setPoolPingEnabled(true);
          if (this.jdbcPingQuery != null) {
            pooledDataSource.setPoolPingQuery(this.jdbcPingQuery);
          }
          pooledDataSource.setPoolPingConnectionsNotUsedFor(this.jdbcPingConnectionNotUsedFor);
        }
        if (this.jdbcDefaultTransactionIsolationLevel > 0) {
          pooledDataSource.setDefaultTransactionIsolationLevel(Integer.valueOf(this.jdbcDefaultTransactionIsolationLevel));
        }
        this.dataSource = pooledDataSource;
      }

      if ((this.dataSource instanceof PooledDataSource))
      {
        ((PooledDataSource)this.dataSource).forceCloseAll();
      }
    }

    if (this.databaseType == null)
      initDatabaseType();
  }

  protected static Properties getDefaultDatabaseTypeMappings()
  {
    Properties databaseTypeMappings = new Properties();
    databaseTypeMappings.setProperty("H2", "h2");
    databaseTypeMappings.setProperty("HSQL Database Engine", "hsql");
    databaseTypeMappings.setProperty("MySQL", "mysql");
    databaseTypeMappings.setProperty("Oracle", "oracle");
    databaseTypeMappings.setProperty("PostgreSQL", "postgres");
    databaseTypeMappings.setProperty("Microsoft SQL Server", "mssql");
    databaseTypeMappings.setProperty("db2", "db2");
    databaseTypeMappings.setProperty("DB2", "db2");
    databaseTypeMappings.setProperty("DB2/NT", "db2");
    databaseTypeMappings.setProperty("DB2/NT64", "db2");
    databaseTypeMappings.setProperty("DB2 UDP", "db2");
    databaseTypeMappings.setProperty("DB2/LINUX", "db2");
    databaseTypeMappings.setProperty("DB2/LINUX390", "db2");
    databaseTypeMappings.setProperty("DB2/LINUXX8664", "db2");
    databaseTypeMappings.setProperty("DB2/LINUXZ64", "db2");
    databaseTypeMappings.setProperty("DB2/LINUXPPC64", "db2");
    databaseTypeMappings.setProperty("DB2/400 SQL", "db2");
    databaseTypeMappings.setProperty("DB2/6000", "db2");
    databaseTypeMappings.setProperty("DB2 UDB iSeries", "db2");
    databaseTypeMappings.setProperty("DB2/AIX64", "db2");
    databaseTypeMappings.setProperty("DB2/HPUX", "db2");
    databaseTypeMappings.setProperty("DB2/HP64", "db2");
    databaseTypeMappings.setProperty("DB2/SUN", "db2");
    databaseTypeMappings.setProperty("DB2/SUN64", "db2");
    databaseTypeMappings.setProperty("DB2/PTX", "db2");
    databaseTypeMappings.setProperty("DB2/2", "db2");
    databaseTypeMappings.setProperty("DB2 UDB AS400", "db2");
    
    databaseTypeMappings.setProperty("HDB", "mysql");//add 20171010
    return databaseTypeMappings;
  }

  public void initDatabaseType() {
    Connection connection = null;
    try {
      connection = this.dataSource.getConnection();
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      String databaseProductName = databaseMetaData.getDatabaseProductName();
      log.debug("database product name: '{}'", databaseProductName);
      this.databaseType = databaseTypeMappings.getProperty(databaseProductName);
      if (this.databaseType == null) {
        throw new ActivitiException(new StringBuilder().append("couldn't deduct database type from database product name '").append(databaseProductName).append("'").toString());
      }
      log.debug("using database type: {}", this.databaseType);
    }
    catch (SQLException e) {
      log.error("Exception while initializing Database connection", e);
    } finally {
      try {
        if (connection != null)
          connection.close();
      }
      catch (SQLException e) {
        log.error("Exception while closing the Database connection", e);
      }
    }
  }

  protected void initTransactionFactory()
  {
    if (this.transactionFactory == null)
      if (this.transactionsExternallyManaged)
        this.transactionFactory = new ManagedTransactionFactory();
      else
        this.transactionFactory = new JdbcTransactionFactory();
  }

  protected void initSqlSessionFactory()
  {
    if (this.sqlSessionFactory == null) {
      InputStream inputStream = null;
      try {
        inputStream = getMyBatisXmlConfigurationSteam();

        Environment environment = new Environment("default", this.transactionFactory, this.dataSource);
        Reader reader = new InputStreamReader(inputStream);
        Properties properties = new Properties();
        properties.put("prefix", this.databaseTablePrefix);
        if (this.databaseType != null) {
          properties.put("limitBefore", DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.get(this.databaseType));
          properties.put("limitAfter", DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get(this.databaseType));
          properties.put("limitBetween", DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.get(this.databaseType));
          properties.put("limitOuterJoinBetween", DbSqlSessionFactory.databaseOuterJoinLimitBetweenStatements.get(this.databaseType));
          properties.put("orderBy", DbSqlSessionFactory.databaseSpecificOrderByStatements.get(this.databaseType));
          properties.put("limitBeforeNativeQuery", ObjectUtils.toString(DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.get(this.databaseType)));
        }

        Configuration configuration = initMybatisConfiguration(environment, reader, properties);
        this.sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
      }
      catch (Exception e) {
        throw new ActivitiException(new StringBuilder().append("Error while building ibatis SqlSessionFactory: ").append(e.getMessage()).toString(), e);
      } finally {
        IoUtil.closeSilently(inputStream);
      }
    }
  }

  protected Configuration initMybatisConfiguration(Environment environment, Reader reader, Properties properties) {
    XMLConfigBuilder parser = new XMLConfigBuilder(reader, "", properties);
    Configuration configuration = parser.getConfiguration();
    configuration.setEnvironment(environment);

    initMybatisTypeHandlers(configuration);
    initCustomMybatisMappers(configuration);

    configuration = parseMybatisConfiguration(configuration, parser);
    return configuration;
  }

  protected void initMybatisTypeHandlers(Configuration configuration) {
    configuration.getTypeHandlerRegistry().register(VariableType.class, JdbcType.VARCHAR, new IbatisVariableTypeHandler());
  }

  protected void initCustomMybatisMappers(Configuration configuration) {
    if (getCustomMybatisMappers() != null)
      for (Class clazz : getCustomMybatisMappers())
        configuration.addMapper(clazz);
  }

  protected Configuration parseMybatisConfiguration(Configuration configuration, XMLConfigBuilder parser)
  {
    return parseCustomMybatisXMLMappers(parser.parse());
  }

  protected Configuration parseCustomMybatisXMLMappers(Configuration configuration) {
    if (getCustomMybatisXMLMappers() != null)
    {
      for (String resource : getCustomMybatisXMLMappers()) {
        XMLMapperBuilder mapperParser = new XMLMapperBuilder(getResourceAsStream(resource), configuration, resource, configuration.getSqlFragments());

        mapperParser.parse();
      }
    }
    return configuration;
  }

  protected InputStream getResourceAsStream(String resource) {
    return ReflectUtil.getResourceAsStream(resource);
  }

  protected InputStream getMyBatisXmlConfigurationSteam() {
    return getResourceAsStream("org/activiti/db/mapping/mappings.xml");
  }

  public Set<Class<?>> getCustomMybatisMappers() {
    return this.customMybatisMappers;
  }

  public void setCustomMybatisMappers(Set<Class<?>> customMybatisMappers) {
    this.customMybatisMappers = customMybatisMappers;
  }

  public Set<String> getCustomMybatisXMLMappers() {
    return this.customMybatisXMLMappers;
  }

  public void setCustomMybatisXMLMappers(Set<String> customMybatisXMLMappers) {
    this.customMybatisXMLMappers = customMybatisXMLMappers;
  }

  protected void initSessionFactories()
  {
    if (this.sessionFactories == null) {
      this.sessionFactories = new HashMap();

      if (this.dbSqlSessionFactory == null) {
        this.dbSqlSessionFactory = new DbSqlSessionFactory();
      }
      this.dbSqlSessionFactory.setDatabaseType(this.databaseType);
      this.dbSqlSessionFactory.setIdGenerator(this.idGenerator);
      this.dbSqlSessionFactory.setSqlSessionFactory(this.sqlSessionFactory);
      this.dbSqlSessionFactory.setDbIdentityUsed(this.isDbIdentityUsed);
      this.dbSqlSessionFactory.setDbHistoryUsed(this.isDbHistoryUsed);
      this.dbSqlSessionFactory.setDatabaseTablePrefix(this.databaseTablePrefix);
      this.dbSqlSessionFactory.setTablePrefixIsSchema(this.tablePrefixIsSchema);
      this.dbSqlSessionFactory.setDatabaseCatalog(this.databaseCatalog);
      this.dbSqlSessionFactory.setDatabaseSchema(this.databaseSchema);
      this.dbSqlSessionFactory.setBulkInsertEnabled(this.isBulkInsertEnabled, this.databaseType);
      this.dbSqlSessionFactory.setMaxNrOfStatementsInBulkInsert(this.maxNrOfStatementsInBulkInsert);
      addSessionFactory(this.dbSqlSessionFactory);

      addSessionFactory(new GenericManagerFactory(AttachmentEntityManager.class));
      addSessionFactory(new GenericManagerFactory(CommentEntityManager.class));
      addSessionFactory(new GenericManagerFactory(DeploymentEntityManager.class));
      addSessionFactory(new GenericManagerFactory(ModelEntityManager.class));
      addSessionFactory(new GenericManagerFactory(ExecutionEntityManager.class));
      addSessionFactory(new GenericManagerFactory(HistoricActivityInstanceEntityManager.class));
      addSessionFactory(new GenericManagerFactory(HistoricDetailEntityManager.class));
      addSessionFactory(new GenericManagerFactory(HistoricProcessInstanceEntityManager.class));
      addSessionFactory(new GenericManagerFactory(HistoricVariableInstanceEntityManager.class));
      addSessionFactory(new GenericManagerFactory(HistoricTaskInstanceEntityManager.class));
      addSessionFactory(new GenericManagerFactory(HistoricIdentityLinkEntityManager.class));
      addSessionFactory(new GenericManagerFactory(IdentityInfoEntityManager.class));
      addSessionFactory(new GenericManagerFactory(IdentityLinkEntityManager.class));
      addSessionFactory(new GenericManagerFactory(JobEntityManager.class));
      addSessionFactory(new GenericManagerFactory(ProcessDefinitionEntityManager.class));
      addSessionFactory(new GenericManagerFactory(ProcessDefinitionInfoEntityManager.class));
      addSessionFactory(new GenericManagerFactory(PropertyEntityManager.class));
      addSessionFactory(new GenericManagerFactory(ResourceEntityManager.class));
      addSessionFactory(new GenericManagerFactory(ByteArrayEntityManager.class));
      addSessionFactory(new GenericManagerFactory(TableDataManager.class));
      addSessionFactory(new GenericManagerFactory(TaskEntityManager.class));
      addSessionFactory(new GenericManagerFactory(VariableInstanceEntityManager.class));
      addSessionFactory(new GenericManagerFactory(EventSubscriptionEntityManager.class));
      addSessionFactory(new GenericManagerFactory(EventLogEntryEntityManager.class));

      addSessionFactory(new DefaultHistoryManagerSessionFactory());

      addSessionFactory(new UserEntityManagerFactory());
      addSessionFactory(new GroupEntityManagerFactory());
      addSessionFactory(new MembershipEntityManagerFactory());
    }

    if (this.customSessionFactories != null)
      for (SessionFactory sessionFactory : this.customSessionFactories)
        addSessionFactory(sessionFactory);
  }

  protected void addSessionFactory(SessionFactory sessionFactory)
  {
    this.sessionFactories.put(sessionFactory.getSessionType(), sessionFactory);
  }

  protected void initConfigurators()
  {
    this.allConfigurators = new ArrayList();

    if (this.configurators != null) {
      for (ProcessEngineConfigurator configurator : this.configurators) {
        this.allConfigurators.add(configurator);
      }

    }

    if (this.enableConfiguratorServiceLoader) {
      ClassLoader classLoader = getClassLoader();
      if (classLoader == null) {
        classLoader = ReflectUtil.getClassLoader();
      }

      ServiceLoader<ProcessEngineConfigurator> configuratorServiceLoader = ServiceLoader.load(ProcessEngineConfigurator.class, classLoader);

      int nrOfServiceLoadedConfigurators = 0;
      for (ProcessEngineConfigurator configurator : configuratorServiceLoader) {
        this.allConfigurators.add(configurator);
        nrOfServiceLoadedConfigurators++;
      }

      if (nrOfServiceLoadedConfigurators > 0) {
        log.info("Found {} auto-discoverable Process Engine Configurator{}", Integer.valueOf(nrOfServiceLoadedConfigurators++), nrOfServiceLoadedConfigurators > 1 ? "s" : "");
      }

      if (!this.allConfigurators.isEmpty())
      {
        Collections.sort(this.allConfigurators, new Comparator<ProcessEngineConfigurator>()
        {
          public int compare(ProcessEngineConfigurator configurator1, ProcessEngineConfigurator configurator2) {
            int priority1 = configurator1.getPriority();
            int priority2 = configurator2.getPriority();

            if (priority1 < priority2)
              return -1;
            if (priority1 > priority2) {
              return 1;
            }
            return 0;
          }

		
        });
        log.info("Found {} Process Engine Configurators in total:", Integer.valueOf(this.allConfigurators.size()));
        for (ProcessEngineConfigurator configurator : this.allConfigurators)
          log.info("{} (priority:{})", configurator.getClass(), Integer.valueOf(configurator.getPriority()));
      }
    }
  }

  protected void configuratorsBeforeInit()
  {
    for (ProcessEngineConfigurator configurator : this.allConfigurators) {
      log.info("Executing beforeInit() of {} (priority:{})", configurator.getClass(), Integer.valueOf(configurator.getPriority()));
      configurator.beforeInit(this);
    }
  }

  protected void configuratorsAfterInit() {
    for (ProcessEngineConfigurator configurator : this.allConfigurators) {
      log.info("Executing configure() of {} (priority:{})", configurator.getClass(), Integer.valueOf(configurator.getPriority()));
      configurator.configure(this);
    }
  }

  protected void initDeployers()
  {
    if (this.deployers == null) {
      this.deployers = new ArrayList();
      if (this.customPreDeployers != null) {
        this.deployers.addAll(this.customPreDeployers);
      }
      this.deployers.addAll(getDefaultDeployers());
      if (this.customPostDeployers != null) {
        this.deployers.addAll(this.customPostDeployers);
      }
    }
    if (this.deploymentManager == null) {
      this.deploymentManager = new DeploymentManager();
      this.deploymentManager.setDeployers(this.deployers);

      if (this.processDefinitionCache == null) {
        if (this.processDefinitionCacheLimit <= 0)
          this.processDefinitionCache = new DefaultDeploymentCache();
        else {
          this.processDefinitionCache = new DefaultDeploymentCache(this.processDefinitionCacheLimit);
        }

      }

      if (this.bpmnModelCache == null) {
        if (this.bpmnModelCacheLimit <= 0)
          this.bpmnModelCache = new DefaultDeploymentCache();
        else {
          this.bpmnModelCache = new DefaultDeploymentCache(this.bpmnModelCacheLimit);
        }
      }

      if (this.processDefinitionInfoCache == null) {
        if (this.processDefinitionInfoCacheLimit <= 0)
          this.processDefinitionInfoCache = new ProcessDefinitionInfoCache(this.commandExecutor);
        else {
          this.processDefinitionInfoCache = new ProcessDefinitionInfoCache(this.commandExecutor, this.processDefinitionInfoCacheLimit);
        }

      }

      if (this.knowledgeBaseCache == null) {
        if (this.knowledgeBaseCacheLimit <= 0)
          this.knowledgeBaseCache = new DefaultDeploymentCache();
        else {
          this.knowledgeBaseCache = new DefaultDeploymentCache(this.knowledgeBaseCacheLimit);
        }
      }

      this.deploymentManager.setProcessDefinitionCache(this.processDefinitionCache);
      this.deploymentManager.setBpmnModelCache(this.bpmnModelCache);
      this.deploymentManager.setProcessDefinitionInfoCache(this.processDefinitionInfoCache);
      this.deploymentManager.setKnowledgeBaseCache(this.knowledgeBaseCache);
    }
  }

  protected Collection<? extends Deployer> getDefaultDeployers() {
    List defaultDeployers = new ArrayList();

    if (this.bpmnDeployer == null) {
      this.bpmnDeployer = new BpmnDeployer();
    }

    this.bpmnDeployer.setExpressionManager(this.expressionManager);
    this.bpmnDeployer.setIdGenerator(this.idGenerator);

    if (this.bpmnParseFactory == null) {
      this.bpmnParseFactory = new DefaultBpmnParseFactory();
    }

    if (this.activityBehaviorFactory == null) {
      DefaultActivityBehaviorFactory defaultActivityBehaviorFactory = new DefaultActivityBehaviorFactory();
      defaultActivityBehaviorFactory.setExpressionManager(this.expressionManager);
      this.activityBehaviorFactory = defaultActivityBehaviorFactory;
    } else if (((this.activityBehaviorFactory instanceof AbstractBehaviorFactory)) && (((AbstractBehaviorFactory)this.activityBehaviorFactory).getExpressionManager() == null))
    {
      ((AbstractBehaviorFactory)this.activityBehaviorFactory).setExpressionManager(this.expressionManager);
    }

    if (this.listenerFactory == null) {
      DefaultListenerFactory defaultListenerFactory = new DefaultListenerFactory();
      defaultListenerFactory.setExpressionManager(this.expressionManager);
      this.listenerFactory = defaultListenerFactory;
    } else if (((this.listenerFactory instanceof AbstractBehaviorFactory)) && (((AbstractBehaviorFactory)this.listenerFactory).getExpressionManager() == null))
    {
      ((AbstractBehaviorFactory)this.listenerFactory).setExpressionManager(this.expressionManager);
    }

    if (this.bpmnParser == null) {
      this.bpmnParser = new BpmnParser();
    }

    this.bpmnParser.setExpressionManager(this.expressionManager);
    this.bpmnParser.setBpmnParseFactory(this.bpmnParseFactory);
    this.bpmnParser.setActivityBehaviorFactory(this.activityBehaviorFactory);
    this.bpmnParser.setListenerFactory(this.listenerFactory);

    List parseHandlers = new ArrayList();
    if (getPreBpmnParseHandlers() != null) {
      parseHandlers.addAll(getPreBpmnParseHandlers());
    }
    parseHandlers.addAll(getDefaultBpmnParseHandlers());
    if (getPostBpmnParseHandlers() != null) {
      parseHandlers.addAll(getPostBpmnParseHandlers());
    }

    BpmnParseHandlers bpmnParseHandlers = new BpmnParseHandlers();
    bpmnParseHandlers.addHandlers(parseHandlers);
    this.bpmnParser.setBpmnParserHandlers(bpmnParseHandlers);

    this.bpmnDeployer.setBpmnParser(this.bpmnParser);

    defaultDeployers.add(this.bpmnDeployer);
    return defaultDeployers;
  }

  protected List<BpmnParseHandler> getDefaultBpmnParseHandlers()
  {
    List bpmnParserHandlers = new ArrayList();
    bpmnParserHandlers.add(new BoundaryEventParseHandler());
    bpmnParserHandlers.add(new BusinessRuleParseHandler());
    bpmnParserHandlers.add(new CallActivityParseHandler());
    bpmnParserHandlers.add(new CancelEventDefinitionParseHandler());
    bpmnParserHandlers.add(new CompensateEventDefinitionParseHandler());
    bpmnParserHandlers.add(new EndEventParseHandler());
    bpmnParserHandlers.add(new ErrorEventDefinitionParseHandler());
    bpmnParserHandlers.add(new EventBasedGatewayParseHandler());
    bpmnParserHandlers.add(new ExclusiveGatewayParseHandler());
    bpmnParserHandlers.add(new InclusiveGatewayParseHandler());
    bpmnParserHandlers.add(new IntermediateCatchEventParseHandler());
    bpmnParserHandlers.add(new IntermediateThrowEventParseHandler());
    bpmnParserHandlers.add(new ManualTaskParseHandler());
    bpmnParserHandlers.add(new MessageEventDefinitionParseHandler());
    bpmnParserHandlers.add(new ParallelGatewayParseHandler());
    bpmnParserHandlers.add(new ProcessParseHandler());
    bpmnParserHandlers.add(new ReceiveTaskParseHandler());
    bpmnParserHandlers.add(new ScriptTaskParseHandler());
    bpmnParserHandlers.add(new SendTaskParseHandler());
    bpmnParserHandlers.add(new SequenceFlowParseHandler());
    bpmnParserHandlers.add(new ServiceTaskParseHandler());
    bpmnParserHandlers.add(new SignalEventDefinitionParseHandler());
    bpmnParserHandlers.add(new StartEventParseHandler());
    bpmnParserHandlers.add(new SubProcessParseHandler());
    bpmnParserHandlers.add(new EventSubProcessParseHandler());
    bpmnParserHandlers.add(new TaskParseHandler());
    bpmnParserHandlers.add(new TimerEventDefinitionParseHandler());
    bpmnParserHandlers.add(new TransactionParseHandler());
    bpmnParserHandlers.add(new UserTaskParseHandler());

    if (this.customDefaultBpmnParseHandlers != null)
    {
      Map customParseHandlerMap = new HashMap();
      BpmnParseHandler bpmnParseHandler;
      for (Iterator i$ = this.customDefaultBpmnParseHandlers.iterator(); i$.hasNext(); ) { bpmnParseHandler = (BpmnParseHandler)i$.next();
        for (Class handledType : bpmnParseHandler.getHandledTypes())
          customParseHandlerMap.put(handledType, bpmnParseHandler);
      }
      
      for (int i = 0; i < bpmnParserHandlers.size(); i++)
      {
        BpmnParseHandler defaultBpmnParseHandler = (BpmnParseHandler)bpmnParserHandlers.get(i);
        if (defaultBpmnParseHandler.getHandledTypes().size() != 1) {
          StringBuilder supportedTypes = new StringBuilder();
          for (Class type : defaultBpmnParseHandler.getHandledTypes()) {
            supportedTypes.append(" ").append(type.getCanonicalName()).append(" ");
          }
          throw new ActivitiException(new StringBuilder().append("The default BPMN parse handlers should only support one type, but ").append(defaultBpmnParseHandler.getClass()).append(" supports ").append(supportedTypes.toString()).append(". This is likely a programmatic error").toString());
        }

        Class handledType = (Class)defaultBpmnParseHandler.getHandledTypes().iterator().next();
        if (customParseHandlerMap.containsKey(handledType)) {
          BpmnParseHandler newBpmnParseHandler = (BpmnParseHandler)customParseHandlerMap.get(handledType);
          log.info(new StringBuilder().append("Replacing default BpmnParseHandler ").append(defaultBpmnParseHandler.getClass().getName()).append(" with ").append(newBpmnParseHandler.getClass().getName()).toString());
          bpmnParserHandlers.set(i, newBpmnParseHandler);
        }

      }

    }

    for (BpmnParseHandler handler : getDefaultHistoryParseHandlers()) {
      bpmnParserHandlers.add(handler);
    }

    return bpmnParserHandlers;
  }

  protected List<BpmnParseHandler> getDefaultHistoryParseHandlers() {
    List parseHandlers = new ArrayList();
    parseHandlers.add(new FlowNodeHistoryParseHandler());
    parseHandlers.add(new ProcessHistoryParseHandler());
    parseHandlers.add(new StartEventHistoryParseHandler());
    parseHandlers.add(new UserTaskHistoryParseHandler());
    return parseHandlers;
  }

  private void initClock() {
    if (this.clock == null)
      this.clock = new DefaultClockImpl();
  }

  protected void initProcessDiagramGenerator()
  {
    if (this.processDiagramGenerator == null)
      this.processDiagramGenerator = new DefaultProcessDiagramGenerator();
  }

  protected void initJobHandlers()
  {
    this.jobHandlers = new HashMap();
    TimerExecuteNestedActivityJobHandler timerExecuteNestedActivityJobHandler = new TimerExecuteNestedActivityJobHandler();
    this.jobHandlers.put(timerExecuteNestedActivityJobHandler.getType(), timerExecuteNestedActivityJobHandler);

    TimerCatchIntermediateEventJobHandler timerCatchIntermediateEvent = new TimerCatchIntermediateEventJobHandler();
    this.jobHandlers.put(timerCatchIntermediateEvent.getType(), timerCatchIntermediateEvent);

    TimerStartEventJobHandler timerStartEvent = new TimerStartEventJobHandler();
    this.jobHandlers.put(timerStartEvent.getType(), timerStartEvent);

    AsyncContinuationJobHandler asyncContinuationJobHandler = new AsyncContinuationJobHandler();
    this.jobHandlers.put(asyncContinuationJobHandler.getType(), asyncContinuationJobHandler);

    ProcessEventJobHandler processEventJobHandler = new ProcessEventJobHandler();
    this.jobHandlers.put(processEventJobHandler.getType(), processEventJobHandler);

    TimerSuspendProcessDefinitionHandler suspendProcessDefinitionHandler = new TimerSuspendProcessDefinitionHandler();
    this.jobHandlers.put(suspendProcessDefinitionHandler.getType(), suspendProcessDefinitionHandler);

    TimerActivateProcessDefinitionHandler activateProcessDefinitionHandler = new TimerActivateProcessDefinitionHandler();
    this.jobHandlers.put(activateProcessDefinitionHandler.getType(), activateProcessDefinitionHandler);

    if (getCustomJobHandlers() != null)
      for (JobHandler customJobHandler : getCustomJobHandlers())
        this.jobHandlers.put(customJobHandler.getType(), customJobHandler);
  }

  protected void initJobExecutor()
  {
    if (!isAsyncExecutorEnabled()) {
      if (this.jobExecutor == null) {
        this.jobExecutor = new DefaultJobExecutor();
      }

      this.jobExecutor.setClockReader(this.clock);

      this.jobExecutor.setCommandExecutor(this.commandExecutor);
      this.jobExecutor.setAutoActivate(this.jobExecutorActivate);

      if (this.jobExecutor.getRejectedJobsHandler() == null)
        if (this.customRejectedJobsHandler != null)
          this.jobExecutor.setRejectedJobsHandler(this.customRejectedJobsHandler);
        else
          this.jobExecutor.setRejectedJobsHandler(new CallerRunsRejectedJobsHandler());
    }
  }

  protected void initAsyncExecutor()
  {
    if (isAsyncExecutorEnabled()) {
      if (this.asyncExecutor == null) {
        this.asyncExecutor = new DefaultAsyncJobExecutor();
      }

      this.asyncExecutor.setCommandExecutor(this.commandExecutor);
      this.asyncExecutor.setAutoActivate(this.asyncExecutorActivate);
    }
  }

  public void initHistoryLevel()
  {
    if (this.historyLevel == null)
      this.historyLevel = HistoryLevel.getHistoryLevelForKey(getHistory());
  }

  protected void initIdGenerator()
  {
    if (this.idGenerator == null) {
      CommandExecutor idGeneratorCommandExecutor = null;
      if (this.idGeneratorDataSource != null) {
        ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(this.idGeneratorDataSource);
        processEngineConfiguration.setDatabaseSchemaUpdate("false");
        processEngineConfiguration.init();
        idGeneratorCommandExecutor = processEngineConfiguration.getCommandExecutor();
      } else if (this.idGeneratorDataSourceJndiName != null) {
        ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        processEngineConfiguration.setDataSourceJndiName(this.idGeneratorDataSourceJndiName);
        processEngineConfiguration.setDatabaseSchemaUpdate("false");
        processEngineConfiguration.init();
        idGeneratorCommandExecutor = processEngineConfiguration.getCommandExecutor();
      } else {
        idGeneratorCommandExecutor = getCommandExecutor();
      }

      DbIdGenerator dbIdGenerator = new DbIdGenerator();
      dbIdGenerator.setIdBlockSize(this.idBlockSize);
      dbIdGenerator.setCommandExecutor(idGeneratorCommandExecutor);
      dbIdGenerator.setCommandConfig(getDefaultCommandConfig().transactionRequiresNew());
      this.idGenerator = dbIdGenerator;
    }
  }

  protected void initCommandContextFactory()
  {
    if (this.commandContextFactory == null) {
      this.commandContextFactory = new CommandContextFactory();
      this.commandContextFactory.setProcessEngineConfiguration(this);
    }
  }

  protected void initTransactionContextFactory() {
    if (this.transactionContextFactory == null)
      this.transactionContextFactory = new StandaloneMybatisTransactionContextFactory();
  }

  protected void initVariableTypes()
  {
    if (this.variableTypes == null) {
      this.variableTypes = new DefaultVariableTypes();
      if (this.customPreVariableTypes != null) {
        for (VariableType customVariableType : this.customPreVariableTypes) {
          this.variableTypes.addType(customVariableType);
        }
      }
      this.variableTypes.addType(new NullType());
      this.variableTypes.addType(new StringType(getMaxLengthString()));
      this.variableTypes.addType(new LongStringType(getMaxLengthString() + 1));
      this.variableTypes.addType(new BooleanType());
      this.variableTypes.addType(new ShortType());
      this.variableTypes.addType(new IntegerType());
      this.variableTypes.addType(new LongType());
      this.variableTypes.addType(new DateType());
      this.variableTypes.addType(new DoubleType());
      this.variableTypes.addType(new UUIDType());
      this.variableTypes.addType(new JsonType(getMaxLengthString(), this.objectMapper));
      this.variableTypes.addType(new LongJsonType(getMaxLengthString() + 1, this.objectMapper));
      this.variableTypes.addType(new ByteArrayType());
      this.variableTypes.addType(new SerializableType());
      this.variableTypes.addType(new CustomObjectType("item", ItemInstance.class));
      this.variableTypes.addType(new CustomObjectType("message", MessageInstance.class));
      if (this.customPostVariableTypes != null)
        for (VariableType customVariableType : this.customPostVariableTypes)
          this.variableTypes.addType(customVariableType);
    }
  }

  protected int getMaxLengthString()
  {
    if (this.maxLengthStringVariableType == -1) {
      if ("oracle".equalsIgnoreCase(this.databaseType) == true) {
        return 2000;
      }
      return 4000;
    }

    return this.maxLengthStringVariableType;
  }

  protected void initFormEngines()
  {
    if (this.formEngines == null) {
      this.formEngines = new HashMap();
      FormEngine defaultFormEngine = new JuelFormEngine();
      this.formEngines.put(null, defaultFormEngine);
      this.formEngines.put(defaultFormEngine.getName(), defaultFormEngine);
    }
    if (this.customFormEngines != null)
      for (FormEngine formEngine : this.customFormEngines)
        this.formEngines.put(formEngine.getName(), formEngine);
  }

  protected void initFormTypes()
  {
    if (this.formTypes == null) {
      this.formTypes = new FormTypes();
      this.formTypes.addFormType(new StringFormType());
      this.formTypes.addFormType(new LongFormType());
      this.formTypes.addFormType(new DateFormType("dd/MM/yyyy"));
      this.formTypes.addFormType(new BooleanFormType());
      this.formTypes.addFormType(new DoubleFormType());
    }
    if (this.customFormTypes != null)
      for (AbstractFormType customFormType : this.customFormTypes)
        this.formTypes.addFormType(customFormType);
  }

  protected void initScriptingEngines()
  {
    if (this.resolverFactories == null) {
      this.resolverFactories = new ArrayList();
      this.resolverFactories.add(new VariableScopeResolverFactory());
      this.resolverFactories.add(new BeansResolverFactory());
    }
    if (this.scriptingEngines == null)
      this.scriptingEngines = new ScriptingEngines(new ScriptBindingsFactory(this.resolverFactories));
  }

  protected void initExpressionManager()
  {
    if (this.expressionManager == null)
      this.expressionManager = new ExpressionManager(this.beans);
  }

  protected void initBusinessCalendarManager()
  {
    if (this.businessCalendarManager == null) {
      MapBusinessCalendarManager mapBusinessCalendarManager = new MapBusinessCalendarManager();
      mapBusinessCalendarManager.addBusinessCalendar(DurationBusinessCalendar.NAME, new DurationBusinessCalendar(this.clock));
      mapBusinessCalendarManager.addBusinessCalendar("dueDate", new DueDateBusinessCalendar(this.clock));
      mapBusinessCalendarManager.addBusinessCalendar(CycleBusinessCalendar.NAME, new CycleBusinessCalendar(this.clock));

      this.businessCalendarManager = mapBusinessCalendarManager;
    }
  }

  protected void initDelegateInterceptor() {
    if (this.delegateInterceptor == null)
      this.delegateInterceptor = new DefaultDelegateInterceptor();
  }

  protected void initEventHandlers()
  {
    if (this.eventHandlers == null) {
      this.eventHandlers = new HashMap();

      SignalEventHandler signalEventHander = new SignalEventHandler();
      this.eventHandlers.put(signalEventHander.getEventHandlerType(), signalEventHander);

      CompensationEventHandler compensationEventHandler = new CompensationEventHandler();
      this.eventHandlers.put(compensationEventHandler.getEventHandlerType(), compensationEventHandler);

      MessageEventHandler messageEventHandler = new MessageEventHandler();
      this.eventHandlers.put(messageEventHandler.getEventHandlerType(), messageEventHandler);
    }

    if (this.customEventHandlers != null)
      for (EventHandler eventHandler : this.customEventHandlers)
        this.eventHandlers.put(eventHandler.getEventHandlerType(), eventHandler);
  }

  protected void initJpa()
  {
    if (this.jpaPersistenceUnitName != null) {
      this.jpaEntityManagerFactory = JpaHelper.createEntityManagerFactory(this.jpaPersistenceUnitName);
    }
    if (this.jpaEntityManagerFactory != null) {
      this.sessionFactories.put(EntityManagerSession.class, new EntityManagerSessionFactory(this.jpaEntityManagerFactory, this.jpaHandleTransaction, this.jpaCloseEntityManager));
      VariableType jpaType = this.variableTypes.getVariableType("jpa-entity");

      if (jpaType == null)
      {
        int serializableIndex = this.variableTypes.getTypeIndex("serializable");
        if (serializableIndex > -1)
          this.variableTypes.addType(new JPAEntityVariableType(), serializableIndex);
        else {
          this.variableTypes.addType(new JPAEntityVariableType());
        }
      }

      jpaType = this.variableTypes.getVariableType("jpa-entity-list");

      if (jpaType == null)
        this.variableTypes.addType(new JPAEntityListVariableType(), this.variableTypes.getTypeIndex("jpa-entity"));
    }
  }

  protected void initBeans()
  {
    if (this.beans == null)
      this.beans = new HashMap();
  }

  protected void initEventDispatcher()
  {
    if (this.eventDispatcher == null) {
      this.eventDispatcher = new ActivitiEventDispatcherImpl();
    }

    this.eventDispatcher.setEnabled(this.enableEventDispatcher);

    if (this.eventListeners != null) {
      for (ActivitiEventListener listenerToAdd : this.eventListeners) {
        this.eventDispatcher.addEventListener(listenerToAdd);
      }
    }

    ActivitiEventType[] types;
    if (this.typedEventListeners != null)
      for (Map.Entry listenersToAdd : this.typedEventListeners.entrySet())
      {
        types = ActivitiEventType.getTypesFromString((String)listenersToAdd.getKey());

        for (ActivitiEventListener listenerToAdd : (List<ActivitiEventListener>)listenersToAdd.getValue())
          this.eventDispatcher.addEventListener(listenerToAdd, types);
      }
    
  }

  protected void initProcessValidator()
  {
    if (this.processValidator == null)
      this.processValidator = new ProcessValidatorFactory().createDefaultProcessValidator();
  }

  protected void initDatabaseEventLogging()
  {
    if (this.enableDatabaseEventLogging)
    {
      getEventDispatcher().addEventListener(new EventLogger(this.clock, this.objectMapper));
    }
  }

  public CommandConfig getDefaultCommandConfig()
  {
    return this.defaultCommandConfig;
  }

  public void setDefaultCommandConfig(CommandConfig defaultCommandConfig) {
    this.defaultCommandConfig = defaultCommandConfig;
  }

  public CommandConfig getSchemaCommandConfig() {
    return this.schemaCommandConfig;
  }

  public void setSchemaCommandConfig(CommandConfig schemaCommandConfig) {
    this.schemaCommandConfig = schemaCommandConfig;
  }

  public CommandInterceptor getCommandInvoker() {
    return this.commandInvoker;
  }

  public void setCommandInvoker(CommandInterceptor commandInvoker) {
    this.commandInvoker = commandInvoker;
  }

  public List<CommandInterceptor> getCustomPreCommandInterceptors() {
    return this.customPreCommandInterceptors;
  }

  public ProcessEngineConfigurationImpl setCustomPreCommandInterceptors(List<CommandInterceptor> customPreCommandInterceptors) {
    this.customPreCommandInterceptors = customPreCommandInterceptors;
    return this;
  }

  public List<CommandInterceptor> getCustomPostCommandInterceptors() {
    return this.customPostCommandInterceptors;
  }

  public ProcessEngineConfigurationImpl setCustomPostCommandInterceptors(List<CommandInterceptor> customPostCommandInterceptors) {
    this.customPostCommandInterceptors = customPostCommandInterceptors;
    return this;
  }

  public List<CommandInterceptor> getCommandInterceptors() {
    return this.commandInterceptors;
  }

  public ProcessEngineConfigurationImpl setCommandInterceptors(List<CommandInterceptor> commandInterceptors) {
    this.commandInterceptors = commandInterceptors;
    return this;
  }

  public CommandExecutor getCommandExecutor() {
    return this.commandExecutor;
  }

  public ProcessEngineConfigurationImpl setCommandExecutor(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
    return this;
  }

  public RepositoryService getRepositoryService() {
    return this.repositoryService;
  }

  public ProcessEngineConfigurationImpl setRepositoryService(RepositoryService repositoryService) {
    this.repositoryService = repositoryService;
    return this;
  }

  public RuntimeService getRuntimeService() {
    return this.runtimeService;
  }

  public ProcessEngineConfigurationImpl setRuntimeService(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
    return this;
  }

  public HistoryService getHistoryService() {
    return this.historyService;
  }

  public ProcessEngineConfigurationImpl setHistoryService(HistoryService historyService) {
    this.historyService = historyService;
    return this;
  }

  public IdentityService getIdentityService() {
    return this.identityService;
  }

  public ProcessEngineConfigurationImpl setIdentityService(IdentityService identityService) {
    this.identityService = identityService;
    return this;
  }

  public TaskService getTaskService() {
    return this.taskService;
  }

  public ProcessEngineConfigurationImpl setTaskService(TaskService taskService) {
    this.taskService = taskService;
    return this;
  }

  public FormService getFormService() {
    return this.formService;
  }

  public ProcessEngineConfigurationImpl setFormService(FormService formService) {
    this.formService = formService;
    return this;
  }

  public ManagementService getManagementService() {
    return this.managementService;
  }

  public ProcessEngineConfigurationImpl setManagementService(ManagementService managementService) {
    this.managementService = managementService;
    return this;
  }

  public DynamicBpmnService getDynamicBpmnService() {
    return this.dynamicBpmnService;
  }

  public ProcessEngineConfigurationImpl setDynamicBpmnService(DynamicBpmnService dynamicBpmnService) {
    this.dynamicBpmnService = dynamicBpmnService;
    return this;
  }

  public ProcessEngineConfiguration getProcessEngineConfiguration() {
    return this;
  }

  public Map<Class<?>, SessionFactory> getSessionFactories() {
    return this.sessionFactories;
  }

  public ProcessEngineConfigurationImpl setSessionFactories(Map<Class<?>, SessionFactory> sessionFactories) {
    this.sessionFactories = sessionFactories;
    return this;
  }

  public List<ProcessEngineConfigurator> getConfigurators() {
    return this.configurators;
  }

  public ProcessEngineConfigurationImpl addConfigurator(ProcessEngineConfigurator configurator) {
    if (this.configurators == null) {
      this.configurators = new ArrayList();
    }
    this.configurators.add(configurator);
    return this;
  }

  public ProcessEngineConfigurationImpl setConfigurators(List<ProcessEngineConfigurator> configurators) {
    this.configurators = configurators;
    return this;
  }

  public void setEnableConfiguratorServiceLoader(boolean enableConfiguratorServiceLoader) {
    this.enableConfiguratorServiceLoader = enableConfiguratorServiceLoader;
  }

  public List<ProcessEngineConfigurator> getAllConfigurators() {
    return this.allConfigurators;
  }

  public BpmnDeployer getBpmnDeployer() {
    return this.bpmnDeployer;
  }

  public ProcessEngineConfigurationImpl setBpmnDeployer(BpmnDeployer bpmnDeployer) {
    this.bpmnDeployer = bpmnDeployer;
    return this;
  }

  public BpmnParser getBpmnParser() {
    return this.bpmnParser;
  }

  public ProcessEngineConfigurationImpl setBpmnParser(BpmnParser bpmnParser) {
    this.bpmnParser = bpmnParser;
    return this;
  }

  public List<Deployer> getDeployers() {
    return this.deployers;
  }

  public ProcessEngineConfigurationImpl setDeployers(List<Deployer> deployers) {
    this.deployers = deployers;
    return this;
  }

  public IdGenerator getIdGenerator() {
    return this.idGenerator;
  }

  public ProcessEngineConfigurationImpl setIdGenerator(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
    return this;
  }

  public String getWsSyncFactoryClassName() {
    return this.wsSyncFactoryClassName;
  }

  public ProcessEngineConfigurationImpl setWsSyncFactoryClassName(String wsSyncFactoryClassName) {
    this.wsSyncFactoryClassName = wsSyncFactoryClassName;
    return this;
  }

  public Map<String, FormEngine> getFormEngines() {
    return this.formEngines;
  }

  public ProcessEngineConfigurationImpl setFormEngines(Map<String, FormEngine> formEngines) {
    this.formEngines = formEngines;
    return this;
  }

  public FormTypes getFormTypes() {
    return this.formTypes;
  }

  public ProcessEngineConfigurationImpl setFormTypes(FormTypes formTypes) {
    this.formTypes = formTypes;
    return this;
  }

  public ScriptingEngines getScriptingEngines() {
    return this.scriptingEngines;
  }

  public ProcessEngineConfigurationImpl setScriptingEngines(ScriptingEngines scriptingEngines) {
    this.scriptingEngines = scriptingEngines;
    return this;
  }

  public VariableTypes getVariableTypes() {
    return this.variableTypes;
  }

  public ProcessEngineConfigurationImpl setVariableTypes(VariableTypes variableTypes) {
    this.variableTypes = variableTypes;
    return this;
  }

  public ExpressionManager getExpressionManager() {
    return this.expressionManager;
  }

  public ProcessEngineConfigurationImpl setExpressionManager(ExpressionManager expressionManager) {
    this.expressionManager = expressionManager;
    return this;
  }

  public BusinessCalendarManager getBusinessCalendarManager() {
    return this.businessCalendarManager;
  }

  public ProcessEngineConfigurationImpl setBusinessCalendarManager(BusinessCalendarManager businessCalendarManager) {
    this.businessCalendarManager = businessCalendarManager;
    return this;
  }

  public CommandContextFactory getCommandContextFactory() {
    return this.commandContextFactory;
  }

  public ProcessEngineConfigurationImpl setCommandContextFactory(CommandContextFactory commandContextFactory) {
    this.commandContextFactory = commandContextFactory;
    return this;
  }

  public TransactionContextFactory getTransactionContextFactory() {
    return this.transactionContextFactory;
  }

  public ProcessEngineConfigurationImpl setTransactionContextFactory(TransactionContextFactory transactionContextFactory) {
    this.transactionContextFactory = transactionContextFactory;
    return this;
  }

  public List<Deployer> getCustomPreDeployers() {
    return this.customPreDeployers;
  }

  public ProcessEngineConfigurationImpl setCustomPreDeployers(List<Deployer> customPreDeployers) {
    this.customPreDeployers = customPreDeployers;
    return this;
  }

  public List<Deployer> getCustomPostDeployers() {
    return this.customPostDeployers;
  }

  public ProcessEngineConfigurationImpl setCustomPostDeployers(List<Deployer> customPostDeployers) {
    this.customPostDeployers = customPostDeployers;
    return this;
  }

  public Map<String, JobHandler> getJobHandlers() {
    return this.jobHandlers;
  }

  public ProcessEngineConfigurationImpl setJobHandlers(Map<String, JobHandler> jobHandlers) {
    this.jobHandlers = jobHandlers;
    return this;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return this.sqlSessionFactory;
  }

  public ProcessEngineConfigurationImpl setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
    return this;
  }

  public DbSqlSessionFactory getDbSqlSessionFactory() {
    return this.dbSqlSessionFactory;
  }

  public ProcessEngineConfigurationImpl setDbSqlSessionFactory(DbSqlSessionFactory dbSqlSessionFactory) {
    this.dbSqlSessionFactory = dbSqlSessionFactory;
    return this;
  }

  public TransactionFactory getTransactionFactory() {
    return this.transactionFactory;
  }

  public ProcessEngineConfigurationImpl setTransactionFactory(TransactionFactory transactionFactory) {
    this.transactionFactory = transactionFactory;
    return this;
  }

  public List<SessionFactory> getCustomSessionFactories() {
    return this.customSessionFactories;
  }

  public ProcessEngineConfigurationImpl setCustomSessionFactories(List<SessionFactory> customSessionFactories) {
    this.customSessionFactories = customSessionFactories;
    return this;
  }

  public List<JobHandler> getCustomJobHandlers() {
    return this.customJobHandlers;
  }

  public ProcessEngineConfigurationImpl setCustomJobHandlers(List<JobHandler> customJobHandlers) {
    this.customJobHandlers = customJobHandlers;
    return this;
  }

  public List<FormEngine> getCustomFormEngines() {
    return this.customFormEngines;
  }

  public ProcessEngineConfigurationImpl setCustomFormEngines(List<FormEngine> customFormEngines) {
    this.customFormEngines = customFormEngines;
    return this;
  }

  public List<AbstractFormType> getCustomFormTypes() {
    return this.customFormTypes;
  }

  public ProcessEngineConfigurationImpl setCustomFormTypes(List<AbstractFormType> customFormTypes) {
    this.customFormTypes = customFormTypes;
    return this;
  }

  public List<String> getCustomScriptingEngineClasses() {
    return this.customScriptingEngineClasses;
  }

  public ProcessEngineConfigurationImpl setCustomScriptingEngineClasses(List<String> customScriptingEngineClasses) {
    this.customScriptingEngineClasses = customScriptingEngineClasses;
    return this;
  }

  public List<VariableType> getCustomPreVariableTypes() {
    return this.customPreVariableTypes;
  }

  public ProcessEngineConfigurationImpl setCustomPreVariableTypes(List<VariableType> customPreVariableTypes) {
    this.customPreVariableTypes = customPreVariableTypes;
    return this;
  }

  public List<VariableType> getCustomPostVariableTypes() {
    return this.customPostVariableTypes;
  }

  public ProcessEngineConfigurationImpl setCustomPostVariableTypes(List<VariableType> customPostVariableTypes) {
    this.customPostVariableTypes = customPostVariableTypes;
    return this;
  }

  public List<BpmnParseHandler> getPreBpmnParseHandlers() {
    return this.preBpmnParseHandlers;
  }

  public ProcessEngineConfigurationImpl setPreBpmnParseHandlers(List<BpmnParseHandler> preBpmnParseHandlers) {
    this.preBpmnParseHandlers = preBpmnParseHandlers;
    return this;
  }

  public List<BpmnParseHandler> getCustomDefaultBpmnParseHandlers() {
    return this.customDefaultBpmnParseHandlers;
  }

  public ProcessEngineConfigurationImpl setCustomDefaultBpmnParseHandlers(List<BpmnParseHandler> customDefaultBpmnParseHandlers) {
    this.customDefaultBpmnParseHandlers = customDefaultBpmnParseHandlers;
    return this;
  }

  public List<BpmnParseHandler> getPostBpmnParseHandlers() {
    return this.postBpmnParseHandlers;
  }

  public ProcessEngineConfigurationImpl setPostBpmnParseHandlers(List<BpmnParseHandler> postBpmnParseHandlers) {
    this.postBpmnParseHandlers = postBpmnParseHandlers;
    return this;
  }

  public ActivityBehaviorFactory getActivityBehaviorFactory() {
    return this.activityBehaviorFactory;
  }

  public ProcessEngineConfigurationImpl setActivityBehaviorFactory(ActivityBehaviorFactory activityBehaviorFactory) {
    this.activityBehaviorFactory = activityBehaviorFactory;
    return this;
  }

  public ListenerFactory getListenerFactory() {
    return this.listenerFactory;
  }

  public ProcessEngineConfigurationImpl setListenerFactory(ListenerFactory listenerFactory) {
    this.listenerFactory = listenerFactory;
    return this;
  }

  public BpmnParseFactory getBpmnParseFactory() {
    return this.bpmnParseFactory;
  }

  public ProcessEngineConfigurationImpl setBpmnParseFactory(BpmnParseFactory bpmnParseFactory) {
    this.bpmnParseFactory = bpmnParseFactory;
    return this;
  }

  public Map<Object, Object> getBeans() {
    return this.beans;
  }

  public ProcessEngineConfigurationImpl setBeans(Map<Object, Object> beans) {
    this.beans = beans;
    return this;
  }

  public List<ResolverFactory> getResolverFactories() {
    return this.resolverFactories;
  }

  public ProcessEngineConfigurationImpl setResolverFactories(List<ResolverFactory> resolverFactories) {
    this.resolverFactories = resolverFactories;
    return this;
  }

  public DeploymentManager getDeploymentManager() {
    return this.deploymentManager;
  }

  public ProcessEngineConfigurationImpl setDeploymentManager(DeploymentManager deploymentManager) {
    this.deploymentManager = deploymentManager;
    return this;
  }

  public ProcessEngineConfigurationImpl setDelegateInterceptor(DelegateInterceptor delegateInterceptor) {
    this.delegateInterceptor = delegateInterceptor;
    return this;
  }

  public DelegateInterceptor getDelegateInterceptor() {
    return this.delegateInterceptor;
  }

  public RejectedJobsHandler getCustomRejectedJobsHandler() {
    return this.customRejectedJobsHandler;
  }

  public ProcessEngineConfigurationImpl setCustomRejectedJobsHandler(RejectedJobsHandler customRejectedJobsHandler) {
    this.customRejectedJobsHandler = customRejectedJobsHandler;
    return this;
  }

  public EventHandler getEventHandler(String eventType) {
    return (EventHandler)this.eventHandlers.get(eventType);
  }

  public ProcessEngineConfigurationImpl setEventHandlers(Map<String, EventHandler> eventHandlers) {
    this.eventHandlers = eventHandlers;
    return this;
  }

  public Map<String, EventHandler> getEventHandlers() {
    return this.eventHandlers;
  }

  public List<EventHandler> getCustomEventHandlers() {
    return this.customEventHandlers;
  }

  public ProcessEngineConfigurationImpl setCustomEventHandlers(List<EventHandler> customEventHandlers) {
    this.customEventHandlers = customEventHandlers;
    return this;
  }

  public FailedJobCommandFactory getFailedJobCommandFactory() {
    return this.failedJobCommandFactory;
  }

  public ProcessEngineConfigurationImpl setFailedJobCommandFactory(FailedJobCommandFactory failedJobCommandFactory) {
    this.failedJobCommandFactory = failedJobCommandFactory;
    return this;
  }

  public DataSource getIdGeneratorDataSource() {
    return this.idGeneratorDataSource;
  }

  public ProcessEngineConfigurationImpl setIdGeneratorDataSource(DataSource idGeneratorDataSource) {
    this.idGeneratorDataSource = idGeneratorDataSource;
    return this;
  }

  public String getIdGeneratorDataSourceJndiName() {
    return this.idGeneratorDataSourceJndiName;
  }

  public ProcessEngineConfigurationImpl setIdGeneratorDataSourceJndiName(String idGeneratorDataSourceJndiName) {
    this.idGeneratorDataSourceJndiName = idGeneratorDataSourceJndiName;
    return this;
  }

  public int getBatchSizeProcessInstances() {
    return this.batchSizeProcessInstances;
  }

  public ProcessEngineConfigurationImpl setBatchSizeProcessInstances(int batchSizeProcessInstances) {
    this.batchSizeProcessInstances = batchSizeProcessInstances;
    return this;
  }

  public int getBatchSizeTasks() {
    return this.batchSizeTasks;
  }

  public ProcessEngineConfigurationImpl setBatchSizeTasks(int batchSizeTasks) {
    this.batchSizeTasks = batchSizeTasks;
    return this;
  }

  public int getProcessDefinitionCacheLimit() {
    return this.processDefinitionCacheLimit;
  }

  public ProcessEngineConfigurationImpl setProcessDefinitionCacheLimit(int processDefinitionCacheLimit) {
    this.processDefinitionCacheLimit = processDefinitionCacheLimit;
    return this;
  }

  public DeploymentCache<ProcessDefinitionEntity> getProcessDefinitionCache() {
    return this.processDefinitionCache;
  }

  public ProcessEngineConfigurationImpl setProcessDefinitionCache(DeploymentCache<ProcessDefinitionEntity> processDefinitionCache) {
    this.processDefinitionCache = processDefinitionCache;
    return this;
  }

  public int getKnowledgeBaseCacheLimit() {
    return this.knowledgeBaseCacheLimit;
  }

  public ProcessEngineConfigurationImpl setKnowledgeBaseCacheLimit(int knowledgeBaseCacheLimit) {
    this.knowledgeBaseCacheLimit = knowledgeBaseCacheLimit;
    return this;
  }

  public DeploymentCache<Object> getKnowledgeBaseCache() {
    return this.knowledgeBaseCache;
  }

  public ProcessEngineConfigurationImpl setKnowledgeBaseCache(DeploymentCache<Object> knowledgeBaseCache) {
    this.knowledgeBaseCache = knowledgeBaseCache;
    return this;
  }

  public boolean isEnableSafeBpmnXml() {
    return this.enableSafeBpmnXml;
  }

  public ProcessEngineConfigurationImpl setEnableSafeBpmnXml(boolean enableSafeBpmnXml) {
    this.enableSafeBpmnXml = enableSafeBpmnXml;
    return this;
  }

  public ActivitiEventDispatcher getEventDispatcher() {
    return this.eventDispatcher;
  }

  public void setEventDispatcher(ActivitiEventDispatcher eventDispatcher) {
    this.eventDispatcher = eventDispatcher;
  }

  public void setEnableEventDispatcher(boolean enableEventDispatcher) {
    this.enableEventDispatcher = enableEventDispatcher;
  }

  public void setTypedEventListeners(Map<String, List<ActivitiEventListener>> typedListeners) {
    this.typedEventListeners = typedListeners;
  }

  public void setEventListeners(List<ActivitiEventListener> eventListeners) {
    this.eventListeners = eventListeners;
  }

  public ProcessValidator getProcessValidator() {
    return this.processValidator;
  }

  public void setProcessValidator(ProcessValidator processValidator) {
    this.processValidator = processValidator;
  }

  public boolean isEnableEventDispatcher() {
    return this.enableEventDispatcher;
  }

  public boolean isEnableDatabaseEventLogging() {
    return this.enableDatabaseEventLogging;
  }

  public ProcessEngineConfigurationImpl setEnableDatabaseEventLogging(boolean enableDatabaseEventLogging) {
    this.enableDatabaseEventLogging = enableDatabaseEventLogging;
    return this;
  }

  public int getMaxLengthStringVariableType() {
    return this.maxLengthStringVariableType;
  }

  public ProcessEngineConfigurationImpl setMaxLengthStringVariableType(int maxLengthStringVariableType) {
    this.maxLengthStringVariableType = maxLengthStringVariableType;
    return this;
  }

  public ProcessEngineConfigurationImpl setBulkInsertEnabled(boolean isBulkInsertEnabled) {
    this.isBulkInsertEnabled = isBulkInsertEnabled;
    return this;
  }

  public boolean isBulkInsertEnabled() {
    return this.isBulkInsertEnabled;
  }

  public int getMaxNrOfStatementsInBulkInsert() {
    return this.maxNrOfStatementsInBulkInsert;
  }

  public ProcessEngineConfigurationImpl setMaxNrOfStatementsInBulkInsert(int maxNrOfStatementsInBulkInsert) {
    this.maxNrOfStatementsInBulkInsert = maxNrOfStatementsInBulkInsert;
    return this;
  }

  public ObjectMapper getObjectMapper() {
    return this.objectMapper;
  }
}