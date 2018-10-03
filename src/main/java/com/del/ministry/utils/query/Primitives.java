package com.del.ministry.utils.query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Фабрики основных частей запроса
 */
public enum Primitives {

    EQ(new ConditionFragmentFactory("=")),
    NEQ(new ConditionFragmentFactory("!=")),
    LIKE(new LikeFragmentFactory()),
    GT(new ConditionFragmentFactory(">")),
    LT(new ConditionFragmentFactory("<")),
    GTE(new ConditionFragmentFactory(">=")),
    LTE(new ConditionFragmentFactory("<=")),
    IS_NULL(new IsNullFragmentFactory()),
    EMPTY(new SimpleFragmentFactory(" ")),
    OR(new SimpleFragmentFactory("or")),
    AND(new SimpleFragmentFactory("and"));

    private QueryFragmentFactory fragmentFactory;

    Primitives(QueryFragmentFactory fragmentFactory) {
        this.fragmentFactory = fragmentFactory;
    }

    public QueryFragmentFactory getFragmentFactory() {
        return fragmentFactory;
    }
}
