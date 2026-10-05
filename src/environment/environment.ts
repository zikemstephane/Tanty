/**
 * Environnement de développement.
 *
 * Le port est **8081** — celui sur lequel écoute le backend. La version
 * actualisée visait 8082 et n'atteignait donc aucun serveur.
 *
 * Les quatre adresses dérivent d'une seule racine. Les écrire séparément les
 * laisse diverger : c'est ainsi que `apiUrl` s'était retrouvé sur un port et le
 * reste sur un autre.
 */
const BASE = 'http://localhost:8081';

export const environment = {
  production: false,
  apiUrl: `${BASE}/api`,
  authUrl: `${BASE}/api/auth`,
  stockUrl: `${BASE}/api/stock`,
  productUrl: `${BASE}/api/products`,
};
