package io.github.tequilacn.starter.util;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * jdk1.8 optional present|not present时无法链式调用
 * 提供一个简洁的链式调用方式
 * @author Nacht
 * Created on 2020/9/27
 */
public class OptionalConsumer<T> {

    private final Optional<T> optional;

    private OptionalConsumer(Optional<T> optional) {
        this.optional = optional;
    }

    public static <T> OptionalConsumer<T> of(Optional<T> optional) {
        return new OptionalConsumer<>(optional);
    }

    public OptionalConsumer<T> ifPresent(Consumer<T> c) {
        optional.ifPresent(c);
        return this;
    }

    public OptionalConsumer<T> ifNotPresent(Runnable r) {
        if (!optional.isPresent()) {
            r.run();
        }
        return this;
    }

    public static void main(String[] args) {
        OptionalConsumer.of(Optional.ofNullable(null)).ifPresent(System.out :: println).ifNotPresent(() -> System.out.println("没有值!"));
    }
}
