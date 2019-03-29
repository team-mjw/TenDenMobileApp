package com.teamMJW.tenden;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class BulbConnection implements Runnable {

    //String of the HTTP Request to Light Bulb
    final private String initialRequestString = "M-SEARCH * HTTP/1.1\r\n" + "HOST:239.255.255.250:1982\r\n" + "MAN:\"ssdp:discover\"\r\n" + "ST:wifi_bulb\r\n";

    private InetAddress finalIpAddress;

    private String bulbId;

    private String currentPowerStatus;

    private String currentBrightness;

    private String currentColorTemperature;

    private String powerButtonStatus;

    //constructor --> might need to add additional parameters
    public BulbConnection(String powerButtonStatus) {
        this.powerButtonStatus = powerButtonStatus;
    }

    //function that will be called when BulbConnection object is called
    @Override
    public void run() {
        bulbConnect();
    }


    //Initial UDP Connection with the light by grabing bulb information in HTTP response packet
    //The following information is provided in the response packet:
    //1) unique id, 2) ip address, 3) power status, 4) brightness, and 5) color temperature
    private void bulbConnect() {
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

            //turns the light on or off depending on the switch button status
            powerSwitchBulb(finalIpAddress);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error @ send");

        }


    }

    //Turns the light on or off depending on the current power switch button status for a specific bulb
    private void powerSwitchBulb(InetAddress location) {

        try {
            //Create a tcp socket for data transfer
            Socket tcpSocket = new Socket(location, 55443);

            //Transfer the Data using the socket to the light bulb
            DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream());

            //Turn on the bulb if switch button is in the "Power On" position and turn off if otherwise
            if (powerButtonStatus.equals("Power On")) {
                out.writeBytes("{\"id\": 1, \"method\": \"set_power\", \"params\":[\"on\", \"smooth\", 500]}\r\n");
            } else {
                out.writeBytes("{\"id\": 1, \"method\": \"set_power\", \"params\":[\"off\", \"smooth\", 500]}\r\n");
            }

            tcpSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error @ lightbulb function");
        }

    }

    //There's probably a better name than changeBulb
    public void changeBulb(int brightness, int temperature) {
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

            //Create a tcp socket for data transfer
            Socket tcpSocket = new Socket(finalIpAddress, 55443);

            //Transfer the Data using the socket to the light bulb
            DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream());

            //Turn on the bulb if switch button is in the "Power On" position and turn off if otherwise
                out.writeBytes("{\"id\":1,\"method\":\"adjust_bright\",\"params\":[" + brightness + ", 500]}\r\n");
                out.writeBytes("{\"id\":1,\"method\":\"set_ct_abx\",\"params\":[" + temperature + ", \"smooth\", 500]}\r\n");

            tcpSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error @ changeBulb function");
        }
    }
}