import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;


public class Menu extends JMenu {

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenu menuItem;
	
	public JMenuBar createMenu(){
		menuBar = new JMenuBar();
		
		menu = new JMenu("swag");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menu);
		return menuBar;
		
	}
}
