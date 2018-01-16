package com.coviam.cache;

import com.aerospike.client.*;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.coviam.util.IOCompressUtil;
import com.jcabi.aspects.Loggable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

@Component
@DependsOn(value = "aeroSpikeConfig")
public class CachingWrapper {
    protected static AerospikeClient client;
    @Autowired private IOCompressUtil ioCompressUtil;
    protected AeroSpikeConfig aeroSpikeConfig;
    protected ApplicationContext applicationContext;
    @Autowired AerospikeConfParams aerospikeConfParams;
    final static private int timeToliveValueDefault = 2700;
    static int count = 0;

    private static Logger log = Logger.getLogger(String.valueOf(CachingWrapper.class));

    @Autowired
    public CachingWrapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initialize();
        try {
            Host[] hosts = aerospikeConfParams.getAerospikeHosts().stream()
                    .map(ht -> new Host(ht,aerospikeConfParams.getAerospikePort()))
                    .toArray(host -> new Host[host]);

            log.info("Aerospike client initialized here");
            client = new AerospikeClient(aeroSpikeConfig.clientPolicy, hosts);
        } catch (Exception e) {
           // log.error("Error in creating aerospike client, AerospikeHosts : {}, Aerospikeport :{}, Exception :{}", aerospikeConfParams.getAerospikeHosts(), aerospikeConfParams.getAerospikePort(), e);
        }
    }


    public void initialize() {
        aeroSpikeConfig = (AeroSpikeConfig) applicationContext.getBean("aeroSpikeConfig");
        aerospikeConfParams = (AerospikeConfParams) applicationContext.getBean("aerospikeConfParams");
    }

    public AerospikeClient getClient() {
        return client;
    }

    public void closeClient() {
        if (client != null) {
            client.close();
        }
    }

    protected void isConnected() throws Throwable {
        client.isConnected();
    }
   /* @Loggable
    public void cache(String setName, String keyName, String colName, String colValue) {
        try{

         *//*   if(propertiesReader.getBooleanValue(setName)) {
                log.info("Cache is switched off for cache name : {}", setName);
                return;
            }*//*
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            Bin bin = new Bin(colName, colValue);
            String timeToliveValue = aerospikeConfParams.getSetTimeToLive().get(setName).toString();
            WritePolicy writePolicy = aeroSpikeConfig.getDefaultWritePolicy();
            if(!StringUtils.isBlank(timeToliveValue)) {
                writePolicy.expiration = Integer.parseInt(timeToliveValue);
            }else{
                writePolicy.expiration = timeToliveValueDefault;
            }
            client.put(writePolicy, key, bin);
            log.info(" response get cached ");
        } catch(Exception exception) {
           // log.error("Error in creating cache, keyName :{}, Exception :{}", keyName, exception);
        }

    }
*/
    @Loggable
    public void writeWithoutCompression(String setName, String keyName, String colName, String colValue) {
        try {
         /*   if(propertiesReader.getBooleanValue(setName)) {
                log.info("Cache is switched off for cache name : {}", setName);
                return;
            }*/
            String	timeToliveValue = aerospikeConfParams.getSetTimeToLive().get(setName).toString();
            WritePolicy writePolicy = aeroSpikeConfig.getDefaultWritePolicy();
            if(!StringUtils.isBlank(timeToliveValue)) {
                writePolicy.expiration = Integer.parseInt(timeToliveValue);
            }else{
                writePolicy.expiration = timeToliveValueDefault;
            }
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            Bin bin = new Bin(colName, colValue);
            client.put(writePolicy, key, bin);
            log.info("response got cached");
        }catch(Exception e){
           // log.error("Exception while inserting for keySet {} with colName {} and into cache : {}", keyName, colName, e);
        }
    }

    @Loggable
    public void write(String setName, String keyName, Map<String, Object> binData) {
        try {

            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            Bin[] bins = new Bin[binData.size()];
            int array = 0;
            for (Map.Entry<String, Object> entry : binData.entrySet()) {
                Bin bin = new Bin((String) entry.getValue(), entry.getValue());
                bins[array] = bin;
                array++;
            }
            client.put(aeroSpikeConfig.clientPolicy.writePolicyDefault, key, bins);
            log.info("response got cached");
        } catch (Exception e) {
           // log.error("Exception while inserting for keySet {} and into cache : {}", keyName, e);
        }
    }

    @Loggable
    public void writeWithCompression(String setName, String keyName, String colName, Object colValue) {
        try {
       /*     if(propertiesReader.getBooleanValue(setName)) {
                log.info("Cache is switched off for cache name : {}", setName);
                return;
            }*/
            String timeToliveValue = aerospikeConfParams.getSetTimeToLive().get(setName).toString();
            WritePolicy writePolicy = aeroSpikeConfig.getDefaultWritePolicy();
            if(!StringUtils.isBlank(timeToliveValue)) {
                writePolicy.expiration = Integer.parseInt(timeToliveValue);
            }else{
                writePolicy.expiration = timeToliveValueDefault;
            }
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            byte [] compressData =  ioCompressUtil.compressString(colValue.toString());
            Bin bin = new Bin(colName, compressData);
            client.put(writePolicy, key, bin);
            log.info("response get cached ");
        } catch (Exception e) {
         //   log.error("Exception while inserting for keySet {} with colName {} and into cache : {}", keyName, colName, e);
        }
    }


    @Loggable
    public boolean exists(String setName, String keyName) {
        Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
        return client.exists(aeroSpikeConfig.clientPolicy.readPolicyDefault, key);
    }

    @Loggable
    public  Record read(String setName, String keyName) {
        Record record = null;
        try{
          /*  if(propertiesReader.getBooleanValue(setName)) {
                log.info("Cache is switched off for key name : {}", setName);
                return null;
            }*/
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            record = client.get(aeroSpikeConfig.clientPolicy.readPolicyDefault, key);
            if(record.bins.size()!=0) {
             //   log.debug(" Cached Value Coming From DB : {}", record);
                log.info(" Response Got Cached successfully");
            }
        } catch (Exception exception) {
         //   log.error("Error in getting value for set {} from cache : {}", setName, exception);
        }
        return record;
    }

    @Loggable
    public  String readValue(String setName, String keyName, String colName) {
        Record record = null;
        String colValue = "";
        try{
          /*  if(propertiesReader.getBooleanValue(setName)) {
                log.info("Cache is switched off for key name : {}", setName);
                return null;
            }*/
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            record = client.get(aeroSpikeConfig.clientPolicy.readPolicyDefault, key);
            if( record != null && record.bins.size()!=0) {
                colValue = (String)record.getValue(colName);
                System.out.println("----------------------Response coming from cache----------------------------");
            }
            return colValue;
        } catch (Exception exception) {
            //   log.error("Error in getting value for set {} from cache : {}", setName, exception);
        }
        return colValue;
    }

    @Loggable
    public String readAfterCompression(String setName, String keyName, String colName) {
        Record record = null;
        String colValue = "";
        try{
        /*    if(propertiesReader.getBooleanValue(setName)) {
                log.info("Cache is switched off for cache name : {}", setName);
                return null;
            }*/
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            record = client.get(aeroSpikeConfig.clientPolicy.readPolicyDefault, key);

            if(!(record == null) && (record.bins.size() != 0)) {
                colValue =  ioCompressUtil.decompressString((byte[])record.getValue(colName));
             //   log.debug(" Cached Value Coming From DB : {}", colValue);
                log.info("Response Got Cached successfully ");
            }
            return colValue;
        } catch (Exception e) {
         //   log.error("Error in getting value for Set {} from cache : {}", setName, e);
        }
        return colValue;
    }

    @Loggable
    public Record read(String setName, String keyName, String bin) {
        try {
         /*   if(propertiesReader.getBooleanValue(setName)) {
                log.info("Cache is switched off for cache name : {}", setName);
                return null;
            }*/
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            Record record = client.get(aeroSpikeConfig.clientPolicy.readPolicyDefault, key, bin);
            if(record.bins.size()!=0) {
              //  log.debug(" Cached Value Coming From DB : {}", record);
                log.info("Response Got Cached successfully  ");

            }
            return record;
        }catch(Exception e){
         //   log.error("Error in getting value from Set {}  for keyName {} from cache :{}", setName, keyName, e);
            return null;
        }
    }

    @Loggable
    public Record[] read(String setName, List<String> keyNames) {
        try {
            int size = keyNames.size();
            Key[] keys = new Key[size];
            for (int i = 0; i < size; i++) {
                keys[i] = new Key(AerospikeConfParams.getNameSpace(), setName, i + 1);
            }
            Record[] record = client.get(aeroSpikeConfig.clientPolicy.batchPolicyDefault, keys);
            if(record.length!= 0 ) {
              //  log.debug(" Cached Value Coming From DB : {}", record);
                log.info("Response Got Cached successfully ");
            }
            return record;
        }catch(Exception e){
          //  log.error("Error in getting value from Set {} from cache: {} ", setName,  e);
            return null;
        }
    }

    @Loggable
    public Record read(String setName, String keyName, String[] binArray) {
        try {
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            Record record = client.get(aeroSpikeConfig.clientPolicy.readPolicyDefault, key, binArray);
            if(record.bins.size()!=0) {
              //  log.debug(" Cached Value Coming From DB : {}", record);
                log.info("Response Got Cached successfully ");
            }
            return record;
        }catch (Exception e){
        //    log.error("Error in getting value from Set {}  for Key {} from cache :{}", setName,  e);
            return  null;
        }
    }

    @Loggable
    public void delete(String setName, String keyName) {
        try {
            Key key = new Key(AerospikeConfParams.getNameSpace(), setName, keyName);
            client.delete(aeroSpikeConfig.clientPolicy.writePolicyDefault, key);
            log.info(" Key  Deleted from Set");
        }catch(Exception e){
        //    log.error("Error in deleting  Key {} from set {} in cache :{}", keyName, setName,  e);
        }
    }

    @Loggable
    public void delete(String setName){

        try {
            ScanPolicy scanPolicy = new ScanPolicy();
            client.scanAll(scanPolicy, AerospikeConfParams.getNameSpace(), setName, new ScanCallback() {

                public void scanCallback(Key key, Record record) throws AerospikeException {
                    client.delete(new WritePolicy(), key);
                    count++;
                }
            }, new String[] {});
            log.info("Deleted "+ count + " records from set " + setName);
        } catch (AerospikeException e) {
            int resultCode = e.getResultCode();
            log.info(ResultCode.getResultString(resultCode));
            log.debug("Error details: ", e);
        }
    }
/*
	@Loggable
	public Record increment(String set, String keyName, String binName) {
		Key key = new Key(aeroSpikeConfig.nameSpace, set, keyName);
		Bin bin = new Bin(binName, 1);
		return client.operate(aeroSpikeConfig.clientPolicy.writePolicyDefault, key, Operation.add(bin));
	}

	@Loggable
	public Record multiOps(String set, String keyName, Operation[] operations) {
		Key key = new Key(aeroSpikeConfig.nameSpace, set, keyName);
		Record record = client.operate(null, key, operations);
		return record;
	}
	@Loggable
	public Map<String, Long> getRateLimit(String keyName, long ttl, int limit) {
		Key key = new Key(aeroSpikeConfig.nameSpace, "ratelimit", keyName);
		Map<String, Long> result =
				(Map<String, Long>) client.execute(aeroSpikeConfig.clientPolicy.writePolicyDefault, key, "ratelimit_udf", "checkRateLimit", Value.get(ttl), Value.get(limit));
		return result;
	}*/


}