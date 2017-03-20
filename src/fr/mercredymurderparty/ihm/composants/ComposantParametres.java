package fr.mercredymurderparty.ihm.composants;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import fr.mercredymurderparty.ihm.fenetres.FenetreAdmin;
import fr.mercredymurderparty.outil.FichierTXT;
import fr.mercredymurderparty.outil.FichierXML;
import fr.mercredymurderparty.outil.Fonctions;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JSlider;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class ComposantParametres extends JPanel 
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private JTextField textField;
	private JScrollPane panelParametres;
	private JComboBox choixTheme;
	private JTextField champNomTheme;
	private Color couleur;
	private JTextField champCouleurP1;
	private JTextField champCouleurP2;
	private JTextField champCouleurP3;
	private JTextField champCouleurS1;
	private JTextField champCouleurS2;
	private JTextField champCouleurS3;
	private JTextField champCouleurW;
	private JTextField champCouleurB;
	private JTextField champOpaciteMenu;
	private JTextField champOpaciteFrame;
	private JSlider sliderOpaciteMenu;
	private JButton btnValider;
	private JSlider sliderOpaciteFrame;
	
	// ----- APPEL DE CLASSES ----- //
	private Fonctions fct;
	private FichierTXT txt;
	private FichierXML xml;
	
	// ----- VARIABLES ----- //
	private boolean nouveauTheme = false;
	
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Create the panel.
	 */
	public ComposantParametres(FenetreAdmin _fenetre, final SpringLayout _springLayout) 
	{
		fct = new Fonctions();
		txt = new FichierTXT();
		xml = new FichierXML("config.xml");
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
	
		panelParametres = new JScrollPane();
		_springLayout.putConstraint(SpringLayout.NORTH, panelParametres, -200, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane()); // Etirer vers le haut
		_springLayout.putConstraint(SpringLayout.SOUTH, panelParametres, 250, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane()); // Etirer vers le bas
		_springLayout.putConstraint(SpringLayout.WEST, panelParametres, -300, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane()); // Etirer vers la gauche
		_springLayout.putConstraint(SpringLayout.EAST, panelParametres, 400, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane()); // Etirer vers la droite
		_fenetre.getContentPane().add(panelParametres);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panelParametres.setViewportView(tabbedPane);
		
		JPanel pnlThemes = new JPanel();
		tabbedPane.addTab("Th\u00E8me de la partie", null, pnlThemes, null);
		pnlThemes.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(93dlu;pref):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(98dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(6dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblChoisirUnThme = new JLabel("Choisir un th\u00E8me :");
		pnlThemes.add(lblChoisirUnThme, "2, 2");
		
		choixTheme = new JComboBox();
		choixTheme.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JComboBox cb = (JComboBox) e.getSource();
				Object element = cb.getSelectedItem();
				
				if (element != null)
				{
					// Recuperer le nom du thème
					//xml = new FichierXML("config.xml");
					xml.modifierNoeud("theme", "nom", (String) element);
					
					// Définir le nom du thème dans la zone de saisie
					champNomTheme.setText((String) element);
					
					// Charger les ressources du thème
					chargerTheme();
					
					// Préciser que nous voulons modifier / supprimer le thème
					nouveauTheme = false;
					btnValider.setText("Modifier le thème");
				}
				else
				{
					// Préciser que nous voulons créer un nouveau thème
					nouveauTheme = true;
					btnValider.setText("Créer le thème");
					
					// Nettoyer les composants
					nettoyerFormulaire();
				}
			}
		});
		pnlThemes.add(choixTheme, "6, 2, fill, default");
		
		JButton btnNouveauTheme = new JButton("Nouveau th\u00E8me");
		btnNouveauTheme.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				// Préciser que nous voulons créer un nouveau thème
				nouveauTheme = true;
				
				// Nettoyer les composants
				nettoyerFormulaire();
			}
		});
		pnlThemes.add(btnNouveauTheme, "8, 2");
		
		JLabel lblAjouterModifier = new JLabel("Formulaire d'\u00E9dition du th\u00E8me");
		lblAjouterModifier.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAjouterModifier.setHorizontalAlignment(SwingConstants.CENTER);
		pnlThemes.add(lblAjouterModifier, "2, 6, 9, 1");
		
		JLabel lblNomDuThme = new JLabel("Nom du th\u00E8me :");
		pnlThemes.add(lblNomDuThme, "2, 8");
		
		champNomTheme = new JTextField();
		pnlThemes.add(champNomTheme, "6, 8, 3, 1, fill, default");
		champNomTheme.setColumns(10);
		
		JLabel lblP1 = new JLabel("p1");
		pnlThemes.add(lblP1, "2, 10");
		
		JButton btnDefinirCouleurSurP1 = new JButton("D\u00E9finir une couleur...");
		btnDefinirCouleurSurP1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				champCouleurP1 = definirCouleur(champCouleurP1);
			}
		});
		
		champCouleurP1 = new JTextField();
		pnlThemes.add(champCouleurP1, "6, 10, fill, default");
		champCouleurP1.setColumns(10);
		pnlThemes.add(btnDefinirCouleurSurP1, "8, 10");
		
		JLabel lblP2 = new JLabel("p2");
		pnlThemes.add(lblP2, "2, 12");
		
		champCouleurP2 = new JTextField();
		pnlThemes.add(champCouleurP2, "6, 12, fill, default");
		champCouleurP2.setColumns(10);
		
		JButton btnDefinirCouleurSurP2 = new JButton("D\u00E9finir une couleur...");
		btnDefinirCouleurSurP2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				champCouleurP2 = definirCouleur(champCouleurP2);
			}
		});
		pnlThemes.add(btnDefinirCouleurSurP2, "8, 12");
		
		JLabel lblP3 = new JLabel("P3");
		pnlThemes.add(lblP3, "2, 14");
		
		champCouleurP3 = new JTextField();
		pnlThemes.add(champCouleurP3, "6, 14, fill, default");
		champCouleurP3.setColumns(10);
		
		JButton btnDefinirCouleurSurP3 = new JButton("D\u00E9finir une couleur...");
		btnDefinirCouleurSurP3.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				champCouleurP3 = definirCouleur(champCouleurP3);
			}
		});
		pnlThemes.add(btnDefinirCouleurSurP3, "8, 14");
		
		JLabel lblS1 = new JLabel("s1");
		pnlThemes.add(lblS1, "2, 16");
		
		champCouleurS1 = new JTextField();
		pnlThemes.add(champCouleurS1, "6, 16, fill, default");
		champCouleurS1.setColumns(10);
		
		JButton btnDefinirCouleurSurS1 = new JButton("D\u00E9finir une couleur...");
		btnDefinirCouleurSurS1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				champCouleurS1 = definirCouleur(champCouleurS1);
			}
		});
		pnlThemes.add(btnDefinirCouleurSurS1, "8, 16");
		
		JLabel lblS2 = new JLabel("s2");
		pnlThemes.add(lblS2, "2, 18");
		
		champCouleurS2 = new JTextField();
		pnlThemes.add(champCouleurS2, "6, 18, fill, default");
		champCouleurS2.setColumns(10);
		
		JButton btnDefinirCouleurSurS2 = new JButton("D\u00E9finir une couleur...");
		btnDefinirCouleurSurS2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				champCouleurS2 = definirCouleur(champCouleurS2);
			}
		});
		pnlThemes.add(btnDefinirCouleurSurS2, "8, 18");
		
		JLabel lblS3 = new JLabel("s3");
		pnlThemes.add(lblS3, "2, 20");
		
		champCouleurS3 = new JTextField();
		pnlThemes.add(champCouleurS3, "6, 20, fill, default");
		champCouleurS3.setColumns(10);
		
		JButton btnDefinirCouleurSurS3 = new JButton("D\u00E9finir une couleur...");
		btnDefinirCouleurSurS3.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				champCouleurS3 = definirCouleur(champCouleurS3);
			}
		});
		pnlThemes.add(btnDefinirCouleurSurS3, "8, 20");
		
		JLabel lblW = new JLabel("w");
		pnlThemes.add(lblW, "2, 22");
		
		champCouleurW = new JTextField();
		pnlThemes.add(champCouleurW, "6, 22, fill, default");
		champCouleurW.setColumns(10);
		
		JButton btnDefinirCouleurSurW = new JButton("D\u00E9finir une couleur...");
		btnDefinirCouleurSurW.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				champCouleurW = definirCouleur(champCouleurW);
			}
		});
		pnlThemes.add(btnDefinirCouleurSurW, "8, 22");
		
		JLabel lblB = new JLabel("b");
		pnlThemes.add(lblB, "2, 24");
		
		champCouleurB = new JTextField();
		pnlThemes.add(champCouleurB, "6, 24, fill, default");
		champCouleurB.setColumns(10);
		
		JButton btnDefinirCouleurSurB = new JButton("D\u00E9finir une couleur...");
		btnDefinirCouleurSurB.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				champCouleurB = definirCouleur(champCouleurB);
			}
		});
		pnlThemes.add(btnDefinirCouleurSurB, "8, 24");
		
		JLabel lblOpacit = new JLabel("Opacit\u00E9 du menu :");
		pnlThemes.add(lblOpacit, "2, 26");
		
		sliderOpaciteMenu = new JSlider();
		sliderOpaciteMenu.addMouseMotionListener(new MouseMotionAdapter() 
		{
			@Override
			public void mouseDragged(MouseEvent e) 
			{
				champOpaciteMenu.setText(Integer.toString(sliderOpaciteMenu.getValue()));
			}
		});
		sliderOpaciteMenu.setValue(100);
		sliderOpaciteMenu.setMaximum(200);
		pnlThemes.add(sliderOpaciteMenu, "6, 26");
		
		champOpaciteMenu = new JTextField();
		champOpaciteMenu.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyReleased(KeyEvent e) 
			{
				sliderOpaciteMenu.setValue(Integer.parseInt(champOpaciteMenu.getText()));
			}
		});
		pnlThemes.add(champOpaciteMenu, "8, 26, fill, default");
		champOpaciteMenu.setColumns(10);
		
		JLabel lblOpacitDuFrame = new JLabel("Opacit\u00E9 du frame :");
		pnlThemes.add(lblOpacitDuFrame, "2, 28");
		
		sliderOpaciteFrame = new JSlider();
		sliderOpaciteFrame.addMouseMotionListener(new MouseMotionAdapter() 
		{
			@Override
			public void mouseDragged(MouseEvent arg0) 
			{
				champOpaciteFrame.setText(Integer.toString(sliderOpaciteFrame.getValue()));
			}
		});
		sliderOpaciteFrame.setValue(100);
		sliderOpaciteFrame.setMaximum(200);
		pnlThemes.add(sliderOpaciteFrame, "6, 28");
		
		champOpaciteFrame = new JTextField();
		champOpaciteFrame.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyReleased(KeyEvent e) 
			{
				sliderOpaciteFrame.setValue(Integer.parseInt(champOpaciteFrame.getText()));
			}
		});
		pnlThemes.add(champOpaciteFrame, "8, 28, fill, default");
		champOpaciteFrame.setColumns(10);
		
		JPanel pnlTabClientServeur = new JPanel();
		tabbedPane.addTab("Client / Serveur", null, pnlTabClientServeur, null);
		pnlTabClientServeur.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblDfinirLipDu = new JLabel("IP du serveur :");
		pnlTabClientServeur.add(lblDfinirLipDu, "2, 2");
		
		textField = new JTextField();
		pnlTabClientServeur.add(textField, "6, 2, fill, default");
		textField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port du serveur :");
		pnlTabClientServeur.add(lblPort, "2, 4");
		
		// Définir paramètres de base
		champOpaciteMenu.setText(Integer.toString(sliderOpaciteMenu.getValue()));
		champOpaciteFrame.setText(Integer.toString(sliderOpaciteFrame.getValue()));
		
		btnValider = new JButton("Cr\u00E9er le th\u00E8me");
		btnValider.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				// Appliquer la création ou modification d'un thème
				if (appliquerTheme())
				{
					if (nouveauTheme)
					{
						JOptionPane.showMessageDialog(ComposantParametres.this, "Un nouveau thème pour votre Murder Party a été correctement créer !", "Message de confirmation", JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(ComposantParametres.this, "Le thème selectionner a été correctement modifié avec vos nouveaux paramètres !", "Message de confirmation", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
				// Afficher la liste des thèmes existants
				if (!listeTheme())
				{
					JOptionPane.showMessageDialog(ComposantParametres.this, "Un problème technique empêche de recuperer les thèmes existants.", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
				}
				
				// Nettoyer les composants du formulaire
				nettoyerFormulaire();
			}
		});
		
		JButton btnSupprimerTheme = new JButton("Supprimer le th\u00E8me");
		btnSupprimerTheme.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (supprimerTheme())
				{
					JOptionPane.showMessageDialog(ComposantParametres.this, "Le thème a été supprimé !", "Message de confirmation", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(ComposantParametres.this, "Impossible de supprimer le thème!\n Il se peux que il est encore utilisé par l'application.\nVous pouvez neanmoins supprimer le fichier (NOM.theme) a la main dans le repertoire thème de l'application.", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pnlThemes.add(btnSupprimerTheme, "6, 32");
		pnlThemes.add(btnValider, "8, 32");
		
		JLabel lblLesModificationsAppliqus = new JLabel("La modification des param\u00E8tres, implique un redemarrage de l'application (cot\u00E9 client et serveur)!");
		lblLesModificationsAppliqus.setHorizontalAlignment(SwingConstants.CENTER);
		panelParametres.setColumnHeaderView(lblLesModificationsAppliqus);
		
		// Afficher la liste des thèmes
		listeTheme();
		
		// Charger le thème
		chargerTheme();
		
		// Quelques paramètres initiales a spécifier
		if (champNomTheme.getText() != null)
		{
			btnValider.setText("Modifier le thème");
		}
	}
	
	
// ----- METHODES ----- //	

	/**
	 * Affiche la liste des thèmes du jeu
	 */
	private boolean listeTheme()
	{
		try 
		{
			choixTheme.removeAllItems();
			
			//xml = new FichierXML("config.xml");
			String themeActuel = xml.valeurNoeud("theme", "nom");
			
			String[] listeFichiers;
			listeFichiers = txt.listerRepertoire(fct.repertoireUtilisateur("themes"), ".theme");
			
			for (int i = 0; i < listeFichiers.length; i++) 
			{
				if (listeFichiers[i] != null)
				{
					choixTheme.addItem(listeFichiers[i].toString());
					
					if (themeActuel.equals(listeFichiers[i]))
					{
						choixTheme.setSelectedItem(themeActuel);
					}
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Fonction qui va créer ou modifier le thème
	 * @return Booleen pour savoir si le thème a été correctement créer ou pas
	 */
	private boolean appliquerTheme()
	{
		String donnees = null;
		
		donnees =	"nimrodlf.p1=" + champCouleurP1.getText() + "\n";
		donnees +=  "nimrodlf.p2=" + champCouleurP2.getText() + "\n";
		donnees +=  "nimrodlf.p3=" + champCouleurP3.getText() + "\n";
		
		donnees +=  "nimrodlf.s1=" + champCouleurS1.getText() + "\n";
		donnees +=  "nimrodlf.s2=" + champCouleurS2.getText() + "\n";
		donnees +=  "nimrodlf.s3=" + champCouleurS3.getText() + "\n";
		
		donnees +=  "nimrodlf.w=" + champCouleurW.getText() + "\n";
		donnees +=  "nimrodlf.b=" + champCouleurB.getText() + "\n";
		
		donnees +=  "nimrodlf.menuOpacity=" + champOpaciteMenu.getText() + "\n";
		donnees +=  "nimrodlf.frameOpacity=" + champOpaciteFrame.getText() + "\n";
		
		try 
		{
			// Si il existe deja un fichier, on le renomme dans ce cas, le client modifier le nom du thème
			if (txt.fichierExiste((fct.repertoireUtilisateur("themes", choixTheme.getSelectedItem() + ".theme"))))
			{
				txt.renommerFichier(fct.repertoireUtilisateur("themes", choixTheme.getSelectedItem() + ".theme"), 
						fct.repertoireUtilisateur("themes", champNomTheme.getText() + ".theme"));
			}
			
			// Enregistrer le fichier qui contient les paramètres du thème
			txt.ecrireFichier(fct.repertoireUtilisateur("themes", champNomTheme.getText() + ".theme"), donnees);
		} 
		catch (Exception e) 
		{
			e.getStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Charger un thème
	 */
	private boolean chargerTheme()
	{
		try 
		{
			// Charger les lignes du fichier
			String[] lignesFichier = txt.lireLigneFichier(fct.repertoireUtilisateur("themes", (String) choixTheme.getSelectedItem() + ".theme"));
			
			champCouleurP1.setText(lignesFichier[0].split("=")[1]);
			champCouleurP1.setBackground(null);
			champCouleurP1 = definirCouleur(champCouleurP1, lignesFichier[0].split("#")[1]);
			
			champCouleurP2.setText(lignesFichier[1].split("=")[1]);
			champCouleurP2.setBackground(null);
			champCouleurP2 = definirCouleur(champCouleurP2, lignesFichier[1].split("#")[1]);
			
			champCouleurP3.setText(lignesFichier[2].split("=")[1]);
			champCouleurP3.setBackground(null);
			champCouleurP3 = definirCouleur(champCouleurP3, lignesFichier[2].split("#")[1]);
			
			champCouleurS1.setText(lignesFichier[3].split("=")[1]);
			champCouleurS1.setBackground(null);
			champCouleurS1 = definirCouleur(champCouleurS1, lignesFichier[3].split("#")[1]);
			
			champCouleurS2.setText(lignesFichier[4].split("=")[1]);
			champCouleurS2.setBackground(null);
			champCouleurS2 = definirCouleur(champCouleurS2, lignesFichier[4].split("#")[1]);
			
			champCouleurS3.setText(lignesFichier[5].split("=")[1]);
			champCouleurS3.setBackground(null);
			champCouleurS3 = definirCouleur(champCouleurS3, lignesFichier[5].split("#")[1]);
			
			champCouleurW.setText(lignesFichier[6].split("=")[1]);
			champCouleurW.setBackground(null);
			champCouleurW = definirCouleur(champCouleurW, lignesFichier[6].split("#")[1]);
			
			champCouleurB.setText(lignesFichier[7].split("=")[1]);
			champCouleurB.setBackground(null);
			champCouleurB = definirCouleur(champCouleurB, lignesFichier[7].split("#")[1]);
			
			sliderOpaciteMenu.setValue(Integer.parseInt(lignesFichier[8].split("=")[1]));
			champOpaciteMenu.setText(lignesFichier[8].split("=")[1]);
			
			sliderOpaciteFrame.setValue(Integer.parseInt(lignesFichier[9].split("=")[1]));
			champOpaciteFrame.setText(lignesFichier[9].split("=")[1]);
		} 
		catch (Exception e) 
		{
			e.getStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Supprimer le thème choisis
	 * @return Boolean pour dire que le fichier a ete ou pas supprimer
	 */
	private boolean supprimerTheme()
	{
		try 
		{
			// Si c'est le thème systeme, par sécurité, on ne le supprime pas
			if (choixTheme.getSelectedItem().toString().contains("Murder Party"))
			{
				JOptionPane.showMessageDialog(this, "Vous ne pouvez pas supprimer le thème base!\nEn revanche vous pouvez le modifier.\nCeci dit, il est recommandé de créer un nouveau thème plutot que bidouiller celui par défaut :)", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			else
			{
				// Supprimer le thème sur le disque dur
				txt.supprimerFichier(fct.repertoireUtilisateur("themes", (String) choixTheme.getSelectedItem() + ".theme"));
				
				// Définir le thème par défaut du système
				choixTheme.setSelectedItem("Murder Party");
			}
		} 
		catch (Exception e) 
		{
			e.getStackTrace();
			return false;
		}

		return true;
	}
	
	/**
	 * Procédure qui nettoie les composants du formulaire
	 */
	private void nettoyerFormulaire()
	{
		champNomTheme.setText(null);
		champNomTheme.requestFocusInWindow();
		
		champCouleurP1.setText(null);
		champCouleurP1.setBackground(null);
		champCouleurP2.setText(null);
		champCouleurP2.setBackground(null);
		champCouleurP3.setText(null);
		champCouleurP3.setBackground(null);
		
		champCouleurS1.setText(null);
		champCouleurS1.setBackground(null);
		champCouleurS2.setText(null);
		champCouleurS2.setBackground(null);
		champCouleurS3.setText(null);
		champCouleurS3.setBackground(null);
		
		champCouleurW.setText(null);
		champCouleurW.setBackground(null);
		champCouleurB.setText(null);
		champCouleurB.setBackground(null);
		
		// Préciser que nous voulons créer un nouveau thème
		nouveauTheme = true;
		btnValider.setText("Créer le thème");
	}
	
	/**
	 * Fonction qui convertit un type Color au format RGB en valeur héxadecimal
	 * @param _couleur Couleur RGB
	 * @return Une couleux hexadecimal
	 */
	private String convertirCouleurRGBenHEX(Color _couleur) 
	{
		StringBuilder sb = new StringBuilder('#');

		if (_couleur.getRed() < 16) sb.append('0');
		sb.append(Integer.toHexString(_couleur.getRed()));

		if (_couleur.getGreen() < 16) sb.append('0');
		sb.append(Integer.toHexString(_couleur.getGreen()));

		if (_couleur.getBlue() < 16) sb.append('0');
		sb.append(Integer.toHexString(_couleur.getBlue()));

		return sb.toString();
	}
	
	/**
	 * Fonction qui définit et colorie un champ
	 * @param _champ Nom du champ JTextField
	 * @return Un champ colorié afin d'avoir un apercu de la couleur choisis
	 */
	private JTextField definirCouleur(JTextField _champ)
	{
		couleur = JColorChooser.showDialog(ComposantParametres.this, "Choisir une couleur", couleur);

		if (couleur == null)
		{
			couleur = Color.white;
		}
		
		_champ.setBackground(couleur);
		_champ.setText("#" + convertirCouleurRGBenHEX(couleur).toUpperCase());
		
		return _champ;
	}
	
	/**
	 * Fonction qui applique une couleur a un champ JTextField
	 * @param _champ Composant JTextField
	 * @param _couleur
	 * @return
	 */
	private JTextField definirCouleur(JTextField _champ, String _couleur)
	{

		int intValue = Integer.parseInt(_couleur, 16);
		Color couleur = new Color( intValue );

		_champ.setBackground(couleur);
		_champ.setText("#" + convertirCouleurRGBenHEX(couleur).toUpperCase());
		
		return _champ;
	}
	
	/**
	 * La méthode estVisible permet d'afficher ou non le conteneur de paramètres
	 * @param _visible : "boolean" affiche ou masque ce conteneur
	 */
	final public void estVisible(boolean _visible)
	{
		panelParametres.setVisible(_visible);
	}
}
