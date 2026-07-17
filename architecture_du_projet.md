# Architecture Complète du Projet TANTY ERP

## Vue d'ensemble

Ce document décrit la structure complète du backend Java/Spring Boot (`Api_tanty`) et du frontend Angular (`frontend_erp_tanty`).
Pour chaque fichier backend, les déclarations (classes, interfaces, enums), champs et méthodes principaux sont extraits.

---

# Backend - Api_tanty

**Racine source** : `C:\Users\User\Downloads\Api_tanty\src\main\java\com\NTFOODS\Api_tanty`

**Nombre total de fichiers Java** : 908

## Arborescence du backend (par module)

### Module `administration`

- `modules\administration\application\ParametresAdminService.java`
- `modules\administration\application\dto\ParametresResponse.java`
- `modules\administration\application\utilisateur\dto\response\UtilisateurResponse.java`
- `modules\administration\application\utilisateur\mapper\UtilisateurAdminMapper.java`
- `modules\administration\application\utilisateur\usecase\ChangerRoleUtilisateurUseCase.java`
- `modules\administration\application\utilisateur\usecase\ConsulterUtilisateurUseCase.java`
- `modules\administration\application\utilisateur\usecase\DebloquerUtilisateurUseCase.java`
- `modules\administration\application\utilisateur\usecase\GererStatutUtilisateurUseCase.java`
- `modules\administration\application\utilisateur\usecase\ListerUtilisateursUseCase.java`
- `modules\administration\application\utilisateur\usecase\ReinitialiserMotDePasseUseCase.java`
- `modules\administration\infrastructure\persistence\ReferentielStringJpaEntity.java`
- `modules\administration\infrastructure\persistence\ReferentielStringJpaRepository.java`
- `modules\administration\presentation\controller\AdministrationController.java`
- `modules\administration\presentation\controller\ParametresAdminController.java`

### Module `auth`

- `modules\auth\application\auth\dto\DemanderOtpRequest.java`
- `modules\auth\application\auth\dto\LoginRequest.java`
- `modules\auth\application\auth\dto\LoginResponse.java`
- `modules\auth\application\auth\dto\ModifierProfilRequest.java`
- `modules\auth\application\auth\dto\ModifierUtilisateurRequest.java`
- `modules\auth\application\auth\dto\OtpResponse.java`
- `modules\auth\application\auth\dto\ProfilResponse.java`
- `modules\auth\application\auth\dto\RegisterRequest.java`
- `modules\auth\application\auth\dto\VerifierOtpRequest.java`
- `modules\auth\application\auth\usecase\CreerUtilisateurTransactionnel.java`
- `modules\auth\application\auth\usecase\DemanderOtpUseCase.java`
- `modules\auth\application\auth\usecase\GererProfilUseCase.java`
- `modules\auth\application\auth\usecase\GererUtilisateursUseCase.java`
- `modules\auth\application\auth\usecase\LoginUseCase.java`
- `modules\auth\application\auth\usecase\LogoutUseCase.java`
- `modules\auth\application\auth\usecase\RegisterUseCase.java`
- `modules\auth\application\auth\usecase\VerifierOtpUseCase.java`
- `modules\auth\application\rh\dto\request\CreerEmployeRequest.java`
- `modules\auth\application\rh\dto\response\EmployeResponse.java`
- `modules\auth\application\rh\mapper\EmployeMapper.java`
- `modules\auth\application\rh\usecase\CreerEmployeUseCase.java`
- `modules\auth\application\rh\usecase\ListerMachinistesUseCase.java`
- `modules\auth\domain\rh\model\Employe.java`
- `modules\auth\domain\rh\repository\EmployeRepository.java`
- `modules\auth\domain\utilisateur\aggregate\Utilisateur.java`
- `modules\auth\domain\utilisateur\enums\Role.java`
- `modules\auth\domain\utilisateur\events\CompteBloqueEvent.java`
- `modules\auth\domain\utilisateur\events\UtilisateurConnecteEvent.java`
- `modules\auth\domain\utilisateur\events\UtilisateurCreeEvent.java`
- `modules\auth\domain\utilisateur\exceptions\AdminException.java`
- `modules\auth\domain\utilisateur\exceptions\CompteBloqueException.java`
- `modules\auth\domain\utilisateur\exceptions\CompteDesactiveException.java`
- `modules\auth\domain\utilisateur\exceptions\CredentialsInvalidesException.java`
- `modules\auth\domain\utilisateur\exceptions\EmailDejaUtiliseException.java`
- `modules\auth\domain\utilisateur\exceptions\UtilisateurNonTrouveException.java`
- `modules\auth\domain\utilisateur\repository\UtilisateurRepository.java`
- `modules\auth\domain\utilisateur\valueobject\Email.java`
- `modules\auth\domain\utilisateur\valueobject\Matricule.java`
- `modules\auth\domain\utilisateur\valueobject\MotDePasse.java`
- `modules\auth\domain\utilisateur\valueobject\NomComplet.java`
- `modules\auth\infrastructure\persistence\jpa\OtpCodeJpaEntity.java`
- `modules\auth\infrastructure\persistence\jpa\OtpCodeJpaRepository.java`
- `modules\auth\infrastructure\persistence\jpa\UtilisateurJpaEntity.java`
- `modules\auth\infrastructure\persistence\jpa\UtilisateurJpaRepository.java`
- `modules\auth\infrastructure\persistence\matricule\MatriculeSequenceService.java`
- `modules\auth\infrastructure\persistence\repository\UtilisateurRepositoryImpl.java`
- `modules\auth\infrastructure\persistence\rh\jpa\EmployeJpaEntity.java`
- `modules\auth\infrastructure\persistence\rh\jpa\EmployeJpaRepository.java`
- `modules\auth\infrastructure\persistence\rh\mapper\EmployePersistenceMapper.java`
- `modules\auth\infrastructure\persistence\rh\matricule\GenerateurMatriculeEmploye.java`
- `modules\auth\infrastructure\persistence\rh\repository\EmployeRepositoryImpl.java`
- `modules\auth\infrastructure\security\JwtAuthFilter.java`
- `modules\auth\infrastructure\security\JwtService.java`
- `modules\auth\infrastructure\security\OtpService.java`
- `modules\auth\infrastructure\security\SecurityConfig.java`
- `modules\auth\presentation\controller\AuthController.java`

### Module `commercial`

- `modules\commercial\application\carburant\dto\response\CarburantResponse.java`
- `modules\commercial\application\carburant\mapper\CarburantMapper.java`
- `modules\commercial\application\client\dto\response\ClientResponse.java`
- `modules\commercial\application\client\mapper\ClientMapper.java`
- `modules\commercial\application\commandedistance\dto\response\CommandeDistanceResponse.java`
- `modules\commercial\application\commandedistance\mapper\CommandeDistanceMapper.java`
- `modules\commercial\application\dashboard\dto\FicheSyntheseResponse.java`
- `modules\commercial\application\dashboard\usecase\DashboardCommercialUseCase.java`
- `modules\commercial\application\dashboard\usecase\FicheSyntheseUseCase.java`
- `modules\commercial\application\feuillroute\dto\response\FeuillRouteResponse.java`
- `modules\commercial\application\feuillroute\mapper\FeuillRouteMapper.java`
- `modules\commercial\application\precommande\dto\response\PreCommandeResponse.java`
- `modules\commercial\application\precommande\mapper\PreCommandeMapper.java`
- `modules\commercial\application\prospect\dto\response\ProspectResponse.java`
- `modules\commercial\application\prospect\mapper\ProspectMapper.java`
- `modules\commercial\application\rapport\usecase\CalculerPrimesHebdoUseCase.java`
- `modules\commercial\application\rapport\usecase\GenererRapportRendementCarburantUseCase.java`
- `modules\commercial\application\rapport\usecase\GenererRapportTourneeUseCase.java`
- `modules\commercial\application\rapport\usecase\GenererRapportVentesUseCase.java`
- `modules\commercial\application\recouvrement\dto\response\RecouvrementResponse.java`
- `modules\commercial\application\recouvrement\mapper\RecouvrementMapper.java`
- `modules\commercial\application\tarification\dto\request\AccorderPrixExceptionnelRequest.java`
- `modules\commercial\application\tarification\dto\request\DefinirGrilleTarifaireRequest.java`
- `modules\commercial\application\tarification\dto\response\GrilleTarifaireResponse.java`
- `modules\commercial\application\tarification\dto\response\PrixExceptionnelResponse.java`
- `modules\commercial\application\tarification\mapper\GrilleTarifaireMapper.java`
- `modules\commercial\application\tarification\mapper\PrixExceptionnelMapper.java`
- `modules\commercial\application\tarification\service\TarificationGuardService.java`
- `modules\commercial\application\tarification\usecase\AccorderPrixExceptionnelUseCase.java`
- `modules\commercial\application\tarification\usecase\DefinirGrilleTarifaireUseCase.java`
- `modules\commercial\application\vente\dto\response\VenteResponse.java`
- `modules\commercial\application\vente\mapper\VenteMapper.java`
- `modules\commercial\application\vente\usecase\DeclencherTransactionNonMarchandeUseCase.java`
- `modules\commercial\domain\carburant\aggregate\DemandeCarburant.java`
- `modules\commercial\domain\carburant\enums\StatutCarburant.java`
- `modules\commercial\domain\carburant\repository\CarburantRepository.java`
- `modules\commercial\domain\carburant\specification\CarburantSpecification.java`
- `modules\commercial\domain\client\aggregate\Client.java`
- `modules\commercial\domain\client\enums\TypeClient.java`
- `modules\commercial\domain\client\repository\ClientRepository.java`
- `modules\commercial\domain\commandedistance\aggregate\CommandeDistance.java`
- `modules\commercial\domain\commandedistance\entity\LigneCommandeDistance.java`
- `modules\commercial\domain\commandedistance\enums\CanalCommande.java`
- `modules\commercial\domain\commandedistance\enums\StatutCommandeDistance.java`
- `modules\commercial\domain\commandedistance\repository\CommandeDistanceRepository.java`
- `modules\commercial\domain\feuillroute\aggregate\FeuillRoute.java`
- `modules\commercial\domain\feuillroute\entity\ArretFeuillRoute.java`
- `modules\commercial\domain\feuillroute\enums\SaisieArret.java`
- `modules\commercial\domain\feuillroute\enums\StatutArret.java`
- `modules\commercial\domain\feuillroute\enums\StatutFeuillRoute.java`
- `modules\commercial\domain\feuillroute\repository\FeuillRouteRepository.java`
- `modules\commercial\domain\marche\model\BoutiquePotentielle.java`
- `modules\commercial\domain\marche\model\ZoneDeVente.java`
- `modules\commercial\domain\marche\repository\BoutiquePotentielleRepository.java`
- `modules\commercial\domain\marche\repository\ZoneDeVenteRepository.java`
- `modules\commercial\domain\precommande\aggregate\PreCommande.java`
- `modules\commercial\domain\precommande\entity\LignePreCommande.java`
- `modules\commercial\domain\precommande\enums\InfoClientEnum.java`
- `modules\commercial\domain\precommande\enums\StatutPreCommande.java`
- `modules\commercial\domain\precommande\events\PreCommandeValideeEvent.java`
- `modules\commercial\domain\precommande\repository\PreCommandeRepository.java`
- `modules\commercial\domain\precommande\specification\PreCommandeSpecification.java`
- `modules\commercial\domain\prospect\aggregate\Prospect.java`
- `modules\commercial\domain\prospect\enums\CanalProspection.java`
- `modules\commercial\domain\prospect\enums\StatutProspect.java`
- `modules\commercial\domain\prospect\repository\ProspectRepository.java`
- `modules\commercial\domain\recouvrement\aggregate\Recouvrement.java`
- `modules\commercial\domain\recouvrement\enums\StatutRecouvrement.java`
- `modules\commercial\domain\recouvrement\repository\RecouvrementRepository.java`
- `modules\commercial\domain\recouvrement\specification\RecouvrementSpecification.java`
- `modules\commercial\domain\tarification\event\PrixExceptionnelAccordeEvent.java`
- `modules\commercial\domain\tarification\model\GrilleTarifaire.java`
- `modules\commercial\domain\tarification\model\PrixExceptionnel.java`
- `modules\commercial\domain\tarification\repository\GrilleTarifaireRepository.java`
- `modules\commercial\domain\tarification\repository\PrixExceptionnelRepository.java`
- `modules\commercial\domain\terminal\aggregate\Terminal.java`
- `modules\commercial\domain\terminal\repository\TerminalRepository.java`
- `modules\commercial\domain\vehicule\aggregate\Vehicule.java`
- `modules\commercial\domain\vehicule\repository\VehiculeRepository.java`
- `modules\commercial\domain\vente\aggregate\FactureAvoir.java`
- `modules\commercial\domain\vente\aggregate\Vente.java`
- `modules\commercial\domain\vente\entity\LigneVente.java`
- `modules\commercial\domain\vente\enums\ModeReglement.java`
- `modules\commercial\domain\vente\enums\StatutVente.java`
- `modules\commercial\domain\vente\enums\SyncStatus.java`
- `modules\commercial\domain\vente\enums\TypeVente.java`
- `modules\commercial\domain\vente\events\VenteFinaliseEvent.java`
- `modules\commercial\domain\vente\repository\FactureAvoirRepository.java`
- `modules\commercial\domain\vente\repository\VenteRepository.java`
- `modules\commercial\domain\vente\specification\VenteSpecification.java`
- `modules\commercial\infrastructure\persistence\carburant\jpa\CarburantJpaEntity.java`
- `modules\commercial\infrastructure\persistence\carburant\jpa\CarburantJpaRepository.java`
- `modules\commercial\infrastructure\persistence\carburant\repository\CarburantRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\client\jpa\ClientJpaEntity.java`
- `modules\commercial\infrastructure\persistence\client\jpa\ClientJpaRepository.java`
- `modules\commercial\infrastructure\persistence\client\repository\ClientRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\commandedistance\jpa\CommandeDistanceJpaEntity.java`
- `modules\commercial\infrastructure\persistence\commandedistance\jpa\CommandeDistanceJpaRepository.java`
- `modules\commercial\infrastructure\persistence\commandedistance\jpa\LigneCommandeDistanceJpaEntity.java`
- `modules\commercial\infrastructure\persistence\commandedistance\repository\CommandeDistanceRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\feuillroute\jpa\ArretFeuillRouteJpaEntity.java`
- `modules\commercial\infrastructure\persistence\feuillroute\jpa\FeuillRouteJpaEntity.java`
- `modules\commercial\infrastructure\persistence\feuillroute\jpa\FeuillRouteJpaRepository.java`
- `modules\commercial\infrastructure\persistence\feuillroute\repository\FeuillRouteRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\marche\jpa\BoutiquePotentielleJpaEntity.java`
- `modules\commercial\infrastructure\persistence\marche\jpa\BoutiquePotentielleJpaRepository.java`
- `modules\commercial\infrastructure\persistence\marche\jpa\ZoneDeVenteJpaEntity.java`
- `modules\commercial\infrastructure\persistence\marche\jpa\ZoneDeVenteJpaRepository.java`
- `modules\commercial\infrastructure\persistence\marche\repository\BoutiquePotentielleRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\marche\repository\ZoneDeVenteRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\precommande\jpa\LignePreCommandeJpaEntity.java`
- `modules\commercial\infrastructure\persistence\precommande\jpa\PreCommandeJpaEntity.java`
- `modules\commercial\infrastructure\persistence\precommande\jpa\PreCommandeJpaRepository.java`
- `modules\commercial\infrastructure\persistence\precommande\repository\PreCommandeRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\prospect\jpa\ProspectJpaEntity.java`
- `modules\commercial\infrastructure\persistence\prospect\jpa\ProspectJpaRepository.java`
- `modules\commercial\infrastructure\persistence\prospect\repository\ProspectRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\recouvrement\jpa\RecouvrementJpaEntity.java`
- `modules\commercial\infrastructure\persistence\recouvrement\jpa\RecouvrementJpaRepository.java`
- `modules\commercial\infrastructure\persistence\recouvrement\repository\RecouvrementRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\tarification\jpa\GrilleTarifaireJpaEntity.java`
- `modules\commercial\infrastructure\persistence\tarification\jpa\GrilleTarifaireJpaRepository.java`
- `modules\commercial\infrastructure\persistence\tarification\jpa\PrixExceptionnelJpaEntity.java`
- `modules\commercial\infrastructure\persistence\tarification\jpa\PrixExceptionnelJpaRepository.java`
- `modules\commercial\infrastructure\persistence\tarification\repository\GrilleTarifaireRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\tarification\repository\PrixExceptionnelRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\terminal\jpa\TerminalJpaEntity.java`
- `modules\commercial\infrastructure\persistence\terminal\jpa\TerminalJpaRepository.java`
- `modules\commercial\infrastructure\persistence\terminal\repository\TerminalRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\vehicule\jpa\VehiculeJpaEntity.java`
- `modules\commercial\infrastructure\persistence\vehicule\jpa\VehiculeJpaRepository.java`
- `modules\commercial\infrastructure\persistence\vehicule\repository\VehiculeRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\vente\jpa\FactureAvoirJpaEntity.java`
- `modules\commercial\infrastructure\persistence\vente\jpa\FactureAvoirJpaRepository.java`
- `modules\commercial\infrastructure\persistence\vente\jpa\LigneVenteJpaEntity.java`
- `modules\commercial\infrastructure\persistence\vente\jpa\VenteJpaEntity.java`
- `modules\commercial\infrastructure\persistence\vente\jpa\VenteJpaRepository.java`
- `modules\commercial\infrastructure\persistence\vente\repository\FactureAvoirRepositoryImpl.java`
- `modules\commercial\infrastructure\persistence\vente\repository\VenteRepositoryImpl.java`
- `modules\commercial\presentation\controller\BoutiquePotentielleController.java`
- `modules\commercial\presentation\controller\CarburantController.java`
- `modules\commercial\presentation\controller\ClientController.java`
- `modules\commercial\presentation\controller\CommandeDistanceController.java`
- `modules\commercial\presentation\controller\DashboardCommercialController.java`
- `modules\commercial\presentation\controller\FeuillRouteController.java`
- `modules\commercial\presentation\controller\PreCommandeController.java`
- `modules\commercial\presentation\controller\ProspectController.java`
- `modules\commercial\presentation\controller\RapportCommercialController.java`
- `modules\commercial\presentation\controller\RecouvrementController.java`
- `modules\commercial\presentation\controller\TarificationController.java`
- `modules\commercial\presentation\controller\TerminalController.java`
- `modules\commercial\presentation\controller\VehiculeController.java`
- `modules\commercial\presentation\controller\VenteController.java`
- `modules\commercial\presentation\controller\ZoneDeVenteController.java`

### Module `commercialisation`

- `modules\commercialisation\application\dto\CampagneResponse.java`
- `modules\commercialisation\application\dto\CreerCampagneRequest.java`
- `modules\commercialisation\application\dto\CreerPromotionRequest.java`
- `modules\commercialisation\application\dto\PromotionResponse.java`
- `modules\commercialisation\application\usecase\GererCampagneUseCase.java`
- `modules\commercialisation\application\usecase\GererPromotionUseCase.java`
- `modules\commercialisation\domain\aggregate\CampagneMarketing.java`
- `modules\commercialisation\domain\aggregate\Promotion.java`
- `modules\commercialisation\domain\enums\StatutCampagne.java`
- `modules\commercialisation\domain\enums\StatutPromotion.java`
- `modules\commercialisation\domain\enums\TypeCampagne.java`
- `modules\commercialisation\domain\enums\TypePromotion.java`
- `modules\commercialisation\domain\repository\CampagneMarketingRepository.java`
- `modules\commercialisation\domain\repository\PromotionRepository.java`
- `modules\commercialisation\infrastructure\persistence\jpa\CampagneMarketingJpaEntity.java`
- `modules\commercialisation\infrastructure\persistence\jpa\CampagneMarketingJpaRepository.java`
- `modules\commercialisation\infrastructure\persistence\jpa\PromotionJpaEntity.java`
- `modules\commercialisation\infrastructure\persistence\jpa\PromotionJpaRepository.java`
- `modules\commercialisation\infrastructure\persistence\repository\CampagneMarketingRepositoryImpl.java`
- `modules\commercialisation\infrastructure\persistence\repository\PromotionRepositoryImpl.java`
- `modules\commercialisation\presentation\controller\CommercialisationController.java`

### Module `comptabilite`

- `modules\comptabilite\application\caisse\dto\response\CaisseResponse.java`
- `modules\comptabilite\application\caisse\mapper\CaisseMapper.java`
- `modules\comptabilite\application\dashboard\usecase\DashboardComptabiliteUseCase.java`
- `modules\comptabilite\application\decaissement\dto\response\DecaissementResponse.java`
- `modules\comptabilite\application\decaissement\mapper\DecaissementMapper.java`
- `modules\comptabilite\application\facture\dto\response\FactureResponse.java`
- `modules\comptabilite\application\facture\mapper\FactureMapper.java`
- `modules\comptabilite\application\objectif\dto\request\DefinirObjectifRequest.java`
- `modules\comptabilite\application\objectif\dto\response\ObjectifCommercialResponse.java`
- `modules\comptabilite\application\objectif\mapper\ObjectifCommercialMapper.java`
- `modules\comptabilite\application\objectif\usecase\DefinirObjectifUseCase.java`
- `modules\comptabilite\application\prime\dto\response\PrimeResponse.java`
- `modules\comptabilite\application\prime\mapper\PrimeMapper.java`
- `modules\comptabilite\application\rapport\usecase\GenererRapportDecaissementsUseCase.java`
- `modules\comptabilite\application\rapport\usecase\GenererRapportFacturesUseCase.java`
- `modules\comptabilite\application\rapport\usecase\GenererRapportPrimesUseCase.java`
- `modules\comptabilite\application\rapport\usecase\GenererRapportVersementsUseCase.java`
- `modules\comptabilite\application\versement\command\EnregistrerVersementCommand.java`
- `modules\comptabilite\application\versement\command\JustifierEcartCommand.java`
- `modules\comptabilite\application\versement\command\ValiderVersementCommand.java`
- `modules\comptabilite\application\versement\dto\response\VersementResponse.java`
- `modules\comptabilite\application\versement\mapper\VersementMapper.java`
- `modules\comptabilite\application\versement\service\ReconciliationService.java`
- `modules\comptabilite\application\versement\usecase\enregistrer\EnregistrerVersementUseCase.java`
- `modules\comptabilite\application\versement\usecase\valider\ValiderVersementUseCase.java`
- `modules\comptabilite\domain\caisse\aggregate\JournalCaisse.java`
- `modules\comptabilite\domain\caisse\entity\LigneCaisse.java`
- `modules\comptabilite\domain\caisse\enums\TypeMouvementCaisse.java`
- `modules\comptabilite\domain\caisse\events\SeuilSecurisationAtteintEvent.java`
- `modules\comptabilite\domain\caisse\repository\JournalCaisseRepository.java`
- `modules\comptabilite\domain\caisse\specification\CaisseSpecification.java`
- `modules\comptabilite\domain\decaissement\aggregate\Decaissement.java`
- `modules\comptabilite\domain\decaissement\enums\StatutDecaissement.java`
- `modules\comptabilite\domain\decaissement\enums\TypeDepense.java`
- `modules\comptabilite\domain\decaissement\repository\DecaissementRepository.java`
- `modules\comptabilite\domain\facture\aggregate\Facture.java`
- `modules\comptabilite\domain\facture\entity\LigneFacture.java`
- `modules\comptabilite\domain\facture\enums\StatutFacture.java`
- `modules\comptabilite\domain\facture\enums\TypeFacture.java`
- `modules\comptabilite\domain\facture\events\FactureEmiseEvent.java`
- `modules\comptabilite\domain\facture\repository\FactureRepository.java`
- `modules\comptabilite\domain\facture\specification\FactureSpecification.java`
- `modules\comptabilite\domain\objectif\aggregate\ObjectifCommercial.java`
- `modules\comptabilite\domain\objectif\repository\ObjectifCommercialRepository.java`
- `modules\comptabilite\domain\prime\aggregate\PrimeCommerciale.java`
- `modules\comptabilite\domain\prime\enums\StatutPrime.java`
- `modules\comptabilite\domain\prime\events\PrimeValideeEvent.java`
- `modules\comptabilite\domain\prime\repository\PrimeRepository.java`
- `modules\comptabilite\domain\prime\specification\PrimeSpecification.java`
- `modules\comptabilite\domain\versement\aggregate\Versement.java`
- `modules\comptabilite\domain\versement\enums\StatutVersement.java`
- `modules\comptabilite\domain\versement\enums\TypeVersement.java`
- `modules\comptabilite\domain\versement\events\AlerteRougeVersementEvent.java`
- `modules\comptabilite\domain\versement\events\VersementValideEvent.java`
- `modules\comptabilite\domain\versement\repository\VersementRepository.java`
- `modules\comptabilite\domain\versement\specification\VersementSpecification.java`
- `modules\comptabilite\infrastructure\persistence\caisse\jpa\JournalCaisseJpaEntity.java`
- `modules\comptabilite\infrastructure\persistence\caisse\jpa\JournalCaisseJpaRepository.java`
- `modules\comptabilite\infrastructure\persistence\caisse\jpa\LigneCaisseJpaEntity.java`
- `modules\comptabilite\infrastructure\persistence\caisse\repository\JournalCaisseRepositoryImpl.java`
- `modules\comptabilite\infrastructure\persistence\decaissement\jpa\DecaissementJpaEntity.java`
- `modules\comptabilite\infrastructure\persistence\decaissement\jpa\DecaissementJpaRepository.java`
- `modules\comptabilite\infrastructure\persistence\decaissement\repository\DecaissementRepositoryImpl.java`
- `modules\comptabilite\infrastructure\persistence\facture\jpa\FactureJpaEntity.java`
- `modules\comptabilite\infrastructure\persistence\facture\jpa\FactureJpaRepository.java`
- `modules\comptabilite\infrastructure\persistence\facture\jpa\LigneFactureJpaEntity.java`
- `modules\comptabilite\infrastructure\persistence\facture\repository\FactureRepositoryImpl.java`
- `modules\comptabilite\infrastructure\persistence\objectif\jpa\ObjectifCommercialJpaEntity.java`
- `modules\comptabilite\infrastructure\persistence\objectif\jpa\ObjectifCommercialJpaRepository.java`
- `modules\comptabilite\infrastructure\persistence\objectif\repository\ObjectifCommercialRepositoryImpl.java`
- `modules\comptabilite\infrastructure\persistence\prime\jpa\PrimeJpaEntity.java`
- `modules\comptabilite\infrastructure\persistence\prime\jpa\PrimeJpaRepository.java`
- `modules\comptabilite\infrastructure\persistence\prime\repository\PrimeRepositoryImpl.java`
- `modules\comptabilite\infrastructure\persistence\versement\jpa\VersementJpaEntity.java`
- `modules\comptabilite\infrastructure\persistence\versement\jpa\VersementJpaRepository.java`
- `modules\comptabilite\infrastructure\persistence\versement\repository\VersementRepositoryImpl.java`
- `modules\comptabilite\presentation\controller\CaisseController.java`
- `modules\comptabilite\presentation\controller\DashboardComptabiliteController.java`
- `modules\comptabilite\presentation\controller\DecaissementController.java`
- `modules\comptabilite\presentation\controller\FactureController.java`
- `modules\comptabilite\presentation\controller\ObjectifCommercialController.java`
- `modules\comptabilite\presentation\controller\PrimeController.java`
- `modules\comptabilite\presentation\controller\RapportComptabiliteController.java`
- `modules\comptabilite\presentation\controller\VersementController.java`

### Module `controle`

- `modules\controle\application\ControleDashboardService.java`
- `modules\controle\application\dto\ControleDashboardResponse.java`
- `modules\controle\infrastructure\persistence\jpa\BudgetJpaEntity.java`
- `modules\controle\infrastructure\persistence\jpa\BudgetJpaRepository.java`
- `modules\controle\infrastructure\persistence\jpa\MargeProduitJpaEntity.java`
- `modules\controle\infrastructure\persistence\jpa\MargeProduitJpaRepository.java`
- `modules\controle\infrastructure\persistence\jpa\VarianceJpaEntity.java`
- `modules\controle\infrastructure\persistence\jpa\VarianceJpaRepository.java`
- `modules\controle\presentation\controller\ControleController.java`

### Module `dg`

- `modules\dg\application\dto\response\ClassementCommercialResponse.java`
- `modules\dg\application\dto\response\JournalAuditResponse.java`
- `modules\dg\application\dto\response\KpisDgResponse.java`
- `modules\dg\application\dto\response\ParametreSystemeResponse.java`
- `modules\dg\application\dto\response\UtilisateurDgResponse.java`
- `modules\dg\application\rapport\AnomalieResponse.java`
- `modules\dg\application\rapport\AnomaliesService.java`
- `modules\dg\application\rapport\PeriodeComparaison.java`
- `modules\dg\application\rapport\RapportComparatifResponse.java`
- `modules\dg\application\rapport\RapportComparatifService.java`
- `modules\dg\application\rapport\TauxOccupationMarcheService.java`
- `modules\dg\application\rapport\TauxOccupationSecteurResponse.java`
- `modules\dg\application\rapport\ValidationEnAttenteResponse.java`
- `modules\dg\application\rapport\ValidationsEnAttenteService.java`
- `modules\dg\application\rapport\ZonePerformanceResponse.java`
- `modules\dg\application\rapport\ZonePerformanceService.java`
- `modules\dg\application\usecase\ConsulterJournalAuditUseCase.java`
- `modules\dg\application\usecase\DashboardFinancierDgUseCase.java`
- `modules\dg\application\usecase\DashboardProductionDgUseCase.java`
- `modules\dg\application\usecase\DashboardStockDgUseCase.java`
- `modules\dg\application\usecase\GererParametresSystemeUseCase.java`
- `modules\dg\application\usecase\ListerUtilisateursDgUseCase.java`
- `modules\dg\application\usecase\ObtenirClassementDgUseCase.java`
- `modules\dg\application\usecase\ObtenirKpisDgUseCase.java`
- `modules\dg\presentation\controller\DgController.java`

### Module `other`

- `ApiTantyApplication.java`

### Module `production`

- `modules\production\application\affectation\dto\request\CreerAffectationJourRequest.java`
- `modules\production\application\affectation\dto\request\DetailsRealisationPosteRequest.java`
- `modules\production\application\affectation\dto\request\LigneAffectationRequest.java`
- `modules\production\application\affectation\dto\request\ProductionIndividuelleRequest.java`
- `modules\production\application\affectation\dto\request\SaisirRealisationPosteRequest.java`
- `modules\production\application\affectation\dto\response\AffectationJournaliereResponse.java`
- `modules\production\application\affectation\dto\response\ClassementExecuteurResponse.java`
- `modules\production\application\affectation\dto\response\ComparaisonFutsProductionResponse.java`
- `modules\production\application\affectation\dto\response\DetailsRealisationPosteResponse.java`
- `modules\production\application\affectation\dto\response\RapportHebdomadaireProductionResponse.java`
- `modules\production\application\affectation\mapper\AffectationMapper.java`
- `modules\production\application\affectation\usecase\ClassementExecuteursHebdomadaireUseCase.java`
- `modules\production\application\affectation\usecase\ComparaisonFutsProductionUseCase.java`
- `modules\production\application\affectation\usecase\CreerAffectationJourUseCase.java`
- `modules\production\application\affectation\usecase\RapportHebdomadaireProductionUseCase.java`
- `modules\production\application\affectation\usecase\SaisirRealisationPosteUseCase.java`
- `modules\production\application\affectation\usecase\ValiderAffectationUseCase.java`
- `modules\production\application\besoin\dto\request\SignalerBesoinRequest.java`
- `modules\production\application\besoin\dto\response\ExpressionBesoinResponse.java`
- `modules\production\application\besoin\mapper\ExpressionBesoinMapper.java`
- `modules\production\application\besoin\usecase\SignalerBesoinUseCase.java`
- `modules\production\application\besoin\usecase\TraiterBesoinUseCase.java`
- `modules\production\application\broyage\dto\request\AutoriserDepassementBroyageRequest.java`
- `modules\production\application\broyage\dto\request\CloturerSessionBroyageRequest.java`
- `modules\production\application\broyage\dto\request\EnregistrerRealisationBroyageRequest.java`
- `modules\production\application\broyage\dto\request\JustifierNonValidationBroyageRequest.java`
- `modules\production\application\broyage\dto\request\ObjectifMachinisteRequest.java`
- `modules\production\application\broyage\dto\request\OuvrirSessionBroyageRequest.java`
- `modules\production\application\broyage\dto\response\ClassementMachinisteResponse.java`
- `modules\production\application\broyage\dto\response\SessionBroyageResponse.java`
- `modules\production\application\broyage\mapper\SessionBroyageMapper.java`
- `modules\production\application\broyage\usecase\AutoriserDepassementBroyageUseCase.java`
- `modules\production\application\broyage\usecase\ClassementMachinistesUseCase.java`
- `modules\production\application\broyage\usecase\CloturerSessionBroyageUseCase.java`
- `modules\production\application\broyage\usecase\EnregistrerRealisationBroyageUseCase.java`
- `modules\production\application\broyage\usecase\JustifierNonValidationBroyageUseCase.java`
- `modules\production\application\broyage\usecase\OuvrirSessionBroyageUseCase.java`
- `modules\production\application\dashboard\usecase\DashboardProductionUseCase.java`
- `modules\production\application\dosage\dto\request\EnregistrerSessionDosageRequest.java`
- `modules\production\application\dosage\dto\response\PredictionProduitFiniResponse.java`
- `modules\production\application\dosage\dto\response\SessionDosageResponse.java`
- `modules\production\application\dosage\dto\response\StatistiquesFutsResponse.java`
- `modules\production\application\dosage\mapper\SessionDosageMapper.java`
- `modules\production\application\dosage\service\DocumentsDosageService.java`
- `modules\production\application\dosage\service\RegistreDosageExportService.java`
- `modules\production\application\dosage\usecase\PredireProduitFiniDepuisFutsUseCase.java`
- `modules\production\application\dosage\usecase\StatistiquesFutsUseCase.java`
- `modules\production\application\fiche\dto\request\EnregistrerFicheRequest.java`
- `modules\production\application\fiche\dto\request\ModifierFicheRequest.java`
- `modules\production\application\fiche\dto\response\FicheResponse.java`
- `modules\production\application\fiche\mapper\FicheMapper.java`
- `modules\production\application\fiche\usecase\enregistrer\EnregistrerFicheUseCase.java`
- `modules\production\application\fiche\usecase\modifier\ModifierFicheUseCase.java`
- `modules\production\application\lot\dto\request\DeclarerLotRequest.java`
- `modules\production\application\lot\dto\response\LotResponse.java`
- `modules\production\application\lot\mapper\LotMapper.java`
- `modules\production\application\lot\usecase\declarer\DeclarerLotUseCase.java`
- `modules\production\application\of\dto\request\CreerOFRequest.java`
- `modules\production\application\of\dto\response\OFResponse.java`
- `modules\production\application\of\mapper\OFMapper.java`
- `modules\production\application\of\usecase\AnnulerOFUseCase.java`
- `modules\production\application\of\usecase\CreerOFUseCase.java`
- `modules\production\application\of\usecase\DemarrerOFUseCase.java`
- `modules\production\application\of\usecase\EnregistrerProductionOFUseCase.java`
- `modules\production\application\pph\dto\request\CreerPPHRequest.java`
- `modules\production\application\pph\dto\request\ModifierLignesPPHRequest.java`
- `modules\production\application\pph\dto\response\PPHResponse.java`
- `modules\production\application\pph\mapper\PPHMapper.java`
- `modules\production\application\pph\service\PlanificationProductionService.java`
- `modules\production\application\pph\usecase\CreerPPHUseCase.java`
- `modules\production\application\pph\usecase\GenererPPHDepuisBCUseCase.java`
- `modules\production\application\pph\usecase\ModifierLignePPHUseCase.java`
- `modules\production\application\pph\usecase\ValiderPPHUseCase.java`
- `modules\production\application\rapport\usecase\GenererRapportComparatifBCUseCase.java`
- `modules\production\application\rapport\usecase\GenererRapportHebdomadaireUseCase.java`
- `modules\production\application\rapport\usecase\GenererRapportLotsUseCase.java`
- `modules\production\application\registre\dto\request\EnregistrerRegistreRequest.java`
- `modules\production\application\registre\dto\response\RegistreProductionResponse.java`
- `modules\production\application\registre\mapper\RegistreMapper.java`
- `modules\production\application\registre\usecase\EnregistrerRegistreUseCase.java`
- `modules\production\domain\affectation\aggregate\AffectationJournaliere.java`
- `modules\production\domain\affectation\entity\DetailsRealisationPoste.java`
- `modules\production\domain\affectation\entity\LigneAffectationPoste.java`
- `modules\production\domain\affectation\entity\ProductionIndividuelle.java`
- `modules\production\domain\affectation\entity\SaisieRealisationPoste.java`
- `modules\production\domain\affectation\enums\CodePoste.java`
- `modules\production\domain\affectation\enums\DureeAffectation.java`
- `modules\production\domain\affectation\enums\StatutAffectation.java`
- `modules\production\domain\affectation\enums\UniteProduction.java`
- `modules\production\domain\affectation\repository\AffectationJournaliereRepository.java`
- `modules\production\domain\besoin\enums\StatutBesoin.java`
- `modules\production\domain\besoin\enums\TypeBesoin.java`
- `modules\production\domain\besoin\model\ExpressionBesoin.java`
- `modules\production\domain\besoin\repository\ExpressionBesoinRepository.java`
- `modules\production\domain\broyage\aggregate\SessionBroyage.java`
- `modules\production\domain\broyage\entity\ObjectifMachiniste.java`
- `modules\production\domain\broyage\entity\RealisationMachiniste.java`
- `modules\production\domain\broyage\enums\OperationBroyage.java`
- `modules\production\domain\broyage\enums\StatutSessionBroyage.java`
- `modules\production\domain\broyage\enums\TypePoudre.java`
- `modules\production\domain\broyage\repository\SessionBroyageRepository.java`
- `modules\production\domain\dosage\aggregate\SessionDosage.java`
- `modules\production\domain\dosage\repository\SessionDosageRepository.java`
- `modules\production\domain\fiche\aggregate\FicheProduitJournaliere.java`
- `modules\production\domain\fiche\entity\LigneFicheProduit.java`
- `modules\production\domain\fiche\events\FicheEnregistreeEvent.java`
- `modules\production\domain\fiche\events\FicheVerrouilleeEvent.java`
- `modules\production\domain\fiche\repository\FicheProduitRepository.java`
- `modules\production\domain\fiche\specification\FicheSpecification.java`
- `modules\production\domain\lot\aggregate\LotProduitFini.java`
- `modules\production\domain\lot\enums\StatutLot.java`
- `modules\production\domain\lot\events\LotDeclareEvent.java`
- `modules\production\domain\lot\events\LotValideParStockEvent.java`
- `modules\production\domain\lot\repository\LotRepository.java`
- `modules\production\domain\lot\specification\LotSpecification.java`
- `modules\production\domain\of\aggregate\OrdreFabrication.java`
- `modules\production\domain\of\enums\StatutOF.java`
- `modules\production\domain\of\repository\OFRepository.java`
- `modules\production\domain\pph\aggregate\PlanProductionHebdomadaire.java`
- `modules\production\domain\pph\entity\LignePPH.java`
- `modules\production\domain\pph\entity\LignePlanDetail.java`
- `modules\production\domain\pph\entity\RepartitionJournaliere.java`
- `modules\production\domain\pph\enums\RecetteBouillie.java`
- `modules\production\domain\pph\enums\StatutPPH.java`
- `modules\production\domain\pph\events\PPHValideEvent.java`
- `modules\production\domain\pph\repository\PPHRepository.java`
- `modules\production\domain\registre\aggregate\RegistreProduction.java`
- `modules\production\domain\registre\entity\LigneRegistreEmploye.java`
- `modules\production\domain\registre\repository\RegistreProductionRepository.java`
- `modules\production\infrastructure\messaging\FicheVerrouillageListener.java`
- `modules\production\infrastructure\persistence\affectation\jpa\AffectationJournaliereJpaEntity.java`
- `modules\production\infrastructure\persistence\affectation\jpa\AffectationJournaliereJpaRepository.java`
- `modules\production\infrastructure\persistence\affectation\jpa\LigneAffectationJpaEntity.java`
- `modules\production\infrastructure\persistence\affectation\jpa\ProductionIndividuelleJpaEntity.java`
- `modules\production\infrastructure\persistence\affectation\jpa\SaisieRealisationJpaEntity.java`
- `modules\production\infrastructure\persistence\affectation\repository\AffectationJournaliereRepositoryImpl.java`
- `modules\production\infrastructure\persistence\besoin\jpa\ExpressionBesoinJpaEntity.java`
- `modules\production\infrastructure\persistence\besoin\jpa\ExpressionBesoinJpaRepository.java`
- `modules\production\infrastructure\persistence\besoin\repository\ExpressionBesoinRepositoryImpl.java`
- `modules\production\infrastructure\persistence\broyage\jpa\ObjectifMachinisteJpaEntity.java`
- `modules\production\infrastructure\persistence\broyage\jpa\RealisationMachinisteJpaEntity.java`
- `modules\production\infrastructure\persistence\broyage\jpa\SessionBroyageJpaEntity.java`
- `modules\production\infrastructure\persistence\broyage\jpa\SessionBroyageJpaRepository.java`
- `modules\production\infrastructure\persistence\broyage\repository\SessionBroyageRepositoryImpl.java`
- `modules\production\infrastructure\persistence\dosage\jpa\SessionDosageJpaEntity.java`
- `modules\production\infrastructure\persistence\dosage\jpa\SessionDosageJpaRepository.java`
- `modules\production\infrastructure\persistence\dosage\repository\SessionDosageRepositoryImpl.java`
- `modules\production\infrastructure\persistence\fiche\jpa\FicheProduitJpaEntity.java`
- `modules\production\infrastructure\persistence\fiche\jpa\FicheProduitJpaRepository.java`
- `modules\production\infrastructure\persistence\fiche\jpa\LigneFicheProduitJpaEntity.java`
- `modules\production\infrastructure\persistence\fiche\repository\FicheProduitRepositoryImpl.java`
- `modules\production\infrastructure\persistence\lot\jpa\LotJpaRepository.java`
- `modules\production\infrastructure\persistence\lot\jpa\LotProduitFiniJpaEntity.java`
- `modules\production\infrastructure\persistence\lot\repository\LotRepositoryImpl.java`
- `modules\production\infrastructure\persistence\of\jpa\OFJpaEntity.java`
- `modules\production\infrastructure\persistence\of\jpa\OFJpaRepository.java`
- `modules\production\infrastructure\persistence\of\repository\OFRepositoryImpl.java`
- `modules\production\infrastructure\persistence\pph\jpa\LignePPHJpaEntity.java`
- `modules\production\infrastructure\persistence\pph\jpa\LignePlanDetailJpaEntity.java`
- `modules\production\infrastructure\persistence\pph\jpa\PPHJpaEntity.java`
- `modules\production\infrastructure\persistence\pph\jpa\PPHJpaRepository.java`
- `modules\production\infrastructure\persistence\pph\jpa\RepartitionJourJpaEntity.java`
- `modules\production\infrastructure\persistence\pph\repository\PPHRepositoryImpl.java`
- `modules\production\infrastructure\persistence\registre\jpa\LigneRegistreJpaEntity.java`
- `modules\production\infrastructure\persistence\registre\jpa\RegistreJpaEntity.java`
- `modules\production\infrastructure\persistence\registre\jpa\RegistreJpaRepository.java`
- `modules\production\infrastructure\persistence\registre\repository\RegistreRepositoryImpl.java`
- `modules\production\presentation\controller\AffectationController.java`
- `modules\production\presentation\controller\BroyageController.java`
- `modules\production\presentation\controller\ChefMachinisteController.java`
- `modules\production\presentation\controller\DashboardProductionController.java`
- `modules\production\presentation\controller\DosageController.java`
- `modules\production\presentation\controller\ExpressionBesoinController.java`
- `modules\production\presentation\controller\FicheController.java`
- `modules\production\presentation\controller\LotController.java`
- `modules\production\presentation\controller\OFController.java`
- `modules\production\presentation\controller\PPHController.java`
- `modules\production\presentation\controller\RapportProductionController.java`
- `modules\production\presentation\controller\RegistreController.java`

### Module `rp`

- `modules\rp\application\RpDashboardService.java`
- `modules\rp\application\dto\RpDashboardResponse.java`
- `modules\rp\infrastructure\persistence\jpa\CampagneJpaEntity.java`
- `modules\rp\infrastructure\persistence\jpa\CampagneJpaRepository.java`
- `modules\rp\infrastructure\persistence\jpa\GrandCompteJpaEntity.java`
- `modules\rp\infrastructure\persistence\jpa\GrandCompteJpaRepository.java`
- `modules\rp\presentation\controller\RpController.java`

### Module `secretaire`

- `modules\secretaire\application\SecretaireDashboardService.java`
- `modules\secretaire\application\dto\SecretaireDashboardResponse.java`
- `modules\secretaire\infrastructure\persistence\jpa\DocumentRecuJpaEntity.java`
- `modules\secretaire\infrastructure\persistence\jpa\DocumentRecuJpaRepository.java`
- `modules\secretaire\infrastructure\persistence\jpa\MicroStockJpaEntity.java`
- `modules\secretaire\infrastructure\persistence\jpa\MicroStockJpaRepository.java`
- `modules\secretaire\infrastructure\persistence\jpa\PetiteCaisseJpaEntity.java`
- `modules\secretaire\infrastructure\persistence\jpa\PetiteCaisseJpaRepository.java`
- `modules\secretaire\presentation\controller\SecretaireController.java`

### Module `shared`

- `shared\application\audit\dto\ActiviteSuspecteResponse.java`
- `shared\application\audit\dto\JournalAuditResponse.java`
- `shared\application\dto\ApiResponse.java`
- `shared\application\dto\PageResponse.java`
- `shared\application\rapport\FormatRapport.java`
- `shared\application\rapport\RapportFichier.java`
- `shared\infrastructure\audit\AuditAspect.java`
- `shared\infrastructure\audit\AuditService.java`
- `shared\infrastructure\audit\JournalAuditJpaEntity.java`
- `shared\infrastructure\audit\JournalAuditJpaRepository.java`
- `shared\infrastructure\config\MinioConfig.java`
- `shared\infrastructure\config\RabbitMQConfig.java`
- `shared\infrastructure\config\WebSocketConfig.java`
- `shared\infrastructure\event\EventPublisherImpl.java`
- `shared\infrastructure\notification\EmailService.java`
- `shared\infrastructure\otp\CodeOtpJpaEntity.java`
- `shared\infrastructure\otp\CodeOtpJpaRepository.java`
- `shared\infrastructure\otp\LogSmsGatewayAdapter.java`
- `shared\infrastructure\otp\OtpInvalideException.java`
- `shared\infrastructure\otp\OtpService.java`
- `shared\infrastructure\otp\SmsGatewayPort.java`
- `shared\infrastructure\parametres\ParametresSystemeInitializer.java`
- `shared\infrastructure\parametres\ParametresSystemeJpaEntity.java`
- `shared\infrastructure\parametres\ParametresSystemeJpaRepository.java`
- `shared\infrastructure\rapport\TableExporter.java`
- `shared\infrastructure\security\AuthenticatedUserResolver.java`
- `shared\infrastructure\seeding\AuthSeeder.java`
- `shared\infrastructure\seeding\CommercialBaseSeeder.java`
- `shared\infrastructure\seeding\CommercialTransactionalSeeder.java`
- `shared\infrastructure\seeding\CommercialisationSeeder.java`
- `shared\infrastructure\seeding\ComptabiliteSeeder.java`
- `shared\infrastructure\seeding\DataSeeder.java`
- `shared\infrastructure\seeding\DataSeederRunner.java`
- `shared\infrastructure\seeding\ProductionSeeder.java`
- `shared\infrastructure\seeding\StockBaseSeeder.java`
- `shared\infrastructure\seeding\StockTransactionalSeeder.java`
- `shared\infrastructure\storage\DocumentStorageService.java`
- `shared\infrastructure\web\GlobalExceptionHandler.java`
- `shared\kernel\audit\TypeActionAudit.java`
- `shared\kernel\event\DomainEvent.java`
- `shared\kernel\event\DomainEventPublisher.java`
- `shared\kernel\event\EventPublisher.java`
- `shared\kernel\exception\AccesRefuseException.java`
- `shared\kernel\exception\DomainException.java`
- `shared\kernel\exception\EntiteNonTrouveeException.java`
- `shared\kernel\exception\RegleMetierException.java`
- `shared\kernel\valueobject\ProductId.java`
- `shared\kernel\valueobject\Quantity.java`
- `shared\kernel\valueobject\UserId.java`
- `shared\presentation\controller\AuditController.java`
- `shared\presentation\controller\DocumentController.java`
- `shared\presentation\controller\OtpController.java`

### Module `stock`

- `modules\stock\application\alerte\dto\response\AlerteResponse.java`
- `modules\stock\application\alerte\mapper\AlerteMapper.java`
- `modules\stock\application\alerte\usecase\ListerAlertesUseCase.java`
- `modules\stock\application\alerte\usecase\TraiterAlerteUseCase.java`
- `modules\stock\application\catalogue\dto\request\AjouterConditionnementRequest.java`
- `modules\stock\application\catalogue\dto\request\CreerProduitRequest.java`
- `modules\stock\application\catalogue\dto\request\ModifierProduitRequest.java`
- `modules\stock\application\catalogue\dto\response\CatalogueHierarchieResponse.java`
- `modules\stock\application\catalogue\dto\response\ConditionnementResponse.java`
- `modules\stock\application\catalogue\dto\response\ProduitResponse.java`
- `modules\stock\application\catalogue\mapper\ProduitMapper.java`
- `modules\stock\application\catalogue\usecase\consulter\ConsulterProduitUseCase.java`
- `modules\stock\application\catalogue\usecase\creer\CreerProduitUseCase.java`
- `modules\stock\application\catalogue\usecase\lister\ConsulterHierarchieCatalogueUseCase.java`
- `modules\stock\application\catalogue\usecase\lister\ListerProduitsUseCase.java`
- `modules\stock\application\catalogue\usecase\modifier\AjouterConditionnementUseCase.java`
- `modules\stock\application\catalogue\usecase\modifier\ModifierProduitUseCase.java`
- `modules\stock\application\catalogue\usecase\supprimer\DesactiverProduitUseCase.java`
- `modules\stock\application\commande\dto\request\CreerBonCommandeRequest.java`
- `modules\stock\application\commande\dto\response\BonCommandeResponse.java`
- `modules\stock\application\commande\mapper\BonCommandeMapper.java`
- `modules\stock\application\commande\usecase\consulter\ConsulterBonCommandeUseCase.java`
- `modules\stock\application\commande\usecase\creer\CreerBonCommandeUseCase.java`
- `modules\stock\application\commande\usecase\cycle\TransitionBonCommandeUseCase.java`
- `modules\stock\application\commande\usecase\lister\ListerBonsCommandeUseCase.java`
- `modules\stock\application\commande\usecase\valider\ValiderBonCommandeUseCase.java`
- `modules\stock\application\dashboard\dto\response\TableauBordEnrichiResponse.java`
- `modules\stock\application\dashboard\dto\response\TableauBordStockResponse.java`
- `modules\stock\application\dashboard\usecase\ConsulterTableauBordEnrichiUseCase.java`
- `modules\stock\application\dashboard\usecase\ConsulterTableauBordStockUseCase.java`
- `modules\stock\application\dotation\dto\request\DecisionLigneRequest.java`
- `modules\stock\application\dotation\dto\request\SoumettreDotationRequest.java`
- `modules\stock\application\dotation\dto\response\DotationResponse.java`
- `modules\stock\application\dotation\mapper\DotationMapper.java`
- `modules\stock\application\dotation\service\VerificationChargementService.java`
- `modules\stock\application\dotation\service\VerificationSurstockService.java`
- `modules\stock\application\dotation\usecase\approuver\ApprouverDotationUseCase.java`
- `modules\stock\application\dotation\usecase\consulter\ConsulterDotationUseCase.java`
- `modules\stock\application\dotation\usecase\lister\ListerDotationsUseCase.java`
- `modules\stock\application\dotation\usecase\modifier\ModifierDotationUseCase.java`
- `modules\stock\application\dotation\usecase\refuser\RefuserDotationUseCase.java`
- `modules\stock\application\dotation\usecase\soumettre\SoumettreDotationUseCase.java`
- `modules\stock\application\entrepot\dto\request\CreerEntrepotRequest.java`
- `modules\stock\application\entrepot\dto\response\EntrepotResponse.java`
- `modules\stock\application\entrepot\mapper\EntrepotMapper.java`
- `modules\stock\application\entrepot\usecase\creer\CreerEntrepotUseCase.java`
- `modules\stock\application\entrepot\usecase\lister\ListerEntrepotsUseCase.java`
- `modules\stock\application\fournisseur\dto\request\CreerFournisseurRequest.java`
- `modules\stock\application\fournisseur\dto\response\FournisseurResponse.java`
- `modules\stock\application\fournisseur\mapper\FournisseurMapper.java`
- `modules\stock\application\fournisseur\usecase\creer\CreerFournisseurUseCase.java`
- `modules\stock\application\fournisseur\usecase\lister\ListerFournisseursUseCase.java`
- `modules\stock\application\inventaire\dto\request\OuvrirInventaireRequest.java`
- `modules\stock\application\inventaire\dto\request\SaisirComptageRequest.java`
- `modules\stock\application\inventaire\dto\response\InventaireResponse.java`
- `modules\stock\application\inventaire\mapper\InventaireMapper.java`
- `modules\stock\application\inventaire\usecase\cloturer\CloturerComptageUseCase.java`
- `modules\stock\application\inventaire\usecase\consulter\ConsulterInventaireUseCase.java`
- `modules\stock\application\inventaire\usecase\lister\ListerInventairesUseCase.java`
- `modules\stock\application\inventaire\usecase\ouvrir\OuvrirInventaireUseCase.java`
- `modules\stock\application\inventaire\usecase\saisir\SaisirComptageUseCase.java`
- `modules\stock\application\inventaire\usecase\valider\ValiderInventaireUseCase.java`
- `modules\stock\application\mouvement\dto\response\MouvementResponse.java`
- `modules\stock\application\mouvement\mapper\MouvementMapper.java`
- `modules\stock\application\mouvement\usecase\lister\ListerMouvementsUseCase.java`
- `modules\stock\application\niveau\dto\response\NiveauStockDetailResponse.java`
- `modules\stock\application\niveau\dto\response\NiveauStockResponse.java`
- `modules\stock\application\niveau\mapper\NiveauStockMapper.java`
- `modules\stock\application\niveau\usecase\ajuster\AjusterStockUseCase.java`
- `modules\stock\application\niveau\usecase\consulter\ConsulterEtatStockUseCase.java`
- `modules\stock\application\niveau\usecase\consulter\ConsulterNiveauxDetaillesUseCase.java`
- `modules\stock\application\niveau\usecase\transferer\TransfererVersTamponUseCase.java`
- `modules\stock\application\notification\dto\MettreAJourPreferencesRequest.java`
- `modules\stock\application\notification\dto\NotificationMessage.java`
- `modules\stock\application\notification\dto\PreferencesNotificationResponse.java`
- `modules\stock\application\rapport\usecase\GenererRapportEtatStockUseCase.java`
- `modules\stock\application\rapport\usecase\GenererRapportMouvementsUseCase.java`
- `modules\stock\application\reception\dto\request\CreerReceptionFournisseurRequest.java`
- `modules\stock\application\reception\dto\request\CreerReceptionProductionRequest.java`
- `modules\stock\application\reception\dto\response\ReceptionResponse.java`
- `modules\stock\application\reception\mapper\ReceptionMapper.java`
- `modules\stock\application\reception\usecase\consulter\ConsulterReceptionUseCase.java`
- `modules\stock\application\reception\usecase\creer\CreerReceptionFournisseurUseCase.java`
- `modules\stock\application\reception\usecase\creer\CreerReceptionProductionUseCase.java`
- `modules\stock\application\reception\usecase\lister\ListerReceptionsUseCase.java`
- `modules\stock\application\reception\usecase\rejeter\RejeterReceptionUseCase.java`
- `modules\stock\application\reception\usecase\valider\ValiderReceptionEtape1UseCase.java`
- `modules\stock\application\reception\usecase\valider\ValiderReceptionEtape2UseCase.java`
- `modules\stock\application\reporting\service\ReportingService.java`
- `modules\stock\application\retour\dto\request\EnregistrerRetourRequest.java`
- `modules\stock\application\retour\dto\response\RetourResponse.java`
- `modules\stock\application\retour\mapper\RetourMapper.java`
- `modules\stock\application\retour\usecase\enregistrer\EnregistrerRetourUseCase.java`
- `modules\stock\application\retour\usecase\lister\ListerRetoursUseCase.java`
- `modules\stock\application\sessioncommercial\dto\response\SessionCommercialResponse.java`
- `modules\stock\application\sessioncommercial\mapper\SessionCommercialMapper.java`
- `modules\stock\application\seuil\dto\request\DefinirSeuilRequest.java`
- `modules\stock\application\seuil\dto\response\HistoriqueSeuilResponse.java`
- `modules\stock\application\seuil\dto\response\SeuilResponse.java`
- `modules\stock\application\seuil\usecase\DefinirSeuilUseCase.java`
- `modules\stock\application\seuil\usecase\EvaluerSeuilsUseCase.java`
- `modules\stock\application\seuil\usecase\ListerSeuilsUseCase.java`
- `modules\stock\application\stockmobile\dto\response\StockMobileResponse.java`
- `modules\stock\application\stockmobile\mapper\StockMobileMapper.java`
- `modules\stock\application\transfert\dto\request\CreerTransfertRequest.java`
- `modules\stock\application\transfert\dto\response\TransfertResponse.java`
- `modules\stock\application\transfert\mapper\TransfertMapper.java`
- `modules\stock\application\transfert\usecase\executer\ExecuterTransfertUseCase.java`
- `modules\stock\application\transfert\usecase\lister\ListerTransfertsUseCase.java`
- `modules\stock\domain\alerte\enums\PrioriteAlerte.java`
- `modules\stock\domain\alerte\enums\StatutAlerte.java`
- `modules\stock\domain\alerte\enums\TypeAlerte.java`
- `modules\stock\domain\alerte\event\AlerteCreeeEvent.java`
- `modules\stock\domain\alerte\model\Alerte.java`
- `modules\stock\domain\alerte\repository\AlerteRepository.java`
- `modules\stock\domain\catalogue\enums\CategorieMetier.java`
- `modules\stock\domain\catalogue\enums\CategorieProduit.java`
- `modules\stock\domain\catalogue\enums\NiveauConditionnement.java`
- `modules\stock\domain\catalogue\event\ConditionnementAjouteEvent.java`
- `modules\stock\domain\catalogue\event\ProduitCreeEvent.java`
- `modules\stock\domain\catalogue\model\Conditionnement.java`
- `modules\stock\domain\catalogue\model\Produit.java`
- `modules\stock\domain\catalogue\repository\ProduitRepository.java`
- `modules\stock\domain\catalogue\specification\ProduitSpecification.java`
- `modules\stock\domain\catalogue\util\ConditionnementParser.java`
- `modules\stock\domain\catalogue\valueobject\CodeProduit.java`
- `modules\stock\domain\commande\enums\StatutBonCommande.java`
- `modules\stock\domain\commande\event\BonCommandeValideEvent.java`
- `modules\stock\domain\commande\model\BonCommande.java`
- `modules\stock\domain\commande\model\LigneBonCommande.java`
- `modules\stock\domain\commande\repository\BonCommandeRepository.java`
- `modules\stock\domain\commande\specification\BonCommandeSpecification.java`
- `modules\stock\domain\dotation\enums\StatutDotation.java`
- `modules\stock\domain\dotation\event\DotationApprouveeEvent.java`
- `modules\stock\domain\dotation\event\DotationSoumiseEvent.java`
- `modules\stock\domain\dotation\model\Dotation.java`
- `modules\stock\domain\dotation\model\LigneDotation.java`
- `modules\stock\domain\dotation\repository\DotationRepository.java`
- `modules\stock\domain\entrepot\enums\TypeEntrepot.java`
- `modules\stock\domain\entrepot\model\Entrepot.java`
- `modules\stock\domain\entrepot\repository\EntrepotRepository.java`
- `modules\stock\domain\fournisseur\model\Fournisseur.java`
- `modules\stock\domain\fournisseur\repository\FournisseurRepository.java`
- `modules\stock\domain\inventaire\enums\StatutInventaire.java`
- `modules\stock\domain\inventaire\enums\TypeInventaire.java`
- `modules\stock\domain\inventaire\event\InventaireValideEvent.java`
- `modules\stock\domain\inventaire\model\LigneInventaire.java`
- `modules\stock\domain\inventaire\model\SessionInventaire.java`
- `modules\stock\domain\inventaire\repository\InventaireRepository.java`
- `modules\stock\domain\mouvement\enums\SensMouvement.java`
- `modules\stock\domain\mouvement\enums\TypeMouvement.java`
- `modules\stock\domain\mouvement\event\MouvementEnregistreEvent.java`
- `modules\stock\domain\mouvement\model\MouvementStock.java`
- `modules\stock\domain\mouvement\repository\MouvementStockRepository.java`
- `modules\stock\domain\niveau\model\NiveauStock.java`
- `modules\stock\domain\niveau\repository\NiveauStockRepository.java`
- `modules\stock\domain\niveau\service\GestionDeStock.java`
- `modules\stock\domain\notification\model\HistoriqueNotification.java`
- `modules\stock\domain\notification\model\PreferencesNotification.java`
- `modules\stock\domain\notification\repository\HistoriqueNotificationRepository.java`
- `modules\stock\domain\notification\repository\PreferencesNotificationRepository.java`
- `modules\stock\domain\reception\enums\SourceReception.java`
- `modules\stock\domain\reception\enums\StatutReception.java`
- `modules\stock\domain\reception\event\ReceptionValideeEvent.java`
- `modules\stock\domain\reception\model\LigneReception.java`
- `modules\stock\domain\reception\model\Reception.java`
- `modules\stock\domain\reception\repository\ReceptionRepository.java`
- `modules\stock\domain\retour\model\BonRetour.java`
- `modules\stock\domain\retour\model\LigneRetour.java`
- `modules\stock\domain\retour\repository\BonRetourRepository.java`
- `modules\stock\domain\sessioncommercial\enums\StatutSession.java`
- `modules\stock\domain\sessioncommercial\model\SessionCommercial.java`
- `modules\stock\domain\sessioncommercial\repository\SessionCommercialRepository.java`
- `modules\stock\domain\seuil\model\SeuilStock.java`
- `modules\stock\domain\seuil\repository\SeuilStockRepository.java`
- `modules\stock\domain\stockmobile\enums\TypeMouvementMobile.java`
- `modules\stock\domain\stockmobile\model\LigneStockMobile.java`
- `modules\stock\domain\stockmobile\model\StockMobile.java`
- `modules\stock\domain\stockmobile\repository\StockMobileRepository.java`
- `modules\stock\domain\transfert\enums\StatutTransfert.java`
- `modules\stock\domain\transfert\event\TransfertExecuteEvent.java`
- `modules\stock\domain\transfert\model\BonTransfert.java`
- `modules\stock\domain\transfert\model\LigneTransfert.java`
- `modules\stock\domain\transfert\repository\BonTransfertRepository.java`
- `modules\stock\infrastructure\messaging\StockEventRelay.java`
- `modules\stock\infrastructure\messaging\StockRabbitListeners.java`
- `modules\stock\infrastructure\persistence\alerte\jpa\AlerteJpaEntity.java`
- `modules\stock\infrastructure\persistence\alerte\jpa\AlerteJpaRepository.java`
- `modules\stock\infrastructure\persistence\alerte\repository\AlerteRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\catalogue\jpa\ConditionnementJpaEntity.java`
- `modules\stock\infrastructure\persistence\catalogue\jpa\ProduitJpaEntity.java`
- `modules\stock\infrastructure\persistence\catalogue\jpa\ProduitJpaRepository.java`
- `modules\stock\infrastructure\persistence\catalogue\mapper\ProduitPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\catalogue\repository\ProduitRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\commande\jpa\BonCommandeJpaEntity.java`
- `modules\stock\infrastructure\persistence\commande\jpa\BonCommandeJpaRepository.java`
- `modules\stock\infrastructure\persistence\commande\jpa\LigneBonCommandeJpaEntity.java`
- `modules\stock\infrastructure\persistence\commande\mapper\BonCommandePersistenceMapper.java`
- `modules\stock\infrastructure\persistence\commande\repository\BonCommandeRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\dotation\jpa\DotationJpaEntity.java`
- `modules\stock\infrastructure\persistence\dotation\jpa\DotationJpaRepository.java`
- `modules\stock\infrastructure\persistence\dotation\jpa\LigneDotationJpaEntity.java`
- `modules\stock\infrastructure\persistence\dotation\mapper\DotationPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\dotation\repository\DotationRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\entrepot\jpa\EntrepotJpaEntity.java`
- `modules\stock\infrastructure\persistence\entrepot\jpa\EntrepotJpaRepository.java`
- `modules\stock\infrastructure\persistence\entrepot\mapper\EntrepotPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\entrepot\repository\EntrepotRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\fournisseur\jpa\FournisseurJpaEntity.java`
- `modules\stock\infrastructure\persistence\fournisseur\jpa\FournisseurJpaRepository.java`
- `modules\stock\infrastructure\persistence\fournisseur\mapper\FournisseurPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\fournisseur\repository\FournisseurRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\inventaire\jpa\LigneInventaireJpaEntity.java`
- `modules\stock\infrastructure\persistence\inventaire\jpa\SessionInventaireJpaEntity.java`
- `modules\stock\infrastructure\persistence\inventaire\jpa\SessionInventaireJpaRepository.java`
- `modules\stock\infrastructure\persistence\inventaire\mapper\InventairePersistenceMapper.java`
- `modules\stock\infrastructure\persistence\inventaire\repository\InventaireRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\mouvement\jpa\MouvementStockJpaEntity.java`
- `modules\stock\infrastructure\persistence\mouvement\jpa\MouvementStockJpaRepository.java`
- `modules\stock\infrastructure\persistence\mouvement\mapper\MouvementStockPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\mouvement\repository\MouvementStockRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\niveau\jpa\NiveauStockJpaEntity.java`
- `modules\stock\infrastructure\persistence\niveau\jpa\NiveauStockJpaRepository.java`
- `modules\stock\infrastructure\persistence\niveau\mapper\NiveauStockPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\niveau\repository\NiveauStockRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\notification\jpa\HistoriqueNotificationJpaEntity.java`
- `modules\stock\infrastructure\persistence\notification\jpa\HistoriqueNotificationJpaRepository.java`
- `modules\stock\infrastructure\persistence\notification\jpa\PreferencesNotificationJpaEntity.java`
- `modules\stock\infrastructure\persistence\notification\jpa\PreferencesNotificationJpaRepository.java`
- `modules\stock\infrastructure\persistence\notification\repository\HistoriqueNotificationRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\notification\repository\PreferencesNotificationRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\reception\jpa\LigneReceptionJpaEntity.java`
- `modules\stock\infrastructure\persistence\reception\jpa\ReceptionJpaEntity.java`
- `modules\stock\infrastructure\persistence\reception\jpa\ReceptionJpaRepository.java`
- `modules\stock\infrastructure\persistence\reception\mapper\ReceptionPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\reception\repository\ReceptionRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\retour\jpa\BonRetourJpaEntity.java`
- `modules\stock\infrastructure\persistence\retour\jpa\BonRetourJpaRepository.java`
- `modules\stock\infrastructure\persistence\retour\jpa\LigneRetourJpaEntity.java`
- `modules\stock\infrastructure\persistence\retour\mapper\BonRetourPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\retour\repository\BonRetourRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\sessioncommercial\jpa\SessionCommercialJpaEntity.java`
- `modules\stock\infrastructure\persistence\sessioncommercial\jpa\SessionCommercialJpaRepository.java`
- `modules\stock\infrastructure\persistence\sessioncommercial\repository\SessionCommercialRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\seuil\jpa\HistoriqueSeuilJpaEntity.java`
- `modules\stock\infrastructure\persistence\seuil\jpa\HistoriqueSeuilJpaRepository.java`
- `modules\stock\infrastructure\persistence\seuil\jpa\SeuilStockJpaEntity.java`
- `modules\stock\infrastructure\persistence\seuil\jpa\SeuilStockJpaRepository.java`
- `modules\stock\infrastructure\persistence\seuil\repository\SeuilStockRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\stockmobile\jpa\LigneStockMobileJpaEntity.java`
- `modules\stock\infrastructure\persistence\stockmobile\jpa\StockMobileJpaEntity.java`
- `modules\stock\infrastructure\persistence\stockmobile\jpa\StockMobileJpaRepository.java`
- `modules\stock\infrastructure\persistence\stockmobile\repository\StockMobileRepositoryImpl.java`
- `modules\stock\infrastructure\persistence\transfert\jpa\BonTransfertJpaEntity.java`
- `modules\stock\infrastructure\persistence\transfert\jpa\BonTransfertJpaRepository.java`
- `modules\stock\infrastructure\persistence\transfert\jpa\LigneTransfertJpaEntity.java`
- `modules\stock\infrastructure\persistence\transfert\mapper\BonTransfertPersistenceMapper.java`
- `modules\stock\infrastructure\persistence\transfert\repository\BonTransfertRepositoryImpl.java`
- `modules\stock\infrastructure\websocket\NotificationWebSocketService.java`
- `modules\stock\presentation\controller\AlerteController.java`
- `modules\stock\presentation\controller\BonCommandeController.java`
- `modules\stock\presentation\controller\CatalogueController.java`
- `modules\stock\presentation\controller\CommercialStockController.java`
- `modules\stock\presentation\controller\DotationController.java`
- `modules\stock\presentation\controller\EntrepotController.java`
- `modules\stock\presentation\controller\EtatStockController.java`
- `modules\stock\presentation\controller\FournisseurController.java`
- `modules\stock\presentation\controller\HistoriqueNotificationController.java`
- `modules\stock\presentation\controller\InternalOrderController.java`
- `modules\stock\presentation\controller\InventaireController.java`
- `modules\stock\presentation\controller\MouvementController.java`
- `modules\stock\presentation\controller\NiveauStockController.java`
- `modules\stock\presentation\controller\PreferencesNotificationController.java`
- `modules\stock\presentation\controller\ProduitController.java`
- `modules\stock\presentation\controller\RapportController.java`
- `modules\stock\presentation\controller\ReceptionController.java`
- `modules\stock\presentation\controller\ReportingController.java`
- `modules\stock\presentation\controller\RetourController.java`
- `modules\stock\presentation\controller\SessionCommercialController.java`
- `modules\stock\presentation\controller\SeuilController.java`
- `modules\stock\presentation\controller\StockMobileController.java`
- `modules\stock\presentation\controller\TableauBordStockController.java`
- `modules\stock\presentation\controller\TransfertController.java`

### Module `sync`

- `modules\sync\application\dto\SyncLogResponse.java`
- `modules\sync\application\dto\SyncPullResponse.java`
- `modules\sync\application\dto\SyncPushResponse.java`
- `modules\sync\application\service\SyncService.java`
- `modules\sync\application\usecase\ConsulterHistoriqueSyncUseCase.java`
- `modules\sync\domain\aggregate\SynchronisationLog.java`
- `modules\sync\domain\enums\SyncEntityType.java`
- `modules\sync\domain\enums\SyncOperationType.java`
- `modules\sync\domain\enums\SyncResult.java`
- `modules\sync\domain\repository\SynchronisationLogRepository.java`
- `modules\sync\infrastructure\persistence\jpa\SynchronisationLogJpaEntity.java`
- `modules\sync\infrastructure\persistence\jpa\SynchronisationLogJpaRepository.java`
- `modules\sync\infrastructure\persistence\repository\SynchronisationLogRepositoryImpl.java`
- `modules\sync\presentation\controller\SyncController.java`

## Détails des fichiers backend

### Module `administration`

#### `modules\administration\application\ParametresAdminService.java`

**class** `ParametresAdminService` (@Service
@Transactional
public)

- **Champs :**
  - `private static final String CLE_TVA = ""`
  - `private static final String CLE_PRIME = ""`
  - `private static final String CLE_SEUIL_STOCK = ""`
  - `private final ParametresSystemeJpaRepository parametresRepo`
  - `private final ReferentielStringJpaRepository referentielRepo`

- **Méthodes :**
  - `public ParametresAdminService(ParametresSystemeJpaRepository parametresRepo,
                                   ReferentielStringJpaRepository referentielRepo)`
  - `@Transactional(readOnly = true) public ParametresResponse consulter()`
  - `public ParametresResponse modifier(double tvaTaux, double primeMontant, double alerteStockSeuil)`
  - `public ParametresResponse ajouterReferentiel(String categorie, String valeur)`
  - `public ParametresResponse supprimerReferentiel(String categorie, String valeur)`
  - `private double getValeur(String cle, double defaut)`
  - `private void setValeur(String cle, double valeur)`
  - `private List<String> listerReferentiel(String categorie)`

#### `modules\administration\application\dto\ParametresResponse.java`

**record** `ParametresResponse` (public)

- **Composants (record) :**
  - `double tvaTaux`
  - `double primeMontant`
  - `double alerteStockSeuil`
  - `List<String> zones`
  - `List<String> gammeProduits`
  - `List<String> villes`

#### `modules\administration\application\utilisateur\dto\response\UtilisateurResponse.java`

**record** `UtilisateurResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matricule`
  - `String prenom`
  - `String nom`
  - `String nomComplet`
  - `String email`
  - `String role`
  - `String roleLibelle`
  - `boolean actif`
  - `boolean compteBloque`
  - `int tentativesEchouees`
  - `boolean sessionActive`
  - `String telephone`
  - `String residence`
  - `String cni`
  - `String photo`
  - `LocalDate dateNaissance`
  - `String lieuNaissance`
  - `LocalDate dateEmbauche`
  - `String sexe`
  - `LocalDateTime dateCreation`

#### `modules\administration\application\utilisateur\mapper\UtilisateurAdminMapper.java`

**class** `UtilisateurAdminMapper` (@Component
public)

- **Méthodes :**
  - `public UtilisateurResponse versResponse(Utilisateur u)`

#### `modules\administration\application\utilisateur\usecase\ChangerRoleUtilisateurUseCase.java`

**class** `ChangerRoleUtilisateurUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final UtilisateurAdminMapper mapper`
  - `Role role`

- **Méthodes :**
  - `public ChangerRoleUtilisateurUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper)`
  - `public UtilisateurResponse executer(String matricule, String nouveauRole)`

#### `modules\administration\application\utilisateur\usecase\ConsulterUtilisateurUseCase.java`

**class** `ConsulterUtilisateurUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final UtilisateurAdminMapper mapper`

- **Méthodes :**
  - `public ConsulterUtilisateurUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper)`
  - `public UtilisateurResponse executer(String matricule)`

#### `modules\administration\application\utilisateur\usecase\DebloquerUtilisateurUseCase.java`

**class** `DebloquerUtilisateurUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final UtilisateurAdminMapper mapper`

- **Méthodes :**
  - `public DebloquerUtilisateurUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper)`
  - `public UtilisateurResponse executer(String matricule)`

#### `modules\administration\application\utilisateur\usecase\GererStatutUtilisateurUseCase.java`

**class** `GererStatutUtilisateurUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final UtilisateurAdminMapper mapper`

- **Méthodes :**
  - `public GererStatutUtilisateurUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper)`
  - `public UtilisateurResponse executer(String matricule, boolean actif)`

#### `modules\administration\application\utilisateur\usecase\ListerUtilisateursUseCase.java`

**class** `ListerUtilisateursUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final UtilisateurAdminMapper mapper`

- **Méthodes :**
  - `public ListerUtilisateursUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper)`
  - `public List<UtilisateurResponse> executer(String role)`

#### `modules\administration\application\utilisateur\usecase\ReinitialiserMotDePasseUseCase.java`

**class** `ReinitialiserMotDePasseUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final UtilisateurAdminMapper mapper`
  - `private final PasswordEncoder passwordEncoder`

- **Méthodes :**
  - `public ReinitialiserMotDePasseUseCase(UtilisateurRepository utilisateurRepository,
                                           UtilisateurAdminMapper mapper,
                                           PasswordEncoder passwordEncoder)`
  - `public UtilisateurResponse executer(String matricule, String nouveauMotDePasse)`

#### `modules\administration\infrastructure\persistence\ReferentielStringJpaEntity.java`

**class** `ReferentielStringJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, length = 40) private String categorie`
  - `@Column(nullable = false, length = 100) private String valeur`

#### `modules\administration\infrastructure\persistence\ReferentielStringJpaRepository.java`

**interface** `ReferentielStringJpaRepository` (public) extends `JpaRepository<ReferentielStringJpaEntity, Long>`

#### `modules\administration\presentation\controller\AdministrationController.java`

**class** `AdministrationController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
@PreAuthorize("")
public)

- **Champs :**
  - `private final ListerUtilisateursUseCase listerUtilisateursUseCase`
  - `private final ConsulterUtilisateurUseCase consulterUtilisateurUseCase`
  - `private final GererStatutUtilisateurUseCase gererStatutUseCase`
  - `private final ChangerRoleUtilisateurUseCase changerRoleUseCase`
  - `private final DebloquerUtilisateurUseCase debloquerUtilisateurUseCase`
  - `private final ReinitialiserMotDePasseUseCase reinitialiserMotDePasseUseCase`

- **Méthodes :**
  - `public AdministrationController(ListerUtilisateursUseCase listerUtilisateursUseCase,
                                  ConsulterUtilisateurUseCase consulterUtilisateurUseCase,
                                  GererStatutUtilisateurUseCase gererStatutUseCase,
                                  ChangerRoleUtilisateurUseCase changerRoleUseCase,
                                  DebloquerUtilisateurUseCase debloquerUtilisateurUseCase,
                                  ReinitialiserMotDePasseUseCase reinitialiserMotDePasseUseCase)`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<UtilisateurResponse>> consulter(@PathVariable String matricule)`
  - `@PatchMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<UtilisateurResponse>> activer(@PathVariable String matricule)`
  - `@PatchMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<UtilisateurResponse>> desactiver(@PathVariable String matricule)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<UtilisateurResponse>> changerRole(@PathVariable String matricule,
                                                                        @RequestParam String role)`
  - `@PatchMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<UtilisateurResponse>> debloquer(@PathVariable String matricule)`
  - `@PatchMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<UtilisateurResponse>> reinitialiserMotDePasse(@PathVariable String matricule,
        @RequestParam String nouveauMotDePasse)`

#### `modules\administration\presentation\controller\ParametresAdminController.java`

**class** `ParametresAdminController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
@PreAuthorize("")
public)

- **Champs :**
  - `private final ParametresAdminService service`

- **Méthodes :**
  - `public ParametresAdminController(ParametresAdminService service)`
  - `@GetMapping
    @Operation(summary = "") public ResponseEntity<ApiResponse<ParametresResponse>> consulter()`
  - `@PutMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ParametresResponse>> modifier(@RequestParam double tvaTaux,
        @RequestParam double primeMontant,
        @RequestParam double alerteStockSeuil)`
  - `@PostMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ParametresResponse>> ajouterReferentiel(@RequestParam String categorie,
        @RequestParam String valeur)`
  - `@DeleteMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ParametresResponse>> supprimerReferentiel(@RequestParam String categorie,
        @RequestParam String valeur)`


### Module `auth`

#### `modules\auth\application\auth\dto\DemanderOtpRequest.java`

_Fichier vide ou non analysable._

#### `modules\auth\application\auth\dto\LoginRequest.java`

_Fichier vide ou non analysable._

#### `modules\auth\application\auth\dto\LoginResponse.java`

**record** `LoginResponse` (public)

- **Composants (record) :**
  - `String token`
  - `String matricule`
  - `String nomComplet`
  - `Role role`
  - `long expirationMs`

#### `modules\auth\application\auth\dto\ModifierProfilRequest.java`

**record** `ModifierProfilRequest` (public)

- **Composants (record) :**
  - `String telephone`
  - `String residence`
  - `String cni`
  - `String photo`
  - `LocalDate dateNaissance`
  - `String lieuNaissance`
  - `String sexe`

#### `modules\auth\application\auth\dto\ModifierUtilisateurRequest.java`

**record** `ModifierUtilisateurRequest` (public)

- **Composants (record) :**
  - `@NotBlank String prenom`
  - `@NotBlank String nom`
  - `@NotBlank @Email String email`
  - `@NotNull Role role`
  - `String telephone`
  - `String residence`
  - `String cni`
  - `String photo`
  - `LocalDate dateNaissance`
  - `String lieuNaissance`
  - `LocalDate dateEmbauche`
  - `String sexe`
  - `String nouveauMotDePasse`

#### `modules\auth\application\auth\dto\OtpResponse.java`

**record** `OtpResponse` (public)

- **Composants (record) :**
  - `String matricule`
  - `boolean otpEnvoye`
  - `String telephoneMasque`
  - `int dureeValiditeMinutes`

#### `modules\auth\application\auth\dto\ProfilResponse.java`

**record** `ProfilResponse` (public)

- **Composants (record) :**
  - `String matricule`
  - `String prenom`
  - `String nom`
  - `String email`
  - `Role role`
  - `boolean actif`
  - `String telephone`
  - `String residence`
  - `String cni`
  - `String photo`
  - `LocalDate dateNaissance`
  - `String lieuNaissance`
  - `LocalDate dateEmbauche`
  - `String sexe`
  - `LocalDateTime dateCreation`

#### `modules\auth\application\auth\dto\RegisterRequest.java`

**record** `RegisterRequest` (public)

- **Composants (record) :**
  - `@NotBlank String prenom`
  - `@NotBlank String nom`
  - `@NotBlank @Email String email`
  - `@NotBlank String motDePasse`
  - `@NotNull Role role`
  - `String telephone`
  - `String residence`
  - `String cni`
  - `String photo`
  - `LocalDate dateNaissance`
  - `String lieuNaissance`
  - `LocalDate dateEmbauche`
  - `String sexe`

#### `modules\auth\application\auth\dto\VerifierOtpRequest.java`

_Fichier vide ou non analysable._

#### `modules\auth\application\auth\usecase\CreerUtilisateurTransactionnel.java`

**class** `CreerUtilisateurTransactionnel` (@Service
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final PasswordEncoder passwordEncoder`
  - `private final MatriculeSequenceService matriculeSequenceService`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public CreerUtilisateurTransactionnel(UtilisateurRepository utilisateurRepository,
                                          PasswordEncoder passwordEncoder,
                                          MatriculeSequenceService matriculeSequenceService,
                                          EventPublisher eventPublisher)`
  - `@Transactional(propagation = Propagation.REQUIRES_NEW) public String executer(RegisterRequest request, Email email)`

#### `modules\auth\application\auth\usecase\DemanderOtpUseCase.java`

**class** `DemanderOtpUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final OtpService otpService`

- **Méthodes :**
  - `public DemanderOtpUseCase(UtilisateurRepository utilisateurRepository, OtpService otpService)`
  - `public OtpResponse executer(DemanderOtpRequest request)`
  - `private String masquerTelephone(String tel)`

#### `modules\auth\application\auth\usecase\GererProfilUseCase.java`

**class** `GererProfilUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`

- **Méthodes :**
  - `public GererProfilUseCase(UtilisateurRepository utilisateurRepository)`
  - `@Transactional(readOnly = true) public ProfilResponse consulter(String matricule)`
  - `public ProfilResponse modifier(String matricule, ModifierProfilRequest request)`
  - `private ProfilResponse versResponse(Utilisateur u)`

#### `modules\auth\application\auth\usecase\GererUtilisateursUseCase.java`

**class** `GererUtilisateursUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final PasswordEncoder passwordEncoder`

- **Méthodes :**
  - `public GererUtilisateursUseCase(UtilisateurRepository utilisateurRepository,
                                     PasswordEncoder passwordEncoder)`
  - `@Transactional(readOnly = true) public List<ProfilResponse> listerTous()`
  - `@Transactional(readOnly = true) public List<ProfilResponse> listerParRole(Role role)`
  - `@Transactional(readOnly = true) public ProfilResponse consulter(String matricule)`
  - `public ProfilResponse modifier(String matricule, ModifierUtilisateurRequest request)`
  - `public void desactiver(String matricule)`
  - `public void reactiver(String matricule)`
  - `public void debloquer(String matricule)`
  - `public void changerMotDePasse(String matricule, String nouveauMotDePasse)`
  - `public void changerRole(String matricule, Role nouveauRole)`
  - `private ProfilResponse versResponse(Utilisateur u)`

#### `modules\auth\application\auth\usecase\LoginUseCase.java`

**class** `LoginUseCase` (@Service
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final PasswordEncoder passwordEncoder`
  - `private final JwtService jwtService`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public LoginUseCase(UtilisateurRepository utilisateurRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService,
                        EventPublisher eventPublisher)`
  - `@Transactional public LoginResponse executer(LoginRequest request)`

#### `modules\auth\application\auth\usecase\LogoutUseCase.java`

**class** `LogoutUseCase` (@Service
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`

- **Méthodes :**
  - `public LogoutUseCase(UtilisateurRepository utilisateurRepository)`
  - `@Transactional public void executer(String matricule)`

#### `modules\auth\application\auth\usecase\RegisterUseCase.java`

**class** `RegisterUseCase` (@Service
public)

- **Champs :**
  - `private static final int MAX_TENTATIVES = 3`
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final CreerUtilisateurTransactionnel creerUtilisateurTransactionnel`
  - `DataIntegrityViolationException derniereErreur = null`
  - `int tentative = 1`

- **Méthodes :**
  - `public RegisterUseCase(UtilisateurRepository utilisateurRepository,
                           CreerUtilisateurTransactionnel creerUtilisateurTransactionnel)`
  - `public String executer(RegisterRequest request)`

#### `modules\auth\application\auth\usecase\VerifierOtpUseCase.java`

**class** `VerifierOtpUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final OtpService otpService`
  - `private final JwtService jwtService`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public VerifierOtpUseCase(UtilisateurRepository utilisateurRepository,
                               OtpService otpService,
                               JwtService jwtService,
                               EventPublisher eventPublisher)`
  - `public LoginResponse executer(VerifierOtpRequest request)`

#### `modules\auth\application\rh\dto\request\CreerEmployeRequest.java`

**record** `CreerEmployeRequest` (public)

- **Composants (record) :**
  - `@NotBlank String prenom`
  - `@NotBlank String nom`
  - `@NotNull Role role`
  - `@NotBlank String zoneTravail`
  - `String superviseurMatricule`
  - `LocalDate dateEmbauche`
  - `@Email String email`
  - `String motDePasse`

#### `modules\auth\application\rh\dto\response\EmployeResponse.java`

**record** `EmployeResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matricule`
  - `String nom`
  - `String prenom`
  - `String nomComplet`
  - `Role role`
  - `String zoneTravail`
  - `String superviseurMatricule`
  - `boolean avecCompte`
  - `boolean actif`
  - `LocalDate dateEmbauche`
  - `LocalDateTime dateCreation`
  - `LocalDateTime dateModification`

#### `modules\auth\application\rh\mapper\EmployeMapper.java`

**class** `EmployeMapper` (@Component
public)

- **Méthodes :**
  - `public EmployeResponse versResponse(Employe e)`

#### `modules\auth\application\rh\usecase\CreerEmployeUseCase.java`

**class** `CreerEmployeUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final EmployeRepository employeRepository`
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final GenerateurMatriculeEmploye generateurMatricule`
  - `private final EmployeMapper employeMapper`
  - `private final PasswordEncoder passwordEncoder`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public CreerEmployeUseCase(EmployeRepository employeRepository,
                               UtilisateurRepository utilisateurRepository,
                               GenerateurMatriculeEmploye generateurMatricule,
                               EmployeMapper employeMapper,
                               PasswordEncoder passwordEncoder,
                               EventPublisher eventPublisher)`
  - `public EmployeResponse executer(CreerEmployeRequest request)`

#### `modules\auth\application\rh\usecase\ListerMachinistesUseCase.java`

**class** `ListerMachinistesUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final EmployeRepository employeRepository`
  - `private final EmployeMapper employeMapper`

- **Méthodes :**
  - `public ListerMachinistesUseCase(EmployeRepository employeRepository, EmployeMapper employeMapper)`
  - `public List<EmployeResponse> executer()`

#### `modules\auth\domain\rh\model\Employe.java`

**class** `Employe` (public)

- **Champs :**
  - `private Long id`
  - `private final String matricule`
  - `private final String nom`
  - `private final String prenom`
  - `private final Role role`
  - `private final String zoneTravail`
  - `private final String superviseurMatricule`
  - `private final boolean avecCompte`
  - `private boolean actif`
  - `private final LocalDate dateEmbauche`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private Employe(String matricule, String nom, String prenom, Role role, String zoneTravail,
                    String superviseurMatricule, boolean avecCompte, LocalDate dateEmbauche,
                    LocalDateTime dateCreation)`
  - `public static Employe creer(String matricule, String nom, String prenom, Role role,
                                 String zoneTravail, String superviseurMatricule,
                                 boolean avecCompte, LocalDate dateEmbauche)`
  - `public static Employe reconstituer(Long id, String matricule, String nom, String prenom, Role role,
                                        String zoneTravail, String superviseurMatricule,
                                        boolean avecCompte, boolean actif, LocalDate dateEmbauche,
                                        LocalDateTime dateCreation, LocalDateTime dateModification, Long version)`
  - `public void desactiver()`
  - `public void reactiver()`
  - `public boolean estExecutant()`
  - `public boolean estResponsable()`
  - `public Long getId()`
  - `public String getMatricule()`
  - `public String getNom()`
  - `public String getPrenom()`
  - `public String getNomComplet()`
  - `public Role getRole()`
  - `public String getZoneTravail()`
  - `public String getSuperviseurMatricule()`
  - `public boolean isAvecCompte()`
  - `public boolean isActif()`
  - `public LocalDate getDateEmbauche()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\auth\domain\rh\repository\EmployeRepository.java`

**interface** `EmployeRepository` (public)

#### `modules\auth\domain\utilisateur\aggregate\Utilisateur.java`

**class** `Utilisateur` (public)

- **Champs :**
  - `private static final int MAX_TENTATIVES = 3`
  - `private Long id`
  - `private final Matricule matricule`
  - `private NomComplet nomComplet`
  - `private Email email`
  - `private MotDePasse motDePasse`
  - `private Role role`
  - `private boolean actif`
  - `private int tentativesEchouees`
  - `private boolean compteBloque`
  - `private String sessionActuelle`
  - `private String cni`
  - `private String telephone`
  - `private String residence`
  - `private String photo`
  - `private LocalDate dateNaissance`
  - `private String lieuNaissance`
  - `private LocalDate dateEmbauche`
  - `private String sexe`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private Utilisateur(Matricule matricule, NomComplet nomComplet, Email email,
                        MotDePasse motDePasse, Role role, LocalDateTime dateCreation)`
  - `public static Utilisateur creer(Matricule matricule, NomComplet nomComplet,
                                    Email email, MotDePasse motDePasse, Role role)`
  - `public static Utilisateur reconstituer(Long id, Matricule matricule, NomComplet nomComplet,
                                           Email email, MotDePasse motDePasse, Role role,
                                           boolean actif, int tentativesEchouees, boolean compteBloque,
                                           String sessionActuelle, LocalDateTime dateCreation,
                                           LocalDateTime dateModification, Long version,
                                           String cni, String telephone, String residence, String photo,
                                           LocalDate dateNaissance, String lieuNaissance,
                                           LocalDate dateEmbauche, String sexe)`
  - `public String ouvrirNouvelleSession()`
  - `public void enregistrerConnexion()`
  - `public void enregistrerEchecConnexion()`
  - `public void debloquer()`
  - `public void cloreSession()`
  - `public void desactiver()`
  - `public void reactiver()`
  - `public void changerMotDePasse(MotDePasse nouveau)`
  - `public void changerRole(Role nouveauRole)`
  - `public void modifierProfil(String telephone, String residence, String photo,
                                String cni, LocalDate dateNaissance, String lieuNaissance,
                                String sexe)`
  - `public void setDateEmbauche(LocalDate dateEmbauche)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public Matricule getMatricule()`
  - `public NomComplet getNomComplet()`
  - `public Email getEmail()`
  - `public MotDePasse getMotDePasse()`
  - `public Role getRole()`
  - `public boolean isActif()`
  - `public int getTentativesEchouees()`
  - `public boolean isCompteBloque()`
  - `public String getSessionActuelle()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `public String getCni()`
  - `public String getTelephone()`
  - `public String getResidence()`
  - `public String getPhoto()`
  - `public LocalDate getDateNaissance()`
  - `public String getLieuNaissance()`
  - `public LocalDate getDateEmbauche()`
  - `public String getSexe()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\auth\domain\utilisateur\enums\Role.java`

**enum** `Role` (public)

- **Constantes :**
  - `{

    DIRECTEUR_GENERAL("")`
  - `DIRECTEUR_COMMERCIAL("")`
  - `GESTIONNAIRE_STOCK("")`
  - `CHEF_PRODUCTION("")`
  - `RESPONSABLE_SALLE("")`
  - `CHEF_MACHINISTE("")`
  - `MACHINISTE("")`
  - `AGENT_DOSEUR("")`
  - `AGENT_PRODUCTION("")`
  - `CONTROLEUR_GENERAL("")`
  - `COMMERCIAL("")`
  - `SECRETAIRE("")`
  - `COMPTABLE("")`
  - `CHARGEE_RP("")`
  - `ADMIN("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\auth\domain\utilisateur\events\CompteBloqueEvent.java`

**class** `CompteBloqueEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String matricule`
  - `private final String nomComplet`

- **Méthodes :**
  - `public CompteBloqueEvent(String matricule, String nomComplet)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getMatricule()`
  - `public String getNomComplet()`

#### `modules\auth\domain\utilisateur\events\UtilisateurConnecteEvent.java`

**class** `UtilisateurConnecteEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String matricule`
  - `private final String nomComplet`
  - `private final Role role`

- **Méthodes :**
  - `public UtilisateurConnecteEvent(String matricule, String nomComplet, Role role)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getMatricule()`
  - `public String getNomComplet()`
  - `public Role getRole()`

#### `modules\auth\domain\utilisateur\events\UtilisateurCreeEvent.java`

**class** `UtilisateurCreeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String matricule`
  - `private final String nomComplet`
  - `private final String email`
  - `private final Role role`

- **Méthodes :**
  - `public UtilisateurCreeEvent(String matricule, String nomComplet, String email, Role role)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getMatricule()`
  - `public String getNomComplet()`
  - `public String getEmail()`
  - `public Role getRole()`

#### `modules\auth\domain\utilisateur\exceptions\AdminException.java`

**class** `AdminException` (public abstract) extends `RuntimeException`

- **Méthodes :**
  - `protected AdminException(String message)`

#### `modules\auth\domain\utilisateur\exceptions\CompteBloqueException.java`

**class** `CompteBloqueException` (public) extends `AdminException`

- **Méthodes :**
  - `public CompteBloqueException(String matricule)`

#### `modules\auth\domain\utilisateur\exceptions\CompteDesactiveException.java`

**class** `CompteDesactiveException` (public) extends `AdminException`

- **Méthodes :**
  - `public CompteDesactiveException(String matricule)`

#### `modules\auth\domain\utilisateur\exceptions\CredentialsInvalidesException.java`

**class** `CredentialsInvalidesException` (public) extends `AdminException`

- **Méthodes :**
  - `public CredentialsInvalidesException()`

#### `modules\auth\domain\utilisateur\exceptions\EmailDejaUtiliseException.java`

**class** `EmailDejaUtiliseException` (public) extends `AdminException`

- **Méthodes :**
  - `public EmailDejaUtiliseException(String email)`

#### `modules\auth\domain\utilisateur\exceptions\UtilisateurNonTrouveException.java`

**class** `UtilisateurNonTrouveException` (public) extends `AdminException`

- **Méthodes :**
  - `public UtilisateurNonTrouveException(String matricule)`

#### `modules\auth\domain\utilisateur\repository\UtilisateurRepository.java`

**interface** `UtilisateurRepository` (public)

#### `modules\auth\domain\utilisateur\valueobject\Email.java`

**class** `Email` (public final)

- **Champs :**
  - `private final String valeur`

- **Méthodes :**
  - `private Email(String valeur)`
  - `public static Email de(String valeur)`
  - `public String getValeur()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\auth\domain\utilisateur\valueobject\Matricule.java`

**class** `Matricule` (public final)

- **Champs :**
  - `private final String valeur`

- **Méthodes :**
  - `private Matricule(String valeur)`
  - `public static Matricule de(String valeur)`
  - `public String getValeur()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\auth\domain\utilisateur\valueobject\MotDePasse.java`

**class** `MotDePasse` (public final)

- **Champs :**
  - `private final String hash`

- **Méthodes :**
  - `private MotDePasse(String hash)`
  - `public static MotDePasse deHash(String hash)`
  - `public String getHash()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\auth\domain\utilisateur\valueobject\NomComplet.java`

**class** `NomComplet` (public final)

- **Champs :**
  - `private final String prenom`
  - `private final String nom`
  - `NomComplet that = (NomComplet) o`

- **Méthodes :**
  - `private NomComplet(String prenom, String nom)`
  - `public static NomComplet de(String prenom, String nom)`
  - `public String getPrenom()`
  - `public String getNom()`
  - `public String getNomComplet()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\auth\infrastructure\persistence\jpa\OtpCodeJpaEntity.java`

**class** `OtpCodeJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, length = 20) private String matricule`
  - `@Column(nullable = false, length = 10) private String codeHash`
  - `@Column(name = "", length = 20) private String telephone`
  - `@Column(nullable = false) private boolean utilise`
  - `@Column(name = "", nullable = false) private LocalDateTime dateCreation`
  - `@Column(name = "", nullable = false) private LocalDateTime dateExpiration`
  - `@Column(name = "", nullable = false) private int tentativesVerification`

#### `modules\auth\infrastructure\persistence\jpa\OtpCodeJpaRepository.java`

**interface** `OtpCodeJpaRepository` (public) extends `JpaRepository<OtpCodeJpaEntity, Long>`

#### `modules\auth\infrastructure\persistence\jpa\UtilisateurJpaEntity.java`

**class** `UtilisateurJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(unique = true, nullable = false, length = 20) private String matricule`
  - `@Column(nullable = false, length = 100) private String prenom`
  - `@Column(nullable = false, length = 100) private String nom`
  - `@Column(unique = true, nullable = false, length = 150) private String email`
  - `@Column(name = "", nullable = false) private String motDePasseHash`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30) private Role role`
  - `@Column(nullable = false) private boolean actif = true`
  - `@Column(name = "", nullable = false) private int tentativesEchouees = 0`
  - `@Column(name = "", nullable = false) private boolean compteBloque = false`
  - `@Column(name = "", length = 40) private String sessionActuelle`
  - `@Column(name = "", nullable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Column(name = "", length = 30) private String cni`
  - `@Column(name = "", length = 20) private String telephone`
  - `@Column(name = "", length = 150) private String residence`
  - `@Column(name = "", length = 500) private String photo`
  - `LocalDate dateNaissance`
  - `@Column(name = "", length = 100) private String lieuNaissance`
  - `LocalDate dateEmbauche`
  - `@Column(name = "", length = 10) private String sexe`
  - `@Version private Long version`

#### `modules\auth\infrastructure\persistence\jpa\UtilisateurJpaRepository.java`

**interface** `UtilisateurJpaRepository` (@Repository
public) extends `JpaRepository<UtilisateurJpaEntity, Long>`

#### `modules\auth\infrastructure\persistence\matricule\MatriculeSequenceService.java`

**class** `MatriculeSequenceService` (@Service
public)

- **Champs :**
  - `private final UtilisateurJpaRepository jpaRepository`

- **Méthodes :**
  - `public MatriculeSequenceService(UtilisateurJpaRepository jpaRepository)`
  - `@Transactional public String generer(Role role)`

#### `modules\auth\infrastructure\persistence\repository\UtilisateurRepositoryImpl.java`

**class** `UtilisateurRepositoryImpl` (@Repository
public) implements `UtilisateurRepository`

- **Champs :**
  - `private final UtilisateurJpaRepository jpa`

- **Méthodes :**
  - `public UtilisateurRepositoryImpl(UtilisateurJpaRepository jpa)`
  - `@Override public Utilisateur sauvegarder(Utilisateur u)`
  - `@Override public Optional<Utilisateur> trouverParMatricule(Matricule matricule)`
  - `@Override public Optional<Utilisateur> trouverParEmail(Email email)`
  - `@Override public boolean existeParEmail(Email email)`
  - `@Override public boolean existeParMatricule(Matricule matricule)`
  - `@Override public List<Utilisateur> listerParRole(Role role)`
  - `@Override public List<Utilisateur> listerTous()`
  - `private UtilisateurJpaEntity toEntity(Utilisateur u)`
  - `private Utilisateur toDomain(UtilisateurJpaEntity e)`

#### `modules\auth\infrastructure\persistence\rh\jpa\EmployeJpaEntity.java`

**class** `EmployeJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true, length = 50) private String matricule`
  - `@Column(nullable = false, length = 100) private String nom`
  - `@Column(nullable = false, length = 100) private String prenom`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30) private Role role`
  - `@Column(nullable = false, length = 50) private String zoneTravail`
  - `@Column(length = 50) private String superviseurMatricule`
  - `@Column(nullable = false) private boolean avecCompte`
  - `@Column(nullable = false) private boolean actif`
  - `private LocalDate dateEmbauche`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\auth\infrastructure\persistence\rh\jpa\EmployeJpaRepository.java`

**interface** `EmployeJpaRepository` (@Repository
public) extends `JpaRepository<EmployeJpaEntity, Long>`

#### `modules\auth\infrastructure\persistence\rh\mapper\EmployePersistenceMapper.java`

**class** `EmployePersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public EmployeJpaEntity versEntite(Employe e)`
  - `public Employe versDomaine(EmployeJpaEntity e)`

#### `modules\auth\infrastructure\persistence\rh\matricule\GenerateurMatriculeEmploye.java`

**class** `GenerateurMatriculeEmploye` (@Component
public)

- **Champs :**
  - `private static final int MAX_TENTATIVES = 10`
  - `private final EmployeRepository employeRepository`
  - `int tentative = 0`
  - `tentative < MAX_TENTATIVES`

- **Méthodes :**
  - `public GenerateurMatriculeEmploye(EmployeRepository employeRepository)`
  - `public String generer(String nom, String zoneTravail)`
  - `private String extrairePrefixeNom(String nom)`
  - `private String normaliser(String valeur)`
  - `private String troisChiffres()`

#### `modules\auth\infrastructure\persistence\rh\repository\EmployeRepositoryImpl.java`

**class** `EmployeRepositoryImpl` (@Repository
public) implements `EmployeRepository`

- **Champs :**
  - `private final EmployeJpaRepository jpaRepository`
  - `private final EmployePersistenceMapper mapper`

- **Méthodes :**
  - `public EmployeRepositoryImpl(EmployeJpaRepository jpaRepository, EmployePersistenceMapper mapper)`
  - `@Override public Employe sauvegarder(Employe employe)`
  - `@Override public Optional<Employe> trouverParMatricule(String matricule)`
  - `@Override public List<Employe> trouverParRole(Role role)`
  - `@Override public List<Employe> trouverParRoleEtActif(Role role, boolean actif)`
  - `@Override public List<Employe> trouverParSuperviseur(String superviseurMatricule)`
  - `@Override public List<Employe> trouverTous()`
  - `@Override public boolean existeParMatricule(String matricule)`

#### `modules\auth\infrastructure\security\JwtAuthFilter.java`

**class** `JwtAuthFilter` (@Component
public) extends `OncePerRequestFilter`

- **Champs :**
  - `private final JwtService jwtService`
  - `private final UtilisateurRepository utilisateurRepository`

- **Méthodes :**
  - `public JwtAuthFilter(JwtService jwtService, UtilisateurRepository utilisateurRepository)`
  - `@Override protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)` throws `ServletException, IOException`
  - `private Optional<Utilisateur> chargerUtilisateur(String matricule)`

#### `modules\auth\infrastructure\security\JwtService.java`

**class** `JwtService` (@Service
public)

- **Champs :**
  - `private final SecretKey secretKey`
  - `private final long expirationMs`

- **Méthodes :**
  - `public String genererToken(String matricule, String role, String sessionId)`
  - `@Deprecated public String genererToken(String matricule, String role)`
  - `public String extraireMatricule(String token)`
  - `public String extraireRole(String token)`
  - `public String extraireSessionId(String token)`
  - `public boolean estValide(String token)`
  - `public long getExpirationMs()`
  - `private Claims extraireClaims(String token)`

#### `modules\auth\infrastructure\security\OtpService.java`

**class** `OtpService` (@Service("")
public)

- **Champs :**
  - `private static final int LONGUEUR_CODE = 6`
  - `private static final int DUREE_VALIDITE_MINUTES = 5`
  - `private static final int MAX_TENTATIVES_VERIFICATION = 3`
  - `private final OtpCodeJpaRepository otpRepository`
  - `int i = 0`
  - `i < LONGUEUR_CODE`

- **Méthodes :**
  - `public OtpService(OtpCodeJpaRepository otpRepository)`
  - `@Transactional public String genererEtEnvoyerOtp(String matricule, String telephone)`
  - `@Transactional public boolean verifierOtp(String matricule, String codeSaisi)`
  - `@Transactional public void purgerOtpExpires()`
  - `private String genererCode()`
  - `private void envoyerOtpParSms(String telephone, String code, String matricule)`

#### `modules\auth\infrastructure\security\SecurityConfig.java`

**class** `SecurityConfig` (@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public)

- **Champs :**
  - `private final JwtAuthFilter jwtAuthFilter`

#### `modules\auth\presentation\controller\AuthController.java`

**class** `AuthController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final LoginUseCase loginUseCase`
  - `private final RegisterUseCase registerUseCase`
  - `private final LogoutUseCase logoutUseCase`
  - `private final GererProfilUseCase gererProfilUseCase`
  - `private final GererUtilisateursUseCase gererUtilisateursUseCase`
  - `private final DemanderOtpUseCase demanderOtpUseCase`
  - `private final VerifierOtpUseCase verifierOtpUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public AuthController(LoginUseCase loginUseCase, RegisterUseCase registerUseCase,
                           LogoutUseCase logoutUseCase, GererProfilUseCase gererProfilUseCase,
                           GererUtilisateursUseCase gererUtilisateursUseCase,
                           DemanderOtpUseCase demanderOtpUseCase,
                           VerifierOtpUseCase verifierOtpUseCase,
                           AuthenticatedUserResolver userResolver)`
  - `@PostMapping("")
    @Operation(summary = "",
        description = "" +
                      "") public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request)`
  - `@PostMapping("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<OtpResponse>> demanderOtp(@RequestBody @Valid DemanderOtpRequest request)`
  - `@PostMapping("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<LoginResponse>> verifierOtp(@RequestBody @Valid VerifierOtpRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "", description = "") public ResponseEntity<ApiResponse<Map<String, String>>> register(@RequestBody @Valid RegisterRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "", description = "") public ResponseEntity<ApiResponse<Void>> logout()`
  - `@GetMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProfilResponse>> monProfil()`
  - `@PutMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<ProfilResponse>> modifierProfil(@RequestBody ModifierProfilRequest request)`
  - `@GetMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProfilResponse>> consulterUtilisateur(@PathVariable String matricule)`
  - `@PutMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProfilResponse>> modifierUtilisateur(@PathVariable String matricule,
        @RequestBody @Valid ModifierUtilisateurRequest request)`
  - `@PutMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> desactiverUtilisateur(@PathVariable String matricule)`
  - `@PutMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> reactiverUtilisateur(@PathVariable String matricule)`
  - `@PutMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> debloquerUtilisateur(@PathVariable String matricule)`
  - `@PutMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> changerMotDePasse(@PathVariable String matricule,
        @RequestBody Map<String, String> body)`
  - `@PutMapping("")
    @PreAuthorize("")
    @SecurityRequirement(name = "")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> changerRole(@PathVariable String matricule,
        @RequestBody Map<String, Role> body)`


### Module `commercial`

#### `modules\commercial\application\carburant\dto\response\CarburantResponse.java`

**record** `CarburantResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referenceCarburant`
  - `String matriculeCommercial`
  - `String immatriculation`
  - `String stationPartenaire`
  - `int kilometrage`
  - `double litresDemandes`
  - `double montant`
  - `LocalDate date`
  - `String statut`
  - `String signatureCommercial`
  - `String signatureSuperviseur`
  - `String motifRefus`
  - `String photoCompteur`
  - `double caZoneMoisCourantFCFA`
  - `double ratioCarburantSurCaPourcent`

#### `modules\commercial\application\carburant\mapper\CarburantMapper.java`

**class** `CarburantMapper` (@Component
public)

- **Champs :**
  - `private final VenteRepository venteRepository`

- **Méthodes :**
  - `public CarburantMapper(VenteRepository venteRepository)`
  - `public CarburantResponse versResponse(DemandeCarburant d)`

#### `modules\commercial\application\client\dto\response\ClientResponse.java`

**record** `ClientResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String codeClient`
  - `String nom`
  - `String telephone`
  - `String adresse`
  - `String localite`
  - `String type`
  - `String matriculeCommercialReferent`
  - `boolean actif`

#### `modules\commercial\application\client\mapper\ClientMapper.java`

**class** `ClientMapper` (@Component
public)

- **Méthodes :**
  - `public ClientResponse versResponse(Client c)`

#### `modules\commercial\application\commandedistance\dto\response\CommandeDistanceResponse.java`

**record** `CommandeDistanceResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroCommande`
  - `String codeClient`
  - `String matriculeCommercial`
  - `String canal`
  - `LocalDate dateCommande`
  - `String statut`
  - `String matriculeValidateurSecretaire`
  - `String matriculeValidateurComptable`
  - `String motifAnnulation`
  - `String observations`
  - `double montantTotal`
  - `LocalDateTime dateCreation`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String codeProduit, String designation, String conditionnement,
                                int quantite, double prixUnitaire)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `String conditionnement`
  - `int quantite`
  - `double prixUnitaire`

#### `modules\commercial\application\commandedistance\mapper\CommandeDistanceMapper.java`

**class** `CommandeDistanceMapper` (@Component
public)

- **Méthodes :**
  - `public CommandeDistanceResponse versResponse(CommandeDistance cd)`

#### `modules\commercial\application\dashboard\dto\FicheSyntheseResponse.java`

**record** `FicheSyntheseResponse` (public)

- **Composants (record) :**
  - `LocalDate date`
  - `double caYaounde`
  - `double caDouala`
  - `double caTotal`
  - `int effectifCommerciaux`
  - `double vacInit`
  - `double ca`
  - `double vac`
  - `double recouvrements`
  - `double vteCpt`
  - `double soldeVAC`
  - `double cumulMensuelCA`
  - `double eCheques`
  - `double vstBanques`
  - `double depensesJustifiees`
  - `double depensesNonJust`
  - `double sCaisse`
  - `double montantAvaries`
  - `double montantDotPromo`
  - `List<LigneFicheSynthese> lignes`

- **Méthodes :**
  - `public record LigneFicheSynthese(String codeProduit,
        long qteProduite,
        long stockVeille,
        long stockCamion,
        long stockTotal,
        long qteSorties,
        long stockFinal,
        long qteAvoDotProm,
        long qteFacturee)`

**record** `LigneFicheSynthese` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `long qteProduite`
  - `long stockVeille`
  - `long stockCamion`
  - `long stockTotal`
  - `long qteSorties`
  - `long stockFinal`
  - `long qteAvoDotProm`
  - `long qteFacturee`

#### `modules\commercial\application\dashboard\usecase\DashboardCommercialUseCase.java`

**class** `DashboardCommercialUseCase` (@Service
public)

- **Champs :**
  - `private final VenteRepository venteRepository`
  - `private final FeuillRouteRepository feuillRouteRepository`
  - `private final CarburantRepository carburantRepository`
  - `private final RecouvrementRepository recouvrementRepository`
  - `int totalArrets = arretVises + arretNonVises`
  - `int tauxVisite = totalArrets > 0 ? (int) ((arretVises * 100.0) / totalArrets) : 0`
  - `int totalArrets = totalArretsVises + totalArretsNonVises`
  - `int tauxVisiteGlobal = totalArrets > 0 ? (int) ((totalArretsVises * 100.0) / totalArrets) : 0`
  - `double rendement = litresTotal > 0 ? caSemaine / litresTotal : 0.0`

- **Méthodes :**
  - `public DashboardCommercialUseCase(VenteRepository venteRepository,
                                       FeuillRouteRepository feuillRouteRepository,
                                       CarburantRepository carburantRepository,
                                       RecouvrementRepository recouvrementRepository)`
  - `public DashboardCommercial executerCommercial(String matriculeCommercial)`
  - `public DashboardDG executerDG()`
  - `public record DashboardCommercial(String matriculeCommercial, LocalDate date,
        double caJour, double caSemaine,
        int nbVentesJour, int nbVentesSemaine,
        int arretVises, int arretNonVises, int tauxVisite,
        double litresCarburantSemaine,
        Map<String, Double> top5Produits,
        List<String> feuillesRouteJour)`
  - `public record DashboardDG(LocalDate date,
        double caJour, double caSemaine,
        int nbVentesJour, int nbVentesSemaine,
        int totalArretsVises, int totalArretsNonVises, int tauxVisiteGlobal,
        int nbRecouvrementsEnCours, int nbRecouvrementsContentieux, double totalCreances,
        double litresCarburantSemaine, double rendementCAparLitre,
        Map<String, Double> caParCommercial)`

**record** `DashboardCommercial` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `LocalDate date`
  - `double caJour`
  - `double caSemaine`
  - `int nbVentesJour`
  - `int nbVentesSemaine`
  - `int arretVises`
  - `int arretNonVises`
  - `int tauxVisite`
  - `double litresCarburantSemaine`
  - `Map<String`
  - `Double> top5Produits`
  - `List<String> feuillesRouteJour`

**record** `DashboardDG` (public)

- **Composants (record) :**
  - `LocalDate date`
  - `double caJour`
  - `double caSemaine`
  - `int nbVentesJour`
  - `int nbVentesSemaine`
  - `int totalArretsVises`
  - `int totalArretsNonVises`
  - `int tauxVisiteGlobal`
  - `int nbRecouvrementsEnCours`
  - `int nbRecouvrementsContentieux`
  - `double totalCreances`
  - `double litresCarburantSemaine`
  - `double rendementCAparLitre`
  - `Map<String`
  - `Double> caParCommercial`

#### `modules\commercial\application\dashboard\usecase\FicheSyntheseUseCase.java`

**class** `FicheSyntheseUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final VenteRepository venteRepository`
  - `private final VersementRepository versementRepository`
  - `private final RecouvrementRepository recouvrementRepository`
  - `private final JournalCaisseRepository caisseRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `double vacInit = caTotal`
  - `Map<String, long[]> ventesParProduit = new HashMap<>()`

- **Méthodes :**
  - `public FicheSyntheseUseCase(VenteRepository venteRepository,
                                 VersementRepository versementRepository,
                                 RecouvrementRepository recouvrementRepository,
                                 JournalCaisseRepository caisseRepository,
                                 NiveauStockRepository niveauStockRepository)`
  - `public FicheSyntheseResponse executer(LocalDate date)`

#### `modules\commercial\application\feuillroute\dto\response\FeuillRouteResponse.java`

**record** `FeuillRouteResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroFeuillRoute`
  - `String matriculeCommercial`
  - `LocalDate date`
  - `String statut`
  - `String matriculeCloture`
  - `LocalDateTime heureCloture`
  - `int nombreArretVises`
  - `int nombreArretNonVises`
  - `List<ArretResponse> arrets`

- **Méthodes :**
  - `public record ArretResponse(Long id,
        String codeClient,
        int ordre,
        String statut,
        String numeroVente,
        String codeInfoClient,
        Integer quantiteInfo,
        String observations,
        LocalDateTime heurePlanifiee,
        LocalDateTime heureEffective,
        String coordonneesGPS)`

**record** `ArretResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String codeClient`
  - `int ordre`
  - `String statut`
  - `String numeroVente`
  - `String codeInfoClient`
  - `Integer quantiteInfo`
  - `String observations`
  - `LocalDateTime heurePlanifiee`
  - `LocalDateTime heureEffective`
  - `String coordonneesGPS`

#### `modules\commercial\application\feuillroute\mapper\FeuillRouteMapper.java`

**class** `FeuillRouteMapper` (@Component
public)

- **Méthodes :**
  - `public FeuillRouteResponse versResponse(FeuillRoute fr)`

#### `modules\commercial\application\precommande\dto\response\PreCommandeResponse.java`

**record** `PreCommandeResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroPreCommande`
  - `String matriculeCommercial`
  - `LocalDate dateSoumission`
  - `LocalDate dateEffet`
  - `String statut`
  - `String matriculeValidateurSecretaire`
  - `String matriculeValidateurComptable`
  - `String motifRefus`
  - `boolean soumiseEnSoiree`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String codeProduit, String designationProduit, String conditionnement,
                                int quantiteDemandee, int quantiteValidee)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designationProduit`
  - `String conditionnement`
  - `int quantiteDemandee`
  - `int quantiteValidee`

#### `modules\commercial\application\precommande\mapper\PreCommandeMapper.java`

**class** `PreCommandeMapper` (@Component
public)

- **Méthodes :**
  - `public PreCommandeResponse versResponse(PreCommande p)`

#### `modules\commercial\application\prospect\dto\response\ProspectResponse.java`

**record** `ProspectResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String codeProspect`
  - `String nom`
  - `String telephone`
  - `String adresse`
  - `String localite`
  - `String matriculeCommercial`
  - `String canal`
  - `String statut`
  - `String observations`
  - `String codeClientConverti`
  - `LocalDate dateProspection`
  - `LocalDateTime dateCreation`

#### `modules\commercial\application\prospect\mapper\ProspectMapper.java`

**class** `ProspectMapper` (@Component
public)

- **Méthodes :**
  - `public ProspectResponse versResponse(Prospect p)`

#### `modules\commercial\application\rapport\usecase\CalculerPrimesHebdoUseCase.java`

**class** `CalculerPrimesHebdoUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `public static final double MONTANT_PRIME = 12_000.0`
  - `public static final double SEUIL_GLOBAL_POURCENT = 80.0`
  - `public static final double SEUIL_PAR_GAMME_POURCENT = 75.0`
  - `private final ObjectifCommercialRepository objectifRepository`
  - `private final VenteRepository venteRepository`
  - `List<PrimeResultat> resultats = new ArrayList<>()`
  - `double tauxGlobal = caCible > 0 ? (caRealise / caCible) * 100.0 : 0.0`
  - `Map<String, Double> tauxParGamme = new LinkedHashMap<>()`
  - `List<String> gammesBloquantes = new ArrayList<>()`
  - `boolean toutesGammesOK = true`
  - `double tauxGamme = cibleGamme > 0 ? (caGamme / cibleGamme) * 100.0 : 0.0`
  - `boolean conditionGlobale = tauxGlobal >= SEUIL_GLOBAL_POURCENT`
  - `boolean eligible = conditionGlobale && toutesGammesOK`
  - `Map<String, Double> caParGamme = new HashMap<>()`

- **Méthodes :**
  - `public CalculerPrimesHebdoUseCase(ObjectifCommercialRepository objectifRepository,
                                       VenteRepository venteRepository)`
  - `public List<PrimeResultat> executer(LocalDate semaineDebut, LocalDate semaineFin)`
  - `private Map<String, Double> calculerCAParGamme(List<Vente> ventes)`
  - `private String extraireGamme(String codeProduit)`
  - `public record PrimeResultat(String matriculeCommercial,
        double caRealise,
        double caCible,
        double tauxGlobal,
        Map<String, Double> tauxParGamme,
        boolean eligible,
        double montantPrime,
        List<String> gammesBloquantes,
        boolean conditionGlobaleOK,
        boolean conditionGammesOK)`

**record** `PrimeResultat` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `double caRealise`
  - `double caCible`
  - `double tauxGlobal`
  - `Map<String`
  - `Double> tauxParGamme`
  - `boolean eligible`
  - `double montantPrime`
  - `List<String> gammesBloquantes`
  - `boolean conditionGlobaleOK`
  - `boolean conditionGammesOK`

#### `modules\commercial\application\rapport\usecase\GenererRapportRendementCarburantUseCase.java`

**class** `GenererRapportRendementCarburantUseCase` (@Service
public)

- **Champs :**
  - `private final VenteRepository venteRepository`
  - `private final CarburantRepository carburantRepository`
  - `Set<String> tousCommerciaux = new LinkedHashSet<>()`
  - `double rendement = litres > 0 ? ca / litres : 0.0`

- **Méthodes :**
  - `public GenererRapportRendementCarburantUseCase(VenteRepository venteRepository,
                                                     CarburantRepository carburantRepository)`
  - `public List<LigneRendement> executer(LocalDate debut, LocalDate fin)`
  - `public record LigneRendement(String matriculeCommercial,
        double caPeriode,
        double litresConsommes,
        double rendementCAparLitre)`

**record** `LigneRendement` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `double caPeriode`
  - `double litresConsommes`
  - `double rendementCAparLitre`

#### `modules\commercial\application\rapport\usecase\GenererRapportTourneeUseCase.java`

**class** `GenererRapportTourneeUseCase` (@Service
public)

- **Champs :**
  - `private final FeuillRouteRepository feuillRouteRepository`
  - `private final ProspectRepository prospectRepository`
  - `Map<String, LigneTournee> parCommercial = new LinkedHashMap<>()`
  - `int tauxGlobal = totalArrets > 0 ? (int) ((totalVises * 100.0) / totalArrets) : 0`
  - `public final String matriculeCommercial`
  - `public int nbFeuilles`
  - `public int totalArrets`
  - `public int arretsVises`
  - `public int arretsNonVises`
  - `public int tauxVisite`

- **Méthodes :**
  - `public GenererRapportTourneeUseCase(FeuillRouteRepository feuillRouteRepository,
                                          ProspectRepository prospectRepository)`
  - `public RapportTournee executer(LocalDate debut, LocalDate fin)`
  - `public LigneTournee(String matriculeCommercial, int nbFeuilles, int totalArrets,
                            int arretsVises, int arretsNonVises, int tauxVisite)`
  - `public int tauxVisite()`
  - `public record RapportTournee(LocalDate debut, LocalDate fin,
        List<LigneTournee> detailsParCommercial,
        int tauxVisiteGlobal,
        int prospectNouveaux,
        int prospectConvertis,
        int prospectNonInteresses,
        int prospectArchives)`

**class** `LigneTournee` (public static)

- **Champs :**
  - `public final String matriculeCommercial`
  - `public int nbFeuilles`
  - `public int totalArrets`
  - `public int arretsVises`
  - `public int arretsNonVises`
  - `public int tauxVisite`

- **Méthodes :**
  - `public LigneTournee(String matriculeCommercial, int nbFeuilles, int totalArrets,
                            int arretsVises, int arretsNonVises, int tauxVisite)`
  - `public int tauxVisite()`

**record** `RapportTournee` (public)

- **Composants (record) :**
  - `LocalDate debut`
  - `LocalDate fin`
  - `List<LigneTournee> detailsParCommercial`
  - `int tauxVisiteGlobal`
  - `int prospectNouveaux`
  - `int prospectConvertis`
  - `int prospectNonInteresses`
  - `int prospectArchives`

#### `modules\commercial\application\rapport\usecase\GenererRapportVentesUseCase.java`

**class** `GenererRapportVentesUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final VenteRepository venteRepository`
  - `private final VenteMapper venteMapper`
  - `private final TableExporter tableExporter`
  - `String titre = ""`

- **Méthodes :**
  - `public GenererRapportVentesUseCase(VenteRepository venteRepository, VenteMapper venteMapper, TableExporter tableExporter)`
  - `public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format)`
  - `private List<List<String>> lignes(List<VenteResponse> ventes)`

#### `modules\commercial\application\recouvrement\dto\response\RecouvrementResponse.java`

**record** `RecouvrementResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referenceRecouvrement`
  - `String codeClient`
  - `String nomClient`
  - `double montantDu`
  - `double montantRembourse`
  - `double montantRestant`
  - `LocalDate dateEcheance`
  - `String statut`
  - `String matriculeCommercial`
  - `String observations`

#### `modules\commercial\application\recouvrement\mapper\RecouvrementMapper.java`

**class** `RecouvrementMapper` (@Component
public)

- **Méthodes :**
  - `public RecouvrementResponse versResponse(Recouvrement r)`

#### `modules\commercial\application\tarification\dto\request\AccorderPrixExceptionnelRequest.java`

**record** `AccorderPrixExceptionnelRequest` (public)

- **Composants (record) :**
  - `@NotBlank String codeProduit`
  - `@NotBlank String codeClient`
  - `@NotNull @Positive Double prixAppliqueFCFA`
  - `@NotBlank String motif`
  - `String referenceVenteCible`
  - `@NotBlank String codeOtp`

#### `modules\commercial\application\tarification\dto\request\DefinirGrilleTarifaireRequest.java`

**record** `DefinirGrilleTarifaireRequest` (public)

- **Composants (record) :**
  - `@NotBlank String codeProduit`
  - `@NotNull TypeClient segment`
  - `@NotNull @Positive Double prixHomologueFCFA`

#### `modules\commercial\application\tarification\dto\response\GrilleTarifaireResponse.java`

**record** `GrilleTarifaireResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String codeProduit`
  - `String segment`
  - `double prixHomologueFCFA`
  - `boolean actif`
  - `String matriculeAuteur`
  - `LocalDateTime dateModification`

#### `modules\commercial\application\tarification\dto\response\PrixExceptionnelResponse.java`

**record** `PrixExceptionnelResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String reference`
  - `String codeProduit`
  - `String codeClient`
  - `double prixHomologueFCFA`
  - `double prixAppliqueFCFA`
  - `double ecartFCFA`
  - `String motif`
  - `String matriculeAutorisateur`
  - `String referenceVenteCible`
  - `LocalDateTime dateAutorisation`

#### `modules\commercial\application\tarification\mapper\GrilleTarifaireMapper.java`

**class** `GrilleTarifaireMapper` (@Component
public)

- **Méthodes :**
  - `public GrilleTarifaireResponse versResponse(GrilleTarifaire g)`

#### `modules\commercial\application\tarification\mapper\PrixExceptionnelMapper.java`

**class** `PrixExceptionnelMapper` (@Component
public)

- **Méthodes :**
  - `public PrixExceptionnelResponse versResponse(PrixExceptionnel p)`

#### `modules\commercial\application\tarification\service\TarificationGuardService.java`

**class** `TarificationGuardService` (@Service
public)

- **Champs :**
  - `private final ClientRepository clientRepository`
  - `private final GrilleTarifaireRepository grilleTarifaireRepository`
  - `private final PrixExceptionnelRepository prixExceptionnelRepository`

- **Méthodes :**
  - `public TarificationGuardService(ClientRepository clientRepository,
                                     GrilleTarifaireRepository grilleTarifaireRepository,
                                     PrixExceptionnelRepository prixExceptionnelRepository)`
  - `public void validerPrix(String codeClient, String codeProduit, double prixUnitaireSoumis,
                             String referencePrixExceptionnel)`

#### `modules\commercial\application\tarification\usecase\AccorderPrixExceptionnelUseCase.java`

**class** `AccorderPrixExceptionnelUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final PrixExceptionnelRepository prixExceptionnelRepository`
  - `private final GrilleTarifaireRepository grilleTarifaireRepository`
  - `private final ClientRepository clientRepository`
  - `private final PrixExceptionnelMapper mapper`
  - `private final OtpService otpService`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public AccorderPrixExceptionnelUseCase(PrixExceptionnelRepository prixExceptionnelRepository,
                                            GrilleTarifaireRepository grilleTarifaireRepository,
                                            ClientRepository clientRepository, PrixExceptionnelMapper mapper,
                                            OtpService otpService, EventPublisher eventPublisher)`
  - `public PrixExceptionnelResponse executer(AccorderPrixExceptionnelRequest request, Utilisateur acteur)`

#### `modules\commercial\application\tarification\usecase\DefinirGrilleTarifaireUseCase.java`

**class** `DefinirGrilleTarifaireUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final GrilleTarifaireRepository repository`
  - `private final GrilleTarifaireMapper mapper`

- **Méthodes :**
  - `public DefinirGrilleTarifaireUseCase(GrilleTarifaireRepository repository, GrilleTarifaireMapper mapper)`
  - `public GrilleTarifaireResponse executer(DefinirGrilleTarifaireRequest request, Utilisateur acteur)`

#### `modules\commercial\application\vente\dto\response\VenteResponse.java`

**record** `VenteResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroVente`
  - `String matriculeCommercial`
  - `String codeClient`
  - `LocalDate date`
  - `String modeReglement`
  - `String typeVente`
  - `String statut`
  - `String syncStatus`
  - `String coordonneesGPS`
  - `String idFeuillRoute`
  - `double montantTotal`
  - `String observations`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String codeProduit, String designationProduit, String conditionnement,
                                int quantite, double prixUnitaire, double remise)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designationProduit`
  - `String conditionnement`
  - `int quantite`
  - `double prixUnitaire`
  - `double remise`

#### `modules\commercial\application\vente\mapper\VenteMapper.java`

**class** `VenteMapper` (@Component
public)

- **Méthodes :**
  - `public VenteResponse versResponse(Vente v)`

#### `modules\commercial\application\vente\usecase\DeclencherTransactionNonMarchandeUseCase.java`

**class** `DeclencherTransactionNonMarchandeUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final VenteRepository venteRepository`

- **Méthodes :**
  - `public DeclencherTransactionNonMarchandeUseCase(VenteRepository venteRepository)`
  - `public Vente executer(NonMarchandeRequest req)`
  - `public record NonMarchandeRequest(TypeVente typeVente,
        String matriculeCommercial,
        String codeClient,
        String codeProduit,
        String designationProduit,
        String conditionnement,
        int quantite,
        LocalDate date,
        String motif,
        double pourcentageLot,
        String photoJustificative)`

**record** `NonMarchandeRequest` (public)

- **Composants (record) :**
  - `TypeVente typeVente`
  - `String matriculeCommercial`
  - `String codeClient`
  - `String codeProduit`
  - `String designationProduit`
  - `String conditionnement`
  - `int quantite`
  - `LocalDate date`
  - `String motif`
  - `double pourcentageLot`
  - `String photoJustificative`

#### `modules\commercial\domain\carburant\aggregate\DemandeCarburant.java`

**class** `DemandeCarburant` (public)

- **Champs :**
  - `private Long id`
  - `private final String referenceCarburant`
  - `private final String matriculeCommercial`
  - `private final String immatriculation`
  - `private final String stationPartenaire`
  - `private final int kilometrage`
  - `private final double litresDemandes`
  - `private final double montant`
  - `private final LocalDate date`
  - `private StatutCarburant statut`
  - `private String signatureCommercial`
  - `private String signatureSuperviseur`
  - `private String motifRefus`
  - `private String photoCompteur`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private DemandeCarburant(String referenceCarburant, String matriculeCommercial,
                              String immatriculation, String stationPartenaire, int kilometrage,
                              double litresDemandes, double montant,
                              LocalDate date, LocalDateTime dateCreation)`
  - `public static DemandeCarburant creer(String referenceCarburant, String matriculeCommercial,
                                          String immatriculation, String stationPartenaire, int kilometrage,
                                          double litresDemandes, double montant, LocalDate date)`
  - `public static DemandeCarburant reconstituer(Long id, String referenceCarburant, String matriculeCommercial,
                                                 String immatriculation, String stationPartenaire, int kilometrage,
                                                 double litresDemandes, double montant,
                                                 LocalDate date, StatutCarburant statut,
                                                 String signatureCommercial, String signatureSuperviseur,
                                                 String motifRefus, String photoCompteur,
                                                 LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version)`
  - `public void signer(String signatureCommercial, String signatureSuperviseur)`
  - `public void refuser(String motif)`
  - `public void setPhotoCompteur(String photoCompteur)`
  - `public Long getId()`
  - `public String getReferenceCarburant()`
  - `public String getMatriculeCommercial()`
  - `public String getImmatriculation()`
  - `public String getStationPartenaire()`
  - `public int getKilometrage()`
  - `public double getLitresDemandes()`
  - `public double getMontant()`
  - `public LocalDate getDate()`
  - `public StatutCarburant getStatut()`
  - `public String getSignatureCommercial()`
  - `public String getSignatureSuperviseur()`
  - `public String getMotifRefus()`
  - `public String getPhotoCompteur()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\carburant\enums\StatutCarburant.java`

**enum** `StatutCarburant` (public)

#### `modules\commercial\domain\carburant\repository\CarburantRepository.java`

**interface** `CarburantRepository` (public)

#### `modules\commercial\domain\carburant\specification\CarburantSpecification.java`

**class** `CarburantSpecification` (public final)

- **Méthodes :**
  - `private CarburantSpecification()`
  - `public static boolean peutDemander(Role role)`
  - `public static boolean peutSigner(Role role)`

#### `modules\commercial\domain\client\aggregate\Client.java`

**class** `Client` (public)

- **Champs :**
  - `private Long id`
  - `private final String codeClient`
  - `private String nom`
  - `private String telephone`
  - `private String adresse`
  - `private String localite`
  - `private TypeClient type`
  - `private String matriculeCommercialReferent`
  - `private boolean actif`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private Client(String codeClient, String nom, String telephone, String adresse,
                   String localite, TypeClient type, String matriculeCommercialReferent,
                   LocalDateTime dateCreation)`
  - `public static Client creer(String codeClient, String nom, String telephone, String adresse,
                               String localite, TypeClient type, String matriculeCommercialReferent)`
  - `public static Client reconstituer(Long id, String codeClient, String nom, String telephone,
                                       String adresse, String localite, TypeClient type,
                                       String matriculeCommercialReferent, boolean actif,
                                       LocalDateTime dateCreation, LocalDateTime dateModification,
                                       Long version)`
  - `public void mettreAJour(String nom, String telephone, String adresse, String localite, TypeClient type)`
  - `public void desactiver()`
  - `public Long getId()`
  - `public String getCodeClient()`
  - `public String getNom()`
  - `public String getTelephone()`
  - `public String getAdresse()`
  - `public String getLocalite()`
  - `public TypeClient getType()`
  - `public String getMatriculeCommercialReferent()`
  - `public boolean isActif()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\client\enums\TypeClient.java`

**enum** `TypeClient` (public)

#### `modules\commercial\domain\client\repository\ClientRepository.java`

**interface** `ClientRepository` (public)

#### `modules\commercial\domain\commandedistance\aggregate\CommandeDistance.java`

**class** `CommandeDistance` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroCommande`
  - `private final String codeClient`
  - `private final String matriculeCommercial`
  - `private final CanalCommande canal`
  - `private final LocalDate dateCommande`
  - `private final List<LigneCommandeDistance> lignes`
  - `private StatutCommandeDistance statut`
  - `private String matriculeValidateurSecretaire`
  - `private String matriculeValidateurComptable`
  - `private String motifAnnulation`
  - `private String observations`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private CommandeDistance(String numeroCommande, String codeClient, String matriculeCommercial,
                             CanalCommande canal, LocalDate dateCommande, LocalDateTime dateCreation)`
  - `public static CommandeDistance recevoir(String numeroCommande, String codeClient, String matriculeCommercial,
                                             CanalCommande canal, LocalDate dateCommande,
                                             List<LigneCommandeDistance> lignes, String observations)`
  - `public static CommandeDistance reconstituer(Long id, String numeroCommande, String codeClient,
                                                  String matriculeCommercial, CanalCommande canal,
                                                  LocalDate dateCommande, List<LigneCommandeDistance> lignes,
                                                  StatutCommandeDistance statut,
                                                  String matriculeValidateurSecretaire,
                                                  String matriculeValidateurComptable,
                                                  String motifAnnulation, String observations,
                                                  LocalDateTime dateCreation, LocalDateTime dateModification,
                                                  Long version)`
  - `public void validerParSecretaire(String matriculeSecretaire)`
  - `public void validerParComptable(String matriculeComptable)`
  - `public void marquerLivree()`
  - `public void annuler(String motif)`
  - `public double montantTotal()`
  - `public Long getId()`
  - `public String getNumeroCommande()`
  - `public String getCodeClient()`
  - `public String getMatriculeCommercial()`
  - `public CanalCommande getCanal()`
  - `public LocalDate getDateCommande()`
  - `public List<LigneCommandeDistance> getLignes()`
  - `public StatutCommandeDistance getStatut()`
  - `public String getMatriculeValidateurSecretaire()`
  - `public String getMatriculeValidateurComptable()`
  - `public String getMotifAnnulation()`
  - `public String getObservations()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\commandedistance\entity\LigneCommandeDistance.java`

**class** `LigneCommandeDistance` (public)

- **Champs :**
  - `private final String codeProduit`
  - `private final String designation`
  - `private final String conditionnement`
  - `private final int quantite`
  - `private final double prixUnitaire`

- **Méthodes :**
  - `public LigneCommandeDistance(String codeProduit, String designation, String conditionnement,
                                  int quantite, double prixUnitaire)`
  - `public double montantHT()`
  - `public String getCodeProduit()`
  - `public String getDesignation()`
  - `public String getConditionnement()`
  - `public int getQuantite()`
  - `public double getPrixUnitaire()`

#### `modules\commercial\domain\commandedistance\enums\CanalCommande.java`

**enum** `CanalCommande` (public)

#### `modules\commercial\domain\commandedistance\enums\StatutCommandeDistance.java`

**enum** `StatutCommandeDistance` (public)

#### `modules\commercial\domain\commandedistance\repository\CommandeDistanceRepository.java`

**interface** `CommandeDistanceRepository` (public)

#### `modules\commercial\domain\feuillroute\aggregate\FeuillRoute.java`

**class** `FeuillRoute` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroFeuillRoute`
  - `private final String matriculeCommercial`
  - `private final LocalDate date`
  - `private final List<ArretFeuillRoute> arrets`
  - `private StatutFeuillRoute statut`
  - `private String matriculeCloture`
  - `private LocalDateTime heureCloture`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private FeuillRoute(String numeroFeuillRoute, String matriculeCommercial, LocalDate date, LocalDateTime dateCreation)`
  - `public static FeuillRoute planifier(String numeroFeuillRoute, String matriculeCommercial,
                                         LocalDate date, List<ArretFeuillRoute> arrets)`
  - `public static FeuillRoute reconstituer(Long id, String numeroFeuillRoute, String matriculeCommercial,
                                            LocalDate date, List<ArretFeuillRoute> arrets, StatutFeuillRoute statut,
                                            String matriculeCloture, LocalDateTime heureCloture,
                                            LocalDateTime dateCreation, LocalDateTime dateModification, Long version)`
  - `public void demarrer()`
  - `public void viserArret(String codeClient, String numeroVente, String coordonneesGPS)`
  - `public void marquerArretNonVise(String codeClient, SaisieArret saisie, String coordonneesGPS)`
  - `public void cloturer(String matriculeCloture)`
  - `public void verrouiller()`
  - `private void verifierModifiable()`
  - `private ArretFeuillRoute trouverArret(String codeClient)`
  - `public int nombreArretVises()`
  - `public int nombreArretNonVises()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumeroFeuillRoute()`
  - `public String getMatriculeCommercial()`
  - `public LocalDate getDate()`
  - `public List<ArretFeuillRoute> getArrets()`
  - `public StatutFeuillRoute getStatut()`
  - `public String getMatriculeCloture()`
  - `public LocalDateTime getHeureCloture()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\feuillroute\entity\ArretFeuillRoute.java`

**class** `ArretFeuillRoute` (public)

- **Champs :**
  - `private Long id`
  - `private final String codeClient`
  - `private final int ordre`
  - `private StatutArret statut`
  - `private String numeroVente`
  - `private SaisieArret saisie`
  - `private final LocalDateTime heurePlanifiee`
  - `private LocalDateTime heureEffective`
  - `private String coordonneesGPS`

- **Méthodes :**
  - `public ArretFeuillRoute(String codeClient, int ordre, LocalDateTime heurePlanifiee)`
  - `public static ArretFeuillRoute reconstituer(Long id, String codeClient, int ordre, StatutArret statut,
                                                  String numeroVente, SaisieArret saisie,
                                                  LocalDateTime heurePlanifiee, LocalDateTime heureEffective,
                                                  String coordonneesGPS)`
  - `public void viser(String numeroVente, String coordonneesGPS)`
  - `public void marquerNonVise(SaisieArret saisie, String coordonneesGPS)`
  - `public void annuler(String motif)`
  - `public Long getId()`
  - `public String getCodeClient()`
  - `public int getOrdre()`
  - `public StatutArret getStatut()`
  - `public String getNumeroVente()`
  - `public SaisieArret getSaisie()`
  - `public LocalDateTime getHeurePlanifiee()`
  - `public LocalDateTime getHeureEffective()`
  - `public String getCoordonneesGPS()`

#### `modules\commercial\domain\feuillroute\enums\SaisieArret.java`

**class** `SaisieArret` (public)

- **Champs :**
  - `private final String codeClient`
  - `private final InfoClientEnum codeInfoClient`
  - `private final Integer quantiteInfo`
  - `private final String observations`

- **Méthodes :**
  - `public SaisieArret(String codeClient, InfoClientEnum codeInfoClient, Integer quantiteInfo, String observations)`
  - `public String getCodeClient()`
  - `public InfoClientEnum getCodeInfoClient()`
  - `public Integer getQuantiteInfo()`
  - `public String getObservations()`

#### `modules\commercial\domain\feuillroute\enums\StatutArret.java`

**enum** `StatutArret` (public)

#### `modules\commercial\domain\feuillroute\enums\StatutFeuillRoute.java`

**enum** `StatutFeuillRoute` (public)

#### `modules\commercial\domain\feuillroute\repository\FeuillRouteRepository.java`

**interface** `FeuillRouteRepository` (public)

#### `modules\commercial\domain\marche\model\BoutiquePotentielle.java`

**class** `BoutiquePotentielle` (public)

- **Champs :**
  - `private Long id`
  - `private String nom`
  - `private final String secteur`
  - `private String adresse`
  - `private final String matriculeRecenseur`
  - `private boolean actif`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private BoutiquePotentielle(String nom, String secteur, String adresse, String matriculeRecenseur,
                                 LocalDateTime dateCreation)`
  - `public static BoutiquePotentielle recenser(String nom, String secteur, String adresse, String matriculeRecenseur)`
  - `public static BoutiquePotentielle reconstituer(Long id, String nom, String secteur, String adresse,
                                                    String matriculeRecenseur, boolean actif,
                                                    LocalDateTime dateCreation, LocalDateTime dateModification,
                                                    Long version)`
  - `public void desactiver()`
  - `public Long getId()`
  - `public String getNom()`
  - `public String getSecteur()`
  - `public String getAdresse()`
  - `public String getMatriculeRecenseur()`
  - `public boolean isActif()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\marche\model\ZoneDeVente.java`

**class** `ZoneDeVente` (public)

- **Champs :**
  - `private Long id`
  - `private final String nom`
  - `private final String type`
  - `private String description`
  - `private int clientsPotentiels`
  - `private boolean actif`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private ZoneDeVente(String nom, String type, String description, int clientsPotentiels,
                         LocalDateTime dateCreation)`
  - `public static ZoneDeVente creer(String nom, String type, String description, int clientsPotentiels)`
  - `public static ZoneDeVente reconstituer(Long id, String nom, String type, String description,
                                            int clientsPotentiels, boolean actif,
                                            LocalDateTime dateCreation, LocalDateTime dateModification,
                                            Long version)`
  - `public void calibrerClientsPotentiels(int nouveauNombre)`
  - `public double calculerTauxOccupation(int clientsLivres3Mois)`
  - `public boolean estSousExploitee(int clientsLivres3Mois)`
  - `public void desactiver()`
  - `public Long getId()`
  - `public String getNom()`
  - `public String getType()`
  - `public String getDescription()`
  - `public int getClientsPotentiels()`
  - `public boolean isActif()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\marche\repository\BoutiquePotentielleRepository.java`

**interface** `BoutiquePotentielleRepository` (public)

#### `modules\commercial\domain\marche\repository\ZoneDeVenteRepository.java`

**interface** `ZoneDeVenteRepository` (public)

#### `modules\commercial\domain\precommande\aggregate\PreCommande.java`

**class** `PreCommande` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroPreCommande`
  - `private final String matriculeCommercial`
  - `private final LocalDate dateSoumission`
  - `private final LocalDate dateEffet`
  - `private final List<LignePreCommande> lignes`
  - `private StatutPreCommande statut`
  - `private String matriculeValidateurSecretaire`
  - `private String matriculeValidateurComptable`
  - `private String motifRefus`
  - `private boolean soumiseEnSoiree`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private PreCommande(String numeroPreCommande, String matriculeCommercial,
                        LocalDate dateSoumission, LocalDate dateEffet, LocalDateTime dateCreation)`
  - `public static PreCommande soumettre(String numeroPreCommande, String matriculeCommercial,
                                         LocalDate dateSoumission, List<LignePreCommande> lignes)`
  - `public static PreCommande reconstituer(Long id, String numeroPreCommande, String matriculeCommercial,
                                            LocalDate dateSoumission, LocalDate dateEffet,
                                            List<LignePreCommande> lignes, StatutPreCommande statut,
                                            String matriculeValidateurSecretaire,
                                            String matriculeValidateurComptable,
                                            String motifRefus, boolean soumiseEnSoiree,
                                            LocalDateTime dateCreation, LocalDateTime dateModification,
                                            Long version)`
  - `public void validerParSecretaire(String matriculeSecretaire)`
  - `public void validerParComptable(String matriculeComptable)`
  - `public void refuser(String motif, String matriculeValidateur)`
  - `public void marquerLivree()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumeroPreCommande()`
  - `public String getMatriculeCommercial()`
  - `public LocalDate getDateSoumission()`
  - `public LocalDate getDateEffet()`
  - `public List<LignePreCommande> getLignes()`
  - `public StatutPreCommande getStatut()`
  - `public String getMatriculeValidateurSecretaire()`
  - `public String getMatriculeValidateurComptable()`
  - `public String getMotifRefus()`
  - `public boolean isSoumiseEnSoiree()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\precommande\entity\LignePreCommande.java`

**class** `LignePreCommande` (public)

- **Champs :**
  - `private final String codeProduit`
  - `private final String designationProduit`
  - `private final String conditionnement`
  - `private int quantiteDemandee`
  - `private int quantiteValidee`

- **Méthodes :**
  - `public LignePreCommande(String codeProduit, String designationProduit, String conditionnement,
                             int quantiteDemandee)`
  - `public void ajusterQuantiteValidee(int quantite)`
  - `public String getCodeProduit()`
  - `public String getDesignationProduit()`
  - `public String getConditionnement()`
  - `public int getQuantiteDemandee()`
  - `public int getQuantiteValidee()`

#### `modules\commercial\domain\precommande\enums\InfoClientEnum.java`

**enum** `InfoClientEnum` (public)

- **Constantes :**
  - `{
    Ab("", false)`
  - `NC("", false)`
  - `PA("", false)`
  - `VPC("", false)`
  - `NVP("", false)`
  - `M("", false)`
  - `PR("", false)`
  - `F("", false)`
  - `EV("", true)`
  - `AV("", true)`
  - `AF("", true)`
  - `S("", false)`

- **Champs :**
  - `private final String libelle`
  - `private final boolean requireQuantity`

- **Méthodes :**
  - `public String getLibelle()`
  - `public boolean isRequireQuantity()`
  - `public static boolean requiresQuantity(InfoClientEnum code)`

#### `modules\commercial\domain\precommande\enums\StatutPreCommande.java`

**enum** `StatutPreCommande` (public)

#### `modules\commercial\domain\precommande\events\PreCommandeValideeEvent.java`

**class** `PreCommandeValideeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numeroPreCommande`
  - `private final String matriculeCommercial`
  - `private final StatutPreCommande statut`
  - `private final LocalDate dateEffet`

- **Méthodes :**
  - `public PreCommandeValideeEvent(String numeroPreCommande, String matriculeCommercial,
                                    StatutPreCommande statut, LocalDate dateEffet)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumeroPreCommande()`
  - `public String getMatriculeCommercial()`
  - `public StatutPreCommande getStatut()`
  - `public LocalDate getDateEffet()`

#### `modules\commercial\domain\precommande\repository\PreCommandeRepository.java`

**interface** `PreCommandeRepository` (public)

#### `modules\commercial\domain\precommande\specification\PreCommandeSpecification.java`

**class** `PreCommandeSpecification` (public final)

- **Méthodes :**
  - `private PreCommandeSpecification()`
  - `public static boolean peutSoumettre(Role role)`
  - `public static boolean peutValiderSecretaire(Role role)`
  - `public static boolean peutValiderComptable(Role role)`

#### `modules\commercial\domain\prospect\aggregate\Prospect.java`

**class** `Prospect` (public)

- **Champs :**
  - `private Long id`
  - `private final String codeProspect`
  - `private String nom`
  - `private String telephone`
  - `private String adresse`
  - `private String localite`
  - `private String matriculeCommercial`
  - `private CanalProspection canal`
  - `private StatutProspect statut`
  - `private String observations`
  - `private String codeClientConverti`
  - `private final LocalDate dateProspection`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private Prospect(String codeProspect, String nom, String telephone, String adresse,
                     String localite, String matriculeCommercial, CanalProspection canal,
                     LocalDate dateProspection, LocalDateTime dateCreation)`
  - `public static Prospect creer(String codeProspect, String nom, String telephone, String adresse,
                                  String localite, String matriculeCommercial, CanalProspection canal,
                                  LocalDate dateProspection, String observations)`
  - `public static Prospect reconstituer(Long id, String codeProspect, String nom, String telephone,
                                          String adresse, String localite, String matriculeCommercial,
                                          CanalProspection canal, StatutProspect statut, String observations,
                                          String codeClientConverti, LocalDate dateProspection,
                                          LocalDateTime dateCreation, LocalDateTime dateModification, Long version)`
  - `public void convertir(String codeClient)`
  - `public void marquerNonInteresse(String motif)`
  - `public void archiver()`
  - `public Long getId()`
  - `public String getCodeProspect()`
  - `public String getNom()`
  - `public String getTelephone()`
  - `public String getAdresse()`
  - `public String getLocalite()`
  - `public String getMatriculeCommercial()`
  - `public CanalProspection getCanal()`
  - `public StatutProspect getStatut()`
  - `public String getObservations()`
  - `public String getCodeClientConverti()`
  - `public LocalDate getDateProspection()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\prospect\enums\CanalProspection.java`

**enum** `CanalProspection` (public)

#### `modules\commercial\domain\prospect\enums\StatutProspect.java`

**enum** `StatutProspect` (public)

#### `modules\commercial\domain\prospect\repository\ProspectRepository.java`

**interface** `ProspectRepository` (public)

#### `modules\commercial\domain\recouvrement\aggregate\Recouvrement.java`

**class** `Recouvrement` (public)

- **Champs :**
  - `private Long id`
  - `private final String referenceRecouvrement`
  - `private final String codeClient`
  - `private final String nomClient`
  - `private final double montantDu`
  - `private double montantRembourse`
  - `private final LocalDate dateEcheance`
  - `private StatutRecouvrement statut`
  - `private final String matriculeCommercial`
  - `private String observations`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private Recouvrement(String referenceRecouvrement, String codeClient, String nomClient,
                          double montantDu, LocalDate dateEcheance, String matriculeCommercial,
                          LocalDateTime dateCreation)`
  - `public static Recouvrement creer(String referenceRecouvrement, String codeClient, String nomClient,
                                      double montantDu, LocalDate dateEcheance, String matriculeCommercial)`
  - `public static Recouvrement reconstituer(Long id, String referenceRecouvrement, String codeClient,
                                             String nomClient, double montantDu, double montantRembourse,
                                             LocalDate dateEcheance, StatutRecouvrement statut,
                                             String matriculeCommercial, String observations,
                                             LocalDateTime dateCreation, LocalDateTime dateModification,
                                             Long version)`
  - `public void enregistrerRemboursement(double montant)`
  - `public void passerEnContentieux(String motif)`
  - `public double montantRestant()`
  - `public Long getId()`
  - `public String getReferenceRecouvrement()`
  - `public String getCodeClient()`
  - `public String getNomClient()`
  - `public double getMontantDu()`
  - `public double getMontantRembourse()`
  - `public LocalDate getDateEcheance()`
  - `public StatutRecouvrement getStatut()`
  - `public String getMatriculeCommercial()`
  - `public String getObservations()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\recouvrement\enums\StatutRecouvrement.java`

**enum** `StatutRecouvrement` (public)

#### `modules\commercial\domain\recouvrement\repository\RecouvrementRepository.java`

**interface** `RecouvrementRepository` (public)

#### `modules\commercial\domain\recouvrement\specification\RecouvrementSpecification.java`

**class** `RecouvrementSpecification` (public final)

- **Méthodes :**
  - `private RecouvrementSpecification()`
  - `public static boolean peutCreer(Role role)`
  - `public static boolean peutEnregistrerRemboursement(Role role)`
  - `public static boolean peutPasserEnContentieux(Role role)`

#### `modules\commercial\domain\tarification\event\PrixExceptionnelAccordeEvent.java`

**class** `PrixExceptionnelAccordeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String reference`
  - `private final String codeProduit`
  - `private final String codeClient`
  - `private final double prixHomologue`
  - `private final double prixApplique`
  - `private final String matriculeAutorisateur`

- **Méthodes :**
  - `public PrixExceptionnelAccordeEvent(String reference, String codeProduit, String codeClient,
                                         double prixHomologue, double prixApplique, String matriculeAutorisateur)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReference()`
  - `public String getCodeProduit()`
  - `public String getCodeClient()`
  - `public double getPrixHomologue()`
  - `public double getPrixApplique()`
  - `public String getMatriculeAutorisateur()`

#### `modules\commercial\domain\tarification\model\GrilleTarifaire.java`

**class** `GrilleTarifaire` (public)

- **Champs :**
  - `private Long id`
  - `private final String codeProduit`
  - `private final TypeClient segment`
  - `private double prixHomologueFCFA`
  - `private boolean actif`
  - `private String matriculeAuteur`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private GrilleTarifaire(String codeProduit, TypeClient segment, double prixHomologueFCFA,
                             String matriculeAuteur, LocalDateTime dateCreation)`
  - `public static GrilleTarifaire definir(String codeProduit, TypeClient segment, double prixHomologueFCFA,
                                           String matriculeAuteur)`
  - `public static GrilleTarifaire reconstituer(Long id, String codeProduit, TypeClient segment,
                                                double prixHomologueFCFA, boolean actif, String matriculeAuteur,
                                                LocalDateTime dateCreation, LocalDateTime dateModification,
                                                Long version)`
  - `public void reviser(double nouveauPrixFCFA, String matriculeAuteur)`
  - `public void desactiver()`
  - `public void reactiver()`
  - `public Long getId()`
  - `public String getCodeProduit()`
  - `public TypeClient getSegment()`
  - `public double getPrixHomologueFCFA()`
  - `public boolean isActif()`
  - `public String getMatriculeAuteur()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\tarification\model\PrixExceptionnel.java`

**class** `PrixExceptionnel` (public)

- **Champs :**
  - `private Long id`
  - `private final String reference`
  - `private final String codeProduit`
  - `private final String codeClient`
  - `private final double prixHomologueFCFA`
  - `private final double prixAppliqueFCFA`
  - `private final String motif`
  - `private final String matriculeAutorisateur`
  - `private final String referenceVenteCible`
  - `private final LocalDateTime dateAutorisation`
  - `private final LocalDateTime dateCreation`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private PrixExceptionnel(String reference, String codeProduit, String codeClient, double prixHomologueFCFA,
                              double prixAppliqueFCFA, String motif, String matriculeAutorisateur,
                              String referenceVenteCible, LocalDateTime dateAutorisation, LocalDateTime dateCreation)`
  - `public static PrixExceptionnel accorder(String reference, String codeProduit, String codeClient,
                                             double prixHomologueFCFA, double prixAppliqueFCFA, String motif,
                                             String matriculeAutorisateur, String referenceVenteCible)`
  - `public static PrixExceptionnel reconstituer(Long id, String reference, String codeProduit, String codeClient,
                                                 double prixHomologueFCFA, double prixAppliqueFCFA, String motif,
                                                 String matriculeAutorisateur, String referenceVenteCible,
                                                 LocalDateTime dateAutorisation, LocalDateTime dateCreation,
                                                 Long version)`
  - `public double getEcartFCFA()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getReference()`
  - `public String getCodeProduit()`
  - `public String getCodeClient()`
  - `public double getPrixHomologueFCFA()`
  - `public double getPrixAppliqueFCFA()`
  - `public String getMotif()`
  - `public String getMatriculeAutorisateur()`
  - `public String getReferenceVenteCible()`
  - `public LocalDateTime getDateAutorisation()`
  - `public LocalDateTime getDateCreation()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\tarification\repository\GrilleTarifaireRepository.java`

**interface** `GrilleTarifaireRepository` (public)

#### `modules\commercial\domain\tarification\repository\PrixExceptionnelRepository.java`

**interface** `PrixExceptionnelRepository` (public)

#### `modules\commercial\domain\terminal\aggregate\Terminal.java`

**class** `Terminal` (public)

- **Champs :**
  - `private Long id`
  - `private final String imei`
  - `private final String modele`
  - `private String matriculeCommercial`
  - `private final double prixAchat`
  - `private double resteAPayer`
  - `private boolean actif`
  - `private LocalDateTime dateDerniereConnexion`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private Terminal(String imei, String modele, String matriculeCommercial,
                     double prixAchat, double resteAPayer, LocalDateTime dateCreation)`
  - `public static Terminal attribuer(String imei, String modele, String matriculeCommercial,
                                      double prixAchat)`
  - `public static Terminal reconstituer(Long id, String imei, String modele, String matriculeCommercial,
                                         double prixAchat, double resteAPayer, boolean actif,
                                         LocalDateTime dateDerniereConnexion,
                                         LocalDateTime dateCreation, LocalDateTime dateModification,
                                         Long version)`
  - `public void deduirePrime(double montant)`
  - `public void desactiver()`
  - `public void reactiver()`
  - `public void reassigner(String nouveauMatricule)`
  - `public void enregistrerConnexion()`
  - `public boolean estRembourse()`
  - `public Long getId()`
  - `public String getImei()`
  - `public String getModele()`
  - `public String getMatriculeCommercial()`
  - `public double getPrixAchat()`
  - `public double getResteAPayer()`
  - `public boolean isActif()`
  - `public LocalDateTime getDateDerniereConnexion()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\terminal\repository\TerminalRepository.java`

**interface** `TerminalRepository` (public)

#### `modules\commercial\domain\vehicule\aggregate\Vehicule.java`

**class** `Vehicule` (public)

- **Champs :**
  - `private Long id`
  - `private final String immatriculation`
  - `private String marque`
  - `private String modele`
  - `private String matriculeCommercial`
  - `private boolean actif`
  - `private double consommationMoyenneL100`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private Vehicule(String immatriculation, String marque, String modele,
                      String matriculeCommercial, double consommationMoyenneL100,
                      LocalDateTime dateCreation)`
  - `public static Vehicule enregistrer(String immatriculation, String marque, String modele,
                                        String matriculeCommercial, double consommationMoyenneL100)`
  - `public static Vehicule reconstituer(Long id, String immatriculation, String marque, String modele,
                                         String matriculeCommercial, boolean actif,
                                         double consommationMoyenneL100,
                                         LocalDateTime dateCreation, LocalDateTime dateModification,
                                         Long version)`
  - `public void reassigner(String nouveauMatricule)`
  - `public void desactiver()`
  - `public void mettreAJourConsommation(double nouvelleConso)`
  - `public Long getId()`
  - `public String getImmatriculation()`
  - `public String getMarque()`
  - `public String getModele()`
  - `public String getMatriculeCommercial()`
  - `public boolean isActif()`
  - `public double getConsommationMoyenneL100()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\vehicule\repository\VehiculeRepository.java`

**interface** `VehiculeRepository` (public)

#### `modules\commercial\domain\vente\aggregate\FactureAvoir.java`

**class** `FactureAvoir` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroAvoir`
  - `private final String numeroVenteOriginale`
  - `private final String matriculeCommercial`
  - `private final String codeClient`
  - `private final String matriculeComptable`
  - `private final double montantHT`
  - `private final double montantTVA`
  - `private final double montantTTC`
  - `private final String motif`
  - `private final LocalDate dateAvoir`
  - `private StatutAvoir statut`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateValidation`
  - `private Long version`

- **Méthodes :**
  - `private FactureAvoir(String numeroAvoir, String numeroVenteOriginale, String matriculeCommercial,
                          String codeClient, String matriculeComptable, double montantHT, double montantTVA,
                          String motif, LocalDate dateAvoir, LocalDateTime dateCreation)`
  - `public static FactureAvoir emettre(String numeroAvoir, String numeroVenteOriginale,
                                        String matriculeCommercial, String codeClient,
                                        String matriculeComptable, double montantHT, double montantTVA,
                                        String motif, LocalDate dateAvoir)`
  - `public static FactureAvoir reconstituer(Long id, String numeroAvoir, String numeroVenteOriginale,
                                             String matriculeCommercial, String codeClient,
                                             String matriculeComptable, double montantHT, double montantTVA,
                                             String motif, LocalDate dateAvoir, StatutAvoir statut,
                                             LocalDateTime dateCreation, LocalDateTime dateValidation,
                                             Long version)`
  - `public void valider()`
  - `public Long getId()`
  - `public String getNumeroAvoir()`
  - `public String getNumeroVenteOriginale()`
  - `public String getMatriculeCommercial()`
  - `public String getCodeClient()`
  - `public String getMatriculeComptable()`
  - `public double getMontantHT()`
  - `public double getMontantTVA()`
  - `public double getMontantTTC()`
  - `public String getMotif()`
  - `public LocalDate getDateAvoir()`
  - `public StatutAvoir getStatut()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateValidation()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

**enum** `StatutAvoir` (public)

#### `modules\commercial\domain\vente\aggregate\Vente.java`

**class** `Vente` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroVente`
  - `private final String uuidVente`
  - `private final String matriculeCommercial`
  - `private final String codeClient`
  - `private final LocalDate date`
  - `private final List<LigneVente> lignes`
  - `private ModeReglement modeReglement`
  - `private TypeVente typeVente`
  - `private StatutVente statut`
  - `private SyncStatus syncStatus`
  - `private String coordonneesGPS`
  - `private String idFeuillRoute`
  - `private String observations`
  - `private boolean signatureClient`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private Vente(String numeroVente, String uuidVente, String matriculeCommercial, String codeClient,
                  LocalDate date, ModeReglement modeReglement, TypeVente typeVente,
                  LocalDateTime dateCreation)`
  - `public static Vente creer(String numeroVente, String matriculeCommercial, String codeClient,
                               LocalDate date, ModeReglement modeReglement, TypeVente typeVente,
                               List<LigneVente> lignes)`
  - `public static Vente creer(String numeroVente, String uuidVente, String matriculeCommercial,
                               String codeClient, LocalDate date, ModeReglement modeReglement,
                               TypeVente typeVente, List<LigneVente> lignes)`
  - `public static Vente reconstituer(Long id, String numeroVente, String uuidVente, String matriculeCommercial,
                                      String codeClient, LocalDate date, List<LigneVente> lignes,
                                      ModeReglement modeReglement, TypeVente typeVente, StatutVente statut,
                                      SyncStatus syncStatus, String coordonneesGPS, String idFeuillRoute,
                                      String observations, boolean signatureClient,
                                      LocalDateTime dateCreation, LocalDateTime dateModification,
                                      Long version)`
  - `public void finaliser()`
  - `public void signerClient()`
  - `public void annuler(String motif)`
  - `public void marquerSyncEnAttente()`
  - `public void marquerSyncConflit()`
  - `public void marquerSyncFait()`
  - `public double montantHT()`
  - `public double montantTotal()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumeroVente()`
  - `public String getUuidVente()`
  - `public String getMatriculeCommercial()`
  - `public String getCodeClient()`
  - `public LocalDate getDate()`
  - `public List<LigneVente> getLignes()`
  - `public ModeReglement getModeReglement()`
  - `public TypeVente getTypeVente()`
  - `public StatutVente getStatut()`
  - `public SyncStatus getSyncStatus()`
  - `public String getCoordonneesGPS()`
  - `public String getIdFeuillRoute()`
  - `public String getObservations()`
  - `public boolean isSignatureClient()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercial\domain\vente\entity\LigneVente.java`

**class** `LigneVente` (public)

- **Champs :**
  - `private final String codeProduit`
  - `private final String designationProduit`
  - `private final String conditionnement`
  - `private final int quantite`
  - `private final double prixUnitaire`
  - `private double remise`

- **Méthodes :**
  - `public LigneVente(String codeProduit, String designationProduit, String conditionnement,
                      int quantite, double prixUnitaire, double remise)`
  - `public double montantHT()`
  - `public String getCodeProduit()`
  - `public String getDesignationProduit()`
  - `public String getConditionnement()`
  - `public int getQuantite()`
  - `public double getPrixUnitaire()`
  - `public double getRemise()`

#### `modules\commercial\domain\vente\enums\ModeReglement.java`

**enum** `ModeReglement` (public)

#### `modules\commercial\domain\vente\enums\StatutVente.java`

**enum** `StatutVente` (public)

#### `modules\commercial\domain\vente\enums\SyncStatus.java`

**enum** `SyncStatus` (public)

#### `modules\commercial\domain\vente\enums\TypeVente.java`

**enum** `TypeVente` (public)

#### `modules\commercial\domain\vente\events\VenteFinaliseEvent.java`

**class** `VenteFinaliseEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numeroVente`
  - `private final String matriculeCommercial`
  - `private final String codeClient`
  - `private final double montantTotal`
  - `private final LocalDate date`

- **Méthodes :**
  - `public VenteFinaliseEvent(String numeroVente, String matriculeCommercial, String codeClient,
                               double montantTotal, LocalDate date)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumeroVente()`
  - `public String getMatriculeCommercial()`
  - `public String getCodeClient()`
  - `public double getMontantTotal()`
  - `public LocalDate getDate()`

#### `modules\commercial\domain\vente\repository\FactureAvoirRepository.java`

**interface** `FactureAvoirRepository` (public)

#### `modules\commercial\domain\vente\repository\VenteRepository.java`

**interface** `VenteRepository` (public)

#### `modules\commercial\domain\vente\specification\VenteSpecification.java`

**class** `VenteSpecification` (public final)

- **Méthodes :**
  - `private VenteSpecification()`
  - `public static boolean peutVendre(Role role)`
  - `public static boolean peutConsulter(Role role)`

#### `modules\commercial\infrastructure\persistence\carburant\jpa\CarburantJpaEntity.java`

**class** `CarburantJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String referenceCarburant`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private String immatriculation`
  - `@Column(length = 100) private String stationPartenaire`
  - `@Column(nullable = false) private int kilometrage`
  - `@Column(nullable = false) private double litresDemandes`
  - `@Column(nullable = false) private double montant`
  - `@Column(nullable = false) private LocalDate date`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutCarburant statut`
  - `@Column(columnDefinition = "") private String signatureCommercial`
  - `@Column(columnDefinition = "") private String signatureSuperviseur`
  - `private String motifRefus`
  - `@Column(length = 500) private String photoCompteur`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\carburant\jpa\CarburantJpaRepository.java`

**interface** `CarburantJpaRepository` (public) extends `JpaRepository<CarburantJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\carburant\repository\CarburantRepositoryImpl.java`

**class** `CarburantRepositoryImpl` (@Repository
public) implements `CarburantRepository`

- **Champs :**
  - `private final CarburantJpaRepository jpaRepository`

- **Méthodes :**
  - `public CarburantRepositoryImpl(CarburantJpaRepository jpaRepository)`
  - `@Override public DemandeCarburant sauvegarder(DemandeCarburant demande)`
  - `@Override public Optional<DemandeCarburant> trouverParReference(String reference)`
  - `@Override public List<DemandeCarburant> listerParStatut(StatutCarburant statut)`
  - `@Override public List<DemandeCarburant> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<DemandeCarburant> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `public String genererReference(LocalDate date)`
  - `private CarburantJpaEntity toEntity(DemandeCarburant d)`
  - `private DemandeCarburant toDomain(CarburantJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\client\jpa\ClientJpaEntity.java`

**class** `ClientJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String codeClient`
  - `@Column(nullable = false) private String nom`
  - `private String telephone`
  - `private String adresse`
  - `private String localite`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private TypeClient type`
  - `private String matriculeCommercialReferent`
  - `@Column(nullable = false) private boolean actif`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\client\jpa\ClientJpaRepository.java`

**interface** `ClientJpaRepository` (public) extends `JpaRepository<ClientJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\client\repository\ClientRepositoryImpl.java`

**class** `ClientRepositoryImpl` (@Repository
public) implements `ClientRepository`

- **Champs :**
  - `private final ClientJpaRepository jpaRepository`

- **Méthodes :**
  - `public ClientRepositoryImpl(ClientJpaRepository jpaRepository)`
  - `@Override public Client sauvegarder(Client client)`
  - `@Override public Optional<Client> trouverParCode(String codeClient)`
  - `@Override public List<Client> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<Client> listerParType(TypeClient type)`
  - `@Override public List<Client> rechercherParNom(String nomPartiel)`
  - `@Override public List<Client> listerParLocalite(String localite)`
  - `@Override public List<Client> listerTous()`
  - `private ClientJpaEntity toEntity(Client c)`
  - `private Client toDomain(ClientJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\commandedistance\jpa\CommandeDistanceJpaEntity.java`

**class** `CommandeDistanceJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numeroCommande`
  - `@Column(nullable = false) private String codeClient`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private CanalCommande canal`
  - `@Column(nullable = false) private LocalDate dateCommande`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "") private List<LigneCommandeDistanceJpaEntity> lignes`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutCommandeDistance statut`
  - `private String matriculeValidateurSecretaire`
  - `private String matriculeValidateurComptable`
  - `@Column(columnDefinition = "") private String motifAnnulation`
  - `@Column(columnDefinition = "") private String observations`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\commandedistance\jpa\CommandeDistanceJpaRepository.java`

**interface** `CommandeDistanceJpaRepository` (public) extends `JpaRepository<CommandeDistanceJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\commandedistance\jpa\LigneCommandeDistanceJpaEntity.java`

**class** `LigneCommandeDistanceJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String codeProduit`
  - `@Column(nullable = false) private String designation`
  - `@Column(nullable = false) private String conditionnement`
  - `@Column(nullable = false) private int quantite`
  - `@Column(nullable = false) private double prixUnitaire`

#### `modules\commercial\infrastructure\persistence\commandedistance\repository\CommandeDistanceRepositoryImpl.java`

**class** `CommandeDistanceRepositoryImpl` (@Repository
public) implements `CommandeDistanceRepository`

- **Champs :**
  - `private final CommandeDistanceJpaRepository jpaRepository`

- **Méthodes :**
  - `public CommandeDistanceRepositoryImpl(CommandeDistanceJpaRepository jpaRepository)`
  - `@Override public CommandeDistance sauvegarder(CommandeDistance commande)`
  - `@Override public Optional<CommandeDistance> trouverParNumero(String numero)`
  - `@Override public List<CommandeDistance> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<CommandeDistance> listerParStatut(StatutCommandeDistance statut)`
  - `@Override public List<CommandeDistance> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `private CommandeDistanceJpaEntity toEntity(CommandeDistance cd)`
  - `private CommandeDistance toDomain(CommandeDistanceJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\feuillroute\jpa\ArretFeuillRouteJpaEntity.java`

**class** `ArretFeuillRouteJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String codeClient`
  - `@Column(nullable = false) private int ordre`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutArret statut`
  - `private String numeroVente`
  - `@Enumerated(EnumType.STRING) private InfoClientEnum codeInfoClient`
  - `private Integer quantiteInfo`
  - `@Column(columnDefinition = "") private String observationsArret`
  - `@Column(nullable = false, updatable = false) private LocalDateTime heurePlanifiee`
  - `private LocalDateTime heureEffective`
  - `@Column(length = 100) private String coordonneesGPS`

#### `modules\commercial\infrastructure\persistence\feuillroute\jpa\FeuillRouteJpaEntity.java`

**class** `FeuillRouteJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numeroFeuillRoute`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private LocalDate date`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "")
    @OrderColumn(name = "") private List<ArretFeuillRouteJpaEntity> arrets`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutFeuillRoute statut`
  - `private String matriculeCloture`
  - `private LocalDateTime heureCloture`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\feuillroute\jpa\FeuillRouteJpaRepository.java`

**interface** `FeuillRouteJpaRepository` (public) extends `JpaRepository<FeuillRouteJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\feuillroute\repository\FeuillRouteRepositoryImpl.java`

**class** `FeuillRouteRepositoryImpl` (@Repository
public) implements `FeuillRouteRepository`

- **Champs :**
  - `private final FeuillRouteJpaRepository jpaRepository`

- **Méthodes :**
  - `public FeuillRouteRepositoryImpl(FeuillRouteJpaRepository jpaRepository)`
  - `@Override public FeuillRoute sauvegarder(FeuillRoute feuillRoute)`
  - `@Override public Optional<FeuillRoute> trouverParNumero(String numeroFeuillRoute)`
  - `@Override public List<FeuillRoute> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<FeuillRoute> listerParCommercialEtDate(String matriculeCommercial, LocalDate date)`
  - `@Override public List<FeuillRoute> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `private FeuillRouteJpaEntity toEntity(FeuillRoute fr)`
  - `private FeuillRoute toDomain(FeuillRouteJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\marche\jpa\BoutiquePotentielleJpaEntity.java`

**class** `BoutiquePotentielleJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, length = 150) private String nom`
  - `@Column(nullable = false, length = 100) private String secteur`
  - `@Column(length = 250) private String adresse`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeRecenseur`
  - `@Column(nullable = false) private boolean actif`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\marche\jpa\BoutiquePotentielleJpaRepository.java`

**interface** `BoutiquePotentielleJpaRepository` (public) extends `JpaRepository<BoutiquePotentielleJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\marche\jpa\ZoneDeVenteJpaEntity.java`

**class** `ZoneDeVenteJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String nom`
  - `@Column(nullable = false) private String type`
  - `@Column(columnDefinition = "") private String description`
  - `@Column(nullable = false) private int clientsPotentiels`
  - `@Column(nullable = false) private boolean actif`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\marche\jpa\ZoneDeVenteJpaRepository.java`

**interface** `ZoneDeVenteJpaRepository` (public) extends `JpaRepository<ZoneDeVenteJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\marche\repository\BoutiquePotentielleRepositoryImpl.java`

**class** `BoutiquePotentielleRepositoryImpl` (@Repository
public) implements `BoutiquePotentielleRepository`

- **Champs :**
  - `private final BoutiquePotentielleJpaRepository jpaRepository`

- **Méthodes :**
  - `public BoutiquePotentielleRepositoryImpl(BoutiquePotentielleJpaRepository jpaRepository)`
  - `@Override public BoutiquePotentielle sauvegarder(BoutiquePotentielle boutique)`
  - `@Override public List<BoutiquePotentielle> listerParSecteur(String secteur)`
  - `@Override public List<BoutiquePotentielle> listerTous()`
  - `@Override public List<String> listerSecteurs()`
  - `private BoutiquePotentielleJpaEntity toEntity(BoutiquePotentielle b)`
  - `private BoutiquePotentielle toDomain(BoutiquePotentielleJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\marche\repository\ZoneDeVenteRepositoryImpl.java`

**class** `ZoneDeVenteRepositoryImpl` (@Repository
public) implements `ZoneDeVenteRepository`

- **Champs :**
  - `private final ZoneDeVenteJpaRepository jpaRepository`

- **Méthodes :**
  - `public ZoneDeVenteRepositoryImpl(ZoneDeVenteJpaRepository jpaRepository)`
  - `@Override public ZoneDeVente sauvegarder(ZoneDeVente zone)`
  - `@Override public Optional<ZoneDeVente> trouverParNom(String nom)`
  - `@Override public Optional<ZoneDeVente> trouverParId(Long id)`
  - `@Override public List<ZoneDeVente> listerToutes()`
  - `@Override public List<ZoneDeVente> listerActives()`
  - `private ZoneDeVenteJpaEntity toEntity(ZoneDeVente z)`
  - `private ZoneDeVente toDomain(ZoneDeVenteJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\precommande\jpa\LignePreCommandeJpaEntity.java`

**class** `LignePreCommandeJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String codeProduit`
  - `@Column(nullable = false) private String designationProduit`
  - `@Column(nullable = false) private String conditionnement`
  - `@Column(nullable = false) private int quantiteDemandee`
  - `@Column(nullable = false) private int quantiteValidee`

#### `modules\commercial\infrastructure\persistence\precommande\jpa\PreCommandeJpaEntity.java`

**class** `PreCommandeJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numeroPreCommande`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private LocalDate dateSoumission`
  - `@Column(nullable = false) private LocalDate dateEffet`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "") private List<LignePreCommandeJpaEntity> lignes`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutPreCommande statut`
  - `private String matriculeValidateurSecretaire`
  - `private String matriculeValidateurComptable`
  - `private String motifRefus`
  - `@Column(nullable = false) private boolean soumiseEnSoiree`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\precommande\jpa\PreCommandeJpaRepository.java`

**interface** `PreCommandeJpaRepository` (public) extends `JpaRepository<PreCommandeJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\precommande\repository\PreCommandeRepositoryImpl.java`

**class** `PreCommandeRepositoryImpl` (@Repository
public) implements `PreCommandeRepository`

- **Champs :**
  - `private final PreCommandeJpaRepository jpaRepository`

- **Méthodes :**
  - `public PreCommandeRepositoryImpl(PreCommandeJpaRepository jpaRepository)`
  - `@Override public PreCommande sauvegarder(PreCommande pc)`
  - `@Override public Optional<PreCommande> trouverParNumero(String numero)`
  - `@Override public List<PreCommande> listerParCommercial(String matricule)`
  - `@Override public List<PreCommande> listerParDateEffet(LocalDate dateEffet)`
  - `@Override public List<PreCommande> listerParStatut(StatutPreCommande statut)`
  - `@Override public List<PreCommande> listerTous()`
  - `@Override public Page<PreCommande> listerParStatutPagine(StatutPreCommande statut, Pageable pageable)`
  - `@Override public Page<PreCommande> listerParDateEffetPagine(LocalDate dateEffet, Pageable pageable)`
  - `public String genererNumero(String matriculeCommercial, LocalDate date)`
  - `private PreCommandeJpaEntity toEntity(PreCommande pc)`
  - `private PreCommande toDomain(PreCommandeJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\prospect\jpa\ProspectJpaEntity.java`

**class** `ProspectJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String codeProspect`
  - `@Column(nullable = false) private String nom`
  - `private String telephone`
  - `private String adresse`
  - `private String localite`
  - `private String matriculeCommercial`
  - `@Enumerated(EnumType.STRING) private CanalProspection canal`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutProspect statut`
  - `@Column(columnDefinition = "") private String observations`
  - `private String codeClientConverti`
  - `@Column(nullable = false) private LocalDate dateProspection`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\prospect\jpa\ProspectJpaRepository.java`

**interface** `ProspectJpaRepository` (public) extends `JpaRepository<ProspectJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\prospect\repository\ProspectRepositoryImpl.java`

**class** `ProspectRepositoryImpl` (@Repository
public) implements `ProspectRepository`

- **Champs :**
  - `private final ProspectJpaRepository jpaRepository`

- **Méthodes :**
  - `public ProspectRepositoryImpl(ProspectJpaRepository jpaRepository)`
  - `@Override public Prospect sauvegarder(Prospect prospect)`
  - `@Override public Optional<Prospect> trouverParCode(String codeProspect)`
  - `@Override public List<Prospect> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<Prospect> listerParStatut(StatutProspect statut)`
  - `@Override public List<Prospect> listerTous()`
  - `private ProspectJpaEntity toEntity(Prospect p)`
  - `private Prospect toDomain(ProspectJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\recouvrement\jpa\RecouvrementJpaEntity.java`

**class** `RecouvrementJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String referenceRecouvrement`
  - `@Column(nullable = false) private String codeClient`
  - `@Column(nullable = false) private String nomClient`
  - `@Column(nullable = false) private double montantDu`
  - `@Column(nullable = false) private double montantRembourse`
  - `@Column(nullable = false) private LocalDate dateEcheance`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutRecouvrement statut`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(columnDefinition = "") private String observations`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\recouvrement\jpa\RecouvrementJpaRepository.java`

**interface** `RecouvrementJpaRepository` (public) extends `JpaRepository<RecouvrementJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\recouvrement\repository\RecouvrementRepositoryImpl.java`

**class** `RecouvrementRepositoryImpl` (@Repository
public) implements `RecouvrementRepository`

- **Champs :**
  - `private final RecouvrementJpaRepository jpaRepository`

- **Méthodes :**
  - `public RecouvrementRepositoryImpl(RecouvrementJpaRepository jpaRepository)`
  - `@Override public Recouvrement sauvegarder(Recouvrement recouvrement)`
  - `@Override public Optional<Recouvrement> trouverParReference(String reference)`
  - `@Override public List<Recouvrement> listerParClient(String codeClient)`
  - `@Override public List<Recouvrement> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<Recouvrement> listerParStatut(StatutRecouvrement statut)`
  - `@Override public List<Recouvrement> listerEnContentieux()`
  - `private RecouvrementJpaEntity toEntity(Recouvrement r)`
  - `private Recouvrement toDomain(RecouvrementJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\tarification\jpa\GrilleTarifaireJpaEntity.java`

**class** `GrilleTarifaireJpaEntity` (@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private TypeClient segment`
  - `@Column(name = "", nullable = false) private double prixHomologueFCFA`
  - `@Column(nullable = false) private boolean actif`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeAuteur`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\tarification\jpa\GrilleTarifaireJpaRepository.java`

**interface** `GrilleTarifaireJpaRepository` (public) extends `JpaRepository<GrilleTarifaireJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\tarification\jpa\PrixExceptionnelJpaEntity.java`

**class** `PrixExceptionnelJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true, length = 40) private String reference`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", nullable = false, length = 50) private String codeClient`
  - `@Column(name = "", nullable = false) private double prixHomologueFCFA`
  - `@Column(name = "", nullable = false) private double prixAppliqueFCFA`
  - `@Column(nullable = false, length = 300) private String motif`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeAutorisateur`
  - `@Column(name = "", length = 40) private String referenceVenteCible`
  - `@Column(name = "", nullable = false) private LocalDateTime dateAutorisation`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\tarification\jpa\PrixExceptionnelJpaRepository.java`

**interface** `PrixExceptionnelJpaRepository` (public) extends `JpaRepository<PrixExceptionnelJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\tarification\repository\GrilleTarifaireRepositoryImpl.java`

**class** `GrilleTarifaireRepositoryImpl` (@Repository
public) implements `GrilleTarifaireRepository`

- **Champs :**
  - `private final GrilleTarifaireJpaRepository jpaRepository`

- **Méthodes :**
  - `public GrilleTarifaireRepositoryImpl(GrilleTarifaireJpaRepository jpaRepository)`
  - `@Override public GrilleTarifaire sauvegarder(GrilleTarifaire grille)`
  - `@Override public Optional<GrilleTarifaire> trouver(String codeProduit, TypeClient segment)`
  - `@Override public List<GrilleTarifaire> listerParProduit(String codeProduit)`
  - `@Override public List<GrilleTarifaire> listerParSegment(TypeClient segment)`
  - `@Override public List<GrilleTarifaire> listerTous()`
  - `private GrilleTarifaireJpaEntity toEntity(GrilleTarifaire g)`
  - `private GrilleTarifaire toDomain(GrilleTarifaireJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\tarification\repository\PrixExceptionnelRepositoryImpl.java`

**class** `PrixExceptionnelRepositoryImpl` (@Repository
public) implements `PrixExceptionnelRepository`

- **Champs :**
  - `private final PrixExceptionnelJpaRepository jpaRepository`

- **Méthodes :**
  - `public PrixExceptionnelRepositoryImpl(PrixExceptionnelJpaRepository jpaRepository)`
  - `@Override public PrixExceptionnel sauvegarder(PrixExceptionnel prixExceptionnel)`
  - `@Override public Optional<PrixExceptionnel> trouverParReference(String reference)`
  - `@Override public List<PrixExceptionnel> listerParClient(String codeClient)`
  - `@Override public List<PrixExceptionnel> listerParAutorisateur(String matriculeAutorisateur)`
  - `@Override public List<PrixExceptionnel> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public String genererReference()`
  - `private PrixExceptionnelJpaEntity toEntity(PrixExceptionnel p)`
  - `private PrixExceptionnel toDomain(PrixExceptionnelJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\terminal\jpa\TerminalJpaEntity.java`

**class** `TerminalJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true, length = 15) private String imei`
  - `@Column(nullable = false) private String modele`
  - `@Column private String matriculeCommercial`
  - `@Column(nullable = false) private double prixAchat`
  - `@Column(nullable = false) private double resteAPayer`
  - `@Column(nullable = false) private boolean actif`
  - `private LocalDateTime dateDerniereConnexion`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\terminal\jpa\TerminalJpaRepository.java`

**interface** `TerminalJpaRepository` (public) extends `JpaRepository<TerminalJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\terminal\repository\TerminalRepositoryImpl.java`

**class** `TerminalRepositoryImpl` (@Repository
public) implements `TerminalRepository`

- **Champs :**
  - `private final TerminalJpaRepository jpaRepository`

- **Méthodes :**
  - `public TerminalRepositoryImpl(TerminalJpaRepository jpaRepository)`
  - `@Override public Terminal sauvegarder(Terminal terminal)`
  - `@Override public Optional<Terminal> trouverParImei(String imei)`
  - `@Override public Optional<Terminal> trouverParMatricule(String matriculeCommercial)`
  - `@Override public List<Terminal> listerTous()`
  - `@Override public List<Terminal> listerActifs()`
  - `private TerminalJpaEntity toEntity(Terminal t)`
  - `private Terminal toDomain(TerminalJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\vehicule\jpa\VehiculeJpaEntity.java`

**class** `VehiculeJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String immatriculation`
  - `private String marque`
  - `private String modele`
  - `private String matriculeCommercial`
  - `@Column(nullable = false) private boolean actif`
  - `@Column(nullable = false) private double consommationMoyenneL100`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\vehicule\jpa\VehiculeJpaRepository.java`

**interface** `VehiculeJpaRepository` (public) extends `JpaRepository<VehiculeJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\vehicule\repository\VehiculeRepositoryImpl.java`

**class** `VehiculeRepositoryImpl` (@Repository
public) implements `VehiculeRepository`

- **Champs :**
  - `private final VehiculeJpaRepository jpaRepository`

- **Méthodes :**
  - `public VehiculeRepositoryImpl(VehiculeJpaRepository jpaRepository)`
  - `@Override public Vehicule sauvegarder(Vehicule vehicule)`
  - `@Override public Optional<Vehicule> trouverParImmatriculation(String immatriculation)`
  - `@Override public List<Vehicule> listerTous()`
  - `@Override public List<Vehicule> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<Vehicule> listerActifs()`
  - `private VehiculeJpaEntity toEntity(Vehicule v)`
  - `private Vehicule toDomain(VehiculeJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\vente\jpa\FactureAvoirJpaEntity.java`

**class** `FactureAvoirJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numeroAvoir`
  - `@Column(nullable = false) private String numeroVenteOriginale`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private String codeClient`
  - `@Column(nullable = false) private String matriculeComptable`
  - `@Column(nullable = false) private double montantHT`
  - `@Column(nullable = false) private double montantTVA`
  - `@Column(nullable = false) private double montantTTC`
  - `@Column(nullable = false) private String motif`
  - `@Column(nullable = false) private LocalDate dateAvoir`
  - `StatutAvoir statut`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateValidation`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\vente\jpa\FactureAvoirJpaRepository.java`

**interface** `FactureAvoirJpaRepository` (public) extends `JpaRepository<FactureAvoirJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\vente\jpa\LigneVenteJpaEntity.java`

**class** `LigneVenteJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String codeProduit`
  - `@Column(nullable = false) private String designationProduit`
  - `@Column(nullable = false) private String conditionnement`
  - `@Column(nullable = false) private int quantite`
  - `@Column(nullable = false) private double prixUnitaire`
  - `@Column(nullable = false) private double remise`

#### `modules\commercial\infrastructure\persistence\vente\jpa\VenteJpaEntity.java`

**class** `VenteJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numeroVente`
  - `@Column(unique = true) private String uuidVente`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private String codeClient`
  - `@Column(nullable = false) private LocalDate date`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "") private List<LigneVenteJpaEntity> lignes`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private ModeReglement modeReglement`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutVente statut`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private TypeVente typeVente`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private SyncStatus syncStatus`
  - `@Column(length = 100) private String coordonneesGPS`
  - `@Column(length = 50) private String idFeuillRoute`
  - `@Column(columnDefinition = "") private String observations`
  - `@Column(nullable = false) private boolean signatureClient`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\commercial\infrastructure\persistence\vente\jpa\VenteJpaRepository.java`

**interface** `VenteJpaRepository` (public) extends `JpaRepository<VenteJpaEntity, Long>`

#### `modules\commercial\infrastructure\persistence\vente\repository\FactureAvoirRepositoryImpl.java`

**class** `FactureAvoirRepositoryImpl` (@Repository
public) implements `FactureAvoirRepository`

- **Champs :**
  - `private final FactureAvoirJpaRepository jpaRepository`

- **Méthodes :**
  - `public FactureAvoirRepositoryImpl(FactureAvoirJpaRepository jpaRepository)`
  - `@Override public FactureAvoir sauvegarder(FactureAvoir avoir)`
  - `@Override public Optional<FactureAvoir> trouverParNumero(String numeroAvoir)`
  - `@Override public List<FactureAvoir> listerParVenteOriginale(String numeroVenteOriginale)`
  - `@Override public List<FactureAvoir> listerTous()`
  - `@Override public String genererNumero(LocalDate date)`
  - `private FactureAvoirJpaEntity toEntity(FactureAvoir a)`
  - `private FactureAvoir toDomain(FactureAvoirJpaEntity e)`

#### `modules\commercial\infrastructure\persistence\vente\repository\VenteRepositoryImpl.java`

**class** `VenteRepositoryImpl` (@Repository
public) implements `VenteRepository`

- **Champs :**
  - `private final VenteJpaRepository jpaRepository`

- **Méthodes :**
  - `public VenteRepositoryImpl(VenteJpaRepository jpaRepository)`
  - `@Override public Vente sauvegarder(Vente vente)`
  - `@Override public Optional<Vente> trouverParNumero(String numeroVente)`
  - `@Override public List<Vente> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<Vente> listerParCommercialEtDate(String matriculeCommercial, LocalDate date)`
  - `@Override public List<Vente> listerParStatut(StatutVente statut)`
  - `@Override public List<Vente> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public double totalVentesParCommercialEtPeriode(String matriculeCommercial, LocalDate debut, LocalDate fin)`
  - `@Override public Page<Vente> listerParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable)`
  - `@Override public Page<Vente> listerParCommercialPagine(String matriculeCommercial, Pageable pageable)`
  - `public String genererNumero(LocalDate date)`
  - `private VenteJpaEntity toEntity(Vente v)`
  - `private Vente toDomain(VenteJpaEntity e)`

#### `modules\commercial\presentation\controller\BoutiquePotentielleController.java`

**class** `BoutiquePotentielleController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final BoutiquePotentielleRepository repository`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public BoutiquePotentielleController(BoutiquePotentielleRepository repository, AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> recenser(@RequestBody RecenserBoutiqueRequest request)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<BoutiquePotentielle>>> parSecteur(@PathVariable String secteur)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<String>>> secteurs()`
  - `public record RecenserBoutiqueRequest(String nom, String secteur, String adresse)`

**record** `RecenserBoutiqueRequest` (public)

- **Composants (record) :**
  - `String nom`
  - `String secteur`
  - `String adresse`

#### `modules\commercial\presentation\controller\CarburantController.java`

**class** `CarburantController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final CarburantRepository carburantRepository`
  - `private final CarburantMapper carburantMapper`
  - `List<DemandeCarburant> demandes`

- **Méthodes :**
  - `public CarburantController(CarburantRepository carburantRepository, CarburantMapper carburantMapper)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<CarburantResponse>> creer(@RequestBody CreerCarburantRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<CarburantResponse>> signer(@PathVariable String reference,
                                                                 @RequestBody SignerCarburantRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<CarburantResponse>> refuser(@PathVariable String reference, @RequestParam String motif)`
  - `public record CreerCarburantRequest(String matriculeCommercial, String immatriculation,
        String stationPartenaire, int kilometrage, double litresDemandes, double montant,
        String photoCompteur)`
  - `public record SignerCarburantRequest(String signatureCommercial, String signatureSuperviseur)`

**record** `CreerCarburantRequest` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `String immatriculation`
  - `String stationPartenaire`
  - `int kilometrage`
  - `double litresDemandes`
  - `double montant`
  - `String photoCompteur`

**record** `SignerCarburantRequest` (public)

- **Composants (record) :**
  - `String signatureCommercial`
  - `String signatureSuperviseur`

#### `modules\commercial\presentation\controller\ClientController.java`

**class** `ClientController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final ClientRepository clientRepository`
  - `private final ClientMapper clientMapper`
  - `List<Client> clients`

- **Méthodes :**
  - `public ClientController(ClientRepository clientRepository, ClientMapper clientMapper)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<ClientResponse>> creer(@RequestBody CreerClientRequest req)`
  - `@PutMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<ClientResponse>> mettreAJour(@PathVariable String code,
                                                                   @RequestBody MettreAJourClientRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<ClientResponse>> desactiver(@PathVariable String code)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<ClientResponse>> consulter(@PathVariable String code)`
  - `public record CreerClientRequest(String nom, String telephone, String adresse, String localite,
                                     TypeClient type, String matriculeCommercialReferent)`
  - `public record MettreAJourClientRequest(String nom, String telephone, String adresse, String localite, TypeClient type)`

**record** `CreerClientRequest` (public)

- **Composants (record) :**
  - `String nom`
  - `String telephone`
  - `String adresse`
  - `String localite`
  - `TypeClient type`
  - `String matriculeCommercialReferent`

**record** `MettreAJourClientRequest` (public)

- **Composants (record) :**
  - `String nom`
  - `String telephone`
  - `String adresse`
  - `String localite`
  - `TypeClient type`

#### `modules\commercial\presentation\controller\CommandeDistanceController.java`

**class** `CommandeDistanceController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final CommandeDistanceRepository repository`
  - `private final CommandeDistanceMapper mapper`

- **Méthodes :**
  - `public CommandeDistanceController(CommandeDistanceRepository repository, CommandeDistanceMapper mapper)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CommandeDistanceResponse>> recevoir(@RequestBody RecevoirCommandeRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<CommandeDistanceResponse>> validerSecretaire(@PathVariable String numero,
                                                                                     @RequestParam String matricule)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<CommandeDistanceResponse>> validerComptable(@PathVariable String numero,
                                                                                     @RequestParam String matricule)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<CommandeDistanceResponse>> livrer(@PathVariable String numero)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CommandeDistanceResponse>> annuler(@PathVariable String numero,
                                                                           @RequestParam String motif)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<CommandeDistanceResponse>> consulter(@PathVariable String numero)`
  - `@GetMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<List<CommandeDistanceResponse>>> lister()`
  - `private CommandeDistance trouverOuErreur(String numero)`
  - `public record RecevoirCommandeRequest(String codeClient, String matriculeCommercial, CanalCommande canal,
                                           LocalDate dateCommande, String observations,
                                           List<LigneDto> lignes)`
  - `public record LigneDto(String codeProduit, String designation, String conditionnement,
                           int quantite, double prixUnitaire)`

**record** `RecevoirCommandeRequest` (public)

- **Composants (record) :**
  - `String codeClient`
  - `String matriculeCommercial`
  - `CanalCommande canal`
  - `LocalDate dateCommande`
  - `String observations`
  - `List<LigneDto> lignes`

**record** `LigneDto` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `String conditionnement`
  - `int quantite`
  - `double prixUnitaire`

#### `modules\commercial\presentation\controller\DashboardCommercialController.java`

**class** `DashboardCommercialController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DashboardCommercialUseCase dashboardUseCase`
  - `private final FicheSyntheseUseCase ficheSyntheseUseCase`
  - `private final ObtenirClassementDgUseCase classementUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public DashboardCommercialController(DashboardCommercialUseCase dashboardUseCase,
                                          FicheSyntheseUseCase ficheSyntheseUseCase,
                                          ObtenirClassementDgUseCase classementUseCase,
                                          AuthenticatedUserResolver userResolver)`
  - `DashboardCommercial>> dashboardCommercial()`
  - `DashboardDG>> dashboardDG()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<List<ClassementCommercialResponse>>> classement()`

#### `modules\commercial\presentation\controller\FeuillRouteController.java`

**class** `FeuillRouteController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final FeuillRouteRepository feuillRouteRepository`
  - `private final FeuillRouteMapper feuillRouteMapper`

- **Méthodes :**
  - `public FeuillRouteController(FeuillRouteRepository feuillRouteRepository, FeuillRouteMapper feuillRouteMapper)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<FeuillRouteResponse>> planifier(@RequestBody PlanifierFeuillRouteRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<FeuillRouteResponse>> demarrer(@PathVariable String numero)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<FeuillRouteResponse>> viserArret(@PathVariable String numero,
                                                                        @RequestBody ViserArretRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<FeuillRouteResponse>> marquerNonVise(@PathVariable String numero,
                                                                             @RequestBody NonViseArretRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<FeuillRouteResponse>> cloturer(@PathVariable String numero,
                                                                      @RequestParam String matriculeCloture)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<FeuillRouteResponse>> consulter(@PathVariable String numero)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<List<FeuillRouteResponse>>> parCommercial(@PathVariable String matricule)`
  - `private FeuillRoute trouverOuErreur(String numero)`
  - `public record PlanifierFeuillRouteRequest(String matriculeCommercial, LocalDate date, List<ArretDto> arrets)`
  - `public record ArretDto(String codeClient, int ordre)`
  - `public record ViserArretRequest(String codeClient, String numeroVente, String coordonneesGPS)`
  - `public record NonViseArretRequest(String codeClient, InfoClientEnum codeInfoClient, Integer quantiteInfo,
                                       String observations, String coordonneesGPS)`

**record** `PlanifierFeuillRouteRequest` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `LocalDate date`
  - `List<ArretDto> arrets`

**record** `ArretDto` (public)

- **Composants (record) :**
  - `String codeClient`
  - `int ordre`

**record** `ViserArretRequest` (public)

- **Composants (record) :**
  - `String codeClient`
  - `String numeroVente`
  - `String coordonneesGPS`

**record** `NonViseArretRequest` (public)

- **Composants (record) :**
  - `String codeClient`
  - `InfoClientEnum codeInfoClient`
  - `Integer quantiteInfo`
  - `String observations`
  - `String coordonneesGPS`

#### `modules\commercial\presentation\controller\PreCommandeController.java`

**class** `PreCommandeController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final PreCommandeRepository preCommandeRepository`
  - `private final PreCommandeMapper preCommandeMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public PreCommandeController(PreCommandeRepository preCommandeRepository, PreCommandeMapper preCommandeMapper,
                                EventPublisher eventPublisher)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<PreCommandeResponse>> soumettre(@RequestBody SoumettrePreCommandeRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<PreCommandeResponse>> validerSecretaire(@PathVariable String numero,
                                                                              @RequestParam String matriculeSecretaire)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<PreCommandeResponse>> validerComptable(@PathVariable String numero,
                                                                             @RequestParam String matriculeComptable)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PreCommandeResponse>> refuser(@PathVariable String numero,
                                                                      @RequestParam String motif,
                                                                      @RequestParam String matriculeValidateur)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PreCommandeResponse>> livrer(@PathVariable String numero)`
  - `public record SoumettrePreCommandeRequest(String matriculeCommercial, LocalDate dateSoumission,
                                              List<LignePreCommandeDto> lignes)`
  - `public record LignePreCommandeDto(String codeProduit, String designation, String conditionnement, int quantite)`

**record** `SoumettrePreCommandeRequest` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `LocalDate dateSoumission`
  - `List<LignePreCommandeDto> lignes`

**record** `LignePreCommandeDto` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `String conditionnement`
  - `int quantite`

#### `modules\commercial\presentation\controller\ProspectController.java`

**class** `ProspectController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ProspectRepository prospectRepository`
  - `private final ProspectMapper prospectMapper`

- **Méthodes :**
  - `public ProspectController(ProspectRepository prospectRepository, ProspectMapper prospectMapper)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "", description = "") public ResponseEntity<ApiResponse<ProspectResponse>> creer(@RequestBody CreerProspectRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProspectResponse>> convertir(@PathVariable String code,
                                                                    @RequestParam String codeClient)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProspectResponse>> marquerNonInteresse(@PathVariable String code,
                                                                               @RequestParam String motif)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProspectResponse>> archiver(@PathVariable String code)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<ProspectResponse>> consulter(@PathVariable String code)`
  - `@GetMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<List<ProspectResponse>>> lister()`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<List<ProspectResponse>>> parCommercial(@PathVariable String matricule)`
  - `private Prospect trouverOuErreur(String code)`
  - `public record CreerProspectRequest(String nom, String telephone, String adresse, String localite,
                                       String matriculeCommercial, CanalProspection canal,
                                       LocalDate dateProspection, String observations)`

**record** `CreerProspectRequest` (public)

- **Composants (record) :**
  - `String nom`
  - `String telephone`
  - `String adresse`
  - `String localite`
  - `String matriculeCommercial`
  - `CanalProspection canal`
  - `LocalDate dateProspection`
  - `String observations`

#### `modules\commercial\presentation\controller\RapportCommercialController.java`

**class** `RapportCommercialController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final GenererRapportVentesUseCase genererRapportVentesUseCase`
  - `private final CalculerPrimesHebdoUseCase calculerPrimesUseCase`
  - `private final VenteRepository venteRepository`
  - `private final GenererRapportRendementCarburantUseCase rapportRendementCarburantUseCase`
  - `private final GenererRapportTourneeUseCase rapportTourneeUseCase`
  - `Set<String> tousCommerciaux = new java.util.LinkedHashSet<>()`
  - `double evolution = caN1 > 0 ? ((caN / caN1) - 1) * 100.0 : 0.0`

- **Méthodes :**
  - `public RapportCommercialController(GenererRapportVentesUseCase genererRapportVentesUseCase,
                                        CalculerPrimesHebdoUseCase calculerPrimesUseCase,
                                        VenteRepository venteRepository,
                                        GenererRapportRendementCarburantUseCase rapportRendementCarburantUseCase,
                                        GenererRapportTourneeUseCase rapportTourneeUseCase)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<List<ComparatifNvsN1Response>>> comparatifNvsN1(@RequestParam int annee,
        @RequestParam int mois)`
  - `public record ComparatifNvsN1Response(String matriculeCommercial, double caMoisActuel,
        double caMoisN1, double evolutionPourcent)`

**record** `ComparatifNvsN1Response` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `double caMoisActuel`
  - `double caMoisN1`
  - `double evolutionPourcent`

#### `modules\commercial\presentation\controller\RecouvrementController.java`

**class** `RecouvrementController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final RecouvrementRepository recouvrementRepository`
  - `private final RecouvrementMapper recouvrementMapper`
  - `List<Recouvrement> resultats`

- **Méthodes :**
  - `public RecouvrementController(RecouvrementRepository recouvrementRepository, RecouvrementMapper recouvrementMapper)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<RecouvrementResponse>> creer(@RequestBody CreerRecouvrementRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<RecouvrementResponse>> rembourser(@PathVariable String reference, @RequestParam double montant)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<RecouvrementResponse>> passerEnContentieux(@PathVariable String reference, @RequestParam String motif)`
  - `public record CreerRecouvrementRequest(String codeClient, String nomClient, double montantDu,
                                           LocalDate dateEcheance, String matriculeCommercial)`

**record** `CreerRecouvrementRequest` (public)

- **Composants (record) :**
  - `String codeClient`
  - `String nomClient`
  - `double montantDu`
  - `LocalDate dateEcheance`
  - `String matriculeCommercial`

#### `modules\commercial\presentation\controller\TarificationController.java`

**class** `TarificationController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DefinirGrilleTarifaireUseCase definirGrilleTarifaireUseCase`
  - `private final AccorderPrixExceptionnelUseCase accorderPrixExceptionnelUseCase`
  - `private final GrilleTarifaireRepository grilleTarifaireRepository`
  - `private final PrixExceptionnelRepository prixExceptionnelRepository`
  - `private final GrilleTarifaireMapper grilleTarifaireMapper`
  - `private final PrixExceptionnelMapper prixExceptionnelMapper`
  - `private final AuthenticatedUserResolver userResolver`
  - `List<GrilleTarifaireResponse> resultats`

- **Méthodes :**
  - `public TarificationController(DefinirGrilleTarifaireUseCase definirGrilleTarifaireUseCase,
                                   AccorderPrixExceptionnelUseCase accorderPrixExceptionnelUseCase,
                                   GrilleTarifaireRepository grilleTarifaireRepository,
                                   PrixExceptionnelRepository prixExceptionnelRepository,
                                   GrilleTarifaireMapper grilleTarifaireMapper,
                                   PrixExceptionnelMapper prixExceptionnelMapper,
                                   AuthenticatedUserResolver userResolver)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<GrilleTarifaireResponse>> definirGrille(@Valid @RequestBody DefinirGrilleTarifaireRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "" +
                      "" +
                      "") public ResponseEntity<ApiResponse<PrixExceptionnelResponse>> accorderPrixExceptionnel(@Valid @RequestBody AccorderPrixExceptionnelRequest request)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<PrixExceptionnelResponse>>> parClient(@PathVariable String codeClient)`

#### `modules\commercial\presentation\controller\TerminalController.java`

**class** `TerminalController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final TerminalRepository terminalRepository`

- **Méthodes :**
  - `public TerminalController(TerminalRepository terminalRepository)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<TerminalResponse>> attribuer(@RequestBody CreerTerminalRequest req)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<TerminalResponse>> trouverParImei(@PathVariable String imei)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<TerminalResponse>> deduirePrime(@PathVariable String imei,
                                                                       @RequestParam double montant)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<TerminalResponse>> desactiver(@PathVariable String imei)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<TerminalResponse>> reassigner(@PathVariable String imei,
                                                                     @RequestParam String matricule)`
  - `private TerminalResponse toResponse(Terminal t)`
  - `public record CreerTerminalRequest(String imei, String modele, String matriculeCommercial, double prixAchat)`
  - `public record TerminalResponse(Long id, String imei, String modele, String matriculeCommercial,
        double prixAchat, double resteAPayer, boolean actif, boolean estRembourse,
        java.time.LocalDateTime dateDerniereConnexion, java.time.LocalDateTime dateCreation)`

**record** `CreerTerminalRequest` (public)

- **Composants (record) :**
  - `String imei`
  - `String modele`
  - `String matriculeCommercial`
  - `double prixAchat`

**record** `TerminalResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String imei`
  - `String modele`
  - `String matriculeCommercial`
  - `double prixAchat`
  - `double resteAPayer`
  - `boolean actif`
  - `boolean estRembourse`
  - `java.time.LocalDateTime dateDerniereConnexion`
  - `java.time.LocalDateTime dateCreation`

#### `modules\commercial\presentation\controller\VehiculeController.java`

**class** `VehiculeController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final VehiculeRepository vehiculeRepository`
  - `List<Vehicule> vehicules`

- **Méthodes :**
  - `public VehiculeController(VehiculeRepository vehiculeRepository)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<VehiculeResponse>> enregistrer(@RequestBody CreerVehiculeRequest req)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<VehiculeResponse>> trouverParImmat(@PathVariable String immatriculation)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<VehiculeResponse>> reassigner(@PathVariable String immatriculation,
                                                                     @RequestParam String matricule)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<VehiculeResponse>> mettreAJourConso(@PathVariable String immatriculation,
                                                                           @RequestParam double conso)`
  - `private VehiculeResponse toResponse(Vehicule v)`
  - `public record CreerVehiculeRequest(String immatriculation, String marque, String modele,
        String matriculeCommercial, double consommationMoyenneL100)`
  - `public record VehiculeResponse(Long id, String immatriculation, String marque, String modele,
        String matriculeCommercial, boolean actif, double consommationMoyenneL100,
        java.time.LocalDateTime dateCreation)`

**record** `CreerVehiculeRequest` (public)

- **Composants (record) :**
  - `String immatriculation`
  - `String marque`
  - `String modele`
  - `String matriculeCommercial`
  - `double consommationMoyenneL100`

**record** `VehiculeResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String immatriculation`
  - `String marque`
  - `String modele`
  - `String matriculeCommercial`
  - `boolean actif`
  - `double consommationMoyenneL100`
  - `java.time.LocalDateTime dateCreation`

#### `modules\commercial\presentation\controller\VenteController.java`

**class** `VenteController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final VenteRepository venteRepository`
  - `private final VenteMapper venteMapper`
  - `private final EventPublisher eventPublisher`
  - `private final TarificationGuardService tarificationGuardService`
  - `private final FactureAvoirRepository factureAvoirRepository`
  - `private final DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase`

- **Méthodes :**
  - `public VenteController(VenteRepository venteRepository, VenteMapper venteMapper, EventPublisher eventPublisher,
                            TarificationGuardService tarificationGuardService,
                            FactureAvoirRepository factureAvoirRepository,
                            DeclencherTransactionNonMarchandeUseCase nonMarchandeUseCase)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "",
        description = "" +
                      "" +
                      "") public ResponseEntity<ApiResponse<VenteResponse>> creerVente(@RequestBody CreerVenteRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<VenteResponse>> finaliserVente(@PathVariable String numeroVente)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<VenteResponse>> annulerVente(@PathVariable String numeroVente,
                                                                     @RequestParam String motif)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "" +
                      "") public ResponseEntity<ApiResponse<VenteResponse>> declarerNonMarchand(@RequestBody NonMarchandeRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "" +
                      "") public ResponseEntity<ApiResponse<AvoirResponse>> emettreAvoir(@PathVariable String numeroVente,
                                                                     @RequestBody CreerAvoirRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<AvoirResponse>> validerAvoir(@PathVariable String numeroAvoir)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<AvoirResponse>> trouverAvoir(@PathVariable String numeroAvoir)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<List<AvoirResponse>>> avoirsParVente(@PathVariable String numeroVente)`
  - `private AvoirResponse toAvoirResponse(FactureAvoir a)`
  - `public record CreerVenteRequest(String matriculeCommercial, String codeClient, LocalDate date,
                                    ModeReglement modeReglement, TypeVente typeVente,
                                    String coordonneesGPS, String idFeuillRoute,
                                    String uuidVente, boolean signatureClient,
                                    List<LigneVenteDto> lignes)`
  - `public record LigneVenteDto(String codeProduit, String designation, String conditionnement,
                                int quantite, double prixUnitaire, double remise,
                                String referencePrixExceptionnel)`
  - `public record CreerAvoirRequest(String matriculeComptable, String motif)`
  - `public record NonMarchandeRequest(TypeVente typeVente, String matriculeCommercial, String codeClient,
        String codeProduit, String designationProduit, String conditionnement,
        int quantite, LocalDate date, String motif, double pourcentageLot,
        String photoJustificative)`
  - `public record AvoirResponse(Long id, String numeroAvoir, String numeroVenteOriginale,
        String matriculeCommercial, String codeClient, String matriculeComptable,
        double montantHT, double montantTVA, double montantTTC, String motif,
        LocalDate dateAvoir, String statut, java.time.LocalDateTime dateCreation,
        java.time.LocalDateTime dateValidation)`

**record** `CreerVenteRequest` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `String codeClient`
  - `LocalDate date`
  - `ModeReglement modeReglement`
  - `TypeVente typeVente`
  - `String coordonneesGPS`
  - `String idFeuillRoute`
  - `String uuidVente`
  - `boolean signatureClient`
  - `List<LigneVenteDto> lignes`

**record** `LigneVenteDto` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `String conditionnement`
  - `int quantite`
  - `double prixUnitaire`
  - `double remise`
  - `String referencePrixExceptionnel`

**record** `CreerAvoirRequest` (public)

- **Composants (record) :**
  - `String matriculeComptable`
  - `String motif`

**record** `NonMarchandeRequest` (public)

- **Composants (record) :**
  - `TypeVente typeVente`
  - `String matriculeCommercial`
  - `String codeClient`
  - `String codeProduit`
  - `String designationProduit`
  - `String conditionnement`
  - `int quantite`
  - `LocalDate date`
  - `String motif`
  - `double pourcentageLot`
  - `String photoJustificative`

**record** `AvoirResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroAvoir`
  - `String numeroVenteOriginale`
  - `String matriculeCommercial`
  - `String codeClient`
  - `String matriculeComptable`
  - `double montantHT`
  - `double montantTVA`
  - `double montantTTC`
  - `String motif`
  - `LocalDate dateAvoir`
  - `String statut`
  - `java.time.LocalDateTime dateCreation`
  - `java.time.LocalDateTime dateValidation`

#### `modules\commercial\presentation\controller\ZoneDeVenteController.java`

**class** `ZoneDeVenteController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final ZoneDeVenteRepository zoneRepository`

- **Méthodes :**
  - `public ZoneDeVenteController(ZoneDeVenteRepository zoneRepository)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<ZoneResponse>> creer(@RequestBody CreerZoneRequest req)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<ZoneResponse>> trouverParNom(@PathVariable String nom)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<ZoneResponse>> calibrer(@PathVariable String nom,
                                                               @RequestParam int clientsPotentiels)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<TauxOccupationResponse>> tauxOccupation(@PathVariable String nom,
                                                                               @RequestParam int clientsLivres3Mois)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<ZoneResponse>> desactiver(@PathVariable String nom)`
  - `private ZoneResponse toResponse(ZoneDeVente z)`
  - `public record CreerZoneRequest(String nom, String type, String description, int clientsPotentiels)`
  - `public record ZoneResponse(Long id, String nom, String type, String description,
        int clientsPotentiels, boolean actif, java.time.LocalDateTime dateCreation)`
  - `public record TauxOccupationResponse(String zone, int clientsPotentiels, int clientsLivres,
        double tauxOccupation, boolean sousExploitee)`

**record** `CreerZoneRequest` (public)

- **Composants (record) :**
  - `String nom`
  - `String type`
  - `String description`
  - `int clientsPotentiels`

**record** `ZoneResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String nom`
  - `String type`
  - `String description`
  - `int clientsPotentiels`
  - `boolean actif`
  - `java.time.LocalDateTime dateCreation`

**record** `TauxOccupationResponse` (public)

- **Composants (record) :**
  - `String zone`
  - `int clientsPotentiels`
  - `int clientsLivres`
  - `double tauxOccupation`
  - `boolean sousExploitee`


### Module `commercialisation`

#### `modules\commercialisation\application\dto\CampagneResponse.java`

**record** `CampagneResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String code`
  - `String libelle`
  - `String description`
  - `TypeCampagne type`
  - `LocalDate dateDebut`
  - `LocalDate dateFin`
  - `String zoneCible`
  - `double budgetPrevuFCFA`
  - `double budgetDepenseFCFA`
  - `double tauxConsommationBudget`
  - `StatutCampagne statut`
  - `String matriculeCreateur`
  - `LocalDate dateCreation`
  - `List<String> promotionsLiees`

#### `modules\commercialisation\application\dto\CreerCampagneRequest.java`

_Fichier vide ou non analysable._

#### `modules\commercialisation\application\dto\CreerPromotionRequest.java`

_Fichier vide ou non analysable._

#### `modules\commercialisation\application\dto\PromotionResponse.java`

**record** `PromotionResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String code`
  - `String libelle`
  - `String description`
  - `TypePromotion type`
  - `double valeurReduction`
  - `String codeProduit`
  - `TypeClient segmentCible`
  - `LocalDate dateDebut`
  - `LocalDate dateFin`
  - `StatutPromotion statut`
  - `int nombreUtilisations`
  - `int limiteUtilisations`
  - `String matriculeCreateur`
  - `LocalDate dateCreation`

#### `modules\commercialisation\application\usecase\GererCampagneUseCase.java`

**class** `GererCampagneUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final CampagneMarketingRepository repository`

- **Méthodes :**
  - `public GererCampagneUseCase(CampagneMarketingRepository repository)`
  - `public CampagneResponse creer(CreerCampagneRequest request, String matriculeCreateur)`
  - `@Transactional(readOnly = true) public List<CampagneResponse> listerTous()`
  - `@Transactional(readOnly = true) public List<CampagneResponse> listerParStatut(StatutCampagne statut)`
  - `@Transactional(readOnly = true) public CampagneResponse consulter(String code)`
  - `public CampagneResponse demarrer(String code)`
  - `public CampagneResponse terminer(String code)`
  - `public CampagneResponse annuler(String code)`
  - `public CampagneResponse enregistrerDepense(String code, double montantFCFA)`
  - `public CampagneResponse lierPromotion(String code, String codePromotion)`
  - `@Transactional(readOnly = true) public List<CampagneResponse> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `private CampagneResponse versResponse(CampagneMarketing c)`

#### `modules\commercialisation\application\usecase\GererPromotionUseCase.java`

**class** `GererPromotionUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final PromotionRepository repository`

- **Méthodes :**
  - `public GererPromotionUseCase(PromotionRepository repository)`
  - `public PromotionResponse creer(CreerPromotionRequest request, String matriculeCreateur)`
  - `@Transactional(readOnly = true) public List<PromotionResponse> listerTous()`
  - `@Transactional(readOnly = true) public List<PromotionResponse> listerParStatut(StatutPromotion statut)`
  - `@Transactional(readOnly = true) public List<PromotionResponse> listerActives()`
  - `@Transactional(readOnly = true) public PromotionResponse consulter(String code)`
  - `public PromotionResponse activer(String code)`
  - `public PromotionResponse suspendre(String code)`
  - `public PromotionResponse annuler(String code)`
  - `private PromotionResponse versResponse(Promotion p)`

#### `modules\commercialisation\domain\aggregate\CampagneMarketing.java`

**class** `CampagneMarketing` (public)

- **Champs :**
  - `private Long id`
  - `private final String code`
  - `private String libelle`
  - `private String description`
  - `private final TypeCampagne type`
  - `private final LocalDate dateDebut`
  - `private LocalDate dateFin`
  - `private String zoneCible`
  - `private double budgetPrevuFCFA`
  - `private double budgetDepenseFCFA`
  - `private StatutCampagne statut`
  - `private final String matriculeCreateur`
  - `private final LocalDate dateCreation`
  - `private LocalDate dateModification`
  - `private final List<String> promotionsLiees = new ArrayList<>()`

- **Méthodes :**
  - `private CampagneMarketing(String code, String libelle, TypeCampagne type, LocalDate dateDebut,
                               String matriculeCreateur, LocalDate dateCreation)`
  - `public static CampagneMarketing creer(String code, String libelle, String description, TypeCampagne type,
                                           LocalDate dateDebut, LocalDate dateFin, String zoneCible,
                                           double budgetPrevuFCFA, String matriculeCreateur)`
  - `public static CampagneMarketing reconstituer(Long id, String code, String libelle, String description,
                                                   TypeCampagne type, LocalDate dateDebut, LocalDate dateFin,
                                                   String zoneCible, double budgetPrevuFCFA,
                                                   double budgetDepenseFCFA, StatutCampagne statut,
                                                   String matriculeCreateur, LocalDate dateCreation,
                                                   LocalDate dateModification, List<String> promotionsLiees)`
  - `public void demarrer()`
  - `public void terminer()`
  - `public void annuler()`
  - `public void enregistrerDepense(double montantFCFA)`
  - `public void lierPromotion(String codePromotion)`
  - `public double getTauxConsommationBudget()`
  - `public List<String> getPromotionsLiees()`
  - `public Long getId()`
  - `public String getCode()`
  - `public String getLibelle()`
  - `public String getDescription()`
  - `public TypeCampagne getType()`
  - `public LocalDate getDateDebut()`
  - `public LocalDate getDateFin()`
  - `public String getZoneCible()`
  - `public double getBudgetPrevuFCFA()`
  - `public double getBudgetDepenseFCFA()`
  - `public StatutCampagne getStatut()`
  - `public String getMatriculeCreateur()`
  - `public LocalDate getDateCreation()`
  - `public LocalDate getDateModification()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercialisation\domain\aggregate\Promotion.java`

**class** `Promotion` (public)

- **Champs :**
  - `private Long id`
  - `private final String code`
  - `private String libelle`
  - `private String description`
  - `private final TypePromotion type`
  - `private double valeurReduction`
  - `private String codeProduit`
  - `private TypeClient segmentCible`
  - `private final LocalDate dateDebut`
  - `private LocalDate dateFin`
  - `private StatutPromotion statut`
  - `private int nombreUtilisations`
  - `private int limiteUtilisations`
  - `private final String matriculeCreateur`
  - `private final LocalDate dateCreation`
  - `private LocalDate dateModification`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private Promotion(String code, String libelle, TypePromotion type, LocalDate dateDebut,
                      String matriculeCreateur, LocalDate dateCreation)`
  - `public static Promotion creer(String code, String libelle, String description, TypePromotion type,
                                   double valeurReduction, String codeProduit, TypeClient segmentCible,
                                   LocalDate dateDebut, LocalDate dateFin, int limiteUtilisations,
                                   String matriculeCreateur)`
  - `public static Promotion reconstituer(Long id, String code, String libelle, String description,
                                          TypePromotion type, double valeurReduction, String codeProduit,
                                          TypeClient segmentCible, LocalDate dateDebut, LocalDate dateFin,
                                          StatutPromotion statut, int nombreUtilisations, int limiteUtilisations,
                                          String matriculeCreateur, LocalDate dateCreation,
                                          LocalDate dateModification)`
  - `public void activer()`
  - `public void suspendre()`
  - `public void annuler()`
  - `public void enregistrerUtilisation()`
  - `public boolean estValide()`
  - `public double calculerRemise(double prixOriginal)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getCode()`
  - `public String getLibelle()`
  - `public String getDescription()`
  - `public TypePromotion getType()`
  - `public double getValeurReduction()`
  - `public String getCodeProduit()`
  - `public TypeClient getSegmentCible()`
  - `public LocalDate getDateDebut()`
  - `public LocalDate getDateFin()`
  - `public StatutPromotion getStatut()`
  - `public int getNombreUtilisations()`
  - `public int getLimiteUtilisations()`
  - `public String getMatriculeCreateur()`
  - `public LocalDate getDateCreation()`
  - `public LocalDate getDateModification()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\commercialisation\domain\enums\StatutCampagne.java`

**enum** `StatutCampagne` (public)

#### `modules\commercialisation\domain\enums\StatutPromotion.java`

**enum** `StatutPromotion` (public)

#### `modules\commercialisation\domain\enums\TypeCampagne.java`

**enum** `TypeCampagne` (public)

- **Constantes :**
  - `{
    LANCEMENT_PRODUIT("")`
  - `PROMO_SAISONNIERE("")`
  - `FIDELISATION("")`
  - `PROSPECTION("")`
  - `COMMUNICATION("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\commercialisation\domain\enums\TypePromotion.java`

**enum** `TypePromotion` (public)

- **Constantes :**
  - `{
    POURCENTAGE("")`
  - `MONTANT_FIXE("")`
  - `GRATUITE("")`
  - `REMISE_VOLUME("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\commercialisation\domain\repository\CampagneMarketingRepository.java`

**interface** `CampagneMarketingRepository` (public)

#### `modules\commercialisation\domain\repository\PromotionRepository.java`

**interface** `PromotionRepository` (public)

#### `modules\commercialisation\infrastructure\persistence\jpa\CampagneMarketingJpaEntity.java`

**class** `CampagneMarketingJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true, length = 30) private String code`
  - `@Column(nullable = false, length = 150) private String libelle`
  - `@Column(length = 500) private String description`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25) private TypeCampagne type`
  - `@Column(name = "", nullable = false) private LocalDate dateDebut`
  - `@Column(name = "") private LocalDate dateFin`
  - `@Column(name = "", length = 100) private String zoneCible`
  - `@Column(name = "", nullable = false) private double budgetPrevuFCFA`
  - `@Column(name = "", nullable = false) private double budgetDepenseFCFA`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15) private StatutCampagne statut`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeCreateur`
  - `@Column(name = "", nullable = false) private LocalDate dateCreation`
  - `@Column(name = "") private LocalDate dateModification`
  - `@Column(name = "", length = 500) private String promotionsLieesCsv`

#### `modules\commercialisation\infrastructure\persistence\jpa\CampagneMarketingJpaRepository.java`

**interface** `CampagneMarketingJpaRepository` (public) extends `JpaRepository<CampagneMarketingJpaEntity, Long>`

#### `modules\commercialisation\infrastructure\persistence\jpa\PromotionJpaEntity.java`

**class** `PromotionJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true, length = 30) private String code`
  - `@Column(nullable = false, length = 150) private String libelle`
  - `@Column(length = 500) private String description`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private TypePromotion type`
  - `@Column(name = "", nullable = false) private double valeurReduction`
  - `@Column(name = "", length = 30) private String codeProduit`
  - `@Enumerated(EnumType.STRING)
    @Column(name = "", length = 20) private TypeClient segmentCible`
  - `@Column(name = "", nullable = false) private LocalDate dateDebut`
  - `@Column(name = "") private LocalDate dateFin`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15) private StatutPromotion statut`
  - `@Column(name = "", nullable = false) private int nombreUtilisations`
  - `@Column(name = "", nullable = false) private int limiteUtilisations`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeCreateur`
  - `@Column(name = "", nullable = false) private LocalDate dateCreation`
  - `@Column(name = "") private LocalDate dateModification`

#### `modules\commercialisation\infrastructure\persistence\jpa\PromotionJpaRepository.java`

**interface** `PromotionJpaRepository` (public) extends `JpaRepository<PromotionJpaEntity, Long>`

#### `modules\commercialisation\infrastructure\persistence\repository\CampagneMarketingRepositoryImpl.java`

**class** `CampagneMarketingRepositoryImpl` (@Repository
public) implements `CampagneMarketingRepository`

- **Champs :**
  - `private final CampagneMarketingJpaRepository jpaRepository`

- **Méthodes :**
  - `public CampagneMarketingRepositoryImpl(CampagneMarketingJpaRepository jpaRepository)`
  - `@Override public CampagneMarketing sauvegarder(CampagneMarketing campagne)`
  - `@Override public Optional<CampagneMarketing> trouverParCode(String code)`
  - `@Override public List<CampagneMarketing> listerTous()`
  - `@Override public List<CampagneMarketing> listerParStatut(StatutCampagne statut)`
  - `@Override public List<CampagneMarketing> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `private CampagneMarketingJpaEntity toEntity(CampagneMarketing c)`
  - `private CampagneMarketing toDomain(CampagneMarketingJpaEntity e)`

#### `modules\commercialisation\infrastructure\persistence\repository\PromotionRepositoryImpl.java`

**class** `PromotionRepositoryImpl` (@Repository
public) implements `PromotionRepository`

- **Champs :**
  - `private final PromotionJpaRepository jpaRepository`

- **Méthodes :**
  - `public PromotionRepositoryImpl(PromotionJpaRepository jpaRepository)`
  - `@Override public Promotion sauvegarder(Promotion promotion)`
  - `@Override public Optional<Promotion> trouverParCode(String code)`
  - `@Override public List<Promotion> listerTous()`
  - `@Override public List<Promotion> listerParStatut(StatutPromotion statut)`
  - `@Override public List<Promotion> listerActives()`
  - `@Override public List<Promotion> listerParProduit(String codeProduit)`
  - `private PromotionJpaEntity toEntity(Promotion p)`
  - `private Promotion toDomain(PromotionJpaEntity e)`

#### `modules\commercialisation\presentation\controller\CommercialisationController.java`

**class** `CommercialisationController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
@PreAuthorize("")
public)

- **Champs :**
  - `private final GererPromotionUseCase promotionUseCase`
  - `private final GererCampagneUseCase campagneUseCase`
  - `private final AuthenticatedUserResolver userResolver`
  - `List<CampagneResponse> r`

- **Méthodes :**
  - `public CommercialisationController(GererPromotionUseCase promotionUseCase,
                                        GererCampagneUseCase campagneUseCase,
                                        AuthenticatedUserResolver userResolver)`
  - `@PostMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PromotionResponse>> creerPromotion(@RequestBody @Valid CreerPromotionRequest request)`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PromotionResponse>> consulterPromotion(@PathVariable String code)`
  - `@PutMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PromotionResponse>> activerPromotion(@PathVariable String code)`
  - `@PutMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PromotionResponse>> suspendrePromotion(@PathVariable String code)`
  - `@PutMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PromotionResponse>> annulerPromotion(@PathVariable String code)`
  - `@PostMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CampagneResponse>> creerCampagne(@RequestBody @Valid CreerCampagneRequest request)`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CampagneResponse>> consulterCampagne(@PathVariable String code)`
  - `@PutMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CampagneResponse>> demarrerCampagne(@PathVariable String code)`
  - `@PutMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CampagneResponse>> terminerCampagne(@PathVariable String code)`
  - `@PutMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CampagneResponse>> annulerCampagne(@PathVariable String code)`
  - `@PutMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CampagneResponse>> enregistrerDepense(@PathVariable String code,
        @RequestBody Map<String, Double> body)`
  - `@PutMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CampagneResponse>> lierPromotion(@PathVariable String code,
        @RequestBody Map<String, String> body)`


### Module `comptabilite`

#### `modules\comptabilite\application\caisse\dto\response\CaisseResponse.java`

**record** `CaisseResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `LocalDate date`
  - `double soldeCourant`
  - `double seuilSecurisation`
  - `double totalEntrees`
  - `double totalSorties`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String referenceOperation, String typeMouvement, double montant,
                                String motif, LocalDateTime dateOperation, String matriculeOperateur)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String referenceOperation`
  - `String typeMouvement`
  - `double montant`
  - `String motif`
  - `LocalDateTime dateOperation`
  - `String matriculeOperateur`

#### `modules\comptabilite\application\caisse\mapper\CaisseMapper.java`

**class** `CaisseMapper` (@Component
public)

- **Méthodes :**
  - `public CaisseResponse versResponse(JournalCaisse j)`

#### `modules\comptabilite\application\dashboard\usecase\DashboardComptabiliteUseCase.java`

**class** `DashboardComptabiliteUseCase` (@Service
public)

- **Champs :**
  - `private final JournalCaisseRepository caisseRepository`
  - `private final FactureRepository factureRepository`
  - `private final DecaissementRepository decaissementRepository`
  - `private final VersementRepository versementRepository`
  - `private final PrimeRepository primeRepository`

- **Méthodes :**
  - `public DashboardComptabiliteUseCase(JournalCaisseRepository caisseRepository,
                                          FactureRepository factureRepository,
                                          DecaissementRepository decaissementRepository,
                                          VersementRepository versementRepository,
                                          PrimeRepository primeRepository)`
  - `public DashboardComptabilite executer()`
  - `public record DashboardComptabilite(LocalDate date,
        double soldeCaisse,
        double totalEntreesCaisse,
        double totalSortiesCaisse,
        double totalFacturesEmises,
        double totalFacturesPayees,
        int nbFacturesImpayees,
        double totalDecaissementsExecutes,
        int nbDecaissementsEnAttente,
        int nbVersementsEnAttente,
        int nbAlertesRouges,
        int nbPrimesValideesNonVersées,
        double totalPrimesAVerser)`

**record** `DashboardComptabilite` (public)

- **Composants (record) :**
  - `LocalDate date`
  - `double soldeCaisse`
  - `double totalEntreesCaisse`
  - `double totalSortiesCaisse`
  - `double totalFacturesEmises`
  - `double totalFacturesPayees`
  - `int nbFacturesImpayees`
  - `double totalDecaissementsExecutes`
  - `int nbDecaissementsEnAttente`
  - `int nbVersementsEnAttente`
  - `int nbAlertesRouges`
  - `int nbPrimesValideesNonVersées`
  - `double totalPrimesAVerser`

#### `modules\comptabilite\application\decaissement\dto\response\DecaissementResponse.java`

**record** `DecaissementResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroDecaissement`
  - `String typeDepense`
  - `String statut`
  - `String matriculeProposant`
  - `String matriculeValidateurCG`
  - `String matriculeValidateurDG`
  - `String matriculeExecuteur`
  - `double montantFCFA`
  - `String beneficiaire`
  - `LocalDate dateDepense`
  - `String motif`
  - `String motifAnnulation`
  - `LocalDateTime dateCreation`

#### `modules\comptabilite\application\decaissement\mapper\DecaissementMapper.java`

**class** `DecaissementMapper` (@Component
public)

- **Méthodes :**
  - `public DecaissementResponse versResponse(Decaissement d)`

#### `modules\comptabilite\application\facture\dto\response\FactureResponse.java`

**record** `FactureResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroFacture`
  - `String codeClient`
  - `String typeFacture`
  - `String referenceFactureOrigine`
  - `LocalDate dateEmission`
  - `String statut`
  - `String matriculeEmetteur`
  - `double montantTotalHT`
  - `double montantTotalTVA`
  - `double montantTotalTTC`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String referenceArticle, String designation, int quantite,
                                double prixUnitaireHT, double tauxTVA, double remise,
                                double montantHT, double montantTVA, double montantTTC)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String referenceArticle`
  - `String designation`
  - `int quantite`
  - `double prixUnitaireHT`
  - `double tauxTVA`
  - `double remise`
  - `double montantHT`
  - `double montantTVA`
  - `double montantTTC`

#### `modules\comptabilite\application\facture\mapper\FactureMapper.java`

**class** `FactureMapper` (@Component
public)

- **Méthodes :**
  - `public FactureResponse versResponse(Facture f)`

#### `modules\comptabilite\application\objectif\dto\request\DefinirObjectifRequest.java`

**record** `DefinirObjectifRequest` (public)

- **Composants (record) :**
  - `@NotBlank String matriculeCommercial`
  - `@NotNull LocalDate semaineDebut`
  - `@NotNull LocalDate semaineFin`
  - `@NotNull @Positive Double objectifGlobalFCFA`
  - `Map<String`
  - `Double> objectifsParGammeFCFA`

#### `modules\comptabilite\application\objectif\dto\response\ObjectifCommercialResponse.java`

**record** `ObjectifCommercialResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matriculeCommercial`
  - `LocalDate semaineDebut`
  - `LocalDate semaineFin`
  - `double objectifGlobalFCFA`
  - `Map<String`
  - `Double> objectifsParGammeFCFA`
  - `String matriculeAuteur`

#### `modules\comptabilite\application\objectif\mapper\ObjectifCommercialMapper.java`

**class** `ObjectifCommercialMapper` (@Component
public)

- **Méthodes :**
  - `public ObjectifCommercialResponse versResponse(ObjectifCommercial o)`

#### `modules\comptabilite\application\objectif\usecase\DefinirObjectifUseCase.java`

**class** `DefinirObjectifUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ObjectifCommercialRepository repository`
  - `private final ObjectifCommercialMapper mapper`

- **Méthodes :**
  - `public DefinirObjectifUseCase(ObjectifCommercialRepository repository, ObjectifCommercialMapper mapper)`
  - `public ObjectifCommercialResponse executer(DefinirObjectifRequest request, Utilisateur acteur)`

#### `modules\comptabilite\application\prime\dto\response\PrimeResponse.java`

**record** `PrimeResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referencePrime`
  - `String matriculeCommercial`
  - `LocalDate semaineDebut`
  - `LocalDate semaineFin`
  - `double objectifGlobalFCFA`
  - `double totalVentesGlobalFCFA`
  - `double tauxRealisationGlobalPourcent`
  - `Map<String`
  - `Double> objectifsParGammeFCFA`
  - `Map<String`
  - `Double> realisationsParGammeFCFA`
  - `Map<String`
  - `Double> tauxParGammePourcent`
  - `boolean eligible`
  - `double montantPrimeFCFA`
  - `String statut`
  - `String matriculeValidateur`

#### `modules\comptabilite\application\prime\mapper\PrimeMapper.java`

**class** `PrimeMapper` (@Component
public)

- **Méthodes :**
  - `public PrimeResponse versResponse(PrimeCommerciale p)`

#### `modules\comptabilite\application\rapport\usecase\GenererRapportDecaissementsUseCase.java`

**class** `GenererRapportDecaissementsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final DecaissementRepository decaissementRepository`
  - `private final DecaissementMapper decaissementMapper`
  - `private final TableExporter tableExporter`
  - `String titre = ""`

- **Méthodes :**
  - `public GenererRapportDecaissementsUseCase(DecaissementRepository decaissementRepository,
                                                DecaissementMapper decaissementMapper,
                                                TableExporter tableExporter)`
  - `public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format)`
  - `private List<List<String>> lignes(List<DecaissementResponse> decaissements)`

#### `modules\comptabilite\application\rapport\usecase\GenererRapportFacturesUseCase.java`

**class** `GenererRapportFacturesUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final FactureRepository factureRepository`
  - `private final FactureMapper factureMapper`
  - `private final TableExporter tableExporter`
  - `String titre = ""`

- **Méthodes :**
  - `public GenererRapportFacturesUseCase(FactureRepository factureRepository, FactureMapper factureMapper,
                                         TableExporter tableExporter)`
  - `public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format)`
  - `private List<List<String>> lignes(List<FactureResponse> factures)`

#### `modules\comptabilite\application\rapport\usecase\GenererRapportPrimesUseCase.java`

**class** `GenererRapportPrimesUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final PrimeRepository primeRepository`
  - `private final PrimeMapper primeMapper`
  - `private final TableExporter tableExporter`
  - `String titre = "" + semaineDebut`
  - `String base = "" + semaineDebut`

- **Méthodes :**
  - `public GenererRapportPrimesUseCase(PrimeRepository primeRepository,
                                         PrimeMapper primeMapper,
                                         TableExporter tableExporter)`
  - `public RapportFichier executer(LocalDate semaineDebut, FormatRapport format)`
  - `private List<List<String>> lignes(List<PrimeResponse> primes)`

#### `modules\comptabilite\application\rapport\usecase\GenererRapportVersementsUseCase.java`

**class** `GenererRapportVersementsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final VersementRepository versementRepository`
  - `private final VersementMapper versementMapper`
  - `private final TableExporter tableExporter`
  - `String titre = ""`

- **Méthodes :**
  - `public GenererRapportVersementsUseCase(VersementRepository versementRepository,
                                             VersementMapper versementMapper,
                                             TableExporter tableExporter)`
  - `public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format)`
  - `private List<List<String>> lignes(List<VersementResponse> versements)`

#### `modules\comptabilite\application\versement\command\EnregistrerVersementCommand.java`

**record** `EnregistrerVersementCommand` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `Double montantAttendu`
  - `double cashVerse`
  - `TypeVersement typeVersement`
  - `LocalDate date`

#### `modules\comptabilite\application\versement\command\JustifierEcartCommand.java`

**record** `JustifierEcartCommand` (public)

- **Composants (record) :**
  - `String referenceVersement`
  - `String justification`

#### `modules\comptabilite\application\versement\command\ValiderVersementCommand.java`

**record** `ValiderVersementCommand` (public)

- **Composants (record) :**
  - `String referenceVersement`
  - `String matriculeValidateur`
  - `String codeOtp`

#### `modules\comptabilite\application\versement\dto\response\VersementResponse.java`

**record** `VersementResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referenceVersement`
  - `String matriculeCommercial`
  - `double montantAttendu`
  - `double cashVerse`
  - `double ecart`
  - `boolean alerteRouge`
  - `String typeVersement`
  - `LocalDate date`
  - `String statut`
  - `String justificationEcart`
  - `String matriculeValidateur`

#### `modules\comptabilite\application\versement\mapper\VersementMapper.java`

**class** `VersementMapper` (@Component
public)

- **Méthodes :**
  - `public VersementResponse versResponse(Versement v)`

#### `modules\comptabilite\application\versement\service\ReconciliationService.java`

**class** `ReconciliationService` (@Service
public)

- **Champs :**
  - `private final VenteRepository venteRepository`
  - `private final BonRetourRepository bonRetourRepository`
  - `private final GrilleTarifaireRepository grilleTarifaireRepository`
  - `double valeurRetoursFCFA = 0.0`
  - `boolean produitsSansPrixHomologue = false`

- **Méthodes :**
  - `public ReconciliationService(VenteRepository venteRepository, BonRetourRepository bonRetourRepository,
                                  GrilleTarifaireRepository grilleTarifaireRepository)`
  - `public ReconciliationResultat calculer(String matriculeCommercial, LocalDate date)`
  - `public record ReconciliationResultat(String matriculeCommercial,
        LocalDate date,
        double caDeclareFCFA,
        double valeurRetoursValidesFCFA,
        double montantAttenduFCFA,
        boolean avertissementPrixHomologueManquant)`

**record** `ReconciliationResultat` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `LocalDate date`
  - `double caDeclareFCFA`
  - `double valeurRetoursValidesFCFA`
  - `double montantAttenduFCFA`
  - `boolean avertissementPrixHomologueManquant`

#### `modules\comptabilite\application\versement\usecase\enregistrer\EnregistrerVersementUseCase.java`

**class** `EnregistrerVersementUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final VersementRepository versementRepository`
  - `private final VersementMapper versementMapper`
  - `private final DomainEventPublisher eventPublisher`
  - `private final ReconciliationService reconciliationService`

- **Méthodes :**
  - `public EnregistrerVersementUseCase(VersementRepository versementRepository, VersementMapper versementMapper,
                                       DomainEventPublisher eventPublisher, ReconciliationService reconciliationService)`
  - `public VersementResponse executer(EnregistrerVersementCommand cmd)`

#### `modules\comptabilite\application\versement\usecase\valider\ValiderVersementUseCase.java`

**class** `ValiderVersementUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final VersementRepository versementRepository`
  - `private final VersementMapper versementMapper`
  - `private final DomainEventPublisher eventPublisher`
  - `private final OtpService otpService`

- **Méthodes :**
  - `public ValiderVersementUseCase(VersementRepository versementRepository, VersementMapper versementMapper,
                                   DomainEventPublisher eventPublisher, OtpService otpService)`
  - `public VersementResponse executer(ValiderVersementCommand cmd)`

#### `modules\comptabilite\domain\caisse\aggregate\JournalCaisse.java`

**class** `JournalCaisse` (public)

- **Champs :**
  - `private static final double SEUIL_SECURISATION_DEFAUT = 500_000.0`
  - `private Long id`
  - `private final LocalDate date`
  - `private double soldeCourant`
  - `private final double seuilSecurisation`
  - `private final List<LigneCaisse> lignes`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private JournalCaisse(LocalDate date, double soldeInitial, double seuilSecurisation,
                           LocalDateTime dateCreation)`
  - `public static JournalCaisse ouvrir(LocalDate date, double soldeInitial)`
  - `public static JournalCaisse ouvrirAvecSeuil(LocalDate date, double soldeInitial, double seuilSecurisation)`
  - `public static JournalCaisse reconstituer(Long id, LocalDate date, double soldeCourant,
                                              double seuilSecurisation, List<LigneCaisse> lignes,
                                              LocalDateTime dateCreation, LocalDateTime dateModification,
                                              Long version)`
  - `public void enregistrerEntree(String referenceOperation, double montant, String motif,
                                   String matriculeOperateur)`
  - `public void enregistrerSortie(String referenceOperation, double montant, String motif,
                                   String matriculeOperateur)`
  - `public double getTotalEntrees()`
  - `public double getTotalSorties()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public LocalDate getDate()`
  - `public double getSoldeCourant()`
  - `public double getSeuilSecurisation()`
  - `public List<LigneCaisse> getLignes()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\comptabilite\domain\caisse\entity\LigneCaisse.java`

**class** `LigneCaisse` (public)

- **Champs :**
  - `private final String referenceOperation`
  - `private final TypeMouvementCaisse typeMouvement`
  - `private final double montant`
  - `private final String motif`
  - `private final LocalDateTime dateOperation`
  - `private final String matriculeOperateur`

- **Méthodes :**
  - `public LigneCaisse(String referenceOperation, TypeMouvementCaisse typeMouvement,
                        double montant, String motif, LocalDateTime dateOperation,
                        String matriculeOperateur)`
  - `public String getReferenceOperation()`
  - `public TypeMouvementCaisse getTypeMouvement()`
  - `public double getMontant()`
  - `public String getMotif()`
  - `public LocalDateTime getDateOperation()`
  - `public String getMatriculeOperateur()`

#### `modules\comptabilite\domain\caisse\enums\TypeMouvementCaisse.java`

**enum** `TypeMouvementCaisse` (public)

#### `modules\comptabilite\domain\caisse\events\SeuilSecurisationAtteintEvent.java`

**class** `SeuilSecurisationAtteintEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final LocalDate date`
  - `private final double soldeCaisse`
  - `private final double seuilSecurisation`

- **Méthodes :**
  - `public SeuilSecurisationAtteintEvent(LocalDate date, double soldeCaisse, double seuilSecurisation)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public LocalDate getDate()`
  - `public double getSoldeCaisse()`
  - `public double getSeuilSecurisation()`

#### `modules\comptabilite\domain\caisse\repository\JournalCaisseRepository.java`

**interface** `JournalCaisseRepository` (public)

#### `modules\comptabilite\domain\caisse\specification\CaisseSpecification.java`

**class** `CaisseSpecification` (public final)

- **Méthodes :**
  - `private CaisseSpecification()`
  - `public static boolean peutOperer(Role role)`
  - `public static boolean peutConsulter(Role role)`

#### `modules\comptabilite\domain\decaissement\aggregate\Decaissement.java`

**class** `Decaissement` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroDecaissement`
  - `private final TypeDepense typeDepense`
  - `private final String matriculeProposant`
  - `private String matriculeValidateurCG`
  - `private String matriculeValidateurDG`
  - `private String matriculeExecuteur`
  - `private final double montantFCFA`
  - `private final String beneficiaire`
  - `private final LocalDate dateDepense`
  - `private String motif`
  - `private String motifAnnulation`
  - `private StatutDecaissement statut`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private Decaissement(String numeroDecaissement, TypeDepense typeDepense, String matriculeProposant,
                         double montantFCFA, String beneficiaire, LocalDate dateDepense,
                         String motif, LocalDateTime dateCreation)`
  - `public static Decaissement proposer(String numeroDecaissement, TypeDepense typeDepense,
                                         String matriculeProposant, double montantFCFA,
                                         String beneficiaire, LocalDate dateDepense, String motif)`
  - `public static Decaissement reconstituer(Long id, String numeroDecaissement, TypeDepense typeDepense,
                                             String matriculeProposant, String matriculeValidateurCG,
                                             String matriculeValidateurDG, String matriculeExecuteur,
                                             double montantFCFA, String beneficiaire, LocalDate dateDepense,
                                             String motif, String motifAnnulation, StatutDecaissement statut,
                                             LocalDateTime dateCreation, LocalDateTime dateModification, Long version)`
  - `public void validerParDG(String matriculeDG)`
  - `public void approuverParDG(String matriculeDG)`
  - `public void executer(String matriculeExecuteur)`
  - `public void annuler(String motif, String matricule)`
  - `public Long getId()`
  - `public String getNumeroDecaissement()`
  - `public TypeDepense getTypeDepense()`
  - `public String getMatriculeProposant()`
  - `public String getMatriculeValidateurCG()`
  - `public String getMatriculeValidateurDG()`
  - `public String getMatriculeExecuteur()`
  - `public double getMontantFCFA()`
  - `public String getBeneficiaire()`
  - `public LocalDate getDateDepense()`
  - `public String getMotif()`
  - `public String getMotifAnnulation()`
  - `public StatutDecaissement getStatut()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\comptabilite\domain\decaissement\enums\StatutDecaissement.java`

**enum** `StatutDecaissement` (public)

#### `modules\comptabilite\domain\decaissement\enums\TypeDepense.java`

**enum** `TypeDepense` (public)

#### `modules\comptabilite\domain\decaissement\repository\DecaissementRepository.java`

**interface** `DecaissementRepository` (public)

#### `modules\comptabilite\domain\facture\aggregate\Facture.java`

**class** `Facture` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroFacture`
  - `private final String codeClient`
  - `private final TypeFacture typeFacture`
  - `private final String referenceFactureOrigine`
  - `private final LocalDate dateEmission`
  - `private final List<LigneFacture> lignes`
  - `private StatutFacture statut`
  - `private String matriculeEmetteur`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private Facture(String numeroFacture, String codeClient, TypeFacture typeFacture,
                    String referenceFactureOrigine, LocalDate dateEmission,
                    List<LigneFacture> lignes, String matriculeEmetteur, LocalDateTime dateCreation)`
  - `public static Facture emettre(String numeroFacture, String codeClient, TypeFacture typeFacture,
                                   String referenceFactureOrigine, LocalDate dateEmission,
                                   List<LigneFacture> lignes, String matriculeEmetteur)`
  - `public static Facture reconstituer(Long id, String numeroFacture, String codeClient,
                                        TypeFacture typeFacture, String referenceFactureOrigine,
                                        LocalDate dateEmission, List<LigneFacture> lignes,
                                        StatutFacture statut, String matriculeEmetteur,
                                        LocalDateTime dateCreation, LocalDateTime dateModification,
                                        Long version)`
  - `public void marquerPayee()`
  - `public void annuler()`
  - `public double getMontantTotalHT()`
  - `public double getMontantTotalTVA()`
  - `public double getMontantTotalTTC()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumeroFacture()`
  - `public String getCodeClient()`
  - `public TypeFacture getTypeFacture()`
  - `public String getReferenceFactureOrigine()`
  - `public LocalDate getDateEmission()`
  - `public List<LigneFacture> getLignes()`
  - `public StatutFacture getStatut()`
  - `public String getMatriculeEmetteur()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\comptabilite\domain\facture\entity\LigneFacture.java`

**class** `LigneFacture` (public)

- **Champs :**
  - `private final String referenceArticle`
  - `private final String designation`
  - `private final int quantite`
  - `private final double prixUnitaireHT`
  - `private final double tauxTVA`
  - `private final double remise`

- **Méthodes :**
  - `public LigneFacture(String referenceArticle, String designation, int quantite,
                         double prixUnitaireHT, double tauxTVA, double remise)`
  - `public double getMontantHT()`
  - `public double getMontantTVA()`
  - `public double getMontantTTC()`
  - `public String getReferenceArticle()`
  - `public String getDesignation()`
  - `public int getQuantite()`
  - `public double getPrixUnitaireHT()`
  - `public double getTauxTVA()`
  - `public double getRemise()`

#### `modules\comptabilite\domain\facture\enums\StatutFacture.java`

**enum** `StatutFacture` (public)

#### `modules\comptabilite\domain\facture\enums\TypeFacture.java`

**enum** `TypeFacture` (public)

#### `modules\comptabilite\domain\facture\events\FactureEmiseEvent.java`

**class** `FactureEmiseEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numeroFacture`
  - `private final String codeClient`
  - `private final double montantTTC`

- **Méthodes :**
  - `public FactureEmiseEvent(String numeroFacture, String codeClient, double montantTTC)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumeroFacture()`
  - `public String getCodeClient()`
  - `public double getMontantTTC()`

#### `modules\comptabilite\domain\facture\repository\FactureRepository.java`

**interface** `FactureRepository` (public)

#### `modules\comptabilite\domain\facture\specification\FactureSpecification.java`

**class** `FactureSpecification` (public final)

- **Méthodes :**
  - `private FactureSpecification()`
  - `public static boolean peutEmettre(Role role)`
  - `public static boolean peutAnnuler(Role role)`

#### `modules\comptabilite\domain\objectif\aggregate\ObjectifCommercial.java`

**class** `ObjectifCommercial` (public)

- **Champs :**
  - `private Long id`
  - `private final String matriculeCommercial`
  - `private final LocalDate semaineDebut`
  - `private final LocalDate semaineFin`
  - `private double objectifGlobalFCFA`
  - `private final Map<String, Double> objectifsParGammeFCFA`
  - `private String matriculeAuteur`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private ObjectifCommercial(String matriculeCommercial, LocalDate semaineDebut, LocalDate semaineFin,
                                double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA,
                                String matriculeAuteur, LocalDateTime dateCreation)`
  - `public static ObjectifCommercial definir(String matriculeCommercial, LocalDate semaineDebut, LocalDate semaineFin,
                                              double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA,
                                              String matriculeAuteur)`
  - `public static ObjectifCommercial reconstituer(Long id, String matriculeCommercial, LocalDate semaineDebut,
                                                   LocalDate semaineFin, double objectifGlobalFCFA,
                                                   Map<String, Double> objectifsParGammeFCFA, String matriculeAuteur,
                                                   LocalDateTime dateCreation, LocalDateTime dateModification,
                                                   Long version)`
  - `public void reviser(double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA, String matriculeAuteur)`
  - `public Long getId()`
  - `public String getMatriculeCommercial()`
  - `public LocalDate getSemaineDebut()`
  - `public LocalDate getSemaineFin()`
  - `public double getObjectifGlobalFCFA()`
  - `public Map<String, Double> getObjectifsParGammeFCFA()`
  - `public String getMatriculeAuteur()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\comptabilite\domain\objectif\repository\ObjectifCommercialRepository.java`

**interface** `ObjectifCommercialRepository` (public)

#### `modules\comptabilite\domain\prime\aggregate\PrimeCommerciale.java`

**class** `PrimeCommerciale` (public)

- **Champs :**
  - `private static final double SEUIL_GLOBAL_POURCENT = 80.0`
  - `private static final double SEUIL_GAMME_POURCENT = 75.0`
  - `private Long id`
  - `private final String referencePrime`
  - `private final String matriculeCommercial`
  - `private final LocalDate semaineDebut`
  - `private final LocalDate semaineFin`
  - `private final double objectifGlobalFCFA`
  - `private final double totalVentesGlobalFCFA`
  - `private final double tauxRealisationGlobalPourcent`
  - `private final Map<String, Double> objectifsParGammeFCFA`
  - `private final Map<String, Double> realisationsParGammeFCFA`
  - `private final Map<String, Double> tauxParGammePourcent`
  - `private final boolean eligible`
  - `private final double montantPrimeFCFA`
  - `private StatutPrime statut`
  - `private String matriculeValidateur`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`
  - `boolean toutesGammesOk = true`
  - `double tauxGamme = objectifGamme > 0 ? (realiseGamme / objectifGamme) * 100.0 : 100.0`

- **Méthodes :**
  - `private PrimeCommerciale(String referencePrime, String matriculeCommercial, LocalDate semaineDebut,
                              LocalDate semaineFin, double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                              Map<String, Double> objectifsParGammeFCFA, Map<String, Double> realisationsParGammeFCFA,
                              double montantPrimeSiEligibleFCFA, LocalDateTime dateCreation)`
  - `public static PrimeCommerciale calculer(String referencePrime, String matriculeCommercial,
                                             LocalDate semaineDebut, LocalDate semaineFin,
                                             double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                                             Map<String, Double> objectifsParGammeFCFA,
                                             Map<String, Double> realisationsParGammeFCFA,
                                             double montantPrimeSiEligibleFCFA)`
  - `public static PrimeCommerciale reconstituer(Long id, String referencePrime, String matriculeCommercial,
                                                 LocalDate semaineDebut, LocalDate semaineFin,
                                                 double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                                                 Map<String, Double> objectifsParGammeFCFA,
                                                 Map<String, Double> realisationsParGammeFCFA,
                                                 double montantPrimeFCFA, StatutPrime statut,
                                                 String matriculeValidateur, LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version)`
  - `public void valider(String matriculeValidateur)`
  - `public void marquerVersee()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getReferencePrime()`
  - `public String getMatriculeCommercial()`
  - `public LocalDate getSemaineDebut()`
  - `public LocalDate getSemaineFin()`
  - `public double getObjectifGlobalFCFA()`
  - `public double getTotalVentesGlobalFCFA()`
  - `public double getTauxRealisationGlobalPourcent()`
  - `public Map<String, Double> getObjectifsParGammeFCFA()`
  - `public Map<String, Double> getRealisationsParGammeFCFA()`
  - `public Map<String, Double> getTauxParGammePourcent()`
  - `public boolean isEligible()`
  - `public double getMontantPrimeFCFA()`
  - `public StatutPrime getStatut()`
  - `public String getMatriculeValidateur()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\comptabilite\domain\prime\enums\StatutPrime.java`

**enum** `StatutPrime` (public)

#### `modules\comptabilite\domain\prime\events\PrimeValideeEvent.java`

**class** `PrimeValideeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String referencePrime`
  - `private final String matriculeCommercial`
  - `private final double montantPrime`

- **Méthodes :**
  - `public PrimeValideeEvent(String referencePrime, String matriculeCommercial, double montantPrime)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReferencePrime()`
  - `public String getMatriculeCommercial()`
  - `public double getMontantPrime()`

#### `modules\comptabilite\domain\prime\repository\PrimeRepository.java`

**interface** `PrimeRepository` (public)

#### `modules\comptabilite\domain\prime\specification\PrimeSpecification.java`

**class** `PrimeSpecification` (public final)

- **Méthodes :**
  - `private PrimeSpecification()`
  - `public static boolean peutCalculer(Role role)`
  - `public static boolean peutValider(Role role)`
  - `public static boolean peutVerser(Role role)`

#### `modules\comptabilite\domain\versement\aggregate\Versement.java`

**class** `Versement` (public)

- **Champs :**
  - `private static final double SEUIL_ALERTE_ROUGE = 5000.0`
  - `private Long id`
  - `private final String referenceVersement`
  - `private final String matriculeCommercial`
  - `private final double montantAttendu`
  - `private final double cashVerse`
  - `private final double ecart`
  - `private final boolean alerteRouge`
  - `private final TypeVersement typeVersement`
  - `private final LocalDate date`
  - `private StatutVersement statut`
  - `private String justificationEcart`
  - `private String matriculeValidateur`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`
  - `cashVerse < montantAttendu`

- **Méthodes :**
  - `private Versement(String referenceVersement, String matriculeCommercial, double montantAttendu,
                       double cashVerse, TypeVersement typeVersement, LocalDate date,
                       LocalDateTime dateCreation)`
  - `public static Versement enregistrer(String referenceVersement, String matriculeCommercial,
                                         double montantAttendu, double cashVerse,
                                         TypeVersement typeVersement, LocalDate date)`
  - `public static Versement reconstituer(Long id, String referenceVersement, String matriculeCommercial,
                                          double montantAttendu, double cashVerse, TypeVersement typeVersement,
                                          LocalDate date, StatutVersement statut,
                                          String justificationEcart, String matriculeValidateur,
                                          LocalDateTime dateCreation, LocalDateTime dateModification,
                                          Long version)`
  - `public void valider(String matriculeValidateur)`
  - `public void justifierEcart(String justification)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getReferenceVersement()`
  - `public String getMatriculeCommercial()`
  - `public double getMontantAttendu()`
  - `public double getCashVerse()`
  - `public double getEcart()`
  - `public boolean isAlerteRouge()`
  - `public TypeVersement getTypeVersement()`
  - `public LocalDate getDate()`
  - `public StatutVersement getStatut()`
  - `public String getJustificationEcart()`
  - `public String getMatriculeValidateur()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\comptabilite\domain\versement\enums\StatutVersement.java`

**enum** `StatutVersement` (public)

#### `modules\comptabilite\domain\versement\enums\TypeVersement.java`

**enum** `TypeVersement` (public)

#### `modules\comptabilite\domain\versement\events\AlerteRougeVersementEvent.java`

**class** `AlerteRougeVersementEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String referenceVersement`
  - `private final String matriculeCommercial`
  - `private final double montantAttendu`
  - `private final double cashVerse`
  - `private final double ecart`

- **Méthodes :**
  - `public AlerteRougeVersementEvent(String referenceVersement, String matriculeCommercial,
                                      double montantAttendu, double cashVerse, double ecart)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReferenceVersement()`
  - `public String getMatriculeCommercial()`
  - `public double getMontantAttendu()`
  - `public double getCashVerse()`
  - `public double getEcart()`

#### `modules\comptabilite\domain\versement\events\VersementValideEvent.java`

**class** `VersementValideEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String referenceVersement`
  - `private final String matriculeCommercial`
  - `private final double montantValide`

- **Méthodes :**
  - `public VersementValideEvent(String referenceVersement, String matriculeCommercial, double montantValide)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReferenceVersement()`
  - `public String getMatriculeCommercial()`
  - `public double getMontantValide()`

#### `modules\comptabilite\domain\versement\repository\VersementRepository.java`

**interface** `VersementRepository` (public)

#### `modules\comptabilite\domain\versement\specification\VersementSpecification.java`

**class** `VersementSpecification` (public final)

- **Méthodes :**
  - `private VersementSpecification()`
  - `public static boolean peutEnregistrer(Role role)`
  - `public static boolean peutValider(Role role)`
  - `public static boolean peutJustifier(Role role)`

#### `modules\comptabilite\infrastructure\persistence\caisse\jpa\JournalCaisseJpaEntity.java`

**class** `JournalCaisseJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private LocalDate date`
  - `@Column(nullable = false) private double soldeCourant`
  - `@Column(nullable = false) private double seuilSecurisation`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "") private List<LigneCaisseJpaEntity> lignes`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\comptabilite\infrastructure\persistence\caisse\jpa\JournalCaisseJpaRepository.java`

**interface** `JournalCaisseJpaRepository` (public) extends `JpaRepository<JournalCaisseJpaEntity, Long>`

#### `modules\comptabilite\infrastructure\persistence\caisse\jpa\LigneCaisseJpaEntity.java`

**class** `LigneCaisseJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String referenceOperation`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private TypeMouvementCaisse typeMouvement`
  - `@Column(nullable = false) private double montant`
  - `@Column(nullable = false) private String motif`
  - `@Column(nullable = false) private LocalDateTime dateOperation`
  - `@Column(nullable = false) private String matriculeOperateur`

#### `modules\comptabilite\infrastructure\persistence\caisse\repository\JournalCaisseRepositoryImpl.java`

**class** `JournalCaisseRepositoryImpl` (@Repository
public) implements `JournalCaisseRepository`

- **Champs :**
  - `private final JournalCaisseJpaRepository jpaRepository`

- **Méthodes :**
  - `public JournalCaisseRepositoryImpl(JournalCaisseJpaRepository jpaRepository)`
  - `@Override public JournalCaisse sauvegarder(JournalCaisse journal)`
  - `@Override public Optional<JournalCaisse> trouverParDate(LocalDate date)`
  - `@Override public List<JournalCaisse> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public boolean existeParDate(LocalDate date)`
  - `private JournalCaisseJpaEntity toEntity(JournalCaisse j)`
  - `private JournalCaisse toDomain(JournalCaisseJpaEntity e)`

#### `modules\comptabilite\infrastructure\persistence\decaissement\jpa\DecaissementJpaEntity.java`

**class** `DecaissementJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numeroDecaissement`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private TypeDepense typeDepense`
  - `@Column(nullable = false) private String matriculeProposant`
  - `private String matriculeValidateurCG`
  - `private String matriculeValidateurDG`
  - `private String matriculeExecuteur`
  - `@Column(nullable = false) private double montantFCFA`
  - `@Column(nullable = false) private String beneficiaire`
  - `@Column(nullable = false) private LocalDate dateDepense`
  - `@Column(columnDefinition = "") private String motif`
  - `@Column(columnDefinition = "") private String motifAnnulation`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutDecaissement statut`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\comptabilite\infrastructure\persistence\decaissement\jpa\DecaissementJpaRepository.java`

**interface** `DecaissementJpaRepository` (public) extends `JpaRepository<DecaissementJpaEntity, Long>`

#### `modules\comptabilite\infrastructure\persistence\decaissement\repository\DecaissementRepositoryImpl.java`

**class** `DecaissementRepositoryImpl` (@Repository
public) implements `DecaissementRepository`

- **Champs :**
  - `private final DecaissementJpaRepository jpaRepository`

- **Méthodes :**
  - `public DecaissementRepositoryImpl(DecaissementJpaRepository jpaRepository)`
  - `@Override public Decaissement sauvegarder(Decaissement d)`
  - `@Override public Optional<Decaissement> trouverParNumero(String numero)`
  - `@Override public List<Decaissement> listerParStatut(StatutDecaissement statut)`
  - `@Override public List<Decaissement> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public List<Decaissement> listerTous()`
  - `@Override public double totalDecaissementsParPeriode(LocalDate debut, LocalDate fin)`
  - `private DecaissementJpaEntity toEntity(Decaissement d)`
  - `private Decaissement toDomain(DecaissementJpaEntity e)`

#### `modules\comptabilite\infrastructure\persistence\facture\jpa\FactureJpaEntity.java`

**class** `FactureJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numeroFacture`
  - `@Column(nullable = false) private String codeClient`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private TypeFacture typeFacture`
  - `private String referenceFactureOrigine`
  - `@Column(nullable = false) private LocalDate dateEmission`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "") private List<LigneFactureJpaEntity> lignes`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutFacture statut`
  - `@Column(nullable = false) private String matriculeEmetteur`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\comptabilite\infrastructure\persistence\facture\jpa\FactureJpaRepository.java`

**interface** `FactureJpaRepository` (public) extends `JpaRepository<FactureJpaEntity, Long>`

#### `modules\comptabilite\infrastructure\persistence\facture\jpa\LigneFactureJpaEntity.java`

**class** `LigneFactureJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String referenceArticle`
  - `@Column(nullable = false) private String designation`
  - `@Column(nullable = false) private int quantite`
  - `@Column(nullable = false) private double prixUnitaireHT`
  - `@Column(nullable = false) private double tauxTVA`
  - `@Column(nullable = false) private double remise`

#### `modules\comptabilite\infrastructure\persistence\facture\repository\FactureRepositoryImpl.java`

**class** `FactureRepositoryImpl` (@Repository
public) implements `FactureRepository`

- **Champs :**
  - `private final FactureJpaRepository jpaRepository`

- **Méthodes :**
  - `public FactureRepositoryImpl(FactureJpaRepository jpaRepository)`
  - `@Override public Facture sauvegarder(Facture facture)`
  - `@Override public Optional<Facture> trouverParNumero(String numeroFacture)`
  - `@Override public List<Facture> listerParClient(String codeClient)`
  - `@Override public List<Facture> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public List<Facture> listerParStatut(StatutFacture statut)`
  - `@Override public List<Facture> listerParType(TypeFacture typeFacture)`
  - `@Override public String genererNumero(TypeFacture typeFacture, LocalDate date)`
  - `@Override public Page<Facture> listerParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable)`
  - `@Override public Page<Facture> listerParStatutPagine(StatutFacture statut, Pageable pageable)`
  - `@Override public Page<Facture> listerParClientPagine(String codeClient, Pageable pageable)`
  - `private FactureJpaEntity toEntity(Facture f)`
  - `private Facture toDomain(FactureJpaEntity e)`

#### `modules\comptabilite\infrastructure\persistence\objectif\jpa\ObjectifCommercialJpaEntity.java`

**class** `ObjectifCommercialJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeCommercial`
  - `@Column(name = "", nullable = false) private LocalDate semaineDebut`
  - `@Column(name = "", nullable = false) private LocalDate semaineFin`
  - `@Column(name = "", nullable = false) private double objectifGlobalFCFA`
  - `@MapKeyColumn(name = "")
    @Column(name = "") private Map<String, Double> objectifsParGammeFCFA = new HashMap<>()`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeAuteur`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\comptabilite\infrastructure\persistence\objectif\jpa\ObjectifCommercialJpaRepository.java`

**interface** `ObjectifCommercialJpaRepository` (public) extends `JpaRepository<ObjectifCommercialJpaEntity, Long>`

#### `modules\comptabilite\infrastructure\persistence\objectif\repository\ObjectifCommercialRepositoryImpl.java`

**class** `ObjectifCommercialRepositoryImpl` (@Repository
public) implements `ObjectifCommercialRepository`

- **Champs :**
  - `private final ObjectifCommercialJpaRepository jpaRepository`

- **Méthodes :**
  - `public ObjectifCommercialRepositoryImpl(ObjectifCommercialJpaRepository jpaRepository)`
  - `@Override public ObjectifCommercial sauvegarder(ObjectifCommercial objectif)`
  - `@Override public Optional<ObjectifCommercial> trouver(String matriculeCommercial, LocalDate semaineDebut)`
  - `@Override public List<ObjectifCommercial> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<ObjectifCommercial> listerParSemaine(LocalDate semaineDebut)`
  - `private ObjectifCommercialJpaEntity toEntity(ObjectifCommercial o)`
  - `private ObjectifCommercial toDomain(ObjectifCommercialJpaEntity e)`

#### `modules\comptabilite\infrastructure\persistence\prime\jpa\PrimeJpaEntity.java`

**class** `PrimeJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String referencePrime`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private LocalDate semaineDebut`
  - `@Column(nullable = false) private LocalDate semaineFin`
  - `@Column(nullable = false) private double objectifGlobalFCFA`
  - `@Column(nullable = false) private double totalVentesGlobalFCFA`
  - `@MapKeyColumn(name = "")
    @Column(name = "") private Map<String, Double> objectifsParGammeFCFA = new HashMap<>()`
  - `@MapKeyColumn(name = "")
    @Column(name = "") private Map<String, Double> realisationsParGammeFCFA = new HashMap<>()`
  - `@Column(nullable = false) private double montantPrimeFCFA`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutPrime statut`
  - `private String matriculeValidateur`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\comptabilite\infrastructure\persistence\prime\jpa\PrimeJpaRepository.java`

**interface** `PrimeJpaRepository` (public) extends `JpaRepository<PrimeJpaEntity, Long>`

#### `modules\comptabilite\infrastructure\persistence\prime\repository\PrimeRepositoryImpl.java`

**class** `PrimeRepositoryImpl` (@Repository
public) implements `PrimeRepository`

- **Champs :**
  - `private final PrimeJpaRepository jpaRepository`

- **Méthodes :**
  - `public PrimeRepositoryImpl(PrimeJpaRepository jpaRepository)`
  - `@Override public PrimeCommerciale sauvegarder(PrimeCommerciale prime)`
  - `@Override public Optional<PrimeCommerciale> trouverParReference(String referencePrime)`
  - `@Override public List<PrimeCommerciale> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<PrimeCommerciale> listerParStatut(StatutPrime statut)`
  - `@Override public List<PrimeCommerciale> listerParSemaine(LocalDate semaineDebut)`
  - `@Override public String genererReference(String matriculeCommercial, LocalDate semaineDebut)`
  - `private PrimeJpaEntity toEntity(PrimeCommerciale p)`
  - `private PrimeCommerciale toDomain(PrimeJpaEntity e)`

#### `modules\comptabilite\infrastructure\persistence\versement\jpa\VersementJpaEntity.java`

**class** `VersementJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String referenceVersement`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private double montantAttendu`
  - `@Column(nullable = false) private double cashVerse`
  - `@Column(nullable = false) private double ecart`
  - `@Column(nullable = false) private boolean alerteRouge`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private TypeVersement typeVersement`
  - `@Column(nullable = false) private LocalDate date`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutVersement statut`
  - `@Column(columnDefinition = "") private String justificationEcart`
  - `private String matriculeValidateur`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\comptabilite\infrastructure\persistence\versement\jpa\VersementJpaRepository.java`

**interface** `VersementJpaRepository` (public) extends `JpaRepository<VersementJpaEntity, Long>`

#### `modules\comptabilite\infrastructure\persistence\versement\repository\VersementRepositoryImpl.java`

**class** `VersementRepositoryImpl` (@Repository
public) implements `VersementRepository`

- **Champs :**
  - `private final VersementJpaRepository jpaRepository`

- **Méthodes :**
  - `public VersementRepositoryImpl(VersementJpaRepository jpaRepository)`
  - `@Override public Versement sauvegarder(Versement versement)`
  - `@Override public Optional<Versement> trouverParReference(String referenceVersement)`
  - `@Override public List<Versement> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<Versement> listerParDate(LocalDate date)`
  - `@Override public List<Versement> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public List<Versement> listerParStatut(StatutVersement statut)`
  - `@Override public List<Versement> listerAlertesRouges()`
  - `@Override public String genererReference(LocalDate date)`
  - `private VersementJpaEntity toEntity(Versement v)`
  - `private Versement toDomain(VersementJpaEntity e)`

#### `modules\comptabilite\presentation\controller\CaisseController.java`

**class** `CaisseController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final JournalCaisseRepository caisseRepository`
  - `private final CaisseMapper caisseMapper`
  - `private final DomainEventPublisher eventPublisher`

- **Méthodes :**
  - `public CaisseController(JournalCaisseRepository caisseRepository, CaisseMapper caisseMapper,
                          DomainEventPublisher eventPublisher)`
  - `@PostMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<CaisseResponse>> ouvrir(@RequestBody OuvrirCaisseRequest req)`
  - `public record OuvrirCaisseRequest(double soldeInitial, LocalDate date)`
  - `public record MouvementRequest(String referenceOperation, double montant, String motif, String matriculeOperateur)`

**record** `OuvrirCaisseRequest` (public)

- **Composants (record) :**
  - `double soldeInitial`
  - `LocalDate date`

**record** `MouvementRequest` (public)

- **Composants (record) :**
  - `String referenceOperation`
  - `double montant`
  - `String motif`
  - `String matriculeOperateur`

#### `modules\comptabilite\presentation\controller\DashboardComptabiliteController.java`

**class** `DashboardComptabiliteController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DashboardComptabiliteUseCase dashboardUseCase`

- **Méthodes :**
  - `public DashboardComptabiliteController(DashboardComptabiliteUseCase dashboardUseCase)`
  - `DashboardComptabilite>> dashboard()`

#### `modules\comptabilite\presentation\controller\DecaissementController.java`

**class** `DecaissementController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DecaissementRepository decaissementRepository`
  - `private final DecaissementMapper decaissementMapper`

- **Méthodes :**
  - `public DecaissementController(DecaissementRepository decaissementRepository, DecaissementMapper decaissementMapper)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<DecaissementResponse>> proposer(@RequestBody ProposerDecaissementRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<DecaissementResponse>> validerParDG(@PathVariable String numero,
                                                                            @RequestParam String matriculeDG)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<DecaissementResponse>> approuverParDG(@PathVariable String numero,
                                                                              @RequestParam String matriculeDG)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<DecaissementResponse>> executer(@PathVariable String numero,
                                                                       @RequestParam String matriculeExecuteur)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<DecaissementResponse>> annuler(@PathVariable String numero,
                                                                      @RequestParam String motif,
                                                                      @RequestParam String matricule)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<DecaissementResponse>> consulter(@PathVariable String numero)`
  - `@GetMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<List<DecaissementResponse>>> lister()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<DecaissementResponse>>> listerParStatut(@PathVariable StatutDecaissement statut)`
  - `private Decaissement trouverOuErreur(String numero)`
  - `public record ProposerDecaissementRequest(TypeDepense typeDepense, String matriculeProposant,
                                               double montantFCFA, String beneficiaire,
                                               LocalDate dateDepense, String motif)`

**record** `ProposerDecaissementRequest` (public)

- **Composants (record) :**
  - `TypeDepense typeDepense`
  - `String matriculeProposant`
  - `double montantFCFA`
  - `String beneficiaire`
  - `LocalDate dateDepense`
  - `String motif`

#### `modules\comptabilite\presentation\controller\FactureController.java`

**class** `FactureController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final FactureRepository factureRepository`
  - `private final FactureMapper factureMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public FactureController(FactureRepository factureRepository, FactureMapper factureMapper,
                              EventPublisher eventPublisher)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<FactureResponse>> emettre(@RequestBody EmettreFactureRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<FactureResponse>> marquerPayee(@PathVariable String numero)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<FactureResponse>> annuler(@PathVariable String numero)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<FactureResponse>> consulter(@PathVariable String numero)`
  - `public record EmettreFactureRequest(String codeClient, TypeFacture typeFacture, String referenceFactureOrigine,
                                        String matriculeEmetteur, List<LigneRequest> lignes)`
  - `public record LigneRequest(String referenceArticle, String designation, int quantite,
                               double prixUnitaireHT, double tauxTVA, double remise)`

**record** `EmettreFactureRequest` (public)

- **Composants (record) :**
  - `String codeClient`
  - `TypeFacture typeFacture`
  - `String referenceFactureOrigine`
  - `String matriculeEmetteur`
  - `List<LigneRequest> lignes`

**record** `LigneRequest` (public)

- **Composants (record) :**
  - `String referenceArticle`
  - `String designation`
  - `int quantite`
  - `double prixUnitaireHT`
  - `double tauxTVA`
  - `double remise`

#### `modules\comptabilite\presentation\controller\ObjectifCommercialController.java`

**class** `ObjectifCommercialController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DefinirObjectifUseCase definirObjectifUseCase`
  - `private final ObjectifCommercialRepository repository`
  - `private final ObjectifCommercialMapper mapper`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public ObjectifCommercialController(DefinirObjectifUseCase definirObjectifUseCase,
                                         ObjectifCommercialRepository repository,
                                         ObjectifCommercialMapper mapper,
                                         AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ObjectifCommercialResponse>> definir(@Valid @RequestBody DefinirObjectifRequest request)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<ObjectifCommercialResponse>>> parCommercial(@PathVariable String matricule)`

#### `modules\comptabilite\presentation\controller\PrimeController.java`

**class** `PrimeController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final PrimeRepository primeRepository`
  - `private final ObjectifCommercialRepository objectifCommercialRepository`
  - `private final PrimeMapper primeMapper`
  - `private final DomainEventPublisher eventPublisher`
  - `private final double montantPrimeHebdomadaireFCFA`

- **Méthodes :**
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PrimeResponse>> calculer(@RequestBody CalculerPrimeRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PrimeResponse>> valider(@PathVariable String reference,
                                                              @RequestParam String matriculeValidateur)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PrimeResponse>> marquerVersee(@PathVariable String reference)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PrimeResponse>> consulter(@PathVariable String reference)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<PrimeResponse>>> parCommercial(@PathVariable String matricule)`
  - `public record CalculerPrimeRequest(String matriculeCommercial, LocalDate semaineDebut,
                                       double totalVentesGlobalFCFA, Map<String, Double> realisationsParGammeFCFA)`

**record** `CalculerPrimeRequest` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `LocalDate semaineDebut`
  - `double totalVentesGlobalFCFA`
  - `Map<String`
  - `Double> realisationsParGammeFCFA`

#### `modules\comptabilite\presentation\controller\RapportComptabiliteController.java`

**class** `RapportComptabiliteController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final GenererRapportFacturesUseCase genererRapportFacturesUseCase`
  - `private final GenererRapportDecaissementsUseCase genererRapportDecaissementsUseCase`
  - `private final GenererRapportVersementsUseCase genererRapportVersementsUseCase`
  - `private final GenererRapportPrimesUseCase genererRapportPrimesUseCase`

- **Méthodes :**
  - `public RapportComptabiliteController(GenererRapportFacturesUseCase genererRapportFacturesUseCase,
                                          GenererRapportDecaissementsUseCase genererRapportDecaissementsUseCase,
                                          GenererRapportVersementsUseCase genererRapportVersementsUseCase,
                                          GenererRapportPrimesUseCase genererRapportPrimesUseCase)`

#### `modules\comptabilite\presentation\controller\VersementController.java`

**class** `VersementController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
public)

- **Champs :**
  - `private final EnregistrerVersementUseCase enregistrerVersementUseCase`
  - `private final ValiderVersementUseCase validerVersementUseCase`
  - `private final VersementRepository versementRepository`
  - `private final VersementMapper versementMapper`
  - `private final ReconciliationService reconciliationService`
  - `List<Versement> resultats`

- **Méthodes :**
  - `public VersementController(EnregistrerVersementUseCase enregistrerVersementUseCase,
                              ValiderVersementUseCase validerVersementUseCase,
                              VersementRepository versementRepository, VersementMapper versementMapper,
                              ReconciliationService reconciliationService)`
  - `@PostMapping
    @PreAuthorize("") public ResponseEntity<ApiResponse<VersementResponse>> enregistrer(@RequestBody EnregistrerVersementRequest req)`
  - `public ResponseEntity<ApiResponse<VersementResponse>> valider(@PathVariable String reference,
                                                                  @RequestParam String matriculeValidateur,
                                                                  @RequestParam String codeOtp)`
  - `@PatchMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<VersementResponse>> justifier(@PathVariable String reference,
                                                                    @RequestBody JustifierEcartCommand cmd)`
  - `@GetMapping("")
    @PreAuthorize("") public ResponseEntity<ApiResponse<List<VersementResponse>>> alertesRouges()`
  - `public record EnregistrerVersementRequest(String matriculeCommercial, Double montantAttendu, double cashVerse,
                                              TypeVersement typeVersement, LocalDate date)`

**record** `EnregistrerVersementRequest` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `Double montantAttendu`
  - `double cashVerse`
  - `TypeVersement typeVersement`
  - `LocalDate date`


### Module `controle`

#### `modules\controle\application\ControleDashboardService.java`

**class** `ControleDashboardService` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final DecaissementRepository decaissementRepository`
  - `private final VersementRepository versementRepository`
  - `private final RecouvrementRepository recouvrementRepository`
  - `private final BudgetJpaRepository budgetJpaRepository`
  - `private final VarianceJpaRepository varianceJpaRepository`
  - `private final MargeProduitJpaRepository margeProduitJpaRepository`
  - `private final AnomaliesService anomaliesService`

- **Méthodes :**
  - `public ControleDashboardService(DecaissementRepository decaissementRepository,
                                     VersementRepository versementRepository,
                                     RecouvrementRepository recouvrementRepository,
                                     BudgetJpaRepository budgetJpaRepository,
                                     VarianceJpaRepository varianceJpaRepository,
                                     MargeProduitJpaRepository margeProduitJpaRepository,
                                     AnomaliesService anomaliesService)`
  - `public ControleDashboardResponse dashboard()`
  - `public List<MargeProduitResponse> marges()`
  - `public List<BudgetResponse> budget()`
  - `public List<VarianceResponse> variances()`

#### `modules\controle\application\dto\ControleDashboardResponse.java`

**record** `ControleDashboardResponse` (public)

- **Composants (record) :**
  - `double tresorerie`
  - `int ecartsRouges`
  - `double ventesCredit`
  - `int margeMoyenne`
  - `double valoStock`
  - `double budgetEngage`
  - `double budgetTotal`
  - `int budgetDepasse`
  - `int decaissementsAttente`
  - `int anomaliesElevees`
  - `List<MargeProduitResponse> marges`
  - `List<BudgetResponse> budget`
  - `List<VarianceResponse> variances`

- **Méthodes :**
  - `public record MargeProduitResponse(String code, String designation, double prixVente, double coutRevient,
        double marge, int margePct, double margeTotale, int ventesMois)`
  - `public record BudgetResponse(String poste, double budget, double engage, double realise)`
  - `public record VarianceResponse(String poste, double standard, double reel, String unite)`

**record** `MargeProduitResponse` (public)

- **Composants (record) :**
  - `String code`
  - `String designation`
  - `double prixVente`
  - `double coutRevient`
  - `double marge`
  - `int margePct`
  - `double margeTotale`
  - `int ventesMois`

**record** `BudgetResponse` (public)

- **Composants (record) :**
  - `String poste`
  - `double budget`
  - `double engage`
  - `double realise`

**record** `VarianceResponse` (public)

- **Composants (record) :**
  - `String poste`
  - `double standard`
  - `double reel`
  - `String unite`

#### `modules\controle\infrastructure\persistence\jpa\BudgetJpaEntity.java`

**class** `BudgetJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String poste`
  - `@Column(nullable = false) private double budget`
  - `@Column(nullable = false) private double engage`
  - `@Column(nullable = false) private double realise`

#### `modules\controle\infrastructure\persistence\jpa\BudgetJpaRepository.java`

**interface** `BudgetJpaRepository` (public) extends `JpaRepository<BudgetJpaEntity, Long>`

#### `modules\controle\infrastructure\persistence\jpa\MargeProduitJpaEntity.java`

**class** `MargeProduitJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String code`
  - `@Column(nullable = false) private String designation`
  - `@Column(nullable = false) private double prixVente`
  - `@Column(nullable = false) private double coutRevient`
  - `@Column(nullable = false) private int ventesMois`

#### `modules\controle\infrastructure\persistence\jpa\MargeProduitJpaRepository.java`

**interface** `MargeProduitJpaRepository` (public) extends `JpaRepository<MargeProduitJpaEntity, Long>`

#### `modules\controle\infrastructure\persistence\jpa\VarianceJpaEntity.java`

**class** `VarianceJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String poste`
  - `@Column(nullable = false) private double standard`
  - `@Column(nullable = false) private double reel`
  - `@Column(nullable = false) private String unite`

#### `modules\controle\infrastructure\persistence\jpa\VarianceJpaRepository.java`

**interface** `VarianceJpaRepository` (public) extends `JpaRepository<VarianceJpaEntity, Long>`

#### `modules\controle\presentation\controller\ControleController.java`

**class** `ControleController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
@PreAuthorize("")
public)

- **Champs :**
  - `private final ControleDashboardService service`

- **Méthodes :**
  - `public ControleController(ControleDashboardService service)`
  - `@GetMapping("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<ControleDashboardResponse>> dashboard()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<MargeProduitResponse>>> marges()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<BudgetResponse>>> budget()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<VarianceResponse>>> variances()`


### Module `dg`

#### `modules\dg\application\dto\response\ClassementCommercialResponse.java`

**record** `ClassementCommercialResponse` (public)

- **Composants (record) :**
  - `int rang`
  - `String matricule`
  - `String nomComplet`
  - `double caRealise`
  - `long nbVentes`

#### `modules\dg\application\dto\response\JournalAuditResponse.java`

**record** `JournalAuditResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matricule`
  - `String role`
  - `TypeActionAudit typeAction`
  - `String module`
  - `String ressource`
  - `String referenceEntite`
  - `String methodeHttp`
  - `String endpoint`
  - `String details`
  - `boolean succes`
  - `String messageErreur`
  - `String adresseIp`
  - `long dureeMs`
  - `LocalDateTime horodatage`

#### `modules\dg\application\dto\response\KpisDgResponse.java`

**record** `KpisDgResponse` (public)

- **Composants (record) :**
  - `double caTotal`
  - `double caMoisN1`
  - `double caMoisN2`
  - `long ventesCount`
  - `int commerciauxActifs`
  - `double objectifCible`
  - `double objectifRealise`
  - `double margeGlobale`
  - `double tresorerie`
  - `double rentabiliteProductionPct`
  - `double stockMatieresPremieres`
  - `double stockConsommables`
  - `double stockProduitsFinis`

#### `modules\dg\application\dto\response\ParametreSystemeResponse.java`

**record** `ParametreSystemeResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String cle`
  - `double valeurNumerique`
  - `String description`
  - `String modifiePar`
  - `java.time.LocalDateTime horodatage`

#### `modules\dg\application\dto\response\UtilisateurDgResponse.java`

**record** `UtilisateurDgResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matricule`
  - `String prenom`
  - `String nom`
  - `String email`
  - `Role role`
  - `boolean actif`
  - `boolean compteBloque`
  - `int tentativesEchouees`
  - `String telephone`
  - `String residence`
  - `String cni`
  - `String photo`
  - `LocalDate dateNaissance`
  - `String lieuNaissance`
  - `LocalDate dateEmbauche`
  - `String sexe`
  - `LocalDateTime dateCreation`

#### `modules\dg\application\rapport\AnomalieResponse.java`

**record** `AnomalieResponse` (public)

- **Composants (record) :**
  - `String categorie`
  - `String reference`
  - `String description`
  - `String matriculeConcerne`

#### `modules\dg\application\rapport\AnomaliesService.java`

**class** `AnomaliesService` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final VersementRepository versementRepository`
  - `private final AlerteRepository alerteRepository`
  - `private final JournalAuditJpaRepository journalAuditJpaRepository`
  - `List<AnomalieResponse> resultats = new ArrayList<>()`

- **Méthodes :**
  - `public AnomaliesService(VersementRepository versementRepository, AlerteRepository alerteRepository,
                             JournalAuditJpaRepository journalAuditJpaRepository)`
  - `public List<AnomalieResponse> detecter()`

#### `modules\dg\application\rapport\PeriodeComparaison.java`

**enum** `PeriodeComparaison` (public)

#### `modules\dg\application\rapport\RapportComparatifResponse.java`

**record** `RapportComparatifResponse` (public)

- **Composants (record) :**
  - `PeriodeComparaison periode`
  - `Tranche actuelle`
  - `Tranche precedente`
  - `double variationAbsolueFCFA`
  - `double variationPourcent`

- **Méthodes :**
  - `public record Tranche(LocalDate debut, LocalDate fin, double caFCFA, long nombreVentes)`

**record** `Tranche` (public)

- **Composants (record) :**
  - `LocalDate debut`
  - `LocalDate fin`
  - `double caFCFA`
  - `long nombreVentes`

#### `modules\dg\application\rapport\RapportComparatifService.java`

**class** `RapportComparatifService` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final VenteJpaRepository venteJpaRepository`
  - `LocalDate finActuelle = ref`

- **Méthodes :**
  - `public RapportComparatifService(VenteJpaRepository venteJpaRepository)`
  - `public RapportComparatifResponse comparerCA(PeriodeComparaison periode, LocalDate dateReference)`
  - `Tranche calculerTranche(LocalDate debut, LocalDate fin)`
  - `private LocalDate debutPeriode(PeriodeComparaison periode, LocalDate ref)`
  - `private LocalDate debutPeriodePrecedente(PeriodeComparaison periode, LocalDate debutActuelle)`

#### `modules\dg\application\rapport\TauxOccupationMarcheService.java`

**class** `TauxOccupationMarcheService` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final BoutiquePotentielleRepository boutiqueRepository`
  - `private final ClientRepository clientRepository`

- **Méthodes :**
  - `public TauxOccupationMarcheService(BoutiquePotentielleRepository boutiqueRepository,
                                        ClientRepository clientRepository)`
  - `public List<TauxOccupationSecteurResponse> calculer()`

#### `modules\dg\application\rapport\TauxOccupationSecteurResponse.java`

**record** `TauxOccupationSecteurResponse` (public)

- **Composants (record) :**
  - `String secteur`
  - `long boutiquesRecensees`
  - `long clientsLivres`
  - `double tauxOccupationPourcent`

#### `modules\dg\application\rapport\ValidationEnAttenteResponse.java`

**record** `ValidationEnAttenteResponse` (public)

- **Composants (record) :**
  - `String type`
  - `String reference`
  - `String description`
  - `String matriculeConcerne`
  - `LocalDateTime depuis`

#### `modules\dg\application\rapport\ValidationsEnAttenteService.java`

**class** `ValidationsEnAttenteService` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final PreCommandeRepository preCommandeRepository`
  - `private final DotationRepository dotationRepository`
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final ReceptionRepository receptionRepository`
  - `private final VersementRepository versementRepository`
  - `private final CarburantRepository carburantRepository`
  - `List<ValidationEnAttenteResponse> resultats = new ArrayList<>()`

- **Méthodes :**
  - `public ValidationsEnAttenteService(PreCommandeRepository preCommandeRepository,
                                        DotationRepository dotationRepository,
                                        BonCommandeRepository bonCommandeRepository,
                                        ReceptionRepository receptionRepository,
                                        VersementRepository versementRepository,
                                        CarburantRepository carburantRepository)`
  - `public List<ValidationEnAttenteResponse> lister()`

#### `modules\dg\application\rapport\ZonePerformanceResponse.java`

**record** `ZonePerformanceResponse` (public)

- **Composants (record) :**
  - `String secteur`
  - `double caMoisCourantFCFA`
  - `long nombreClients`
  - `long nombreVentesMoisCourant`

#### `modules\dg\application\rapport\ZonePerformanceService.java`

**class** `ZonePerformanceService` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final ClientRepository clientRepository`
  - `private final VenteJpaRepository venteJpaRepository`

- **Méthodes :**
  - `public ZonePerformanceService(ClientRepository clientRepository, VenteJpaRepository venteJpaRepository)`
  - `public List<ZonePerformanceResponse> calculer()`

#### `modules\dg\application\usecase\ConsulterJournalAuditUseCase.java`

**class** `ConsulterJournalAuditUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final JournalAuditJpaRepository journalAuditJpaRepository`
  - `List<JournalAuditJpaEntity> entries`

- **Méthodes :**
  - `public ConsulterJournalAuditUseCase(JournalAuditJpaRepository journalAuditJpaRepository)`
  - `public List<JournalAuditResponse> executer(String matricule,
                                                LocalDateTime debut,
                                                LocalDateTime fin,
                                                String module,
                                                TypeActionAudit typeAction,
                                                int limite)`
  - `private JournalAuditResponse versResponse(JournalAuditJpaEntity e)`

#### `modules\dg\application\usecase\DashboardFinancierDgUseCase.java`

**class** `DashboardFinancierDgUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final JournalCaisseRepository caisseRepository`
  - `private final FactureRepository factureRepository`
  - `private final DecaissementRepository decaissementRepository`
  - `private final VersementRepository versementRepository`
  - `private final PrimeRepository primeRepository`
  - `private final RecouvrementRepository recouvrementRepository`

- **Méthodes :**
  - `public DashboardFinancierDgUseCase(JournalCaisseRepository caisseRepository,
                                        FactureRepository factureRepository,
                                        DecaissementRepository decaissementRepository,
                                        VersementRepository versementRepository,
                                        PrimeRepository primeRepository,
                                        RecouvrementRepository recouvrementRepository)`
  - `public DashboardFinancier executer()`
  - `public record DashboardFinancier(LocalDate date,
        double soldeCaisse,
        double totalFacturesEmises,
        double totalFacturesPayees,
        int nbFacturesImpayees,
        double totalDecaissementsExecutes,
        int nbDecaissementsEnAttente,
        int nbVersementsEnAttente,
        int nbAlertesRouges,
        double totalVersementsValides,
        int nbPrimesValideesNonVersées,
        double totalPrimesAVerser,
        int nbRecouvrementsEnCours,
        int nbRecouvrementsContentieux,
        double totalCreances)`

**record** `DashboardFinancier` (public)

- **Composants (record) :**
  - `LocalDate date`
  - `double soldeCaisse`
  - `double totalFacturesEmises`
  - `double totalFacturesPayees`
  - `int nbFacturesImpayees`
  - `double totalDecaissementsExecutes`
  - `int nbDecaissementsEnAttente`
  - `int nbVersementsEnAttente`
  - `int nbAlertesRouges`
  - `double totalVersementsValides`
  - `int nbPrimesValideesNonVersées`
  - `double totalPrimesAVerser`
  - `int nbRecouvrementsEnCours`
  - `int nbRecouvrementsContentieux`
  - `double totalCreances`

#### `modules\dg\application\usecase\DashboardProductionDgUseCase.java`

**class** `DashboardProductionDgUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final PPHRepository pphRepository`
  - `private final OFRepository ofRepository`
  - `private final LotRepository lotRepository`

- **Méthodes :**
  - `public DashboardProductionDgUseCase(PPHRepository pphRepository,
                                         OFRepository ofRepository,
                                         LotRepository lotRepository)`
  - `public DashboardProduction executer()`
  - `public record DashboardProduction(LocalDate date,
        int nbPPHEnCours,
        int nbPPHClotures,
        int nbOFPlanifies,
        int nbOFEnCours,
        int nbOFHonores,
        int nbOFAnnules,
        int nbLotsDeclares,
        int nbLotsReceptionnes,
        int nbLotsValides,
        int nbLotsRejetes,
        double quantiteProduiteKg,
        int nbCartons)`

**record** `DashboardProduction` (public)

- **Composants (record) :**
  - `LocalDate date`
  - `int nbPPHEnCours`
  - `int nbPPHClotures`
  - `int nbOFPlanifies`
  - `int nbOFEnCours`
  - `int nbOFHonores`
  - `int nbOFAnnules`
  - `int nbLotsDeclares`
  - `int nbLotsReceptionnes`
  - `int nbLotsValides`
  - `int nbLotsRejetes`
  - `double quantiteProduiteKg`
  - `int nbCartons`

#### `modules\dg\application\usecase\DashboardStockDgUseCase.java`

**class** `DashboardStockDgUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final AlerteRepository alerteRepository`
  - `private final SeuilStockRepository seuilStockRepository`
  - `int nbProduitsSousSeuil = 0`

- **Méthodes :**
  - `public DashboardStockDgUseCase(NiveauStockRepository niveauStockRepository,
                                    AlerteRepository alerteRepository,
                                    SeuilStockRepository seuilStockRepository)`
  - `public DashboardStock executer()`
  - `public record DashboardStock(LocalDate date,
        int nbProduitsCentral,
        long totalQuantiteCentral,
        int nbProduitsTampon,
        long totalQuantiteTampon,
        int nbProduitsMobile,
        long totalQuantiteMobile,
        int nbAlertesActives,
        long totalAlertes,
        int nbProduitsSousSeuil)`

**record** `DashboardStock` (public)

- **Composants (record) :**
  - `LocalDate date`
  - `int nbProduitsCentral`
  - `long totalQuantiteCentral`
  - `int nbProduitsTampon`
  - `long totalQuantiteTampon`
  - `int nbProduitsMobile`
  - `long totalQuantiteMobile`
  - `int nbAlertesActives`
  - `long totalAlertes`
  - `int nbProduitsSousSeuil`

#### `modules\dg\application\usecase\GererParametresSystemeUseCase.java`

**class** `GererParametresSystemeUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ParametresSystemeJpaRepository repository`

- **Méthodes :**
  - `public GererParametresSystemeUseCase(ParametresSystemeJpaRepository repository)`
  - `@Transactional(readOnly = true) public List<ParametreSystemeResponse> lister()`
  - `@Transactional(readOnly = true) public ParametreSystemeResponse consulter(String cle)`
  - `public List<ParametreSystemeResponse> modifier(Map<String, Double> modifications, String matriculeDg)`
  - `private ParametreSystemeResponse versResponse(ParametresSystemeJpaEntity e)`

#### `modules\dg\application\usecase\ListerUtilisateursDgUseCase.java`

**class** `ListerUtilisateursDgUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final UtilisateurJpaRepository utilisateurJpaRepository`
  - `List<UtilisateurJpaEntity> utilisateurs`

- **Méthodes :**
  - `public ListerUtilisateursDgUseCase(UtilisateurJpaRepository utilisateurJpaRepository)`
  - `public List<UtilisateurDgResponse> executer(Role roleFiltre, Boolean actifFiltre)`
  - `public Optional<UtilisateurDgResponse> trouverParMatricule(String matricule)`
  - `private UtilisateurDgResponse versResponse(UtilisateurJpaEntity u)`

#### `modules\dg\application\usecase\ObtenirClassementDgUseCase.java`

**class** `ObtenirClassementDgUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final VenteJpaRepository venteJpaRepository`
  - `private final UtilisateurJpaRepository utilisateurJpaRepository`
  - `Map<String, Double> caParCommercial = new HashMap<>()`
  - `Map<String, Long> nbVentesParCommercial = new HashMap<>()`
  - `List<ClassementCommercialResponse> resultat = new ArrayList<>()`
  - `int i = 0`

- **Méthodes :**
  - `public ObtenirClassementDgUseCase(VenteJpaRepository venteJpaRepository,
                                      UtilisateurJpaRepository utilisateurJpaRepository)`
  - `public List<ClassementCommercialResponse> executer()`

#### `modules\dg\application\usecase\ObtenirKpisDgUseCase.java`

**class** `ObtenirKpisDgUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final VenteJpaRepository venteJpaRepository`
  - `private final UtilisateurJpaRepository utilisateurJpaRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final JournalCaisseRepository caisseRepository`
  - `private final VersementRepository versementRepository`
  - `private final DecaissementRepository decaissementRepository`
  - `double tresorerie = soldeCaisse + totalVersementsValides - totalDecaissementsExecutes`
  - `double margeGlobale = caTotal * 0.30`

- **Méthodes :**
  - `public ObtenirKpisDgUseCase(VenteJpaRepository venteJpaRepository,
                                UtilisateurJpaRepository utilisateurJpaRepository,
                                NiveauStockRepository niveauStockRepository,
                                JournalCaisseRepository caisseRepository,
                                VersementRepository versementRepository,
                                DecaissementRepository decaissementRepository)`
  - `public KpisDgResponse executer()`
  - `private double calculerCa(List<VenteJpaEntity> ventes)`

#### `modules\dg\presentation\controller\DgController.java`

**class** `DgController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
@PreAuthorize("")
public)

- **Champs :**
  - `private final ObtenirKpisDgUseCase kpisUseCase`
  - `private final ObtenirClassementDgUseCase classementUseCase`
  - `private final RapportComparatifService rapportComparatifService`
  - `private final TauxOccupationMarcheService tauxOccupationMarcheService`
  - `private final ZonePerformanceService zonePerformanceService`
  - `private final ValidationsEnAttenteService validationsEnAttenteService`
  - `private final AnomaliesService anomaliesService`
  - `private final DashboardFinancierDgUseCase dashboardFinancierUseCase`
  - `private final DashboardProductionDgUseCase dashboardProductionUseCase`
  - `private final DashboardStockDgUseCase dashboardStockUseCase`
  - `private final ConsulterJournalAuditUseCase journalAuditUseCase`
  - `private final ListerUtilisateursDgUseCase listerUtilisateursUseCase`
  - `private final GererParametresSystemeUseCase parametresUseCase`
  - `private final CreerEmployeUseCase creerEmployeUseCase`

- **Méthodes :**
  - `public DgController(ObtenirKpisDgUseCase kpisUseCase, ObtenirClassementDgUseCase classementUseCase,
                         RapportComparatifService rapportComparatifService,
                         TauxOccupationMarcheService tauxOccupationMarcheService,
                         ZonePerformanceService zonePerformanceService,
                         ValidationsEnAttenteService validationsEnAttenteService,
                         AnomaliesService anomaliesService,
                         DashboardFinancierDgUseCase dashboardFinancierUseCase,
                         DashboardProductionDgUseCase dashboardProductionUseCase,
                         DashboardStockDgUseCase dashboardStockUseCase,
                         ConsulterJournalAuditUseCase journalAuditUseCase,
                         ListerUtilisateursDgUseCase listerUtilisateursUseCase,
                         GererParametresSystemeUseCase parametresUseCase,
                         CreerEmployeUseCase creerEmployeUseCase)`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<KpisDgResponse>> kpis()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<ClassementCommercialResponse>>> classement()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<TauxOccupationSecteurResponse>>> tauxOccupationMarches()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<ZonePerformanceResponse>>> zones()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<ValidationEnAttenteResponse>>> validations()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<AnomalieResponse>>> anomalies()`
  - `DashboardFinancier>> dashboardFinancier()`
  - `DashboardProduction>> dashboardProduction()`
  - `DashboardStock>> dashboardStock()`
  - `@PostMapping("")
    @Operation(summary = "",
        description = "" +
                      "" +
                      "") public ResponseEntity<ApiResponse<EmployeResponse>> creerEmploye(@RequestBody @Valid CreerEmployeRequest request)`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<ParametreSystemeResponse>>> parametres()`
  - `@PutMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<List<ParametreSystemeResponse>>> modifierParametres(@RequestBody Map<String, Double> modifications,
        Authentication authentication)`


### Module `other`

#### `ApiTantyApplication.java`

**class** `ApiTantyApplication` (@SpringBootApplication
public)

- **Méthodes :**
  - `public static void main(String[] args)`


### Module `production`

#### `modules\production\application\affectation\dto\request\CreerAffectationJourRequest.java`

**record** `CreerAffectationJourRequest` (public)

- **Composants (record) :**
  - `@NotBlank String referencePPH`
  - `@NotNull LocalDate date`
  - `@NotEmpty @Valid List<LigneAffectationRequest> lignes`

#### `modules\production\application\affectation\dto\request\DetailsRealisationPosteRequest.java`

_Fichier vide ou non analysable._

#### `modules\production\application\affectation\dto\request\LigneAffectationRequest.java`

**record** `LigneAffectationRequest` (public)

- **Composants (record) :**
  - `@NotNull CodePoste poste`
  - `@NotEmpty List<String> matriculesEmployes`
  - `@NotNull DureeAffectation duree`

#### `modules\production\application\affectation\dto\request\ProductionIndividuelleRequest.java`

_Fichier vide ou non analysable._

#### `modules\production\application\affectation\dto\request\SaisirRealisationPosteRequest.java`

_Fichier vide ou non analysable._

#### `modules\production\application\affectation\dto\response\AffectationJournaliereResponse.java`

**record** `AffectationJournaliereResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referencePPH`
  - `LocalDate date`
  - `String matriculeAgent`
  - `StatutAffectation statut`
  - `List<LigneAffectationResponse> lignes`
  - `List<SaisieRealisationResponse> saisies`
  - `List<ClassementPosteResponse> classement`
  - `LocalDateTime dateCreation`
  - `LocalDateTime dateModification`

- **Méthodes :**
  - `public record LigneAffectationResponse(String codePoste,
        String libellePoste,
        int effectifReference,
        int effectifReel,
        String duree,
        double objectifAjuste,
        String unite,
        List<String> matriculesEmployes)`
  - `public record SaisieRealisationResponse(String codePoste,
        String libellePoste,
        double quantiteRealisee,
        double objectifAjuste,
        double pctRealisation,
        String unite,
        List<ProductionIndividuelleResponse> productionsIndividuelles,
        DetailsRealisationPosteResponse details,
        List<String> warnings,
        LocalDateTime dateEnregistrement)`
  - `public record ProductionIndividuelleResponse(String matriculeEmploye,
        String nomEmploye,
        double quantiteRealisee,
        String unite)`
  - `public record ClassementPosteResponse(int rang,
        String codePoste,
        String libellePoste,
        double quantiteRealisee,
        double objectifAjuste,
        double pctRealisation,
        String unite)`

**record** `LigneAffectationResponse` (public)

- **Composants (record) :**
  - `String codePoste`
  - `String libellePoste`
  - `int effectifReference`
  - `int effectifReel`
  - `String duree`
  - `double objectifAjuste`
  - `String unite`
  - `List<String> matriculesEmployes`

**record** `SaisieRealisationResponse` (public)

- **Composants (record) :**
  - `String codePoste`
  - `String libellePoste`
  - `double quantiteRealisee`
  - `double objectifAjuste`
  - `double pctRealisation`
  - `String unite`
  - `List<ProductionIndividuelleResponse> productionsIndividuelles`
  - `DetailsRealisationPosteResponse details`
  - `List<String> warnings`
  - `LocalDateTime dateEnregistrement`

**record** `ProductionIndividuelleResponse` (public)

- **Composants (record) :**
  - `String matriculeEmploye`
  - `String nomEmploye`
  - `double quantiteRealisee`
  - `String unite`

**record** `ClassementPosteResponse` (public)

- **Composants (record) :**
  - `int rang`
  - `String codePoste`
  - `String libellePoste`
  - `double quantiteRealisee`
  - `double objectifAjuste`
  - `double pctRealisation`
  - `String unite`

#### `modules\production\application\affectation\dto\response\ClassementExecuteurResponse.java`

**record** `ClassementExecuteurResponse` (public)

- **Composants (record) :**
  - `int rang`
  - `String matriculeEmploye`
  - `String nomEmploye`
  - `double quantiteTotaleRealisee`
  - `String unite`
  - `double tauxRealisationMoyen`
  - `int nbJoursAffectes`

#### `modules\production\application\affectation\dto\response\ComparaisonFutsProductionResponse.java`

**record** `ComparaisonFutsProductionResponse` (public)

- **Composants (record) :**
  - `String referencePPH`
  - `LocalDate date`
  - `int nbFutsNets`
  - `double poidsTotalPoudreKg`
  - `double productionReelleKg`
  - `double ecartKg`
  - `double ecartPourcentage`
  - `String uniteProduction`

#### `modules\production\application\affectation\dto\response\DetailsRealisationPosteResponse.java`

**record** `DetailsRealisationPosteResponse` (public)

- **Composants (record) :**
  - `Integer nbRegimesEpluches`
  - `Integer nbRegimesDecoupes`
  - `Integer nbSceauxEtiquetes`
  - `Integer nbEtiquettesDatees`
  - `Integer nbSachetsDates`
  - `Integer nbCartonsProduits`
  - `Double kgChipsProduits`

#### `modules\production\application\affectation\dto\response\RapportHebdomadaireProductionResponse.java`

**record** `RapportHebdomadaireProductionResponse` (public)

- **Composants (record) :**
  - `LocalDate debut`
  - `LocalDate fin`
  - `String periodeLibelle`
  - `int nbFutsNets`
  - `double poidsTotalPoudreKg`
  - `double productionTotaleKg`
  - `int nbSessionsDosage`
  - `int nbJoursAffectes`
  - `List<ClassementExecuteurResponse> classementExecuteurs`
  - `List<ComparaisonFutsProductionResponse> comparaisonsJournalieres`

#### `modules\production\application\affectation\mapper\AffectationMapper.java`

**class** `AffectationMapper` (@Component
public)

- **Méthodes :**
  - `public AffectationJournaliereResponse versResponse(AffectationJournaliere a)`
  - `SaisieRealisationResponse mapSaisie(SaisieRealisationPoste s)`
  - `private DetailsRealisationPosteResponse mapDetails(DetailsRealisationPoste d)`
  - `ClassementPosteResponse> buildClassement(List<SaisieRealisationPoste> postes)`

#### `modules\production\application\affectation\usecase\ClassementExecuteursHebdomadaireUseCase.java`

**class** `ClassementExecuteursHebdomadaireUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final AffectationJournaliereRepository repository`
  - `Map<String, StatsExecuteur> stats = new HashMap<>()`
  - `final String matricule`
  - `final String nom`
  - `final String unite`
  - `double quantite`
  - `double tauxCumule`
  - `int nbTaux`
  - `final Set<LocalDate> joursAffectes = new HashSet<>()`

- **Méthodes :**
  - `public ClassementExecuteursHebdomadaireUseCase(AffectationJournaliereRepository repository)`
  - `public List<ClassementExecuteurResponse> executer(LocalDate debut, LocalDate fin)`
  - `void ajouter(double qte, double taux, LocalDate date)`
  - `double quantiteTotale()`
  - `double tauxMoyen()`

**class** `StatsExecuteur` (private static)

- **Champs :**
  - `final String matricule`
  - `final String nom`
  - `final String unite`
  - `double quantite`
  - `double tauxCumule`
  - `int nbTaux`
  - `final Set<LocalDate> joursAffectes = new HashSet<>()`

- **Méthodes :**
  - `void ajouter(double qte, double taux, LocalDate date)`
  - `double quantiteTotale()`
  - `double tauxMoyen()`

#### `modules\production\application\affectation\usecase\ComparaisonFutsProductionUseCase.java`

**class** `ComparaisonFutsProductionUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final SessionDosageRepository dosageRepository`
  - `private final AffectationJournaliereRepository affectationRepository`
  - `double poidsTotalPoudreKg = nbFutsNets * 50.0`
  - `double ecartKg = productionReelleKg - poidsTotalPoudreKg`
  - `double ecartPct = poidsTotalPoudreKg > 0 ? (ecartKg / poidsTotalPoudreKg) * 100.0 : 0.0`

- **Méthodes :**
  - `public ComparaisonFutsProductionUseCase(SessionDosageRepository dosageRepository,
                                             AffectationJournaliereRepository affectationRepository)`
  - `public ComparaisonFutsProductionResponse executer(String referencePPH, LocalDate date)`
  - `private double calculerProductionAval(AffectationJournaliere affectation)`
  - `private boolean estPosteConditionnement(CodePoste poste)`

#### `modules\production\application\affectation\usecase\CreerAffectationJourUseCase.java`

**class** `CreerAffectationJourUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final AffectationJournaliereRepository repository`
  - `private final AffectationMapper mapper`

- **Méthodes :**
  - `public CreerAffectationJourUseCase(AffectationJournaliereRepository repository, AffectationMapper mapper)`
  - `public AffectationJournaliereResponse executer(CreerAffectationJourRequest req, Utilisateur acteur)`
  - `private boolean peutGererAffectation(Utilisateur u)`

#### `modules\production\application\affectation\usecase\RapportHebdomadaireProductionUseCase.java`

**class** `RapportHebdomadaireProductionUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final AffectationJournaliereRepository affectationRepository`
  - `private final SessionDosageRepository dosageRepository`
  - `private final ComparaisonFutsProductionUseCase comparaisonUseCase`
  - `private final ClassementExecuteursHebdomadaireUseCase classementUseCase`
  - `int nbFutsNets = 0`
  - `double poidsTotalPoudreKg = 0.0`
  - `double productionTotaleKg = 0.0`
  - `List<ComparaisonFutsProductionResponse> comparaisons = new ArrayList<>()`

- **Méthodes :**
  - `public RapportHebdomadaireProductionUseCase(AffectationJournaliereRepository affectationRepository,
                                                 SessionDosageRepository dosageRepository,
                                                 ComparaisonFutsProductionUseCase comparaisonUseCase,
                                                 ClassementExecuteursHebdomadaireUseCase classementUseCase)`
  - `public RapportHebdomadaireProductionResponse executer(LocalDate debut, LocalDate fin)`
  - `private boolean estPosteProductionKg(CodePoste poste)`

#### `modules\production\application\affectation\usecase\SaisirRealisationPosteUseCase.java`

**class** `SaisirRealisationPosteUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final AffectationJournaliereRepository repository`
  - `private final AffectationMapper mapper`
  - `List<String> warnings = new ArrayList<>()`

- **Méthodes :**
  - `public SaisirRealisationPosteUseCase(AffectationJournaliereRepository repository, AffectationMapper mapper)`
  - `public AffectationJournaliereResponse executer(SaisirRealisationPosteRequest req, Utilisateur acteur)`
  - `private void verifierCoherenceChipsPlantain(SaisirRealisationPosteRequest req,
                                                AffectationJournaliere affectation,
                                                List<String> warnings)`
  - `private DetailsRealisationPoste mapDetails(DetailsRealisationPosteRequest d)`
  - `private boolean peutGererAffectation(Utilisateur u)`

#### `modules\production\application\affectation\usecase\ValiderAffectationUseCase.java`

**class** `ValiderAffectationUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final AffectationJournaliereRepository repository`
  - `private final AffectationMapper mapper`

- **Méthodes :**
  - `public ValiderAffectationUseCase(AffectationJournaliereRepository repository, AffectationMapper mapper)`
  - `public AffectationJournaliereResponse executer(Long affectationId, Utilisateur acteur)`
  - `private boolean peutValider(Utilisateur u)`

#### `modules\production\application\besoin\dto\request\SignalerBesoinRequest.java`

_Fichier vide ou non analysable._

#### `modules\production\application\besoin\dto\response\ExpressionBesoinResponse.java`

**record** `ExpressionBesoinResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numero`
  - `String referencePPH`
  - `TypeBesoin type`
  - `String codeProduit`
  - `String designationProduit`
  - `double quantiteDemandee`
  - `String unite`
  - `String matriculeDemandeur`
  - `LocalDate dateBesoin`
  - `String motif`
  - `StatutBesoin statut`
  - `String matriculeValidateur`
  - `String motifRejet`
  - `LocalDateTime dateCreation`
  - `LocalDateTime dateModification`

#### `modules\production\application\besoin\mapper\ExpressionBesoinMapper.java`

**class** `ExpressionBesoinMapper` (@Component
public)

- **Méthodes :**
  - `public ExpressionBesoinResponse versResponse(ExpressionBesoin b)`

#### `modules\production\application\besoin\usecase\SignalerBesoinUseCase.java`

**class** `SignalerBesoinUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ExpressionBesoinRepository repository`
  - `private final ExpressionBesoinMapper mapper`

- **Méthodes :**
  - `public SignalerBesoinUseCase(ExpressionBesoinRepository repository, ExpressionBesoinMapper mapper)`
  - `public ExpressionBesoinResponse executer(SignalerBesoinRequest req, Utilisateur utilisateur)`

#### `modules\production\application\besoin\usecase\TraiterBesoinUseCase.java`

**class** `TraiterBesoinUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ExpressionBesoinRepository repository`
  - `private final ExpressionBesoinMapper mapper`

- **Méthodes :**
  - `public TraiterBesoinUseCase(ExpressionBesoinRepository repository, ExpressionBesoinMapper mapper)`
  - `public ExpressionBesoinResponse valider(String numero, String matriculeValidateur)`
  - `public ExpressionBesoinResponse rejeter(String numero, String matriculeValidateur, String motifRejet)`

#### `modules\production\application\broyage\dto\request\AutoriserDepassementBroyageRequest.java`

_Fichier vide ou non analysable._

#### `modules\production\application\broyage\dto\request\CloturerSessionBroyageRequest.java`

_Fichier vide ou non analysable._

#### `modules\production\application\broyage\dto\request\EnregistrerRealisationBroyageRequest.java`

_Fichier vide ou non analysable._

#### `modules\production\application\broyage\dto\request\JustifierNonValidationBroyageRequest.java`

**record** `JustifierNonValidationBroyageRequest` (public)

- **Composants (record) :**
  - `@NotNull Long sessionId`
  - `@NotBlank String justificatif`

#### `modules\production\application\broyage\dto\request\ObjectifMachinisteRequest.java`

**record** `ObjectifMachinisteRequest` (public)

- **Composants (record) :**
  - `@NotBlank String machinisteMatricule`
  - `@NotBlank String nomMachiniste`
  - `@NotNull OperationBroyage operation`
  - `@Positive double objectifKg`

#### `modules\production\application\broyage\dto\request\OuvrirSessionBroyageRequest.java`

**record** `OuvrirSessionBroyageRequest` (public)

- **Composants (record) :**
  - `@NotBlank String referencePPH`
  - `@NotNull LocalDate date`
  - `@NotNull TypePoudre typePoudre`
  - `@Positive double objectifJournalierKg`
  - `@NotEmpty @Valid List<ObjectifMachinisteRequest> objectifsMachinistes`

#### `modules\production\application\broyage\dto\response\ClassementMachinisteResponse.java`

**record** `ClassementMachinisteResponse` (public)

- **Composants (record) :**
  - `List<LigneClassement> classements`
  - `String periode`

- **Méthodes :**
  - `public record LigneClassement(int rang,
        String machinisteMatricule,
        String nomMachiniste,
        double productiviteKg,
        double objectifTotalKg,
        double tauxRealisationMoyen,
        double efficacite,
        int nbJours,
        List<String> operations)`

**record** `LigneClassement` (public)

- **Composants (record) :**
  - `int rang`
  - `String machinisteMatricule`
  - `String nomMachiniste`
  - `double productiviteKg`
  - `double objectifTotalKg`
  - `double tauxRealisationMoyen`
  - `double efficacite`
  - `int nbJours`
  - `List<String> operations`

#### `modules\production\application\broyage\dto\response\SessionBroyageResponse.java`

**record** `SessionBroyageResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referencePPH`
  - `LocalDate date`
  - `TypePoudre typePoudre`
  - `String typePoudreLibelle`
  - `double objectifJournalierKg`
  - `String matriculeChefMachiniste`
  - `List<ObjectifMachinisteResponse> objectifsMachinistes`
  - `List<RealisationMachinisteResponse> realisations`
  - `Double quantiteNetteBroyeeKg`
  - `Double pertesKg`
  - `Double pctPertes`
  - `double totalRealiseKg`
  - `double depassementAutoriseKg`
  - `boolean journeeValidee`
  - `String justificatifNonValidation`
  - `StatutSessionBroyage statut`
  - `LocalDateTime dateCreation`
  - `LocalDateTime dateModification`

- **Méthodes :**
  - `public record ObjectifMachinisteResponse(String machinisteMatricule,
        String nomMachiniste,
        String operation,
        String operationLibelle,
        double objectifKg)`
  - `public record RealisationMachinisteResponse(String machinisteMatricule,
        String nomMachiniste,
        String operation,
        String operationLibelle,
        double objectifKg,
        double realiseKg,
        double pctRealisation)`

**record** `ObjectifMachinisteResponse` (public)

- **Composants (record) :**
  - `String machinisteMatricule`
  - `String nomMachiniste`
  - `String operation`
  - `String operationLibelle`
  - `double objectifKg`

**record** `RealisationMachinisteResponse` (public)

- **Composants (record) :**
  - `String machinisteMatricule`
  - `String nomMachiniste`
  - `String operation`
  - `String operationLibelle`
  - `double objectifKg`
  - `double realiseKg`
  - `double pctRealisation`

#### `modules\production\application\broyage\mapper\SessionBroyageMapper.java`

**class** `SessionBroyageMapper` (@Component
public)

- **Méthodes :**
  - `public SessionBroyageResponse versResponse(SessionBroyage s)`

#### `modules\production\application\broyage\usecase\AutoriserDepassementBroyageUseCase.java`

**class** `AutoriserDepassementBroyageUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final SessionBroyageRepository repository`
  - `private final SessionBroyageMapper mapper`

- **Méthodes :**
  - `public AutoriserDepassementBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper)`
  - `public SessionBroyageResponse executer(AutoriserDepassementBroyageRequest req, Utilisateur acteur)`
  - `private boolean peutGererBroyage(Utilisateur u)`

#### `modules\production\application\broyage\usecase\ClassementMachinistesUseCase.java`

**class** `ClassementMachinistesUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final SessionBroyageRepository repository`
  - `Map<String, StatsMachiniste> statsParMachiniste = new HashMap<>()`
  - `double efficacite = s.objectifTotal > 0 ? s.realiseTotal / s.objectifTotal : 0.0`
  - `LigneClassement> classements = new ArrayList<>()`
  - `int i = 0`
  - `final String nom`
  - `double objectifTotal`
  - `double realiseTotal`
  - `double sommePctRealisation`
  - `int nbLignes`
  - `final Set<LocalDate> jours = new HashSet<>()`
  - `final Set<String> operations = new HashSet<>()`

- **Méthodes :**
  - `public ClassementMachinistesUseCase(SessionBroyageRepository repository)`
  - `public ClassementMachinisteResponse executer(LocalDate debut, LocalDate fin)`
  - `void ajouter(LocalDate date, String operation, double objectif, double realise, double pctRealisation)`
  - `double tauxRealisationMoyen()`
  - `int nbJoursDistincts()`

**class** `StatsMachiniste` (private static)

- **Champs :**
  - `final String nom`
  - `double objectifTotal`
  - `double realiseTotal`
  - `double sommePctRealisation`
  - `int nbLignes`
  - `final Set<LocalDate> jours = new HashSet<>()`
  - `final Set<String> operations = new HashSet<>()`

- **Méthodes :**
  - `void ajouter(LocalDate date, String operation, double objectif, double realise, double pctRealisation)`
  - `double tauxRealisationMoyen()`
  - `int nbJoursDistincts()`

#### `modules\production\application\broyage\usecase\CloturerSessionBroyageUseCase.java`

**class** `CloturerSessionBroyageUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final SessionBroyageRepository repository`
  - `private final SessionBroyageMapper mapper`

- **Méthodes :**
  - `public CloturerSessionBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper)`
  - `public SessionBroyageResponse executer(CloturerSessionBroyageRequest req, Utilisateur acteur)`
  - `private boolean peutGererBroyage(Utilisateur u)`

#### `modules\production\application\broyage\usecase\EnregistrerRealisationBroyageUseCase.java`

**class** `EnregistrerRealisationBroyageUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final SessionBroyageRepository repository`
  - `private final SessionBroyageMapper mapper`

- **Méthodes :**
  - `public EnregistrerRealisationBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper)`
  - `public SessionBroyageResponse executer(EnregistrerRealisationBroyageRequest req, Utilisateur acteur)`
  - `private boolean peutGererBroyage(Utilisateur u)`

#### `modules\production\application\broyage\usecase\JustifierNonValidationBroyageUseCase.java`

**class** `JustifierNonValidationBroyageUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final SessionBroyageRepository repository`
  - `private final SessionBroyageMapper mapper`

- **Méthodes :**
  - `public JustifierNonValidationBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper)`
  - `public SessionBroyageResponse executer(JustifierNonValidationBroyageRequest req, Utilisateur acteur)`
  - `private boolean peutGererBroyage(Utilisateur u)`

#### `modules\production\application\broyage\usecase\OuvrirSessionBroyageUseCase.java`

**class** `OuvrirSessionBroyageUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final SessionBroyageRepository repository`
  - `private final SessionBroyageMapper mapper`

- **Méthodes :**
  - `public OuvrirSessionBroyageUseCase(SessionBroyageRepository repository, SessionBroyageMapper mapper)`
  - `public SessionBroyageResponse executer(OuvrirSessionBroyageRequest req, Utilisateur acteur)`
  - `private boolean peutGererBroyage(Utilisateur u)`

#### `modules\production\application\dashboard\usecase\DashboardProductionUseCase.java`

**class** `DashboardProductionUseCase` (@Service
public)

- **Champs :**
  - `private final LotRepository lotRepository`
  - `private final OFRepository ofRepository`
  - `private final PPHRepository pphRepository`
  - `private final FicheProduitRepository ficheRepository`
  - `int tauxAvancementGlobal = 0`

- **Méthodes :**
  - `public DashboardProductionUseCase(LotRepository lotRepository, OFRepository ofRepository,
                                       PPHRepository pphRepository, FicheProduitRepository ficheRepository)`
  - `public DashboardChefProduction executerChef()`
  - `public DashboardAgentProduction executerAgent(String matriculeAgent)`
  - `public record DashboardChefProduction(double quantiteKgSemaine, int cartonsSemaine, long lotsEnAttenteValidation,
        int ofsEnCours, int ofsPlanifies, int ofsHonores,
        int tauxAvancementPPH, long fichesSemaine,
        String pphEnCoursReference, String pphEnCoursSemaine,
        LocalDate debutSemaine, LocalDate finSemaine)`
  - `public record DashboardAgentProduction(LocalDate date, int ofsEnCours, int ofsPlanifies,
        int fichesSemaine, boolean ficheJourExistante,
        List<OFResume> ofs)`
  - `public record OFResume(String idOF, String codeProduit, String designationProduit,
        int qteDemandee, int qteRealisee, int tauxAvancement)`

**record** `DashboardChefProduction` (public)

- **Composants (record) :**
  - `double quantiteKgSemaine`
  - `int cartonsSemaine`
  - `long lotsEnAttenteValidation`
  - `int ofsEnCours`
  - `int ofsPlanifies`
  - `int ofsHonores`
  - `int tauxAvancementPPH`
  - `long fichesSemaine`
  - `String pphEnCoursReference`
  - `String pphEnCoursSemaine`
  - `LocalDate debutSemaine`
  - `LocalDate finSemaine`

**record** `DashboardAgentProduction` (public)

- **Composants (record) :**
  - `LocalDate date`
  - `int ofsEnCours`
  - `int ofsPlanifies`
  - `int fichesSemaine`
  - `boolean ficheJourExistante`
  - `List<OFResume> ofs`

**record** `OFResume` (public)

- **Composants (record) :**
  - `String idOF`
  - `String codeProduit`
  - `String designationProduit`
  - `int qteDemandee`
  - `int qteRealisee`
  - `int tauxAvancement`

#### `modules\production\application\dosage\dto\request\EnregistrerSessionDosageRequest.java`

**record** `EnregistrerSessionDosageRequest` (public)

- **Composants (record) :**
  - `@NotBlank String referencePPH`
  - `@NotNull LocalDate date`
  - `@NotBlank String matriculeAgentDoseur`

#### `modules\production\application\dosage\dto\response\PredictionProduitFiniResponse.java`

**record** `PredictionProduitFiniResponse` (public)

- **Composants (record) :**
  - `int nbFutsNets`
  - `double poidsTotalPoudreKg`
  - `List<LignePrediction> predictions`

- **Méthodes :**
  - `public record LignePrediction(String codeProduit,
        String designation,
        String recette,
        int quantiteUnitesBase,
        int quantiteCartons,
        double poidsUnitaireGrammes,
        int nombreUnitesParCarton)`

**record** `LignePrediction` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `String recette`
  - `int quantiteUnitesBase`
  - `int quantiteCartons`
  - `double poidsUnitaireGrammes`
  - `int nombreUnitesParCarton`

#### `modules\production\application\dosage\dto\response\SessionDosageResponse.java`

**record** `SessionDosageResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referencePPH`
  - `LocalDate date`
  - `String matriculeAgentDoseur`
  - `List<MatiereResponse> matieresUtilisees`
  - `List<String> machinesMobilisees`
  - `int nbFutsProduits`
  - `int nbFutsNets`
  - `String justificationEcart`
  - `boolean validee`
  - `boolean cloturee`
  - `LocalDateTime dateCreation`
  - `LocalDateTime dateModification`

- **Méthodes :**
  - `public record MatiereResponse(String typeMatiere, double quantiteKg)`

**record** `MatiereResponse` (public)

- **Composants (record) :**
  - `String typeMatiere`
  - `double quantiteKg`

#### `modules\production\application\dosage\dto\response\StatistiquesFutsResponse.java`

**record** `StatistiquesFutsResponse` (public)

- **Composants (record) :**
  - `LocalDate debut`
  - `LocalDate fin`
  - `String periodeLibelle`
  - `int nbFutsProduits`
  - `int nbFutsNets`
  - `double poudreMaisKg`
  - `double poudreSojaKg`
  - `double poudreArachideKg`
  - `ComparaisonPeriode comparaison`
  - `int nbSessions`

- **Méthodes :**
  - `public record ComparaisonPeriode(LocalDate debutPeriodePrecedente,
        LocalDate finPeriodePrecedente,
        int nbFutsNetsPeriodePrecedente,
        double variationPourcentage)`

**record** `ComparaisonPeriode` (public)

- **Composants (record) :**
  - `LocalDate debutPeriodePrecedente`
  - `LocalDate finPeriodePrecedente`
  - `int nbFutsNetsPeriodePrecedente`
  - `double variationPourcentage`

#### `modules\production\application\dosage\mapper\SessionDosageMapper.java`

**class** `SessionDosageMapper` (@Component
public)

- **Méthodes :**
  - `public SessionDosageResponse versResponse(SessionDosage s)`
  - `public List<SessionDosageResponse> versResponseList(List<SessionDosage> list)`

#### `modules\production\application\dosage\service\DocumentsDosageService.java`

**class** `DocumentsDosageService` (@Service
public)

- **Champs :**
  - `private final RegistreDosageExportService exportService`

- **Méthodes :**
  - `public DocumentsDosageService(RegistreDosageExportService exportService)`
  - `public byte[] genererZipDocuments(Long sessionId)`

#### `modules\production\application\dosage\service\RegistreDosageExportService.java`

**class** `RegistreDosageExportService` (@Service
public)

- **Champs :**
  - `private final SessionDosageRepository repository`
  - `int rowIdx = 0`
  - `int i = 0`

- **Méthodes :**
  - `public RegistreDosageExportService(SessionDosageRepository repository)`
  - `public byte[] exportPdf(Long sessionId)`
  - `public byte[] exportExcel(Long sessionId)`

#### `modules\production\application\dosage\usecase\PredireProduitFiniDepuisFutsUseCase.java`

**class** `PredireProduitFiniDepuisFutsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private static final double POUDRE_FUT_KG = 50.0`
  - `private final ProduitRepository produitRepository`
  - `double poidsTotalPoudreKg = nbFutsNets * POUDRE_FUT_KG`
  - `List<Produit> produits`
  - `LignePrediction> predictions = new ArrayList<>()`
  - `double poidsProduitKg = poidsTotalPoudreKg`
  - `int quantiteCartons = 0`
  - `int nombreUnitesParCarton = 1`

- **Méthodes :**
  - `public PredireProduitFiniDepuisFutsUseCase(ProduitRepository produitRepository)`
  - `public PredictionProduitFiniResponse executer(int nbFutsNets, String codeProduitOptionnel)`
  - `LignePrediction> predirePourProduit(Produit produit, double poidsTotalPoudreKg)`

#### `modules\production\application\dosage\usecase\StatistiquesFutsUseCase.java`

**class** `StatistiquesFutsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final SessionDosageRepository repository`
  - `double variation = precedente.futsNets > 0
            ? ((courante.futsNets - precedente.futsNets) * 100.0 / precedente.futsNets)
            : 0.0`
  - `String periodeLibelle = debut + "" + fin`
  - `int futsProduits = 0`
  - `int futsNets = 0`
  - `double mais = 0.0`
  - `double soja = 0.0`
  - `double arachide = 0.0`

- **Méthodes :**
  - `public StatistiquesFutsUseCase(SessionDosageRepository repository)`
  - `public StatistiquesFutsResponse executer(LocalDate debut, LocalDate fin)`
  - `private PeriodeStats calculerPeriode(LocalDate debut, LocalDate fin)`
  - `private record PeriodeStats(int futsProduits, int futsNets,
        double mais, double soja, double arachide,
        int nbSessions)`

**record** `PeriodeStats` (private)

- **Composants (record) :**
  - `int futsProduits`
  - `int futsNets`
  - `double mais`
  - `double soja`
  - `double arachide`
  - `int nbSessions`

#### `modules\production\application\fiche\dto\request\EnregistrerFicheRequest.java`

**record** `EnregistrerFicheRequest` (public)

- **Composants (record) :**
  - `@NotBlank String referencePPH`
  - `@NotBlank String semaine`
  - `LocalDate date`
  - `@NotBlank String matriculeAgentProduction`
  - `@NotEmpty List<LigneFicheDto> lignes`
  - `Map<String`
  - `Integer> predictionDosage`
  - `Map<String`
  - `Integer> predictionPostes`

- **Méthodes :**
  - `public record LigneFicheDto(String codeProduit, String nomProduit, double quantiteProduite)`

**record** `LigneFicheDto` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String nomProduit`
  - `double quantiteProduite`

#### `modules\production\application\fiche\dto\request\ModifierFicheRequest.java`

**record** `ModifierFicheRequest` (public)

- **Composants (record) :**
  - `@NotEmpty List<EnregistrerFicheRequest.LigneFicheDto> lignes`

#### `modules\production\application\fiche\dto\response\FicheResponse.java`

**record** `FicheResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referencePPH`
  - `String semaine`
  - `LocalDate date`
  - `String matriculeAgentProduction`
  - `String justificationEcart`
  - `boolean verrouillee`
  - `Map<String`
  - `Integer> predictionDosage`
  - `Map<String`
  - `Integer> predictionPostes`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String codeProduit, String nomProduit, double quantiteProduite,
                                 Integer quantitePrediteDosage, Integer quantitePreditePostes, Integer ecart)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String nomProduit`
  - `double quantiteProduite`
  - `Integer quantitePrediteDosage`
  - `Integer quantitePreditePostes`
  - `Integer ecart`

#### `modules\production\application\fiche\mapper\FicheMapper.java`

**class** `FicheMapper` (@Component
public)

- **Méthodes :**
  - `public FicheResponse versResponse(FicheProduitJournaliere fiche)`

#### `modules\production\application\fiche\usecase\enregistrer\EnregistrerFicheUseCase.java`

**class** `EnregistrerFicheUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final FicheProduitRepository ficheRepository`
  - `private final PPHRepository pphRepository`
  - `private final FicheMapper ficheMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public EnregistrerFicheUseCase(FicheProduitRepository ficheRepository, PPHRepository pphRepository,
                                   FicheMapper ficheMapper, EventPublisher eventPublisher)`
  - `public FicheResponse executer(EnregistrerFicheRequest req, Utilisateur acteur)`

#### `modules\production\application\fiche\usecase\modifier\ModifierFicheUseCase.java`

**class** `ModifierFicheUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final FicheProduitRepository ficheRepository`
  - `private final PPHRepository pphRepository`
  - `private final FicheMapper ficheMapper`

- **Méthodes :**
  - `public ModifierFicheUseCase(FicheProduitRepository ficheRepository, PPHRepository pphRepository,
                                 FicheMapper ficheMapper)`
  - `public FicheResponse executer(String referencePPH, LocalDate date, ModifierFicheRequest request,
                                   Utilisateur acteur)`

#### `modules\production\application\lot\dto\request\DeclarerLotRequest.java`

**record** `DeclarerLotRequest` (public)

- **Composants (record) :**
  - `@NotBlank String referencePPH`
  - `@NotBlank String codeProduit`
  - `@NotBlank String designationProduit`
  - `double quantiteKg`
  - `int nbCartons`
  - `LocalDate dateProduction`
  - `LocalDate dlc`

#### `modules\production\application\lot\dto\response\LotResponse.java`

**record** `LotResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroLot`
  - `String referencePPH`
  - `String codeProduit`
  - `String designationProduit`
  - `double quantiteKg`
  - `int nbCartons`
  - `LocalDate dateProduction`
  - `LocalDate dlc`
  - `String matriculeChefProduction`
  - `String statut`
  - `String matriculeGestionnaireValidation`
  - `String motifRejet`

#### `modules\production\application\lot\mapper\LotMapper.java`

**class** `LotMapper` (@Component
public)

- **Méthodes :**
  - `public LotResponse versResponse(LotProduitFini lot)`

#### `modules\production\application\lot\usecase\declarer\DeclarerLotUseCase.java`

**class** `DeclarerLotUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final LotRepository lotRepository`
  - `private final LotMapper lotMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public DeclarerLotUseCase(LotRepository lotRepository, LotMapper lotMapper, EventPublisher eventPublisher)`
  - `public LotResponse executer(DeclarerLotRequest req, Utilisateur acteur)`
  - `private String genererNumeroLot(LocalDate date)`

#### `modules\production\application\of\dto\request\CreerOFRequest.java`

**record** `CreerOFRequest` (public)

- **Composants (record) :**
  - `@NotBlank String idOF`
  - `@NotBlank String referencePPH`
  - `@NotBlank String codeProduit`
  - `@NotBlank String designationProduit`
  - `@Positive int qteDemandee`
  - `LocalDate dateButoir`

#### `modules\production\application\of\dto\response\OFResponse.java`

**record** `OFResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String idOF`
  - `String referencePPH`
  - `String codeProduit`
  - `String designationProduit`
  - `int qteDemandee`
  - `int qteRealisee`
  - `LocalDate dateButoir`
  - `String statut`

#### `modules\production\application\of\mapper\OFMapper.java`

**class** `OFMapper` (@Component
public)

- **Méthodes :**
  - `public OFResponse versResponse(OrdreFabrication of)`

#### `modules\production\application\of\usecase\AnnulerOFUseCase.java`

**class** `AnnulerOFUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final OFRepository repository`
  - `private final OFMapper mapper`

- **Méthodes :**
  - `public AnnulerOFUseCase(OFRepository repository, OFMapper mapper)`
  - `public OFResponse executer(String idOF)`

#### `modules\production\application\of\usecase\CreerOFUseCase.java`

**class** `CreerOFUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final OFRepository repository`
  - `private final OFMapper mapper`

- **Méthodes :**
  - `public CreerOFUseCase(OFRepository repository, OFMapper mapper)`
  - `public OFResponse executer(CreerOFRequest req)`

#### `modules\production\application\of\usecase\DemarrerOFUseCase.java`

**class** `DemarrerOFUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final OFRepository repository`
  - `private final OFMapper mapper`

- **Méthodes :**
  - `public DemarrerOFUseCase(OFRepository repository, OFMapper mapper)`
  - `public OFResponse executer(String idOF)`

#### `modules\production\application\of\usecase\EnregistrerProductionOFUseCase.java`

**class** `EnregistrerProductionOFUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final OFRepository repository`
  - `private final OFMapper mapper`

- **Méthodes :**
  - `public EnregistrerProductionOFUseCase(OFRepository repository, OFMapper mapper)`
  - `public OFResponse executer(String idOF, int quantite)`

#### `modules\production\application\pph\dto\request\CreerPPHRequest.java`

**record** `CreerPPHRequest` (public)

- **Composants (record) :**
  - `@NotBlank String referencePPH`
  - `String referenceBC`
  - `@NotBlank String semaine`
  - `@NotNull LocalDate dateDebut`
  - `@NotNull LocalDate dateFin`
  - `@NotEmpty List<LignePPHDto> lignes`

- **Méthodes :**
  - `public record LignePPHDto(String codeProduit, String nomProduit, int objectifSemaine)`

**record** `LignePPHDto` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String nomProduit`
  - `int objectifSemaine`

#### `modules\production\application\pph\dto\request\ModifierLignesPPHRequest.java`

**record** `ModifierLignesPPHRequest` (public)

- **Composants (record) :**
  - `@NotNull List<LigneDto> lignes`

- **Méthodes :**
  - `public record LigneDto(@NotBlank String codeProduit,
        @NotNull Integer objectifSemaine)`

**record** `LigneDto` (public)

- **Composants (record) :**
  - `@NotBlank String codeProduit`
  - `@NotNull Integer objectifSemaine`

#### `modules\production\application\pph\dto\response\PPHResponse.java`

**record** `PPHResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referencePPH`
  - `String referenceBC`
  - `String semaine`
  - `LocalDate dateDebut`
  - `LocalDate dateFin`
  - `LocalDate dateSoumission`
  - `String matriculeChefProduction`
  - `String statut`
  - `List<LigneResponse> lignes`
  - `List<LigneDetailResponse> lignesDetaillees`
  - `List<RepartitionResponse> repartitionJours`

- **Méthodes :**
  - `public record LigneResponse(String codeProduit, String nomProduit, int objectifSemaine, int productionRealisee)`
  - `public record LigneDetailResponse(String codeProduit,
        String nomProduit,
        String categorieMetier,
        int quantiteCartons,
        int nombreUnites,
        double poidsUnitaireGrammes,
        String recette,
        LocalDate dateJour,
        int quantiteJour,
        double poudreMaisKg,
        double poudreSojaKg,
        double poudreArachideKg,
        int futsPrevus,
        int sachetsPrevus,
        int couverclesPrevus,
        int scellesPrevus,
        double poidsTotalGrammes)`
  - `public record RepartitionResponse(LocalDate dateJour,
        double poudreMaisKg,
        double poudreSojaKg,
        double poudreArachideKg,
        int futsPrevus,
        int sachetsPrevus,
        int couverclesPrevus,
        int scellesPrevus)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String nomProduit`
  - `int objectifSemaine`
  - `int productionRealisee`

**record** `LigneDetailResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String nomProduit`
  - `String categorieMetier`
  - `int quantiteCartons`
  - `int nombreUnites`
  - `double poidsUnitaireGrammes`
  - `String recette`
  - `LocalDate dateJour`
  - `int quantiteJour`
  - `double poudreMaisKg`
  - `double poudreSojaKg`
  - `double poudreArachideKg`
  - `int futsPrevus`
  - `int sachetsPrevus`
  - `int couverclesPrevus`
  - `int scellesPrevus`
  - `double poidsTotalGrammes`

**record** `RepartitionResponse` (public)

- **Composants (record) :**
  - `LocalDate dateJour`
  - `double poudreMaisKg`
  - `double poudreSojaKg`
  - `double poudreArachideKg`
  - `int futsPrevus`
  - `int sachetsPrevus`
  - `int couverclesPrevus`
  - `int scellesPrevus`

#### `modules\production\application\pph\mapper\PPHMapper.java`

**class** `PPHMapper` (@Component
public)

- **Méthodes :**
  - `public PPHResponse versResponse(PlanProductionHebdomadaire pph)`

#### `modules\production\application\pph\service\PlanificationProductionService.java`

**class** `PlanificationProductionService` (@Service
public)

- **Champs :**
  - `private static final double POUDRE_FUT_KG = 50.0`
  - `private static final Locale LOCALE_FR = Locale.FRANCE`
  - `List<LignePPH> lignesPPH = new ArrayList<>()`
  - `List<LignePlanDetail> lignesDetaillees = new ArrayList<>()`
  - `List<RepartitionJournaliere> repartitionJours = new ArrayList<>()`
  - `int objectif = quantiteCartons`
  - `List<LignePlanDetail> resultat = new ArrayList<>()`
  - `int base = quantiteCartons / nbJours`
  - `int reste = quantiteCartons % nbJours`
  - `int i = 0`
  - `i < nbJours`
  - `int qteJour = base + (i < reste ? 1 : 0)`
  - `List<LocalDate> jours = new ArrayList<>()`
  - `LocalDate courant = debut`

- **Méthodes :**
  - `public ResultatPlanification calculer(BonCommande bc, Map<String, Produit> produitsParCode)`
  - `private Optional<Conditionnement> trouverConditionnement(Produit produit, String sku)`
  - `private List<LignePlanDetail> repartirSurJoursOuvres(String codeProduit, String designation, String categorieMetier,
                                                         int quantiteCartons, int nombreUnites,
                                                         double poidsUnitaireGrammes, RecetteBouillie recette,
                                                         List<LocalDate> joursOuvres)`
  - `private LocalDate debutSemaine(LocalDate date)`
  - `private List<LocalDate> joursOuvresEntre(LocalDate debut, LocalDate fin)`
  - `public record ResultatPlanification(String semaine,
        LocalDate dateDebut,
        LocalDate dateFin,
        List<LignePPH> lignesPPH,
        List<LignePlanDetail> lignesDetaillees,
        List<RepartitionJournaliere> repartitionJours)`

**record** `ResultatPlanification` (public)

- **Composants (record) :**
  - `String semaine`
  - `LocalDate dateDebut`
  - `LocalDate dateFin`
  - `List<LignePPH> lignesPPH`
  - `List<LignePlanDetail> lignesDetaillees`
  - `List<RepartitionJournaliere> repartitionJours`

#### `modules\production\application\pph\usecase\CreerPPHUseCase.java`

**class** `CreerPPHUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final PPHRepository pphRepository`
  - `private final PPHMapper pphMapper`
  - `PlanProductionHebdomadaire pph`

- **Méthodes :**
  - `public CreerPPHUseCase(PPHRepository pphRepository, PPHMapper pphMapper)`
  - `public PPHResponse executer(CreerPPHRequest request, Utilisateur acteur)`

#### `modules\production\application\pph\usecase\GenererPPHDepuisBCUseCase.java`

**class** `GenererPPHDepuisBCUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final PPHRepository pphRepository`
  - `private final ProduitRepository produitRepository`
  - `private final PlanificationProductionService planificationService`

- **Méthodes :**
  - `public GenererPPHDepuisBCUseCase(PPHRepository pphRepository,
                                     ProduitRepository produitRepository,
                                     PlanificationProductionService planificationService)`
  - `public PlanProductionHebdomadaire executer(BonCommande bc, Utilisateur acteur)`
  - `private String genererReferencePPH(String referenceBC, LocalDate dateDebut)`

#### `modules\production\application\pph\usecase\ModifierLignePPHUseCase.java`

**class** `ModifierLignePPHUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final PPHRepository pphRepository`

- **Méthodes :**
  - `public ModifierLignePPHUseCase(PPHRepository pphRepository)`
  - `public PlanProductionHebdomadaire executer(String referencePPH, List<LigneModifiable> lignes, Utilisateur acteur)`
  - `public record LigneModifiable(String codeProduit, int objectifSemaine)`

**record** `LigneModifiable` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `int objectifSemaine`

#### `modules\production\application\pph\usecase\ValiderPPHUseCase.java`

**class** `ValiderPPHUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final PPHRepository pphRepository`
  - `private final PPHMapper pphMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public ValiderPPHUseCase(PPHRepository pphRepository, PPHMapper pphMapper, EventPublisher eventPublisher)`
  - `public PPHResponse executer(String referencePPH, Utilisateur acteur)`

#### `modules\production\application\rapport\usecase\GenererRapportComparatifBCUseCase.java`

**class** `GenererRapportComparatifBCUseCase` (@Service
public)

- **Champs :**
  - `private final PPHRepository pphRepository`
  - `private final LotRepository lotRepository`
  - `Map<String, LigneComparatif> lignesParProduit = new LinkedHashMap<>()`
  - `public final String codeProduit`
  - `public final String designation`
  - `public final int quantitePlanifiee`
  - `public int quantiteProduite`
  - `public double quantiteLotsValidesKg`
  - `public int nbCartonsValides`

- **Méthodes :**
  - `public GenererRapportComparatifBCUseCase(PPHRepository pphRepository, LotRepository lotRepository)`
  - `public RapportComparatifBC executer(String referenceBC)`
  - `public record RapportComparatifBC(String referenceBC,
        List<LigneComparatif> lignes,
        int totalQuantitePlanifiee,
        int totalQuantiteProduite,
        int totalCartonsValides,
        double totalKgValides)`
  - `public int tauxAvancementGlobal()`
  - `public LigneComparatif(String codeProduit, String designation, int quantitePlanifiee,
                               int quantiteProduite, int nbCartonsValides)`
  - `public int tauxAvancement()`
  - `public int ecart()`

**record** `RapportComparatifBC` (public)

- **Composants (record) :**
  - `String referenceBC`
  - `List<LigneComparatif> lignes`
  - `int totalQuantitePlanifiee`
  - `int totalQuantiteProduite`
  - `int totalCartonsValides`
  - `double totalKgValides`

- **Méthodes :**
  - `public int tauxAvancementGlobal()`

**class** `LigneComparatif` (public static)

- **Champs :**
  - `public final String codeProduit`
  - `public final String designation`
  - `public final int quantitePlanifiee`
  - `public int quantiteProduite`
  - `public double quantiteLotsValidesKg`
  - `public int nbCartonsValides`

- **Méthodes :**
  - `public LigneComparatif(String codeProduit, String designation, int quantitePlanifiee,
                               int quantiteProduite, int nbCartonsValides)`
  - `public int tauxAvancement()`
  - `public int ecart()`

#### `modules\production\application\rapport\usecase\GenererRapportHebdomadaireUseCase.java`

**class** `GenererRapportHebdomadaireUseCase` (@Service
public)

- **Champs :**
  - `private final FicheProduitRepository ficheRepository`
  - `private final SessionBroyageRepository broyageRepository`
  - `private final SessionDosageRepository dosageRepository`
  - `private final LotRepository lotRepository`
  - `private final PPHRepository pphRepository`
  - `Map<String, Integer> quantitesParReference = new HashMap<>()`
  - `Map<String, Double> pertesParTypePoudre = new HashMap<>()`
  - `Map<String, Double> quantiteBroyeeParTypePoudre = new HashMap<>()`
  - `int totalFutsNets = totalFutsProduits`
  - `Map<String, Integer> tauxAvancementParBC = new HashMap<>()`
  - `Map<String, List<PlanProductionHebdomadaire>> pphsParBC = new HashMap<>()`
  - `double totalBrut = totalBroye + totalPertes`
  - `Map<String, Double> result = new HashMap<>()`

- **Méthodes :**
  - `public GenererRapportHebdomadaireUseCase(FicheProduitRepository ficheRepository,
                                              SessionBroyageRepository broyageRepository,
                                              SessionDosageRepository dosageRepository,
                                              LotRepository lotRepository,
                                              PPHRepository pphRepository)`
  - `public RapportHebdomadaireProduction executer(LocalDate debutSemaine, LocalDate finSemaine)`
  - `public record RapportHebdomadaireProduction(LocalDate debutSemaine,
        LocalDate finSemaine,
        Map<String, Integer> quantitesParReference,
        Map<String, Integer> tauxAvancementParBC,
        Map<String, Double> pertesParTypePoudre,
        Map<String, Double> quantiteBroyeeParTypePoudre,
        int totalFutsProduits,
        int totalFutsNets,
        int totalLotsValides,
        double totalQuantiteLots,
        int nbFiches,
        int nbSessionsBroyage,
        int nbSessionsDosage)`
  - `public double getTauxPertesGlobal()`
  - `public Map<String, Double> getPctPertesParTypePoudre()`

**record** `RapportHebdomadaireProduction` (public)

- **Composants (record) :**
  - `LocalDate debutSemaine`
  - `LocalDate finSemaine`
  - `Map<String`
  - `Integer> quantitesParReference`
  - `Map<String`
  - `Integer> tauxAvancementParBC`
  - `Map<String`
  - `Double> pertesParTypePoudre`
  - `Map<String`
  - `Double> quantiteBroyeeParTypePoudre`
  - `int totalFutsProduits`
  - `int totalFutsNets`
  - `int totalLotsValides`
  - `double totalQuantiteLots`
  - `int nbFiches`
  - `int nbSessionsBroyage`
  - `int nbSessionsDosage`

- **Champs :**
  - `double totalBrut = totalBroye + totalPertes`
  - `Map<String, Double> result = new HashMap<>()`

- **Méthodes :**
  - `public double getTauxPertesGlobal()`
  - `public Map<String, Double> getPctPertesParTypePoudre()`

#### `modules\production\application\rapport\usecase\GenererRapportLotsUseCase.java`

**class** `GenererRapportLotsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final LotRepository lotRepository`
  - `private final LotMapper lotMapper`
  - `private final TableExporter tableExporter`
  - `String titre = ""`

- **Méthodes :**
  - `public GenererRapportLotsUseCase(LotRepository lotRepository, LotMapper lotMapper, TableExporter tableExporter)`
  - `public RapportFichier executer(LocalDate debut, LocalDate fin, FormatRapport format)`
  - `private List<List<String>> lignes(List<LotResponse> lots)`
  - `private String n(String v)`

#### `modules\production\application\registre\dto\request\EnregistrerRegistreRequest.java`

**record** `EnregistrerRegistreRequest` (public)

- **Composants (record) :**
  - `@NotBlank String referencePPH`
  - `LocalDate date`
  - `@NotEmpty List<LigneRegistreRequest> lignes`

- **Méthodes :**
  - `public record LigneRegistreRequest(@NotBlank String matriculeEmploye,
        @NotBlank String nomEmploye,
        @NotBlank String poste,
        boolean present,
        String motifAbsence,
        String heureArrivee,
        double qteRealisee,
        String unite)`

**record** `LigneRegistreRequest` (public)

- **Composants (record) :**
  - `@NotBlank String matriculeEmploye`
  - `@NotBlank String nomEmploye`
  - `@NotBlank String poste`
  - `boolean present`
  - `String motifAbsence`
  - `String heureArrivee`
  - `double qteRealisee`
  - `String unite`

#### `modules\production\application\registre\dto\response\RegistreProductionResponse.java`

**record** `RegistreProductionResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String referencePPH`
  - `LocalDate date`
  - `String matriculeChefProduction`
  - `List<LigneRegistreResponse> lignes`
  - `long nbPresents`
  - `long nbAbsents`
  - `LocalDateTime dateCreation`
  - `LocalDateTime dateModification`

- **Méthodes :**
  - `public record LigneRegistreResponse(String matriculeEmploye,
        String nomEmploye,
        String poste,
        boolean present,
        String motifAbsence,
        String heureArrivee,
        double qteRealisee,
        String unite)`

**record** `LigneRegistreResponse` (public)

- **Composants (record) :**
  - `String matriculeEmploye`
  - `String nomEmploye`
  - `String poste`
  - `boolean present`
  - `String motifAbsence`
  - `String heureArrivee`
  - `double qteRealisee`
  - `String unite`

#### `modules\production\application\registre\mapper\RegistreMapper.java`

**class** `RegistreMapper` (@Component
public)

- **Méthodes :**
  - `public RegistreProductionResponse versResponse(RegistreProduction r)`

#### `modules\production\application\registre\usecase\EnregistrerRegistreUseCase.java`

**class** `EnregistrerRegistreUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final RegistreProductionRepository repository`
  - `private final RegistreMapper mapper`

- **Méthodes :**
  - `public EnregistrerRegistreUseCase(RegistreProductionRepository repository, RegistreMapper mapper)`
  - `public RegistreProductionResponse executer(EnregistrerRegistreRequest req, Utilisateur utilisateur)`

#### `modules\production\domain\affectation\aggregate\AffectationJournaliere.java`

**class** `AffectationJournaliere` (public)

- **Champs :**
  - `private Long id`
  - `private final String referencePPH`
  - `private final LocalDate date`
  - `private final String matriculeAgent`
  - `private final List<LigneAffectationPoste> lignes`
  - `private final List<SaisieRealisationPoste> saisies`
  - `private StatutAffectation statut`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `AffectationJournaliere that = (AffectationJournaliere) o`

- **Méthodes :**
  - `private AffectationJournaliere(String referencePPH, LocalDate date,
                                    String matriculeAgent, LocalDateTime dateCreation)`
  - `public static AffectationJournaliere creer(String referencePPH, LocalDate date,
                                                String matriculeAgent,
                                                List<LigneAffectationPoste> lignes)`
  - `public static AffectationJournaliere reconstituer(Long id, String referencePPH, LocalDate date,
                                                       String matriculeAgent,
                                                       List<LigneAffectationPoste> lignes,
                                                       List<SaisieRealisationPoste> saisies,
                                                       StatutAffectation statut,
                                                       LocalDateTime dateCreation,
                                                       LocalDateTime dateModification, Long version)`
  - `public void saisirRealisation(SaisieRealisationPoste saisie)`
  - `public void valider()`
  - `public List<SaisieRealisationPoste> getClassementPostes()`
  - `public Optional<LigneAffectationPoste> getLignePourPoste(CodePoste poste)`
  - `public Long getId()`
  - `public String getReferencePPH()`
  - `public LocalDate getDate()`
  - `public String getMatriculeAgent()`
  - `public List<LigneAffectationPoste> getLignes()`
  - `public List<SaisieRealisationPoste> getSaisies()`
  - `public StatutAffectation getStatut()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\production\domain\affectation\entity\DetailsRealisationPoste.java`

**class** `DetailsRealisationPoste` (public)

- **Champs :**
  - `private final Integer nbRegimesEpluches`
  - `private final Integer nbRegimesDecoupes`
  - `private final Integer nbSceauxEtiquetes`
  - `private final Integer nbEtiquettesDatees`
  - `private final Integer nbSachetsDates`
  - `private final Integer nbCartonsProduits`
  - `private final Double kgChipsProduits`

- **Méthodes :**
  - `public static DetailsRealisationPoste vide()`
  - `public DetailsRealisationPoste(Integer nbRegimesEpluches, Integer nbRegimesDecoupes,
                                    Integer nbSceauxEtiquetes, Integer nbEtiquettesDatees,
                                    Integer nbSachetsDates, Integer nbCartonsProduits,
                                    Double kgChipsProduits)`
  - `public Integer getNbRegimesEpluches()`
  - `public Integer getNbRegimesDecoupes()`
  - `public Integer getNbSceauxEtiquetes()`
  - `public Integer getNbEtiquettesDatees()`
  - `public Integer getNbSachetsDates()`
  - `public Integer getNbCartonsProduits()`
  - `public Double getKgChipsProduits()`

#### `modules\production\domain\affectation\entity\LigneAffectationPoste.java`

**class** `LigneAffectationPoste` (public)

- **Champs :**
  - `private final CodePoste poste`
  - `private final List<String> matriculesEmployes`
  - `private final DureeAffectation duree`
  - `private final double objectifAjuste`

- **Méthodes :**
  - `public LigneAffectationPoste(CodePoste poste, List<String> matriculesEmployes, DureeAffectation duree)`
  - `public int getEffectifReel()`
  - `public CodePoste getPoste()`
  - `public List<String> getMatriculesEmployes()`
  - `public DureeAffectation getDuree()`
  - `public double getObjectifAjuste()`

#### `modules\production\domain\affectation\entity\ProductionIndividuelle.java`

**class** `ProductionIndividuelle` (public)

- **Champs :**
  - `private final String matriculeEmploye`
  - `private final String nomEmploye`
  - `private final CodePoste poste`
  - `private final double quantiteRealisee`

- **Méthodes :**
  - `public ProductionIndividuelle(String matriculeEmploye, String nomEmploye,
                                   CodePoste poste, double quantiteRealisee)`
  - `public String getMatriculeEmploye()`
  - `public String getNomEmploye()`
  - `public CodePoste getPoste()`
  - `public double getQuantiteRealisee()`

#### `modules\production\domain\affectation\entity\SaisieRealisationPoste.java`

**class** `SaisieRealisationPoste` (public)

- **Champs :**
  - `private final CodePoste poste`
  - `private final double quantiteRealisee`
  - `private final double objectifAjuste`
  - `private final List<ProductionIndividuelle> productionsIndividuelles`
  - `private final DetailsRealisationPoste details`
  - `private final List<String> warnings`
  - `private final LocalDateTime dateEnregistrement`

- **Méthodes :**
  - `public SaisieRealisationPoste(CodePoste poste, double quantiteRealisee, double objectifAjuste,
                                   List<ProductionIndividuelle> productionsIndividuelles,
                                   DetailsRealisationPoste details,
                                   List<String> warnings)`
  - `public double getPctRealisation()`
  - `public UniteProduction getUnite()`
  - `public CodePoste getPoste()`
  - `public double getQuantiteRealisee()`
  - `public double getObjectifAjuste()`
  - `public List<ProductionIndividuelle> getProductionsIndividuelles()`
  - `public DetailsRealisationPoste getDetails()`
  - `public List<String> getWarnings()`
  - `public LocalDateTime getDateEnregistrement()`

#### `modules\production\domain\affectation\enums\CodePoste.java`

**enum** `CodePoste` (public)

- **Constantes :**
  - `{

    CARAMEL(                            "",  "",                                  1, 8.2,    65.6,   UniteProduction.KG)`
  - `CROQUETTE(                          "",  "",                                1, 3.6,    28.8,   UniteProduction.KG)`
  - `ARACHIDE_ENROBEE(                   "",  "",                         1, 6.1,    48.8,   UniteProduction.KG)`
  - `CHIPS_PLANTAIN(                     "",  "",                           2, 7.5,    60.0,   UniteProduction.KG)`
  - `CONDITIONNEMENT_MINI_GRIGNOTER(     "",  "",   3, 30.0,   2400.0, UniteProduction.SACHET)`
  - `CONDITIONNEMENT_GRIGNOTER(          "",  "",              2, 200.0,  1600.0, UniteProduction.SACHET)`
  - `CONDITIONNEMENT_SCEAUX_2L(          "",  "",                4, 100.0,  800.0,  UniteProduction.SCEAU)`
  - `CONDITIONNEMENT_RASS(               "","",                     2, 625.0,  5000.0, UniteProduction.SACHET)`
  - `CONDITIONNEMENT_PRESTIGE_CHAP(      "",  "",           2, 50.0,   400.0,  UniteProduction.ETUI)`
  - `ETIQUETAGE(                         "",  "",                               1, 225.0,  1800.0, UniteProduction.SCEAU)`
  - `DATAGE(                             "",  "",                                   1, 313.0,  2500.0, UniteProduction.ETIQUETTE)`

- **Champs :**
  - `private final String code`
  - `private final String libelle`
  - `private final int effectifReference`
  - `private final double productionParHeure`
  - `private final double productionJournaliere`
  - `private final UniteProduction unite`

- **Méthodes :**
  - `public double calculerObjectifAjuste(int effectifReel, DureeAffectation duree)`
  - `public String getCode()`
  - `public String getLibelle()`
  - `public int getEffectifReference()`
  - `public double getProductionParHeure()`
  - `public double getProductionJournaliere()`
  - `public UniteProduction getUnite()`
  - `public boolean hasTauxReference()`

#### `modules\production\domain\affectation\enums\DureeAffectation.java`

**enum** `DureeAffectation` (public)

- **Constantes :**
  - `{

    DEMI_JOURNEE("", 0.5)`
  - `JOURNEE_COMPLETE("", 1.0)`

- **Champs :**
  - `private final String libelle`
  - `private final double coefficient`

- **Méthodes :**
  - `public String getLibelle()`
  - `public double getCoefficient()`

#### `modules\production\domain\affectation\enums\StatutAffectation.java`

**enum** `StatutAffectation` (public)

#### `modules\production\domain\affectation\enums\UniteProduction.java`

**enum** `UniteProduction` (public)

- **Constantes :**
  - `{
    KG("")`
  - `SACHET("")`
  - `CARTON("")`
  - `SCEAU("")`
  - `ETUI("")`
  - `ETIQUETTE("")`
  - `REGIME("")`
  - `FUT("")`
  - `AUCUNE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\production\domain\affectation\repository\AffectationJournaliereRepository.java`

**interface** `AffectationJournaliereRepository` (public)

#### `modules\production\domain\besoin\enums\StatutBesoin.java`

**enum** `StatutBesoin` (public)

- **Constantes :**
  - `{
    EN_ATTENTE("")`
  - `VALIDE("")`
  - `REJETE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\production\domain\besoin\enums\TypeBesoin.java`

**enum** `TypeBesoin` (public)

- **Constantes :**
  - `{
    MATIERE_PREMIERE("")`
  - `EMBALLAGE("")`
  - `CONSOMMABLE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\production\domain\besoin\model\ExpressionBesoin.java`

**class** `ExpressionBesoin` (public)

- **Champs :**
  - `private Long id`
  - `private final String numero`
  - `private final String referencePPH`
  - `private final TypeBesoin type`
  - `private final String codeProduit`
  - `private final String designationProduit`
  - `private final double quantiteDemandee`
  - `private final String unite`
  - `private final String matriculeDemandeur`
  - `private final LocalDate dateBesoin`
  - `private final String motif`
  - `private StatutBesoin statut`
  - `private String matriculeValidateur`
  - `private String motifRejet`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private ExpressionBesoin(String numero, String referencePPH, TypeBesoin type, String codeProduit,
                             String designationProduit, double quantiteDemandee, String unite,
                             String matriculeDemandeur, LocalDate dateBesoin, String motif,
                             LocalDateTime dateCreation)`
  - `public static ExpressionBesoin signaler(String numero, String referencePPH, TypeBesoin type,
                                             String codeProduit, String designationProduit,
                                             double quantiteDemandee, String unite,
                                             String matriculeDemandeur, LocalDate dateBesoin, String motif)`
  - `public static ExpressionBesoin reconstituer(Long id, String numero, String referencePPH, TypeBesoin type,
                                                 String codeProduit, String designationProduit,
                                                 double quantiteDemandee, String unite,
                                                 String matriculeDemandeur, LocalDate dateBesoin,
                                                 String motif, StatutBesoin statut, String matriculeValidateur,
                                                 String motifRejet, LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version)`
  - `public void valider(String matriculeValidateur)`
  - `public void rejeter(String matriculeValidateur, String motifRejet)`
  - `public Long getId()`
  - `public String getNumero()`
  - `public String getReferencePPH()`
  - `public TypeBesoin getType()`
  - `public String getCodeProduit()`
  - `public String getDesignationProduit()`
  - `public double getQuantiteDemandee()`
  - `public String getUnite()`
  - `public String getMatriculeDemandeur()`
  - `public LocalDate getDateBesoin()`
  - `public String getMotif()`
  - `public StatutBesoin getStatut()`
  - `public String getMatriculeValidateur()`
  - `public String getMotifRejet()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\production\domain\besoin\repository\ExpressionBesoinRepository.java`

**interface** `ExpressionBesoinRepository` (public)

#### `modules\production\domain\broyage\aggregate\SessionBroyage.java`

**class** `SessionBroyage` (public)

- **Champs :**
  - `private Long id`
  - `private final String referencePPH`
  - `private final LocalDate date`
  - `private final TypePoudre typePoudre`
  - `private final double objectifJournalierKg`
  - `private final String matriculeChefMachiniste`
  - `private final List<ObjectifMachiniste> objectifsMachinistes`
  - `private final List<RealisationMachiniste> realisations`
  - `private Double quantiteNetteBroyeeKg`
  - `private Double pertesKg`
  - `private double depassementAutoriseKg`
  - `private String justificatifNonValidation`
  - `private StatutSessionBroyage statut`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `double totalAutorise = objectifJournalierKg + depassementAutoriseKg`
  - `double brut = quantiteNetteBroyeeKg + pertesKg`
  - `SessionBroyage that = (SessionBroyage) o`

- **Méthodes :**
  - `private SessionBroyage(String referencePPH, LocalDate date, TypePoudre typePoudre,
                            double objectifJournalierKg, String matriculeChefMachiniste,
                            LocalDateTime dateCreation)`
  - `public static SessionBroyage ouvrir(String referencePPH, LocalDate date, TypePoudre typePoudre,
                                         double objectifJournalierKg, String matriculeChefMachiniste,
                                         List<ObjectifMachiniste> objectifs)`
  - `public static SessionBroyage reconstituer(Long id, String referencePPH, LocalDate date,
                                               TypePoudre typePoudre, double objectifJournalierKg,
                                               String matriculeChefMachiniste,
                                               List<ObjectifMachiniste> objectifs,
                                               List<RealisationMachiniste> realisations,
                                               Double quantiteNetteBroyeeKg, Double pertesKg,
                                               double depassementAutoriseKg, String justificatifNonValidation,
                                               StatutSessionBroyage statut, LocalDateTime dateCreation,
                                               LocalDateTime dateModification, Long version)`
  - `public void enregistrerRealisation(RealisationMachiniste realisation)`
  - `public void cloturer(double quantiteNetteBroyeeKg, double pertesKg)`
  - `public void autoriserDepassement(double quantiteSupplementaireKg)`
  - `public void justifierNonValidation(String justificatif)`
  - `public boolean estJourneeValidee()`
  - `public Double getPctPertes()`
  - `public double totalRealiseKg()`
  - `public Long getId()`
  - `public String getReferencePPH()`
  - `public LocalDate getDate()`
  - `public TypePoudre getTypePoudre()`
  - `public double getObjectifJournalierKg()`
  - `public String getMatriculeChefMachiniste()`
  - `public List<ObjectifMachiniste> getObjectifsMachinistes()`
  - `public List<RealisationMachiniste> getRealisations()`
  - `public Double getQuantiteNetteBroyeeKg()`
  - `public Double getPertesKg()`
  - `public double getDepassementAutoriseKg()`
  - `public String getJustificatifNonValidation()`
  - `public StatutSessionBroyage getStatut()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\production\domain\broyage\entity\ObjectifMachiniste.java`

**class** `ObjectifMachiniste` (public)

- **Champs :**
  - `private final String machinisteMatricule`
  - `private final String nomMachiniste`
  - `private final OperationBroyage operation`
  - `private final TypePoudre typePoudre`
  - `private final double objectifKg`

- **Méthodes :**
  - `public ObjectifMachiniste(String machinisteMatricule, String nomMachiniste,
                              OperationBroyage operation, TypePoudre typePoudre, double objectifKg)`
  - `public String getMachinisteMatricule()`
  - `public String getNomMachiniste()`
  - `public OperationBroyage getOperation()`
  - `public TypePoudre getTypePoudre()`
  - `public double getObjectifKg()`

#### `modules\production\domain\broyage\entity\RealisationMachiniste.java`

**class** `RealisationMachiniste` (public)

- **Champs :**
  - `private final String machinisteMatricule`
  - `private final String nomMachiniste`
  - `private final OperationBroyage operation`
  - `private final TypePoudre typePoudre`
  - `private final double objectifKg`
  - `private final double realiseKg`

- **Méthodes :**
  - `public RealisationMachiniste(String machinisteMatricule, String nomMachiniste,
                                 OperationBroyage operation, TypePoudre typePoudre,
                                 double objectifKg, double realiseKg)`
  - `public double getPctRealisation()`
  - `public String getMachinisteMatricule()`
  - `public String getNomMachiniste()`
  - `public OperationBroyage getOperation()`
  - `public TypePoudre getTypePoudre()`
  - `public double getObjectifKg()`
  - `public double getRealiseKg()`

#### `modules\production\domain\broyage\enums\OperationBroyage.java`

**enum** `OperationBroyage` (public)

- **Constantes :**
  - `{

    ECRASAGE("")`
  - `TRIAGE("")`
  - `TAMISAGE("")`
  - `DEPULPAGE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\production\domain\broyage\enums\StatutSessionBroyage.java`

**enum** `StatutSessionBroyage` (public)

#### `modules\production\domain\broyage\enums\TypePoudre.java`

**enum** `TypePoudre` (public)

- **Constantes :**
  - `{

    MAIS("")`
  - `SOJA("")`
  - `ARACHIDE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\production\domain\broyage\repository\SessionBroyageRepository.java`

**interface** `SessionBroyageRepository` (public)

#### `modules\production\domain\dosage\aggregate\SessionDosage.java`

**class** `SessionDosage` (public)

- **Champs :**
  - `private Long id`
  - `private final String referencePPH`
  - `private final LocalDate date`
  - `private final String matriculeAgentDoseur`
  - `private final List<MatierePremiereUtilisee> matieresUtilisees`
  - `private final List<String> machinesMobilisees`
  - `private int nbFutsProduits`
  - `private String justificationEcart`
  - `private boolean validee`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final String typeMatiere`
  - `private final double quantiteKg`

- **Méthodes :**
  - `private SessionDosage(String referencePPH, LocalDate date, String matriculeAgentDoseur,
                           LocalDateTime dateCreation)`
  - `public static SessionDosage ouvrir(String referencePPH, LocalDate date, String matriculeAgentDoseur)`
  - `public static SessionDosage reconstituer(Long id, String referencePPH, LocalDate date,
                                              String matriculeAgentDoseur,
                                              List<MatierePremiereUtilisee> matieresUtilisees,
                                              List<String> machinesMobilisees,
                                              int nbFutsProduits,
                                              String justificationEcart, boolean validee,
                                              LocalDateTime dateCreation,
                                              LocalDateTime dateModification, Long version)`
  - `public void enregistrerMatiereUtilisee(String typeMatiere, double quantiteKg)`
  - `public void enregistrerMachine(String machineId)`
  - `public void enregistrerFuts(int nbProduits)`
  - `public void justifierEcart(String justification)`
  - `public void valider()`
  - `public void cloturer()`
  - `public boolean isCloturee()`
  - `public int getNbFutsNets()`
  - `public boolean aEcartAvecPrevision(int nbFutsPrevus)`
  - `public boolean necessiteJustification(int nbFutsPrevus)`
  - `public Long getId()`
  - `public String getReferencePPH()`
  - `public LocalDate getDate()`
  - `public String getMatriculeAgentDoseur()`
  - `public List<MatierePremiereUtilisee> getMatieresUtilisees()`
  - `public List<String> getMachinesMobilisees()`
  - `public int getNbFutsProduits()`
  - `public String getJustificationEcart()`
  - `public boolean isValidee()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `public MatierePremiereUtilisee(String typeMatiere, double quantiteKg)`
  - `public String getTypeMatiere()`
  - `public double getQuantiteKg()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

**class** `MatierePremiereUtilisee` (public static)

- **Champs :**
  - `private final String typeMatiere`
  - `private final double quantiteKg`

- **Méthodes :**
  - `public MatierePremiereUtilisee(String typeMatiere, double quantiteKg)`
  - `public String getTypeMatiere()`
  - `public double getQuantiteKg()`

#### `modules\production\domain\dosage\repository\SessionDosageRepository.java`

**interface** `SessionDosageRepository` (public)

#### `modules\production\domain\fiche\aggregate\FicheProduitJournaliere.java`

**class** `FicheProduitJournaliere` (public)

- **Champs :**
  - `private Long id`
  - `private final String referencePPH`
  - `private final String semaine`
  - `private final LocalDate date`
  - `private final String matriculeAgentProduction`
  - `private final List<LigneFicheProduit> lignes`
  - `private final Map<String, Integer> produitsRealises`
  - `private final Map<String, Integer> predictionDosage`
  - `private final Map<String, Integer> predictionPostes`
  - `private String justificationEcart`
  - `private boolean verrouillee`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`
  - `Set<String> codes = new HashSet<>()`
  - `Set<String> codes = new HashSet<>()`
  - `FicheProduitJournaliere that = (FicheProduitJournaliere) o`

- **Méthodes :**
  - `private FicheProduitJournaliere(String referencePPH, String semaine, LocalDate date,
                                     String matriculeAgentProduction,
                                     Map<String, Integer> predictionDosage,
                                     Map<String, Integer> predictionPostes,
                                     LocalDateTime dateCreation)`
  - `public static FicheProduitJournaliere enregistrer(String referencePPH, String semaine,
                                                       LocalDate date, String matriculeAgentProduction,
                                                       List<LigneFicheProduit> lignes,
                                                       Map<String, Integer> predictionDosage,
                                                       Map<String, Integer> predictionPostes)`
  - `public static FicheProduitJournaliere reconstituer(Long id, String referencePPH, String semaine,
                                                        LocalDate date, String matriculeAgentProduction,
                                                        List<LigneFicheProduit> lignes,
                                                        Map<String, Integer> produitsRealises,
                                                        Map<String, Integer> predictionDosage,
                                                        Map<String, Integer> predictionPostes,
                                                        String justificationEcart, boolean verrouillee,
                                                        LocalDateTime dateCreation,
                                                        LocalDateTime dateModification, Long version)`
  - `public int calculerEcart(String codeProduit)`
  - `public double totalProduit()`
  - `public void modifierLignes(List<LigneFicheProduit> nouvellesLignes)`
  - `public void verrouiller()`
  - `public void definirJustificationEcart(String justification)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getReferencePPH()`
  - `public String getSemaine()`
  - `public LocalDate getDate()`
  - `public String getMatriculeAgentProduction()`
  - `public List<LigneFicheProduit> getLignes()`
  - `public Map<String, Integer> getProduitsRealises()`
  - `public Map<String, Integer> getPredictionDosage()`
  - `public Map<String, Integer> getPredictionPostes()`
  - `public String getJustificationEcart()`
  - `public boolean isVerrouillee()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\production\domain\fiche\entity\LigneFicheProduit.java`

**class** `LigneFicheProduit` (public)

- **Champs :**
  - `private final String codeProduit`
  - `private final String nomProduit`
  - `private final double quantiteProduite`

- **Méthodes :**
  - `public LigneFicheProduit(String codeProduit, String nomProduit, double quantiteProduite)`
  - `public String getCodeProduit()`
  - `public String getNomProduit()`
  - `public double getQuantiteProduite()`

#### `modules\production\domain\fiche\events\FicheEnregistreeEvent.java`

**class** `FicheEnregistreeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String referencePPH`
  - `private final LocalDate date`
  - `private final String matriculeAgent`
  - `private final int nombreLignes`

- **Méthodes :**
  - `public FicheEnregistreeEvent(String referencePPH, LocalDate date, String matriculeAgent, int nombreLignes)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReferencePPH()`
  - `public LocalDate getDate()`
  - `public String getMatriculeAgent()`
  - `public int getNombreLignes()`

#### `modules\production\domain\fiche\events\FicheVerrouilleeEvent.java`

**class** `FicheVerrouilleeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String referencePPH`
  - `private final LocalDate date`
  - `private final String matriculeAgentProduction`

- **Méthodes :**
  - `public FicheVerrouilleeEvent(String referencePPH, LocalDate date, String matriculeAgentProduction)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReferencePPH()`
  - `public LocalDate getDate()`
  - `public String getMatriculeAgentProduction()`

#### `modules\production\domain\fiche\repository\FicheProduitRepository.java`

**interface** `FicheProduitRepository` (public)

#### `modules\production\domain\fiche\specification\FicheSpecification.java`

**class** `FicheSpecification` (public final)

- **Méthodes :**
  - `private FicheSpecification()`
  - `public static boolean peutEnregistrer(Role role)`
  - `public static boolean peutConsulter(Role role)`

#### `modules\production\domain\lot\aggregate\LotProduitFini.java`

**class** `LotProduitFini` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroLot`
  - `private final String referencePPH`
  - `private final String codeProduit`
  - `private final String designationProduit`
  - `private final double quantiteKg`
  - `private final int nbCartons`
  - `private final LocalDate dateProduction`
  - `private final LocalDate dlc`
  - `private final String matriculeChefProduction`
  - `private StatutLot statut`
  - `private String matriculeGestionnaireValidation`
  - `private String motifRejet`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private LotProduitFini(String numeroLot, String referencePPH, String codeProduit,
                            String designationProduit, double quantiteKg, int nbCartons,
                            LocalDate dateProduction, LocalDate dlc, String matriculeChefProduction,
                            LocalDateTime dateCreation)`
  - `public static LotProduitFini declarer(String numeroLot, String referencePPH, String codeProduit,
                                           String designationProduit, double quantiteKg, int nbCartons,
                                           LocalDate dateProduction, LocalDate dlc,
                                           String matriculeChefProduction)`
  - `static LocalDate appliquerRegleDatage(LocalDate dateProduction)`
  - `static LocalDate ajusterDLC(LocalDate dateFabrication, LocalDate dlcOriginale, LocalDate dateProductionOriginale)`
  - `public static LotProduitFini reconstituer(Long id, String numeroLot, String referencePPH,
                                               String codeProduit, String designationProduit,
                                               double quantiteKg, int nbCartons, LocalDate dateProduction,
                                               LocalDate dlc, String matriculeChefProduction,
                                               StatutLot statut, String matriculeGestionnaireValidation,
                                               String motifRejet, LocalDateTime dateCreation,
                                               LocalDateTime dateModification, Long version)`
  - `public void validerParStock(String matriculeGestionnaire)`
  - `public void rejeter(String motif, String matriculeGestionnaire)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumeroLot()`
  - `public String getReferencePPH()`
  - `public String getCodeProduit()`
  - `public String getDesignationProduit()`
  - `public double getQuantiteKg()`
  - `public int getNbCartons()`
  - `public LocalDate getDateProduction()`
  - `public LocalDate getDlc()`
  - `public String getMatriculeChefProduction()`
  - `public StatutLot getStatut()`
  - `public String getMatriculeGestionnaireValidation()`
  - `public String getMotifRejet()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\production\domain\lot\enums\StatutLot.java`

**enum** `StatutLot` (public)

#### `modules\production\domain\lot\events\LotDeclareEvent.java`

**class** `LotDeclareEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numeroLot`
  - `private final String codeProduit`
  - `private final double quantiteKg`
  - `private final int nbCartons`
  - `private final LocalDate dateProduction`
  - `private final LocalDate dlc`
  - `private final String matriculeChefProduction`

- **Méthodes :**
  - `public LotDeclareEvent(String numeroLot, String codeProduit, double quantiteKg,
                           int nbCartons, LocalDate dateProduction, LocalDate dlc,
                           String matriculeChefProduction)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumeroLot()`
  - `public String getCodeProduit()`
  - `public double getQuantiteKg()`
  - `public int getNbCartons()`
  - `public LocalDate getDateProduction()`
  - `public LocalDate getDlc()`
  - `public String getMatriculeChefProduction()`

#### `modules\production\domain\lot\events\LotValideParStockEvent.java`

**class** `LotValideParStockEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numeroLot`
  - `private final String referencePPH`
  - `private final String codeProduit`
  - `private final double quantiteKg`
  - `private final int nbCartons`
  - `private final LocalDate dateProduction`
  - `private final String matriculeGestionnaire`

- **Méthodes :**
  - `public LotValideParStockEvent(String numeroLot, String referencePPH, String codeProduit, double quantiteKg,
                                   int nbCartons, LocalDate dateProduction, String matriculeGestionnaire)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumeroLot()`
  - `public String getReferencePPH()`
  - `public String getCodeProduit()`
  - `public double getQuantiteKg()`
  - `public int getNbCartons()`
  - `public LocalDate getDateProduction()`
  - `public String getMatriculeGestionnaire()`

#### `modules\production\domain\lot\repository\LotRepository.java`

**interface** `LotRepository` (public)

#### `modules\production\domain\lot\specification\LotSpecification.java`

**class** `LotSpecification` (public final)

- **Méthodes :**
  - `private LotSpecification()`
  - `public static boolean peutDeclarer(Role role)`
  - `public static boolean peutValiderParStock(Role role)`

#### `modules\production\domain\of\aggregate\OrdreFabrication.java`

**class** `OrdreFabrication` (public)

- **Champs :**
  - `private Long id`
  - `private final String idOF`
  - `private final String referencePPH`
  - `private final String codeProduit`
  - `private final String designationProduit`
  - `private final int qteDemandee`
  - `private int qteRealisee`
  - `private final LocalDate dateButoir`
  - `private StatutOF statut`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private OrdreFabrication(String idOF, String referencePPH, String codeProduit,
                              String designationProduit, int qteDemandee, LocalDate dateButoir,
                              LocalDateTime dateCreation)`
  - `public static OrdreFabrication creer(String idOF, String referencePPH, String codeProduit,
                                          String designationProduit, int qteDemandee, LocalDate dateButoir)`
  - `public static OrdreFabrication reconstituer(Long id, String idOF, String referencePPH,
                                                 String codeProduit, String designationProduit,
                                                 int qteDemandee, int qteRealisee, LocalDate dateButoir,
                                                 StatutOF statut, LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version)`
  - `public void demarrer()`
  - `public void enregistrerProduction(int quantite)`
  - `public void annuler()`
  - `public int tauxAvancement()`
  - `public Long getId()`
  - `public String getIdOF()`
  - `public String getReferencePPH()`
  - `public String getCodeProduit()`
  - `public String getDesignationProduit()`
  - `public int getQteDemandee()`
  - `public int getQteRealisee()`
  - `public LocalDate getDateButoir()`
  - `public StatutOF getStatut()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\production\domain\of\enums\StatutOF.java`

**enum** `StatutOF` (public)

#### `modules\production\domain\of\repository\OFRepository.java`

**interface** `OFRepository` (public)

#### `modules\production\domain\pph\aggregate\PlanProductionHebdomadaire.java`

**class** `PlanProductionHebdomadaire` (public)

- **Champs :**
  - `private Long id`
  - `private final String referencePPH`
  - `private final String referenceBC`
  - `private final String semaine`
  - `private final LocalDate dateDebut`
  - `private final LocalDate dateFin`
  - `private final String matriculeChefProduction`
  - `private final List<LignePPH> lignes`
  - `private final List<LignePlanDetail> lignesDetaillees`
  - `private final List<RepartitionJournaliere> repartitionJours`
  - `private LocalDate dateSoumission`
  - `private StatutPPH statut`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private PlanProductionHebdomadaire(String referencePPH, String referenceBC, String semaine, LocalDate dateDebut,
                                        LocalDate dateFin, String matriculeChefProduction,
                                        LocalDateTime dateCreation)`
  - `public static PlanProductionHebdomadaire creer(String referencePPH, String semaine,
                                                    LocalDate dateDebut, LocalDate dateFin,
                                                    String matriculeChefProduction,
                                                    List<LignePPH> lignesProduits)`
  - `public static PlanProductionHebdomadaire creerAvecBC(String referencePPH, String referenceBC, String semaine,
                                                    LocalDate dateDebut, LocalDate dateFin,
                                                    String matriculeChefProduction,
                                                    List<LignePPH> lignesProduits)`
  - `public static PlanProductionHebdomadaire creerAvecDetails(String referencePPH, String referenceBC, String semaine,
                                                    LocalDate dateDebut, LocalDate dateFin,
                                                    String matriculeChefProduction,
                                                    List<LignePPH> lignesProduits,
                                                    List<LignePlanDetail> lignesDetaillees,
                                                    List<RepartitionJournaliere> repartitionJours,
                                                    LocalDate dateSoumission)`
  - `public static PlanProductionHebdomadaire reconstituer(Long id, String referencePPH, String semaine,
                                                           LocalDate dateDebut, LocalDate dateFin,
                                                           String matriculeChefProduction,
                                                           List<LignePPH> lignes, StatutPPH statut,
                                                           LocalDateTime dateCreation,
                                                           LocalDateTime dateModification, Long version)`
  - `public static PlanProductionHebdomadaire reconstituerAvecBC(Long id, String referencePPH, String referenceBC,
                                                           String semaine,
                                                           LocalDate dateDebut, LocalDate dateFin,
                                                           String matriculeChefProduction,
                                                           List<LignePPH> lignes, StatutPPH statut,
                                                           LocalDateTime dateCreation,
                                                           LocalDateTime dateModification, Long version)`
  - `public static PlanProductionHebdomadaire reconstituerComplet(Long id, String referencePPH, String referenceBC,
                                                           String semaine,
                                                           LocalDate dateDebut, LocalDate dateFin,
                                                           String matriculeChefProduction,
                                                           List<LignePPH> lignes,
                                                           List<LignePlanDetail> lignesDetaillees,
                                                           List<RepartitionJournaliere> repartitionJours,
                                                           LocalDate dateSoumission,
                                                           StatutPPH statut,
                                                           LocalDateTime dateCreation,
                                                           LocalDateTime dateModification, Long version)`
  - `public void valider()`
  - `public void demarrer()`
  - `public void cloturer()`
  - `public void cumulerProductionLigne(String codeProduit, int quantite)`
  - `public int tauxAvancementGlobal()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getReferencePPH()`
  - `public String getReferenceBC()`
  - `public String getSemaine()`
  - `public LocalDate getDateDebut()`
  - `public LocalDate getDateFin()`
  - `public String getMatriculeChefProduction()`
  - `public List<LignePPH> getLignes()`
  - `public List<LignePlanDetail> getLignesDetaillees()`
  - `public List<RepartitionJournaliere> getRepartitionJours()`
  - `public LocalDate getDateSoumission()`
  - `public StatutPPH getStatut()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\production\domain\pph\entity\LignePPH.java`

**class** `LignePPH` (public)

- **Champs :**
  - `private final String codeProduit`
  - `private final String nomProduit`
  - `private int objectifSemaine`
  - `private int productionRealisee`

- **Méthodes :**
  - `public LignePPH(String codeProduit, String nomProduit, int objectifSemaine)`
  - `public void cumulerProduction(int quantite)`
  - `public void modifierObjectif(int nouvelObjectif)`
  - `public int tauxAvancement()`
  - `public boolean estObjectifAtteint()`
  - `public String getCodeProduit()`
  - `public String getNomProduit()`
  - `public int getObjectifSemaine()`
  - `public int getProductionRealisee()`

#### `modules\production\domain\pph\entity\LignePlanDetail.java`

**class** `LignePlanDetail` (public)

- **Champs :**
  - `private final String codeProduit`
  - `private final String nomProduit`
  - `private final String categorieMetier`
  - `private final int quantiteCartons`
  - `private final int nombreUnitesParConditionnement`
  - `private final double poidsUnitaireGrammes`
  - `private final RecetteBouillie recette`
  - `private final LocalDate dateJour`
  - `private final int quantiteJour`

- **Méthodes :**
  - `public LignePlanDetail(String codeProduit, String nomProduit, String categorieMetier,
                           int quantiteCartons, int nombreUnitesParConditionnement,
                           double poidsUnitaireGrammes, RecetteBouillie recette,
                           LocalDate dateJour, int quantiteJour)`
  - `public int getTotalUnites()`
  - `public int getTotalSachets()`
  - `public int getTotalCouvercles()`
  - `public int getTotalScelles()`
  - `public double getTotalPoidsGrammes()`
  - `public double getTotalPoidsKg()`
  - `public double getPoudreMaisKg()`
  - `public double getPoudreSojaKg()`
  - `public double getPoudreArachideKg()`
  - `public int getNombreFuts50kg()`
  - `public String getCodeProduit()`
  - `public String getNomProduit()`
  - `public String getCategorieMetier()`
  - `public int getQuantiteCartons()`
  - `public int getNombreUnitesParConditionnement()`
  - `public double getPoidsUnitaireGrammes()`
  - `public RecetteBouillie getRecette()`
  - `public LocalDate getDateJour()`
  - `public int getQuantiteJour()`

#### `modules\production\domain\pph\entity\RepartitionJournaliere.java`

**class** `RepartitionJournaliere` (public)

- **Champs :**
  - `private final LocalDate date`
  - `private final List<LignePlanDetail> lignes`
  - `private final double poudreMaisKg`
  - `private final double poudreSojaKg`
  - `private final double poudreArachideKg`
  - `private final int totalFuts`
  - `private final int totalSachets`
  - `private final int totalCouvercles`
  - `private final int totalScelles`

- **Méthodes :**
  - `public RepartitionJournaliere(LocalDate date, List<LignePlanDetail> lignes,
                                   double poudreMaisKg, double poudreSojaKg, double poudreArachideKg,
                                   int totalFuts, int totalSachets, int totalCouvercles, int totalScelles)`
  - `public LocalDate getDate()`
  - `public List<LignePlanDetail> getLignes()`
  - `public double getPoudreMaisKg()`
  - `public double getPoudreSojaKg()`
  - `public double getPoudreArachideKg()`
  - `public int getTotalFuts()`
  - `public int getTotalSachets()`
  - `public int getTotalCouvercles()`
  - `public int getTotalScelles()`

#### `modules\production\domain\pph\enums\RecetteBouillie.java`

**enum** `RecetteBouillie` (public)

- **Constantes :**
  - `{

    SIMPLE_PORRIDGE("", 40.0, 9.0, 1.0)`
  - `PEANUT_PORRIDGE("", 40.0, 1.0, 9.0)`

- **Champs :**
  - `private final String libelle`
  - `private final double proportionMais`
  - `private final double proportionSoja`
  - `private final double proportionArachide`

- **Méthodes :**
  - `public String getLibelle()`
  - `public double getProportionMais()`
  - `public double getProportionSoja()`
  - `public double getProportionArachide()`
  - `public double getTotalProportions()`
  - `public static RecetteBouillie depuisDesignation(String designation)`

#### `modules\production\domain\pph\enums\StatutPPH.java`

**enum** `StatutPPH` (public)

#### `modules\production\domain\pph\events\PPHValideEvent.java`

**class** `PPHValideEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String referencePPH`
  - `private final String semaine`
  - `private final LocalDate dateDebut`
  - `private final String matriculeChefProduction`

- **Méthodes :**
  - `public PPHValideEvent(String referencePPH, String semaine, LocalDate dateDebut, String matriculeChefProduction)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReferencePPH()`
  - `public String getSemaine()`
  - `public LocalDate getDateDebut()`
  - `public String getMatriculeChefProduction()`

#### `modules\production\domain\pph\repository\PPHRepository.java`

**interface** `PPHRepository` (public)

#### `modules\production\domain\registre\aggregate\RegistreProduction.java`

**class** `RegistreProduction` (public)

- **Champs :**
  - `private Long id`
  - `private final String referencePPH`
  - `private final LocalDate date`
  - `private final String matriculeChefProduction`
  - `private final List<LigneRegistreEmploye> lignesEmployes`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `RegistreProduction that = (RegistreProduction) o`

- **Méthodes :**
  - `private RegistreProduction(String referencePPH, LocalDate date,
                                String matriculeChefProduction, LocalDateTime dateCreation)`
  - `public static RegistreProduction enregistrer(String referencePPH, LocalDate date,
                                                  String matriculeChefProduction,
                                                  List<LigneRegistreEmploye> lignes)`
  - `public static RegistreProduction reconstituer(Long id, String referencePPH, LocalDate date,
                                                   String matriculeChefProduction,
                                                   List<LigneRegistreEmploye> lignes,
                                                   LocalDateTime dateCreation,
                                                   LocalDateTime dateModification, Long version)`
  - `public long nbPresents()`
  - `public long nbAbsents()`
  - `public Long getId()`
  - `public String getReferencePPH()`
  - `public LocalDate getDate()`
  - `public String getMatriculeChefProduction()`
  - `public List<LigneRegistreEmploye> getLignesEmployes()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\production\domain\registre\entity\LigneRegistreEmploye.java`

**class** `LigneRegistreEmploye` (public)

- **Champs :**
  - `private final String matriculeEmploye`
  - `private final String nomEmploye`
  - `private final String poste`
  - `private final boolean present`
  - `private final String motifAbsence`
  - `private final String heureArrivee`
  - `private final double qteRealisee`
  - `private final String unite`

- **Méthodes :**
  - `public LigneRegistreEmploye(String matriculeEmploye, String nomEmploye, String poste,
                                 boolean present, String motifAbsence)`
  - `public LigneRegistreEmploye(String matriculeEmploye, String nomEmploye, String poste,
                                 boolean present, String motifAbsence,
                                 String heureArrivee, double qteRealisee, String unite)`
  - `public String getMatriculeEmploye()`
  - `public String getNomEmploye()`
  - `public String getPoste()`
  - `public boolean isPresent()`
  - `public String getMotifAbsence()`
  - `public String getHeureArrivee()`
  - `public double getQteRealisee()`
  - `public String getUnite()`

#### `modules\production\domain\registre\repository\RegistreProductionRepository.java`

**interface** `RegistreProductionRepository` (public)

#### `modules\production\infrastructure\messaging\FicheVerrouillageListener.java`

**class** `FicheVerrouillageListener` (@Component
public)

- **Champs :**
  - `private final FicheProduitRepository ficheRepository`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public FicheVerrouillageListener(FicheProduitRepository ficheRepository, EventPublisher eventPublisher)`
  - `@EventListener
    @Transactional public void surLotValideParStock(LotValideParStockEvent event)`

#### `modules\production\infrastructure\persistence\affectation\jpa\AffectationJournaliereJpaEntity.java`

**class** `AffectationJournaliereJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 30) private String referencePPH`
  - `@Column(nullable = false) private LocalDate date`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeAgent`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private StatutAffectation statut`
  - `@OneToMany(mappedBy = "", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) private List<LigneAffectationJpaEntity> lignes = new ArrayList<>()`
  - `@OneToMany(mappedBy = "", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) private List<SaisieRealisationJpaEntity> saisies = new ArrayList<>()`
  - `@Column(name = "", nullable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\production\infrastructure\persistence\affectation\jpa\AffectationJournaliereJpaRepository.java`

**interface** `AffectationJournaliereJpaRepository` (public) extends `JpaRepository<AffectationJournaliereJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\affectation\jpa\LigneAffectationJpaEntity.java`

**class** `LigneAffectationJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private AffectationJournaliereJpaEntity affectation`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private CodePoste poste`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private DureeAffectation duree`
  - `@Column(name = "", nullable = false) private double objectifAjuste`
  - `@Column(name = "", nullable = false, length = 500) private String matriculesEmployesJson`

#### `modules\production\infrastructure\persistence\affectation\jpa\ProductionIndividuelleJpaEntity.java`

**class** `ProductionIndividuelleJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private SaisieRealisationJpaEntity saisie`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeEmploye`
  - `@Column(name = "", nullable = false, length = 100) private String nomEmploye`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private CodePoste poste`
  - `@Column(name = "", nullable = false) private double quantiteRealisee`

#### `modules\production\infrastructure\persistence\affectation\jpa\SaisieRealisationJpaEntity.java`

**class** `SaisieRealisationJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private AffectationJournaliereJpaEntity affectation`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private CodePoste poste`
  - `@Column(name = "", nullable = false) private double quantiteRealisee`
  - `@Column(name = "", nullable = false) private double objectifAjuste`
  - `@Column(name = "") private Integer nbRegimesEpluches`
  - `@Column(name = "") private Integer nbRegimesDecoupes`
  - `@Column(name = "") private Integer nbSceauxEtiquetes`
  - `@Column(name = "") private Integer nbEtiquettesDatees`
  - `@Column(name = "") private Integer nbSachetsDates`
  - `@Column(name = "") private Integer nbCartonsProduits`
  - `@Column(name = "") private Double kgChipsProduits`
  - `@Column(name = "", length = 1000) private String warningsJson`
  - `@Column(name = "", nullable = false) private LocalDateTime dateEnregistrement`
  - `@OneToMany(mappedBy = "", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) private List<ProductionIndividuelleJpaEntity> productionsIndividuelles = new ArrayList<>()`

#### `modules\production\infrastructure\persistence\affectation\repository\AffectationJournaliereRepositoryImpl.java`

**class** `AffectationJournaliereRepositoryImpl` (@Repository
public) implements `AffectationJournaliereRepository`

- **Champs :**
  - `private final AffectationJournaliereJpaRepository jpaRepository`
  - `private final ObjectMapper objectMapper`

- **Méthodes :**
  - `public AffectationJournaliereRepositoryImpl(AffectationJournaliereJpaRepository jpaRepository,
                                                ObjectMapper objectMapper)`
  - `@Override public AffectationJournaliere sauvegarder(AffectationJournaliere affectation)`
  - `@Override public Optional<AffectationJournaliere> trouverParId(Long id)`
  - `@Override public Optional<AffectationJournaliere> trouverParPPHEtDate(String referencePPH, LocalDate date)`
  - `@Override public List<AffectationJournaliere> listerParPPH(String referencePPH)`
  - `@Override public List<AffectationJournaliere> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public boolean existeParPPHEtDate(String referencePPH, LocalDate date)`
  - `private AffectationJournaliereJpaEntity toEntity(AffectationJournaliere a)`
  - `private AffectationJournaliere toDomain(AffectationJournaliereJpaEntity e)`
  - `private String toWarningsJson(List<String> warnings)`
  - `@SuppressWarnings("") private List<String> fromWarningsJson(String json)`

#### `modules\production\infrastructure\persistence\besoin\jpa\ExpressionBesoinJpaEntity.java`

**class** `ExpressionBesoinJpaEntity` (@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numero`
  - `private String referencePPH`
  - `@Column(nullable = false)
    @Enumerated(EnumType.STRING) private TypeBesoin type`
  - `@Column(nullable = false) private String codeProduit`
  - `@Column(nullable = false) private String designationProduit`
  - `@Column(nullable = false) private double quantiteDemandee`
  - `@Column(nullable = false) private String unite`
  - `@Column(nullable = false) private String matriculeDemandeur`
  - `@Column(nullable = false) private LocalDate dateBesoin`
  - `private String motif`
  - `@Column(nullable = false)
    @Enumerated(EnumType.STRING) private StatutBesoin statut`
  - `private String matriculeValidateur`
  - `private String motifRejet`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\production\infrastructure\persistence\besoin\jpa\ExpressionBesoinJpaRepository.java`

**interface** `ExpressionBesoinJpaRepository` (public) extends `JpaRepository<ExpressionBesoinJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\besoin\repository\ExpressionBesoinRepositoryImpl.java`

**class** `ExpressionBesoinRepositoryImpl` (@Repository
public) implements `ExpressionBesoinRepository`

- **Champs :**
  - `private final ExpressionBesoinJpaRepository jpaRepository`

- **Méthodes :**
  - `public ExpressionBesoinRepositoryImpl(ExpressionBesoinJpaRepository jpaRepository)`
  - `@Override public ExpressionBesoin sauvegarder(ExpressionBesoin besoin)`
  - `@Override public Optional<ExpressionBesoin> trouverParNumero(String numero)`
  - `@Override public List<ExpressionBesoin> listerParStatut(StatutBesoin statut)`
  - `@Override public List<ExpressionBesoin> listerTous()`
  - `@Override public boolean existeParNumero(String numero)`
  - `private ExpressionBesoinJpaEntity toEntity(ExpressionBesoin b)`
  - `private ExpressionBesoin toDomain(ExpressionBesoinJpaEntity e)`

#### `modules\production\infrastructure\persistence\broyage\jpa\ObjectifMachinisteJpaEntity.java`

**class** `ObjectifMachinisteJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private SessionBroyageJpaEntity session`
  - `@Column(name = "", nullable = false, length = 20) private String machinisteMatricule`
  - `@Column(name = "", nullable = false, length = 100) private String nomMachiniste`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private OperationBroyage operation`
  - `@Enumerated(EnumType.STRING)
    @Column(name = "", nullable = false, length = 20) private TypePoudre typePoudre`
  - `@Column(name = "", nullable = false) private double objectifKg`

#### `modules\production\infrastructure\persistence\broyage\jpa\RealisationMachinisteJpaEntity.java`

**class** `RealisationMachinisteJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private SessionBroyageJpaEntity session`
  - `@Column(name = "", nullable = false, length = 20) private String machinisteMatricule`
  - `@Column(name = "", nullable = false, length = 100) private String nomMachiniste`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private OperationBroyage operation`
  - `@Enumerated(EnumType.STRING)
    @Column(name = "", nullable = false, length = 20) private TypePoudre typePoudre`
  - `@Column(name = "", nullable = false) private double objectifKg`
  - `@Column(name = "", nullable = false) private double realiseKg`

#### `modules\production\infrastructure\persistence\broyage\jpa\SessionBroyageJpaEntity.java`

**class** `SessionBroyageJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 30) private String referencePPH`
  - `@Column(nullable = false) private LocalDate date`
  - `@Enumerated(EnumType.STRING)
    @Column(name = "", nullable = false, length = 20) private TypePoudre typePoudre`
  - `@Column(name = "", nullable = false) private double objectifJournalierKg`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeChefMachiniste`
  - `@OneToMany(mappedBy = "", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) private List<ObjectifMachinisteJpaEntity> objectifsMachinistes = new ArrayList<>()`
  - `@OneToMany(mappedBy = "", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) private List<RealisationMachinisteJpaEntity> realisations = new ArrayList<>()`
  - `@Column(name = "") private Double quantiteNetteBroyeeKg`
  - `@Column(name = "") private Double pertesKg`
  - `@Column(name = "") private double depassementAutoriseKg`
  - `@Column(name = "", length = 1000) private String justificatifNonValidation`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) private StatutSessionBroyage statut`
  - `@Column(name = "", nullable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\production\infrastructure\persistence\broyage\jpa\SessionBroyageJpaRepository.java`

**interface** `SessionBroyageJpaRepository` (public) extends `JpaRepository<SessionBroyageJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\broyage\repository\SessionBroyageRepositoryImpl.java`

**class** `SessionBroyageRepositoryImpl` (@Repository
public) implements `SessionBroyageRepository`

- **Champs :**
  - `private final SessionBroyageJpaRepository jpaRepository`

- **Méthodes :**
  - `public SessionBroyageRepositoryImpl(SessionBroyageJpaRepository jpaRepository)`
  - `@Override public SessionBroyage sauvegarder(SessionBroyage session)`
  - `@Override public Optional<SessionBroyage> trouverParId(Long id)`
  - `@Override public Optional<SessionBroyage> trouverParPPHDateEtType(String referencePPH, LocalDate date, TypePoudre typePoudre)`
  - `@Override public List<SessionBroyage> listerParPPH(String referencePPH)`
  - `@Override public List<SessionBroyage> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public List<SessionBroyage> listerParMachinisteEtPeriode(String machinisteMatricule, LocalDate debut, LocalDate fin)`
  - `@Override public boolean existeParPPHDateEtType(String referencePPH, LocalDate date, TypePoudre typePoudre)`
  - `private SessionBroyageJpaEntity toEntity(SessionBroyage s)`
  - `private SessionBroyage toDomain(SessionBroyageJpaEntity e)`

#### `modules\production\infrastructure\persistence\dosage\jpa\SessionDosageJpaEntity.java`

**class** `SessionDosageJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String referencePPH`
  - `@Column(nullable = false) private LocalDate date`
  - `@Column(nullable = false) private String matriculeAgentDoseur`
  - `private List<MatierePremiereJpa> matieresUtilisees`
  - `@Column(name = "") private List<String> machinesMobilisees`
  - `@Column(nullable = false) private int nbFutsProduits`
  - `private String justificationEcart`
  - `@Column(nullable = false) private boolean validee`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`
  - `private String typeMatiere`
  - `private double quantiteKg`

**class** `MatierePremiereJpa` (@Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static)

- **Champs :**
  - `private String typeMatiere`
  - `private double quantiteKg`

#### `modules\production\infrastructure\persistence\dosage\jpa\SessionDosageJpaRepository.java`

**interface** `SessionDosageJpaRepository` (public) extends `JpaRepository<SessionDosageJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\dosage\repository\SessionDosageRepositoryImpl.java`

**class** `SessionDosageRepositoryImpl` (@Repository
public) implements `SessionDosageRepository`

- **Champs :**
  - `private final SessionDosageJpaRepository jpaRepository`

- **Méthodes :**
  - `public SessionDosageRepositoryImpl(SessionDosageJpaRepository jpaRepository)`
  - `@Override public SessionDosage sauvegarder(SessionDosage session)`
  - `@Override public Optional<SessionDosage> trouverParId(Long id)`
  - `@Override public Optional<SessionDosage> trouverParPPHEtDate(String referencePPH, LocalDate date)`
  - `@Override public List<SessionDosage> listerParPPH(String referencePPH)`
  - `@Override public List<SessionDosage> listerParDate(LocalDate date)`
  - `@Override public List<SessionDosage> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public List<SessionDosage> listerTous()`
  - `private SessionDosageJpaEntity toEntity(SessionDosage s)`
  - `private SessionDosage toDomain(SessionDosageJpaEntity e)`

#### `modules\production\infrastructure\persistence\fiche\jpa\FicheProduitJpaEntity.java`

**class** `FicheProduitJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 30) private String referencePPH`
  - `@Column(nullable = false, length = 20) private String semaine`
  - `@Column(nullable = false) private LocalDate date`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeAgentProduction`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "") private List<LigneFicheProduitJpaEntity> lignes = new ArrayList<>()`
  - `@JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "", columnDefinition = "") private Map<String, Integer> produitsRealises`
  - `@JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "", columnDefinition = "") private Map<String, Integer> predictionDosage`
  - `@JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "", columnDefinition = "") private Map<String, Integer> predictionPostes`
  - `@Column(name = "") private String justificationEcart`
  - `@Column(nullable = false) private boolean verrouillee = false`
  - `@Column(name = "", nullable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\production\infrastructure\persistence\fiche\jpa\FicheProduitJpaRepository.java`

**interface** `FicheProduitJpaRepository` (@Repository
public) extends `JpaRepository<FicheProduitJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\fiche\jpa\LigneFicheProduitJpaEntity.java`

**class** `LigneFicheProduitJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 30) private String codeProduit`
  - `@Column(name = "", nullable = false, length = 200) private String nomProduit`
  - `@Column(name = "", nullable = false) private double quantiteProduite`

#### `modules\production\infrastructure\persistence\fiche\repository\FicheProduitRepositoryImpl.java`

**class** `FicheProduitRepositoryImpl` (@Repository
public) implements `FicheProduitRepository`

- **Champs :**
  - `private final FicheProduitJpaRepository jpaRepository`

- **Méthodes :**
  - `public FicheProduitRepositoryImpl(FicheProduitJpaRepository jpaRepository)`
  - `@Override public FicheProduitJournaliere sauvegarder(FicheProduitJournaliere fiche)`
  - `@Override public Optional<FicheProduitJournaliere> trouverParPPHEtDate(String referencePPH, LocalDate date)`
  - `@Override public List<FicheProduitJournaliere> listerParPPH(String referencePPH)`
  - `@Override public List<FicheProduitJournaliere> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public boolean existeParPPHEtDate(String referencePPH, LocalDate date)`
  - `private FicheProduitJpaEntity toEntity(FicheProduitJournaliere f)`
  - `private FicheProduitJournaliere toDomain(FicheProduitJpaEntity e)`

#### `modules\production\infrastructure\persistence\lot\jpa\LotJpaRepository.java`

**interface** `LotJpaRepository` (@Repository
public) extends `JpaRepository<LotProduitFiniJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\lot\jpa\LotProduitFiniJpaEntity.java`

**class** `LotProduitFiniJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", unique = true, nullable = false, length = 30) private String numeroLot`
  - `@Column(name = "", nullable = false, length = 30) private String referencePPH`
  - `@Column(name = "", nullable = false, length = 30) private String codeProduit`
  - `@Column(name = "", nullable = false, length = 200) private String designationProduit`
  - `@Column(name = "", nullable = false) private double quantiteKg`
  - `@Column(name = "", nullable = false) private int nbCartons`
  - `@Column(name = "", nullable = false) private LocalDate dateProduction`
  - `@Column(nullable = false) private LocalDate dlc`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeChefProduction`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30) private StatutLot statut`
  - `@Column(name = "", length = 20) private String matriculeGestionnaireValidation`
  - `@Column(name = "") private String motifRejet`
  - `@Column(name = "", nullable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\production\infrastructure\persistence\lot\repository\LotRepositoryImpl.java`

**class** `LotRepositoryImpl` (@Repository
public) implements `LotRepository`

- **Champs :**
  - `private final LotJpaRepository jpaRepository`

- **Méthodes :**
  - `public LotRepositoryImpl(LotJpaRepository jpaRepository)`
  - `@Override public LotProduitFini sauvegarder(LotProduitFini lot)`
  - `@Override public Optional<LotProduitFini> trouverParNumero(String numeroLot)`
  - `@Override public List<LotProduitFini> listerParStatut(StatutLot statut)`
  - `@Override public List<LotProduitFini> listerParPPH(String referencePPH)`
  - `@Override public List<LotProduitFini> listerParCodeProduit(String codeProduit)`
  - `@Override public List<LotProduitFini> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public long compterParStatutEtDate(StatutLot statut, LocalDate date)`
  - `private LotProduitFiniJpaEntity toEntity(LotProduitFini lot)`
  - `private LotProduitFini toDomain(LotProduitFiniJpaEntity e)`

#### `modules\production\infrastructure\persistence\of\jpa\OFJpaEntity.java`

**class** `OFJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String idOF`
  - `@Column(nullable = false) private String referencePPH`
  - `@Column(nullable = false) private String codeProduit`
  - `@Column(nullable = false) private String designationProduit`
  - `@Column(nullable = false) private int qteDemandee`
  - `@Column(nullable = false) private int qteRealisee`
  - `@Column(nullable = false) private LocalDate dateButoir`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutOF statut`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\production\infrastructure\persistence\of\jpa\OFJpaRepository.java`

**interface** `OFJpaRepository` (public) extends `JpaRepository<OFJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\of\repository\OFRepositoryImpl.java`

**class** `OFRepositoryImpl` (@Repository
public) implements `OFRepository`

- **Champs :**
  - `private final OFJpaRepository jpaRepository`

- **Méthodes :**
  - `public OFRepositoryImpl(OFJpaRepository jpaRepository)`
  - `@Override public OrdreFabrication sauvegarder(OrdreFabrication of)`
  - `@Override public Optional<OrdreFabrication> trouverParId(String idOF)`
  - `@Override public List<OrdreFabrication> listerParPPH(String referencePPH)`
  - `@Override public List<OrdreFabrication> listerParStatut(StatutOF statut)`
  - `@Override public List<OrdreFabrication> listerTous()`
  - `private OFJpaEntity toEntity(OrdreFabrication of)`
  - `private OrdreFabrication toDomain(OFJpaEntity e)`

#### `modules\production\infrastructure\persistence\pph\jpa\LignePPHJpaEntity.java`

**class** `LignePPHJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String codeProduit`
  - `@Column(nullable = false) private String nomProduit`
  - `@Column(nullable = false) private int objectifSemaine`
  - `@Column(nullable = false) private int productionRealisee`

#### `modules\production\infrastructure\persistence\pph\jpa\LignePlanDetailJpaEntity.java`

**class** `LignePlanDetailJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "") private Long pphId`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", nullable = false, length = 255) private String nomProduit`
  - `@Column(name = "", nullable = false, length = 30) private String categorieMetier`
  - `@Column(name = "", nullable = false) private int quantiteCartons`
  - `@Column(name = "", nullable = false) private int nombreUnites`
  - `@Column(name = "", nullable = false) private double poidsUnitaireGrammes`
  - `@Column(name = "", length = 30) private String recette`
  - `@Column(name = "", nullable = false) private LocalDate dateJour`
  - `@Column(name = "", nullable = false) private int quantiteJour`
  - `@Column(name = "") private double poudreMaisKg`
  - `@Column(name = "") private double poudreSojaKg`
  - `@Column(name = "") private double poudreArachideKg`
  - `@Column(name = "") private int futsPrevus`
  - `@Column(name = "") private int sachetsPrevus`
  - `@Column(name = "") private int couverclesPrevus`
  - `@Column(name = "") private int scellesPrevus`
  - `@Column(name = "") private double poidsTotalGrammes`

#### `modules\production\infrastructure\persistence\pph\jpa\PPHJpaEntity.java`

**class** `PPHJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String referencePPH`
  - `private String referenceBC`
  - `@Column(nullable = false) private String semaine`
  - `@Column(nullable = false) private LocalDate dateDebut`
  - `@Column(nullable = false) private LocalDate dateFin`
  - `@Column(nullable = false) private String matriculeChefProduction`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "")
    @Fetch(FetchMode.SUBSELECT) private List<LignePPHJpaEntity> lignes`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "")
    @Fetch(FetchMode.SUBSELECT) private List<LignePlanDetailJpaEntity> lignesDetaillees`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "")
    @Fetch(FetchMode.SUBSELECT) private List<RepartitionJourJpaEntity> repartitionJours`
  - `@Column(name = "") private LocalDate dateSoumission`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutPPH statut`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\production\infrastructure\persistence\pph\jpa\PPHJpaRepository.java`

**interface** `PPHJpaRepository` (public) extends `JpaRepository<PPHJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\pph\jpa\RepartitionJourJpaEntity.java`

**class** `RepartitionJourJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "") private Long pphId`
  - `@Column(name = "", nullable = false) private LocalDate dateJour`
  - `@Column(name = "") private double poudreMaisKg`
  - `@Column(name = "") private double poudreSojaKg`
  - `@Column(name = "") private double poudreArachideKg`
  - `@Column(name = "") private int futsPrevus`
  - `@Column(name = "") private int sachetsPrevus`
  - `@Column(name = "") private int couverclesPrevus`
  - `@Column(name = "") private int scellesPrevus`

#### `modules\production\infrastructure\persistence\pph\repository\PPHRepositoryImpl.java`

**class** `PPHRepositoryImpl` (@Repository
public) implements `PPHRepository`

- **Champs :**
  - `private final PPHJpaRepository jpaRepository`

- **Méthodes :**
  - `public PPHRepositoryImpl(PPHJpaRepository jpaRepository)`
  - `@Override public PlanProductionHebdomadaire sauvegarder(PlanProductionHebdomadaire pph)`
  - `@Override public Optional<PlanProductionHebdomadaire> trouverParReference(String referencePPH)`
  - `@Override public List<PlanProductionHebdomadaire> listerParStatut(StatutPPH statut)`
  - `@Override public Optional<PlanProductionHebdomadaire> trouverEnCours()`
  - `@Override public List<PlanProductionHebdomadaire> listerParBC(String referenceBC)`
  - `@Override public List<PlanProductionHebdomadaire> listerTous()`
  - `private PPHJpaEntity toEntity(PlanProductionHebdomadaire pph)`
  - `private PlanProductionHebdomadaire toDomain(PPHJpaEntity e)`
  - `private RecetteBouillie parseRecette(String valeur)`

#### `modules\production\infrastructure\persistence\registre\jpa\LigneRegistreJpaEntity.java`

**class** `LigneRegistreJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String matriculeEmploye`
  - `@Column(nullable = false) private String nomEmploye`
  - `@Column(nullable = false) private boolean present`
  - `private String motifAbsence`
  - `private String poste`
  - `private String heureArrivee`
  - `private double qteRealisee`
  - `private String unite`

#### `modules\production\infrastructure\persistence\registre\jpa\RegistreJpaEntity.java`

**class** `RegistreJpaEntity` (@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String referencePPH`
  - `@Column(nullable = false) private LocalDate date`
  - `@Column(nullable = false) private String matriculeChefProduction`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "") private List<LigneRegistreJpaEntity> lignesEmployes`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\production\infrastructure\persistence\registre\jpa\RegistreJpaRepository.java`

**interface** `RegistreJpaRepository` (public) extends `JpaRepository<RegistreJpaEntity, Long>`

#### `modules\production\infrastructure\persistence\registre\repository\RegistreRepositoryImpl.java`

**class** `RegistreRepositoryImpl` (@Repository
public) implements `RegistreProductionRepository`

- **Champs :**
  - `private final RegistreJpaRepository jpaRepository`

- **Méthodes :**
  - `public RegistreRepositoryImpl(RegistreJpaRepository jpaRepository)`
  - `public RegistreProduction sauvegarder(RegistreProduction registre)`
  - `public Optional<RegistreProduction> trouverParPPHEtDate(String referencePPH, LocalDate date)`
  - `public List<RegistreProduction> listerParPPH(String referencePPH)`
  - `@Override public boolean existeParPPHEtDate(String referencePPH, LocalDate date)`
  - `@Override public List<RegistreProduction> listerParPeriode(LocalDate debut, LocalDate fin)`
  - `private RegistreJpaEntity toEntity(RegistreProduction r)`
  - `private RegistreProduction toDomain(RegistreJpaEntity e)`

#### `modules\production\presentation\controller\AffectationController.java`

**class** `AffectationController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final CreerAffectationJourUseCase creerUseCase`
  - `private final SaisirRealisationPosteUseCase saisirUseCase`
  - `private final ValiderAffectationUseCase validerUseCase`
  - `private final ComparaisonFutsProductionUseCase comparaisonFutsProductionUseCase`
  - `private final ClassementExecuteursHebdomadaireUseCase classementExecuteursUseCase`
  - `private final RapportHebdomadaireProductionUseCase rapportHebdomadaireUseCase`
  - `private final AffectationJournaliereRepository repository`
  - `private final AffectationMapper mapper`
  - `private final AuthenticatedUserResolver userResolver`
  - `List<AffectationJournaliereResponse> list`

- **Méthodes :**
  - `public AffectationController(CreerAffectationJourUseCase creerUseCase,
                                  SaisirRealisationPosteUseCase saisirUseCase,
                                  ValiderAffectationUseCase validerUseCase,
                                  ComparaisonFutsProductionUseCase comparaisonFutsProductionUseCase,
                                  ClassementExecuteursHebdomadaireUseCase classementExecuteursUseCase,
                                  RapportHebdomadaireProductionUseCase rapportHebdomadaireUseCase,
                                  AffectationJournaliereRepository repository,
                                  AffectationMapper mapper,
                                  AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> creer(@RequestBody @Valid CreerAffectationJourRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> saisirRealisation(@RequestBody @Valid SaisirRealisationPosteRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> valider(@PathVariable Long id)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> detail(@PathVariable Long id)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> aujourdhui(@RequestParam String referencePPH)`

#### `modules\production\presentation\controller\BroyageController.java`

**class** `BroyageController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final OuvrirSessionBroyageUseCase ouvrirUseCase`
  - `private final EnregistrerRealisationBroyageUseCase enregistrerUseCase`
  - `private final CloturerSessionBroyageUseCase cloturerUseCase`
  - `private final AutoriserDepassementBroyageUseCase autoriserDepassementUseCase`
  - `private final JustifierNonValidationBroyageUseCase justifierNonValidationUseCase`
  - `private final ClassementMachinistesUseCase classementUseCase`
  - `private final SessionBroyageRepository repository`
  - `private final SessionBroyageMapper mapper`
  - `private final AuthenticatedUserResolver userResolver`
  - `List<SessionBroyageResponse> sessions`

- **Méthodes :**
  - `public BroyageController(OuvrirSessionBroyageUseCase ouvrirUseCase,
                              EnregistrerRealisationBroyageUseCase enregistrerUseCase,
                              CloturerSessionBroyageUseCase cloturerUseCase,
                              AutoriserDepassementBroyageUseCase autoriserDepassementUseCase,
                              JustifierNonValidationBroyageUseCase justifierNonValidationUseCase,
                              ClassementMachinistesUseCase classementUseCase,
                              SessionBroyageRepository repository,
                              SessionBroyageMapper mapper,
                              AuthenticatedUserResolver userResolver)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionBroyageResponse>> ouvrir(@RequestBody @Valid OuvrirSessionBroyageRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionBroyageResponse>> enregistrerRealisation(@RequestBody @Valid EnregistrerRealisationBroyageRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionBroyageResponse>> cloturer(@RequestBody @Valid CloturerSessionBroyageRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionBroyageResponse>> autoriserDepassement(@RequestBody @Valid AutoriserDepassementBroyageRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionBroyageResponse>> justifierNonValidation(@RequestBody @Valid JustifierNonValidationBroyageRequest req)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionBroyageResponse>> detail(@PathVariable Long id)`

#### `modules\production\presentation\controller\ChefMachinisteController.java`

**class** `ChefMachinisteController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ListerMachinistesUseCase listerMachinistesUseCase`

- **Méthodes :**
  - `public ChefMachinisteController(ListerMachinistesUseCase listerMachinistesUseCase)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<EmployeResponse>>> listerMachinistes()`

#### `modules\production\presentation\controller\DashboardProductionController.java`

**class** `DashboardProductionController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DashboardProductionUseCase dashboardUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public DashboardProductionController(DashboardProductionUseCase dashboardUseCase,
                                          AuthenticatedUserResolver userResolver)`
  - `DashboardChefProduction>> dashboardChef()`
  - `DashboardAgentProduction>> dashboardAgent()`

#### `modules\production\presentation\controller\DosageController.java`

**class** `DosageController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final SessionDosageRepository repository`
  - `private final SessionDosageMapper mapper`
  - `private final StatistiquesFutsUseCase statistiquesFutsUseCase`
  - `private final PredireProduitFiniDepuisFutsUseCase predireProduitFiniUseCase`
  - `private final RegistreDosageExportService exportService`
  - `private final DocumentsDosageService documentsService`

- **Méthodes :**
  - `public DosageController(SessionDosageRepository repository, SessionDosageMapper mapper,
                           StatistiquesFutsUseCase statistiquesFutsUseCase,
                           PredireProduitFiniDepuisFutsUseCase predireProduitFiniUseCase,
                           RegistreDosageExportService exportService,
                           DocumentsDosageService documentsService)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionDosageResponse>> ouvrir(@Valid @RequestBody EnregistrerSessionDosageRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionDosageResponse>> enregistrerMatiere(@PathVariable Long id,
                                                                                  @RequestParam String typeMatiere,
                                                                                  @RequestParam double quantiteKg)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionDosageResponse>> enregistrerMachine(@PathVariable Long id,
                                                                                  @RequestParam String machineId)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionDosageResponse>> enregistrerFuts(@PathVariable Long id,
                                                                               @RequestParam int nbProduits,
                                                                               @RequestParam int nbCasses)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionDosageResponse>> justifierEcart(@PathVariable Long id,
                                                                              @RequestParam String justification)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionDosageResponse>> valider(@PathVariable Long id)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionDosageResponse>> cloturer(@PathVariable Long id)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionDosageResponse>> consulter(@PathVariable Long id)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<SessionDosageResponse>>> listerParPPH(@PathVariable String reference)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<SessionDosageResponse>>> listerParDate(@PathVariable String date)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<SessionDosageResponse>>> listerParPeriode(@RequestParam String debut, @RequestParam String fin)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<StatistiquesFutsResponse>> statistiquesFuts(@RequestParam String debut, @RequestParam String fin)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> telechargerDocuments(@PathVariable Long id)`
  - `private SessionDosage trouverOuErreur(Long id)`

#### `modules\production\presentation\controller\ExpressionBesoinController.java`

**class** `ExpressionBesoinController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final SignalerBesoinUseCase signalerUseCase`
  - `private final TraiterBesoinUseCase traiterUseCase`
  - `private final ExpressionBesoinRepository repository`
  - `private final ExpressionBesoinMapper mapper`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public ExpressionBesoinController(SignalerBesoinUseCase signalerUseCase, TraiterBesoinUseCase traiterUseCase,
                                       ExpressionBesoinRepository repository, ExpressionBesoinMapper mapper,
                                       AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ExpressionBesoinResponse>> signaler(@RequestBody @Valid SignalerBesoinRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ExpressionBesoinResponse>> valider(@PathVariable String numero)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ExpressionBesoinResponse>> rejeter(@PathVariable String numero, @RequestParam String motif)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ExpressionBesoinResponse>> consulter(@PathVariable String numero)`

#### `modules\production\presentation\controller\FicheController.java`

**class** `FicheController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final EnregistrerFicheUseCase enregistrerFicheUseCase`
  - `private final ModifierFicheUseCase modifierFicheUseCase`
  - `private final FicheProduitRepository ficheRepository`
  - `private final FicheMapper ficheMapper`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public FicheController(EnregistrerFicheUseCase enregistrerFicheUseCase, ModifierFicheUseCase modifierFicheUseCase,
                         FicheProduitRepository ficheRepository, FicheMapper ficheMapper,
                         AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<FicheResponse>> enregistrer(@RequestBody @Valid EnregistrerFicheRequest req)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<FicheResponse>>> parPPH(@PathVariable String referencePPH)`

#### `modules\production\presentation\controller\LotController.java`

**class** `LotController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DeclarerLotUseCase declarerLotUseCase`
  - `private final LotRepository lotRepository`
  - `private final LotMapper lotMapper`
  - `private final AuthenticatedUserResolver userResolver`
  - `EventPublisher eventPublisher`

- **Méthodes :**
  - `public LotController(DeclarerLotUseCase declarerLotUseCase, LotRepository lotRepository,
                       LotMapper lotMapper, AuthenticatedUserResolver userResolver,
                       com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher eventPublisher)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<LotResponse>> declarer(@RequestBody @Valid DeclarerLotRequest req)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<LotResponse>>> aValider()`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "" +
                      "") public ResponseEntity<ApiResponse<LotResponse>> valider(@PathVariable String numeroLot)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<LotResponse>> rejeter(@PathVariable String numeroLot, @RequestParam String motif)`

#### `modules\production\presentation\controller\OFController.java`

**class** `OFController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final OFRepository ofRepository`
  - `private final OFMapper ofMapper`
  - `private final CreerOFUseCase creerOFUseCase`
  - `private final DemarrerOFUseCase demarrerOFUseCase`
  - `private final EnregistrerProductionOFUseCase enregistrerProductionOFUseCase`
  - `private final AnnulerOFUseCase annulerOFUseCase`
  - `OrdreFabrication> ofs`

- **Méthodes :**
  - `public OFController(OFRepository ofRepository, OFMapper ofMapper,
                        CreerOFUseCase creerOFUseCase, DemarrerOFUseCase demarrerOFUseCase,
                        EnregistrerProductionOFUseCase enregistrerProductionOFUseCase,
                        AnnulerOFUseCase annulerOFUseCase)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<OFResponse>> creer(@RequestBody @Valid CreerOFRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<OFResponse>> demarrer(@PathVariable String idOF)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<OFResponse>> produire(@PathVariable String idOF, @RequestParam int quantite)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<OFResponse>> annuler(@PathVariable String idOF)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<OFResponse>> consulter(@PathVariable String idOF)`

#### `modules\production\presentation\controller\PPHController.java`

**class** `PPHController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final PPHRepository pphRepository`
  - `private final PPHMapper pphMapper`
  - `private final CreerPPHUseCase creerPPHUseCase`
  - `private final ValiderPPHUseCase validerPPHUseCase`
  - `private final ModifierLignePPHUseCase modifierLignePPHUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public PPHController(PPHRepository pphRepository, PPHMapper pphMapper, CreerPPHUseCase creerPPHUseCase,
                          ValiderPPHUseCase validerPPHUseCase, ModifierLignePPHUseCase modifierLignePPHUseCase,
                          AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PPHResponse>> creer(@Valid @RequestBody CreerPPHRequest req)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PPHResponse>> valider(@PathVariable String reference)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<PPHResponse>> demarrer(@PathVariable String reference)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PPHResponse>> cloturer(@PathVariable String reference)`
  - `@PutMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<PPHResponse>> modifierLignes(@PathVariable String reference,
        @Valid @RequestBody ModifierLignesPPHRequest req)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PPHResponse>> enCours()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PPHResponse>> consulter(@PathVariable String reference)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<List<PPHResponse>>> listerParBC(@PathVariable String referenceBC)`

#### `modules\production\presentation\controller\RapportProductionController.java`

**class** `RapportProductionController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final GenererRapportLotsUseCase genererRapportLotsUseCase`
  - `private final GenererRapportHebdomadaireUseCase genererRapportHebdomadaireUseCase`
  - `private final GenererRapportComparatifBCUseCase genererRapportComparatifBCUseCase`

- **Méthodes :**
  - `public RapportProductionController(GenererRapportLotsUseCase genererRapportLotsUseCase,
                                        GenererRapportHebdomadaireUseCase genererRapportHebdomadaireUseCase,
                                        GenererRapportComparatifBCUseCase genererRapportComparatifBCUseCase)`
  - `RapportComparatifBC>> comparatifBC(@RequestParam String referenceBC)`

#### `modules\production\presentation\controller\RegistreController.java`

**class** `RegistreController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final EnregistrerRegistreUseCase enregistrerUseCase`
  - `private final RegistreProductionRepository repository`
  - `private final RegistreMapper mapper`
  - `private final AuthenticatedUserResolver userResolver`
  - `List<RegistreProductionResponse> list`

- **Méthodes :**
  - `public RegistreController(EnregistrerRegistreUseCase enregistrerUseCase,
                               RegistreProductionRepository repository,
                               RegistreMapper mapper,
                               AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<RegistreProductionResponse>> enregistrer(@RequestBody @Valid EnregistrerRegistreRequest req)`


### Module `rp`

#### `modules\rp\application\RpDashboardService.java`

**class** `RpDashboardService` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final PrimeRepository primeRepository`
  - `private final GrandCompteJpaRepository grandCompteJpaRepository`
  - `private final CampagneJpaRepository campagneJpaRepository`
  - `private final TauxOccupationMarcheService tauxOccupationMarcheService`

- **Méthodes :**
  - `public RpDashboardService(PrimeRepository primeRepository,
                               GrandCompteJpaRepository grandCompteJpaRepository,
                               CampagneJpaRepository campagneJpaRepository,
                               TauxOccupationMarcheService tauxOccupationMarcheService)`
  - `public RpDashboardResponse dashboard()`
  - `public List<PrimeResponse> primes()`
  - `public List<PerformanceGammeResponse> performanceGammes()`
  - `public List<OccupationMarcheResponse> occupation()`
  - `public List<GrandCompteResponse> grandsComptes()`
  - `public List<CampagneResponse> campagnes()`

#### `modules\rp\application\dto\RpDashboardResponse.java`

**record** `RpDashboardResponse` (public)

- **Composants (record) :**
  - `int nbEligibles`
  - `double totalPrimes`
  - `int occupationMoyenne`
  - `int grandsComptesActifs`
  - `int relancesDues`
  - `double caGrandsComptes`
  - `List<PrimeResponse> primes`
  - `List<PerformanceGammeResponse> gammes`
  - `List<OccupationMarcheResponse> occupation`
  - `List<GrandCompteResponse> grandsComptes`
  - `List<CampagneResponse> campagnes`

- **Méthodes :**
  - `public record PrimeResponse(String commercialId, String nom, int tauxGlobal,
        int gammesValidees, int gammesTotal, boolean eligible, double montant)`
  - `public record PerformanceGammeResponse(String gamme, double ca, double objectif, int marge, String stock)`
  - `public record OccupationMarcheResponse(String zone, String commercialId, int livrees, int repertoriees, String potentiel)`
  - `public record GrandCompteResponse(Long id, String nom, String type, String contact, String telephone,
        double caMensuel, LocalDate derniereCommande, LocalDate prochaineRelance, String statut)`
  - `public record CampagneResponse(Long id, String titre, String cible, String canal, String statut,
        LocalDate datePublication, int portee)`

**record** `PrimeResponse` (public)

- **Composants (record) :**
  - `String commercialId`
  - `String nom`
  - `int tauxGlobal`
  - `int gammesValidees`
  - `int gammesTotal`
  - `boolean eligible`
  - `double montant`

**record** `PerformanceGammeResponse` (public)

- **Composants (record) :**
  - `String gamme`
  - `double ca`
  - `double objectif`
  - `int marge`
  - `String stock`

**record** `OccupationMarcheResponse` (public)

- **Composants (record) :**
  - `String zone`
  - `String commercialId`
  - `int livrees`
  - `int repertoriees`
  - `String potentiel`

**record** `GrandCompteResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String nom`
  - `String type`
  - `String contact`
  - `String telephone`
  - `double caMensuel`
  - `LocalDate derniereCommande`
  - `LocalDate prochaineRelance`
  - `String statut`

**record** `CampagneResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String titre`
  - `String cible`
  - `String canal`
  - `String statut`
  - `LocalDate datePublication`
  - `int portee`

#### `modules\rp\infrastructure\persistence\jpa\CampagneJpaEntity.java`

**class** `CampagneJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String titre`
  - `@Column(nullable = false) private String cible`
  - `@Column(nullable = false) private String canal`
  - `@Column(nullable = false) private String statut`
  - `private LocalDate datePublication`
  - `@Column(nullable = false) private int portee`

#### `modules\rp\infrastructure\persistence\jpa\CampagneJpaRepository.java`

**interface** `CampagneJpaRepository` (public) extends `JpaRepository<CampagneJpaEntity, Long>`

#### `modules\rp\infrastructure\persistence\jpa\GrandCompteJpaEntity.java`

**class** `GrandCompteJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String nom`
  - `@Column(nullable = false) private String type`
  - `private String contact`
  - `private String telephone`
  - `@Column(nullable = false) private double caMensuel`
  - `private LocalDate derniereCommande`
  - `private LocalDate prochaineRelance`
  - `@Column(nullable = false) private String statut`

#### `modules\rp\infrastructure\persistence\jpa\GrandCompteJpaRepository.java`

**interface** `GrandCompteJpaRepository` (public) extends `JpaRepository<GrandCompteJpaEntity, Long>`

#### `modules\rp\presentation\controller\RpController.java`

**class** `RpController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
@PreAuthorize("")
public)

- **Champs :**
  - `private final RpDashboardService service`

- **Méthodes :**
  - `public RpController(RpDashboardService service)`
  - `@GetMapping("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<RpDashboardResponse>> dashboard()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<PrimeResponse>>> primes()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<GrandCompteResponse>>> grandsComptes()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<CampagneResponse>>> campagnes()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<OccupationMarcheResponse>>> occupation()`


### Module `secretaire`

#### `modules\secretaire\application\SecretaireDashboardService.java`

**class** `SecretaireDashboardService` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private static final double SEUIL_SECURISATION = 500_000.0`
  - `private final VersementRepository versementRepository`
  - `private final PetiteCaisseJpaRepository petiteCaisseJpaRepository`
  - `private final MicroStockJpaRepository microStockJpaRepository`
  - `private final DocumentRecuJpaRepository documentRecuJpaRepository`
  - `double soldeCaisse = totalEntrees - totalSorties`
  - `boolean seuilDepasse = soldeCaisse > SEUIL_SECURISATION`

- **Méthodes :**
  - `public SecretaireDashboardService(VersementRepository versementRepository,
                                       PetiteCaisseJpaRepository petiteCaisseJpaRepository,
                                       MicroStockJpaRepository microStockJpaRepository,
                                       DocumentRecuJpaRepository documentRecuJpaRepository)`
  - `public SecretaireDashboardResponse dashboard()`
  - `public List<PetiteCaisseResponse> petiteCaisse()`
  - `public List<MicroStockResponse> microStock()`
  - `public List<DocumentRecuResponse> documents()`

#### `modules\secretaire\application\dto\SecretaireDashboardResponse.java`

**record** `SecretaireDashboardResponse` (public)

- **Composants (record) :**
  - `int versementsEnAttente`
  - `int commandesAValider`
  - `int alertesRouges`
  - `double totalVerse`
  - `double totalEntrees`
  - `double totalSorties`
  - `double soldeCaisse`
  - `boolean seuilDepasse`
  - `int microStockBas`
  - `double valeurMicroStock`
  - `int docsIncomplets`
  - `List<PetiteCaisseResponse> petiteCaisse`
  - `List<MicroStockResponse> microStock`
  - `List<DocumentRecuResponse> documents`

- **Méthodes :**
  - `public record PetiteCaisseResponse(Long id, String libelle, String type, double montant, String heure, String categorie, LocalDateTime dateOperation)`
  - `public record MicroStockResponse(Long id, String code, String designation, int qte, double prixUnite, int seuil)`
  - `public record DocumentRecuResponse(Long id, String matriculeCommercial, String nom, boolean feuilleRoute, boolean bonCommande, boolean factures)`

**record** `PetiteCaisseResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String libelle`
  - `String type`
  - `double montant`
  - `String heure`
  - `String categorie`
  - `LocalDateTime dateOperation`

**record** `MicroStockResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String code`
  - `String designation`
  - `int qte`
  - `double prixUnite`
  - `int seuil`

**record** `DocumentRecuResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matriculeCommercial`
  - `String nom`
  - `boolean feuilleRoute`
  - `boolean bonCommande`
  - `boolean factures`

#### `modules\secretaire\infrastructure\persistence\jpa\DocumentRecuJpaEntity.java`

**class** `DocumentRecuJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private String nom`
  - `@Column(nullable = false) private boolean feuilleRoute`
  - `@Column(nullable = false) private boolean bonCommande`
  - `@Column(nullable = false) private boolean factures`

#### `modules\secretaire\infrastructure\persistence\jpa\DocumentRecuJpaRepository.java`

**interface** `DocumentRecuJpaRepository` (public) extends `JpaRepository<DocumentRecuJpaEntity, Long>`

#### `modules\secretaire\infrastructure\persistence\jpa\MicroStockJpaEntity.java`

**class** `MicroStockJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String code`
  - `@Column(nullable = false) private String designation`
  - `@Column(nullable = false) private int qte`
  - `@Column(nullable = false) private double prixUnite`
  - `@Column(nullable = false) private int seuil`

#### `modules\secretaire\infrastructure\persistence\jpa\MicroStockJpaRepository.java`

**interface** `MicroStockJpaRepository` (public) extends `JpaRepository<MicroStockJpaEntity, Long>`

#### `modules\secretaire\infrastructure\persistence\jpa\PetiteCaisseJpaEntity.java`

**class** `PetiteCaisseJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String libelle`
  - `@Column(nullable = false) private String type`
  - `@Column(nullable = false) private double montant`
  - `@Column(nullable = false) private String heure`
  - `@Column(nullable = false) private String categorie`
  - `@Column(nullable = false) private LocalDateTime dateOperation`

#### `modules\secretaire\infrastructure\persistence\jpa\PetiteCaisseJpaRepository.java`

**interface** `PetiteCaisseJpaRepository` (public) extends `JpaRepository<PetiteCaisseJpaEntity, Long>`

#### `modules\secretaire\presentation\controller\SecretaireController.java`

**class** `SecretaireController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
@PreAuthorize("")
public)

- **Champs :**
  - `private final SecretaireDashboardService service`

- **Méthodes :**
  - `public SecretaireController(SecretaireDashboardService service)`
  - `@GetMapping("")
    @Operation(summary = "",
        description = "") public ResponseEntity<ApiResponse<SecretaireDashboardResponse>> dashboard()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<PetiteCaisseResponse>>> petiteCaisse()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<MicroStockResponse>>> microStock()`
  - `@GetMapping("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<DocumentRecuResponse>>> documents()`


### Module `shared`

#### `shared\application\audit\dto\ActiviteSuspecteResponse.java`

**record** `ActiviteSuspecteResponse` (public)

- **Composants (record) :**
  - `String matricule`
  - `String ressource`
  - `long occurrences`

#### `shared\application\audit\dto\JournalAuditResponse.java`

**record** `JournalAuditResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matricule`
  - `String role`
  - `TypeActionAudit typeAction`
  - `String module`
  - `String ressource`
  - `String referenceEntite`
  - `String methodeHttp`
  - `String endpoint`
  - `String details`
  - `boolean succes`
  - `String messageErreur`
  - `String adresseIp`
  - `long dureeMs`
  - `LocalDateTime horodatage`

#### `shared\application\dto\ApiResponse.java`

**class** `ApiResponse` (@JsonInclude(JsonInclude.Include.NON_NULL)
public)

- **Champs :**
  - `private boolean succes`
  - `private String message`
  - `private T donnees`
  - `private String erreur`
  - `private LocalDateTime horodatage`
  - `ApiResponse<T> response = new ApiResponse<>()`
  - `ApiResponse<T> response = new ApiResponse<>()`
  - `ApiResponse<T> response = new ApiResponse<>()`

- **Méthodes :**
  - `private ApiResponse()`
  - `public static <T> ApiResponse<T> succes(T donnees, String message)`
  - `public static <T> ApiResponse<T> succes(T donnees)`
  - `public static <T> ApiResponse<T> erreur(String erreur)`
  - `public static <T> ApiResponse<T> erreur(String message, String erreur)`
  - `public boolean isSucces()`
  - `public String getMessage()`
  - `public T getDonnees()`
  - `public String getErreur()`
  - `public LocalDateTime getHorodatage()`

#### `shared\application\dto\PageResponse.java`

**class** `PageResponse` (public)

- **Champs :**
  - `private final List<T> contenu`
  - `private final long totalElements`
  - `private final int totalPages`
  - `private final int pageCourante`
  - `private final int taillePage`
  - `private final boolean dernierePage`

- **Méthodes :**
  - `public PageResponse(List<T> contenu, long totalElements, int totalPages,
                        int pageCourante, int taillePage, boolean dernierePage)`
  - `public static <T> PageResponse<T> de(Page<T> page)`
  - `public List<T> getContenu()`
  - `public long getTotalElements()`
  - `public int getTotalPages()`
  - `public int getPageCourante()`
  - `public int getTaillePage()`
  - `public boolean isDernierePage()`

#### `shared\application\rapport\FormatRapport.java`

**enum** `FormatRapport` (public)

- **Constantes :**
  - `{
    PDF("", "")`
  - `XLSX("", "")`
  - `CSV("", "")`
  - `JSON("", "")`

- **Champs :**
  - `private final String contentType`
  - `private final String extension`

- **Méthodes :**
  - `public String getContentType()`
  - `public String getExtension()`
  - `public static FormatRapport depuis(String valeur)`

#### `shared\application\rapport\RapportFichier.java`

**record** `RapportFichier` (public)

- **Composants (record) :**
  - `String nomFichier`
  - `String contentType`
  - `byte[] contenu`

#### `shared\infrastructure\audit\AuditAspect.java`

**class** `AuditAspect` (@Aspect
@Component
public)

- **Champs :**
  - `private final AuditService auditService`
  - `String matricule = null`
  - `String role = null`
  - `String marqueur = ""`
  - `String nom = noms[i]`
  - `Object v = valeurs[i]`

- **Méthodes :**
  - `public AuditAspect(AuditService auditService)`
  - `@Around("") public Object tracer(ProceedingJoinPoint joinPoint)` throws `Throwable`
  - `private HttpServletRequest requeteCourante()`
  - `private String adresseIpReelle(HttpServletRequest request)`
  - `private String extraireModule(String nomComplet)`
  - `private TypeActionAudit deduireTypeAction(String methodeHttp, String endpoint)`
  - `private String resumerArguments(ProceedingJoinPoint joinPoint)`

#### `shared\infrastructure\audit\AuditService.java`

**class** `AuditService` (@Service
public)

- **Champs :**
  - `private final JournalAuditJpaRepository repository`

- **Méthodes :**
  - `public AuditService(JournalAuditJpaRepository repository)`
  - `@Transactional(propagation = Propagation.REQUIRES_NEW) public void enregistrer(String matricule, String role, TypeActionAudit typeAction, String module,
                             String ressource, String referenceEntite, String methodeHttp, String endpoint,
                             String details, boolean succes, String messageErreur, String adresseIp, long dureeMs)`
  - `public void tracer(String matricule, TypeActionAudit typeAction, String module, String ressource,
                        String referenceEntite, String details)`
  - `private String tronquer(String valeur, int max)`

#### `shared\infrastructure\audit\JournalAuditJpaEntity.java`

**class** `JournalAuditJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, length = 20) private String matricule`
  - `@Column(length = 40) private String role`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30) private TypeActionAudit typeAction`
  - `@Column(length = 40) private String module`
  - `@Column(length = 80) private String ressource`
  - `@Column(length = 100) private String referenceEntite`
  - `@Column(length = 10) private String methodeHttp`
  - `@Column(length = 300) private String endpoint`
  - `@Column(length = 500) private String details`
  - `@Column(nullable = false) private boolean succes`
  - `@Column(length = 500) private String messageErreur`
  - `@Column(length = 45) private String adresseIp`
  - `@Column(nullable = false) private long dureeMs`
  - `@Column(nullable = false, updatable = false) private LocalDateTime horodatage`

#### `shared\infrastructure\audit\JournalAuditJpaRepository.java`

**interface** `JournalAuditJpaRepository` (public) extends `JpaRepository<JournalAuditJpaEntity, Long>`

**interface** `ActiviteSuspecteProjection`

#### `shared\infrastructure\config\MinioConfig.java`

**class** `MinioConfig` (@Configuration
public)

#### `shared\infrastructure\config\RabbitMQConfig.java`

**class** `RabbitMQConfig` (@Configuration
public)

- **Champs :**
  - `public static final String EXCHANGE_TANTY = ""`
  - `public static final String QUEUE_STOCK_SEUIL = ""`
  - `public static final String QUEUE_DOTATION_APPROUVEE = ""`
  - `public static final String QUEUE_LOT_DECLARE = ""`
  - `public static final String QUEUE_LOT_VALIDE_STOCK = ""`
  - `public static final String QUEUE_VERSEMENT_ALERTE = ""`
  - `public static final String QUEUE_PRECOMMANDE_VALIDEE = ""`

- **Méthodes :**
  - `@Bean public TopicExchange tantyExchange()`
  - `@Bean Queue queueSeuilStock()`
  - `@Bean Queue queueDotationApprouvee()`
  - `@Bean Queue queueLotDeclare()`
  - `@Bean Queue queueLotValideStock()`
  - `@Bean Queue queueVersementAlerte()`
  - `@Bean Queue queuePreCommandeValidee()`
  - `@Bean Binding bindingSeuilStock(Queue queueSeuilStock, TopicExchange tantyExchange)`
  - `@Bean Binding bindingDotation(Queue queueDotationApprouvee, TopicExchange tantyExchange)`
  - `@Bean Binding bindingLotDeclare(Queue queueLotDeclare, TopicExchange tantyExchange)`
  - `@Bean Binding bindingLotValide(Queue queueLotValideStock, TopicExchange tantyExchange)`
  - `@Bean Binding bindingVersementAlerte(Queue queueVersementAlerte, TopicExchange tantyExchange)`
  - `@Bean Binding bindingPreCommande(Queue queuePreCommandeValidee, TopicExchange tantyExchange)`
  - `@Bean public Jackson2JsonMessageConverter messageConverter()`
  - `@Bean public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory)`

#### `shared\infrastructure\config\WebSocketConfig.java`

**class** `WebSocketConfig` (@Configuration
@EnableWebSocketMessageBroker
public) implements `WebSocketMessageBrokerConfigurer`

- **Méthodes :**
  - `@Override public void configureMessageBroker(MessageBrokerRegistry registry)`
  - `@Override public void registerStompEndpoints(StompEndpointRegistry registry)`

#### `shared\infrastructure\event\EventPublisherImpl.java`

**class** `EventPublisherImpl` (@Component
public) implements `EventPublisher, DomainEventPublisher`

- **Champs :**
  - `private final ApplicationEventPublisher applicationEventPublisher`

- **Méthodes :**
  - `public EventPublisherImpl(ApplicationEventPublisher applicationEventPublisher)`
  - `@Override public void publish(Object event)`
  - `@Override public void publishAll(Iterable<?> events)`

#### `shared\infrastructure\notification\EmailService.java`

**class** `EmailService` (@Service
public)

- **Champs :**
  - `private final JavaMailSender mailSender`
  - `private final String expediteur`

- **Méthodes :**
  - `public void envoyer(String destinataire, String sujet, String corps)`

#### `shared\infrastructure\otp\CodeOtpJpaEntity.java`

**class** `CodeOtpJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, length = 20) private String matricule`
  - `@Column(nullable = false, length = 6) private String code`
  - `@Column(nullable = false, length = 150) private String operationCible`
  - `@Column(nullable = false) private boolean utilise = false`
  - `@Column(nullable = false) private int tentativesVerification = 0`
  - `@Column(nullable = false) private LocalDateTime dateGeneration`
  - `@Column(nullable = false) private LocalDateTime dateExpiration`

#### `shared\infrastructure\otp\CodeOtpJpaRepository.java`

**interface** `CodeOtpJpaRepository` (public) extends `JpaRepository<CodeOtpJpaEntity, Long>`

#### `shared\infrastructure\otp\LogSmsGatewayAdapter.java`

**class** `LogSmsGatewayAdapter` (@Component
@ConditionalOnProperty(name = "", havingValue = "", matchIfMissing = true)
public) implements `SmsGatewayPort`

- **Champs :**
  - `@Value("") private String provider`

- **Méthodes :**
  - `@Override public void envoyer(String destinataire, String message)`

#### `shared\infrastructure\otp\OtpInvalideException.java`

**class** `OtpInvalideException` (public) extends `RegleMetierException`

- **Méthodes :**
  - `public OtpInvalideException(String message)`

#### `shared\infrastructure\otp\OtpService.java`

**class** `OtpService` (@Service
public)

- **Champs :**
  - `private static final int MAX_TENTATIVES_VERIFICATION = 3`
  - `private final CodeOtpJpaRepository repository`
  - `private final SmsGatewayPort smsGateway`
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final int dureeValiditeMinutes`

- **Méthodes :**
  - `@Transactional public void genererEtEnvoyer(String matricule, String operationCible)`
  - `@Transactional public void verifier(String matricule, String operationCible, String code)`

#### `shared\infrastructure\otp\SmsGatewayPort.java`

**interface** `SmsGatewayPort` (public)

#### `shared\infrastructure\parametres\ParametresSystemeInitializer.java`

**class** `ParametresSystemeInitializer` (@Component
public) implements `CommandLineRunner`

- **Champs :**
  - `private final ParametresSystemeJpaRepository repository`

- **Méthodes :**
  - `public ParametresSystemeInitializer(ParametresSystemeJpaRepository repository)`
  - `@Override public void run(String... args)`
  - `private void initIfAbsent(String cle, double valeur, String description)`

#### `shared\infrastructure\parametres\ParametresSystemeJpaEntity.java`

**class** `ParametresSystemeJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true, length = 60) private String cle`
  - `@Column(nullable = false) private double valeurNumerique`
  - `@Column(length = 200) private String description`
  - `@Column(length = 40) private String modifiePar`
  - `false, updatable = false)
    private java.time.LocalDateTime horodatage`

#### `shared\infrastructure\parametres\ParametresSystemeJpaRepository.java`

**interface** `ParametresSystemeJpaRepository` (public) extends `JpaRepository<ParametresSystemeJpaEntity, Long>`

#### `shared\infrastructure\rapport\TableExporter.java`

**class** `TableExporter` (@Component
public)

- **Champs :**
  - `private static final String EXCEL_GREEN_HEX = ""`
  - `private static final String EXCEL_CREAM_HEX = ""`
  - `private final ObjectMapper objectMapper`
  - `int c = 0`
  - `int r = 1`
  - `CellStyle style = (r % 2 == 0) ? styleLigneAlternee : styleCorps`
  - `int c = 0`
  - `int c = 0`
  - `int ligneIndex = 0`
  - `DeviceRgb fond = (ligneIndex % 2 == 0) ? CREAM : WHITE`

- **Méthodes :**
  - `public TableExporter(ObjectMapper objectMapper)`
  - `public byte[] versExcel(String titre, List<String> entetes, List<List<String>> lignes)`
  - `public byte[] versCsv(List<String> entetes, List<List<String>> lignes)`
  - `public byte[] versPdf(String titre, List<String> entetes, List<List<String>> lignes)`
  - `public byte[] versJson(Object donnees)`
  - `private XSSFColor couleurExcel(XSSFWorkbook workbook, String hex)`
  - `private String joindreCsv(List<String> valeurs)`
  - `private String echapperCsv(String valeur)`
  - `private String tronquer(String titre)`

#### `shared\infrastructure\security\AuthenticatedUserResolver.java`

**class** `AuthenticatedUserResolver` (@Component
public)

- **Méthodes :**
  - `public Utilisateur getUtilisateurCourant()`
  - `public String getMatriculeCourant()`
  - `public Role getRoleCourant()`
  - `public boolean aRole(Role role)`

#### `shared\infrastructure\seeding\AuthSeeder.java`

**class** `AuthSeeder` (@Component
@Order(1)
public) implements `DataSeeder`

- **Champs :**
  - `private static final String DEFAULT_PASSWORD = ""`
  - `private static final String DEFAULT_OTP_CODE = ""`
  - `private final UtilisateurJpaRepository utilisateurRepo`
  - `private final OtpCodeJpaRepository otpRepo`
  - `private final PasswordEncoder passwordEncoder`

- **Méthodes :**
  - `public AuthSeeder(UtilisateurJpaRepository utilisateurRepo,
                      OtpCodeJpaRepository otpRepo,
                      PasswordEncoder passwordEncoder)`
  - `@Override
    @Transactional public void seed()`
  - `private void seedUser(String matricule, String nom, String prenom, String email, Role role,
                          String telephone, String encodedPassword, LocalDateTime now,
                          String sexe, String lieuNaissance, String residence)`
  - `private void seedOtpForAll(LocalDateTime now, String otpHash)`

#### `shared\infrastructure\seeding\CommercialBaseSeeder.java`

**class** `CommercialBaseSeeder` (@Component
@Order(3)
public) implements `DataSeeder`

- **Champs :**
  - `private final ClientJpaRepository clientRepo`
  - `private final ProspectJpaRepository prospectRepo`
  - `private final GrilleTarifaireJpaRepository grilleRepo`
  - `private final ZoneDeVenteJpaRepository zoneRepo`
  - `private final BoutiquePotentielleJpaRepository boutiqueRepo`
  - `private final VehiculeJpaRepository vehiculeRepo`
  - `private final TerminalJpaRepository terminalRepo`
  - `Object[][] data = {
                {"", "", "", "", "", TypeClient.DISTRIBUTEUR, ""},
                {"", "", "", "", "", TypeClient.GROSSISTE, ""},
                {"", "", "", "", "", TypeClient.DETAIL, ""},
                {"", "", "", "", "", TypeClient.SEMI_GROSSISTE, ""},
                {"", "", "", "", "", TypeClient.DETAIL, ""},
                {"", "", "", "", "", TypeClient.DISTRIBUTEUR, ""},
                {"", "", "", "", "", TypeClient.DETAIL, ""},
                {"", "", "", "", "", TypeClient.GROSSISTE, ""},
                {"", "", "", "", "", TypeClient.DETAIL, ""},
                {"", "", "", "", "", TypeClient.SEMI_GROSSISTE, ""},
                {"", "", "", "", "", TypeClient.DETAIL, ""},
                {"", "", "", "", "", TypeClient.REVENDEUR, ""},
                {"", "", "", "", "", TypeClient.DISTRIBUTEUR, ""},
                {"", "", "", "", "", TypeClient.DETAIL, ""},
                {"", "", "", "", "", TypeClient.GROSSISTE, ""},
                {"", "", "", "", "", TypeClient.DETAIL, ""},
                {"", "", "", "", "", TypeClient.SEMI_GROSSISTE, ""},
                {"", "", "", "", "", TypeClient.DETAIL, ""},
                {"", "", "", "", "", TypeClient.REVENDEUR, ""},
                {"", "", "", "", "", TypeClient.SEMI_GROSSISTE, ""},
        }`
  - `String code = (String) d[0]`
  - `Object[][] data = {
                {"", "", "", "", "", "", CanalProspection.VISITE_TERRAIN, StatutProspect.NOUVEAU, null, ""},
                {"", "", "", "", "", "", CanalProspection.APPEL_TELEPHONIQUE, StatutProspect.NOUVEAU, null, ""},
                {"", "", "", "", "", "", CanalProspection.RECOMMANDATION, StatutProspect.CONVERTI, "", ""},
                {"", "", "", "", "", "", CanalProspection.VISITE_TERRAIN, StatutProspect.NON_INTERESSE, null, ""},
                {"", "", "", "", "", "", CanalProspection.RESEAU_SOCIAL, StatutProspect.NOUVEAU, null, ""},
                {"", "", "", "", "", "", CanalProspection.VISITE_TERRAIN, StatutProspect.CONVERTI, "", ""},
                {"", "", "", "", "", "", CanalProspection.APPEL_TELEPHONIQUE, StatutProspect.NOUVEAU, null, ""},
                {"", "", "", "", "", "", CanalProspection.VISITE_TERRAIN, StatutProspect.ARCHIVE, null, ""},
                {"", "", "", "", "", "", CanalProspection.RECOMMANDATION, StatutProspect.NOUVEAU, null, ""},
                {"", "", "", "", "", "", CanalProspection.VISITE_TERRAIN, StatutProspect.NOUVEAU, null, ""},
        }`
  - `String code = (String) d[0]`
  - `String[] productCodes = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}`
  - `TypeClient[] segments = {TypeClient.DISTRIBUTEUR, TypeClient.GROSSISTE, TypeClient.SEMI_GROSSISTE, TypeClient.DETAIL, TypeClient.REVENDEUR}`
  - `double[][] prices = {
                {1000, 1100, 1150, 1200, 1200},
                {4800, 5000, 5200, 5500, 5500},
                {9000, 9500, 10000, 10500, 10500},
                {800, 900, 950, 1000, 1000},
                {3800, 4000, 4200, 4500, 4500},
                {200, 250, 270, 300, 300},
                {400, 430, 470, 500, 500},
                {550, 600, 650, 700, 700},
                {300, 330, 370, 400, 400},
                {1200, 1300, 1400, 1500, 1500},
                {300, 330, 370, 400, 400},
                {1200, 1300, 1400, 1500, 1500},
                {180, 200, 220, 250, 250},
                {180, 200, 220, 250, 250},
                {650, 700, 750, 800, 800},
                {600, 650, 700, 750, 750},
                {230, 250, 280, 300, 300},
        }`
  - `int i = 0`
  - `int j = 0`
  - `Object[][] data = {
                {"", "", "", 120},
                {"", "", "", 200},
                {"", "", "", 150},
                {"", "", "", 180},
                {"", "", "", 80},
                {"", "", "", 60},
                {"", "", "", 40},
                {"", "", "", 35},
        }`
  - `String nom = (String) d[0]`
  - `Object[][] data = {
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
        }`
  - `Object[][] data = {
                {"", "", "", "", 8.5},
                {"", "", "", "", 9.0},
                {"", "", "", "", 8.8},
                {"", "", "", "", 6.5},
                {"", "", "", "", 6.0},
        }`
  - `String immat = (String) d[0]`
  - `Object[][] data = {
                {"", "", "", 85000, 0},
                {"", "", "", 85000, 15000},
                {"", "", "", 65000, 25000},
                {"", "", "", 65000, 0},
                {"", "", "", 45000, 20000},
        }`
  - `String imei = (String) d[0]`

- **Méthodes :**
  - `public CommercialBaseSeeder(ClientJpaRepository clientRepo,
                                ProspectJpaRepository prospectRepo,
                                GrilleTarifaireJpaRepository grilleRepo,
                                ZoneDeVenteJpaRepository zoneRepo,
                                BoutiquePotentielleJpaRepository boutiqueRepo,
                                VehiculeJpaRepository vehiculeRepo,
                                TerminalJpaRepository terminalRepo)`
  - `@Override
    @Transactional public void seed()`
  - `private void seedClients(LocalDateTime now)`
  - `private void seedProspects(LocalDateTime now)`
  - `private void seedGrilleTarifaire(LocalDateTime now)`
  - `private void seedZonesDeVente(LocalDateTime now)`
  - `private void seedBoutiques(LocalDateTime now)`
  - `private void seedVehicules(LocalDateTime now)`
  - `private void seedTerminaux(LocalDateTime now)`

#### `shared\infrastructure\seeding\CommercialTransactionalSeeder.java`

**class** `CommercialTransactionalSeeder` (@Component
@Order(4)
public) implements `DataSeeder`

- **Champs :**
  - `private final VenteJpaRepository venteRepo`
  - `private final PreCommandeJpaRepository preCommandeRepo`
  - `private final CommandeDistanceJpaRepository commandeDistanceRepo`
  - `private final FeuillRouteJpaRepository feuillRouteRepo`
  - `private final CarburantJpaRepository carburantRepo`
  - `private final RecouvrementJpaRepository recouvrementRepo`
  - `private final FactureAvoirJpaRepository factureAvoirRepo`
  - `private final PrixExceptionnelJpaRepository prixExceptionnelRepo`
  - `String[][] clients = {
                {"", ""}, {"", ""}, {"", ""},
                {"", ""}, {"", ""}, {"", ""},
                {"", ""}, {"", ""},
                {"", ""}, {"", ""}, {"", ""},
                {"", ""}, {"", ""},
        }`
  - `String[][] products = {
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
        }`
  - `StatutVente[] statuts = {StatutVente.FINALISEE, StatutVente.FINALISEE, StatutVente.FINALISEE,
                StatutVente.FINALISEE, StatutVente.EN_COURS, StatutVente.ANNULEE}`
  - `ModeReglement[] modes = {ModeReglement.COMPTANT, ModeReglement.COMPTANT, ModeReglement.CREDIT,
                ModeReglement.MOBILE_MONEY, ModeReglement.CHEQUE, ModeReglement.NON_APPLICABLE}`
  - `TypeVente[] types = {TypeVente.COMPTANT, TypeVente.COMPTANT, TypeVente.CREDIT,
                TypeVente.COMPTANT, TypeVente.COMPTANT, TypeVente.AVARIE}`
  - `SyncStatus[] syncs = {SyncStatus.SYNCED, SyncStatus.SYNCED, SyncStatus.SYNCED,
                SyncStatus.SYNCED, SyncStatus.PENDING, SyncStatus.CONFLICT}`
  - `int counter = 1`
  - `int day = 0`
  - `int idx = (counter - 1) % statuts.length`
  - `List<LigneVenteJpaEntity> lignes = new ArrayList<>()`
  - `int nbLignes = 1 + (counter % 3)`
  - `int l = 0`
  - `l < nbLignes`
  - `String[] prod = products[(counter + l) % products.length]`
  - `String[][] commercials = {{""}, {""}, {""}, {""}, {""}}`
  - `StatutPreCommande[] statuts = {
                StatutPreCommande.SOUMISE, StatutPreCommande.VALIDEE_SECRETAIRE,
                StatutPreCommande.VALIDEE_COMPTABLE, StatutPreCommande.LIVREE,
                StatutPreCommande.REFUSEE
        }`
  - `String[][] products = {
                {"", "", ""},
                {"", "", ""},
                {"", "", ""},
                {"", "", ""},
        }`
  - `int counter = 1`
  - `int day = 0`
  - `int idx = (counter - 1) % statuts.length`
  - `List<LignePreCommandeJpaEntity> lignes = new ArrayList<>()`
  - `int l = 0`
  - `String[] prod = products[(counter + l) % products.length]`
  - `Object[][] data = {
                {"", "", CanalCommande.APPEL_TELEPHONIQUE, StatutCommandeDistance.RECUE},
                {"", "", CanalCommande.WHATSAPP, StatutCommandeDistance.VALIDEE_SECRETAIRE},
                {"", "", CanalCommande.SMS, StatutCommandeDistance.VALIDEE_COMPTABLE},
                {"", "", CanalCommande.EMAIL, StatutCommandeDistance.LIVREE},
                {"", "", CanalCommande.WHATSAPP, StatutCommandeDistance.ANNULEE},
        }`
  - `String[][] products = {
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
        }`
  - `int counter = 1`
  - `StatutCommandeDistance statut = (StatutCommandeDistance) d[3]`
  - `List<LigneCommandeDistanceJpaEntity> lignes = new ArrayList<>()`
  - `int l = 0`
  - `String[] prod = products[(counter + l) % products.length]`
  - `String[][] commercials = {{""}, {""}, {""}, {""}, {""}}`
  - `StatutFeuillRoute[] statuts = {StatutFeuillRoute.CLOTURE, StatutFeuillRoute.EN_COURS, StatutFeuillRoute.PLANIFIE, StatutFeuillRoute.VERROUILLE}`
  - `String[][] allClients = {
                {""}, {""}, {""}, {""}, {""},
                {""}, {""}, {""},
                {""}, {""},
                {""}, {""}, {""}, {""}, {""},
                {""}, {""}, {""},
        }`
  - `InfoClientEnum[] infoCodes = {InfoClientEnum.Ab, InfoClientEnum.NC, InfoClientEnum.PA, InfoClientEnum.VPC, InfoClientEnum.EV, InfoClientEnum.AV, null, null, null, null}`
  - `StatutArret[] arretStatuts = {StatutArret.VISE, StatutArret.VISE, StatutArret.NON_VISE, StatutArret.PLANIFIE, StatutArret.ANNULE}`
  - `int counter = 1`
  - `int clientIdx = 0`
  - `int day = 0`
  - `int idx = (counter - 1) % statuts.length`
  - `List<ArretFeuillRouteJpaEntity> arrets = new ArrayList<>()`
  - `int a = 0`
  - `String clientCode = allClients[clientIdx % allClients.length][0]`
  - `InfoClientEnum infoCode = infoCodes[(counter + a) % infoCodes.length]`
  - `Object[][] data = {
                {"", "", "", "", 12500, 20.0, 13000.0, StatutCarburant.VALIDEE},
                {"", "", "", "", 18200, 30.0, 19500.0, StatutCarburant.VALIDEE},
                {"", "", "", "", 15000, 25.0, 16250.0, StatutCarburant.EN_ATTENTE_SIGNATURE},
                {"", "", "", "", 8400, 12.0, 9000.0, StatutCarburant.VALIDEE},
                {"", "", "", "", 9600, 14.0, 10200.0, StatutCarburant.REFUSEE},
                {"", "", "", "", 11200, 18.0, 11700.0, StatutCarburant.EN_ATTENTE_SIGNATURE},
        }`
  - `String ref = (String) d[0]`
  - `StatutCarburant statut = (StatutCarburant) d[7]`
  - `Object[][] data = {
                {"", "", "", 150000.0, 50000.0, StatutRecouvrement.REMBOURSE_PARTIEL, ""},
                {"", "", "", 320000.0, 0.0, StatutRecouvrement.EN_COURS, ""},
                {"", "", "", 85000.0, 85000.0, StatutRecouvrement.REMBOURSE_TOTAL, ""},
                {"", "", "", 220000.0, 0.0, StatutRecouvrement.CONTENTIEUX, ""},
                {"", "", "", 95000.0, 30000.0, StatutRecouvrement.REMBOURSE_PARTIEL, ""},
                {"", "", "", 180000.0, 0.0, StatutRecouvrement.EN_COURS, ""},
        }`
  - `String ref = (String) d[0]`
  - `Object[][] data = {
                {"", "", "", "", "", 15000.0, 2850.0, 17850.0, ""},
                {"", "", "", "", "", 8000.0, 1520.0, 9520.0, ""},
        }`
  - `String num = (String) d[0]`
  - `Object[][] data = {
                {"", "", "", 1200.0, 1000.0, "", ""},
                {"", "", "", 500.0, 400.0, "", ""},
        }`
  - `String ref = (String) d[0]`

- **Méthodes :**
  - `public CommercialTransactionalSeeder(VenteJpaRepository venteRepo,
                                         PreCommandeJpaRepository preCommandeRepo,
                                         CommandeDistanceJpaRepository commandeDistanceRepo,
                                         FeuillRouteJpaRepository feuillRouteRepo,
                                         CarburantJpaRepository carburantRepo,
                                         RecouvrementJpaRepository recouvrementRepo,
                                         FactureAvoirJpaRepository factureAvoirRepo,
                                         PrixExceptionnelJpaRepository prixExceptionnelRepo)`
  - `@Override
    @Transactional public void seed()`
  - `private void seedVentes(LocalDateTime now)`
  - `private void seedPreCommandes(LocalDateTime now)`
  - `private void seedCommandesDistance(LocalDateTime now)`
  - `private void seedFeuillesRoute(LocalDateTime now)`
  - `private void seedCarburants(LocalDateTime now)`
  - `private void seedRecouvrements(LocalDateTime now)`
  - `private void seedFacturesAvoir(LocalDateTime now)`
  - `private void seedPrixExceptionnels(LocalDateTime now)`

#### `shared\infrastructure\seeding\CommercialisationSeeder.java`

**class** `CommercialisationSeeder` (@Component
@Order(8)
public) implements `DataSeeder`

- **Champs :**
  - `private final CampagneMarketingJpaRepository campagneRepo`
  - `private final PromotionJpaRepository promotionRepo`
  - `String code = (String) d[0]`
  - `String code = (String) d[0]`

- **Méthodes :**
  - `public CommercialisationSeeder(CampagneMarketingJpaRepository campagneRepo,
                                   PromotionJpaRepository promotionRepo)`
  - `@Override
    @Transactional public void seed()`
  - `private void seedCampagnes()`
  - `private void seedPromotions()`

#### `shared\infrastructure\seeding\ComptabiliteSeeder.java`

**class** `ComptabiliteSeeder` (@Component
@Order(7)
public) implements `DataSeeder`

- **Champs :**
  - `private final FactureJpaRepository factureRepo`
  - `private final JournalCaisseJpaRepository caisseRepo`
  - `private final DecaissementJpaRepository decaissementRepo`
  - `private final ObjectifCommercialJpaRepository objectifRepo`
  - `private final PrimeJpaRepository primeRepo`
  - `private final VersementJpaRepository versementRepo`
  - `Object[][] data = {
                {"", "", TypeFacture.FACTURE, null, StatutFacture.PAYEE, ""},
                {"", "", TypeFacture.FACTURE, null, StatutFacture.EMISE, ""},
                {"", "", TypeFacture.FACTURE, null, StatutFacture.PAYEE, ""},
                {"", "", TypeFacture.FACTURE, null, StatutFacture.EMISE, ""},
                {"", "", TypeFacture.AVOIR, "", StatutFacture.EMISE, ""},
        }`
  - `String[][][] lignesData = {
                {{"", "", "", "", "", ""}, {"", "", "", "", "", ""}},
                {{"", "", "", "", "", ""}, {"", "", "", "", "", ""}},
                {{"", "", "", "", "", ""}, {"", "", "", "", "", ""}},
                {{"", "", "", "", "", ""}},
                {{"", "", "", "", "", ""}},
        }`
  - `int i = 0`
  - `Object[] d = data[i]`
  - `String num = (String) d[0]`
  - `List<LigneFactureJpaEntity> lignes = new ArrayList<>()`
  - `List<LigneCaisseJpaEntity> lignes = new ArrayList<>()`
  - `List<LigneCaisseJpaEntity> lignes = new ArrayList<>()`
  - `Object[][] data = {
                {"", TypeDepense.CARBURANT, "", "", "", "", 50000, "", StatutDecaissement.EXECUTE, ""},
                {"", TypeDepense.ACHAT_MATIERE_PREMIERE, "", "", null, null, 500000, "", StatutDecaissement.VALIDEE_DG, ""},
                {"", TypeDepense.SALAIRES, "", null, null, null, 1200000, "", StatutDecaissement.VALIDEE_CG, ""},
                {"", TypeDepense.MAINTENANCE_EQUIPEMENT, "", null, null, null, 75000, "", StatutDecaissement.PROPOSE, ""},
                {"", TypeDepense.TRANSPORT, "", "", null, null, 30000, "", StatutDecaissement.ANNULE, ""},
        }`
  - `String num = (String) d[0]`
  - `StatutDecaissement statut = (StatutDecaissement) d[8]`
  - `String[] commercials = {"", "", "", "", ""}`
  - `double[] globals = {500000, 400000, 350000, 300000, 250000}`
  - `int i = 0`
  - `String mat = commercials[i]`
  - `Object[][] data = {
                {"", "", 500000, 480000, 24000, StatutPrime.VALIDEE, ""},
                {"", "", 400000, 350000, 17500, StatutPrime.CALCULEE, null},
                {"", "", 350000, 360000, 18000, StatutPrime.VERSEE, ""},
                {"", "", 300000, 250000, 12500, StatutPrime.CALCULEE, null},
                {"", "", 250000, 200000, 10000, StatutPrime.VALIDEE, ""},
        }`
  - `String ref = (String) d[0]`
  - `Object[][] data = {
                {"", "", 150000, 150000, TypeVersement.ESPECES, StatutVersement.VALIDE, null, ""},
                {"", "", 80000, 75000, TypeVersement.ESPECES, StatutVersement.VALIDE, null, ""},
                {"", "", 120000, 115000, TypeVersement.MOBILE_MONEY, StatutVersement.VALIDE, null, ""},
                {"", "", 60000, 45000, TypeVersement.ESPECES, StatutVersement.ALERTE_ROUGE, null, null},
                {"", "", 90000, 90000, TypeVersement.CHEQUE, StatutVersement.EN_ATTENTE, null, null},
                {"", "", 100000, 95000, TypeVersement.ESPECES, StatutVersement.JUSTIFIE, "", null},
        }`
  - `String ref = (String) d[0]`
  - `double montantAttendu = (Double) d[2]`
  - `double cashVerse = (Double) d[3]`
  - `double ecart = cashVerse - montantAttendu`

- **Méthodes :**
  - `public ComptabiliteSeeder(FactureJpaRepository factureRepo,
                              JournalCaisseJpaRepository caisseRepo,
                              DecaissementJpaRepository decaissementRepo,
                              ObjectifCommercialJpaRepository objectifRepo,
                              PrimeJpaRepository primeRepo,
                              VersementJpaRepository versementRepo)`
  - `@Override
    @Transactional public void seed()`
  - `private void seedFactures(LocalDateTime now)`
  - `private void seedJournalCaisse(LocalDateTime now)`
  - `private void seedDecaissements(LocalDateTime now)`
  - `private void seedObjectifs(LocalDateTime now)`
  - `private void seedPrimes(LocalDateTime now)`
  - `private void seedVersements(LocalDateTime now)`

#### `shared\infrastructure\seeding\DataSeeder.java`

**interface** `DataSeeder` (public)

#### `shared\infrastructure\seeding\DataSeederRunner.java`

**class** `DataSeederRunner` (@Component
@Order(1)
public) implements `CommandLineRunner`

- **Champs :**
  - `private final List<DataSeeder> seeders`
  - `boolean seedRequested = false`

- **Méthodes :**
  - `public DataSeederRunner(List<DataSeeder> seeders)`
  - `@Override public void run(String... args)`

#### `shared\infrastructure\seeding\ProductionSeeder.java`

**class** `ProductionSeeder` (@Component
@Order(6)
public) implements `DataSeeder`

- **Champs :**
  - `private final PPHJpaRepository pphRepo`
  - `private final OFJpaRepository ofRepo`
  - `private final AffectationJournaliereJpaRepository affectationRepo`
  - `private final SessionBroyageJpaRepository broyageRepo`
  - `private final SessionDosageJpaRepository dosageRepo`
  - `private final LotJpaRepository lotRepo`
  - `private final ExpressionBesoinJpaRepository besoinRepo`
  - `private final FicheProduitJpaRepository ficheRepo`
  - `private final RegistreJpaRepository registreRepo`
  - `String pphRef = ""`
  - `List<LignePPHJpaEntity> lignes = new ArrayList<>()`
  - `String pphRef2 = ""`
  - `List<LignePPHJpaEntity> lignes2 = new ArrayList<>()`
  - `Object[][] data = {
                {"", "", "", "", 2000, 1800, StatutOF.HONORE},
                {"", "", "", "", 1500, 1400, StatutOF.HONORE},
                {"", "", "", "", 5000, 4800, StatutOF.HONORE},
                {"", "", "", "", 3000, 2500, StatutOF.HONORE},
                {"", "", "", "", 2200, 800, StatutOF.EN_COURS},
                {"", "", "", "", 1600, 0, StatutOF.PLANIFIE},
                {"", "", "", "", 5500, 1200, StatutOF.EN_COURS},
        }`
  - `String idOF = (String) d[0]`
  - `String pphRef = ""`
  - `List<LigneAffectationJpaEntity> lignes = new ArrayList<>()`
  - `String pphRef = ""`
  - `String pphRef = ""`
  - `MatierePremiereJpa> matieres = new ArrayList<>()`
  - `Object[][] data = {
                {"", "", "", "", 1800.0, 150, StatutLot.VALIDATED_BY_STOCK, "", ""},
                {"", "", "", "", 1400.0, 120, StatutLot.VALIDATED_BY_STOCK, "", ""},
                {"", "", "", "", 4800.0, 400, StatutLot.RECEPTIONNE_STOCK, "", null},
                {"", "", "", "", 800.0, 67, StatutLot.DECLARE, "", null},
                {"", "", "", "", 2400.0, 200, StatutLot.REJETE, "", null},
        }`
  - `String num = (String) d[0]`
  - `Object[][] data = {
                {"", "", TypeBesoin.MATIERE_PREMIERE, "", "", 500.0, "", "", StatutBesoin.VALIDE, "", null},
                {"", "", TypeBesoin.MATIERE_PREMIERE, "", "", 300.0, "", "", StatutBesoin.VALIDE, "", null},
                {"", "", TypeBesoin.EMBALLAGE, "", "", 20.0, "", "", StatutBesoin.EN_ATTENTE, null, null},
                {"", "", TypeBesoin.CONSOMMABLE, "", "", 10.0, "", "", StatutBesoin.REJETE, "", ""},
        }`
  - `String num = (String) d[0]`
  - `String pphRef = ""`
  - `List<LigneFicheProduitJpaEntity> lignes = new ArrayList<>()`
  - `String pphRef = ""`

- **Méthodes :**
  - `public ProductionSeeder(PPHJpaRepository pphRepo,
                            OFJpaRepository ofRepo,
                            AffectationJournaliereJpaRepository affectationRepo,
                            SessionBroyageJpaRepository broyageRepo,
                            SessionDosageJpaRepository dosageRepo,
                            LotJpaRepository lotRepo,
                            ExpressionBesoinJpaRepository besoinRepo,
                            FicheProduitJpaRepository ficheRepo,
                            RegistreJpaRepository registreRepo)`
  - `@Override
    @Transactional public void seed()`
  - `private void seedPPH(LocalDateTime now)`
  - `private void seedOFs(LocalDateTime now)`
  - `private void seedAffectations(LocalDateTime now)`
  - `private void addLigneAffectation(List<LigneAffectationJpaEntity> lignes, AffectationJournaliereJpaEntity aff,
                                      CodePoste poste, DureeAffectation duree, double objectif, String matricules)`
  - `private void seedBroyages(LocalDateTime now)`
  - `private void seedDosages(LocalDateTime now)`
  - `private void seedLots(LocalDateTime now)`
  - `private void seedExpressionsBesoin(LocalDateTime now)`
  - `private void seedFichesProduit(LocalDateTime now)`
  - `private void seedRegistres(LocalDateTime now)`
  - `private void addLigneRegistre(List<LigneRegistreJpaEntity> lignes, String matricule, String nom,
                                   boolean present, String motifAbsence, String poste, String heureArrivee,
                                   double qteRealisee, String unite)`

#### `shared\infrastructure\seeding\StockBaseSeeder.java`

**class** `StockBaseSeeder` (@Component
@Order(2)
public) implements `DataSeeder`

- **Champs :**
  - `private final FournisseurJpaRepository fournisseurRepo`
  - `private final EntrepotJpaRepository entrepotRepo`
  - `private final ProduitJpaRepository produitRepo`
  - `private final SeuilStockJpaRepository seuilRepo`
  - `String[][] data = {
                {"", "", "", "", ""},
                {"", "", "", "", ""},
                {"", "", "", "", ""},
                {"", "", "", "", ""},
                {"", "", "", "", ""},
        }`
  - `Object[][] data = {
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 1200.0, 5500.0, 1000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 5500.0, 5000.0, 5000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 10500.0, 10000.0, 10000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 1000.0, 4500.0, 1000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 4500.0, 4000.0, 5000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 300.0, 250.0, 300.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 500.0, 400.0, 500.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 700.0, 600.0, 700.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 4000.0, 3500.0, 9000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 400.0, 350.0, 250.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 1500.0, 1200.0, 1000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 400.0, 350.0, 250.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 1500.0, 1200.0, 1000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 250.0, 200.0, 50.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 250.0, 200.0, 50.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 800.0, 700.0, 200.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 750.0, 650.0, 150.0, "", ""},
                {"", "", "", "", "", CategorieProduit.PRODUIT_FINI, 300.0, 250.0, 100.0, "", ""},
                {"", "", "", "", "", CategorieProduit.MATIERE_PREMIERE, 0.0, 0.0, 50000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.MATIERE_PREMIERE, 0.0, 0.0, 50000.0, "", ""},
                {"", "", "", "", "", CategorieProduit.CONSOMMABLE, 0.0, 0.0, 0.0, "", ""},
                {"", "", "", "", "", CategorieProduit.CONSOMMABLE, 0.0, 0.0, 0.0, "", ""},
        }`
  - `String code = (String) d[0]`
  - `CategorieProduit cat = (CategorieProduit) d[5]`
  - `List<ConditionnementJpaEntity> conds = new ArrayList<>()`

- **Méthodes :**
  - `public StockBaseSeeder(FournisseurJpaRepository fournisseurRepo,
                           EntrepotJpaRepository entrepotRepo,
                           ProduitJpaRepository produitRepo,
                           SeuilStockJpaRepository seuilRepo)`
  - `@Override
    @Transactional public void seed()`
  - `private void seedFournisseurs(LocalDateTime now)`
  - `private void seedEntrepots(LocalDateTime now)`
  - `private void seedProduits(LocalDateTime now)`
  - `private ConditionnementJpaEntity buildCond(ProduitJpaEntity produit, String codeProduit,
                                                NiveauConditionnement niveau, String libelle,
                                                long facteur, double prixHt,
                                                double poidsG, double volCm3)`
  - `private void seedSeuils(LocalDateTime now)`

#### `shared\infrastructure\seeding\StockTransactionalSeeder.java`

**class** `StockTransactionalSeeder` (@Component
@Order(5)
public) implements `DataSeeder`

- **Champs :**
  - `private final BonCommandeJpaRepository bonCommandeRepo`
  - `private final ReceptionJpaRepository receptionRepo`
  - `private final BonTransfertJpaRepository bonTransfertRepo`
  - `private final DotationJpaRepository dotationRepo`
  - `private final BonRetourJpaRepository bonRetourRepo`
  - `private final SessionCommercialJpaRepository sessionCommercialRepo`
  - `private final NiveauStockJpaRepository niveauStockRepo`
  - `private final MouvementStockJpaRepository mouvementRepo`
  - `private final AlerteJpaRepository alerteRepo`
  - `private final SessionInventaireJpaRepository sessionInventaireRepo`
  - `String numero = (String) d[0]`
  - `List<LigneBonCommandeJpaEntity> lignes = new ArrayList<>()`
  - `String[][] products = {
                {"", ""},
                {"", ""},
                {"", ""},
                {"", ""},
                {"", ""},
        }`
  - `int i = 0`
  - `Object[] d = data[i]`
  - `String numero = (String) d[0]`
  - `String statut = (String) d[7]`
  - `List<LigneReceptionJpaEntity> lignes = new ArrayList<>()`
  - `String numero = (String) d[0]`
  - `List<LigneTransfertJpaEntity> lignes = new ArrayList<>()`
  - `Object[][] products = {
                {"", "", 50L},
                {"", "", 100L},
                {"", "", 30L},
                {"", "", 80L},
                {"", "", 60L},
        }`
  - `int i = 0`
  - `Object[] d = data[i]`
  - `String numero = (String) d[0]`
  - `String statut = (String) d[2]`
  - `List<LigneDotationJpaEntity> lignes = new ArrayList<>()`
  - `Object[] prod = products[i]`
  - `long qteDemandee = (Long) prod[2]`
  - `Object[][] data = {
                {"", "", "", "", "", 5, ""},
                {"", "", "", "", "", 10, ""},
                {"", "", "", "", "", 3, ""},
        }`
  - `String numero = (String) d[0]`
  - `List<LigneRetourJpaEntity> lignes = new ArrayList<>()`
  - `String numero = (String) d[0]`
  - `String[] centralProducts = {"", "", "", "", "", "", "", "", "", ""}`
  - `long[] centralQtys = {500, 400, 800, 600, 500, 300, 400, 300, 200, 150}`
  - `String[] tamponProducts = {"", "", "", ""}`
  - `long[] tamponQtys = {150, 120, 200, 100}`
  - `int i = 0`
  - `String code = centralProducts[i]`
  - `int i = 0`
  - `String code = tamponProducts[i]`
  - `String[] mobileProducts = {"", "", ""}`
  - `long[] mobileQtys = {30, 50, 20}`
  - `String[] mobileEntrepots = {"", "", ""}`
  - `int i = 0`
  - `String code = mobileProducts[i]`
  - `Object[][] data = {
                {"", "", "", null, "", 100, "", ""},
                {"", "", "", null, "", 80, "", ""},
                {"", "", "", "", "", 200, "", ""},
                {"", "", "", "", "", 300, "", ""},
                {"", "", "", "", "", 50, "", ""},
                {"", "", "", "", "", 5, "", ""},
        }`
  - `Object[][] data = {
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", null, null, ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", null, "", ""},
        }`
  - `String ref = (String) d[0]`
  - `String[][] invProducts = {
                {"", "", "", ""},
                {"", ""},
                {""},
        }`
  - `int i = 0`
  - `Object[] d = data[i]`
  - `String numero = (String) d[0]`
  - `String statut = (String) d[3]`
  - `List<LigneInventaireJpaEntity> lignes = new ArrayList<>()`

- **Méthodes :**
  - `public StockTransactionalSeeder(BonCommandeJpaRepository bonCommandeRepo,
                                    ReceptionJpaRepository receptionRepo,
                                    BonTransfertJpaRepository bonTransfertRepo,
                                    DotationJpaRepository dotationRepo,
                                    BonRetourJpaRepository bonRetourRepo,
                                    SessionCommercialJpaRepository sessionCommercialRepo,
                                    NiveauStockJpaRepository niveauStockRepo,
                                    MouvementStockJpaRepository mouvementRepo,
                                    AlerteJpaRepository alerteRepo,
                                    SessionInventaireJpaRepository sessionInventaireRepo)`
  - `@Override
    @Transactional public void seed()`
  - `private void seedBonsCommande(LocalDateTime now)`
  - `private void seedReceptions(LocalDateTime now)`
  - `private void seedBonsTransfert(LocalDateTime now)`
  - `private void seedDotations(LocalDateTime now)`
  - `private void seedBonsRetour(LocalDateTime now)`
  - `private void seedSessionsCommercial(LocalDateTime now)`
  - `private void seedNiveauxStock(LocalDateTime now)`
  - `private void seedMouvements(LocalDateTime now)`
  - `private void seedAlertes(LocalDateTime now)`
  - `private void seedInventaires(LocalDateTime now)`

#### `shared\infrastructure\storage\DocumentStorageService.java`

**class** `DocumentStorageService` (@Service
public)

- **Champs :**
  - `@Value("") private String storagePath`
  - `String cle = typeDocument + "" + reference + "" + extension`

- **Méthodes :**
  - `public String archiver(String typeDocument, String reference, String extension,
                            InputStream contenu, long tailleOctets, String contentType)`
  - `public InputStream recuperer(String cle)`
  - `public boolean existe(String cle)`
  - `public long taille(String cle)`

#### `shared\infrastructure\web\GlobalExceptionHandler.java`

**class** `GlobalExceptionHandler` (@RestControllerAdvice
public)

- **Méthodes :**
  - `@ExceptionHandler(CredentialsInvalidesException.class) public ResponseEntity<ApiResponse<Void>> handleCredentialsInvalides(CredentialsInvalidesException ex)`
  - `@ExceptionHandler(CompteBloqueException.class) public ResponseEntity<ApiResponse<Void>> handleCompteBloque(CompteBloqueException ex)`
  - `@ExceptionHandler(CompteDesactiveException.class) public ResponseEntity<ApiResponse<Void>> handleCompteDesactive(CompteDesactiveException ex)`
  - `@ExceptionHandler(AdminException.class) public ResponseEntity<ApiResponse<Void>> handleAdmin(AdminException ex)`
  - `@ExceptionHandler(EntiteNonTrouveeException.class) public ResponseEntity<ApiResponse<Void>> handleNonTrouvee(EntiteNonTrouveeException ex)`
  - `@ExceptionHandler({AccesRefuseException.class, AccessDeniedException.class}) public ResponseEntity<ApiResponse<Void>> handleAccesRefuse(Exception ex)`
  - `@ExceptionHandler(RegleMetierException.class) public ResponseEntity<ApiResponse<Void>> handleRegleMetier(RegleMetierException ex)`
  - `@ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException ex)`
  - `@ExceptionHandler(IllegalStateException.class) public ResponseEntity<ApiResponse<Void>> handleConflict(IllegalStateException ex)`
  - `@ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex)`
  - `@ExceptionHandler(Exception.class) public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex)`

#### `shared\kernel\audit\TypeActionAudit.java`

**enum** `TypeActionAudit` (public)

#### `shared\kernel\event\DomainEvent.java`

**interface** `DomainEvent` (public)

#### `shared\kernel\event\DomainEventPublisher.java`

**interface** `DomainEventPublisher` (public)

#### `shared\kernel\event\EventPublisher.java`

**interface** `EventPublisher` (public)

#### `shared\kernel\exception\AccesRefuseException.java`

**class** `AccesRefuseException` (public) extends `DomainException`

- **Méthodes :**
  - `public AccesRefuseException(String message)`

#### `shared\kernel\exception\DomainException.java`

**class** `DomainException` (public abstract) extends `RuntimeException`

- **Méthodes :**
  - `protected DomainException(String message)`
  - `protected DomainException(String message, Throwable cause)`

#### `shared\kernel\exception\EntiteNonTrouveeException.java`

**class** `EntiteNonTrouveeException` (public) extends `DomainException`

- **Méthodes :**
  - `public EntiteNonTrouveeException(String message)`
  - `public static EntiteNonTrouveeException de(String typeEntite, String identifiant)`

#### `shared\kernel\exception\RegleMetierException.java`

**class** `RegleMetierException` (public) extends `DomainException`

- **Méthodes :**
  - `public RegleMetierException(String message)`

#### `shared\kernel\valueobject\ProductId.java`

**class** `ProductId` (public final)

- **Champs :**
  - `private final Long value`
  - `ProductId productId= (ProductId) o`

- **Méthodes :**
  - `public ProductId(Long value)`
  - `public Long getValue()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `shared\kernel\valueobject\Quantity.java`

**class** `Quantity` (public final)

- **Champs :**
  - `private final BigDecimal value`
  - `private final String unit`

- **Méthodes :**
  - `public Quantity(BigDecimal value, String unit)`
  - `public BigDecimal getValue()`
  - `public String getUnit()`
  - `public Quantity add(Quantity other)`
  - `public Quantity subtract(Quantity other)`

#### `shared\kernel\valueobject\UserId.java`

**class** `UserId` (public final)

- **Champs :**
  - `private final String matricule`
  - `UserId userId= (UserId) o`

- **Méthodes :**
  - `public UserId(String matricule)`
  - `public String getMatricule()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `shared\presentation\controller\AuditController.java`

**class** `AuditController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
@PreAuthorize("")
public)

- **Champs :**
  - `private final JournalAuditJpaRepository repository`

- **Méthodes :**
  - `public AuditController(JournalAuditJpaRepository repository)`
  - `private JournalAuditResponse versResponse(JournalAuditJpaEntity e)`

#### `shared\presentation\controller\DocumentController.java`

**class** `DocumentController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DocumentStorageService storageService`

- **Méthodes :**
  - `public DocumentController(DocumentStorageService storageService)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> recuperer(@RequestParam String cle)` throws `java.io.IOException`
  - `private String guessContentType(String cle)`

#### `shared\presentation\controller\OtpController.java`

**class** `OtpController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final OtpService otpService`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public OtpController(OtpService otpService, AuthenticatedUserResolver userResolver)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> generer(@RequestBody GenererOtpRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> verifier(@RequestBody VerifierOtpRequest request)`
  - `public record GenererOtpRequest(String operationCible)`
  - `public record VerifierOtpRequest(String operationCible, String code)`

**record** `GenererOtpRequest` (public)

- **Composants (record) :**
  - `String operationCible`

**record** `VerifierOtpRequest` (public)

- **Composants (record) :**
  - `String operationCible`
  - `String code`


### Module `stock`

#### `modules\stock\application\alerte\dto\response\AlerteResponse.java`

**record** `AlerteResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String reference`
  - `String type`
  - `String typeLibelle`
  - `String message`
  - `String destinataireRole`
  - `String actionSuggeree`
  - `String priorite`
  - `String codeProduit`
  - `String codeEntrepot`
  - `String referenceLiee`
  - `String statut`
  - `LocalDateTime dateCreation`
  - `LocalDateTime dateTraitement`

#### `modules\stock\application\alerte\mapper\AlerteMapper.java`

**class** `AlerteMapper` (@Component
public)

- **Méthodes :**
  - `public AlerteResponse versResponse(Alerte a)`

#### `modules\stock\application\alerte\usecase\ListerAlertesUseCase.java`

**class** `ListerAlertesUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final AlerteRepository alerteRepository`
  - `private final AlerteMapper alerteMapper`

- **Méthodes :**
  - `public ListerAlertesUseCase(AlerteRepository alerteRepository, AlerteMapper alerteMapper)`
  - `public List<AlerteResponse> executer(String statut, String role)`

#### `modules\stock\application\alerte\usecase\TraiterAlerteUseCase.java`

**class** `TraiterAlerteUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final AlerteRepository alerteRepository`
  - `private final AlerteMapper alerteMapper`

- **Méthodes :**
  - `public TraiterAlerteUseCase(AlerteRepository alerteRepository, AlerteMapper alerteMapper)`
  - `public AlerteResponse executer(String reference, Action action, Utilisateur acteur)`

**enum** `Action` (public)

#### `modules\stock\application\catalogue\dto\request\AjouterConditionnementRequest.java`

_Fichier vide ou non analysable._

#### `modules\stock\application\catalogue\dto\request\CreerProduitRequest.java`

_Fichier vide ou non analysable._

#### `modules\stock\application\catalogue\dto\request\ModifierProduitRequest.java`

**record** `ModifierProduitRequest` (public)

- **Composants (record) :**
  - `String marque`
  - `String gamme`
  - `String variete`
  - `String designation`
  - `String categorieMetier`
  - `Double prixDistributeur`
  - `Double prixGrossiste`
  - `Double prixDetaillant`
  - `Boolean tva`
  - `String conditionnementCommercial`
  - `String uniteVenteCommerciale`

#### `modules\stock\application\catalogue\dto\response\CatalogueHierarchieResponse.java`

**record** `CatalogueHierarchieResponse` (public)

- **Composants (record) :**
  - `List<Marque> marques`
  - `List<Gamme> gammes`
  - `List<Variete> varietes`

- **Méthodes :**
  - `public record Marque(long id, String nom, String code)`
  - `public record Gamme(long id, long marqueId, String nom, String code)`
  - `public record Variete(long id, long gammeId, String nom, String code)`

**record** `Marque` (public)

- **Composants (record) :**
  - `long id`
  - `String nom`
  - `String code`

**record** `Gamme` (public)

- **Composants (record) :**
  - `long id`
  - `long marqueId`
  - `String nom`
  - `String code`

**record** `Variete` (public)

- **Composants (record) :**
  - `long id`
  - `long gammeId`
  - `String nom`
  - `String code`

#### `modules\stock\application\catalogue\dto\response\ConditionnementResponse.java`

**record** `ConditionnementResponse` (public)

- **Composants (record) :**
  - `String id`
  - `String sku`
  - `String niveau`
  - `String niveauLibelle`
  - `String libelle`
  - `long facteurConversionVersBase`
  - `int nombreUnites`
  - `String uniteConditionnement`
  - `double poidsUnitaireGrammes`
  - `double volumeUnitaireCm3`
  - `BigDecimal prixUnitaireHT`
  - `String codeBarres`

#### `modules\stock\application\catalogue\dto\response\ProduitResponse.java`

**record** `ProduitResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String code`
  - `String marque`
  - `String gamme`
  - `String variete`
  - `String designation`
  - `String categorie`
  - `String categorieLibelle`
  - `String categorieMetier`
  - `String categorieMetierLibelle`
  - `boolean actif`
  - `Double prixDistributeur`
  - `Double prixGrossiste`
  - `Double prixDetaillant`
  - `boolean tva`
  - `String conditionnementCommercial`
  - `String uniteVenteCommerciale`
  - `List<ConditionnementResponse> conditionnements`
  - `LocalDateTime dateCreation`
  - `LocalDateTime dateModification`

#### `modules\stock\application\catalogue\mapper\ProduitMapper.java`

**class** `ProduitMapper` (@Component
public)

- **Méthodes :**
  - `public ProduitResponse versResponse(Produit produit)`
  - `public ConditionnementResponse versConditionnementResponse(Conditionnement c)`

#### `modules\stock\application\catalogue\usecase\consulter\ConsulterProduitUseCase.java`

**class** `ConsulterProduitUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final ProduitRepository produitRepository`
  - `private final ProduitMapper produitMapper`

- **Méthodes :**
  - `public ConsulterProduitUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper)`
  - `public ProduitResponse executer(String codeProduit)`

#### `modules\stock\application\catalogue\usecase\creer\CreerProduitUseCase.java`

**class** `CreerProduitUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ProduitRepository produitRepository`
  - `private final ProduitMapper produitMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public CreerProduitUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper,
                               EventPublisher eventPublisher)`
  - `public ProduitResponse executer(CreerProduitRequest request, Utilisateur acteur)`
  - `private CategorieProduit parserCategorie(String valeur)`
  - `private CategorieMetier parserCategorieMetier(String valeur)`
  - `private NiveauConditionnement parserNiveau(String valeur)`

#### `modules\stock\application\catalogue\usecase\lister\ConsulterHierarchieCatalogueUseCase.java`

**class** `ConsulterHierarchieCatalogueUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final ListerProduitsUseCase listerProduitsUseCase`
  - `Map<String, Long> marqueIds = new LinkedHashMap<>()`
  - `Map<String, Long> gammeIds = new LinkedHashMap<>()`
  - `Marque> marques = new ArrayList<>()`
  - `Gamme> gammes = new ArrayList<>()`
  - `Variete> varietes = new ArrayList<>()`
  - `long seqMarque = 0, seqGamme = 0, seqVariete = 0`
  - `long id = ++seqMarque`
  - `String cleGamme = marque + "" + gamme`
  - `long id = ++seqGamme`
  - `long id = ++seqVariete`
  - `Variete> varietesUniques = new ArrayList<>()`
  - `Map<String, Boolean> vues = new LinkedHashMap<>()`

- **Méthodes :**
  - `public ConsulterHierarchieCatalogueUseCase(ListerProduitsUseCase listerProduitsUseCase)`
  - `public CatalogueHierarchieResponse executer()`
  - `private String valeur(String s)`
  - `private String code(String nom)`

#### `modules\stock\application\catalogue\usecase\lister\ListerProduitsUseCase.java`

**class** `ListerProduitsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final ProduitRepository produitRepository`
  - `private final ProduitMapper produitMapper`
  - `boolean sansFiltre = marque == null && gamme == null && variete == null && actif == null`

- **Méthodes :**
  - `public ListerProduitsUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper)`
  - `public List<ProduitResponse> executer(String marque, String gamme, String variete, Boolean actif)`

#### `modules\stock\application\catalogue\usecase\modifier\AjouterConditionnementUseCase.java`

**class** `AjouterConditionnementUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ProduitRepository produitRepository`
  - `private final ProduitMapper produitMapper`
  - `private final EventPublisher eventPublisher`
  - `NiveauConditionnement niveau`

- **Méthodes :**
  - `public AjouterConditionnementUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper,
                                         EventPublisher eventPublisher)`
  - `public ProduitResponse executer(String codeProduit, AjouterConditionnementRequest request, Utilisateur acteur)`

#### `modules\stock\application\catalogue\usecase\modifier\ModifierProduitUseCase.java`

**class** `ModifierProduitUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ProduitRepository produitRepository`
  - `private final ProduitMapper produitMapper`

- **Méthodes :**
  - `public ModifierProduitUseCase(ProduitRepository produitRepository, ProduitMapper produitMapper)`
  - `public ProduitResponse executer(String codeProduit, ModifierProduitRequest request, Utilisateur acteur)`

#### `modules\stock\application\catalogue\usecase\supprimer\DesactiverProduitUseCase.java`

**class** `DesactiverProduitUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ProduitRepository produitRepository`

- **Méthodes :**
  - `public DesactiverProduitUseCase(ProduitRepository produitRepository)`
  - `public void executer(String codeProduit, Utilisateur acteur)`

#### `modules\stock\application\commande\dto\request\CreerBonCommandeRequest.java`

_Fichier vide ou non analysable._

#### `modules\stock\application\commande\dto\response\BonCommandeResponse.java`

**record** `BonCommandeResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numero`
  - `String codeFournisseur`
  - `String statut`
  - `String statutLibelle`
  - `LocalDate dateCommande`
  - `LocalDate dateLivraisonPrevue`
  - `boolean enRetard`
  - `BigDecimal montantTotalHT`
  - `String matriculeCreateur`
  - `List<LigneResponse> lignes`
  - `LocalDateTime dateCreation`

- **Méthodes :**
  - `public record LigneResponse(String id,
        String codeProduit,
        String designation,
        String sku,
        long quantiteCommandee,
        long quantiteRecue,
        BigDecimal prixUnitaireHT)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String id`
  - `String codeProduit`
  - `String designation`
  - `String sku`
  - `long quantiteCommandee`
  - `long quantiteRecue`
  - `BigDecimal prixUnitaireHT`

#### `modules\stock\application\commande\mapper\BonCommandeMapper.java`

**class** `BonCommandeMapper` (@Component
public)

- **Méthodes :**
  - `public BonCommandeResponse versResponse(BonCommande bc)`

#### `modules\stock\application\commande\usecase\consulter\ConsulterBonCommandeUseCase.java`

**class** `ConsulterBonCommandeUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final BonCommandeMapper bonCommandeMapper`

- **Méthodes :**
  - `public ConsulterBonCommandeUseCase(BonCommandeRepository bonCommandeRepository, BonCommandeMapper bonCommandeMapper)`
  - `public BonCommandeResponse executer(String numero)`

#### `modules\stock\application\commande\usecase\creer\CreerBonCommandeUseCase.java`

**class** `CreerBonCommandeUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final FournisseurRepository fournisseurRepository`
  - `private final BonCommandeMapper bonCommandeMapper`

- **Méthodes :**
  - `public CreerBonCommandeUseCase(BonCommandeRepository bonCommandeRepository,
                                   FournisseurRepository fournisseurRepository,
                                   BonCommandeMapper bonCommandeMapper)`
  - `public BonCommandeResponse executer(CreerBonCommandeRequest request, Utilisateur acteur)`
  - `private String genererNumero(LocalDate dateCommande)`

#### `modules\stock\application\commande\usecase\cycle\TransitionBonCommandeUseCase.java`

**class** `TransitionBonCommandeUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final BonCommandeMapper bonCommandeMapper`

- **Méthodes :**
  - `public TransitionBonCommandeUseCase(BonCommandeRepository bonCommandeRepository, BonCommandeMapper bonCommandeMapper)`
  - `public BonCommandeResponse executer(String numero, Action action, Utilisateur acteur)`

**enum** `Action` (public)

#### `modules\stock\application\commande\usecase\lister\ListerBonsCommandeUseCase.java`

**class** `ListerBonsCommandeUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final BonCommandeMapper bonCommandeMapper`
  - `BonCommande> bcs`

- **Méthodes :**
  - `public ListerBonsCommandeUseCase(BonCommandeRepository bonCommandeRepository, BonCommandeMapper bonCommandeMapper)`
  - `public List<BonCommandeResponse> executer(String statut, String fournisseur, boolean enRetard)`

#### `modules\stock\application\commande\usecase\valider\ValiderBonCommandeUseCase.java`

**class** `ValiderBonCommandeUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final BonCommandeMapper bonCommandeMapper`
  - `private final EventPublisher eventPublisher`
  - `private final GenererPPHDepuisBCUseCase genererPPHDepuisBCUseCase`

- **Méthodes :**
  - `public ValiderBonCommandeUseCase(BonCommandeRepository bonCommandeRepository,
                                     BonCommandeMapper bonCommandeMapper, EventPublisher eventPublisher,
                                     GenererPPHDepuisBCUseCase genererPPHDepuisBCUseCase)`
  - `public BonCommandeResponse executer(String numero, Utilisateur acteur)`

#### `modules\stock\application\dashboard\dto\response\TableauBordEnrichiResponse.java`

**record** `TableauBordEnrichiResponse` (public)

- **Composants (record) :**
  - `long totalLignesStock`
  - `BigDecimal valeurTotaleStock`
  - `long alertesCritiques`
  - `long alertesFaibles`
  - `long alertesSecurite`
  - `long receptionsEnAttente`
  - `long receptionsDuJour`
  - `long mouvementsDuJour`
  - `long lotsEnAttente`
  - `long commandesInternesActives`
  - `BigDecimal ventesDuJour`
  - `long stockCentralTotal`
  - `long stockTamponTotal`
  - `long stockMobileTotal`
  - `long sessionsOuvertes`
  - `long sessionsEnPreparation`
  - `long commerciauxActifs`
  - `List<StatutTamponResponse> niveauxTampon`
  - `List<PerformanceCommercialResponse> performancesCommerciaux`
  - `List<InvenduResponse> alertesInvendus`
  - `List<ActionRapideResponse> actionsRapides`

- **Méthodes :**
  - `public record StatutTamponResponse(String codeProduit, String designation,
        long quantiteCentrale, long quantiteTampon,
        long seuilReappro, long seuilSecurite,
        String statut)`
  - `public record PerformanceCommercialResponse(String matriculeCommercial,
        long totalVendu, long totalRestant, long totalAvarie,
        double tauxVente, String statutSession)`
  - `public record InvenduResponse(String matriculeCommercial, String codeProduit, String designation,
        long quantiteRestante, LocalDate dateSession)`
  - `public record ActionRapideResponse(String label, String endpoint, String couleur)`

**record** `StatutTamponResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `long quantiteCentrale`
  - `long quantiteTampon`
  - `long seuilReappro`
  - `long seuilSecurite`
  - `String statut`

**record** `PerformanceCommercialResponse` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `long totalVendu`
  - `long totalRestant`
  - `long totalAvarie`
  - `double tauxVente`
  - `String statutSession`

**record** `InvenduResponse` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `String codeProduit`
  - `String designation`
  - `long quantiteRestante`
  - `LocalDate dateSession`

**record** `ActionRapideResponse` (public)

- **Composants (record) :**
  - `String label`
  - `String endpoint`
  - `String couleur`

#### `modules\stock\application\dashboard\dto\response\TableauBordStockResponse.java`

**record** `TableauBordStockResponse` (public)

- **Composants (record) :**
  - `long totalLignesStock`
  - `BigDecimal valeurTotaleStock`
  - `long alertesCritiques`
  - `long alertesFaibles`
  - `long receptionsEnAttente`
  - `long receptionsDuJour`
  - `long mouvementsDuJour`
  - `long lotsEnAttente`
  - `long commandesInternesActives`
  - `BigDecimal ventesDuJour`

#### `modules\stock\application\dashboard\usecase\ConsulterTableauBordEnrichiUseCase.java`

**class** `ConsulterTableauBordEnrichiUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final ProduitRepository produitRepository`
  - `private final SeuilStockRepository seuilStockRepository`
  - `private final MouvementStockRepository mouvementStockRepository`
  - `private final ReceptionRepository receptionRepository`
  - `private final LotRepository lotRepository`
  - `private final StockMobileRepository stockMobileRepository`
  - `private final SessionCommercialRepository sessionRepository`
  - `BigDecimal valeurTotale = BigDecimal.ZERO`
  - `long alertesCritiques = 0, alertesFaibles = 0, alertesSecurite = 0`
  - `long stockCentralTotal = 0, stockTamponTotal = 0, stockMobileTotal = 0`
  - `Map<String, Long> centralParProduit = new HashMap<>()`
  - `Map<String, Long> tamponParProduit = new HashMap<>()`
  - `Set<String> commerciauxActifs = new HashSet<>()`
  - `Set<String> produits = new HashSet<>()`
  - `String statut = sSecurite > 0 && qTampon <= sSecurite ? ""
                : qTampon <= sReappro ? ""
                : qTampon <= sReappro + (sReappro / 2) ? ""
                : ""`
  - `Map<String, String> sessionStatut = new HashMap<>()`

- **Méthodes :**
  - `public ConsulterTableauBordEnrichiUseCase(NiveauStockRepository niveauStockRepository,
                                                ProduitRepository produitRepository,
                                                SeuilStockRepository seuilStockRepository,
                                                MouvementStockRepository mouvementStockRepository,
                                                ReceptionRepository receptionRepository,
                                                LotRepository lotRepository,
                                                StockMobileRepository stockMobileRepository,
                                                SessionCommercialRepository sessionRepository)`
  - `public TableauBordEnrichiResponse executer()`
  - `StatutTamponResponse> buildNiveauxTampon(Map<String, Long> central, Map<String, Long> tampon)`
  - `PerformanceCommercialResponse> buildPerformances(List<StockMobile> stocks, List<SessionCommercial> sessions)`
  - `InvenduResponse> buildAlertesInvendus(List<StockMobile> stocks)`
  - `private BigDecimal valoriser(NiveauStock niveau, Produit produit)`

#### `modules\stock\application\dashboard\usecase\ConsulterTableauBordStockUseCase.java`

**class** `ConsulterTableauBordStockUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final ProduitRepository produitRepository`
  - `private final SeuilStockRepository seuilStockRepository`
  - `private final MouvementStockRepository mouvementStockRepository`
  - `private final ReceptionRepository receptionRepository`
  - `private final LotRepository lotRepository`
  - `BigDecimal valeurTotale = BigDecimal.ZERO`
  - `long alertesCritiques = 0`
  - `long alertesFaibles = 0`

- **Méthodes :**
  - `public ConsulterTableauBordStockUseCase(NiveauStockRepository niveauStockRepository,
                                            ProduitRepository produitRepository,
                                            SeuilStockRepository seuilStockRepository,
                                            MouvementStockRepository mouvementStockRepository,
                                            ReceptionRepository receptionRepository,
                                            LotRepository lotRepository)`
  - `public TableauBordStockResponse executer()`
  - `private BigDecimal valoriser(NiveauStock niveau, Produit produit)`

#### `modules\stock\application\dotation\dto\request\DecisionLigneRequest.java`

**record** `DecisionLigneRequest` (public)

- **Composants (record) :**
  - `@NotBlank String idLigne`
  - `Long nouvelleQuantite`
  - `String nouveauSku`
  - `String motif`

#### `modules\stock\application\dotation\dto\request\SoumettreDotationRequest.java`

**record** `LigneDotationRequest` (public)

- **Composants (record) :**
  - `@NotBlank String codeProduit`
  - `String sku`
  - `@NotNull Long quantiteDemandee`

#### `modules\stock\application\dotation\dto\response\DotationResponse.java`

**record** `DotationResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numero`
  - `String matriculeCommercial`
  - `LocalDate dateDotation`
  - `LocalDate dateEffet`
  - `String statut`
  - `String statutLibelle`
  - `String matriculeGestionnaire`
  - `String motifRefus`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String id,
        String codeProduit,
        String sku,
        long quantiteDemandee,
        long quantiteApprouvee,
        String motifModification)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String id`
  - `String codeProduit`
  - `String sku`
  - `long quantiteDemandee`
  - `long quantiteApprouvee`
  - `String motifModification`

#### `modules\stock\application\dotation\mapper\DotationMapper.java`

**class** `DotationMapper` (@Component
public)

- **Méthodes :**
  - `public DotationResponse versResponse(Dotation d)`

#### `modules\stock\application\dotation\service\VerificationChargementService.java`

**class** `VerificationChargementService` (@Service
public)

- **Champs :**
  - `private final PreCommandeRepository preCommandeRepository`
  - `private final VersementRepository versementRepository`

- **Méthodes :**
  - `public VerificationChargementService(PreCommandeRepository preCommandeRepository,
                                          VersementRepository versementRepository)`
  - `public void verifierDoubleSignature(String matriculeCommercial, LocalDate dateDotation, LocalDate dateEffet)`
  - `private boolean estValideeParComptable(PreCommande pc)`

#### `modules\stock\application\dotation\service\VerificationSurstockService.java`

**class** `VerificationSurstockService` (@Service
public)

- **Champs :**
  - `private final StockMobileRepository stockMobileRepository`
  - `long totalApresDotation = disponible + quantiteDemandee`

- **Méthodes :**
  - `public VerificationSurstockService(StockMobileRepository stockMobileRepository)`
  - `public void verifierPasSurstock(String matriculeCommercial, String codeProduit,
                                     long quantiteDemandee, long seuilSurstock)`

#### `modules\stock\application\dotation\usecase\approuver\ApprouverDotationUseCase.java`

**class** `ApprouverDotationUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final DotationRepository dotationRepository`
  - `private final EntrepotRepository entrepotRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final MouvementStockRepository mouvementRepository`
  - `private final ProduitRepository produitRepository`
  - `private final DotationMapper dotationMapper`
  - `private final EventPublisher eventPublisher`
  - `private final VerificationChargementService verificationChargementService`

- **Méthodes :**
  - `public ApprouverDotationUseCase(DotationRepository dotationRepository, EntrepotRepository entrepotRepository,
                                    NiveauStockRepository niveauStockRepository,
                                    MouvementStockRepository mouvementRepository, ProduitRepository produitRepository,
                                    DotationMapper dotationMapper, EventPublisher eventPublisher,
                                    VerificationChargementService verificationChargementService)`
  - `public DotationResponse executer(String numero, Utilisateur acteur)`
  - `private String resoudreEntrepotMobile(String matriculeCommercial)`
  - `private long convertirEnBase(String codeProduit, String sku, long quantite)`
  - `private String genererReferenceMouvement()`

#### `modules\stock\application\dotation\usecase\consulter\ConsulterDotationUseCase.java`

**class** `ConsulterDotationUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final DotationRepository dotationRepository`
  - `private final DotationMapper dotationMapper`

- **Méthodes :**
  - `public ConsulterDotationUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper)`
  - `public DotationResponse executer(String numero)`

#### `modules\stock\application\dotation\usecase\lister\ListerDotationsUseCase.java`

**class** `ListerDotationsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final DotationRepository dotationRepository`
  - `private final DotationMapper dotationMapper`

- **Méthodes :**
  - `public ListerDotationsUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper)`
  - `public List<DotationResponse> executer(String statut, String commercial)`

#### `modules\stock\application\dotation\usecase\modifier\ModifierDotationUseCase.java`

**class** `ModifierDotationUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final DotationRepository dotationRepository`
  - `private final DotationMapper dotationMapper`

- **Méthodes :**
  - `public ModifierDotationUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper)`
  - `public DotationResponse executer(String numero, DecisionLigneRequest request, Utilisateur acteur)`

#### `modules\stock\application\dotation\usecase\refuser\RefuserDotationUseCase.java`

**class** `RefuserDotationUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final DotationRepository dotationRepository`
  - `private final DotationMapper dotationMapper`

- **Méthodes :**
  - `public RefuserDotationUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper)`
  - `public DotationResponse executer(String numero, String motif, Utilisateur acteur)`

#### `modules\stock\application\dotation\usecase\soumettre\SoumettreDotationUseCase.java`

**class** `SoumettreDotationUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final DotationRepository dotationRepository`
  - `private final DotationMapper dotationMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public SoumettreDotationUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper,
                                    EventPublisher eventPublisher)`
  - `public DotationResponse executer(SoumettreDotationRequest request, Utilisateur acteur)`
  - `private String genererNumero()`

#### `modules\stock\application\entrepot\dto\request\CreerEntrepotRequest.java`

_Fichier vide ou non analysable._

#### `modules\stock\application\entrepot\dto\response\EntrepotResponse.java`

**record** `EntrepotResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String code`
  - `String libelle`
  - `String type`
  - `String typeLibelle`
  - `String matriculeProprietaire`
  - `boolean actif`

#### `modules\stock\application\entrepot\mapper\EntrepotMapper.java`

**class** `EntrepotMapper` (@Component
public)

- **Méthodes :**
  - `public EntrepotResponse versResponse(Entrepot e)`

#### `modules\stock\application\entrepot\usecase\creer\CreerEntrepotUseCase.java`

**class** `CreerEntrepotUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final EntrepotRepository entrepotRepository`
  - `private final EntrepotMapper entrepotMapper`

- **Méthodes :**
  - `public CreerEntrepotUseCase(EntrepotRepository entrepotRepository, EntrepotMapper entrepotMapper)`
  - `public EntrepotResponse executer(CreerEntrepotRequest request, Utilisateur acteur)`
  - `private TypeEntrepot parserType(String valeur)`
  - `private String exigerCode(CreerEntrepotRequest request)`
  - `private String libelleOuDefaut(CreerEntrepotRequest request, String defaut)`

#### `modules\stock\application\entrepot\usecase\lister\ListerEntrepotsUseCase.java`

**class** `ListerEntrepotsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final EntrepotRepository entrepotRepository`
  - `private final EntrepotMapper entrepotMapper`

- **Méthodes :**
  - `public ListerEntrepotsUseCase(EntrepotRepository entrepotRepository, EntrepotMapper entrepotMapper)`
  - `public List<EntrepotResponse> executer(String type)`

#### `modules\stock\application\fournisseur\dto\request\CreerFournisseurRequest.java`

_Fichier vide ou non analysable._

#### `modules\stock\application\fournisseur\dto\response\FournisseurResponse.java`

**record** `FournisseurResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String code`
  - `String raisonSociale`
  - `String telephone`
  - `String email`
  - `String adresse`
  - `boolean actif`

#### `modules\stock\application\fournisseur\mapper\FournisseurMapper.java`

**class** `FournisseurMapper` (@Component
public)

- **Méthodes :**
  - `public FournisseurResponse versResponse(Fournisseur f)`

#### `modules\stock\application\fournisseur\usecase\creer\CreerFournisseurUseCase.java`

**class** `CreerFournisseurUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final FournisseurRepository fournisseurRepository`
  - `private final FournisseurMapper fournisseurMapper`

- **Méthodes :**
  - `public CreerFournisseurUseCase(FournisseurRepository fournisseurRepository, FournisseurMapper fournisseurMapper)`
  - `public FournisseurResponse executer(CreerFournisseurRequest request, Utilisateur acteur)`

#### `modules\stock\application\fournisseur\usecase\lister\ListerFournisseursUseCase.java`

**class** `ListerFournisseursUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final FournisseurRepository fournisseurRepository`
  - `private final FournisseurMapper fournisseurMapper`

- **Méthodes :**
  - `public ListerFournisseursUseCase(FournisseurRepository fournisseurRepository, FournisseurMapper fournisseurMapper)`
  - `public List<FournisseurResponse> executer(String recherche)`

#### `modules\stock\application\inventaire\dto\request\OuvrirInventaireRequest.java`

_Fichier vide ou non analysable._

#### `modules\stock\application\inventaire\dto\request\SaisirComptageRequest.java`

**record** `SaisirComptageRequest` (public)

- **Composants (record) :**
  - `@NotBlank String idLigne`
  - `@NotNull Long quantiteComptee`

#### `modules\stock\application\inventaire\dto\response\InventaireResponse.java`

**record** `InventaireResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numero`
  - `String type`
  - `String typeLibelle`
  - `String codeEntrepot`
  - `String perimetre`
  - `String statut`
  - `String statutLibelle`
  - `boolean ecartSignificatif`
  - `String matriculeInitiateur`
  - `String matriculeValidateurDG`
  - `String justificationEcarts`
  - `LocalDate dateInventaire`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String id,
        String codeProduit,
        String emplacement,
        long quantiteTheorique,
        Long quantiteComptee,
        long ecart)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String id`
  - `String codeProduit`
  - `String emplacement`
  - `long quantiteTheorique`
  - `Long quantiteComptee`
  - `long ecart`

#### `modules\stock\application\inventaire\mapper\InventaireMapper.java`

**class** `InventaireMapper` (@Component
public)

- **Méthodes :**
  - `public InventaireResponse versResponse(SessionInventaire s)`

#### `modules\stock\application\inventaire\usecase\cloturer\CloturerComptageUseCase.java`

**class** `CloturerComptageUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final InventaireRepository inventaireRepository`
  - `private final InventaireMapper inventaireMapper`

- **Méthodes :**
  - `public CloturerComptageUseCase(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper)`
  - `public InventaireResponse executer(String numero)`

#### `modules\stock\application\inventaire\usecase\consulter\ConsulterInventaireUseCase.java`

**class** `ConsulterInventaireUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final InventaireRepository inventaireRepository`
  - `private final InventaireMapper inventaireMapper`

- **Méthodes :**
  - `public ConsulterInventaireUseCase(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper)`
  - `public InventaireResponse executer(String numero)`

#### `modules\stock\application\inventaire\usecase\lister\ListerInventairesUseCase.java`

**class** `ListerInventairesUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final InventaireRepository inventaireRepository`
  - `private final InventaireMapper inventaireMapper`

- **Méthodes :**
  - `public ListerInventairesUseCase(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper)`
  - `public List<InventaireResponse> executer(String statut)`

#### `modules\stock\application\inventaire\usecase\ouvrir\OuvrirInventaireUseCase.java`

**class** `OuvrirInventaireUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final InventaireRepository inventaireRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final ProduitRepository produitRepository`
  - `private final InventaireMapper inventaireMapper`

- **Méthodes :**
  - `public OuvrirInventaireUseCase(InventaireRepository inventaireRepository,
                                   NiveauStockRepository niveauStockRepository,
                                   ProduitRepository produitRepository, InventaireMapper inventaireMapper)`
  - `public InventaireResponse executer(OuvrirInventaireRequest request, Utilisateur acteur)`
  - `private boolean correspondAuPerimetre(String codeProduit, OuvrirInventaireRequest request)`
  - `private String perimetre(OuvrirInventaireRequest request)`
  - `private TypeInventaire parserType(String valeur)`
  - `private String genererNumero()`

#### `modules\stock\application\inventaire\usecase\saisir\SaisirComptageUseCase.java`

**class** `SaisirComptageUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final InventaireRepository inventaireRepository`
  - `private final InventaireMapper inventaireMapper`

- **Méthodes :**
  - `public SaisirComptageUseCase(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper)`
  - `public InventaireResponse executer(String numero, SaisirComptageRequest request)`

#### `modules\stock\application\inventaire\usecase\valider\ValiderInventaireUseCase.java`

**class** `ValiderInventaireUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final InventaireRepository inventaireRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final MouvementStockRepository mouvementRepository`
  - `private final InventaireMapper inventaireMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public ValiderInventaireUseCase(InventaireRepository inventaireRepository,
                                    NiveauStockRepository niveauStockRepository,
                                    MouvementStockRepository mouvementRepository,
                                    InventaireMapper inventaireMapper, EventPublisher eventPublisher)`
  - `public InventaireResponse executer(String numero, String justification, Utilisateur acteur)`
  - `TypeEntrepot niveauTypeFallback(String entrepot)`
  - `private String genererReferenceMouvement()`

#### `modules\stock\application\mouvement\dto\response\MouvementResponse.java`

**record** `MouvementResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String reference`
  - `String type`
  - `String typeLibelle`
  - `String sens`
  - `String codeProduit`
  - `String sku`
  - `long quantiteEnBase`
  - `String codeEntrepotSource`
  - `String codeEntrepotCible`
  - `String matriculeActeur`
  - `String referenceDocument`
  - `String motif`
  - `LocalDateTime horodatage`

#### `modules\stock\application\mouvement\mapper\MouvementMapper.java`

**class** `MouvementMapper` (@Component
public)

- **Méthodes :**
  - `public MouvementResponse versResponse(MouvementStock m)`

#### `modules\stock\application\mouvement\usecase\lister\ListerMouvementsUseCase.java`

**class** `ListerMouvementsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final MouvementStockRepository mouvementRepository`
  - `private final MouvementMapper mouvementMapper`
  - `MouvementStock> mouvements`
  - `Page<MouvementStock> page`

- **Méthodes :**
  - `public ListerMouvementsUseCase(MouvementStockRepository mouvementRepository, MouvementMapper mouvementMapper)`
  - `public List<MouvementResponse> executer(String codeProduit, String codeEntrepot, String type,
                                            String referenceDocument, LocalDate debut, LocalDate fin)`
  - `public Page<MouvementResponse> executerPagine(String codeProduit, String codeEntrepot, String type,
                                                   String referenceDocument, LocalDate debut, LocalDate fin,
                                                   Pageable pageable)`

#### `modules\stock\application\niveau\dto\response\NiveauStockDetailResponse.java`

**record** `NiveauStockDetailResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `Long produitId`
  - `String codeProduit`
  - `String sku`
  - `String designation`
  - `String marque`
  - `String gamme`
  - `String categorie`
  - `Long entrepotId`
  - `String codeEntrepot`
  - `String libelleEntrepot`
  - `String typeEntrepot`
  - `long quantite`
  - `long seuilReapprovisionnement`
  - `long stockSecurite`
  - `String niveauAlerte`
  - `BigDecimal valeurStock`
  - `LocalDateTime dateModification`

#### `modules\stock\application\niveau\dto\response\NiveauStockResponse.java`

**record** `NiveauStockResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `String marque`
  - `String gamme`
  - `String codeEntrepot`
  - `String typeEntrepot`
  - `long quantiteEnBase`
  - `List<LigneDecomposition> decomposition`

- **Méthodes :**
  - `public record LigneDecomposition(String niveau, String libelle, long quantite)`

**record** `LigneDecomposition` (public)

- **Composants (record) :**
  - `String niveau`
  - `String libelle`
  - `long quantite`

#### `modules\stock\application\niveau\mapper\NiveauStockMapper.java`

**class** `NiveauStockMapper` (@Component
public)

- **Champs :**
  - `private final ProduitRepository produitRepository`
  - `LigneDecomposition> lignes = new ArrayList<>()`
  - `long reste = quantiteEnBase`
  - `long nombre = reste / facteur`

- **Méthodes :**
  - `public NiveauStockMapper(ProduitRepository produitRepository)`
  - `public NiveauStockResponse versResponse(NiveauStock niveau)`
  - `LigneDecomposition> decomposer(long quantiteEnBase, Produit produit)`

#### `modules\stock\application\niveau\usecase\ajuster\AjusterStockUseCase.java`

**class** `AjusterStockUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final MouvementStockRepository mouvementRepository`
  - `private final ConsulterNiveauxDetaillesUseCase detailUseCase`

- **Méthodes :**
  - `public AjusterStockUseCase(NiveauStockRepository niveauStockRepository,
                               MouvementStockRepository mouvementRepository,
                               ConsulterNiveauxDetaillesUseCase detailUseCase)`
  - `public NiveauStockDetailResponse executer(Long stockLevelId, long nouvelleQuantite, String motif, String matricule)`
  - `private String genererReferenceMouvement()`

#### `modules\stock\application\niveau\usecase\consulter\ConsulterEtatStockUseCase.java`

**class** `ConsulterEtatStockUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final NiveauStockMapper niveauStockMapper`
  - `List<NiveauStock> niveaux`

- **Méthodes :**
  - `public ConsulterEtatStockUseCase(NiveauStockRepository niveauStockRepository, NiveauStockMapper niveauStockMapper)`
  - `public List<NiveauStockResponse> executer(String codeEntrepot, String codeProduit, String typeEntrepot)`

#### `modules\stock\application\niveau\usecase\consulter\ConsulterNiveauxDetaillesUseCase.java`

**class** `ConsulterNiveauxDetaillesUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final ProduitRepository produitRepository`
  - `private final SeuilStockRepository seuilStockRepository`
  - `private final EntrepotRepository entrepotRepository`
  - `long seuil = 0L`

- **Méthodes :**
  - `public ConsulterNiveauxDetaillesUseCase(NiveauStockRepository niveauStockRepository,
                                            ProduitRepository produitRepository,
                                            SeuilStockRepository seuilStockRepository,
                                            EntrepotRepository entrepotRepository)`
  - `public List<NiveauStockDetailResponse> lister(String codeEntrepot)`
  - `public List<NiveauStockDetailResponse> listerParEntrepotId(Long entrepotId)`
  - `public Optional<NiveauStockDetailResponse> consulterParId(Long id)`
  - `public List<NiveauStockDetailResponse> listerAlertes(boolean critiquesSeulement)`
  - `private List<NiveauStockDetailResponse> enrichir(List<NiveauStock> niveaux)`
  - `private NiveauStockDetailResponse versDetail(NiveauStock niveau, Map<String, Entrepot> entrepots)`
  - `private String calculerNiveauAlerte(long quantite, long seuil, TypeEntrepot type)`
  - `private BigDecimal prixUnitaireBase(Produit produit)`
  - `private Optional<String> skuBase(Produit produit)`

#### `modules\stock\application\niveau\usecase\transferer\TransfererVersTamponUseCase.java`

**class** `TransfererVersTamponUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final EntrepotRepository entrepotRepository`
  - `private final MouvementStockRepository mouvementRepository`
  - `private final ConsulterNiveauxDetaillesUseCase detailUseCase`

- **Méthodes :**
  - `public TransfererVersTamponUseCase(NiveauStockRepository niveauStockRepository,
                                       EntrepotRepository entrepotRepository,
                                       MouvementStockRepository mouvementRepository,
                                       ConsulterNiveauxDetaillesUseCase detailUseCase)`
  - `public NiveauStockDetailResponse executer(Long stockLevelId, long quantite, String matricule)`
  - `private String genererReferenceMouvement()`

#### `modules\stock\application\notification\dto\MettreAJourPreferencesRequest.java`

**record** `MettreAJourPreferencesRequest` (public)

- **Composants (record) :**
  - `boolean notificationInApp`
  - `boolean notificationEmail`
  - `boolean notificationSms`
  - `boolean notificationPush`
  - `String prioriteMinimale`
  - `LocalTime heureDebutReception`
  - `LocalTime heureFinReception`
  - `Set<String> typesFiltres`

#### `modules\stock\application\notification\dto\NotificationMessage.java`

**record** `NotificationMessage` (public)

- **Composants (record) :**
  - `String type`
  - `String titre`
  - `String message`
  - `String destinataireRole`
  - `String actionSuggeree`
  - `String referenceLiee`
  - `LocalDateTime horodatage`

- **Méthodes :**
  - `public static NotificationMessage de(String type, String titre, String message, String destinataireRole,
                                         String actionSuggeree, String referenceLiee)`

#### `modules\stock\application\notification\dto\PreferencesNotificationResponse.java`

**record** `PreferencesNotificationResponse` (public)

- **Composants (record) :**
  - `String matriculeUtilisateur`
  - `boolean notificationInApp`
  - `boolean notificationEmail`
  - `boolean notificationSms`
  - `boolean notificationPush`
  - `String prioriteMinimale`
  - `LocalTime heureDebutReception`
  - `LocalTime heureFinReception`
  - `Set<String> typesFiltres`

#### `modules\stock\application\rapport\usecase\GenererRapportEtatStockUseCase.java`

**class** `GenererRapportEtatStockUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final ConsulterEtatStockUseCase consulterEtatStockUseCase`
  - `private final TableExporter tableExporter`
  - `String titre = ""`

- **Méthodes :**
  - `public GenererRapportEtatStockUseCase(ConsulterEtatStockUseCase consulterEtatStockUseCase, TableExporter tableExporter)`
  - `public RapportFichier executer(String entrepot, String produit, String type, FormatRapport format)`
  - `private List<List<String>> lignes(List<NiveauStockResponse> etat)`

#### `modules\stock\application\rapport\usecase\GenererRapportMouvementsUseCase.java`

**class** `GenererRapportMouvementsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final ListerMouvementsUseCase listerMouvementsUseCase`
  - `private final TableExporter tableExporter`
  - `String titre = ""`

- **Méthodes :**
  - `public GenererRapportMouvementsUseCase(ListerMouvementsUseCase listerMouvementsUseCase, TableExporter tableExporter)`
  - `public RapportFichier executer(String produit, String entrepot, String type, LocalDate debut, LocalDate fin,
                                   FormatRapport format)`
  - `private List<List<String>> lignes(List<MouvementResponse> mouvements)`

#### `modules\stock\application\reception\dto\request\CreerReceptionFournisseurRequest.java`

**record** `LigneReceptionRequest` (public)

- **Composants (record) :**
  - `@NotBlank String codeProduit`
  - `String sku`
  - `@NotNull Long quantiteAttendue`
  - `@NotNull Long quantiteRecue`
  - `String idLigneBC`

#### `modules\stock\application\reception\dto\request\CreerReceptionProductionRequest.java`

**record** `LigneReceptionRequest` (public)

- **Composants (record) :**
  - `@NotBlank String codeProduit`
  - `String sku`
  - `@NotNull Long quantiteAttendue`
  - `@NotNull Long quantiteRecue`

#### `modules\stock\application\reception\dto\response\ReceptionResponse.java`

**record** `ReceptionResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numero`
  - `String source`
  - `String categorie`
  - `String codeFournisseur`
  - `String numeroBonCommande`
  - `String numeroLotProduction`
  - `String codeEntrepotCible`
  - `String statut`
  - `String statutLibelle`
  - `boolean ecartSignificatif`
  - `String matriculeValidateur1`
  - `String matriculeValidateur2`
  - `String motifRejet`
  - `LocalDate dateReception`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String id,
        String codeProduit,
        String sku,
        long quantiteAttendue,
        long quantiteRecue,
        long ecart,
        double pourcentageEcart)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String id`
  - `String codeProduit`
  - `String sku`
  - `long quantiteAttendue`
  - `long quantiteRecue`
  - `long ecart`
  - `double pourcentageEcart`

#### `modules\stock\application\reception\mapper\ReceptionMapper.java`

**class** `ReceptionMapper` (@Component
public)

- **Méthodes :**
  - `public ReceptionResponse versResponse(Reception r)`

#### `modules\stock\application\reception\usecase\consulter\ConsulterReceptionUseCase.java`

**class** `ConsulterReceptionUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final ReceptionRepository receptionRepository`
  - `private final ReceptionMapper receptionMapper`

- **Méthodes :**
  - `public ConsulterReceptionUseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper)`
  - `public ReceptionResponse executer(String numero)`

#### `modules\stock\application\reception\usecase\creer\CreerReceptionFournisseurUseCase.java`

**class** `CreerReceptionFournisseurUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `@Value("") private double seuilEcart`
  - `private final ReceptionRepository receptionRepository`
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final EntrepotRepository entrepotRepository`
  - `private final ReceptionMapper receptionMapper`
  - `CategorieProduit categorie`

- **Méthodes :**
  - `public CreerReceptionFournisseurUseCase(ReceptionRepository receptionRepository,
                                            BonCommandeRepository bonCommandeRepository,
                                            EntrepotRepository entrepotRepository,
                                            ReceptionMapper receptionMapper)`
  - `public ReceptionResponse executer(CreerReceptionFournisseurRequest request, Utilisateur acteur)`
  - `private CategorieProduit parserCategorie(String valeur)`
  - `private String resoudreEntrepotCentral(String codeFourni)`
  - `private String genererNumero()`

#### `modules\stock\application\reception\usecase\creer\CreerReceptionProductionUseCase.java`

**class** `CreerReceptionProductionUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `@Value("") private double seuilEcart`
  - `private final ReceptionRepository receptionRepository`
  - `private final EntrepotRepository entrepotRepository`
  - `private final ReceptionMapper receptionMapper`

- **Méthodes :**
  - `public CreerReceptionProductionUseCase(ReceptionRepository receptionRepository,
                                           EntrepotRepository entrepotRepository,
                                           ReceptionMapper receptionMapper)`
  - `public ReceptionResponse executer(CreerReceptionProductionRequest request, Utilisateur acteur)`
  - `private String resoudreEntrepotCentral(String codeFourni)`
  - `private String genererNumero()`

#### `modules\stock\application\reception\usecase\lister\ListerReceptionsUseCase.java`

**class** `ListerReceptionsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final ReceptionRepository receptionRepository`
  - `private final ReceptionMapper receptionMapper`
  - `Reception> receptions`

- **Méthodes :**
  - `public ListerReceptionsUseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper)`
  - `public List<ReceptionResponse> executer(String statut, String numeroBonCommande)`

#### `modules\stock\application\reception\usecase\rejeter\RejeterReceptionUseCase.java`

**class** `RejeterReceptionUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ReceptionRepository receptionRepository`
  - `private final ReceptionMapper receptionMapper`

- **Méthodes :**
  - `public RejeterReceptionUseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper)`
  - `public ReceptionResponse executer(String numero, String motif, Utilisateur acteur)`

#### `modules\stock\application\reception\usecase\valider\ValiderReceptionEtape1UseCase.java`

**class** `ValiderReceptionEtape1UseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ReceptionRepository receptionRepository`
  - `private final ReceptionMapper receptionMapper`

- **Méthodes :**
  - `public ValiderReceptionEtape1UseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper)`
  - `public ReceptionResponse executer(String numero, Utilisateur acteur)`

#### `modules\stock\application\reception\usecase\valider\ValiderReceptionEtape2UseCase.java`

**class** `ValiderReceptionEtape2UseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final ReceptionRepository receptionRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final MouvementStockRepository mouvementRepository`
  - `private final ProduitRepository produitRepository`
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final ReceptionMapper receptionMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public ValiderReceptionEtape2UseCase(ReceptionRepository receptionRepository,
                                         NiveauStockRepository niveauStockRepository,
                                         MouvementStockRepository mouvementRepository,
                                         ProduitRepository produitRepository,
                                         BonCommandeRepository bonCommandeRepository,
                                         ReceptionMapper receptionMapper,
                                         EventPublisher eventPublisher)`
  - `public ReceptionResponse executer(String numero, Utilisateur acteur)`
  - `private BonCommande chargerBonCommandeSiFournisseur(Reception reception)`
  - `private long convertirEnBase(String codeProduit, String sku, long quantite)`
  - `private String genererReferenceMouvement()`

#### `modules\stock\application\reporting\service\ReportingService.java`

**class** `ReportingService` (@Service
public)

- **Champs :**
  - `private final StockMobileRepository stockMobileRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final MouvementStockRepository mouvementStockRepository`
  - `private final DotationRepository dotationRepository`
  - `private final ProduitRepository produitRepository`
  - `private final InventaireRepository inventaireRepository`
  - `String[] cols = {"", "", "", "", "",
                "", "", "", "", "", ""}`
  - `int i = 0`
  - `int rowIdx = 1`
  - `int i = 0`
  - `String[] headers = {"", "", "", "", "", "", "", "", "", ""}`
  - `String[] cols = {"", "", "", "", "", ""}`
  - `int i = 0`
  - `int rowIdx = 1`
  - `int i = 0`
  - `String[] cols = {"", "", "", "", "", "", "", "", "", "", ""}`
  - `int i = 0`
  - `int rowIdx = 1`
  - `int i = 0`
  - `String[] cols = {"", "", "", "", "", ""}`
  - `int i = 0`
  - `int rowIdx = 1`
  - `BigDecimal totalGlobal = BigDecimal.ZERO`
  - `int i = 0`
  - `String[] cols = {"", "", "", "", "", "", "", ""}`
  - `int i = 0`
  - `int rowIdx = 1`
  - `double tauxVente = totalDotation > 0 ? (totalVendu * 100.0 / totalDotation) : 0`
  - `double tauxRetour = totalDotation > 0 ? (totalRestant * 100.0 / totalDotation) : 0`
  - `int i = 0`
  - `String[] cols = {"", "", "", "", "", "", "", "", "", ""}`
  - `int i = 0`
  - `int rowIdx = 1`
  - `int i = 0`
  - `Map<String, Long> sortiesParProduit = new HashMap<>()`
  - `String[] cols = {"", "", "", "", "", "", ""}`
  - `int i = 0`
  - `int rowIdx = 1`
  - `String statut = tauxRotation == 0 ? ""
                    : tauxRotation < 25 ? ""
                    : tauxRotation < 75 ? ""
                    : ""`
  - `int i = 0`
  - `String[] cols = {"", "", "", ""}`
  - `int i = 0`
  - `int rowIdx = 1`
  - `int i = 0`
  - `String[] headers = {"", "", "", ""}`
  - `String[] headers = {"", "", "", "", "", ""}`

- **Méthodes :**
  - `public ReportingService(StockMobileRepository stockMobileRepository,
                            NiveauStockRepository niveauStockRepository,
                            MouvementStockRepository mouvementStockRepository,
                            DotationRepository dotationRepository,
                            ProduitRepository produitRepository,
                            InventaireRepository inventaireRepository)`
  - `public byte[] rapportStocksMobilesExcel()` throws `Exception`
  - `public byte[] rapportStocksMobilesPdf()` throws `Exception`
  - `public byte[] rapportPerformancesExcel()` throws `Exception`
  - `public byte[] rapportStockCentralExcel()` throws `Exception`
  - `public byte[] rapportStockCentralPdf()` throws `Exception`
  - `public byte[] rapportTamponExcel()` throws `Exception`
  - `public byte[] rapportTamponPdf()` throws `Exception`
  - `public byte[] rapportMouvementsExcel(LocalDate debut, LocalDate fin)` throws `Exception`
  - `public byte[] rapportValorisationExcel()` throws `Exception`
  - `public byte[] rapportDotationsVsVentesExcel()` throws `Exception`
  - `public byte[] rapportEcartsInventaireExcel()` throws `Exception`
  - `public byte[] rapportRotationStockExcel()` throws `Exception`
  - `public byte[] rapportStockCentralCsv()` throws `Exception`
  - `public byte[] rapportTamponCsv()` throws `Exception`
  - `public byte[] rapportMouvementsCsv(LocalDate debut, LocalDate fin)` throws `Exception`
  - `private byte[] genererCsvNiveauxStock(List<NiveauStock> niveaux)` throws `Exception`
  - `private String csv(String value)`
  - `private byte[] genererExcelNiveauxStock(List<NiveauStock> niveaux, String titre, IndexedColors color)` throws `Exception`
  - `private byte[] genererPdfNiveauxStock(List<NiveauStock> niveaux, String titre)` throws `Exception`
  - `public byte[] rapportPerformancesPdf()` throws `Exception`

#### `modules\stock\application\retour\dto\request\EnregistrerRetourRequest.java`

**record** `LigneRetourRequest` (public)

- **Composants (record) :**
  - `@NotBlank String codeProduit`
  - `String sku`
  - `@NotNull Long quantite`
  - `String motif`

#### `modules\stock\application\retour\dto\response\RetourResponse.java`

**record** `RetourResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numero`
  - `String matriculeCommercial`
  - `String matriculeGestionnaire`
  - `LocalDate dateRetour`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String id, String codeProduit, String sku, long quantite, String motif)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String id`
  - `String codeProduit`
  - `String sku`
  - `long quantite`
  - `String motif`

#### `modules\stock\application\retour\mapper\RetourMapper.java`

**class** `RetourMapper` (@Component
public)

- **Méthodes :**
  - `public RetourResponse versResponse(BonRetour b)`

#### `modules\stock\application\retour\usecase\enregistrer\EnregistrerRetourUseCase.java`

**class** `EnregistrerRetourUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final BonRetourRepository retourRepository`
  - `private final EntrepotRepository entrepotRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final MouvementStockRepository mouvementRepository`
  - `private final ProduitRepository produitRepository`
  - `private final RetourMapper retourMapper`

- **Méthodes :**
  - `public EnregistrerRetourUseCase(BonRetourRepository retourRepository, EntrepotRepository entrepotRepository,
                                    NiveauStockRepository niveauStockRepository,
                                    MouvementStockRepository mouvementRepository, ProduitRepository produitRepository,
                                    RetourMapper retourMapper)`
  - `public RetourResponse executer(EnregistrerRetourRequest request, Utilisateur acteur)`
  - `private long convertirEnBase(String codeProduit, String sku, long quantite)`
  - `private String genererNumero()`
  - `private String genererReferenceMouvement()`

#### `modules\stock\application\retour\usecase\lister\ListerRetoursUseCase.java`

**class** `ListerRetoursUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final BonRetourRepository retourRepository`
  - `private final RetourMapper retourMapper`

- **Méthodes :**
  - `public ListerRetoursUseCase(BonRetourRepository retourRepository, RetourMapper retourMapper)`
  - `public List<RetourResponse> executer(String commercial)`

#### `modules\stock\application\sessioncommercial\dto\response\SessionCommercialResponse.java`

**record** `SessionCommercialResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numeroSession`
  - `String matriculeCommercial`
  - `LocalDate datePreparation`
  - `LocalDate dateEffet`
  - `String statut`
  - `int nombreDotations`
  - `List<String> dotationsIds`
  - `String matriculeCloture`
  - `String matriculeValidateurSecretaire`
  - `String matriculeValidateurComptable`
  - `LocalDateTime heureOuverture`
  - `LocalDateTime heureCloture`
  - `LocalDateTime dateCreation`

#### `modules\stock\application\sessioncommercial\mapper\SessionCommercialMapper.java`

**class** `SessionCommercialMapper` (@Component
public)

- **Méthodes :**
  - `public SessionCommercialResponse versResponse(SessionCommercial s)`
  - `public List<SessionCommercialResponse> versResponseList(List<SessionCommercial> list)`

#### `modules\stock\application\seuil\dto\request\DefinirSeuilRequest.java`

_Fichier vide ou non analysable._

#### `modules\stock\application\seuil\dto\response\HistoriqueSeuilResponse.java`

**record** `HistoriqueSeuilResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String codeProduit`
  - `String typeEntrepot`
  - `long ancienneValeur`
  - `long nouvelleValeur`
  - `String matriculeAuteur`
  - `LocalDateTime dateModification`

#### `modules\stock\application\seuil\dto\response\SeuilResponse.java`

**record** `SeuilResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String codeProduit`
  - `String typeEntrepot`
  - `long seuilReapprovisionnement`
  - `boolean actif`

#### `modules\stock\application\seuil\usecase\DefinirSeuilUseCase.java`

**class** `DefinirSeuilUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final SeuilStockRepository seuilRepository`
  - `private final HistoriqueSeuilJpaRepository historiqueRepository`

- **Méthodes :**
  - `public DefinirSeuilUseCase(SeuilStockRepository seuilRepository,
                                HistoriqueSeuilJpaRepository historiqueRepository)`
  - `public SeuilResponse executer(DefinirSeuilRequest request, Utilisateur acteur)`

#### `modules\stock\application\seuil\usecase\EvaluerSeuilsUseCase.java`

**class** `EvaluerSeuilsUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final SeuilStockRepository seuilRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final AlerteRepository alerteRepository`
  - `private final AlerteMapper alerteMapper`
  - `private final EventPublisher eventPublisher`
  - `List<Alerte> creees = new ArrayList<>()`

- **Méthodes :**
  - `public EvaluerSeuilsUseCase(SeuilStockRepository seuilRepository, NiveauStockRepository niveauStockRepository,
                                AlerteRepository alerteRepository, AlerteMapper alerteMapper,
                                EventPublisher eventPublisher)`
  - `public List<AlerteResponse> executer()`
  - `private String genererReference()`

#### `modules\stock\application\seuil\usecase\ListerSeuilsUseCase.java`

**class** `ListerSeuilsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final SeuilStockRepository seuilRepository`

- **Méthodes :**
  - `public ListerSeuilsUseCase(SeuilStockRepository seuilRepository)`
  - `public List<SeuilResponse> executer()`

#### `modules\stock\application\stockmobile\dto\response\StockMobileResponse.java`

**record** `StockMobileResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matriculeCommercial`
  - `LocalDate dateSession`
  - `long totalVentes`
  - `long totalRestant`
  - `long totalAvaries`
  - `LocalDateTime dateModification`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String codeProduit, String designation, String conditionnement,
        long quantiteDisponible, long quantiteInitiale, long quantiteVendue,
        long quantiteAvariee, long quantiteDonnee, LocalDateTime dateDernierMouvement)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `String conditionnement`
  - `long quantiteDisponible`
  - `long quantiteInitiale`
  - `long quantiteVendue`
  - `long quantiteAvariee`
  - `long quantiteDonnee`
  - `LocalDateTime dateDernierMouvement`

#### `modules\stock\application\stockmobile\mapper\StockMobileMapper.java`

**class** `StockMobileMapper` (@Component
public)

- **Méthodes :**
  - `public StockMobileResponse versResponse(StockMobile sm)`
  - `public List<StockMobileResponse> versResponseList(List<StockMobile> list)`

#### `modules\stock\application\transfert\dto\request\CreerTransfertRequest.java`

**record** `LigneTransfertRequest` (public)

- **Composants (record) :**
  - `@NotBlank String codeProduit`
  - `String sku`
  - `@NotNull Long quantite`

#### `modules\stock\application\transfert\dto\response\TransfertResponse.java`

**record** `TransfertResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String numero`
  - `String codeEntrepotSource`
  - `String codeEntrepotCible`
  - `String statut`
  - `String statutLibelle`
  - `boolean declencheParSeuil`
  - `String matriculeGestionnaire`
  - `LocalDateTime dateExecution`
  - `List<LigneResponse> lignes`

- **Méthodes :**
  - `public record LigneResponse(String id, String codeProduit, String sku, long quantite)`

**record** `LigneResponse` (public)

- **Composants (record) :**
  - `String id`
  - `String codeProduit`
  - `String sku`
  - `long quantite`

#### `modules\stock\application\transfert\mapper\TransfertMapper.java`

**class** `TransfertMapper` (@Component
public)

- **Méthodes :**
  - `public TransfertResponse versResponse(BonTransfert bt)`

#### `modules\stock\application\transfert\usecase\executer\ExecuterTransfertUseCase.java`

**class** `ExecuterTransfertUseCase` (@Service
@Transactional
public)

- **Champs :**
  - `private final BonTransfertRepository transfertRepository`
  - `private final EntrepotRepository entrepotRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final MouvementStockRepository mouvementRepository`
  - `private final ProduitRepository produitRepository`
  - `private final TransfertMapper transfertMapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public ExecuterTransfertUseCase(BonTransfertRepository transfertRepository,
                                    EntrepotRepository entrepotRepository,
                                    NiveauStockRepository niveauStockRepository,
                                    MouvementStockRepository mouvementRepository,
                                    ProduitRepository produitRepository,
                                    TransfertMapper transfertMapper,
                                    EventPublisher eventPublisher)`
  - `public TransfertResponse executer(CreerTransfertRequest request, Utilisateur acteur)`
  - `private String resoudre(String codeFourni, TypeEntrepot type)`
  - `private long convertirEnBase(String codeProduit, String sku, long quantite)`
  - `private String genererNumero()`
  - `private String genererReferenceMouvement()`

#### `modules\stock\application\transfert\usecase\lister\ListerTransfertsUseCase.java`

**class** `ListerTransfertsUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final BonTransfertRepository transfertRepository`
  - `private final TransfertMapper transfertMapper`

- **Méthodes :**
  - `public ListerTransfertsUseCase(BonTransfertRepository transfertRepository, TransfertMapper transfertMapper)`
  - `public List<TransfertResponse> executer(String statut)`

#### `modules\stock\domain\alerte\enums\PrioriteAlerte.java`

**enum** `PrioriteAlerte` (public)

- **Constantes :**
  - `{

    CRITIQUE("", 4)`
  - `HAUTE("", 3)`
  - `MOYENNE("", 2)`
  - `BASSE("", 1)`

- **Champs :**
  - `private final String libelle`
  - `private final int niveau`

- **Méthodes :**
  - `public String getLibelle()`
  - `public int getNiveau()`
  - `public boolean estCritique()`
  - `public boolean requireEmail()`
  - `public boolean requireSMS()`

#### `modules\stock\domain\alerte\enums\StatutAlerte.java`

**enum** `StatutAlerte` (public)

- **Constantes :**
  - `{
    NOUVELLE("")`
  - `LUE("")`
  - `TRAITEE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\stock\domain\alerte\enums\TypeAlerte.java`

**enum** `TypeAlerte` (public)

- **Constantes :**
  - `{

    TAMPON_SOUS_SEUIL("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.HAUTE)`
  - `CENTRAL_SOUS_SEUIL("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.HAUTE)`
  - `CENTRAL_SURSTOCK("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.MOYENNE)`
  - `PEREMPTION_IMMINENTE("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.CRITIQUE)`
  - `BC_RETARD_LIVRAISON("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.HAUTE)`
  - `ECART_RECEPTION("", Role.DIRECTEUR_GENERAL, "", PrioriteAlerte.MOYENNE)`
  - `STOCK_MOBILE_ANORMAL("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.MOYENNE)`
  - `STOCK_MOBILE_RUPTURE("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.CRITIQUE)`
  - `ROTATION_LENTE("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.BASSE)`
  - `ANOMALIE_VENTES("", Role.DIRECTEUR_COMMERCIAL, "", PrioriteAlerte.MOYENNE)`
  - `NOUVELLE_DOTATION("", Role.GESTIONNAIRE_STOCK, "", PrioriteAlerte.MOYENNE)`

- **Champs :**
  - `private final String libelle`
  - `private final Role destinataire`
  - `private final String actionSuggeree`
  - `private final PrioriteAlerte priorite`

- **Méthodes :**
  - `public String getLibelle()`
  - `public Role getDestinataire()`
  - `public String getActionSuggeree()`
  - `public PrioriteAlerte getPriorite()`

#### `modules\stock\domain\alerte\event\AlerteCreeeEvent.java`

**class** `AlerteCreeeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String reference`
  - `private final TypeAlerte type`
  - `private final String message`
  - `private final String destinataireRole`
  - `private final String priorite`

- **Méthodes :**
  - `public AlerteCreeeEvent(String reference, TypeAlerte type, String message, String destinataireRole)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReference()`
  - `public TypeAlerte getType()`
  - `public String getMessage()`
  - `public String getDestinataireRole()`
  - `public String getPriorite()`

#### `modules\stock\domain\alerte\model\Alerte.java`

**class** `Alerte` (public)

- **Champs :**
  - `private Long id`
  - `private final String reference`
  - `private final TypeAlerte type`
  - `private final String message`
  - `private final String destinataireRole`
  - `private final String actionSuggeree`
  - `private final String codeProduit`
  - `private final String codeEntrepot`
  - `private final String referenceLiee`
  - `private StatutAlerte statut`
  - `private String matriculeTraitant`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateLecture`
  - `private LocalDateTime dateTraitement`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private Alerte(String reference, TypeAlerte type, String message, String codeProduit,
                   String codeEntrepot, String referenceLiee, LocalDateTime dateCreation)`
  - `public static Alerte creer(String reference, TypeAlerte type, String message, String codeProduit,
                               String codeEntrepot, String referenceLiee)`
  - `public static Alerte reconstituer(Long id, String reference, TypeAlerte type, String message,
                                      String codeProduit, String codeEntrepot, String referenceLiee,
                                      StatutAlerte statut, String matriculeTraitant, LocalDateTime dateCreation,
                                      LocalDateTime dateLecture, LocalDateTime dateTraitement, Long version)`
  - `public void marquerLue()`
  - `public void traiter(String matricule)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getReference()`
  - `public TypeAlerte getType()`
  - `public String getMessage()`
  - `public String getDestinataireRole()`
  - `public String getActionSuggeree()`
  - `public String getCodeProduit()`
  - `public String getCodeEntrepot()`
  - `public String getReferenceLiee()`
  - `public StatutAlerte getStatut()`
  - `public String getMatriculeTraitant()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateLecture()`
  - `public LocalDateTime getDateTraitement()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\alerte\repository\AlerteRepository.java`

**interface** `AlerteRepository` (public)

#### `modules\stock\domain\catalogue\enums\CategorieMetier.java`

**enum** `CategorieMetier` (public)

- **Constantes :**
  - `{

    TANTY_BOUILLIE_DE_SOJA("")`
  - `TANTY_CHOCOLAT("")`
  - `INGREDIENTS("")`
  - `TANTY_A_GRIGNOTER("")`
  - `AUTRE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`
  - `public boolean estBouillie()`
  - `public boolean estGrignoter()`
  - `public static CategorieMetier depuisLibelle(String valeur)`

#### `modules\stock\domain\catalogue\enums\CategorieProduit.java`

**enum** `CategorieProduit` (public)

- **Constantes :**
  - `{

    MATIERE_PREMIERE("")`
  - `CONSOMMABLE("")`
  - `PRODUIT_FINI("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`
  - `public boolean estReceptionFournisseur()`

#### `modules\stock\domain\catalogue\enums\NiveauConditionnement.java`

**enum** `NiveauConditionnement` (public)

- **Constantes :**
  - `{

    UNITE_BASE("", 1)`
  - `INTERMEDIAIRE("", 2)`
  - `CARTON("", 3)`
  - `PALETTE("", 4)`

- **Champs :**
  - `private final String libelle`
  - `private final int rang`

- **Méthodes :**
  - `public String getLibelle()`
  - `public int getRang()`
  - `public boolean estUniteBase()`

#### `modules\stock\domain\catalogue\event\ConditionnementAjouteEvent.java`

**class** `ConditionnementAjouteEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String codeProduit`
  - `private final String sku`
  - `private final NiveauConditionnement niveau`

- **Méthodes :**
  - `public ConditionnementAjouteEvent(String codeProduit, String sku, NiveauConditionnement niveau)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getCodeProduit()`
  - `public String getSku()`
  - `public NiveauConditionnement getNiveau()`

#### `modules\stock\domain\catalogue\event\ProduitCreeEvent.java`

**class** `ProduitCreeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String codeProduit`
  - `private final String marque`
  - `private final String gamme`
  - `private final String variete`
  - `private final String designation`
  - `private final CategorieProduit categorie`

- **Méthodes :**
  - `public ProduitCreeEvent(String codeProduit, String marque, String gamme, String variete,
                            String designation, CategorieProduit categorie)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getCodeProduit()`
  - `public String getMarque()`
  - `public String getGamme()`
  - `public String getVariete()`
  - `public String getDesignation()`
  - `public CategorieProduit getCategorie()`

#### `modules\stock\domain\catalogue\model\Conditionnement.java`

**class** `Conditionnement` (public)

- **Champs :**
  - `private final String id`
  - `private final String codeProduit`
  - `private String sku`
  - `private NiveauConditionnement niveau`
  - `private String libelle`
  - `private long facteurConversionVersBase`
  - `private int nombreUnites`
  - `private String uniteConditionnement`
  - `private double poidsUnitaireGrammes`
  - `private double volumeUnitaireCm3`
  - `private BigDecimal prixUnitaireHT`
  - `private String codeBarres`
  - `double poidsFinal = poidsUnitaireGrammes > 0 ? poidsUnitaireGrammes : poidsParse`

- **Méthodes :**
  - `private Conditionnement(String id, String codeProduit, String sku, NiveauConditionnement niveau,
                            String libelle, long facteurConversionVersBase, int nombreUnites,
                            String uniteConditionnement, double poidsUnitaireGrammes,
                            double volumeUnitaireCm3, BigDecimal prixUnitaireHT, String codeBarres)`
  - `public static Conditionnement creer(String codeProduit, String sku, NiveauConditionnement niveau,
                                        String libelle, long facteurConversionVersBase,
                                        double poidsUnitaireGrammes, double volumeUnitaireCm3,
                                        BigDecimal prixUnitaireHT, String codeBarres)`
  - `public static Conditionnement reconstituer(String id, String codeProduit, String sku,
                                               NiveauConditionnement niveau, String libelle,
                                               long facteurConversionVersBase, double poidsUnitaireGrammes,
                                               double volumeUnitaireCm3, BigDecimal prixUnitaireHT,
                                               String codeBarres)`
  - `public long versUnitesBase(long quantite)`
  - `public void mettreAJourPrix(BigDecimal nouveauPrix)`
  - `public String getId()`
  - `public String getCodeProduit()`
  - `public String getSku()`
  - `public NiveauConditionnement getNiveau()`
  - `public String getLibelle()`
  - `public long getFacteurConversionVersBase()`
  - `public int getNombreUnites()`
  - `public String getUniteConditionnement()`
  - `public double getPoidsUnitaireGrammes()`
  - `public double getVolumeUnitaireCm3()`
  - `public BigDecimal getPrixUnitaireHT()`
  - `public String getCodeBarres()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\catalogue\model\Produit.java`

**class** `Produit` (public)

- **Champs :**
  - `private Long id`
  - `private final CodeProduit code`
  - `private String marque`
  - `private String gamme`
  - `private String variete`
  - `private String designation`
  - `private CategorieProduit categorie`
  - `private CategorieMetier categorieMetier`
  - `private boolean actif`
  - `private Double prixDistributeur`
  - `private Double prixGrossiste`
  - `private Double prixDetaillant`
  - `private boolean tva`
  - `private String conditionnementCommercial`
  - `private String uniteVenteCommerciale`
  - `private final List<Conditionnement> conditionnements`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private Produit(CodeProduit code, String marque, String gamme, String variete,
                    String designation, CategorieProduit categorie, CategorieMetier categorieMetier,
                    LocalDateTime dateCreation)`
  - `public static Produit creer(CodeProduit code, String marque, String gamme, String variete,
                                String designation, CategorieProduit categorie)`
  - `public static Produit creer(CodeProduit code, String marque, String gamme, String variete,
                                String designation, CategorieProduit categorie, CategorieMetier categorieMetier)`
  - `public static Produit reconstituer(Long id, CodeProduit code, String marque, String gamme,
                                       String variete, String designation, CategorieProduit categorie,
                                       boolean actif, List<Conditionnement> conditionnements,
                                       LocalDateTime dateCreation, LocalDateTime dateModification,
                                       Long version,
                                       Double prixDistributeur, Double prixGrossiste, Double prixDetaillant,
                                       boolean tva, String conditionnementCommercial, String uniteVenteCommerciale)`
  - `public static Produit reconstituer(Long id, CodeProduit code, String marque, String gamme,
                                       String variete, String designation, CategorieProduit categorie,
                                       CategorieMetier categorieMetier,
                                       boolean actif, List<Conditionnement> conditionnements,
                                       LocalDateTime dateCreation, LocalDateTime dateModification,
                                       Long version,
                                       Double prixDistributeur, Double prixGrossiste, Double prixDetaillant,
                                       boolean tva, String conditionnementCommercial, String uniteVenteCommerciale)`
  - `public void definirPrixCommerciaux(Double prixDistributeur, Double prixGrossiste, Double prixDetaillant,
                                        boolean tva, String conditionnementCommercial, String uniteVenteCommerciale)`
  - `public Conditionnement ajouterConditionnement(String sku, NiveauConditionnement niveau, String libelle,
                                                  long facteurConversionVersBase, double poidsUnitaireGrammes,
                                                  double volumeUnitaireCm3, BigDecimal prixUnitaireHT,
                                                  String codeBarres)`
  - `public void modifierInfos(String marque, String gamme, String variete, String designation)`
  - `public void definirCategorieMetier(CategorieMetier categorieMetier)`
  - `public void desactiver()`
  - `public void reactiver()`
  - `public long convertir(long quantite, NiveauConditionnement source, NiveauConditionnement cible)`
  - `public Conditionnement trouverParNiveau(NiveauConditionnement niveau)`
  - `public Optional<Conditionnement> trouverParSku(String sku)`
  - `private void verifierActif()`
  - `private boolean existeNiveau(NiveauConditionnement niveau)`
  - `private boolean existeSku(String sku)`
  - `private boolean existeCodeBarres(String codeBarres)`
  - `private static String exigerNonVide(String valeur, String champ)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public CodeProduit getCode()`
  - `public String getMarque()`
  - `public String getGamme()`
  - `public String getVariete()`
  - `public String getDesignation()`
  - `public CategorieProduit getCategorie()`
  - `public CategorieMetier getCategorieMetier()`
  - `public boolean isActif()`
  - `public List<Conditionnement> getConditionnements()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `public Double getPrixDistributeur()`
  - `public Double getPrixGrossiste()`
  - `public Double getPrixDetaillant()`
  - `public boolean isTva()`
  - `public String getConditionnementCommercial()`
  - `public String getUniteVenteCommerciale()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\catalogue\repository\ProduitRepository.java`

**interface** `ProduitRepository` (public)

#### `modules\stock\domain\catalogue\specification\ProduitSpecification.java`

**class** `ProduitSpecification` (public final)

- **Méthodes :**
  - `private ProduitSpecification()`
  - `public static boolean peutGerer(Role role)`
  - `public static boolean peutConsulter(Role role)`

#### `modules\stock\domain\catalogue\util\ConditionnementParser.java`

**class** `ConditionnementParser` (public)

- **Méthodes :**
  - `private ConditionnementParser()`
  - `public static Resultat parse(String libelle)`
  - `private static double parsePoids(String valeur, String unite)`
  - `private static String normaliserUnite(String unite)`
  - `public record Resultat(int nombreUnites, String unite, double poidsUnitaireGrammes)`

**record** `Resultat` (public)

- **Composants (record) :**
  - `int nombreUnites`
  - `String unite`
  - `double poidsUnitaireGrammes`

#### `modules\stock\domain\catalogue\valueobject\CodeProduit.java`

**class** `CodeProduit` (public final)

- **Champs :**
  - `private final String valeur`

- **Méthodes :**
  - `private CodeProduit(String valeur)`
  - `public static CodeProduit de(String valeur)`
  - `public String getValeur()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`
  - `@Override public String toString()`

#### `modules\stock\domain\commande\enums\StatutBonCommande.java`

**enum** `StatutBonCommande` (public)

- **Constantes :**
  - `{

    BROUILLON("")`
  - `VALIDE("")`
  - `ENVOYE("")`
  - `RECU_PARTIEL("")`
  - `RECU_TOTAL("")`
  - `CLOTURE("")`
  - `ANNULE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`
  - `public boolean peutTransitionnerVers(StatutBonCommande cible)`
  - `public boolean estModifiable()`
  - `public boolean estTerminal()`

#### `modules\stock\domain\commande\event\BonCommandeValideEvent.java`

**class** `BonCommandeValideEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numero`
  - `private final String codeFournisseur`
  - `private final LocalDate dateLivraisonPrevue`

- **Méthodes :**
  - `public BonCommandeValideEvent(String numero, String codeFournisseur, LocalDate dateLivraisonPrevue)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumero()`
  - `public String getCodeFournisseur()`
  - `public LocalDate getDateLivraisonPrevue()`

#### `modules\stock\domain\commande\model\BonCommande.java`

**class** `BonCommande` (public)

- **Champs :**
  - `private Long id`
  - `private final String numero`
  - `private final String codeFournisseur`
  - `private StatutBonCommande statut`
  - `private final LocalDate dateCommande`
  - `private LocalDate dateLivraisonPrevue`
  - `private final String matriculeCreateur`
  - `private final List<LigneBonCommande> lignes`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private BonCommande(String numero, String codeFournisseur, LocalDate dateCommande,
                        LocalDate dateLivraisonPrevue, String matriculeCreateur, LocalDateTime dateCreation)`
  - `public static BonCommande creer(String numero, String codeFournisseur, LocalDate dateCommande,
                                    LocalDate dateLivraisonPrevue, String matriculeCreateur)`
  - `public static BonCommande reconstituer(Long id, String numero, String codeFournisseur, StatutBonCommande statut,
                                           LocalDate dateCommande, LocalDate dateLivraisonPrevue,
                                           String matriculeCreateur, List<LigneBonCommande> lignes,
                                           LocalDateTime dateCreation, LocalDateTime dateModification, Long version)`
  - `public LigneBonCommande ajouterLigne(String codeProduit, String designation, String sku,
                                         long quantiteCommandee, BigDecimal prixUnitaireHT)`
  - `public void valider()`
  - `public void envoyer()`
  - `public void annuler()`
  - `public void cloturer()`
  - `public void enregistrerReceptionLigne(String idLigne, long quantiteRecue)`
  - `public boolean estEnRetard(LocalDate aujourdhui)`
  - `public BigDecimal montantTotalHT()`
  - `private void transitionner(StatutBonCommande cible)`
  - `private void exigerNonVide()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumero()`
  - `public String getCodeFournisseur()`
  - `public StatutBonCommande getStatut()`
  - `public LocalDate getDateCommande()`
  - `public LocalDate getDateLivraisonPrevue()`
  - `public String getMatriculeCreateur()`
  - `public List<LigneBonCommande> getLignes()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\commande\model\LigneBonCommande.java`

**class** `LigneBonCommande` (public)

- **Champs :**
  - `private final String id`
  - `private final String codeProduit`
  - `private final String designation`
  - `private final String sku`
  - `private final long quantiteCommandee`
  - `private long quantiteRecue`
  - `private final BigDecimal prixUnitaireHT`

- **Méthodes :**
  - `private LigneBonCommande(String id, String codeProduit, String designation, String sku,
                            long quantiteCommandee, long quantiteRecue, BigDecimal prixUnitaireHT)`
  - `public static LigneBonCommande creer(String codeProduit, String designation, String sku,
                                         long quantiteCommandee, BigDecimal prixUnitaireHT)`
  - `public static LigneBonCommande reconstituer(String id, String codeProduit, String designation, String sku,
                                                long quantiteCommandee, long quantiteRecue, BigDecimal prixUnitaireHT)`
  - `void enregistrerReception(long quantite)`
  - `public boolean estCompletementRecue()`
  - `public BigDecimal montantLigneHT()`
  - `public String getId()`
  - `public String getCodeProduit()`
  - `public String getDesignation()`
  - `public String getSku()`
  - `public long getQuantiteCommandee()`
  - `public long getQuantiteRecue()`
  - `public BigDecimal getPrixUnitaireHT()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\commande\repository\BonCommandeRepository.java`

**interface** `BonCommandeRepository` (public)

#### `modules\stock\domain\commande\specification\BonCommandeSpecification.java`

**class** `BonCommandeSpecification` (public final)

- **Méthodes :**
  - `private BonCommandeSpecification()`
  - `public static boolean peutGerer(Role role)`
  - `public static boolean peutCreer(Role role)`
  - `public static boolean peutValider(Role role)`
  - `public static boolean peutModifierApresValidation(Role role)`
  - `public static boolean peutSupprimerApresValidation(Role role)`
  - `public static boolean peutConsulter(Role role)`

#### `modules\stock\domain\dotation\enums\StatutDotation.java`

**enum** `StatutDotation` (public)

- **Constantes :**
  - `{
    SOUMISE("")`
  - `APPROUVEE("")`
  - `REFUSEE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\stock\domain\dotation\event\DotationApprouveeEvent.java`

**class** `DotationApprouveeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numero`
  - `private final String matriculeCommercial`
  - `private final String matriculeGestionnaire`
  - `private final LocalDate dateEffet`

- **Méthodes :**
  - `public DotationApprouveeEvent(String numero, String matriculeCommercial, String matriculeGestionnaire,
                                  LocalDate dateEffet)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumero()`
  - `public String getMatriculeCommercial()`
  - `public String getMatriculeGestionnaire()`
  - `public LocalDate getDateEffet()`

#### `modules\stock\domain\dotation\event\DotationSoumiseEvent.java`

**class** `DotationSoumiseEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numero`
  - `private final String matriculeCommercial`
  - `private final LocalDate dateEffet`

- **Méthodes :**
  - `public DotationSoumiseEvent(String numero, String matriculeCommercial, LocalDate dateEffet)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumero()`
  - `public String getMatriculeCommercial()`
  - `public LocalDate getDateEffet()`

#### `modules\stock\domain\dotation\model\Dotation.java`

**class** `Dotation` (public)

- **Champs :**
  - `private Long id`
  - `private final String numero`
  - `private final String matriculeCommercial`
  - `private final LocalDate dateDotation`
  - `private final LocalDate dateEffet`
  - `private StatutDotation statut`
  - `private final List<LigneDotation> lignes`
  - `private String matriculeGestionnaire`
  - `private String motifRefus`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateDecision`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private Dotation(String numero, String matriculeCommercial, LocalDate dateDotation, LocalDate dateEffet,
                     LocalDateTime dateCreation)`
  - `public static Dotation soumettre(String numero, String matriculeCommercial, LocalDate dateDotation)`
  - `public static Dotation reconstituer(Long id, String numero, String matriculeCommercial, LocalDate dateDotation,
                                        LocalDate dateEffet, StatutDotation statut, List<LigneDotation> lignes,
                                        String matriculeGestionnaire, String motifRefus, LocalDateTime dateCreation,
                                        LocalDateTime dateDecision, Long version)`
  - `public LigneDotation ajouterLigne(String codeProduit, String sku, long quantiteDemandee)`
  - `public void modifierLigne(String idLigne, long nouvelleQuantite, String motif, Role role)`
  - `public void substituerConditionnement(String idLigne, String nouveauSku, String motif, Role role)`
  - `public void approuver(String matriculeGestionnaire, Role role)`
  - `public void refuser(String matriculeGestionnaire, Role role, String motif)`
  - `private void exigerGestionnaire(Role role)`
  - `private void exigerSoumise()`
  - `private LigneDotation trouverLigne(String idLigne)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumero()`
  - `public String getMatriculeCommercial()`
  - `public LocalDate getDateDotation()`
  - `public LocalDate getDateEffet()`
  - `public StatutDotation getStatut()`
  - `public List<LigneDotation> getLignes()`
  - `public String getMatriculeGestionnaire()`
  - `public String getMotifRefus()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateDecision()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\dotation\model\LigneDotation.java`

**class** `LigneDotation` (public)

- **Champs :**
  - `private final String id`
  - `private final String codeProduit`
  - `private String sku`
  - `private final long quantiteDemandee`
  - `private long quantiteApprouvee`
  - `private String motifModification`

- **Méthodes :**
  - `private LigneDotation(String id, String codeProduit, String sku, long quantiteDemandee,
                         long quantiteApprouvee, String motifModification)`
  - `public static LigneDotation creer(String codeProduit, String sku, long quantiteDemandee)`
  - `public static LigneDotation reconstituer(String id, String codeProduit, String sku, long quantiteDemandee,
                                             long quantiteApprouvee, String motifModification)`
  - `void modifierQuantite(long nouvelleQuantite, String motif)`
  - `void substituerConditionnement(String nouveauSku, String motif)`
  - `public String getId()`
  - `public String getCodeProduit()`
  - `public String getSku()`
  - `public long getQuantiteDemandee()`
  - `public long getQuantiteApprouvee()`
  - `public String getMotifModification()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\dotation\repository\DotationRepository.java`

**interface** `DotationRepository` (public)

#### `modules\stock\domain\entrepot\enums\TypeEntrepot.java`

**enum** `TypeEntrepot` (public)

- **Constantes :**
  - `{

    CENTRAL("")`
  - `TAMPON("")`
  - `MOBILE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`
  - `public boolean estMobile()`

#### `modules\stock\domain\entrepot\model\Entrepot.java`

**class** `Entrepot` (public)

- **Champs :**
  - `private Long id`
  - `private final String code`
  - `private String libelle`
  - `private final TypeEntrepot type`
  - `private final String matriculeProprietaire`
  - `private boolean actif`
  - `private final LocalDateTime dateCreation`
  - `private Long version`

- **Méthodes :**
  - `private Entrepot(String code, String libelle, TypeEntrepot type,
                     String matriculeProprietaire, LocalDateTime dateCreation)`
  - `public static Entrepot creerCentral(String code, String libelle)`
  - `public static Entrepot creerTampon(String code, String libelle)`
  - `public static Entrepot creerMobile(String matriculeCommercial)`
  - `public static Entrepot reconstituer(Long id, String code, String libelle, TypeEntrepot type,
                                        String matriculeProprietaire, boolean actif,
                                        LocalDateTime dateCreation, Long version)`
  - `public void renommer(String nouveauLibelle)`
  - `public void desactiver()`
  - `public Long getId()`
  - `public String getCode()`
  - `public String getLibelle()`
  - `public TypeEntrepot getType()`
  - `public String getMatriculeProprietaire()`
  - `public boolean isActif()`
  - `public LocalDateTime getDateCreation()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\entrepot\repository\EntrepotRepository.java`

**interface** `EntrepotRepository` (public)

#### `modules\stock\domain\fournisseur\model\Fournisseur.java`

**class** `Fournisseur` (public)

- **Champs :**
  - `private Long id`
  - `private final String code`
  - `private String raisonSociale`
  - `private String telephone`
  - `private String email`
  - `private String adresse`
  - `private boolean actif`
  - `private final LocalDateTime dateCreation`
  - `private Long version`

- **Méthodes :**
  - `private Fournisseur(String code, String raisonSociale, String telephone, String email,
                        String adresse, LocalDateTime dateCreation)`
  - `public static Fournisseur creer(String code, String raisonSociale, String telephone,
                                    String email, String adresse)`
  - `public static Fournisseur reconstituer(Long id, String code, String raisonSociale, String telephone,
                                           String email, String adresse, boolean actif,
                                           LocalDateTime dateCreation, Long version)`
  - `public void modifier(String raisonSociale, String telephone, String email, String adresse)`
  - `public void desactiver()`
  - `public Long getId()`
  - `public String getCode()`
  - `public String getRaisonSociale()`
  - `public String getTelephone()`
  - `public String getEmail()`
  - `public String getAdresse()`
  - `public boolean isActif()`
  - `public LocalDateTime getDateCreation()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\fournisseur\repository\FournisseurRepository.java`

**interface** `FournisseurRepository` (public)

#### `modules\stock\domain\inventaire\enums\StatutInventaire.java`

**enum** `StatutInventaire` (public)

- **Constantes :**
  - `{
    OUVERT("")`
  - `COMPTE("")`
  - `VALIDE("")`
  - `CLOTURE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\stock\domain\inventaire\enums\TypeInventaire.java`

**enum** `TypeInventaire` (public)

- **Constantes :**
  - `{
    COMPLET("")`
  - `TOURNANT("")`
  - `CIBLE("")`
  - `SURPRISE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\stock\domain\inventaire\event\InventaireValideEvent.java`

**class** `InventaireValideEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numero`
  - `private final String codeEntrepot`
  - `private final TypeInventaire type`

- **Méthodes :**
  - `public InventaireValideEvent(String numero, String codeEntrepot, TypeInventaire type)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumero()`
  - `public String getCodeEntrepot()`
  - `public TypeInventaire getType()`

#### `modules\stock\domain\inventaire\model\LigneInventaire.java`

**class** `LigneInventaire` (public)

- **Champs :**
  - `private final String id`
  - `private final String codeProduit`
  - `private final String emplacement`
  - `private final long quantiteTheorique`
  - `private Long quantiteComptee`

- **Méthodes :**
  - `private LigneInventaire(String id, String codeProduit, String emplacement,
                           long quantiteTheorique, Long quantiteComptee)`
  - `public static LigneInventaire creer(String codeProduit, String emplacement, long quantiteTheorique)`
  - `public static LigneInventaire reconstituer(String id, String codeProduit, String emplacement,
                                               long quantiteTheorique, Long quantiteComptee)`
  - `void saisirComptage(long quantiteComptee)`
  - `public boolean estComptee()`
  - `public long getEcart()`
  - `public String getId()`
  - `public String getCodeProduit()`
  - `public String getEmplacement()`
  - `public long getQuantiteTheorique()`
  - `public Long getQuantiteComptee()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\inventaire\model\SessionInventaire.java`

**class** `SessionInventaire` (public)

- **Champs :**
  - `private static final double SEUIL_ECART_DEROGATION = 5.0`
  - `private Long id`
  - `private final String numero`
  - `private final TypeInventaire type`
  - `private final String codeEntrepot`
  - `private final String perimetre`
  - `private StatutInventaire statut`
  - `private final List<LigneInventaire> lignes`
  - `private final String matriculeInitiateur`
  - `private String matriculeValidateurDG`
  - `private String justificationEcarts`
  - `private final LocalDate dateInventaire`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateValidation`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private SessionInventaire(String numero, TypeInventaire type, String codeEntrepot, String perimetre,
                             String matriculeInitiateur, LocalDate dateInventaire, LocalDateTime dateCreation)`
  - `public static SessionInventaire ouvrir(String numero, TypeInventaire type, String codeEntrepot, String perimetre,
                                           String matriculeInitiateur, Role role)`
  - `public static SessionInventaire reconstituer(Long id, String numero, TypeInventaire type, String codeEntrepot,
                                                 String perimetre, StatutInventaire statut, List<LigneInventaire> lignes,
                                                 String matriculeInitiateur, String matriculeValidateurDG,
                                                 String justificationEcarts, LocalDate dateInventaire,
                                                 LocalDateTime dateCreation, LocalDateTime dateValidation, Long version)`
  - `public LigneInventaire ajouterLigne(String codeProduit, String emplacement, long quantiteTheorique)`
  - `public void saisirComptage(String idLigne, long quantiteComptee)`
  - `public void cloturerComptage()`
  - `public void valider(String matriculeDG, Role role, String justification)`
  - `public boolean aEcartSignificatif()`
  - `private LigneInventaire trouverLigne(String idLigne)`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumero()`
  - `public TypeInventaire getType()`
  - `public String getCodeEntrepot()`
  - `public String getPerimetre()`
  - `public StatutInventaire getStatut()`
  - `public List<LigneInventaire> getLignes()`
  - `public String getMatriculeInitiateur()`
  - `public String getMatriculeValidateurDG()`
  - `public String getJustificationEcarts()`
  - `public LocalDate getDateInventaire()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateValidation()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\inventaire\repository\InventaireRepository.java`

**interface** `InventaireRepository` (public)

#### `modules\stock\domain\mouvement\enums\SensMouvement.java`

**enum** `SensMouvement` (public)

#### `modules\stock\domain\mouvement\enums\TypeMouvement.java`

**enum** `TypeMouvement` (public)

- **Constantes :**
  - `{

    ENTREE_RECEPTION_FOURNISSEUR("", SensMouvement.ENTREE)`
  - `ENTREE_RECEPTION_PRODUCTION("", SensMouvement.ENTREE)`
  - `TRANSFERT_CENTRAL_VERS_TAMPON("", SensMouvement.TRANSFERT)`
  - `SORTIE_DOTATION("", SensMouvement.SORTIE)`
  - `RETOUR_COMMERCIAL("", SensMouvement.ENTREE)`
  - `AJUSTEMENT_INVENTAIRE("", SensMouvement.ENTREE)`

- **Champs :**
  - `private final String libelle`
  - `private final SensMouvement sens`

- **Méthodes :**
  - `public String getLibelle()`
  - `public SensMouvement getSens()`

#### `modules\stock\domain\mouvement\event\MouvementEnregistreEvent.java`

**class** `MouvementEnregistreEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String reference`
  - `private final TypeMouvement type`
  - `private final String codeProduit`
  - `private final String sku`
  - `private final long quantiteEnBase`
  - `private final String codeEntrepotSource`
  - `private final String codeEntrepotCible`
  - `private final String matriculeActeur`

- **Méthodes :**
  - `public MouvementEnregistreEvent(String reference, TypeMouvement type, String codeProduit, String sku,
                                    long quantiteEnBase, String codeEntrepotSource, String codeEntrepotCible,
                                    String matriculeActeur)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getReference()`
  - `public TypeMouvement getType()`
  - `public String getCodeProduit()`
  - `public String getSku()`
  - `public long getQuantiteEnBase()`
  - `public String getCodeEntrepotSource()`
  - `public String getCodeEntrepotCible()`
  - `public String getMatriculeActeur()`

#### `modules\stock\domain\mouvement\model\MouvementStock.java`

**class** `MouvementStock` (public)

- **Champs :**
  - `private Long id`
  - `private final String reference`
  - `private final TypeMouvement type`
  - `private final String codeProduit`
  - `private final String sku`
  - `private final long quantiteEnBase`
  - `private final String codeEntrepotSource`
  - `private final String codeEntrepotCible`
  - `private final String matriculeActeur`
  - `private final String referenceDocument`
  - `private final String motif`
  - `private final LocalDateTime horodatage`

- **Méthodes :**
  - `private MouvementStock(String reference, TypeMouvement type, String codeProduit, String sku,
                           long quantiteEnBase, String codeEntrepotSource, String codeEntrepotCible,
                           String matriculeActeur, String referenceDocument, String motif,
                           LocalDateTime horodatage)`
  - `public static MouvementStock enregistrer(String reference, TypeMouvement type, String codeProduit,
                                             String sku, long quantiteEnBase, String codeEntrepotSource,
                                             String codeEntrepotCible, String matriculeActeur,
                                             String referenceDocument, String motif)`
  - `public static MouvementStock reconstituer(Long id, String reference, TypeMouvement type, String codeProduit,
                                              String sku, long quantiteEnBase, String codeEntrepotSource,
                                              String codeEntrepotCible, String matriculeActeur,
                                              String referenceDocument, String motif, LocalDateTime horodatage)`
  - `public Long getId()`
  - `public String getReference()`
  - `public TypeMouvement getType()`
  - `public String getCodeProduit()`
  - `public String getSku()`
  - `public long getQuantiteEnBase()`
  - `public String getCodeEntrepotSource()`
  - `public String getCodeEntrepotCible()`
  - `public String getMatriculeActeur()`
  - `public String getReferenceDocument()`
  - `public String getMotif()`
  - `public LocalDateTime getHorodatage()`

#### `modules\stock\domain\mouvement\repository\MouvementStockRepository.java`

**interface** `MouvementStockRepository` (public)

#### `modules\stock\domain\niveau\model\NiveauStock.java`

**class** `NiveauStock` (public)

- **Champs :**
  - `private Long id`
  - `private final String codeProduit`
  - `private final String codeEntrepot`
  - `private final TypeEntrepot typeEntrepot`
  - `private long quantiteEnBase`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private NiveauStock(String codeProduit, String codeEntrepot, TypeEntrepot typeEntrepot,
                        long quantiteEnBase)`
  - `public static NiveauStock creer(String codeProduit, String codeEntrepot, TypeEntrepot typeEntrepot)`
  - `public static NiveauStock reconstituer(Long id, String codeProduit, String codeEntrepot,
                                           TypeEntrepot typeEntrepot, long quantiteEnBase,
                                           LocalDateTime dateModification, Long version)`
  - `public void entrer(long quantiteEnBase)`
  - `public void sortir(long quantiteEnBase)`
  - `public boolean aAuMoins(long quantiteEnBase)`
  - `public Long getId()`
  - `public String getCodeProduit()`
  - `public String getCodeEntrepot()`
  - `public TypeEntrepot getTypeEntrepot()`
  - `public long getQuantiteEnBase()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\niveau\repository\NiveauStockRepository.java`

**interface** `NiveauStockRepository` (public)

#### `modules\stock\domain\niveau\service\GestionDeStock.java`

**class** `GestionDeStock` (public final)

- **Méthodes :**
  - `private GestionDeStock()`
  - `public static MouvementStock appliquerEntree(NiveauStock cible, TypeMouvement type, String reference,
                                                 String sku, long quantiteEnBase, String matriculeActeur,
                                                 String referenceDocument, String motif)`
  - `public static MouvementStock appliquerSortie(NiveauStock source, TypeMouvement type, String reference,
                                                 String sku, long quantiteEnBase, String matriculeActeur,
                                                 String referenceDocument, String motif)`
  - `public static MouvementStock appliquerTransfert(NiveauStock source, NiveauStock cible, String reference,
                                                    String sku, long quantiteEnBase, String matriculeActeur,
                                                    String referenceDocument, String motif)`

#### `modules\stock\domain\notification\model\HistoriqueNotification.java`

**class** `HistoriqueNotification` (public)

- **Champs :**
  - `private Long id`
  - `private final String typeNotification`
  - `private final String titre`
  - `private final String message`
  - `private final String destinataireRole`
  - `private final String matriculeDestinataire`
  - `private final String canal`
  - `private final String priorite`
  - `private final String referenceLiee`
  - `private boolean lu`
  - `private LocalDateTime dateCreation`
  - `private LocalDateTime dateLecture`
  - `private Long version`

- **Méthodes :**
  - `public HistoriqueNotification(String typeNotification, String titre, String message,
                                  String destinataireRole, String matriculeDestinataire,
                                  String canal, String priorite, String referenceLiee)`
  - `public static HistoriqueNotification reconstituer(Long id, String typeNotification, String titre,
            String message, String destinataireRole, String matriculeDestinataire,
            String canal, String priorite, String referenceLiee, boolean lu,
            LocalDateTime dateCreation, LocalDateTime dateLecture, Long version)`
  - `public void marquerCommeLu()`
  - `public Long getId()`
  - `public String getTypeNotification()`
  - `public String getTitre()`
  - `public String getMessage()`
  - `public String getDestinataireRole()`
  - `public String getMatriculeDestinataire()`
  - `public String getCanal()`
  - `public String getPriorite()`
  - `public String getReferenceLiee()`
  - `public boolean isLu()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateLecture()`
  - `public Long getVersion()`

#### `modules\stock\domain\notification\model\PreferencesNotification.java`

**class** `PreferencesNotification` (public)

- **Champs :**
  - `private Long id`
  - `private final String matriculeUtilisateur`
  - `private boolean notificationInApp`
  - `private boolean notificationEmail`
  - `private boolean notificationSms`
  - `private boolean notificationPush`
  - `private String prioriteMinimale`
  - `private LocalTime heureDebutReception`
  - `private LocalTime heureFinReception`
  - `private Set<String> typesFiltres`
  - `private Long version`

- **Méthodes :**
  - `public PreferencesNotification(String matriculeUtilisateur)`
  - `public static PreferencesNotification reconstituer(Long id, String matriculeUtilisateur,
            boolean inApp, boolean email, boolean sms, boolean push,
            String prioriteMinimale, LocalTime heureDebut, LocalTime heureFin,
            Set<String> typesFiltres, Long version)`
  - `public void mettreAJour(boolean inApp, boolean email, boolean sms, boolean push,
            String prioriteMin, LocalTime heureDebut, LocalTime heureFin,
            Set<String> typesFiltres)`
  - `public boolean accepteCanal(String canal)`
  - `public boolean acceptePriorite(String priorite)`
  - `private int ordre(String p)`
  - `public Long getId()`
  - `public String getMatriculeUtilisateur()`
  - `public boolean isNotificationInApp()`
  - `public boolean isNotificationEmail()`
  - `public boolean isNotificationSms()`
  - `public boolean isNotificationPush()`
  - `public String getPrioriteMinimale()`
  - `public LocalTime getHeureDebutReception()`
  - `public LocalTime getHeureFinReception()`
  - `public Set<String> getTypesFiltres()`
  - `public Long getVersion()`

#### `modules\stock\domain\notification\repository\HistoriqueNotificationRepository.java`

**interface** `HistoriqueNotificationRepository` (public)

#### `modules\stock\domain\notification\repository\PreferencesNotificationRepository.java`

**interface** `PreferencesNotificationRepository` (public)

#### `modules\stock\domain\reception\enums\SourceReception.java`

**enum** `SourceReception` (public)

- **Constantes :**
  - `{
    FOURNISSEUR("")`
  - `PRODUCTION("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\stock\domain\reception\enums\StatutReception.java`

**enum** `StatutReception` (public)

- **Constantes :**
  - `{
    EN_ATTENTE_VALIDATION_1("")`
  - `EN_ATTENTE_VALIDATION_2("")`
  - `VALIDEE("")`
  - `REJETEE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`
  - `public boolean estTerminal()`

#### `modules\stock\domain\reception\event\ReceptionValideeEvent.java`

**class** `ReceptionValideeEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numero`
  - `private final SourceReception source`
  - `private final String codeEntrepotCible`
  - `private final String numeroBonCommande`

- **Méthodes :**
  - `public ReceptionValideeEvent(String numero, SourceReception source, String codeEntrepotCible, String numeroBonCommande)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumero()`
  - `public SourceReception getSource()`
  - `public String getCodeEntrepotCible()`
  - `public String getNumeroBonCommande()`

#### `modules\stock\domain\reception\model\LigneReception.java`

**class** `LigneReception` (public)

- **Champs :**
  - `private final String id`
  - `private final String codeProduit`
  - `private final String sku`
  - `private final long quantiteAttendue`
  - `private final long quantiteRecue`
  - `private final String idLigneBC`

- **Méthodes :**
  - `private LigneReception(String id, String codeProduit, String sku, long quantiteAttendue,
                          long quantiteRecue, String idLigneBC)`
  - `public static LigneReception creer(String codeProduit, String sku, long quantiteAttendue,
                                       long quantiteRecue, String idLigneBC)`
  - `public static LigneReception creerSimple(String codeProduit, String sku, long quantiteRecue, String idLigneBC)`
  - `public static LigneReception reconstituer(String id, String codeProduit, String sku, long quantiteAttendue,
                                              long quantiteRecue, String idLigneBC)`
  - `public long getEcart()`
  - `public double pourcentageEcart()`
  - `public String getId()`
  - `public String getCodeProduit()`
  - `public String getSku()`
  - `public long getQuantiteAttendue()`
  - `public long getQuantiteRecue()`
  - `public String getIdLigneBC()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\reception\model\Reception.java`

**class** `Reception` (public)

- **Champs :**
  - `private Long id`
  - `private final String numero`
  - `private final SourceReception source`
  - `private final CategorieProduit categorie`
  - `private final String codeFournisseur`
  - `private final String numeroBonCommande`
  - `private final String numeroLotProduction`
  - `private final String codeEntrepotCible`
  - `private StatutReception statut`
  - `private final List<LigneReception> lignes`
  - `private String matriculeValidateur1`
  - `private String matriculeValidateur2`
  - `private String motifRejet`
  - `private final double seuilEcartPourcentage`
  - `private final LocalDate dateReception`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private Reception(String numero, SourceReception source, CategorieProduit categorie, String codeFournisseur,
                      String numeroBonCommande, String numeroLotProduction, String codeEntrepotCible,
                      double seuilEcartPourcentage, LocalDate dateReception, LocalDateTime dateCreation)`
  - `public static Reception creerFournisseur(String numero, CategorieProduit categorie, String codeFournisseur,
                                             String numeroBonCommande, String codeEntrepotCible,
                                             double seuilEcartPourcentage, LocalDate dateReception)`
  - `public static Reception creerProduction(String numero, String numeroLotProduction, String codeEntrepotCible,
                                            double seuilEcartPourcentage, LocalDate dateReception)`
  - `public static Reception reconstituer(Long id, String numero, SourceReception source, CategorieProduit categorie,
                                         String codeFournisseur, String numeroBonCommande, String numeroLotProduction,
                                         String codeEntrepotCible, StatutReception statut, List<LigneReception> lignes,
                                         String matriculeValidateur1, String matriculeValidateur2, String motifRejet,
                                         double seuilEcartPourcentage, LocalDate dateReception,
                                         LocalDateTime dateCreation, LocalDateTime dateModification, Long version)`
  - `public LigneReception ajouterLigne(String codeProduit, String sku, long quantiteAttendue,
                                       long quantiteRecue, String idLigneBC)`
  - `public LigneReception ajouterLigneSimple(String codeProduit, String sku, long quantiteRecue, String idLigneBC)`
  - `public void validerEtape1(String matriculeValidateur, Role role)`
  - `public void validerEtape2(String matriculeValidateur, Role role)`
  - `public void rejeter(String matricule, Role role, String motif)`
  - `public boolean aEcartSignificatif()`
  - `private void verifierRoleEtape2(Role role)`
  - `private void exigerNonVide()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumero()`
  - `public SourceReception getSource()`
  - `public CategorieProduit getCategorie()`
  - `public String getCodeFournisseur()`
  - `public String getNumeroBonCommande()`
  - `public String getNumeroLotProduction()`
  - `public String getCodeEntrepotCible()`
  - `public StatutReception getStatut()`
  - `public List<LigneReception> getLignes()`
  - `public String getMatriculeValidateur1()`
  - `public String getMatriculeValidateur2()`
  - `public String getMotifRejet()`
  - `public double getSeuilEcartPourcentage()`
  - `public LocalDate getDateReception()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\reception\repository\ReceptionRepository.java`

**interface** `ReceptionRepository` (public)

#### `modules\stock\domain\retour\model\BonRetour.java`

**class** `BonRetour` (public)

- **Champs :**
  - `private Long id`
  - `private final String numero`
  - `private final String matriculeCommercial`
  - `private final String matriculeGestionnaire`
  - `private final List<LigneRetour> lignes`
  - `private final LocalDate dateRetour`
  - `private final LocalDateTime dateCreation`
  - `private Long version`

- **Méthodes :**
  - `private BonRetour(String numero, String matriculeCommercial, String matriculeGestionnaire,
                      LocalDate dateRetour, LocalDateTime dateCreation)`
  - `public static BonRetour creer(String numero, String matriculeCommercial, String matriculeGestionnaire,
                                  LocalDate dateRetour)`
  - `public static BonRetour reconstituer(Long id, String numero, String matriculeCommercial,
                                         String matriculeGestionnaire, List<LigneRetour> lignes,
                                         LocalDate dateRetour, LocalDateTime dateCreation, Long version)`
  - `public LigneRetour ajouterLigne(String codeProduit, String sku, long quantite, String motif)`
  - `public void exigerNonVide()`
  - `public Long getId()`
  - `public String getNumero()`
  - `public String getMatriculeCommercial()`
  - `public String getMatriculeGestionnaire()`
  - `public List<LigneRetour> getLignes()`
  - `public LocalDate getDateRetour()`
  - `public LocalDateTime getDateCreation()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\retour\model\LigneRetour.java`

**class** `LigneRetour` (public)

- **Champs :**
  - `private final String id`
  - `private final String codeProduit`
  - `private final String sku`
  - `private final long quantite`
  - `private final String motif`

- **Méthodes :**
  - `private LigneRetour(String id, String codeProduit, String sku, long quantite, String motif)`
  - `public static LigneRetour creer(String codeProduit, String sku, long quantite, String motif)`
  - `public static LigneRetour reconstituer(String id, String codeProduit, String sku, long quantite, String motif)`
  - `public String getId()`
  - `public String getCodeProduit()`
  - `public String getSku()`
  - `public long getQuantite()`
  - `public String getMotif()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\retour\repository\BonRetourRepository.java`

**interface** `BonRetourRepository` (public)

#### `modules\stock\domain\sessioncommercial\enums\StatutSession.java`

**enum** `StatutSession` (public)

#### `modules\stock\domain\sessioncommercial\model\SessionCommercial.java`

**class** `SessionCommercial` (public)

- **Champs :**
  - `private Long id`
  - `private final String numeroSession`
  - `private final String matriculeCommercial`
  - `private final LocalDate datePreparation`
  - `private final LocalDate dateEffet`
  - `private StatutSession statut`
  - `private final List<String> dotationsIds`
  - `private String matriculeCloture`
  - `private String matriculeValidateurSecretaire`
  - `private String matriculeValidateurComptable`
  - `private LocalDateTime heureOuverture`
  - `private LocalDateTime heureCloture`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private SessionCommercial(String numeroSession, String matriculeCommercial,
                              LocalDate datePreparation, LocalDate dateEffet, LocalDateTime dateCreation)`
  - `public static SessionCommercial preparer(String numeroSession, String matriculeCommercial,
                                              LocalDate datePreparation)`
  - `public static SessionCommercial reconstituer(Long id, String numeroSession, String matriculeCommercial,
                                                  LocalDate datePreparation, LocalDate dateEffet,
                                                  StatutSession statut, List<String> dotationsIds,
                                                  String matriculeCloture, String matriculeValidateurSecretaire,
                                                  String matriculeValidateurComptable,
                                                  LocalDateTime heureOuverture, LocalDateTime heureCloture,
                                                  LocalDateTime dateCreation, LocalDateTime dateModification, Long version)`
  - `public void lierDotation(String dotationId)`
  - `public void ouvrir()`
  - `public void cloturer(String matriculeCloture)`
  - `public void validerParSecretaire(String matriculeSecretaire)`
  - `public void validerParComptable(String matriculeComptable)`
  - `public boolean estEnCours()`
  - `public int nombreDotations()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumeroSession()`
  - `public String getMatriculeCommercial()`
  - `public LocalDate getDatePreparation()`
  - `public LocalDate getDateEffet()`
  - `public StatutSession getStatut()`
  - `public List<String> getDotationsIds()`
  - `public String getMatriculeCloture()`
  - `public String getMatriculeValidateurSecretaire()`
  - `public String getMatriculeValidateurComptable()`
  - `public LocalDateTime getHeureOuverture()`
  - `public LocalDateTime getHeureCloture()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\sessioncommercial\repository\SessionCommercialRepository.java`

**interface** `SessionCommercialRepository` (public)

#### `modules\stock\domain\seuil\model\SeuilStock.java`

**class** `SeuilStock` (public)

- **Champs :**
  - `private Long id`
  - `private final String codeProduit`
  - `private final TypeEntrepot typeEntrepot`
  - `private long seuilReapprovisionnement`
  - `private long seuilSecurite`
  - `private boolean actif`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `private SeuilStock(String codeProduit, TypeEntrepot typeEntrepot, long seuilReapprovisionnement, long seuilSecurite)`
  - `public static SeuilStock definir(String codeProduit, TypeEntrepot typeEntrepot, long seuil)`
  - `public static SeuilStock definirAvecSeuilSecurite(String codeProduit, TypeEntrepot typeEntrepot,
                                                       long seuilReappro, long seuilSecurite)`
  - `public static SeuilStock reconstituer(Long id, String codeProduit, TypeEntrepot typeEntrepot, long seuil,
                                          boolean actif, LocalDateTime dateModification, Long version)`
  - `public static SeuilStock reconstituerAvecSeuilSecurite(Long id, String codeProduit, TypeEntrepot typeEntrepot,
                                                            long seuilReappro, long seuilSecurite,
                                                            boolean actif, LocalDateTime dateModification, Long version)`
  - `public void modifierSeuil(long nouveauSeuil)`
  - `public void modifierSeuilSecurite(long nouveauSeuilSecurite)`
  - `public boolean estFranchiPar(long quantiteEnBase)`
  - `public boolean estEnSeuilSecurite(long quantiteEnBase)`
  - `public Long getId()`
  - `public String getCodeProduit()`
  - `public TypeEntrepot getTypeEntrepot()`
  - `public long getSeuilReapprovisionnement()`
  - `public long getSeuilSecurite()`
  - `public boolean isActif()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\seuil\repository\SeuilStockRepository.java`

**interface** `SeuilStockRepository` (public)

#### `modules\stock\domain\stockmobile\enums\TypeMouvementMobile.java`

**enum** `TypeMouvementMobile` (public)

#### `modules\stock\domain\stockmobile\model\LigneStockMobile.java`

**class** `LigneStockMobile` (public)

- **Champs :**
  - `private Long id`
  - `private final String codeProduit`
  - `private final String designation`
  - `private final String conditionnement`
  - `private long quantiteDisponible`
  - `private long quantiteInitiale`
  - `private long quantiteVendue`
  - `private long quantiteAvariee`
  - `private long quantiteDonnee`
  - `private LocalDateTime dateDernierMouvement`
  - `return quantiteDisponible > seuilSurstock`

- **Méthodes :**
  - `public LigneStockMobile(String codeProduit, String designation, String conditionnement, long quantiteInitiale)`
  - `public static LigneStockMobile reconstituer(Long id, String codeProduit, String designation,
                                                  String conditionnement, long quantiteDisponible,
                                                  long quantiteInitiale, long quantiteVendue,
                                                  long quantiteAvariee, long quantiteDonnee,
                                                  LocalDateTime dateDernierMouvement)`
  - `public void crediterDotation(long quantite)`
  - `public void debiterVente(long quantite)`
  - `public void debiterAvarie(long quantite)`
  - `public void debiterDon(long quantite)`
  - `public long getQuantiteRestante()`
  - `public boolean estEnSurstock(long seuilSurstock)`
  - `public Long getId()`
  - `public String getCodeProduit()`
  - `public String getDesignation()`
  - `public String getConditionnement()`
  - `public long getQuantiteDisponible()`
  - `public long getQuantiteInitiale()`
  - `public long getQuantiteVendue()`
  - `public long getQuantiteAvariee()`
  - `public long getQuantiteDonnee()`
  - `public LocalDateTime getDateDernierMouvement()`

#### `modules\stock\domain\stockmobile\model\StockMobile.java`

**class** `StockMobile` (public)

- **Champs :**
  - `private Long id`
  - `private final String matriculeCommercial`
  - `private final Map<String, LigneStockMobile> lignes`
  - `private LocalDate dateSession`
  - `private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `private Long version`

- **Méthodes :**
  - `public StockMobile(String matriculeCommercial, LocalDate dateSession)`
  - `public static StockMobile reconstituer(Long id, String matriculeCommercial, LocalDate dateSession,
                                            List<LigneStockMobile> lignesList,
                                            LocalDateTime dateCreation, LocalDateTime dateModification, Long version)`
  - `public void recevoirDotation(String codeProduit, String designation, String conditionnement, long quantite)`
  - `public void enregistrerVente(String codeProduit, long quantite)`
  - `public void enregistrerAvarie(String codeProduit, long quantite)`
  - `public void enregistrerDon(String codeProduit, long quantite)`
  - `public long quantiteDisponible(String codeProduit)`
  - `public boolean peutRecevoirDotation(String codeProduit, long quantiteDemandee, long seuilSurstock)`
  - `public List<LigneStockMobile> getLignes()`
  - `public Optional<LigneStockMobile> trouverLigne(String codeProduit)`
  - `public long totalVentes()`
  - `public long totalRestant()`
  - `public long totalAvaries()`
  - `public Long getId()`
  - `public String getMatriculeCommercial()`
  - `public LocalDate getDateSession()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateModification()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\stockmobile\repository\StockMobileRepository.java`

**interface** `StockMobileRepository` (public)

#### `modules\stock\domain\transfert\enums\StatutTransfert.java`

**enum** `StatutTransfert` (public)

- **Constantes :**
  - `{
    CREE("")`
  - `EXECUTE("")`
  - `ANNULE("")`

- **Champs :**
  - `private final String libelle`

- **Méthodes :**
  - `public String getLibelle()`

#### `modules\stock\domain\transfert\event\TransfertExecuteEvent.java`

**class** `TransfertExecuteEvent` (public) implements `DomainEvent`

- **Champs :**
  - `private final String numero`
  - `private final String codeEntrepotSource`
  - `private final String codeEntrepotCible`

- **Méthodes :**
  - `public TransfertExecuteEvent(String numero, String codeEntrepotSource, String codeEntrepotCible)`
  - `@Override public String getEventId()`
  - `@Override public Instant getOccurredOn()`
  - `public String getNumero()`
  - `public String getCodeEntrepotSource()`
  - `public String getCodeEntrepotCible()`

#### `modules\stock\domain\transfert\model\BonTransfert.java`

**class** `BonTransfert` (public)

- **Champs :**
  - `private Long id`
  - `private final String numero`
  - `private final String codeEntrepotSource`
  - `private final String codeEntrepotCible`
  - `private StatutTransfert statut`
  - `private final String matriculeGestionnaire`
  - `private final boolean declencheParSeuil`
  - `private final List<LigneTransfert> lignes`
  - `private final LocalDateTime dateCreation`
  - `private LocalDateTime dateExecution`
  - `private Long version`
  - `private final List<Object> domainEvents = new ArrayList<>()`

- **Méthodes :**
  - `private BonTransfert(String numero, String codeEntrepotSource, String codeEntrepotCible,
                         String matriculeGestionnaire, boolean declencheParSeuil, LocalDateTime dateCreation)`
  - `public static BonTransfert creer(String numero, String codeEntrepotSource, String codeEntrepotCible,
                                     String matriculeGestionnaire, boolean declencheParSeuil)`
  - `public static BonTransfert reconstituer(Long id, String numero, String codeEntrepotSource, String codeEntrepotCible,
                                            StatutTransfert statut, String matriculeGestionnaire, boolean declencheParSeuil,
                                            List<LigneTransfert> lignes, LocalDateTime dateCreation,
                                            LocalDateTime dateExecution, Long version)`
  - `public LigneTransfert ajouterLigne(String codeProduit, String sku, long quantite)`
  - `public void marquerExecute()`
  - `public void annuler()`
  - `public List<Object> getDomainEvents()`
  - `public void clearDomainEvents()`
  - `public Long getId()`
  - `public String getNumero()`
  - `public String getCodeEntrepotSource()`
  - `public String getCodeEntrepotCible()`
  - `public StatutTransfert getStatut()`
  - `public String getMatriculeGestionnaire()`
  - `public boolean isDeclencheParSeuil()`
  - `public List<LigneTransfert> getLignes()`
  - `public LocalDateTime getDateCreation()`
  - `public LocalDateTime getDateExecution()`
  - `public Long getVersion()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\transfert\model\LigneTransfert.java`

**class** `LigneTransfert` (public)

- **Champs :**
  - `private final String id`
  - `private final String codeProduit`
  - `private final String sku`
  - `private final long quantite`

- **Méthodes :**
  - `private LigneTransfert(String id, String codeProduit, String sku, long quantite)`
  - `public static LigneTransfert creer(String codeProduit, String sku, long quantite)`
  - `public static LigneTransfert reconstituer(String id, String codeProduit, String sku, long quantite)`
  - `public String getId()`
  - `public String getCodeProduit()`
  - `public String getSku()`
  - `public long getQuantite()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\stock\domain\transfert\repository\BonTransfertRepository.java`

**interface** `BonTransfertRepository` (public)

#### `modules\stock\infrastructure\messaging\StockEventRelay.java`

**class** `StockEventRelay` (@Component
public)

- **Champs :**
  - `private final RabbitTemplate rabbitTemplate`
  - `private final NotificationWebSocketService webSocketService`
  - `private final EmailService emailService`
  - `private final HistoriqueNotificationRepository historiqueRepository`

- **Méthodes :**
  - `public StockEventRelay(RabbitTemplate rabbitTemplate, NotificationWebSocketService webSocketService,
                           EmailService emailService, HistoriqueNotificationRepository historiqueRepository)`
  - `@EventListener public void surAlerteCreee(AlerteCreeeEvent event)`
  - `@EventListener public void surMouvementEnregistre(MouvementEnregistreEvent event)`
  - `@EventListener public void surDotationApprouvee(DotationApprouveeEvent event)`
  - `@EventListener public void surDotationSoumise(DotationSoumiseEvent event)`
  - `@EventListener public void surReceptionValidee(ReceptionValideeEvent event)`

#### `modules\stock\infrastructure\messaging\StockRabbitListeners.java`

**class** `StockRabbitListeners` (@Component
public)

- **Champs :**
  - `private final NotificationWebSocketService webSocketService`

- **Méthodes :**
  - `public StockRabbitListeners(NotificationWebSocketService webSocketService)`
  - `@RabbitListener(queues = RabbitMQConfig.QUEUE_LOT_DECLARE) public void surLotProductionDeclare(Map<String, Object> message)`
  - `@RabbitListener(queues = RabbitMQConfig.QUEUE_PRECOMMANDE_VALIDEE) public void surPreCommandeValidee(Map<String, Object> message)`

#### `modules\stock\infrastructure\persistence\alerte\jpa\AlerteJpaEntity.java`

**class** `AlerteJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 30) private String reference`
  - `@Column(name = "", nullable = false, length = 30) private String type`
  - `@Column(name = "", nullable = false, length = 500) private String message`
  - `@Column(name = "", nullable = false, length = 30) private String destinataireRole`
  - `@Column(name = "", length = 200) private String actionSuggeree`
  - `@Column(name = "", length = 50) private String codeProduit`
  - `@Column(name = "", length = 60) private String codeEntrepot`
  - `@Column(name = "", length = 30) private String referenceLiee`
  - `@Column(name = "", nullable = false, length = 20) private String statut`
  - `@Column(name = "", length = 20) private String matriculeTraitant`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateLecture`
  - `@Column(name = "") private LocalDateTime dateTraitement`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\alerte\jpa\AlerteJpaRepository.java`

**interface** `AlerteJpaRepository` (public) extends `JpaRepository<AlerteJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\alerte\repository\AlerteRepositoryImpl.java`

**class** `AlerteRepositoryImpl` (@Component
public) implements `AlerteRepository`

- **Champs :**
  - `private final AlerteJpaRepository jpaRepository`

- **Méthodes :**
  - `public AlerteRepositoryImpl(AlerteJpaRepository jpaRepository)`
  - `@Override public Alerte sauvegarder(Alerte alerte)`
  - `@Override public Optional<Alerte> trouverParReference(String reference)`
  - `@Override public List<Alerte> trouverParStatut(StatutAlerte statut)`
  - `@Override public List<Alerte> trouverParDestinataire(String role)`
  - `@Override public List<Alerte> trouverTous()`
  - `@Override public boolean existeAlerteActive(TypeAlerte type, String codeProduit, String codeEntrepot)`
  - `@Override public long compter()`
  - `private AlerteJpaEntity toEntity(Alerte a)`
  - `private Alerte toDomain(AlerteJpaEntity e)`

#### `modules\stock\infrastructure\persistence\catalogue\jpa\ConditionnementJpaEntity.java`

**class** `ConditionnementJpaEntity` (@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @Column(name = "", length = 36) private String id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private ProduitJpaEntity produit`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", nullable = false, length = 60) private String sku`
  - `@Column(name = "", nullable = false, length = 20) private String niveau`
  - `@Column(name = "", nullable = false, length = 120) private String libelle`
  - `@Column(name = "", nullable = false) private long facteurConversionVersBase`
  - `@Column(name = "", nullable = false) private int nombreUnites`
  - `@Column(name = "", nullable = false, length = 30) private String uniteConditionnement`
  - `@Column(name = "", nullable = false) private double poidsUnitaireGrammes`
  - `@Column(name = "", nullable = false) private double volumeUnitaireCm3`
  - `@Column(name = "", precision = 15, scale = 2) private BigDecimal prixUnitaireHT`
  - `@Column(name = "", length = 60) private String codeBarres`

#### `modules\stock\infrastructure\persistence\catalogue\jpa\ProduitJpaEntity.java`

**class** `ProduitJpaEntity` (@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 50, unique = true) private String code`
  - `@Column(name = "", nullable = false, length = 100) private String marque`
  - `@Column(name = "", nullable = false, length = 100) private String gamme`
  - `@Column(name = "", nullable = false, length = 100) private String variete`
  - `@Column(name = "", nullable = false, length = 255) private String designation`
  - `@Column(name = "", nullable = false, length = 30) private String categorie`
  - `@Column(name = "", length = 30) private String categorieMetier`
  - `@Column(name = "", nullable = false) private boolean actif`
  - `@Column(name = "") private Double prixDistributeur`
  - `@Column(name = "") private Double prixGrossiste`
  - `@Column(name = "") private Double prixDetaillant`
  - `@Column(name = "", nullable = false) private boolean tva`
  - `@Column(name = "", length = 50) private String conditionnementCommercial`
  - `@Column(name = "", length = 50) private String uniteVenteCommerciale`
  - `ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ConditionnementJpaEntity> conditionnements = new ArrayList<>()`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version
    @Column(name = "", nullable = false) private Long version`

#### `modules\stock\infrastructure\persistence\catalogue\jpa\ProduitJpaRepository.java`

**interface** `ProduitJpaRepository` (public) extends `JpaRepository<ProduitJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\catalogue\mapper\ProduitPersistenceMapper.java`

**class** `ProduitPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public ProduitJpaEntity versEntite(Produit produit)`
  - `private ConditionnementJpaEntity versConditionnementEntite(Conditionnement c, ProduitJpaEntity parent)`
  - `public Produit versDomaine(ProduitJpaEntity entite)`
  - `private CategorieMetier parseCategorieMetier(String valeur)`
  - `private Conditionnement versConditionnementDomaine(ConditionnementJpaEntity e)`

#### `modules\stock\infrastructure\persistence\catalogue\repository\ProduitRepositoryImpl.java`

**class** `ProduitRepositoryImpl` (@Component
public) implements `ProduitRepository`

- **Champs :**
  - `private final ProduitJpaRepository jpaRepository`
  - `private final ProduitPersistenceMapper mapper`

- **Méthodes :**
  - `public ProduitRepositoryImpl(ProduitJpaRepository jpaRepository, ProduitPersistenceMapper mapper)`
  - `@Override public Produit sauvegarder(Produit produit)`
  - `@Override public Optional<Produit> trouverParCode(CodeProduit code)`
  - `@Override public Optional<Produit> trouverParId(Long id)`
  - `@Override public Optional<Produit> trouverParSku(String sku)`
  - `@Override public boolean existeParCode(CodeProduit code)`
  - `@Override public List<Produit> trouverTous()`
  - `@Override public List<Produit> trouverParCategorie(CategorieProduit categorie)`
  - `@Override public List<Produit> trouverParCategorieMetier(CategorieMetier categorieMetier)`
  - `@Override public List<Produit> rechercher(String marque, String gamme, String variete, Boolean actif)`
  - `@Override public void supprimer(CodeProduit code)`

#### `modules\stock\infrastructure\persistence\commande\jpa\BonCommandeJpaEntity.java`

**class** `BonCommandeJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 30) private String numero`
  - `@Column(name = "", nullable = false, length = 50) private String codeFournisseur`
  - `@Column(name = "", nullable = false, length = 20) private String statut`
  - `@Column(name = "", nullable = false) private LocalDate dateCommande`
  - `@Column(name = "") private LocalDate dateLivraisonPrevue`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeCreateur`
  - `ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneBonCommandeJpaEntity> lignes = new ArrayList<>()`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\commande\jpa\BonCommandeJpaRepository.java`

**interface** `BonCommandeJpaRepository` (public) extends `JpaRepository<BonCommandeJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\commande\jpa\LigneBonCommandeJpaEntity.java`

**class** `LigneBonCommandeJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @Column(name = "", length = 36) private String id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private BonCommandeJpaEntity bonCommande`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", length = 255) private String designation`
  - `@Column(name = "", length = 60) private String sku`
  - `@Column(name = "", nullable = false) private long quantiteCommandee`
  - `@Column(name = "", nullable = false) private long quantiteRecue`
  - `@Column(name = "", precision = 15, scale = 2) private BigDecimal prixUnitaireHT`

#### `modules\stock\infrastructure\persistence\commande\mapper\BonCommandePersistenceMapper.java`

**class** `BonCommandePersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public BonCommandeJpaEntity versEntite(BonCommande bc)`
  - `private LigneBonCommandeJpaEntity versLigneEntite(LigneBonCommande l, BonCommandeJpaEntity parent)`
  - `public BonCommande versDomaine(BonCommandeJpaEntity e)`

#### `modules\stock\infrastructure\persistence\commande\repository\BonCommandeRepositoryImpl.java`

**class** `BonCommandeRepositoryImpl` (@Component
public) implements `BonCommandeRepository`

- **Champs :**
  - `private final BonCommandeJpaRepository jpaRepository`
  - `private final BonCommandePersistenceMapper mapper`

- **Méthodes :**
  - `public BonCommandeRepositoryImpl(BonCommandeJpaRepository jpaRepository, BonCommandePersistenceMapper mapper)`
  - `@Override public BonCommande sauvegarder(BonCommande bonCommande)`
  - `@Override public Optional<BonCommande> trouverParNumero(String numero)`
  - `@Override public List<BonCommande> trouverParStatut(StatutBonCommande statut)`
  - `@Override public List<BonCommande> trouverParFournisseur(String codeFournisseur)`
  - `@Override public List<BonCommande> trouverTous()`
  - `@Override public List<BonCommande> trouverEnRetard()`
  - `@Override public long compterParAnnee(int annee)`
  - `@Override public void supprimer(BonCommande bonCommande)`

#### `modules\stock\infrastructure\persistence\dotation\jpa\DotationJpaEntity.java`

**class** `DotationJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 30) private String numero`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeCommercial`
  - `@Column(name = "", nullable = false) private LocalDate dateDotation`
  - `@Column(name = "", nullable = false) private LocalDate dateEffet`
  - `@Column(name = "", nullable = false, length = 20) private String statut`
  - `ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneDotationJpaEntity> lignes = new ArrayList<>()`
  - `@Column(name = "", length = 20) private String matriculeGestionnaire`
  - `@Column(name = "", length = 500) private String motifRefus`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateDecision`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\dotation\jpa\DotationJpaRepository.java`

**interface** `DotationJpaRepository` (public) extends `JpaRepository<DotationJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\dotation\jpa\LigneDotationJpaEntity.java`

**class** `LigneDotationJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @Column(name = "", length = 36) private String id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private DotationJpaEntity dotation`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", length = 60) private String sku`
  - `@Column(name = "", nullable = false) private long quantiteDemandee`
  - `@Column(name = "", nullable = false) private long quantiteApprouvee`
  - `@Column(name = "", length = 300) private String motifModification`

#### `modules\stock\infrastructure\persistence\dotation\mapper\DotationPersistenceMapper.java`

**class** `DotationPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public DotationJpaEntity versEntite(Dotation d)`
  - `public Dotation versDomaine(DotationJpaEntity e)`

#### `modules\stock\infrastructure\persistence\dotation\repository\DotationRepositoryImpl.java`

**class** `DotationRepositoryImpl` (@Component
public) implements `DotationRepository`

- **Champs :**
  - `private final DotationJpaRepository jpaRepository`
  - `private final DotationPersistenceMapper mapper`

- **Méthodes :**
  - `public DotationRepositoryImpl(DotationJpaRepository jpaRepository, DotationPersistenceMapper mapper)`
  - `@Override public Dotation sauvegarder(Dotation dotation)`
  - `@Override public Optional<Dotation> trouverParNumero(String numero)`
  - `@Override public List<Dotation> trouverParStatut(StatutDotation statut)`
  - `@Override public List<Dotation> trouverParCommercial(String matriculeCommercial)`
  - `@Override public List<Dotation> trouverParDateEffet(LocalDate dateEffet)`
  - `@Override public List<Dotation> trouverTous()`
  - `@Override public long compterParAnnee(int annee)`

#### `modules\stock\infrastructure\persistence\entrepot\jpa\EntrepotJpaEntity.java`

**class** `EntrepotJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 60) private String code`
  - `@Column(name = "", nullable = false, length = 120) private String libelle`
  - `@Column(name = "", nullable = false, length = 20) private String type`
  - `@Column(name = "", length = 20) private String matriculeProprietaire`
  - `@Column(name = "", nullable = false) private boolean actif`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\entrepot\jpa\EntrepotJpaRepository.java`

**interface** `EntrepotJpaRepository` (public) extends `JpaRepository<EntrepotJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\entrepot\mapper\EntrepotPersistenceMapper.java`

**class** `EntrepotPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public EntrepotJpaEntity versEntite(Entrepot e)`
  - `public Entrepot versDomaine(EntrepotJpaEntity e)`

#### `modules\stock\infrastructure\persistence\entrepot\repository\EntrepotRepositoryImpl.java`

**class** `EntrepotRepositoryImpl` (@Component
public) implements `EntrepotRepository`

- **Champs :**
  - `private final EntrepotJpaRepository jpaRepository`
  - `private final EntrepotPersistenceMapper mapper`

- **Méthodes :**
  - `public EntrepotRepositoryImpl(EntrepotJpaRepository jpaRepository, EntrepotPersistenceMapper mapper)`
  - `@Override public Entrepot sauvegarder(Entrepot entrepot)`
  - `@Override public Optional<Entrepot> trouverParCode(String code)`
  - `@Override public Optional<Entrepot> trouverMobileParCommercial(String matriculeCommercial)`
  - `@Override public Optional<Entrepot> trouverUniqueParType(TypeEntrepot type)`
  - `@Override public boolean existeParCode(String code)`
  - `@Override public List<Entrepot> trouverTous()`
  - `@Override public List<Entrepot> trouverParType(TypeEntrepot type)`

#### `modules\stock\infrastructure\persistence\fournisseur\jpa\FournisseurJpaEntity.java`

**class** `FournisseurJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 50) private String code`
  - `@Column(name = "", nullable = false, length = 200) private String raisonSociale`
  - `@Column(name = "", length = 30) private String telephone`
  - `@Column(name = "", length = 150) private String email`
  - `@Column(name = "", length = 300) private String adresse`
  - `@Column(name = "", nullable = false) private boolean actif`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\fournisseur\jpa\FournisseurJpaRepository.java`

**interface** `FournisseurJpaRepository` (public) extends `JpaRepository<FournisseurJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\fournisseur\mapper\FournisseurPersistenceMapper.java`

**class** `FournisseurPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public FournisseurJpaEntity versEntite(Fournisseur f)`
  - `public Fournisseur versDomaine(FournisseurJpaEntity e)`

#### `modules\stock\infrastructure\persistence\fournisseur\repository\FournisseurRepositoryImpl.java`

**class** `FournisseurRepositoryImpl` (@Component
public) implements `FournisseurRepository`

- **Champs :**
  - `private final FournisseurJpaRepository jpaRepository`
  - `private final FournisseurPersistenceMapper mapper`

- **Méthodes :**
  - `public FournisseurRepositoryImpl(FournisseurJpaRepository jpaRepository, FournisseurPersistenceMapper mapper)`
  - `@Override public Fournisseur sauvegarder(Fournisseur fournisseur)`
  - `@Override public Optional<Fournisseur> trouverParCode(String code)`
  - `@Override public boolean existeParCode(String code)`
  - `@Override public List<Fournisseur> trouverTous()`
  - `@Override public List<Fournisseur> rechercherParRaisonSociale(String partiel)`

#### `modules\stock\infrastructure\persistence\inventaire\jpa\LigneInventaireJpaEntity.java`

**class** `LigneInventaireJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @Column(name = "", length = 36) private String id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private SessionInventaireJpaEntity session`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", length = 120) private String emplacement`
  - `@Column(name = "", nullable = false) private long quantiteTheorique`
  - `@Column(name = "") private Long quantiteComptee`

#### `modules\stock\infrastructure\persistence\inventaire\jpa\SessionInventaireJpaEntity.java`

**class** `SessionInventaireJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 30) private String numero`
  - `@Column(name = "", nullable = false, length = 20) private String type`
  - `@Column(name = "", nullable = false, length = 60) private String codeEntrepot`
  - `@Column(name = "", length = 120) private String perimetre`
  - `@Column(name = "", nullable = false, length = 20) private String statut`
  - `ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneInventaireJpaEntity> lignes = new ArrayList<>()`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeInitiateur`
  - `@Column(name = "", length = 20) private String matriculeValidateurDG`
  - `@Column(name = "", length = 1000) private String justificationEcarts`
  - `@Column(name = "", nullable = false) private LocalDate dateInventaire`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateValidation`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\inventaire\jpa\SessionInventaireJpaRepository.java`

**interface** `SessionInventaireJpaRepository` (public) extends `JpaRepository<SessionInventaireJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\inventaire\mapper\InventairePersistenceMapper.java`

**class** `InventairePersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public SessionInventaireJpaEntity versEntite(SessionInventaire s)`
  - `public SessionInventaire versDomaine(SessionInventaireJpaEntity e)`

#### `modules\stock\infrastructure\persistence\inventaire\repository\InventaireRepositoryImpl.java`

**class** `InventaireRepositoryImpl` (@Component
public) implements `InventaireRepository`

- **Champs :**
  - `private final SessionInventaireJpaRepository jpaRepository`
  - `private final InventairePersistenceMapper mapper`

- **Méthodes :**
  - `public InventaireRepositoryImpl(SessionInventaireJpaRepository jpaRepository, InventairePersistenceMapper mapper)`
  - `@Override public SessionInventaire sauvegarder(SessionInventaire session)`
  - `@Override public Optional<SessionInventaire> trouverParNumero(String numero)`
  - `@Override public List<SessionInventaire> trouverParStatut(StatutInventaire statut)`
  - `@Override public List<SessionInventaire> trouverTous()`
  - `@Override public long compterParAnnee(int annee)`

#### `modules\stock\infrastructure\persistence\mouvement\jpa\MouvementStockJpaEntity.java`

**class** `MouvementStockJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 30) private String reference`
  - `@Column(name = "", nullable = false, length = 40) private String type`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", length = 60) private String sku`
  - `@Column(name = "", nullable = false) private long quantiteEnBase`
  - `@Column(name = "", length = 60) private String codeEntrepotSource`
  - `@Column(name = "", length = 60) private String codeEntrepotCible`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeActeur`
  - `@Column(name = "", length = 30) private String referenceDocument`
  - `@Column(name = "", length = 500) private String motif`
  - `@Column(name = "", nullable = false) private LocalDateTime horodatage`

#### `modules\stock\infrastructure\persistence\mouvement\jpa\MouvementStockJpaRepository.java`

**interface** `MouvementStockJpaRepository` (public) extends `JpaRepository<MouvementStockJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\mouvement\mapper\MouvementStockPersistenceMapper.java`

**class** `MouvementStockPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public MouvementStockJpaEntity versEntite(MouvementStock m)`
  - `public MouvementStock versDomaine(MouvementStockJpaEntity e)`

#### `modules\stock\infrastructure\persistence\mouvement\repository\MouvementStockRepositoryImpl.java`

**class** `MouvementStockRepositoryImpl` (@Component
public) implements `MouvementStockRepository`

- **Champs :**
  - `private final MouvementStockJpaRepository jpaRepository`
  - `private final MouvementStockPersistenceMapper mapper`
  - `private final EventPublisher eventPublisher`

- **Méthodes :**
  - `public MouvementStockRepositoryImpl(MouvementStockJpaRepository jpaRepository,
                                        MouvementStockPersistenceMapper mapper,
                                        EventPublisher eventPublisher)`
  - `@Override public MouvementStock enregistrer(MouvementStock mouvement)`
  - `@Override public List<MouvementStock> trouverParProduit(String codeProduit)`
  - `@Override public List<MouvementStock> trouverParEntrepot(String codeEntrepot)`
  - `@Override public List<MouvementStock> trouverParType(TypeMouvement type)`
  - `@Override public List<MouvementStock> trouverParPeriode(LocalDate debut, LocalDate fin)`
  - `@Override public List<MouvementStock> trouverParDocument(String referenceDocument)`
  - `@Override public long compter()`
  - `@Override public Page<MouvementStock> trouverParProduitPagine(String codeProduit, Pageable pageable)`
  - `@Override public Page<MouvementStock> trouverParEntrepotPagine(String codeEntrepot, Pageable pageable)`
  - `@Override public Page<MouvementStock> trouverParTypePagine(TypeMouvement type, Pageable pageable)`
  - `@Override public Page<MouvementStock> trouverParPeriodePagine(LocalDate debut, LocalDate fin, Pageable pageable)`
  - `@Override public Page<MouvementStock> trouverTousPagine(Pageable pageable)`

#### `modules\stock\infrastructure\persistence\niveau\jpa\NiveauStockJpaEntity.java`

**class** `NiveauStockJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", nullable = false, length = 60) private String codeEntrepot`
  - `@Column(name = "", nullable = false, length = 20) private String typeEntrepot`
  - `@Column(name = "", nullable = false) private long quantiteEnBase`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\niveau\jpa\NiveauStockJpaRepository.java`

**interface** `NiveauStockJpaRepository` (public) extends `JpaRepository<NiveauStockJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\niveau\mapper\NiveauStockPersistenceMapper.java`

**class** `NiveauStockPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public NiveauStockJpaEntity versEntite(NiveauStock n)`
  - `public NiveauStock versDomaine(NiveauStockJpaEntity e)`

#### `modules\stock\infrastructure\persistence\niveau\repository\NiveauStockRepositoryImpl.java`

**class** `NiveauStockRepositoryImpl` (@Component
public) implements `NiveauStockRepository`

- **Champs :**
  - `private final NiveauStockJpaRepository jpaRepository`
  - `private final NiveauStockPersistenceMapper mapper`

- **Méthodes :**
  - `public NiveauStockRepositoryImpl(NiveauStockJpaRepository jpaRepository, NiveauStockPersistenceMapper mapper)`
  - `@Override public NiveauStock sauvegarder(NiveauStock niveau)`
  - `@Override public Optional<NiveauStock> trouver(String codeProduit, String codeEntrepot)`
  - `@Override public List<NiveauStock> trouverParEntrepot(String codeEntrepot)`
  - `@Override public List<NiveauStock> trouverParProduit(String codeProduit)`
  - `@Override public List<NiveauStock> trouverParType(TypeEntrepot type)`
  - `@Override public List<NiveauStock> trouverTous()`

#### `modules\stock\infrastructure\persistence\notification\jpa\HistoriqueNotificationJpaEntity.java`

**class** `HistoriqueNotificationJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 50) private String typeNotification`
  - `@Column(name = "", nullable = false, length = 200) private String titre`
  - `@Column(name = "", nullable = false, length = 1000) private String message`
  - `@Column(name = "", length = 30) private String destinataireRole`
  - `@Column(name = "", length = 20) private String matriculeDestinataire`
  - `@Column(name = "", nullable = false, length = 10) private String canal`
  - `@Column(name = "", nullable = false, length = 10) private String priorite`
  - `@Column(name = "", length = 30) private String referenceLiee`
  - `@Column(name = "", nullable = false) private boolean lu`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateLecture`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\notification\jpa\HistoriqueNotificationJpaRepository.java`

**interface** `HistoriqueNotificationJpaRepository` (public) extends `JpaRepository<HistoriqueNotificationJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\notification\jpa\PreferencesNotificationJpaEntity.java`

**class** `PreferencesNotificationJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 20) private String matriculeUtilisateur`
  - `@Column(name = "", nullable = false) private boolean notificationInApp`
  - `@Column(name = "", nullable = false) private boolean notificationEmail`
  - `@Column(name = "", nullable = false) private boolean notificationSms`
  - `@Column(name = "", nullable = false) private boolean notificationPush`
  - `@Column(name = "", nullable = false, length = 10) private String prioriteMinimale`
  - `@Column(name = "", nullable = false) private LocalTime heureDebutReception`
  - `@Column(name = "", nullable = false) private LocalTime heureFinReception`
  - `@Column(name = "", length = 500) private String typesFiltres`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\notification\jpa\PreferencesNotificationJpaRepository.java`

**interface** `PreferencesNotificationJpaRepository` (public) extends `JpaRepository<PreferencesNotificationJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\notification\repository\HistoriqueNotificationRepositoryImpl.java`

**class** `HistoriqueNotificationRepositoryImpl` (@Repository
public) implements `HistoriqueNotificationRepository`

- **Champs :**
  - `private final HistoriqueNotificationJpaRepository jpaRepository`

- **Méthodes :**
  - `public HistoriqueNotificationRepositoryImpl(HistoriqueNotificationJpaRepository jpaRepository)`
  - `@Override public HistoriqueNotification sauvegarder(HistoriqueNotification notification)`
  - `@Override public Optional<HistoriqueNotification> trouverParId(Long id)`
  - `@Override public List<HistoriqueNotification> trouverParMatricule(String matricule)`
  - `@Override public List<HistoriqueNotification> trouverParRole(String role)`
  - `@Override public List<HistoriqueNotification> trouverNonLusParMatricule(String matricule)`
  - `@Override public List<HistoriqueNotification> trouverTous()`
  - `@Override public void supprimerParId(Long id)`
  - `private HistoriqueNotificationJpaEntity toEntity(HistoriqueNotification h)`
  - `private HistoriqueNotification toDomain(HistoriqueNotificationJpaEntity e)`

#### `modules\stock\infrastructure\persistence\notification\repository\PreferencesNotificationRepositoryImpl.java`

**class** `PreferencesNotificationRepositoryImpl` (@Repository
public) implements `PreferencesNotificationRepository`

- **Champs :**
  - `private final PreferencesNotificationJpaRepository jpaRepository`

- **Méthodes :**
  - `public PreferencesNotificationRepositoryImpl(PreferencesNotificationJpaRepository jpaRepository)`
  - `@Override public PreferencesNotification sauvegarder(PreferencesNotification preferences)`
  - `@Override public Optional<PreferencesNotification> trouverParMatricule(String matricule)`
  - `private PreferencesNotification mapToDomain(PreferencesNotificationJpaEntity e)`

#### `modules\stock\infrastructure\persistence\reception\jpa\LigneReceptionJpaEntity.java`

**class** `LigneReceptionJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @Column(name = "", length = 36) private String id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private ReceptionJpaEntity reception`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", length = 60) private String sku`
  - `@Column(name = "", nullable = false) private long quantiteAttendue`
  - `@Column(name = "", nullable = false) private long quantiteRecue`
  - `@Column(name = "", length = 36) private String idLigneBC`

#### `modules\stock\infrastructure\persistence\reception\jpa\ReceptionJpaEntity.java`

**class** `ReceptionJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 30) private String numero`
  - `@Column(name = "", nullable = false, length = 20) private String source`
  - `@Column(name = "", nullable = false, length = 30) private String categorie`
  - `@Column(name = "", length = 50) private String codeFournisseur`
  - `@Column(name = "", length = 30) private String numeroBonCommande`
  - `@Column(name = "", length = 40) private String numeroLotProduction`
  - `@Column(name = "", nullable = false, length = 60) private String codeEntrepotCible`
  - `@Column(name = "", nullable = false, length = 30) private String statut`
  - `ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneReceptionJpaEntity> lignes = new ArrayList<>()`
  - `@Column(name = "", length = 20) private String matriculeValidateur1`
  - `@Column(name = "", length = 20) private String matriculeValidateur2`
  - `@Column(name = "", length = 500) private String motifRejet`
  - `@Column(name = "", nullable = false) private double seuilEcartPourcentage`
  - `@Column(name = "", nullable = false) private LocalDate dateReception`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\reception\jpa\ReceptionJpaRepository.java`

**interface** `ReceptionJpaRepository` (public) extends `JpaRepository<ReceptionJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\reception\mapper\ReceptionPersistenceMapper.java`

**class** `ReceptionPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public ReceptionJpaEntity versEntite(Reception r)`
  - `private LigneReceptionJpaEntity versLigneEntite(LigneReception l, ReceptionJpaEntity parent)`
  - `public Reception versDomaine(ReceptionJpaEntity e)`

#### `modules\stock\infrastructure\persistence\reception\repository\ReceptionRepositoryImpl.java`

**class** `ReceptionRepositoryImpl` (@Component
public) implements `ReceptionRepository`

- **Champs :**
  - `private final ReceptionJpaRepository jpaRepository`
  - `private final ReceptionPersistenceMapper mapper`

- **Méthodes :**
  - `public ReceptionRepositoryImpl(ReceptionJpaRepository jpaRepository, ReceptionPersistenceMapper mapper)`
  - `@Override public Reception sauvegarder(Reception reception)`
  - `@Override public Optional<Reception> trouverParNumero(String numero)`
  - `@Override public List<Reception> trouverParStatut(StatutReception statut)`
  - `@Override public List<Reception> trouverParBonCommande(String numeroBonCommande)`
  - `@Override public List<Reception> trouverTous()`
  - `@Override public long compterParAnnee(int annee)`

#### `modules\stock\infrastructure\persistence\retour\jpa\BonRetourJpaEntity.java`

**class** `BonRetourJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 30) private String numero`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeCommercial`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeGestionnaire`
  - `ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneRetourJpaEntity> lignes = new ArrayList<>()`
  - `@Column(name = "", nullable = false) private LocalDate dateRetour`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\retour\jpa\BonRetourJpaRepository.java`

**interface** `BonRetourJpaRepository` (public) extends `JpaRepository<BonRetourJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\retour\jpa\LigneRetourJpaEntity.java`

**class** `LigneRetourJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @Column(name = "", length = 36) private String id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private BonRetourJpaEntity bonRetour`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", length = 60) private String sku`
  - `@Column(name = "", nullable = false) private long quantite`
  - `@Column(name = "", length = 300) private String motif`

#### `modules\stock\infrastructure\persistence\retour\mapper\BonRetourPersistenceMapper.java`

**class** `BonRetourPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public BonRetourJpaEntity versEntite(BonRetour b)`
  - `public BonRetour versDomaine(BonRetourJpaEntity e)`

#### `modules\stock\infrastructure\persistence\retour\repository\BonRetourRepositoryImpl.java`

**class** `BonRetourRepositoryImpl` (@Component
public) implements `BonRetourRepository`

- **Champs :**
  - `private final BonRetourJpaRepository jpaRepository`
  - `private final BonRetourPersistenceMapper mapper`

- **Méthodes :**
  - `public BonRetourRepositoryImpl(BonRetourJpaRepository jpaRepository, BonRetourPersistenceMapper mapper)`
  - `@Override public BonRetour sauvegarder(BonRetour bonRetour)`
  - `@Override public Optional<BonRetour> trouverParNumero(String numero)`
  - `@Override public List<BonRetour> trouverParCommercial(String matriculeCommercial)`
  - `@Override public List<BonRetour> trouverTous()`
  - `@Override public long compterParAnnee(int annee)`

#### `modules\stock\infrastructure\persistence\sessioncommercial\jpa\SessionCommercialJpaEntity.java`

**class** `SessionCommercialJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String numeroSession`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private LocalDate datePreparation`
  - `@Column(nullable = false) private LocalDate dateEffet`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private StatutSession statut`
  - `@Column(name = "") private List<String> dotationsIds`
  - `private String matriculeCloture`
  - `private String matriculeValidateurSecretaire`
  - `private String matriculeValidateurComptable`
  - `private LocalDateTime heureOuverture`
  - `private LocalDateTime heureCloture`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\sessioncommercial\jpa\SessionCommercialJpaRepository.java`

**interface** `SessionCommercialJpaRepository` (public) extends `JpaRepository<SessionCommercialJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\sessioncommercial\repository\SessionCommercialRepositoryImpl.java`

**class** `SessionCommercialRepositoryImpl` (@Repository
public) implements `SessionCommercialRepository`

- **Champs :**
  - `private final SessionCommercialJpaRepository jpaRepository`

- **Méthodes :**
  - `public SessionCommercialRepositoryImpl(SessionCommercialJpaRepository jpaRepository)`
  - `@Override public SessionCommercial sauvegarder(SessionCommercial session)`
  - `@Override public Optional<SessionCommercial> trouverParNumero(String numero)`
  - `@Override public Optional<SessionCommercial> trouverSessionOuverteParCommercial(String matriculeCommercial)`
  - `@Override public List<SessionCommercial> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<SessionCommercial> listerParDateEffet(LocalDate dateEffet)`
  - `@Override public List<SessionCommercial> listerParStatut(String statut)`
  - `private SessionCommercialJpaEntity toEntity(SessionCommercial s)`
  - `private SessionCommercial toDomain(SessionCommercialJpaEntity e)`

#### `modules\stock\infrastructure\persistence\seuil\jpa\HistoriqueSeuilJpaEntity.java`

**class** `HistoriqueSeuilJpaEntity` (@Getter @Setter @NoArgsConstructor
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, length = 50) private String codeProduit`
  - `@Column(nullable = false, length = 20) private String typeEntrepot`
  - `@Column(nullable = false) private long ancienneValeur`
  - `@Column(nullable = false) private long nouvelleValeur`
  - `@Column(nullable = false, length = 20) private String matriculeAuteur`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateModification`

#### `modules\stock\infrastructure\persistence\seuil\jpa\HistoriqueSeuilJpaRepository.java`

**interface** `HistoriqueSeuilJpaRepository` (public) extends `JpaRepository<HistoriqueSeuilJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\seuil\jpa\SeuilStockJpaEntity.java`

**class** `SeuilStockJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", nullable = false, length = 20) private String typeEntrepot`
  - `@Column(name = "", nullable = false) private long seuilReapprovisionnement`
  - `@Column(name = "", nullable = false) private boolean actif`
  - `@Column(name = "") private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\seuil\jpa\SeuilStockJpaRepository.java`

**interface** `SeuilStockJpaRepository` (public) extends `JpaRepository<SeuilStockJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\seuil\repository\SeuilStockRepositoryImpl.java`

**class** `SeuilStockRepositoryImpl` (@Component
public) implements `SeuilStockRepository`

- **Champs :**
  - `private final SeuilStockJpaRepository jpaRepository`

- **Méthodes :**
  - `public SeuilStockRepositoryImpl(SeuilStockJpaRepository jpaRepository)`
  - `@Override public SeuilStock sauvegarder(SeuilStock seuil)`
  - `@Override public Optional<SeuilStock> trouver(String codeProduit, TypeEntrepot typeEntrepot)`
  - `@Override public List<SeuilStock> trouverParType(TypeEntrepot typeEntrepot)`
  - `@Override public List<SeuilStock> trouverTous()`
  - `private SeuilStockJpaEntity toEntity(SeuilStock s)`
  - `private SeuilStock toDomain(SeuilStockJpaEntity e)`

#### `modules\stock\infrastructure\persistence\stockmobile\jpa\LigneStockMobileJpaEntity.java`

**class** `LigneStockMobileJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String codeProduit`
  - `@Column(nullable = false) private String designation`
  - `@Column(nullable = false) private String conditionnement`
  - `@Column(nullable = false) private long quantiteDisponible`
  - `@Column(nullable = false) private long quantiteInitiale`
  - `@Column(nullable = false) private long quantiteVendue`
  - `@Column(nullable = false) private long quantiteAvariee`
  - `@Column(nullable = false) private long quantiteDonnee`
  - `private LocalDateTime dateDernierMouvement`

#### `modules\stock\infrastructure\persistence\stockmobile\jpa\StockMobileJpaEntity.java`

**class** `StockMobileJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Column(nullable = false) private LocalDate dateSession`
  - `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "") private List<LigneStockMobileJpaEntity> lignes`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `private LocalDateTime dateModification`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\stockmobile\jpa\StockMobileJpaRepository.java`

**interface** `StockMobileJpaRepository` (public) extends `JpaRepository<StockMobileJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\stockmobile\repository\StockMobileRepositoryImpl.java`

**class** `StockMobileRepositoryImpl` (@Repository
public) implements `StockMobileRepository`

- **Champs :**
  - `private final StockMobileJpaRepository jpaRepository`

- **Méthodes :**
  - `public StockMobileRepositoryImpl(StockMobileJpaRepository jpaRepository)`
  - `@Override public StockMobile sauvegarder(StockMobile stockMobile)`
  - `@Override public Optional<StockMobile> trouverParCommercialEtDate(String matriculeCommercial, LocalDate date)`
  - `@Override public Optional<StockMobile> trouverDernierParCommercial(String matriculeCommercial)`
  - `@Override public List<StockMobile> listerParDate(LocalDate date)`
  - `@Override public List<StockMobile> listerParCommercial(String matriculeCommercial)`
  - `@Override public List<StockMobile> listerTous()`
  - `private StockMobileJpaEntity toEntity(StockMobile sm)`
  - `private StockMobile toDomain(StockMobileJpaEntity e)`

#### `modules\stock\infrastructure\persistence\transfert\jpa\BonTransfertJpaEntity.java`

**class** `BonTransfertJpaEntity` (@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(name = "", nullable = false, unique = true, length = 30) private String numero`
  - `@Column(name = "", nullable = false, length = 60) private String codeEntrepotSource`
  - `@Column(name = "", nullable = false, length = 60) private String codeEntrepotCible`
  - `@Column(name = "", nullable = false, length = 20) private String statut`
  - `@Column(name = "", nullable = false, length = 20) private String matriculeGestionnaire`
  - `@Column(name = "", nullable = false) private boolean declencheParSeuil`
  - `ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<LigneTransfertJpaEntity> lignes = new ArrayList<>()`
  - `@Column(name = "", nullable = false, updatable = false) private LocalDateTime dateCreation`
  - `@Column(name = "") private LocalDateTime dateExecution`
  - `@Version private Long version`

#### `modules\stock\infrastructure\persistence\transfert\jpa\BonTransfertJpaRepository.java`

**interface** `BonTransfertJpaRepository` (public) extends `JpaRepository<BonTransfertJpaEntity, Long>`

#### `modules\stock\infrastructure\persistence\transfert\jpa\LigneTransfertJpaEntity.java`

**class** `LigneTransfertJpaEntity` (@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public)

- **Champs :**
  - `@Id
    @Column(name = "", length = 36) private String id`
  - `@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false) private BonTransfertJpaEntity bonTransfert`
  - `@Column(name = "", nullable = false, length = 50) private String codeProduit`
  - `@Column(name = "", length = 60) private String sku`
  - `@Column(name = "", nullable = false) private long quantite`

#### `modules\stock\infrastructure\persistence\transfert\mapper\BonTransfertPersistenceMapper.java`

**class** `BonTransfertPersistenceMapper` (@Component
public)

- **Méthodes :**
  - `public BonTransfertJpaEntity versEntite(BonTransfert bt)`
  - `public BonTransfert versDomaine(BonTransfertJpaEntity e)`

#### `modules\stock\infrastructure\persistence\transfert\repository\BonTransfertRepositoryImpl.java`

**class** `BonTransfertRepositoryImpl` (@Component
public) implements `BonTransfertRepository`

- **Champs :**
  - `private final BonTransfertJpaRepository jpaRepository`
  - `private final BonTransfertPersistenceMapper mapper`

- **Méthodes :**
  - `public BonTransfertRepositoryImpl(BonTransfertJpaRepository jpaRepository, BonTransfertPersistenceMapper mapper)`
  - `@Override public BonTransfert sauvegarder(BonTransfert bonTransfert)`
  - `@Override public Optional<BonTransfert> trouverParNumero(String numero)`
  - `@Override public List<BonTransfert> trouverParStatut(StatutTransfert statut)`
  - `@Override public List<BonTransfert> trouverTous()`
  - `@Override public long compterParAnnee(int annee)`

#### `modules\stock\infrastructure\websocket\NotificationWebSocketService.java`

**class** `NotificationWebSocketService` (@Service
public)

- **Champs :**
  - `private final SimpMessagingTemplate messagingTemplate`

- **Méthodes :**
  - `public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate)`
  - `public void diffuser(NotificationMessage message)`
  - `public void diffuserAlerte(NotificationMessage message)`

#### `modules\stock\presentation\controller\AlerteController.java`

**class** `AlerteController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ListerAlertesUseCase listerAlertesUseCase`
  - `private final TraiterAlerteUseCase traiterAlerteUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public AlerteController(ListerAlertesUseCase listerAlertesUseCase, TraiterAlerteUseCase traiterAlerteUseCase,
                          AuthenticatedUserResolver userResolver)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<AlerteResponse>> marquerLue(@PathVariable String reference)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<AlerteResponse>> traiter(@PathVariable String reference)`

#### `modules\stock\presentation\controller\BonCommandeController.java`

**class** `BonCommandeController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final CreerBonCommandeUseCase creerBonCommandeUseCase`
  - `private final ValiderBonCommandeUseCase validerBonCommandeUseCase`
  - `private final TransitionBonCommandeUseCase transitionBonCommandeUseCase`
  - `private final ListerBonsCommandeUseCase listerBonsCommandeUseCase`
  - `private final ConsulterBonCommandeUseCase consulterBonCommandeUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public BonCommandeController(CreerBonCommandeUseCase creerBonCommandeUseCase,
                                ValiderBonCommandeUseCase validerBonCommandeUseCase,
                                TransitionBonCommandeUseCase transitionBonCommandeUseCase,
                                ListerBonsCommandeUseCase listerBonsCommandeUseCase,
                                ConsulterBonCommandeUseCase consulterBonCommandeUseCase,
                                AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> creer(@Valid @RequestBody CreerBonCommandeRequest request)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> consulter(@PathVariable String numero)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> valider(@PathVariable String numero)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> envoyer(@PathVariable String numero)`
  - `@DeleteMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable String numero)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> annuler(@PathVariable String numero)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> cloturer(@PathVariable String numero)`

#### `modules\stock\presentation\controller\CatalogueController.java`

**class** `CatalogueController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ConsulterHierarchieCatalogueUseCase consulterHierarchieCatalogueUseCase`

- **Méthodes :**
  - `public CatalogueController(ConsulterHierarchieCatalogueUseCase consulterHierarchieCatalogueUseCase)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<CatalogueHierarchieResponse>> hierarchie()`

#### `modules\stock\presentation\controller\CommercialStockController.java`

**class** `CommercialStockController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final UtilisateurRepository utilisateurRepository`
  - `private final EntrepotRepository entrepotRepository`
  - `private final NiveauStockRepository niveauStockRepository`
  - `private final ConsulterNiveauxDetaillesUseCase consulterNiveauxUseCase`

- **Méthodes :**
  - `public CommercialStockController(UtilisateurRepository utilisateurRepository,
                                      EntrepotRepository entrepotRepository,
                                      NiveauStockRepository niveauStockRepository,
                                      ConsulterNiveauxDetaillesUseCase consulterNiveauxUseCase)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<CommercialResponse>>> listerCommerciaux()`
  - `public record CommercialResponse(Long id, String name, String phone, boolean active)`
  - `public record InfoProduitsResponse(Long id,
        String matriculeCommercial,
        String nomCommercial,
        String date,
        String statut,
        List<InfoProduitsLigneResponse> lignes)`
  - `public record InfoProduitsLigneResponse(Long niveauId,
        Long produitId,
        String designation,
        String sku,
        long quantite,
        java.math.BigDecimal valeurStock)`

**record** `CommercialResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String name`
  - `String phone`
  - `boolean active`

**record** `InfoProduitsResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String matriculeCommercial`
  - `String nomCommercial`
  - `String date`
  - `String statut`
  - `List<InfoProduitsLigneResponse> lignes`

**record** `InfoProduitsLigneResponse` (public)

- **Composants (record) :**
  - `Long niveauId`
  - `Long produitId`
  - `String designation`
  - `String sku`
  - `long quantite`
  - `java.math.BigDecimal valeurStock`

#### `modules\stock\presentation\controller\DotationController.java`

**class** `DotationController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final SoumettreDotationUseCase soumettreDotationUseCase`
  - `private final ApprouverDotationUseCase approuverDotationUseCase`
  - `private final ModifierDotationUseCase modifierDotationUseCase`
  - `private final RefuserDotationUseCase refuserDotationUseCase`
  - `private final ListerDotationsUseCase listerDotationsUseCase`
  - `private final ConsulterDotationUseCase consulterDotationUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public DotationController(SoumettreDotationUseCase soumettreDotationUseCase,
                            ApprouverDotationUseCase approuverDotationUseCase,
                            ModifierDotationUseCase modifierDotationUseCase,
                            RefuserDotationUseCase refuserDotationUseCase,
                            ListerDotationsUseCase listerDotationsUseCase,
                            ConsulterDotationUseCase consulterDotationUseCase,
                            AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<DotationResponse>> soumettre(@Valid @RequestBody SoumettreDotationRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<DotationResponse>> approuver(@PathVariable String numero)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<DotationResponse>> modifierLigne(@PathVariable String numero, @Valid @RequestBody DecisionLigneRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<DotationResponse>> refuser(@PathVariable String numero, @RequestParam String motif)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<DotationResponse>> consulter(@PathVariable String numero)`

#### `modules\stock\presentation\controller\EntrepotController.java`

**class** `EntrepotController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final CreerEntrepotUseCase creerEntrepotUseCase`
  - `private final ListerEntrepotsUseCase listerEntrepotsUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public EntrepotController(CreerEntrepotUseCase creerEntrepotUseCase,
                             ListerEntrepotsUseCase listerEntrepotsUseCase,
                             AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<EntrepotResponse>> creer(@Valid @RequestBody CreerEntrepotRequest request)`

#### `modules\stock\presentation\controller\EtatStockController.java`

**class** `EtatStockController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ConsulterEtatStockUseCase consulterEtatStockUseCase`

- **Méthodes :**
  - `public EtatStockController(ConsulterEtatStockUseCase consulterEtatStockUseCase)`

#### `modules\stock\presentation\controller\FournisseurController.java`

**class** `FournisseurController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final CreerFournisseurUseCase creerFournisseurUseCase`
  - `private final ListerFournisseursUseCase listerFournisseursUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public FournisseurController(CreerFournisseurUseCase creerFournisseurUseCase,
                                ListerFournisseursUseCase listerFournisseursUseCase,
                                AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<FournisseurResponse>> creer(@Valid @RequestBody CreerFournisseurRequest request)`

#### `modules\stock\presentation\controller\HistoriqueNotificationController.java`

**class** `HistoriqueNotificationController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final HistoriqueNotificationRepository repository`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public HistoriqueNotificationController(HistoriqueNotificationRepository repository,
                                            AuthenticatedUserResolver userResolver)`
  - `@GetMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<NotificationHistoryItem>>> monHistorique()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<NotificationHistoryItem>>> nonLus()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<NotificationHistoryItem>>> parRole(@PathVariable String role)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<NotificationHistoryItem>> marquerCommeLu(@PathVariable Long id)`
  - `@DeleteMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id)`
  - `private NotificationHistoryItem toResponse(HistoriqueNotification h)`
  - `public record NotificationHistoryItem(Long id, String typeNotification, String titre, String message,
        String destinataireRole, String matriculeDestinataire, String canal,
        String priorite, String referenceLiee, boolean lu,
        java.time.LocalDateTime dateCreation, java.time.LocalDateTime dateLecture)`

**record** `NotificationHistoryItem` (public)

- **Composants (record) :**
  - `Long id`
  - `String typeNotification`
  - `String titre`
  - `String message`
  - `String destinataireRole`
  - `String matriculeDestinataire`
  - `String canal`
  - `String priorite`
  - `String referenceLiee`
  - `boolean lu`
  - `java.time.LocalDateTime dateCreation`
  - `java.time.LocalDateTime dateLecture`

#### `modules\stock\presentation\controller\InternalOrderController.java`

**class** `InternalOrderController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final BonCommandeRepository bonCommandeRepository`
  - `private final BonCommandeMapper mapper`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public InternalOrderController(BonCommandeRepository bonCommandeRepository,
                                    BonCommandeMapper mapper,
                                    AuthenticatedUserResolver userResolver)`
  - `@GetMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<BonCommandeResponse>>> lister()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<BonCommandeResponse>>> listerActives()`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> creer(@RequestBody CreerCommandeInterneRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> approuver(@PathVariable Long id)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<BonCommandeResponse>> annuler(@PathVariable Long id)`
  - `private BonCommande trouverParId(Long id)`
  - `public record CreerCommandeInterneRequest(String numero,
        String codeFournisseur,
        java.time.LocalDate dateCommande,
        java.time.LocalDate dateLivraisonPrevue,
        List<LigneCommandeInterneRequest> lignes)`
  - `public record LigneCommandeInterneRequest(String codeProduit,
        String designation,
        String sku,
        long quantiteCommandee,
        java.math.BigDecimal prixUnitaireHT)`

**record** `CreerCommandeInterneRequest` (public)

- **Composants (record) :**
  - `String numero`
  - `String codeFournisseur`
  - `java.time.LocalDate dateCommande`
  - `java.time.LocalDate dateLivraisonPrevue`
  - `List<LigneCommandeInterneRequest> lignes`

**record** `LigneCommandeInterneRequest` (public)

- **Composants (record) :**
  - `String codeProduit`
  - `String designation`
  - `String sku`
  - `long quantiteCommandee`
  - `java.math.BigDecimal prixUnitaireHT`

#### `modules\stock\presentation\controller\InventaireController.java`

**class** `InventaireController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final OuvrirInventaireUseCase ouvrirInventaireUseCase`
  - `private final SaisirComptageUseCase saisirComptageUseCase`
  - `private final CloturerComptageUseCase cloturerComptageUseCase`
  - `private final ValiderInventaireUseCase validerInventaireUseCase`
  - `private final ListerInventairesUseCase listerInventairesUseCase`
  - `private final ConsulterInventaireUseCase consulterInventaireUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public InventaireController(OuvrirInventaireUseCase ouvrirInventaireUseCase,
                              SaisirComptageUseCase saisirComptageUseCase,
                              CloturerComptageUseCase cloturerComptageUseCase,
                              ValiderInventaireUseCase validerInventaireUseCase,
                              ListerInventairesUseCase listerInventairesUseCase,
                              ConsulterInventaireUseCase consulterInventaireUseCase,
                              AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<InventaireResponse>> ouvrir(@Valid @RequestBody OuvrirInventaireRequest request)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<InventaireResponse>> saisirComptage(@PathVariable String numero, @Valid @RequestBody SaisirComptageRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<InventaireResponse>> cloturerComptage(@PathVariable String numero)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<InventaireResponse>> consulter(@PathVariable String numero)`

#### `modules\stock\presentation\controller\MouvementController.java`

**class** `MouvementController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ListerMouvementsUseCase listerMouvementsUseCase`

- **Méthodes :**
  - `public MouvementController(ListerMouvementsUseCase listerMouvementsUseCase)`

#### `modules\stock\presentation\controller\NiveauStockController.java`

**class** `NiveauStockController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ConsulterNiveauxDetaillesUseCase consulterUseCase`
  - `private final AjusterStockUseCase ajusterUseCase`
  - `private final TransfererVersTamponUseCase transfererUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public NiveauStockController(ConsulterNiveauxDetaillesUseCase consulterUseCase,
                                 AjusterStockUseCase ajusterUseCase,
                                 TransfererVersTamponUseCase transfererUseCase,
                                 AuthenticatedUserResolver userResolver)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<NiveauStockDetailResponse>>> listerParEntrepot(@PathVariable Long entrepotId)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<NiveauStockDetailResponse>>> alertes()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<NiveauStockDetailResponse>>> alertesCritiques()`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<NiveauStockDetailResponse>> ajuster(@RequestBody AjustementRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<NiveauStockDetailResponse>> transfererVersTampon(@PathVariable Long id, @RequestParam long quantity)`
  - `public record AjustementRequest(Long stockLevelId, long newQuantity, String reason)`

**record** `AjustementRequest` (public)

- **Composants (record) :**
  - `Long stockLevelId`
  - `long newQuantity`
  - `String reason`

#### `modules\stock\presentation\controller\PreferencesNotificationController.java`

**class** `PreferencesNotificationController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final PreferencesNotificationRepository repository`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public PreferencesNotificationController(PreferencesNotificationRepository repository,
                                             AuthenticatedUserResolver userResolver)`
  - `@GetMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PreferencesNotificationResponse>> consulter()`
  - `@PutMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<PreferencesNotificationResponse>> mettreAJour(@Valid @RequestBody MettreAJourPreferencesRequest request)`
  - `private PreferencesNotificationResponse toResponse(PreferencesNotification p)`

#### `modules\stock\presentation\controller\ProduitController.java`

**class** `ProduitController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final CreerProduitUseCase creerProduitUseCase`
  - `private final ListerProduitsUseCase listerProduitsUseCase`
  - `private final ConsulterProduitUseCase consulterProduitUseCase`
  - `private final ModifierProduitUseCase modifierProduitUseCase`
  - `private final AjouterConditionnementUseCase ajouterConditionnementUseCase`
  - `private final DesactiverProduitUseCase desactiverProduitUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public ProduitController(CreerProduitUseCase creerProduitUseCase,
                             ListerProduitsUseCase listerProduitsUseCase,
                             ConsulterProduitUseCase consulterProduitUseCase,
                             ModifierProduitUseCase modifierProduitUseCase,
                             AjouterConditionnementUseCase ajouterConditionnementUseCase,
                             DesactiverProduitUseCase desactiverProduitUseCase,
                             AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProduitResponse>> creer(@Valid @RequestBody CreerProduitRequest request)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProduitResponse>> consulter(@PathVariable String code)`
  - `@PutMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProduitResponse>> modifier(@PathVariable String code, @RequestBody ModifierProduitRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ProduitResponse>> ajouterConditionnement(@PathVariable String code, @Valid @RequestBody AjouterConditionnementRequest request)`
  - `@DeleteMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<Void>> desactiver(@PathVariable String code)`

#### `modules\stock\presentation\controller\RapportController.java`

**class** `RapportController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final GenererRapportEtatStockUseCase genererRapportEtatStockUseCase`
  - `private final GenererRapportMouvementsUseCase genererRapportMouvementsUseCase`

- **Méthodes :**
  - `public RapportController(GenererRapportEtatStockUseCase genererRapportEtatStockUseCase,
                           GenererRapportMouvementsUseCase genererRapportMouvementsUseCase)`
  - `private ResponseEntity<byte[]> reponseFichier(RapportFichier fichier)`

#### `modules\stock\presentation\controller\ReceptionController.java`

**class** `ReceptionController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final CreerReceptionFournisseurUseCase creerFournisseurUseCase`
  - `private final CreerReceptionProductionUseCase creerProductionUseCase`
  - `private final ValiderReceptionEtape1UseCase validerEtape1UseCase`
  - `private final ValiderReceptionEtape2UseCase validerEtape2UseCase`
  - `private final RejeterReceptionUseCase rejeterReceptionUseCase`
  - `private final ListerReceptionsUseCase listerReceptionsUseCase`
  - `private final ConsulterReceptionUseCase consulterReceptionUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public ReceptionController(CreerReceptionFournisseurUseCase creerFournisseurUseCase,
                              CreerReceptionProductionUseCase creerProductionUseCase,
                              ValiderReceptionEtape1UseCase validerEtape1UseCase,
                              ValiderReceptionEtape2UseCase validerEtape2UseCase,
                              RejeterReceptionUseCase rejeterReceptionUseCase,
                              ListerReceptionsUseCase listerReceptionsUseCase,
                              ConsulterReceptionUseCase consulterReceptionUseCase,
                              AuthenticatedUserResolver userResolver)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ReceptionResponse>> creerFournisseur(@Valid @RequestBody CreerReceptionFournisseurRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ReceptionResponse>> creerProduction(@Valid @RequestBody CreerReceptionProductionRequest request)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ReceptionResponse>> validerEtape1(@PathVariable String numero)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ReceptionResponse>> validerEtape2(@PathVariable String numero)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ReceptionResponse>> rejeter(@PathVariable String numero, @RequestParam String motif)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<ReceptionResponse>> consulter(@PathVariable String numero)`

#### `modules\stock\presentation\controller\ReportingController.java`

**class** `ReportingController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ReportingService reportingService`

- **Méthodes :**
  - `public ReportingController(ReportingService reportingService)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportStocksMobilesExcel()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportStocksMobilesPdf()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportPerformancesExcel()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportPerformancesPdf()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportStockCentralExcel()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportStockCentralPdf()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportTamponExcel()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportTamponPdf()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportValorisationExcel()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportDotationsVsVentesExcel()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportEcartsInventaireExcel()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportRotationStockExcel()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportStockCentralCsv()` throws `Exception`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<byte[]> exportTamponCsv()` throws `Exception`
  - `private ResponseEntity<byte[]> buildFileResponse(byte[] data, String filename, String contentType)`

#### `modules\stock\presentation\controller\RetourController.java`

**class** `RetourController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final EnregistrerRetourUseCase enregistrerRetourUseCase`
  - `private final ListerRetoursUseCase listerRetoursUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public RetourController(EnregistrerRetourUseCase enregistrerRetourUseCase,
                          ListerRetoursUseCase listerRetoursUseCase,
                          AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<RetourResponse>> enregistrer(@Valid @RequestBody EnregistrerRetourRequest request)`

#### `modules\stock\presentation\controller\SessionCommercialController.java`

**class** `SessionCommercialController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final SessionCommercialRepository repository`
  - `private final SessionCommercialMapper mapper`

- **Méthodes :**
  - `public SessionCommercialController(SessionCommercialRepository repository, SessionCommercialMapper mapper)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionCommercialResponse>> preparer(@RequestBody PreparerSessionRequest req)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionCommercialResponse>> lierDotation(@PathVariable String numero,
                                                                                @RequestParam String dotationId)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionCommercialResponse>> ouvrir(@PathVariable String numero)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionCommercialResponse>> cloturer(@PathVariable String numero,
                                                                            @RequestParam String matricule)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionCommercialResponse>> validerSecretaire(@PathVariable String numero,
                                                                                     @RequestParam String matricule)`
  - `@PatchMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionCommercialResponse>> validerComptable(@PathVariable String numero,
                                                                                     @RequestParam String matricule)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<SessionCommercialResponse>> consulter(@PathVariable String numero)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParCommercial(@PathVariable String matricule)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParDate(@PathVariable String date)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParStatut(@PathVariable String statut)`
  - `private SessionCommercial trouverOuErreur(String numero)`
  - `public record PreparerSessionRequest(String matriculeCommercial, LocalDate datePreparation)`

**record** `PreparerSessionRequest` (public)

- **Composants (record) :**
  - `String matriculeCommercial`
  - `LocalDate datePreparation`

#### `modules\stock\presentation\controller\SeuilController.java`

**class** `SeuilController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final DefinirSeuilUseCase definirSeuilUseCase`
  - `private final ListerSeuilsUseCase listerSeuilsUseCase`
  - `private final EvaluerSeuilsUseCase evaluerSeuilsUseCase`
  - `private final AuthenticatedUserResolver userResolver`
  - `private final HistoriqueSeuilJpaRepository historiqueRepository`

- **Méthodes :**
  - `public SeuilController(DefinirSeuilUseCase definirSeuilUseCase, ListerSeuilsUseCase listerSeuilsUseCase,
                          EvaluerSeuilsUseCase evaluerSeuilsUseCase, AuthenticatedUserResolver userResolver,
                          HistoriqueSeuilJpaRepository historiqueRepository)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "", description = "") public ResponseEntity<ApiResponse<SeuilResponse>> definir(@Valid @RequestBody DefinirSeuilRequest request)`
  - `@GetMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<SeuilResponse>>> lister()`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<AlerteResponse>>> evaluer()`

#### `modules\stock\presentation\controller\StockMobileController.java`

**class** `StockMobileController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ConsulterEtatStockUseCase consulterEtatStockUseCase`
  - `private final StockMobileRepository stockMobileRepository`
  - `private final StockMobileMapper stockMobileMapper`
  - `String codeEntrepot = "" + matriculeCommercial`

- **Méthodes :**
  - `public StockMobileController(ConsulterEtatStockUseCase consulterEtatStockUseCase,
                                  StockMobileRepository stockMobileRepository,
                                  StockMobileMapper stockMobileMapper)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<NiveauStockResponse>>> consulter(@PathVariable String matriculeCommercial)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<StockMobileResponse>>> listerTous()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<StockMobileResponse>>> historiqueCommercial(@PathVariable String matricule)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<StockMobileResponse>> stockCourant(@PathVariable String matricule)`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<List<StockMobileResponse>>> listerParDate(@PathVariable String date)`

#### `modules\stock\presentation\controller\TableauBordStockController.java`

**class** `TableauBordStockController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ConsulterTableauBordStockUseCase consulterTableauBordStockUseCase`
  - `private final ConsulterTableauBordEnrichiUseCase consulterTableauBordEnrichiUseCase`

- **Méthodes :**
  - `public TableauBordStockController(ConsulterTableauBordStockUseCase consulterTableauBordStockUseCase,
                                       ConsulterTableauBordEnrichiUseCase consulterTableauBordEnrichiUseCase)`
  - `@GetMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<TableauBordStockResponse>> consulter()`
  - `@GetMapping("")
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<TableauBordEnrichiResponse>> consulterEnrichi()`

#### `modules\stock\presentation\controller\TransfertController.java`

**class** `TransfertController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final ExecuterTransfertUseCase executerTransfertUseCase`
  - `private final ListerTransfertsUseCase listerTransfertsUseCase`
  - `private final AuthenticatedUserResolver userResolver`

- **Méthodes :**
  - `public TransfertController(ExecuterTransfertUseCase executerTransfertUseCase,
                             ListerTransfertsUseCase listerTransfertsUseCase,
                             AuthenticatedUserResolver userResolver)`
  - `@PostMapping
    @PreAuthorize("")
    @Operation(summary = "") public ResponseEntity<ApiResponse<TransfertResponse>> executer(@Valid @RequestBody CreerTransfertRequest request)`


### Module `sync`

#### `modules\sync\application\dto\SyncLogResponse.java`

**record** `SyncLogResponse` (public)

- **Composants (record) :**
  - `Long id`
  - `String idempotencyKey`
  - `String matriculeCommercial`
  - `SyncEntityType entityType`
  - `SyncOperationType operationType`
  - `String entityRef`
  - `String payloadHash`
  - `SyncResult result`
  - `String conflictDetail`
  - `String serverEntityRef`
  - `LocalDateTime dateReception`
  - `LocalDateTime dateTraitement`

#### `modules\sync\application\dto\SyncPullResponse.java`

**record** `SyncPullResponse` (public)

- **Composants (record) :**
  - `LocalDateTime serverTime`
  - `String matriculeCommercial`
  - `List<EntityBatch> batches`

- **Méthodes :**
  - `public record EntityBatch(SyncEntityType entityType,
        LocalDateTime lastSync,
        List<EntityPayload> entities)`
  - `public record EntityPayload(String ref,
        String payload,
        LocalDateTime lastModified)`

**record** `EntityBatch` (public)

- **Composants (record) :**
  - `SyncEntityType entityType`
  - `LocalDateTime lastSync`
  - `List<EntityPayload> entities`

**record** `EntityPayload` (public)

- **Composants (record) :**
  - `String ref`
  - `String payload`
  - `LocalDateTime lastModified`

#### `modules\sync\application\dto\SyncPushResponse.java`

**record** `SyncPushResponse` (public)

- **Composants (record) :**
  - `String idempotencyKey`
  - `String status`
  - `String serverRef`
  - `String conflictDetail`
  - `LocalDateTime dateTraitement`

#### `modules\sync\application\service\SyncService.java`

**class** `SyncService` (@Service
public)

- **Champs :**
  - `private final SynchronisationLogRepository syncLogRepository`
  - `private final VenteRepository venteRepository`
  - `private final PreCommandeRepository preCommandeRepository`
  - `private final FeuillRouteRepository feuillRouteRepository`

- **Méthodes :**
  - `public SyncService(SynchronisationLogRepository syncLogRepository,
                       VenteRepository venteRepository,
                       PreCommandeRepository preCommandeRepository,
                       FeuillRouteRepository feuillRouteRepository)`
  - `public SynchronisationLog traiterPush(String idempotencyKey, String matriculeCommercial,
                                           SyncEntityType entityType, String entityRef,
                                           String payloadHash, Runnable processingLogic)`
  - `public SyncPullData pullIncremental(String matriculeCommercial, LocalDateTime lastSync)`
  - `public record SyncPullData(LocalDateTime serverTime,
        List<Vente> ventes,
        List<PreCommande> preCommandes,
        List<FeuillRoute> feuillesRoute)`

**record** `SyncPullData` (public)

- **Composants (record) :**
  - `LocalDateTime serverTime`
  - `List<Vente> ventes`
  - `List<PreCommande> preCommandes`
  - `List<FeuillRoute> feuillesRoute`

#### `modules\sync\application\usecase\ConsulterHistoriqueSyncUseCase.java`

**class** `ConsulterHistoriqueSyncUseCase` (@Service
@Transactional(readOnly = true)
public)

- **Champs :**
  - `private final SynchronisationLogJpaRepository jpaRepository`
  - `List<SynchronisationLogJpaEntity> entries`

- **Méthodes :**
  - `public ConsulterHistoriqueSyncUseCase(SynchronisationLogJpaRepository jpaRepository)`
  - `public List<SyncLogResponse> executer(String matricule,
                                           SyncEntityType entityType,
                                           SyncResult result,
                                           LocalDateTime debut,
                                           LocalDateTime fin,
                                           int limite)`
  - `private SyncLogResponse versResponse(SynchronisationLogJpaEntity e)`

#### `modules\sync\domain\aggregate\SynchronisationLog.java`

**class** `SynchronisationLog` (public)

- **Champs :**
  - `private Long id`
  - `private final String idempotencyKey`
  - `private final String matriculeCommercial`
  - `private final SyncEntityType entityType`
  - `private final SyncOperationType operationType`
  - `private final String entityRef`
  - `private final String payloadHash`
  - `private SyncResult result`
  - `private String conflictDetail`
  - `private String serverEntityRef`
  - `private final LocalDateTime dateReception`
  - `private LocalDateTime dateTraitement`

- **Méthodes :**
  - `private SynchronisationLog(String idempotencyKey, String matriculeCommercial, SyncEntityType entityType,
                                SyncOperationType operationType, String entityRef, String payloadHash,
                                LocalDateTime dateReception)`
  - `public static SynchronisationLog recevoirPush(String idempotencyKey, String matriculeCommercial,
                                                   SyncEntityType entityType, String entityRef, String payloadHash)`
  - `public static SynchronisationLog reconstituer(Long id, String idempotencyKey, String matriculeCommercial,
                                                    SyncEntityType entityType, SyncOperationType operationType,
                                                    String entityRef, String payloadHash, SyncResult result,
                                                    String conflictDetail, String serverEntityRef,
                                                    LocalDateTime dateReception, LocalDateTime dateTraitement)`
  - `public void marquerSucces(String serverEntityRef)`
  - `public void marquerConflit(String conflictDetail)`
  - `public void marquerRejete(String reason)`
  - `public static String genererIdempotencyKey()`
  - `public Long getId()`
  - `public String getIdempotencyKey()`
  - `public String getMatriculeCommercial()`
  - `public SyncEntityType getEntityType()`
  - `public SyncOperationType getOperationType()`
  - `public String getEntityRef()`
  - `public String getPayloadHash()`
  - `public SyncResult getResult()`
  - `public String getConflictDetail()`
  - `public String getServerEntityRef()`
  - `public LocalDateTime getDateReception()`
  - `public LocalDateTime getDateTraitement()`
  - `@Override public boolean equals(Object o)`
  - `@Override public int hashCode()`

#### `modules\sync\domain\enums\SyncEntityType.java`

**enum** `SyncEntityType` (public)

#### `modules\sync\domain\enums\SyncOperationType.java`

**enum** `SyncOperationType` (public)

#### `modules\sync\domain\enums\SyncResult.java`

**enum** `SyncResult` (public)

#### `modules\sync\domain\repository\SynchronisationLogRepository.java`

**interface** `SynchronisationLogRepository` (public)

#### `modules\sync\infrastructure\persistence\jpa\SynchronisationLogJpaEntity.java`

**class** `SynchronisationLogJpaEntity` (@Entity
@Table(name = "")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public)

- **Champs :**
  - `@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id`
  - `@Column(nullable = false, unique = true) private String idempotencyKey`
  - `@Column(nullable = false) private String matriculeCommercial`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private SyncEntityType entityType`
  - `@Enumerated(EnumType.STRING)
    @Column(nullable = false) private SyncOperationType operationType`
  - `private String entityRef`
  - `private String payloadHash`
  - `@Enumerated(EnumType.STRING) private SyncResult result`
  - `@Column(columnDefinition = "") private String conflictDetail`
  - `private String serverEntityRef`
  - `@Column(nullable = false, updatable = false) private LocalDateTime dateReception`
  - `private LocalDateTime dateTraitement`

#### `modules\sync\infrastructure\persistence\jpa\SynchronisationLogJpaRepository.java`

**interface** `SynchronisationLogJpaRepository` (public) extends `JpaRepository<SynchronisationLogJpaEntity, Long>`

#### `modules\sync\infrastructure\persistence\repository\SynchronisationLogRepositoryImpl.java`

**class** `SynchronisationLogRepositoryImpl` (@Repository
public) implements `SynchronisationLogRepository`

- **Champs :**
  - `private final SynchronisationLogJpaRepository jpaRepository`

- **Méthodes :**
  - `public SynchronisationLogRepositoryImpl(SynchronisationLogJpaRepository jpaRepository)`
  - `@Override public SynchronisationLog sauvegarder(SynchronisationLog log)`
  - `@Override public Optional<SynchronisationLog> trouverParIdempotencyKey(String key)`
  - `private SynchronisationLogJpaEntity toEntity(SynchronisationLog log)`
  - `private SynchronisationLog toDomain(SynchronisationLogJpaEntity e)`

#### `modules\sync\presentation\controller\SyncController.java`

**class** `SyncController` (@RestController
@RequestMapping("")
@Tag(name = "", description = "")
@SecurityRequirement(name = "")
public)

- **Champs :**
  - `private final SyncService syncService`
  - `private final ConsulterHistoriqueSyncUseCase historiqueSyncUseCase`

- **Méthodes :**
  - `public SyncController(SyncService syncService, ConsulterHistoriqueSyncUseCase historiqueSyncUseCase)`
  - `@PostMapping("")
    @PreAuthorize("")
    @Operation(summary = "",
        description = "" +
                      "") public ResponseEntity<ApiResponse<SyncPushResponse>> push(@RequestBody SyncPushRequest req)`
  - `public record SyncPushRequest(String idempotencyKey,
        String matriculeCommercial,
        SyncEntityType entityType,
        String entityRef,
        String payloadHash)`

**record** `SyncPushRequest` (public)

- **Composants (record) :**
  - `String idempotencyKey`
  - `String matriculeCommercial`
  - `SyncEntityType entityType`
  - `String entityRef`
  - `String payloadHash`


---

# Frontend - frontend_erp_tanty

**Racine source** : `C:\Users\User\Downloads\Api_tanty\frontend_erp_tanty_architecture\frontend_erp_tanty_architecture\frontend_erp_tanty\src`

**Nombre total de fichiers source** : 291

## Arborescence du frontend (par domaine)

### Domaine `admin`

- `app\domains\admin\infrastructure\admin.service.ts` (ts)
- `app\domains\admin\presentation\pages\audit\admin-audit.component.css` (css)
- `app\domains\admin\presentation\pages\audit\admin-audit.component.html` (html)
- `app\domains\admin\presentation\pages\audit\admin-audit.component.ts` (ts)
- `app\domains\admin\presentation\pages\parametres\admin-parametres.component.css` (css)
- `app\domains\admin\presentation\pages\parametres\admin-parametres.component.html` (html)
- `app\domains\admin\presentation\pages\parametres\admin-parametres.component.ts` (ts)
- `app\domains\admin\presentation\pages\utilisateurs\admin-utilisateurs.component.css` (css)
- `app\domains\admin\presentation\pages\utilisateurs\admin-utilisateurs.component.html` (html)
- `app\domains\admin\presentation\pages\utilisateurs\admin-utilisateurs.component.ts` (ts)

### Domaine `app`

- `app\app.component.css` (css)
- `app\app.component.html` (html)
- `app\app.component.ts` (ts)
- `app\app.config.server.ts` (ts)
- `app\app.config.ts` (ts)
- `app\app.routes.ts` (ts)
- `environment\environment.production.ts` (ts)
- `environment\environment.ts` (ts)
- `index.html` (html)
- `main.server.ts` (ts)
- `main.ts` (ts)
- `styles.css` (css)

### Domaine `commercial`

- `app\domains\commercial\domain\models\vente.models.ts` (ts)
- `app\domains\commercial\infrastructure\commercial.service.ts` (ts)
- `app\domains\commercial\presentation\commercial.routes.ts` (ts)
- `app\domains\commercial\presentation\pages\carburant\commercial-carburant.component.css` (css)
- `app\domains\commercial\presentation\pages\carburant\commercial-carburant.component.html` (html)
- `app\domains\commercial\presentation\pages\carburant\commercial-carburant.component.ts` (ts)
- `app\domains\commercial\presentation\pages\classement\commercial-classement.component.css` (css)
- `app\domains\commercial\presentation\pages\classement\commercial-classement.component.html` (html)
- `app\domains\commercial\presentation\pages\classement\commercial-classement.component.ts` (ts)
- `app\domains\commercial\presentation\pages\fiche-synthese\fiche-synthese.component.css` (css)
- `app\domains\commercial\presentation\pages\fiche-synthese\fiche-synthese.component.html` (html)
- `app\domains\commercial\presentation\pages\fiche-synthese\fiche-synthese.component.ts` (ts)
- `app\domains\commercial\presentation\pages\precommandes\commercial-precommandes.component.css` (css)
- `app\domains\commercial\presentation\pages\precommandes\commercial-precommandes.component.html` (html)
- `app\domains\commercial\presentation\pages\precommandes\commercial-precommandes.component.ts` (ts)
- `app\domains\commercial\presentation\pages\prospects\commercial-prospects.component.css` (css)
- `app\domains\commercial\presentation\pages\prospects\commercial-prospects.component.html` (html)
- `app\domains\commercial\presentation\pages\prospects\commercial-prospects.component.ts` (ts)
- `app\domains\commercial\presentation\pages\recouvrement\commercial-recouvrement.component.css` (css)
- `app\domains\commercial\presentation\pages\recouvrement\commercial-recouvrement.component.html` (html)
- `app\domains\commercial\presentation\pages\recouvrement\commercial-recouvrement.component.ts` (ts)
- `app\domains\commercial\presentation\pages\ventes\commercial-ventes.component.css` (css)
- `app\domains\commercial\presentation\pages\ventes\commercial-ventes.component.html` (html)
- `app\domains\commercial\presentation\pages\ventes\commercial-ventes.component.ts` (ts)
- `app\domains\commercial\presentation\pages\versements\commercial-versements.component.css` (css)
- `app\domains\commercial\presentation\pages\versements\commercial-versements.component.html` (html)
- `app\domains\commercial\presentation\pages\versements\commercial-versements.component.ts` (ts)

### Domaine `comptable`

- `app\domains\comptable\infrastructure\comptable.service.ts` (ts)
- `app\domains\comptable\presentation\comptable.routes.ts` (ts)
- `app\domains\comptable\presentation\pages\caisse\comptable-caisse.component.css` (css)
- `app\domains\comptable\presentation\pages\caisse\comptable-caisse.component.html` (html)
- `app\domains\comptable\presentation\pages\caisse\comptable-caisse.component.ts` (ts)
- `app\domains\comptable\presentation\pages\commandes\comptable-commandes.component.css` (css)
- `app\domains\comptable\presentation\pages\commandes\comptable-commandes.component.html` (html)
- `app\domains\comptable\presentation\pages\commandes\comptable-commandes.component.ts` (ts)
- `app\domains\comptable\presentation\pages\dashboard\comptable-dashboard.component.css` (css)
- `app\domains\comptable\presentation\pages\dashboard\comptable-dashboard.component.html` (html)
- `app\domains\comptable\presentation\pages\dashboard\comptable-dashboard.component.ts` (ts)
- `app\domains\comptable\presentation\pages\factures\comptable-factures.component.css` (css)
- `app\domains\comptable\presentation\pages\factures\comptable-factures.component.html` (html)
- `app\domains\comptable\presentation\pages\factures\comptable-factures.component.ts` (ts)
- `app\domains\comptable\presentation\pages\objectifs\comptable-objectifs.component.css` (css)
- `app\domains\comptable\presentation\pages\objectifs\comptable-objectifs.component.html` (html)
- `app\domains\comptable\presentation\pages\objectifs\comptable-objectifs.component.ts` (ts)
- `app\domains\comptable\presentation\pages\recouvrement\comptable-recouvrement.component.css` (css)
- `app\domains\comptable\presentation\pages\recouvrement\comptable-recouvrement.component.html` (html)
- `app\domains\comptable\presentation\pages\recouvrement\comptable-recouvrement.component.ts` (ts)
- `app\domains\comptable\presentation\pages\reporting\comptable-reporting.component.css` (css)
- `app\domains\comptable\presentation\pages\reporting\comptable-reporting.component.html` (html)
- `app\domains\comptable\presentation\pages\reporting\comptable-reporting.component.ts` (ts)

### Domaine `controle`

- `app\domains\controle\infrastructure\controle.service.ts` (ts)
- `app\domains\controle\presentation\controle.routes.ts` (ts)
- `app\domains\controle\presentation\pages\budget\controle-budget.component.html` (html)
- `app\domains\controle\presentation\pages\budget\controle-budget.component.ts` (ts)
- `app\domains\controle\presentation\pages\credits\controle-credits.component.html` (html)
- `app\domains\controle\presentation\pages\credits\controle-credits.component.ts` (ts)
- `app\domains\controle\presentation\pages\dashboard\controle-dashboard.component.html` (html)
- `app\domains\controle\presentation\pages\dashboard\controle-dashboard.component.ts` (ts)
- `app\domains\controle\presentation\pages\marges\controle-marges.component.html` (html)
- `app\domains\controle\presentation\pages\marges\controle-marges.component.ts` (ts)
- `app\domains\controle\presentation\pages\valorisation\controle-valorisation.component.html` (html)
- `app\domains\controle\presentation\pages\valorisation\controle-valorisation.component.ts` (ts)

### Domaine `core`

- `app\core\audit\audit.service.ts` (ts)
- `app\core\auth\auth.guard.ts` (ts)
- `app\core\auth\auth.service.ts` (ts)
- `app\core\auth\role.guard.ts` (ts)
- `app\core\http\api-error-parser.ts` (ts)
- `app\core\http\api.service.ts` (ts)
- `app\core\http\auth.interceptor.ts` (ts)
- `app\core\models\api-response.model.ts` (ts)
- `app\core\models\auth.models.ts` (ts)
- `app\core\models\page-response.model.ts` (ts)
- `app\core\models\user.models.ts` (ts)
- `app\core\services\document.service.ts` (ts)

### Domaine `dg`

- `app\domains\dg\infrastructure\dg.service.ts` (ts)
- `app\domains\dg\presentation\dg.routes.ts` (ts)
- `app\domains\dg\presentation\pages\arbitrage\dg-arbitrage.component.css` (css)
- `app\domains\dg\presentation\pages\arbitrage\dg-arbitrage.component.html` (html)
- `app\domains\dg\presentation\pages\arbitrage\dg-arbitrage.component.ts` (ts)
- `app\domains\dg\presentation\pages\classement\dg-classement.component.css` (css)
- `app\domains\dg\presentation\pages\classement\dg-classement.component.html` (html)
- `app\domains\dg\presentation\pages\classement\dg-classement.component.ts` (ts)
- `app\domains\dg\presentation\pages\dashboard\dg-dashboard.component.css` (css)
- `app\domains\dg\presentation\pages\dashboard\dg-dashboard.component.html` (html)
- `app\domains\dg\presentation\pages\dashboard\dg-dashboard.component.ts` (ts)
- `app\domains\dg\presentation\pages\decaissements\dg-decaissements.component.css` (css)
- `app\domains\dg\presentation\pages\decaissements\dg-decaissements.component.html` (html)
- `app\domains\dg\presentation\pages\decaissements\dg-decaissements.component.ts` (ts)
- `app\domains\dg\presentation\pages\documents\dg-documents.component.css` (css)
- `app\domains\dg\presentation\pages\documents\dg-documents.component.html` (html)
- `app\domains\dg\presentation\pages\documents\dg-documents.component.ts` (ts)
- `app\domains\dg\presentation\pages\objectifs\dg-objectifs.component.css` (css)
- `app\domains\dg\presentation\pages\objectifs\dg-objectifs.component.html` (html)
- `app\domains\dg\presentation\pages\objectifs\dg-objectifs.component.ts` (ts)
- `app\domains\dg\presentation\pages\reporting\dg-reporting.component.css` (css)
- `app\domains\dg\presentation\pages\reporting\dg-reporting.component.html` (html)
- `app\domains\dg\presentation\pages\reporting\dg-reporting.component.ts` (ts)
- `app\domains\dg\presentation\pages\sync\dg-sync.component.css` (css)
- `app\domains\dg\presentation\pages\sync\dg-sync.component.html` (html)
- `app\domains\dg\presentation\pages\sync\dg-sync.component.ts` (ts)

### Domaine `layout`

- `app\layout\commercial-layout\commercial-layout.component.html` (html)
- `app\layout\commercial-layout\commercial-layout.component.ts` (ts)
- `app\layout\comptable-layout\comptable-layout.component.css` (css)
- `app\layout\comptable-layout\comptable-layout.component.html` (html)
- `app\layout\comptable-layout\comptable-layout.component.ts` (ts)
- `app\layout\controle-layout\controle-layout.component.html` (html)
- `app\layout\controle-layout\controle-layout.component.ts` (ts)
- `app\layout\dg-layout\dg-layout.component.css` (css)
- `app\layout\dg-layout\dg-layout.component.html` (html)
- `app\layout\dg-layout\dg-layout.component.ts` (ts)
- `app\layout\production-layout\production-layout.component.html` (html)
- `app\layout\production-layout\production-layout.component.ts` (ts)
- `app\layout\rp-layout\rp-layout.component.html` (html)
- `app\layout\rp-layout\rp-layout.component.ts` (ts)
- `app\layout\secretaire-layout\secretaire-layout.component.css` (css)
- `app\layout\secretaire-layout\secretaire-layout.component.html` (html)
- `app\layout\secretaire-layout\secretaire-layout.component.ts` (ts)
- `app\layout\stock-layout\stock-layout.component.css` (css)
- `app\layout\stock-layout\stock-layout.component.html` (html)
- `app\layout\stock-layout\stock-layout.component.ts` (ts)

### Domaine `pages`

- `app\pages\auth\auth.routes.ts` (ts)
- `app\pages\auth\forbidden\forbidden.component.ts` (ts)
- `app\pages\auth\forgot-password\forgot-password.component.css` (css)
- `app\pages\auth\forgot-password\forgot-password.component.html` (html)
- `app\pages\auth\forgot-password\forgot-password.component.ts` (ts)
- `app\pages\auth\login\login.component.css` (css)
- `app\pages\auth\login\login.component.html` (html)
- `app\pages\auth\login\login.component.ts` (ts)
- `app\pages\auth\reset-password\reset-password.component.css` (css)
- `app\pages\auth\reset-password\reset-password.component.html` (html)
- `app\pages\auth\reset-password\reset-password.component.ts` (ts)
- `app\pages\auth\two-fa\two-fa.component.css` (css)
- `app\pages\auth\two-fa\two-fa.component.html` (html)
- `app\pages\auth\two-fa\two-fa.component.ts` (ts)
- `app\pages\unauthorized\unauthorized.component.css` (css)
- `app\pages\unauthorized\unauthorized.component.html` (html)
- `app\pages\unauthorized\unauthorized.component.ts` (ts)

### Domaine `production`

- `app\domains\production\infrastructure\production.service.ts` (ts)
- `app\domains\production\presentation\pages\agent-doseur\agent-doseur.component.css` (css)
- `app\domains\production\presentation\pages\agent-doseur\agent-doseur.component.html` (html)
- `app\domains\production\presentation\pages\agent-doseur\agent-doseur.component.ts` (ts)
- `app\domains\production\presentation\pages\chef-machiniste\chef-machiniste.component.css` (css)
- `app\domains\production\presentation\pages\chef-machiniste\chef-machiniste.component.html` (html)
- `app\domains\production\presentation\pages\chef-machiniste\chef-machiniste.component.ts` (ts)
- `app\domains\production\presentation\pages\dashboard\production-dashboard.component.css` (css)
- `app\domains\production\presentation\pages\dashboard\production-dashboard.component.html` (html)
- `app\domains\production\presentation\pages\dashboard\production-dashboard.component.ts` (ts)
- `app\domains\production\presentation\pages\employes\production-employes.component.css` (css)
- `app\domains\production\presentation\pages\employes\production-employes.component.html` (html)
- `app\domains\production\presentation\pages\employes\production-employes.component.ts` (ts)
- `app\domains\production\presentation\pages\lots\production-lots.component.css` (css)
- `app\domains\production\presentation\pages\lots\production-lots.component.html` (html)
- `app\domains\production\presentation\pages\lots\production-lots.component.ts` (ts)
- `app\domains\production\presentation\pages\plan\production-plan.component.css` (css)
- `app\domains\production\presentation\pages\plan\production-plan.component.html` (html)
- `app\domains\production\presentation\pages\plan\production-plan.component.ts` (ts)
- `app\domains\production\presentation\pages\responsable-salle\responsable-salle.component.css` (css)
- `app\domains\production\presentation\pages\responsable-salle\responsable-salle.component.html` (html)
- `app\domains\production\presentation\pages\responsable-salle\responsable-salle.component.ts` (ts)
- `app\domains\production\presentation\pages\saisie\production-saisie.component.css` (css)
- `app\domains\production\presentation\pages\saisie\production-saisie.component.html` (html)
- `app\domains\production\presentation\pages\saisie\production-saisie.component.ts` (ts)
- `app\domains\production\presentation\production.routes.ts` (ts)

### Domaine `rp`

- `app\domains\rp\infrastructure\rp.service.ts` (ts)
- `app\domains\rp\presentation\pages\dashboard\rp-dashboard.component.html` (html)
- `app\domains\rp\presentation\pages\dashboard\rp-dashboard.component.ts` (ts)
- `app\domains\rp\presentation\pages\grands-comptes\rp-grands-comptes.component.html` (html)
- `app\domains\rp\presentation\pages\grands-comptes\rp-grands-comptes.component.ts` (ts)
- `app\domains\rp\presentation\pages\marketing\rp-marketing.component.html` (html)
- `app\domains\rp\presentation\pages\marketing\rp-marketing.component.ts` (ts)
- `app\domains\rp\presentation\pages\zones\rp-zones.component.html` (html)
- `app\domains\rp\presentation\pages\zones\rp-zones.component.ts` (ts)
- `app\domains\rp\presentation\rp.routes.ts` (ts)

### Domaine `secretaire`

- `app\domains\secretaire\infrastructure\secretaire.service.ts` (ts)
- `app\domains\secretaire\presentation\pages\commandes\secretaire-commandes.component.css` (css)
- `app\domains\secretaire\presentation\pages\commandes\secretaire-commandes.component.html` (html)
- `app\domains\secretaire\presentation\pages\commandes\secretaire-commandes.component.ts` (ts)
- `app\domains\secretaire\presentation\pages\dashboard\secretaire-dashboard.component.css` (css)
- `app\domains\secretaire\presentation\pages\dashboard\secretaire-dashboard.component.html` (html)
- `app\domains\secretaire\presentation\pages\dashboard\secretaire-dashboard.component.ts` (ts)
- `app\domains\secretaire\presentation\pages\session\secretaire-session.component.css` (css)
- `app\domains\secretaire\presentation\pages\session\secretaire-session.component.html` (html)
- `app\domains\secretaire\presentation\pages\session\secretaire-session.component.ts` (ts)
- `app\domains\secretaire\presentation\pages\validation\secretaire-validation.component.css` (css)
- `app\domains\secretaire\presentation\pages\validation\secretaire-validation.component.html` (html)
- `app\domains\secretaire\presentation\pages\validation\secretaire-validation.component.ts` (ts)
- `app\domains\secretaire\presentation\pages\versements\secretaire-versements.component.css` (css)
- `app\domains\secretaire\presentation\pages\versements\secretaire-versements.component.html` (html)
- `app\domains\secretaire\presentation\pages\versements\secretaire-versements.component.ts` (ts)
- `app\domains\secretaire\presentation\secretaire.routes.ts` (ts)

### Domaine `shared`

- `app\domains\shared\infrastructure\document.service.ts` (ts)
- `app\shared\data\mock-erp.store.ts` (ts)
- `app\shared\preloader\preloader.component.css` (css)
- `app\shared\preloader\preloader.component.html` (html)
- `app\shared\preloader\preloader.component.ts` (ts)
- `app\shared\shared.module.ts` (ts)
- `app\shared\ui\toast\toast-container.component.ts` (ts)
- `app\shared\ui\toast\toast.service.ts` (ts)
- `app\shared\utils\format.utils.ts` (ts)

### Domaine `stock`

- `app\domains\stock\application\use-cases\add-products.ts` (ts)
- `app\domains\stock\application\use-cases\commercial\commercial.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\dashboard\get-alertes.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\dashboard\get-dashboard-stats.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\dashboard\get-dashboard.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\get-products.ts` (ts)
- `app\domains\stock\application\use-cases\inventaire\ajuster-stock.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\inventaire\get-produits.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\inventaire\stock-level.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\inventaire\transfert-tampon.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\mouvement\get-mouvements.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\orders\internal-order.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\production\get-production.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\production\production-batch.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\reception\create-reception.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\reception\get-fournisseurs.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\reception\get-receptions.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\reception\receipt.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\reception\validate-reception.use-case.ts` (ts)
- `app\domains\stock\application\use-cases\update-stock.ts` (ts)
- `app\domains\stock\domain\models\product.models.ts` (ts)
- `app\domains\stock\domain\models\stock.models.ts` (ts)
- `app\domains\stock\domain\services\stock-rules.domain.service.ts` (ts)
- `app\domains\stock\domain\services\stock.domain.ts` (ts)
- `app\domains\stock\infrastructure\repositories\stock-api.repository.ts` (ts)
- `app\domains\stock\infrastructure\repositories\stock-mock.repository.ts` (ts)
- `app\domains\stock\infrastructure\stock.api.ts` (ts)
- `app\domains\stock\infrastructure\stock.repository.ts` (ts)
- `app\domains\stock\infrastructure\stock.service.ts` (ts)
- `app\domains\stock\presentation\pages\alertes\alertes.component.css` (css)
- `app\domains\stock\presentation\pages\alertes\alertes.component.html` (html)
- `app\domains\stock\presentation\pages\alertes\alertes.component.ts` (ts)
- `app\domains\stock\presentation\pages\commercial\commercial.component.css` (css)
- `app\domains\stock\presentation\pages\commercial\commercial.component.html` (html)
- `app\domains\stock\presentation\pages\commercial\commercial.component.ts` (ts)
- `app\domains\stock\presentation\pages\dashboard\stock-dashboard.component.css` (css)
- `app\domains\stock\presentation\pages\dashboard\stock-dashboard.component.html` (html)
- `app\domains\stock\presentation\pages\dashboard\stock-dashboard.component.ts` (ts)
- `app\domains\stock\presentation\pages\inventaire\inventaire.component.css` (css)
- `app\domains\stock\presentation\pages\inventaire\inventaire.component.html` (html)
- `app\domains\stock\presentation\pages\inventaire\inventaire.component.ts` (ts)
- `app\domains\stock\presentation\pages\mouvements\mouvements.component.css` (css)
- `app\domains\stock\presentation\pages\mouvements\mouvements.component.html` (html)
- `app\domains\stock\presentation\pages\mouvements\mouvements.component.ts` (ts)
- `app\domains\stock\presentation\pages\notifications\notifications.component.css` (css)
- `app\domains\stock\presentation\pages\notifications\notifications.component.html` (html)
- `app\domains\stock\presentation\pages\notifications\notifications.component.ts` (ts)
- `app\domains\stock\presentation\pages\production\production.component.css` (css)
- `app\domains\stock\presentation\pages\production\production.component.html` (html)
- `app\domains\stock\presentation\pages\production\production.component.ts` (ts)
- `app\domains\stock\presentation\pages\reception\reception-form.component.css` (css)
- `app\domains\stock\presentation\pages\reception\reception-form.component.html` (html)
- `app\domains\stock\presentation\pages\reception\reception-form.component.ts` (ts)
- `app\domains\stock\presentation\pages\reception\reception-list.component.css` (css)
- `app\domains\stock\presentation\pages\reception\reception-list.component.html` (html)
- `app\domains\stock\presentation\pages\reception\reception-list.component.ts` (ts)
- `app\domains\stock\presentation\pages\session\session.component.css` (css)
- `app\domains\stock\presentation\pages\session\session.component.html` (html)
- `app\domains\stock\presentation\pages\session\session.component.ts` (ts)
- `app\domains\stock\presentation\pages\settings\settings.component.css` (css)
- `app\domains\stock\presentation\pages\settings\settings.component.html` (html)
- `app\domains\stock\presentation\pages\settings\settings.component.ts` (ts)
- `app\domains\stock\presentation\pages\validation\validation.component.css` (css)
- `app\domains\stock\presentation\pages\validation\validation.component.html` (html)
- `app\domains\stock\presentation\pages\validation\validation.component.ts` (ts)
- `app\domains\stock\presentation\stock-routing.module.ts` (ts)
- `app\domains\stock\presentation\stock.routes.ts` (ts)
- `app\domains\stock\stock.module.ts` (ts)

### Domaine `sync`

- `app\domains\sync\infrastructure\sync.service.ts` (ts)

### Domaine `users`

- `app\domains\users\user.module.ts` (ts)

## Détails des fichiers frontend (TypeScript)

### Domaine `admin`

#### `app\domains\admin\infrastructure\admin.service.ts`

**interface** `UtilisateurBE`

- **Propriétés :**
  - `id: number`
  - `role: string`

**interface** `ParametresBE`

- **Propriétés :**
  - `tvaTaux: number`
  - `zones: string[]`

**class** `AdminService`

- **Propriétés :**
  - `prenom: string`
  - `telephone: string`
  - `dateNaissance: string`

- **Méthodes :**
  - `getUtilisateurs(role?: string): Observable<UtilisateurBE[]> `
  - `if(role && role !== 'TOUS'): void`
  - `activer(matricule: string): Observable<UtilisateurBE> `
  - `desactiver(matricule: string): Observable<UtilisateurBE> `
  - `changerRole(matricule: string, role: string): Observable<UtilisateurBE> `
  - `debloquer(matricule: string): Observable<UtilisateurBE> `
  - `getParametres(): Observable<ParametresBE> `
  - `modifierParametres(tvaTaux: number, primeMontant: number, alerteStockSeuil: number): Observable<ParametresBE> `
  - `ajouterReferentiel(categorie: string, valeur: string): Observable<ParametresBE> `
  - `supprimerReferentiel(categorie: string, valeur: string): Observable<ParametresBE> `


#### `app\domains\admin\presentation\pages\audit\admin-audit.component.ts`

**class** `AdminAuditComponent` extends/implements `OnInit `

- **Propriétés :**
  - `INFO: 'badge bg-neutral',`
  - `IMPORTANT: 'badge bg-orange',`
  - `ALERTE: 'badge bg-red',`

- **Méthodes :**
  - `ngOnInit(): void `
  - `niveauClass(n: string): string `


#### `app\domains\admin\presentation\pages\parametres\admin-parametres.component.ts`

**class** `AdminParametresComponent` extends/implements `OnInit `

- **Propriétés :**
  - `tvaTaux: 0,`
  - `primeMontant: 0,`
  - `alerteStockSeuil: 0,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `sauvegarder(): void `
  - `setTimeout((): void`
  - `onFieldChange(): void `
  - `addZone(): void `
  - `if(!z): void`
  - `removeZone(z: string): void `
  - `addGamme(): void `
  - `if(!g): void`
  - `removeGamme(g: string): void `
  - `addVille(): void `
  - `if(!v): void`
  - `removeVille(v: string): void `


#### `app\domains\admin\presentation\pages\utilisateurs\admin-utilisateurs.component.ts`

**class** `AdminUtilisateursComponent` extends/implements `OnInit `

- **Propriétés :**
  - `id: u.matricule,`
  - `matricule: u.matricule,`
  - `nom: u.nomComplet,`
  - `prenom: null,`
  - `email: u.email,`
  - `role: u.role,`
  - `actif: u.actif,`
  - `terminalImei: null,`
  - `derniereConnexion: null,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `getRoleLabel(role: string): string `
  - `return(ROLE_LABELS as Record<string, string>): void`
  - `toggleActif(matricule: string): void `
  - `if(!user): void`
  - `mapU(u: UtilisateurBE): UserUI `


### Domaine `app`

#### `app\app.component.ts`

**class** `AppComponent`


### Domaine `commercial`

#### `app\domains\commercial\domain\models\vente.models.ts`

**interface** `LigneVenteBE`

- **Propriétés :**
  - `codeProduit: string`
  - `designation: string`
  - `conditionnement: string`
  - `quantite: number`
  - `prixUnitaire: number`
  - `remise: number`
  - `referencePrixExceptionnel: string`

**interface** `VenteBE`

- **Propriétés :**
  - `id: number`
  - `numeroVente: string`
  - `matriculeCommercial: string`
  - `codeClient: string`
  - `date: string`
  - `typeVente: TypeVente`
  - `modeReglement: ModeReglement`
  - `uuidVente: string`
  - `coordonneesGPS: string`
  - `idFeuillRoute: string`
  - `signatureClient: boolean`
  - `statut: string`
  - `montantHT: number`
  - `montantTVA: number`
  - `montantTTC: number`
  - `lignes: LigneVenteBE[]`
  - `dateCreation: string`
  - `dateFinalisation: string`

**interface** `CreerVenteRequest`

- **Propriétés :**
  - `matriculeCommercial: string`
  - `codeClient: string`
  - `date: string`
  - `modeReglement: ModeReglement`
  - `typeVente: TypeVente`
  - `uuidVente: string`
  - `coordonneesGPS: string`
  - `idFeuillRoute: string`
  - `signatureClient: boolean`
  - `lignes: LigneVenteBE[]`

**interface** `NonMarchandeRequest`

- **Propriétés :**
  - `typeVente: TypeVente`
  - `matriculeCommercial: string`
  - `codeClient: string`
  - `codeProduit: string`
  - `designationProduit: string`
  - `conditionnement: string`
  - `quantite: number`
  - `date: string`
  - `motif: string`
  - `pourcentageLot: number`
  - `photoJustificative: string`

**interface** `AvoirBE`

- **Propriétés :**
  - `id: number`
  - `numeroAvoir: string`
  - `numeroVenteOriginale: string`
  - `matriculeCommercial: string`
  - `codeClient: string`
  - `matriculeComptable: string`
  - `montantHT: number`
  - `montantTVA: number`
  - `montantTTC: number`
  - `motif: string`
  - `dateAvoir: string`
  - `statut: string`
  - `dateCreation: string`
  - `dateValidation: string`

**interface** `CreerAvoirRequest`

- **Propriétés :**
  - `matriculeComptable: string`
  - `motif: string`


#### `app\domains\commercial\infrastructure\commercial.service.ts`

**interface** `ClientBE`

- **Propriétés :**
  - `id: number`
  - `adresse: string`
  - `matriculeCommercialReferent: string`

**interface** `LignePCBE`

- **Propriétés :**
  - `codeProduit: string`
  - `quantiteDemandee: number`

**interface** `PreCommandeBE`

- **Propriétés :**
  - `id: number`
  - `dateSoumission: string`
  - `matriculeValidateurSecretaire: string`
  - `motifRefus: string`

**interface** `RecouvrementBE`

- **Propriétés :**
  - `id: number`
  - `montantDu: number`
  - `dateEcheance: string`

**interface** `CarburantBE`

- **Propriétés :**
  - `id: number`
  - `immatriculation: string`
  - `litresDemandes: number`
  - `date: string`
  - `signatureSuperviseur: string`
  - `motifRefus: string`
  - `caZoneMoisCourantFCFA: number`

**interface** `VenteBE`

- **Propriétés :**
  - `id: number`
  - `codeClient: string`
  - `typeVente: string`
  - `lignes: LigneVenteBE[]`

**interface** `LigneVenteBE`

- **Propriétés :**
  - `codeProduit: string`
  - `quantite: number`
  - `montantHT: number`

**interface** `AvoirBE`

- **Propriétés :**
  - `id: number`
  - `matriculeCommercial: string`
  - `montantHT: number`
  - `motif: string`
  - `dateCreation: string`

**interface** `FicheSyntheseBE`

- **Propriétés :**
  - `date: string`
  - `caYaounde: number`
  - `effectifCommerciaux: number`
  - `vacInit: number`
  - `recouvrements: number`
  - `cumulMensuelCA: number`
  - `depensesJustifiees: number`
  - `sCaisse: number`
  - `lignes: LigneFicheSyntheseBE[]`

**interface** `LigneFicheSyntheseBE`

- **Propriétés :**
  - `codeProduit: string`
  - `stockCamion: number`
  - `stockFinal: number`

**class** `CommercialService`

- **Propriétés :**
  - `nom: string`
  - `localite: string`
  - `matriculeCommercial: string`
  - `lignes: { codeProduit: string`
  - `matriculeCommercial: string`
  - `stationPartenaire: string`
  - `litresDemandes: number`
  - `photoCompteur: string`
  - `signatureCommercial: string`
  - `typeVente: string`
  - `codeProduit: string`
  - `quantite: number`
  - `pourcentageLot: number`
  - `matriculeComptable: string`

- **Méthodes :**
  - `getClients(commercial?: string): Observable<ClientBE[]> `
  - `if(commercial): void`
  - `getPrecommandesAValider(page?: PageParams): Observable<PageResponse<PreCommandeBE>> `
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `getPrecommandesParDate(date: string, page?: PageParams): Observable<PageResponse<PreCommandeBE>> `
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `validerSecretaire(numero: string, matricule: string): Observable<PreCommandeBE> `
  - `validerComptable(numero: string, matricule: string): Observable<PreCommandeBE> `
  - `getRecouvrements(params: { commercial?: string; client?: string; statut?: string } = {}): Observable<RecouvrementBE[]> `
  - `if(params.commercial): void`
  - `if(params.client): void`
  - `if(params.statut): void`
  - `rembourserRecouvrement(reference: string, montant: number): Observable<RecouvrementBE> `
  - `getCarburant(params: { commercial?: string; statut?: string } = {}): Observable<CarburantBE[]> `
  - `if(params.commercial): void`
  - `if(params.statut): void`
  - `refuserCarburant(reference: string, motif: string): Observable<CarburantBE> `
  - `getVentes(debut: string, fin: string, page?: PageParams): Observable<PageResponse<VenteBE>> `
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `getVentesParCommercial(matricule: string, page?: PageParams): Observable<PageResponse<VenteBE>> `
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `finaliserVente(numero: string): Observable<VenteBE> `
  - `annulerVente(numero: string, motif: string): Observable<VenteBE> `
  - `validerAvoir(numeroAvoir: string): Observable<AvoirBE> `
  - `refuserPrecommande(numero: string, motif: string, matriculeValidateur: string): Observable<PreCommandeBE> `
  - `livrerPrecommande(numero: string): Observable<PreCommandeBE> `
  - `getFicheSynthese(date?: string): Observable<FicheSyntheseBE> `
  - `if(date): void`


#### `app\domains\commercial\presentation\pages\carburant\commercial-carburant.component.ts`

**class** `CommercialCarburantComponent` extends/implements `OnInit `

- **Propriétés :**
  - `matriculeCommercial: matricule,`
  - `immatriculation: f.immatriculation || 'NC',`
  - `stationPartenaire: f.station || 'NC',`
  - `kilometrage: f.kmCompteur,`
  - `litresDemandes: f.litresDemandes,`
  - `montant: f.montant,`
  - `next: c => {`
  - `id: c.referenceCarburant,`
  - `commercialId: c.matriculeCommercial,`
  - `immatriculation: c.immatriculation,`
  - `kmCompteur: c.kilometrage,`
  - `station: c.stationPartenaire,`
  - `litresDemandes: c.litresDemandes,`
  - `montant: c.montant,`
  - `superviseur: c.matriculeValidateur,`
  - `statut: c.statut,`
  - `signatureCommercial: !!c.signatureCommercial,`
  - `signatureSuperviseur: !!c.signatureSuperviseur,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `if(!matricule): void`
  - `soumettre(): void `
  - `if(f.kmCompteur <= 0 || f.litresDemandes <= 0): void`
  - `updateKmCompteur(v: number | string): void `
  - `updateLitresDemandes(v: number | string): void `
  - `updateStation(v: string): void `
  - `updateMontant(v: number | string): void `
  - `mapC(c: CarburantBE): CarburantUI `


#### `app\domains\commercial\presentation\pages\classement\commercial-classement.component.ts`

**class** `CommercialClassementComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `
  - `tauxRealisation(c: ClassementCommercialBE): number `
  - `isEligible(c: ClassementCommercialBE): boolean `


#### `app\domains\commercial\presentation\pages\fiche-synthese\fiche-synthese.component.ts`

**class** `FicheSyntheseComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `
  - `getProduit(code: string): ProduitUI | undefined `


#### `app\domains\commercial\presentation\pages\precommandes\commercial-precommandes.component.ts`

**class** `CommercialPrecommandesComponent` extends/implements `OnInit, OnDestroy `

- **Propriétés :**
  - `next: page => {`
  - `code: p.sku,`
  - `designation: p.designation ?? p.sku,`
  - `gamme: p.variant?.name ?? '—',`
  - `prixHT: p.unitPriceAmount,`
  - `unite: p.unit ?? 'u',`
  - `matriculeCommercial: matricule,`
  - `codeProduit: l.produitCode,`
  - `conditionnement: 'CARTON',`
  - `quantite: l.quantite,`
  - `next: pc => {`
  - `next: pc => {`
  - `next: pc => {`
  - `SOUMISE: 'Soumise',`
  - `VALIDEE_SECRETAIRE: 'Validée Secrétaire',`
  - `VALIDEE_COMPTABLE: 'Prête Chargement',`
  - `id: pc.numeroPreCommande,`
  - `commercialId: pc.matriculeCommercial,`
  - `statut: pc.statut,`
  - `produitCode: l.codeProduit,`
  - `designation: l.designationProduit,`
  - `quantite: l.quantiteDemandee,`
  - `signatureSecretaire: !!pc.matriculeValidateurSecretaire,`
  - `signatureComptable: !!pc.matriculeValidateurComptable,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `getProduit(code: string): ProduitUI | undefined `
  - `soumettre(): void `
  - `if(!valid.length): void`
  - `addLigne(): void `
  - `removeLigne(i: number): void `
  - `openRefus(numero: string): void `
  - `confirmerRefus(): void `
  - `if(this.refusSaving(): void`
  - `livrer(numero: string): void `
  - `showToast(msg: string, type: 'success'|'error'): void `
  - `setTimeout((): void`
  - `ngOnDestroy(): void `
  - `getStatutLabel(s: string): string `
  - `mapPC(pc: PreCommandeBE): PrecommandeUI `
  - `fmtDate(d: Date): string `


#### `app\domains\commercial\presentation\pages\prospects\commercial-prospects.component.ts`

**class** `CommercialProspectsComponent` extends/implements `OnInit `

- **Propriétés :**
  - `next: clients => {`
  - `nom: p.nomCommerce,`
  - `telephone: p.telephone,`
  - `adresse: '',`
  - `localite: '',`
  - `type: 'PROSPECT',`
  - `matriculeCommercialReferent: matricule,`
  - `next: client => {`
  - `id: c.codeClient,`
  - `nomCommerce: c.nom,`
  - `telephone: c.telephone,`
  - `typeEstime: c.type,`
  - `localite: c.localite,`
  - `statut: c.actif ? 'EN_ATTENTE_VALIDATION' : 'REFUSE',`
  - `zone: c.localite || null,`
  - `dateProspection: null,`
  - `notes: null,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `if(!matricule): void`
  - `soumettre(): void `
  - `if(!p.nomCommerce): void`
  - `mapClient(c: ClientBE): Prospect `


#### `app\domains\commercial\presentation\pages\recouvrement\commercial-recouvrement.component.ts`

**class** `CommercialRecouvrementComponent` extends/implements `OnInit `

- **Propriétés :**
  - `id: r.referenceRecouvrement,`
  - `clientId: r.codeClient,`
  - `clientNom: r.nomClient,`
  - `montantDette: r.montantDu,`
  - `montantEncaisse: r.montantRembourse,`
  - `soldeRestant: r.montantRestant,`
  - `commercialId: r.matriculeCommercial,`
  - `statut: r.statut,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `if(!matricule): void`
  - `getClientType(clientId: string): string `
  - `mapR(r: RecouvrementBE): RecouvrementUI `


#### `app\domains\commercial\presentation\pages\ventes\commercial-ventes.component.ts`

**class** `CommercialVentesComponent` extends/implements `OnInit, OnDestroy `

- **Propriétés :**
  - `next: v => {`
  - `typeVente: this.nmTypeVente,`
  - `matriculeCommercial: matricule,`
  - `codeClient: this.nmCodeClient,`
  - `codeProduit: this.nmCodeProduit,`
  - `designationProduit: this.nmDesignation,`
  - `conditionnement: this.nmConditionnement,`
  - `quantite: this.nmQuantite,`
  - `motif: this.nmMotif,`
  - `next: v => {`
  - `motif: this.avoirMotif,`
  - `next: a => {`
  - `EN_COURS: 'badge bg-orange', FINALISEE: 'badge bg-success', ANNULEE: 'badge bg-red',`

- **Méthodes :**
  - `ngOnInit(): void `
  - `chargerVentes(): void `
  - `finaliserVente(numero: string): void `
  - `openAnnuler(numero: string): void `
  - `confirmerAnnuler(): void `
  - `if(this.saving(): void`
  - `openNonMarchand(): void `
  - `confirmerNonMarchand(): void `
  - `if(this.saving(): void`
  - `openAvoir(numero: string): void `
  - `confirmerAvoir(): void `
  - `if(this.saving(): void`
  - `validerAvoir(numeroAvoir: string): void `
  - `statutClass(s: string): string `
  - `showToast(msg: string, type: 'success' | 'error'): void `
  - `setTimeout((): void`
  - `ngOnDestroy(): void `


#### `app\domains\commercial\presentation\pages\versements\commercial-versements.component.ts`

**class** `CommercialVersementsComponent` extends/implements `OnInit `

- **Propriétés :**
  - `matriculeCommercial: matricule,`
  - `next: v => {`
  - `id: v.referenceVersement,`
  - `commercialId: v.matriculeCommercial,`
  - `date: dateV,`
  - `montantAttendu: v.montantAttendu,`
  - `cashVerse: v.cashVerse,`
  - `ecart: v.ecart,`
  - `motifEcart: v.justificationEcart ?? null,`
  - `statut: v.statut,`
  - `typeVersement: v.typeVersement ?? null,`
  - `alerteRouge: v.alerteRouge,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `if(!matricule): void`
  - `submit(): void `
  - `if(this.montantVerse(): void`
  - `mapV(v: VersementBE): VersementUI `


### Domaine `comptable`

#### `app\domains\comptable\infrastructure\comptable.service.ts`

**interface** `VersementBE`

- **Propriétés :**
  - `id: number`
  - `montantAttendu: number`
  - `alerteRouge: boolean`
  - `statut: string`

**interface** `LigneFactureBE`

- **Propriétés :**
  - `referenceArticle: string`
  - `prixUnitaireHT: number`
  - `montantHT: number`

**interface** `FactureBE`

- **Propriétés :**
  - `id: number`
  - `typeFacture: string`
  - `dateEmission: string`
  - `montantTotalHT: number`
  - `lignes: LigneFactureBE[]`

**interface** `CaisseLigneBE`

- **Propriétés :**
  - `referenceOperation: string`
  - `motif: string`

**interface** `CaisseBE`

- **Propriétés :**
  - `id: number`
  - `totalEntrees: number`

**interface** `DecaissementBE`

- **Propriétés :**
  - `id: number`
  - `matriculeProposant: string`
  - `beneficiaire: string`
  - `statut: string`
  - `dateValidationDG: string`
  - `dateExecution: string`

**interface** `ObjectifCommercialBE`

- **Propriétés :**
  - `id: number`
  - `semaineDebut: string`
  - `objectifGlobal: number`
  - `objectifEaux: number`
  - `objectifSnacks: number`
  - `objectifConfiseries: number`
  - `matriculeDefinisseur: string`

**interface** `ReconciliationBE`

- **Propriétés :**
  - `matricule: string`
  - `caDeclare: number`
  - `resultatNet: number`

**interface** `DashboardComptabiliteBE`

- **Propriétés :**
  - `date: string`
  - `soldeCaisse: number`
  - `totalEntreesCaisse: number`
  - `totalSortiesCaisse: number`
  - `totalFacturesEmises: number`
  - `totalFacturesPayees: number`
  - `nbFacturesImpayees: number`
  - `totalDecaissementsExecutes: number`
  - `nbDecaissementsEnAttente: number`
  - `nbVersementsEnAttente: number`
  - `nbAlertesRouges: number`
  - `totalPrimesAVerser: number`

**class** `ComptableService`

- **Propriétés :**
  - `matriculeCommercial: string`
  - `typeVersement: string`
  - `codeClient: string`
  - `matriculeEmetteur: string`
  - `lignes: { referenceArticle: string`
  - `prixUnitaireHT: number`
  - `referenceOperation: string`
  - `referenceOperation: string`
  - `typeDepense: string`
  - `montantFCFA: number`
  - `dateDepense: string`
  - `matriculeCommercial: string`
  - `objectifGlobal: number`
  - `objectifEaux: number`
  - `objectifSnacks: number`
  - `objectifConfiseries: number`

- **Méthodes :**
  - `getDashboard(): Observable<DashboardComptabiliteBE> `
  - `getVersements(params: { commercial?: string; date?: string; statut?: string } = {}): Observable<VersementBE[]> `
  - `if(params.commercial): void`
  - `if(params.date): void`
  - `if(params.statut): void`
  - `getAlertes(): Observable<VersementBE[]> `
  - `validerVersement(reference: string, matricule: string, codeOtp: string): Observable<VersementBE> `
  - `justifierEcart(reference: string, justification: string): Observable<VersementBE> `
  - `getFactures(debut: string, fin: string, page?: PageParams): Observable<PageResponse<FactureBE>> `
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `consulterFacture(numero: string): Observable<FactureBE> `
  - `getFacturesParStatut(statut: string, page?: PageParams): Observable<PageResponse<FactureBE>> `
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `getFacturesClient(codeClient: string, page?: PageParams): Observable<PageResponse<FactureBE>> `
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `payerFacture(numero: string): Observable<FactureBE> `
  - `annulerFacture(numero: string): Observable<FactureBE> `
  - `getCaisse(date: string): Observable<CaisseBE | null> `
  - `ouvrirCaisse(req: { soldeInitial: number; date?: string }): Observable<CaisseBE> `
  - `getCaisseParPeriode(debut: string, fin: string): Observable<CaisseBE[]> `
  - `getDecaissements(): Observable<DecaissementBE[]> `
  - `getDecaissement(numero: string): Observable<DecaissementBE> `
  - `getDecaissementsParPeriode(debut: string, fin: string): Observable<DecaissementBE[]> `
  - `getDecaissementsParStatut(statut: string): Observable<DecaissementBE[]> `
  - `validerDecaissementDG(numero: string, matriculeDG: string): Observable<DecaissementBE> `
  - `approuverDecaissementDG(numero: string, matriculeDG: string): Observable<DecaissementBE> `
  - `executerDecaissement(numero: string, matriculeExecuteur: string): Observable<DecaissementBE> `
  - `annulerDecaissement(numero: string, motif: string, matricule: string): Observable<DecaissementBE> `
  - `getReconciliation(matricule: string, date: string): Observable<ReconciliationBE> `
  - `getObjectifsParCommercial(matricule: string): Observable<ObjectifCommercialBE[]> `
  - `getObjectifsParSemaine(semaineDebut: string): Observable<ObjectifCommercialBE[]> `


#### `app\domains\comptable\presentation\pages\caisse\comptable-caisse.component.ts`

**class** `ComptableCaisseComponent` extends/implements `OnInit, OnDestroy `

- **Propriétés :**
  - `mouvementType: 'ENTREE' | 'SORTIE' = 'ENTREE'`
  - `next: c => {`
  - `montant: this.montant,`
  - `motif: this.motif,`
  - `matriculeOperateur: matricule,`
  - `next: c => {`

- **Méthodes :**
  - `ngOnInit(): void `
  - `chargerCaisseJour(): void `
  - `chargerHistorique(): void `
  - `ouvrirCaisse(): void `
  - `if(this.saving(): void`
  - `enregistrerMouvement(): void `
  - `if(this.saving(): void`
  - `fCFA(n: number): string `
  - `showToast(msg: string, type: 'success' | 'error'): void `
  - `setTimeout((): void`
  - `ngOnDestroy(): void `


#### `app\domains\comptable\presentation\pages\commandes\comptable-commandes.component.ts`

**class** `ComptableCommandesComponent` extends/implements `OnInit `

- **Propriétés :**
  - `SOUMISE: 'Soumise',`
  - `VALIDEE_SECRETAIRE: 'Validée Secrétaire',`
  - `VALIDEE_COMPTABLE: 'Validée Comptable',`
  - `SOUMISE: 'badge bg-orange',`
  - `VALIDEE_SECRETAIRE: 'badge bg-neutral',`
  - `VALIDEE_COMPTABLE: 'badge bg-success',`
  - `id: pc.numeroPreCommande,`
  - `commercialId: pc.matriculeCommercial,`
  - `statut: pc.statut,`
  - `produitCode: l.codeProduit,`
  - `designation: l.designationProduit,`
  - `quantite: l.quantiteDemandee,`
  - `signatureSecretaire: !!pc.matriculeValidateurSecretaire,`
  - `signatureComptable: !!pc.matriculeValidateurComptable,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `validerCommande(id: string): void `
  - `getCommercialNom(id: string): string `
  - `getProduitDesignation(code: string): string `
  - `statutLabel(s: string): string `
  - `statutClass(s: string): string `
  - `mapPC(pc: PreCommandeBE): CommandeUI `


#### `app\domains\comptable\presentation\pages\dashboard\comptable-dashboard.component.ts`

**class** `ComptableDashboardComponent` extends/implements `OnInit `

- **Propriétés :**
  - `versements: VersementDashUI[] = []`
  - `factures: FactureDashUI[] = []`
  - `recouvrement: RecouvrementDashUI[] = []`
  - `precommandes: { id: string`
  - `charges: { categorie: string`
  - `variance: { poste: string`
  - `primes: { commercialId: string`
  - `sCaisse: dashboard.soldeCaisse,`
  - `totalEntrees: dashboard.totalEntreesCaisse,`
  - `totalSorties: dashboard.totalSortiesCaisse,`
  - `totalDecaissements: dashboard.totalDecaissementsExecutes,`
  - `nbDecaissementsEnAttente: dashboard.nbDecaissementsEnAttente,`
  - `nbFacturesImpayees: dashboard.nbFacturesImpayees,`
  - `nbVersementsEnAttente: dashboard.nbVersementsEnAttente,`
  - `nbAlertesRouges: dashboard.nbAlertesRouges,`
  - `nbPrimesValidees: dashboard.nbPrimesValideesNonVersées,`
  - `totalPrimesAVerser: dashboard.totalPrimesAVerser,`
  - `id: v.id,`
  - `commercialId: v.matriculeCommercial,`
  - `cashVerse: v.cashVerse,`
  - `typeVersement: v.typeVersement ?? null,`
  - `statut: v.statut,`
  - `alerteRouge: v.alerteRouge,`
  - `id: f.id,`
  - `numero: f.numeroFacture,`
  - `clientNom: f.codeClient,`
  - `type: f.typeFacture,`
  - `montantHT: f.montantTotalHT,`
  - `montantTTC: f.montantTotalTTC,`
  - `statut: f.statut,`
  - `montantDu: r.montantDu,`
  - `montantRembourse: r.montantRembourse,`
  - `statut: r.statut,`

- **Méthodes :**
  - `totalVersements(): number `
  - `versementsValides(): number `
  - `versementsEnAttente(): number `
  - `totalFacture(): number `
  - `facturesEmises(): number `
  - `caFactureReel(): number `
  - `totalDettes(): number `
  - `totalEncaisse(): number `
  - `dossierActifs(): number `
  - `totalCharges(): number `
  - `resultat(): number `
  - `margeNette(): number `
  - `dso(): number `
  - `totalPrimes(): number `
  - `variancePct(v: { standard: number; reel: number }): number `
  - `ngOnInit(): void `


#### `app\domains\comptable\presentation\pages\factures\comptable-factures.component.ts`

**class** `ComptableFacturesComponent` extends/implements `OnInit `

- **Propriétés :**
  - `codeClient: this.formCodeClient,`
  - `typeFacture: this.formTypeFacture,`
  - `matriculeEmetteur: 'CURRENT_USER',`
  - `next: f => {`
  - `EMISE: 'badge bg-orange', PAYEE: 'badge bg-success', ANNULEE: 'badge bg-red',`
  - `numero: f.numeroFacture,`
  - `codeClient: f.codeClient,`
  - `clientNom: f.codeClient,`
  - `type: f.typeFacture,`
  - `dateEmission: d,`
  - `dateFacture: d,`
  - `statut: f.statut,`
  - `montantHT: f.montantTotalHT,`
  - `montantTVA: f.montantTotalTVA,`
  - `tva: f.montantTotalTVA,`
  - `montantTTC: f.montantTotalTTC,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `ajouterLigne(): void `
  - `supprimerLigne(i: number): void `
  - `emettreFacture(): void `
  - `if(this.formSaving(): void`
  - `showToast(msg: string, type: 'success' | 'error'): void `
  - `setTimeout((): void`
  - `payer(numero: string): void `
  - `annuler(numero: string): void `
  - `statutClass(s: string): string `
  - `typeClass(t: string): string `
  - `mapF(f: FactureBE): FactureUI `
  - `fmtDate(d: Date): string `


#### `app\domains\comptable\presentation\pages\objectifs\comptable-objectifs.component.ts`

**class** `ComptableObjectifsComponent` extends/implements `OnInit `

- **Méthodes :**
  - `tauxAtteinte(this.totalCA(): void`
  - `ngOnInit(): void `
  - `progressClass(ca: number, obj: number): string `
  - `if(t >= 100): void`
  - `if(t >= 75): void`
  - `statutClass(ca: number, obj: number): string `
  - `cappedTaux(ca: number, obj: number): number `


#### `app\domains\comptable\presentation\pages\recouvrement\comptable-recouvrement.component.ts`

**class** `ComptableRecouvrementComponent` extends/implements `OnInit `

- **Propriétés :**
  - `APURE: 'badge bg-success',`
  - `PARTIEL: 'badge bg-orange',`
  - `EN_ATTENTE: 'badge bg-red',`
  - `id: r.referenceRecouvrement,`
  - `clientId: r.codeClient,`
  - `clientNom: r.nomClient,`
  - `montantDette: r.montantDu,`
  - `montantEncaisse: r.montantRembourse,`
  - `soldeRestant: r.montantRestant,`
  - `commercialId: r.matriculeCommercial,`
  - `statut: r.statut,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `rembourser(id: string, montant: number): void `
  - `getCommercialNom(id: string): string `
  - `tauxRecouvrement(): number `
  - `if(total === 0): void`
  - `statutClass(s: string): string `
  - `mapR(r: RecouvrementBE): RecouvrementUI `


#### `app\domains\comptable\presentation\pages\reporting\comptable-reporting.component.ts`

**class** `ComptableReportingComponent` extends/implements `OnInit `

- **Méthodes :**
  - `if(prev === 0): void`
  - `ngOnInit(): void `
  - `getFactureTTC(f: FactureBE): number `


### Domaine `controle`

#### `app\domains\controle\infrastructure\controle.service.ts`

**interface** `ControleDashboard`

- **Propriétés :**
  - `tresorerie: number`
  - `ecartsRouges: number`
  - `ventesCredit: number`
  - `margeMoyenne: number`
  - `valoStock: number`
  - `budgetEngage: number`
  - `budgetTotal: number`
  - `budgetDepasse: number`
  - `decaissementsAttente: number`
  - `anomaliesElevees: number`
  - `marges: MargeProduit[]`
  - `budget: BudgetItem[]`
  - `variances: VarianceItem[]`

**interface** `MargeProduit`

- **Propriétés :**
  - `code: string`
  - `designation: string`
  - `prixVente: number`
  - `coutRevient: number`
  - `marge: number`
  - `margePct: number`
  - `margeTotale: number`
  - `ventesMois: number`

**interface** `BudgetItem`

- **Propriétés :**
  - `poste: string`
  - `budget: number`
  - `engage: number`
  - `realise: number`

**interface** `VarianceItem`

- **Propriétés :**
  - `poste: string`
  - `standard: number`
  - `reel: number`
  - `unite: string`

**class** `ControleService`

- **Méthodes :**
  - `getDashboard(): Observable<ControleDashboard> `
  - `getMarges(): Observable<MargeProduit[]> `
  - `getBudget(): Observable<BudgetItem[]> `
  - `getVariances(): Observable<VarianceItem[]> `


#### `app\domains\controle\presentation\pages\budget\controle-budget.component.ts`

**class** `ControleBudgetComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `
  - `pct(engage: number, budget: number): number `


#### `app\domains\controle\presentation\pages\credits\controle-credits.component.ts`

**class** `ControleCreditsComponent` extends/implements `OnInit `

- **Propriétés :**
  - `nom: com.nomComplet,`
  - `zone: '—',`

- **Méthodes :**
  - `commercialNom(mat: string): string `
  - `ngOnInit(): void `


#### `app\domains\controle\presentation\pages\dashboard\controle-dashboard.component.ts`

**class** `ControleDashboardComponent` extends/implements `OnInit `

- **Propriétés :**
  - `next: d => {`

- **Méthodes :**
  - `ngOnInit(): void `
  - `budgetPct(b: { engage: number; budget: number }): number `
  - `variancePct(v: { standard: number; reel: number }): number `


#### `app\domains\controle\presentation\pages\marges\controle-marges.component.ts`

**class** `ControleMargesComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `


#### `app\domains\controle\presentation\pages\valorisation\controle-valorisation.component.ts`

**class** `ControleValorisationComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `


### Domaine `core`

#### `app\core\audit\audit.service.ts`

**interface** `JournalAuditBE`

- **Propriétés :**
  - `id: number`
  - `matricule: string`
  - `role: string`
  - `typeAction: string`
  - `module: string`
  - `ressource: string`
  - `referenceEntite: string`
  - `methodeHttp: string`
  - `endpoint: string`
  - `details: string`
  - `succes: boolean`
  - `messageErreur: string`
  - `adresseIp: string`
  - `dureeMs: number`
  - `horodatage: string`

**interface** `ActiviteSuspecteBE`

- **Propriétés :**
  - `matricule: string`
  - `ressource: string`
  - `occurrences: number`

**class** `AuditService`

- **Méthodes :**
  - `pageParamsToRecord(page?: PageParams): Record<string, string> `
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `getAuditParUtilisateur(matricule: string, page?: PageParams): Observable<PageResponse<JournalAuditBE>> `
  - `getAuditParPeriode(debut: string, fin: string, page?: PageParams): Observable<PageResponse<JournalAuditBE>> `
  - `getAuditParModule(module: string, page?: PageParams): Observable<PageResponse<JournalAuditBE>> `
  - `getAuditParType(typeAction: string, page?: PageParams): Observable<PageResponse<JournalAuditBE>> `
  - `getAuditEchecs(page?: PageParams): Observable<PageResponse<JournalAuditBE>> `
  - `getActivitesSuspectes(fenetreHeures: number = 24, seuil: number = 5): Observable<ActiviteSuspecteBE[]> `


#### `app\core\auth\auth.service.ts`

**interface** `SessionUser`

- **Propriétés :**
  - `matricule: string`
  - `nomComplet: string`
  - `role: UserRole`

**class** `AuthService`

- **Propriétés :**
  - `matricule: data.matricule,`
  - `nomComplet: data.nomComplet,`
  - `role: data.role,`

- **Méthodes :**
  - `login(credentials: LoginRequest): Observable<LoginResponse> `
  - `map((res): void`
  - `if(!res.succes || !data): void`
  - `requestOtp(matricule: string, raison: string = 'AUTHENTIFICATION'): Observable<`
  - `map((res): void`
  - `if(!res.succes || !res.donnees): void`
  - `verifyOtp(matricule: string, otpCode: string): Observable<LoginResponse> `
  - `map((res): void`
  - `if(!res.succes || !data): void`
  - `logout(): void `
  - `getToken(): string | null `
  - `if(token): void`
  - `hasAnyRole(roles: UserRole[]): boolean `
  - `persistSession(data: LoginResponse): void `
  - `readStoredUser(): SessionUser | null `
  - `if(!raw): void`


#### `app\core\http\api.service.ts`

**class** `ApiService`

- **Méthodes :**
  - `getBlob(url: string, params?: Record<string, string>): Observable<Blob> `


#### `app\core\models\auth.models.ts`

**interface** `LoginRequest`

- **Propriétés :**
  - `matricule: string`
  - `motDePasse: string`

**interface** `LoginResponse`

- **Propriétés :**
  - `token: string`
  - `matricule: string`
  - `nomComplet: string`
  - `role: UserRole`
  - `expirationMs: number`

**interface** `RegisterRequest`

- **Propriétés :**
  - `prenom: string`
  - `nom: string`
  - `email: string`
  - `motDePasse: string`
  - `role: UserRole`
  - `telephone: string`
  - `residence: string`
  - `cni: string`
  - `photo: string`
  - `dateNaissance: string`
  - `lieuNaissance: string`
  - `dateEmbauche: string`
  - `sexe: string`

**interface** `OtpRequest`

- **Propriétés :**
  - `matricule: string`
  - `raison: string`

**interface** `OtpVerifyRequest`

- **Propriétés :**
  - `matricule: string`
  - `codeOtp: string`


#### `app\core\models\page-response.model.ts`

**interface** `PageParams`

- **Propriétés :**
  - `page: number`
  - `size: number`
  - `sort: string`


#### `app\core\models\user.models.ts`

**interface** `AppUser`

- **Propriétés :**
  - `id: string`
  - `nom: string`
  - `prenom: string`
  - `email: string`
  - `role: UserRole`
  - `matricule: string`
  - `actif: boolean`
  - `terminalImei: string`


#### `app\core\services\document.service.ts`

**class** `DocumentService`

- **Propriétés :**
  - `params: { cle },`
  - `responseType: 'blob',`

- **Méthodes :**
  - `importerDocument(typeDocument: string, reference: string, file: File): Observable<string> `
  - `recupererDocument(cle: string): Observable<Blob> `


### Domaine `dg`

#### `app\domains\dg\infrastructure\dg.service.ts`

**interface** `KpisDgBE`

- **Propriétés :**
  - `caTotal: number`
  - `caMoisN1: number`
  - `caMoisN2: number`
  - `ventesCount: number`
  - `commerciauxActifs: number`
  - `objectifCible: number`
  - `objectifRealise: number`
  - `margeGlobale: number`
  - `tresorerie: number`
  - `rentabiliteProductionPct: number`
  - `stockMatieresPremieres: number`
  - `stockConsommables: number`
  - `stockProduitsFinis: number`

**interface** `ClassementCommercialBE`

- **Propriétés :**
  - `rang: number`
  - `matricule: string`
  - `nomComplet: string`
  - `caRealise: number`
  - `nbVentes: number`

**interface** `TrancheComparatifBE`

- **Propriétés :**
  - `debut: string`
  - `fin: string`
  - `caFCFA: number`
  - `nombreVentes: number`

**interface** `RapportComparatifBE`

- **Propriétés :**
  - `periode: string`
  - `actuelle: TrancheComparatifBE`
  - `precedente: TrancheComparatifBE`
  - `variationAbsolueFCFA: number`
  - `variationPourcent: number`

**interface** `TauxOccupationSecteurBE`

- **Propriétés :**
  - `secteur: string`
  - `boutiquesRecensees: number`
  - `clientsLivres: number`
  - `tauxOccupationPourcent: number`

**interface** `ZonePerformanceBE`

- **Propriétés :**
  - `secteur: string`
  - `caMoisCourantFCFA: number`
  - `nombreClients: number`
  - `nombreVentesMoisCourant: number`

**interface** `ValidationEnAttenteBE`

- **Propriétés :**
  - `type: string`
  - `reference: string`
  - `description: string`
  - `matriculeConcerne: string`
  - `depuis: string`

**interface** `AnomalieBE`

- **Propriétés :**
  - `categorie: string`
  - `reference: string`
  - `description: string`
  - `matriculeConcerne: string`

**interface** `DashboardStockDgBE`

- **Propriétés :**
  - `date: string`
  - `nbProduitsCentral: number`
  - `totalQuantiteCentral: number`
  - `nbProduitsTampon: number`
  - `totalQuantiteTampon: number`
  - `nbProduitsMobile: number`
  - `totalQuantiteMobile: number`
  - `nbAlertesActives: number`
  - `totalAlertes: number`
  - `nbProduitsSousSeuil: number`

**interface** `DashboardFinancierDgBE`

- **Propriétés :**
  - `soldeCaisse: number`
  - `totalFacturesEmises: number`
  - `totalFacturesPayees: number`
  - `totalDecaissementsExecutes: number`
  - `totalVersements: number`
  - `totalPrimes: number`
  - `totalRecouvrements: number`

**interface** `DashboardProductionDgBE`

- **Propriétés :**
  - `totalPPH: number`
  - `totalOF: number`
  - `totalLots: number`
  - `totalQuantiteProduite: number`

**class** `DgService`

- **Méthodes :**
  - `getKpis(): Observable<KpisDgBE> `
  - `getClassement(): Observable<ClassementCommercialBE[]> `
  - `getComparatif(periode: string, date?: string): Observable<RapportComparatifBE> `
  - `if(date): void`
  - `getTauxOccupationMarches(): Observable<TauxOccupationSecteurBE[]> `
  - `getZones(): Observable<ZonePerformanceBE[]> `
  - `getValidationsEnAttente(): Observable<ValidationEnAttenteBE[]> `
  - `getAnomalies(): Observable<AnomalieBE[]> `
  - `getDashboardStock(): Observable<DashboardStockDgBE> `
  - `getDashboardFinancier(): Observable<DashboardFinancierDgBE> `
  - `getDashboardProduction(): Observable<DashboardProductionDgBE> `


#### `app\domains\dg\presentation\pages\arbitrage\dg-arbitrage.component.ts`

**class** `DgArbitrageComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `
  - `approuver(ref: string): void`
  - `refuser(ref: string): void`


#### `app\domains\dg\presentation\pages\classement\dg-classement.component.ts`

**class** `DgClassementComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `
  - `cappedTaux(ca: number, obj: number): number `
  - `tauxRealisation(c: ClassementCommercialBE): number `
  - `isEligible(c: ClassementCommercialBE): boolean `


#### `app\domains\dg\presentation\pages\dashboard\dg-dashboard.component.ts`

**class** `DgDashboardComponent` extends/implements `OnInit, AfterViewInit, OnDestroy `

- **Propriétés :**
  - `private charts: Chart[] = []`
  - `type: 'bar',`
  - `data: {`
  - `datasets: [`
  - `type: 'line',`
  - `data: {`
  - `datasets: [`

- **Méthodes :**
  - `if(!k || k.caTotal === 0): void`
  - `if(!k): void`
  - `return(k.stockMatieresPremieres + k.stockConsommables + k.stockProduitsFinis): void`
  - `if(list.length === 0): void`
  - `tauxOccupation(o: TauxOccupationSecteurBE): number `
  - `getCommercialNom(id: string): string `
  - `tauxRealisationCom(com: ClassementCommercialBE): number `
  - `if(cible === 0): void`
  - `ngOnInit(): void`
  - `setTimeout((): void`
  - `ngAfterViewInit(): void`
  - `setTimeout((): void`
  - `setPeriod(p: 'today' | 'week' | 'month'): void`
  - `buildCharts(): void`
  - `if(this.caZoneCanvas?.nativeElement && zones.length > 0): void`
  - `if(this.caMensuelCanvas?.nativeElement && caMensuel.length > 0): void`
  - `ngOnDestroy(): void`


#### `app\domains\dg\presentation\pages\decaissements\dg-decaissements.component.ts`

**class** `DgDecaissementsComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `
  - `approuverDG(numero: string): void`
  - `refuserDG(numero: string): void`


#### `app\domains\dg\presentation\pages\documents\dg-documents.component.ts`

**class** `DgDocumentsComponent` extends/implements `OnDestroy `

- **Propriétés :**
  - `selectedFile: File | null = null`
  - `typeDocument: this.typeDocument,`
  - `reference: this.reference,`
  - `storageKey: key,`

- **Méthodes :**
  - `onFileSelected(event: Event): void `
  - `if(input.files?.length): void`
  - `importer(): void `
  - `if(this.uploading(): void`
  - `telecharger(doc: ArchivedDoc): void `
  - `showToast(msg: string, type: 'success' | 'error'): void `
  - `setTimeout((): void`
  - `ngOnDestroy(): void `


#### `app\domains\dg\presentation\pages\objectifs\dg-objectifs.component.ts`

**class** `DgObjectifsComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `
  - `cappedTaux(ca: number, obj: number): number `
  - `toggleEdit(): void`
  - `saveObjectifs(): void`
  - `setTimeout((): void`
  - `totalObjectif(): void`
  - `totalCA(): void`


#### `app\domains\dg\presentation\pages\reporting\dg-reporting.component.ts`

**class** `DgReportingComponent` extends/implements `OnInit, AfterViewInit, OnDestroy `

- **Propriétés :**
  - `private charts: Chart[] = []`
  - `type: 'line',`
  - `data: {`
  - `datasets: [`
  - `type: 'bar',`
  - `data: {`
  - `datasets: [`
  - `type: 'radar',`
  - `data: {`
  - `datasets: [`
  - `options: { responsive: true, maintainAspectRatio: false, scales: { r: { min: 0, max: 120, ticks: { stepSize: 20 } } } }`

- **Méthodes :**
  - `if(prev === 0): void`
  - `ngOnInit(): void`
  - `setTimeout((): void`
  - `ngAfterViewInit(): void`
  - `buildCharts(): void`
  - `if(this.lineCanvas?.nativeElement && caMensuel.length > 0): void`
  - `if(this.barCanvas?.nativeElement && zones.length > 0): void`
  - `if(this.radarCanvas?.nativeElement && commerciaux.length > 0): void`
  - `ngOnDestroy(): void`


#### `app\domains\dg\presentation\pages\sync\dg-sync.component.ts`

**class** `DgSyncComponent` extends/implements `OnInit, OnDestroy `

- **Propriétés :**
  - `SUCCESS: 'badge bg-success', CONFLICT: 'badge bg-orange', FAILED: 'badge bg-red', PENDING: 'badge bg-neutral',`

- **Méthodes :**
  - `ngOnInit(): void `
  - `charger(): void `
  - `if(this.filterMatricule): void`
  - `if(this.filterEntityType): void`
  - `if(this.filterResult): void`
  - `applyFilters(): void `
  - `resultClass(r: string): string `
  - `showToast(msg: string, type: 'success' | 'error'): void `
  - `setTimeout((): void`
  - `ngOnDestroy(): void `


### Domaine `layout`

#### `app\layout\commercial-layout\commercial-layout.component.ts`

**class** `CommercialLayoutComponent` extends/implements `OnInit `

- **Propriétés :**
  - `navItems: NavItem[] = [`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setInterval((): void`
  - `if(window.innerWidth < 1024): void`
  - `if(cur): void`
  - `if(window.innerWidth < 768): void`
  - `onResize(e: Event): void`
  - `if(w < 1024): void`
  - `toggleSidebar(): void`
  - `closeMobile(): void`


#### `app\layout\comptable-layout\comptable-layout.component.ts`

**class** `ComptableLayoutComponent` extends/implements `OnInit `

- **Propriétés :**
  - `navItems: NavItem[] = [`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setInterval((): void`
  - `if(window.innerWidth < 1024): void`
  - `if(cur): void`
  - `if(window.innerWidth < 768): void`
  - `onResize(e: Event): void`
  - `if(w < 1024): void`
  - `toggleSidebar(): void`
  - `closeMobile(): void`


#### `app\layout\controle-layout\controle-layout.component.ts`

**class** `ControleLayoutComponent` extends/implements `OnInit `

- **Propriétés :**
  - `navItems: NavItem[] = [`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setInterval((): void`
  - `if(window.innerWidth < 1024): void`
  - `if(cur): void`
  - `if(window.innerWidth < 768): void`
  - `onResize(e: Event): void`
  - `if(w < 1024): void`
  - `toggleSidebar(): void`
  - `if(window.innerWidth < 768): void`
  - `closeMobile(): void`


#### `app\layout\dg-layout\dg-layout.component.ts`

**class** `DgLayoutComponent` extends/implements `OnInit `

- **Propriétés :**
  - `navItems: NavItem[] = [`
  - `adminItems: NavItem[] = [`
  - `matricule: '',`
  - `nomComplet: '',`
  - `role: 'DIRECTEUR_GENERAL',`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setInterval((): void`
  - `if(window.innerWidth < 1024): void`
  - `if(cur): void`
  - `if(window.innerWidth < 768): void`
  - `onResize(e: Event): void`
  - `if(w < 1024): void`
  - `toggleSidebar(): void`
  - `if(window.innerWidth < 768): void`
  - `closeMobile(): void`


#### `app\layout\production-layout\production-layout.component.ts`

**class** `ProductionLayoutComponent` extends/implements `OnInit `

- **Propriétés :**
  - `navItems: NavItem[] = [`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setInterval((): void`
  - `if(window.innerWidth < 1024): void`
  - `if(cur): void`
  - `if(window.innerWidth < 768): void`
  - `onResize(e: Event): void`
  - `if(w < 1024): void`
  - `toggleSidebar(): void`
  - `closeMobile(): void`


#### `app\layout\rp-layout\rp-layout.component.ts`

**class** `RpLayoutComponent` extends/implements `OnInit `

- **Propriétés :**
  - `navItems: NavItem[] = [`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setInterval((): void`
  - `if(window.innerWidth < 1024): void`
  - `if(cur): void`
  - `if(window.innerWidth < 768): void`
  - `onResize(e: Event): void`
  - `if(w < 1024): void`
  - `toggleSidebar(): void`
  - `if(window.innerWidth < 768): void`
  - `closeMobile(): void`


#### `app\layout\secretaire-layout\secretaire-layout.component.ts`

**class** `SecretaireLayoutComponent` extends/implements `OnInit `

- **Propriétés :**
  - `navItems: NavItem[] = [`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setInterval((): void`
  - `if(window.innerWidth < 1024): void`
  - `if(cur): void`
  - `if(window.innerWidth < 768): void`
  - `onResize(e: Event): void`
  - `if(w < 1024): void`
  - `toggleSidebar(): void`
  - `closeMobile(): void`


#### `app\layout\stock-layout\stock-layout.component.ts`

**class** `StockLayoutComponent` extends/implements `OnInit `

- **Propriétés :**
  - `navItems: NavItem[] = [`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setInterval((): void`
  - `if(window.innerWidth < 1024): void`
  - `if(cur): void`
  - `if(window.innerWidth < 768): void`
  - `onResize(e: Event): void`
  - `if(w < 1024): void`
  - `toggleSidebar(): void`
  - `if(window.innerWidth < 768): void`
  - `closeMobile(): void`


### Domaine `pages`

#### `app\pages\auth\forbidden\forbidden.component.ts`

**class** `ForbiddenComponent`

- **Propriétés :**
  - `private readonly router: Router,`
  - `private readonly auth: AuthService,`

- **Méthodes :**
  - `if(state?.message): void`
  - `goBack(): void `
  - `if(this.auth.isAuthenticated(): void`


#### `app\pages\auth\forgot-password\forgot-password.component.ts`

**class** `ForgotPasswordComponent`

- **Propriétés :**
  - `forgotForm: FormGroup`
  - `private fb: FormBuilder,`
  - `private router: Router`
  - `email: ['', [Validators.required, Validators.email]]`

- **Méthodes :**
  - `onSubmit(): void`
  - `if(this.forgotForm.valid): void`
  - `setTimeout((): void`
  - `goToLogin(): void`


#### `app\pages\auth\login\login.component.ts`

**class** `LoginComponent` extends/implements `OnInit `

- **Propriétés :**
  - `loginForm: FormGroup`
  - `matricule: ['', [Validators.required]],`

- **Méthodes :**
  - `constructor(): void`
  - `ngOnInit(): void `
  - `if(reason === 'forbidden'): void`
  - `onSubmit(): void`
  - `if(this.loginForm.invalid): void`
  - `togglePassword(): void`
  - `goToForgotPassword(): void`


#### `app\pages\auth\reset-password\reset-password.component.ts`

**class** `ResetPasswordComponent`

- **Propriétés :**
  - `resetForm: FormGroup`
  - `private fb: FormBuilder,`
  - `private router: Router`
  - `confirmPassword: ['', [Validators.required]]`

- **Méthodes :**
  - `passwordMatchValidator(formGroup: FormGroup): void`
  - `onSubmit(): void`
  - `if(this.resetForm.valid): void`
  - `setTimeout((): void`
  - `togglePassword(): void`
  - `toggleConfirmPassword(): void`
  - `goToLogin(): void`


#### `app\pages\auth\two-fa\two-fa.component.ts`

**class** `TwoFaComponent`

- **Propriétés :**
  - `otpForm: FormGroup`

- **Méthodes :**
  - `constructor(): void`
  - `onSubmit(): void`
  - `if(this.otpForm.invalid): void`
  - `startCountdown(): void`
  - `if(this.countdown <= 0): void`
  - `clearInterval(interval): void`
  - `resendOtp(): void`
  - `if(this.canResend && this.matricule): void`
  - `goToLogin(): void`


#### `app\pages\unauthorized\unauthorized.component.ts`

**class** `UnauthorizedComponent`


### Domaine `production`

#### `app\domains\production\infrastructure\production.service.ts`

**interface** `LotBE`

- **Propriétés :**
  - `id: number`
  - `referencePPH: string`
  - `dlc: string`
  - `matriculeDeclarant: string`

**interface** `OFBE`

- **Propriétés :**
  - `id: string`
  - `quantiteDemandee: number`
  - `dateCreation: string`

**interface** `PPHBE`

- **Propriétés :**
  - `id: number`
  - `dateDebut: string`
  - `referenceBC: string`
  - `matriculeChefProduction: string`
  - `dateCreation: string`
  - `lignes: PPHLigneBE[]`
  - `lignesDetaillees: PPHLigneDetailBE[]`
  - `repartitionJours: PPHRepartitionJourBE[]`

**interface** `PPHLigneBE`

- **Propriétés :**
  - `codeProduit: string`
  - `objectifSemaine: number`

**interface** `PPHLigneDetailBE`

- **Propriétés :**
  - `codeProduit: string`
  - `quantiteCartons: number`
  - `recette: string | null`
  - `poudreMaisKg: number`
  - `futsPrevus: number`
  - `poidsTotalGrammes: number`

**interface** `PPHRepartitionJourBE`

- **Propriétés :**
  - `dateJour: string`
  - `poudreMaisKg: number`
  - `futsPrevus: number`

**interface** `SessionBroyageBE`

- **Propriétés :**
  - `id: number`
  - `typePoudre: string`
  - `objectifJournalierKg: number`
  - `objectifsMachinistes: ObjectifMachinisteBE[]`
  - `realisations: RealisationMachinisteBE[]`
  - `quantiteNetteBroyeeKg: number | null`
  - `pctPertes: number | null`
  - `depassementAutoriseKg: number`
  - `indicateursGlobaux: { totalPoudreKg: number`
  - `justificatifNonValidation: string | null`
  - `dateCreation: string`

**interface** `ObjectifMachinisteBE`

- **Propriétés :**
  - `machinisteMatricule: string`
  - `operation: string`

**interface** `RealisationMachinisteBE`

- **Propriétés :**
  - `machinisteMatricule: string`
  - `operation: string`
  - `objectifKg: number`

**interface** `EmployeBE`

- **Propriétés :**
  - `id: number`
  - `role: string`
  - `avecCompte: boolean`
  - `dateCreation: string`

**interface** `ClassementMachinisteBE`

- **Propriétés :**
  - `classements: {`
  - `rang: number`
  - `productiviteKg: number`
  - `tauxRealisationMoyen: number`
  - `operations: string[]`
  - `periode: string`

**interface** `SessionDosageBE`

- **Propriétés :**
  - `id: number`
  - `matriculeAgentDoseur: string`
  - `matieresUtilisees: { typeMatiere: string`
  - `machines: string[]`
  - `futsProduits: number`
  - `justificationEcart: string | null`
  - `validee: boolean`
  - `dateCreation: string`

**interface** `StatistiquesFutsBE`

- **Propriétés :**
  - `debut: string`
  - `nbFutsProduits: number`
  - `poudreMaisKg: number`
  - `comparaison: {`
  - `debutPeriodePrecedente: string`
  - `nbFutsNetsPeriodePrecedente: number`
  - `nbSessions: number`

**interface** `PredictionProduitFiniBE`

- **Propriétés :**
  - `nbFutsNets: number`
  - `predictions: {`
  - `codeProduit: string`
  - `quantiteUnitesBase: number`
  - `poidsUnitaireGrammes: number`

**interface** `FicheProductionBE`

- **Propriétés :**
  - `id: number`
  - `matriculeAgentProduction: string`
  - `verrouillee: boolean`
  - `predictionDosage: Record<string, number>`
  - `predictionPostes: Record<string, number>`
  - `lignes: {`
  - `codeProduit: string`
  - `quantitePrediteDosage: number`

**interface** `AffectationBE`

- **Propriétés :**
  - `id: number`
  - `matriculeAgent: string`
  - `lignes: {`
  - `codePoste: string`
  - `effectifReference: number`
  - `duree: string`
  - `matriculesEmployes: string[]`
  - `saisies: {`
  - `codePoste: string`
  - `quantiteRealisee: number`
  - `productionsIndividuelles: { matriculeEmploye: string`
  - `details: {`
  - `nbRegimesEpluches: number | null`
  - `nbSceauxEtiquetes: number | null`
  - `nbSachetsDates: number | null`
  - `kgChipsProduits: number | null`
  - `warnings: string[]`
  - `dateEnregistrement: string`
  - `classement: {`
  - `rang: number`
  - `quantiteRealisee: number`
  - `dateCreation: string`

**interface** `ClassementExecuteurBE`

- **Propriétés :**
  - `rang: number`
  - `quantiteTotaleRealisee: number`
  - `tauxRealisationMoyen: number`

**interface** `ComparaisonFutsProductionBE`

- **Propriétés :**
  - `referencePPH: string`
  - `nbFutsNets: number`
  - `productionReelleKg: number`
  - `uniteProduction: string`

**interface** `RapportHebdomadaireProductionBE`

- **Propriétés :**
  - `debut: string`
  - `nbFutsNets: number`
  - `nbSessionsDosage: number`
  - `classementExecuteurs: ClassementExecuteurBE[]`
  - `comparaisonsJournalieres: ComparaisonFutsProductionBE[]`

**interface** `LigneRegistreBE`

- **Propriétés :**
  - `matriculeEmploye: string`
  - `present: boolean`
  - `heureArrivee: string | null`

**interface** `RegistreProductionBE`

- **Propriétés :**
  - `id: number`
  - `matriculeChefProduction: string`
  - `lignes: LigneRegistreBE[]`
  - `nbPresents: number`
  - `dateCreation: string`

**interface** `DashboardChefBE`

- **Propriétés :**
  - `quantiteKgSemaine: number`
  - `ofsEnCours: number`
  - `tauxAvancementPPH: number`
  - `pphEnCoursReference: string | null`
  - `debutSemaine: string`

**interface** `DashboardAgentBE`

- **Propriétés :**
  - `date: string`
  - `fichesSemaine: number`
  - `ofs: { idOF: string`

**interface** `RapportHebdomadaireProductionBE`

- **Propriétés :**
  - `debutSemaine: string`
  - `quantitesParReference: Record<string, number>`
  - `tauxAvancementParBC: Record<string, number>`
  - `pertesParTypePoudre: Record<string, number>`
  - `quantiteBroyeeParTypePoudre: Record<string, number>`
  - `totalFutsProduits: number`
  - `totalLotsValides: number`
  - `nbFiches: number`
  - `tauxPertesGlobal: number`
  - `pctPertesParTypePoudre: Record<string, number>`

**interface** `RapportComparatifBCBE`

- **Propriétés :**
  - `referenceBC: string`
  - `details: { codeProduit: string`

**interface** `ExpressionBesoinBE`

- **Propriétés :**
  - `numero: string`
  - `quantiteDemandee: number`
  - `statut: string`
  - `dateCreation: string`

**class** `ProductionService`

- **Propriétés :**
  - `codeProduit: string`
  - `quantiteKg: number`
  - `idOF: string`
  - `qteDemandee: number`
  - `referenceBC: string`
  - `referencePPH: string`
  - `referencePPH: string`
  - `objectifJournalierKg: number`
  - `objectifsMachinistes: { machinisteMatricule: string`
  - `sessionId: number`
  - `operation: string`
  - `sessionId: number`
  - `prenom: string`
  - `superviseurMatricule: string`
  - `email: string`
  - `referencePPH: string`
  - `referencePPH: string`
  - `lignes: { codeProduit: string`
  - `predictionDosage: Record<string, number>`
  - `lignes: { codeProduit: string`
  - `referencePPH: string`
  - `referencePPH: string`
  - `lignes: { codePoste: string`
  - `affectationId: number`
  - `productionsIndividuelles: { matriculeEmploye: string`
  - `details: {`
  - `nbRegimesEpluches: number`
  - `nbSceauxEtiquetes: number`
  - `nbSachetsDates: number`
  - `referencePPH: string`
  - `lignes: { matriculeEmploye: string`
  - `numero: string`
  - `quantiteDemandee: number`

- **Méthodes :**
  - `getLots(statut?: string): Observable<LotBE[]> `
  - `if(statut): void`
  - `getOFs(statut?: string): Observable<OFBE[]> `
  - `if(statut): void`
  - `getLotsAValider(): Observable<LotBE[]> `
  - `validerLot(numeroLot: string): Observable<LotBE> `
  - `rejeterLot(numeroLot: string, motif: string): Observable<LotBE> `
  - `getOF(idOF: string): Observable<OFBE> `
  - `demarrerOF(idOF: string): Observable<OFBE> `
  - `produireOF(idOF: string, quantite: number): Observable<OFBE> `
  - `annulerOF(idOF: string): Observable<OFBE> `
  - `getPPHs(statut?: string): Observable<PPHBE[]> `
  - `if(statut): void`
  - `getPPHEnCours(): Observable<PPHBE> `
  - `getPPH(reference: string): Observable<PPHBE> `
  - `validerPPH(reference: string): Observable<PPHBE> `
  - `demarrerPPH(reference: string): Observable<PPHBE> `
  - `cloturerPPH(reference: string): Observable<PPHBE> `
  - `modifierLignesPPH(reference: string, lignes: { codeProduit: string; objectifSemaine: number }[]): Observable<PPHBE> `
  - `getPPHParBC(referenceBC: string): Observable<PPHBE[]> `
  - `if(params.referencePPH): void`
  - `if(params.debut): void`
  - `if(params.fin): void`
  - `getSessionBroyage(id: number): Observable<SessionBroyageBE> `
  - `autoriserDepassementBroyage(sessionId: number, quantiteSupplementaireKg: number): Observable<SessionBroyageBE> `
  - `justifierNonValidationBroyage(sessionId: number, justificatif: string): Observable<SessionBroyageBE> `
  - `getClassementMachinistes(debut: string, fin: string): Observable<ClassementMachinisteBE> `
  - `getMachinistes(): Observable<EmployeBE[]> `
  - `enregistrerMatiereDosage(id: number, typeMatiere: string, quantiteKg: number): Observable<SessionDosageBE> `
  - `enregistrerMachineDosage(id: number, machineId: string): Observable<SessionDosageBE> `
  - `enregistrerFutsDosage(id: number, nbProduits: number): Observable<SessionDosageBE> `
  - `justifierEcartDosage(id: number, justification: string): Observable<SessionDosageBE> `
  - `validerSessionDosage(id: number): Observable<SessionDosageBE> `
  - `cloturerSessionDosage(id: number): Observable<SessionDosageBE> `
  - `getStatistiquesFuts(debut: string, fin: string): Observable<StatistiquesFutsBE> `
  - `predireProduitFini(nbFutsNets: number, codeProduit?: string): Observable<PredictionProduitFiniBE> `
  - `if(codeProduit): void`
  - `telechargerRegistreDosage(id: number, format: 'pdf' | 'excel' = 'pdf'): Observable<Blob> `
  - `telechargerDocumentsDosage(id: number): Observable<Blob> `
  - `getSessionsDosageParPPH(reference: string): Observable<SessionDosageBE[]> `
  - `getSessionsDosageParDate(date: string): Observable<SessionDosageBE[]> `
  - `getFichesParPPH(referencePPH: string): Observable<FicheProductionBE[]> `
  - `getFiche(referencePPH: string, date: string): Observable<FicheProductionBE> `
  - `if(params.referencePPH): void`
  - `if(params.debut): void`
  - `if(params.fin): void`
  - `getAffectation(id: number): Observable<AffectationBE> `
  - `getAffectationDuJour(referencePPH: string): Observable<AffectationBE> `
  - `validerAffectation(id: number): Observable<AffectationBE> `
  - `getComparaisonFutsProduction(referencePPH: string, date: string): Observable<ComparaisonFutsProductionBE> `
  - `getClassementExecuteurs(debut: string, fin: string): Observable<ClassementExecuteurBE[]> `
  - `getRapportHebdomadaire(debut: string, fin: string): Observable<RapportHebdomadaireProductionBE> `
  - `getRegistres(params: { referencePPH?: string; debut?: string; fin?: string } = {}): Observable<RegistreProductionBE[]> `
  - `if(params.referencePPH): void`
  - `if(params.debut): void`
  - `if(params.fin): void`
  - `getDashboardChef(): Observable<DashboardChefBE> `
  - `getDashboardAgent(): Observable<DashboardAgentBE> `
  - `getRapportLots(debut: string, fin: string, format: string = 'xlsx'): Observable<Blob> `
  - `getRapportHebdomadaireProduction(debut: string, fin: string): Observable<RapportHebdomadaireProductionBE> `
  - `getRapportComparatifBC(referenceBC: string): Observable<RapportComparatifBCBE> `
  - `getRapportResponsableSalle(debut: string, fin: string): Observable<RapportHebdomadaireProductionBE> `
  - `getBesoins(statut?: string): Observable<ExpressionBesoinBE[]> `
  - `if(statut): void`
  - `validerBesoin(numero: string): Observable<ExpressionBesoinBE> `
  - `rejeterBesoin(numero: string, motif: string): Observable<ExpressionBesoinBE> `
  - `getPerformancesMachiniste(machinisteMatricule: string, debut: string, fin: string): Observable<SessionBroyageBE[]> `


#### `app\domains\production\presentation\pages\agent-doseur\agent-doseur.component.ts`

**class** `AgentDoseurComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `
  - `chargerDonnees(): void `
  - `ouvrirSession(): void `
  - `ajouterMatiere(session: SessionDosageBE): void `
  - `ajouterMachine(session: SessionDosageBE): void `
  - `if(!this.nouvelleMachine.trim(): void`
  - `enregistrerFuts(session: SessionDosageBE): void `
  - `validerSession(session: SessionDosageBE): void `
  - `if(!confirm('Valider cette session de dosage ?'): void`
  - `cloturerSession(session: SessionDosageBE): void `
  - `if(!confirm('Clôturer définitivement cette session de dosage ?'): void`
  - `justifierEcart(session: SessionDosageBE): void `
  - `if(motif): void`
  - `lancerPrediction(): void `
  - `if(this.nbFutsPrediction <= 0): void`
  - `telechargerRegistre(session: SessionDosageBE, format: 'pdf' | 'excel'): void `
  - `telechargerDocuments(session: SessionDosageBE): void `
  - `declencherTelechargement(blob: Blob, nom: string): void `
  - `debutSemaine(d: Date): string `
  - `debutMois(d: Date): string `


#### `app\domains\production\presentation\pages\chef-machiniste\chef-machiniste.component.ts`

**class** `ChefMachinisteComponent` extends/implements `OnInit `

- **Propriétés :**
  - `referencePPH: '', date: this.today, typePoudre: 'MAIS',`
  - `objectifJournalierKg: 0,`
  - `objectifsMachinistes: [] as { machinisteMatricule: string`
  - `sessionId: 0, machinisteMatricule: '', nomMachiniste: '',`
  - `operation: 'ECRASAGE', typePoudre: 'MAIS', objectifKg: 0, realiseKg: 0`
  - `machinisteMatricule: '', nomMachiniste: '', operation: 'ECRASAGE', objectifKg: 0`
  - `sessionId: session.id,`
  - `machinisteMatricule: '',`
  - `nomMachiniste: '',`
  - `operation: 'ECRASAGE',`
  - `typePoudre: session.typePoudre,`
  - `objectifKg: 0,`
  - `realiseKg: 0`
  - `next: s => {`
  - `sessionId: session.id,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `chargerDonnees(): void `
  - `ouvrirSession(): void `
  - `ajouterObjectif(): void `
  - `retirerObjectif(index: number): void `
  - `selectionnerMachiniste(index: number, matricule: string): void `
  - `if(m): void`
  - `ouvrirRealisation(session: SessionBroyageBE): void `
  - `selectionnerMachinisteRealisation(matricule: string): void `
  - `if(m): void`
  - `enregistrerRealisation(): void `
  - `cloturerSession(session: SessionBroyageBE): void `
  - `if(quantite && pertes): void`
  - `autoriserDepassement(session: SessionBroyageBE): void `
  - `if(qte): void`
  - `justifierSession(session: SessionBroyageBE): void `
  - `if(motif): void`
  - `reloadClassements(): void `
  - `debutSemaine(d: Date): string `
  - `debutMois(d: Date): string `


#### `app\domains\production\presentation\pages\dashboard\production-dashboard.component.ts`

**class** `ProductionDashboardComponent` extends/implements `OnInit `

- **Propriétés :**
  - `semaine: '—', dateDebut: '—', dateFin: '—',`
  - `chefProduction: '—', statut: '—', lignesProduitsFinis: [],`
  - `quantiteProduiteKg: 0, quantiteConditionneeCartons: 0,`
  - `rendementMatiere: 0, tauxRebut: 0, trs: 0,`
  - `productiviteHoraire: 0, valorisationJour: 0,`
  - `carburant: { id: string`
  - `maintenance: { equipement: string`
  - `variance: { poste: string`
  - `idOF: '', referencePPH: '', codeProduit: '', designationProduit: '', qteDemandee: 0, dateButoir: ''`
  - `id: l.numeroLot,`
  - `codeProduit: l.codeProduit,`
  - `designationProduit: l.designationProduit,`
  - `declaredQuantityKg: l.quantiteKg,`
  - `statut: l.statut,`
  - `id: o.id,`
  - `referencePPH: o.referencePPH,`
  - `designation: o.designation,`
  - `qteDemandee: o.quantiteDemandee,`
  - `qteRealisee: o.quantiteRealisee,`
  - `statut: o.statut,`
  - `semaine: dashboard.pphEnCoursSemaine ?? p.semaine,`
  - `next: o => {`
  - `id: o.id, referencePPH: o.referencePPH, designation: o.designation,`
  - `qteDemandee: o.quantiteDemandee, qteRealisee: o.quantiteRealisee,`
  - `qteRealisee: o.quantiteRealisee,`
  - `statut: o.statut,`

- **Méthodes :**
  - `carburantAValider(): number `
  - `ngOnInit(): void `
  - `chargerDonnees(): void `
  - `creerOF(): void `
  - `demarrerOF(idOF: string): void `
  - `annulerOF(idOF: string): void `
  - `if(!confirm('Annuler cet ordre de fabrication ?'): void`
  - `ouvrirProduire(idOF: string): void `
  - `produireOF(idOF: string): void `
  - `if(this.quantiteProduire <= 0): void`
  - `mettreAJourOF(o: OFBE): void `
  - `getProduitDesignation(code: string): string `
  - `progression(realise: number, prevu: number): number `
  - `if(prevu === 0): void`
  - `progressClass(pct: number): string `
  - `if(pct >= 100): void`
  - `if(pct >= 60): void`


#### `app\domains\production\presentation\pages\employes\production-employes.component.ts`

**class** `ProductionEmployesComponent` extends/implements `OnInit `

- **Propriétés :**
  - `lignesRegistre: { matriculeEmploye: string`
  - `referencePPH: this.referencePPH,`
  - `date: this.dateRegistre,`
  - `next: r => {`

- **Méthodes :**
  - `if(total === 0): void`
  - `ngOnInit(): void `
  - `if(registres.length > 0): void`
  - `ouvrirEnregistrerRegistre(): void `
  - `ajouterLigne(): void `
  - `retirerLigne(index: number): void `
  - `nouvelleLigneVide(): void`
  - `enregistrerRegistre(): void `
  - `if(!this.referencePPH || lignes.length === 0): void`


#### `app\domains\production\presentation\pages\lots\production-lots.component.ts`

**class** `ProductionLotsComponent` extends/implements `OnInit `

- **Propriétés :**
  - `codeProduit: '', referencePPH: '', quantiteKg: 0, quantiteUnites: 0, dlc: ''`
  - `next: l => {`
  - `id: l.numeroLot,`
  - `numeroLot: l.numeroLot,`
  - `codeProduit: l.codeProduit,`
  - `designation: l.designationProduit,`
  - `declaredQuantityKg: l.quantiteKg,`
  - `equivalentUnits: l.quantiteUnites,`
  - `statut: l.statut,`
  - `dateDeclaration: date,`
  - `productionDate: date,`
  - `declaredBy: l.matriculeDeclarant,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `declarerLot(): void `
  - `if(!req.codeProduit || !req.referencePPH || req.quantiteKg <= 0): void`
  - `getProduitDesignation(code: string): string `
  - `statutClass(s: string): string `
  - `return(s === 'VALIDATED_BY_STOCK' || s === 'VALIDE'): void`
  - `statutLabel(s: string): string `
  - `return(s === 'VALIDATED_BY_STOCK' || s === 'VALIDE'): void`
  - `mapL(l: LotBE): LotUI `


#### `app\domains\production\presentation\pages\plan\production-plan.component.ts`

**class** `ProductionPlanComponent` extends/implements `OnInit `

- **Propriétés :**
  - `lignesModifiees: { codeProduit: string`
  - `next: p => {`
  - `codeProduit: l.codeProduit, nomProduit: l.nomProduit, objectifSemaine: l.objectifSemaine`
  - `EN_COURS: 'badge bg-orange',`
  - `CLOTURE: 'badge bg-success',`
  - `VALIDE: 'badge bg-neutral',`
  - `BROUILLON: 'badge bg-neutral',`

- **Méthodes :**
  - `if(!p || !p.repartitionJours): void`
  - `if(totalMais > 0): void`
  - `if(totalSoja > 0): void`
  - `if(totalArachide > 0): void`
  - `if(!p || !p.lignes): void`
  - `if(!p || !p.lignes): void`
  - `if(total === 0): void`
  - `getProduitDesignation(code: string): string `
  - `ngOnInit(): void `
  - `selectionnerPPH(ref: string): void `
  - `creerPPH(): void `
  - `validerPPH(): void `
  - `if(!ref): void`
  - `demarrerPPH(): void `
  - `if(!ref): void`
  - `cloturerPPH(): void `
  - `if(!ref): void`
  - `ouvrirModifierLignes(): void `
  - `modifierLignes(): void `
  - `if(!ref): void`
  - `progression(realise: number, prevu: number): number `
  - `if(prevu === 0): void`
  - `progressClass(pct: number): string `
  - `if(pct >= 100): void`
  - `if(pct >= 60): void`
  - `statutPlanClass(s: string): string `


#### `app\domains\production\presentation\pages\responsable-salle\responsable-salle.component.ts`

**class** `ResponsableSalleComponent` extends/implements `OnInit `

- **Propriétés :**
  - `pphs: PPHBE[] = []`
  - `selectedPph: PPHBE | null = null`
  - `affectation: AffectationBE | null = null`
  - `fiche: FicheProductionBE | null = null`
  - `error: string | null = null`
  - `success: string | null = null`
  - `tab: 'affectation' | 'saisie' | 'fiche' | 'classement' | 'rapport' = 'affectation'`
  - `nouvelleAffectation: { codePoste: string`
  - `next: pphs => this.pphs = pphs,`
  - `next: a => this.affectation = a,`
  - `error: err => {`
  - `next: f => this.fiche = f,`
  - `referencePPH: this.selectedPph.referenceBC,`
  - `date: this.date,`
  - `matriculeAgent: 'RS-001',`
  - `next: a => {`
  - `affectationId: this.affectation.id,`
  - `quantiteRealisee: qte,`
  - `productionsIndividuelles: []`
  - `next: a => {`
  - `referencePPH: this.selectedPph.referenceBC,`
  - `semaine: this.selectedPph.semaine,`
  - `date: this.date,`
  - `matriculeAgentProduction: 'RS-001',`
  - `lignes: this.fiche.lignes,`
  - `predictionDosage: this.fiche.predictionDosage,`
  - `predictionPostes: this.fiche.predictionPostes`
  - `next: f => {`

- **Méthodes :**
  - `ngOnInit(): void`
  - `chargerPPHs(): void`
  - `selectionnerPph(pph: PPHBE): void`
  - `chargerAffectation(referencePPH: string): void`
  - `chargerFiche(referencePPH: string, date: string): void`
  - `creerAffectation(): void`
  - `if(!this.selectedPph || this.nouvelleAffectation.length === 0): void`
  - `ajouterLigneAffectation(): void`
  - `saisirRealisation(codePoste: string): void`
  - `if(!saisie || !this.affectation): void`
  - `if(Number.isNaN(qte): void`
  - `enregistrerFiche(): void`
  - `if(!this.selectedPph || !this.fiche): void`
  - `allerEmployes(): void`
  - `chargerClassement(): void`
  - `chargerRapport(): void`
  - `trackByIndex(index: number): void`
  - `debutSemaine(d: Date): string `


#### `app\domains\production\presentation\pages\saisie\production-saisie.component.ts`

**class** `ProductionSaisieComponent` extends/implements `OnInit `

- **Propriétés :**
  - `objectifJournalierKg: 0,`
  - `objectifsMachinistes: [] as { machinisteMatricule: string`
  - `next: a => {`
  - `machinisteMatricule: '', nomMachiniste: '', operation: 'ECRASAGE', objectifKg: 0`
  - `next: s => {`
  - `next: s => {`

- **Méthodes :**
  - `ngOnInit(): void `
  - `validerAffectation(id: number): void `
  - `ouvrirDetail(a: AffectationBE): void `
  - `ajouterObjectifBroyage(): void `
  - `retirerObjectifBroyage(index: number): void `
  - `ouvrirSessionBroyage(): void `
  - `ouvrirSessionDosage(): void `


### Domaine `rp`

#### `app\domains\rp\infrastructure\rp.service.ts`

**interface** `RpDashboard`

- **Propriétés :**
  - `nbEligibles: number`
  - `totalPrimes: number`
  - `occupationMoyenne: number`
  - `grandsComptesActifs: number`
  - `relancesDues: number`
  - `caGrandsComptes: number`
  - `primes: PrimeResponse[]`
  - `gammes: PerformanceGammeResponse[]`
  - `occupation: OccupationMarcheResponse[]`
  - `grandsComptes: GrandCompteResponse[]`
  - `campagnes: CampagneResponse[]`

**interface** `PrimeResponse`

- **Propriétés :**
  - `commercialId: string`
  - `nom: string`
  - `tauxGlobal: number`
  - `gammesValidees: number`
  - `gammesTotal: number`
  - `eligible: boolean`
  - `montant: number`

**interface** `PerformanceGammeResponse`

- **Propriétés :**
  - `gamme: string`
  - `ca: number`
  - `objectif: number`
  - `marge: number`
  - `stock: string`

**interface** `OccupationMarcheResponse`

- **Propriétés :**
  - `zone: string`
  - `commercialId: string`
  - `livrees: number`
  - `repertoriees: number`
  - `potentiel: string`

**interface** `GrandCompteResponse`

- **Propriétés :**
  - `id: number`
  - `nom: string`
  - `type: string`
  - `contact: string`
  - `telephone: string`
  - `caMensuel: number`
  - `derniereCommande: string`
  - `prochaineRelance: string`
  - `statut: string`

**interface** `CampagneResponse`

- **Propriétés :**
  - `id: number`
  - `titre: string`
  - `cible: string`
  - `canal: string`
  - `statut: string`
  - `datePublication: string`
  - `portee: number`

**class** `RpService`

- **Méthodes :**
  - `getDashboard(): Observable<RpDashboard> `
  - `getPrimes(): Observable<PrimeResponse[]> `
  - `getGrandsComptes(): Observable<GrandCompteResponse[]> `
  - `getCampagnes(): Observable<CampagneResponse[]> `
  - `getOccupation(): Observable<OccupationMarcheResponse[]> `


#### `app\domains\rp\presentation\pages\dashboard\rp-dashboard.component.ts`

**class** `RpDashboardComponent` extends/implements `OnInit `

- **Propriétés :**
  - `next: d => {`

- **Méthodes :**
  - `ngOnInit(): void `
  - `tauxOccupation(o: { livrees: number; repertoriees: number }): number `
  - `occupationClass(o: { livrees: number; repertoriees: number }): string `
  - `gammeClass(g: { ca: number; objectif: number }): string `


#### `app\domains\rp\presentation\pages\grands-comptes\rp-grands-comptes.component.ts`

**class** `RpGrandsComptesComponent` extends/implements `OnInit `

- **Méthodes :**
  - `icon(type: string): string `
  - `ngOnInit(): void `


#### `app\domains\rp\presentation\pages\marketing\rp-marketing.component.ts`

**class** `RpMarketingComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void `


#### `app\domains\rp\presentation\pages\zones\rp-zones.component.ts`

**class** `RpZonesComponent` extends/implements `OnInit `

- **Méthodes :**
  - `taux(o: TauxOccupationSecteurBE): number `
  - `commercialNom(mat: string): string `
  - `ngOnInit(): void `


### Domaine `secretaire`

#### `app\domains\secretaire\infrastructure\secretaire.service.ts`

**interface** `SecretaireDashboard`

- **Propriétés :**
  - `versementsEnAttente: number`
  - `commandesAValider: number`
  - `alertesRouges: number`
  - `totalVerse: number`
  - `totalEntrees: number`
  - `totalSorties: number`
  - `soldeCaisse: number`
  - `seuilDepasse: boolean`
  - `microStockBas: number`
  - `valeurMicroStock: number`
  - `docsIncomplets: number`
  - `petiteCaisse: PetiteCaisseResponse[]`
  - `microStock: MicroStockResponse[]`
  - `documents: DocumentRecuResponse[]`

**interface** `PetiteCaisseResponse`

- **Propriétés :**
  - `id: number`
  - `libelle: string`
  - `type: string`
  - `montant: number`
  - `heure: string`
  - `categorie: string`
  - `dateOperation: string`

**interface** `MicroStockResponse`

- **Propriétés :**
  - `id: number`
  - `code: string`
  - `designation: string`
  - `qte: number`
  - `prixUnite: number`
  - `seuil: number`

**interface** `DocumentRecuResponse`

- **Propriétés :**
  - `id: number`
  - `matriculeCommercial: string`
  - `nom: string`
  - `feuilleRoute: boolean`
  - `bonCommande: boolean`
  - `factures: boolean`

**class** `SecretaireService`

- **Méthodes :**
  - `getDashboard(): Observable<SecretaireDashboard> `
  - `getPetiteCaisse(): Observable<PetiteCaisseResponse[]> `
  - `getMicroStock(): Observable<MicroStockResponse[]> `
  - `getDocuments(): Observable<DocumentRecuResponse[]> `


#### `app\domains\secretaire\presentation\pages\commandes\secretaire-commandes.component.ts`

**class** `SecretaireCommandesComponent` extends/implements `OnInit `

- **Propriétés :**
  - `next: pc => {`
  - `SOUMISE: 'badge bg-orange',`
  - `VALIDEE_SECRETAIRE: 'badge bg-success',`
  - `VALIDEE_COMPTABLE: 'badge bg-success',`
  - `id: pc.numeroPreCommande,`
  - `commercialId: pc.matriculeCommercial,`
  - `statut: pc.statut,`
  - `signatureSecretaire: !!pc.matriculeValidateurSecretaire,`
  - `signatureComptable: !!pc.matriculeValidateurComptable,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `valider(id: string): void `
  - `getCommercialNom(id: string): string `
  - `getProduitDesignation(code: string): string `
  - `statutClass(s: string): string `
  - `mapPC(pc: PreCommandeBE): CommandeUI `


#### `app\domains\secretaire\presentation\pages\dashboard\secretaire-dashboard.component.ts`

**class** `SecretaireDashboardComponent` extends/implements `OnInit `

- **Propriétés :**
  - `next: d => {`

- **Méthodes :**
  - `ngOnInit(): void `


#### `app\domains\secretaire\presentation\pages\session\secretaire-session.component.ts`

**class** `SecretaireSessionComponent` extends/implements `OnInit `

- **Propriétés :**
  - `depensesJustifiees: 0, depensesNonJust: 0,`
  - `montantAvaries: 0, montantDotPromo: 0,`
  - `eCheques: 0, vstBanques: 0, vacInit: vac,`
  - `id: v.referenceVersement,`
  - `commercialId: v.matriculeCommercial,`
  - `date: d,`
  - `montantAttendu: v.montantAttendu,`
  - `cashVerse: v.cashVerse,`
  - `ecart: v.ecart,`
  - `statut: v.statut,`
  - `typeVersement: v.typeVersement ?? null,`
  - `alerteRouge: v.alerteRouge,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `cloturerSession(): void `
  - `getCommercialNom(id: string): string `
  - `ecartClass(): string `
  - `if(e > 0): void`
  - `if(e < 0): void`
  - `mapV(v: VersementBE): VersementUI `
  - `fmtDate(d: Date): string `


#### `app\domains\secretaire\presentation\pages\validation\secretaire-validation.component.ts`

**class** `SecretaireValidationComponent` extends/implements `OnInit `

- **Propriétés :**
  - `EN_ATTENTE_DG: 'badge bg-orange',`
  - `DG_APPROUVE: 'badge bg-neutral',`
  - `VALIDE: 'badge bg-success',`

- **Méthodes :**
  - `ngOnInit(): void `
  - `statutDecClass(s: string): string `


#### `app\domains\secretaire\presentation\pages\versements\secretaire-versements.component.ts`

**class** `SecretaireVersementsComponent` extends/implements `OnInit `

- **Propriétés :**
  - `montantSaisis: Record<string, number> = {}`
  - `typesSaisis: Record<string, string> = {}`
  - `motifsSaisis: Record<string, string> = {}`
  - `matriculeCommercial: id,`
  - `montantAttendu: verse,`
  - `cashVerse: verse,`
  - `typeVersement: type,`
  - `next: v => {`
  - `VALIDE: 'badge bg-success',`
  - `EN_ATTENTE: 'badge bg-neutral',`
  - `EN_ATTENTE_VALIDATION: 'badge bg-orange',`
  - `ECART_JUSTIFIE: 'badge bg-orange',`
  - `id: v.referenceVersement,`
  - `commercialId: v.matriculeCommercial,`
  - `date: d,`
  - `montantAttendu: v.montantAttendu,`
  - `cashVerse: v.cashVerse,`
  - `ecart: v.ecart,`
  - `motifEcart: v.justificationEcart ?? null,`
  - `statut: v.statut,`
  - `typeVersement: v.typeVersement ?? null,`
  - `alerteRouge: v.alerteRouge,`

- **Méthodes :**
  - `ngOnInit(): void `
  - `valider(id: string): void `
  - `getCommercialNom(id: string): string `
  - `ecartCalc(attendu: number, verse: number): number `
  - `statutClass(s: string): string `
  - `mapV(v: VersementBE): VersementUI `
  - `fmtDate(d: Date): string `


### Domaine `shared`

#### `app\domains\shared\infrastructure\document.service.ts`

**class** `DocumentService`

- **Propriétés :**
  - `params: { typeDocument, reference },`
  - `responseType: 'blob',`

- **Méthodes :**
  - `importer(typeDocument: string, reference: string, file: File): Observable<string> `
  - `recuperer(typeDocument: string, reference: string): Observable<Blob> `


#### `app\shared\preloader\preloader.component.ts`

**class** `PreloaderComponent` extends/implements `OnInit `

- **Méthodes :**
  - `ngOnInit(): void`
  - `setTimeout((): void`


#### `app\shared\ui\toast\toast-container.component.ts`

**class** `ToastContainerComponent`

- **Méthodes :**
  - `iconFor(type: ToastType): string `
  - `switch(type): void`


#### `app\shared\ui\toast\toast.service.ts`

**interface** `Toast`

- **Propriétés :**
  - `id: number`
  - `type: ToastType`
  - `message: string`
  - `duration: number`

**class** `ToastService`

- **Méthodes :**
  - `success(message: string, duration = 4000): void `
  - `error(message: string, duration = 5000): void `
  - `info(message: string, duration = 4000): void `
  - `warning(message: string, duration = 4000): void `
  - `show(message: string, type: ToastType = 'info', duration = 4000): void `
  - `if(duration > 0): void`
  - `setTimeout((): void`
  - `dismiss(id: number): void `
  - `clear(): void `


### Domaine `stock`

#### `app\domains\stock\application\use-cases\commercial\commercial.use-case.ts`

**class** `CommercialUseCase`

- **Méthodes :**
  - `getCommercials(): Observable<Commercial[]>    `
  - `getInfoProduits(): Observable<InfoProduits[]> `


#### `app\domains\stock\application\use-cases\dashboard\get-alertes.use-case.ts`

**class** `GetAlertesUseCase`

- **Méthodes :**
  - `execute(): Observable<StockLevel[]> `


#### `app\domains\stock\application\use-cases\dashboard\get-dashboard-stats.use-case.ts`

**class** `GetDashboardStatsUseCase`

- **Méthodes :**
  - `execute(): Observable<DashboardStatsResponse> `


#### `app\domains\stock\application\use-cases\dashboard\get-dashboard.use-case.ts`

**class** `GetDashboardUseCase`

- **Méthodes :**
  - `getStats(): Observable<DashboardStatsResponse> `
  - `getAlerts(): Observable<StockLevel[]>           `
  - `getMovements(): Observable<StockMovement[]>     `


#### `app\domains\stock\application\use-cases\inventaire\ajuster-stock.use-case.ts`

**class** `AjusterStockUseCase`

- **Méthodes :**
  - `execute(stockLevelId: number, newQty: number, motif: string): Observable<StockLevel> `


#### `app\domains\stock\application\use-cases\inventaire\get-produits.use-case.ts`

**class** `GetProduitsUseCase`

- **Méthodes :**
  - `execute(): Observable<StockLevel[]> `
  - `executeByWarehouse(warehouseId: number): Observable<StockLevel[]> `


#### `app\domains\stock\application\use-cases\inventaire\stock-level.use-case.ts`

**class** `StockLevelUseCase`

- **Méthodes :**
  - `getAll(): Observable<StockLevel[]>   `
  - `getAlerts(): Observable<StockLevel[]>`
  - `getWarehouses(): Observable<Warehouse[]> `
  - `adjust(id: number, qty: number, reason: string): Observable<StockLevel> `
  - `getMovements(): Observable<StockMovement[]> `


#### `app\domains\stock\application\use-cases\inventaire\transfert-tampon.use-case.ts`

**class** `TransfertTamponUseCase`

- **Méthodes :**
  - `execute(stockLevelId: number, quantite: number): Observable<StockLevel> `


#### `app\domains\stock\application\use-cases\mouvement\get-mouvements.use-case.ts`

**class** `GetMouvementsUseCase`

- **Méthodes :**
  - `execute(): Observable<StockMovement[]> `
  - `executeByProduit(produitId: number): Observable<StockMovement[]> `


#### `app\domains\stock\application\use-cases\orders\internal-order.use-case.ts`

**class** `InternalOrderUseCase`

- **Méthodes :**
  - `getAll(): Observable<InternalOrder[]>     `
  - `getActive(): Observable<InternalOrder[]>  `
  - `create(d: Partial<InternalOrder>): Observable<InternalOrder> `
  - `approve(id: number): Observable<InternalOrder> `
  - `getFinishedProducts(): Observable<Product[]> `


#### `app\domains\stock\application\use-cases\production\get-production.use-case.ts`

**class** `GetProductionUseCase`

- **Méthodes :**
  - `execute(): Observable<ProductionBatch[]> `
  - `valider(numeroLot: string): Observable<ProductionBatch> `


#### `app\domains\stock\application\use-cases\production\production-batch.use-case.ts`

**class** `ProductionBatchUseCase`

- **Méthodes :**
  - `getAll(): Observable<ProductionBatch[]>    `
  - `getPending(): Observable<ProductionBatch[]>`
  - `declare(d: Partial<ProductionBatch>): Observable<ProductionBatch> `
  - `validate(numeroLot: string): Observable<ProductionBatch> `
  - `reject(numeroLot: string, reason: string): Observable<ProductionBatch>  `
  - `getFinishedProducts(): Observable<Product[]> `


#### `app\domains\stock\application\use-cases\reception\create-reception.use-case.ts`

**class** `CreateReceptionUseCase`

- **Méthodes :**
  - `execute(data: Partial<Receipt>): Observable<Receipt> `


#### `app\domains\stock\application\use-cases\reception\get-fournisseurs.use-case.ts`

**class** `GetFournisseursUseCase`

- **Méthodes :**
  - `execute(): Observable<Supplier[]> `


#### `app\domains\stock\application\use-cases\reception\get-receptions.use-case.ts`

**class** `GetReceptionsUseCase`

- **Méthodes :**
  - `execute(): Observable<Receipt[]> `
  - `executeEnAttente(): Observable<Receipt[]> `


#### `app\domains\stock\application\use-cases\reception\receipt.use-case.ts`

**class** `ReceiptUseCase`

- **Méthodes :**
  - `getAll(): Observable<Receipt[]>  `
  - `getPending(): Observable<Receipt[]>  `
  - `create(d: Partial<Receipt>): Observable<Receipt>   `
  - `validateFirst(numero: string, notes: string): Observable<Receipt> `
  - `validateSecond(numero: string, notes: string): Observable<Receipt> `
  - `reject(numero: string, reason: string): Observable<Receipt> `
  - `getSuppliers(): Observable<Supplier[]> `
  - `getWarehouses(): Observable<Warehouse[]> `


#### `app\domains\stock\application\use-cases\reception\validate-reception.use-case.ts`

**class** `ValidateReceptionUseCase`

- **Méthodes :**
  - `executeGestionnaire(numero: string, obs?: string): Observable<Receipt> `
  - `executeChefProduction(numero: string, obs?: string): Observable<Receipt> `
  - `executeRejet(numero: string, motif: string): Observable<Receipt> `


#### `app\domains\stock\domain\models\stock.models.ts`

**interface** `User`

- **Propriétés :**
  - `id: number`
  - `matricule: string`
  - `firstName: string`
  - `lastName: string`
  - `email: string`
  - `role: Role`
  - `fullName: string`

**interface** `Brand`

**interface** `ProductLine`

**interface** `ProductVariant`

**interface** `Product`

- **Propriétés :**
  - `id: number`
  - `sku: string`
  - `variantId: number`
  - `barcode: string`
  - `category: Category`
  - `unit: UnitType`
  - `unitPriceAmount: number`
  - `leadTimeDays: number`
  - `safetyStockDays: number`
  - `active: boolean`
  - `variant: ProductVariant`
  - `designation: string`

**interface** `Warehouse`

**interface** `StockLevel`

- **Propriétés :**
  - `id: number`
  - `productId: number`
  - `warehouseId: number`
  - `quantity: number`
  - `reservedQty: number`
  - `reorderPoint: number`
  - `safetyStock: number`
  - `lastUpdated: string`
  - `product: Product`
  - `warehouse: Warehouse`
  - `availableQty: number`
  - `alertLevel: AlertLevel`
  - `stockValue: number`
  - `productName: string`
  - `productSku: string`
  - `productUnit: string`
  - `productCategory: string`
  - `warehouseName: string`
  - `warehouseType: string`
  - `unitPrice: number`
  - `leadTimeDays: number`
  - `variantName: string`
  - `productLineName: string`
  - `brandName: string`

**interface** `Supplier`

- **Propriétés :**
  - `id: number`
  - `name: string`
  - `leadTimeDays: number`
  - `pays: string`
  - `telephone: string`
  - `email: string`

**interface** `ReceiptItem`

- **Propriétés :**
  - `id: number`
  - `productId: number`
  - `productName: string`
  - `productSku: string`
  - `productUnit: string`
  - `orderedQty: number`
  - `receivedQty: number`
  - `deviation: number`
  - `deviationReason: string`
  - `lotNumber: string`
  - `unitPrice: number`
  - `lineTotal: number`

**interface** `Receipt`

- **Propriétés :**
  - `id: number`
  - `receiptNumber: string`
  - `source: SourceType`
  - `sourceId: number`
  - `receiptDate: string`
  - `warehouseId: number`
  - `warehouseName: string`
  - `status: ReceiptStatus`
  - `firstValidatorId: number`
  - `firstValidatorName: string`
  - `firstValidatedAt: string`
  - `firstValidatorNotes: string`
  - `secondValidatorId: number`
  - `secondValidatorName: string`
  - `secondValidatedAt: string`
  - `secondValidatorNotes: string`
  - `rejectionReason: string`
  - `totalAmount: number`
  - `createdAt: string`
  - `createdByName: string`
  - `items: ReceiptItem[]`
  - `warehouse: Warehouse`
  - `fournisseur: Supplier`

**interface** `ProductionBatch`

- **Propriétés :**
  - `id: number`
  - `numeroLot: string`
  - `productId: number`
  - `productName: string`
  - `productSku: string`
  - `productUnit: string`
  - `declaredQuantityKg: number`
  - `equivalentUnits: number`
  - `productionDate: string`
  - `batchDate: string`
  - `status: ProductionStatus`
  - `declaredBy: number`
  - `declaredByName: string`
  - `stockValidator: number`
  - `stockValidatorName: string`
  - `createdAt: string`
  - `validatedAt: string`
  - `notes: string`

**interface** `InternalOrderItem`

- **Propriétés :**
  - `id: number`
  - `productId: number`
  - `productName: string`
  - `productSku: string`
  - `productUnit: string`
  - `requestedQty: number`
  - `deliveredQty: number`
  - `fullyDelivered: boolean`
  - `notes: string`

**interface** `InternalOrder`

- **Propriétés :**
  - `id: number`
  - `orderNumber: string`
  - `orderDate: string`
  - `status: InternalOrderStatus`
  - `requestedBy: number`
  - `requestedByName: string`
  - `approvedBy: number`
  - `approvedByName: string`
  - `createdAt: string`
  - `approvedAt: string`
  - `notes: string`
  - `items: InternalOrderItem[]`

**interface** `Commercial`

**interface** `CommercialStock`

**interface** `InfoProduitsLine`

- **Propriétés :**
  - `id: number`
  - `productId: number`
  - `product: Product`
  - `takenQty: number`
  - `soldQty: number`
  - `returnedQty: number`
  - `unitPrice: number`

**interface** `InfoProduits`

- **Propriétés :**
  - `id: number`
  - `commercialId: number`
  - `commercial: Commercial`
  - `date: string`
  - `status: InfoStatus`
  - `preparedBy: number`
  - `cashierValidatedBy: number`
  - `accountantValidatedBy: number`
  - `createdAt: string`
  - `lines: InfoProduitsLine[]`
  - `montantTotal: number`

**interface** `DailySession`

- **Propriétés :**
  - `id: number`
  - `date: string`
  - `status: SessionStatus`
  - `openedBy: number`
  - `closedBy: number`
  - `totalCashExpected: number`
  - `totalCashActual: number`

**interface** `StockMovement`

- **Propriétés :**
  - `id: number`
  - `type: MovementType`
  - `productId: number`
  - `warehouseId: number`
  - `quantity: number`
  - `previousStock: number`
  - `newStock: number`
  - `reference: string`
  - `createdBy: number`
  - `createdAt: string`
  - `notes: string`
  - `product: Product`
  - `warehouse: Warehouse`

**interface** `DashboardStatsResponse`

- **Propriétés :**
  - `totalStockLevels: number`
  - `totalStockValue: number`
  - `criticalAlerts: number`
  - `lowAlerts: number`
  - `pendingReceipts: number`
  - `todayReceipts: number`
  - `todayMovements: number`
  - `pendingBatches: number`
  - `activeInternalOrders: number`
  - `todaySales: number`

**interface** `StockAlert`

- **Propriétés :**
  - `produitId: number`
  - `designation: string`
  - `magasin: string`
  - `stockActuel: number`
  - `stockMinimum: number`
  - `niveau: AlertLevel`
  - `leadTimeDays: number`


#### `app\domains\stock\domain\services\stock-rules.domain.service.ts`

**class** `StockRulesDomainService`

- **Propriétés :**
  - `SACHET_42G: 42, SEAU_1L: 1000, SEAU_5L: 5000, SEAU_10L: 10000, KG: 1000`

- **Méthodes :**
  - `requiresSecondValidation(warehouseName: string): boolean `
  - `calcAlertLevel(sl: StockLevel): AlertLevel `
  - `if(sl.quantity <= sl.safetyStock): void`
  - `if(sl.quantity <= sl.reorderPoint): void`
  - `if(sl.quantity >= sl.reorderPoint * 3): void`
  - `calcBatchDate(productionDate: Date): Date `
  - `kgToUnits(kg: number, unit: string): number `
  - `needsUrgentOrder(sl: StockLevel, leadTimeDays?: number): boolean `
  - `formatCFA(n: number): string `


#### `app\domains\stock\infrastructure\repositories\stock-api.repository.ts`

**class** `StockApiRepository`

- **Propriétés :**
  - `id: p.id,`
  - `sku: p.code,`
  - `unit: 'KG',`
  - `unitPriceAmount: base?.prixUnitaireHT ?? 0,`
  - `active: p.actif,`
  - `designation: p.designation,`
  - `default: return 'RAW_MATERIAL'`
  - `id: f.id,`
  - `name: f.raisonSociale,`
  - `leadTimeDays: 0,`
  - `telephone: f.telephone ?? undefined,`
  - `email: f.email ?? undefined,`
  - `totalStockLevels: d?.totalLignesStock ?? 0,`
  - `totalStockValue: d?.valeurTotaleStock ?? 0,`
  - `criticalAlerts: d?.alertesCritiques ?? 0,`
  - `lowAlerts: d?.alertesFaibles ?? 0,`
  - `pendingReceipts: d?.receptionsEnAttente ?? 0,`
  - `todayReceipts: d?.receptionsDuJour ?? 0,`
  - `todayMovements: d?.mouvementsDuJour ?? 0,`
  - `pendingBatches: d?.lotsEnAttente ?? 0,`
  - `activeInternalOrders: d?.commandesInternesActives ?? 0,`
  - `todaySales: d?.ventesDuJour ?? 0,`
  - `id: n.id,`
  - `productId: n.produitId ?? 0,`
  - `warehouseId: n.entrepotId ?? 0,`
  - `quantity: n.quantite,`
  - `reservedQty: 0,`
  - `reorderPoint: n.seuilReapprovisionnement,`
  - `safetyStock: n.stockSecurite,`
  - `lastUpdated: n.dateModification ?? '',`
  - `availableQty: n.quantite,`
  - `alertLevel: n.niveauAlerte as AlertLevel,`
  - `stockValue: n.valeurStock,`
  - `productName: n.designation,`
  - `productSku: n.sku,`
  - `productCategory: n.categorie ?? undefined,`
  - `warehouseName: n.libelleEntrepot,`
  - `warehouseType: n.typeEntrepot,`
  - `brandName: n.marque ?? undefined,`
  - `productLineName: n.gamme ?? undefined,`
  - `id: idx,`
  - `productId: 0,`
  - `productName: l.codeProduit,`
  - `productSku: l.sku,`
  - `orderedQty: l.quantiteAttendue,`
  - `receivedQty: l.quantiteRecue,`
  - `deviation: l.ecart,`
  - `id: r.id,`
  - `receiptNumber: r.numero,`
  - `receiptDate: r.dateReception,`
  - `warehouseId: 0,`
  - `firstValidatorName: r.matriculeValidateur1 ?? undefined,`
  - `secondValidatorName: r.matriculeValidateur2 ?? undefined,`
  - `rejectionReason: r.motifRejet ?? undefined,`
  - `totalAmount: 0,`
  - `createdAt: r.dateReception,`
  - `default: return 'PENDING_FIRST_VALIDATION'`
  - `referencePPH: data.referencePPH ?? '',`
  - `codeProduit: data.codeProduit ?? data.productSku ?? '',`
  - `designationProduit: data.designationProduit ?? data.productName ?? '',`
  - `quantiteKg: data.declaredQuantityKg ?? data.quantiteKg ?? 0,`
  - `nbCartons: data.nbCartons ?? 0,`
  - `dateProduction: data.productionDate ?? data.dateProduction,`
  - `dlc: data.dlc ?? data.productionDate ?? data.dateProduction,`
  - `id: l.id,`
  - `numeroLot: l.numeroLot,`
  - `productId: 0,`
  - `productName: l.designationProduit,`
  - `productSku: l.codeProduit,`
  - `productUnit: 'KG',`
  - `declaredQuantityKg: l.quantiteKg,`
  - `equivalentUnits: l.nbCartons,`
  - `productionDate: l.dateProduction,`
  - `batchDate: l.dlc,`
  - `declaredBy: 0,`
  - `declaredByName: l.matriculeChefProduction,`
  - `stockValidatorName: l.matriculeGestionnaireValidation,`
  - `createdAt: l.dateProduction,`
  - `notes: l.motifRejet,`
  - `id: e.id,`
  - `name: e.libelle,`
  - `type: e.type === 'CENTRAL' ? 'CENTRAL' : 'BUFFER',`
  - `isBuffer: e.type !== 'CENTRAL',`
  - `id: m.id,`
  - `productId: 0,`
  - `warehouseId: 0,`
  - `quantity: m.quantiteEnBase,`
  - `previousStock: 0,`
  - `newStock: 0,`
  - `reference: m.reference,`
  - `createdBy: 0,`
  - `createdAt: m.horodatage ?? '',`
  - `notes: m.motif ?? undefined,`
  - `default: return 'ADJUSTMENT'`

- **Méthodes :**
  - `getHierarchie(): Observable<HierarchieBackend> `
  - `if(!this.hierarchie$): void`
  - `map((r): void`
  - `shareReplay(1): void`
  - `getProducts(): Observable<Product[]> `
  - `getBrands(): Observable<Brand[]> `
  - `getLines(): Observable<ProductLine[]> `
  - `getVariants(): Observable<ProductVariant[]> `
  - `mapProduit(p: ProduitBackend): Product `
  - `mapCategorie(c: string | null): Category `
  - `switch(c): void`
  - `getWarehouses(): Observable<Warehouse[]> `
  - `getSuppliers(): Observable<Supplier[]> `
  - `getDashboard(): Observable<DashboardStatsResponse> `
  - `map((res): void`
  - `getStockLevels(): Observable<StockLevel[]> `
  - `getStockLevelsByWarehouse(id: number): Observable<StockLevel[]> `
  - `getAlerts(): Observable<StockLevel[]> `
  - `getCriticalAlerts(): Observable<StockLevel[]> `
  - `adjustStock(stockLevelId: number, newQuantity: number, reason: string): Observable<StockLevel> `
  - `transferToBuffer(id: number, quantity: number): Observable<StockLevel> `
  - `mapNiveau(n: NiveauDetailBackend): StockLevel `
  - `getReceipts(): Observable<Receipt[]> `
  - `getPendingReceipts(): Observable<Receipt[]> `
  - `getReceiptByNumber(numero: string): Observable<Receipt> `
  - `validateFirst(numero: string, _notes = ''): Observable<Receipt> `
  - `validateSecond(numero: string, _notes = ''): Observable<Receipt> `
  - `rejectReceipt(numero: string, reason: string): Observable<Receipt> `
  - `createReceipt(data: any): Observable<Receipt> `
  - `mapReception(r: ReceptionBackend): Receipt `
  - `mapStatutReception(statut: string): ReceiptStatus `
  - `switch(statut): void`
  - `getBatches(): Observable<ProductionBatch[]> `
  - `getPendingBatches(): Observable<ProductionBatch[]> `
  - `getBatchById(id: number): Observable<ProductionBatch> `
  - `validateBatch(numeroLot: string): Observable<ProductionBatch> `
  - `rejectBatch(numeroLot: string, reason: string): Observable<ProductionBatch> `
  - `declareBatch(data: any): Observable<ProductionBatch> `
  - `mapLot(l: LotBackend): ProductionBatch `
  - `mapStatutLot(statut: string): ProductionStatus `
  - `switch(statut): void`
  - `getOrders(): Observable<InternalOrder[]> `
  - `getActiveOrders(): Observable<InternalOrder[]> `
  - `createOrder(data: any): Observable<InternalOrder> `
  - `approveOrder(id: number): Observable<InternalOrder> `
  - `cancelOrder(id: number): Observable<InternalOrder> `
  - `getMovements(): Observable<StockMovement[]> `
  - `mapWarehouse(e: EntrepotBackend): Warehouse `
  - `mapMovement(m: MouvementBackend): StockMovement `
  - `mapTypeMouvement(t: string): MovementType `
  - `switch(t): void`
  - `getCommercials(): Observable<Commercial[]> `
  - `getInfoProduits(date?: string): Observable<InfoProduits[]> `


#### `app\domains\stock\infrastructure\repositories\stock-mock.repository.ts`

**class** `StockMockRepository`

- **Propriétés :**
  - `readonly warehouses: Warehouse[] = [`
  - `readonly brands: Brand[] = [`
  - `readonly lines: ProductLine[] = [`
  - `readonly variants: ProductVariant[] = [`
  - `readonly products: Product[] = [`
  - `private _stockLevels: StockLevel[] = [`
  - `readonly suppliers: Supplier[] = [`
  - `readonly commercials: Commercial[] = [`
  - `private _receipts: Receipt[] = [`
  - `id: 1, receiptNumber:'REC-2026-0042', source:'SUPPLIER', sourceId:1,`
  - `receiptDate: '2026-05-07', warehouseId:1, warehouseName:'Magasin Matières Premières',`
  - `status: 'PENDING_FIRST_VALIDATION', totalAmount:306500, createdAt:'2026-05-07T08:30:00',`
  - `warehouse: this.warehouses[0], fournisseur: this.suppliers[0],`
  - `items: [`
  - `id: 2, receiptNumber:'REC-2026-0041', source:'SUPPLIER', sourceId:2,`
  - `receiptDate: '2026-05-06', warehouseId:2, warehouseName:'Magasin Consommables & Emballages',`
  - `status: 'PENDING_SECOND_VALIDATION', totalAmount:336400, createdAt:'2026-05-06T14:00:00',`
  - `firstValidatorId: 1, firstValidatorName:'Mvondo Jean-Baptiste', firstValidatedAt:'2026-05-06',`
  - `warehouse: this.warehouses[1], fournisseur: this.suppliers[1],`
  - `items: [`
  - `id: 3, receiptNumber:'REC-2026-0040', source:'SUPPLIER', sourceId:5,`
  - `receiptDate: '2026-05-05', warehouseId:1, warehouseName:'Magasin Matières Premières',`
  - `status: 'VALIDATED', totalAmount:183000, createdAt:'2026-05-05T10:15:00',`
  - `firstValidatorId: 1, firstValidatorName:'Mvondo Jean-Baptiste', firstValidatedAt:'2026-05-05',`
  - `secondValidatorId: 2, secondValidatorName:'M. Clive Nkomo', secondValidatedAt:'2026-05-05',`
  - `warehouse: this.warehouses[0], fournisseur: this.suppliers[4],`
  - `items: [{ id:5, productId:2, productName:'Soja décortiqué', productSku:'MP-SOJA-001', productUnit:'KG', orderedQty:300, receivedQty:305, deviation:5, deviationReason:'Surplus fournisseur', lotNumber:'SOJA-CI-0505', unitPrice:600, lineTotal:183000 }]`
  - `id: 4, receiptNumber:'REC-2026-0039', source:'PRODUCTION', sourceId:1,`
  - `receiptDate: '2026-05-04', warehouseId:3, warehouseName:'Magasin Produits Finis',`
  - `status: 'VALIDATED', totalAmount:327250, createdAt:'2026-05-04T09:00:00',`
  - `firstValidatorId: 1, firstValidatorName:'M. Clive Nkomo', firstValidatedAt:'2026-05-04',`
  - `secondValidatorId: 1, secondValidatorName:'Mvondo Jean-Baptiste', secondValidatedAt:'2026-05-04',`
  - `warehouse: this.warehouses[2],`
  - `items: [{ id:6, productId:9, productName:'TBSA 42g', productSku:'PF-TBSA-042', productUnit:'SACHET_42G', orderedQty:1309, receivedQty:1309, deviation:0, lotNumber:'PROD-2026-05-01', unitPrice:250, lineTotal:327250 }]`
  - `private _batches: ProductionBatch[] = [`
  - `private _orders: InternalOrder[] = [`
  - `id: 1, orderNumber:'CMD-INT-0100', orderDate:'2026-05-08', status:'APPROVED',`
  - `requestedBy: 1, requestedByName:'Mvondo Jean-Baptiste',`
  - `approvedBy: 2, approvedByName:'M. Clive Nkomo',`
  - `createdAt: '2026-05-08T09:00:00', approvedAt:'2026-05-08T10:30:00',`
  - `notes: 'Production semaine du 11 mai',`
  - `items: [`
  - `id: 2, orderNumber:'CMD-INT-0099', orderDate:'2026-05-06', status:'DRAFT',`
  - `requestedBy: 1, requestedByName:'Mvondo Jean-Baptiste',`
  - `createdAt: '2026-05-06T14:00:00', notes:'Préparation stock salon',`
  - `items: [`
  - `private _movements: StockMovement[] = [`
  - `private _infoProduits: InfoProduits[] = [`
  - `lines: [`
  - `montantTotal: 45*250+7*3500`
  - `lines: [`
  - `montantTotal: 80*250+32*250`
  - `product: p, warehouse: w,`
  - `productName: p?.designation || p?.sku || '—',`
  - `productSku: p?.sku || '—',`
  - `productUnit: p?.unit || '—',`
  - `productCategory: p?.category || '—',`
  - `unitPrice: p?.unitPriceAmount || 0,`
  - `leadTimeDays: p?.leadTimeDays,`
  - `warehouseName: w?.name || '—',`
  - `warehouseType: w?.type || '—',`
  - `variantName: v?.name,`
  - `productLineName: l?.name,`
  - `brandName: b?.name,`
  - `availableQty: sl.quantity - sl.reservedQty,`
  - `totalStockLevels: levels.length,`
  - `todayReceipts: 2,`
  - `todaySales: 74250,`
  - `warehouseId: data.warehouseId!, status: 'PENDING_FIRST_VALIDATION',`
  - `productSku: p?.sku, productUnit: p?.unit,`
  - `declaredQuantityKg: data.declaredQuantityKg!, equivalentUnits: units,`
  - `status: 'DECLARED_BY_PRODUCTION', declaredBy: 2, declaredByName: 'M. Clive Nkomo',`

- **Méthodes :**
  - `enrichSL(sl: StockLevel): StockLevel `
  - `getWarehouses(): Observable<Warehouse[]>         `
  - `getBrands(): Observable<Brand[]>             `
  - `getLines(): Observable<ProductLine[]>       `
  - `getVariants(): Observable<ProductVariant[]>    `
  - `getProducts(): Observable<Product[]>           `
  - `getSuppliers(): Observable<Supplier[]>          `
  - `getCommercials(): Observable<Commercial[]>        `
  - `getInfoProduits(): Observable<InfoProduits[]>      `
  - `getStockLevels(): Observable<StockLevel[]> `
  - `getStockLevelsByWarehouse(wid: number): Observable<StockLevel[]> `
  - `getAlerts(): Observable<StockLevel[]> `
  - `getDashboard(): Observable<DashboardStatsResponse> `
  - `getReceipts(): Observable<Receipt[]> `
  - `getPendingReceipts(): Observable<Receipt[]> `
  - `createReceipt(data: Partial<Receipt>): Observable<Receipt> `
  - `validateFirst(id: number, notes: string): Observable<Receipt> `
  - `if(r): void`
  - `if(!needsSecond): void`
  - `validateSecond(id: number, notes: string): Observable<Receipt> `
  - `if(r): void`
  - `rejectReceipt(id: number, reason: string): Observable<Receipt> `
  - `if(r): void`
  - `_updateStockFromReceipt(r: Receipt): void `
  - `if(sl): void`
  - `getBatches(): Observable<ProductionBatch[]> `
  - `getPendingBatches(): Observable<ProductionBatch[]> `
  - `declareBatch(data: Partial<ProductionBatch>): Observable<ProductionBatch> `
  - `validateBatch(id: number): Observable<ProductionBatch> `
  - `if(b): void`
  - `if(sl): void`
  - `rejectBatch(id: number, reason: string): Observable<ProductionBatch> `
  - `if(b): void`
  - `getOrders(): Observable<InternalOrder[]> `
  - `getActiveOrders(): Observable<InternalOrder[]> `
  - `createOrder(data: Partial<InternalOrder>): Observable<InternalOrder> `
  - `approveOrder(id: number): Observable<InternalOrder> `
  - `if(o): void`
  - `getMovements(): Observable<StockMovement[]> `
  - `adjustStock(slId: number, newQty: number, reason: string): Observable<StockLevel> `
  - `if(sl): void`


#### `app\domains\stock\infrastructure\stock.service.ts`

**interface** `TableauBordStockBE`

- **Propriétés :**
  - `valeurStockCentral: number`
  - `valeurStockTampon: number`
  - `valeurStockMobile: number`
  - `nbAlertesActives: number`
  - `nbReceptionsEnAttente: number`
  - `nbMouvementsJour: number`
  - `nbLotsEnAttente: number`

**interface** `TableauBordEnrichiBE`

- **Propriétés :**
  - `stats: Record<string, number>`
  - `tampon: { produit: string`
  - `stockCentral: { produit: string`
  - `stockMobile: { matricule: string`
  - `performances: { matricule: string`
  - `invendus: { produit: string`

**interface** `NiveauStockBE`

- **Propriétés :**
  - `id: number`
  - `produitId: number | null`
  - `codeProduit: string`
  - `sku: string`
  - `designation: string`
  - `marque: string | null`
  - `gamme: string | null`
  - `categorie: string | null`
  - `entrepotId: number | null`
  - `codeEntrepot: string`
  - `libelleEntrepot: string`
  - `typeEntrepot: string`
  - `quantite: number`
  - `seuilReapprovisionnement: number`
  - `stockSecurite: number`
  - `niveauAlerte: string`
  - `valeurStock: number`
  - `dateModification: string | null`

**interface** `MouvementBE`

- **Propriétés :**
  - `id: number`
  - `date: string`
  - `type: string`
  - `entrepot: string`
  - `codeProduit: string`
  - `designation: string`
  - `quantite: number`
  - `unite: string`
  - `referenceDocument: string`
  - `matriculeOperateur: string`

**interface** `LigneInventaireBE`

- **Propriétés :**
  - `codeProduit: string`
  - `designation: string`
  - `quantiteTheorique: number`
  - `quantiteComptee: number`
  - `ecart: number`
  - `unite: string`

**interface** `InventaireBE`

- **Propriétés :**
  - `id: number`
  - `numero: string`
  - `type: string`
  - `entrepot: string`
  - `statut: string`
  - `dateOuverture: string`
  - `dateCloture: string`
  - `matriculeResponsable: string`
  - `lignes: LigneInventaireBE[]`

**interface** `LigneReceptionBE`

- **Propriétés :**
  - `codeProduit: string`
  - `designation: string`
  - `quantite: number`
  - `unite: string`

**interface** `ReceptionBE`

- **Propriétés :**
  - `id: number`
  - `numero: string`
  - `type: string`
  - `statut: string`
  - `bonCommande: string`
  - `matriculeResponsable: string`
  - `dateReception: string`
  - `lignes: LigneReceptionBE[]`

**interface** `AlerteBE`

- **Propriétés :**
  - `id: number`
  - `reference: string`
  - `type: string`
  - `typeLibelle: string`
  - `message: string`
  - `destinataireRole: string`
  - `actionSuggeree: string`
  - `priorite: string`
  - `codeProduit: string`
  - `codeEntrepot: string`
  - `referenceLiee: string`
  - `statut: string`
  - `dateCreation: string`
  - `dateTraitement: string | null`

**interface** `SessionCommercialBE`

- **Propriétés :**
  - `id: number`
  - `numero: string`
  - `matriculeCommercial: string`
  - `datePreparation: string`
  - `dateEffet: string`
  - `statut: string`
  - `dotations: string[]`
  - `matriculeValidateurSecretaire: string`
  - `matriculeValidateurComptable: string`

**class** `StockService`

- **Propriétés :**
  - `produit: string`
  - `document: string`
  - `type: string`
  - `codeProduit: string`
  - `bonCommande: string`
  - `lotProduction: string`
  - `matriculeCommercial: string`
  - `entrepot: string`

- **Méthodes :**
  - `getDashboard(): Observable<TableauBordStockBE> `
  - `getDashboardEnrichi(): Observable<TableauBordEnrichiBE> `
  - `getEtatStock(params: { entrepot?: string } = {}): Observable<NiveauStockBE[]> `
  - `if(params.entrepot): void`
  - `if(params.produit): void`
  - `if(params.entrepot): void`
  - `if(params.type): void`
  - `if(params.document): void`
  - `if(params.debut): void`
  - `if(params.fin): void`
  - `if(page?.page !== undefined): void`
  - `if(page?.size !== undefined): void`
  - `if(page?.sort): void`
  - `getInventaires(statut?: string): Observable<InventaireBE[]> `
  - `if(statut): void`
  - `getInventaire(numero: string): Observable<InventaireBE> `
  - `cloturerComptage(numero: string): Observable<InventaireBE> `
  - `validerInventaire(numero: string, justification?: string): Observable<InventaireBE> `
  - `if(justification): void`
  - `getReceptions(params: { statut?: string; bonCommande?: string } = {}): Observable<ReceptionBE[]> `
  - `if(params.statut): void`
  - `if(params.bonCommande): void`
  - `getReception(numero: string): Observable<ReceptionBE> `
  - `validerReceptionEtape1(numero: string): Observable<ReceptionBE> `
  - `validerReceptionEtape2(numero: string): Observable<ReceptionBE> `
  - `rejeterReception(numero: string, motif: string): Observable<ReceptionBE> `
  - `getAlertes(params: { statut?: string; role?: string } = {}): Observable<AlerteBE[]> `
  - `if(params.statut): void`
  - `if(params.role): void`
  - `getStockLevelAlertes(): Observable<NiveauStockBE[]> `
  - `getStockLevelAlertesCritiques(): Observable<NiveauStockBE[]> `
  - `getStockLevelsByWarehouse(entrepotId: number): Observable<NiveauStockBE[]> `
  - `adjustStock(stockLevelId: number, newQuantity: number, reason: string): Observable<NiveauStockBE> `
  - `transferToBuffer(id: number, quantity: number): Observable<NiveauStockBE> `
  - `marquerAlerteLue(reference: string): Observable<AlerteBE> `
  - `traiterAlerte(reference: string): Observable<AlerteBE> `
  - `getSessionsParCommercial(matricule: string): Observable<SessionCommercialBE[]> `
  - `getSessionsParDate(date: string): Observable<SessionCommercialBE[]> `
  - `getSessionsParStatut(statut: string): Observable<SessionCommercialBE[]> `
  - `ouvrirSession(numero: string): Observable<SessionCommercialBE> `
  - `cloturerSession(numero: string, matricule: string): Observable<SessionCommercialBE> `
  - `validerSessionSecretaire(numero: string, matricule: string): Observable<SessionCommercialBE> `
  - `validerSessionComptable(numero: string, matricule: string): Observable<SessionCommercialBE> `
  - `if(params.entrepot): void`
  - `if(params.produit): void`
  - `if(params.type): void`


#### `app\domains\stock\presentation\pages\alertes\alertes.component.ts`

**class** `AlertesComponent` extends/implements `OnInit, OnDestroy `

- **Méthodes :**
  - `critiques(): void`
  - `faibles(): void`
  - `getFiltered(): void`
  - `ngOnInit(): void`
  - `fCFA(n:number): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\commercial\commercial.component.ts`

**class** `CommercialComponent` extends/implements `OnInit, AfterViewInit, OnDestroy `

- **Propriétés :**
  - `private charts: Chart[]=[]`

- **Méthodes :**
  - `ngOnInit(): void`
  - `ngAfterViewInit(): void`
  - `buildCharts(): void`
  - `buildSalesChart(): void`
  - `buildPerfChart(): void`
  - `getInfoForComm(id:number): void`
  - `getPrise(ip:InfoProduits): void`
  - `getVendu(ip:InfoProduits): void`
  - `getRestant(ip:InfoProduits): void`
  - `getTaux(ip:InfoProduits): void`
  - `getMontant(ip:InfoProduits): void`
  - `getTotalVentes(): void`
  - `getTotalVendus(): void`
  - `getTauxGlobal(): void`
  - `getStatusClass(s:string): void`
  - `getStatusLabel(s:string): void`
  - `openDotation(c:Commercial): void`
  - `closeDotation(): void`
  - `getDotTotal(): void`
  - `submitDotation(): void`
  - `fCFA(n:number): void`
  - `showToast(msg:string,type:'success'|'error'): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\dashboard\stock-dashboard.component.ts`

**class** `StockDashboardComponent` extends/implements `OnInit, AfterViewInit, OnDestroy `

- **Propriétés :**
  - `private charts: Chart[] = []`
  - `type: 'bar',`
  - `data: {`
  - `datasets: [`
  - `options: {`
  - `responsive: true, maintainAspectRatio: false,`
  - `plugins: {`
  - `legend: { position: 'top', labels: { font: { family: 'DM Sans', size: 12 }, usePointStyle: true } },`
  - `tooltip: {`
  - `callbacks: {`
  - `scales: {`
  - `x: { grid: { display: false }, ticks: { font: { family: 'DM Sans', size: 10 }, maxRotation: 35 } }`
  - `type: 'doughnut',`
  - `data: {`
  - `labels: ['Matières Premières', 'Consommables', 'Produits Finis', 'Tampon'],`
  - `datasets: [{`
  - `data: [v1, v2, v3, 0],`
  - `borderWidth: 3, borderColor: '#fff', hoverOffset: 8,`
  - `options: {`
  - `responsive: true, maintainAspectRatio: false,`
  - `cutout: '65%',`
  - `plugins: {`
  - `legend: { position: 'bottom', labels: { font: { family: 'DM Sans', size: 11 }, padding: 12, usePointStyle: true } },`
  - `tooltip: {`
  - `callbacks: {`
  - `type: 'bar',`
  - `data: {`
  - `labels: ['Entrées Fournisseur/Prod.', 'Sorties Commercial', 'Ajustements', 'Transferts Tampon'],`
  - `datasets: [{`
  - `label: 'Mouvements',`
  - `data: [entrees, sorties, ajust, transfert],`
  - `borderRadius: 8, borderSkipped: false,`
  - `options: {`
  - `responsive: true, maintainAspectRatio: false, indexAxis: 'y',`
  - `plugins: {`
  - `legend: { display: false },`
  - `scales: {`
  - `y: { grid: { display: false }, ticks: { font: { family: 'DM Sans', size: 11 } } }`
  - `type: 'polarArea',`
  - `data: {`
  - `labels: ['🔴 Critiques', '🟠 Faibles', '🟢 Normaux', '🔵 Surplus'],`
  - `datasets: [{`
  - `data: [critique, faible, normal, surplus],`
  - `borderWidth: 2, borderColor: '#fff',`
  - `options: {`
  - `responsive: true, maintainAspectRatio: false,`
  - `plugins: {`
  - `legend: { position: 'bottom', labels: { font: { family: 'DM Sans', size: 11 }, padding: 10 } },`

- **Méthodes :**
  - `ngOnInit(): void`
  - `setPeriod(period: 'today' | 'week' | 'month'): void`
  - `ngAfterViewInit(): void`
  - `if(!this.loading(): void`
  - `loadAll(): void`
  - `setTimeout((): void`
  - `buildCharts(): void`
  - `if(this.stockBarCanvas): void`
  - `if(this.donutCanvas): void`
  - `if(this.movementsCanvas): void`
  - `if(this.alertRadarCanvas): void`
  - `buildStockBarChart(): void`
  - `buildDonutChart(): void`
  - `buildMovementsChart(): void`
  - `buildAlertRadarChart(): void`
  - `getTopLevels(): void`
  - `getPct(sl: StockLevel): void`
  - `getColor(sl: StockLevel): string `
  - `if(sl.alertLevel === 'CRITIQUE'): void`
  - `if(sl.alertLevel === 'FAIBLE'): void`
  - `getPendingBatches(): void`
  - `getCriticalItems(): void`
  - `getCriticalClass(sl: StockLevel): void`
  - `if(pct < 20): void`
  - `if(pct < 40): void`
  - `getSessionLabel(s?: string): void`
  - `getRStatutLabel(s: string): void`
  - `getRStatutClass(s: string): void`
  - `getWShort(name?: string): void`
  - `getWBadge(name?: string): void`
  - `getBStatutLabel(s: string): void`
  - `getBStatutClass(s: string): void`
  - `isEntree(t: string): void`
  - `getMIcon(t: string): void`
  - `getMColor(t: string): void`
  - `getMLabel(t: string): void`
  - `formatCFA(n: number): void`
  - `getNAlertClass(n: string): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\inventaire\inventaire.component.ts`

**class** `InventaireComponent` extends/implements `OnInit, AfterViewInit, OnDestroy `

- **Propriétés :**
  - `private chart: Chart | null = null`
  - `tooltip: { callbacks: {`

- **Méthodes :**
  - `ngOnInit(): void`
  - `ngAfterViewInit(): void`
  - `applyFilters(): void`
  - `goTo(p: number): void`
  - `buildChart(): void`
  - `getTotalVal(): void`
  - `getPct(sl: StockLevel): void`
  - `getColor(sl: StockLevel): void`
  - `getNClass(n: string): void`
  - `countAlert(level: string): void`
  - `mathMin(a: number, b: number): void`
  - `fCFA(n: number): void`
  - `openAjust(sl: StockLevel): void`
  - `closeAjust(): void`
  - `getAjustSL(): void`
  - `sauverAjust(): void`
  - `showToast(msg: string, type: 'success' | 'error'): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\mouvements\mouvements.component.ts`

**class** `MouvementsComponent` extends/implements `OnInit, OnDestroy `

- **Méthodes :**
  - `ngOnInit(): void`
  - `applyFilters(): void`
  - `goTo(p:number): void`
  - `count(t:string): void`
  - `isEntree(t:string): void`
  - `getIcon(t:string): void`
  - `getColor(t:string): void`
  - `getLabel(t:string): void`
  - `fCFA(n:number): void`
  - `getAjustSL(): void`
  - `getTransfertSL(): void`
  - `getMPLevels(): void`
  - `sauverAjust(): void`
  - `sauverTransfert(): void`
  - `showToast(msg:string,type:'success'|'error'): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\notifications\notifications.component.ts`

**class** `NotificationsComponent`

- **Propriétés :**
  - `notifications: Notification[] = [`
  - `id: 1,`
  - `type: 'warning',`
  - `title: 'Alerte de Stock',`
  - `message: 'Le stock de TBSA est inférieur au seuil de réapprovisionnement',`
  - `time: 'Il y a 5 minutes',`
  - `read: false,`
  - `icon: 'fa-triangle-exclamation'`
  - `id: 2,`
  - `type: 'success',`
  - `title: 'Réception Validée',`
  - `message: 'La réception #REC-2024-001 a été validée avec succès',`
  - `time: 'Il y a 1 heure',`
  - `read: false,`
  - `icon: 'fa-check-circle'`
  - `id: 3,`
  - `type: 'info',`
  - `title: 'Nouvelle Commande',`
  - `message: 'Commande interne #CMD-2024-045 créée pour la production',`
  - `time: 'Il y a 2 heures',`
  - `read: true,`
  - `icon: 'fa-info-circle'`
  - `id: 4,`
  - `type: 'warning',`
  - `title: 'Lot en Attente',`
  - `message: 'Lot de production #LOT-2024-089 en attente de validation',`
  - `time: 'Il y a 3 heures',`
  - `read: true,`
  - `icon: 'fa-clock'`
  - `id: 5,`
  - `type: 'error',`
  - `title: 'Erreur Système',`
  - `message: 'Échec de synchronisation avec le serveur',`
  - `time: 'Hier à 14:30',`
  - `read: true,`
  - `icon: 'fa-exclamation-circle'`
  - `id: 6,`
  - `type: 'success',`
  - `title: 'Inventaire Terminé',`
  - `message: 'Inventaire mensuel terminé avec succès',`
  - `time: 'Hier à 10:00',`
  - `read: true,`
  - `icon: 'fa-check-circle'`
  - `unreadCount: number = 0`
  - `filterType: string = 'all'`
  - `default: return 'notif-info'`

- **Méthodes :**
  - `constructor(): void`
  - `updateUnreadCount(): void `
  - `markAsRead(notification: Notification): void `
  - `markAllAsRead(): void `
  - `deleteNotification(id: number): void `
  - `getFilteredNotifications(): Notification[] `
  - `if(this.filterType === 'all'): void`
  - `if(this.filterType === 'unread'): void`
  - `setFilter(type: string): void `
  - `getNotificationClass(type: string): string `
  - `switch(type): void`


#### `app\domains\stock\presentation\pages\production\production.component.ts`

**class** `ProductionComponent` extends/implements `OnInit, AfterViewInit, OnDestroy `

- **Propriétés :**
  - `private chart: Chart | null = null`
  - `orderForm: { items: Array<{productId: number`
  - `type: 'line',`
  - `data: {`
  - `datasets: [`
  - `options: {`
  - `responsive: true, maintainAspectRatio: false,`
  - `plugins: { legend: { position: 'top', labels: { font: { family: 'DM Sans', size: 12 }, usePointStyle: true } } },`
  - `scales: {`
  - `y2: { position: 'right', beginAtZero: true, grid: { display: false }, ticks: { font: { family: 'DM Sans', size: 11 } }, title: { display: true, text: 'x100 unités' } },`
  - `x: { grid: { display: false }, ticks: { font: { family: 'DM Sans', size: 10 } } }`
  - `productId: this.declareForm.productId,`
  - `declaredQuantityKg: this.declareForm.declaredQuantityKg,`
  - `productionDate: this.declareForm.productionDate,`
  - `notes: this.declareForm.notes,`
  - `next: b => {`
  - `next: o => {`

- **Méthodes :**
  - `ngOnInit(): void`
  - `ngAfterViewInit(): void`
  - `loadAll(): void`
  - `setTimeout((): void`
  - `buildChart(): void`
  - `if(!this.trendCanvas): void`
  - `openAction(b: ProductionBatch, type: 'validate'|'reject'): void`
  - `closeAction(): void`
  - `confirmerAction(): void`
  - `openDeclareModal(): void`
  - `closeDeclareModal(): void`
  - `getPreviewUnits(): number `
  - `if(!this.declareForm.productId || !this.declareForm.declaredQuantityKg): void`
  - `getPreviewBatchDate(): string `
  - `if(!this.declareForm.productionDate): void`
  - `declareBatch(): void`
  - `if(!this.declareForm.productId || !this.declareForm.declaredQuantityKg || this.declaring(): void`
  - `openOrderModal(): void`
  - `closeOrderModal(): void`
  - `addOrderItem(): void`
  - `removeOrderItem(i: number): void`
  - `createOrder(): void`
  - `if(this.ordering(): void`
  - `approveOrder(id: number): void`
  - `getStatusLabel(s: string): void`
  - `getStatusClass(s: string): void`
  - `getOStatusLabel(s: string): void`
  - `getOStatusClass(s: string): void`
  - `formatCFA(n: number): void`
  - `showToast(msg: string, type: 'success'|'error'): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\reception\reception-form.component.ts`

**class** `ReceptionFormComponent` extends/implements `OnInit, OnDestroy `

- **Propriétés :**
  - `lignes: LineForm[] = []`

- **Méthodes :**
  - `ngOnInit(): void`
  - `forkJoin({s:this.uc.getSuppliers(): void`
  - `getFiltered(): void`
  - `addLine(): void`
  - `removeLine(i:number): void`
  - `onSLChange(l:LineForm): void`
  - `onQteChange(l:LineForm): void`
  - `recalc(l:LineForm): void`
  - `getTotal(): void`
  - `isValid(): void`
  - `getWName(): void`
  - `getSupName(): void`
  - `fCFA(n:number): void`
  - `requiresSecond(): void`
  - `sauvegarder(): void`
  - `if(!this.isValid(): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\reception\reception-list.component.ts`

**class** `ReceptionListComponent` extends/implements `OnInit, OnDestroy `

- **Méthodes :**
  - `ngOnInit(): void`
  - `updatePag(): void`
  - `goTo(p:number): void`
  - `countStatus(s:string): void`
  - `sLabel(s:string): void`
  - `sClass(s:string): void`
  - `fCFA(n:number): void`
  - `showToast(msg:string,type:'success'|'error'): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\session\session.component.ts`

**class** `SessionComponent` extends/implements `OnInit, OnDestroy `

- **Méthodes :**
  - `getPrise(ip:InfoProduits): void`
  - `getVendu(ip:InfoProduits): void`
  - `getRestant(ip:InfoProduits): void`
  - `getTaux(ip:InfoProduits): void`
  - `getMontant(ip:InfoProduits): void`
  - `getInfoColor(s:string): void`
  - `getInfoLabel(s:string): void`
  - `fCFA(n:number): void`
  - `ngOnInit(): void`
  - `ngOnDestroy(): void`


#### `app\domains\stock\presentation\pages\settings\settings.component.ts`

**class** `SettingsComponent`

- **Propriétés :**
  - `profileForm: FormGroup`
  - `notificationForm: FormGroup`
  - `securityForm: FormGroup`
  - `activeTab: string = 'profile'`
  - `firstName: ['Mvondo', Validators.required],`
  - `lastName: ['Jean-Baptiste', Validators.required],`
  - `email: ['mvondo.jb@tanty.com', [Validators.required, Validators.email]],`
  - `phone: ['+237 6XX XXX XXX', Validators.required],`
  - `role: ['Gestionnaire de Stock', Validators.required]`
  - `emailNotifications: [true],`
  - `pushNotifications: [true],`
  - `stockAlerts: [true],`
  - `orderUpdates: [true],`
  - `systemUpdates: [false]`
  - `currentPassword: ['', Validators.required],`
  - `confirmPassword: ['', Validators.required]`

- **Méthodes :**
  - `constructor(private fb: FormBuilder): void`
  - `setActiveTab(tab: string): void `
  - `saveProfile(): void `
  - `if(this.profileForm.valid): void`
  - `saveNotifications(): void `
  - `changePassword(): void `
  - `if(this.securityForm.valid): void`
  - `if(newPassword !== confirmPassword): void`
  - `alert('Les mots de passe ne correspondent pas'): void`


#### `app\domains\stock\presentation\pages\validation\validation.component.ts`

**class** `ValidationComponent` extends/implements `OnInit, OnDestroy `

- **Méthodes :**
  - `ngOnInit(): void`
  - `load(): void`
  - `openAction(r:Receipt,a:'v1'|'v2'|'rej'): void`
  - `closeAction(): void`
  - `confirm(): void`
  - `canV1(r:Receipt): void`
  - `canV2(r:Receipt): void`
  - `isMP(r:Receipt): void`
  - `totalQty(r:Receipt): void`
  - `sLabel(s:string): void`
  - `sClass(s:string): void`
  - `fCFA(n:number): void`
  - `showToast(msg:string,type:'success'|'error'): void`
  - `ngOnDestroy(): void`


### Domaine `sync`

#### `app\domains\sync\infrastructure\sync.service.ts`

**interface** `SyncLogBE`

- **Propriétés :**
  - `id: number`
  - `idempotencyKey: string`
  - `matriculeCommercial: string`
  - `entityType: string`
  - `entityRef: string`
  - `result: string`
  - `serverEntityRef: string | null`
  - `conflictDetail: string | null`
  - `dateTraitement: string`

**interface** `SyncPushResponseBE`

- **Propriétés :**
  - `idempotencyKey: string`
  - `result: string`
  - `serverEntityRef: string | null`
  - `conflictDetail: string | null`
  - `dateTraitement: string`

**interface** `SyncPullResponseBE`

- **Propriétés :**
  - `serverTime: string`
  - `matriculeCommercial: string`
  - `batches: { entityType: string`

**class** `SyncService`

- **Propriétés :**
  - `idempotencyKey: string`
  - `matriculeCommercial: string`
  - `entityType: string`
  - `entityRef: string`
  - `payloadHash: string`
  - `matricule: string`
  - `entityType: string`
  - `result: string`
  - `debut: string`
  - `fin: string`
  - `limite: number`

- **Méthodes :**
  - `pull(matriculeCommercial: string, lastSync: string): Observable<SyncPullResponseBE> `
  - `if(params?.matricule): void`
  - `if(params?.entityType): void`
  - `if(params?.result): void`
  - `if(params?.debut): void`
  - `if(params?.fin): void`


### Domaine `users`

---

# Interactions et architecture

## Backend

- **Architecture hexagonale / DDD** : chaque module est organisé en `domain`, `application`, `infrastructure`, `presentation`.
- **Domain** : entités, aggregates, value objects, enums, événements, repositories (interfaces), specifications.
- **Application** : use cases, DTOs, mappers, services applicatifs.
- **Infrastructure** : entités JPA, repositories Spring Data, implémentations, services techniques (JWT, OTP, stockage).
- **Presentation** : controllers REST (Spring MVC).
- **Shared** : composants transverses (audit, exceptions, events, value objects, seeding, web, storage).

## Frontend

- **Framework** : Angular 17+ avec standalone components.
- **Organisation par domaine métier** sous `src/app/domains/` : commercial, comptable, controle, dg, production, rp, secretaire, stock, admin, users.
- **Core** : authentification, guards, interceptors HTTP, modèles partagés, services de base (API, audit, documents).
- **Layout** : composants de mise en page par rôle (commercial-layout, dg-layout, etc.).
- **Shared** : composants/utilitaires réutilisables (toast, preloader, format utils, mock store).
- **Stock** : architecture Clean/DDD avec use-cases, domain services, repositories (API + mock).

## Couplage backend / frontend

- Les services Angular (`*Service`) consomment les endpoints REST exposés par les controllers du backend.
- `AuthService` gère JWT/OTP via `/api/auth/**`.
- `ProductionService` communique avec les endpoints du module `production` pour les affectations, fiches, sessions de broyage/dosage, rapports.
- `StockService` et les use-cases du domaine stock utilisent les endpoints du module stock (non présent dans la liste actuelle, mais référencés via `stock.api.ts`).
