package cn.escheduler.common.utils;

import cn.escheduler.common.utils.hbase.HBaseSqlContants;
import cn.escheduler.common.utils.hbase.entity.HResult;
import cn.escheduler.common.utils.hbase.sql.visitor.SelectSqlVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.SQLSyntaxErrorException;
import java.util.*;


/**
 * @Description 用列值过滤数据的时候,扫描必须将要过滤的列名字加入进去,或者直接不加Column这样默认是显示所有数据
 * @Author chongzi
 * @Date 2018/11/15 14:28
 * @Param
 * @return
 **/
public class HbaseUtils {

    private static final Logger log = LoggerFactory.getLogger(HbaseUtils.class);

    private static HbaseUtils instance = null;

    public HbaseUtils(){}

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new HbaseUtils();
        }
    }

    public static HbaseUtils getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    private static Configuration conf = null;
    private static Connection connection;
    protected static Admin admin;


    public HbaseUtils(String server, String port){
        try {
            if (conf == null) {
                conf = HBaseConfiguration.create();
            }
            conf.set("hbase.zookeeper.quorum", server);
            conf.set("hbase.zookeeper.property.clientPort", port);
            conf.set("hbase.client.retries.number", "3");
            conf.set("hbase.rpc.timeout", "50000");
            conf.set("hbase.client.operation.timeout", "10000");
            conf.set("hbase.client.scanner.timeout.period", "10000");
            connection = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("init HBase Configuration Exception: " + e.getMessage());
        }
    }

    /**
     * @Description 获取连接状态
     * @Author chongzi
     * @Date 2019/1/19 14:25
     * @Param tableName
     * @return
     **/
    public boolean getConnectionState(String tableName) {
        boolean res = false;
        try {
            if(admin.tableExists(TableName.valueOf(tableName))) {
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return res;
    }

    /**
     * 创建 HBase 表
     * @param tableName
     * @param familys
     * @throws IOException
     */
    public static void creatTable(String tableName,String... familys) throws IOException {
        if(admin.tableExists(TableName.valueOf(tableName))) {
            log.debug("table already exists");
        } else {
            HTableDescriptor hTableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
            for (String family : familys) {
                hTableDescriptor.addFamily(new HColumnDescriptor(family));
            }
            admin.createTable(hTableDescriptor);
            log.debug("create table:" + tableName + "  success");
        }
     }

    /**
     * ============================================================================================
     * pre-Creating Regions 创建Hbase Table的时候做预分区处理
     */
    public static boolean createTable(String tableName,HTableDescriptor hTableDescriptor,byte[][]splits) throws Exception {
        if(admin.tableExists(TableName.valueOf(tableName))) {
            log.debug("table already exists");
            return false;
        } else {
            admin.createTable(hTableDescriptor,splits);
            log.debug("create table:" + tableName + "  success");
        }
        return true;
    }

    /**
     * 得到Regions 分区 只能针对那种数字型的RowKey
     * @param startKeys
     * @param endKey
     * @param numRegions
     * @return
     */
    public static byte[][] getHexSplits(String startKeys,String endKey,int numRegions){
        //start:001 end:1000 numReginon10
        byte[][]splits=new byte[numRegions-1][];
        BigInteger lowestKey=new BigInteger(startKeys,16);
        BigInteger highestKey=new BigInteger(endKey,16);
        BigInteger range=highestKey.subtract(lowestKey);
        BigInteger regionIncrement=range.divide(BigInteger.valueOf(numRegions));
        lowestKey=lowestKey.add(regionIncrement);
        for(int i=0;i< numRegions-1;i++){
            BigInteger key=lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            byte[]b =String.format("%016x",key).getBytes();
            splits[i]=b;
        }
        return splits;
    }
    /**
     * ============================================================================================
     */

    /**
     * 删除 Hbase 表
     * @param tableName
     * @throws IOException
     */
    public static void deleteTable(String tableName) throws IOException {
        TableName table_name = TableName.valueOf(tableName);
        admin.disableTable(table_name);
        admin.deleteTable(table_name);
        log.debug("delete table:" + tableName + "  success");
    }

    /**
     * 清空表
     * @param tableName
     * @throws IOException
     */
    public static void truncateTable(String tableName) throws IOException {
        TableName table_name = TableName.valueOf(tableName);
        admin.truncateTable(table_name,false);
        log.debug("delete table:" + tableName + "  success");
    }

    /**
     *  增加列族
     * @param tableName
     * @param FamilyName
     * @throws IOException
     */
    public static void addCoulumFamily(String tableName,String FamilyName) throws IOException {
        TableName tableName1=TableName.valueOf(tableName);
        if(admin.tableExists(tableName1)) {
            admin.disableTable(tableName1);
            HTableDescriptor tableDescriptor=admin.getTableDescriptor(tableName1);
            tableDescriptor.addFamily(new HColumnDescriptor(FamilyName));
            admin.modifyTable(tableName1,tableDescriptor);
            admin.enableTable(tableName1);
        }
    }


    /**
     * 查看已有表
     * @throws IOException
     */
    public static void listTables() throws IOException {
        HTableDescriptor hTableDescriptors[] = admin.listTables();
        for(HTableDescriptor hTableDescriptor :hTableDescriptors){
            System.out.println(hTableDescriptor.getNameAsString());
        }
    }

    /**
     *  删除列族
     * @param tableName
     * @param FamilyName
     * @throws IOException
     */
    public static void deleteCoulumFamily(String tableName,String FamilyName) throws IOException {
        TableName tableName1=TableName.valueOf(tableName);
        if(admin.tableExists(tableName1)) {
            admin.disableTable(tableName1);
            HTableDescriptor tableDescriptor=admin.getTableDescriptor(tableName1);
            tableDescriptor.removeFamily(FamilyName.getBytes());
            admin.modifyTable(tableName1,tableDescriptor);
            admin.enableTable(tableName1);
        }
    }

    /**
     * Hbase 插入数据
     * @param tableName
     * @param rowKey
     * @param family
     * @param qualifier
     * @param value
     * @throws IOException
     */
    public static void addRecord (String tableName, String rowKey, String family,
                                  String qualifier, String value) throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        table.put(put);
        log.debug("insert recored " + rowKey + " to table " + tableName + " success.");
        table.close();
    }

    public static void addRecords(String tableName, List<Put> putList) throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        table.put(putList);
        table.close();
    }


    /**
     * Hbase 删除数据
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public static void delRecord (String tableName, String rowKey) throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Delete del = new Delete(rowKey.getBytes());
        table.delete(del);
        log.debug("del recored " + rowKey + " success.");
        table.close();
    }

    /**
     * 根据 RowKey 查找数据
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public static void getOneRecord (String tableName, String rowKey) throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for(Cell cell : rs.rawCells()){
            System.out.println("列簇为：" + new String(CellUtil.cloneFamily(cell)));
            System.out.println("值为："+new String(CellUtil.cloneValue(cell)));
        }
        table.close();
    }

    /**
     * 查询表中所有数据
     * @param tableName
     * @throws IOException
     */
    public List<Result> getAllRecord (String tableName) throws IOException {
        List resultList = new ArrayList();
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Scan s = new Scan();
        s.setMaxVersions(1000);
        ResultScanner ss = table.getScanner(s);
        for(Result r : ss){
            log.debug("该表RowKey为：" + new String(r.getRow()));
            for(Cell cell : r.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为："+new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(r);
        }
        table.close();
        return resultList;
    }

    /**
     * 根据rwokey查询
     * @param tableName
     * @param rowKey
     * @return
     * @throws IOException
     */
    public  Result getRecordByRowKey(String tableName, String rowKey)
            throws IOException {
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.setMaxVersions(10);
        Result result = table.get(get);
        for(Cell cell : result.rawCells()){
            log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
            log.debug("列修饰符为："+new String(CellUtil.cloneQualifier(cell)));
            log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
        }
        table.close();
        return result;
    }

    /**
     * 获取某一列的所有数据
     * @param tableName
     * @param familyName
     * @param qualifierNameList
     * @return
     * @throws IOException
     */
    public  List<Result> getAllRecordByQualifier(String tableName, String familyName, String[] qualifierNameList)
            throws IOException {
        List resultList = new ArrayList();
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        Scan s = new Scan();
        for(String qualifier : qualifierNameList){
            s.addColumn(familyName.getBytes(), qualifier.getBytes());
        }
        ResultScanner ss = table.getScanner(s);
        for (Result r : ss){
            log.debug("该表RowKey为：" + new String(r.getRow()));
            for(Cell cell : r.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为：" + new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(r);
        }
        table.close();
        ss.close();
        return resultList;
    }

    /**
     *  列值过滤器--SingleColumnValueFilter根据列值过滤器查找行元素
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param value
     * @param fiedlist fiedlist为需要显示的字段 如果为null表示显示所有列
     * @return
     * @throws IOException
     */
    public  List<Result> getColumnResultbyFilter(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,String value,String[]fiedlist) throws IOException {
        List resultList = new ArrayList();
        HTable table =(HTable) connection.getTable(TableName.valueOf(tableName));
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        SingleColumnValueFilter filter = new SingleColumnValueFilter(
                familyName.getBytes(),
                qualifer.getBytes(),
                compareOp,
                new BinaryComparator(value.getBytes())
        );
        filterList.addFilter(filter);
        Scan s = new Scan();

        if(fiedlist!=null){
            for(String field:fiedlist)
            s.addColumn(familyName.getBytes(),field.getBytes());
        }
        s.setFilter(filterList);

        ResultScanner ss = table.getScanner(s);
        for (Result r : ss){
            log.debug("该表RowKey为：" + new String(r.getRow()));
            for(Cell cell : r.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为：" + new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(r);
        }
        table.close();
        ss.close();
        return resultList;
    }

    /**
     *  列值过滤器--SingleColumnValueFilter根据列值过滤器正则表达式查找行元素
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param regx
     * @param fiedlist  fiedlist为需要显示的字段 如果为null表示显示所有列
     * @return
     * @throws IOException
     */
    public  List<Result> getColumnResultbyRegxFilter(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,String regx,String[]fiedlist) throws IOException {
        RegexStringComparator regexStringComparator=new RegexStringComparator(regx);
        return getColumnResultByComparable(tableName,familyName,qualifer,compareOp,regexStringComparator,fiedlist);
    }

    /**
     *  列值过滤器--SingleColumnValueFilter根据列值过滤器匹配完整字节数组查找行元素
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param binary
     * @param fiedlist
     * @return
     * @throws IOException
     */
    public  List<Result> getColumnResultbyBinaryComparatorFilter(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,String binary,String[]fiedlist) throws IOException {
        BinaryComparator binaryComparator=new BinaryComparator(binary.getBytes());
        return getColumnResultByComparable(tableName,familyName,qualifer,compareOp,binaryComparator,fiedlist);
    }

    /**
     *   列值过滤器--SingleColumnValueFilter根据列值过滤器匹配匹配字节数组前缀查找行元素
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param binaryPriex
     * @param fiedlist
     * @return
     * @throws IOException
     */
    public  List<Result> getColumnResultbyBinaryPrefixComparatorFilter(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,String binaryPriex,String[]fiedlist) throws IOException {
        BinaryPrefixComparator prefixComparator=new BinaryPrefixComparator(binaryPriex.getBytes());
        return getColumnResultByComparable(tableName,familyName,qualifer,compareOp,prefixComparator,fiedlist);
    }

    /**
     *  .PageFilter 指定页面行数，返回对应行数的结果集。
     * @param tableName
     * @param startRows
     * @param pageNum   页面行数
     * @return
     * @throws IOException
     */
    public  List<Result> getPageResultByFilter(String tableName,String startRows,long pageNum) throws IOException {
        List resultList = new ArrayList();
        HTable table =(HTable) connection.getTable(TableName.valueOf(tableName));
        PageFilter pageFilter=new PageFilter(pageNum);
        Scan scan=new Scan();
        scan.setFilter(pageFilter);
        scan.setStartRow(startRows.getBytes());

        ResultScanner ss=table.getScanner(scan);
        for(Result result:ss){
            log.debug("该表RowKey为：" + new String(result.getRow()));
            for(Cell cell:result.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为：" + new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(result);
        }
        table.close();
        ss.close();
        return resultList;
    }

    /**
     *  列值过滤器
     *  得到数据从比较器中得到数据Result
     *  （2）比较器  ByteArrayComparable
     *      通过比较器可以实现多样化目标匹配效果，比较器有以下子类可以使用：
     *      BinaryComparator               匹配完整字节数组
     *      BinaryPrefixComparator     匹配字节数组前缀
     *      BitComparator
     *      NullComparator
     *      RegexStringComparator    正则表达式匹配
     *      SubstringComparator        子串匹配
     * @param tableName
     * @param familyName
     * @param qualifer
     * @param compareOp
     * @param comparable 比较器
     * @param fiedlist
     * @return
     * @throws IOException
     */
    public  List<Result> getColumnResultByComparable(String tableName, String familyName ,String qualifer, CompareFilter.CompareOp compareOp,ByteArrayComparable comparable,String[]fiedlist) throws IOException {
        List resultList = new ArrayList();
        HTable table =(HTable) connection.getTable(TableName.valueOf(tableName));
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);

        SingleColumnValueFilter singleColumnValueFilter=new SingleColumnValueFilter(familyName.getBytes(),qualifer.getBytes(),
                compareOp,comparable
        );
        filterList.addFilter(singleColumnValueFilter);
        Scan scan=new Scan();
        if(fiedlist!=null){
            for(String field:fiedlist)
                scan.addColumn(familyName.getBytes(),field.getBytes());
        }
        scan.setFilter(filterList);

        ResultScanner ss=table.getScanner(scan);
        for(Result result:ss){
            log.debug("该表RowKey为：" + new String(result.getRow()));
            for(Cell cell:result.rawCells()){
                log.debug("列簇为：" + new String(CellUtil.cloneFamily(cell)));
                log.debug("列修饰符为：" + new String(CellUtil.cloneQualifier(cell)));
                log.debug("值为：" + new String(CellUtil.cloneValue(cell)));
            }
            resultList.add(result);
        }
        table.close();
        ss.close();
        return resultList;
    }


    /*****************************************下面是为了统计一个Hbase表的个数的API*****************************/

    /**
     *  为一个Hbae表注册个Table注册该Coprocessor，用来统计Hbase中记录的个数
     * @param name
     * @throws IOException
     */
    public  void addCoprocessorToTable(String name) throws IOException {
        String coprocessClassName = "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
        TableName tableName=TableName.valueOf(name);
        admin.disableTable(tableName);

        HTableDescriptor htd = admin.getTableDescriptor(tableName);

        htd.addCoprocessor(coprocessClassName);

        admin.modifyTable(tableName, htd);

        admin.enableTable(tableName);
        log.debug("=======================addCoprocessorToTable success!!========================== ");
    }

    /**
     *  得到Table的记录的个数
     * @param tableName
     * @return
     */
    public long getTableCount(String tableName,Scan scan){
        //s.addColumn(Bytes.toBytes("debug"), Bytes.toBytes("c1"));

        AggregationClient ac = new AggregationClient(conf);

        try {
            long total=0;
            total=ac.rowCount(TableName.valueOf(tableName), new LongColumnInterpreter(), scan);
            log.debug("=====================tableName:个数 "+total);
            return total;

        } catch (Throwable e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }
        return 0;
    }


    /**
     * @Description 获取总数
     * @Author chongzi
     * @Date 2019/1/19 14:58
     * @Param
     * @return
     **/
    public Long getCount(String tableName){
        Long result =  null;
        try {
            HTable table = new HTable(conf,tableName);
            Scan scan = new Scan();
            scan.setFilter(new FirstKeyOnlyFilter());
            ResultScanner resultScanner = table.getScanner(scan);
            for (Result r : resultScanner) {
                result += r.size();
            }
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
     * @Date 2019/1/10 15:12
     * @Param
     * @return
     **/
    public Long getSqlData(String sql){
        Long result =  null;
        try {
            SelectSqlVisitor sqlVisitor = parseSql(CommonUtils.replaceDateFormat(sql));
            String tableName = sqlVisitor.getTableName();
            String rowKey = null;
            List<String> rowKeys = null;
            Map<String, List<String>> queryColumnMap = sqlVisitor.getQueryColumnMap();
            Set<String> queryColumns = sqlVisitor.getQueryColumns();
            List<Result> results = null;
            Scan scan = sqlVisitor.getScanner();
            Long offset = sqlVisitor.getOffset();
            Long rowCount = sqlVisitor.getRowCount();
            results = cn.escheduler.common.utils.hbase.client.HBaseUtils.getResults(connection, tableName, scan, offset, rowCount, queryColumnMap);
            List<HResult> hResultList = null;
            if (results != null) {
                hResultList = new ArrayList<HResult>();
                for (Result r : results) {
                    HResult hResult = getHResult(r, queryColumns);
                    if (hResult != null) {
                        hResultList.add(hResult);
                    }
                }
                result = Long.valueOf(hResultList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    private SelectSqlVisitor parseSql(String sql) throws SQLSyntaxErrorException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        SelectSqlVisitor sqlVisitor = null;
        try {
            Select select = (Select) parserManager.parse(new StringReader(sql));
            sqlVisitor = new SelectSqlVisitor(select);
        } catch (Exception e) {
            throw new SQLSyntaxErrorException(sql, e);
        }
        return sqlVisitor;
    }

    private HResult getHResult(Result result, Set<String> queryColumns) {
        HResult hResult = null;
        if (result != null && !result.isEmpty()) {
            hResult = new HResult();
            Map<String, Object> resultMap = null;
            List<Cell> cells = result.listCells();
            if (cells != null) {
                resultMap = new HashMap<String, Object>();

                if (queryColumns != null &&
                        (queryColumns.contains(HBaseSqlContants.ROW_KEY) || queryColumns
                                .contains(HBaseSqlContants.ASTERISK))) { // 设置rowkey
                    String rowkey = Bytes.toString(result.getRow());
                    resultMap.put(HBaseSqlContants.ROW_KEY, rowkey);
                }

                for (Cell cell : cells) {
                    String family = Bytes.toString(CellUtil.cloneFamily(cell)); // family
                    String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell)); // qualifier

                    String column = family + "." + qualifier;
                    if (queryColumns == null ||
                            (!queryColumns.contains(column) && !queryColumns.contains(HBaseSqlContants.ASTERISK))) {
                        continue;
                    }

                    String value = Bytes.toString(CellUtil.cloneValue(cell)); // value

                    resultMap.put(family + "." + qualifier, value);

                    String columnWithTS = family + "." + qualifier + HBaseSqlContants.TS_SUFFIX;

                    if (queryColumns != null && queryColumns.contains(columnWithTS)) {
                        long ts = cell.getTimestamp(); // 时间戳
                        resultMap.put(columnWithTS, ts);
                    }
                }
                hResult.setResultMap(resultMap);
            }
        }
        return hResult;
    }

    /**
     * 关闭连接
     */
    public void close(){
        try {
            if(null != admin)
                admin.close();
            if(null != connection)
                connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        HbaseUtils hbaseUtils = new HbaseUtils("bts-masterbak,bts-datanode01,bts-datanode02,bts-datanode03,bts-datanode04","2181");
        /*long rowCount = 0;
        try {
            HTable table = new HTable(conf,"chongzi");
            Scan scan = new Scan();
            scan.setFilter(new FirstKeyOnlyFilter());
            ResultScanner resultScanner = table.getScanner(scan);
            for (Result result : resultScanner) {
                rowCount += result.size();
            }
            System.out.println("rowCount-->"+rowCount);
        } catch (IOException e) {
        }*/

        System.out.println(hbaseUtils.getConnectionState("chongzi"));
    }
}