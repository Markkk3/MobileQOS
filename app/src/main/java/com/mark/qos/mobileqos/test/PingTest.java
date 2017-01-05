package com.mark.qos.mobileqos.test;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;


/**
 * Created by tushkevich_m on 17.11.2016.
 */

public class PingTest {

    final String LOG_TAG = "myLogss";
    private InetAddress serv_addr;
    private String server_ip = "93.85.84.234";
    int portServer;
    int portMy;
    private long currenttime;
    private long receivetime;
    private static final int MAX_UDP_DATAGRAM_LEN = 1500;
    ArrayList<Integer> arrayPing =  new ArrayList();


    public PingTest() {
    }

    public void startTestPing() {

        arrayPing.clear();
        try {
            serv_addr = InetAddress.getByName(server_ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Random r = new Random();
      //  portServer = 5002 + r.nextInt(4);
        portServer = 52002;
        Log.d("UDP", "Отправляем на порт " + portServer);
        String mess = "testping";
        new sendmessage(serv_addr, portServer, mess, true).execute();

    }

    public class sendmessage extends AsyncTask<String, Integer, String> {
        int port;
        InetAddress serv_addr;
        byte[] buf;
        boolean ismess;
        String messageText;

        public sendmessage(InetAddress serv_addr, int port, String mess, boolean ismess) {
            this.port = port;
            this.serv_addr = serv_addr;
            this.messageText = mess;
            this.ismess = ismess;
        }



        protected String doInBackground(String... urls) {
            DatagramSocket sock;
            //String g = buf + getLocalIpAddress();
            boolean bKeepRunning= true;
            int count = 0;
            int packetloss=0;
         //   String message = "";
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //	buf = g.getBytes();
            try {

                sock = new DatagramSocket();
                Log.d("UDP", "локальный порт: " + sock.getLocalPort());
                //
                portMy = sock.getLocalPort();
                //port = sock.getLocalPort();
                byte[] buf = messageText.getBytes();
                Log.d("UDP", "Сообщение1  " + buf.toString());
                Log.d("UDP", "Сообщение2  " + new String(buf, 0, buf.length));
                DatagramPacket pack = new DatagramPacket(buf, buf.length, serv_addr, port);
                currenttime = System.currentTimeMillis();
                Log.d("UDP", "время до отправки " + System.currentTimeMillis());
                sock.send(pack);
                Log.d("UDP", "время после отправки  " + System.currentTimeMillis());
                // currenttime = System.currentTimeMillis();

             /*   if(myDatagramReceiver == null) {
                    myDatagramReceiver = new MyDatagramReceiver();
                    myDatagramReceiver.start();
                }
                */
                byte[] lmessage = new byte[MAX_UDP_DATAGRAM_LEN];
                DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
                while (bKeepRunning && count < 20) {
                    sock.receive(packet);
                    Log.d("UDP", "время получения " + System.currentTimeMillis());
                    String message = new String(lmessage, 0, packet.getLength());
                    Log.d(LOG_TAG, "mess получили = " + message);
                    //  ping();
                    count++;
                    publishProgress(ping(), count);
                    // DatagramPacket pack = new DatagramPacket(buf, buf.length, serv_addr, port);
                    Log.d(LOG_TAG, "спим = ");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!message.equals("")) {
                        Log.d(LOG_TAG, "оптравляем = ");
                        currenttime = System.currentTimeMillis();
                        sock.send(pack);
                    }
                    /*
					if(message.equals("true")) {
						Log.d(LOG_TAG, "true");
						try {
							ping();
						} catch (Exception e) {
							mess.append("Error: " + e);
							Log.d(LOG_TAG, "не получилось");
						}
					}
					else {

					}
					*/
                    // lastMessage = message;
                //    runOnUiThread(updateTextMessage);
                }

/*
                while (bKeepRunning && count < 20) {
                   // sock.setSoTimeout(2000);
                    try {
                        sock.receive(packet);
                        message = new String(lmessage, 0, packet.getLength());
                        arrayPing.add(ping());
                        Log.d(LOG_TAG, "mess получили = " + message);
                    } catch (SocketTimeoutException e) {
                        message = "";
                        Log.d(LOG_TAG, "timeout = ");
                        packetloss++;
                        arrayPing.add(-1);
                        if(packetloss>4){
                            Log.d(LOG_TAG, "ошибка");
                            break;
                        }
                        sock.send(pack);

                    }

                    //  ping();
                    count++;
                    publishProgress(ping(), count);
                    // DatagramPacket pack = new DatagramPacket(buf, buf.length, serv_addr, port);
                    Log.d(LOG_TAG, "спим = ");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!"".equals(message)) {
                        Log.d(LOG_TAG, "оптравляем = ");
                        currenttime = System.currentTimeMillis();
                        sock.send(pack);
                    }
                    /*
					if(message.equals("true")) {
						Log.d(LOG_TAG, "true");
						try {
							ping();
						} catch (Exception e) {
							mess.append("Error: " + e);
							Log.d(LOG_TAG, "не получилось");
						}
					}
					else {

					}
					*/
                    // lastMessage = message;
                 //   runOnUiThread(updateTextMessage);
             //   }

	    	/*
	    	while(true) {
				Log.d(LOG_TAG, "пытаемся получить" + port);
				DatagramPacket packet= new DatagramPacket(buf, buf.length);
				sock.receive(packet);
				Log.d(LOG_TAG, "result " + packet.getData() + " == "  + packet.getData().length);
	    	}
	    	*/
                sock.close();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "s";

        }




        protected void onProgressUpdate(Integer... values) {
     //       editTextLog.append("i = " + values[1] + " ping = " + values[0] + "\n");

        }

        protected void onPostExecute(String s) {
            if (ismess) {
           //     editTextLog.append("End " + "\n");
                //editTextLog.append(message.getText().toString() + "\n");
            }

        }
    }

    private Runnable updateTextMessage = new Runnable() {
        public void run() {

          //  editTextLog.append("He: ");
          //  editTextLog.append(myDatagramReceiver.getLastMessage() + "\n");
/*
			try {
				String mess = "true";

				serv_addr= InetAddress.getByName(server_ip.getText().toString());
				int port= Integer.parseInt(server_port.getText().toString());
				new sendmessage(serv_addr, port, mess, false).execute();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
*/
            //  textMessage.setText(myDatagramReceiver.getLastMessage());
        }
    };

    private int ping() {
        Log.d(LOG_TAG, "set ping");
        receivetime = System.currentTimeMillis();
        Log.d(LOG_TAG, "время полученя основное" +  receivetime);
        int  ping = (int) (receivetime - currenttime);
        Log.d(LOG_TAG, "ping = " + ping);
        return ping;
    }


    public String getDeviceIPAddress() {
        Log.d(LOG_TAG, "getDeviceIPAddress ");
        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                Log.d(LOG_TAG, "IP getDeviceIPAddress = " + i.getHostAddress() + i.getAddress());
                if (!i.isLoopbackAddress()) {
                    Log.d(LOG_TAG, "вот то что нужно= " + i.getHostAddress());
                    //!2 && !3
                    if (!i.isSiteLocalAddress() && !i.isLinkLocalAddress()) {
                        Log.d(LOG_TAG, "вот самый правильный= " + i.getHostAddress());
                        return i.getHostAddress().toString();
                    } else {
                        if (!i.isAnyLocalAddress() && !i.isLinkLocalAddress()) {
                            Log.d(LOG_TAG, "else правильный= " + i.getHostAddress());
                            return i.getHostAddress().toString();
                        }
                    }
                }
                if (!i.isAnyLocalAddress()) {
                    Log.d(LOG_TAG, "вот 1= " + i.getHostAddress());
                }
                if (!i.isLinkLocalAddress()) {
                    Log.d(LOG_TAG, "вот 2= " + i.getHostAddress());
                }
                if (!i.isSiteLocalAddress()) {
                    Log.d(LOG_TAG, "вот 3= " + i.getHostAddress());
                }
                if (!i.isMulticastAddress()) {
                    Log.d(LOG_TAG, "вот 4= " + i.getHostAddress());
                }


                //System.out.println(i.getHostAddress());
            }
        }
        return "не определено";
    }
}
