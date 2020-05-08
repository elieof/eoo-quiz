package com.fahkap.eoo.quiz.config.audit

enum class EntityAuditAction(val value: String) {

    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    open fun value(): String {
        return value
    }

    override fun toString(): String {
        return value()
    }
}
