package cn.escheduler.common.utils.hbase.sql.visitor;

import cn.escheduler.common.utils.hbase.HBaseSqlContants;
import cn.escheduler.common.utils.hbase.sql.util.ExpressionUtil;
import cn.escheduler.common.utils.hbase.util.HBaseSqlMapParser;
import com.github.vincentrussell.query.mongodb.sql.converter.ParseException;
import com.github.vincentrussell.query.mongodb.sql.converter.util.SqlUtils;
import com.google.common.base.Strings;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.values.ValuesStatement;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by linghf on 2016/12/21.
 */
public class SelectSqlVisitor implements SelectVisitor, FromItemVisitor, ExpressionVisitor {

    private List<String> tableNames = new ArrayList<String>();

    private Set<String> queryColumns = new HashSet<String>();

    private Map<String, List<String>> queryColumnMap = new HashMap<String, List<String>>();

    private FilterList filterList = new FilterList();

    private String startRow;

    private String stopRow;

    private Scan scanner = new Scan();

    private Long rowCount;

    private Long offset;

    public SelectSqlVisitor(Select select) {
        SelectBody selectBody = select.getSelectBody();
        selectBody.accept(this);
    }


    public String getTableName() {
        return tableNames.get(0);
    }

    public Set<String> getQueryColumns() {
        return queryColumns;
    }

    public Map<String, List<String>> getQueryColumnMap() {
        return queryColumnMap;
    }

    public Long getOffset() {
        return offset;
    }

    public Long getRowCount() {
        return rowCount;
    }

    public Scan getScanner() {
        if (filterList != null && filterList.getFilters() != null && filterList.getFilters().size() > 0) {
            scanner.setFilter(filterList);
        }
        if (!Strings.isNullOrEmpty(startRow)) {
            scanner.setStartRow(Bytes.toBytes(startRow));
        }
        if (!Strings.isNullOrEmpty(stopRow)) {
            scanner.setStopRow(Bytes.toBytes(stopRow));
        }
        scanner.setCaching(1000);

        return scanner;
    }

    @Override
    public void visit(BitwiseRightShift bitwiseRightShift) {

    }

    @Override
    public void visit(BitwiseLeftShift bitwiseLeftShift) {

    }

    public void visit(NullValue nullValue) {

    }

    public void visit(Function function) {

    }

    public void visit(SignedExpression signedExpression) {

    }

    public void visit(JdbcParameter jdbcParameter) {

    }

    public void visit(JdbcNamedParameter jdbcNamedParameter) {

    }

    public void visit(DoubleValue doubleValue) {

    }

    public void visit(LongValue longValue) {

    }

    public void visit(HexValue hexValue) {

    }

    public void visit(DateValue dateValue) {

    }

    public void visit(TimeValue timeValue) {

    }

    public void visit(TimestampValue timestampValue) {

    }

    public void visit(Parenthesis parenthesis) {

    }

    public void visit(StringValue stringValue) {

    }

    public void visit(Addition addition) {

    }

    public void visit(Division division) {

    }

    public void visit(Multiplication multiplication) {

    }

    public void visit(Subtraction subtraction) {

    }

    public void visit(AndExpression andExpression) {
        andExpression.getLeftExpression().accept(this);
        andExpression.getRightExpression().accept(this);
    }

    public void visit(OrExpression orExpression) {
        orExpression.getLeftExpression().accept(this);
        orExpression.getRightExpression().accept(this);
    }

    public void visit(Between between) {
        String x = between.getLeftExpression().toString();
        String start = between.getBetweenExpressionStart().toString();
        String end = between.getBetweenExpressionEnd().toString();

        System.out.println(x + " between " + start + " and " + end);
    }

