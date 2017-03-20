package fr.mercredymurderparty.client;

import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.outil.FichierXML;
import fr.mercredymurderparty.outil.Theme;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.sql.DriverManager;

import fr.mercredymurderparty.ihm.fenetres.FenetreJoueur;

public class LanceurClient
{	
	/**
	 * @param _args : un tableau d'argument qu'on peut passer à l'appel de l'application
	 */
	public static void main(String[] _args)
	{	
		// lancement de l'interface graphique et de la connexion
		new LanceurClient();
	}
	
	/**
	 * Le constructeur du lanceur
	 */
	public LanceurClient()
	{
		try 
		{
			// Définir le thème de l'interface de l'application
			Theme theme = new Theme();
			theme.charger();
			
			// Définir l'environnement graphique
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = env.getScreenDevices();
			
			// Charger fichier de config
			FichierXML xml = new FichierXML("config.xml");
			
			// Créer une nouvelle connexion client
			CoeurClient coeurClient = new CoeurClient(xml.valeurNoeud("serveur", "ip"), Integer.parseInt(xml.valeurNoeud("serveur", "port")));
			
			// Affichage de l'interface graphique
			FenetreJoueur fenetreJoueur = new FenetreJoueur(devices[0], coeurClient);
			fenetreJoueur.pleinEcran(false);
			/*
			BaseDeDonnees.envoyerRequete("SELECT id FROM personnage WHERE login = 'cgiu'", coeurClient, "id");
			System.out.println("SQL RŽsultat : "+(Integer)coeurClient.getResultatSQL());*/
			
		} 
		catch (Exception e) 
		{
			System.err.println("Impossible de lancer l'interface client: verifiez si vous avez correctement la partie serveur !");
		}
	}
}
