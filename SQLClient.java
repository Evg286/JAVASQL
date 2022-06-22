package com.company;

@FunctionalInterface
public interface SQLClient <T> {

    abstract void accept(T t);
}
