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
import java.util.Observable;
import java.util.Observer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JSplitPane;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class ComposantAlerte implements Observer
{
	private int tempsMaj; //le temps entre chaque màj de temps du joueur
	private int tempsServeur; //la variable local du temps
	JOptionPane jopAlerte;
	// les différentiel
		int tempsMoins;
		int tempsPlus;
	// ----- BASE DE DONNEES ----- //
	private BaseDeDonnees bdd;
	
	// ----- MODELE DU SERVEUR ----- //
	CoeurServeur coeurServeur;
	
	// ----- CONSTRUCTEUR ----- //
	//TODO instancier l'objet =DDDD
	public ComposantAlerte(CoeurServeur _coeurServeur)
	{
		coeurServeur = _coeurServeur;
		//coeurServeur.getTempsGlobalRestant().addObserver(this);
	}
	
	// ----- METHODES ----- //
	
	public void recupererContenuAlerteActive(int _temps)
	{
		
		
		// Ouvrir une nouvelle connexion à la bdd
		bdd = new BaseDeDonnees(coeurServeur);
			
		try 
		{
			tempsMoins = (_temps - 10);
			tempsPlus = _temps + 10;
			//System.out.println("trololo entrée");
			// Parser les données de la requête dans un ResultSet
			ResultSet rs = bdd.executerRequete("" +
						" SELECT id, titre, contenu, ref, temps " +
						" FROM alerte WHERE temps > " + tempsMoins + " AND temps < " + tempsPlus + " "
				);
				
			// Afficher les données du ResultSet
			while (rs.next()) 
			{
				//Boîte du message d'information
				//-------------------------------
				
				jopAlerte = new JOptionPane();
				jopAlerte.showMessageDialog(null, (rs.getString("contenu")) + " . APRES : " + (rs.getString("temps")) + " SECONDES DE JEU.", " Alerte : " + (rs.getString("titre")), JOptionPane.INFORMATION_MESSAGE);
				
				/*champTitre.setText(rs.getString("titre"));
				champContenu.setText(rs.getString("contenu"));
				champRef.setText(rs.getString("ref"));
				champTemps.setText(rs.getString("temps"));*/
			}
				
				// Fermer le ResultSet
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
			
			// Fermer la connexion à la bdd
			bdd.fermerConnexion();	
			//System.out.println("trololo sortie");
		}


	@Override
	public void update(Observable arg0, Object arg1)
	{
		//System.out.println("ciseau");
		tempsMaj = 20;
		tempsServeur = coeurServeur.getTempsGlobalRestant().getTempsEcoule();
		if(tempsServeur % tempsMaj == 1)
		{
			recupererContenuAlerteActive(tempsServeur);
		}
	}
		
		
	
	
	
}
