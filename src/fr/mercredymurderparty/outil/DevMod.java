package fr.mercredymurderparty.outil;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class DevMod extends JFrame {
	private Fonctions fct;

	private JPanel contentPane;
	private JLabel lblLog;
	private JButton btnDownBdd;
	private JButton btnUpBdd;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DevMod frame = new DevMod();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DevMod() 
	{
		setAlwaysOnTop(true);
		setTitle("Outils interne");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 112);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		btnDownBdd = new JButton(
				"T\u00E9l\u00E9charger la base de donn\u00E9es");
		btnDownBdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				telechargerBDD();
			}
		});
		contentPane.add(btnDownBdd, "2, 2");

		btnUpBdd = new JButton(
				"Transf\u00E9rer la base de donn\u00E9es");
		btnUpBdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				transfererBDD();
			}
		});
		contentPane.add(btnUpBdd, "4, 2");

		lblLog = new JLabel("");
		contentPane.add(lblLog, "2, 4, 3, 1");
		
		/*Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				telechargerBDD();
			}
		}, 0, 500000);*/
	}

	public void telechargerBDD() 
	{
		lblLog.setText("Téléchargement de la bdd en cours...");
		btnDownBdd.setEnabled(false);
		btnUpBdd.setEnabled(false);
		
		try 
		{
			// Choix de la langue francaise
			Locale locale = Locale.getDefault();
			Date actuelle = new Date();
			
			// Definition du format utilise pour les dates
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			fct = new Fonctions();
			fct.telechargerFichierViaFtp("yakipe.com", "yakip263492", "M2F5YnPpoi", "/sqlitemanager/murder_bdd.sqlite", fct.repertoireUtilisateur("res", "murder_bdd.sqlite"));
			lblLog.setText("La BDD a ete mise a jours en local le " + dateFormat.format(actuelle));
		} 
		catch (Exception e1) 
		{
			lblLog.setText("Impossible de télécharger la base de données !");
			e1.printStackTrace();
		}
		
		btnDownBdd.setEnabled(true);
		btnUpBdd.setEnabled(true);
	}

	public void transfererBDD() 
	{
		lblLog.setText("Transfert de la bdd en cours...");
		btnDownBdd.setEnabled(false);
		btnUpBdd.setEnabled(false);
		
		try 
		{
			// Choix de la langue francaise
			Locale locale = Locale.getDefault();
			Date actuelle = new Date();
			
			// Definition du format utilise pour les dates
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			fct = new Fonctions();
			fct.envoyerFichierViaFtp("yakipe.com", "yakip263492", "M2F5YnPpoi", "/sqlitemanager/murder_bdd.sqlite", fct.repertoireUtilisateur("res", "murder_bdd.sqlite"));
			lblLog.setText("La BDD a ete mise a jours a distance le " + dateFormat.format(actuelle));
		} 
		catch (Exception e2) 
		{
			System.err.println("Impossible d'envoyer la base de données !");
			e2.printStackTrace();
		}
		
		btnDownBdd.setEnabled(true);
		btnUpBdd.setEnabled(true);
	}
}
