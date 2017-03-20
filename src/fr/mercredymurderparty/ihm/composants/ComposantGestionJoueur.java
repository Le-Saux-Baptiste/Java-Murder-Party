package fr.mercredymurderparty.ihm.composants;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.mercredymurderparty.ihm.fenetres.FenetreAdmin;
import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.serveur.CoeurServeur;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

@SuppressWarnings("serial")
public class ComposantGestionJoueur extends JPanel
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private JPanel panelCreerModifierJoueur;
	private JScrollPane panelBarre;
	private	SpringLayout slPanelCreerModifierJoueur;
	private JLabel lblMenu;
	private JTextField champIdentifiant;
	private JTextField champMotDePasse;
	private JTextField champNom;
	private JTextField champPrenom;
	private JTextField champTemps;
	private JLabel lblDureDuChronomtre;
	private JLabel lblselectionnerJoueur;
	private JList liste;
	
	// ----- BASE DE DONNEES ----- //
	private BaseDeDonnees bdd;
	
	// ----- MODELE DU SERVEUR ----- //
	private CoeurServeur coeurServeur;
	
	// ----- variables supplementaires ----- //
	private int actionAEffectuer; // variable qui va nous permettre de savoir quelle action effectuer si on clique sur le bouton valider :
								  // si vaut 1 : creation d'un joueur
								  // si vaut 2 : modification d'un joueur existant
								  // si vaut 3 : suppression d'un joueur
								  // si vaut 4 : donner / prendre du temps à un joueur
	
// ----- CONSTRUCTEUR ----- //
	
	public ComposantGestionJoueur(FenetreAdmin _fenetre, final SpringLayout _springLayout, CoeurServeur _coeurServeur)
	{	
		coeurServeur = _coeurServeur;
		panelCreerModifierJoueur = new JPanel();
		_springLayout.putConstraint(SpringLayout.NORTH, panelCreerModifierJoueur, -200, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.SOUTH, panelCreerModifierJoueur, 200, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.WEST, panelCreerModifierJoueur, -250, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.EAST, panelCreerModifierJoueur, 250, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane());
		_fenetre.getContentPane().add(panelCreerModifierJoueur);
		this.setLayout(_springLayout);
		slPanelCreerModifierJoueur = new SpringLayout();
		panelCreerModifierJoueur.setLayout(slPanelCreerModifierJoueur);
		
		lblMenu = new JLabel("Test :");
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, lblMenu, 10, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, lblMenu, 0, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(lblMenu);
		
		JLabel lblIdentifiant = new JLabel("Identifiant :");
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, lblIdentifiant, 50, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, lblIdentifiant, 0, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(lblIdentifiant);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe :");
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, lblMotDePasse, 90, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, lblMotDePasse, 0, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(lblMotDePasse);
		
		JLabel lblNom = new JLabel("Nom :");
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, lblNom, 130, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, lblNom, 0, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(lblNom);
		
		JLabel lblPrnom = new JLabel("Prénom :");
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, lblPrnom, 170, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, lblPrnom, 0, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(lblPrnom);
		
		lblDureDuChronomtre = new JLabel("<html>Durée du chronomètre<br> en sec :");
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, lblDureDuChronomtre, 220, SpringLayout.WEST, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, lblDureDuChronomtre, 0, SpringLayout.NORTH, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(lblDureDuChronomtre);
		
		champIdentifiant = new JTextField();
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, champIdentifiant, 50, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, champIdentifiant, 170, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(champIdentifiant);
		champIdentifiant.setColumns(10);
		
		champMotDePasse = new JTextField();
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, champMotDePasse, 90, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, champMotDePasse, 170, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(champMotDePasse);
		champMotDePasse.setColumns(10);
		
		champNom = new JTextField();
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, champNom, 130, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, champNom, 170, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(champNom);
		champNom.setColumns(10);
		
		champPrenom = new JTextField();
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, champPrenom, 170, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, champPrenom, 170, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(champPrenom);
		champPrenom.setColumns(10);
		
		champTemps = new JTextField();
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, champTemps, 220, SpringLayout.NORTH, panelCreerModifierJoueur);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.WEST, champTemps, 170, SpringLayout.WEST, panelCreerModifierJoueur);
		panelCreerModifierJoueur.add(champTemps);
		champTemps.setColumns(10);
	}
	
	
