package fr.mercredymurderparty.ihm.composants;

import java.awt.Color;
import java.awt.Font;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.ihm.fenetres.FenetreJoueur;
import fr.mercredymurderparty.outil.Communication;
import fr.mercredymurderparty.outil.FichierXML;

public class ComposantHorloge
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private JPanel panelCompteur;
	private JLabel labelCompteur;
	private JLabel labelArret;
	
	// ----- GESTION COMPTEUR ----- //
	private Communication communication;
	private CoeurClient coeurClient;
	
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Le constructeur de la partie pour les chronomètres
	 * @param _fenetre : "Fenetre" la fenêtre parent
	 * @param _springLayout : "SpringLayout" le type de mise en page de la fenêtre parent
	 */
	
	public ComposantHorloge(FenetreJoueur _fenetre, SpringLayout _springLayout, CoeurClient _coeurClient)
	{
		coeurClient = _coeurClient;
		/*
		 * Partie de l'IHM pour l'affichage du compteur général ( HAUT-DROITE )
		 *
		 * @conteneur panelCompteur
		 * @label labelCompteur
		 * @label labelArret (invisible par défaut)
		 */

		panelCompteur = new JPanel();
		SpringLayout slPanelCompteur = new SpringLayout();
		panelCompteur.setLayout(slPanelCompteur);
		panelCompteur.setOpaque(false);
		_springLayout.putConstraint(SpringLayout.NORTH, panelCompteur, 0, SpringLayout.NORTH, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.WEST, panelCompteur, -147, SpringLayout.EAST, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.SOUTH, panelCompteur, 100, SpringLayout.NORTH, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.EAST, panelCompteur, 0, SpringLayout.EAST, _fenetre.getContentPane());
		_fenetre.getContentPane().add(panelCompteur);

		labelCompteur = new JLabel("00:00:00");
		labelCompteur.setForeground(Color.WHITE);
		slPanelCompteur.putConstraint(SpringLayout.NORTH, labelCompteur, 10, SpringLayout.NORTH, panelCompteur);
		slPanelCompteur.putConstraint(SpringLayout.WEST, labelCompteur, 10, SpringLayout.WEST, panelCompteur);
		labelCompteur.setFont(new Font("Tahoma", Font.PLAIN, 30));
		panelCompteur.add(labelCompteur);

		labelArret = new JLabel("ARRET");
		labelArret.setForeground(Color.WHITE);
		labelArret.setVisible(false);
		slPanelCompteur.putConstraint(SpringLayout.NORTH, labelArret, 6, SpringLayout.SOUTH, labelCompteur);
		slPanelCompteur.putConstraint(SpringLayout.WEST, labelArret, 10, SpringLayout.WEST, labelCompteur);
		labelArret.setFont(new Font("Tahoma", Font.PLAIN, 30));
		panelCompteur.add(labelArret);
		communication = new Communication(labelCompteur, "JLabel", coeurClient);
		communication.demarrerSession();
	}
	
// ----- METHODES ----- //
	
	/**
	 * La méthode estVisible permet d'afficher ou non le conteneur de connection
	 * @param _visible : "boolean" affiche ou masque ce conteneur
	 */
	final public void estVisible(boolean _visible)
	{
		panelCompteur.setVisible(_visible);
	}
	
	
}
