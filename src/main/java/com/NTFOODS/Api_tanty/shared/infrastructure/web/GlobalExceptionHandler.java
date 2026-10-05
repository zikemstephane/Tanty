package com.NTFOODS.Api_tanty.shared.infrastructure.web;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteBloqueException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteDesactiveException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CredentialsInvalidesException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.AdminException;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions — réponses homogènes ApiResponse.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CredentialsInvalidesException.class)
    public ResponseEntity<ApiResponse<Void>> handleCredentialsInvalides(CredentialsInvalidesException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.erreur("Authentification échouée", ex.getMessage()));
    }

    @ExceptionHandler(CompteBloqueException.class)
    public ResponseEntity<ApiResponse<Void>> handleCompteBloque(CompteBloqueException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED)
            .body(ApiResponse.erreur("Compte bloqué", ex.getMessage()));
    }

    @ExceptionHandler(CompteDesactiveException.class)
    public ResponseEntity<ApiResponse<Void>> handleCompteDesactive(CompteDesactiveException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.erreur("Compte désactivé", ex.getMessage()));
    }

    @ExceptionHandler(AdminException.class)
    public ResponseEntity<ApiResponse<Void>> handleAdmin(AdminException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.erreur("Requête d'administration invalide", ex.getMessage()));
    }

    @ExceptionHandler(EntiteNonTrouveeException.class)
    public ResponseEntity<ApiResponse<Void>> handleNonTrouvee(EntiteNonTrouveeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.erreur("Ressource introuvable", ex.getMessage()));
    }

    @ExceptionHandler({AccesRefuseException.class, AccessDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> handleAccesRefuse(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.erreur("Accès refusé", ex.getMessage()));
    }

    @ExceptionHandler(RegleMetierException.class)
    public ResponseEntity<ApiResponse<Void>> handleRegleMetier(RegleMetierException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.erreur("Règle métier violée", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.erreur("Requête invalide", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.erreur("Conflit d'état", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + " : " + e.getDefaultMessage())
            .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.erreur("Validation échouée", details));
    }

    // ── Erreurs de requête : la faute est côté appelant, pas côté serveur ──
    //
    // Sans ces trois gestionnaires, un paramètre oublié, une méthode HTTP
    // erronée ou une URL inconnue tombaient dans le filet générique et
    // ressortaient en 500. L'appelant croyait le serveur en panne alors que
    // sa requête était simplement mal formée — et le journal se remplissait
    // de fausses erreurs internes qui noyaient les vraies.

    @ExceptionHandler({MissingServletRequestParameterException.class,
                       MethodArgumentTypeMismatchException.class,
                       HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<Void>> handleRequeteMalFormee(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.erreur("Requête invalide", ex.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodeNonSupportee(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ApiResponse.erreur("Méthode non autorisée",
                ex.getMethod() + " n'est pas accepté sur cette ressource. Méthodes possibles : "
                    + String.join(", ", ex.getSupportedMethods() != null ? ex.getSupportedMethods() : new String[]{})));
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleRouteInconnue(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.erreur("Ressource introuvable", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        log.error("Erreur interne non gérée", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.erreur("Erreur interne du serveur", ex.getMessage()));
    }
}
