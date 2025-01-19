package com.mycompany.iprange;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
public class IPrange {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the CIDR (e.g., network_ip/subnet_mask): ");
        String cidr = input.nextLine();
        saveIPsToFile(cidr);
        input.close();
    }
    
     public static void saveIPsToFile(String cidr) {
        try {
            String[] parts = cidr.split("/");
            String ipAddress = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            byte[] ipBytes = inetAddress.getAddress();
            int ip = 0;
            for (int i = 0; i < ipBytes.length; i++) {
                ip |= (ipBytes[i] & 0xFF) << (24 - (8 * i));
            }
            int subnetMask = (0xFFFFFFFF << (32 - prefixLength)) & 0xFFFFFFFF;
            int networkAddress = ip & subnetMask;
            int numIPs = (int) Math.pow(2, (32 - prefixLength));
            StringBuilder ipList = new StringBuilder();
            for (int i = 0; i < numIPs; i++) {
                int currentIp = networkAddress + i;
                ipList.append(intToIp(currentIp));
                if (i < numIPs - 1) {
                    ipList.append("; ");
                }
            }
            saveToFile(ipList.toString());
        } catch (UnknownHostException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile(String content) {
        try {
            String userHome = System.getProperty("user.home");
            String desktopPath = userHome + "/Desktop/IPrange.txt";
            File file = new File(desktopPath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            }
            System.out.println("IP range saved to: " + desktopPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String intToIp(int ip) {
        return ((ip >> 24) & 0xFF) + "." +
               ((ip >> 16) & 0xFF) + "." +
               ((ip >> 8) & 0xFF) + "." +
               (ip & 0xFF);
    }
}
