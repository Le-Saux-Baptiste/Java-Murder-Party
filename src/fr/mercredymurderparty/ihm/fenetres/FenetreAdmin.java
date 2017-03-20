package fr.mercredymurderparty.ihm.fenetres;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JButton;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.ihm.composants.ComposantGestionChronoGeneral;
import fr.mercredymurderparty.ihm.composants.ComposantGestionIndices;
import fr.mercredymurderparty.ihm.composants.ComposantGestionJoueur;
import fr.mercredymurderparty.ihm.composants.ComposantGestionJoueurs;
import fr.mercredymurderparty.ihm.composants.ComposantGestionAlerte;
import fr.mercredymurderparty.ihm.composants.ComposantAlerte;
import fr.mercredymurderparty.ihm.composants.ComposantParametres;
import fr.mercredymurderparty.ihm.composants.ComposantTchat;
import fr.mercredymurderparty.outil.Fonctions;
import fr.mercredymurderparty.serveur.CoeurServeur;
import fr.mercredymurderparty.serveur.CompteurTempsGeneral;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("serial")
public class FenetreAdmin extends JFrame 
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private Dimension screenSize;
	private JPanel contentPane;
	private JButton btnArreterLaPartie;
	private JButton boutonChargerPartie;
	private JButton boutonNouvellePartie;
	private JButton btnGestionDesJoueurs;
	private JButton btnGestionDesAlerte;
	private JButton btngestionChronoGeneral;
	private boolean partieLance = false;
	private boolean partieChargeeCree = false; // vaudra vrai si la partie est chargée ou créée
	private JButton boutonPleinEcran;
	private boolean estEnPleinEcran;
	private GraphicsDevice device;
	
	// ----- APPEL DE CLASSES ----- //
	private Fonctions fct;
	
	// ----- COMPOSANTS ----- //
	private ComposantTchat partieTchat;
	private ComposantGestionJoueur partieCreerModifierJoueur;
	private ComposantGestionJoueurs partieGestionJoueurs;
	private ComposantGestionAlerte partieGestionAlerte;
	private ComposantGestionIndices partieGestionIndices;
	private ComposantParametres partieParametres;
	private ComposantGestionChronoGeneral partieGestionChronoGeneral;
	private ComposantAlerte partieAlerte;
	
	// ----- PARTIE CLIENT DE l'APPLICATION ----- //
	private CoeurClient coeurClient;
	
	// ----- MODELE DU SERVEUR ------ //
	private CoeurServeur coeurServeur;
	private CompteurTempsGeneral chronoGeneral;
	
	//private Server server;
	
	
// ----- CONSTRUCTEUR ----- //

	/**
	 * Constructeur de la classe FenetreAdmin
	 */
	public FenetreAdmin(GraphicsDevice _device, CoeurClient _coeurClient, CoeurServeur _coeurServeur) 
	{
		coeurClient = _coeurClient;
		coeurServeur = _coeurServeur;
        // Démarrer / arreter le serveur pour accéder a la bdd à distance
		/*addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				server.stop();
			}
		});
		
		try 
		{
			server = Server.createTcpServer("-tcpPort", "12500");
			server.start();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}*/
        
        
        //System.out.println("Le serveur bdd a ete créer !");
        //System.out.println("Adresse : jdbc:h2:tcp://localhost:12500/~/murder_bdd (user: sa, password: sa)");
        
		// Afficher les composants de l'interface utilisateur
		afficherInterface(_device);
	}
	
	
