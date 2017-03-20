package fr.mercredymurderparty.client;

import java.sql.SQLException;
import java.util.Observable;

import fr.mercredymurderparty.outil.BaseDeDonnees;

public class CompteurTempsJoueur extends Observable implements Runnable 
{

// ----- ATTRIBUTS ----- //

	private int tempsAffiche; // le temps qui s'affiche, en secondes
	private int tempsEcoule; // le temps écoulé depuis la connexion du joueur, en secondes
	private int tempsMaj; //le temps entre chaque màj de temps du joueur
	
	private Thread threadCompteur; // tout est dans le titre
	private Personnage perso;
	private CoeurClient coeurClient;

	
// ----- CONSTRUCTEUR ----- //

	/**
	 * Constructeur de la classe CompteurTemps
	 * @param _temps le temps initial
	 */
	public CompteurTempsJoueur(Personnage _perso, int _tempsAffiche, CoeurClient _coeurClient) 
	{
		// On recupère l'instance du personnage
		this.perso = _perso;
		 
		tempsAffiche = _tempsAffiche;
		tempsEcoule = 0;
		tempsMaj = 20;
		coeurClient = _coeurClient;
	}

	// ----- METHODES ----- //

	/**
	 * Méthode qui démarre un nouveau thread pour le Compteur de temps
	 */
	final public void demarrer()
	{
		if (threadCompteur == null) 
		{
			threadCompteur = new Thread(this);
			threadCompteur.start();
		}
	}
	
	/**
	 * Méthode qui met à jour le temps du joueur sur la bdd
	 */
	final public void majTemps()
	{
		
			// On va chercher le temps en BDD
			int ancienTemps = perso.recupererTemps(perso.getLogin()); 
			
			// Mettre a jours le personnage avec le nouveau temps
			BaseDeDonnees.envoyerInstruction("UPDATE personnage SET temps = " + (ancienTemps - tempsEcoule) + " WHERE login = '" + perso.getLogin() + "'", coeurClient);
		

			// Fermer la connexion a la bdd
		
	}
	
	/**
	 * Méthode qui stop le thread du Compteur de temps
	 */
	final public void arreter() 
	{
		
		if (threadCompteur != null) 
		{
			threadCompteur.stop();//le .interrupt ne fonctionne pas.
			threadCompteur = null;
			// On va chercher le temps en BDD
			int ancienTemps = perso.recupererTemps(perso.getLogin()); 
			BaseDeDonnees.envoyerInstruction("UPDATE personnage SET temps = " + (ancienTemps - tempsEcoule%tempsMaj) + " WHERE login = '" + perso.getLogin() + "'", coeurClient);
		}
	}

	/**
	 * Méthode éxécutée par le thread de la classe ( décrémentation du temps de 1 toutes les mille milisecondes )
	 */
	public void run() 
	{
		while (true) 
		{
			try 
			{
				Thread.sleep(1000); // attendre 1 seconde
				tempsAffiche--;
				tempsEcoule++;
				setChanged(); // J'ai changé
				notifyObservers(this); // Je préviens ceux qui m'observent que j'ai changé
				if(tempsEcoule % tempsMaj == 0) //toutes les 20 secondes, je met le temps du joueur à jour
				{
					majTemps();
					System.out.println("Temps mis à jour" + tempsAffiche );
				}
			} 
			catch (Exception ex) 
			{
				System.out.println("Une erreur est survenue au niveau d'un compteur de temps : " + ex.getMessage());
				arreter();
			}
		}
	}

	/**
	 * Méthode qui génère une chaine de caractère en HHhMMminSSsec à partir de la valeur de l'attribut temps
	 * @return une chaine de caractère pour l'affichage du temps.
	 */
	final public String chaineCaracTemps()
	{
		int heure = tempsAffiche/3600;
		int min = (tempsAffiche%3600)/60;
		int sec = (tempsAffiche%3600) % 60;
		String stringHeure = Integer.toString(heure);
		if (heure < 10)
		{
			stringHeure = "0"+stringHeure;
		}
		String stringMin = Integer.toString(min);
		if (min < 10)
		{
			stringMin = "0"+stringMin;
		}
		String stringSec = Integer.toString(sec);
		if (sec < 10)
		{
			stringSec = "0"+stringSec;
		}
		String chaineTemps = stringHeure+":"+stringMin+":"+stringSec;
		return chaineTemps;
	}

	
// ----- Getters & Setters -----

	/**
	 * Retourne la valeur du compteur
	 * @return le valeur du compteur
	 */
	public int getTempsAffiche() 
	{
		return tempsAffiche;
	}
}
