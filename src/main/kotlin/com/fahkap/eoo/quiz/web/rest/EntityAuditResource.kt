package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.security.ADMIN
import com.fahkap.eoo.quiz.service.EntityAuditEventService
import com.fahkap.eoo.quiz.service.dto.EntityAuditEventDTO
import io.github.jhipster.web.util.PaginationUtil
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api")
@Transactional
class EntityAuditResource(
    private val entityAuditEventService: EntityAuditEventService
) {

    @GetMapping("/audits/entity/all")
    @PreAuthorize("hasAuthority( $ADMIN )")
    fun getAuditedEntities(): ResponseEntity<MutableList<String>> {
        return ResponseEntity.ok().body(entityAuditEventService.findAllEntityTypes())
    }

    @GetMapping("/audits/entity/changes")
    @PreAuthorize("hasAuthority( $ADMIN )")
    fun getChanges(
        @RequestParam("entityType") entityType: String,
        @RequestParam("limit") limit: Int
    ): ResponseEntity<MutableList<EntityAuditEventDTO>> {
        val page = entityAuditEventService.findAllByEntityType(entityType, PageRequest.of(0, limit))
        val headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), page
        )
        return ResponseEntity<MutableList<EntityAuditEventDTO>>(page.content, headers, HttpStatus.OK)
    }

    @GetMapping("/audits/entity/changes/version/previous")
    @PreAuthorize("hasAuthority( $ADMIN )")
    fun getPrevVersion(
        @RequestParam(value = "qualifiedName") qualifiedName: String,
        @RequestParam(value = "entityId") entityId: String,
        @RequestParam(value = "commitVersion") commitVersion: Int
    ): ResponseEntity<EntityAuditEventDTO?>? {
        val prev: EntityAuditEventDTO? = entityAuditEventService.findOneByEntityTypeAndEntityIdAndNextCommitVersion(
            qualifiedName,
            entityId,
            commitVersion
        )
        return ResponseEntity<EntityAuditEventDTO?>(prev, HttpStatus.OK)
    }
}
