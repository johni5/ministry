package com.del.ministry.utils.query;

import com.del.ministry.utils.StringUtil;

import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.Collection;

/**
 * Created by dodolinel
 * date: 16.03.15
 * Фабрика: in вхождение в SQL select<br/>
 */
public class InSelectFragmentFactory implements QueryFragmentFactory {

    private String sql;

    public InSelectFragmentFactory(String sql) {
        this.sql = sql;
    }

    @Override
    public String build(QueryParameter parameter) {
        StringBuilder fragment = new StringBuilder(" in ");
        if (parameter != null && parameter.getValue() != null) {
            Object[] variables = (Object[]) parameter.getValue();
            Object[] aliases = new Object[variables.length];
            for (int i = 0; i < variables.length; i++) {
                aliases[i] = ":" + parameter.getAlias() + "_" + i;
            }
            fragment.append("(").append(MessageFormat.format(sql, aliases)).append(")");
        } else {
            fragment.append("(").append(sql).append(")");
        }
        return fragment.toString();
    }

    @Override
    public void init(Query query, QueryParameter parameter) {
        if (parameter != null && parameter.getValue() != null) {
            Object[] variables = (Object[]) parameter.getValue();
            if (variables.length > 0) {
                int index = 0;
                for (Object variable : variables) {
                    String alias = parameter.getAlias() + "_" + index++;
                    query.setParameter(alias, variable);
                }
            }
        }
    }

    @Override
    public boolean ready(QueryParameter parameter) {
        if (parameter == null || parameter.getValue() == null) {
            return true;
        }
        boolean ready = parameter.getValue() instanceof Object[];
        if (ready) {
            Object[] variables = (Object[]) parameter.getValue();
            for (Object variable : variables) {
                if (variable instanceof Collection) {
                    ready &= !((Collection) variable).isEmpty();
                } else {
                    ready &= !StringUtil.isTrimmedEmpty(variable);
                }
            }
        }
        return ready;
    }
}
