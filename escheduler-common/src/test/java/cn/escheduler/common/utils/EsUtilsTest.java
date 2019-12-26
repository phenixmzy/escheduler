package cn.escheduler.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 本类主要用于检测es连接是否成功
 */
public class EsUtilsTest {

    private static final String ES_INDEX = "sentinel-dy-realtime-data-query-service-bak";
    private static final String ES_DEFAULT_TYPE = "default";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void selectCount() throws Exception {
        RestHighLevelClient client =
                new RestHighLevelClient(RestClient.builder(
                        new HttpHost("18.205.134.225", 9200, "http")));

        SearchRequest searchRequest = new SearchRequest(ES_INDEX);
        searchRequest.types(ES_DEFAULT_TYPE);
        //SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //BoolQueryBuilder qb = QueryBuilders.boolQuery();
        //qb.must(QueryBuilders.termQuery("app.keyword", "dy-realtime-data-query-service"));
        //searchSourceBuilder.query(qb);
        //searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            System.out.println(hits.getTotalHits());
            SearchHit[] searchHits = searchResponse.getHits().getHits();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ping() throws Exception {
        RestHighLevelClient client =
                new RestHighLevelClient(RestClient.builder(
                        new HttpHost("18.215.206.192", 9202, "http")));
        boolean res = client.ping(RequestOptions.DEFAULT);
        System.out.println(res);
        /*MainResponse response = client.info();
        ClusterName clusterName = response.getClusterName();
        System.out.println(clusterName);*/
    }

    @Test
    public void createIndex() throws Exception {
        RestHighLevelClient client =
                new RestHighLevelClient(RestClient.builder(
                        new HttpHost("100.26.74.93", 9202, "http"),
                        new HttpHost("100.26.77.0", 9202, "http"),
                        new HttpHost("18.215.206.192", 9202, "http")));
        try {
            // 1.创建索引名
            CreateIndexRequest request = new CreateIndexRequest("dy-gb-goods-info-test");

            // 2.索引setting配置
            request.settings(Settings.builder().put("index.number_of_shards",3)
                    .put("index.number_of_replicas", 1) // 副本数
                    .put("analysis.analyzer.default.tokenizer","standard"));
            // 3.设置索引的mapping

            // 设置索引别名
            request.alias(new Alias("lab1"));

            // 5.发送请求
            // 5.1同步方式
            CreateIndexResponse response = client.indices().create(request);

            // 处理响应
            boolean acknowledged = response.isAcknowledged();
            boolean shardsAcknowledged = response.isShardsAcknowledged();

            System.out.println("请求结果---------------");
            System.out.println("acknowledged:"+acknowledged);
            System.out.println("shardsAcknowledged:"+shardsAcknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void list(){
        String hostname = "10.4.4.87,10.4.4.88,10.4.4.89,10.4.6.105,10.4.6.106,10.4.6.107";
        Integer port = 9202;
        ArrayList<HttpHost> hosts = Lists.newArrayList();

        Arrays.stream(hostname.split(",")).forEach(host -> {
            hosts.add(new HttpHost(host, port, "http"));
        });
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(hosts.toArray(new HttpHost[0])));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.must(QueryBuilders.termQuery("jobInfoId", "1"));
        searchSourceBuilder.query(qb);
        SearchRequest searchRequest = new SearchRequest("dy-quality-measure-details");
        searchRequest.types("default");
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            System.out.println(hits.getTotalHits());
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                System.out.println(sourceAsMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void httpCreateIndex() throws Exception {
        try {
            String index = "dy-gb-goods-info-2019-01-17";
            int number_of_shards = 3;
            int number_of_replicas = 1;
            String json = "{\"settings\": { \"index\": { \"number_of_shards\": \"" + number_of_shards + "\", \"number_of_replicas\": \"" + number_of_replicas + "\" }}}";
            String result = HttpRequest.put("http://100.26.77.0:9202/"+index)
                    .body(json)
                    .execute().body();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void httpDeleteIndex() throws Exception {
        try {
            String index = "dy-gb-goods-info-test-gb";
            HttpRequest.delete("http://100.26.77.0:9202/"+index)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void httpRemoveAlias() throws Exception {
        try {
            String index = "dy-gb-goods-info-2019-01-17";
            String alias = "dy-gb-goods-info";
            String json = "{\"actions\": [{ \"remove\": { \"index\": \"" + index + "\", \"alias\": \"" + alias + "\" }}]}";
            String result = HttpRequest.post("http://100.26.77.0:9202/_aliases/")
                    .body(json)
                    .execute().body();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void httpAddAlias() throws Exception {
        try {
            String index = "dy-gb-goods-info-2019-01-17";
            String alias = "dy-gb-goods-info";
            String json = "{\"actions\": [{ \"add\": { \"index\": \"" + index + "\", \"alias\": \"" + alias + "\" }}]}";
            String result = HttpRequest.post("http://100.26.77.0:9202/_aliases/")
                    .body(json)
                    .execute().body();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void httpCount() throws Exception {
        try {
            String index = "dy-gb-goods-info-2019-01-23";
            String type = "default";
            String result = HttpUtil.get("http://100.26.77.0:9202/"+index+"/"+type+"/_count/");
            System.out.println(result);
            JSON json = JSONUtil.parse(result);
            System.out.println(json.getByPath("count"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DateTime BeforeYesterday = DateUtil.offsetDay(DateUtil.date(), -2);
        System.out.println(BeforeYesterday);
    }

}