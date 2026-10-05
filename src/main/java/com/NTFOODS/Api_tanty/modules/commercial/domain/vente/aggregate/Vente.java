package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.SyncStatus;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.events.VenteFinaliseEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Vente.
 * Représente une transaction de vente terrain d'un commercial à un client.
 * Numérotation : VTE-YYYYMMDD-NNN
 * UUID généré côté client pour anti-doublon offline (RG-04).
 */
public class Vente {

    private Long id;
    private final String numeroVente;
    private final String uuidVente;
    private final String matriculeCommercial;
    private final String codeClient;
    private final LocalDate date;
    private final List<LigneVente> lignes;
    private ModeReglement modeReglement;
    private TypeVente typeVente;
    private StatutVente statut;
    private SyncStatus syncStatus;
    private String coordonneesGPS;
    private String idFeuillRoute;
    private String observations;
    private boolean signatureClient;
    private String photoProduits;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private Vente(String numeroVente, String uuidVente, String matriculeCommercial, String codeClient,
                  LocalDate date, ModeReglement modeReglement, TypeVente typeVente,
                  LocalDateTime dateCreation) {
        this.numeroVente = Objects.requireNonNull(numeroVente);
        this.uuidVente = uuidVente != null ? uuidVente : UUID.randomUUID().toString();
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.codeClient = Objects.requireNonNull(codeClient);
        this.date = Objects.requireNonNull(date);
        this.modeReglement = Objects.requireNonNull(modeReglement);
        this.typeVente = typeVente != null ? typeVente : TypeVente.COMPTANT;
        this.lignes = new ArrayList<>();
        this.statut = StatutVente.EN_COURS;
        this.syncStatus = SyncStatus.SYNCED;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static Vente creer(String numeroVente, String matriculeCommercial, String codeClient,
                               LocalDate date, ModeReglement modeReglement, TypeVente typeVente,
                               List<LigneVente> lignes) {
        return creer(numeroVente, null, matriculeCommercial, codeClient, date, modeReglement, typeVente, lignes, null);
    }

    public static Vente creer(String numeroVente, String uuidVente, String matriculeCommercial,
                               String codeClient, LocalDate date, ModeReglement modeReglement,
                               TypeVente typeVente, List<LigneVente> lignes, String photoProduits) {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("La vente doit contenir au moins une ligne");
        }
        Vente vente = new Vente(numeroVente, uuidVente, matriculeCommercial, codeClient, date, modeReglement, typeVente, LocalDateTime.now());
        vente.lignes.addAll(lignes);
        vente.photoProduits = photoProduits;
        return vente;
    }

    public static Vente reconstituer(Long id, String numeroVente, String uuidVente, String matriculeCommercial,
                                      String codeClient, LocalDate date, List<LigneVente> lignes,
                                      ModeReglement modeReglement, TypeVente typeVente, StatutVente statut,
                                      SyncStatus syncStatus, String coordonneesGPS, String idFeuillRoute,
                                      String observations, boolean signatureClient, String photoProduits,
                                      LocalDateTime dateCreation, LocalDateTime dateModification,
                                      Long version) {
        Vente v = new Vente(numeroVente, uuidVente, matriculeCommercial, codeClient, date, modeReglement, typeVente, dateCreation);
        v.id = id;
        v.lignes.addAll(lignes != null ? lignes : List.of());
        v.statut = statut;
        v.syncStatus = syncStatus;
        v.coordonneesGPS = coordonneesGPS;
        v.idFeuillRoute = idFeuillRoute;
        v.observations = observations;
        v.signatureClient = signatureClient;
        v.photoProduits = photoProduits;
        v.dateModification = dateModification;
        v.version = version;
        return v;
    }

    public void finaliser() {
        if (statut != StatutVente.EN_COURS) {
            throw new IllegalStateException("Seule une vente EN_COURS peut être finalisée");
        }
        this.statut = StatutVente.FINALISEE;
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new VenteFinaliseEvent(numeroVente, matriculeCommercial, codeClient,
            montantTotal(), date));
    }

    public void signerClient() {
        this.signatureClient = true;
    }

    /**
     * Enregistre la position relevée au moment de la transaction.
     *
     * <p>Élément de traçabilité : atteste du lieu réel de la vente terrain. La
     * position n'est pas modifiable une fois la vente finalisée, afin qu'elle
     * conserve sa valeur probante.</p>
     *
     * @param coordonneesGPS couple « latitude,longitude » ; ignoré si absent, le
     *                       relevé pouvant échouer légitimement (GPS indisponible)
     */
    public void enregistrerPosition(String coordonneesGPS) {
        if (coordonneesGPS == null || coordonneesGPS.isBlank()) {
            return;
        }
        if (statut == StatutVente.FINALISEE) {
            throw new IllegalStateException("La position d'une vente finalisée ne peut plus être modifiée");
        }
        this.coordonneesGPS = coordonneesGPS;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Rattache la vente à la feuille de route de la tournée en cours.
     *
     * <p>Permet de reconstituer l'activité d'une tournée et d'imputer la vente au
     * bon déplacement. Sans ce rattachement, la vente existe mais reste orpheline
     * de son contexte de tournée.</p>
     *
     * @param idFeuillRoute identifiant de la feuille de route ; ignoré si absent,
     *                      une vente pouvant être réalisée hors tournée planifiée
     */
    public void rattacherATournee(String idFeuillRoute) {
        if (idFeuillRoute == null || idFeuillRoute.isBlank()) {
            return;
        }
        if (statut == StatutVente.FINALISEE) {
            throw new IllegalStateException("Une vente finalisée ne peut plus être rattachée à une tournée");
        }
        this.idFeuillRoute = idFeuillRoute;
        this.dateModification = LocalDateTime.now();
    }

    public void annuler(String motif) {
        if (statut == StatutVente.FINALISEE) {
            throw new IllegalStateException("Une vente finalisée ne peut pas être annulée — créer un avoir (§4.3)");
        }
        this.observations = motif;
        this.statut = StatutVente.ANNULEE;
        this.dateModification = LocalDateTime.now();
    }

    public void marquerSyncEnAttente() {
        this.syncStatus = SyncStatus.PENDING;
    }

    public void marquerSyncConflit() {
        this.syncStatus = SyncStatus.CONFLICT;
    }

    public void marquerSyncFait() {
        this.syncStatus = SyncStatus.SYNCED;
    }

    public double montantHT() {
        return lignes.stream().mapToDouble(LigneVente::montantHT).sum();
    }

    public double montantTotal() {
        return montantHT();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumeroVente() { return numeroVente; }
    public String getUuidVente() { return uuidVente; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public String getCodeClient() { return codeClient; }
    public LocalDate getDate() { return date; }
    public List<LigneVente> getLignes() { return Collections.unmodifiableList(lignes); }
    public ModeReglement getModeReglement() { return modeReglement; }
    public TypeVente getTypeVente() { return typeVente; }
    public StatutVente getStatut() { return statut; }
    public SyncStatus getSyncStatus() { return syncStatus; }
    public String getCoordonneesGPS() { return coordonneesGPS; }
    public String getIdFeuillRoute() { return idFeuillRoute; }
    public String getObservations() { return observations; }
    public boolean isSignatureClient() { return signatureClient; }
    public String getPhotoProduits() { return photoProduits; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vente)) return false;
        return Objects.equals(numeroVente, ((Vente) o).numeroVente);
    }
    @Override public int hashCode() { return Objects.hash(numeroVente); }
}
