# Étude de Cas - Benchmark des Web Services REST

##  Objectif de l'Étude

Cette étude comparative évalue les performances de **trois approches différentes** pour implémenter des services REST en Java, dans un contexte de données massives (2 000 catégories, 100 000 produits).

##  Architecture des Variantes

### **Variante A : Jersey REST API**  IMPLÉMENTÉE
- **Framework** : Jersey (JAX-RS)
- **Architecture** : 6 couches (REST → Service → DAO → Database)
- **Approche** : Contrôle manuel, architecture clean
- **Port** : 8080

### **Variante C : Spring @RestController** 
- **Framework** : Spring Boot + @RestController
- **Approche** : Contrôle semi-automatique avec Spring MVC
- **Port** : 8081

### **Variante D : Spring Data REST** IMPLÉMENTÉE
- **Framework** : Spring Boot + Spring Data REST
- **Approche** : Exposition automatique des repositories
- **Format** : HAL/JSON
- **Port** : 8083

##  Résultats des Tests de Performance

### Scénario READ-heavy (50→100→200 threads)

| Mesure | Variante A (Jersey) | Variante D (Spring Data REST) |
|--------|-------------------|-----------------------------|
| **RPS** | 1,250 req/s | 980 req/s |
| **p95** | 180ms | 220ms |
| **Erreurs** | 0.2% | 0.5% |

### Scénario JOIN-filter (60→120 threads)

| Mesure | Variante A | Variante D |
|--------|-----------|-----------|
| **RPS** | 890 req/s | 720 req/s |
| **p95** | 150ms | 190ms |
| **Erreurs** | 0.1% | 0.3% |

##  Démarrage Rapide

### Prérequis
- Java 21
- Maven 3.8+
- PostgreSQL 14+
- Python 3 (pour génération données)

### 1. Configuration Base de Données
```bash
# Créer la base de données
createdb -U postgres benchmark_db

# Exécuter le schéma
psql -U postgres -d benchmark_db -f serviceA-jersey/src/main/resources/schema.sql
```

### 2. Génération des Données Massives
```bash
# Générer 2,000 catégories et 100,000 produits
python3 scripts/generate_data.py
```

### 3. Déploiement des Services

#### Variante A (Jersey)
```bash
cd serviceA-jersey
mvn clean package
# Déployer le WAR sur votre serveur d'application
```

#### Variante D (Spring Data REST)
```bash
cd serviceD-spring-data-rest
mvn spring-boot:run
# Service disponible sur http://localhost:8083
```

##  Tests de Charge avec JMeter

### Configuration des Scénarios

#### Scénario 1: READ-heavy
```
50%   GET /items?page=0&size=50
20%   GET /items?categoryId=X&page=0&size=20  
20%   GET /categories/X/items?page=0&size=20
10%   GET /categories?page=0&size=20
```

#### Scénario 2: JOIN-filter
```
70%   GET /items?categoryId=X&page=0&size=20
30%   GET /items/X
```

#### Scénario 3: MIXED (écritures)
```
40%   GET /items?page=0&size=20
20%   POST /items (1KB payload)
10%   PUT /items/X (1KB payload)
10%   DELETE /items/X
10%   POST /categories (0.5-1KB)
10%   PUT /categories/X
```

#### Scénario 4: HEAVY-body
```
50%   POST /items (5KB payload)
50%   PUT /items/X (5KB payload)
```

### Exécution des Tests
```bash
# Lancer JMeter avec les fichiers de test
jmeter -n -t scenarios/read-heavy.jmx -l results/read-heavy.jtl
```

##  Tableaux de Résultats Complets

### T1 — Configuration Matérielle & Logicielle

| Élément | Valeur |
|---------|--------|
| Machine | Intel i7-12700H, 6 cœurs, 16GB RAM |
| OS | Ubuntu 22.04 LTS |
| Java version | OpenJDK 21.0.1 |
| PostgreSQL | 14.8 |
| JMeter | 5.6.2 |
| JVM flags | -Xms2G -Xmx4G, G1GC |
| HikariCP | min:10, max:50, timeout:30000ms |

### T2 — Résultats 

Test POUR C
 <img width="945" height="557" alt="image" src="https://github.com/user-attachments/assets/9c04c26f-b02d-4d9a-93b2-274dc6f6ba96" />

<img width="945" height="463" alt="image" src="https://github.com/user-attachments/assets/b41fdc65-67b7-40a8-8a8e-a8da124afbe0" />

 
 <img width="945" height="413" alt="image" src="https://github.com/user-attachments/assets/a53b5c85-be29-4157-a5c4-7b4c4b1a649c" />

<img width="945" height="403" alt="image" src="https://github.com/user-attachments/assets/c13e60c9-87c1-4514-8e48-d36a88c5f0e1" />

 
<img width="945" height="314" alt="image" src="https://github.com/user-attachments/assets/05e16daa-7ce6-49d3-a9a9-6de1d2d758a6" />


<img width="945" height="502" alt="image" src="https://github.com/user-attachments/assets/002a0d62-583d-4338-9d64-9b78626343fd" />




https://github.com/user-attachments/assets/cca27993-1009-4d68-95db-7806620e9ca4



Test POUR D
Test1

  <img width="747" height="445" alt="image" src="https://github.com/user-attachments/assets/e0f654c2-d6c3-473c-8570-5e6cddb1914d" />


  <img width="816" height="486" alt="image" src="https://github.com/user-attachments/assets/0d405c2e-903d-448e-a82f-5e8517ce4656" />




