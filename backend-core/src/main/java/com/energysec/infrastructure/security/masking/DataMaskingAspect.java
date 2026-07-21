package com.energysec.infrastructure.security.masking;

import com.energysec.infrastructure.rest.dto.MaskedMetricResponseDTO;
import com.energysec.infrastructure.rest.dto.FinancialResponseDTO;
import com.energysec.infrastructure.rest.dto.FacilityResponseDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class DataMaskingAspect {

    @Around("execution(* com.energysec.infrastructure.rest.controller.*.*(..)) && !@annotation(com.energysec.infrastructure.security.masking.ExposeFullData)")
    public Object maskDataByDefault(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        // Check if user has ADMIN role. If so, bypass masking.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_FINANCE"));

        if (isAdmin) {
            return result; // No masking for admins/finance
        }

        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();

            if (body instanceof List) {
                List<?> list = (List<?>) body;
                if (!list.isEmpty()) {
                    list.forEach(this::applyMaskingToGeneric);
                }
            } else {
                applyMaskingToGeneric(body);
            }
        }

        return result;
    }

    private void applyMaskingToGeneric(Object dto) {
        if (dto instanceof MaskedMetricResponseDTO metric) {
            if (metric.getAssetId() != null && metric.getAssetId().length() > 4) {
                metric.setAssetId(metric.getAssetId().substring(0, 4) + "****");
            }
            metric.setMasked(true);
        } else if (dto instanceof FinancialResponseDTO finance) {
            if (finance.getCif() != null && finance.getCif().length() > 2) {
                finance.setCif(finance.getCif().substring(0, 2) + "*******");
            }
            // Hide exact amount
            finance.setAmount(0.0);
            finance.setMasked(true);
        } else if (dto instanceof FacilityResponseDTO facility) {
            if (facility.getCadastralReference() != null && facility.getCadastralReference().length() > 5) {
                facility.setCadastralReference(facility.getCadastralReference().substring(0, 5) + "***********");
            }
            facility.setMasked(true);
        }
    }
}
