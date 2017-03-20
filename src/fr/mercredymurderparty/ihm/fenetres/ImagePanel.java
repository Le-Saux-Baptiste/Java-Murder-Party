package fr.mercredymurderparty.ihm.fenetres;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel
{

// ----- ATTRIBUTS ----- //
	
	private BufferedImage image;
	private JPanel panel;
	private int longueur, hauteur;
	
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * ImagePanel est une classe pour afficher une image dans l'IHM
	 * @param _cheminImage : une chaîne de caractères qui est le chemin de l'image
	 * @param _panel : le JPanel dont on veut récupérer les dimensions et les mettre à jour
	 */
	
	public ImagePanel(String _cheminImage, JPanel _panel)
	{
		panel = _panel;
		longueur = panel.getWidth();
		hauteur = panel.getHeight();
		
		try
		{
			image = ImageIO.read(new File(_cheminImage));
		}
		catch (IOException ex)
		{
			System.err.println("Erreur lors du chargement de l'image!");
		}
	}
	
	
// ----- METHODES ----- //
	
	/**
	 * La méthode paintComponent est appelée dés qu'il y a besoin de rafraichir la fenêtre
	 * @param _g : un objet Graphics permettant de dessiner des objets
	 */
	
	final public void paintComponent(Graphics _g)
	{
		super.paintComponent(_g);
		longueur = panel.getWidth();
		hauteur = panel.getHeight();
		_g.drawImage(image, 0,0,longueur,hauteur,null);
	}
}
