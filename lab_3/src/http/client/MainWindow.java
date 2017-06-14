package http.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainWindow extends JFrame {
	private JTextArea txt;
	private String url;
	private int port;
	private JTextArea getTxt;
	private JTextArea attrTxt;

	public MainWindow(String url, int port) {
		this.url = url;
		this.port = port;
		this.setSize(700, 400);
		this.setLocation(350, 200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("HTTP Client 1.0");
		this.setLayout(null);
		txt = new JTextArea();
		txt.setEditable(false);
		JScrollPane scroll = new JScrollPane(txt);
		scroll.setBounds(20, 15, 400, 330);
		this.add(scroll);
		getTxt = new JTextArea();
		JScrollPane scr = new JScrollPane(getTxt);
		scr.setBounds(470, 40, 170, 35);
		this.add(scr);
		JLabel getLabel = new JLabel("Requesting page");
		getLabel.setBounds(470, 10, 170, 35);
		this.add(getLabel);
		JLabel attLabel = new JLabel("POST attributes");
		attLabel.setBounds(470, 75, 170, 35);
		this.add(attLabel);
		attrTxt = new JTextArea();
		JScrollPane scrl = new JScrollPane(attrTxt);
		scrl.setBounds(470, 100, 170, 35);
		this.add(scrl);
		addControlArea();
		this.setResizable(false);
		this.setVisible(true);
	}

	private void addControlArea() {
		JButton b1 = new JButton("GET");
		b1.setBounds(510, 155, 100, 30);
		this.add(b1);
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txt.setText(txt.getText() + "\nConnecting to " + url + "..");
				try {
					Socket socket = new Socket(url, port);
					String getStr = getTxt.getText();
					getTxt.setText("");
					String request = "GET /" + getStr.replaceAll(" ", "")
							+ " HTTP/1.0\n\n";
					txt.setText(txt.getText() + "\nSend request: \n" + request);
					String result = send(socket, request);
					txt.setText(txt.getText() + "\nGet answer: \n" + result);
					socket.close();
				} catch (IOException ex) {
					txt.setText(txt.getText() + "\nFailed to connect " + url);
				}
			}
		});
		JButton b2 = new JButton("POST");
		b2.setBounds(510, 200, 100, 30);
		this.add(b2);
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txt.setText(txt.getText() + "\nConnecting to " + url + "..");
				try {
					Socket socket = new Socket(url, port);
					String getStr = getTxt.getText();
					String attrStr = attrTxt.getText();
					getTxt.setText("");
					attrTxt.setText("");
					String request = "POST /" + getStr.replaceAll(" ", "")
							+ " HTTP/1.0\n" + "Content-Length: "
							+ attrStr.replaceAll(" ", "").length() + "\n\n"
							+ attrStr.replaceAll(" ", "") + "\n\n";
					txt.setText(txt.getText() + "\nSend request: \n" + request);
					String result = send(socket, request);
					txt.setText(txt.getText() + "\nGet answer: \n" + result);
					socket.close();
				} catch (IOException ex) {
					txt.setText(txt.getText() + "\nFailed to connect " + url);
				}
			}
		});
		JButton b3 = new JButton("HEAD");
		b3.setBounds(510, 245, 100, 30);
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txt.setText(txt.getText() + "\nConnecting to " + url + "..");
				try {
					Socket socket = new Socket(url, port);
					String getStr = getTxt.getText();
					getTxt.setText("");
					String request = "HEAD /" + getStr.replaceAll(" ", "")
							+ " HTTP/1.0\n\n";
					txt.setText(txt.getText() + "\nSend request: \n" + request);
					String result = send(socket, request);
					txt.setText(txt.getText() + "\nGet answer: \n" + result);
					socket.close();
				} catch (IOException ex) {
					txt.setText(txt.getText() + "\nFailed to connect " + url);
				}
			}
		});
		this.add(b3);
		JButton b4 = new JButton("Clear log");
		b4.setBounds(510, 315, 100, 30);
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt.setText("");
			}
		});
		this.add(b4);
	}

	private String send(Socket socket, String writeTo) throws IOException {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
			bufferedWriter.write(writeTo);
			bufferedWriter.flush();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				sb.append(str + "\n");
			}
			bufferedReader.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}