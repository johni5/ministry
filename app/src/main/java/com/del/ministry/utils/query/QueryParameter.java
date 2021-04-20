package com.del.ministry.utils.query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Параметр фрагмента запроса
 */
public class QueryParameter {

    /**
     * Имя параметра, используется как ссылка на переменную в запросе
     */
    private String alias;

    /**
     * Значение параметра
     */
    private Object value;

    /**
     * Учитывать при построении запроса значение NULL
     */
    private boolean checkForNull;

    public QueryParameter(String alias, Object value) {
        this(alias, value, false);
    }

    public QueryParameter(String alias, Object value, boolean checkForNull) {
        this.alias = alias;
        this.value = value;
        this.checkForNull = checkForNull;
    }

    public String getAlias() {
        return alias;
    }

    public Object getValue() {
        return value;
    }

    public boolean isCheckForNull() {
        return checkForNull;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
