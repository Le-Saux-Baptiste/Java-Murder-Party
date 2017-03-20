package fr.mercredymurderparty.ihm.composants;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import fr.mercredymurderparty.ihm.fenetres.FenetreAdmin;
import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.serveur.CoeurServeur;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JSplitPane;

public class ComposantGestionAlerte extends JPanel {
	private JTextField champTitre;
	private JTextField champContenu;
	private JTextField champRef;
	//private JTextField champPrenom;
	private JTextField champTemps;
	private JTable tableAlerte;
	private DefaultTableModel modeleTableIndice;
	
	private JButton btnValider;
	
	// ----- BASE DE DONNEES ----- //
	private BaseDeDonnees bdd;
	
	// ----- MODELE DU SERVEUR ----- //
	CoeurServeur coeurServeur;
	
	// ----- variables supplementaires ----- //
	private int actionAEffectuer; // variable qui va nous permettre de savoir quelle action effectuer si on clique sur le bouton valider :
								  // si vaut 1 : creation d'un joueur
								  // si vaut 2 : modification d'un joueur existant
								  // si vaut 3 : suppression d'un joueur
								  // si vaut 4 : donner / prendre du temps à un joueur

	private int idAlerte = -1;

	/**
	 * Create the panel.
	 */
	public ComposantGestionAlerte(FenetreAdmin _fenetre, final SpringLayout _springLayout, CoeurServeur _coeurServeur) 
	{
		coeurServeur = _coeurServeur;
		setLayout(new MigLayout("", "[281.00][5.00][grow]", "[281.00,grow]"));
		
		JPanel pnlFormulaire = new JPanel();
		
		_springLayout.putConstraint(SpringLayout.NORTH, this, -200, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.SOUTH, this, 300, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.WEST, this, -350, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.EAST, this, 400, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane());
		_fenetre.getContentPane().add(this);		
	
		add(pnlFormulaire, "cell 0 0,grow");
		
		//_fenetre.getContentPane().add(pnlFormulaire, "cell 0 0,grow");
		pnlFormulaire.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(66dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(85dlu;min):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(5dlu;default)"),},
			new RowSpec[] {
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
		
		JButton btnNouveau = new JButton("Nouvelle alerte");
		btnNouveau.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				initChamp();
			}
		});
		
		JLabel lblAjouterModifier = new JLabel("Formulaire pour Ajouter / Modifier l'alerte :");
		pnlFormulaire.add(lblAjouterModifier, "2, 2, 5, 1");
		pnlFormulaire.add(btnNouveau, "4, 4");
		
		JLabel lblTitre = new JLabel("Titre :");
		pnlFormulaire.add(lblTitre, "2, 6, left, default");
		
		champTitre = new JTextField();
		pnlFormulaire.add(champTitre, "4, 6, fill, default");
		champTitre.setColumns(10);
		
		JLabel lblContenu = new JLabel("Contenu :");
		pnlFormulaire.add(lblContenu, "2, 8, left, default");
		
		champContenu = new JTextField();
		pnlFormulaire.add(champContenu, "4, 8, fill, default");
		champContenu.setColumns(10);
		
		JLabel lblRef = new JLabel("Ref :");
		pnlFormulaire.add(lblRef, "2, 10, left, default");
		
		champRef = new JTextField();
		pnlFormulaire.add(champRef, "4, 10, fill, default");
		champRef.setColumns(10);
		
		/*JLabel lblPrnom = new JLabel("Pr\u00E9nom :");
		pnlFormulaire.add(lblPrnom, "2, 12, left, default");
		
		champPrenom = new JTextField();
		pnlFormulaire.add(champPrenom, "4, 12, fill, default");
		champPrenom.setColumns(10);*/
		
		JLabel lblDureDuChrono = new JLabel("Dur\u00E9e du chrono en sec :");
		pnlFormulaire.add(lblDureDuChrono, "2, 14, left, default");
		
		champTemps = new JTextField();
		pnlFormulaire.add(champTemps, "4, 14, fill, default");
		champTemps.setColumns(10);
		