Test 2

  <img width="945" height="563" alt="image" src="https://github.com/user-attachments/assets/02986470-2358-4377-a4c9-8b6313b2aaa1" />


 <img width="945" height="563" alt="image" src="https://github.com/user-attachments/assets/035d6a5f-216a-42d2-8c3a-5ca7c40a3e22" />


Teste 3
 
<img width="945" height="607" alt="image" src="https://github.com/user-attachments/assets/358ccec7-92e7-44b8-b1de-9f02d57af454" />

<img width="945" height="607" alt="image" src="https://github.com/user-attachments/assets/c6e189cb-dc43-47af-8819-5da8e442dac5" />


Test 4 

<img width="945" height="607" alt="image" src="https://github.com/user-attachments/assets/2048ff6e-129e-4d7b-b5e9-85865e157710" />

<img width="945" height="607" alt="image" src="https://github.com/user-attachments/assets/f877e2e2-9f8b-476d-a793-58bc8b1e0508" />

 


 


#### Scénario READ-heavy
| Variante | RPS | p50 | p95 | p99 | Err % |
|----------|-----|-----|-----|-----|-------|
| A: Jersey | 1,250 | 45ms | 180ms | 320ms | 0.2% |
| D: Spring Data REST | 980 | 65ms | 220ms | 380ms | 0.5% |

#### Scénario JOIN-filter  
| Variante | RPS | p50 | p95 | p99 | Err % |
|----------|-----|-----|-----|-----|-------|
| A: Jersey | 890 | 35ms | 150ms | 280ms | 0.1% |
| D: Spring Data REST | 720 | 55ms | 190ms | 340ms | 0.3% |

### T3 — Ressources JVM

| Variante | CPU (%) moy/pic | Heap (Mo) moy/pic | GC time (ms/s) | Threads actifs |
|----------|-----------------|-------------------|----------------|----------------|
| A: Jersey | 45%/75% | 1.2G/2.8G | 50/150 | 45/80 |
| D: Spring Data REST | 55%/85% | 1.5G/3.2G | 80/220 | 55/95 |

##  Analyse des Résultats

### Points Forts Variante A (Jersey)
-  **Meilleures performances** (RPS +25% vs D)
-  **Latence plus faible** (p95 -18%)
-  **Contrôle fin** sur les requêtes et réponses
-  **Architecture modulaire** et maintenable
-  **Moindre consommation mémoire**

### Points Forts Variante D (Spring Data REST)  
- **Développement rapide** (moins de code)
-  **API auto-documentée** (format HAL)
- **Standards RESTful** stricts
-  **Intégration Spring** native

### Limitations Observées

#### Variante D
- **Overhead HAL** : Format de réponse plus verbeux
- **Problèmes N+1** : Requêtes supplémentaires pour les relations
- **Moins flexible** : Difficulté pour les optimisations spécifiques

#### Variante A
- **Plus de code** à maintenir
- **Configuration manuelle** des endpoints

##  Recommandations d'Implémentation

### Pour les Requêtes de Lecture
```java
// OPTIMISÉ - Évite le N+1
@Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId")
Page<Item> findByCategoryIdWithCategory(@Param("categoryId") Long categoryId, Pageable pageable);
```

### Configuration HikariCP
```properties
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=30000
```

### Monitoring JVM
```bash
# Flags recommandés pour la production
java -Xms2G -Xmx4G -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar application.jar
```

## 📋 Livrables Fournis

###  Code Source
- `serviceA-jersey/` - Implémentation complète Jersey
- `serviceD-spring-data-rest/` - Implémentation Spring Data REST

###  Scripts et Configuration
- `scripts/generate_data.py` - Générateur de données massives
- `jmeter/scenarios/` - 4 scénarios JMeter complets
- `sql/schema.sql` - Structure de base de données

###  Résultats et Analyses
- `results/performance-comparison.csv` - Données brutes des tests
- `screenshots/` - Captures des dashboards Grafana
- `analysis/technical-report.md` - Analyse détaillée

###  Documentation
- Ce README complet
- Guides d'installation et d'exécution
- Procédures de test reproductibles

##  Conclusion et Recommandations

### Cas d'Usage Recommandés

####  Choisir Variante A (Jersey) quand :
- Performance et latence critiques
- Contrôle fin requis sur le comportement API
- Équipe expérimentée avec JAX-RS
- Architecture microservices complexe

####  Choisir Variante D (Spring Data REST) quand :
- Rapidité de développement prioritaire
- Standards REST/HAL importants
- CRUD simple sans logique métier complexe
- Équipe familiarisée avec l'écosystème Spring

### Recommandation Générale

Pour des **applications de production** avec des exigences de performance, la **Variante A (Jersey)** est recommandée grâce à son meilleur débit, sa latence réduite et son contrôle supérieur. La Variante D convient mieux pour des prototypes rapides ou des APIs CRUD simples.

## 🔮 Améliorations Futures

1. **Implémenter Variante C** pour comparaison complète
2. **Tests avec base de données clusterisée** 
3. **Benchmark avec cache Redis**
4. **Analyse coût/performance sur cloud**

## Encadrement & Auteurs
**Encadré par** :Mr.LACHGR mohammed
**Réalisée par** :


 BARAHOU aicha
   
BENZIAT hana

GHANIMI fatimazzahra

DABACHINE jamila

---
* Résultats complets disponibles dans le dossier `Etude de cas/`*  
* Code source entièrement fonctionnel et documenté*



