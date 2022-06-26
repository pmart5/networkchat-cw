package com.pmart5a.servaces;

public class GeneratorId {
        private int nextId = 1;
        private static GeneratorId generatorId = null;

        private GeneratorId() {}

        public static GeneratorId getGeneratorId() {
            if (generatorId == null) {
                generatorId = new GeneratorId();
            }
            return generatorId;
        }

        public Integer getId() {
            return nextId++;
        }
    }