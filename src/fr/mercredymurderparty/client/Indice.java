package fr.mercredymurderparty.client;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import fr.mercredymurderparty.outil.BaseDeDonnees;

public class Indice 
{
	// ----- ATTRIBUT ----- //
	CoeurClient coeurClient; // le modèle
	
	/**
	 * Constructeur de la classe
	 */
	public Indice(CoeurClient _coeurClient)
	{
		coeurClient = _coeurClient;
	}
	
	/**
	 * Cette fonction remplit un JTable des indices du joueur connecté
	 * @param _loginJoueur Identifiant du joueur
	 * @param _table Composant JTable ou seront parser les données
	 * @return Un JTable de données
	 */
	public final JTable afficherListeIndices(String _loginJoueur, JTable _table)
	{
		// Variables locales
		ResultSet rs;
		JTable tableLocal = _table;
		
		// Classes locales
		BaseDeDonnees bdd = new BaseDeDonnees();
		Personnage perso = new Personnage(coeurClient);
		
		try 
		{
			// Définition des colonnes de la table
			String[] tableColumnsName = { "id", "titre", "contenu" };
			
			DefaultTableModel tableModele = (DefaultTableModel) tableLocal.getModel();
			tableModele.setColumnIdentifiers(tableColumnsName);
			
			rs = bdd.executerRequete("" +
					"SELECT ind.id, ind.titre, ind.contenu " +
					"FROM indice AS ind, indice_relation idr " +
					"WHERE ind.id = idr.ref_indice " +
					"AND idr.ref_perso = " + perso.recupererIdentifiant(_loginJoueur));
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
			
			return tableLocal;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
