package com.pmart5a.servaces;

import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorId {
    private final AtomicInteger nextId = new AtomicInteger(1);

    private GeneratorId() {}

    private static class GeneratorIdHolder {
        public static final GeneratorId generatorId = new GeneratorId();
    }

    public static GeneratorId getGeneratorId() {
        return GeneratorIdHolder.generatorId;
    }

    public Integer getId() {
        return nextId.getAndIncrement();
    }
}