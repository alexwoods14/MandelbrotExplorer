import javax.swing.JFrame;

public class MyWindow extends JFrame {

	public MyWindow() {

		setTitle("Mandlebrot Set");
		setSize(1920, 1080);
		setLocationRelativeTo(null);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new DrawImage());


	}
}

