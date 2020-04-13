package com.fahkap.eoo.quiz

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchTest {

    @Test
    fun servicesAndRepositoriesShouldNotDependOnWebLayer() {

        val importedClasses = ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.fahkap.eoo.quiz")

        noClasses()
            .that()
                .resideInAnyPackage("com.fahkap.eoo.quiz.service..")
            .or()
                .resideInAnyPackage("com.fahkap.eoo.quiz.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.fahkap.eoo.quiz.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses)
    }
}
