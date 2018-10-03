package com.del.ministry.utils.query;

import com.del.ministry.utils.ListUtil;
import com.google.common.collect.Lists;

import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Группа фрагментов запроса. Представляет собой законченый фрагмент запроса состоящий из нескольких частей.
 * При этом группа может быть частью другого фрагмента.<br/>
 * Например:<br/>
 * часть:           |  1  |     2     |
 * запрос:      and f.name = :fieldName
 * группа:      |          1          |
 * ready:       |  fieldName != null  |
 */
public class QueryGroup extends QueryChain {

    /**
     * Основная последовательность запросов
     */
    private QuerySequence parent;

    /**
     * Номер группы в последовательности запроса
     */
    private int index;

    protected List<QueryChain> child = Lists.newArrayList();

    public QueryGroup(QuerySequence parent, int index, QueryFragmentFactory fragmentFactory) {
        super(fragmentFactory);
        this.parent = parent;
        this.index = index;
    }

    public void init(Query query) {
        super.init(query);
        for (QueryChain chain : child) {
            chain.init(query);
        }
    }

    @Override
    protected String getQuery() {
        if (ready()) {
            StringBuilder query = new StringBuilder(super.getQuery());
            for (QueryChain chain : child) {
                query.append(chain.getQuery());
            }
            return query.toString();
        }
        return "";
    }

    /**
     * Группа будет готова если готовы все ее составные части.
     * Если мы строим запрос, при этом параметр, входящий в него - null, то мы не будем строить этот фрагмент
     */
    @Override
    protected boolean ready() {
        boolean ready = super.ready();
        for (QueryChain chain : child) {
            if (!chain.ready()) {
                return false;
            }
        }
        return ready;
    }

    private QuerySequence getParent() {
        return parent;
    }

    /**
     * Строим запрос - условие
     */
    private void condition(String name, QueryChain chain) {
        child.add(new QueryChain(new SimpleFragmentFactory(name)));
        child.add(chain);
    }

    /**
     * Псевдоним параметра: уникальное в цепочке запросов значение
     */
    private String getFieldAlias() {
        return "field_" + parent.getId() + "_" + index + "_" + child.size();
    }

    /**
     * [fName] = [fValue]
     */
    public QuerySequence eq(String fName, Object fValue) {
        return eq(fName, fValue, false);
    }

    /**
     * if nullCheck == false
     * [fName] = [fValue]
     * if nullCheck == true
     * [fName] = [fValue]   // if fValue != null
     * [fName] is null      // if fValue == null
     */
    public QuerySequence eq(String fName, Object fValue, boolean nullCheck) {
        condition(fName, new QueryChain(Primitives.EQ.getFragmentFactory(), new QueryParameter(getFieldAlias(), fValue, nullCheck)));
        return getParent();
    }

    /**
     * [fName] != [fValue]
     */
    public QuerySequence neq(String fName, Object fValue) {
        condition(fName, new QueryChain(Primitives.NEQ.getFragmentFactory(), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] < [fValue]
     */
    public QuerySequence lt(String fName, Object fValue) {
        condition(fName, new QueryChain(Primitives.LT.getFragmentFactory(), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] <= [fValue]
     */
    public QuerySequence lte(String fName, Object fValue) {
        condition(fName, new QueryChain(Primitives.LTE.getFragmentFactory(), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] > [fValue]
     */
    public QuerySequence gt(String fName, Object fValue) {
        condition(fName, new QueryChain(Primitives.GT.getFragmentFactory(), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] >= [fValue]
     */
    public QuerySequence gte(String fName, Object fValue) {
        condition(fName, new QueryChain(Primitives.GTE.getFragmentFactory(), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] like lower(%[fValue]%)
     */
    public QuerySequence like(String fName, Object fValue) {
        condition(fName, new QueryChain(Primitives.LIKE.getFragmentFactory(), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] is null      // if fValue == true<br/>
     * [fName] is not null  // if fValue == false
     */
    public QuerySequence isNull(String fName, Boolean fValue) {
        condition(fName, new QueryChain(Primitives.IS_NULL.getFragmentFactory(), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fValue] = some elements([fName])
     */
    public QuerySequence haveSomeElement(String fName, Object fValue) {
        child.add(new QueryChain(new HaveSomeElementFragmentFactory(fName), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] = true                           // if fValue == true<br/>
     * ([fName] is null or [fName] = false)     // if fValue == false
     */
    public QuerySequence is(String fName, Boolean fValue) {
        child.add(new QueryChain(new IsBooleanFragmentFactory(fName), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] in ([select])
     * Параметры селекта задаем как для MessageFormat {0}, {1}, ... и т.д. select un.id from UserNames where lower(un.lastName) like lower({0})
     * Если хотя бы один fValue == NULL или пустой то все условие игнорируется
     */
    public QuerySequence inSelect(String fName, String select, Object... fValue) {
        condition(fName, new QueryChain(new InSelectFragmentFactory(select), new QueryParameter(getFieldAlias(), fValue)));
        return getParent();
    }

    /**
     * [fName] in ([c])
     */
    public QuerySequence inList(String fName, Collection c) {
        if (!ListUtil.isEmpty(c)) {
            List split = ListUtil.split(500, c); // операция IN ограничена 1000 значениями, для надежности делим ее на операции по 500
            int count = 0;
            child.add(new QueryChain(new SimpleFragmentFactory("(")));
            for (Object v : split) {
                condition(fName, new QueryChain(new InListFragmentFactory(), new QueryParameter(getFieldAlias(), v)));
                if (count++ < split.size() - 1) child.add(new QueryChain(Primitives.OR.getFragmentFactory()));
            }
            child.add(new QueryChain(new SimpleFragmentFactory(")")));
        } else {
            condition(fName, new QueryChain(new InListFragmentFactory(), new QueryParameter(getFieldAlias(), c)));
        }
        return getParent();
    }

    /**
     * [fName] not in ([c])
     */
    public QuerySequence notInList(String fName, Collection c) {
        if (!ListUtil.isEmpty(c)) {
            List split = ListUtil.split(500, c); // операция IN ограничена 1000 значениями, для надежности делим ее на операции по 500
            int count = 0;
            child.add(new QueryChain(new SimpleFragmentFactory("(")));
            for (Object v : split) {
                condition(fName, new QueryChain(new NotInListFragmentFactory(), new QueryParameter(getFieldAlias(), v)));
                if (count++ < split.size() - 1) child.add(new QueryChain(Primitives.AND.getFragmentFactory()));
            }
            child.add(new QueryChain(new SimpleFragmentFactory(")")));
        } else {
            condition(fName, new QueryChain(new InListFragmentFactory(), new QueryParameter(getFieldAlias(), c)));
        }
        return getParent();
    }

}
