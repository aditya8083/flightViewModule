package com.coviam.cache;

import com.aerospike.client.Log;
import com.aerospike.client.async.AsyncClientPolicy;
import com.aerospike.client.policy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
//@DependsOn(value = "AerospikeConfParams")
public class AeroSpikeConfig {

    public static ClientPolicy clientPolicy;
    public static AsyncClientPolicy asyncClientPolicy;
    @Autowired AerospikeConfParams aerospikeConfParams;
    protected static int timeOut = 0;
    protected ApplicationContext applicationContext;


    @Autowired
    public AeroSpikeConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initialize();

        setDefaultClientPolicies();
        setAsyncClientPolicy();
        configureLog();
    }

    public void initialize() {
        aerospikeConfParams = (AerospikeConfParams) applicationContext.getBean("aerospikeConfParams");
    }

    protected void setDefaultClientPolicies() {
        clientPolicy = new ClientPolicy();
        clientPolicy.maxConnsPerNode = aerospikeConfParams.getMaxThreads();
        clientPolicy.queryPolicyDefault = getDefaultQueryPolicy();
        clientPolicy.readPolicyDefault = getDefaultReadPolicy();
        clientPolicy.writePolicyDefault = getDefaultWritePolicy();
        clientPolicy.scanPolicyDefault =  getDefaultScanPolicy();
       /* clientPolicy.user = user;
        clientPolicy.password = password;*/
    }

    private ScanPolicy getDefaultScanPolicy() {
        return new ScanPolicy();

    }

    private Policy getDefaultReadPolicy() {
        Policy readPolicy = new Policy();
        readPolicy.maxRetries = 0;
        readPolicy.totalTimeout = timeOut;
        return readPolicy;
    }

    protected static WritePolicy getDefaultWritePolicy() {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.totalTimeout = timeOut;
        writePolicy.maxRetries = 0;
        return writePolicy;
    }

    protected QueryPolicy getDefaultQueryPolicy() {
        QueryPolicy readPolicy = new QueryPolicy();
        readPolicy.totalTimeout = timeOut;
        readPolicy.maxRetries = 0;
        return readPolicy;
    }


    public void setAsyncClientPolicy() {
        asyncClientPolicy = new AsyncClientPolicy();
        asyncClientPolicy.asyncMaxCommands = aerospikeConfParams.getAsyncMaxCommands();
        asyncClientPolicy.asyncWritePolicyDefault = getDefaultWritePolicy();
        asyncClientPolicy.asyncReadPolicyDefault = getDefaultQueryPolicy();
    }

   protected void configureLog() {
        if (aerospikeConfParams.isLogEnabled()) {
            Log aerospikelog = new Log();
            aerospikelog.log(Log.Level.INFO, "log is enabled in Info mode");
        }
    }

}
