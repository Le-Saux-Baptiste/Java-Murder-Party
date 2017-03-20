package fr.mercredymurderparty.ihm.composants;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import javax.swing.SpringLayout;
import javax.swing.JTable;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.client.Indice;
import fr.mercredymurderparty.ihm.fenetres.FenetreJoueur;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ComposantIndices extends JPanel 
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private JPanel panelIndice;
	private DefaultTableModel modeleTableIndice;
	private JTable tableIndices;
	private Indice indice;
	
	private String login;
	
	// ----- MODELE ----- //
	private CoeurClient coeurClient;
	
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Create the frame.
	 */
	public ComposantIndices(FenetreJoueur _fenetre, SpringLayout _springLayout, CoeurClient _coeurClient)
	{
		coeurClient = _coeurClient;
		panelIndice = new JPanel();
		SpringLayout sl_contentPane = new SpringLayout();
		panelIndice.setLayout(sl_contentPane);
		
		_springLayout.putConstraint(SpringLayout.NORTH, panelIndice, 10, SpringLayout.NORTH, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.SOUTH, panelIndice, -5, SpringLayout.SOUTH, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.WEST, panelIndice, -500, SpringLayout.EAST, _fenetre.getContentPane());
		_springLayout.putConstraint(SpringLayout.EAST, panelIndice, -150, SpringLayout.EAST, _fenetre.getContentPane());
		_fenetre.getContentPane().add(panelIndice);
		
		JScrollPane spIndices = new JScrollPane();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spIndices, 0, SpringLayout.NORTH, panelIndice);
		sl_contentPane.putConstraint(SpringLayout.WEST, spIndices, 0, SpringLayout.WEST, panelIndice);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, spIndices, 0, SpringLayout.SOUTH, panelIndice);
		sl_contentPane.putConstraint(SpringLayout.EAST, spIndices, 0, SpringLayout.EAST, panelIndice);
		panelIndice.add(spIndices);
		
		modeleTableIndice = new DefaultTableModel();
		tableIndices = new JTable(modeleTableIndice);
		spIndices.setViewportView(tableIndices);
		
		indice = new Indice(coeurClient);
	}
	
// ----- METHODES ----- //
	
	/**
	 * La méthode estVisible permet d'afficher ou non le conteneur des indices
	 * @param _visible : "boolean" affiche ou masque ce conteneur
	 */
	final public void estVisible(boolean _visible)
	{
		panelIndice.setVisible(_visible);
	}
	
	final public void afficherIndices() 
	{
		int nombreLignes = tableIndices.getRowCount();
		for(int i = nombreLignes - 1; i >= 0; i--)
		{
			modeleTableIndice.removeRow(i);
		}
		System.out.println(login);
		tableIndices = indice.afficherListeIndices(login, tableIndices);
		tableIndices.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
}
