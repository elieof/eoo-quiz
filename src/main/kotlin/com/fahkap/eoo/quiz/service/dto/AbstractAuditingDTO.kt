package com.fahkap.eoo.quiz.service.dto

import java.io.Serializable
import java.time.Instant

/**
 * Base abstract class for Dto which will hold definitions for created, last modified by, created by,
 * last modified by attributes.
 */
abstract class AbstractAuditingDTO(

    var createdBy: String? = null,

    var createdDate: Instant? = null,

    var lastModifiedBy: String? = null,

    var lastModifiedDate: Instant? = null

) : Serializable
