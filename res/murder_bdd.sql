DROP TABLE IF EXISTS indice;
CREATE TABLE indice (id INTEGER NOT NULL,titre VARCHAR(150) NOT NULL ,contenu TEXT NOT NULL ,importance INTEGER,image VARCHAR(255), PRIMARY KEY (id));
INSERT INTO indice VALUES(1,'Mon premier indice','Pour moi meme !',2,'null');
INSERT INTO indice VALUES(3,'Pour mon amie','La star du web',0,'null');
INSERT INTO indice VALUES(4,'Cosa nostra','La mafia est près de vous !',1,'null');
INSERT INTO indice VALUES(5,'Aleluhia','Ca marche... ou pas !',0,'null');
INSERT INTO indice VALUES(6,'Wesh cousin','Ca gaz mon pote ? Tention, je test l''edit du msg !',0,'null');
INSERT INTO indice VALUES(7,'Je test','En dur',1,'null');
INSERT INTO indice VALUES(8,'Qui a vu','Le film Time Out ?',2,'null');
INSERT INTO indice VALUES(10,'Mon bel indice','Roi des indices...',0,'null');
INSERT INTO indice VALUES(11,'La belle','et la bete :)',0,'null');
INSERT INTO indice VALUES(12,'Un indice pour tout le monde','Enjoy bande de petits veinards :)',0,'null');
INSERT INTO indice VALUES(14,'Une image vaux 1000 mots','Regarder moi ca...',0,'1333987068893_gestion-projet-informatique.jpg');
INSERT INTO indice VALUES(15,'Tic tac tic tac','Le temps tourne',0,'null');
INSERT INTO indice VALUES(16,'Asta la vista','Baby',0,'null');
INSERT INTO indice VALUES(17,'L''empire contre attaque','Mais faite quelque chose !!!',0,'null');
INSERT INTO indice VALUES(20,'Enjoy','My work bitches :)',0,'null');
INSERT INTO indice VALUES(21,'Un indice','Pour les gouverner tous.',0,'null');
INSERT INTO indice VALUES(23,'Hello','World',0,'null');
DROP TABLE IF EXISTS indice_relation;
CREATE TABLE indice_relation (id INTEGER NOT NULL,ref_perso INTEGER,ref_indice INTEGER, PRIMARY KEY (id));
INSERT INTO indice_relation VALUES(1,1,1);
INSERT INTO indice_relation VALUES(6,7,11);
INSERT INTO indice_relation VALUES(7,1,12);
INSERT INTO indice_relation VALUES(8,2,12);
INSERT INTO indice_relation VALUES(9,3,12);
INSERT INTO indice_relation VALUES(10,4,12);
INSERT INTO indice_relation VALUES(11,6,12);
INSERT INTO indice_relation VALUES(12,7,12);
INSERT INTO indice_relation VALUES(13,8,12);
INSERT INTO indice_relation VALUES(14,9,12);
INSERT INTO indice_relation VALUES(16,3,14);
INSERT INTO indice_relation VALUES(17,1,15);
INSERT INTO indice_relation VALUES(18,6,16);
INSERT INTO indice_relation VALUES(19,9,17);
INSERT INTO indice_relation VALUES(22,1,20);
INSERT INTO indice_relation VALUES(23,4,21);
INSERT INTO indice_relation VALUES(25,2,23);
DROP TABLE IF EXISTS personnage;
CREATE TABLE personnage (id INTEGER NOT NULL,login VARCHAR(22) NOT NULL ,mdp VARCHAR(22) NOT NULL ,nom VARCHAR(34),prenom VARCHAR(34),temps INTEGER NOT NULL  DEFAULT (1000), PRIMARY KEY (id) );
INSERT INTO personnage VALUES(1,'cgiu','super','Cindolo','Giuseppe',1000);
INSERT INTO personnage VALUES(2,'alice','1234','Petite','Alice',150);
INSERT INTO personnage VALUES(3,'bob','1234','Bob','L''eponge',500);
INSERT INTO personnage VALUES(4,'cnoris','0000','Chuck','Norris',1200);
INSERT INTO personnage VALUES(6,'hford','azerty','Harrison','Ford',888);
INSERT INTO personnage VALUES(7,'katsumi','qwerty','Katsu','Ni',420);
INSERT INTO personnage VALUES(8,'kevin','3210','Jean','Peuplus',999);
INSERT INTO personnage VALUES(9,'bbdu73','extra','Cric','Crac',1234);
