package fr.mercredymurderparty.ihm.composants;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.ihm.fenetres.FenetreJoueur;
import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.serveur.CoeurServeur;

@SuppressWarnings("serial")
public class ComposantDonTemps extends JPanel
{
// ----- ATTRIBUTS ----- //
	private JPanel panelDonTemps;
	private SpringLayout layoutDonTemps;
	private JTable table;
	private DefaultTableModel modeleTableJoueur;
	
	private CoeurClient joueur;
	private CoeurServeur maitreJeu;

// ----- CONSTRUCTEUR ----- //
	/**
	 * 
	 */
	public ComposantDonTemps(FenetreJoueur _fenetre, final SpringLayout _springLayout) 
	{
		panelDonTemps = new JPanel();
		_springLayout.putConstraint(SpringLayout.NORTH, panelDonTemps, -200, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.SOUTH, panelDonTemps, 200, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.WEST, panelDonTemps, -250, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.EAST, panelDonTemps, 250, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane());
		_fenetre.getContentPane().add(panelDonTemps);
		this.setLayout(_springLayout);
		layoutDonTemps = new SpringLayout();
		panelDonTemps.setLayout(layoutDonTemps);
		SpringLayout springLayout = new SpringLayout();
	}
	
// ----- METHODES ----- //
	/**
	 * rend visible le composant ou pas
	 * @param : true : rend visible le composant
	 * 	   <br/>false : masque le composant
	 */
	final public void estVisible(boolean _valeur)
	{
		this.setVisible(_valeur);
	}
	
	
	/**
	 * m�thode qui cr�e et remplie la Jtable avec le login, nom, prenom de tous les joueurs
	 */
	final public void Cr�erTablePreremplie()
	{
		BaseDeDonnees bdd = new BaseDeDonnees();
		
		try
		{
			// cr�ation de la JTable
			JScrollPane scrollPane = new JScrollPane();
			panelDonTemps.add(scrollPane);
			
			modeleTableJoueur = new DefaultTableModel();

			table = new JTable(modeleTableJoueur);
			scrollPane.add(table);
			
			// affiche les noms des colonnes
			String[] nomsColonnes = {"identifiant", "nom", "prenom"};
			modeleTableJoueur.setColumnIdentifiers(nomsColonnes);
			
			// r�cup�ration des �l�ments de la base de donn�es
			ResultSet retourBaseDonnees;
			retourBaseDonnees = bdd.executerRequete(" SELECT login, nom, prenom FROM personnage ");
			
			ResultSetMetaData retourBaseDonneesMetaData = retourBaseDonnees.getMetaData();
			int nombreColonne = retourBaseDonneesMetaData.getColumnCount(); // R�cuperer le nombre de colonnes
			
			// place les �l�ments r�cup�r�s dans la JTable
			while (retourBaseDonnees.next()) 
			{
				Object[] objet = new Object[nombreColonne];

				for (int i = 0; i < nombreColonne; i++) 
				{
					objet[i] = retourBaseDonnees.getObject(i + 1);
				}
				
				// Ajouter une ligne dans le JTable avec les donn�es du l'array objet
				modeleTableJoueur.addRow(objet);
			}
			// passe le mod�le dans la JTable
			table.setModel(modeleTableJoueur);
			
			// D�finir la taille des colonnes
		    TableColumn tableColonne = table.getColumnModel().getColumn(0);        
		    tableColonne.setPreferredWidth(50);
		    
		    TableColumn tableColonne2 = table.getColumnModel().getColumn(1);        
		    tableColonne2.setPreferredWidth(50);
		    
		    TableColumn tableColonne3 = table.getColumnModel().getColumn(2);        
		    tableColonne3.setPreferredWidth(50);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}