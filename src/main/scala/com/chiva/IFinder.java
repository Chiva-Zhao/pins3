package com.chiva;

/**
 * Created by Coder on 2016/11/25.
 */
@FunctionalInterface
public interface IFinder {
    String find(String criteria);

    default String criteria() {
        return "Search criteria";
    }
    IFinder finder = (input) -> "output result";
}
