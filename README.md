# Étude de Cas - Benchmark des Web Services REST

##  Liens Utiles

###  Drive du Projet
https://drive.google.com/file/d/1Kyaf5buuQJFI3QyOE2vfHAqs4cdSH-De/view?usp=sharing

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
 
 <img width="945" height="557" alt="image" src="https://github.com/user-attachments/assets/7926e2e3-423e-49f8-85ef-686d86e46543" />


 <img width="945" height="463" alt="image" src="https://github.com/user-attachments/assets/4d4b5022-b2e0-4cd1-916b-4106ae54634c" />

 <img width="945" height="413" alt="image" src="https://github.com/user-attachments/assets/c163170b-3437-4155-9a3f-9616e720886a" />

 <img width="945" height="314" alt="image" src="https://github.com/user-attachments/assets/129e2e74-75ee-47cc-86b1-94e307b78e51" />

 <img width="945" height="403" alt="image" src="https://github.com/user-attachments/assets/4da07107-5c58-4376-bbba-37b0b1526786" />



 <img width="945" height="502" alt="image" src="https://github.com/user-attachments/assets/9585c2d5-0c9b-4911-9488-15dc983130b4" />


https://github.com/user-attachments/assets/bb60d5ec-47cf-46d4-8c43-563c5c91fb23

 Test POUR D

Test1

  <img width="747" height="445" alt="image" src="https://github.com/user-attachments/assets/2e6979ab-ba27-4770-be80-e1cecf930f9d" />

  <img width="816" height="486" alt="image" src="https://github.com/user-attachments/assets/143989fe-370d-41f3-9d49-8c7d486fc448" />


Test 2

<img width="945" height="563" alt="image" src="https://github.com/user-attachments/assets/561e2362-9261-4e2d-aa3d-07b1cc5847ee" />

<img width="945" height="563" alt="image" src="https://github.com/user-attachments/assets/30fc75f5-c0e2-4aa7-b7b8-679b3de40186" />


Teste 3
 
<img width="945" height="607" alt="image" src="https://github.com/user-attachments/assets/59e2fea5-7951-4518-a5f5-3ffb32917fb9" />

<img width="945" height="607" alt="image" src="https://github.com/user-attachments/assets/9db1c301-a885-4f7d-adb0-b54eca419621" />

 

Test 4 
 
<img width="945" height="607" alt="image" src="https://github.com/user-attachments/assets/58c76c59-ced0-4e28-bb29-595751629173" />


 <img width="945" height="607" alt="image" src="https://github.com/user-attachments/assets/69fed3fa-57cb-4f0e-a1a0-33a6eeb5f748" />



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

##  Améliorations Futures

1. **Implémenter Variante C** pour comparaison complète
2. **Tests avec base de données clusterisée** 
3. **Benchmark avec cache Redis**
4. **Analyse coût/performance sur cloud**

## Encadrement & Auteurs
**Encadré par** :Mr.LACHGR mohammed

**Réalisée par** :AICHA BARAHOU

BENZIAT hana

GHANIMI fatimazzahra


DABACHINE jamila


---
* Résultats complets disponibles dans le dossier `Etude de cas/`*  
* Code source entièrement fonctionnel et documenté*
