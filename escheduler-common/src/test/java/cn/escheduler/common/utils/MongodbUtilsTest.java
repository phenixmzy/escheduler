package cn.escheduler.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.vincentrussell.query.mongodb.sql.converter.QueryConverter;
import com.github.vincentrussell.query.mongodb.sql.converter.QueryResultIterator;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.Test;

import java.util.List;

/**
 * 本类主要用于检测jdbc连接是否成功
 */
public class MongodbUtilsTest {

    //private static String ip = "34.236.225.15";
    private static String ip = "52.201.39.240";
    private static int port = 27017;
    private static String dbName = "recommender_online";
    private static String collName = "result_detail_page_gtq_gb";
    private static String userName = "bigdevelp_user";
    private static String pwd = "ePVxXNZKuNbAzNOH";

    /**
     * @Description 集群的连接方式
     * @Author chongzi
     * @Date 2019/1/9 10:35
     * @Param 
     * @return 
     **/
    @Test
    public void cluster(){
        try{
            MongoUtils mongoUtil = new MongoUtils(ip, String.valueOf(port),userName,pwd,dbName,collName);
            MongoDatabase mongoDatabase = mongoUtil.getDB(dbName);
            MongoCollection<Document> mcd = mongoDatabase.getCollection(collName);
            //MongoCollection<Document> coll = MongoUtils.instance.getCollection(dbName, collName);
            System.out.println("+++++++++++++++"+mcd.count());
           /* List<String> list = mongoUtil.getAllCollections(databases);
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }*/
            mongoUtil.close();
            System.out.println("Connect to database successfully");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    @Test
    public void getCount() throws Exception {
        //MongoUtils mongoUtil = new MongoUtils(ip, String.valueOf(port),userName,pwd,dbName,collName);
        //MongoUtils mongoUtil = new MongoUtils(ip, String.valueOf(port),null,null,null,null);
        MongoUtils mongoUtil = new MongoUtils("52.200.167.98,52.201.39.240", String.valueOf(port),"recommender_read_user","1w7db7UIh6","recommender_online","manual_intervention_rank_index");
        MongoCollection<Document> coll = mongoUtil.getCollection("recommender_online","manual_intervention_rank_index");
        int res = mongoUtil.getCount(coll);
        System.out.println("res========="+res);
    }

    @Test
    public void getConnectionState() throws Exception {
        MongoUtils mongoUtil = new MongoUtils(ip, String.valueOf(port),userName,pwd,dbName,null);
        System.out.println( mongoUtil.getConnectionState());
    }

    @Test
    public void getAllDBNames() throws Exception {
        MongoUtils mongoUtil = new MongoUtils(ip, String.valueOf(port),userName,pwd,dbName,collName);
        List<String> databases = mongoUtil.getAllDBNames();
        for (int i = 0; i < databases.size(); i++) {
            System.out.println(databases.get(i));
        }
    }

    @Test
    public void getCountBySql() throws Exception {
        /*IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(ip,port, false))
                .build();*/
        //MongodStarter starter = MongodStarter.getDefaultInstance();
        //MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        //MongodProcess mongodProcess = mongodExecutable.start();
        //MongoClient mongoClient = new MongoClient(ip,port);

        String uri = String.format("mongodb://%s:%d",ip, Integer.valueOf(port));
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        MongoClient mongoClient = new MongoClient(mongoClientURI);

        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection mongoCollection = mongoDatabase.getCollection(collName);

        QueryConverter queryConverter = new QueryConverter("select * from "+collName);
        /*long count  = queryConverter.run(mongoDatabase);
        System.out.println(count);*/
        QueryResultIterator<Document> findIterable = queryConverter.run(mongoDatabase);
        List<Document> documents = Lists.newArrayList(findIterable);
        System.out.println(documents.size());

        mongoClient.close();
        //mongodProcess.stop();
        //mongodExecutable.stop();
    }

    @Test
    public void getCountBySql2() throws Exception {

//        String uri = String.format("mongodb://%s:%s@%s/%s","recommender_read_user","1w7db7UIh6", "52.201.39.240", "recommender_online.result_gb_email_create");
//        MongoClientURI mongoClientURI = new MongoClientURI(uri);
//        MongoClient mongoClient = new MongoClient(mongoClientURI);
//
//
//        MongoDatabase mongoDatabase = mongoClient.getDatabase("recommender_online");
//        MongoCollection mongoCollection = mongoDatabase.getCollection("result_gb_email_create");
//
//        QueryConverter queryConverter = new QueryConverter("select count(*) from result_gb_email_create");
//
//        long count  = queryConverter.run(mongoDatabase);
//        System.out.println(count);
//        /*QueryConverter queryConverter = new QueryConverter("select * from result_gb_email_create where sku='276832201' and date = '20190411'");
//        QueryConverter queryConverter = new QueryConverter("select * from zaful_result_list_pc_c where country='global' and date = '20190411'");
//        QueryResultIterator<Document> findIterable = queryConverter.run(mongoDatabase);
//        List<Document> documents = Lists.newArrayList(findIterable);
//        System.out.println(documents.size());*/
//
//        mongoClient.close();
        //mongodProcess.stop();
        //mongodExecutable.stop();

//        String uri = String.format("mongodb://%s:%s@%s/%s","recommender_read_user","1w7db7UIh6", "52.201.39.240", "recommender_online.manual_intervention_rank_index");
//        MongoClientURI mongoClientURI = new MongoClientURI(uri);
//        MongoClient mongoClient = new MongoClient(mongoClientURI);
//        MongoDatabase mongoDatabase = mongoClient.getDatabase("recommender_online");
//        QueryConverter queryConverter = new QueryConverter("select count(*) from manual_intervention_rank_index");
//        long count  = queryConverter.run(mongoDatabase);
//        System.out.println(count);
//        mongoClient.close();

        MongoUtils mongoUtil = new MongoUtils("52.201.39.240", String.valueOf(port),"recommender_read_user","1w7db7UIh6","recommender_online","manual_intervention_rank_index");
        /*long res = mongoUtil.getSqlData("recommender_online","select count(*) from manual_intervention_rank_index");
        System.out.println(res);*/
        /*List<Document> result = mongoUtil.getSqlData("recommender_online","select * from manual_intervention_rank_index");
        System.out.println("execute sql result : "+ JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue));*/
        System.out.println( mongoUtil.getConnectionState());

        /*MongoCollection<Document> coll = mongoUtil.getCollection("recommender_online","tianchi_dm_zf_pcm_total_station_goods_feature_fact_d");
        MongoCursor<Document> cursor1 = coll.find(Filters.eq("dt", "2019-09-09")).skip((10 - 1) * 1).limit(1).iterator();
        while(cursor1.hasNext()){
            Document sd = cursor1.next();
            System.out.println(sd.toJson());
        }*/

        //Bson filter
    }

}