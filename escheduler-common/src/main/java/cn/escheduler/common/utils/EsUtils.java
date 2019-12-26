package cn.escheduler.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Description es工具类
 * @Author chongzi
 * @Date 2019/1/18 16:54
 **/
public class EsUtils {

    private static EsUtils instance = null;

    public EsUtils(){}

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new EsUtils();
        }
    }

    public static EsUtils getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    private static RestHighLevelClient client = null;

    /**
     * @Description http方式创建索引
     * @Author chongzi
     * @Date 2019/1/19 13:39
     * @Param
     * @return
     **/
    public void httpCreateIndex(String hostname,String port,String index,int number_of_shards,int number_of_replicas) {
        try {
            String json = "{\"settings\": { \"index\": { \"number_of_shards\": \"" + number_of_shards + "\", \"number_of_replicas\": \"" + number_of_replicas + "\" }}}";
            HttpRequest.put("http://"+hostname+":"+port+"/"+index)
                    .body(json)
                    .execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description http方式删除索引
     * @Author chongzi
     * @Date 2019/1/19 13:39
     * @Param
     * @return
     **/
    public void httpDeleteIndex(String hostname,String port,String index) {
        try {
            HttpRequest.delete("http://"+hostname+":"+port+"/"+index).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description http方式创建别名
     * @Author chongzi
     * @Date 2019/1/19 13:39
     * @Param
     * @return
     **/
    public void httpAddAlias(String hostname,String port,String index,String alias) {
        try {
            String json = "{\"actions\": [{ \"add\": { \"index\": \"" + index + "\", \"alias\": \"" + alias + "\" }}]}";
            HttpRequest.post("http://"+hostname+":"+port+"/_aliases/")
                    .body(json)
                    .execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description http方式删除别名
     * @Author chongzi
     * @Date 2019/1/19 13:39
     * @Param
     * @return
     **/
    public void httpRemoveAlias(String hostname,String port,String index,String alias) {
        try {
            String json = "{\"actions\": [{ \"remove\": { \"index\": \"" + index + "\", \"alias\": \"" + alias + "\" }}]}";
            HttpRequest.post("http://"+hostname+":"+port+"/_aliases/")
                    .body(json)
                    .execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description 获取总数
     * @Author chongzi
     * @Date 2019/1/19 14:28
     * @Param
     * @return
     **/
    public Long httpCount(String server,String port,String databaseName,String tableName){
        Long result = null;
        try {
            databaseName = CommonUtils.replaceDateFormat(databaseName);
            String resultStr = null;
            if(StrUtil.isEmpty(tableName) || tableName.equals("#all")){
                resultStr = HttpUtil.get("http://"+server+":"+port+"/"+databaseName+"/_count/");
            }else{
                resultStr = HttpUtil.get("http://"+server+":"+port+"/"+databaseName+"/"+tableName+"/_count/");
            }
            JSON json = JSONUtil.parse(resultStr);
            result = Long.valueOf(json.getByPath("count").toString());
            if(result == 0){
                result = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description 获取连接状态
     * @Author chongzi
     * @Date 2019/1/19 14:25
     * @Param 
     * @return 
     **/
    public boolean getConnectionState(String hostname,String port) {
        boolean res = false;
        ArrayList<HttpHost> hosts = Lists.newArrayList();
        Arrays.stream(hostname.split(",")).forEach(host -> {
            hosts.add(new HttpHost(host, Integer.valueOf(port), "http"));
        });
        RestHighLevelClient client =
                new RestHighLevelClient(RestClient.builder(
                        hosts.toArray(new HttpHost[0])));
        try {
             res = client.ping(RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @Description 获取总数
     * @Author chongzi
     * @Date 2019/1/19 14:28
     * @Param 
     * @return 
     **/
    public Long getCount(String server,String port,String databaseName,String tableName) throws IOException {
        Long result =  null;
        ArrayList<HttpHost> hosts = Lists.newArrayList();
        Arrays.stream(server.split(",")).forEach(host -> {
            hosts.add(new HttpHost(host, Integer.valueOf(port), "http"));
        });
        client =
                new RestHighLevelClient(RestClient.builder(
                        hosts.toArray(new HttpHost[0])));
        try {
            databaseName = CommonUtils.replaceDateFormat(databaseName);

            SearchRequest searchRequest = new SearchRequest(databaseName);
            searchRequest.types(tableName);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            result = hits.getTotalHits();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
        return result;
    }
}
