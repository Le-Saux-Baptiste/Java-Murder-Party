package fr.mercredymurderparty.ihm.composants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.ihm.fenetres.FenetreJoueur;
import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.outil.Communication;
import fr.mercredymurderparty.outil.FichierXML;
import fr.mercredymurderparty.outil.Message;

@SuppressWarnings("serial")
public class ComposantTchat extends JPanel
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private JPanel panelDiscussion;
	private JScrollPane zoneTchat;
	private JTextField champMessage;
	private JTextArea messagesTchat;
	private JLabel labelTchat;
	
	private String nomJoueur = "Invité";
	
	// ----- GESTION TCHAT ----- //
	private Communication communication;
	private CoeurClient coeurClient;
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Le constructeur de la partie de la zone de discussion
	 * @param _fenetre : "Fenetre" la fenêtre parent
	 * @param _springLayout : "SpringLayout" le type de mise en page de la fenêtre parent
	 * @param _panelAuthentification : "JPanel" le conteneur permettant de positionner ce module
	 */
	
	public ComposantTchat(JFrame _fenetre, SpringLayout _springLayout, JPanel _panelAuthentification, CoeurClient _coeurClient)
	{
		coeurClient = _coeurClient;
		
		/*
		 * Partie de l'IHM pour le tchat de la page d'accueil ( BAS-GAUCHE )
		 * 
		 * @conteneur panelDiscussion
		 * @zone_de_texte zoneTchat
		 * 		@label labelTchat
		 * 		@champ_texte_déroulant messagesTchat
		 * @champ_texte champMessage
		 */
		
		panelDiscussion = new JPanel();
		_fenetre.getContentPane().add(panelDiscussion);
		SpringLayout slPanelDiscussion = new SpringLayout();
		panelDiscussion.setOpaque(false);
		_springLayout.putConstraint(SpringLayout.NORTH, panelDiscussion, 50, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.SOUTH, panelDiscussion, 0, SpringLayout.SOUTH, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.WEST, panelDiscussion, 0, SpringLayout.WEST, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.EAST, panelDiscussion, 0, SpringLayout.WEST, _panelAuthentification);
		panelDiscussion.setLayout(slPanelDiscussion);
		
		champMessage = new JTextField();
		slPanelDiscussion.putConstraint(SpringLayout.NORTH, champMessage, -40, SpringLayout.SOUTH, panelDiscussion);
		slPanelDiscussion.putConstraint(SpringLayout.SOUTH, champMessage, -10, SpringLayout.SOUTH, panelDiscussion);
		slPanelDiscussion.putConstraint(SpringLayout.WEST, champMessage, 10, SpringLayout.WEST, panelDiscussion);
		slPanelDiscussion.putConstraint(SpringLayout.EAST, champMessage, -10, SpringLayout.EAST, panelDiscussion);
		panelDiscussion.add(champMessage);
		champMessage.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent _e) 
			{
				int cle = _e.getKeyCode();
				// si appuie sur entree et message non vide alors envoie sur serveur
				if (cle == KeyEvent.VK_ENTER && champMessage.getText().length() > 0)
				{
					try 
					{

						communication.getSortie().writeObject(new Message(coeurClient.getIdClient(), Message.MESSAGE_TCHAT, nomJoueur + " : " + champMessage.getText()));
						communication.getSortie().flush(); // vider le buffer
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
					finally
					{
						// Effacer la zone de saisie
						champMessage.setText(null);
					}
				}
			}
		});
		
		zoneTchat = new JScrollPane();
		slPanelDiscussion.putConstraint(SpringLayout.NORTH, zoneTchat, 10, SpringLayout.NORTH, panelDiscussion);
		slPanelDiscussion.putConstraint(SpringLayout.SOUTH, zoneTchat, -6, SpringLayout.NORTH, champMessage);
		slPanelDiscussion.putConstraint(SpringLayout.WEST, zoneTchat, 10, SpringLayout.WEST, panelDiscussion);
		slPanelDiscussion.putConstraint(SpringLayout.EAST, zoneTchat, -10, SpringLayout.EAST, panelDiscussion);
		panelDiscussion.add(zoneTchat);
		
		labelTchat = new JLabel("Titre");
		zoneTchat.setColumnHeaderView(labelTchat);
		
		messagesTchat = new JTextArea();
		messagesTchat.setEditable(false);
		messagesTchat.setLineWrap(true);
		zoneTchat.setViewportView(messagesTchat);
	}
	
// ----- METHODES ----- //
	
	/**
	 * 
	 * 
	 */
	final public void changerNomJoueur(String _nomJoueur)
	{
		nomJoueur = _nomJoueur;
	}
	
	/**
	 * La méthode estVisible permet d'afficher ou non le conteneur de connection
	 * @param _visible : "boolean" affiche ou masque ce conteneur
	 */
	final public void estVisible(boolean _visible)
	{
		panelDiscussion.setVisible(_visible);
	}
	
	/**
	 * La méthode demarrerSession permet de démarrer une nouvelle session "joueur" ou "invité"
	 * @param _estJoueur : "booléen"<br>
	 * si vrai alors démarrer une session "joueur"<br>
	 * si faux alors démarrer une session "invité"
	 */
	final public void demarrerCommunication(boolean _estJoueur)
	{
		communication = new Communication(messagesTchat,"JTextArea", coeurClient);
		//communication.demarrerSession();
		if(_estJoueur)
		{
			communication.getCoeurClient().setLogin(this.nomJoueur);
		}
	}
	
	/**
	 * Procédure qui va fermer la session du joueur et les objets du chat
	 */
	final public void arreterCommuniation()
	{
		// Arreter le coeur client, socket & thread associé
		try
		{
			communication.getCoeurClient().arreter();
		}
		catch (Exception e)
		{
			System.err.println("Erreur lors de la fermeture: " + e.getMessage());
		}
	}
}
