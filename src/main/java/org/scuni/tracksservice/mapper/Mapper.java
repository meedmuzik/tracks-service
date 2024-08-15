package org.scuni.tracksservice.mapper;

public interface Mapper<T, E>{

    E map(T t);
}
