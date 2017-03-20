package fr.mercredymurderparty.ihm.composants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.ihm.fenetres.FenetreJoueur;

public class ComposantJoueur
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private JButton boutonDeconnexion;
	private JLabel labelConnecteEnTant;
	private JLabel labelIdentifiant;
	
	private ComposantIndices partieIndices;
	private ComposantHorlogeJoueur horlogeJoueur;
	
	// ----- MODELE ----- //
	private CoeurClient coeurClient;
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Le constructeur de la partie pour le joueur connecté
	 * @param _partieConnection 
	 * @param _login : "String" l'identifiant du joueur
	 * @param _fenetre : "Fenetre" la fenêtre parent
	 * @param _springLayout : "SpringLayout" le type de mise en page de la fenêtre parent
	 * @param _partieTchat 
	 */
	
	public ComposantJoueur(final ComposantConnection _partieConnection, FenetreJoueur _fenetre, SpringLayout _springLayout, final ComposantTchat _partieTchat, CoeurClient _coeurClient)
	{
		coeurClient = _coeurClient;
		boutonDeconnexion = new JButton("Déconnexion");
		_springLayout.putConstraint(SpringLayout.NORTH, boutonDeconnexion, 10, SpringLayout.NORTH, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.WEST, boutonDeconnexion, 10, SpringLayout.WEST, _fenetre.getContentPane());
		boutonDeconnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _e)
			{
				horlogeJoueur.estVisible(false);
				horlogeJoueur.getCompteurJoueur().arreter();
				
				// Réafficher la fenêtre d'accueil & supprimer la session courante
				//_partieTchat.arreterCommunication();
				//_partieTchat.demarrerCommunication(false);
				_partieTchat.changerNomJoueur("Invité");
				
				_partieConnection.estVisible(true);
				partieIndices.estVisible(false);

				estVisible(false);
			}
		});
		_fenetre.getContentPane().add(boutonDeconnexion);
		
		labelConnecteEnTant = new JLabel("Connecté en tant que:");
		_springLayout.putConstraint(SpringLayout.NORTH, labelConnecteEnTant, 10, SpringLayout.SOUTH, boutonDeconnexion);
		_springLayout.putConstraint(SpringLayout.WEST, labelConnecteEnTant, 10, SpringLayout.WEST, _fenetre.getContentPane());
		_fenetre.getContentPane().add(labelConnecteEnTant);
		
		labelIdentifiant = new JLabel("<login>");
		_springLayout.putConstraint(SpringLayout.NORTH, labelIdentifiant, 10, SpringLayout.SOUTH, boutonDeconnexion);
		_springLayout.putConstraint(SpringLayout.WEST, labelIdentifiant, 6, SpringLayout.EAST, labelConnecteEnTant);
		_fenetre.getContentPane().add(labelIdentifiant);
		
		partieIndices = new ComposantIndices(_fenetre, _springLayout, coeurClient);
		partieIndices.estVisible(false);
		horlogeJoueur = new ComposantHorlogeJoueur(_fenetre, _springLayout);
		partieIndices.estVisible(false);
	}
	
// ----- METHODES ----- //
	
	/**
	 * La méthode estVisible permet d'afficher ou non le conteneur de connection
	 * @param _visible : "boolean" affiche ou masque ce conteneur
	 */
	final public void estVisible(boolean _visible)
	{
		boutonDeconnexion.setVisible(_visible);
		labelConnecteEnTant.setVisible(_visible);
		labelIdentifiant.setVisible(_visible);
		partieIndices.estVisible(_visible);
		horlogeJoueur.estVisible(_visible);
	}
	
	/**
	 * 
	 * @return le label de l'identificant
	 */
	final public JLabel getLabelIdentifiant() 
	{
		return labelIdentifiant;
	}
	
	/**
	 * 
	 * @param _labelIdentifiant le label de l'identificant
	 */
	final public void changerNomJoueur(String _login) 
	{
		labelIdentifiant.setText(_login);
		partieIndices.setLogin(_login);
		partieIndices.afficherIndices();
	}
	
	// ----- Getters & Setter ----- //
	
	/**
	 * 
	 * @return
	 */
	
	final public ComposantIndices getPartieIndices()
	{
		return partieIndices;
	}
	
	/**
	 * 
	 * @param _partieIndices
	 */
	
	final public void setPartieIndices(ComposantIndices _partieIndices)
	{
		this.partieIndices = _partieIndices;
	}

	/**
	 * Getter du composant horloge joueur
	 * @return le composant horloge joueur
	 */
	public ComposantHorlogeJoueur getHorlogeJoueur()
	{
		return horlogeJoueur;
	}

	/**
	 * Setter du composant horloge joueur 
	 * @param _horlogeJoueur le composant horloge joueur
	 */
	public void setHorlogeJoueur(ComposantHorlogeJoueur _horlogeJoueur)
	{
		this.horlogeJoueur = _horlogeJoueur;
	}
	
	
}
