package newElevator;

import javax.swing.JFrame;

public class MAIN {
	//主线程，让程序跑起来
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame=new View();                             
		frame.setSize(200, 300);
		frame.setTitle("电梯调度系统");
		frame.show();
	}

}
