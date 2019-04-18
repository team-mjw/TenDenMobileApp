package com.teamMJW.tenden;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class BulbAlert implements Runnable {

    //String of the HTTP Request to Light Bulb
    final private String initialRequestString = "M-SEARCH * HTTP/1.1\r\n" + "HOST:239.255.255.250:1982\r\n" + "MAN:\"ssdp:discover\"\r\n" + "ST:wifi_bulb\r\n";

    private InetAddress finalIpAddress;

    private String bulbId;

    private String currentPowerStatus;

    private String currentBrightness;

    private String currentColorTemperature;

    //function that will be called when BulbAlert object is called
    @Override
    public void run() {
        setupAlertCycle();
    }


    //UDP Connection with the light bulb and run 'flicker' when there is weather alert in the user input zipcode
    private void setupAlertCycle() {
        try {
            System.out.println("*****Searching for Device*****\n");

            //Convert HTTP request to byte array
            byte[] httpRequest = initialRequestString.getBytes();
            int httpRequestLength = initialRequestString.length();

            //Create a Socket for data transfer of HTTP Request and Response
            DatagramSocket socket = new DatagramSocket();

            //Change the IP address from a String to a InetAddress
            InetAddress address = InetAddress.getByName("239.255.255.250");
            //Port Number of HOST (239.255.255.250)
            int portNumber = 1982;

            //Container for the HTTP Response
            byte[] receiveData = new byte[2048];

            //Prepare the HTTP Request packet
            DatagramPacket sendPacket = new DatagramPacket(httpRequest, httpRequestLength, address, portNumber);

            //Send the HTTP request packet to the socket
            socket.send(sendPacket);

            //Setup the packet for the Response HTTP packet
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            //Store the data of the HTTP Response in the Response HTTP packet
            socket.receive(receivePacket);

            //Convert the Data in the Response Packet to String form for ip address extraction
            String responseString = new String(receivePacket.getData());

            //temporary variables to extract various information of the light bulb
            String ipAddressString = responseString;
            String idString = responseString;
            String powerStatusString = responseString;
            String brightnessString = responseString;
            String colorTemperatureString = responseString;

            //Close the socket connection
            socket.close();

            //Obtain the IP address from HTTP Response Packet
            ipAddressString = ipAddressString.substring(ipAddressString.lastIndexOf("//") + 2);
            ipAddressString = ipAddressString.substring(0, ipAddressString.indexOf("\n"));
            ipAddressString = ipAddressString.substring(0, ipAddressString.indexOf(":"));

            //Extract unique bulb id
            idString = idString.substring(idString.indexOf("id:"));
            idString = idString.substring(0, idString.indexOf("\n"));
            bulbId = idString.substring(idString.indexOf(": ") + 2);

            //Extract current power status
            powerStatusString = powerStatusString.substring(powerStatusString.indexOf("power:"));
            powerStatusString = powerStatusString.substring(0, powerStatusString.indexOf("\n"));
            currentPowerStatus = powerStatusString.substring(powerStatusString.indexOf(": ") + 2);

            //Extract current brightness value
            brightnessString = brightnessString.substring(brightnessString.indexOf("bright:"));
            brightnessString = brightnessString.substring(0, brightnessString.indexOf("\n"));
            currentBrightness = brightnessString.substring(brightnessString.indexOf(": ") + 2);

            //Extract current color temperature
            colorTemperatureString = colorTemperatureString.substring(colorTemperatureString.indexOf("ct:"));
            colorTemperatureString = colorTemperatureString.substring(0, colorTemperatureString.indexOf("\n"));
            currentColorTemperature = colorTemperatureString.substring(colorTemperatureString.indexOf(": ") + 2);

            //Convert string ip address to InetAddress form
            finalIpAddress = InetAddress.getByName(ipAddressString);

            System.out.println("*****Device Discovered*****\n");

            //Run weather alert feature
            alertCronJobs(finalIpAddress);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error @ send");

        }


    }

    //Flicker the light (currently only 5 times for testing purposes) if weather alerts exist
    private void alertCronJobs(InetAddress location) {

        try {
            //Create a tcp socket for data transfer
            Socket tcpSocket = new Socket(location, 55443);

            //Transfer the Data using the socket to the light bulb
            DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream());

            int count = 0;
            while(count < 5) {

                Thread.sleep(10000);
                out.writeBytes("{\"id\":1,\"method\":\"start_cf\",\"params\":[3, 0, \"750, 2, 6500, 100, 750, 2, 2700, 100, 750, 2, 4000, 100\"]}\r\n");

                count++;
            }

            tcpSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error @ lightbulb function");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}