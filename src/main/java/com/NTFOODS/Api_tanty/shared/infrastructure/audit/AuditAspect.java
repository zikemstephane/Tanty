package com.NTFOODS.Api_tanty.shared.infrastructure.audit;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Aspect transversal — Journal d'Audit automatique (Pilier 1 & Pilier 3).
 *
 * Intercepte TOUTE méthode publique de TOUT contrôleur REST, de TOUT module
 * (Stock, Production, Commercial, Comptabilité, Administration, DG...), sans
 * qu'aucun module n'ait à s'en préoccuper explicitement. Chaque appel est
 * journalisé avec : matricule de l'auteur, rôle, module, endpoint, méthode
 * HTTP, succès/échec, message d'erreur éventuel et durée d'exécution.
 *
 * Le type d'action (CONSULTATION / CREATION / MODIFICATION / ...) est déduit
 * de la méthode HTTP par défaut, puis affiné par des mots-clés présents dans
 * le chemin (ex: "annuler", "rejeter", "supprimer" → ANNULATION /
 * SUPPRESSION_TENTATIVE ; "valider" → VALIDATION).
 */
@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Around("execution(public * com.NTFOODS.Api_tanty..presentation.controller..*.*(..))")
    public Object tracer(ProceedingJoinPoint joinPoint) throws Throwable {
        long debut = System.currentTimeMillis();

        String classeSimple = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methode = joinPoint.getSignature().getName();
        String module = extraireModule(joinPoint.getSignature().getDeclaringTypeName());

        HttpServletRequest request = requeteCourante();
        String methodeHttp = request != null ? request.getMethod() : "N/A";
        String endpoint = request != null ? request.getRequestURI() : classeSimple + "#" + methode;
        String adresseIp = request != null ? adresseIpReelle(request) : null;

        String matricule = null;
        String role = null;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Utilisateur u) {
                matricule = u.getMatricule().getValeur();
                role = u.getRole().name();
            }
        } catch (Exception ignored) {
            // Contexte non authentifié (ex: /api/auth/login) : on trace en ANONYME
        }

        TypeActionAudit typeAction = deduireTypeAction(methodeHttp, endpoint);
        String details = resumerArguments(joinPoint);

        try {
            Object resultat = joinPoint.proceed();
            long duree = System.currentTimeMillis() - debut;
            auditService.enregistrer(matricule, role, typeAction, module, classeSimple, null,
                methodeHttp, endpoint, details, true, null, adresseIp, duree);
            return resultat;
        } catch (Throwable ex) {
            long duree = System.currentTimeMillis() - debut;
            auditService.enregistrer(matricule, role, typeAction, module, classeSimple, null,
                methodeHttp, endpoint, details, false, ex.getMessage(), adresseIp, duree);
            throw ex;
        }
    }

    private HttpServletRequest requeteCourante() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attrs != null ? attrs.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String adresseIpReelle(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String extraireModule(String nomComplet) {
        // com.NTFOODS.Api_tanty.modules.<module>.presentation.controller.XxxController
        String marqueur = ".modules.";
        int idx = nomComplet.indexOf(marqueur);
        if (idx < 0) return "shared";
        String reste = nomComplet.substring(idx + marqueur.length());
        int pointSuivant = reste.indexOf('.');
        return pointSuivant > 0 ? reste.substring(0, pointSuivant) : reste;
    }

    private TypeActionAudit deduireTypeAction(String methodeHttp, String endpoint) {
        String chemin = endpoint == null ? "" : endpoint.toLowerCase();
        if (chemin.contains("annul")) return TypeActionAudit.ANNULATION;
        if (chemin.contains("rejet") || chemin.contains("refus") || chemin.contains("supprim")
            || chemin.contains("desactiv")) return TypeActionAudit.SUPPRESSION_TENTATIVE;
        if (chemin.contains("valid") || chemin.contains("approuv")) return TypeActionAudit.VALIDATION;
        if (chemin.contains("export") || chemin.contains("rapport") || chemin.contains("pdf")
            || chemin.contains("excel") || chemin.contains("imprim")) return TypeActionAudit.IMPRESSION_EXPORT;
        if (chemin.contains("debloqu")) return TypeActionAudit.DEBLOCAGE_PRIX;

        if (methodeHttp == null) return TypeActionAudit.AUTRE;
        return switch (methodeHttp) {
            case "GET" -> TypeActionAudit.CONSULTATION;
            case "POST" -> TypeActionAudit.CREATION;
            case "PUT", "PATCH" -> TypeActionAudit.MODIFICATION;
            case "DELETE" -> TypeActionAudit.SUPPRESSION_TENTATIVE;
            default -> TypeActionAudit.AUTRE;
        };
    }

    /** Résumé court et non sensible des arguments (évite de journaliser des mots de passe en clair). */
    private String resumerArguments(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String[] noms = signature.getParameterNames();
            Object[] valeurs = joinPoint.getArgs();
            if (noms == null || valeurs == null) return null;

            return java.util.stream.IntStream.range(0, Math.min(noms.length, valeurs.length))
                .mapToObj(i -> {
                    String nom = noms[i];
                    if (nom != null && (nom.toLowerCase().contains("password")
                        || nom.toLowerCase().contains("motdepasse") || nom.toLowerCase().contains("otp")
                        || nom.toLowerCase().contains("secret"))) {
                        return nom + "=***";
                    }
                    Object v = valeurs[i];
                    String repr = v == null ? "null" : v.toString();
                    if (repr.length() > 80) repr = repr.substring(0, 77) + "...";
                    return nom + "=" + repr;
                })
                .collect(Collectors.joining(", "));
        } catch (Exception e) {
            return null;
        }
    }
}
