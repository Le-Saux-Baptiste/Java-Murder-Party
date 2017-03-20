package fr.mercredymurderparty.serveur;

import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.outil.Message;

public class CompteurTempsGeneral extends Observable implements Runnable
{

// ----- ATTRIBUTS ----- //
	
	private int temps; // le temps en secondes
	private int tempsEcoule; //le temps écoulé depuis le début de la partie
	
	private Thread threadCompteur; // tout est dans le titre
	
	// Définir le serveur
	private CoeurServeur server;

// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Constructeur de la classe CompteurTemps
	 * @param _temps le temps initial
	 */
	public CompteurTempsGeneral (CoeurServeur _server)
	{
		server = _server;
		BaseDeDonnees bdd = new BaseDeDonnees(server);
		ResultSet rs;
		try
		{
			rs = bdd.executerRequete("SELECT temps FROM info WHERE id = 1");
			rs.next();
			temps = rs.getInt("temps");
			tempsEcoule = 0;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		// Fermer la connexion a la bdd
		bdd.fermerConnexion();
		demarrer();
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
	 * Méthode qui stop le thread du Compteur de temps
	 */
	final public void arreter()
	{
		if (threadCompteur != null)
		{
			threadCompteur.stop(); // FIXME : si ne marche pas, utiliser stop()
			threadCompteur = null;
		}
		// Ouvrir une nouvelle conexion à la bdd
		BaseDeDonnees bdd = new BaseDeDonnees(server);

		try 
		{	
			// Mettre a jour le compteur général
			bdd.executerInstruction("UPDATE info SET temps = '" + temps + "' WHERE id = '1'");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		// Fermer la connexion a la bdd
		bdd.fermerConnexion();
	}
	
	/**
	 * Méthode éxécutée par le thread de la classe 
	 * ( décrémentation du temps de 1 toutes les mille milisecondes )
	 */
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(1000); // attendre 1 seconde
				if(temps > 0)
				{
					temps --;
					tempsEcoule++;
					
					server.traiterMessage(1, new Message(1, Message.MAJ_HEURE, chaineCaracTemps()));
					setChanged();
					notifyObservers(this);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.out.println("Une erreur est survenue au niveau d'un compteur de temps : "+ex.getMessage());
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
		int heure = temps/3600;
		int min = (temps%3600)/60;
		int sec = (temps%3600) % 60;
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
	

	// ----- GETTERS & SETTERS ----- //
	/**
	 * getter sur la valeur du temps
	 */
	public int getTemps()
	{
		return temps;
	}
	
	public int getTempsEcoule()
	{
		return tempsEcoule;
	}
	
	
	/**
	 * @param temps the temps to set
	 */
	public void setTemps(int _temps) {
		this.temps = _temps;
	}
}
