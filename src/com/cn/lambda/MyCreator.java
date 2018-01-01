package com.cn.lambda;

import java.util.List;

@FunctionalInterface
public interface MyCreator<T> {
    T Create();
}
