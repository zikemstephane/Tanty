/**
 * Environnement de production.
 *
 * L'hébergement `https://api-tanty.onrender.com` existe mais **n'est pas encore
 * la production** : il reste ci-dessous en commentaire, pour ne pas avoir à le
 * retrouver le jour où il le deviendra. Le basculer suppose d'abord de lui
 * donner un secret JWT propre — celui du dépôt ne vaut que pour un poste de
 * développement.
 *
 * Les quatre adresses dérivent d'une seule racine, comme en développement.
 */
const BASE = 'http://localhost:8081';
// const BASE = 'https://api-tanty.onrender.com';

export const environment = {
  production: true,
  apiUrl: `${BASE}/api`,
  authUrl: `${BASE}/api/auth`,
  stockUrl: `${BASE}/api/stock`,
  productUrl: `${BASE}/api/products`,
};
