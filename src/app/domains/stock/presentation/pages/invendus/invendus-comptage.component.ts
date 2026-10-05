import { Component, OnInit, signal, computed, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { StockService, ConstatInvendusBE } from '../../../infrastructure/stock.service';

/**
 * Comptage des invendus déclarés par les commerciaux.
 *
 * <p>Écran manquant : le commercial pouvait déclarer, mais rien au siège ne
 * permettait de confronter son annonce au réel. Le constat restait ouvert
 * indéfiniment.</p>
 *
 * <p>Aucun mouvement de stock n'est produit ici — les invendus restent au camion.
 * Ce que le gestionnaire arrête, c'est l'écart entre le déclaré et le compté.</p>
 */
@Component({
  selector: 'app-invendus-comptage',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './invendus-comptage.component.html',
  styleUrls: ['./invendus-comptage.component.css'],
})
export class InvendusComptageComponent implements OnInit {
  private readonly svc = inject(StockService);

  today = new Date();

  loading = signal(false);
  traitementEnCours = signal<string | null>(null);
  message = signal<{ texte: string; type: 'succes' | 'erreur' } | null>(null);

  aCompter = signal<ConstatInvendusBE[]>([]);
  comptes = signal<ConstatInvendusBE[]>([]);

  /** Constat déplié pour saisie, par numéro. */
  ouvert = signal<string | null>(null);

  /** Saisie en cours : numéro de constat → code produit → quantité comptée. */
  private readonly saisies = signal<Record<string, Record<string, number | null>>>({});

  nbLignesACompter = computed(() =>
    this.aCompter().reduce((n, c) => n + c.lignes.length, 0)
  );
  nbEcarts = computed(() =>
    this.comptes().filter(c => (c.ecartTotal ?? 0) !== 0).length
  );

  ngOnInit(): void {
    this.charger();
  }

  private charger(): void {
    this.loading.set(true);
    forkJoin({
      aCompter: this.svc.getConstatsInvendus({ statut: 'DECLARE' })
        .pipe(catchError(() => of([] as ConstatInvendusBE[]))),
      comptes: this.svc.getConstatsInvendus({ statut: 'COMPTE' })
        .pipe(catchError(() => of([] as ConstatInvendusBE[]))),
    }).subscribe({
      next: ({ aCompter, comptes }) => {
        this.aCompter.set(aCompter);
        this.comptes.set(comptes);
        this.initialiserSaisies(aCompter);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.afficher("Les constats n'ont pas pu être chargés.", 'erreur');
      },
    });
  }

  /**
   * Les champs partent vides, jamais préremplis avec la quantité déclarée :
   * un comptage doit être une saisie, pas une confirmation par défaut.
   */
  private initialiserSaisies(constats: ConstatInvendusBE[]): void {
    const etat: Record<string, Record<string, number | null>> = {};
    for (const c of constats) {
      etat[c.numero] = {};
      for (const l of c.lignes) etat[c.numero][l.codeProduit] = null;
    }
    this.saisies.set(etat);
  }

  basculer(numero: string): void {
    this.ouvert.set(this.ouvert() === numero ? null : numero);
  }

  valeurSaisie(numero: string, codeProduit: string): number | null {
    return this.saisies()[numero]?.[codeProduit] ?? null;
  }

  majSaisie(numero: string, codeProduit: string, valeur: string): void {
    const nombre = valeur.trim() === '' ? null : Number(valeur);
    this.saisies.update(etat => ({
      ...etat,
      [numero]: { ...(etat[numero] ?? {}), [codeProduit]: nombre },
    }));
  }

  ecartPrevisionnel(constat: ConstatInvendusBE, codeProduit: string, declaree: number): number | null {
    const compte = this.valeurSaisie(constat.numero, codeProduit);
    return compte === null ? null : compte - declaree;
  }

  /** Toutes les lignes doivent être renseignées : zéro compte, l'omission non. */
  comptageComplet(constat: ConstatInvendusBE): boolean {
    return constat.lignes.every(l => {
      const v = this.valeurSaisie(constat.numero, l.codeProduit);
      return v !== null && Number.isFinite(v) && v >= 0;
    });
  }

  enregistrer(constat: ConstatInvendusBE): void {
    if (!this.comptageComplet(constat)) {
      this.afficher(
        'Renseignez chaque ligne, y compris à zéro : une ligne laissée vide n\'est pas un comptage.',
        'erreur');
      return;
    }

    const lignes = constat.lignes.map(l => ({
      codeProduit: l.codeProduit,
      quantiteComptee: this.valeurSaisie(constat.numero, l.codeProduit)!,
    }));

    this.traitementEnCours.set(constat.numero);
    this.svc.compterInvendus(constat.numero, lignes).subscribe({
      next: (resultat) => {
        this.traitementEnCours.set(null);
        this.ouvert.set(null);
        const ecart = resultat.ecartTotal ?? 0;
        this.afficher(
          ecart === 0
            ? `Constat ${constat.numero} compté : conforme à la déclaration.`
            : `Constat ${constat.numero} compté : écart de ${ecart > 0 ? '+' : ''}${ecart} unité(s).`,
          'succes');
        this.charger();
      },
      error: (e) => {
        this.traitementEnCours.set(null);
        this.afficher(this.messageErreur(e), 'erreur');
      },
    });
  }

  ecartClass(ecart: number | null): string {
    if (ecart === null) return '';
    if (ecart > 0) return 'ecart-pos';
    if (ecart < 0) return 'ecart-neg';
    return 'ecart-ok';
  }

  private afficher(texte: string, type: 'succes' | 'erreur'): void {
    this.message.set({ texte, type });
  }

  private messageErreur(e: unknown): string {
    const err = e as { error?: { message?: string; erreur?: string } };
    return err?.error?.erreur ?? err?.error?.message ?? "Le comptage n'a pas abouti.";
  }
}
