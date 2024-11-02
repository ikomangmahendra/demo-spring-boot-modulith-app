package com.angjaya.core.demospringmodulithapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class DemoSpringModulithAppApplicationTests {

    @Test
    void contextLoads() {

        var modules = ApplicationModules.of(DemoSpringModulithAppApplication.class);
        for (var module : modules) {
            System.out.println(module.getClass().getName() + ": " + module.getBasePackage());
        }
        modules.verify();

        new Documenter(modules)
            .writeIndividualModulesAsPlantUml()
            .writeModulesAsPlantUml();
    }

}