// ----- METHODES ----- //
	
	/**
	 * La méthode estVisible permet d'afficher ou non le conteneur de création joueur
	 * @param _visible : "boolean" affiche ou masque ce conteneur
	 */
	final public void estVisible(boolean _visible)
	{
		panelCreerModifierJoueur.setVisible(_visible);
	}
	
	
	final public void changerNomMenu(String titre)
	{
		lblMenu.setText(titre);
	}
	
	/**
	 * Methode qui initialise les champs des JTextField avec les valeurs suivantes :
	 * 		<br>- identifiant : vide => ""
	 * 		<br>- mot de passe : vide
	 * 		<br>- nom : vide
	 * 		<br>- prenom : vide
	 * 		<br>- initialisation du chronometre : 600 s => 10 min
	 */
	final public void initChamp()
	{
		champIdentifiant.setText("");
		champMotDePasse.setText("");
		champNom.setText("");
		champPrenom.setText("");
		champTemps.setText("");
	}
	
	
	/**
	 * Methode qui permet de creer, modifier ou supprimer un nouveau joueur dans la base de donnees
	 */
	final public void gestionJoueur()
	{				
		// initialisation des champs des JTextField
		initChamp();
		
		JButton boutonValider = new JButton("Valider");
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.SOUTH, boutonValider, 50, SpringLayout.SOUTH, champTemps);
		slPanelCreerModifierJoueur.putConstraint(SpringLayout.EAST, boutonValider, 0, SpringLayout.EAST, champTemps);
		boutonValider.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{				
				// traitement des differents cas (creation, modification ou suppression d'un joueur)
				switch(getActionAEffectuer())
				{
				// creation joueur
				case 1 :
					try
					{	
						int champChronoInt; // variable qui contiendra la valeur du JtextField mais en int
						
						// recuperation du JTextField
						champChronoInt = Integer.parseInt(champTemps.getText());
						
						// connexion a la base de donnes
						bdd = new BaseDeDonnees(coeurServeur);
						
						// insertion dans la base de données des valeurs des champs
						bdd.executerInstruction("INSERT INTO personnage (login, mdp, nom, prenom, temps) VALUES ( " +
												"\"" + champIdentifiant.getText() +"\", " +
												"\"" + champMotDePasse.getText()  +"\", " +
												"\"" + champNom.getText()         +"\", " +
												"\"" + champPrenom.getText()      +"\", " +
												"\"" + champChronoInt       +"\")");
						
						// deconnexion base de donnes
						bdd.fermerConnexion();
						
						// message informant le maitre de jeu que la création a bien ete effectue
						JOptionPane.showMessageDialog(null, "le joueur à bien été créé", "confirmation de la création", JOptionPane.INFORMATION_MESSAGE); 
					} 
					catch (SQLException e)
					{
						e.printStackTrace();
						
						// message informant le maitre de jeu qu'il y a eu un probleme lors de la creation d'un joueur
						JOptionPane.showMessageDialog(null, "un problème est survenu lors de la creation du joueur", "annulation de la création", JOptionPane.ERROR_MESSAGE); 
					}
					// si n'est pas un nombre
					catch(NumberFormatException e)
					{
						System.out.println(e.getStackTrace());
					}
					break;
					
				// modification joueur
				case 2 :
					modifierJoueur();
					detruireComposantListe();
					break;
					
				// suppression joueur
				case 3 :
					supprimerJoueur();
					detruireComposantListe();
					break;
				//Donner / prendre temps
				case 4 :
					modifierTemps();
					break;
				}
				
				// masque tous le panel de creation, modification, suppression des joueurs
				estVisible(false);
			}
		});
		panelCreerModifierJoueur.add(boutonValider);
	}

	
	/**
	 * methode qui crée une JListe et préremplie les JTextField
	 */
	final public void creerListe()
	{	
		try
		{	
			ResultSet retourBaseDeDonnees;
			
			// connexion base de donnes
			bdd = new BaseDeDonnees(coeurServeur);
			
			retourBaseDeDonnees = bdd.executerRequete("SELECT login FROM personnage");
			
			// vector dans lequel on stockera les valeurs de la base de donnees (les noms de tous les joueurs de la base de donnees)
			Vector<String> identifiant = new Vector<String>();
			
			// recuperation des valeurs de la base de donnes et stockage dans le vector
			while(retourBaseDeDonnees.next())
			{
				identifiant.add(retourBaseDeDonnees.getString("login"));
			}
			
			lblselectionnerJoueur = new JLabel("selectionner le login du joueur :");
			slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, lblselectionnerJoueur, 0, SpringLayout.WEST, panelCreerModifierJoueur);
			slPanelCreerModifierJoueur.putConstraint(SpringLayout.EAST, lblselectionnerJoueur, 0, SpringLayout.EAST, panelCreerModifierJoueur);
			panelCreerModifierJoueur.add(lblselectionnerJoueur);
			
			// creation d'une JList pour proposer les different noms des joueurs dans la base de donnees
			liste = new JList(identifiant);
			
			// si un element est selectionne dans la JList, on le place dans les JTextField
			liste.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent arg0)
				{
					Object elementselectionne; // l'element selectionne dans la JList
					ResultSet retourBaseDonnees;
					
					// recupere et place le nom a modifier dans la variable
					elementselectionne = liste.getModel().getElementAt( liste.getSelectedIndex() );
					try
					{
						// connexion a la base de donnees
						bdd = new BaseDeDonnees(coeurServeur);
						
						// place les champs du nom dans les JTextField adéquat ( => préremplie les JTextField )
						retourBaseDonnees = bdd.executerRequete("SELECT login, mdp, nom, prenom, temps FROM personnage WHERE login = '" + elementselectionne + "'");
						preremplirText(retourBaseDonnees);
						
						// deconnexion de la base de donnees
						bdd.fermerConnexion();
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
			});
			// fin ecouteur JList
			
			// ajout d'une barre verticale sur la liste
			panelBarre = new JScrollPane(liste);
			slPanelCreerModifierJoueur.putConstraint(SpringLayout.NORTH, panelBarre, 40, SpringLayout.NORTH, panelCreerModifierJoueur);
			slPanelCreerModifierJoueur.putConstraint(SpringLayout.EAST, panelBarre, -40, SpringLayout.EAST, panelCreerModifierJoueur);
			panelCreerModifierJoueur.add(panelBarre);
			
			// deconnexion base de donnees
			bdd.fermerConnexion();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode qui prerempli les JtextFields avec les valeurs de la base de donnees
	 * @param nom : le nom du joueur dont il faut recuperer les champs de la base de donnes
	 */
	final public void preremplirText(ResultSet _retourBaseDeDonnees)
	{
		try
		{
			while(_retourBaseDeDonnees.next())
			{
				String login = _retourBaseDeDonnees.getString("login");
				String motPasse = _retourBaseDeDonnees.getString("mdp");
				String nomPerso = _retourBaseDeDonnees.getString("nom");
				String prenomPerso = _retourBaseDeDonnees.getString("prenom");
				String tempsPerso = _retourBaseDeDonnees.getString("temps");
				
				champIdentifiant.setText(login);
				champMotDePasse.setText(motPasse);
				champNom.setText(nomPerso);
				champPrenom.setText(prenomPerso);
				//champTemps.setText(tempsPerso);
				lblDureDuChronomtre.setText("<html>Durée du chronomètre<br> en sec : " + tempsPerso);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Methode qui modifie un joueur
	 * TODO bug si apastrophe dans un champ de la base de donnees
	 */
	final public void modifierJoueur()
	{		
		Object elementselectionne = liste.getModel().getElementAt( liste.getSelectedIndex() );

		// mise à jour de la base de données avec les valeurs des champs
		try
		{			
			// connexion base de donnees
			bdd = new BaseDeDonnees(coeurServeur);
			
			bdd.executerInstruction("UPDATE personnage " +
									"SET login = "   + "'" + champIdentifiant.getText() + "'" + "," +
										"mdp = "     + "'" + champMotDePasse.getText()  + "'" + "," +
										"nom = "     + "'" + champNom.getText()         + "'" + "," +
										"prenom = "  + "'" + champPrenom.getText()      + "'"       +
									"WHERE login = " + "'" + elementselectionne         + "'");
			
			// deconnexion base de donnees
			bdd.fermerConnexion();
			
			// message informant le maitre de jeu que la modification a bien ete effectue
			JOptionPane.showMessageDialog(null, "le joueur à bien été modifié", "confirmation de la modification", JOptionPane.INFORMATION_MESSAGE); 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			
			// message informant le maitre de jeu qu'il y a eu un probleme lors de la modification d'un joueur
			JOptionPane.showMessageDialog(null, "un problème est survenu lors de la modification du joueur", "annulation de la modification", JOptionPane.ERROR_MESSAGE); 
		}
	}
	
	/**
	 * 
	 */
	final public void modifierTemps()
	{		
		Object elementselectionne = liste.getModel().getElementAt( liste.getSelectedIndex() );

		// mise à jour de la base de données avec les valeurs des champs
		try
		{
			int champChronoInt; // variable qui contiendra la valeur du JtextField mais en int
			// recuperation du JTextField
			champChronoInt = Integer.parseInt(champTemps.getText());
			int tempsActuel;//variable qui contient le temps bdd du joueur
			
			ResultSet retourBaseDeDonnees;
			// connexion base de donnees
			bdd = new BaseDeDonnees(coeurServeur);
			
			//Recuperation du temps en bdd pour le mettre à jour
			retourBaseDeDonnees = bdd.executerRequete("SELECT temps FROM personnage WHERE login = '" + elementselectionne + "'");
			tempsActuel = Integer.parseInt(retourBaseDeDonnees.getString("temps"));
			
			
			bdd.executerInstruction("UPDATE personnage " +
									"SET temps = "   + "'" + (tempsActuel + champChronoInt) + "'"  +
									"WHERE login = " + "'" + elementselectionne         + "'");
			
			// deconnexion base de donnees
			bdd.fermerConnexion();
			
			// message informant le maitre de jeu que la modification a bien ete effectue
			JOptionPane.showMessageDialog(null, "le temps à bien été modifié", "confirmation de la modification", JOptionPane.INFORMATION_MESSAGE); 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			
			// message informant le maitre de jeu qu'il y a eu un probleme lors de la modification d'un joueur
			JOptionPane.showMessageDialog(null, "un problème est survenu lors de l'ajout / retire temps du joueur", "annulation de la modification", JOptionPane.ERROR_MESSAGE); 
		}
	}

	/**
	 * Methode qui supprime un joueur
	 */
	final public void supprimerJoueur()
	{
		Object elementselectionne = liste.getModel().getElementAt( liste.getSelectedIndex() );
		
		try
		{
			// connexion base de donnees
			bdd = new BaseDeDonnees(coeurServeur);
			
			bdd.executerInstruction("DELETE FROM personnage WHERE login = '" + elementselectionne + "'");
			
			// deconnexion base de donnees
			bdd.fermerConnexion();
			
			// message informant le maitre de jeu que la suppression a bien ete effectue
			JOptionPane.showMessageDialog(null, "le joueur à bien été supprimé", "confirmation de la suppression", JOptionPane.INFORMATION_MESSAGE); 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			
			// message informant le maitre de jeu qu'il y a eu un probleme lors de la suppression d'un joueur
			JOptionPane.showMessageDialog(null, "un problème est survenu lors de la suppression du joueur", "annulation de la suppression", JOptionPane.ERROR_MESSAGE); 
		}
	}

	
	/**
	 * Methode qui montre ou cache le champs d'initialisation du chronomètre
	 * @param : _param :
	 * 		<br> - si vaut true : montre les composants
	 * 		<br> - si vaut false : cache les composants
	 */
	final public void estVisibleComposantChrono(boolean _param)
	{
		champTemps.setEnabled(_param);
		champTemps.setVisible(_param);
		lblDureDuChronomtre.setVisible(_param);
	}
	
	/**
	 * Methode qui rend détruit du panel la liste, le JScrollPane, le JLabel "selectionner le login du joueur :"
	 */
	final public void detruireComposantListe()
	{
		try
		{
			// traitement de la liste
			panelCreerModifierJoueur.remove(liste);
			
			// traitement du JScrollpane
			panelCreerModifierJoueur.remove(panelBarre);
			
			// traitement du JLabel
			panelCreerModifierJoueur.remove(lblselectionnerJoueur);
			
			// rafraichissement du panel
			panelCreerModifierJoueur.updateUI();
		}
		catch(NullPointerException e)
		{
			// sert juste à eviter une erreur lorsque l'utilisateur creer en premier un joueur car la liste, le JScrollPane et le JLabel ne sont pas creer
		}
	}
	
// ----- GETTERS & SETTERS ----- //
	
	/**
	 * getter sur l'attribut ActionAEffectuer
	 * @return la valeur de l'attribut
	 * * 	<br> - 1 si on veut creer un joueur
	 * 		<br> - 2 si on veut modifier un joueur
	 * 		<br> - 3 si on veut supprimer un joueur
	 * 		<br> - 4 si on veut récompenser / punir un joueur
	 */
	public int getActionAEffectuer() 
	{
		return actionAEffectuer;
	}


	/**
	 * setter sur ActionAEffectuer qui dit si on veut l'action qu'on veut faire sur le joueur 
	 * @param _actionAEffectuer qui vaut :
	 * 		<br> - 1 si on veut creer un joueur
	 * 		<br> - 2 si on veut modifier un joueur
	 * 		<br> - 3 si on veut supprimer un joueur
	 */
	public void setActionAEffectuer(int _actionAEffectuer)
	{
		this.actionAEffectuer = _actionAEffectuer;
	}
	
	
	/**
	 * getter sur la liste
	 * @return la liste
	 */
	public JList getListe()
	{
		return liste;
	}
	
	
	/**
	 * getter sur le JTextField du temps d'initialisation du chronometre
	 * @return le JTextField
	 */
	public JTextField getChampTemps()
	{
		return champTemps;
	}


	/**
	 * getter sur le JLabel pour dire que c'est le champ d'initialisation du chronometre
	 * @return le JLabel
	 */
	public JLabel getLblDureDuChronomtre()
	{
		return lblDureDuChronomtre;
	}
	
	
	/**
	 * getter sur le JScrollPane contenat la liste des joueurs présants dans la table personnages
	 * @return le JScrollPane
	 */
	public JScrollPane getPanelBarre()
	{
		return panelBarre;
	}


	/**
	 * getter sur le JLabel contenant la phrase "selectionner le login du joueur :"
	 * @return le JLabel
	 */
	public JLabel getLblselectionnerJoueur() 
	{
		return lblselectionnerJoueur;
	}
}
