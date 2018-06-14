package com.balpos.app.web.rest.mapper;

import java.util.List;

public interface VMMapper <V, E> {

    E toEntity(V vm);

    V toVm(E entity);

    List<E> toEntity(List<V> vmList);

    List <V> toVm(List<E> entityList);
}
