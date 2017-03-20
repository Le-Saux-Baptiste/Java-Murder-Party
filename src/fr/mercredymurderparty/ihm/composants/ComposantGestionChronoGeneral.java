package fr.mercredymurderparty.ihm.composants;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

import fr.mercredymurderparty.ihm.fenetres.FenetreAdmin;
import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.serveur.CoeurServeur;

import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ComposantGestionChronoGeneral extends JPanel
{
// ----- ATTRIBUTS ----- //
	
	// ----- VARIABLES DE CLASSES ----- //
	static final int minimum = 0; // la valeur minimale que peut prendre le chrono g�n�ral en seconde
	static final int maximum = 25200; // la valeur max que peut prendre le chrono g�n�ral en seconde (=> 7heure)
	static final int increment = 1; // le pas d'incr�mentation du chrono en seconde
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private JPanel panelGestionChronoGeneral;
	private	SpringLayout slPanelGestionChronoGeneral;
	private JSpinner spinner;
	
	public ComposantGestionChronoGeneral(FenetreAdmin _fenetre, final SpringLayout _springLayout, CoeurServeur _coeurServeur)
	{
		panelGestionChronoGeneral = new JPanel();
		_springLayout.putConstraint(SpringLayout.NORTH, panelGestionChronoGeneral, -200, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.SOUTH, panelGestionChronoGeneral, 200, SpringLayout.VERTICAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.WEST, panelGestionChronoGeneral, -250, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.EAST, panelGestionChronoGeneral, 250, SpringLayout.HORIZONTAL_CENTER, _fenetre.getContentPane());
		_fenetre.getContentPane().add(panelGestionChronoGeneral);
		slPanelGestionChronoGeneral = new SpringLayout();
		panelGestionChronoGeneral.setLayout(slPanelGestionChronoGeneral);			
	}

	
// ----- METHODES ----- //

	/**
	 * m�thode qui cr� l'interface du JSpinner avec les labels et boutons
	 */
	final public void constructionIHM(final CoeurServeur _coeurServeur)
	{
		JLabel lbinformation= new JLabel("module gestion du chronom�tre g�n�ral : ");
		slPanelGestionChronoGeneral.putConstraint(SpringLayout.NORTH, lbinformation, 0, SpringLayout.NORTH, panelGestionChronoGeneral);
		slPanelGestionChronoGeneral.putConstraint(SpringLayout.WEST, lbinformation, 0, SpringLayout.WEST, panelGestionChronoGeneral);
		panelGestionChronoGeneral.add(lbinformation);
		
		JLabel lbexplications = new JLabel("Veuiller saisir la nouvelle valeur du chronom�tre en seconde (" + minimum + "<= valeur <=" + maximum + ")");
		slPanelGestionChronoGeneral.putConstraint(SpringLayout.NORTH, lbexplications, 40, SpringLayout.NORTH, panelGestionChronoGeneral);
		slPanelGestionChronoGeneral.putConstraint(SpringLayout.WEST, lbexplications, 0, SpringLayout.WEST, panelGestionChronoGeneral);
		panelGestionChronoGeneral.add(lbexplications);
		
		// recup�re la valeur actuelle du chrono et la place dans le JSpinner
		int valeurInit = _coeurServeur.getTempsGlobalRestant().getTemps();
		System.out.println("avant : " + _coeurServeur.getTempsGlobalRestant().getTemps());
		spinner = new JSpinner(new SpinnerNumberModel(valeurInit, minimum, maximum, increment));
		slPanelGestionChronoGeneral.putConstraint(SpringLayout.NORTH, spinner, 80, SpringLayout.NORTH, panelGestionChronoGeneral);
		slPanelGestionChronoGeneral.putConstraint(SpringLayout.WEST, spinner, 0, SpringLayout.WEST, panelGestionChronoGeneral);
		panelGestionChronoGeneral.add(spinner);
		
		JButton btnValider = new JButton("Valider");
		slPanelGestionChronoGeneral.putConstraint(SpringLayout.NORTH, btnValider, 120, SpringLayout.NORTH, panelGestionChronoGeneral);
		slPanelGestionChronoGeneral.putConstraint(SpringLayout.WEST, btnValider, 0, SpringLayout.WEST, panelGestionChronoGeneral);
		btnValider.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				BaseDeDonnees bdd = new BaseDeDonnees();
				
				try
				{
					// mise � jour de la base de donn�es
					bdd.executerInstruction("UPDATE info SET temps = " + "'" + spinner.getValue() + "'" + "WHERE id = 1");
					
					// mise � jour de la variable temps de la classe CompteurTempsGeneral (necessite une consersion object -> string -> int )
					_coeurServeur.getTempsGlobalRestant().setTemps(Integer.parseInt(spinner.getValue().toString()));
					
					System.out.println("apres : " + _coeurServeur.getTempsGlobalRestant().getTemps());
				}
				catch(SQLException except)
				{
					except.printStackTrace();
				}
				catch(NullPointerException exceptnull)
				{
					// simplement si la JSpinner n'est pas cr��e
				}
				// fermeture de la connexion � la base de donn�es
				bdd.fermerConnexion();
				
				estVisible(false);
				
				JOptionPane.showMessageDialog(null, "le chronom�tre g�n�ral a bien �t� modifi�", "confirmation de la modification", JOptionPane.INFORMATION_MESSAGE);
				
				// obliger de d�truire le spinner puis de la recharger � chaque fois sinon, probl�me de mise � jour du composant, il fait n'importe quoi
				panelGestionChronoGeneral.remove(spinner);
			}
		});
		panelGestionChronoGeneral.add(btnValider);
	}
	
	
	/**
	 * g�re la visibilit� du panel panelGestionChronoGeneral
	 * @param valeur : true : rend visible le panel
	 * 			<br/>  false : masque le panel
	 */
	final public void estVisible(boolean _valeur)
	{
		panelGestionChronoGeneral.setVisible(_valeur);
	}
}