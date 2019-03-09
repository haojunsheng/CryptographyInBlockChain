import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

public class MD5Listener implements ActionListener {
	private JTextField inputcontent;
	private JTextField outputcontent;

	public MD5Listener(JTextField f1, JTextField f2) {
		inputcontent = f1;
		outputcontent = f2;
	}

	public void actionPerformed(ActionEvent e) {
		String str = MD5.getInstance().getMD5(inputcontent.getText());
		outputcontent.setText(str);
		System.out.println(str);
	}
}