# Lamagent

## Répartition des rôles au sein de l'équipe
| Rôle                                           | Membre                                               |
|------------------------------------------------|------------------------------------------------------|
| Développement                                  | Romain Courbaize / Clément Rieuneau / Jodie Monterde |
| Tests                                          | Romain Courbaize / Clément Rieuneau / Jodie Monterde |
| Documentation                                  | Jodie Monterde                                       |
| Superviseur Git / Superviseur de configuration | Clément Rieuneau                                     |
| Architecture                                   | Romain Courbaize / Clément Rieuneau / Jodie Monterde |
| Chef de projet                                 | Jodie Monterde                                       |
| Tech Lead                                      | Romain Courbaize                                     |

Le superviseur va s'assurer de la bonne gestion du git (branches et commits bien organisés) mais également de la configuration du projet
Le chef de projet va s'assurer du bon déroulement du projet et éventuellement faire office de porte-parole si le besoin se présente
Le tech lead va s'assurer que le projet dans son ensemble suit les préceptes de bon développement 


## Charte du bon commiteur
Cette charte définit les règles et bonnes pratiques à suivre pour contribuer au dépôt afin d’assurer la qualité, la cohérence et la traçabilité du code.
Utiliser la syntaxe Conventional Commits : `<type>(scope): <message>`
Les commits doivent être court ( single responsability ) et rédigé en anglais.

Types principaux :
| Type         | Description                                                                          |
|--------------|--------------------------------------------------------------------------------------|
| **FEAT**     | Ajout d’une nouvelle fonctionnalité                                                  |
| **FIX**      | Correction d’un bug                                                                  |
| **DOCS**     | Modification ou ajout de documentation                                               |
| **STYLE**    | Changements de style (indentation, espaces, formatage, etc.) sans impact sur le code |
| **REFACTOR** | Réécriture ou amélioration du code sans changement de comportement                   | 
| **TEST**     | Ajout ou modification de tests                                                       |
| **CHORE**    | Tâches diverses n’affectant pas le code source (build, dépendances, CI/CD, etc.)     |
| **PERF**     | Amélioration des performances                                                        |
| **CI**       | Changements liés à l’intégration continue                                            |
| **BUILD**    | Modifications du système de build ou des dépendances                                 |
| **REVERT**   | Annulation d’un commit précédent                                                     |


## L'Agent
Notre agent est un assistant culinaire intelligent développé en Java utilisant LangChain4j et Ollama. Il propose des recettes personnalisées selon les allergies et préférences de l'utilisateur. L'agent filtre automatiquement les ingrédients allergènes et mémorise les choix de l'utilisateur. Il s'exécute localement via une interface console simple et modulaire.
Lorsque notre agent se basera sur les allergies de l'utilisateur, il utilisera une base de données locale contenant les allergies les plus courantes. De ce fait, si l'utilisateur a une allergie trop spécifique alors notre agent ne répondra pas à sa demande plutôt que de donner une réponse fausse.
