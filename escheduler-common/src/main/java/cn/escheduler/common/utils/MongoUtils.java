package cn.escheduler.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.github.vincentrussell.query.mongodb.sql.converter.QueryConverter;
import com.github.vincentrussell.query.mongodb.sql.converter.QueryResultIterator;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description mongo工具类
 * @Author chongzi
 * @Date 2019/1/9 10:36
 * @Param https://www.cnblogs.com/sa-dan/p/6836055.html
 * @return
 **/
public class MongoUtils {

	private static MongoUtils instance = null;

	public MongoUtils(){}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new MongoUtils();
		}
	}

	public static MongoUtils getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	private  MongoClient mongoClient = null;

	public MongoUtils(String server, String port, String username, String password, String databaseName, String tableName){
		String[] servers = Convert.toStrArray(server);
		Integer[] ports = Convert.toIntArray(port);
		if(servers.length > 1){
			// 集群有密码
			List<ServerAddress> seeds = new ArrayList();
			for (int i = 0; i < servers.length; i++) {
				if(ports.length > 1){
					ServerAddress serverAddress = new ServerAddress(servers[i], ports[i]);
					seeds.add(serverAddress);
				}else{
					ServerAddress serverAddress = new ServerAddress(servers[i], Integer.parseInt(port));
					seeds.add(serverAddress);
				}
			}
			char[] passwords = password.toCharArray();
			MongoCredential mc = MongoCredential.createCredential(username, databaseName, passwords);
			List<MongoCredential> credentialList = new ArrayList();
			credentialList.add(mc);
			mongoClient = new MongoClient(seeds,credentialList);
		}else{
			if(StrUtil.isNotEmpty(username) && StrUtil.isNotEmpty(password)){
				// 单机有密码
				String uri = String.format("mongodb://%s:%s@%s/%s",username,password, server, databaseName+"."+tableName);
				MongoClientURI mongoClientURI = new MongoClientURI(uri);
				mongoClient = new MongoClient(mongoClientURI);
			}else if(StrUtil.isNotEmpty(databaseName) && StrUtil.isNotEmpty(tableName)){
				// 单机没密码
				String uri = String.format("mongodb://%s/%s", server, databaseName+"."+tableName);
				MongoClientURI mongoClientURI = new MongoClientURI(uri);
				mongoClient = new MongoClient(mongoClientURI);
			}else{
				// 单机没密码
				String uri = String.format("mongodb://%s:%d",server, Integer.valueOf(port));
				MongoClientURI mongoClientURI = new MongoClientURI(uri);
				mongoClient = new MongoClient(mongoClientURI);
			}
		}
	}
	/**
	 * @Description 单机没密码
	 * @Author chongzi
	 * @Date 2019/1/9 14:34
	 * @Param
	 * @return
	 **/
	/*public MongoUtils(String server,int port)
	{
		String uri = String.format("mongodb://%s:%d",server, port);
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(mongoClientURI);
	}*/

	/**
	 * @Description 单机有密码
	 * @Author chongzi
	 * @Date 2019/1/9 14:34
	 * @Param
	 * @return
	 **/
	/*public MongoClient MongoUtils(String username ,String password, String server,int port)
	{
		String uri = String.format("mongodb://%s:%s@%s:%d",username,password, server,port);
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(mongoClientURI);
		return mongoClient;
	}*/

	/**
	 * @Description 单机没密码
	 * @Author chongzi
	 * @Date 2019/1/9 14:34
	 * @Param 
	 * @return 
	 **/
	/*public MongoUtils(String server,String dbName,String collName)
	{
		String uri = String.format("mongodb://%s/%s", server, dbName+"."+collName);
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(mongoClientURI);
	}*/
	/**
	 * @Description 单机有密码
	 * @Author chongzi
	 * @Date 2019/1/9 14:34
	 * @Param 
	 * @return 
	 **/
	/*public MongoUtils(String username ,String password, String server,String dbName,String collName)
	{
		String uri = String.format("mongodb://%s:%s@%s/%s",username,password, server, dbName+"."+collName);
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(mongoClientURI);
	}*/

	/**
	 * @Description 集群有密码
	 * @Author chongzi
	 * @Date 2019/1/9 14:42
	 * @Param 
	 * @return 
	 **/
	/*public MongoUtils(String username ,String password, String[] servers,Integer[] ports,String dbName)
	{
		List<ServerAddress> seeds = new ArrayList();
		for (int i = 0; i < servers.length; i++) {
			ServerAddress serverAddress = new ServerAddress(servers[i], ports[i]);
			seeds.add(serverAddress);
		}
		char[] passwords = password.toCharArray();
		MongoCredential mc = MongoCredential.createCredential(username, dbName, passwords);
		List<MongoCredential> credentialList = new ArrayList();
		credentialList.add(mc);
		mongoClient = new MongoClient(seeds,credentialList);
	}*/


	/**
	 * 获取DB实例 - 指定DB
	 *
	 * @param dbName
	 * @return
	 */
	public MongoDatabase getDB(String dbName) {
		if (dbName != null && !"".equals(dbName)) {
			MongoDatabase database = mongoClient.getDatabase(dbName);
			return database;
		}
		return null;
	}

	/**
	 * 获取collection对象 - 指定Collection
	 *
	 * @param collName
	 * @return
	 */
	public MongoCollection<Document> getCollection(String dbName, String collName) {
		if (null == collName || "".equals(collName)) {
			return null;
		}
		if (null == dbName || "".equals(dbName)) {
			return null;
		}
		MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
		return collection;
	}

	/**
	 * 查询DB下的所有表名
	 */
	public List<String> getAllCollections(String dbName) {
		MongoIterable<String> colls = getDB(dbName).listCollectionNames();
		List<String> _list = new ArrayList<String>();
		for (String s : colls) {
			_list.add(s);
		}
		return _list;
	}

	/**
	 * 获取所有数据库名称列表
	 *
	 * @return
	 */
	public List<String> getAllDBNames() {
		List<String> databases = new LinkedList<>();
		MongoIterable<String> s = mongoClient.listDatabaseNames();
		Iterator it = s.iterator();
		while (it.hasNext()){
			databases.add(it.next().toString());
		}
		return databases;
	}

	/**
	 * 删除一个数据库
	 */
	public void dropDB(String dbName) {
		getDB(dbName).drop();
	}

	/**
	 * 查找对象 - 根据主键_id
	 *
	 * @param coll
	 * @param id
	 * @return
	 */
	public Document findById(MongoCollection<Document> coll, String id) {
		ObjectId _idobj = null;
		try {
			_idobj = new ObjectId(id);
		} catch (Exception e) {
			return null;
		}
		Document myDoc = coll.find(Filters.eq("_id", _idobj)).first();
		return myDoc;
	}

	/** 统计数 */
	public int getCount(MongoCollection<Document> coll) {
		int count = (int) coll.count();
		return count;
	}

	/** 条件查询 */
	public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
		return coll.find(filter).iterator();
	}

	/** 分页查询 */
	public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
		Bson orderBy = new BasicDBObject("_id", 1);
		return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
	}


	/**
	 * 通过ID删除
	 *
	 * @param coll
	 * @param id
	 * @return
	 */
	public int deleteById(MongoCollection<Document> coll, String id) {
		int count = 0;
		ObjectId _id = null;
		try {
			_id = new ObjectId(id);
		} catch (Exception e) {
			return 0;
		}
		Bson filter = Filters.eq("_id", _id);
		DeleteResult deleteResult = coll.deleteOne(filter);
		count = (int) deleteResult.getDeletedCount();
		return count;
	}

	/**
	 * FIXME
	 *
	 * @param coll
	 * @param id
	 * @param newdoc
	 * @return
	 */
	public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
		ObjectId _idobj = null;
		try {
			_idobj = new ObjectId(id);
		} catch (Exception e) {
			return null;
		}
		Bson filter = Filters.eq("_id", _idobj);
		// coll.replaceOne(filter, newdoc); // 完全替代
		coll.updateOne(filter, new Document("$set", newdoc));
		return newdoc;
	}

	public void dropCollection(String dbName, String collName) {
		getDB(dbName).getCollection(collName).drop();
	}

	/**
	 * @Description 获取连接状态
	 * @Author chongzi
	 * @Date 2019/1/19 14:25
	 * @Param
	 * @return
	 **/
	public boolean getConnectionState() {
		boolean res = false;
		try {
			mongoClient.getConnectPoint();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return res;
	}

	/**
	 * @Description 获取总数
	 * @Author chongzi
	 * @Date 2019/1/19 14:58
	 * @Param 
	 * @return 
	 **/
	public Long getCount(String databaseName,String tableName){
		Long result =  null;
		try {
			MongoCollection<Document> coll = getCollection(databaseName,tableName);
			result = Long.valueOf(getCount(coll));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * @Description 获取sql数据
	 * @Author chongzi
	 * @Date 2019/1/19 14:58
	 * @Param
	 * @return
	 **/
	public List<Document> getSqlData(String databaseName,String sql){
		List<Document> documents =  null;
		try {
			MongoDatabase mongoDatabase = mongoClient.getDatabase(CommonUtils.replaceDateFormat(databaseName));
			QueryConverter queryConverter = new QueryConverter(CommonUtils.replaceDateFormat(sql));
			QueryResultIterator<Document> findIterable = queryConverter.run(mongoDatabase);
			documents = Lists.newArrayList(findIterable);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return documents;
	}

	/**
	 * 关闭Mongodb
	 */
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
		}
	}

	/**
	 * 有用户名密码的方式连接mongoDB（MongoCredential方式）
	 * @param mongodbAddr
	 * @param databaseName
	 * @return
	 */
	/*public static MongoClient getMongoClientByCredent(String mongodbAddr, String databaseName){
		MongoClient mongoClient;
		Properties p = TDTFile.getProperAddr("db.properties");
		String user = p.getProperty("username");
		String pswd = p.getProperty("password");
		List<ServerAddress> serverAddrList = new ArrayList<ServerAddress>();
		ServerAddress serverAddress = new ServerAddress(mongodbAddr);
		serverAddrList.add(serverAddress);
		List<MongoCredential> credentialList = new ArrayList<MongoCredential>();
		MongoCredential credential = MongoCredential.createCredential(user, databaseName, pswd.toCharArray());
		credentialList.add(credential);
		mongoClient = new MongoClient(serverAddrList, credentialList);
		return mongoClient;
	}*/

	/**
	 * 有用户名密码的方式连接mongoDB（URI方式）
	 * @param mongodbAddr
	 * @param databaseName
	 * @return
	 */
	/*public static MongoClient getMongoClientByURI(String mongodbAddr, String databaseName){
		MongoClient mongoClient;
		Properties p = TDTFile.getProperAddr("db.properties");
		String user = p.getProperty("username");
		String pswd = p.getProperty("password");
		//System.out.println(user + "," + pswd);
		String uri = String.format("mongodb://%s:%s@%s/%s", user, pswd, mongodbAddr, databaseName);
		System.out.println(uri);
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(mongoClientURI);
		return mongoClient;
	}*/


}
