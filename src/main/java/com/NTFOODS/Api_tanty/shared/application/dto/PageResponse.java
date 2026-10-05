package com.NTFOODS.Api_tanty.shared.application.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Wrapper générique pour les réponses paginées de l'API REST.
 *
 * Encapsule le contenu de la page courante ainsi que les métadonnées
 * de pagination (total d'éléments, total de pages, page courante, taille).
 *
 * Utilisé par tous les contrôleurs retournant des listes volumineuses
 * afin d'éviter de charger toute la table en mémoire.
 */
public class PageResponse<T> {

    private final List<T> contenu;
    private final long totalElements;
    private final int totalPages;
    private final int pageCourante;
    private final int taillePage;
    private final boolean dernierePage;

    public PageResponse(List<T> contenu, long totalElements, int totalPages,
                        int pageCourante, int taillePage, boolean dernierePage) {
        this.contenu = contenu;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageCourante = pageCourante;
        this.taillePage = taillePage;
        this.dernierePage = dernierePage;
    }

    /**
     * Construit un {@link PageResponse} à partir d'une {@link Page} Spring Data.
     */
    public static <T> PageResponse<T> de(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize(),
            page.isLast()
        );
    }

    public List<T> getContenu() { return contenu; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getPageCourante() { return pageCourante; }
    public int getTaillePage() { return taillePage; }
    public boolean isDernierePage() { return dernierePage; }
}
