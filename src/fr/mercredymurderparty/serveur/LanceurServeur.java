package fr.mercredymurderparty.serveur;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.h2.tools.Server;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.ihm.fenetres.FenetreAdmin;
import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.outil.DevMod;
import fr.mercredymurderparty.outil.FichierXML;
import fr.mercredymurderparty.outil.Fonctions;
import fr.mercredymurderparty.outil.Theme;

public class LanceurServeur
{

	/**
	 * @param _args : un tableau d'argument qu'on peut passer à l'appel de l'application
	 */
	public static void main(String[] _args)
	{
		// lancement du serveur
		new LanceurServeur();
	}
	
	/**
	 * Le constructeur du lanceur
	 */
	public LanceurServeur()
	{
		try 
		{
			// Charger fichier de config
			FichierXML xml = new FichierXML("config.xml");
			
			// Lancer le serveur
			@SuppressWarnings("unused")
			CoeurServeur server = new CoeurServeur(Integer.parseInt(xml.valeurNoeud("serveur", "port")));
			
			// Définir le thème de l'interface
			Theme theme = new Theme();
			theme.charger();
			
			// Définir l'environnement graphique
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = env.getScreenDevices();
			
			// Coeur du client du serveur
				// Charger fichier de config
				
				// Créer une nouvelle connexion client
				CoeurClient coeurClient = new CoeurClient(xml.valeurNoeud("serveur", "ip"), Integer.parseInt(xml.valeurNoeud("serveur", "port")));
				
			// Lancer la fenetre
			FenetreAdmin fenetreAdmin = new FenetreAdmin(devices[0], coeurClient, server);
			fenetreAdmin.setVisible(true);
			
			// [[[ a supprimer avant la fin du projet ]]]
			DevMod dev = new DevMod();
			dev.setVisible(true);
			dev.toBack();
		} 
		catch (Exception e) 
		{
			//System.err.println("Impossible de lancer l'interface serveur: peux-etre une partie est deja en cours !");
			e.printStackTrace();
		}
	}
}