		/*Inutile dans le cadre des alertes
		 * JSplitPane splitPane = new JSplitPane();
		pnlFormulaire.add(splitPane, "4, 16, fill, fill");
		
		JButton btnAjouterTemps = new JButton("Ajouter Temps");
		btnAjouterTemps.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int tempsChrono;
				
				tempsChrono = Integer.parseInt(champTemps.getText());
				
				tempsChrono += 10;//Rajoute 10secondes quand on appuye sur le bouton
				
				champTemps.setText(String.valueOf(tempsChrono));
			}
		});
		splitPane.setLeftComponent(btnAjouterTemps);
		
		JButton btnRetirerTemps = new JButton("Retirer Temps");
		btnRetirerTemps.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int tempsChrono;
				
				tempsChrono = Integer.parseInt(champTemps.getText());
				
				tempsChrono -= 10;//Enlève 10secondes quand on appuye sur le bouton
				
				champTemps.setText(String.valueOf(tempsChrono));
			}
		});
		splitPane.setRightComponent(btnRetirerTemps);
		*/
		btnValider = new JButton("Cr\u00E9er l'alerte");
		btnValider.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				// Vérifier si les champs sont biens remplis
				//if ((choixPersonnage.getSelectedItem() != null || tempsTousJoueurs == true) && champTitre.getText() != null && zoneContenu.getText() != null)
				//{
					if (idAlerte == -1)
					{
						if (ajouterAlerte())
						{
							JOptionPane.showMessageDialog(ComposantGestionAlerte.this, "L'alerte a été créer avec succès !", "Message de confirmation", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							JOptionPane.showMessageDialog(ComposantGestionAlerte.this, "Un problème empèche de créer l'alerte :(", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
						}
					}
					else
					{
						if (modifierAlerte())
						{
							JOptionPane.showMessageDialog(ComposantGestionAlerte.this, "L'alerte a été modifé avec succès !", "Message de confirmation", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							JOptionPane.showMessageDialog(ComposantGestionAlerte.this, "Un problème empèche de modifier l'alerte :(", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
						}
					}
				//}
				//else
				//{
					//JOptionPane.showMessageDialog(ComposantGestionJoueurs.this, "Un ou plusieurs champs n'ont pas étés remplis ou ne correspondent pas aux critères requis.\nChamps obligatoires: titre, contenu, personnage (a défaut coché sur la case 'indice pour tous les joueurs').", "Formulaire non valide", JOptionPane.ERROR_MESSAGE);
				//}
			}
		});
		pnlFormulaire.add(btnValider, "4, 18");
		
		JPanel pnlTableau = new JPanel();
		add(pnlTableau, "cell 2 0,grow");
		pnlTableau.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(135dlu;min)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(170dlu;min)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblHistoriqueDesAlertes = new JLabel("Historique des alertes :");
		pnlTableau.add(lblHistoriqueDesAlertes, "2, 2");
		
		JScrollPane scrollPane = new JScrollPane();
		pnlTableau.add(scrollPane, "2, 4, fill, fill");
		
		modeleTableIndice = new DefaultTableModel();
		tableAlerte = new JTable(modeleTableIndice);
		tableAlerte.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				idAlerte = (Integer) contenuPremiereCelluleTableau(tableAlerte, 0);
				recupererContenuAlerte(idAlerte);
			}
		});
		scrollPane.setViewportView(tableAlerte);
		
		JButton btnSupprimerAlerte = new JButton("Supprimer l'alerte");
		btnSupprimerAlerte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				idAlerte = (Integer) contenuPremiereCelluleTableau(tableAlerte, 0);
				if (supprimerAlerte(idAlerte))
				{
					JOptionPane.showMessageDialog(ComposantGestionAlerte.this, "L'alerte a été supprimer avec succès !", "Message de confirmation", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(ComposantGestionAlerte.this, "Un problème empèche la suppression de l'alerte :(", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pnlTableau.add(btnSupprimerAlerte, "2, 6");
		
	//	listeJoueurs(tableJoueurs, null);

	}
	
	public boolean supprimerAlerte(int idAlerte) 
	{
		try
		{
			// connexion base de donnees
			bdd = new BaseDeDonnees(coeurServeur);
			
			bdd.executerInstruction("DELETE FROM alerte WHERE rowid = " + idAlerte + "");
			
			// deconnexion base de donnees
			bdd.fermerConnexion();
			
			// Rafraichir la liste des joueurs
			rafraichirListeAlerte();
			
			// R.A.Z le formulaire et le mode d'edition
			initChamp();
		}
		catch (SQLException esq)
		{
			esq.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean ajouterAlerte()
	{
		int champChronoInt; // variable qui contiendra la valeur du JtextField mais en int
		
		try
		{	
			// recuperation du JTextField
			champChronoInt = Integer.parseInt(champTemps.getText());
			
			// connexion a la base de donnes
			bdd = new BaseDeDonnees(coeurServeur);
			
			// insertion dans la base de données des valeurs des champs
			bdd.executerInstruction("INSERT INTO alerte (titre, contenu, ref, temps) VALUES ( " +
									"\"" + champTitre.getText() +"\", " +
									"\"" + champContenu.getText()  +"\", " +
									"\"" + champRef.getText()         +"\", " +
									"\"" + champChronoInt       +"\")");
			
			// deconnexion base de donnes
			bdd.fermerConnexion(); 
			
			// Rafraichir la liste des joueurs
			rafraichirListeAlerte();
			
			// R.A.Z le formulaire et le mode d'edition
			initChamp();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return false; 
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public final JTable listeAlerte(JTable _table, String _login)
	{
		// Variables locales
		ResultSet rs;
		JTable tableLocal = _table;
		
		bdd = new BaseDeDonnees(coeurServeur);
		
		try 
		{
			// Définition des colonnes de la table
			String[] tableColumnsName = { "N°", "Alerte"};
			
			DefaultTableModel tableModele = (DefaultTableModel) tableLocal.getModel();
			tableModele.setColumnIdentifiers(tableColumnsName);
			
			// Parser les données de la requête dans un ResultSet
			rs = bdd.executerRequete("" +
					" SELECT rowid, titre FROM alerte "
			);

			ResultSetMetaData rsMetaData = rs.getMetaData();
			
			// Récuperer le nombre de colonnes
			int numeroColonne = rsMetaData.getColumnCount();
			
			// Boucle à travers le ResultSet et le transfert dans le modèle
			while (rs.next()) 
			{
				Object[] objet = new Object[numeroColonne];

				for (int i = 0; i < numeroColonne; i++) 
				{
					objet[i] = rs.getObject(i + 1);
				}
				
				// Ajouter une ligne dans le JTable avec les données du l'array objet
				tableModele.addRow(objet);
			}
			
			// Définir le modèle du tableau
			tableLocal.setModel(tableModele);
			
			// Définir la taille des colonnes
		    TableColumn tableColonne = tableAlerte.getColumnModel().getColumn(0);        
		    tableColonne.setPreferredWidth(50);
		    
		    TableColumn tableColonne2 = tableAlerte.getColumnModel().getColumn(1);        
		    tableColonne2.setPreferredWidth(100);
			
			return tableLocal;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void recupererContenuAlerte(int _idAlerte)
	{
		
		// Verrouille certains composants qui risquent de rentrer en conflits
		//verrouillerComposant = true;
		
		if (_idAlerte != -1)
		{
			// Ouvrir une nouvelle connexion à la bdd
			bdd = new BaseDeDonnees(coeurServeur);
			
			try 
			{
				// Parser les données de la requête dans un ResultSet
				ResultSet rs = bdd.executerRequete("" +
						" SELECT titre, contenu, ref, temps " +
						" FROM alerte WHERE rowid  = " + _idAlerte + " "
				);
				
				// Afficher les données du ResultSet
				while (rs.next()) 
				{
					champTitre.setText(rs.getString("titre"));
					champContenu.setText(rs.getString("contenu"));
					champRef.setText(rs.getString("ref"));
					//champPrenom.setText(rs.getString("prenom"));
					champTemps.setText(rs.getString("temps"));
				}
				
				// Femer le ResultSet
				rs.close();
			} 
			catch (SQLException sqe) 
			{
				System.out.println("Erreur base de données: " + sqe.getMessage());
			}
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				btnValider.setText("Modifier l'alerte");
			}
			
			// Fermer la connexion à la bdd
			bdd.fermerConnexion();	
		}
		
		// Dé-Verrouille certains composants qui risquent de rentrer en conflits
		//verrouillerComposant = false;
	}	
	
	
	/**
	 * Procédure qui modifie un indice existant dans la bdd
	 */
	public boolean modifierAlerte()
	{
		int champChronoInt; // variable qui contiendra la valeur du JtextField mais en int
		
		champTemps.setEditable(false);
		
		try 
		{
			// Ouvrir une nouvelle connexion à la bdd
			bdd = new BaseDeDonnees(coeurServeur); 
			
			// Recuperation du JTextField avec le temps
			champChronoInt = Integer.parseInt(champTemps.getText());
			
			// Mettre a jours le joueur
			bdd.executerInstruction("UPDATE alerte SET " +
					" titre = "   + "'" + champTitre.getText() + "'," +
					" contenu = "     + "'" + champContenu.getText()  + "'," +
					" ref = "     + "'" + champRef.getText()         + "'," +
					" temps = "   + "" + champChronoInt + " " +
					" WHERE rowid = " + "" + idAlerte  + "");
			
			// Fermer la connexion à la base de données
			bdd.fermerConnexion();
			
			// Actualiser la liste des joueurs
			rafraichirListeAlerte();
			
			// R.A.Z des composants du formulaire
			initChamp();
		} 
		catch (SQLException sqe) 
		{
			System.out.println("Erreur base de données: " + sqe.getMessage());
			return false;
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return false;
		}
		
		// Si on arrive ici, c'est qu'on a pas eu de problèmes
		return true;
	}
	
	public int getActionAEffectuer() 
	{
		return actionAEffectuer;
	}

	public void setActionAEffectuer(int actionAEffectuer) 
	{
		this.actionAEffectuer = actionAEffectuer;
	}

	/**
	 * La méthode estVisible permet d'afficher ou non le conteneur de paramètres
	 * @param _visible : "boolean" affiche ou masque ce conteneur
	 */
	final public void estVisible(boolean _visible)
	{
		this.setVisible(_visible);
	}
	
	/**
	 * Procédure qui rafraichit les indices dans le tableau
	 * A utiliser par exemple si vous avez ajouter un nouvelle indice à la bdd
	 */
	public void rafraichirListeAlerte()
	{
		int nombreLignes = tableAlerte.getRowCount();
		
		for(int i = nombreLignes - 1; i >= 0; i--)
		{
			modeleTableIndice.removeRow(i);
		}
		
		tableAlerte = listeAlerte(tableAlerte, null);
	}
	
	/**
	 * Fonction qui prend le contenu de la 1ère colonne à la 1ère ligne du tableau
	 * @param _table Fourir le tableau de type JTable
	 * @param _indexColonne A quelle colonne le contenu doit etre piocher (par défaut mettre 0 !!!)
	 * @return Un objet generique à caster comme vous voulez (string, int, ...)
	 */
	public Object contenuPremiereCelluleTableau(JTable _table, int _indexColonne)
	{
		// Variables locale
		Object cellule = null;
		int ligne = _table.getSelectedRow();
		int colonne = _indexColonne;
		
		// Vérifier si on a une ligne pointé
		if (ligne != -1)
		{
			cellule = _table.getValueAt(ligne, colonne);
		}
		return cellule;
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
		btnValider.setText("Créer l'alerte");
		
		champTemps.setEditable(true);
		
		champTitre.setText("");
		champContenu.setText("");
		champRef.setText("");
		//champPrenom.setText("");
		champTemps.setText("");
		
		champTitre.requestFocusInWindow();
		tableAlerte.getSelectionModel().clearSelection();
		
		idAlerte = -1;
	}

	// ----- Getters & Setters ----- //
	
	public JTable getTableAlerte()
	{
		return tableAlerte;
	}

	public void setTableJoueurs(JTable tableAlerte)
	{
		this.tableAlerte = tableAlerte;
	}
	
	
}