    public void visit(EqualsTo equalsTo) {
        String key = equalsTo.getLeftExpression().toString();
        String value = equalsTo.getRightExpression().toString();

        if (!Strings.isNullOrEmpty(key) && !Strings.isNullOrEmpty(value)) {
            if (HBaseSqlContants.ROW_KEY.equalsIgnoreCase(key)) {
                RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(value));
                filterList.addFilter(rowFilter);
            } else if (HBaseSqlContants.START_ROW.equalsIgnoreCase(key)) {
                this.startRow = value;
            } else if (HBaseSqlContants.STOP_ROW.equalsIgnoreCase(key)) {
                this.stopRow = value;
            }
        }
    }

    public void visit(GreaterThan greaterThan) {

    }

    public void visit(GreaterThanEquals greaterThanEquals) {

    }

    public void visit(InExpression inExpression) {
        String key = inExpression.getLeftExpression().toString();

        ItemsList itemsList = inExpression.getRightItemsList();
        List<String> values = ExpressionUtil.getStringList(itemsList);

        List<Filter> filters = new ArrayList<Filter>();
        if (values != null && !values.isEmpty()) {
            for (String value : values) {
                if (Strings.isNullOrEmpty(value)) {
                    continue;
                }
                if (HBaseSqlContants.ROW_KEY.equalsIgnoreCase(key)) {
                    RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(value));
                    filters.add(rowFilter);
                }
            }
        }
        if (filters != null && !filters.isEmpty()) {
            FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ONE, filters);
            filterList.addFilter(list);
        }
    }


    public void visit(IsNullExpression isNullExpression) {

    }

    public void visit(LikeExpression likeExpression) {
        String left = likeExpression.getLeftExpression().toString();
        String right = likeExpression.getRightExpression().toString();

        if (HBaseSqlContants.ROW_KEY.equalsIgnoreCase(left)) {
            Filter rkFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(right));
            filterList.addFilter(rkFilter);
        } else {
            String tableName = getTableName();
            cn.escheduler.common.utils.hbase.entity.Table table = HBaseSqlMapParser.getTable(tableName);
            if (table != null && table.getRowKey() != null) {
                if (table.getRowKey().getAliasName().equals(left)) {
                    Filter rkFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(right));
                    filterList.addFilter(rkFilter);
                }
            }
        }
    }

    public void visit(MinorThan minorThan) {

    }

    public void visit(MinorThanEquals minorThanEquals) {

    }

    public void visit(NotEqualsTo notEqualsTo) {

    }

    public void visit(Column column) {

    }

    public void visit(CaseExpression caseExpression) {

    }

    public void visit(WhenClause whenClause) {

    }

    public void visit(ExistsExpression existsExpression) {

    }

    public void visit(AllComparisonExpression allComparisonExpression) {

    }

    public void visit(AnyComparisonExpression anyComparisonExpression) {

    }

    public void visit(Concat concat) {

    }

    public void visit(Matches matches) {

    }

    public void visit(BitwiseAnd bitwiseAnd) {

    }

    public void visit(BitwiseOr bitwiseOr) {

    }

    public void visit(BitwiseXor bitwiseXor) {

    }

    public void visit(CastExpression castExpression) {

    }

    public void visit(Modulo modulo) {

    }

    public void visit(AnalyticExpression analyticExpression) {

    }



    public void visit(ExtractExpression extractExpression) {

    }

    public void visit(IntervalExpression intervalExpression) {

    }

    public void visit(OracleHierarchicalExpression oracleHierarchicalExpression) {

    }

    public void visit(RegExpMatchOperator regExpMatchOperator) {

    }

    public void visit(JsonExpression jsonExpression) {

    }

    @Override
    public void visit(JsonOperator jsonOperator) {

    }

    public void visit(RegExpMySQLOperator regExpMySQLOperator) {

    }

    public void visit(UserVariable userVariable) {

    }

    public void visit(NumericBind numericBind) {

    }

    public void visit(KeepExpression keepExpression) {

    }

    public void visit(MySQLGroupConcat mySQLGroupConcat) {

    }

    @Override
    public void visit(ValueListExpression valueListExpression) {

    }

    public void visit(RowConstructor rowConstructor) {

    }

    public void visit(OracleHint oracleHint) {

    }

    public void visit(TimeKeyExpression timeKeyExpression) {

    }

    public void visit(DateTimeLiteralExpression dateTimeLiteralExpression) {

    }

    @Override
    public void visit(NotExpression notExpression) {

    }

    public void visit(Table table) {
        if (table != null && !Strings.isNullOrEmpty(table.getName())) {
            tableNames.add(table.getName());
        }
    }

    public void visit(SubSelect subSelect) {
        System.out.println(subSelect.getWithItemsList());
    }

    public void visit(SubJoin subJoin) {

    }

    public void visit(LateralSubSelect lateralSubSelect) {

    }

    public void visit(ValuesList valuesList) {

    }

    public void visit(TableFunction tableFunction) {

    }

    @Override
    public void visit(ParenthesisFromItem parenthesisFromItem) {

    }

    /**
     * 设置column
     *
     * @param selectItems
     */
    private void setQueryColumns(List<SelectItem> selectItems) {
        if (selectItems == null || selectItems.size() <= 0) {
            return;
        }
        for (SelectItem item : selectItems) {
            String colStr = item.toString();
            if (Strings.isNullOrEmpty(colStr)) {
                continue;
            }

            String[] columnGroup = colStr.split(".");
            if (columnGroup != null && columnGroup.length == 2) {
                String columnFamily = columnGroup[0];
                String column = columnGroup[1];
                if (Strings.isNullOrEmpty(columnFamily) || Strings.isNullOrEmpty(column)) {
                    List<String> columns = queryColumnMap.get(columnGroup[0]);
                    if (columns == null) {
                        columns = new ArrayList<String>();
                    }
                    columns.add(column);
                    queryColumnMap.put(columnFamily, columns);

                    scanner.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
                }
            } else if (HBaseSqlContants.ROW_KEY.equalsIgnoreCase(colStr)) {

            } else if (HBaseSqlContants.ASTERISK.equalsIgnoreCase(colStr)) {

            }
            queryColumns.add(colStr);
        }
    }

    public void visit(PlainSelect plainSelect) {
        if (plainSelect == null) {
            return;
        }
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        setQueryColumns(selectItems);
        Limit limit = plainSelect.getLimit();
        if (limit != null) {
            if(limit.getRowCount() != null){
                String rowCountString = SqlUtils.getStringValue(limit.getRowCount());
                BigInteger rowCountBigInt = new BigInteger(rowCountString);
                try {
                    SqlUtils.isFalse(rowCountBigInt.compareTo(BigInteger.valueOf(2147483647L)) > 0, rowCountString + ": value is too large");
                } catch (ParseException e) {
                    rowCount = -1L;
                    e.printStackTrace();
                }
                rowCount = rowCountBigInt.longValue();
            }
            if(limit.getOffset() != null){
                String offsetString = SqlUtils.getStringValue(limit.getOffset());
                BigInteger offsetBigInt = new BigInteger(offsetString);
                try {
                    SqlUtils.isFalse(offsetBigInt.compareTo(BigInteger.valueOf(2147483647L)) > 0, offsetString + ": value is too large");
                } catch (ParseException e) {
                    e.printStackTrace();
                    offset = -1L;
                }
                offset = offsetBigInt.longValue();
            }
        }

        plainSelect.getFromItem().accept(this);
        if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(this);
        }
    }

    public void visit(SetOperationList setOperationList) {

    }

    public void visit(WithItem withItem) {

    }

    @Override
    public void visit(ValuesStatement valuesStatement) {

    }

}
