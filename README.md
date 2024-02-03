# App Trade 

## Description
Dans le cadre du module de développement d'application mobile, nous avons été amené à réaliser une application mobile. Nous avons choisi de dévellopper une application de trading. Cette application permet de suivre les cours de différentes cryptomonnaies et de passer des ordres d'achat ou de vente.

L'utilisateur peut s'inscrire et se connecter à l'application. Une fois connecté, il peut consulter la liste des différentes cryptomonnaies et consulter les cours en temps réel, ajouter des cryptomonnaies à sa liste de favoris, consulter l'historique des cours, passer des ordres d'achat ou de vente, consulter son portefeuille et son historique d'ordres. Il peut également ajouter ou retirer des fonds de son portefeuille.

## Mise en place
Pour mettre en place l'application, nous avons créé une base de données en utilisant MariaDB. Nous avons ensuite créé une API en Java avec Spring Boot. De plus, pour récupérer les cours des cryptomonnaies, nous avons utilisé l'API de CoinBase. Nous avons donc relié notre API à celle de CoinBase pour récupérer les cours en temps réel.

## 