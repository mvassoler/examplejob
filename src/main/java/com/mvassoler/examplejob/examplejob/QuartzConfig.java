package com.mvassoler.examplejob.examplejob;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@EnableAutoConfiguration
public class QuartzConfig {


    private final ApplicationContext applicationContext;
    private final Environment environment;
    private final DataSource dataSource;

    public QuartzConfig(ApplicationContext applicationContext, Environment environment, DataSource dataSource) {
        this.applicationContext = applicationContext;
        this.environment = environment;
        this.dataSource = dataSource;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        schedulerFactoryBean.setSchedulerName("XGraccoScheduler");
        schedulerFactoryBean.setAutoStartup(Boolean.TRUE);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(Boolean.TRUE);
        schedulerFactoryBean.setOverwriteExistingJobs(Boolean.TRUE);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setExposeSchedulerInRepository(Boolean.TRUE);
        schedulerFactoryBean.setApplicationContext(applicationContext);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");

        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "XGraccoScheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.jobStore.useProperties", "false");
        properties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", environment.getProperty("org.quartz.driver-delegate"));
        properties.setProperty("org.quartz.jobStore.dataSource","quartzDs");

        properties.setProperty("org.quartz.dataSource.quartzDs.driver",environment.getProperty("spring.datasource.driver-class-name"));
        properties.setProperty("org.quartz.dataSource.quartzDs.URL",environment.getProperty("spring.datasource.url"));
        properties.setProperty("org.quartz.dataSource.quartzDs.user",environment.getProperty("spring.datasource.username"));
        properties.setProperty("org.quartz.dataSource.quartzDs.password",environment.getProperty("spring.datasource.password"));
        properties.setProperty("org.quartz.dataSource.quartzDs.maxConnections","30");



        schedulerFactoryBean.setQuartzProperties(properties);

        return schedulerFactoryBean;
    }
}
