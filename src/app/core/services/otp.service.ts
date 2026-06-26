import { Injectable, signal } from '@angular/core';
import { Observable, of, throwError, delay } from 'rxjs';

export interface OTPRecord {
  id: string;
  codeOTP: string;
  matriculeCible: string;
  matriculeDG: string;
  nomDG: string;
  referenceBC?: string;
  motif?: string;
  createdAt: Date;
  expiresAt: Date;
  consomme: boolean;
  consommeAt?: Date;
}

export interface GenererOTPRequest {
  matriculeCible: string;
  referenceBC?: string;
  motif?: string;
}

export interface GenererOTPResponse {
  codeOTP: string;
  matriculeCible: string;
  validiteMinutes: number;
  expiresAt: Date;
  message: string;
}

export interface ValiderOTPRequest {
  matriculeChef: string;
  codeOTP: string;
}

@Injectable({ providedIn: 'root' })
export class OtpService {
  // Stockage en mémoire des OTPs générés
  private readonly _otps = signal<OTPRecord[]>([]);
  readonly otps = this._otps.asReadonly();

  generer(req: GenererOTPRequest, dgMatricule: string, dgNom: string): Observable<GenererOTPResponse> {
    // Invalider tout OTP existant pour ce matricule cible
    this._otps.update(list =>
      list.map(o =>
        o.matriculeCible === req.matriculeCible && !o.consomme
          ? { ...o, consomme: true, consommeAt: new Date() }
          : o
      )
    );

    // Générer un nouveau code OTP à 6 chiffres
    const code = String(Math.floor(100000 + Math.random() * 900000));
    const now  = new Date();
    const exp  = new Date(now.getTime() + 15 * 60 * 1000); // 15 minutes

    const record: OTPRecord = {
      id:             Math.random().toString(36).slice(2),
      codeOTP:        code,
      matriculeCible: req.matriculeCible.toUpperCase(),
      matriculeDG:    dgMatricule,
      nomDG:          dgNom,
      referenceBC:    req.referenceBC,
      motif:          req.motif,
      createdAt:      now,
      expiresAt:      exp,
      consomme:       false
    };

    this._otps.update(list => [record, ...list]);

    return of({
      codeOTP:         code,
      matriculeCible:  req.matriculeCible.toUpperCase(),
      validiteMinutes: 15,
      expiresAt:       exp,
      message:         `Code OTP généré pour ${req.matriculeCible}. Valide 15 minutes, usage unique.`
    }).pipe(delay(600));
  }

  /**
   * Valide et consomme un OTP (simulé côté frontend).
   * Dans le vrai backend, cette validation est faite côté serveur.
   */
  validerOTP(req: ValiderOTPRequest): { valide: boolean; raison?: string } {
    const otp = this._otps().find(o =>
      o.matriculeCible === req.matriculeChef.toUpperCase() &&
      o.codeOTP === req.codeOTP &&
      !o.consomme
    );

    if (!otp) return { valide: false, raison: 'Code OTP invalide ou déjà utilisé' };
    if (new Date() > otp.expiresAt) return { valide: false, raison: 'Code OTP expiré' };

    // Consommer l'OTP
    this._otps.update(list =>
      list.map(o => o.id === otp.id ? { ...o, consomme: true, consommeAt: new Date() } : o)
    );

    return { valide: true };
  }

  getOTPsForMatricule(matricule: string): OTPRecord[] {
    return this._otps().filter(o => o.matriculeCible === matricule.toUpperCase());
  }

  getHistorique(): OTPRecord[] {
    return [...this._otps()].sort((a, b) => b.createdAt.getTime() - a.createdAt.getTime());
  }

  isExpired(otp: OTPRecord): boolean {
    return new Date() > otp.expiresAt;
  }

  getStatut(otp: OTPRecord): 'actif' | 'consomme' | 'expire' {
    if (otp.consomme) return 'consomme';
    if (this.isExpired(otp)) return 'expire';
    return 'actif';
  }

  getRemainingSeconds(otp: OTPRecord): number {
    const diff = otp.expiresAt.getTime() - new Date().getTime();
    return Math.max(0, Math.floor(diff / 1000));
  }
}
