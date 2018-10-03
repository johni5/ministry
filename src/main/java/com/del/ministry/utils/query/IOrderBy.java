package com.del.ministry.utils.query;

import java.io.Serializable;

/**
 * Created by dodolinel
 * date: 28.11.14
 * <p/>
 * Интерфейс описания поля сортировки
 */
public interface IOrderBy<T> extends Serializable {

    boolean isAscending();

    T getField();

}
