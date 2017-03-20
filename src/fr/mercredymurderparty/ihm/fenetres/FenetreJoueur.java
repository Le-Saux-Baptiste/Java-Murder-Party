package fr.mercredymurderparty.ihm.fenetres;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.client.CompteurTempsJoueur;
import fr.mercredymurderparty.ihm.composants.ComposantConnection;
import fr.mercredymurderparty.ihm.composants.ComposantHorloge;
import fr.mercredymurderparty.outil.Fonctions;
import fr.mercredymurderparty.outil.Theme;

@SuppressWarnings("serial")
public class FenetreJoueur extends JFrame
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- INTERFACE GRAPHIQUE ----- //
	private Dimension screenSize;
	
	//private ComposantConnection partieConnection;
	//private ComposantHorloge partieHorloge;
	
	private JButton boutonPleinEcran;
	private ImagePanel panelImageTitre;
	
	private GraphicsDevice device;
	private boolean estEnPleinEcran;
	
	private CompteurTempsJoueur tempsJoueur;
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Le constructeur de la fenêtre
	 * @param _device : "GraphicsDevice" un objet permettant la mise en plein écran de la fenêtre
	 */
	
	public FenetreJoueur(GraphicsDevice _device, CoeurClient coeurClient)
	{
		super(_device.getDefaultConfiguration());
		device = _device;
		setMinimumSize(new Dimension(800, 600));
		setTitle("Menu d'accueil");
		setSize(new Dimension(800, 600));
		screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-this.getWidth())/2, (screenSize.height-this.getHeight())/2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		/*partieConnection =*/ new ComposantConnection(this, springLayout, coeurClient);
		
		/*partieHorloge =*/ new ComposantHorloge(this, springLayout, coeurClient);
		
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
		springLayout.putConstraint(SpringLayout.SOUTH, boutonPleinEcran, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, boutonPleinEcran, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(boutonPleinEcran);
		
		/*
		 * TEST BANNIERE
		 * TEST BANNIERE
		 * TEST BANNIERE
		 */

		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout(0, 0));
		springLayout.putConstraint(SpringLayout.NORTH, pan, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, pan, 100, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pan, -100, SpringLayout.HORIZONTAL_CENTER, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pan, 100, SpringLayout.HORIZONTAL_CENTER, getContentPane());
		getContentPane().add(pan);

		JPanel imgPan = new ImagePanel("themes/logo.png", (JPanel) pan);
		pan.add(imgPan);
		
		/*
		 * Arrière-plan de l'IHM
		 * 
		 * @ImagePanel panelImageTitre
		 */
		Theme theme = new Theme();
		panelImageTitre = new ImagePanel("themes/" + theme.definirFond(), (JPanel) getContentPane());
		panelImageTitre.setLayout(new BorderLayout(0, 0));
		springLayout.putConstraint(SpringLayout.NORTH, panelImageTitre, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panelImageTitre, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panelImageTitre, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panelImageTitre, 0, SpringLayout.EAST, getContentPane());
		getContentPane().add(panelImageTitre);
		

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
