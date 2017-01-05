package com.mark.qos.mobileqos.test;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.mark.qos.mobileqos.fragments.FragmentTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by tushkevich_m on 17.11.2016.
 */

public class PingAsyncTask extends AsyncTask<String, Integer, ArrayList> {



    int port;
    byte[] buf;
    boolean ismess;
    String messageText;
    final String LOG_TAG = "myLogss";
    private InetAddress serv_addr;
    private String server_ip = "93.85.84.234";
    int portServer;
    int portMy;
    private long currenttime;
    private long receivetime;
    private static final int MAX_UDP_DATAGRAM_LEN = 1500;
    ArrayList<Integer> arrayPing =  new ArrayList();
    FragmentTest fragmentTest;


    public PingAsyncTask(InetAddress serv_addr, int port, String mess, boolean ismess) {
        this.port = port;
        this.serv_addr = serv_addr;
        this.messageText = mess;
        this.ismess = ismess;
    //    arrayPing = arrayListPing;

      //  this.fragmentTest = fragmentTest;
    }


    protected ArrayList<Integer> doInBackground(String... urls) {
        DatagramSocket sock;
        //String g = buf + getLocalIpAddress();
        boolean bKeepRunning= true;
        int count = 0;
        int packetloss=0;
           String message = "";
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
         //   Log.d("UDP", "Сообщение2  " + new String(buf, 0, buf.length));
            DatagramPacket pack = new DatagramPacket(buf, buf.length, serv_addr, port);
            currenttime = System.currentTimeMillis();
            Log.d("UDP", "время до отправки  " + currenttime);
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
                     sock.setSoTimeout(2000);
                    try {
                        sock.receive(packet);
                      //  Log.d("UDP", "время получения;  " + count + "; " + System.currentTimeMillis() + "; " + currenttime);
                        Log.d("UDP", "" + count + "; " + System.currentTimeMillis() + "; " + currenttime + ";");
                        message = new String(lmessage, 0, packet.getLength());
                        arrayPing.add(ping());
                      //  Log.d(LOG_TAG, "mess получили = " + message);
                    } catch (SocketTimeoutException e) {
                        message = "";
                        Log.d(LOG_TAG, "timeout = ");
                        packetloss++;
                        arrayPing.add(-1);
                        if (packetloss > 4) {
                            Log.d(LOG_TAG, "ошибка");
                            break;
                        }
                        sock.send(pack);

                    }

                    //  ping();
                    count++;
                  //  publishProgress(ping(), count);
                    // DatagramPacket pack = new DatagramPacket(buf, buf.length, serv_addr, port);
                  //  Log.d(LOG_TAG, "спим = ");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!"".equals(message)) {
                     //   Log.d(LOG_TAG, "оптравляем = ");
                       // Log.d("UDP", "время до отправки  " + System.currentTimeMillis());
                        sock.send(pack);
                        currenttime = System.currentTimeMillis();
                      //  Log.d("UDP", "время после отправки  " + currenttime);
                    }
                }

                  /*
            while (bKeepRunning && count < 20) {
                Log.d(LOG_TAG, "слушаем = ");
                sock.receive(packet);
                String message = new String(lmessage, 0, packet.getLength());
                Log.d(LOG_TAG, "mess получили = " + message);
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
            //   }


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

        return arrayPing;

    }


    private int ping() {
     //   Log.d(LOG_TAG, "set ping");
        int  ping = (int) (System.currentTimeMillis() - currenttime);
      //  Log.d(LOG_TAG, "ping = " + ping);
        return ping;
    }

    protected void onProgressUpdate(Integer... values) {
        //       editTextLog.append("i = " + values[1] + " ping = " + values[0] + "\n");

    }

    protected void onPostExecute(ArrayList s) {

            Log.d(LOG_TAG, "size= " + s.size());
            //     editTextLog.append("End " + "\n");
            //editTextLog.append(message.getText().toString() + "\n");
        //    fragmentTest.setPingResult(35);


    }


}
