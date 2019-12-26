package cn.escheduler.common.utils.hbase.sql.engine;


import cn.escheduler.common.utils.hbase.entity.HResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface HBaseSqlEngine {

    public List<HResult> select(String sql) throws Exception;

    public void insert(String sql) throws Exception;

    public void insert(String sql, HashMap<String, String> map) throws Exception;

    public void insert(String sql, ArrayList<HashMap<String, String>> list) throws Exception;

    public void del(String sql) throws Exception;

    public void del(String sql, List<String> rowkeys) throws Exception;

    public void del(String sql, List<String> rowkeys, HashMap<String, ArrayList<String>> columnMap) throws Exception;
}