// ----- METHODES ----- //	
	
	/**
	 * Procédure qui va afficher tous les composants de l'interface utilisateur
	 */
	private void afficherInterface(GraphicsDevice _device)
	{
		setTitle("Murder Party - Interface Administrateur");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 699, 442);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setMinimumSize(new Dimension(800, 600));
		setSize(new Dimension(800, 600));
		screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-this.getWidth())/2, (screenSize.height-this.getHeight())/2);
		setContentPane(contentPane);
		final SpringLayout slContentPane = new SpringLayout();
		contentPane.setLayout(slContentPane);
		device = _device;
		
		/**
		 * Boutton composant paramètres
		 */
		JButton btnParamtres = new JButton("Paramètres");
		btnParamtres.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				// Masquer les autres composants
				partieGestionIndices.estVisible(false);
				//partieCreerModifierJoueur.estVisible(false);
				partieGestionJoueurs.estVisible(false);
				partieGestionAlerte.estVisible(false);
				partieGestionChronoGeneral.estVisible(false);
				
				// Afficher le composant paramètres
				partieParametres.estVisible(true);
			}
		});
		contentPane.add(btnParamtres);
		
		/**
		 * Boutton composant arreter la partie
		 */
		btnArreterLaPartie = new JButton("Lancer la partie");
		slContentPane.putConstraint(SpringLayout.NORTH, btnParamtres, 0, SpringLayout.NORTH, btnArreterLaPartie);
		slContentPane.putConstraint(SpringLayout.NORTH, btnArreterLaPartie, 0, SpringLayout.NORTH, contentPane);
		slContentPane.putConstraint(SpringLayout.WEST, btnArreterLaPartie, 0, SpringLayout.WEST, contentPane);
		//slContentPane.putConstraint(SpringLayout.EAST, btnGestionDesJoueurs, -6, SpringLayout.WEST, btnDonnerPrendreTemps);
		
		btnArreterLaPartie.setEnabled(false);
		btnArreterLaPartie.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent _e) 
			{
				if(partieLance == true)
				{
					// On arrête la partie
					partieLance = false;
					btnArreterLaPartie.setText("Lancer la partie");
					boutonChargerPartie.setVisible(true);
					boutonNouvellePartie.setVisible(true);
					coeurServeur.getTempsGlobalRestant().arreter();
					coeurServeur.setPartieLancee(false);
					btngestionChronoGeneral.setVisible(false);
					
					// si le panel de gestion du chrono général est visible, on le masque
					if(partieGestionChronoGeneral.isVisible())
					{
						partieGestionChronoGeneral.estVisible(false);
					}
				}
				else
				{
					// On lance la partie
					if(partieChargeeCree == true)
					{
						partieLance = true;
						btnArreterLaPartie.setText("Arrêter la partie");
						boutonChargerPartie.setVisible(false);
						boutonNouvellePartie.setVisible(false);
						coeurServeur.setTempsGlobalRestant(new CompteurTempsGeneral(coeurServeur));
						coeurServeur.getTempsGlobalRestant().addObserver(partieAlerte);
						coeurServeur.setPartieLancee(true);
						btngestionChronoGeneral.setVisible(true);
					}
				}
				//partieTchat.arreterCommunication();
			}
		});
		contentPane.add(btnArreterLaPartie);
		
		/**
		 * Boutton composant creer nouveau joueur
		 */
		/*final JButton btnCrerNouveauJoueur = new JButton("Créer nouveau joueur");
		slContentPane.putConstraint(SpringLayout.NORTH, btnCrerNouveauJoueur, 40, SpringLayout.NORTH, contentPane);
		slContentPane.putConstraint(SpringLayout.EAST, btnCrerNouveauJoueur, 0, SpringLayout.EAST, contentPane);
		btnCrerNouveauJoueur.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Masquer les autres composants
				partieGestionIndices.estVisible(false);
				partieParametres.estVisible(false);
				
				// montre le champ du chrono (utile si l'utilisateur clique d'abord sur modifier joueur ou supprimer joueur puis creer joueur)
				partieCreerModifierJoueur.estVisibleComposantChrono(true);
				
				// destruction de la liste obligatoire car si le joueur clique deja sur supprimer joueur puis modifier, pb sur la liste
				partieCreerModifierJoueur.detruireComposantListe();
				
				partieCreerModifierJoueur.setActionAEffectuer(1);
				partieCreerModifierJoueur.estVisible(true);
				partieCreerModifierJoueur.gestionJoueur();
				partieCreerModifierJoueur.changerNomMenu("Créer un nouveau joueur");
			}
		});
		contentPane.add(btnCrerNouveauJoueur);
		btnCrerNouveauJoueur.setVisible(false);*/
		
		/**
		 * Boutton composant modifier joueur
		 */
		/*final JButton btnModifierJoueur = new JButton("modifier joueur");
		slContentPane.putConstraint(SpringLayout.NORTH, btnModifierJoueur, 0, SpringLayout.NORTH, btnCrerNouveauJoueur);
		slContentPane.putConstraint(SpringLayout.EAST, btnModifierJoueur, -6, SpringLayout.WEST, btnCrerNouveauJoueur);
		btnModifierJoueur.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Masquer les autres composants
				partieGestionIndices.estVisible(false);
				partieParametres.estVisible(false);
				
				// masque les informations liees au temps d'initialisation du chronometre. Ce n'est pas comme ca qu'on le modifie
				partieCreerModifierJoueur.estVisibleComposantChrono(false);
				
				// destruction de la liste obligatoire car si le joueur clique deja sur supprimer joueur puis modifier, pb sur la liste
				partieCreerModifierJoueur.detruireComposantListe();
				
				partieCreerModifierJoueur.setActionAEffectuer(2);
				partieCreerModifierJoueur.estVisible(true);
				partieCreerModifierJoueur.creerListe();
				partieCreerModifierJoueur.gestionJoueur();
				partieCreerModifierJoueur.changerNomMenu("Modifier un joueur");
			}
		});
		contentPane.add(btnModifierJoueur);
		btnModifierJoueur.setVisible(false);*/
		
		/**
		 * Boutton composant récompenser / punir
		 */
		/*final JButton btnDonnerPrendreTemps = new JButton("Récompenser / Punir");
		slContentPane.putConstraint(SpringLayout.NORTH, btnDonnerPrendreTemps, -40, SpringLayout.NORTH, btnCrerNouveauJoueur);
		slContentPane.putConstraint(SpringLayout.EAST, btnDonnerPrendreTemps, -100, SpringLayout.WEST, btnCrerNouveauJoueur);
		btnDonnerPrendreTemps.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Masquer les autres composants
				partieGestionIndices.estVisible(false);
				partieParametres.estVisible(false);
				partieCreerModifierJoueur.estVisible(false);
				
				//partieCreerModifierJoueur.estVisible(false);
				//  les informations liees au temps d'initialisation du chronometre. Ce n'est pas comme ca qu'on le modifie
				partieCreerModifierJoueur.estVisibleComposantChrono(true);
				
				// destruction de la liste obligatoire car si le joueur clique deja sur supprimer joueur puis modifier, pb sur la liste
				partieCreerModifierJoueur.detruireComposantListe();
				
				partieCreerModifierJoueur.setActionAEffectuer(4);
				partieCreerModifierJoueur.estVisible(true);
				partieCreerModifierJoueur.creerListe();
				partieCreerModifierJoueur.gestionJoueur();
				partieCreerModifierJoueur.changerNomMenu("Récompenser / Punir");
			}
		});
		contentPane.add(btnDonnerPrendreTemps);
		btnDonnerPrendreTemps.setVisible(false);*/
		
		/**
		 * Boutton composant supprimer joueur
		 */
		/*final JButton btnSupprimerJoueur = new JButton("supprimer joueur");
		slContentPane.putConstraint(SpringLayout.NORTH, btnSupprimerJoueur, 0, SpringLayout.NORTH, btnCrerNouveauJoueur);
		slContentPane.putConstraint(SpringLayout.EAST, btnSupprimerJoueur, -6, SpringLayout.WEST, btnModifierJoueur);
		btnSupprimerJoueur.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Masquer les autres composants
				partieGestionIndices.estVisible(false);
				partieParametres.estVisible(false);
				
				// masque les informations liees au temps d'initialisation du chronometre. Ce n'est pas comme ca qu'on le modifie
				partieCreerModifierJoueur.estVisibleComposantChrono(false);
				
				// destruction de la liste obligatoire car si le joueur clique deja sur modifier joueur puis supprimer, pb sur la liste
				partieCreerModifierJoueur.detruireComposantListe();
				
				partieCreerModifierJoueur.setActionAEffectuer(3);
				partieCreerModifierJoueur.estVisible(true);
				partieCreerModifierJoueur.creerListe();
				partieCreerModifierJoueur.gestionJoueur();
				partieCreerModifierJoueur.changerNomMenu("Supprimer un joueur");
			}
		});
		contentPane.add(btnSupprimerJoueur);
		btnSupprimerJoueur.setVisible(false);*/
		
		/**
		 * Boutton gestion des indices
		 */
		final JButton btnGestionIndices = new JButton("Gestion des indices");
		slContentPane.putConstraint(SpringLayout.NORTH, btnGestionIndices, 0, SpringLayout.NORTH, btnParamtres);
		slContentPane.putConstraint(SpringLayout.EAST, btnGestionIndices, -10, SpringLayout.EAST, contentPane);
		btnGestionIndices.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Masquer les autres composants
				//partieCreerModifierJoueur.estVisible(false);
				partieGestionJoueurs.estVisible(false);
				partieParametres.estVisible(false);
				partieGestionAlerte.estVisible(false);
				partieGestionChronoGeneral.estVisible(false);
				
				// Affiche la fenetre ou l'on gère des indices
				partieGestionIndices.effacerFormulaire();
				partieGestionIndices.chargerPersonnages();
				partieGestionIndices.rafraichirListeIndices();
				partieGestionIndices.estVisible(true);
			}
		});
		contentPane.add(btnGestionIndices);
		btnGestionIndices.setVisible(false);
		
		/**
		 * Bouton gestion du chrono general
		 */
		btngestionChronoGeneral = new JButton("gestion chronomètre general");
		slContentPane.putConstraint(SpringLayout.NORTH, btngestionChronoGeneral, 30, SpringLayout.NORTH, contentPane);
		slContentPane.putConstraint(SpringLayout.EAST, btngestionChronoGeneral, -10, SpringLayout.EAST, contentPane);
		btngestionChronoGeneral.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Masquer les autres composants
				partieGestionJoueurs.estVisible(false);
				partieParametres.estVisible(false);
				partieGestionAlerte.estVisible(false);
				partieGestionIndices.estVisible(false);
				
				// si une partie est lancée
				if(partieLance)
				{
					System.out.println("ok");
					partieGestionChronoGeneral.constructionIHM(coeurServeur);
					partieGestionChronoGeneral.estVisible(true);
				}
			}
		});
		contentPane.add(btngestionChronoGeneral);
		btngestionChronoGeneral.setVisible(false);
		
		/**
		 * Bouton nouvelle partie
		 */
		
		boutonNouvellePartie = new JButton("Nouvelle partie");
		slContentPane.putConstraint(SpringLayout.NORTH, boutonNouvellePartie, 0, SpringLayout.NORTH, contentPane);
		slContentPane.putConstraint(SpringLayout.WEST, boutonNouvellePartie, 10, SpringLayout.EAST, btnArreterLaPartie);
		boutonNouvellePartie.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent _e) 
			{
				JOptionPane jopSaisie = new JOptionPane();
				String nom = jopSaisie.showInputDialog(null, "Nom de la nouvelle partie : ", "Rentrez un nom", JOptionPane.QUESTION_MESSAGE);
				coeurServeur.setNomBDD(nom+".sqlite");
				if (nom != null)
				{
					// "CREATION" DE LA BDD 
					// ( en réalité on copie une bdd sqlite "vide" dans le répertoire res et on lui donne le nom entré par l'utilisateur
					File fichierSource = new File("res/modele/bdd_vide.sqlite"); // la bdd vide
					File fichierDest = new File("res/"+nom+".sqlite"); // la bdd à "créer"
					try{
						// Declaration et ouverture des flux
						java.io.FileInputStream sourceFile = new java.io.FileInputStream(fichierSource);
						
						try{
							java.io.FileOutputStream destinationFile = null;
							
							try{
								destinationFile = new FileOutputStream(fichierDest);
								
								// Lecture par segment de 0.5Mo 
								byte buffer[] = new byte[512 * 1024];
								int nbLecture;
								
								while ((nbLecture = sourceFile.read(buffer)) != -1)
								{
									destinationFile.write(buffer, 0, nbLecture);
								}
							} finally {
								destinationFile.close();
							}
						} finally {
							sourceFile.close();
						}
					} catch (IOException e){
						e.printStackTrace();
					}
					
					// MODIFICATION DE L'INTERFACE
					partieGestionJoueurs.listeJoueurs(partieGestionJoueurs.getTableJoueurs(), null);
					partieGestionIndices.rafraichirListeIndices();
					//boutonNouvellePartie.setVisible(false);
					//boutonChargerPartie.setVisible(true);
					//btnCrerNouveauJoueur.setVisible(true);
					//btnDonnerPrendreTemps.setVisible(true);
					btnGestionIndices.setVisible(true);
					btnGestionDesJoueurs.setVisible(true);
					btnGestionDesAlerte.setVisible(true);
					btngestionChronoGeneral.setVisible(false);
					//btnModifierJoueur.setVisible(true);
					//btnSupprimerJoueur.setVisible(true);
					partieChargeeCree = true;
					btnArreterLaPartie.setEnabled(true);
				}
			}
		});
		contentPane.add(boutonNouvellePartie);
		
		/**
		 * Bouton charger partie
		 */
		
		boutonChargerPartie = new JButton("Charger Partie");
		slContentPane.putConstraint(SpringLayout.WEST, btnParamtres, 43, SpringLayout.EAST, boutonChargerPartie);
		slContentPane.putConstraint(SpringLayout.NORTH, boutonChargerPartie, 0, SpringLayout.NORTH, contentPane);
		slContentPane.putConstraint(SpringLayout.WEST, boutonChargerPartie, 10, SpringLayout.EAST, boutonNouvellePartie);
		boutonChargerPartie.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent _e) 
			{
				JFileChooser selectionneurFichier = new JFileChooser();
				selectionneurFichier.setCurrentDirectory(new File(
						System.getProperty("user.dir") + File.separator + "res"));
				File fichier;
				if(selectionneurFichier.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					fichier = selectionneurFichier.getSelectedFile();	
					coeurServeur.setNomBDD(fichier.getName());
					partieGestionJoueurs.listeJoueurs(partieGestionJoueurs.getTableJoueurs(), null);
					partieGestionAlerte.listeAlerte(partieGestionAlerte.getTableAlerte(), null);
					partieGestionIndices.rafraichirListeIndices();
					//boutonChargerPartie.setVisible(false);
					//boutonNouvellePartie.setVisible(true);
					//btnCrerNouveauJoueur.setVisible(true);
					//btnDonnerPrendreTemps.setVisible(true);
					btnGestionIndices.setVisible(true);
					btnGestionDesJoueurs.setVisible(true);
					btnGestionDesAlerte.setVisible(true);
					btngestionChronoGeneral.setVisible(false);
					//btnModifierJoueur.setVisible(true);
					//btnSupprimerJoueur.setVisible(true);
					partieChargeeCree = true;
					btnArreterLaPartie.setEnabled(true);
				}
			}
		});
		contentPane.add(boutonChargerPartie);
		
		btnGestionDesJoueurs = new JButton("Gestion des Joueurs");
		slContentPane.putConstraint(SpringLayout.NORTH, btnGestionDesJoueurs, 0, SpringLayout.NORTH, btnParamtres);
		slContentPane.putConstraint(SpringLayout.EAST, btnGestionDesJoueurs, -6, SpringLayout.WEST, btnGestionIndices);
		btnGestionDesJoueurs.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				// Masquer les autres composants
				partieGestionIndices.estVisible(false);
				partieParametres.estVisible(false);
				partieGestionAlerte.estVisible(false);
				//partieCreerModifierJoueur.estVisible(false);
				
				// Afficher le composant paramètres
				partieGestionJoueurs.estVisible(true);
			}
		});
		contentPane.add(btnGestionDesJoueurs);
		btnGestionDesJoueurs.setVisible(false);
		
		btnGestionDesAlerte = new JButton("Gestion des Alertes");
		slContentPane.putConstraint(SpringLayout.NORTH, btnGestionDesAlerte, 0, SpringLayout.NORTH, btnParamtres);
		slContentPane.putConstraint(SpringLayout.EAST, btnGestionDesAlerte, -6, SpringLayout.WEST, btnGestionDesJoueurs);
		btnGestionDesAlerte.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				// Masquer les autres composants
				partieGestionIndices.estVisible(false);
				partieParametres.estVisible(false);
				partieGestionJoueurs.estVisible(false);
				partieGestionChronoGeneral.estVisible(false);
				
				// Afficher le composant paramètres
				partieGestionAlerte.estVisible(true);
			}
		});
		contentPane.add(btnGestionDesAlerte);
		btnGestionDesAlerte.setVisible(false);
		
		/*
		 * Bouton de pour mettre en plein écran la fenêtre ( BAS-DROITE )
		 * 
		 * @bouton boutonPleinEcran
		 */
		
		boutonPleinEcran = new JButton(new ImageIcon("themes/bouton_plein_ecran.png"));
		boutonPleinEcran.setRolloverIcon(new ImageIcon("themes/bouton_plein_ecran_survol.png"));
		boutonPleinEcran.setPressedIcon(new ImageIcon("themes/bouton_plein_ecran_enfonce.png"));
		boutonPleinEcran.setContentAreaFilled(false);
		boutonPleinEcran.setFocusPainted(false);
		boutonPleinEcran.setFocusable(false);
		boutonPleinEcran.setBorder(null);
		boutonPleinEcran.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent _e)
			{
				if(estEnPleinEcran == true)
				{
					pleinEcran(false);
				}
				else
				{
					pleinEcran(true);
				}
			}
		});
		slContentPane.putConstraint(SpringLayout.SOUTH, boutonPleinEcran, -10, SpringLayout.SOUTH, getContentPane());
		slContentPane.putConstraint(SpringLayout.EAST, boutonPleinEcran, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(boutonPleinEcran);
		
		/**
		 * Instance des differents composants de l'ihm
		 */
		
		// Instancier le composant : gestion des joueurs
		//partieCreerModifierJoueur = new ComposantGestionJoueur(this, slContentPane);
		//partieCreerModifierJoueur.estVisible(false);
		
		// Instancier le composant : gestion des joueurs
		partieGestionJoueurs = new ComposantGestionJoueurs(this, slContentPane, coeurServeur);
		partieGestionJoueurs.estVisible(false);
		
		// Instancier le composant : gestion des indices
		partieGestionIndices = new ComposantGestionIndices(this, slContentPane, coeurServeur);
		partieGestionIndices.estVisible(false);
		
		// Instancier le composant : gestion du chrono général (rem : une partie doit être lancée pour avoir accées à la valeur du chronomètre principal)
		partieGestionChronoGeneral = new ComposantGestionChronoGeneral(this, slContentPane, coeurServeur);
		partieGestionChronoGeneral.estVisible(false);
		
		// Instancier le composant : paramètres
		partieParametres = new ComposantParametres(this, slContentPane);
		partieParametres.estVisible(false);
		
		// Instancier le composant : gestion des alertes
		partieGestionAlerte = new ComposantGestionAlerte(this, slContentPane, coeurServeur);
		partieGestionAlerte.estVisible(false);
		
		// Instancier le composant : alertes
		partieAlerte = new ComposantAlerte(coeurServeur);
		
		// Instancier le composant : partie tchat
		partieTchat = new ComposantTchat(this, slContentPane, partieGestionIndices.getPanelGestionIndices(), coeurClient);
		partieTchat.demarrerCommunication(true);
		partieTchat.changerNomJoueur("MaitreDeJeu");
		partieTchat.estVisible(true);
	}
	
// ----- METHODES ----- //
	
	/**
	 * La méthode PleinEcran permet de mettre la fenêtre en plein écran
	 * @param _pleinEcran : un booléen activant ou désactivant le mode plein écran
	 */
	
	final public void pleinEcran (boolean _pleinEcran)
	{
		if(_pleinEcran == true)
		{
			_pleinEcran = device.isFullScreenSupported();
		}
		dispose();
		setUndecorated(_pleinEcran);
		setResizable(!_pleinEcran);
		if (_pleinEcran)
		{
			// Mode plein écran
			device.setFullScreenWindow(this);
			validate();
			estEnPleinEcran = true;
		}
		else
		{
			// Mode fenêtré
			device.setFullScreenWindow(null);
			setSize(new Dimension(800, 600));
			estEnPleinEcran = false;
		}
		setVisible(true);
	}
}


