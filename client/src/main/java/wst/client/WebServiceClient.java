package wst.client;

import wst.generated.Shop;
import wst.generated.ShopService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class WebServiceClient {
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static ShopService shopService;

    public static void main(String[] args) throws MalformedURLException {
        shopService = new ShopService(new URL("http://localhost:8080/app/ShopService?wsdl"));

        while (true) {
            System.out.println("Enter command (1 - find/2 - update/3 - delete/4 - insert):");

            int command = 0;
            try {
                command = Integer.parseInt(in.readLine());
            } catch (IOException e) {
                continue;
            }
            
            switch (command) {
                case 1:
                    read();
                    break;
                case 2:
                    update();
                    break;
                case 3:
                    delete();
                    break;
                case 4:
                    create();
                    break;
                default:
                    break;
            }
        }
    }

    public static void read() {
        String name = getColumn("Name: ");
        String city = getColumn("City: ");
        String address = getColumn("Address: ");
        Boolean isActive = getBooaleanColumn("Active (y/n): ");
        String type = getColumn("Type: ");

        List<Shop> shops = shopService.getShopWebServicePort().read(
                name, city, address, isActive, type);

        if (shops.isEmpty()) {
            System.out.println("Did not found any shops");
        } else {
            for (Shop shop : shops) {
                System.out.println(
                        "Shop{" +
                                "id='" +shop.getId()+'\''+
                                ", name='" + shop.getName() + '\'' +
                                ", isActive=" + shop.isIsActive() +
                                ", city='" + shop.getCity() + '\'' +
                                ", address='" + shop.getAddress() + '\'' +
                                ", type='" + shop.getType() + '\'' +
                                '}');
            }

            System.out.println("Total shops: " + shops.size());
        }
    }

    public static void update() {
        int id = getIntColumn("ID: ");
        String name = getColumn("Name: ");
        String city = getColumn("City: ");
        String address = getColumn("Address: ");
        Boolean isActive = getBooaleanColumn("Active (y/n): ");
        String type = getColumn("Type: ");

        try {
            int status = shopService.getShopWebServicePort().update(id, name, city, address, isActive, type);
            System.out.println(status);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delete() {
        int id = getIntColumn("ID: ");

        try {
            int status = shopService.getShopWebServicePort().delete(id);
            System.out.println(status);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void create() {
        String name = getColumn("Name: ");
        String city = getColumn("City: ");
        String address = getColumn("Address: ");
        Boolean isActive = getBooaleanColumn("Active (y/n): ");
        String type = getColumn("Type: ");

        try {
            long id = shopService.getShopWebServicePort().create(name, city, address, isActive, type);
            System.out.println(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String checkNull (String s) {
        return s.length() == 0 ? null : s;
    }

    public static Boolean checkBoolean(String s) {
        if (s.length() == 0) return null;

        return s.equals("y") ? Boolean.TRUE : Boolean.FALSE;
    }

    public static String getColumn(String msg) {
        System.out.print(msg);
        try {
            return checkNull(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean getBooaleanColumn(String msg) {
        System.out.print(msg);
        try {
            return checkBoolean(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getIntColumn(String msg) {
        System.out.print(msg);
        try {
            return Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
};