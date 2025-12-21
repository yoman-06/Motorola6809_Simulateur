N# Simulateur Motorola 6809

## Description du Projet

Ce projet est un simulateur complet du microprocesseur Motorola 6809, développé en Java avec une interface graphique Swing. Il permet d'écrire, compiler et exécuter du code assembleur pour le processeur 6809, offrant des fonctionnalités de débogage pas à pas et d'exécution complète.

## Fonctionnalités Principales

### Interface Graphique
- **Éditeur d'assembleur** : Éditeur de texte avec numérotation des lignes pour écrire du code assembleur
- **Affichage des registres** : Visualisation en temps réel des registres du processeur (A, B, D, X, Y, U, S, PC, CC)
- **Mémoire RAM** : Affichage et modification de la mémoire vive
- **Mémoire ROM** : Affichage de la mémoire morte contenant le programme compilé
- **Interface moderne** : Design avec thème sombre et couleurs accentuées

### Modes d'Exécution
- **Compilation** : Traduction du code assembleur en code machine
- **Exécution pas à pas** : Débogage instruction par instruction avec visualisation des changements
- **Exécution complète** : Exécution automatique du programme entier
- **Reset** : Réinitialisation complète du simulateur

## Instructions Supportées

### Instructions de Chargement (Load)
- `LDA`, `LDB`, `LDD`, `LDS`, `LDU`, `LDX`, `LDY` avec différents modes d'adressage

### Instructions de Stockage (Store)
- `STA`, `STB`, `STD`, `STS`, `STU`, `STX`, `STY`

### Instructions Arithmétiques
- `ADDA`, `ADDB`, `ADDD` (addition)
- `ADCA`, `ADCB` (addition avec retenue)
- `INCA`, `INCB`, `INC` (incrémentation)
- `DECA`, `DECB`, `DEC` (décrémentation)
- `ABX` (addition à l'index X)

### Instructions de Comparaison
- `CMPA`, `CMPB`, `CMPD`, `CMPS`, `CMPU`, `CMPX`, `CMPY`

### Autres Instructions
- `CLRA`, `CLRB`, `CLR` (effacement)
- `PSHS`, `PSHU`, `PULS`, `PULU` (pile)
- `TFR` (transfert entre registres)

## Modes d'Adressage Supportés

- **Immediat** : Valeur directe (ex: `LDA #$10`)
- **Direct** : Adresse sur 8 bits (ex: `LDA $10`)
- **Indexé Direct** : Adressage indexé (ex: `LDA 0,X`)
- **Étendu Direct** : Adresse sur 16 bits (ex: `LDA $1000`)
- **Étendu Indirect** : Adressage indirect (ex: `LDA [$1000]`)
- **Inhérent** : Pas d'opérande (ex: `INCA`)

## Architecture du Projet

### Classes Principales

- **`clsMain`** : Point d'entrée de l'application
- **`clsMoto6809`** : Interface graphique principale et gestion des événements
- **`clsCompiler`** : Compilation du code assembleur en code machine
- **`clsExecuter`** : Exécution complète du programme
- **`clsPasàpas`** : Exécution pas à pas pour le débogage
- **`clsInstructions`** : Définition des instructions et de leur codage
- **`clsAdressingModes`** : Gestion des modes d'adressage
- **`clsRegisters`** : Gestion des registres du processeur
- **`clsRAM`** : Simulation de la mémoire vive
- **`clsROM`** : Simulation de la mémoire morte
- **`clsErreur`** : Gestion des erreurs de compilation et d'exécution

### Structure des Fichiers

```
simulateur_moto6809/
├── src/
│   ├── clsMain.java              # Point d'entrée
│   ├── clsMoto6809.java          # Interface graphique
│   ├── clsCompiler.java          # Compilateur
│   ├── clsExecuter.java          # Exécuteur complet
│   ├── clsPasàpas.java           # Débogage pas à pas
│   ├── clsInstructions.java      # Définition des instructions
│   ├── clsAdressingModes.java    # Modes d'adressage
│   ├── clsRegisters.java         # Registres CPU
│   ├── clsRAM.java               # Mémoire RAM
│   ├── clsROM.java               # Mémoire ROM
│   └── clsErreur.java            # Gestion d'erreurs
├── lib/                          # Dépendances
├── bin/                          # Fichiers compilés
└── README.md                     # Ce fichier
```

## Installation et Utilisation

### Prérequis
- Java Development Kit (JDK) 8 ou supérieur
- Environnement de développement Java (recommandé : VS Code avec extension Java)

### Compilation et Exécution
1. Ouvrir le projet dans votre IDE Java
2. Compiler tous les fichiers source
3. Exécuter `clsMain.java`

### Utilisation du Simulateur
1. **Écrire du code** : Utiliser l'éditeur d'assembleur pour écrire votre programme
2. **Compiler** : Cliquer sur "COMPILER" pour traduire en code machine
3. **Déboguer** : Utiliser "PAS À PAS" pour exécuter instruction par instruction
4. **Exécuter** : Cliquer sur "EXÉCUTER" pour lancer le programme complet
5. **Reset** : Utiliser "RESET" pour réinitialiser le simulateur

## Exemple de Programme

```

LDA #$10      ; Charger 16 dans A
STA $1000     ; Stocker A à l'adresse $1000
LDB #$20      ; Charger 32 dans B
ADDA #$05     ; Ajouter 5 à A

```

## Fonctionnalités Avancées

### Débogage Pas à Pas
- Exécution instruction par instruction
- Visualisation des changements de registres en temps réel
- Suivi de l'exécution dans la mémoire

### Gestion d'Erreurs
- Détection d'erreurs de syntaxe lors de la compilation
- Messages d'erreur détaillés pour faciliter le débogage
- Validation des modes d'adressage

### Interface Utilisateur
- Design moderne avec thème sombre
- Boutons interactifs avec effets visuels
- Mise à jour en temps réel des affichages

## Développement et Extension

Le projet est modulaire et extensible. Pour ajouter de nouvelles instructions :

1. Ajouter l'instruction dans `clsInstructions.java`
2. Implémenter la logique dans `clsCompiler.java` et `clsPasàpas.java`
3. Mettre à jour l'affichage si nécessaire

## Technologies Utilisées

- **Java** : Langage de programmation principal
- **Swing** : Bibliothèque pour l'interface graphique
- **AWT** : Pour les composants graphiques avancés

## Auteur

Développé dans le cadre d'un projet éducatif sur l'architecture des ordinateurs et la simulation de processeurs.

## Licence

Ce projet est fourni à des fins éducatives. Veuillez respecter les droits d'auteur du Motorola 6809.
